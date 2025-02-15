package com.Jackye.holamundo;

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

import org.json.JSONObject;

public class MainActivity extends Activity {
    Button btn;
    TextView tempVal;
    FloatingActionButton fab;
    utilidades utls;
    String id="", rev="", idAmigo="", accion="nuevo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utls = new utilidades();
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
        mostrarDatosAmigos();
    }
    void mostrarDatosAmigos(){
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");
            if (accion.equals("modificar")) {
                JSONObject datosAmigos = new JSONObject(parametros.getString("amigos")).getJSONObject("key");
                id = datosAmigos.getString("_id");
                rev = datosAmigos.getString("_rev");
                idAmigo = datosAmigos.getString("idAmigo");

                tempVal = findViewById(R.id.txtnombre);
                tempVal.setText(datosAmigos.getString("nombre"));

                tempVal = findViewById(R.id.txtdireccion);
                tempVal.setText(datosAmigos.getString("direccion"));

                tempVal = findViewById(R.id.txttelefono);
                tempVal.setText(datosAmigos.getString("telefono"));

                tempVal = findViewById(R.id.txtemail);
                tempVal.setText(datosAmigos.getString("email"));

                tempVal = findViewById(R.id.txtdui);
                tempVal.setText(datosAmigos.getString("dui"));
            }else{
                idAmigo = utls.generarUnicoId();
            }
        }catch (Exception e){
            tempVal = findViewById(R.id.lblDepuracion);
            tempVal.setText(e.getMessage());
            mostrarMsg("Error al mostrar datos: "+e.getMessage());
        }
    }
    void guardarAmigos(){
        try {
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

            JSONObject datosAmigos = new JSONObject();
            if (accion.equals("modificar")) {
                datosAmigos.put("_id", id);
                datosAmigos.put("_rev", rev);
            }
            datosAmigos.put("idAmigo", idAmigo);
            datosAmigos.put("nombre", nombre);
            datosAmigos.put("direccion", direccion);
            datosAmigos.put("telefono", telefono);
            datosAmigos.put("email", email);
            datosAmigos.put("dui", dui);

            //enviar los datos al servidor.
            enviarDatosServidor objEnviarDatosServidor = new enviarDatosServidor(getApplicationContext());
            String respuesta = objEnviarDatosServidor.execute(datosAmigos.toString(), "POST", utilidades.url_mto).get();

            JSONObject respuestaJsonObject = new JSONObject(respuesta);
            if (respuestaJsonObject.getBoolean("ok")) {
                id = respuestaJsonObject.getString("id");
                rev = respuestaJsonObject.getString("rev");
                mostrarMsg("Amigo almacenado con exito...");
                regresarListaAmigos();
            }else{
                mostrarMsg("Error al intentar guardar datos de amigos"+ respuesta);
            }
        }catch (Exception e){
            tempVal = findViewById(R.id.lblDepuracion);
            tempVal.setText(e.getMessage());
            mostrarMsg("Error: "+e.getMessage());
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