package Si3.divertech.qr_reader.java;

import static java.lang.Math.max;
import static java.lang.Math.min;

import Si3.divertech.qr_reader.BitmapUtils;
import Si3.divertech.qr_reader.CameraImageGraphic;
import Si3.divertech.qr_reader.FrameMetadata;
import Si3.divertech.qr_reader.GraphicOverlay;
import Si3.divertech.qr_reader.InferenceInfoGraphic;
import Si3.divertech.qr_reader.ScopedExecutor;
import Si3.divertech.qr_reader.VisionImageProcessor;
import Si3.divertech.qr_reader.preference.PreferenceUtils;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.gms.tasks.Tasks;
import com.google.android.odml.image.ByteBufferMlImageBuilder;
import com.google.android.odml.image.MlImage;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.common.InputImage;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Abstract base class for vision frame processors. Subclasses need to implement {@link
 * #onSuccess(Object, GraphicOverlay)} to define what they want to with the detection results and
 * {@link #detectInImage(InputImage)} to specify the detector object.
 *
 * @param <T> The type of the detected feature.
 */
public abstract class VisionProcessorBase<T> implements VisionImageProcessor {
    // Enable fps display
    private final boolean fpsLog = false;

    private final ActivityManager activityManager;
    private final Timer fpsTimer = new Timer();
    private final ScopedExecutor executor;

    // Whether this processor is already shut down
    private boolean isShutdown;

    // Used to calculate latency, running in the same thread, no sync needed.
    private int numRuns = 0;
    private long maxFrameMs = 0;
    private long minFrameMs = Long.MAX_VALUE;
    private long maxDetectorMs = 0;
    private long minDetectorMs = Long.MAX_VALUE;

    // Frame count that have been processed so far in an one second interval to calculate FPS.
    private int frameProcessedInOneSecondInterval = 0;
    private int framesPerSecond = 0;

    // To keep the latest images and its metadata.
    @GuardedBy("this")
    private ByteBuffer latestImage;

    @GuardedBy("this")
    private FrameMetadata latestImageMetaData;
    // To keep the images and metadata in process.
    @GuardedBy("this")
    private ByteBuffer processingImage;

    @GuardedBy("this")
    private FrameMetadata processingMetaData;

    protected VisionProcessorBase(Context context) {
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        executor = new ScopedExecutor(TaskExecutors.MAIN_THREAD);
        fpsTimer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        framesPerSecond = frameProcessedInOneSecondInterval;
                        frameProcessedInOneSecondInterval = 0;
                    }
                },
                /* delay= */ 0,
                /* period= */ 1000);
    }

    // -----------------Code for processing live preview frame from Camera1 API-----------------------
    @Override
    public synchronized void processByteBuffer(
            ByteBuffer data, final FrameMetadata frameMetadata, final GraphicOverlay graphicOverlay) {
        latestImage = data;
        latestImageMetaData = frameMetadata;
        if (processingImage == null && processingMetaData == null) {
            processLatestImage(graphicOverlay);
        }
    }

    private synchronized void processLatestImage(final GraphicOverlay graphicOverlay) {
        processingImage = latestImage;
        processingMetaData = latestImageMetaData;
        latestImage = null;
        latestImageMetaData = null;
        if (processingImage != null && processingMetaData != null && !isShutdown) {
            processImage(processingImage, processingMetaData, graphicOverlay);
        }
    }

    private void processImage(
            ByteBuffer data, final FrameMetadata frameMetadata, final GraphicOverlay graphicOverlay) {
        long frameStartMs = SystemClock.elapsedRealtime();

        // If live viewport is on (that is the underneath surface view takes care of the camera preview
        // drawing), skip the unnecessary bitmap creation that used for the manual preview drawing.
        Bitmap bitmap =
                PreferenceUtils.isCameraLiveViewportEnabled(graphicOverlay.getContext())
                        ? null
                        : BitmapUtils.getBitmap(data, frameMetadata);

        if (isMlImageEnabled()) {
            MlImage mlImage =
                    new ByteBufferMlImageBuilder(
                            data,
                            frameMetadata.getWidth(),
                            frameMetadata.getHeight(),
                            MlImage.IMAGE_FORMAT_NV21)
                            .setRotation(frameMetadata.getRotation())
                            .build();

            requestDetectInImage(graphicOverlay, bitmap, frameStartMs)
                    .addOnSuccessListener(executor, results -> processLatestImage(graphicOverlay));

            // This is optional. Java Garbage collection can also close it eventually.
            mlImage.close();
            return;
        }

        requestDetectInImage(
                InputImage.fromByteBuffer(
                        data,
                        frameMetadata.getWidth(),
                        frameMetadata.getHeight(),
                        frameMetadata.getRotation(),
                        InputImage.IMAGE_FORMAT_NV21),
                graphicOverlay,
                bitmap,
                frameStartMs)
                .addOnSuccessListener(executor, results -> processLatestImage(graphicOverlay));
    }

    // -----------------Code for processing live preview frame from CameraX API-----------------------
    @Override
    @ExperimentalGetImage
    public void processImageProxy(ImageProxy image, GraphicOverlay graphicOverlay) {
        long frameStartMs = SystemClock.elapsedRealtime();
        if (isShutdown) {
            image.close();
            return;
        }

        Bitmap bitmap = null;
        if (!PreferenceUtils.isCameraLiveViewportEnabled(graphicOverlay.getContext())) {
            bitmap = BitmapUtils.getBitmap(image);
        }

        if (isMlImageEnabled()) {
            requestDetectInImage(
                    graphicOverlay,
                    /* originalCameraImage= */ bitmap,
                    frameStartMs)
                    // When the image is from CameraX analysis use case, must call image.close() on received
                    // images when finished using them. Otherwise, new images may not be received or the
                    // camera may stall.
                    // Currently MlImage doesn't support ImageProxy directly, so we still need to call
                    // ImageProxy.close() here.
                    .addOnCompleteListener(results -> image.close());
            return;
        }

        requestDetectInImage(
                InputImage.fromMediaImage(Objects.requireNonNull(image.getImage()), image.getImageInfo().getRotationDegrees()),
                graphicOverlay,
                /* originalCameraImage= */ bitmap,
                frameStartMs)
                // When the image is from CameraX analysis use case, must call image.close() on received
                // images when finished using them. Otherwise, new images may not be received or the camera
                // may stall.
                .addOnCompleteListener(results -> image.close());
    }

    // -----------------Common processing logic-------------------------------------------------------
    private Task<T> requestDetectInImage(
            final InputImage image,
            final GraphicOverlay graphicOverlay,
            @Nullable final Bitmap originalCameraImage,
            long frameStartMs) {
        return setUpListener(
                detectInImage(image), graphicOverlay, originalCameraImage, frameStartMs);
    }

    private Task<T> requestDetectInImage(
            final GraphicOverlay graphicOverlay,
            @Nullable final Bitmap originalCameraImage,
            long frameStartMs) {
        return setUpListener(
                detectInImage(), graphicOverlay, originalCameraImage, frameStartMs);
    }

    private Task<T> setUpListener(
            Task<T> task,
            final GraphicOverlay graphicOverlay,
            @Nullable final Bitmap originalCameraImage,
            long frameStartMs) {
        final long detectorStartMs = SystemClock.elapsedRealtime();
        return task.addOnSuccessListener(
                        executor,
                        results -> {
                            long endMs = SystemClock.elapsedRealtime();
                            long currentFrameLatencyMs = endMs - frameStartMs;
                            long currentDetectorLatencyMs = endMs - detectorStartMs;
                            if (numRuns >= 500) {
                                resetLatencyStats();
                            }
                            numRuns++;
                            frameProcessedInOneSecondInterval++;
                            maxFrameMs = max(currentFrameLatencyMs, maxFrameMs);
                            minFrameMs = min(currentFrameLatencyMs, minFrameMs);
                            maxDetectorMs = max(currentDetectorLatencyMs, maxDetectorMs);
                            minDetectorMs = min(currentDetectorLatencyMs, minDetectorMs);

                            // Only log inference info once per second. When frameProcessedInOneSecondInterval is
                            // equal to 1, it means this is the first frame processed during the current second.
                            if (frameProcessedInOneSecondInterval == 1) {
                                ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                                activityManager.getMemoryInfo(mi);
                            }

                            graphicOverlay.clear();
                            if (originalCameraImage != null) {
                                graphicOverlay.add(new CameraImageGraphic(graphicOverlay, originalCameraImage));
                            }
                            VisionProcessorBase.this.onSuccess(results, graphicOverlay);
                            if (!PreferenceUtils.shouldHideDetectionInfo(graphicOverlay.getContext())) {
                                if(fpsLog){
                                    graphicOverlay.add(
                                            new InferenceInfoGraphic(
                                                    graphicOverlay,
                                                    currentFrameLatencyMs,
                                                    currentDetectorLatencyMs,
                                                    framesPerSecond));
                                }
                            }
                            graphicOverlay.postInvalidate();
                        })
                .addOnFailureListener(
                        executor,
                        e -> {
                            graphicOverlay.clear();
                            graphicOverlay.postInvalidate();
                            String error = "Failed to process. Error: " + e.getLocalizedMessage();
                            Toast.makeText(
                                            graphicOverlay.getContext(),
                                            error + "\nCause: " + e.getCause(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                            e.printStackTrace();
                            VisionProcessorBase.this.onFailure(e);
                        });
    }

    @Override
    public void stop() {
        executor.shutdown();
        isShutdown = true;
        resetLatencyStats();
        fpsTimer.cancel();
    }

    private void resetLatencyStats() {
        numRuns = 0;
        maxFrameMs = 0;
        minFrameMs = Long.MAX_VALUE;
        maxDetectorMs = 0;
        minDetectorMs = Long.MAX_VALUE;
    }

    protected abstract Task<T> detectInImage(InputImage image);

    protected Task<T> detectInImage() {
        return Tasks.forException(
                new MlKitException(
                        "MlImage is currently not demonstrated for this feature",
                        MlKitException.INVALID_ARGUMENT));
    }

    protected abstract void onSuccess(@NonNull T results, @NonNull GraphicOverlay graphicOverlay);

    protected abstract void onFailure(@NonNull Exception e);

    protected boolean isMlImageEnabled() {
        return false;
    }
}
