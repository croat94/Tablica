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
import android.os.Handler;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;


public class Tablica extends Activity {

    public String URL;
    public static final String URLM = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,70%20/Itemid,529/";
    public static final String URLP = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,71/Itemid,514/";
    public static final String URLD = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,72%20/Itemid,535/";
    public static final String URLT = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,73%20/Itemid,538/";
    public int broj;
    ProgressDialog progress;
    public ListAdapter mojAdapter;
    public String[] mojipodatci = new String[9];
    public int i = 0;
    public int j;
    public boolean flag = false;
    public boolean prvi = true;
    public Momcad[] konacanPoredak;
    public String rasporedURL;
    TextView txtView;
    public boolean prethodnoPozvanPosljednje = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Handler handler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablica);
        Button buttonTablica1 = (Button) findViewById(R.id.buttonTablica1);
        Button buttonPosljednje1 = (Button) findViewById(R.id.buttonPosljednje1);

        ListView predlozak = (ListView) findViewById(R.id.predlozak);


        URL = URLM;
        broj = 16;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            URL = extras.getString("newUrl");
            broj = extras.getInt("newNumberOfTeams");
        }


        buttonTablica1.setBackgroundResource(R.drawable.clicked_button);
        buttonTablica1.setEnabled(false);
        buttonPosljednje1.setBackgroundResource(R.drawable.not_clicked_button);
        buttonPosljednje1.setEnabled(true);

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

        konacanPoredak = new Momcad[broj];

        glavniPosao(URL, broj);


    }

    public void glavniPosao(String URL, int broj){

        progress = ProgressDialog.show(this, "Dohvaćanje podataka",
                "Pričekajte...", true);


        mojAdapter = new CustomAdapter(this, konacanPoredak);

        //inicijalizacija svih objekata
        for (int k = 0;k<broj;k++)
            konacanPoredak[k] = new Momcad();

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
        else
            new FetchWebsiteData().execute();


    }

    private class FetchWebsiteData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to website
                Document document = Jsoup.connect(URL).get();

                Elements podatci = document.select("td[valign=top]");
                for (Element podatak : podatci) {
                    if (!flag) {
                        String tekstPodatka = podatak.text();
                        //makni višak praznih znakova sa kraja
                        tekstPodatka = tekstPodatka.replaceAll("\\s+$", "");
                        //Log.i("MOJ TAG", "-" + tekstPodatka + "-");

                        if ((tekstPodatka.indexOf('(')) == 0 && ( (tekstPodatka.indexOf(')')) == 2 || (tekstPodatka.indexOf(')') ) == 3)) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            txtView = (TextView) findViewById(R.id.momcad);
            progress.dismiss();
            ListView lista = (ListView) findViewById(R.id.predlozak);
            lista.setAdapter(mojAdapter);
            if(konacanPoredak[1].getBodovi().equals("E")){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            AlertDialog alert = new AlertDialog.Builder(Tablica.this).create();
                            alert.setCancelable(false);

                            alert.setTitle("Slaba Internetska veza!");
                            alert.setMessage("Provjerite Internet vezu i pokušajte ponovno!");
                            alert.setIcon(android.R.drawable.ic_dialog_alert);
                            alert.setButton2("Pokušaj ponovno", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    glavniPosao(URL, broj);
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
                });
            }

            //prikaži dosadašnje utakmice odabranog kluba
            lista.setOnItemClickListener(
                    new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String imeKluba = "";
                            for (int n = 0; n<broj; n++){
                                if(position == n){
                                    if (n!=0)
                                        imeKluba = konacanPoredak[position].getImeMomcadi();
                                    else
                                        imeKluba = konacanPoredak[0].getImeMomcadi();
                                }
                            }

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
}