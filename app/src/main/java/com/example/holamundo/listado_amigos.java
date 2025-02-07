package com.example.holamundo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class listado_amigos extends Activity {
    FloatingActionButton fab;
    ListView lts;
    Cursor cursorAmigos;
    DB db;
    amigos misAmigos;
    final ArrayList<amigos> amigosArrayList = new ArrayList<amigos>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_amigos);

        fab = findViewById(R.id.fabAgregarAmigos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividad();
            }
        });
        obtenerDatosAmigos();
    }
    private void abrirActividad(){
        Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(abrirActividad);
    }
    private void obtenerDatosAmigos(){
        try{
            amigosArrayList.clear();

            db = new DB(getApplicationContext(), "", null, 1);
            cursorAmigos = db.consultar_amigos();

            if( cursorAmigos.moveToFirst() ){
                lts = findViewById(R.id.ltsAmigos);
                do{
                    misAmigos = new amigos(
                            cursorAmigos.getString(0),//idAmigo
                            cursorAmigos.getString(1),//nombre
                            cursorAmigos.getString(2),//direccion
                            cursorAmigos.getString(3),//telefono
                            cursorAmigos.getString(4),//email
                            cursorAmigos.getString(5) //dui
                    );
                    amigosArrayList.add(misAmigos);
                }while (cursorAmigos.moveToNext());
                adaptadorAmigos adaterAmigos = new adaptadorAmigos(listado_amigos.this, amigosArrayList);
                lts.setAdapter(adaterAmigos);
                registerForContextMenu(lts);
            }else{
                mostrarMsg("No hay datos que mostrar...");
                abrirActividad();
            }
        }catch (Exception e){
            mostrarMsg(e.getMessage());
        }
    }
    void mostrarMsg(String msg){
        Toast.makeText(listado_amigos.this, msg, Toast.LENGTH_LONG).show();
    }
}