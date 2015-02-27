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

class PosljednjeAdapter extends ArrayAdapter<Posljednje> {

    public ImageView domacinSlika;
    public ImageView gostSlika;

    //constructor
    PosljednjeAdapter(Context context, Posljednje[] foods) {
        super(context, R.layout.predlozak_za_posljednje_kolo, foods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater buckysInflater = LayoutInflater.from(getContext());
        View customView = buckysInflater.inflate(R.layout.predlozak_za_posljednje_kolo, parent, false);

        String home = getItem(position).getDomacin();
        String away = getItem(position).getGost();
        String score = getItem(position).getRezultat();

        TextView domacin = (TextView) customView.findViewById(R.id.domacin);
        TextView rezultat = (TextView) customView.findViewById(R.id.rezultat);
        TextView gost = (TextView) customView.findViewById(R.id.gost);
        domacinSlika = (ImageView) customView.findViewById(R.id.domacinSlika);
        gostSlika = (ImageView) customView.findViewById(R.id.gostSlika);

        domacin.setText(home);
        gost.setText(away);
        rezultat.setText(score);

        String imeResursaZaGrbDomacina = PostavljanjeGrbova.postaviGrbove(home);
        String imeResursaZaGrbGosta = PostavljanjeGrbova.postaviGrbove(away);

        if (!imeResursaZaGrbDomacina.equals("-")) {
            int id = getResId(imeResursaZaGrbDomacina);
            domacinSlika.setImageResource(id);
        }

        if (!imeResursaZaGrbGosta.equals("-")) {
            int id = getResId(imeResursaZaGrbGosta);
            gostSlika.setImageResource(id);
        }

        Inflater inflater = new Inflater();
        if (home.equals("E"))
            return new View(getContext());
        //return inflater.inflate(R.layout.null_item, null);



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
