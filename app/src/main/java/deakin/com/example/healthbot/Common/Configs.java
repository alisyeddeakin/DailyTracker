package deakin.com.example.healthbot.Common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.widget.ArrayAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Configs

{
    public static String ip_server = "http://192.168.1.16:8080";
    //    public static final String ip_server = "http://192.168.100.76:8080";
//            public static final String ip_server = "http://192.168.20.108:8080";
//    public static final String ip_server = "http://172.16.0.243:8080";
    public static final String STATUS_SERVICE_DESTROYED = "STATUS_SERVICE_DESTROYED";
    public static final String MOMENT_MEDIA_TYPE_VIDEO = "video";
    public static final String MOMENT_MEDIA_TYPE_IMAGE = "image";
    public static final String BACKENDLESS_APP_ID = "F426E4B8-7BDD-4BF0-FF14-518108B16700";
    public static final String BACKENDLESS_APP_SECRET = "AB7FED79-3488-7C50-FF87-9AD49737BB00";


    public static String GCM_PROJECT_NUMBER = "656150074533";  //old=200671326947

    public static final int GALLERY_OPEN_FOR_PHOTO_REQUEST_CODE = 100;
    public static final int GALLERY_OPEN_FOR_CHAT_REQUEST_CODE = 110;
    public static final int CAMERA_AND_CROP_OPEN_FOR_CHAT_REQUEST_CODE = 120;
    public static final int CAMERA_OPEN_FOR_CHAT_REQUEST_CODE = 125;
    public static final int CROP_OPEN_FOR_CHAT_REQUEST_CODE = 130;
    public static boolean isMapFirstTime = false;


    public static String UPDATE_MESSAGES = "updatemessages";


    /**
     * Create error AlertDialog.
     */
    public static Dialog createErrorAlertDialog(final Context context, String title, String message) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
    }

    /**
     * Create error AlertDialog.
     */
    public static Dialog createErrorAlertDialog(final Context context, String title, String message, DialogInterface.OnClickListener OnOKListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, OnOKListener).create();
    }

    public static Dialog createListDialog(final Context context
            , String title
            , ArrayAdapter<String> adapter
            , DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setAdapter(adapter, listener);
        return builder.create();
    }

    /**
     * Create error AlertDialog.
     */
    public static Dialog createDefaultAlertDialog(final Context context
            , String title
            , String message
            , DialogInterface.OnClickListener OnOKListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, OnOKListener)
                .setNegativeButton(android.R.string.cancel, null).create();
    }

    /**
     * Create error AlertDialog.
     */
    public static Dialog createCustomAlertDialog(final Context context
            , String title
            , String message
            , String butOK
            , DialogInterface.OnClickListener OnOKListener
            , String butCancel
            , DialogInterface.OnClickListener OnCancelListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(butOK, OnOKListener)
                .setNegativeButton(butCancel, OnCancelListener).create();
    }

    public static File getOutputImageFile(Context context) {
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        File mediaFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "_IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    public static File getOutputImageFile() {
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "_IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    public static File getOutputVideoFile(Context context) {

        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        // Create a media file name
//        String userId = new UserPreferences(context).getUser().getUserId()+"";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

//        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + userId+"_VID_" + timeStamp + ".mp4";
        if (!isSDPresent || !Environment.isExternalStorageRemovable()) {
//            return new File(context.getFilesDir(), userId+"_VID_" + timeStamp + ".mp4");
            return null;
        } else {
//            File mediaFile = new File(path);
            return null;
        }
    }

//    public static File getMomentVideoFile(Context context, String videoUrl) {
//
//        String fileName = Utils.GetFileName(videoUrl);
//        Boolean isSDPresent = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
//        // Create a media file name
//        String userId = new UserPreferences(context).getUser().getUserId()+"";
//
//        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + userId+"_VID_" + fileName + ".mp4";
//        if(!isSDPresent || !Environment.isExternalStorageRemovable())
//        {
//            return new File(context.getFilesDir(), userId+"_VID_" + fileName + ".mp4");
//        }
//        else {
//            File mediaFile = new File(path);
//            return mediaFile;
//        }
//    }

    public static String getMimeType(Context context, Uri fileUri) {
        ContentResolver cr = context.getContentResolver();
        String mimeType = cr.getType(fileUri);

        return mimeType;
    }


    /**
     * Convert image uri to file
     */
    public static String/*File*/ convertImageUriToFile(Context context, Uri imageUri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID/*, MediaStore.Images.ImageColumns.ORIENTATION*/};
            cursor = context.getContentResolver().query(
                    imageUri,
                    projection, // Which columns to return
                    null,       // WHERE clause; which rows to return (all rows)
                    null,       // WHERE clause selection arguments (none)
                    null);      // Order-by clause (ascending by name)

            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //int orientation_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

            if (cursor.moveToFirst()) {
                //String orientation = cursor.getString(orientation_ColumnIndex);
                return cursor.getString(file_ColumnIndex)/*new File(cursor.getString(file_ColumnIndex))*/;
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Get bitmap from internal image file.
     */
    public static Bitmap getBitmapFromUri(Uri fileUri) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), newOpts);

        if (bitmap != null)
            bitmap.recycle();

        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        newOpts.inSampleSize = calculateInSampleSize(newOpts, 200, 200);

        bitmap = BitmapFactory.decodeFile(fileUri.getPath(), newOpts);

        return bitmap;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static byte[] compressBitmap(Bitmap origin, int nTargetSize) {
//      int nRatio = 90;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        origin.compress(Bitmap.CompressFormat.JPEG, 100, stream);

//      int nSize = stream.toByteArray().length;
//
//      while (nSize > nTargetSize) {
//          stream.reset();
//          origin.compress(Bitmap.CompressFormat.JPEG, nRatio, stream);
//          nRatio -= 10;
//
//          nSize = stream.toByteArray().length;
//      }

        return stream.toByteArray();
    }

//    public static String getDeviceID(Context context) {
//		Activity activity = ((android.app.Activity) context);
//
//		// check deviceId in shared preferences and get from it
//
////		UserPreferences prefs = new UserPreferences(context);
////		String deviceId = prefs.getDeviceId();
//
//		// if deviceId in not stored in shared preferences then generate it &
//		// store it in preferences
//		if (deviceId == null) {
//			final TelephonyManager tm = (TelephonyManager) activity
//					.getBaseContext().getSystemService(
//							Context.TELEPHONY_SERVICE);
//			String tmDevice = "" + tm.getDeviceId();
//			String tmSerial = "" + tm.getSimSerialNumber();
//
//			String androidId = ""
//					+ android.provider.Settings.Secure.getString(
//							activity.getContentResolver(),
//							android.provider.Settings.Secure.ANDROID_ID);
//
//			UUID deviceUuid = new UUID(androidId.hashCode(),
//					((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
//
//			deviceId = deviceUuid.toString().replaceAll("-", "");
//
//			// save deviceId in shared preferences to be retrieved from it later
//			prefs.setDeviceId(deviceId);
//
//			context = null;
//			prefs = null;
//		}
//		return deviceId;
//    }

    public static String saveImageToFile(Bitmap bitmap) {
        File filename = getOutputImageFile();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return filename.getAbsolutePath();
    }

    public static float toPix(Context context, float dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
