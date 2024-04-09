package Si3.divertech.qr_reader;

import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.view.ViewCompat;

public class InferenceInfoGraphic extends GraphicOverlay.Graphic {
    private static final float TEXT_SIZE = 60.0f;
    private final long detectorLatency;
    private final long frameLatency;
    private final Integer framesPerSecond;
    private final GraphicOverlay overlay;
    private boolean showLatencyInfo;
    private final Paint textPaint;

    public InferenceInfoGraphic(GraphicOverlay overlay, long frameLatency, long detectorLatency, Integer framesPerSecond) {
        super(overlay);
        this.showLatencyInfo = true;
        this.overlay = overlay;
        this.frameLatency = frameLatency;
        this.detectorLatency = detectorLatency;
        this.framesPerSecond = framesPerSecond;
        this.textPaint = new Paint();
        this.textPaint.setColor(-1);
        this.textPaint.setTextSize(TEXT_SIZE);
        this.textPaint.setShadowLayer(5.0f, 0.0f, 0.0f, ViewCompat.MEASURED_STATE_MASK);
        postInvalidate();
    }

    @Override
    public synchronized void draw(Canvas canvas) {
        canvas.drawText("InputImage size: " + this.overlay.getImageHeight() + "x" + this.overlay.getImageWidth(), 30.0f, 90.0f, this.textPaint);
        if (this.showLatencyInfo) {
            if (this.framesPerSecond != null) {
                canvas.drawText("FPS: " + this.framesPerSecond + ", Frame latency: " + this.frameLatency + " ms", 30.0f, TEXT_SIZE + 90.0f, this.textPaint);
            } else {
                canvas.drawText("Frame latency: " + this.frameLatency + " ms", 30.0f, TEXT_SIZE + 90.0f, this.textPaint);
            }
            canvas.drawText("Detector latency: " + this.detectorLatency + " ms", 30.0f, 120.0f + 90.0f, this.textPaint);
        }
    }
}
