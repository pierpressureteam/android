package nl.hr.shiptogether;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;


public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        CreateLineChart();

    }
    public LineChart CreateLineChart() {
        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        LineDataSet newData = CreateLinechartDataSet();
        ArrayList<String> labels = CreateLabelsForLineChart();

        LineData data = new LineData(labels, newData);
        lineChart.setData(data); // set the data and list of lables into chart
        return lineChart;
    }

    public LineDataSet CreateLinechartDataSet() {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(12f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");
        return dataset;
    }

    public ArrayList<String> CreateLabelsForLineChart() {
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        return labels;
    }



}
