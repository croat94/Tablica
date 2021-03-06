package com.rafo.jasamrafoooo.znl_rezultati;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rafo.jasamrafoooo.znl_rezultati.util.ContextSettings;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RasporedUtakmica extends Activity {

    public String imeKluba;
    public String URL;
    public ListAdapter mojAdapter;
    public String rasporedURL;
    public ImageView slikaKluba;
    private List<Kolo> matches = new ArrayList<Kolo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raspored_utakmica);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imeKluba = extras.getString("team");
            URL = extras.getString("URL");
        }
        System.setProperty("http.keepAlive", "false");
        TextView imeEkipe = (TextView) findViewById(R.id.imeEkipe);
        slikaKluba = (ImageView) findViewById(R.id.slikaKluba);
        ContextSettings.setUpAd(this, R.id.adView4);
        imeEkipe.setText(imeKluba);
        postaviSliku();
        pokreni();
    }

    public void pokreni() {
        ContextSettings.showLoader(this, R.id.imageView7);
        new FetchWebsiteData(this).execute();
    }

    private class FetchWebsiteData extends AsyncTask<Void, Void, Void> {

        public RasporedUtakmica activity;

        public FetchWebsiteData(RasporedUtakmica a)
        {
            this.activity = a;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(URL).get();
                Elements podatci = document.select("td[valign=top]");
                String rasporedURL = dohvatiLinkZaRaspored(podatci);
                Document docRaspored = Jsoup.connect(rasporedURL).get();
                matches = WebDataManipulator.sokolRaspored(docRaspored);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ContextSettings.hideLoader(activity, R.id.imageView7);
            if (matches.size() > 0) {
                mojAdapter = new RasporedAdapter(getApplicationContext(), matches);
                ListView lista = (ListView) findViewById(R.id.predlozak_za_raspored);
                lista.setAdapter(mojAdapter);
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        neuspjesanDohvatAlertDialog();
                    }
                });
            }
        }
    }

    public String dohvatiLinkZaRaspored(Elements podatci) {
        for (Element podatak : podatci) {
            String tekstPodatka = podatak.text();
            tekstPodatka = tekstPodatka.replaceAll("\\s+$", "");

            if (tekstPodatka.equals(imeKluba)) {
                int brojac = 0;
                Elements links = podatak.select("a[href]");
                for (Element link : links) {
                    if (brojac < 2)
                        brojac++;
                    else {
                        rasporedURL = link.attr("href");
                        break;
                    }
                }
            }
        }
        return rasporedURL;
    }

    public void neuspjesanDohvatAlertDialog() {
        try {
            AlertDialog alert = new AlertDialog.Builder(RasporedUtakmica.this).create();
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
        } catch (Exception e) {
        }
    }

    public void postaviSliku() {
        String imeResursaZaGrb = FetchStartData.postaviGrbove(imeKluba);

        if (!imeResursaZaGrb.equals("-")) {
            byte[] decodedString = Base64.decode(imeResursaZaGrb, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            slikaKluba.setImageBitmap(decodedByte);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ContextSettings.hideLoader(this, R.id.imageView7);
    }

}