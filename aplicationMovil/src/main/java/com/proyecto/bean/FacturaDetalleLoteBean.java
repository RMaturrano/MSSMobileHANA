package com.proyecto.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class FacturaDetalleLoteBean implements Parcelable {

    private int claveBase;
    private String lote;
    private double cantidad;
    private double cantidadTemp;
    private int lineaBase;

    public FacturaDetalleLoteBean(){}

    public int getClaveBase() {
        return claveBase;
    }

    public void setClaveBase(int claveBase) {
        this.claveBase = claveBase;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getCantidadTemp() {
        return cantidadTemp;
    }

    public void setCantidadTemp(double cantidadTemp) {
        this.cantidadTemp = cantidadTemp;
    }

    public int getLineaBase() {
        return lineaBase;
    }

    public void setLineaBase(int lineaBase) {
        this.lineaBase = lineaBase;
    }

    protected FacturaDetalleLoteBean(Parcel in) {
        claveBase = in.readInt();
        lote = in.readString();
        cantidad = in.readDouble();
        cantidadTemp = in.readDouble();
        lineaBase = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(claveBase);
        dest.writeString(lote);
        dest.writeDouble(cantidad);
        dest.writeDouble(cantidadTemp);
        dest.writeInt(lineaBase);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FacturaDetalleLoteBean> CREATOR = new Parcelable.Creator<FacturaDetalleLoteBean>() {
        @Override
        public FacturaDetalleLoteBean createFromParcel(Parcel in) {
            return new FacturaDetalleLoteBean(in);
        }

        @Override
        public FacturaDetalleLoteBean[] newArray(int size) {
            return new FacturaDetalleLoteBean[size];
        }
    };
}