package Si3.divertech.qr_reader.barcodescanner;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

import Si3.divertech.qr_reader.CameraPreviewActivity;
import Si3.divertech.qr_reader.GraphicOverlay;
import Si3.divertech.qr_reader.QRDataListener;
import Si3.divertech.qr_reader.VisionProcessorBase;

public class BarcodeScannerProcessor extends VisionProcessorBase<List<Barcode>> {

    private static final String TAG = "BarcodeProcessor";
    private final QRDataListener qrDataListener;
    private final BarcodeScanner barcodeScanner;
    public BarcodeScannerProcessor(Context context, QRDataListener qrDataListener) {
        super(context);
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                        .build();
        barcodeScanner = BarcodeScanning.getClient(options);
        this.qrDataListener = qrDataListener;
    }

    @Override
    public void stop() {
        super.stop();
        barcodeScanner.close();
    }

    @Override
    protected Task<List<Barcode>> detectInImage(InputImage image) {
        return barcodeScanner.process(image);
    }

    @Override
    protected void onSuccess(
            @NonNull List<Barcode> barcodes, @NonNull GraphicOverlay graphicOverlay) {
        if (!CameraPreviewActivity.barcodeScanEnabled) {
            return;
        }

        if(!barcodes.isEmpty()){
            Barcode barcode = barcodes.get(0);
            graphicOverlay.add(new BarcodeGraphic(graphicOverlay, barcode));
        }

        if(!barcodes.isEmpty() && qrDataListener!=null){
            String eventId = barcodes.get(0).getDisplayValue();
            qrDataListener.onDataReceived(eventId);
        }
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Barcode detection failed " + e);
    }

}

