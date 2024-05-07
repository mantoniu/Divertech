package Si3.divertech.qr_reader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.camera2.interop.ExperimentalCamera2Interop;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.resolutionselector.ResolutionSelector;
import androidx.camera.core.resolutionselector.ResolutionStrategy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.mlkit.common.MlKitException;

import Si3.divertech.DataBaseListener;
import Si3.divertech.DataBaseResponses;
import Si3.divertech.EventActivity;
import Si3.divertech.ListEvent;
import Si3.divertech.R;
import Si3.divertech.qr_reader.barcodescanner.BarcodeScannerProcessor;
import Si3.divertech.qr_reader.preference.PreferenceUtils;

public final class CameraPreviewActivity extends AppCompatActivity implements QRDataListener, DataBaseListener {
    private static final int CAMERA_PERMISSION_CODE = 100;
    private String eventId;
    @Nullable
    private Camera camera;
    private static final String TAG = "CameraPreview";
    private ImageAnalysis analysisUseCase;
    private ProcessCameraProvider cameraProvider;
    private CameraSelector cameraSelector;
    private GraphicOverlay graphicOverlay;
    private VisionImageProcessor imageProcessor;
    private boolean needUpdateGraphicOverlayImageSourceInfo;
    private Preview previewUseCase;
    private PreviewView previewView;
    private boolean flashLightState = false;
    private PopupWindow popupWindow;
    private PopupWindow errorPopup;
    public static boolean barcodeScanEnabled = true;

    @SuppressLint("ClickableViewAccessibility")
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

        ImageView flashLightButton = findViewById(R.id.flashlight);
        flashLightButton.setOnClickListener((click) -> flashLightClick((ImageView) click));
        Button manualCode = findViewById(R.id.manual_button);


        manualCode.setOnClickListener(this::onButtonShowPopupWindowClick);
        ImageView returnButton = findViewById(R.id.back);
        returnButton.setOnClickListener(click -> finish());


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

    private void showKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.postDelayed(() -> imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0), 200);
    }

    public void showEventErrorPopup(@StringRes int textRes) {
        if (errorPopup != null)
            return;

        errorPopup = createPopup(R.layout.event_not_found_popup);

        errorPopup.showAtLocation(getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);
        View popupView = errorPopup.getContentView();

        TextView text = popupView.findViewById(R.id.text_input);
        text.setText(textRes);

        View.OnClickListener listener = (click) -> {
            dismissPopup(errorPopup);
            errorPopup = null;
        };

        popupView.findViewById(R.id.close_button).setOnClickListener(listener);

        popupView.findViewById(R.id.validate_code).setOnClickListener(listener);
    }


    private PopupWindow createPopup(@LayoutRes int resId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(resId, findViewById(R.id.popup));

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        PopupWindow popup = new PopupWindow(popupView, width, height, true) {
            @Override
            public void dismiss() {
                super.dismiss();
                barcodeScanEnabled = true;
            }
        };

        popup.setAnimationStyle(R.style.Animation);

        return popup;
    }


    public void onButtonShowPopupWindowClick(View view) {
        barcodeScanEnabled = false;
        popupWindow = createPopup(R.layout.manual_code_popup);
        View popupView = popupWindow.getContentView();

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        EditText codeInput = popupView.findViewById(R.id.code_input);
        showKeyboard(codeInput);

        popupView.findViewById(R.id.close_button).setOnClickListener((click) -> dismissPopup(popupWindow));

        popupView.findViewById(R.id.validate_code).setOnClickListener((clickedView) -> {
            dismissPopup(popupWindow);
            String id = codeInput.getText().toString();
            onDataReceived(id);
        });
    }

    private void dismissPopup(PopupWindow popup) {
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
        }
    }

    private void flashLightClick(ImageView flashLightButton) {
        if (camera != null && camera.getCameraInfo().hasFlashUnit()) {
            flashLightState = !flashLightState;
            camera.getCameraControl().enableTorch(flashLightState);

            if(flashLightState)
                flashLightButton.setImageResource(R.drawable.flash_light_on);
            else flashLightButton.setImageResource(R.drawable.flash_light_off);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
        ResolutionSelector.Builder resolutionSelector = new ResolutionSelector.Builder();

        if (targetResolution != null)
            resolutionSelector.setResolutionStrategy(new ResolutionStrategy(targetResolution, ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER));

        builder.setResolutionSelector(resolutionSelector.build());

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
    public void onDataReceived(String eventId) {
        Log.d("RECEIVED_DATA", eventId);
        if (!ListEvent.containsEvent(eventId)) {
            this.eventId = eventId;
            ListEvent.eventExists(eventId, this);
            return;
        }
        goToEventActivity(eventId);
    }

    @Override
    public void onDataBaseResponse(DataBaseResponses response) {
        if (eventId == null)
            return;

        if (response == DataBaseResponses.SUCCESS) {
            ListEvent.requestEvent(eventId);
            goToEventActivity(eventId);
        } else {
            barcodeScanEnabled = false;
            showEventErrorPopup(R.string.event_does_not_exist);
        }
    }

    private void goToEventActivity(String eventId) {
        Intent receivedData = new Intent(this, EventActivity.class);
        //TODO A MODIF SINGLETON
        receivedData.putExtra("event", ListEvent.getEventMap().get(eventId));
        startActivity(receivedData);
        finish();
    }
}
