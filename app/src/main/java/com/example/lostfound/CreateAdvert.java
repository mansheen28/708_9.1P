package com.example.lostfound;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AdvertActivity extends AppCompatActivity {

    private EditText nameInput, descriptionInput, phoneInput, dateInput;
    private Button saveButton, locationButton;
    private RadioButton radioLost, radioFound;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private static final int LOCATION_PERMISSION_REQUEST = 100;
    private String coordinates = "";
    private String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        nameInput = findViewById(R.id.nameInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        phoneInput = findViewById(R.id.phoneInput);
        dateInput = findViewById(R.id.dateInput);
        saveButton = findViewById(R.id.saveButton);
        locationButton = findViewById(R.id.locationButton);
        radioLost = findViewById(R.id.radioLost);
        radioFound = findViewById(R.id.radioFound);

        saveButton.setOnClickListener(v -> saveAdvert());

        locationButton.setOnClickListener(v -> requestLocationPermission());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initializePlaces();

        setupLocationCallback();
    }

    private void initializePlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "FYUfRyufyueduyfDYf$W$rtuihbnmB");
        }
        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    address = place.getName();
                    if (place.getLatLng() != null) {
                        coordinates = place.getLatLng().latitude + "|" + place.getLatLng().longitude;
                    }
                }

                @Override
                public void onError(Status status) {
                    Log.e("AdvertActivity", "An error occurred: " + status);
                }
            });
        }
    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    coordinates = latitude + "|" + longitude;

                    Geocoder geocoder = new Geocoder(AdvertActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            address = addresses.get(0).getAddressLine(0);
                            Toast.makeText(AdvertActivity.this, "Location saved!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        };
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        } else {
            fetchCurrentLocation();
        }
    }

    private void fetchCurrentLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCurrentLocation();
            } else {
                Log.w("AdvertActivity", "Location permission denied.");
            }
        }
    }

    private void saveAdvert() {
        String name = nameInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String date = dateInput.getText().toString();
        String type = radioLost.isChecked() ? "lost" : radioFound.isChecked() ? "found" : "";

        if (name.isEmpty() || description.isEmpty() || phone.isEmpty() || date.isEmpty() || address.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("phone", phone);
        values.put("date", date);
        values.put("type", type);
        values.put("location", address + "|" + coordinates);
        db.insert("adverts", null, values);
        Toast.makeText(this, "Advert saved successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
