package nl.hr.shiptogether;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.RunnableFuture;

import objectslibrary.Ship;
import objectslibrary.SocketObjectWrapper;
import socketclient.SocketClient;


public class DataActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    Ship shipData;

    class NetworkHandler extends AsyncTask<SocketObjectWrapper, Void, Ship> {
        private Exception exception;
        SocketClient sc = new SocketClient();

        @Override
        protected Ship doInBackground(SocketObjectWrapper... params) {
            Ship shipData;
            SocketObjectWrapper sow = params[0];

            try {
                shipData = (Ship) sc.communicateWithSocket(sow);
                return shipData;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(Ship shipData2) {

            if (shipData2 != null) {
                System.out.println("LOLOLOLOLOLOLOLOLOOLOOLOOLOLO");
                shipData = shipData2;
                refreshTextView.run();

            } else {

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Integer MMSI = sharedpreferences.getInt("sharedPrefMMSI", 0);

        SocketObjectWrapper sow = new SocketObjectWrapper(new Ship(MMSI), 6);
        new NetworkHandler().execute(sow);

    }



    private Runnable refreshTextView = new Runnable() {
        public void run() {
            Integer MMSI = sharedpreferences.getInt("sharedPrefMMSI", 0);
            TextView MMSIView = (TextView) findViewById(R.id.MMSItextView);
            TextView SOGView = (TextView) findViewById(R.id.SOGtextView);
            TextView CarbonFootprintView = (TextView) findViewById(R.id.CarbonFootprinttextView);

            MMSIView.setText(MMSI.toString());
            SOGView.setText( Double.toString(Math.round(shipData.getSpeed())) + " km/h");
            CarbonFootprintView.setText(Double.toString(Math.round(shipData.carbonFootprint())) + " KG");
            System.out.println(SOGView);
            System.out.println(CarbonFootprintView);
        }
    };

}


