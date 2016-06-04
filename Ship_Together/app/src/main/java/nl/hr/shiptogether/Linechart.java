package nl.hr.shiptogether;

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
import java.util.ArrayList;

import objectslibrary.SocketObjectWrapper;
import socketclient.SocketClient;

/**
 * Created by gover_000 on 3-6-2016.
 */
public class Linechart {

    private LineChart lineChart;

    public Linechart(LineChart lineChart)
    {
        this.lineChart = lineChart;
    }



    /*
    public LineChart CreateLineChart() {
        LineChart lineChart;

        LineDataSet newData = CreateLinechartDataSet();
        ArrayList<String> labels = CreateLabelsForLineChart();

        LineData data = new LineData(labels, newData);
        lineChart.setData(data); // set the data and list of lables into chart
        return lineChart;
    }

    public LineDataSet CreateLinechartDataSet() {
        ArrayList<Entry> entries = new ArrayList<>();


        LineDataSet dataset = new LineDataSet(entries, "# of Calls");
        return dataset;
    }

    public ArrayList<String> CreateLabelsForLineChart() {
        ArrayList<String> labels = new ArrayList<String>();

        return labels;
    }
    */
}
