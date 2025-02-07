package com.example.holamundo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
public class MainActivity extends Activity {
    Button btn;
    TextView tempVal;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btnGuardarAmigos);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarAmigos();
            }
        });
        fab = findViewById(R.id.fabRegresarlistaAmigos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresarListaAmigos();
            }
        });
    }
    void guardarAmigos(){
        tempVal = findViewById(R.id.txtnombre);
        String nombre = tempVal.getText().toString();
        tempVal = findViewById(R.id.txtdireccion);
        String direccion = tempVal.getText().toString();
        tempVal = findViewById(R.id.txttelefono);
        String telefono = tempVal.getText().toString();
        tempVal = findViewById(R.id.txtemail);
        String email = tempVal.getText().toString();
        tempVal = findViewById(R.id.txtdui);
        String dui = tempVal.getText().toString();
        DB db = new DB(getApplicationContext(), "", null, 1);
        String resp = db.administrar_amigos("nuevo", new String[]{
                "",nombre, direccion, telefono, email, dui
        });
        if( resp.equals("ok") ){
            mostrarMsg("Amigo almacenado con exito...");
            regresarListaAmigos();
        }
    }
    void regresarListaAmigos(){
        Intent abrirActividad = new Intent(getApplicationContext(), listado_amigos.class);
        startActivity(abrirActividad);
    }
    void mostrarMsg(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}
