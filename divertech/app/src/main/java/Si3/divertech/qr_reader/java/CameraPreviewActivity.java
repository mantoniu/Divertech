package Si3.divertech.qr_reader.java;

import Si3.divertech.MainActivity;
import Si3.divertech.R;
import Si3.divertech.qr_reader.CameraXViewModel;
import Si3.divertech.qr_reader.GraphicOverlay;
import Si3.divertech.qr_reader.VisionImageProcessor;
import Si3.divertech.qr_reader.java.barcodescanner.BarcodeScannerProcessor;
import Si3.divertech.qr_reader.preference.PreferenceUtils;

import android.Manifest;
import android.content.Intent;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.camera2.interop.Camera2Interop;
import androidx.camera.camera2.interop.ExperimentalCamera2Interop;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.resolutionselector.AspectRatioStrategy;
import androidx.camera.core.resolutionselector.ResolutionFilter;
import androidx.camera.core.resolutionselector.ResolutionSelector;
import androidx.camera.core.resolutionselector.ResolutionStrategy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.common.MlKitException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class CameraPreviewActivity extends AppCompatActivity implements QRDataListener {
    private static final int CAMERA_PERMISSION_CODE = 100;
    @Nullable
    private Camera camera;
    private static final String TAG = "CameraXLivePreview";
    private ImageAnalysis analysisUseCase;
    private ProcessCameraProvider cameraProvider;
    private CameraSelector cameraSelector;
    private GraphicOverlay graphicOverlay;
    private VisionImageProcessor imageProcessor;
    private boolean needUpdateGraphicOverlayImageSourceInfo;
    private Preview previewUseCase;
    private PreviewView previewView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);

        cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        previewView = findViewById(R.id.preview_view);
        if (previewView == null) {
            Log.d(TAG, "previewView is null");
        }
        graphicOverlay = findViewById(R.id.graphic_overlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        new ViewModelProvider(this)
                .get(CameraXViewModel.class)
                .getProcessCameraProvider()
                .observe(
                        this,
                        provider -> {
                            cameraProvider = provider;
                            bindAllCameraUseCases();
                        });
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        bindAllCameraUseCases();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.imageProcessor != null) {
            this.imageProcessor.stop();
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) == -1) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            Log.d(TAG, "Permission already granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                Log.d(TAG, "Camera Permission Granted");
            } else {
                Log.d(TAG, "Camera Permission Denied");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.imageProcessor != null) {
            this.imageProcessor.stop();
        }
    }

    private void bindAllCameraUseCases() {
        if (this.cameraProvider != null) {
            this.cameraProvider.unbindAll();
            bindPreviewUseCase();
            bindAnalysisUseCase();
        }
    }

    private void bindPreviewUseCase() {
        if (!PreferenceUtils.isCameraLiveViewportEnabled(this) || this.cameraProvider == null) {
            return;
        }
        if (this.previewUseCase != null) {
            this.cameraProvider.unbind(this.previewUseCase);
        }
        Preview.Builder builder = new Preview.Builder();
        Size targetResolution = PreferenceUtils.getCameraXTargetResolution(this, 1);
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution);
        }
        this.previewUseCase = builder.build();
        this.previewUseCase.setSurfaceProvider(this.previewView.getSurfaceProvider());
        camera =
                cameraProvider.bindToLifecycle(this, cameraSelector, previewUseCase);
    }

    
    @OptIn(markerClass = ExperimentalCamera2Interop.class) private void bindAnalysisUseCase() {
        if (cameraProvider == null) {
            return;
        }
        if (analysisUseCase != null) {
            cameraProvider.unbind(analysisUseCase);
        }
        if (imageProcessor != null) {
            imageProcessor.stop();
        }

        try {
            Log.i(TAG, "Using Barcode Detector Processor");
            imageProcessor = new BarcodeScannerProcessor(this, this);
        } catch (Exception e) {
            Log.e(TAG, "Can not create image processor: ", e);
            Toast.makeText(
                            getApplicationContext(),
                            "Can not create image processor: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        Size targetResolution = PreferenceUtils.getCameraXTargetResolution(this, 1);
        ResolutionSelector.Builder resolutionSelector = new ResolutionSelector.Builder();

        if (targetResolution != null)
            resolutionSelector.setResolutionStrategy(new ResolutionStrategy(targetResolution, ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER));

        ImageAnalysis.Builder imageAnalysis = new ImageAnalysis.Builder()
                .setResolutionSelector(resolutionSelector.build())
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST);

        analysisUseCase = imageAnalysis.build();

        needUpdateGraphicOverlayImageSourceInfo = true;
        analysisUseCase.setAnalyzer(
                // imageProcessor.processImageProxy will use another thread to run the detection underneath,
                // thus we can just runs the analyzer itself on main thread.
                ContextCompat.getMainExecutor(this),
                imageProxy -> {
                    if (needUpdateGraphicOverlayImageSourceInfo) {
                        int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
                        if (rotationDegrees == 0 || rotationDegrees == 180) {
                            graphicOverlay.setImageSourceInfo(
                                    imageProxy.getWidth(), imageProxy.getHeight(), false);
                        } else {
                            graphicOverlay.setImageSourceInfo(
                                    imageProxy.getHeight(), imageProxy.getWidth(), false);
                        }
                        needUpdateGraphicOverlayImageSourceInfo = false;
                    }
                    try {
                        imageProcessor.processImageProxy(imageProxy, graphicOverlay);
                    } catch (MlKitException e) {
                        Log.e(TAG, "Failed to process image. Error: " + e.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        camera = cameraProvider.bindToLifecycle(/* lifecycleOwner= */ this, cameraSelector, analysisUseCase);
    }
    @Override
    public void onDataReceived(String qrData) {
        Log.d("RECEIVED_DATA", qrData);
        Intent receivedData = new Intent(this, MainActivity.class);
        receivedData.putExtra("qr_data", qrData);
        startActivity(receivedData);
    }
}
