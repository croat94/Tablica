package com.rafo.jasamrafoooo.znl_rezultati;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;


public class SplashScreen extends Activity {

    public static final String URLM = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,70%20/Itemid,529/";
    public static final String URLP = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,71/Itemid,514/";
    public static final String URLD = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,72%20/Itemid,535/";
    public static final String URLT = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,73%20/Itemid,538/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("705A531EF2DFC7439759DDD27F57A110")
                .build();
        adView.loadAd(adRequest);
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
