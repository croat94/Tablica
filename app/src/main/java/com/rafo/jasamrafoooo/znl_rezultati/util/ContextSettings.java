package com.rafo.jasamrafoooo.znl_rezultati.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

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
    private static AnimationDrawable animation;

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
                .addTestDevice("42C4D63EDF8A9179437D91F46C4ABF05") //dodati id svih uredaja na kojima se testira
                .build();
        adView.loadAd(adRequest);
    }

    public static void showLoader(Activity activity, int id){
        ImageView image;
        image = (ImageView) activity.findViewById(id);
        image.setImageDrawable(activity.getResources().getDrawable(R.drawable.animation));
        animation = (AnimationDrawable) image.getDrawable();
        animation.start();
    }

    public static void hideLoader(Activity activity, int id){
        ImageView image;
        image = (ImageView) activity.findViewById(id);
        animation.stop();
        image.setImageDrawable(null);
    }
}
