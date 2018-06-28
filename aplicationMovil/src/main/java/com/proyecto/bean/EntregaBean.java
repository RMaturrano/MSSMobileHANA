package com.proyecto.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class EntregaBean implements Parcelable {

    private String  Tipo;
    private int  clave;
    private int  numero;
    private String  Referencia;
    private String  socioNegocio;
    private String socioNegocioNombre;
    private int  ListaPrecio;
    private String ListaPrecioNombre;
    private int  Contacto;
    private String ContactoNombre;
    private String  Moneda;
    private String  EmpleadoVenta;
    private String  Comentario;
    private String  FechaContable;
    private String  FechaVencimiento;
    private String  DireccionFiscal;
    private String DireccionFiscalDescripcion;
    private String  DireccionEntrega;
    private String DireccionEntregaDescripcion;
    private String DireccionEntregaLatitud;
    private String DireccionEntregaLongitud;
    private String  CondicionPago;
    private String CondicionPagoNombre;
    private String  Indicador;
    private String IndicadorNombre;
    private String  SubTotal;
    private String  Descuento;
    private String  Impuesto;
    private String  Total;
    private String  Saldo;
    private boolean isSelected;
    private List<EntregaDetalleBean> Lineas;

    public EntregaBean(){};

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String referencia) {
        Referencia = referencia;
    }

    public String getSocioNegocio() {
        return socioNegocio;
    }

    public void setSocioNegocio(String socioNegocio) {
        this.socioNegocio = socioNegocio;
    }

    public int getListaPrecio() {
        return ListaPrecio;
    }

    public void setListaPrecio(int listaPrecio) {
        ListaPrecio = listaPrecio;
    }

    public int getContacto() {
        return Contacto;
    }

    public void setContacto(int contacto) {
        Contacto = contacto;
    }

    public String getMoneda() {
        return Moneda;
    }

    public void setMoneda(String moneda) {
        Moneda = moneda;
    }

    public String getEmpleadoVenta() {
        return EmpleadoVenta;
    }

    public void setEmpleadoVenta(String empleadoVenta) {
        EmpleadoVenta = empleadoVenta;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String comentario) {
        Comentario = comentario;
    }

    public String getFechaContable() {
        return FechaContable;
    }

    public void setFechaContable(String fechaContable) {
        FechaContable = fechaContable;
    }

    public String getFechaVencimiento() {
        return FechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        FechaVencimiento = fechaVencimiento;
    }

    public String getDireccionFiscal() {
        return DireccionFiscal;
    }

    public void setDireccionFiscal(String direccionFiscal) {
        DireccionFiscal = direccionFiscal;
    }

    public String getDireccionEntrega() {
        return DireccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        DireccionEntrega = direccionEntrega;
    }

    public String getCondicionPago() {
        return CondicionPago;
    }

    public void setCondicionPago(String condicionPago) {
        CondicionPago = condicionPago;
    }

    public String getIndicador() {
        return Indicador;
    }

    public void setIndicador(String indicador) {
        Indicador = indicador;
    }

    public String getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(String subTotal) {
        SubTotal = subTotal;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String descuento) {
        Descuento = descuento;
    }

    public String getImpuesto() {
        return Impuesto;
    }

    public void setImpuesto(String impuesto) {
        Impuesto = impuesto;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getSaldo() {
        return Saldo;
    }

    public void setSaldo(String saldo) {
        Saldo = saldo;
    }

    public List<EntregaDetalleBean> getLineas() {
        return Lineas;
    }

    public void setLineas(List<EntregaDetalleBean> lineas) {
        Lineas = lineas;
    }

    public String getSocioNegocioNombre() {
        return socioNegocioNombre;
    }

    public void setSocioNegocioNombre(String socioNegocioNombre) {
        this.socioNegocioNombre = socioNegocioNombre;
    }

    public String getListaPrecioNombre() {
        return ListaPrecioNombre;
    }

    public void setListaPrecioNombre(String listaPrecioNombre) {
        ListaPrecioNombre = listaPrecioNombre;
    }

    public String getContactoNombre() {
        return ContactoNombre;
    }

    public void setContactoNombre(String contactoNombre) {
        ContactoNombre = contactoNombre;
    }

    public String getDireccionFiscalDescripcion() {
        return DireccionFiscalDescripcion;
    }

    public void setDireccionFiscalDescripcion(String direccionFiscalDescripcion) {
        DireccionFiscalDescripcion = direccionFiscalDescripcion;
    }

    public String getDireccionEntregaDescripcion() {
        return DireccionEntregaDescripcion;
    }

    public void setDireccionEntregaDescripcion(String direccionEntregaDescripcion) {
        DireccionEntregaDescripcion = direccionEntregaDescripcion;
    }

    public String getCondicionPagoNombre() {
        return CondicionPagoNombre;
    }

    public void setCondicionPagoNombre(String condicionPagoNombre) {
        CondicionPagoNombre = condicionPagoNombre;
    }

    public String getIndicadorNombre() {
        return IndicadorNombre;
    }

    public void setIndicadorNombre(String indicadorNombre) {
        IndicadorNombre = indicadorNombre;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDireccionEntregaLatitud() {
        return DireccionEntregaLatitud;
    }

    public void setDireccionEntregaLatitud(String direccionEntregaLatitud) {
        DireccionEntregaLatitud = direccionEntregaLatitud;
    }

    public String getDireccionEntregaLongitud() {
        return DireccionEntregaLongitud;
    }

    public void setDireccionEntregaLongitud(String direccionEntregaLongitud) {
        DireccionEntregaLongitud = direccionEntregaLongitud;
    }

    protected EntregaBean(Parcel in) {
        Tipo = in.readString();
        clave = in.readInt();
        numero = in.readInt();
        Referencia = in.readString();
        socioNegocio = in.readString();
        socioNegocioNombre = in.readString();
        ListaPrecio = in.readInt();
        ListaPrecioNombre = in.readString();
        Contacto = in.readInt();
        ContactoNombre = in.readString();
        Moneda = in.readString();
        EmpleadoVenta = in.readString();
        Comentario = in.readString();
        FechaContable = in.readString();
        FechaVencimiento = in.readString();
        DireccionFiscal = in.readString();
        DireccionFiscalDescripcion = in.readString();
        DireccionEntrega = in.readString();
        DireccionEntregaDescripcion = in.readString();
        DireccionEntregaLatitud = in.readString();
        DireccionEntregaLongitud = in.readString();
        CondicionPago = in.readString();
        CondicionPagoNombre = in.readString();
        Indicador = in.readString();
        IndicadorNombre = in.readString();
        SubTotal = in.readString();
        Descuento = in.readString();
        Impuesto = in.readString();
        Total = in.readString();
        Saldo = in.readString();
        isSelected = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            Lineas = new ArrayList<EntregaDetalleBean>();
            in.readList(Lineas, EntregaDetalleBean.class.getClassLoader());
        } else {
            Lineas = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Tipo);
        dest.writeInt(clave);
        dest.writeInt(numero);
        dest.writeString(Referencia);
        dest.writeString(socioNegocio);
        dest.writeString(socioNegocioNombre);
        dest.writeInt(ListaPrecio);
        dest.writeString(ListaPrecioNombre);
        dest.writeInt(Contacto);
        dest.writeString(ContactoNombre);
        dest.writeString(Moneda);
        dest.writeString(EmpleadoVenta);
        dest.writeString(Comentario);
        dest.writeString(FechaContable);
        dest.writeString(FechaVencimiento);
        dest.writeString(DireccionFiscal);
        dest.writeString(DireccionFiscalDescripcion);
        dest.writeString(DireccionEntrega);
        dest.writeString(DireccionEntregaDescripcion);
        dest.writeString(DireccionEntregaLatitud);
        dest.writeString(DireccionEntregaLongitud);
        dest.writeString(CondicionPago);
        dest.writeString(CondicionPagoNombre);
        dest.writeString(Indicador);
        dest.writeString(IndicadorNombre);
        dest.writeString(SubTotal);
        dest.writeString(Descuento);
        dest.writeString(Impuesto);
        dest.writeString(Total);
        dest.writeString(Saldo);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
        if (Lineas == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(Lineas);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EntregaBean> CREATOR = new Parcelable.Creator<EntregaBean>() {
        @Override
        public EntregaBean createFromParcel(Parcel in) {
            return new EntregaBean(in);
        }

        @Override
        public EntregaBean[] newArray(int size) {
            return new EntregaBean[size];
        }
    };
}