package com.example.myapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Homepage_Activity extends AppCompatActivity implements View.OnClickListener {

    // Declaration of the Variables
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
                        // On click technical support Activity
                        if (itemId == R.id.nav_support) {
                            Intent intent = new Intent(getApplicationContext(), Technical_Support_Activity.class);
                            startActivity(intent);
                        }
                        // On click logout Activity
                        else if (itemId == R.id.nav_logout) {
                            Intent intent = new Intent (getApplicationContext(), Logout_Activity.class);
                            startActivity(intent);
                        // On click Legal information Activity
                        } else if (itemId == R.id.nav_legal) {
                            Intent intent = new Intent (getApplicationContext(), Legal_Information_Activity.class);
                            startActivity(intent);
                        // On click important notice Activity
                        } else if (itemId == R.id.nav_important_notice) {
                            Intent intent = new Intent (getApplicationContext(), Important_Notice_Activity.class);
                            startActivity(intent);
                        }
                        // Close the drawer after handling the click
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });

        // Fetch the user's details
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();

            // Get the NavigationView's header
            View header = navigationView.getHeaderView(0);
            TextView nameTextView = header.findViewById(R.id.user_name);
            TextView emailTextView = header.findViewById(R.id.email);

            // Set the TextViews to the user's name and email
            nameTextView.setText(name);
            emailTextView.setText(email);
        }

        //  Get references from views (XML) for each Image Button that was defined
        ImageButton addMedicationButton = findViewById(R.id.btn_add_new_medication);
        ImageButton savedMedicationsButton = findViewById(R.id.btn_saved_medication);
        ImageButton medicationCommentsButton = findViewById(R.id.btn_medication_notes);
        ImageButton medicationlocator = findViewById(R.id.btn_medication_locator);

        addMedicationButton.setOnClickListener(this);
        savedMedicationsButton.setOnClickListener(this);
        medicationCommentsButton.setOnClickListener(this);
        medicationlocator.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_add_new_medication) { // Handle "Add New Medication" button click
            Intent addMedicationIntent = new Intent(this, Add_Medication_Activity.class);
            startActivity(addMedicationIntent);
        } else if (id == R.id.btn_saved_medication) { // Handle "List of Saved Medications" button click
            Intent savedMedicationsIntent = new Intent(this, Saved_Medication_Activity.class);
            startActivity(savedMedicationsIntent);
        } else if (id == R.id.btn_medication_notes) { // Handle "Comments About Medications" button click
            Intent medicationCommentsIntent = new Intent(this, Medication_Notes_Activity.class);
            startActivity(medicationCommentsIntent);
        } else if (id == R.id.btn_medication_locator) { // Handle "Medication Locator" button click
            Intent accessibilityIntent = new Intent(this, Maps_Activity.class);
            startActivity(accessibilityIntent);
        }
    }
}

