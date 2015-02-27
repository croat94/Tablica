package com.example.jasamrafoooo.tablica3znl_kutina;

public class Kolo {

    private int kolo;
    private String domacin;
    private String gost;
    private String rezultat;
    private String datum;

    public Kolo(){
        kolo = 0;
        domacin = "E";
        gost = "E";
        rezultat = "E";
        datum = "E";
    }

    public Kolo(int num, String home, String away, String score, String date) {
        kolo = num;
        domacin = home;
        gost = away;
        rezultat = score;
        datum = date;
    }

    public int getKolo() {
        return kolo;
    }

    public String getDomacin() {
        return domacin;
    }

    public String getGost() {
        return gost;
    }

    public String getRezultat() {
        return rezultat;
    }

    public String getDatum() {
        return datum;
    }
}
