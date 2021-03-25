package com.example.valet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView nDrawerLayout;
    private ActionBarDrawerToggle nToggle;
    int isAvilable = 0;
    String barcodeValue ;

    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nDrawerLayout = findViewById(R.id.nav_view);
        Button request = findViewById(R.id.request_valet);
        Spinner loc = findViewById(R.id.loc);
        ImageView map = findViewById(R.id.mapView);
        TextView avi = findViewById(R.id.available);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAvilable == 1) {
                    progressDialog = new ProgressDialog(MainActivity.this, R.style.MyAlertDialogStyle);
                    progressDialog.setMessage("Waiting for valet...");
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                            Dialog dialog = new Dialog(MainActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.valet_dialog);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            Button req = dialog.findViewById(R.id.request);

                            req.setOnClickListener(new View.OnClickListener() {
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

                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    }, 3000);

                }

        }
        });


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avi.setVisibility(View.VISIBLE);
                isAvilable = 1;
            }
        });

        List<String> gradeList  = new ArrayList<>();
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


        MaterialToolbar mTopToolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(mTopToolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,mTopToolbar,
                R.string.common_open_on_phone,
                R.string.common_open_on_phone);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)
                findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
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

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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
}