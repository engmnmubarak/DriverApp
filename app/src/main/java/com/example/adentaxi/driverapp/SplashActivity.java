package com.example.adentaxi.driverapp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adentaxi.driverapp.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    ImageView contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        WifiManager wifi = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()){

            contact = (ImageView) findViewById(R.id.contact);
            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SplashActivity.this,ContactActivity.class));
                    finish();
                }

            });

        }else {
            Toast.makeText(this, "Please Check The Internet Connection!",
                    Toast.LENGTH_LONG).show(); }

    }

    public void  login(View view)
    {
        startActivity(new Intent(this,Login.class));
    }


    public void  getStarted(View view)
    {
        startActivity(new Intent(this,Signup.class));
    }

}
