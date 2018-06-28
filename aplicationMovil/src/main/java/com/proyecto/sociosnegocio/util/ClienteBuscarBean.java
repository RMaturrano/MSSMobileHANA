package com.proyecto.sociosnegocio.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.proyecto.bean.CondicionPagoBean;
import com.proyecto.bean.IndicadorBean;
import com.proyecto.bean.ListaPrecioBean;

import java.util.ArrayList;
import java.util.List;

public class ClienteBuscarBean implements Parcelable {
    private String codigo;
    private String nombre;
    private String telefono;
    private String numeroDocumento;
    private String direccionFiscalCodigo;
    private String direccionFiscalNombre;
    private ListaPrecioBean listaPrecio;
    private CondicionPagoBean condicionPago;
    private IndicadorBean indicador;
    private List<ContactoBuscarBean> contactos;
    private List<DireccionBuscarBean> direcciones;

    public ClienteBuscarBean(){};

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public List<ContactoBuscarBean> getContactos() {
        return contactos;
    }

    public void setContactos(List<ContactoBuscarBean> contactos) {
        this.contactos = contactos;
    }

    public List<DireccionBuscarBean> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<DireccionBuscarBean> direcciones) {
        this.direcciones = direcciones;
    }

    public ListaPrecioBean getListaPrecio() {
        return listaPrecio;
    }

    public void setListaPrecio(ListaPrecioBean listaPrecio) {
        this.listaPrecio = listaPrecio;
    }

    public CondicionPagoBean getCondicionPago() {
        return condicionPago;
    }

    public void setCondicionPago(CondicionPagoBean condicionPago) {
        this.condicionPago = condicionPago;
    }

    public IndicadorBean getIndicador() {
        return indicador;
    }

    public void setIndicador(IndicadorBean indicador) {
        this.indicador = indicador;
    }

    public String getDireccionFiscalCodigo() {
        return direccionFiscalCodigo;
    }

    public void setDireccionFiscalCodigo(String direccionFiscalCodigo) {
        this.direccionFiscalCodigo = direccionFiscalCodigo;
    }

    public String getDireccionFiscalNombre() {
        return direccionFiscalNombre;
    }

    public void setDireccionFiscalNombre(String direccionFiscalNombre) {
        this.direccionFiscalNombre = direccionFiscalNombre;
    }

    protected ClienteBuscarBean(Parcel in) {
        codigo = in.readString();
        nombre = in.readString();
        telefono = in.readString();
        numeroDocumento = in.readString();
        direccionFiscalCodigo = in.readString();
        direccionFiscalNombre = in.readString();
        listaPrecio = (ListaPrecioBean) in.readValue(ListaPrecioBean.class.getClassLoader());
        condicionPago = (CondicionPagoBean) in.readValue(CondicionPagoBean.class.getClassLoader());
        indicador = (IndicadorBean) in.readValue(IndicadorBean.class.getClassLoader());
        if (in.readByte() == 0x01) {
            contactos = new ArrayList<ContactoBuscarBean>();
            in.readList(contactos, ContactoBuscarBean.class.getClassLoader());
        } else {
            contactos = null;
        }
        if (in.readByte() == 0x01) {
            direcciones = new ArrayList<DireccionBuscarBean>();
            in.readList(direcciones, DireccionBuscarBean.class.getClassLoader());
        } else {
            direcciones = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(codigo);
        dest.writeString(nombre);
        dest.writeString(telefono);
        dest.writeString(numeroDocumento);
        dest.writeString(direccionFiscalCodigo);
        dest.writeString(direccionFiscalNombre);
        dest.writeValue(listaPrecio);
        dest.writeValue(condicionPago);
        dest.writeValue(indicador);
        if (contactos == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(contactos);
        }
        if (direcciones == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(direcciones);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ClienteBuscarBean> CREATOR = new Parcelable.Creator<ClienteBuscarBean>() {
        @Override
        public ClienteBuscarBean createFromParcel(Parcel in) {
            return new ClienteBuscarBean(in);
        }

        @Override
        public ClienteBuscarBean[] newArray(int size) {
            return new ClienteBuscarBean[size];
        }
    };
}