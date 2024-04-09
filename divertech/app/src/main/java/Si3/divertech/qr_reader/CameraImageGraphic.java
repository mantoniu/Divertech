package Si3.divertech.qr_reader;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CameraImageGraphic extends GraphicOverlay.Graphic {
    private final Bitmap bitmap;

    public CameraImageGraphic(GraphicOverlay overlay, Bitmap bitmap) {
        super(overlay);
        this.bitmap = bitmap;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.bitmap, getTransformationMatrix(), null);
    }
}
