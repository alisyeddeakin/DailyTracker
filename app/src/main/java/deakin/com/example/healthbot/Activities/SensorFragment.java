
package deakin.com.example.healthbot.Activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mbientlab.metawear.Route;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import deakin.com.example.healthbot.Common.Configs;
import deakin.com.example.healthbot.Common.Utils;
import deakin.com.example.healthbot.Models.CurrentUser;
import deakin.com.example.healthbot.R;


public abstract class SensorFragment extends ModuleFragmentBase {
    protected final ArrayList<String> chartXValues = new ArrayList<>();
    protected LineChart chart;
    protected int sampleCount;
    protected long prevUpdate = -1;

    protected float min, max;
    protected Route streamRoute = null;

    private byte globalLayoutListenerCounter = 0;
    private final int layoutId;

    private final Handler chartHandler = new Handler();

    protected SensorFragment(int sensorResId, int layoutId, float min, float max) {
        super(sensorResId);
        this.layoutId = layoutId;
        this.min = min;
        this.max = max;
    }

    protected void updateChart() {
        long current = Calendar.getInstance().getTimeInMillis();
        if (prevUpdate == -1 || (current - prevUpdate) >= 33) {
            chartHandler.post(() -> {
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();

                moveViewToLast();
            });

            prevUpdate = current;
        }
    }

    private void moveViewToLast() {
        chart.setVisibleXRangeMinimum(120);
        chart.setVisibleXRangeMaximum(120);
        chart.moveViewToX(Math.max(0f, chartXValues.size() - 1));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        View v = inflater.inflate(layoutId, container, false);
        final View scrollView = v.findViewById(R.id.scrollView);
        if (scrollView != null) {
            globalLayoutListenerCounter = 1;
            scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    LineChart.LayoutParams params = chart.getLayoutParams();
                    params.height = scrollView.getHeight();
                    chart.setLayoutParams(params);

                    globalLayoutListenerCounter--;
                    if (globalLayoutListenerCounter < 0) {
                        scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }

        return v;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = (LineChart) view.findViewById(R.id.data_chart);

        initializeChart();
        resetData(false);
        chart.invalidate();
        chart.setDescription(null);

        Button clearButton = (Button) view.findViewById(R.id.layout_two_button_left);
        clearButton.setOnClickListener(view1 -> refreshChart(true));
        clearButton.setText(R.string.label_clear);

        ((Switch) view.findViewById(R.id.sample_control)).setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                moveViewToLast();
                setup();

                // press unknown
            } else {
                chart.setVisibleXRangeMinimum(1);
                chart.setVisibleXRangeMaximum(sampleCount);
                clean();
                if (streamRoute != null) {
                    streamRoute.remove();
                    streamRoute = null;
                }
                // press
            }
        });

        Button saveButton = (Button) view.findViewById(R.id.layout_two_button_right);
        saveButton.setText(R.string.label_save);
        saveButton.setOnClickListener(view12 -> {
            String filename = saveData();

            if (filename != null) {
//                File dataFile = getActivity().getFileStreamPath(filename);
//                Uri contentUri = FileProvider.getUriForFile(getActivity(), "deakin.com.example.healthbot.fileprovider", dataFile);
//
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_SUBJECT, filename);
//                intent.putExtra(Intent.EXTRA_STREAM, contentUri);
//                startActivity(Intent.createChooser(intent, "Saving Data"));


            }
            if(Utils.selectedButton != null){
                saveDetails();
            }else{
                Toast.makeText(getActivity(), "Please select a label beforing saving.", Toast.LENGTH_LONG).show();
            }



        });
    }

    public void saveDetails() {


        Utils.ShowProgressDialog(getActivity(), "Saving records, please wait while we upload your data.");
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected())) {


//            String url = "http://192.168.1.1:8080/SportsPro/servlets/LoginServlet";// enter url here
            String url = Configs.ip_server + "/HealthBot/servlets/RecordsServlet?";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Toast.makeText(UiRegister.this, "Response: " + response, Toast.LENGTH_LONG).show();
                            if (response != null) {
                                Toast.makeText(getActivity(), "Data Saved Successfully", Toast.LENGTH_LONG).show();
                                Gson gson = new GsonBuilder().create();
                                String[] signUpResponse = gson.fromJson(response, String[].class);

                                if (signUpResponse[0] != null) {
//                                    Intent next = new Intent(getActivity(), MainActivity.class);
//                                    startActivity(next);
                                    Toast.makeText(getActivity(), "Data Saved Successfully", Toast.LENGTH_LONG).show();
                                    Utils.HideDialog();
//                                    finish();
//                                    showAlertDialog();
//                                    finish();
                                }

                            }

                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Error: " + error.getCause(), Toast.LENGTH_LONG).show();
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
                    Gson gson = new Gson();
                    String json = gson.toJson(Utils.dataUploadable);
                    params.put("data", json);
                    params.put("userid", Integer.toString(CurrentUser.thisUser.id));
                    params.put("label", Utils.selectedButton);

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
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        } else {
            //no connection
            Toast toast = Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
        }


    }

    protected void refreshChart(boolean clearData) {
        chart.resetTracking();
        chart.clear();
        resetData(clearData);
        chart.invalidate();
        chart.fitScreen();
    }

    protected void initializeChart() {
        ///< configure axis settings
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setStartAtZero(false);
        leftAxis.setAxisMaxValue(max);
        leftAxis.setAxisMinValue(min);
        chart.getAxisRight().setEnabled(false);
    }

    protected abstract void setup();

    protected abstract void clean();

    protected abstract String saveData();

    protected abstract void resetData(boolean clearData);
}
