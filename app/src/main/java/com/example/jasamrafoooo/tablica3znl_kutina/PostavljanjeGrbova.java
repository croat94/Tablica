package com.example.jasamrafoooo.tablica3znl_kutina;

public class PostavljanjeGrbova{

    public static String nista = "-";

    public static String postaviGrbove(String imeKluba) {

        switch (imeKluba) {
            case "Banovac":
                return "banovac_burned";
            case "Vinogradar":
                return "vinogradar_burned";
            case "Dinamo O.O.":
                return "dinamo_oo_burned";
            case "Gradići":
                return "gradici_burned";
            case "Karlovac 1919":
                return "karlovac_1919_burned";
            case "Udarnik":
                return "udarnik_burned";
            case "Moslavina":
                return "moslavina_burned";
            case "Savski Marof":
                return "savski_marof_burned";
            case "Ogulin":
                return "ogulin_burned";
            case "Ponikve":
                return "ponikve_burned";
            case "Mladost Z.":
                return "mladost_z_burned";
            case "Libertas":
                return "libertas_burned";
            case "Klas":
                return "klas";
            case "Radoboj":
                return "radoboj_burned";
            case "Kustošija":
                return "kustosija_burned";
            case "Lekenik":
                return "lekenik_burned";

            //-------------1.ŽNL---------------------------

            case "Moslavac":
                return "moslavac_burned";
            case "Sokol":
                return "sokol_burned";
            case "Mladost G.":
                return "mladost_gg_burned";

            //-------------2.ŽNL---------------------------

            case "Dinamo (Os)":
                return "dinamo_osekovo_burned";
            case "Metalac M.":
                return "metalac_burned";
            case "Mladost R.":
                return "mladost_r_burned";
            case "Strijelac":
                return "strijelac_burned";
            case "Lokomotiva":
                return "lokomotiva_burned";

            //-------------3.ŽNL---------------------------
            case "Šartovac":
                return "sartovac_burned";
            case "Brinjani":
                return "brinjani";
            case "Dinamo Kt.":
                return "dinamo_krc";
            case "Matija Gubec":
                return "matija_gubec_burned";
            case "Moslavina D.G.":
                return "moslavina_dg_burned";
            case "Selište":
                return "seliste_burned";
            case "Vatrogasac":
                return "vatrogasac_burned";

        }


    return nista;
    }

}
