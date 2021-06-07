package com.example.valet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.google.android.material.appbar.MaterialToolbar;
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

public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

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

    SearchView searchView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(this);

        searchView = findViewById(R.id.search);
        scan = findViewById(R.id.scan);
        nDrawerLayout = findViewById(R.id.nav_view);


        String current = dbHandler.getUSER_INFO().getCurrentPage();
        if (current.equals("map1")){
            scan.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Your valet is waiting for you !", Toast.LENGTH_LONG).show();
            initialization();
        };


        Spinner loc = findViewById(R.id.loc);
        //ImageButton map = findViewById(R.id.mapView);
        TextView avi = findViewById(R.id.available);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if(location!= null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng , 15));

                    reqLatLong = latLng;
                    //Log.e("*****" ,  getAddressName(MainActivity.this , address.getLatitude() ,  address.getLongitude()));


                    mMap.setOnMarkerClickListener(MainActivity.this);
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //mapFragment.getMapAsync(this);

        initialization();
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanDialog();
            }
        });

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
                Toast.makeText(MainActivity.this,
                        point.latitude + ", " + point.longitude,
                        Toast.LENGTH_SHORT).show();

                mMap.addMarker(new MarkerOptions().position(point).title(getAddressName(MainActivity.this , point.latitude , point.longitude)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point , 15));
                mMap.setOnMarkerClickListener(MainActivity.this);

                Log.e("*****" ,  getAddressName(MainActivity.this , point.latitude ,  point.longitude));
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

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.conferm_req);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


        TextView text = dialog.findViewById(R.id.text);
        Button req = dialog.findViewById(R.id.request);

        text.setText("Request a valet in '"+ marker.getTitle() + "'");

        req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new JSONTask().execute();


                progressDialog = new ProgressDialog(MainActivity.this, R.style.MyAlertDialogStyle);
                progressDialog.setMessage("Waiting for valet...");
                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //progressDialog.dismiss();

                        new JSONTask2().execute();


                    }
                }, 6000);


                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @SuppressLint("SetTextI18n")
    void scanDialog(){

        Dialog dialog3 = new Dialog(MainActivity.this);
        dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog3.setContentView(R.layout.waiting_dialog);
        dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog3.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


        TextView textView = dialog3.findViewById(R.id.tt);
        textView.setText(captain.getCaptainName() +" is waiting for you !");

        Button scan = dialog3.findViewById(R.id.scan);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("SCAN");
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();

                dialog3.dismiss();
            }
        });
        dialog3.show();

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (Result != null) {
            if (Result.getContents() == null) {
                Log.d("MainActivity", "cancelled scan");
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
                barcodeValue = "cancelled";


            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned ! ", Toast.LENGTH_SHORT).show();

                barcodeValue = Result.getContents();


                parts = barcodeValue.split("/");
//39.96430206305241/21.65560942143202/+962222222222/+962222222222/13:51:55
                scan.setVisibility(View.GONE);

                new JSONTask5().execute();

                Intent intent = new Intent(MainActivity.this, ParkingInfo.class);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public String[] getParkingInfo(){
        return parts;
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
            Dialog dialog = new Dialog(MainActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DrawerLayout drawer =
                (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI("http://5.189.130.98:8085/exportt.php"));


                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                Clients clientDB = dbHandler.getUSER_INFO();

                Clients newClient = new Clients();
                newClient.setUserName(clientDB.getUserName());
                newClient.setPassword(clientDB.getPassword());
                newClient.setE_mail(clientDB.getE_mail());
                newClient.setPhoneNumber(clientDB.getPhoneNumber());
                newClient.setCarType(clientDB.getCarType());
                newClient.setCarModel(clientDB.getCarModel());
                newClient.setCarColor(clientDB.getCarColor());
                newClient.setCarLot(clientDB.getCarLot());
                newClient.setTime(currentTime);
                newClient.setDate(currentDate);
                newClient.setLatitude(latitude);
                newClient.setLongitude(longitude);


                JSONObject jsonObjectNewClient = newClient.getJSONObject2();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("REQUEST_VALET", jsonObjectNewClient.toString().trim()));

                //Log.e("tag", "" + jsonArrayPics.toString());

                request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = client.execute(request);

                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();

                JsonResponse = sb.toString();

                Log.e("tag***", "" + JsonResponse);

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.contains("REQUEST_VALET SUCCESS")) {

                    Log.e("tag", "****Success");
                } else {
                    Log.e("tag", "****Failed to export data");
                }
            } else {
                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }


    private class JSONTask2 extends AsyncTask<String, String, List<Clients>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<Clients> doInBackground(String... params) {
            URLConnection connection = null;
            BufferedReader reader = null;

            try {

                URL url = new URL("http://5.189.130.98:8085/importt.php?FLAG=1");

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);

                reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                String finalJson = sb.toString();
//                Log.e("finalJson*********", finalJson);

                JSONObject parentObject = new JSONObject(finalJson);

                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                try {
                    JSONArray parentArrayOrders = parentObject.getJSONArray("CAPTAINS_STATUS");

                    for (int i = 0; i < parentArrayOrders.length(); i++) {
                        JSONObject finalObject = parentArrayOrders.getJSONObject(i);

                        captain = new Captains();
                        Log.e("****", finalObject.getString("STATUS") + " " + finalObject.getString("CLIENT_NAME") );
                        if(finalObject.getString("DATE_").equals(currentDate) && finalObject.getString("STATUS").equals("0") && finalObject.getString("CLIENT_NAME").equals(dbHandler.getName())) {
                            captain.setCaptainName(finalObject.getString("CAPTAIN_NAME"));
                            captain.setCaptainNumber(finalObject.getString("CAPTAIN_NO"));
                            captain.setCaptain_rate(finalObject.getString("CAPTAIN_RATE"));
                            //captain.setCaptainPic( finalObject.getString("CAPTAIN_PIC"));
                            //captain.setClientName( finalObject.getString("CLIENT_NAME"));
                            //captain.setClientPhone( finalObject.getString("CLIENT_PHONE"));
                            break;
                        }
//                        client.setPassword( finalObject.getString("CAR_PIC"));

                    }
                } catch (JSONException e) {
//                    Log.e("Import Data2", e.getMessage().toString());
                }

            } catch (MalformedURLException e) {
//                Log.e("Customer", "********ex1");
                e.printStackTrace();
            } catch (IOException e) {
//                Log.e("Customer", e.getMessage().toString());
                e.printStackTrace();

            } catch (JSONException e) {
//                Log.e("Customer", "********ex3  " + e.toString());
                e.printStackTrace();
            } finally {
//                Log.e("Customer", "********finally");
                if (connection != null) {
//                    Log.e("Customer", "********ex4");
                    // connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(final List<Clients> result) {
            super.onPostExecute(result);

            if (captain != null) {
                if (captain.getCaptainName() != null) {
                    Log.e("Captain", "********" + captain.getCaptainName());
                    progressDialog.dismiss();
                    valetDialog();
                } else {
                    Log.e("Captain", "********else" );
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new JSONTask2().execute();

                        }
                    }, 6000);
                }

            } else {

//                Toast.makeText(LoginActivity.this, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class JSONTask3 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI("http://5.189.130.98:8085/exportt.php"));



                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                captain.setClientName(dbHandler.getUSER_INFO().getUserName());
                captain.setClientPhone(dbHandler.getUSER_INFO().getPhoneNumber());

                JSONObject jsonObjectCaptain = captain.getJSONObject();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("ACCEPT_CAPTAIN", jsonObjectCaptain.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("CLIENT_NAME", captain.getClientName()));
                nameValuePairs.add(new BasicNameValuePair("CLIENT_NO", captain.getClientPhone()));
                nameValuePairs.add(new BasicNameValuePair("CURRENT_DATE", currentDate));
                nameValuePairs.add(new BasicNameValuePair("CAPTAIN_NAME", captain.getCaptainName()));
                nameValuePairs.add(new BasicNameValuePair("CAPTAIN_No", captain.getCaptainNumber()));


                //Log.e("tag", "" + jsonArrayPics.toString());

                request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = client.execute(request);

                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();

                JsonResponse = sb.toString();
                Log.e("*****", JsonResponse);

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.contains("ACCEPT_CAPTAIN SUCCESS")) {

                    dbHandler.updateCurrentPage("map1");

                    Log.e("tag", "****Success");
                } else {
                    Log.e("tag", "****Failed to export data");
                }
            } else {
                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }


    private class JSONTask4 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI("http://5.189.130.98:8085/exportt.php"));


                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


                captain.setClientName(dbHandler.getUSER_INFO().getUserName());
                captain.setClientPhone(dbHandler.getUSER_INFO().getUserName());

                JSONObject jsonObjectCaptain = captain.getJSONObject();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("REJECT_CAPTAIN", jsonObjectCaptain.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("CLIENT_NAME", captain.getClientName()));
                nameValuePairs.add(new BasicNameValuePair("CLIENT_NO", captain.getClientPhone()));
                nameValuePairs.add(new BasicNameValuePair("CURRENT_DATE", currentDate));
                nameValuePairs.add(new BasicNameValuePair("CAPTAIN_NAME", captain.getCaptainName()));
                nameValuePairs.add(new BasicNameValuePair("CAPTAIN_No", captain.getCaptainNumber()));


                //Log.e("tag", "" + jsonArrayPics.toString());

                request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = client.execute(request);

                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();

                JsonResponse = sb.toString();

                Log.e("****", JsonResponse);

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.contains("REJECT_CAPTAIN SUCCESS")) {

                    progressDialog.show();
                    new JSONTask2().execute();
                    Log.e("tag", "****Success");
                } else {
                    Log.e("tag", "****Failed to export data");
                }
            } else {
                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }


    private class JSONTask5 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI("http://5.189.130.98:8085/exportt.php"));


                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                captain.setClientName(dbHandler.getUSER_INFO().getUserName());
                captain.setClientPhone(dbHandler.getUSER_INFO().getUserName());

                JSONObject jsonObjectCaptain = captain.getJSONObject();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("CAR_PARKED", jsonObjectCaptain.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("CLIENT_NAME", captain.getClientName()));
                nameValuePairs.add(new BasicNameValuePair("CLIENT_NO", captain.getClientPhone()));
                nameValuePairs.add(new BasicNameValuePair("CURRENT_DATE", currentDate));
                nameValuePairs.add(new BasicNameValuePair("CURRENT_TIE", currentTime));


                //Log.e("tag", "" + jsonArrayPics.toString());

                request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = client.execute(request);

                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();

                JsonResponse = sb.toString();

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.contains("REJECT_CAPTAIN SUCCESS")) {

                    Log.e("tag", "****Success");
                } else {
                    Log.e("tag", "****Failed to export data");
                }
            } else {
                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }


    void valetDialog(){

        Dialog dialog2 = new Dialog(MainActivity.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.valet_dialog);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


        TextView name = dialog2.findViewById(R.id.name);
        TextView phone = dialog2.findViewById(R.id.phone);
        RatingBar rate = dialog2.findViewById(R.id.rating);

        name.setText("Name :" + captain.getCaptainName());
        phone.setText("Phone # :" + captain.getCaptainNumber());
        int rat = Integer.parseInt(captain.getCaptain_rate());
        rate.setRating(rat);

/*
        name.setText(captain.getCaptainName());
        phone.setText(captain.getCaptainNumber());
        rate.setRating(Integer.parseInt(captain.getCaptain_rate()));
*/

        Button req = dialog2.findViewById(R.id.request);
        Button cancel = dialog2.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new JSONTask4().execute();

                dialog2.dismiss();
            }
        });


        req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new JSONTask3().execute();

                dialog2.dismiss();

                initialization();

                scan.setVisibility(View.VISIBLE);

            }
        });
        dialog2.show();

    }


}