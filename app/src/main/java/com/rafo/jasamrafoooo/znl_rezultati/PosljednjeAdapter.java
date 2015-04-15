package com.rafo.jasamrafoooo.znl_rezultati;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;

class PosljednjeAdapter extends ArrayAdapter<Posljednje> {

    protected Context mContext;
    public int ukupanBrojKlubova;

    //constructor
    PosljednjeAdapter(Context context, Posljednje[] foods, int num) {
        super(context, R.layout.predlozak_za_posljednje_kolo, foods);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.predlozak_za_posljednje_kolo, null);
            holder = new ViewHolder();
            holder.domacin = (TextView) convertView.findViewById(R.id.domacin);
            holder.rezultat = (TextView) convertView.findViewById(R.id.rezultat);
            holder.gost = (TextView) convertView.findViewById(R.id.gost);
            holder.domacinSlika = (ImageView) convertView.findViewById(R.id.domacinSlika);
            holder.gostSlika = (ImageView) convertView.findViewById(R.id.gostSlika);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Posljednje posljednje = getItem(position);

        String home = posljednje.getDomacin();
        String away = posljednje.getGost();
        String score = posljednje.getRezultat();

        //makni rezultat s poluvremena
        if (score.contains("(")){
            int index = score.indexOf("(");
            score = score.substring(0, index-1);
        }

        holder.domacin.setText(home);
        holder.gost.setText(away);
        holder.rezultat.setText(score);

        String imeResursaZaGrbDomacina = PostavljanjeGrbova.postaviGrbove(home);
        String imeResursaZaGrbGosta = PostavljanjeGrbova.postaviGrbove(away);

        if (!imeResursaZaGrbDomacina.equals("-")) {
            int id = getResId(imeResursaZaGrbDomacina);
            holder.domacinSlika.setImageResource(id);
        }

        if (!imeResursaZaGrbGosta.equals("-")) {
            int id = getResId(imeResursaZaGrbGosta);
            holder.gostSlika.setImageResource(id);
        }
        return convertView;
    }

    static class ViewHolder{
        TextView domacin;
        TextView rezultat;
        TextView gost;
        ImageView domacinSlika;
        ImageView gostSlika;
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
