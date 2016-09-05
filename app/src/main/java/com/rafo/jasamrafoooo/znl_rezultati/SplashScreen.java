package com.rafo.jasamrafoooo.znl_rezultati;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rafo.jasamrafoooo.znl_rezultati.util.ContextSettings;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Typeface manifestTypeface= Typeface.createFromAsset(this.getAssets(), "fonts/asimov.otf");
        ContextSettings.setUpAd(this, R.id.adView);
        ImageView logoSlika = (ImageView) findViewById(R.id.logoSlika);
        Button buttonTznl = (Button) findViewById(R.id.buttonTznl);
        Button buttonDznl = (Button) findViewById(R.id.buttonDznl);
        Button buttonPznl = (Button) findViewById(R.id.buttonPznl);
        Button buttonMznl = (Button) findViewById(R.id.buttonMznl);
        Button buttonJuni = (Button) findViewById(R.id.buttonJuni);
        buttonTznl.setTypeface(manifestTypeface);
        buttonDznl.setTypeface(manifestTypeface);
        buttonPznl.setTypeface(manifestTypeface);
        buttonMznl.setTypeface(manifestTypeface);
        buttonJuni.setTypeface(manifestTypeface);
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase();
        Log.i("device id=", deviceId);
    }

    public void clickedMznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl", ContextSettings.getURLM());
        startActivity(i);
    }
    public void clickedPznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl", ContextSettings.getURLP());
        startActivity(i);
    }
    public void clickedDznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl", ContextSettings.getURLD());
        startActivity(i);
    }
    public void clickedTznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl", ContextSettings.getURLT());
        startActivity(i);
    }
    public void clickedJuni(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl", ContextSettings.getURLJ());
        startActivity(i);
    }

    //obriši cache na izlazu iz aplikacije
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            trimCache(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
