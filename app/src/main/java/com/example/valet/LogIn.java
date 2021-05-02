package com.example.valet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class LogIn extends AppCompatActivity {

    String name = "Ali";
    String password = "111";
    String Email = "ali1993@gmail.com";
    String number = "0795425583";
    String type = "1";
    String carTyp = "Bicanto";
    String carColor = " Orange";
    String carNo = "2121112";
    Button logIn;

    Spinner carType;
    Bitmap profilePicture , carPicture;

    TextView signUp;

    EditText userNameLog, passwordLog;

    EditText Tname, Tpassword, TEmail, Tnumber, Ttype, TcarType, TcarColor, TcarNo;

    ArrayList<Clients> clientsList;

    ImageButton proPic, carPic;
    public static final int PICK_IMAGE = 1;
    int flag;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // getSupportActionBar().hide();

        logIn = findViewById(R.id.logInButton);
        signUp = findViewById(R.id.sign_up);
        userNameLog = findViewById(R.id.username);
        passwordLog = findViewById(R.id.password);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userNameLog.getText().toString().equals("")) { // for me !

                    Intent intent = new Intent(LogIn.this, LogIn2.class);
                    startActivity(intent);

                }


                if (!userNameLog.getText().toString().equals("") && !passwordLog.getText().toString().equals("")) {


                    for (int i = 0; i < clientsList.size(); i++) {

                        if (userNameLog.getText().toString().equals(clientsList.get(i).getUserName())) {
                            if (passwordLog.getText().toString().toString().equals(clientsList.get(i).getPassword())) {

                                PublicInfo.name = clientsList.get(i).getUserName();
                                PublicInfo.password = clientsList.get(i).getPassword();
                                PublicInfo.Email = clientsList.get(i).getE_mail();
                                PublicInfo.number = clientsList.get(i).getPhoneNumber();
                                PublicInfo.carType = clientsList.get(i).getCarType();
                                PublicInfo.name = clientsList.get(i).getUserName();
                                PublicInfo.name = clientsList.get(i).getUserName();

                                Intent intent = new Intent(LogIn.this, LogIn2.class);
                                startActivity(intent);

                                break;
                            }
                        }
                    }

                } else {
                    Toast.makeText(LogIn.this, "Please Enter Username and password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(LogIn.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.sign_up);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


                Spinner loc = dialog.findViewById(R.id.loc);

                loc.setSelection(0);
                List<String> gradeList = new ArrayList<>();
                gradeList.add("Kia");
                gradeList.add("Toyota");
                gradeList.add("Ford");
                gradeList.add("Mersides");

                ArrayAdapter gradeAdapter = new ArrayAdapter<String>(LogIn.this, R.layout.spinner_layout, gradeList);
                gradeAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
                loc.setAdapter(gradeAdapter);

                Tname = dialog.findViewById(R.id.username);
                Tpassword = dialog.findViewById(R.id.password);
                TEmail = dialog.findViewById(R.id.e_mail);
                Tnumber = dialog.findViewById(R.id.number);
                TcarType = dialog.findViewById(R.id.car_type);
                TcarColor = dialog.findViewById(R.id.car_color);
                TcarNo = dialog.findViewById(R.id.car_num);
                logIn = dialog.findViewById(R.id.logInButton);
                signUp = dialog.findViewById(R.id.sign_up);
                Button save = dialog.findViewById(R.id.save);
                proPic = dialog.findViewById(R.id.pro_pic);
                carPic = dialog.findViewById(R.id.car_pic);

                proPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        flag = 1;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"),1);
                        flag = 1;
                    }
                });

                carPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        flag = 2;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"),1);
                        flag = 2;
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!TextUtils.isEmpty(Tname.getText().toString())) {
                            if (!TextUtils.isEmpty(Tpassword.getText().toString())) {
                                if (!TextUtils.isEmpty(TEmail.getText().toString())) {
                                    if (!TextUtils.isEmpty(Tnumber.getText().toString())) {
                                        if (!TextUtils.isEmpty(TcarColor.getText().toString())) {
                                            if (!TextUtils.isEmpty(TcarNo.getText().toString())) {

                                                PublicInfo.name = Tname.getText().toString();
                                                PublicInfo.password = Tpassword.getText().toString();
                                                PublicInfo.Email = TEmail.getText().toString();
                                                PublicInfo.number = Tnumber.getText().toString();
                                                PublicInfo.type = Tname.getText().toString();
                                                PublicInfo.carType = TcarType.getText().toString();
                                                PublicInfo.carColor = TcarColor.getText().toString();
                                                PublicInfo.carNo = TcarNo.getText().toString();


                                                Intent intent = new Intent(LogIn.this, LogIn2.class);
                                                startActivity(intent);

                                                new JSONTask2().execute();


                                            } else {
                                                TcarNo.setError("Required!");
                                            }
                                        } else {
                                            TcarColor.setError("Required!");
                                        }
                                    } else {
                                        Tnumber.setError("Required!");
                                    }
                                } else {
                                    TEmail.setError("Required!");
                                }
                            } else {
                                Tpassword.setError("Required!");
                            }
                        } else {
                            Tname.setError("Required!");
                        }

                    }
                });

                dialog.show();
            }
        });


        clientsList = new ArrayList<Clients>();
        new JSONTask().execute();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            if (data != null)
            {
                Uri fileUri = data.getData(); //added this line
                try {

                    switch (flag){
                        case 1:
                            profilePicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                            profilePicture = getResizedBitmap(profilePicture, 400);
                            proPic.setImageBitmap(profilePicture);
                            break;
                        case 2:
                            carPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                            carPicture = getResizedBitmap(carPicture, 400);
                            carPic.setImageBitmap(carPicture);
                            break;

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED)
        {
            Toast.makeText(getApplicationContext(), "Cancelled",     Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private class JSONTask extends AsyncTask<String, String, List<Clients>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Clients> doInBackground(String... params) {
            URLConnection connection = null;
            BufferedReader reader = null;

            try {

                URL url = new URL("http://5.189.130.98:8085/importt.php?FLAG=0");

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

                Log.e("Import Data", parentObject.toString());

                try {
                    JSONArray parentArrayOrders = parentObject.getJSONArray("CLIENTS");

                    for (int i = 0; i < parentArrayOrders.length(); i++) {
                        JSONObject finalObject = parentArrayOrders.getJSONObject(0);

                        Clients client = new Clients();
                        client.setUserName(finalObject.getString("USERNAME"));
                        client.setPassword(finalObject.getString("PASSWORD"));
                        client.setE_mail(finalObject.getString("E_MAIL"));
                        client.setPhoneNumber(finalObject.getString("PHONE_NO"));
                        client.setCarType(finalObject.getString("CAR_TYPE"));
                        client.setCarModel(finalObject.getString("CAR_MODEL"));
                        client.setCarColor(finalObject.getString("CAR_COLOR"));
                        client.setCarLot(finalObject.getString("CAR_LOT"));
//                        client.setPassword( finalObject.getString("USER_PIC"));
//                        client.setPassword( finalObject.getString("CAR_PIC"));


                        clientsList.add(client);
                    }
                } catch (JSONException e) {
                    Log.e("Import Data", e.getMessage().toString());
                }

            } catch (MalformedURLException e) {
                Log.e("Customer1", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Customer2", e.getMessage().toString());
                e.printStackTrace();

            } catch (JSONException e) {
                Log.e("Customer3", "********ex3  " + e.toString());
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
            return clientsList;
        }


        @Override
        protected void onPostExecute(final List<Clients> result) {
            super.onPostExecute(result);

            if (result != null) {
                Log.e("Clients", "********" + result.size());
            } else {
//                Toast.makeText(LoginActivity.this, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class JSONTask2 extends AsyncTask<String, String, String> {

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

                String newCust = "";

                Clients newClient = new Clients();
                newClient.setUserName(PublicInfo.name);
                newClient.setPassword(PublicInfo.password);
                newClient.setE_mail(PublicInfo.Email);
                newClient.setPhoneNumber(PublicInfo.number);
                newClient.setCarType(PublicInfo.carType);
                newClient.setCarModel(PublicInfo.carModel);
                newClient.setCarColor(PublicInfo.carColor);
                newClient.setCarLot(PublicInfo.carNo);


                JSONObject jsonObjectNewClient = newClient.getJSONObject();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("NEW_CLIENT", jsonObjectNewClient.toString().trim()));


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

                Log.e("tag***", JsonResponse);

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("****" , e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.contains("NEW_CLIENTS SUCCESS")) {

                    Log.e("tag", "****Success");
                } else {
                    Log.e("tag", "****Failed to export data");
                }
            } else {
                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }


    public String bitMapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] arr = baos.toByteArray();
            String result = Base64.encodeToString(arr, Base64.DEFAULT);
            Log.e("follow1", result);
            return result;
        }

        return "";
    }


}