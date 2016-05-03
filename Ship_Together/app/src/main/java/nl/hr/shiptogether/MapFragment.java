package nl.hr.shiptogether;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;

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
        double latitude = 51.9244;
        double longitude = 4.4777;




        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        // Perform any camera updates here

        LatLng lat = new LatLng(51.85, 4.43);
        LatLng latlng = new LatLng(51.90, 4.44);
        WeightedLatLng weightedLatLng2 = new WeightedLatLng(latlng, 50000.0);
        WeightedLatLng weightedLatLng = new WeightedLatLng(lat, 50000.0);
        ArrayList list = new ArrayList();

        list.add(0, weightedLatLng);
        list.add(1, weightedLatLng2);
        weightedLatLngListToHeatmap(list);

        return v;
    }

    // passes a list of weightedlatlng objects to the map and generates a heatmap based on it.
    public void weightedLatLngListToHeatmap(ArrayList list){

        HeatmapTileProvider.Builder mBuilder = new HeatmapTileProvider.Builder();
        mBuilder.radius(25);
        mBuilder.weightedData(list);
        HeatmapTileProvider mProvider = mBuilder.build();

        googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
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