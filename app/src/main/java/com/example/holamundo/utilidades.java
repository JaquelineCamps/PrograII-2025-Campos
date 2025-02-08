package com.example.holamundo;

import java.util.Base64;

public class utilidades {
    static String url_consulta = "http://10.0.2.2:5984/agenda/_design/agenda/_view/agenda";
    static String url_mto = "http://10.0.2.2:5984/agenda";
    static String user = "admin";
    static String clave = "12345";
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user + ":" + clave).getBytes());

    public String generarUnicoId() {
        return java.util.UUID.randomUUID().toString();
    }
}