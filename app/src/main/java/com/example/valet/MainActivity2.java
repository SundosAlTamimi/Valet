package com.example.valet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity2 extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener , GoogleApiClient.OnConnectionFailedListener {

    private NavigationView nDrawerLayout;
    private ActionBarDrawerToggle nToggle;
    int isAvilable = 0;
    String barcodeValue;

    ProgressDialog progressDialog;
    String latitude , longitude;
    Captains captain;

    private GoogleMap mMap;
    Button scan;
    public static List<LatLng> LatLngListMarker;
    private LatLngBounds.Builder builder;
    LatLngBounds bounds;
    boolean flag = false;
    Timer timer;
    double v1 = 31.969570, v2 = 35.914191;
    double a1 = 31.968420, a2 = 35.916258;

    LatLng reqLatLong;

    DBHandler dbHandler;

    public static String[] parts ;


    private Map<Marker, Map<String, Object>> markers = new HashMap<>();
    private Map<String, Object> dataModel = new HashMap<>();

    AutoCompleteTextView autoCompleteTextView;

    GoogleApiClient googleApiClient;

    static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40 , -168), new LatLng(71,136)
    );

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        dbHandler = new DBHandler(this);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.search);
        scan = findViewById(R.id.scan);
        nDrawerLayout = findViewById(R.id.nav_view);

        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this , this)
                .build();

        PlaceAutocompleteAdapter placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this , googleApiClient , LAT_LNG_BOUNDS , null);

        autoCompleteTextView.setAdapter(placeAutocompleteAdapter);

//        String current = dbHandler.getUSER_INFO().getCurrentPage();
//        if (current.equals("map1")){
//            scan.setVisibility(View.VISIBLE);
//            Toast.makeText(this, "Your valet is waiting for you !", Toast.LENGTH_LONG).show();
//            initialization();
//        };


        Spinner loc = findViewById(R.id.loc);
        //ImageButton map = findViewById(R.id.mapView);
        TextView avi = findViewById(R.id.available);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                String location = searchView.getQuery().toString();
//                List<Address> addressList = null;
//
//                if(location!= null || !location.equals("")){
//                    Geocoder geocoder = new Geocoder(MainActivity2.this);
//                    try {
//                        addressList = geocoder.getFromLocationName(location,1);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    Address address = addressList.get(0);
//                    LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
//                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng , 15));
//
//                    reqLatLong = latLng;
//                    //Log.e("*****" ,  getAddressName(MainActivity.this , address.getLatitude() ,  address.getLongitude()));
//
//
//                    mMap.setOnMarkerClickListener(MainActivity2.this);
//                }
//
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

        //mapFragment.getMapAsync(this);

        initialization();

        List<String> gradeList = new ArrayList<>();
        gradeList.add("");
        gradeList.add("Tag Mall");
        gradeList.add("Pollyvard");
        gradeList.add("Work");

        ArrayAdapter gradeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, gradeList);
        gradeAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
        loc.setAdapter(gradeAdapter);
        loc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                isAvilable = 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        MaterialToolbar mTopToolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(mTopToolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,
                R.string.common_open_on_phone,
                R.string.common_open_on_phone);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)
                findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


    }

    private void initialization() {

        LatLngListMarker = new ArrayList<>();

        LatLng latLng = new LatLng(v1, v2);

        LatLng latLng2 = new LatLng(a1, a2);
        LatLngListMarker.add(latLng);
        LatLngListMarker.add(latLng2);
        builder = new LatLngBounds.Builder();

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (flag) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // globelFunction.getSalesManInfo(SalesmanMapsActivity.this,2);
                            v1 = v1 - 0.000010;
                            v2 = v2 - 0.000010;
                            a1 = 31.968420;
                            a2 = 35.916258;
                            //Log.e("Location", "loc" + v1 + "  " + v2 + "   " + a1 + "   " + a2);
                            LatLng latLng = new LatLng(v1, v2);
                            LatLng latLng2 = new LatLng(a1, a2);
                            LatLngListMarker.clear();
                            LatLngListMarker.add(latLng);
                            LatLngListMarker.add(latLng2);
                            location(1);

                        }
                    });

                }
//
            }

        }, 0, 1000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            public void onMapClick(LatLng point){
                Toast.makeText(MainActivity2.this,
                        point.latitude + ", " + point.longitude,
                        Toast.LENGTH_SHORT).show();

                mMap.addMarker(new MarkerOptions().position(point).title(getAddressName(MainActivity2.this , point.latitude , point.longitude)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point , 15));
                mMap.setOnMarkerClickListener(MainActivity2.this);

                Log.e("*****" ,  getAddressName(MainActivity2.this , point.latitude ,  point.longitude));
            }
        });


        //location(0);


    }

    public void location(int move) {

        try {

            if (move == 1) {

                mMap.clear();
            }
        } catch (Exception e) {
            Log.e("Problem", "problennnn" + e.getMessage());
        }

        // Add a marker in Sydney and move the camera
        //Log.e("mmmmmm", "locationCall");
        LatLng sydney = null;
        for (int i = 0; i < LatLngListMarker.size(); i++) {

            //if (!salesManInfosList.get(i).getLatitudeLocation().equals("0") && !salesManInfosList.get(i).getLongitudeLocation().equals("0")) {
            sydney = LatLngListMarker.get(i);

//            MarkerOptions marker = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconSize())).position(sydney).title("aaa");
//            mMap.addMarker(marker);

            Marker myMarker;

            if (i == 0) {
                myMarker = mMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("My Location")
                        .icon(BitmapDescriptorFactory.fromBitmap(iconSize())));
            } else {
                myMarker = mMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("Tag Mall")
                        //.snippet("This is my spot!")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            }

            mMap.setOnMarkerClickListener(this);

            builder.include(sydney);


//            mMap.setOnMarkerClickListener(MainActivity.this);
//
//            MarkerOptions marker = new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromBitmap(iconSize()))
//                    .position(sydney)
//                    .title("aaa");
//
//            builder.include(sydney);


        }
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(sydney));
        if (move == 0) {
            try {
                bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                mMap.animateCamera(cu);
            } catch (Exception e) {

            }
        }
        flag = true;
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0));
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {


        Log.e("******", marker.getTitle());
        latitude = ""+ marker.getPosition().latitude;
        longitude = ""+ marker.getPosition().longitude;

        if (!marker.getTitle().equals("My Location"))
            reqService(marker);

        return false;
    }

    Bitmap iconSize() {
        int height = 50;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.car);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        return smallMarker;
    }

    void reqService(Marker marker) {

        Dialog dialog = new Dialog(MainActivity2.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.conferm_req);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


        TextView text = dialog.findViewById(R.id.text);
        Button req = dialog.findViewById(R.id.request);

        text.setText("Request a valet in '"+ marker.getTitle() + "'");


    }

    public String getAddressName(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String add = ""; //obj.getAddressLine(0);
//            add = add + "\n" + obj.getCountryName();
//            add = add + "\n" + obj.getCountryCode();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getPostalCode();
//            add = add + "\n" + obj.getSubAdminArea();
//            add = add + "\n" + obj.getLocality();
//            add = add + "\n" + obj.getSubThoroughfare() ;
            add = add + "\n" + obj.getSubLocality() ;
//            add = add + "\n" + obj.getFeatureName() ;
//            add = add + "\n" + obj.getPremises() ;
//            add = add + "\n" + obj.getSubAdminArea() ;
//            add = add + "\n" + obj.getExtras() ;
//            add = add + "\n" + obj.getMaxAddressLineIndex() ;

            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            finish();
//        }

        if (nToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item1) {
            // Intent i = new Intent(MainActivity.this, MainScreen.class);
            //startActivity(i);
            Dialog dialog = new Dialog(MainActivity2.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.profile);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            dialog.show();
        }

        if (id == R.id.item2) {
            //Intent i = new Intent(DetailActivity.this, Saved.class);
            //startActivity(i);
        }


        // nDrawerLayout.closeDrawers();
        return false;
    }


}