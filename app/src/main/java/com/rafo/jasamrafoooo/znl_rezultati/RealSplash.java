package com.rafo.jasamrafoooo.znl_rezultati;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;


public class RealSplash extends Activity {

    private TextView textViewLoading;
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
        Typeface oJuiceTypeFace= Typeface.createFromAsset(this.getAssets(), "fonts/orangejuice.ttf");
        textViewLoading = (TextView) findViewById(R.id.textViewLoading);
        textViewLoading.setTypeface(oJuiceTypeFace);

        ImageView slikaSponzor = (ImageView) findViewById(R.id.slikaSponzor);
        String imeResursaZaSponzor = PostavljanjeGrbova.postaviGrbove("sponzor");

        if (!imeResursaZaSponzor.equals("-")) {
            byte[] decodedString = Base64.decode(imeResursaZaSponzor, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            slikaSponzor.setImageBitmap(decodedByte);
        }

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
            new PostavljanjeGrbova();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    while (mProgressStatus < 100) {
                        mProgressStatus = doWork();
                        mHandler.post(new Runnable() {
                            public void run() {
                                textViewLoading.setText(mProgressStatus + "%");
                            }
                        });
                    }
                    Intent i = new Intent(RealSplash.this, SplashScreen.class);
                    startActivity(i);
                    finish();
                }

                public int doWork() {
                    try {
                        synchronized (this) {
                            wait(25);
                            i++;
                        }
                    } catch (InterruptedException ex) {
                    }
                    return i;
                }
            };

            thread.start();

        }



    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}