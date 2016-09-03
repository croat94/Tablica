package com.rafo.jasamrafoooo.znl_rezultati.util;

/**
 * Created by Rafo on 2.9.2016..
 */
public class ContextSettings {

    public static String URLM = "";// = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,87%20/Itemid,529/";
    public static String URLP = "";// = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,99%20/Itemid,582/";
    public static String URLD = "";// = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,89%20/Itemid,578/";
    public static String URLT = "";// = "http://www.nk-sokol.hr/component/option,com_joomleague/func,showResultsRank/p,100%20/Itemid,582/";

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
}
