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

import objectslibrary.Ship;
import objectslibrary.SocketObjectWrapper;
import socketclient.SocketClient;


public class DataActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    class NetworkHandler extends AsyncTask<SocketObjectWrapper, Void, ArrayList<Ship>> {
        private Exception exception;
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
                refreshData(shipData);
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


    public void refreshData(ArrayList<Ship> shipData){
        Integer MMSI = sharedpreferences.getInt("sharedPrefMMSI", 0);
        TextView MMSIView = (TextView) findViewById(R.id.MMSItextView);
        TextView SOGView = (TextView) findViewById(R.id.SOGtextView);
        TextView CarbonFootprintView = (TextView) findViewById(R.id.CarbonFootprinttextView);
        Ship currentShipData = shipData.get(0);

        MMSIView.setText(MMSI);
        SOGView.setText( Double.toString(currentShipData.getSpeed()));
        CarbonFootprintView.setText(Double.toString(currentShipData.carbonFootprint()));

    }


}
