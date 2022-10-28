package com.prototype_1.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            if(grantResults.length>0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView longitude=(TextView)findViewById(R.id.longitude);
        TextView latitude=(TextView)findViewById(R.id.latitude);

        locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation!=null)
            {
                updateLocationInfo(lastKnownLocation);
            }
        }
    }
    @SuppressLint("SetTextI18n")
    public  void updateLocationInfo(Location location)
    {
        TextView lat=(TextView)findViewById(R.id.latitude);
        TextView longi=(TextView)findViewById(R.id.longitude);
        TextView alt=(TextView)findViewById(R.id.altitude);
        TextView accu=(TextView)findViewById(R.id.accuracy);
        TextView addre=(TextView)findViewById(R.id.address);

        lat.setText("Latitude: "+ location.getLatitude());
        longi.setText("Longitude: "+ location.getLongitude());
        alt.setText("Altitude: "+ location.getAltitude());
        accu.setText("Accuracy "+ location.getAccuracy());

        String address="Couldn't find address :(";
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());

        try {
            List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            Log.i("List Address!!!!",listAddress.get(0).toString());
            if(listAddress!=null && listAddress.size()>0)
            {
                address="Address :\n";
                if(listAddress.get(0).getAddressLine(0)!=null)
                {
                    address+=listAddress.get(0).getAddressLine(0)+"\n";
                }
                if(listAddress.get(0).getLocality()!=null)
                {
                    address+=listAddress.get(0).getLocality()+"\n";
                }
                if (listAddress.get(0).getPostalCode()!=null)
                {
                    address+=listAddress.get(0).getPostalCode()+"\n";
                }
                if(listAddress.get(0).getAdminArea()!=null)
                {
                    address+=listAddress.get(0).getAdminArea();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        addre.setText(address);
    }
}