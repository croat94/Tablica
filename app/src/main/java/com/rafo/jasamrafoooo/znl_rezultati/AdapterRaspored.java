package com.rafo.jasamrafoooo.znl_rezultati;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.lang.reflect.Field;

class AdapterRaspored extends ArrayAdapter<Kolo> {

    protected Context mContext;
    public int ukupanBrojKlubova;

    //constructor
    AdapterRaspored(Context context, Kolo[] foods, int num) {
        super(context, R.layout.predlozak_za_raspored, foods);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.predlozak_za_raspored, null);
            holder = new ViewHolder();
            holder.kolo = (TextView) convertView.findViewById(R.id.kolo);
            holder.domacin = (TextView) convertView.findViewById(R.id.domacin);
            holder.rezultat = (TextView) convertView.findViewById(R.id.rezultat);
            holder.gost = (TextView) convertView.findViewById(R.id.gost);
            holder.datum = (TextView) convertView.findViewById(R.id.datum);
            holder.vrijeme = (TextView) convertView.findViewById(R.id.vrijeme);
            holder.slikaDomacin = (ImageView) convertView.findViewById(R.id.domacinSlika);
            holder.slikaGost = (ImageView) convertView.findViewById(R.id.gostSlika);
            holder.slobodnoKolo = (TextView) convertView.findViewById(R.id.slobodnoKolo);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        Kolo kolo = getItem(position);


        int num = kolo.getKolo();
        String home = kolo.getDomacin();
        String away = kolo.getGost();
        String score = kolo.getRezultat();
        String date = kolo.getDatum();
        String time = kolo.getVrijeme();
        //provjeri je li slobodan to kolo
        if (!home.equals("E")) {
            holder.kolo.setText(String.valueOf(num));
            holder.domacin.setText(home);
            holder.gost.setText(away);
            holder.rezultat.setText(score);
            holder.datum.setText(date + '.');
            holder.vrijeme.setText(time);
            holder.slobodnoKolo.setText("");

            String imeResursaZaGrbDomacina = PostavljanjeGrbova.postaviGrbove(home);
            String imeResursaZaGrbGosta = PostavljanjeGrbova.postaviGrbove(away);

            if (!imeResursaZaGrbDomacina.equals("-")) {
                byte[] decodedString = Base64.decode(imeResursaZaGrbDomacina, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.slikaDomacin.setImageBitmap(decodedByte);
            }else{
                holder.slikaDomacin.setImageBitmap(null);
            }

            if (!imeResursaZaGrbGosta.equals("-")) {
                byte[] decodedString = Base64.decode(imeResursaZaGrbGosta, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.slikaGost.setImageBitmap(decodedByte);
            }else{
                holder.slikaGost.setImageBitmap(null);
            }
        }
        else{
            holder.slobodnoKolo.setText("   Slobodno kolo");
            holder.kolo.setText(String.valueOf(num));
            holder.domacin.setText("");
            holder.gost.setText("");
            holder.rezultat.setText("");
            holder.datum.setText("");
            holder.vrijeme.setText("");
            holder.slikaDomacin.setImageBitmap(null);
            holder.slikaGost.setImageBitmap(null);
        }

        return convertView;
    }

    static class ViewHolder{
        TextView kolo;
        TextView domacin;
        TextView rezultat;
        TextView gost;
        TextView datum;
        TextView vrijeme;
        TextView slobodnoKolo;
        ImageView slikaDomacin;
        ImageView slikaGost;
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
