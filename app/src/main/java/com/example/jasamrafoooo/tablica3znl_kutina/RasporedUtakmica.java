package com.example.jasamrafoooo.tablica3znl_kutina;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Field;


public class RasporedUtakmica extends Activity {

    public String imeKluba;
    public String URL;
    public ListAdapter mojAdapter;
    public String rasporedURL;
    public String[] mojipodatci = new String[6];
    public int j=0;
    public int brojKola = 0;
    public boolean pronasaoPodatke = false;
    public Kolo[] konacanRaspored;
    ProgressDialog progress;
    public String privremena=" ";
    public ImageView slikaKluba;
    public int odredivanjeSlobodnogKola = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raspored_utakmica);

        System.setProperty("http.keepAlive", "false");

        TextView imeEkipe = (TextView) findViewById(R.id.imeEkipe);
        slikaKluba = (ImageView) findViewById(R.id.slikaKluba);

        AdView adView = (AdView)this.findViewById(R.id.adView4);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("705A531EF2DFC7439759DDD27F57A110")
                .build();
        adView.loadAd(adRequest);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imeKluba = extras.getString("team");
            URL = extras.getString("URL");
        }
        imeEkipe.setText(imeKluba);
        postaviSliku();
        konacanRaspored = new Kolo[35];
        //inicijalizacija svih objekata
        for (int k = 0;k<35;k++)
            konacanRaspored[k] = new Kolo();
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
                //pronađi link na kojem se nalazi raspored utakmica
                Elements podatci = document.select("td[valign=top]");
                rasporedURL = dohvatiLinkZaRaspored(podatci);
                //spoji se na novi link i preuzmi podatke
                Document docRaspored =  Jsoup.connect(rasporedURL).get();
                Elements podatciRaspored = docRaspored.select("td[align]");
                obradiPodatke(podatciRaspored);
                brojacKlubova.brojKlubova = brojKola; //koristi se brojacKlubova, ali to samo sluzi da se odredi broj kola
            } catch (IOException e) {
                e.printStackTrace();
            }
            return brojacKlubova;
        }

        @Override
        protected void onPostExecute(BrojacKlubova brojacKlubova) {
            progress.dismiss();
            mojAdapter = new AdapterRaspored(getApplicationContext(), konacanRaspored, brojacKlubova.brojKlubova);
            ListView lista = (ListView) findViewById(R.id.predlozak_za_raspored);
            lista.setAdapter(mojAdapter);

            if(konacanRaspored[0].getDatum().equals("E") && konacanRaspored[1].getDatum().equals("E")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        neuspjesanDohvatAlertDialog();
                    }
                });
            }
        }
    }

    public void postaviSliku(){
        String imeResursaZaGrb = PostavljanjeGrbova.postaviGrbove(imeKluba);

        if (!imeResursaZaGrb.equals("-")) {
            int id = getResId(imeResursaZaGrb);
            slikaKluba.setImageResource(id);
        }
    }

    public int getResId(String variableName) {

        try {
            Class res = R.drawable.class;
            Field idField = res.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        progress.dismiss();
    }

    public void neuspjesanDohvatAlertDialog(){
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
        }
        catch(Exception e){
        }
    }

    public void obradiPodatke(Elements podatciRaspored){

        for (Element podatakRaspored: podatciRaspored){
            String tekstPodatka = podatakRaspored.text();
            //makni višak praznih znakova sa kraja
            tekstPodatka = tekstPodatka.replaceAll("\\s+$", "");
            //kada nađeš datum, tada znaš da slijede podatci
            if (pronasaoPodatke){
                if (j<6) {
                    mojipodatci[j] = tekstPodatka;
                    j++;
                }
                else {
                    j = 0;
                    pronasaoPodatke = false;
                    konacanRaspored[brojKola-1] = new Kolo(
                            brojKola, mojipodatci[2], mojipodatci[4],
                            mojipodatci[5], mojipodatci[0], mojipodatci[1]
                    );
                }
            }

            if( tekstPodatka.length()>5 && (tekstPodatka.charAt(2) == '.' && tekstPodatka.charAt(5) == '.')){
                j = 1;
                brojKola = Integer.valueOf(privremena);
                if (brojKola > odredivanjeSlobodnogKola + 1){
                    konacanRaspored[odredivanjeSlobodnogKola] = new Kolo(
                            brojKola-1, "E", "E","E", "E", "E");
                }
                odredivanjeSlobodnogKola = brojKola;
                mojipodatci[0] = tekstPodatka;
                pronasaoPodatke = true;
            }
            privremena = tekstPodatka;
        }
    }

    public String dohvatiLinkZaRaspored(Elements podatci){
        for (Element podatak : podatci) {
            String tekstPodatka = podatak.text();
            tekstPodatka = tekstPodatka.replaceAll("\\s+$", "");

            if (tekstPodatka.equals(imeKluba)){
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

}