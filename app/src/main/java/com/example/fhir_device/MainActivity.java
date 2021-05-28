package com.example.fhir_device;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Gombra kattintás következtében megnyitásra kerül a telefonon található böngőszőn Madarász Máté github oldala
     *
     * @param view
     */
    public void openWebsite(View view) {
        String url = "https://github.com/xMediMadix";
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.w(LOG_TAG, "Hiba az weboldal megnyitásakor");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.main);
        menuItem.setIcon(R.drawable.ic_home_selected);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.listing:
                intent = new Intent(this, DevicesActivity.class);
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
