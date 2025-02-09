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

    private void obtenerDatosAmigos() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String resultado = new obtenerDatosServidor().executeRequest();

                    // Volver al hilo principal para actualizar la UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "JSON completo: " + resultado);

                            if (resultado != null) {
                                try {
                                    jsonObject = new JSONObject(resultado);
                                    jsonArray = jsonObject.getJSONArray("rows");
                                    Log.d(TAG, "Estructura del jsonArray: " + jsonArray.toString());
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
                    });
                } catch (Exception e) {
                    final String errorMsg = e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarMsg("Error: " + errorMsg);
                            Log.e(TAG, "Error en la obtención de datos: " + errorMsg);
                        }
                    });
                }
            }
        }).start();
    }

    // El resto de los métodos permanecen igual
    private void mostrarMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void abrirActividad(Bundle parametros) {
        Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        abrirActividad.putExtras(parametros);
        startActivity(abrirActividad);
    }

    private void mostrarDatosAmigos() {
        try {
            Log.d(TAG, "Mostrando datos de amigos...");
            if (jsonArray.length() > 0) {
                lts = findViewById(R.id.ltsAmigos);
                amigosArrayList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject row = jsonArray.getJSONObject(i);
                    JSONObject key = row.getJSONObject("key");

                    Log.d(TAG, "Row " + i + ": " + row.toString());
                    Log.d(TAG, "Key " + i + ": " + key.toString());

                    misAmigos = new amigos(
                            key.optString("_id", ""),
                            key.optString("_rev", ""),
                            key.optString("idAmigo", ""),
                            key.optString("nombre", ""),
                            key.optString("telefono", ""),
                            key.optString("email", "")
                    );
                    amigosArrayList.add(misAmigos);
                }

                adaptadorAmigos adapterAmigos = new adaptadorAmigos(listado_amigos.this, amigosArrayList);
                lts.setAdapter(adapterAmigos);
                registerForContextMenu(lts);
            } else {
                mostrarMsg("No hay datos que mostrar...");
                parametros.putString("accion", "nuevo");
                abrirActividad(parametros);
            }
        } catch (Exception e) {
            mostrarMsg("Error al mostrar: " + e.getMessage());
            Log.e(TAG, "Error al mostrar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}