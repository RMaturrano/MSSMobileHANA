package com.proyecto.sociosnegocio.util;

import android.os.Parcel;
import android.os.Parcelable;
public class DireccionBuscarBean implements Parcelable {

    private String codigoCliente;
    private String nombreCliente;
    private String numUltimaCompra;
    private String fecUltimaCompra;
    private String monUltimaCompra;
    private String codigo;
    private String calle;
    private String tipo;
    private String latitud;
    private String longitud;
    private String departamento;
    private String departamentoNombre;
    private String provincia;
    private String provinciaNombre;
    private String distrito;
    private String distritoNombre;
    private String frecuenciaVisita;
    private String fechaInicio;
    private String visitaLunes;
    private String visitaMartes;
    private String visitaMiercoles;
    private String visitaJueves;
    private String visitaViernes;
    private String visitaSabado;
    private String visitaDomingo;
    private String personaContacto;
    private String telefonoContacto;
    private boolean isSelected = false;

    public DireccionBuscarBean(){};

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getDepartamentoNombre() {
        return departamentoNombre;
    }

    public void setDepartamentoNombre(String departamentoNombre) {
        this.departamentoNombre = departamentoNombre;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getProvinciaNombre() {
        return provinciaNombre;
    }

    public void setProvinciaNombre(String provinciaNombre) {
        this.provinciaNombre = provinciaNombre;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDistritoNombre() {
        return distritoNombre;
    }

    public void setDistritoNombre(String distritoNombre) {
        this.distritoNombre = distritoNombre;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getFrecuenciaVisita() {
        return frecuenciaVisita;
    }

    public void setFrecuenciaVisita(String frecuenciaVisita) {
        this.frecuenciaVisita = frecuenciaVisita;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getVisitaLunes() {
        return visitaLunes;
    }

    public void setVisitaLunes(String visitaLunes) {
        this.visitaLunes = visitaLunes;
    }

    public String getVisitaMartes() {
        return visitaMartes;
    }

    public void setVisitaMartes(String visitaMartes) {
        this.visitaMartes = visitaMartes;
    }

    public String getVisitaMiercoles() {
        return visitaMiercoles;
    }

    public void setVisitaMiercoles(String visitaMiercoles) {
        this.visitaMiercoles = visitaMiercoles;
    }

    public String getVisitaJueves() {
        return visitaJueves;
    }

    public void setVisitaJueves(String visitaJueves) {
        this.visitaJueves = visitaJueves;
    }

    public String getVisitaViernes() {
        return visitaViernes;
    }

    public void setVisitaViernes(String visitaViernes) {
        this.visitaViernes = visitaViernes;
    }

    public String getVisitaSabado() {
        return visitaSabado;
    }

    public void setVisitaSabado(String visitaSabado) {
        this.visitaSabado = visitaSabado;
    }

    public String getVisitaDomingo() {
        return visitaDomingo;
    }

    public void setVisitaDomingo(String visitaDomingo) {
        this.visitaDomingo = visitaDomingo;
    }

    public String getNumUltimaCompra() {
        return numUltimaCompra;
    }

    public void setNumUltimaCompra(String numUltimaCompra) {
        this.numUltimaCompra = numUltimaCompra;
    }

    public String getFecUltimaCompra() {
        return fecUltimaCompra;
    }

    public void setFecUltimaCompra(String fecUltimaCompra) {
        this.fecUltimaCompra = fecUltimaCompra;
    }

    public String getMonUltimaCompra() {
        return monUltimaCompra;
    }

    public void setMonUltimaCompra(String monUltimaCompra) {
        this.monUltimaCompra = monUltimaCompra;
    }

    public String getPersonaContacto() {
        return personaContacto;
    }

    public void setPersonaContacto(String personaContacto) {
        this.personaContacto = personaContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }


    protected DireccionBuscarBean(Parcel in) {
        codigoCliente = in.readString();
        nombreCliente = in.readString();
        numUltimaCompra = in.readString();
        fecUltimaCompra = in.readString();
        monUltimaCompra = in.readString();
        codigo = in.readString();
        calle = in.readString();
        tipo = in.readString();
        latitud = in.readString();
        longitud = in.readString();
        departamento = in.readString();
        departamentoNombre = in.readString();
        provincia = in.readString();
        provinciaNombre = in.readString();
        distrito = in.readString();
        distritoNombre = in.readString();
        frecuenciaVisita = in.readString();
        fechaInicio = in.readString();
        visitaLunes = in.readString();
        visitaMartes = in.readString();
        visitaMiercoles = in.readString();
        visitaJueves = in.readString();
        visitaViernes = in.readString();
        visitaSabado = in.readString();
        visitaDomingo = in.readString();
        personaContacto = in.readString();
        telefonoContacto = in.readString();
        isSelected = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(codigoCliente);
        dest.writeString(nombreCliente);
        dest.writeString(numUltimaCompra);
        dest.writeString(fecUltimaCompra);
        dest.writeString(monUltimaCompra);
        dest.writeString(codigo);
        dest.writeString(calle);
        dest.writeString(tipo);
        dest.writeString(latitud);
        dest.writeString(longitud);
        dest.writeString(departamento);
        dest.writeString(departamentoNombre);
        dest.writeString(provincia);
        dest.writeString(provinciaNombre);
        dest.writeString(distrito);
        dest.writeString(distritoNombre);
        dest.writeString(frecuenciaVisita);
        dest.writeString(fechaInicio);
        dest.writeString(visitaLunes);
        dest.writeString(visitaMartes);
        dest.writeString(visitaMiercoles);
        dest.writeString(visitaJueves);
        dest.writeString(visitaViernes);
        dest.writeString(visitaSabado);
        dest.writeString(visitaDomingo);
        dest.writeString(personaContacto);
        dest.writeString(telefonoContacto);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DireccionBuscarBean> CREATOR = new Parcelable.Creator<DireccionBuscarBean>() {
        @Override
        public DireccionBuscarBean createFromParcel(Parcel in) {
            return new DireccionBuscarBean(in);
        }

        @Override
        public DireccionBuscarBean[] newArray(int size) {
            return new DireccionBuscarBean[size];
        }
    };
}