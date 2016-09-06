package com.rafo.jasamrafoooo.znl_rezultati;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;


public class RealSplash extends Activity {

    AnimationDrawable animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_splash);
        ImageView rocketImage = (ImageView) findViewById(R.id.imageView2);
        rocketImage.setBackgroundResource(R.drawable.animation);
        animation = (AnimationDrawable) rocketImage.getBackground();
        animation.start();

        boolean spojen = isNetworkAvailable();
        if (spojen) {
            new FetchStartData(this);
        } else {
            showAlertDialog();
        }
    }

    private void showAlertDialog(){
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setCancelable(false);

            alertDialog.setTitle("Info");
            alertDialog.setMessage("Provjerite Internet vezu!");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            alertDialog.show();
        } catch (Exception e) {
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void dataLoaded() {
        Intent i = new Intent(RealSplash.this, SplashScreen.class);
        startActivity(i);
        finish();
    }
}