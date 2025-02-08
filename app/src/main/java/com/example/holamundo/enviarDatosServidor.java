package com.example.holamundo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class enviarDatosServidor extends AsyncTask<String, String, String> {
    private static final String TAG = "enviarDatosServidor";
    private Context context;
    private HttpURLConnection httpURLConnection;

    public enviarDatosServidor(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... parametros) {
        String jsonResponse = null;
        String jsonDatos = parametros[0];
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        Writer writer = null;

        try {
            Log.d(TAG, "Enviando datos a: " + utilidades.url_mto);
            URL url = new URL(utilidades.url_mto);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestProperty("Authorization", "Basic " + utilidades.credencialesCodificadas);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(15000);

            // Enviar datos
            writer = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
            writer.write(jsonDatos);
            writer.flush();

            int responseCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "Código de respuesta: " + responseCode);

            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
                return "Error del servidor: " + responseCode;
            }

            inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                response.append(linea);
            }

            jsonResponse = response.toString();
            Log.d(TAG, "Respuesta: " + jsonResponse);

        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        } finally {
            try {
                if (writer != null) writer.close();
                if (bufferedReader != null) bufferedReader.close();
                if (inputStream != null) inputStream.close();
                if (httpURLConnection != null) httpURLConnection.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Error cerrando recursos: " + e.getMessage());
            }
        }
        return jsonResponse;
    }
}
