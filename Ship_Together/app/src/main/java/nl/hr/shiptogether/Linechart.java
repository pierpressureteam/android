package nl.hr.shiptogether;

import android.content.Context;
import android.content.SharedPreferences;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import objectslibrary.Ship;

/**
 * Created by gover_000 on 3-6-2016.
 */
public class Linechart {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";



    public LineData CreateLineData(ArrayList<Ship> shipData, Context context) {

        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        LineDataSet newData = CreateLinechartDataSet(shipData);
        ArrayList<String> labels = CreateLabelsForLineChart(shipData);

        LineData data = new LineData(labels, newData);
        return data;
    }


    public LineDataSet CreateLinechartDataSet(ArrayList<Ship> shipData) {

        ArrayList<Entry> entries = new ArrayList<>();

        System.out.println("making carbonfootprint dataset");
        for (int i = 0; i < shipData.size(); i++) {
            Ship currentShipData = shipData.get(i);
            float carbonFootprint = (float) currentShipData.carbonFootprint();
            entries.add(new Entry(carbonFootprint, i));
        }

        LineDataSet dataset = new LineDataSet(entries, "KG of CO2");
        return dataset;
    }

    public ArrayList<String> CreateLabelsForLineChart(ArrayList<Ship> shipData) {
        ArrayList<String> labels = new ArrayList<String>();

        String chartType = sharedpreferences.getString("sharedPrefChartType", "");
        if (chartType.equals("tijd")) {
            for (int i = 0; i < shipData.size(); i++)
            {

                Ship currentShipData = shipData.get(i);
                Long time = currentShipData.getTime();
                Date timeStamp = new Date(time);
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String timeString = df.format(timeStamp);
                labels.add(timeString);
            }
        } else {
            System.out.println("graphtype = snelheid");
            for (int i = 0; i < shipData.size(); i++)
            {
                Ship currentShipData = shipData.get(i);
                double dShipSpeed = currentShipData.getSpeed();
                String shipSpeed = String.valueOf(dShipSpeed);
                labels.add(shipSpeed);
            }
        }
        return labels;
    }
}
