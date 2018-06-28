package com.proyecto.bean;

public class TipoClienteRegistroBean {

    private String codigo, descripcion;

    public TipoClienteRegistroBean(){}

    public TipoClienteRegistroBean(String codigo, String descripcion){
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
