package com.example.fhir_device;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class DevicesActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
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
                startActivity(intent);
                return true;
            case R.id.listing:
                intent = new Intent(this, DevicesActivity.class);
                startActivity(intent);
                return true;
            case R.id.add:
                intent = new Intent(this, AddDeviceActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}
