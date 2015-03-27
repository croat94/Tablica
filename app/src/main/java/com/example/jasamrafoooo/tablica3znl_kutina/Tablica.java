package com.example.jasamrafoooo.tablica3znl_kutina;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;


public class Tablica extends Activity {

    public String URL;
    public int maxBrojKlubova = 20;
    ProgressDialog progress;
    public ListAdapter mojAdapter;
    public String[] mojipodatci = new String[9];
    public int i = 0;
    public int n;
    public int j;
    public boolean flag = false;
    public boolean prvi = true;
    public Momcad[] konacanPoredak;
    TextView txtView;
    public boolean prethodnoPozvanPosljednje = false;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Tablica.this, SplashScreen.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablica);
        Button buttonTablica1 = (Button) findViewById(R.id.buttonTablica1);
        Button buttonPosljednje1 = (Button) findViewById(R.id.buttonPosljednje1);

        AdView adView = (AdView)this.findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("705A531EF2DFC7439759DDD27F57A110")
                .build();
        adView.loadAd(adRequest);

        ListView predlozak = (ListView) findViewById(R.id.predlozak);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            URL = extras.getString("newUrl");
        }

        buttonTablica1.setBackgroundResource(R.drawable.clicked_button);
        buttonTablica1.setEnabled(false);
        buttonPosljednje1.setBackgroundResource(R.drawable.not_clicked_button);
        buttonPosljednje1.setEnabled(true);


        //omogući slideanje lijevo-desno između tablice i posljednjeg kola
        predlozak.setOnTouchListener(new OnSwipeTouchListener(Tablica.this){

            public void onSwipeLeft() {
                Intent i = new Intent(getApplicationContext(), PosljednjeKolo.class);
                i.putExtra("newUrl", URL);
                if (prethodnoPozvanPosljednje)
                    i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                else
                    prethodnoPozvanPosljednje = true;
                startActivity(i);
            }

        });
        konacanPoredak = new Momcad[maxBrojKlubova];
        glavniPosao(URL, maxBrojKlubova);
    }

    public void glavniPosao(String URL, int maxBrojKlubova){

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
        //inicijalizacija svih objekata
        for (int k = 0;k<maxBrojKlubova;k++)
            konacanPoredak[k] = new Momcad();

        boolean spojen = isNetworkAvailable();
        if (!spojen){
            internetNeRadiAlertDialog();
        }
        else
            new FetchWebsiteData().execute();
    }

    private class FetchWebsiteData extends AsyncTask<Void, Void, BrojacKlubova> {

        @Override
        protected BrojacKlubova doInBackground(Void... params) {
            BrojacKlubova brojacKlubova = new BrojacKlubova();
            try {
                // Connect to website
                Document document = Jsoup.connect(URL).get();
                Elements podatci = document.select("td[valign=top]");
                for (Element podatak : podatci) {
                    if (!flag) {
                        String tekstPodatka = podatak.text();
                        //makni višak praznih znakova sa kraja
                        tekstPodatka = tekstPodatka.replaceAll("\\s+$", "");

                        if ((tekstPodatka.indexOf('(')) == 0 && ( (tekstPodatka.indexOf(')')) == 2
                                || (tekstPodatka.indexOf(')') ) == 3)) {
                            flag = true;
                            j = 1;
                            i++;
                            mojipodatci[0] = String.valueOf(i);
                        }

                        else if (j != 9) {
                            //ubaci podatke o momcadi u polje
                            mojipodatci[j] = tekstPodatka;
                            j++;

                            //kada doznaš sve podatke, napravi novi objekt za tu momcad
                            if (j == 9) {
                                if (prvi)
                                    prvi = false;
                                else
                                    konacanPoredak[i-1] = new Momcad(
                                            mojipodatci[0], mojipodatci[1], mojipodatci[2],
                                            mojipodatci[3], mojipodatci[4], mojipodatci[5],
                                            mojipodatci[6], mojipodatci[7], mojipodatci[8]
                                    );
                            }
                        }
                    }
                    else
                        flag = false;
                }
                brojacKlubova.brojKlubova = i;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return brojacKlubova;
        }

        @Override
        protected void onPostExecute(BrojacKlubova brojacKlubova) {
            mojAdapter = new CustomAdapter(getApplicationContext(), konacanPoredak, brojacKlubova.brojKlubova);
            txtView = (TextView) findViewById(R.id.momcad);
            progress.dismiss();
            ListView lista = (ListView) findViewById(R.id.predlozak);
            lista.setAdapter(mojAdapter);
            if(konacanPoredak[1].getBodovi().equals("E")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        neuspjesanDohvatAlertDialog();
                    }
                });
            }
            //prikaži dosadašnje utakmice odabranog kluba
            lista.setOnItemClickListener(
                    new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String imeKluba = dohvatiImeMomcadi(position);
                            Intent i = new Intent(Tablica.this, RasporedUtakmica.class);
                            i.putExtra("team", imeKluba);
                            i.putExtra("URL", URL);
                            startActivity(i);
                        }
                    }
            );
        }
    }

    //provjeri je li spojen na internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void clickedButtonPosljednje(View view) {
        Intent i = new Intent(getApplicationContext(), PosljednjeKolo.class);
        i.putExtra("newUrl", URL);
        if (prethodnoPozvanPosljednje)
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        else
            prethodnoPozvanPosljednje = true;
        startActivity(i);
    }

    public void internetNeRadiAlertDialog(){
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

    public void neuspjesanDohvatAlertDialog(){
        try {
            AlertDialog alert = new AlertDialog.Builder(Tablica.this).create();
            alert.setCancelable(false);

            alert.setTitle("Slaba Internetska veza!");
            alert.setMessage("Provjerite Internet vezu i pokušajte ponovno!");
            alert.setIcon(android.R.drawable.ic_dialog_alert);
            alert.setButton2("Pokušaj ponovno", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            glavniPosao(URL, maxBrojKlubova);
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
        catch(Exception e){}
    }

    public String dohvatiImeMomcadi(int position){
        String imeKluba = "";
        for (int n = 0; n<maxBrojKlubova; n++){
            if(position == n){
                if (n!=0)
                    imeKluba = konacanPoredak[position].getImeMomcadi();
                else
                    imeKluba = konacanPoredak[0].getImeMomcadi();
            }
        }
        return imeKluba;
    }
}