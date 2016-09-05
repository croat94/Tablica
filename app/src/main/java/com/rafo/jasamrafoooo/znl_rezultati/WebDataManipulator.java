package com.rafo.jasamrafoooo.znl_rezultati;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafo on 4.9.2016..
 */
public class WebDataManipulator {

    public static List<Momcad> sokolTable(Document document) {
        List<Momcad> teamOrderList = new ArrayList<>();
        boolean flag = false, prvi = true;
        String[] teamData = new String[9];
        int i = 0, j = 0;

        Elements podatci = document.select("td[valign=top]");
        for (Element podatak : podatci) {
            if (!flag) {
                String tekstPodatka = podatak.text();
                //makni višak praznih znakova sa kraja
                tekstPodatka = tekstPodatka.replaceAll("\\s+$", "");
                if ((tekstPodatka.indexOf('(')) == 0 && ((tekstPodatka.indexOf(')')) == 2
                        || (tekstPodatka.indexOf(')')) == 3)) {
                    flag = true;
                    j = 1;
                    i++;
                    teamData[0] = String.valueOf(i);
                } else if (j != 9) {
                    //ubaci podatke o momcadi u polje
                    teamData[j] = tekstPodatka;
                    j++;
                    //kada doznaš sve podatke, dodaj momcad u listu
                    if (j == 9) {
                        if (prvi)
                            prvi = false;
                        else
                            teamOrderList.add(new Momcad(
                                    teamData[0], teamData[1], teamData[2],
                                    teamData[3], teamData[4], teamData[5],
                                    teamData[6], teamData[7], teamData[8]
                            ));
                    }
                }
            } else
                flag = false;
        }
        return teamOrderList;
    }

    public static List<Kolo> sokolRaspored(Document document) {
        boolean pronasaoPodatke = false;
        String[] data = new String[6];
        List<Kolo> matches = new ArrayList<Kolo>();
        int brojKola = 0;
        String privremena = " ";
        int odredivanjeSlobodnogKola = 0;
        int j = 0;

        Elements podatciRaspored = document.select("td[align]");
        for (Element podatakRaspored : podatciRaspored) {
            String tekstPodatka = podatakRaspored.text();
            //makni višak praznih znakova sa kraja
            tekstPodatka = tekstPodatka.replaceAll("\\s+$", "");
            //kada nađeš datum, tada znaš da slijede podatci
            if (pronasaoPodatke) {
                if (j < 6) {
                    data[j] = tekstPodatka;
                    j++;
                } else {
                    j = 0;
                    pronasaoPodatke = false;
                    matches.add(new Kolo(
                            brojKola, data[2], data[4],
                            data[5], data[0], data[1]
                    ));
                }
            }

            if (tekstPodatka.length() > 5 && (tekstPodatka.charAt(2) == '.' && tekstPodatka.charAt(5) == '.')) {
                //ovo mi na neki cudan nacin skuzi da se radi o slobodnom kolu
                j = 1;
                brojKola = Integer.valueOf(privremena);
                if (brojKola > odredivanjeSlobodnogKola + 1) {
                    matches.add(new Kolo(
                            brojKola - 1, "E", "E", "E", "E", "E"));
                }
                odredivanjeSlobodnogKola = brojKola;
                data[0] = tekstPodatka;
                pronasaoPodatke = true;
            }
            privremena = tekstPodatka;
        }
        return matches;
    }

    public static List<Posljednje> sokolPosljednjeKolo(Document document) {
        int j = 0;
        boolean pronasaoPodatke = false;
        String[] data = new String[6];
        List<Posljednje> posljednjeList = new ArrayList<>();

        Elements podatciRaspored = document.select("td[nowrap=nowrap]");
        for (Element podatakRaspored : podatciRaspored) {
            String tekstPodatka = podatakRaspored.text();
            //makni višak praznih znakova sa kraja
            tekstPodatka = tekstPodatka.replaceAll("\\s+$", "");
            if (pronasaoPodatke) {
                if (j < 6) {
                    data[j] = tekstPodatka;
                    j++;
                } else {
                    j = 0;
                    pronasaoPodatke = false;
                    posljednjeList.add(new Posljednje(
                            data[1], data[4], data[5]
                    ));
                }
            } else if (tekstPodatka.length() > 2 && tekstPodatka.charAt(2) == ':') {
                j = 0;
                pronasaoPodatke = true;
            }

        }
        return posljednjeList;
    }

    public static int sokolPosljednjeKoloBroj(Document document) {
        int brojPosljednjegKola = 0;
        Elements podatciOKolu = document.select("option");
        for (Element podatakOKolu : podatciOKolu) {
            String pomText = podatakOKolu.text();
            pomText = pomText.replaceAll("\\s+$", "");
            if (pomText.substring(0, 1).matches("0"))
                pomText = pomText.substring(1, 2);
            brojPosljednjegKola = Integer.parseInt(pomText);
        }
        return brojPosljednjegKola;
    }

    public static int sokolPosljednjeKoloPrikazano(Document document) {
        int brojPrikazanogKola = 0;
        Elements podatciOKolu = document.select("option");
        podatciOKolu = document.select("td[class=contentheading]");
        int pomocnaVarijabla = 0;
        for (Element podatakOKolu : podatciOKolu) {
            if (pomocnaVarijabla == 0) {
                String pomText = podatakOKolu.text();
                pomText = pomText.substring(10, 12);
                if (pomText.contains("."))
                    pomText = pomText.substring(0, 1);
                brojPrikazanogKola = Integer.parseInt(pomText);
            }
            pomocnaVarijabla = 1;
        }
        return brojPrikazanogKola;
    }
}
