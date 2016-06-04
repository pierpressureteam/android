package nl.hr.shiptogether;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.IOException;
import java.util.ArrayList;

import objectslibrary.Ship;
import objectslibrary.SocketObjectWrapper;
import socketclient.SocketClient;


public class GraphActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    private Spinner chartSpinner;
    private Button makeChartButton;
    SharedPreferences sharedpreferences;

    class NetworkHandler extends AsyncTask<SocketObjectWrapper, Void,ArrayList<Ship>> {
        private Exception exception;
        SocketClient sc = new SocketClient();

        @Override
        protected ArrayList<Ship> doInBackground(SocketObjectWrapper... params) {
            ArrayList<Ship> shipData;
            SocketObjectWrapper sow = params[0];

            try {
                shipData = (ArrayList<Ship>) sc.communicateWithSocket(sow);
                System.out.println("returning shipdata");
                return shipData;

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("nope");
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Le nope");
                return null;
            }
        }

        protected void onPostExecute(ArrayList<Ship> shipData) {


            if (shipData != null) {
                System.out.println("making linedata");
                LineChart lineChart = (LineChart) findViewById(R.id.chart);
                LineData lineData = new Linechart().CreateLineData(shipData, getApplicationContext());
                lineChart.setData(lineData);
                lineChart.invalidate();
                System.out.println("Chart data set");

            } else {

            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        addListenerOnButton();

    }

    public void addListenerOnButton() {

        chartSpinner = (Spinner) findViewById(R.id.chartSpinner);
        makeChartButton = (Button) findViewById(R.id.makeChartButton);


        makeChartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("buttonclicked");
                SharedPreferences.Editor editor = sharedpreferences.edit();
                String selectedChart = String.valueOf(chartSpinner.getSelectedItem());
                Integer MMSI = sharedpreferences.getInt("sharedPrefMMSI", 0);

                if (selectedChart.equals("CO2 uitstoot - Tijd")) {
                    editor.putString("sharedPrefChartType", "tijd");
                    editor.commit();
                } else if (selectedChart.equals("CO2 uitstoot - Snelheid")) {
                    editor.putString("sharedPrefChartType", "snelheid");
                    editor.commit();
                }

                SocketObjectWrapper sow = new SocketObjectWrapper(new Ship(MMSI), 3);
                System.out.println("starting socketconnection");
                new NetworkHandler().execute(sow);
            }

        });
    }




}
