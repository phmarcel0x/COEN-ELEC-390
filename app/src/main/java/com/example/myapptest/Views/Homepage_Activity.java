package com.example.myapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapptest.R;
import com.google.android.material.navigation.NavigationView;

public class Homepage_Activity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Set up the NavigationView
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // Handle menu item clicks
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.nav_support){
                            Intent intent = new Intent(getApplicationContext(), Technical_Support_Activity.class);
                            startActivity(intent);
                        }
                        else if (itemId == R.id.nav_logout){
                            Intent intent = new Intent (getApplicationContext(), Logout_Activity.class);
                            startActivity(intent);
                        } else if (itemId == R.id.nav_legal) {
                            Intent intent = new Intent (getApplicationContext(), Legal_Information_Activity.class);
                            startActivity(intent);
                        }
                        // Close the drawer after handling the click
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
        ImageButton addMedicationButton = findViewById(R.id.btn_add_new_medication);
        ImageButton savedMedicationsButton = findViewById(R.id.btn_saved_medication);
        ImageButton medicationCommentsButton = findViewById(R.id.btn_medication_notes);
        ImageButton medicationlocator = findViewById(R.id.btn_medication_locator);
        Button settingsButton = findViewById(R.id.btn_settings);
//        TextView text_add_medication = findViewById(R.id.text_add_med);
//        TextView text_saved_medication = findViewById(R.id.text_list);
//        TextView text_notes = findViewById(R.id.text_medication_notes);
//        TextView text_med_locator = findViewById(R.id.text_medication_locator);

        addMedicationButton.setOnClickListener(this);
        savedMedicationsButton.setOnClickListener(this);
        medicationCommentsButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        medicationlocator.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_add_new_medication) { // Handle "Add New Medication" button click
            Intent addMedicationIntent = new Intent(this, AddMedicationActivity.class);
            startActivity(addMedicationIntent);
        } else if (id == R.id.btn_saved_medication) { // Handle "List of Saved Medications" button click
            Intent savedMedicationsIntent = new Intent(this, SavedMedicationActivity.class);
            startActivity(savedMedicationsIntent);
        } else if (id == R.id.btn_medication_notes) { // Handle "Comments About Medications" button click
            Intent medicationCommentsIntent = new Intent(this, MedicationNotesActivity.class);
            startActivity(medicationCommentsIntent);
        } else if (id == R.id.btn_medication_locator) { // Handle "Medication Locator" button click
            Intent accessibilityIntent = new Intent(this, MapsActivity.class);
            startActivity(accessibilityIntent);
        }
        else if (id == R.id.btn_settings) { // Handle "Settings" button click
            Intent accessibilityIntent = new Intent(this, Settings_Activity.class);
            startActivity(accessibilityIntent);
        }
    }
}
