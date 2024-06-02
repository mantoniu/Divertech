package Si3.divertech.notificationservice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

public class ConvertImage {

    public static Bitmap getBitmapFromUrl(String imageUrl) {
        try{
            URL url = new URL(imageUrl);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            Log.d("RenderImage", "Error while rendering image: " + e.getMessage());
            return null;
        }


    }
}
