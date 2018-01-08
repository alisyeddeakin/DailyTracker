package deakin.com.example.healthbot.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import deakin.com.example.healthbot.Common.Configs;
import deakin.com.example.healthbot.Common.Utils;
import deakin.com.example.healthbot.R;


public class UiRegister extends FragmentActivity implements AdapterView.OnItemSelectedListener {

    final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1123;

    public static final String FACEBOOK_SIGNUP_STATE = "facebook_signup_action";
    public static final String SIGNED_UP_STATE = "signed_up_action";
    Context context;

    Spinner txtRole;
    EditText txtEmail;
    EditText txtPassword, txtConfirmPass;

    Button btnRegister;
    View btnLogin;
    LinearLayout layoutFbSignUp;
    private AlertDialog dialog;
    private EditText txtuserName;
    private String email, uname, pass, pass2;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ui_register);

        context = this;

        // Spinner element

        txtRole = (Spinner) findViewById(R.id.spinnerProfile);
        // Spinner click listener
        txtRole.setOnItemSelectedListener(this);


        // Spinner Drop down elements

        List<String> categories4 = new ArrayList<String>();
        addCategoriesRoles(categories4);

        txtEmail = (EditText) findViewById(R.id.txt_signup_email);
        txtuserName = (EditText) findViewById(R.id.txt_signup_name);
        txtPassword = (EditText) findViewById(R.id.txt_signup_password);
        txtConfirmPass = (EditText) findViewById(R.id.txt_signup_confirm_password);


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories4);

        // Drop down layout style - list view with radio button
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        txtRole.setAdapter(dataAdapter4);

        btnRegister = (Button) findViewById(R.id.btn_signup_register);
        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (applyValidation()) {
                    Utils.ShowProgressDialog(UiRegister.this, "Creating Account...");
                    getData();
                    verifySignUpDetails();

                }

            }
        });


        btnLogin = findViewById(R.id.btn_signup_already_registered);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void addCategoriesRoles(List<String> categories4) {
        String[] roles = Utils.roles;
        for (int x = 0; x < roles.length; x++) {
            categories4.add(roles[x]);
        }
        return;

    }

    private void getData() {

        email = txtEmail.getText().toString().trim();
        uname = txtuserName.getText().toString().trim();
        role = Integer.toString(txtRole.getSelectedItemPosition());
        pass = txtPassword.getText().toString().trim();
        pass2 = txtConfirmPass.getText().toString().trim();

    }

    private boolean applyValidation() {

        boolean value = true;

        if (txtEmail.getText().toString().isEmpty()) {
            txtEmail.setError("Enter email");
            txtEmail.requestFocus();
            return false;
        }

        if (!Utils.isValidEmail(txtEmail.getText().toString())) {
            txtEmail.setError("Enter valid email");
            txtEmail.requestFocus();
            return false;
        }

//        if (txtName.getText().toString().isEmpty()) {
//
//            txtName.setError("Enter name");
//            txtName.requestFocus();
//            return false;
//        }

        if (txtPassword.getText().toString().isEmpty()) {

            txtPassword.setError("Enter password");
            txtPassword.requestFocus();
            return false;
        }

        if (txtConfirmPass.getText().toString().isEmpty()) {

            txtConfirmPass.setError("Enter confirm password");
            txtConfirmPass.requestFocus();
            return false;
        }

        if (!txtConfirmPass.getText().toString()
                .equalsIgnoreCase(txtPassword.getText().toString())) {

            txtConfirmPass.setError("Password did'nt match");
            txtConfirmPass.requestFocus();
            return false;
        }

        return value;
    }


    private void showAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Account Verification");
        alertDialog
                .setMessage(
                        "We have sent a verification code to your email. Kindly enter the verfication code here to verify your account")
                .setCancelable(false);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        // set text length
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(4);
        input.setFilters(filterArray);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

        dialog = alertDialog.create();
        dialog.show();

        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (input.getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Enter verification code",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Utils.ShowProgressDialog(UiRegister.this, "Creating Account...");
//					userControler.accountVerification(txtEmail.getText()
//							.toString(), input.getText().toString());
                }
            }
        });
    }

    public void verifySignUpDetails() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected())) {


//            String url = "http://192.168.1.1:8080/SportsPro/servlets/LoginServlet";// enter url here
            String url = Configs.ip_server + "/HealthBot/servlets/RegisterServlet?";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Toast.makeText(UiRegister.this, "Response: " + response, Toast.LENGTH_LONG).show();
                            if (response != null) {
                                Toast.makeText(UiRegister.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                Gson gson = new GsonBuilder().create();
                                String[] signUpResponse = gson.fromJson(response, String[].class);

                                if (signUpResponse[0] != null) {
                                    Intent next = new Intent(UiRegister.this, MainActivity.class);
                                    startActivity(next);
                                    Utils.newReg = true;
                                    finish();
//                                    showAlertDialog();
//                                    finish();
                                }

                            }

                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UiRegister.this, "Error: " + error.getCause(), Toast.LENGTH_LONG).show();
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
                    params.put("username", uname);
                    params.put("password", pass);
                    params.put("email", email);
                    params.put("role", role);

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
            Toast toast = Toast.makeText(UiRegister.this, "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if (view.getId() == txtRole.getId()) {
//            Toast toast = Toast.makeText(UiRegister.this, Utils.roles[txtRole.getSelectedItemPosition()], Toast.LENGTH_LONG);
//            toast.show();
//        }
//        Toast toast = Toast.makeText(UiRegister.this, Utils.roles[txtRole.getSelectedItemPosition()], Toast.LENGTH_LONG);
//        toast.show();
//        role = Utils.roles[txtRole.getSelectedItemPosition()];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
