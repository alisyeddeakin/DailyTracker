
package deakin.com.example.healthbot.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.components.YAxis;
import com.mbientlab.metawear.AsyncDataProducer;
import com.mbientlab.metawear.UnsupportedModuleException;
import com.mbientlab.metawear.data.Acceleration;
import com.mbientlab.metawear.module.Accelerometer;
import com.mbientlab.metawear.module.AccelerometerBosch;
import com.mbientlab.metawear.module.AccelerometerMma8452q;

import deakin.com.example.healthbot.Common.Utils;
import deakin.com.example.healthbot.R;
import deakin.com.example.healthbot.help.HelpOption;
import deakin.com.example.healthbot.help.HelpOptionAdapter;

public class AccelerometerFragment extends ThreeAxisChartFragment {
    private static final float[] MMA845Q_RANGES = {2.f, 4.f, 8.f}, BOSCH_RANGES = {2.f, 4.f, 8.f, 16.f}, freq_range = {12.f, 25.f, 50.f, 100.f};
    private static final float INITIAL_RANGE = 2.f;
    private static float ACC_FREQ = 50.f;
    private Spinner frequency;
    private Spinner accRangeSelection;
    private static Button btn0, btn1, btn2, btn3;

    private Accelerometer accelerometer = null;
    private int rangeIndex = 0;
    private boolean booleanButton = false;

    public AccelerometerFragment() {
        super("acceleration", R.layout.fragment_sensor_config_spinner,
                R.string.navigation_fragment_accelerometer, -INITIAL_RANGE, INITIAL_RANGE);
    }

    public void clearButtons() {
        btn0.setPressed(false);
        btn1.setPressed(false);
        btn2.setPressed(false);
        btn3.setPressed(false);
        btn0.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btn3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

    }

    private static String getLabel() {
        String label = "";
        if (btn0.isPressed()) {
            label = btn0.getText().toString();
        } else if (btn1.isPressed()) {
            label = btn1.getText().toString();
        } else if (btn2.isPressed()) {
            label = btn2.getText().toString();
        } else if (btn3.isPressed()) {
            label = btn3.getText().toString();
        }
        return label;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        ((TextView) view.findViewById(R.id.freq_title)).setText("Acceleration Frequency");
//
//        frequency = (Spinner) view.findViewById(R.id.freq_spinner);
//        frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                rangeIndex = position;
//                ACC_FREQ = freq_range[position];
//
//                refreshChart(false);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        btn0 = (Button) view.findViewById(R.id.btn0);
        btn1 = (Button) view.findViewById(R.id.btn1);
        btn2 = (Button) view.findViewById(R.id.btn2);
        btn3 = (Button) view.findViewById(R.id.btn3);

        clearButtons();

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booleanButton) {
                    if (btn0.isPressed()) {
                        clearButtons();
                        booleanButton = true;
                        btn0.setPressed(true);
                        btn0.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                        Utils.selectedButton = btn0.getText().toString();
                    } else {
                        booleanButton = false;
                        clearButtons();
                        btn0.setPressed(true);
                        Utils.selectedButton = btn0.getText().toString();
                    }
                } else {
                    booleanButton = true;
                    btn0.setPressed(true);
                    btn0.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                    Utils.selectedButton = btn0.getText().toString();

                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booleanButton) {
                    if (btn1.isPressed()) {
                        booleanButton = true;
                        clearButtons();
                        btn1.setPressed(true);
                        btn1.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                        Utils.selectedButton = btn1.getText().toString();
                    } else {
                        booleanButton = false;
                        clearButtons();
                        btn1.setPressed(true);
                        Utils.selectedButton = btn1.getText().toString();
                    }
                } else {
                    booleanButton = true;
                    btn1.setPressed(true);
                    btn1.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                    Utils.selectedButton = btn1.getText().toString();
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booleanButton) {
                    if (btn2.isPressed()) {
                        booleanButton = true;
                        clearButtons();
                        btn2.setPressed(true);
                        btn2.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                        Utils.selectedButton = btn2.getText().toString();
                    } else {
                        booleanButton = false;
                        clearButtons();
                        btn2.setPressed(true);
                        Utils.selectedButton = btn2.getText().toString();
                    }
                } else {
                    booleanButton = true;
                    btn2.setPressed(true);
                    btn2.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                    Utils.selectedButton = btn2.getText().toString();
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booleanButton) {
                    if (btn3.isPressed()) {
                        booleanButton = true;
                        clearButtons();
                        btn3.setPressed(true);
                        btn3.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                        Utils.selectedButton = btn3.getText().toString();
                    } else {
                        booleanButton = false;
                        clearButtons();
                        btn3.setPressed(true);
                        Utils.selectedButton = btn3.getText().toString();
                    }
                } else {
                    booleanButton = true;
                    btn3.setPressed(true);
                    btn3.setBackgroundColor(getResources().getColor(R.color.button_pressed));
                    Utils.selectedButton = btn3.getText().toString();
                }
            }
        });

        ((TextView) view.findViewById(R.id.config_option_title)).setText(R.string.config_name_acc_range);

        accRangeSelection = (Spinner) view.findViewById(R.id.config_option_spinner);
        accRangeSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rangeIndex = position;

                final YAxis leftAxis = chart.getAxisLeft();
                if (accelerometer instanceof AccelerometerBosch) {
                    leftAxis.setAxisMaxValue(BOSCH_RANGES[rangeIndex]);
                    leftAxis.setAxisMinValue(-BOSCH_RANGES[rangeIndex]);
                } else if (accelerometer instanceof AccelerometerMma8452q) {
                    leftAxis.setAxisMaxValue(MMA845Q_RANGES[rangeIndex]);
                    leftAxis.setAxisMinValue(-MMA845Q_RANGES[rangeIndex]);
                }

                refreshChart(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fillRangeAdapter();
    }

    @Override
    protected void boardReady() throws UnsupportedModuleException {
        accelerometer = mwBoard.getModuleOrThrow(Accelerometer.class);

        fillRangeAdapter();
    }

    @Override
    protected void fillHelpOptionAdapter(HelpOptionAdapter adapter) {
        adapter.add(new HelpOption(R.string.config_name_acc_range, R.string.config_desc_acc_range));
    }

    @Override
    protected void setup() {
        Accelerometer.ConfigEditor<?> editor = accelerometer.configure();

        editor.odr(ACC_FREQ);
        if (accelerometer instanceof AccelerometerBosch) {
            editor.range(BOSCH_RANGES[rangeIndex]);
        } else if (accelerometer instanceof AccelerometerMma8452q) {
            editor.range(MMA845Q_RANGES[rangeIndex]);
        }
        editor.commit();

        samplePeriod = 1 / accelerometer.getOdr();

        final AsyncDataProducer producer = accelerometer.packedAcceleration() == null ?
                accelerometer.packedAcceleration() :
                accelerometer.acceleration();
        producer.addRouteAsync(source -> source.stream((data, env) -> {
            final Acceleration value = data.value(Acceleration.class);
            addChartData(value.x(), value.y(), value.z(), samplePeriod);
        })).continueWith(task -> {
            streamRoute = task.getResult();
            producer.start();
            accelerometer.start();

            return null;
        });
    }

    @Override
    protected void clean() {
        accelerometer.stop();

        (accelerometer.packedAcceleration() == null ?
                accelerometer.packedAcceleration() :
                accelerometer.acceleration()
        ).stop();
    }

    private void fillRangeAdapter() {


        ArrayAdapter<CharSequence> spinnerAdapter = null;
        ArrayAdapter<CharSequence> spinnerAdapter1 = null;

//        spinnerAdapter1 = ArrayAdapter.createFromResource(getContext(), R.array.values_acc_freq, android.R.layout.simple_spinner_item);

        if (accelerometer instanceof AccelerometerBosch) {
            spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.values_bmi160_acc_range, android.R.layout.simple_spinner_item);
        } else if (accelerometer instanceof AccelerometerMma8452q) {
            spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.values_mma8452q_acc_range, android.R.layout.simple_spinner_item);
        }

        if (spinnerAdapter != null) {
//            spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            frequency.setAdapter(spinnerAdapter1);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            accRangeSelection.setAdapter(spinnerAdapter);
        }
    }
}
