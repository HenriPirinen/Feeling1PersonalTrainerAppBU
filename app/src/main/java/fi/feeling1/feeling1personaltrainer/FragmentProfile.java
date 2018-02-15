package fi.feeling1.feeling1personaltrainer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by Henri Pirinen on 1/29/2018.
 */

public class FragmentProfile extends Fragment {

    EditText weightInput;
    EditText heightInput;
    EditText waistInput;
    EditText hipInput;
    EditText neckInput;
    int weight;
    int height;
    int waist;
    int hip;
    int neck;
    Context context;

    int counter = 100;

    private BarGraphSeries<DataPoint> activityMeter;
    private LineGraphSeries<DataPoint> weightMeter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_profile, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        weightInput = (EditText) view.findViewById(R.id.inputWeight);
        heightInput = (EditText) view.findViewById(R.id.inputHeight);
        waistInput = (EditText) view.findViewById(R.id.inputWaist);
        hipInput = (EditText) view.findViewById(R.id.inputHip);
        neckInput = (EditText) view.findViewById(R.id.inputNeck);
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        GraphView graphActivity = (GraphView) view.findViewById(R.id.activity);

        updateView("null");

        weightMeter = new LineGraphSeries<>(generateData("weight"));

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(150);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(4);
        graph.getViewport().setMaxX(80);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.addSeries(weightMeter);

        activityMeter = new BarGraphSeries<>(generateData("activity"));

        graphActivity.getViewport().setYAxisBoundsManual(true);
        graphActivity.getViewport().setMinY(0);
        graphActivity.getViewport().setMaxY(8);

        graphActivity.getViewport().setXAxisBoundsManual(true);
        graphActivity.getViewport().setMinX(0);
        graphActivity.getViewport().setMaxX(31);

        // enable scaling and scrolling
        graphActivity.getViewport().setScalable(true);
        graphActivity.getViewport().setScalableY(true);

        activityMeter.setSpacing(35);
        graphActivity.addSeries(activityMeter);

        view.findViewById(R.id.btnUpdateProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateView("update");
            }
        });
    }

    public void updateView(String function)
    {
        final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        if(function.equals("update")) {
            weight = Integer.parseInt(weightInput.getText().toString());
            height = Integer.parseInt(heightInput.getText().toString());
            waist = Integer.parseInt(waistInput.getText().toString());
            hip = Integer.parseInt(hipInput.getText().toString());
            neck = Integer.parseInt(neckInput.getText().toString());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.profile_height_value), height);
            editor.putInt(getString(R.string.profile_weight_value), weight);
            editor.putInt(getString(R.string.profile_hip_value), hip);
            editor.putInt(getString(R.string.profile_waist_value), waist);
            editor.putInt(getString(R.string.profile_neck_value), neck);
            editor.apply();

            counter++;

            //activityMeter.resetData(generateData("activity"));
            //weightMeter.resetData(generateData("weight"));
            weightMeter.appendData(new DataPoint(counter, weight),true,100);

            Toast.makeText(context, "Päivitetty", Toast.LENGTH_LONG).show();
        }

        weightInput.setText(Integer.toString(sharedPref.getInt(getString(R.string.profile_weight_value),0)));
        heightInput.setText(Integer.toString(sharedPref.getInt(getString(R.string.profile_height_value), 0)));
        waistInput.setText(Integer.toString(sharedPref.getInt(getString(R.string.profile_waist_value), 0)));
        neckInput.setText(Integer.toString(sharedPref.getInt(getString(R.string.profile_neck_value), 0)));
        hipInput.setText(Integer.toString(sharedPref.getInt(getString(R.string.profile_hip_value), 0)));
    }

    private DataPoint[] generateData(String graph)
    {
        DataPoint[] newData;
        switch (graph){
            case "activity":
                newData = new DataPoint[60];
                for (int i = 0; i < newData.length; i++) { //Placeholder
                    newData[i] = new DataPoint(i, Math.random() * 8);
                }
                break;

            case "weight":
                newData = new DataPoint[100];
                for (int i = 0; i < newData.length; i++) { //Placeholder
                    newData[i] = new DataPoint(i, 100-i*(Math.random()*1));
                }
                break;
            default:
                newData = new DataPoint[10];
                break;
        }
        return newData;
    }
}
