package com.rafo.jasamrafoooo.znl_rezultati;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;


public class RealSplash extends Activity {

    private TextView textViewLoading;
    private ProgressBar progressBar;

    private int mProgressStatus = 0;
    public int i = 0;

    private Handler mHandler = new Handler();

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_splash);
        //Typeface oJuiceTypeFace= Typeface.createFromAsset(this.getAssets(), "fonts/orangejuice.ttf");
//        textViewLoading = (TextView) findViewById(R.id.textViewLoading);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //textViewLoading.setTypeface(oJuiceTypeFace);

//        ImageView slikaSponzor = (ImageView) findViewById(R.id.slikaSponzor);
//        String imeResursaZaSponzor = FetchStartData.postaviGrbove("sponzor");

//        if (!imeResursaZaSponzor.equals("-")) {
//            byte[] decodedString = Base64.decode(imeResursaZaSponzor, Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            slikaSponzor.setImageBitmap(decodedByte);
//        }

        boolean spojen = isNetworkAvailable();
        if (!spojen){
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
            }
            catch(Exception e){}
        }

        else {
            new FetchStartData(this);
        }



    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void dataLoaded(){
        Intent i = new Intent(RealSplash.this, SplashScreen.class);
        startActivity(i);
        finish();
    }
}