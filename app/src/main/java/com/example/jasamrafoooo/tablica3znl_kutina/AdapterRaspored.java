package com.example.jasamrafoooo.tablica3znl_kutina;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.zip.Inflater;

class AdapterRaspored extends ArrayAdapter<Kolo> {

    public ImageView slikaDomacin;
    public ImageView slikaGost;

    //constructor
    AdapterRaspored(Context context, Kolo[] foods) {
        super(context, R.layout.predlozak_za_raspored, foods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View customView = buckysInflater.inflate(R.layout.predlozak_za_raspored, parent, false);

        int num = getItem(position).getKolo();
        String home = getItem(position).getDomacin();
        String away = getItem(position).getGost();
        String score = getItem(position).getRezultat();
        String date = getItem(position).getDatum();
        String time = getItem(position).getVrijeme();

        TextView kolo = (TextView) customView.findViewById(R.id.kolo);
        TextView domacin = (TextView) customView.findViewById(R.id.domacin);
        TextView rezultat = (TextView) customView.findViewById(R.id.rezultat);
        TextView gost = (TextView) customView.findViewById(R.id.gost);
        TextView datum = (TextView) customView.findViewById(R.id.datum);
        TextView vrijeme = (TextView) customView.findViewById(R.id.vrijeme);
        slikaDomacin = (ImageView) customView.findViewById(R.id.domacinSlika);
        slikaGost = (ImageView) customView.findViewById(R.id.gostSlika);

        kolo.setText(String.valueOf(num));
        domacin.setText(home);
        gost.setText(away);
        rezultat.setText(score);
        datum.setText(date + '.');
        vrijeme.setText(time);

        String imeResursaZaGrbDomacina = PostavljanjeGrbova.postaviGrbove(home);
        String imeResursaZaGrbGosta = PostavljanjeGrbova.postaviGrbove(away);

        if (!imeResursaZaGrbDomacina.equals("-")) {
            int id = getResId(imeResursaZaGrbDomacina);
            slikaDomacin.setImageResource(id);
        }

        if (!imeResursaZaGrbGosta.equals("-")) {
            int id = getResId(imeResursaZaGrbGosta);
            slikaGost.setImageResource(id);
        }


        Inflater inflater = new Inflater();
        if (num == 0)
            return new View(getContext());

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
