package Si3.divertech;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCropperActivity extends AppCompatActivity {
    private int aspectRatioX;
    private int aspectRatioY;
    private CropImageView.CropShape cropShape;
    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                launchImageCropper(imageUri);
            }
        } else finish();
    });

    private final ActivityResultLauncher<String> requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            getImageFile();
        } else {
            permissionDenied();
        }
    });

    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
        if (result.isSuccessful()) {
            Bitmap cropped = BitmapFactory.decodeFile(result.getUriFilePath(getApplicationContext(), true));
            saveCroppedImage(cropped);
        }
        finish();
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null)
            return;

        aspectRatioX = intent.getIntExtra("aspectRatioX", -1);
        aspectRatioY = intent.getIntExtra("aspectRatioY", -1);
        int shapeInt = intent.getIntExtra("shape", -1);

        if (aspectRatioX < 0 || aspectRatioY < 0 || shapeInt < 0) {
            finish();
            return;
        }

        Log.d("created => test", "");

        cropShape = CropImageView.CropShape.values()[shapeInt];

        if (isPermitted()) {
            getImageFile();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void getImageFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
    }

    private boolean isPermitted() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
            return ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return true;
    }

    private void launchImageCropper(Uri uri) {
        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.imageSourceIncludeGallery = true;
        cropImageOptions.imageSourceIncludeCamera = true;
        cropImageOptions.cropShape = cropShape;
        cropImageOptions.fixAspectRatio = true;
        cropImageOptions.aspectRatioX = aspectRatioX;
        cropImageOptions.aspectRatioY = aspectRatioY;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(uri, cropImageOptions);
        cropImage.launch(cropImageContractOptions);
    }

    private void permissionDenied() {
        Toast.makeText(getApplicationContext(), R.string.permission_denied, Toast.LENGTH_LONG).show();
        finish();
    }

    private void saveCroppedImage(Bitmap bitmap) {
        File tempFile;
        try {
            tempFile = File.createTempFile("cropped_image", ".jpg", getCacheDir());
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("croppedImageUri", Uri.fromFile(tempFile).toString());
            setResult(RESULT_OK, resultIntent);
        } catch (IOException e) {
            Log.d("Cache image error", "", e);
        }
    }
}