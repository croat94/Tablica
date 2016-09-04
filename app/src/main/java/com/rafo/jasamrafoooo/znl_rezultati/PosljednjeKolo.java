package com.rafo.jasamrafoooo.znl_rezultati;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PosljednjeKolo extends Activity {

    private static String originalURL;
    public String URL;
    ProgressDialog progress;
    public ListAdapter mojAdapter;
    public int i = 0;
    public int brojPrikazanogKola;
    public int brojPosljednjegKola;
    public Spinner spinnerKolo;
    public Button buttonProsloKolo;
    public Button buttonSljedeceKolo;
    public List<Posljednje> posljednjeList = new ArrayList<>();

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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            originalURL = extras.getString("newUrl");
            URL = originalURL;
        }
        System.setProperty("http.keepAlive", "false");
        Button buttonTablica2 = (Button) findViewById(R.id.buttonTablica2);
        Button buttonPosljednje2 = (Button) findViewById(R.id.buttonPosljednje2);
        buttonTablica2.setBackgroundResource(R.drawable.not_active);
        buttonTablica2.setEnabled(true);
        buttonPosljednje2.setBackgroundResource(R.drawable.active);
        buttonProsloKolo = (Button) findViewById(R.id.buttonProsloKolo);
        buttonSljedeceKolo = (Button) findViewById(R.id.buttonSljedeceKolo);
        spinnerKolo = (Spinner) findViewById(R.id.spinnerKolo);
        ListView predlozak_za_posljednje_kolo = (ListView) findViewById(R.id.predlozak_za_posljednje_kolo);
        spinnerKolo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                URL = originalURL + "/r," + spinnerKolo.getSelectedItem().toString();
                Log.i("MOJ TAG URL", URL);
                if (brojPrikazanogKola != spinnerKolo.getSelectedItem())
                    pokreni();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        predlozak_za_posljednje_kolo.setOnTouchListener(new OnSwipeTouchListener(PosljednjeKolo.this) {

            public void onSwipeRight() {
                Intent i = new Intent(getApplicationContext(), Tablica.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }

        });
        setUpAd();
        pokreni();
    }

    public void pokreni() {
        progress = ProgressDialog.show(this, "Dohvaćanje podataka",
                "Pričekajte...", true);
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(false);
        progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        buttonProsloKolo.setEnabled(true);
        buttonSljedeceKolo.setEnabled(true);
        i = 0;
        new FetchWebsiteData().execute();
    }

    private class FetchWebsiteData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to website
                Document document = Jsoup.connect(URL).get();
                brojPosljednjegKola = WebDataManipulator.sokolPosljednjeKoloBroj(document);
                brojPrikazanogKola = WebDataManipulator.sokolPosljednjeKoloPrikazano(document);
                posljednjeList = WebDataManipulator.sokolPosljednjeKolo(document);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progress.dismiss();
            if (posljednjeList.size() > 0) {
                mojAdapter = new PosljednjeAdapter(getApplicationContext(), posljednjeList);
                ListView lista = (ListView) findViewById(R.id.predlozak_za_posljednje_kolo);
                lista.setAdapter(mojAdapter);
                dodajSvaKolaUListu(brojPosljednjegKola);
                provjeriJeLiPrvoIliZadnje();
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

    public void clickedButtonTablica(View view) {
        Intent i = new Intent(getApplicationContext(), Tablica.class);
        //i.putExtra("newUrl", URL);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }

    public void dodajSvaKolaUListu(int maxBrojKola) {
        ArrayList<Integer> lista = new ArrayList<Integer>(maxBrojKola);
        for (int brojac = 0; brojac < maxBrojKola; brojac++) {
            lista.add(brojac + 1);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, lista);
        spinnerKolo.setAdapter(adapter);
        spinnerKolo.setSelection(brojPrikazanogKola - 1);
    }

    public void onClickButtonProsloKolo(View view) {
        spinnerKolo.setSelection(brojPrikazanogKola - 1 - 1);
    }

    public void onClickButtonSljedeceKolo(View view) {
        spinnerKolo.setSelection(brojPrikazanogKola - 1 + 1);
    }

    public void clickedButtonPosljednje(View view) {
        URL = originalURL;
        pokreni();
    }

    public void provjeriJeLiPrvoIliZadnje() {
        if (spinnerKolo.getSelectedItem() == 1) {
            buttonProsloKolo.setEnabled(false);
        } else if (spinnerKolo.getSelectedItem() == brojPosljednjegKola) {
            buttonSljedeceKolo.setEnabled(false);
        }
    }

    public void neuspjesanDohvatAlertDialog() {
        try {
            AlertDialog alert = new AlertDialog.Builder(PosljednjeKolo.this).create();
            alert.setCancelable(false);

            alert.setTitle("Problem pri dohvaćanju podataka");
            alert.setMessage("Dohvat podataka nije uspio!");
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

    private void setUpAd() {
        AdView adView = (AdView) this.findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("705A531EF2DFC7439759DDD27F57A110")
                .build();
        adView.loadAd(adRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        progress.dismiss();
    }
}
