package com.climo.al.climoproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.climo.al.climoproject.Activities.MainActivity;
import com.climo.al.climoproject.Models.Postingan;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.lang.*;

public class PostAjakan extends AppCompatActivity implements OnMapReadyCallback {
    private static final int MAX_LENGTH = 1385;

    private GoogleMap mMap;

    private String API_KEY = "AIzaSyB2xpxxrkm_0nk4YLnDQu8jcY-dPuALuuk";

    public LatLng pickUpLatLng = null;
    public LatLng locationLatLng = null;

    public static final int PICK_UP = 0;
    public static final int DEST_LOC = 1;
    private static int REQUEST_CODE = 0;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private TextView tvPickUpFrom, tvDestLocation;
    private TextView tvPickUpAddr, tvPickUpLatLng, tvPickUpName;
   private ImageView imgPost;
   private EditText newPostDesc;
   private EditText newPostDate;
   private Spinner spGunung;
   private Button btnPost;

   private DatabaseReference database;
   private StorageReference storageReference;
   private FirebaseFirestore firebaseFirestore;
    FirebaseUser currentUSer;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ajakan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUSer = mAuth.getCurrentUser();
        getSupportActionBar().setTitle("Post");

        newPostDesc = findViewById(R.id.deskripsi);
        newPostDate = findViewById(R.id.txt_tgl);
        spGunung = findViewById(R.id.sp_gunung_id);

        btnPost = findViewById(R.id.btnPost);

        // Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        wigetInit();

        tvPickUpFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Jalankan Method untuk menampilkan Place Auto Complete
                // Bawa constant PICK_UP
                showPlaceAutoComplete(PICK_UP);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(spGunung.getSelectedItem().toString()) && !isEmpty(newPostDesc.getContext().toString()) && !isEmpty(tvPickUpFrom.getText().toString()) && !isEmpty(newPostDesc.getText().toString()))
                    //submitPostingan(new formpostingan(spinner.getSelectedItem().toString(), tvDateResult.getContext().toString(), tvPickUpFrom.getText().toString(), deskripsi.getText().toString()));
                    submitPostingan();

                else {
                    Toast errorToast = Toast.makeText(PostAjakan.this, "Error, pls chech your internet connection and try again!", Toast.LENGTH_SHORT);
                    errorToast.show();
                }
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        spGunung.getWindowToken(), 0);

            }
        });



    }

    private void submitPostingan() {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        // mengambil referensi ke Firebase Database
        database = FirebaseDatabase.getInstance().getReference();
        String Desc = newPostDesc.getText().toString();
        String tgl = newPostDate.getText().toString();
        String Gunung = spGunung.getSelectedItem().toString();
        String meepo = tvPickUpFrom.getText().toString().trim();
        double lat = pickUpLatLng.latitude;
        String lat1 = new Double(lat).toString();
        double lon = pickUpLatLng.longitude;
        String lon1 = new Double(lon).toString();
        String uname = currentFirebaseUser.getDisplayName();


        database.child("postingansaya").child(currentFirebaseUser.getUid()).push().setValue(new Postingan(Gunung, tgl, meepo, lat1, lon1, Desc, uname)).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                spGunung.setSelected(true);
                newPostDate.setText("");
                tvPickUpFrom.setText("");
                //tvPickUpLatLng.setText(lat1 + "," + lon1);
                newPostDesc.setText("");

            }
        });

        database.child("postingan").push().setValue(new Postingan(Gunung, tgl, meepo, lat1, lon1, Desc, uname)).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                spGunung.setSelected(true);
                newPostDate.setText("");
                tvPickUpFrom.setText("");
                //tvPickUpLatLng.setText(lat1 + "," + lon1);
                newPostDesc.setText("");

                Toast.makeText(PostAjakan.this ,"Post has been added",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PostAjakan.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void showDateDialog(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                newPostDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    // Method untuk Inisilisasi Widget agar lebih rapih
    private void wigetInit() {

        // Widget
        tvPickUpFrom = findViewById(R.id.tvPickUpFrom);
        tvPickUpAddr = findViewById(R.id.tvPickUpAddr);
        tvPickUpLatLng = findViewById(R.id.tvPickUpLatLng);
        tvPickUpName = findViewById(R.id.tvPickUpName);

    }

    private void showPlaceAutoComplete(int typeLocation) {
        // isi RESUT_CODE tergantung tipe lokasi yg dipilih.
        // titik jmput atau tujuan
        REQUEST_CODE = typeLocation;
        // Filter hanya tmpat yg ada di Indonesia
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("ID").build();
        try {
            // Intent untuk mengirim Implisit Intent
            Intent mIntent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter)
                    .build(this);
            // jalankan intent impilist
            startActivityForResult(mIntent, REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace(); // cetak error
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace(); // cetak error
            // Display Toast
            Toast.makeText(this, "Layanan Play Services Tidak Tersedia", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, "Sini Gaes", Toast.LENGTH_SHORT).show();
        // Pastikan Resultnya OK
        if (resultCode == RESULT_OK){
            //Toast.makeText(this, "Sini Gaes2", Toast.LENGTH_SHORT).show();
            // Tampung Data tempat ke variable
            Place placeData = PlaceAutocomplete.getPlace(this, data);
            if (placeData.isDataValid()){
                // Show in Log Cat
                Log.d("autoCompletePlace Data", placeData.toString());
                // Dapatkan Detail
                String placeAddress = placeData.getAddress().toString();
                LatLng placeLatLng = placeData.getLatLng();
                String placeName = placeData.getName().toString();
                // Cek user milih titik jemput atau titik tujuan
                switch (REQUEST_CODE){
                    case PICK_UP:
                        // Set ke widget lokasi asal
                        tvPickUpFrom.setText(placeAddress);
                        tvPickUpAddr.setText("Location Address : " + placeAddress);
                        tvPickUpLatLng.setText("LatLang : " + placeLatLng.toString());
                        tvPickUpName.setText("Place Name : " + placeName);
                        pickUpLatLng = placeData.getLatLng();
                        break;
                }
                if (pickUpLatLng != null) {
                    actionRoute(placeLatLng, REQUEST_CODE);
                }
            } else {
                // Data tempat tidak valid
                Toast.makeText(this, "Invalid Place !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void actionRoute(LatLng placeLatLng, int requestCode) {

        mMap.addMarker(new MarkerOptions().position(new LatLng(placeLatLng.latitude, placeLatLng.longitude))
                .title("Meeting Point"));


        /** START
         * Logic untuk membuat layar berada ditengah2 dua koordinat
         */

        LatLngBounds.Builder latLongBuilder = new LatLngBounds.Builder();
        latLongBuilder.include(pickUpLatLng);


        // Bounds Coordinata
        LatLngBounds bounds = latLongBuilder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int paddingMap = (int) (width * 0.2); //jarak dari
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, paddingMap);
        mMap.animateCamera(cu);

        /** END
         * Logic untuk membuat layar berada ditengah2 dua koordinat
         */
        mMap.setPadding(10, 180, 10, 180);
        //mMap.clear();
    }

    private boolean isEmpty(String s) {
        // Cek apakah ada fields yang kosong, sebelum disubmit
        return TextUtils.isEmpty(s);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public void Tanggal(View view) {
        showDateDialog();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(10, 180, 10, 10);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);

        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.getUiSettings().setRotateGesturesEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(true);

            }
        }
    }
    }
