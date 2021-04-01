package com.example.valet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ParkingInfo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle nToggle;
    String barcodeValue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_dialog);
        getSupportActionBar().hide();

        NavigationView navigationView = (NavigationView)
                findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

    }

    void scanDialog(){

        Dialog dialog3 = new Dialog(ParkingInfo.this);
        dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog3.setContentView(R.layout.waiting_dialog);
        dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog3.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


        Button scan = dialog3.findViewById(R.id.scan);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator intentIntegrator = new IntentIntegrator(ParkingInfo.this);
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
            Dialog dialog = new Dialog(ParkingInfo.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.profile);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            dialog.show();
        }

        if (id == R.id.item2) {


            Dialog dialog = new Dialog(ParkingInfo.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.conferm_check_out);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


            Button req = dialog.findViewById(R.id.request);

            req.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ProgressDialog progressDialog = new ProgressDialog(ParkingInfo.this, R.style.MyAlertDialogStyle);
                    progressDialog.setMessage("Waiting for valet...");
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                            Dialog dialog2 = new Dialog(ParkingInfo.this);
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog2.setContentView(R.layout.valet_back_dialog);
                            dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


                            Button req = dialog2.findViewById(R.id.request);

                            req.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    IntentIntegrator intentIntegrator = new IntentIntegrator(ParkingInfo.this);
                                    intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
                                    intentIntegrator.setBeepEnabled(true);
                                    intentIntegrator.setCameraId(0);
                                    intentIntegrator.setOrientationLocked(true);
                                    intentIntegrator.setPrompt("SCAN");
                                    intentIntegrator.setBarcodeImageEnabled(false);
                                    intentIntegrator.initiateScan();

                                    dialog2.dismiss();

                                }
                            });
                            dialog2.show();
                        }
                    }, 6000);


                    dialog.dismiss();
                }
            });
            dialog.show();

        }


        // nDrawerLayout.closeDrawers();
        return false;
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

                Intent intent = new Intent(ParkingInfo.this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}