package nl.hr.shiptogether;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import objectslibrary.Ship;
import objectslibrary.SocketObjectWrapper;
import socketclient.SocketClient;


public class DataActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    Ship shipData;

    class NetworkHandler extends AsyncTask<SocketObjectWrapper, Void, Ship> {
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
                shipData = shipData2;


                refreshTextView.run();

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Integer MMSI = sharedpreferences.getInt("sharedPrefMMSI", 0);

        final SocketObjectWrapper sow = new SocketObjectWrapper(new Ship(MMSI), 6);
        new NetworkHandler().execute(sow);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new NetworkHandler().execute(sow);
            }

        }, 0, 1000 * 60);
    }

    private Runnable refreshTextView = new Runnable() {
        public void run() {
            Integer MMSI = sharedpreferences.getInt("sharedPrefMMSI", 0);
            TextView MMSIView = (TextView) findViewById(R.id.MMSItextView);
            TextView SOGView = (TextView) findViewById(R.id.SOGtextView);
            TextView CarbonFootprintView = (TextView) findViewById(R.id.CarbonFootprinttextView);
            TextView timeStampView = (TextView) findViewById(R.id.tijdStipTextView);

            Long time = shipData.getTime();
            Date timeStamp = new Date(time);
            DateFormat df = new SimpleDateFormat("dd/MM HH:mm");
            String timeString = df.format(timeStamp);

            MMSIView.setText(MMSI.toString());
            SOGView.setText( Double.toString(Math.round(shipData.getSpeed())) + " km/h");
            CarbonFootprintView.setText(Double.toString(Math.round(shipData.carbonFootprint())) + " KG");
            timeStampView.setText(timeString);
        }
    };



}


