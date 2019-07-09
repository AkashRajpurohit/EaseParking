package com.bougainvillea.sanchit.easeparking.Activity;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bougainvillea.sanchit.easeparking.Fragments.LoginFragment;
import com.bougainvillea.sanchit.easeparking.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            if(new Date().before(new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-20"))){
                openLoginFragment();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                builder.setTitle("Access Denied")
                        .setMessage("Your free trail is over.Kindly contact developer for more support.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                finishAndRemoveTask();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
        catch(Exception e){

        }
    }

    private void openLoginFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.red));
        }
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.rellay1).setVisibility(View.VISIBLE);
                findViewById(R.id.rellay2).setVisibility(View.VISIBLE);
            }
        };
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new LoginFragment()).commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
    }
}
