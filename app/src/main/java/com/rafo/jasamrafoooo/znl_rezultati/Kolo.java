package com.rafo.jasamrafoooo.znl_rezultati;

public class Kolo {

    private int kolo;
    private String domacin;
    private String gost;
    private String rezultat;
    private String datum;
    private String vrijeme;

    public Kolo(){
        kolo = 0;
        domacin = "E";
        gost = "E";
        rezultat = "E";
        datum = "E";
        vrijeme = "E";
    }

    public Kolo(int num, String home, String away, String score, String date, String time) {
        kolo = num;
        domacin = home;
        gost = away;
        rezultat = score;
        datum = date;
        vrijeme = time;
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

    public String getVrijeme() {
        return vrijeme;
    }
}
