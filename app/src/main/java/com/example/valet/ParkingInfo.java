package com.example.valet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.annotation.RequiresApi;
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
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.valet.MainActivity.serialOfRaw;

public class ParkingInfo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle nToggle;
    String barcodeValue;
    Button reqCar;
    String captainName = " ", captainNo = " ";
    TextView location, vName, vPhone, time;
    ProgressDialog progressDialog;

    Captains captain;
    String timeToArrive = "";
    boolean ready = false;

    Dialog dialog2,  dialog3;

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

                        progressDialog = new ProgressDialog(ParkingInfo.this, R.style.MyAlertDialogStyle);
                        progressDialog.setMessage("Waiting for valet...");
                        progressDialog.show();


                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });
        NavigationView navigationView = (NavigationView)
                findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        Timer();

    }

    @SuppressLint("SetTextI18n")
    void setParkingInfo() {

        MainActivity obj = new MainActivity();
        String[] parkInfo = obj.getParkingInfo();

        location.setText("My Location : Taj Mall");
        vName.setText("Valet Name : " + parkInfo[2]);
        vPhone.setText("Phone # : " + parkInfo[3]);
        time.setText("Time Of Parking : " + parkInfo[4]);

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

    void paymentDialog(String[] parts) {

        Dialog dialog2 = new Dialog(ParkingInfo.this,R.style.Theme_Dialog);
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
                        dialog.dismiss();
                        new PAY_JSONTask().execute();

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
                newClient.setUserName(new DBHandler(ParkingInfo.this).getUSER_INFO().getUserName());
                newClient.setPhoneNumber(new DBHandler(ParkingInfo.this).getUSER_INFO().getPhoneNumber());

                JSONObject jsonObjectNewClient = newClient.getJSONObject3();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("REQUEST_CAR", jsonObjectNewClient.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("CLIENT_NAME", newClient.getUserName()));
                nameValuePairs.add(new BasicNameValuePair("CLIENT_NO", newClient.getPhoneNumber()));
                nameValuePairs.add(new BasicNameValuePair("CURRENT_DATE", currentDate));
                nameValuePairs.add(new BasicNameValuePair("SERIAL", serialOfRaw));
                Log.e("serialOfRaw",""+serialOfRaw);
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

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            new JSONTask2().execute();
//
//                        }
//                    }, 6000);

                } else {
                    Log.e("tag", "****Failed to export data");
                }
            } else {
                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }

    private class PAY_JSONTask extends AsyncTask<String, String, String> {

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
                newClient.setUserName(new DBHandler(ParkingInfo.this).getUSER_INFO().getUserName());
                newClient.setPhoneNumber(new DBHandler(ParkingInfo.this).getUSER_INFO().getPhoneNumber());

                JSONObject jsonObjectNewClient = newClient.getJSONObject3();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("PAY_CAR", jsonObjectNewClient.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("CLIENT_NAME", newClient.getUserName()));
                nameValuePairs.add(new BasicNameValuePair("CLIENT_NO", newClient.getPhoneNumber()));
                nameValuePairs.add(new BasicNameValuePair("CURRENT_DATE", currentDate));
                nameValuePairs.add(new BasicNameValuePair("SERIAL", serialOfRaw));
                Log.e("serialOfRaw",""+serialOfRaw);
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
                if (s.contains("PAY_CAR_SUCCESS")) {

                    Log.e("tag", "****Success");

                    Intent intent = new Intent(ParkingInfo.this, LogIn2.class);
                    startActivity(intent);


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
                    timeToArrive="";
                    for (int i = 0; i < parentArrayOrders.length(); i++) {
                        JSONObject finalObject = parentArrayOrders.getJSONObject(i);

                        captain = new Captains();
                        Log.e("****", finalObject.getString("STATUS") + " " + finalObject.getString("CLIENT_NAME"));
                        if (finalObject.getString("SERIAL").equals(serialOfRaw)&&finalObject.getString("DATE_").equals(currentDate) && finalObject.getString("STATUS").equals("7") && finalObject.getString("CLIENT_NAME").equals(new DBHandler(ParkingInfo.this).getName())) {
                            captain.setCaptainName(finalObject.getString("CAPTAIN_NAME"));
//                            captain.setCaptainNumber(finalObject.getString("CAPTAIN_NO"));
//                            captain.setCaptain_rate(finalObject.getString("CAPTAIN_RATE"));
                            //captain.setCaptainPic( finalObject.getString("CAPTAIN_PIC"));
                            //captain.setClientName( finalObject.getString("CLIENT_NAME"));
                            //captain.setClientPhone( finalObject.getString("CLIENT_PHONE"));

                            timeToArrive = finalObject.getString("TIME_TO_ARRIVE");
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

            if (!timeToArrive.equals("")) {

                try {
                    progressDialog.dismiss();
                }catch (Exception e){
                    Log.e("errorInParkingTask2","error");
                }
                try{
                    if(dialog2!=null) {
                        if (dialog2.isShowing()) {
                            //
                        }else {
                            showParkDialog();
                            dialog2.show();
                        }
                    }else {
                        showParkDialog();
                        dialog2.show();
                    }
                }catch (Exception e){

                }

            } else {

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        new JSONTask2().execute();
//
//                    }
//                }, 6000);
//                Toast.makeText(LoginActivity.this, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    void showParkDialog (){

        dialog2 = new Dialog(ParkingInfo.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.valet_back_dialog);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


        TextView textView = dialog2.findViewById(R.id.tt);
        TextView name = dialog2.findViewById(R.id.name);

        textView.setText("Your car will be ready after " + timeToArrive + " minutes with :");
        name.setText(captain.getCaptainName());
        Button req = dialog2.findViewById(R.id.request);

        req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

// here
                new JSONTask3().execute();
            }
        });



    }

    private class JSONTask3 extends AsyncTask<String, String, List<Clients>> {

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
                        Log.e("****", finalObject.getString("STATUS") + " " + finalObject.getString("CLIENT_NAME"));
                        if (finalObject.getString("SERIAL").equals(serialOfRaw) &&finalObject.getString("DATE_").equals(currentDate) && finalObject.getString("STATUS").equals("5") && finalObject.getString("CLIENT_NAME").equals(new DBHandler(ParkingInfo.this).getName())) {
                            ready = true;
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

            if (ready) {

                try {
                    dialog2.dismiss();
                }catch (Exception e){
                    Log.e("errorInParkingTask3","error");
                }
                try{
                    if(dialog3!=null) {
                        if (dialog3.isShowing()) {
                            //
                        }else {
                            requestDialog();
                            dialog3.show();
                        }
                    }else {
                        requestDialog();
                        dialog3.show();
                    }
                }catch (Exception e){

                }




//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                        Dialog dialog3 = new Dialog(ParkingInfo.this);
//                        dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog3.setContentView(R.layout.notify);
//                        dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        dialog3.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
//
//                        Button req = dialog3.findViewById(R.id.request);
//
//                        req.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//
//                                IntentIntegrator intentIntegrator = new IntentIntegrator(ParkingInfo.this);
//                                intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
//                                intentIntegrator.setBeepEnabled(true);
//                                intentIntegrator.setCameraId(0);
//                                intentIntegrator.setOrientationLocked(true);
//                                intentIntegrator.setPrompt("SCAN");
//                                intentIntegrator.setBarcodeImageEnabled(false);
//                                intentIntegrator.initiateScan();
//
//                            }
//                        });
//
//                        dialog3.show();
//                    }
//                }, 6000);
            } else {

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        new JSONTask3().execute();
//
//                    }
//                }, 6000);
//                Toast.makeText(LoginActivity.this, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void requestDialog(){

         dialog3 = new Dialog(ParkingInfo.this);
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

        //dialog3.show();
    }

    void Timer() {

        Timer T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @SuppressLint("LongLogTag")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        Log.e("mm", "in 123123");
                        Handler h = new Handler(Looper.getMainLooper());
                        h.post(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            public void run() {

                                new JSONTask2().execute();
                                new JSONTask3().execute();
                            }
                        });
                    }
                });


            }
        }, 10, 1000);
    }


}