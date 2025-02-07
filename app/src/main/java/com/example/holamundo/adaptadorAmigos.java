package com.example.holamundo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class adaptadorAmigos extends BaseAdapter {
    Context context;
    ArrayList<amigos> amigosArrayList;
    amigos misAmigos;
    LayoutInflater layoutInflater;

    public adaptadorAmigos(Context context, ArrayList<amigos> amigosArrayList) {
        this.context = context;
        this.amigosArrayList = amigosArrayList;
    }

    @Override
    public int getCount() {
        return amigosArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return amigosArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(amigosArrayList.get(position).getIdAmigo());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.listview_amigos, parent, false);

        try{
            misAmigos = amigosArrayList.get(position);

            TextView tempVal = itemView.findViewById(R.id.lblNombreAmigo);
            tempVal.setText(misAmigos.getNombre());

            tempVal = itemView.findViewById(R.id.lblTelefonoAmigo);
            tempVal.setText(misAmigos.getTelefono());

            tempVal = itemView.findViewById(R.id.lblEmailAmigo);
            tempVal.setText(misAmigos.getEmail());
        }catch (Exception e){
            Toast.makeText(context, "Error al mostrar los datos en el listview: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }
}
