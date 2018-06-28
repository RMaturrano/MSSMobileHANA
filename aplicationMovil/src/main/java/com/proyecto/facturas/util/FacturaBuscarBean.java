package com.proyecto.facturas.util;


import android.os.Parcel;
import android.os.Parcelable;

public class FacturaBuscarBean implements Parcelable {

    private String clave;
    private String codigoCliente;
    private String nombreCliente;
    private String referencia;
    private String total;
    private String vencimiento;

    public FacturaBuscarBean(){};

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getSerie(){
        String serie = "";

        try {
            if(this.referencia != null && !this.referencia.equals("")){
                serie = this.referencia.split("-")[0];
            }
        }catch(Exception e){
            serie = "";
        }

        return serie;
    }

    public int getCorrelativo(){
        int correlativo = 0;

        try{
            if(this.referencia != null && !this.referencia.equals("")){
                correlativo = Integer.parseInt(this.referencia.split("-")[1]);
            }
        }catch(Exception e){
            correlativo = 0;
        }

        return correlativo;
    }

    protected FacturaBuscarBean(Parcel in) {
        clave = in.readString();
        codigoCliente = in.readString();
        nombreCliente = in.readString();
        referencia = in.readString();
        total = in.readString();
        vencimiento = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clave);
        dest.writeString(codigoCliente);
        dest.writeString(nombreCliente);
        dest.writeString(referencia);
        dest.writeString(total);
        dest.writeString(vencimiento);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FacturaBuscarBean> CREATOR = new Parcelable.Creator<FacturaBuscarBean>() {
        @Override
        public FacturaBuscarBean createFromParcel(Parcel in) {
            return new FacturaBuscarBean(in);
        }

        @Override
        public FacturaBuscarBean[] newArray(int size) {
            return new FacturaBuscarBean[size];
        }
    };
}