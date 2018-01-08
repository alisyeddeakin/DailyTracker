package deakin.com.example.healthbot.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import deakin.com.example.healthbot.Common.Configs;
import deakin.com.example.healthbot.Common.Utils;
import deakin.com.example.healthbot.Models.CurrentUser;
import deakin.com.example.healthbot.Models.User;
import deakin.com.example.healthbot.R;


public class MainActivity extends AppCompatActivity {

    private TextView myusername;
    private TextView myPassword;
    private Button myButton;
    private TextView signUp;
    private LinearLayout fbLogin;
    private TextView forgotPass;
    private Context context;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        FacebookSdk.sdkInitialize(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();
        context = this;
        if (!Utils.newReg) {
//            showChangeLangDialog();
        }
        initViews();
    }


    private void initViews() {
        myusername = (TextView) findViewById(R.id.txt_signin_email);
        myPassword = (TextView) findViewById(R.id.txt_signin_password);

        myusername.setText("abbas");
        myPassword.setText("123");

        myButton = (Button) findViewById(R.id.btn_splash_login);
        signUp = (TextView) findViewById(R.id.btn_splash_signup);

        forgotPass = (TextView) findViewById(R.id.txt_splash_forget_password);

        initListeners();


    }

    private void initListeners() {
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLoginDetails();
            }
        });


        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UiRegister.class);
                startActivity(intent);

            }
        });


//        loginButton = (LoginButton) findViewById(R.id.loginButn);
//        loginButton.setReadPermissions("email");

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                Log.e("fb response", "" + response.getRawResponse());
                                final JSONObject json = response.getJSONObject();

                                // get user data that you require from the jsonObject above
//                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                                startActivity(intent);
                                finish();


                            }
                        }
                );
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,first_name,gender,last_name,link,locale,name,timezone,updated_time,verified,age_range,friends,picture");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }


            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(context, "An error has occurred. Please try again +" + exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


    private void checkLoginDetails() {


        String username = myusername.getText().toString();
        String pass = myPassword.getText().toString().trim();

        Utils.ShowProgressDialog(MainActivity.this, "Logging in, please wait while we verify your credentials.");
        verifyLoginDetails(username, pass);

        if (username.equals("123@abc.com") && (pass.equals("123"))) {
            Toast.makeText(this, "Login Sucessful", Toast.LENGTH_SHORT).show();
            loginSuccesful();
        }
    }

    public void verifyLoginDetails(String email, String pass) {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected())) {


            String url = Configs.ip_server + "/HealthBot/servlets/LoginServlet?" + "username=" + email + "&password=" + pass;
//            String url = "http://172.16.1.17:8080/SportsPro/servlets/LoginServlet?" + "username=" + email + "&password=" + pass;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Toast.makeText(LoginActivity.this, "Response: " + response, Toast.LENGTH_LONG).show();
                            if (response != null) {

                                Gson gson = new GsonBuilder().create();
                                if (!response.contains("fail")) {
                                    User loginResponse = gson.fromJson(response, User.class);

//                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                    if (loginResponse != null) {

                                        CurrentUser.loggedIN = true;
                                        CurrentUser.thisUser = loginResponse;
                                        loginSuccesful();
                                        finish();
                                        Utils.HideDialog();
                                    }
                                } else {
                                    Utils.HideDialog();
                                    Toast.makeText(MainActivity.this, "Login failed, Username or Password is invalid", Toast.LENGTH_LONG).show();
                                }
                                Utils.HideDialog();
                            }

                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Error: " + error.getCause(), Toast.LENGTH_LONG).show();
                            if (error.getMessage() == null) {
                                try {

                                } catch (Exception e) {

                                }
                            }
                            Utils.HideDialog();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("username", Key_username);
//                    params.put("password", Key_password);
                    return params;
                }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/x-www-form-urlencoded");
//                return headers;
//            }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        } else {
            //no connection
            Toast toast = Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
            Utils.HideDialog();
        }


    }


    private void loginSuccesful() {

        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Welcome");
        dialogBuilder.setMessage("Enter your running server's ip address");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                Configs.ip_server = "http://" + edt.getText().toString() + ":8080";

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

}
