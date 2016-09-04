package com.rafo.jasamrafoooo.znl_rezultati;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rafo.jasamrafoooo.znl_rezultati.util.ContextSettings;

import java.io.File;


public class SplashScreen extends Activity {


    public static final String linkNaURLove = "http://znlrezultati.simplesite.com/";
    public boolean readLinks = false;
    public static String URLM;
    public static String URLP;
    public static String URLD;
    public static String URLT;
    public static String URLJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Typeface manifestTypeface= Typeface.createFromAsset(this.getAssets(), "fonts/asimov.otf");
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("705A531EF2DFC7439759DDD27F57A110")
                .build();
        adView.loadAd(adRequest);
        ImageView logoSlika = (ImageView) findViewById(R.id.logoSlika);
        String imeResursaZaGrb = FetchStartData.postaviGrbove("logo");
        Button buttonTznl = (Button) findViewById(R.id.buttonTznl);
        Button buttonDznl = (Button) findViewById(R.id.buttonDznl);
        Button buttonPznl = (Button) findViewById(R.id.buttonMznl);
        Button buttonMznl = (Button) findViewById(R.id.buttonPznl);
        Button buttonJuni = (Button) findViewById(R.id.buttonJuni);
        buttonTznl.setTypeface(manifestTypeface);
        buttonDznl.setTypeface(manifestTypeface);
        buttonPznl.setTypeface(manifestTypeface);
        buttonMznl.setTypeface(manifestTypeface);
        buttonJuni.setTypeface(manifestTypeface);
        URLM = ContextSettings.getURLM();
        URLP = ContextSettings.getURLP();
        URLD = ContextSettings.getURLD();
        URLT = ContextSettings.getURLT();
        URLJ = ContextSettings.getURLJ();

        if (!imeResursaZaGrb.equals("-")) {
            byte[] decodedString = Base64.decode(imeResursaZaGrb, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            logoSlika.setImageBitmap(decodedByte);
        }
    }

    public void clickedMznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl", URLM);
        startActivity(i);
    }
    public void clickedPznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl", URLP);
        startActivity(i);
    }
    public void clickedDznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl", URLD);
        startActivity(i);
    }
    public void clickedTznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl", URLT);
        startActivity(i);
    }
    public void clickedJuni(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl", URLJ);
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

}
