package com.example.holamundo;

public class amigos {
    private String _id;
    private String _rev;
    private String idAmigo;
    private String nombre;
    private String telefono;
    private String email;

    public amigos() {
        // Constructor vacío
    }

    public amigos(String _id, String _rev, String idAmigo, String nombre, String telefono, String email) {
        this._id = _id;
        this._rev = _rev;
        this.idAmigo = idAmigo;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    // Getters y Setters
    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }

    public String get_rev() { return _rev; }
    public void set_rev(String _rev) { this._rev = _rev; }

    public String getIdAmigo() { return idAmigo; }
    public void setIdAmigo(String idAmigo) { this.idAmigo = idAmigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Amigo{" +
                "nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}