package com.example.valet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivityValet extends AppCompatActivity {
TextView singInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_valet);

        initialization();

    }

    private void initialization() {
        singInButton=findViewById(R.id.singInButton);
        singInButton.setOnClickListener(onClickListener);



    }


    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.singInButton:
                    Intent intent=new Intent( MainActivityValet.this,  LogInValet.class);
                    startActivity(intent);
                    break;

            }
        }
    };
}