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

import com.rafo.jasamrafoooo.znl_rezultati.util.ContextSettings;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PosljednjeKolo extends Activity {

    private static String originalURL;
    public String URL;
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
                URL = originalURL + "/r," + spinnerKolo.getSelectedItem().toString().replace(". kolo","");
                Log.i("MOJ TAG URL", URL);
                if (!String.valueOf(brojPrikazanogKola).equals(spinnerKolo.getSelectedItem().toString().replace(". kolo","")))
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

        ContextSettings.setUpAd(this, R.id.adView3);
        pokreni();
    }

    public void pokreni() {
        ContextSettings.showLoader(this, R.id.imageView6);
        buttonProsloKolo.setEnabled(true);
        buttonSljedeceKolo.setEnabled(true);
        i = 0;
        new FetchWebsiteData(this).execute();
    }

    private class FetchWebsiteData extends AsyncTask<Void, Void, Void> {

        public PosljednjeKolo activity;

        public FetchWebsiteData(PosljednjeKolo a)
        {
            this.activity = a;
        }

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
            ContextSettings.hideLoader(activity, R.id.imageView6);
            if (posljednjeList.size() > 0) {
                mojAdapter = new PosljednjeAdapter(getApplicationContext(), posljednjeList);
                ListView lista = (ListView) findViewById(R.id.predlozak_za_posljednje_kolo);
                lista.setAdapter(mojAdapter);
                dodajSvaKolaUListu(brojPosljednjegKola);
                provjeriJeLiPrvoIliZadnje();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        neuspjesanDohvatAlertDialog();
                    }
                });
            }
        }
    }

    public void dodajSvaKolaUListu(int maxBrojKola) {
        ArrayList<String> lista = new ArrayList<String>(maxBrojKola);
        for (int brojac = 0; brojac < maxBrojKola; brojac++) {
            lista.add(brojac + 1 + ". kolo");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinnerKolo.setAdapter(adapter);
        spinnerKolo.setSelection(brojPrikazanogKola - 1);
    }

    public void clickedButtonTablica(View view) {
        Intent i = new Intent(getApplicationContext(), Tablica.class);
        //i.putExtra("newUrl", URL);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
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

    @Override
    protected void onPause() {
        super.onPause();
        ContextSettings.hideLoader(this, R.id.imageView6);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(0, 0);
    }
}
