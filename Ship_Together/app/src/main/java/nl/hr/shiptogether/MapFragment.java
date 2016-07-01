package nl.hr.shiptogether;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

import objectslibrary.GeneralShipData;
import objectslibrary.Ship;
import objectslibrary.SocketObjectWrapper;
import socketclient.SocketClient;

public class MapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private GeneralShipData gsd = new GeneralShipData();
    private ArrayList<Ship> shipEmissionData = new ArrayList();
    private boolean gsdDone = false;
    private boolean shipEmissionDataDone = false;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        sharedpreferences = MapDataActivity.context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        int MMSI = sharedpreferences.getInt("sharedPrefMMSI", 0); // Gets the MMSI of the ship the user is tracking.

        SocketObjectWrapper sow = new SocketObjectWrapper(new Ship(MMSI), 3);
        SocketObjectWrapper sow2 = new SocketObjectWrapper(new Ship(MMSI), 7);

        new NetworkHandlerGeneralData().execute(sow2);
        new NetworkHandler().execute(sow);

        return v;
    }

    // Positions the camera to the location in the parameters.
    public void positionCamera(double latitude, double longitude) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(10).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    // Receive an arraylist with ships and adds that to the google map.
    public void dataPointsToMap(ArrayList<Ship> list) {

        for (Ship ship : list) {
            int color = 0;
            if (gsd != null) {
                color = emissionToColor(ship.carbonFootprint(), gsd.getAverage());
            } else {
                int MMSI = sharedpreferences.getInt("sharedPrefMMSI", 0);
                SocketObjectWrapper sow2 = new SocketObjectWrapper(new Ship(MMSI), 7);
                new NetworkHandlerGeneralData().execute(sow2);
                break;
            }

            googleMap.addCircle(new CircleOptions()
                    .center(new LatLng(ship.getLatitude(), ship.getLongitude()))
                    .radius(75)
                    .strokeWidth(15f)
                    .strokeColor(color)
                    .fillColor(color));
        }
    }

    public int emissionToColor(double emission, double mean) {
        int red = Color.rgb(255, 0, 0);
        int orange = Color.rgb(255, 150, 0);
        int yellow = Color.rgb(255, 255, 0);
        int lightGreen = Color.rgb(150, 255, 0);
        int green = Color.rgb(0, 255, 0);
        if (emission <= mean * 0.8) {
            return green;
        }
        if (emission > mean * 0.8 && emission < mean * 0.9) {
            return lightGreen;
        }
        if (emission > mean * 0.9 && emission <= mean * 1.1) {
            return yellow;
        }
        if (emission > mean * 1.1 && emission <= mean * 1.2) {
            return orange;
        }
        if (emission > mean * 1.2) {
            return red;
        }

        return Color.rgb(255, 255, 255); // This is black.
    }

    class NetworkHandlerGeneralData extends AsyncTask<SocketObjectWrapper, Void, GeneralShipData> {
        SocketClient sc = new SocketClient();

        @Override
        protected GeneralShipData doInBackground(SocketObjectWrapper... params) {
            GeneralShipData shipData;
            SocketObjectWrapper sow = params[0];

            try {
                shipData = (GeneralShipData) sc.communicateWithSocket(sow);
                return shipData;

            } catch (IOException e) {
                return null;
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        protected void onPostExecute(GeneralShipData gsdIn) {
            gsd = gsdIn;
            gsdDone = true;
            // Here we check to see if the other thread is done, if it is not we will let the other thread do this.
            if (shipEmissionDataDone) {
                dataPointsToMap(shipEmissionData);
            }
        }
    }

    class NetworkHandler extends AsyncTask<SocketObjectWrapper, Void, ArrayList<Ship>> {
        SocketClient sc = new SocketClient();

        @Override
        protected ArrayList<Ship> doInBackground(SocketObjectWrapper... params) {
            ArrayList<Ship> shipData;
            SocketObjectWrapper sow = params[0];

            try {
                shipData = (ArrayList<Ship>) sc.communicateWithSocket(sow);
                return shipData;

            } catch (IOException e) {
                return null;
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        protected void onPostExecute(ArrayList<Ship> shipLocationEmissionData) {
            double latitudeCamera = 51.9244;
            double longitudeCamera = 4.4777;

            if (shipLocationEmissionData.size() > 0) {
                latitudeCamera = shipLocationEmissionData.get(0).getLatitude();
                longitudeCamera = shipLocationEmissionData.get(0).getLongitude();
            }

            shipEmissionDataDone = true;
            shipEmissionData = shipLocationEmissionData;
            // Here we check to see if the other thread is done, if it is not we will let the other thread do this.
            if (gsdDone) {
                dataPointsToMap(shipEmissionData);
            }

            positionCamera(latitudeCamera, longitudeCamera);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}