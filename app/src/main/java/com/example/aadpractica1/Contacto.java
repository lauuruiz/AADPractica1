package com.example.aadpractica1;

public class Contacto {
    private long id;
    private String nombre;
    private String telefono;

    public long getId() {
        return id;
    }

    public Contacto setId(long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Contacto setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Contacto nº: " + id + " - Nombre: " + nombre +" - Telefono: " + telefono+" \n";
        //return "Contacto{" + "id=" + id + ", nombre='" + nombre +"}";
    }
}
