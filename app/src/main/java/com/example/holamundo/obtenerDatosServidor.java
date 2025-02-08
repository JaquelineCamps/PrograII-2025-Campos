package com.example.holamundo;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class obtenerDatosServidor {

    private static final String TAG = "obtenerDatosServidor";
    private HttpURLConnection httpURLConnection;

    public String executeRequest() {
        StringBuilder respuesta = new StringBuilder();
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;

        try {
            Log.d(TAG, "Conectando a: " + utilidades.url_consulta);
            URL url = new URL(utilidades.url_consulta);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", "Basic " + utilidades.credencialesCodificadas);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(15000);

            int responseCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "Código de respuesta: " + responseCode);

            if (responseCode != HttpURLConnection.HTTP_OK) {
                return "Error del servidor: " + responseCode;
            }

            inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                respuesta.append(linea);
            }

            Log.d(TAG, "Respuesta: " + respuesta.toString());
            return respuesta.toString();

        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (inputStream != null) inputStream.close();
                if (httpURLConnection != null) httpURLConnection.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Error cerrando recursos: " + e.getMessage());
            }
        }
    }
}