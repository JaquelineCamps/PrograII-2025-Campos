package com.example.holamundo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    Button btn;
    TextView tempVal;
    FloatingActionButton fab;
    utilidades utls;
    String id = "", rev = "", idAmigo = "1", accion = "nuevo";

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

    void guardarAmigos() {
        try {
            // Validar campos
            tempVal = findViewById(R.id.txtnombre);
            String nombre = tempVal.getText().toString().trim();
            if (nombre.isEmpty()) {
                mostrarMsg("El nombre es requerido");
                return;
            }

            tempVal = findViewById(R.id.txttelefono);
            String telefono = tempVal.getText().toString().trim();
            if (telefono.isEmpty()) {
                mostrarMsg("El teléfono es requerido");
                return;
            }

            tempVal = findViewById(R.id.txtemail);
            String email = tempVal.getText().toString().trim();
            if (email.isEmpty()) {
                mostrarMsg("El email es requerido");
                return;
            }

            final JSONObject datosAmigos = new JSONObject();
            if (accion.equals("modificar")) {
                datosAmigos.put("_id", id);
                datosAmigos.put("_rev", rev);
            }
            datosAmigos.put("idAmigo", idAmigo);
            datosAmigos.put("nombre", nombre);
            datosAmigos.put("telefono", telefono);
            datosAmigos.put("email", email);

            Log.d(TAG, "Datos a enviar: " + datosAmigos.toString());

            // Crear nuevo hilo para la operación de red
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        enviarDatosServidor objEnviarDatosServidor = new enviarDatosServidor(getApplicationContext());
                        final String respuesta = objEnviarDatosServidor.doInBackground(datosAmigos.toString());

                        // Volver al hilo principal para actualizar la UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                procesarRespuestaServidor(respuesta);
                            }
                        });
                    } catch (Exception e) {
                        final String errorMsg = e.getMessage();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mostrarMsg("Error: " + errorMsg);
                                Log.e(TAG, "Error al guardar amigo: " + errorMsg);
                            }
                        });
                    }
                }
            }).start();

        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
            Log.e(TAG, "Error al preparar datos: " + e.getMessage());
        }
    }

    private void procesarRespuestaServidor(String respuesta) {
        Log.d(TAG, "Respuesta del servidor: " + respuesta);
        try {
            JSONObject respuestaJson = new JSONObject(respuesta);
            if (respuestaJson.has("ok") && respuestaJson.getBoolean("ok")) {
                mostrarMsg("Amigo guardado exitosamente");
                regresarListaAmigos();
            } else {
                mostrarMsg("Error al guardar: " + respuesta);
                Log.e(TAG, "Error al guardar: " + respuesta);
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
            Log.e(TAG, "Error al procesar respuesta del servidor: " + e.getMessage());
        }
    }

    void mostrarMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    void regresarListaAmigos() {
        Intent intent = new Intent(getApplicationContext(), listado_amigos.class);
        startActivity(intent);
    }
}