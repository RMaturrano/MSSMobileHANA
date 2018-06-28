package com.proyecto.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class NotaCreditoDetalleLoteBean implements Parcelable {

    private String claveBase;
    private String lote;
    private double cantidad;
    private int lineaBase;

    public NotaCreditoDetalleLoteBean(){}

    public String getClaveBase() {
        return claveBase;
    }

    public void setClaveBase(String claveBase) {
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

    public int getLineaBase() {
        return lineaBase;
    }

    public void setLineaBase(int lineaBase) {
        this.lineaBase = lineaBase;
    }

    protected NotaCreditoDetalleLoteBean(Parcel in) {
        claveBase = in.readString();
        lote = in.readString();
        cantidad = in.readDouble();
        lineaBase = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(claveBase);
        dest.writeString(lote);
        dest.writeDouble(cantidad);
        dest.writeInt(lineaBase);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NotaCreditoDetalleLoteBean> CREATOR = new Parcelable.Creator<NotaCreditoDetalleLoteBean>() {
        @Override
        public NotaCreditoDetalleLoteBean createFromParcel(Parcel in) {
            return new NotaCreditoDetalleLoteBean(in);
        }

        @Override
        public NotaCreditoDetalleLoteBean[] newArray(int size) {
            return new NotaCreditoDetalleLoteBean[size];
        }
    };
}