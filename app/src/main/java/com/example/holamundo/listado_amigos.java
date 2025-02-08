package com.example.holamundo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class listado_amigos extends Activity {
    private static final String TAG = "listado_amigos";
    Bundle parametros = new Bundle();
    FloatingActionButton fab;
    ListView lts;
    amigos misAmigos;
    final ArrayList<amigos> amigosArrayList = new ArrayList<>();
    JSONArray jsonArray;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_amigos);

        fab = findViewById(R.id.fabAgregarAmigos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parametros.putString("accion", "nuevo");
                abrirActividad(parametros);
            }
        });
        obtenerDatosAmigos();
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void abrirActividad(Bundle parametros) {
        Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        abrirActividad.putExtras(parametros);
        startActivity(abrirActividad);
    }

    private void obtenerDatosAmigos() {
        new ObtenerDatosAmigosTask().execute();
    }

    private class ObtenerDatosAmigosTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            obtenerDatosServidor datosServidor = new obtenerDatosServidor();
            return datosServidor.executeRequest();
        }

        @Override
        protected void onPostExecute(String resultado) {
            Log.d(TAG, "Resultado del servidor: " + resultado);
            if (resultado != null) {
                try {
                    jsonObject = new JSONObject(resultado);
                    jsonArray = jsonObject.getJSONArray("rows");
                    Log.d(TAG, "Número de filas obtenidas: " + jsonArray.length());
                    mostrarDatosAmigos();
                } catch (Exception e) {
                    mostrarMsg("Error al procesar JSON: " + e.getMessage());
                    Log.e(TAG, "Error al procesar JSON: " + e.getMessage());
                }
            } else {
                mostrarMsg("No se pudieron obtener datos del servidor");
                Log.e(TAG, "Resultado del servidor es nulo");
            }
        }
    }

    private void mostrarDatosAmigos() {
        try {
            Log.d(TAG, "Mostrando datos de amigos...");
            if (jsonArray.length() > 0) {
                lts = findViewById(R.id.ltsAmigos);
                amigosArrayList.clear();

                JSONObject misDatosJsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    misDatosJsonObject = jsonArray.getJSONObject(i).getJSONObject("key");
                    Log.d(TAG, "Datos del amigo: " + misDatosJsonObject.toString());
                    misAmigos = new amigos(
                            misDatosJsonObject.getString("_id"),
                            misDatosJsonObject.getString("_rev"),
                            misDatosJsonObject.getString("idAmigo"),
                            misDatosJsonObject.getString("nombre"),
                            misDatosJsonObject.getString("telefono"),
                            misDatosJsonObject.getString("email")
                    );
                    amigosArrayList.add(misAmigos);
                }
                adaptadorAmigos adapterAmigos = new adaptadorAmigos(listado_amigos.this, amigosArrayList);
                lts.setAdapter(adapterAmigos);
                registerForContextMenu(lts);
            } else {
                mostrarMsg("No hay datos que mostrar...");
                Log.d(TAG, "No hay datos que mostrar...");
                parametros.putString("accion", "nuevo");
                abrirActividad(parametros);
            }
        } catch (Exception e) {
            mostrarMsg("Error al mostrar: " + e.getMessage());
            Log.e(TAG, "Error al mostrar: " + e.getMessage());
        }
    }
}