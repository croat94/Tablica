package com.example.jasamrafoooo.tablica3znl_kutina;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;

class CustomAdapter extends ArrayAdapter<Momcad>{

    public ImageView slika;

    //constructor
    CustomAdapter(Context context, Momcad[] foods) {
        super(context, R.layout.predlozak, foods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View customView = buckysInflater.inflate(R.layout.predlozak, parent, false);

        String place = getItem(position).getPozicija();
        String name = getItem(position).getImeMomcadi();
        String played = getItem(position).getOdigranoUtakmica();
        String win = getItem(position).getPobjede();
        String draw = getItem(position).getNerijeseno();
        String lose = getItem(position).getIzgubljeno();
        String goals = getItem(position).getGolovi();
        String diff = getItem(position).getGolRazlika();
        String points = getItem(position).getBodovi();



        TextView momcad = (TextView) customView.findViewById(R.id.momcad);
        TextView pozicija = (TextView) customView.findViewById(R.id.kolo);
        TextView odigranoUtakmica = (TextView) customView.findViewById(R.id.golGost);
        TextView pobjede = (TextView) customView.findViewById(R.id.pobjede);
        TextView nerijeseno = (TextView) customView.findViewById(R.id.nerijeseno);
        TextView izgubljeno = (TextView) customView.findViewById(R.id.izgubljeno);
        TextView golovi = (TextView) customView.findViewById(R.id.rezultat);
        TextView golRazlika = (TextView) customView.findViewById(R.id.golRazlika);
        TextView bodovi = (TextView) customView.findViewById(R.id.bodovi);
        slika = (ImageView) customView.findViewById(R.id.slika);


        momcad.setText(name);
        pozicija.setText(place);
        odigranoUtakmica.setText(played);
        pobjede.setText(win);
        nerijeseno.setText(draw);
        izgubljeno.setText(lose);
        golovi.setText(goals);
        golRazlika.setText(diff);
        bodovi.setText(points);

        String imeResursaZaGrb = PostavljanjeGrbova.postaviGrbove(name);

        if (!imeResursaZaGrb.equals("-")) {
            int id = getResId(imeResursaZaGrb);
            slika.setImageResource(id);
        }

        return customView;
    }



    public int getResId(String variableName) {

        try {
            Class res = R.drawable.class;
            Field idField = res.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}