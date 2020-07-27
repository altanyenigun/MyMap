package com.altanyenigun.mymaps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override   //harita hazır olduğunda
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                /*
                mMap.clear();
                LatLng userLocation= new LatLng(location.getLatitude(),location.getLongitude()); //kullanıcı konumu alma
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));

                 */

                /*
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {

                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if(addressList != null && addressList.size() > 0 ) {
                        System.out.println("Adres : " + addressList.get(0).toString());
                        System.out.println("Posta Kode : " + addressList.get(0).getPostalCode());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

            }


        };
        if(Build.VERSION.SDK_INT >= 23){
            //Kontrol et bir izin varmı                          Hangi izin ACCES_FINE_LOC          EŞİT DEĞİL İSE İZİN VAR
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                //olumsuz durum
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,50,locationListener); //Kullanıcının yerini al hangi süreyle , hangi mesafeyle
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                System.out.println("Last Location : " + lastLocation);
                LatLng userLastLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().title("Your Location").position(userLastLocation));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,15));
            }
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,50,locationListener); //Kullanıcının yerini al hangi süreyle , hangi mesafeyle

            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            System.out.println("Last Location : " + lastLocation);
            LatLng userLastLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().title("Your Location").position(userLastLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,15));
        }

        mMap.setOnMapLongClickListener(this);



        /*
        // Add a marker in Sydney and move the camera
        //LatLng = Enlem ve boylam LatLng bir yer belirten lokasyon obje

        // Enlem ve boylamla oynayarak haritayı haraket ettirme
        LatLng eiffel = new LatLng(48.8583736,2.2922873);
        mMap.addMarker(new MarkerOptions().position(eiffel).title("Marker in Eiffel"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(eiffel)); // Genis gösterim
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eiffel,15)); //zoomlu yakınlasmıs gösterim*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { // Kullanıcı ilk defa izin veriyorsa

      /*  if(grantResults.length > 0){
            if(requestCode == 1){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
            }*/

         if(requestCode == 1 && grantResults.length > 0 && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,50,locationListener);

         }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        String adres = "";
        try {

            List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if(addressList != null && addressList.size()> 0)
            {
                if(addressList.get(0).getThoroughfare() != null)
                {
                    adres += addressList.get(0).getThoroughfare();

                    if (addressList.get(0).getSubThoroughfare() != null)
                    {
                        adres += " / " + addressList.get(0).getSubThoroughfare();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(adres.matches(""))
        {
            adres="No Adress";
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title(adres));
    }
}
