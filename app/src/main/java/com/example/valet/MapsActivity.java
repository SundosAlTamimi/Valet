package com.example.valet;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button requestButton;
    public static List<LatLng> LatLngListMarker;
    private LatLngBounds.Builder builder;
    LatLngBounds bounds;
    boolean flag=false;
    Timer timer;
    double v1=31.969570, v2=35.914191;
    double a1=31.968420,a2= 35.916258;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valet_captin_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initialization();
    }

    private void initialization() {

        requestButton=findViewById(R.id.requestButton);
       requestButton.setOnClickListener(onClickListener);
        LatLngListMarker=new ArrayList<>();

        LatLng latLng=new LatLng(v1,v2);

        LatLng latLng2=new LatLng(a1,a2);
        LatLngListMarker.add(latLng);
        LatLngListMarker.add(latLng2);
        builder = new LatLngBounds.Builder();

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if(flag){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                           // globelFunction.getSalesManInfo(SalesmanMapsActivity.this,2);
                            v1=v1-0.000010;
                            v2=v2-0.000010;
                            a1=31.968420;
                            a2= 35.916258;
                            Log.e("Location","loc"+v1+"  "+v2+"   "+a1+"   "+a2);
                            LatLng latLng=new LatLng(v1,v2);
                            LatLng latLng2=new LatLng(a1,a2);
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

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.requestButton:
                   barcodeOpen();
                    break;

            }
        }
    };
    void barcodeOpen (){
        final Dialog dialog = new Dialog( MapsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.qr_);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button endParking=dialog.findViewById(R.id.endParking);
        endParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent =new Intent(MapsActivity.this,MainValetActivity.class);
//                startActivity(intent);
            }
        });
        dialog.show();
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        mMap = googleMap;
        location(0);
    }


    public void location(int move) {

        try {

            if(move==1){

                        mMap.clear();
            }
        }catch (Exception e){
            Log.e("Problem","problennnn"+e.getMessage());
        }

        // Add a marker in Sydney and move the camera
        Log.e("mmmmmm", "locationCall");
        LatLng sydney = null;
        for (int i = 0; i < LatLngListMarker.size(); i++) {

            //if (!salesManInfosList.get(i).getLatitudeLocation().equals("0") && !salesManInfosList.get(i).getLongitudeLocation().equals("0")) {
                sydney = LatLngListMarker.get(i);

                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconSize())).position(sydney).title("aaa"));
                builder.include(sydney);
            //}
        }
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(sydney));
        if(move==0) {
            try {
                bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                mMap.animateCamera(cu);
            }catch (Exception e){

            }
        }
        flag=true;
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0));
    }

    Bitmap iconSize(){
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.van_blue);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        return smallMarker;
    }
}