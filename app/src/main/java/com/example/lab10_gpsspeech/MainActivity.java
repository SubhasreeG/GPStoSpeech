package com.example.lab10_gpsspeech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements  LocationListener{
    private static final int REQUEST_LOCATION = 1;
    Button Get_location,textspeech;
    TextView showlatitude, showlongitude;
    LocationManager locationManager;
    String Latitude, Longitude;
    TextToSpeech texttospeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        showlatitude = (TextView) findViewById(R.id.latitude);
        showlongitude = (TextView) findViewById(R.id.longitude);
        Get_location = findViewById(R.id.getLocation);
        textspeech=findViewById(R.id.text2speech);
        texttospeech= new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR)
                {
                    texttospeech.setLanguage(Locale.US);
                }
            }
        });
        textspeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Text="Your Location is Latitude:"+Latitude+" Degree North  Longitude"+Longitude +" Degree East";
                texttospeech.speak(Text,TextToSpeech.QUEUE_FLUSH,null);
                //texttospeech.speak("Longitude"+Longitude,TextToSpeech.QUEUE_FLUSH,null);
            }
        });
        Get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                }
            }
        });


    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, (LocationListener) this);
           // Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //if (locationGPS != null) {
             //   double lat = locationGPS.getLatitude();
             //   double longi = locationGPS.getLongitude();
             //   Latitude = String.valueOf(lat);
             //   Longitude = String.valueOf(longi);
             //   showlatitude.setText("Your Location: " + "\n" + "Latitude: " + Latitude);
              //  showlongitude.setText("Longitude :" + Longitude);
           // } else {
            //    Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            //}
        }
    }
    @Override
    public void onLocationChanged(Location location)
    {
        double lat = location.getLatitude();
        double longi = location.getLongitude();
        Latitude = String.valueOf(lat);
        Longitude = String.valueOf(longi);
        showlatitude.setText("Your Location: " + "\n" + "Latitude: " + Latitude+"deg N");
        showlongitude.setText("Longitude :" + Longitude + "deg E");
    }
}