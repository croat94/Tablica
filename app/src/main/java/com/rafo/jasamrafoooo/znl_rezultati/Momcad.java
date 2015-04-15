package com.rafo.jasamrafoooo.znl_rezultati;

public class Momcad {

    private String imeMomcadi;
    private String pozicija;
    private String odigranoUtakmica;
    private String pobjede;
    private String nerijeseno;
    private String izgubljeno;
    private String golovi;
    private String golRazlika;
    private String bodovi;

    public Momcad(){
        imeMomcadi = "E";
        pozicija = "E";
        odigranoUtakmica = "E";
        pobjede = "E";
        nerijeseno = "E";
        izgubljeno = "E";
        golovi = "E";
        golRazlika = "E";
        bodovi = "E";
    }

    public Momcad(String place, String team, String played, String win, String draw, String lose,
                  String goals, String diff, String points) {
        imeMomcadi = team;
        pozicija = place;
        odigranoUtakmica = played;
        pobjede = win;
        nerijeseno = draw;
        izgubljeno = lose;
        golovi = goals;
        golRazlika = diff;
        bodovi = points;
    }

    public String getImeMomcadi() {
        return imeMomcadi;
    }

    public String getOdigranoUtakmica() {
        return odigranoUtakmica;
    }

    public String getPozicija() {
        return pozicija;
    }

    public String getPobjede() {
        return pobjede;
    }

    public String getNerijeseno() {
        return nerijeseno;
    }

    public String getIzgubljeno() {
        return izgubljeno;
    }

    public String getGolovi() {
        return golovi;
    }

    public String getGolRazlika() {
        return golRazlika;
    }

    public String getBodovi() {
        return bodovi;
    }
}
