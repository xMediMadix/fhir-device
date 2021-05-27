package com.example.fhir_device;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class EditDeviceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner statusSpinner;
    String selectedStatus;

    TextView nameDeviceTV;
    TextView typeTV;
    TextView nameManufacturerTV;
    TextView serialNumberTV;

    private FirebaseFirestore firestore;
    private CollectionReference items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_device);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);
        int SECRET_KEY = Integer.MAX_VALUE;
        if (secret_key != SECRET_KEY) {
            finish();
        }

        firestore = FirebaseFirestore.getInstance();
        items = firestore.collection("devices");

        getViews();
        fillViews();
    }

    public void getViews() {
        nameDeviceTV = findViewById(R.id.name_device_edit);
        typeTV = findViewById(R.id.type_edit);
        nameManufacturerTV = findViewById(R.id.manufacturer_name_edit);
        serialNumberTV = findViewById(R.id.serial_number_edit);
        statusSpinner = findViewById(R.id.status_spinner_edit);
    }

    public void fillViews() {
        String id = getIntent().getStringExtra("ID");
        items.document(id).get().addOnSuccessListener(
            queryDocumentSnapshots -> {
                Device device = queryDocumentSnapshots.toObject(Device.class);
                device.setId(queryDocumentSnapshots.getId());
                nameDeviceTV.setText(device.getDeviceName());
                typeTV.setText(device.getType().getText());
                nameManufacturerTV.setText(device.getManufacturer());
                serialNumberTV.setText(device.getSerialNumber());
            }
        );

        statusSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.statuses, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
    }

    public void backToHomePage(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void updateDevice(View view) {
        String id = getIntent().getStringExtra("ID");
        items.document(id).update("status", selectedStatus)
                .addOnSuccessListener(success -> {
                    Toast.makeText(this, "Device was successfully updated.", Toast.LENGTH_LONG).show();
        });
        Intent intent = new Intent(this, DevicesActivity.class);
        intent.putExtra("SECRET_KEY", Integer.MAX_VALUE);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedStatus = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
