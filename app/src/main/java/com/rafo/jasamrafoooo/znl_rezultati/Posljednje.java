package com.rafo.jasamrafoooo.znl_rezultati;

public class Posljednje {

    private String domacin;
    private String gost;
    private String rezultat;

    public Posljednje(){
        domacin = "E";
        gost = "E";
        rezultat = "E";
    }

    public Posljednje(String home, String away, String score) {
        domacin = home;
        gost = away;
        rezultat = score;
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
}
