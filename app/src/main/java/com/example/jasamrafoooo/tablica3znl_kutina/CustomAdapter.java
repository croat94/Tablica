package com.example.jasamrafoooo.tablica3znl_kutina;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;

public class CustomAdapter extends ArrayAdapter<Momcad>{

    protected Context mContext;
    public int ukupanBrojKlubova;

    //constructor
    public CustomAdapter(Context context, Momcad[] values, int num) {
        super(context, R.layout.predlozak, values);
        mContext = context;
        ukupanBrojKlubova = num;
    }

    @Override
    public int getCount() {
        return ukupanBrojKlubova;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.predlozak, null);
            holder = new ViewHolder();
            holder.imeMomcadi = (TextView) convertView.findViewById(R.id.momcad);
            holder.pozicija = (TextView) convertView.findViewById(R.id.kolo);
            holder.odigranoUtakmica = (TextView) convertView.findViewById(R.id.golGost);
            holder.pobjede = (TextView) convertView.findViewById(R.id.pobjede);
            holder.nerijeseno = (TextView) convertView.findViewById(R.id.nerijeseno);
            holder.izgubljeno = (TextView) convertView.findViewById(R.id.izgubljeno);
            holder.golovi = (TextView) convertView.findViewById(R.id.rezultat);
            holder.golRazlika = (TextView) convertView.findViewById(R.id.golRazlika);
            holder.bodovi = (TextView) convertView.findViewById(R.id.bodovi);
            holder.slika = (ImageView) convertView.findViewById(R.id.slika);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Momcad momcad = getItem(position);

        holder.imeMomcadi.setText(momcad.getImeMomcadi());
        holder.pozicija.setText(momcad.getPozicija());
        holder.odigranoUtakmica.setText(momcad.getOdigranoUtakmica());
        holder.pobjede.setText(momcad.getPobjede());
        holder.nerijeseno.setText(momcad.getNerijeseno());
        holder.izgubljeno.setText(momcad.getIzgubljeno());
        holder.golovi.setText(momcad.getGolovi());
        holder.golRazlika.setText(momcad.getGolRazlika());
        holder.bodovi.setText(momcad.getBodovi());

        String imeResursaZaGrb = PostavljanjeGrbova.postaviGrbove(momcad.getImeMomcadi());

        if (!imeResursaZaGrb.equals("-")) {
            int id = getResId(imeResursaZaGrb);
            holder.slika.setImageResource(id);
        }

        if (momcad.getImeMomcadi().equals("E"))
            return new View(getContext());

        return convertView;
    }

    static class ViewHolder{
        TextView imeMomcadi;
        TextView pozicija;
        TextView odigranoUtakmica;
        TextView pobjede;
        TextView nerijeseno;
        TextView izgubljeno;
        TextView golovi;
        TextView golRazlika;
        TextView bodovi;
        ImageView slika;
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