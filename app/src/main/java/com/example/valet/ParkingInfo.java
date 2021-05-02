package com.example.valet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ParkingInfo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle nToggle;
    String barcodeValue ;
    Button reqCar;
    String captainName = " " , captainNo = " " ;
    TextView location, vName, vPhone, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_dialog);

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        location = findViewById(R.id.location);
        vName = findViewById(R.id.valet_name);
        vPhone = findViewById(R.id.phone);
        time = findViewById(R.id.time);

        setParkingInfo();

        reqCar = findViewById(R.id.equestCar);
        reqCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(ParkingInfo.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.conferm_check_out);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


                Button req = dialog.findViewById(R.id.request);

                req.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new JSONTask().execute();

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

// here
                                        dialog2.dismiss();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {


                                                Dialog dialog3 = new Dialog(ParkingInfo.this);
                                                dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                dialog3.setContentView(R.layout.notify);
                                                dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                dialog3.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

                                                Button req = dialog3.findViewById(R.id.request);

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

                                                    }
                                                });

                                                dialog3.show();
                                            }
                                        }, 6000);
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
        });
        NavigationView navigationView = (NavigationView)
                findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

    }

    @SuppressLint("SetTextI18n")
    void setParkingInfo(){

        MainActivity obj = new MainActivity();
        String[] parkInfo = obj.getParkingInfo();

        location.setText("My Location : " +parkInfo[0]);
        vName.setText("Valet Name : "+parkInfo[1]);
        vPhone.setText("Phone # : "+parkInfo[2]);
        time.setText("Time Of Parking : "+parkInfo[3]);

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

                String[] parts = barcodeValue.split("-");


                paymentDialog(parts);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void paymentDialog(String[] parts){

        Dialog dialog2 = new Dialog(ParkingInfo.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.pay);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


        TextView value = dialog2.findViewById(R.id.value);
        Button pay = dialog2.findViewById(R.id.pay);

        value.setText(parts[0] + " JD");

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog2.dismiss();

                Dialog dialog = new Dialog(ParkingInfo.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.rate_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


                RatingBar rate = dialog.findViewById(R.id.rating);
                Button submit = dialog.findViewById(R.id.submit);

                rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                        rate.setRating(ratingBar.getRating());
                    }
                });



                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ParkingInfo.this, LogIn2.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });


        dialog2.show();
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

                Clients newClient = new Clients();
                newClient.setUserName(PublicInfo.name);
                newClient.setPassword(PublicInfo.password);
                newClient.setE_mail(PublicInfo.Email);
                newClient.setPhoneNumber(PublicInfo.number);
                newClient.setCarType(PublicInfo.carType);
                newClient.setCarModel(PublicInfo.carModel);
                newClient.setCarColor(PublicInfo.carColor);
                newClient.setCarLot(PublicInfo.carNo);
                newClient.setTime(currentTime);
                newClient.setDate(currentDate);
                newClient.setCaptainName(PublicInfo.valetName);
                newClient.setCaptainNumber(PublicInfo.valetNo);


                JSONObject jsonObjectNewClient = newClient.getJSONObject3();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("REQUEST_CAR", jsonObjectNewClient.toString().trim()));

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
                Log.e("tag", JsonResponse);

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
                if (s.contains("REQUEST_CAR SUCCESS")) {

                    Log.e("tag", "****Success");
                } else {
                    Log.e("tag", "****Failed to export data");
                }
            } else {
                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }


}