package com.rafo.jasamrafoooo.znl_rezultati.util;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rafo.jasamrafoooo.znl_rezultati.R;

/**
 * Created by Rafo on 2.9.2016..
 */
public class ContextSettings {

    public static String URLM = "";
    public static String URLP = "";
    public static String URLD = "";
    public static String URLT = "";
    public static String URLJ = "";

    public static String getURLM() {
        return URLM;
    }

    public static void setURLM(String URLM) {
        if (ContextSettings.URLM.equals(""))
            ContextSettings.URLM = URLM;
    }

    public static String getURLP() {
        return URLP;
    }

    public static void setURLP(String URLP) {
        if (ContextSettings.URLP.equals(""))
            ContextSettings.URLP = URLP;
    }

    public static String getURLD() {
        return URLD;
    }

    public static void setURLD(String URLD) {
        if (ContextSettings.URLD.equals(""))
            ContextSettings.URLD = URLD;
    }

    public static String getURLT() {
        return URLT;
    }

    public static void setURLT(String URLT) {
        if (ContextSettings.URLT.equals(""))
            ContextSettings.URLT = URLT;
    }

    public static String getURLJ() {
        return URLJ;
    }

    public static void setURLJ(String URLJ) {
        ContextSettings.URLJ = URLJ;
    }

    public static void setUpAd(Activity activity, int id){
        AdView adView = (AdView)activity.findViewById(id);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("AF8BE4CB49854EC57FD72F2C4B84859") //dodati id svih uredaja na kojima se testira
                .build();
        adView.loadAd(adRequest);
    }
}
