package com.tit.oxigenapp;

public class ListaUsuario {
    String id;
    String nombre;

    public ListaUsuario (String id, String nombre) {
    //public ListaUsuario (String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString () {
        return nombre;
    }
}
