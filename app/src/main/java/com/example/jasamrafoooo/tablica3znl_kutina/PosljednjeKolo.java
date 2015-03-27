package com.example.jasamrafoooo.tablica3znl_kutina;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class PosljednjeKolo extends Activity {

    private static String URL;
    public Posljednje[] konacanRaspored;
    ProgressDialog progress;
    public ListAdapter mojAdapter;
    public boolean pronasaoPodatke = false;
    public String[] mojipodatci = new String[6];
    public int j=0;
    public int i=0;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PosljednjeKolo.this, SplashScreen.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posljednje_kolo);

        //System.setProperty("http.keepAlive", "false");

        Button buttonTablica2 = (Button) findViewById(R.id.buttonTablica2);
        Button buttonPosljednje2 = (Button) findViewById(R.id.buttonPosljednje2);

        ListView predlozak_za_posljednje_kolo = (ListView) findViewById(R.id.predlozak_za_posljednje_kolo);

        AdView adView = (AdView)this.findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("705A531EF2DFC7439759DDD27F57A110")
                .build();
        adView.loadAd(adRequest);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            URL = extras.getString("newUrl");
        }

        buttonTablica2.setBackgroundResource(R.drawable.not_clicked_button);
        buttonTablica2.setEnabled(true);
        buttonPosljednje2.setBackgroundResource(R.drawable.clicked_button);
        buttonPosljednje2.setEnabled(false);

        predlozak_za_posljednje_kolo.setOnTouchListener(new OnSwipeTouchListener(PosljednjeKolo.this){

            public void onSwipeRight() {
                Intent i = new Intent(getApplicationContext(), Tablica.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }

        });

        konacanRaspored = new Posljednje[15];

        //inicijalizacija svih objekata
        for (int k = 0;k<15;k++)
            konacanRaspored[k] = new Posljednje();

        pokreni();

    }

    public void pokreni(){
        progress = ProgressDialog.show(this, "Dohvaćanje podataka",
                "Pričekajte...", true);
        // omogući prekidanje progress dialoga
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(false);
        progress.setOnCancelListener(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog){
                finish();
            }});


        new FetchWebsiteData().execute();
    }
    private class FetchWebsiteData extends AsyncTask<Void, Void, BrojacKlubova> {

        @Override
        protected BrojacKlubova doInBackground(Void... params) {
            BrojacKlubova brojacKlubova = new BrojacKlubova();
            try {
                // Connect to website
                Document document = Jsoup.connect(URL).get();
                Elements podatciRaspored = document.select("td[nowrap=nowrap]");
                for (Element podatakRaspored: podatciRaspored){
                    String tekstPodatka = podatakRaspored.text();
                    //makni višak praznih znakova sa kraja
                    tekstPodatka = tekstPodatka.replaceAll("\\s+$", "");

                    if (pronasaoPodatke){
                        if (j<6) {
                            mojipodatci[j] = tekstPodatka;
                            j++;
                        }
                        else {
                            j = 0;
                            pronasaoPodatke = false;
                            konacanRaspored[i-1] = new Posljednje(
                                    mojipodatci[1], mojipodatci[4], mojipodatci[5]
                            );
                        }
                    }

                    if( tekstPodatka.length() > 2 &&tekstPodatka.charAt(2) == ':'){
                        j = 0;
                        i++;
                        pronasaoPodatke = true;
                    }

                }
                brojacKlubova.brojKlubova = i;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return brojacKlubova;
        }

        @Override
        protected void onPostExecute(BrojacKlubova brojacKlubova) {
            progress.dismiss();
            mojAdapter = new PosljednjeAdapter(getApplicationContext(), konacanRaspored, brojacKlubova.brojKlubova);
            ListView lista = (ListView) findViewById(R.id.predlozak_za_posljednje_kolo);
            lista.setAdapter(mojAdapter);

            if(konacanRaspored[0].getDomacin().equals("E")){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            AlertDialog alert = new AlertDialog.Builder(PosljednjeKolo.this).create();
                            alert.setCancelable(false);


                            alert.setTitle("Problem pri dohvaćanju podataka");
                            alert.setMessage("Provjerite Internet vezu i pokušajte ponovno!");
                            alert.setIcon(android.R.drawable.ic_dialog_alert);
                            alert.setButton2("Pokušaj ponovno", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            pokreni();
                                        }
                                    }
                            );
                            alert.setButton("Izađi", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    }
                            );


                            alert.show();
                        }
                        catch(Exception e){
                        }

                    }
                });
            }
        }
    }

    public void clickedButtonTablica(View view) {
        Intent i = new Intent(getApplicationContext(), Tablica.class);
        //i.putExtra("newUrl", URL);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        progress.dismiss();
    }
}
