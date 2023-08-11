package com.example.myapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapptest.Controllers.GPS_Data_Helper;
import com.example.myapptest.R;
import com.example.myapptest.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Maps_Activity extends AppCompatActivity implements OnMapReadyCallback {

    // Declaration of the Variables
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private float zoom = 10f;
    private Marker trackerMarker;

    private DatabaseReference databaseReference;

    private List<GPS_Data_Helper> gps_DataHelper_location;
    private LatLng mostRecentLocation;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    // Refresh the Marker status in the maps activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(Maps_Activity.this, Homepage_Activity.class));
            return true;
        }
        if (item.getItemId() == R.id.action_refresh) {
            // Refresh the marker manually
            updateMarker();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to update the marker position manually
    private void updateMarker() {
        if (mostRecentLocation != null) {
            // Update the marker's position to the most recent location
            if (trackerMarker != null) {
                trackerMarker.setPosition(mostRecentLocation);
            } else {
                // If the marker doesn't exist, create it at the most recent location
                trackerMarker = mMap.addMarker(new MarkerOptions().position(mostRecentLocation).title("MediKit"));
            }

            // Move the camera to the most recent location with the specified zoom level
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mostRecentLocation, zoom));
        } else {
            Toast.makeText(this, "No recent location data available.", Toast.LENGTH_SHORT).show();
        }
    }


    // Get the variables from views
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar mapToolbar = findViewById(R.id.mapToolbar);
        setSupportActionBar(mapToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Notify the user when the SupportMapFragrment is available.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapMap);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    // Inside onMapReady method

    // On Map Ready function that retrieves the data from the RTDB and updates the maps activity accordingly
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Get a reference to the "gps_location" node in Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("gps_location");

        // Add a listener to retrieve the data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear existing markers from the map
                mMap.clear();

                // Check if the "latitude" and "longitude" nodes exist on the RTDB. If yes then retrieve the saved data, if not therefore no location available toast is printed.
                if (dataSnapshot.child("latitude").exists() && dataSnapshot.child("longitude").exists()) {
                    // Get the latitude and longitude values from the "gps_location" node
                    double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                    double longitude = dataSnapshot.child("longitude").getValue(Double.class);

                    // Create a LatLng object from the latitude and longitude
                    mostRecentLocation = new LatLng(latitude, longitude);

                    // Add a marker to the map
                    mMap.addMarker(new MarkerOptions().position(mostRecentLocation).title("MediKit")); // You can customize the title if needed

                    // Move the camera to the most recent location with the specified zoom level
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mostRecentLocation, zoom));
                } else {
                    // Handle the case where latitude or longitude data is missing
                    Toast.makeText(Maps_Activity.this, "No location data available.", Toast.LENGTH_SHORT).show();
                }
            }

            // On cancel function if unable to retrieve data from firebase RTDB.
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database read error if needed
                Toast.makeText(Maps_Activity.this, "Error fetching data from Firebase", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });
    }

}

