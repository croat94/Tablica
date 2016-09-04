package com.rafo.jasamrafoooo.znl_rezultati;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Tablica extends FragmentActivity {

    private String URL;
    private ProgressDialog progress;
    private ListAdapter mojAdapter;
    public TextView txtView;
    private boolean prethodnoPozvanPosljednje = false;
    private List<Momcad> teamOrderList = new ArrayList<>();

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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            URL = extras.getString("newUrl");
        }
        System.setProperty("http.keepAlive", "false");

        if (URL.equals("nodata")) {
            setContentView(R.layout.activity_tablica_no_data);
        } else {
            setContentView(R.layout.activity_tablica);
            Button buttonTablica1 = (Button) findViewById(R.id.buttonTablica1);
            Button buttonPosljednje1 = (Button) findViewById(R.id.buttonPosljednje1);
            ListView predlozak = (ListView) findViewById(R.id.predlozak_tablica_redak);
            //postavi reklamu
            setUpAd();
            buttonTablica1.setBackgroundResource(R.drawable.active);
            buttonTablica1.setEnabled(false);
            buttonPosljednje1.setBackgroundResource(R.drawable.not_active);
            buttonPosljednje1.setEnabled(true);
            //omogući slideanje lijevo-desno između tablice i posljednjeg kola
            predlozak.setOnTouchListener(new OnSwipeTouchListener(Tablica.this) {
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
            glavniPosao();
        }
    }

    public void glavniPosao() {

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

        boolean spojen = isNetworkAvailable();
        if (!spojen) {
            internetNeRadiAlertDialog();
        } else {
            new FetchWebsiteData().execute();
        }
    }

    private class FetchWebsiteData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to website
                Document document = Jsoup.connect(URL).get();
                teamOrderList = WebDataManipulator.sokolTable(document);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (teamOrderList.size()>0) {
                mojAdapter = new TablicaAdapter(getApplicationContext(),
                        teamOrderList);
                txtView = (TextView) findViewById(R.id.momcad);
                progress.dismiss();
                ListView lista = (ListView) findViewById(R.id.predlozak_tablica_redak);
                lista.setAdapter(mojAdapter);
                lista.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                prikaziRasporedKluba(position);
                            }
                        }
                );
            }else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        neuspjesanDohvatAlertDialog();
                    }
                });
            }
        }
    }

    public void prikaziRasporedKluba(int position) {
        String imeKluba = "";
        for (int n = 0; n < teamOrderList.size(); n++) {
            if (position == n) {
                if (n != 0)
                    imeKluba = teamOrderList.get(position).getImeMomcadi();
                else
                    imeKluba = teamOrderList.get(0).getImeMomcadi();
            }
        }
        Intent i = new Intent(Tablica.this, RasporedUtakmica.class);
        i.putExtra("team", imeKluba);
        i.putExtra("URL", URL);
        startActivity(i);
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void internetNeRadiAlertDialog() {
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
        } catch (Exception e) {
        }
    }

    public void neuspjesanDohvatAlertDialog() {
        try {
            AlertDialog alert = new AlertDialog.Builder(Tablica.this).create();
            alert.setCancelable(false);

            alert.setTitle("Slaba Internetska veza!");
            alert.setMessage("Provjerite Internet vezu i pokušajte ponovno!");
            alert.setIcon(android.R.drawable.ic_dialog_alert);
            alert.setButton2("Pokušaj ponovno", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            glavniPosao();
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
        AdView adView = (AdView) this.findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("705A531EF2DFC7439759DDD27F57A110")
                .build();
        adView.loadAd(adRequest);
    }

}