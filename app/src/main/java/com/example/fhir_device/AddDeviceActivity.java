package com.example.fhir_device;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.time.LocalDate;
import java.util.Date;

public class AddDeviceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String PREF_KEY = AddDeviceActivity.class.getPackage().toString();
    private static final String LOG_TAG = AddDeviceActivity.class.getName();

    EditText deviceNameET;
    EditText typeET;
    EditText manufacturerNameET;
    EditText manufacturerDateET;
    EditText serialNumberET;
    Spinner statusSpinner;
    String selectedStatus;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);
        int SECRET_KEY = Integer.MAX_VALUE;
        if (secret_key != SECRET_KEY) {
            finish();
        }
        getInputFields();
        fillInputFields();
    }

    public void getInputFields(){
        deviceNameET = findViewById(R.id.name_device);
        typeET = findViewById(R.id.type);
        manufacturerNameET = findViewById(R.id.manufacturer_name);
        manufacturerDateET = findViewById(R.id.manufacturer_date);
        serialNumberET = findViewById(R.id.serial_number);
        statusSpinner = findViewById(R.id.status_spinner);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
    }

    public void fillInputFields(){
        deviceNameET.setText(preferences.getString("name_device", ""));
        typeET.setText(preferences.getString("type", ""));
        manufacturerNameET.setText(preferences.getString("manufacturer_name", ""));
        manufacturerDateET.setText(preferences.getString("manufacturer_date", ""));
        serialNumberET.setText(preferences.getString("serial_number", ""));
        statusSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.statuses, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
    }

    public void backToHomePage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addNewDevice(View view) {
        String deviceName = deviceNameET.getText().toString();
        String type = typeET.getText().toString();
        String manufacturerName = manufacturerNameET.getText().toString();
        String manufacturerDate = manufacturerDateET.getText().toString();
        String serialNumber = serialNumberET.getText().toString();
        try {
            // TODO: dátum megfelelő lekezelése és átalakítása
            Device d = new Device(selectedStatus, manufacturerName, LocalDate.of(2000, 1, 1), serialNumber, deviceName, type);
            Log.d(LOG_TAG, d.toString());
        } catch (Exception e){
            Log.d(LOG_TAG, e.getMessage());
        }
        // TODO: Firebase and firestore configuration
        // TODO: Submitkor töröld a preferenceseket
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setIcon(R.drawable.ic_add_selected);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.main:
                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.listing:
                intent = new Intent(this, DevicesActivity.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        intent.putExtra("SECRET_KEY", Integer.MAX_VALUE);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedStatus = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name_device", deviceNameET.getText().toString());
        editor.putString("type", typeET.getText().toString());
        editor.putString("manufacturer_name", manufacturerNameET.getText().toString());
        editor.putString("manufacturer_date", manufacturerDateET.getText().toString());
        editor.putString("serial_number", serialNumberET.getText().toString());
        editor.apply();
    }
}
