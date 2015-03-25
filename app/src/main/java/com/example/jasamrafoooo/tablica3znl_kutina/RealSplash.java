package com.example.jasamrafoooo.tablica3znl_kutina;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;


public class RealSplash extends Activity {

    private ProgressBar mProgress;
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

        mProgress = (ProgressBar) findViewById(R.id.customProgress);

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

            Thread thread = new Thread() {
                @Override
                public void run() {

                    while (mProgressStatus < 100) {
                        mProgressStatus = doWork();

                        // Update the progress bar
                        mHandler.post(new Runnable() {
                            public void run() {
                                mProgress.setProgress(mProgressStatus);
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
                            wait(35);
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

    //provjeri je li spojen na internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}