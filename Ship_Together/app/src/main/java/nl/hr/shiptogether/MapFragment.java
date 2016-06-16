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

        int MMSI = sharedpreferences.getInt("sharedPrefMMSI", 0);

        SocketObjectWrapper sow = new SocketObjectWrapper(new Ship(MMSI), 3);
        SocketObjectWrapper sow2 = new SocketObjectWrapper(new Ship(MMSI), 7);

        new NetworkHandlerGeneralData().execute(sow2);
        new NetworkHandler().execute(sow);

        return v;
    }

    public void positionCamera(double latitude, double longitude) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(13).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    public void dataPointsToMap(ArrayList<Ship> list) {

        for (Ship ship : list) {
            int color = 0;
            if (gsd != null) {
                System.out.println(gsd.getAverage());
                System.out.println(gsd.getHighest());
                System.out.println(gsd.getLowest());
                color = emissionToColor(ship.carbonFootprint(), gsd.getLowest(), gsd.getHighest());
            } else {
                int MMSI = sharedpreferences.getInt("sharedPrefMMSI", 0);
                SocketObjectWrapper sow2 = new SocketObjectWrapper(new Ship(MMSI), 7);
                new NetworkHandlerGeneralData().execute(sow2);
                break;
            }


            googleMap.addCircle(new CircleOptions()
                    .center(new LatLng(ship.getLatitude(), ship.getLongitude()))
                    .radius(25)
                    .strokeColor(color)
                    .fillColor(color));
        }
    }

    public int emissionToColor(double emission, double lowest, double highest) {
        double lowestOfRange = 0;
        double highestOfRange = 100;

        int r = 0, g = 0 , b = 0;

        //TODO make color change between a preset range of 5 colors, every 20% the color should change.

        int color = Color.rgb(r, g, b);

        return color;
    }

    public LatLng shipToLatLng(Ship ship) {
        double lat = ship.getLatitude();
        double lng = ship.getLongitude();

        LatLng latLng = new LatLng(lat, lng);

        return latLng;
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
            ArrayList<LatLng> latLngArrayList = new ArrayList();
            double latitudeCamera = 51.9244;
            double longitudeCamera = 4.4777;

            for (Ship ship : shipLocationEmissionData) {
                LatLng latLng = shipToLatLng(ship);
                latLngArrayList.add(latLng);

                latitudeCamera = ship.getLatitude();
                longitudeCamera = ship.getLongitude();
            }

            shipEmissionDataDone = true;
            shipEmissionData = shipLocationEmissionData;
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