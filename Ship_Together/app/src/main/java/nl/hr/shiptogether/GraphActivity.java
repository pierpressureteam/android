package nl.hr.shiptogether;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    private Spinner chartSpinner;
    private Button makeChartButton;

    /*
    class NetworkHandler extends AsyncTask<SocketObjectWrapper, Void, ArrayList<Ship>> {
        private Exception exception;
        SocketClient sc = new SocketClient();

        @Override
        protected ArrayList<Ship> doInBackground(SocketObjectWrapper... params) {
            SocketObjectWrapper sow = params[0];

            try {
                success = (boolean) sc.communicateWithSocket(sow);


            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        protected void onPostExecute(Boolean success) {


            if (success) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);



        addListenerOnButton();
        //addListenerOnSpinnerItemSelection();

    }

    public void addListenerOnButton() {

        chartSpinner = (Spinner) findViewById(R.id.chartSpinner);
        makeChartButton = (Button) findViewById(R.id.makeChartButton);

        makeChartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(chartSpinner.getSelectedItem() == null) {

                    Toast toast = Toast.makeText(getApplicationContext(), "Please select a chart", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    String selectedChart = String.valueOf(chartSpinner.getSelectedItem());
                    //make chart based on selected value of spinner
                }



            }

        });
    }




}
