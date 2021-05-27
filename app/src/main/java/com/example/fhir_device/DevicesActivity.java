package com.example.fhir_device;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.time.LocalDate;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.Date;

public class DevicesActivity extends AppCompatActivity {
    private static final String LOG_TAG = DevicesActivity.class.getName();

    private RecyclerView recyclerView;
    private ArrayList<Device> itemList;
    private DeviceItemAdapter adapter;

    private FirebaseFirestore firestore;
    private CollectionReference items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);
        int SECRET_KEY = Integer.MAX_VALUE;
        if (secret_key != SECRET_KEY) {
            finish();
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        itemList = new ArrayList<>();

        adapter = new DeviceItemAdapter(this, itemList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        items = firestore.collection("devices");

        initializeCards();
    }

    /**
     * Firestoreból eszközök lekérése
     */
    private void initializeCards() {
        itemList.clear();

        items.orderBy("deviceName", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                Device device = d.toObject(Device.class);
                device.setId(d.getId());
                itemList.add(device);
            }
            adapter.notifyDataSetChanged();
        });
    }

    public void deleteDevice(Device d) {
        DocumentReference ref = items.document(d._getId());

        ref.delete().addOnSuccessListener(success -> {
            Toast.makeText(this, "Device " + d.getDeviceName() + " was deleted.", Toast.LENGTH_LONG).show();
            initializeCards();
        })
                .addOnFailureListener(failure -> {
                    Toast.makeText(this, "Device " + d._getId() + " cannot be deleted.", Toast.LENGTH_LONG).show();
        });
    }

    private void updateDevice(Device d) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.listing);
        menuItem.setIcon(R.drawable.ic_list_selected);
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
            case R.id.add:
                intent = new Intent(this, AddDeviceActivity.class);
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
}
