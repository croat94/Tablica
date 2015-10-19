package com.rafo.jasamrafoooo.znl_rezultati;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;


public class SplashScreen extends Activity {


    public static final String linkNaURLove = "http://znlrezultati.simplesite.com/";
    public boolean readLinks = false;

    public String URLM = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,87%20/Itemid,529/";
    public String URLP = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,91%20/Itemid,574/";
    public String URLD = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,89%20/Itemid,578/";
    public String URLT = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,90%20/Itemid,582/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Typeface manifestTypeface= Typeface.createFromAsset(this.getAssets(), "fonts/track.ttf");
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("705A531EF2DFC7439759DDD27F57A110")
                .build();
        adView.loadAd(adRequest);
        ImageView logoSlika = (ImageView) findViewById(R.id.logoSlika);
        String imeResursaZaGrb = PostavljanjeGrbova.postaviGrbove("logo");
        Button buttonTznl = (Button) findViewById(R.id.buttonTznl);
        Button buttonDznl = (Button) findViewById(R.id.buttonDznl);
        Button buttonPznl = (Button) findViewById(R.id.buttonMznl);
        Button buttonMznl = (Button) findViewById(R.id.buttonPznl);
        buttonTznl.setTypeface(manifestTypeface);
        buttonDznl.setTypeface(manifestTypeface);
        buttonPznl.setTypeface(manifestTypeface);
        buttonMznl.setTypeface(manifestTypeface);

        if (!imeResursaZaGrb.equals("-")) {
            byte[] decodedString = Base64.decode(imeResursaZaGrb, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            logoSlika.setImageBitmap(decodedByte);
        }
    }

    public void clickedMznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl",URLM);
        startActivity(i);
    }
    public void clickedPznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl",URLP);
        startActivity(i);
    }
    public void clickedDznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl",URLD);
        startActivity(i);
    }
    public void clickedTznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl",URLT);
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
