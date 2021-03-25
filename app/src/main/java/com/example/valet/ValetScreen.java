package com.example.valet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;

public class ValetScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valet_screen);


        ImageView img = findViewById(R.id.mapView);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(ValetScreen.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.client_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button serve = dialog.findViewById(R.id.serve);

                serve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Dialog dialog2 = new Dialog(ValetScreen.this);
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog2.setContentView(R.layout.qr);
                        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Button serve = dialog2.findViewById(R.id.serve);


                        dialog2.show();

                    }
                });
                dialog.show();

            }
        });
    }


}