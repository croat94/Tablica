package com.example.jasamrafoooo.tablica3znl_kutina;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class SplashScreen extends Activity {

    public static final String URLM = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,70%20/Itemid,529/";
    public static final String URLP = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,71/Itemid,514/";
    public static final String URLD = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,72%20/Itemid,535/";
    public static final String URLT = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,73%20/Itemid,538/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }


    public void clickedMznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl",URLM);
        i.putExtra("newNumberOfTeams", 16);
        startActivity(i);
    }
    public void clickedPznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl",URLP);
        i.putExtra("newNumberOfTeams", 14);
        startActivity(i);
    }
    public void clickedDznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl",URLD);
        i.putExtra("newNumberOfTeams" , 12);
        startActivity(i);
    }
    public void clickedTznl(View view){
        Intent i = new Intent(SplashScreen.this, Tablica.class);
        i.putExtra("newUrl",URLT);
        i.putExtra("newNumberOfTeams" , 13);
        startActivity(i);
    }
}
