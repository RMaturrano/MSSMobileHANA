package com.proyecto.notacredito.adapter.recyclerview;

public class ItemAddCreditNote {

    private String titulo;
    private String contenido;
    private boolean isEditable;

    public ItemAddCreditNote(){contenido = "";}

    public ItemAddCreditNote(String titulo, String contenido, boolean editable){
        this.titulo = titulo;
        this.contenido = contenido;
        this.isEditable = editable;
    }

    public ItemAddCreditNote(String titulo, boolean editable){
        this.titulo = titulo;
        this.contenido = null;
        this.isEditable = editable;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }


}
