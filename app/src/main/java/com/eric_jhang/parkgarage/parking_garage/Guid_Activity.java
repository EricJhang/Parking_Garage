package com.eric_jhang.parkgarage.parking_garage;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class Guid_Activity extends AppCompatActivity {

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private MapFragment mMapFragment;
    public String provider = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);
        //取得map元件
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragmentmap)).getMap();
        mMap.setMyLocationEnabled(true);

        //設定map地圖類型
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        locationServiceInitial();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void locationServiceInitial() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);    //取得系統定位服務

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);    //使用GPS定位座標
        Criteria criteria = new Criteria();
        //String provider = mLocationManager.getBestProvider(criteria, true);
        provider = LocationManager.GPS_PROVIDER;
        mLocationManager.addGpsStatusListener(gpsListener);
        mLocationManager.getLastKnownLocation(provider);

        int minTime = 5000;//ms
        int minDist = 5;//meter
        mLocationManager.requestLocationUpdates(provider, minTime, minDist, locationListener );
        //getLocation(location);
        //經度
        double lng = location.getLongitude();
        //緯度
        double lat = location.getLatitude();
      //  cameraFocusOnMe(lat, lng);
        CameraUpdate center =
                CameraUpdateFactory.newLatLngZoom(new LatLng(lng, lat), 15);
        mMap.animateCamera(center);
        //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.047924, 121.517081), 15.0f));
    }
    LocationListener locationListener = new LocationListener(){
        @Override
        public void onLocationChanged(Location location) {
            //updateWithNewLocation(location);
            //經度
            double lng = location.getLongitude();
            //緯度
            double lat = location.getLatitude();
            cameraFocusOnMe(lat, lng);
        }

        @Override
        public void onProviderDisabled(String provider) {
           // updateWithNewLocation(null);
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
           switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                   // Log.v(TAG, "Status Changed: Out of Service");
                    Toast.makeText(Guid_Activity.this, "Status Changed: Out of Service", Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                  //  Log.v(TAG, "Status Changed: Temporarily Unavailable");
                    Toast.makeText(Guid_Activity.this, "Status Changed: Temporarily Unavailable", Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.AVAILABLE:
                   // Log.v(TAG, "Status Changed: Available");
                    Toast.makeText(Guid_Activity.this, "Status Changed: Available", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };
    private void cameraFocusOnMe(double lat, double lng) {
        CameraPosition camPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(16)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));

    }
    GpsStatus.Listener gpsListener = new GpsStatus.Listener() {

        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_STARTED:
                   // Log.d(TAG, "GPS_EVENT_STARTED");
                    Toast.makeText(Guid_Activity.this, "GPS_EVENT_STARTED", Toast.LENGTH_SHORT).show();
                    break;

                case GpsStatus.GPS_EVENT_STOPPED:
                   // Log.d(TAG, "GPS_EVENT_STOPPED");
                    Toast.makeText(Guid_Activity.this, "GPS_EVENT_STOPPED", Toast.LENGTH_SHORT).show();
                    break;

                case GpsStatus.GPS_EVENT_FIRST_FIX:
                  //  Log.d(TAG, "GPS_EVENT_FIRST_FIX");
                    Toast.makeText(Guid_Activity.this, "GPS_EVENT_FIRST_FIX", Toast.LENGTH_SHORT).show();
                    break;

                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                  //  Log.d(TAG, "GPS_EVENT_SATELLITE_STATUS");
                    break;
            }
        }
    };

}
