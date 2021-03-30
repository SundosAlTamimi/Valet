package com.example.valet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LogInValet extends AppCompatActivity {
    Button logInButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_);

        initialization();

    }

    private void initialization() {
        logInButton=findViewById(R.id.logInButton);
        logInButton.setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.logInButton:
                    Intent intent=new Intent( LogInValet.this,MainValetActivity.class);
                    startActivity(intent);
                    break;

            }
        }
    };

}
