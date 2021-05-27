package com.example.valet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LogIn2 extends AppCompatActivity {

    LinearLayout logInLinear;
    RelativeLayout linear;
    Button left, right, login, startRequest;
    TextView t1, t2, name;
    int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in2);



        EditText user = findViewById(R.id.login_username);
        linear = findViewById(R.id.linearr);
        logInLinear = findViewById(R.id.login_linearLayout);
        login = findViewById(R.id.login_login_btn);
        startRequest = findViewById(R.id.startRequest);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        name = findViewById(R.id.name);

        name.setText( "Welcome " +  new DBHandler(this).getName());

        left.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                switch (index) {
                    case 1:
                        ImageViewAnimatedChange(LogIn2.this, linear, getResources().getDrawable(R.drawable.login_back_3));
                        t1.setText("PARK YOUR VEHICLE");
                        t2.setText("SMARTLY");
                        index = 3;
                        break;

                    case 2:
                        ImageViewAnimatedChange(LogIn2.this, linear, getResources().getDrawable(R.drawable.login_back));
                        t1.setText("PARKING SOLUTION");
                        t2.setText("AT YOUR FINGERTIPS");
                        index = 1;
                        break;

                    case 3:
                        ImageViewAnimatedChange(LogIn2.this, linear, getResources().getDrawable(R.drawable.login_back_2));
                        t1.setText("SMART PARKING");
                        t2.setText("MANAGEMENT");
                        index = 2;
                        break;
                }

                // changePic();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                switch (index) {
                    case 1:
                        ImageViewAnimatedChange(LogIn2.this, linear, getResources().getDrawable(R.drawable.login_back_2));
                        t1.setText("SMART PARKING");
                        t2.setText("MANAGEMENT");
                        index = 2;
                        break;

                    case 2:
                        ImageViewAnimatedChange(LogIn2.this, linear, getResources().getDrawable(R.drawable.login_back_3));
                        t1.setText("PARK YOUR VEHICLE");
                        t2.setText("SMARTLY");
                        index = 3;
                        break;

                    case 3:
                        ImageViewAnimatedChange(LogIn2.this, linear, getResources().getDrawable(R.drawable.login_back));
                        t1.setText("PARKING SOLUTION");
                        t2.setText("AT YOUR FINGERTIPS");
                        index = 1;
                        break;
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent intent = new Intent(LogIn2.this, MainActivity.class);
                    startActivity(intent);
            }
        });

        startRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LogIn2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        changePic();

        animateLogin();
    }

    void animateLogin() {

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(2000);

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(fadeIn);
        logInLinear.setAnimation(animation);
    }

    public void changePic() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (index) {
                    case 1:
                        ImageViewAnimatedChange(LogIn2.this, linear, getResources().getDrawable(R.drawable.login_back_2));
                        t1.setText("SMART PARKING");
                        t2.setText("MANAGEMENT");
                        index = 2;
                        break;

                    case 2:
                        ImageViewAnimatedChange(LogIn2.this, linear, getResources().getDrawable(R.drawable.login_back_3));
                        t1.setText("PARK YOUR VEHICLE");
                        t2.setText("SMARTLY");
                        index = 3;
                        break;

                    case 3:
                        ImageViewAnimatedChange(LogIn2.this, linear, getResources().getDrawable(R.drawable.login_back));
                        t1.setText("PARKING SOLUTION");
                        t2.setText("AT YOUR FINGERTIPS");
                        index = 1;
                        break;

                }

                changePic();

            }
        }, 6000);


    }

    public static void ImageViewAnimatedChange(Context c, final RelativeLayout v, final Drawable new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setBackgroundDrawable(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }


}