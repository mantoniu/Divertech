package Si3.divertech.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class UploadUtils {
    public static void uploadImage(String url, String fireStoragePath, int quality, Context context, OnSuccessListener<? super Uri> successListener, OnFailureListener failureListener) {
        if (url == null)
            return;

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(url));

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            byte[] data = stream.toByteArray();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference filePath = storage.getReference().child(fireStoragePath);
            com.google.firebase.storage.UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                if (taskSnapshot.getMetadata() == null || taskSnapshot.getMetadata().getReference() == null)
                    return;
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(successListener)
                        .addOnFailureListener(failureListener);
            }).addOnFailureListener(failureListener);

            if (inputStream != null)
                inputStream.close();
        } catch (Exception e) {
            failureListener.onFailure(e);
        }
    }
}
