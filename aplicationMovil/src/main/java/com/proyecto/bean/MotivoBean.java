package com.proyecto.bean;

/**
 * Created by PC on 20/03/2018.
 */

public class MotivoBean {

    private int id;
    private String descripcion, valOrden, valEntrega, valFactura;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValOrden() {
        return valOrden;
    }

    public void setValOrden(String valOrden) {
        this.valOrden = valOrden;
    }

    public String getValEntrega() {
        return valEntrega;
    }

    public void setValEntrega(String valEntrega) {
        this.valEntrega = valEntrega;
    }

    public String getValFactura() {
        return valFactura;
    }

    public void setValFactura(String valFactura) {
        this.valFactura = valFactura;
    }
}
