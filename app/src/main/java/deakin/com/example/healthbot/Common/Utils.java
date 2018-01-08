package deakin.com.example.healthbot.Common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import deakin.com.example.healthbot.Models.Row;


public class Utils {


    public static String name;
    public static Boolean newReg = false;
    public static String listFlag = "";

    private static final String TAG = Utils.class.getSimpleName();
    private static ProgressDialog progressDialog;
    public static long LastRequestedTime;

    public static String[] roles = {"Select Role", "End User", "Data Collector"};

    public static ArrayList<Row> dataUploadable;
    public static String selectedButton;
    public static int selectedINDEX_OF_Member;
    public static String roleFlag;
    public static String thisFlag;
    public static String typeFlag;


    public static String getValid(String text) {
        String text_ = Utils.cleanText(text);
        if (Utils.isValid(text_)) {
            return text_;
        }
        return "";
    }

    public static String cleanText(String text) {
        if (Utils.isValid(text)) {
            return text.trim();
        }
        return "";
    }

    public static boolean isValid(String text) {
        return (text != null) && (!(text.trim().isEmpty()));
    }

    public static boolean isValid(Object[] array) {
        return (array != null) && (array.length > 0);
    }

    public static boolean isValidEmail(String email) {
        if (Utils.isValid(email)) {
            final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
        }
        return false;
    }

    public static String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader_ = null;
        String line_ = "";
        StringBuilder stringBuilder_ = new StringBuilder();
        try {
            bufferedReader_ = new BufferedReader(new InputStreamReader(
                    inputStream, "utf-8"));
            while ((line_ = bufferedReader_.readLine()) != null) {
                stringBuilder_.append(line_);
            }
            inputStream.close();
            return stringBuilder_.toString();
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        } finally {
            if (bufferedReader_ != null) {
                try {
                    bufferedReader_.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager_ = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo_ = connectivityManager_.getActiveNetworkInfo();
        return (networkInfo_ != null) && (networkInfo_.isConnected());
    }

    public static Date getDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);

    }

    // shows dialog providing context and message for progress dialog
    public static void ShowProgressDialog(Context context, String message) {
        progressDialog = ProgressDialog.show(context, "", message, true, false);
    }

    public static void HideDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static Bitmap getProfileImageFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTimeString(Date serverDate) {
        Calendar mCalendar = Calendar.getInstance();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        Date localDate = new Date(serverDate.getTime()
                + mTimeZone.getRawOffset());

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        return dateFormat.format(localDate);
    }

    public static String getTimeSpan(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date serverDate = format.parse(dateString);
            Calendar mCalendar = Calendar.getInstance();
            TimeZone mTimeZone = mCalendar.getTimeZone();
            Date localBasedDate = new Date(serverDate.getTime()
                    + mTimeZone.getRawOffset());

            Date currentDate = Calendar.getInstance().getTime();

            long timeDifference = currentDate.getTime()
                    - localBasedDate.getTime();

            String lastSeen = "";

            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            if (timeDifference < 86400000) {
                if (localBasedDate.getDay() == currentDate.getDay()) {
                    lastSeen += "today at " + dateFormat.format(localBasedDate);
                } else {
                    lastSeen += "yesterday at "
                            + dateFormat.format(localBasedDate);
                }
            } else if (timeDifference < 604800000) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(localBasedDate);
                String dayName = getDayName(calendar.get(Calendar.DAY_OF_WEEK));

                calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                String todayName = getDayName(calendar
                        .get(Calendar.DAY_OF_WEEK));

                if (!todayName.equals(dayName)) {
                    lastSeen += dayName + " "
                            + dateFormat.format(localBasedDate);
                } else {
                    dateFormat = new SimpleDateFormat("d MMM, hh:mm a");
                    lastSeen += dateFormat.format(localBasedDate);
                }
            } else if (timeDifference < 2592000000L) {
                if (serverDate.getMonth() == currentDate.getMonth()) {
                    dateFormat = new SimpleDateFormat("d MMM, hh:mm a");
                    lastSeen += dateFormat.format(localBasedDate);
                } else {
                    dateFormat = new SimpleDateFormat("MMM d, yyyy");
                    lastSeen += dateFormat.format(localBasedDate);
                }
            } else {
                dateFormat = new SimpleDateFormat("MMM d, yyyy");
                lastSeen += dateFormat.format(localBasedDate);
            }

            lastSeen = lastSeen.replace("AM", "am").replace("PM", "pm");
            return lastSeen;

        } catch (ParseException e) {
            e.printStackTrace();

            return "Offline";
        }
    }

    public static String getTimeSpan(Date serverDate) {
        Calendar mCalendar = Calendar.getInstance();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        Date localBasedDate = new Date(serverDate.getTime()
                + mTimeZone.getRawOffset());

        Date currentDate = Calendar.getInstance().getTime();

        long timeDifference = currentDate.getTime()
                - localBasedDate.getTime();

        String lastSeen = "";
        if (timeDifference < 3600000) {
            if (timeDifference < 60000) {
                lastSeen = "just now";
            } else {
                lastSeen = timeDifference / 60000 + " m ago";
            }
        } else {
            lastSeen = timeDifference / 3600000 + " h ago";
        }
        return lastSeen;
    }

    private static String getDayName(int index) {
        switch (index) {
            case 1:
                return "Sun";

            case 2:
                return "Mon";

            case 3:
                return "Tue";

            case 4:
                return "Wed";

            case 5:
                return "Thu";

            case 6:
                return "Fri";

            case 7:
                return "Sat";
        }

        return "Monday";
    }

    public static boolean IsNullOrEmpty(String str) {
        if (str == null) {
            return true;
        } else if (str.equals("")) {
            return true;
        }
        return false;
    }

    public static String getHighResImage(String url) {
        if (!Utils.IsNullOrEmpty(url) && url.contains("fb_url")) {
            String fbId = url.replaceAll("\\D+", "");
            String highResUrl = "https://graph.facebook.com/" + fbId
                    + "/picture?width=1024&height=1024";
            return highResUrl;
        }
        return url;
    }

    public static void ShowErrorPopup(Context context, String errorMessage) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(errorMessage);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static String getElapsedTime(int seconds) {
        if (seconds < 9) {
            return "00:0" + seconds;
        }
        return "00:" + seconds;
    }

    public static String GetFileName(String path) {
        int index = path.lastIndexOf("/");
        return path.substring(index + 1);
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

}
