package nl.hr.shiptogether;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;

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
        SocketClient sc = new SocketClient();

        @Override
        protected ArrayList<Ship> doInBackground(SocketObjectWrapper... params) {
            ArrayList<Ship> shipData;
            SocketObjectWrapper sow = params[0];

            try {
                shipData = (ArrayList<Ship>) sc.communicateWithSocket(sow);
                return shipData;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(ArrayList<Ship> shipData) {

            if (shipData != null) {
                String selectedChart = String.valueOf(chartSpinner.getSelectedItem());
                LineChart lineChart = (LineChart) findViewById(R.id.chart);
                double dCumulativeEmmision = 0.0;
                String sCumulativeEmmision = "";

                LineData lineData = new Linechart().CreateLineData(shipData, getApplicationContext());
                lineChart.setData(lineData);


                if (selectedChart.equals("CO2 uitstoot - Tijd")) {
                    lineChart.setDescription("CO2 uitstoot tegen de tijd");
                    lineChart.setDescriptionTextSize(14f);

                    for (int i = 0; i < shipData.size(); i++) {
                        Ship currentShipData = shipData.get(i);
                        double carbonFootprint = currentShipData.carbonFootprint();
                        dCumulativeEmmision = dCumulativeEmmision + carbonFootprint;
                    }
                    sCumulativeEmmision =  String.format("%.2f", dCumulativeEmmision);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("sharedPrefCumulativeEmmision", sCumulativeEmmision);
                    editor.commit();
                } else {
                    lineChart.setDescription("CO2 uitstoot tegen de snelheid");
                    lineChart.setDescriptionTextSize(14f);
                }

                refreshTextView.run();
                XAxis x = lineChart.getXAxis();
                x.setPosition(XAxis.XAxisPosition.BOTTOM);
                x.setAvoidFirstLastClipping(true);
                lineChart.invalidate();

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
                SharedPreferences.Editor editor = sharedpreferences.edit();
                String selectedChart = String.valueOf(chartSpinner.getSelectedItem());
                Integer MMSI = sharedpreferences.getInt("sharedPrefMMSI", 0);
                if (selectedChart.equals("CO2 uitstoot - Tijd")) {
                    SocketObjectWrapper sow = new SocketObjectWrapper(new Ship(MMSI), 3);
                    editor.putString("sharedPrefChartType", "tijd");
                    editor.commit();
                    new NetworkHandler().execute(sow);
                } else if (selectedChart.equals("CO2 uitstoot - Snelheid")) {
                    SocketObjectWrapper sow = new SocketObjectWrapper(new Ship(MMSI), 4);
                    editor.putString("sharedPrefChartType", "snelheid");
                    editor.commit();
                    new NetworkHandler().execute(sow);
                }


            }

        });
    }

    private Runnable refreshTextView = new Runnable() {

        public void run() {
            String selectedChart = String.valueOf(chartSpinner.getSelectedItem());
            TextView infoView = (TextView) findViewById(R.id.infoView);
            String cumulativeEmmision = sharedpreferences.getString("sharedPrefCumulativeEmmision", "");

            if (selectedChart.equals("CO2 uitstoot - Tijd")) {
                infoView.setText("Totale uitstoot: " + cumulativeEmmision + " KG CO2");
            } else {
                infoView.setText("");
            }

        }
    };


}
