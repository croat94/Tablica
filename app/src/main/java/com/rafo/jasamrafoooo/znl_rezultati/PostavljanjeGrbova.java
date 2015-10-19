package com.rafo.jasamrafoooo.znl_rezultati;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PostavljanjeGrbova{

    public static String nista = "-";
    public static List<String[]> grboviKlubova = new ArrayList<String[]>();

    public PostavljanjeGrbova(){
        if (grboviKlubova.size() <= 0)
            new FetchWebsiteData().execute();
    }

    public static String postaviGrbove(String imeKluba) {
        for (int i = 0; i<grboviKlubova.size(); i++){
            if (imeKluba.equals(grboviKlubova.get(i)[0])){
                return grboviKlubova.get(i)[1];
            }
        }
        return nista;
    }

    private class FetchWebsiteData extends AsyncTask<Void, Void, List<String[]>> {

        @Override
        protected List<String[]> doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect("http://justpaste.it/o1o2").get();
                Element podatak = document.select("div#articleContent").first();
                try {
                    grboviKlubova.clear();
                    String tekst = podatak.toString();
                    tekst = tekst.replace("<span id=\"\"></span>", "");
                    String[] podatciOKlubovima = tekst.split("!!!!!!!!!!");
                    for (String podatakOKlubu : podatciOKlubovima) {
                        String[] vrijednostiZaJedanKlub = podatakOKlubu.split("##########");
                        grboviKlubova.add(vrijednostiZaJedanKlub);
                    }
                }catch (Exception e){
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return grboviKlubova;
        }
    }

}