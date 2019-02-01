package com.climo.al.climoproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.climo.al.climoproject.Models.Postingan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.text.SimpleDateFormat;
import java.lang.*;

import com.climo.al.climoproject.Activities.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.storage.StorageReference;

public class DetailPost extends AppCompatActivity implements OnMapReadyCallback {

    View view;
    private RecyclerView blogListView2;
    private List<Postingan> blogList2;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerViewAdapterDetailPost recyclerViewAdapter;
    private FloatingActionButton fab;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PostingSaya.OnFragmentInteractionListener mListener;

    private DocumentReference docs;
    private FieldPath field;
    private CollectionReference colls;
    private FieldValue fieldValue;

    private static final int MAX_LENGTH = 1385;

    private GoogleMap mMap;

    private String API_KEY = "AIzaSyB2xpxxrkm_0nk4YLnDQu8jcY-dPuALuuk";

    private LatLng pickUpLatLng = null;
    public LatLng locationLatLng = null;

    public static final int PICK_UP = 0;
    public static final int DEST_LOC = 1;
    private static int REQUEST_CODE = 0;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private TextView tvPickUpFrom2, tvDestLocation;
    private TextView tvPickUpAddr, tvPickUpLatLng, tvPickUpName;
    private ImageView imgPost;
    private TextView newPostDesc, latitude, longitude;
    private TextView newPostDate;
    private TextView spGunung;
    private Button btnPost;
    private Double lat1, lon1;


    private StorageReference storageReference;
    FirebaseUser currentUSer;
    FirebaseAuth mAuth;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Post");


        newPostDesc = findViewById(R.id.deskripsi2);
        newPostDate = findViewById(R.id.txt_tgl2);
        spGunung = findViewById(R.id.sp_gunung_id2);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        // Widget
        tvPickUpFrom2 = findViewById(R.id.tvPickUpFrom2);
        tvPickUpAddr = findViewById(R.id.tvPickUpAddr);
        tvPickUpLatLng = findViewById(R.id.tvPickUpLatLng);
        tvPickUpName = findViewById(R.id.tvPickUpName);

        // Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //reviece
        Intent intent = getIntent();
        String title = intent.getStringExtra("Title");
        String tgl = intent.getStringExtra("Tgl");
        String meepo = intent.getStringExtra("meepo");
        String lat = intent.getStringExtra("latitude");
        String lon = intent.getStringExtra("longitude");
        String deskrispsi = intent.getStringExtra("Deskripsi");

        spGunung.setText(title);
        newPostDate.setText(tgl);
        tvPickUpFrom2.setText(meepo);
        newPostDesc.setText(deskrispsi);
        //latitude.setText(lat);
        //longitude.setText(lon);

        //deklarasi konversi string ke double
        lat1 = Double.valueOf(lat).doubleValue();
        lon1 = Double.valueOf(lon).doubleValue();


        //field = FieldPath.of("userID");
        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }



    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

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

        LatLng sydney = new LatLng(lat1, lon1);
        mMap.addMarker(new MarkerOptions().position(sydney)
                .title("Meeting Point"));

        LatLngBounds.Builder latLongBuilder = new LatLngBounds.Builder();
        latLongBuilder.include(sydney);
        //latLongBuilder.include(locationLatLng);

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
    }
}
