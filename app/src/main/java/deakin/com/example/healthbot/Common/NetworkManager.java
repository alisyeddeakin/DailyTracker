package deakin.com.example.healthbot.Common;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;


public class NetworkManager {
    public static ProgressDialog progress;


    public static void loadingDialog(Context context, String title, String message) {
        progress = new ProgressDialog(context);
        progress.setTitle(title);
        progress.setMessage(message);
        progress.show();

    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   Boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = (int) Math.round((float) ratio * realImage.getWidth());
        int height = (int) Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public static String encodeTobase64(Bitmap image) {
        String imageEncoded = null;
        try {
            if (image != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] b = baos.toByteArray();
                imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

                return imageEncoded;
            }
            return imageEncoded;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageEncoded;
    }

    public static Bitmap decodetoBitmap(String encodedString) {
        Bitmap decodedByte = null;
        try {
            byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        } catch (Exception e) {
            Log.d("error@ decode", "");
        }


        return decodedByte;
    }


}
