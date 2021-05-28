package com.example.fhir_device;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddDeviceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String PREF_KEY = AddDeviceActivity.class.getPackage().toString(); // Shared preferencies-hez

    /**
     * GUI elemek
     */
    EditText deviceNameET;
    EditText typeET;
    EditText manufacturerNameET;
    Button manufacturerDateButton;
    EditText serialNumberET;
    Spinner statusSpinner;
    String selectedStatus;

    /**
     * Firebase és Firestore
     */
    private FirebaseFirestore firestore;
    private CollectionReference items;

    private DatePickerDialog datePickerDialog; // Dátum választó
    private SharedPreferences preferences; // SharedPreferencies, input mezők eltárolására

    private NotificationHandler notificationHandler; // Értesítés küldése új eszköz hozzáadásakor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        /**
         * IntentExtra ellenőrzése
         */
        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);
        int SECRET_KEY = Integer.MAX_VALUE;
        if (secret_key != SECRET_KEY) {
            finish();
        }

        firestore = FirebaseFirestore.getInstance();
        items = firestore.collection("devices");

        getInputFields(); // GUI elemek lekérése
        fillInputFields(); // Shared Preferencies-ből kapott adatokkal feltöltés
        initDatePicker(); // Dátum dialógus ablak beállítása
        manufacturerDateButton.setText("Date when the device was made");
        // NotificationHandler inicalizálása
        notificationHandler = new NotificationHandler(this);
    }

    /**
     * Új eszköz hozzáadásakor lehetőség van gyártási dátumot kiválasztani
     * Ennek a dialógus ablaknak a megfelelő inicializálása történik ebben a metódusban
     */
    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                manufacturerDateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    /**
     * Form mezők lekérése, hogy később hivatkozni lehessen rájuk
     */
    public void getInputFields() {
        deviceNameET = findViewById(R.id.name_device);
        typeET = findViewById(R.id.type);
        manufacturerNameET = findViewById(R.id.manufacturer_name);
        manufacturerDateButton = findViewById(R.id.manufacturer_date);
        serialNumberET = findViewById(R.id.serial_number);
        statusSpinner = findViewById(R.id.status_spinner);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
    }

    /**
     * Shared Preferencies-be elmentem azokat az értékeket, amelyeket a user beírt, de nem submittolt.
     * Így visszatérve ott folytathatja ahol abbahagyta. Ha submitra kattint akkor default értékre visszaállnak
     * a preferencies-ben tárolt értékek
     */
    public void fillInputFields() {
        deviceNameET.setText(preferences.getString("name_device", ""));
        typeET.setText(preferences.getString("type", ""));
        manufacturerNameET.setText(preferences.getString("manufacturer_name", ""));
        manufacturerDateButton.setText(preferences.getString("manufacturer_date", ""));
        serialNumberET.setText(preferences.getString("serial_number", ""));
        statusSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.statuses, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
    }

    /**
     * Form mező értékek ürítése
     */
    public void clearInputFields() {
        deviceNameET.setText("");
        typeET.setText("");
        manufacturerNameET.setText("");
        manufacturerDateButton.setText("");
        serialNumberET.setText("");
    }

    /**
     * Back gombra kattintva a kezdőlapra irányítja a metódus a felhasználót
     *
     * @param view
     */
    public void backToHomePage(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Új eszköz létrehozása a Device osztály alapján, amely a FHIR-szabvány által lett definiálva.
     * A metódus bekéri a form-ban megadott adatokat, majd ezekből feldolgozás után egy új eszközt hoz létre,
     * melyet FireStore adatbázisában eltárol.
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addNewDevice(View view) {
        String deviceName = deviceNameET.getText().toString();
        String type = typeET.getText().toString();
        String manufacturerName = manufacturerNameET.getText().toString();
        String manufacturerDate = manufacturerDateButton.getText().toString();
        String serialNumber = serialNumberET.getText().toString();
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(manufacturerDate);
            Device d = new Device(
                    selectedStatus,
                    manufacturerName,
                    date,
                    serialNumber,
                    deviceName,
                    type
            );
            items.add(d);
        } catch (Exception e) {
            /**
             * Amennyiben nincs kiválasztva a dátum ne haljon el, hanem legyen a mai dátum
             */
            items.add(new Device(selectedStatus, manufacturerName, new Date(), serialNumber, deviceName, type));
        }
        clearInputFields();
        notificationHandler.send("Device " + deviceName + " was successfully added to the database.");
        Intent intent = new Intent(this, DevicesActivity.class);
        intent.putExtra("SECRET_KEY", Integer.MAX_VALUE);
        startActivity(intent);
    }

    /**
     * Fejlécben található menü itemek lekérése
     * Ha ez lefut akkor azt jelenti, hogy a felhasználó az "add" menüpontra klikkelt, ekkor az ikon színét
     * megváltoztatja cián színre
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setIcon(R.drawable.ic_add_selected);
        return true;
    }

    /**
     * Ha a user valamelyik menüpontra kattint, akkor az ahhoz tartozó Activity kerül megnyitásra
     *
     * @param item: Menü elem
     * @return
     */
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

    /**
     * Új státusz kiválasztása
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedStatus = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    /**
     * Ez a metódus menti el a Shared Preferencies-be a formban kitöltött értékeket, hogy legközelebbi megnyitáskor
     * ezek automatikusan megjelenjenek a usernek és folytatni tudja ott ahol abbahagyta.
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name_device", deviceNameET.getText().toString());
        editor.putString("type", typeET.getText().toString());
        editor.putString("manufacturer_name", manufacturerNameET.getText().toString());
        editor.putString("manufacturer_date", manufacturerDateButton.getText().toString());
        editor.putString("serial_number", serialNumberET.getText().toString());
        editor.apply();
    }

    /**
     * Gombra kattintva megjelenik a beállított dialógus ablak, amelyen dátumot lehet választani
     * @param view
     */
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}
