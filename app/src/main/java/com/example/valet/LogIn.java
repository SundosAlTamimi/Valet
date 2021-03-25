package com.example.valet;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class LogIn extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in3);

        EditText user = findViewById(R.id.login_username);

        Button login = findViewById(R.id.login_login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user.getText().toString().equals("222")){

                    Intent intent = new Intent(LogIn.this, ValetScreen.class);
                    startActivity(intent);

                } else {

                    Intent intent = new Intent(LogIn.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


}