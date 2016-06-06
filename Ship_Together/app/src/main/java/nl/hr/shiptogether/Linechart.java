package nl.hr.shiptogether;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import objectslibrary.Ship;
import objectslibrary.SocketObjectWrapper;
import socketclient.SocketClient;

/**
 * Created by gover_000 on 3-6-2016.
 */
public class Linechart {

    private LineChart lineChart;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";



    public LineData CreateLineData(ArrayList<Ship> shipData, Context context) {

        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        LineDataSet newData = CreateLinechartDataSet(shipData);
        ArrayList<String> labels = CreateLabelsForLineChart(shipData);

        LineData data = new LineData(labels, newData);
        System.out.println("chart data created");
        return data;
    }


    public LineDataSet CreateLinechartDataSet(ArrayList<Ship> shipData) {

        ArrayList<Entry> entries = new ArrayList<>();
        System.out.println("start creating line data");
        System.out.println("size of shipdata: "+ shipData.size());
        for (int i = 0; i < shipData.size(); i++) {
            Ship currentShipData = shipData.get(i);
            float carbonFootprint = (float) currentShipData.carbonFootprint();
            System.out.println(carbonFootprint);
            entries.add(new Entry(carbonFootprint, i));
        }

        LineDataSet dataset = new LineDataSet(entries, "KG of CO2");
        System.out.println("line data created");
        return dataset;

    }

    public ArrayList<String> CreateLabelsForLineChart(ArrayList<Ship> shipData) {
        ArrayList<String> labels = new ArrayList<String>();

        System.out.println("start creating labels");
        String chartType = sharedpreferences.getString("sharedPrefChartType", "");
        if (chartType.equals("tijd")) {
            for (int i = 0; i < shipData.size(); i++)
            {
                Ship currentShipData = shipData.get(i);
                Long time = currentShipData.getTime();
                Date timeStamp = new Date((long)time);
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String timeString = df.format(timeStamp);
                labels.add(timeString);
            }
        } else {
            for (int i = 0; i < shipData.size(); i++)
            {
                Ship currentShipData = shipData.get(i);
                double dShipSpeed = currentShipData.getSpeed();
                String shipSpeed = String.valueOf(dShipSpeed);
                labels.add(shipSpeed);
            }
        }
        System.out.println("labels created");
        return labels;
    }


}
