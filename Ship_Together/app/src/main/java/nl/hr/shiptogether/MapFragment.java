package nl.hr.shiptogether;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.SharedPreferences;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.io.IOException;
import java.util.ArrayList;

import objectslibrary.Ship;
import objectslibrary.SocketObjectWrapper;
import socketclient.SocketClient;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // inflate and return the layout
        View v = inflater.inflate(R.layout.fragment_map, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        // latitude and longitude


        // Perform any camera updates here
        //todo get coordinates from database
        sharedpreferences = MapDataActivity.context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        int MMSI = sharedpreferences.getInt("sharedPrefMMSI", 0);;

        System.out.println(MMSI);

        SocketObjectWrapper sow = new SocketObjectWrapper(new Ship(MMSI), 3);
        System.out.println("starting socketconnection");
        new NetworkHandler().execute(sow);

        return v;
    }

    public void positionCamera(double latitude, double longitude){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(13).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    // passes a list of weightedlatlng objects to the map and generates a heatmap based on it.
    public void weightedLatLngListToHeatmap(ArrayList<WeightedLatLng> list){

        HeatmapTileProvider.Builder mBuilder = new HeatmapTileProvider.Builder();
        mBuilder.radius(25);
        mBuilder.weightedData(list);
        HeatmapTileProvider mProvider = mBuilder.build();

        googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    public double calculateMean(ArrayList<Ship> shipData)
    {
        double sum = 0;
        double length = 0;
        for(Ship ship : shipData){
            sum += ship.carbonFootprint();
            length++;
        }

        return sum/length;
    }

    public WeightedLatLng ShipToWeightedLatLng(Ship ship, double mean){
        double lat = ship.getLatitude();
        double lng = ship.getLongitude();
        double weight = 0;
        if(ship.carbonFootprint() > mean * 1.2){
            weight = 5;
        } else if(ship.carbonFootprint() < mean * 0.8) {
            weight = 1;
        } else {
            weight = 3;
        }



        LatLng latLng = new LatLng(lat, lng);
        Log.i("Latitude", lat + "");
        Log.i("Longitude", lng + "");

        WeightedLatLng weightedLatLng = new WeightedLatLng(latLng, weight);

        return weightedLatLng;
    }

    class NetworkHandler extends AsyncTask<SocketObjectWrapper, Void, ArrayList<Ship>> {
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

        protected void onPostExecute(ArrayList<Ship> shipLocationEmissionData) {
            ArrayList<WeightedLatLng> weightedLatLngArrayList = new ArrayList();
            double latitudeCamera = 51.9244;
            double longitudeCamera = 4.4777;
            double mean = 0;
            mean = calculateMean(shipLocationEmissionData);

            if (shipLocationEmissionData != null) {
                for (Ship ship : shipLocationEmissionData){
                    WeightedLatLng weightedLatLng = ShipToWeightedLatLng(ship, mean);
                    weightedLatLngArrayList.add(weightedLatLng);

                    latitudeCamera = ship.getLatitude();
                    longitudeCamera = ship.getLongitude();
                }
                weightedLatLngListToHeatmap(weightedLatLngArrayList);

                positionCamera(latitudeCamera, longitudeCamera);

            } else {
                Log.wtf("Array retrieved", "Array retrieved is somehow null.");
            }
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