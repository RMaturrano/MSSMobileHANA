package com.proyecto.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class NotaCreditoDetalleBean implements Parcelable {

    private String ClaveNotaCredito;
    private String Linea;
    private String LineaBase;
    private String Articulo;
    private String ArticuloNombre;
    private String UnidadMedida;
    private String Almacen;
    private String AlmacenNombre;
    private String Cantidad;
    private int ListaPrecio;
    private String PrecioUnitario;
    private String PorcentajeDescuento;
    private String Impuesto;
    private List<NotaCreditoDetalleLoteBean> Lotes;

    public NotaCreditoDetalleBean(){}

    public String getClaveNotaCredito() {
        return ClaveNotaCredito;
    }

    public void setClaveNotaCredito(String claveNotaCredito) {
        ClaveNotaCredito = claveNotaCredito;
    }

    public String getLinea() {
        return Linea;
    }

    public void setLinea(String linea) {
        Linea = linea;
    }

    public String getLineaBase() {
        return LineaBase;
    }

    public void setLineaBase(String lineaBase) {
        LineaBase = lineaBase;
    }

    public String getArticulo() {
        return Articulo;
    }

    public void setArticulo(String articulo) {
        Articulo = articulo;
    }

    public String getArticuloNombre() {
        return ArticuloNombre;
    }

    public void setArticuloNombre(String articuloNombre) {
        ArticuloNombre = articuloNombre;
    }

    public String getUnidadMedida() {
        return UnidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        UnidadMedida = unidadMedida;
    }

    public String getAlmacen() {
        return Almacen;
    }

    public void setAlmacen(String almacen) {
        Almacen = almacen;
    }

    public String getAlmacenNombre() {
        return AlmacenNombre;
    }

    public void setAlmacenNombre(String almacenNombre) {
        AlmacenNombre = almacenNombre;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public int getListaPrecio() {
        return ListaPrecio;
    }

    public void setListaPrecio(int listaPrecio) {
        ListaPrecio = listaPrecio;
    }

    public String getPrecioUnitario() {
        return PrecioUnitario;
    }

    public void setPrecioUnitario(String precioUnitario) {
        PrecioUnitario = precioUnitario;
    }

    public String getPorcentajeDescuento() {
        return PorcentajeDescuento;
    }

    public void setPorcentajeDescuento(String porcentajeDescuento) {
        PorcentajeDescuento = porcentajeDescuento;
    }

    public String getImpuesto() {
        return Impuesto;
    }

    public void setImpuesto(String impuesto) {
        Impuesto = impuesto;
    }

    public List<NotaCreditoDetalleLoteBean> getLotes() {
        return Lotes;
    }

    public void setLotes(List<NotaCreditoDetalleLoteBean> lotes) {
        Lotes = lotes;
    }

    protected NotaCreditoDetalleBean(Parcel in) {
        ClaveNotaCredito = in.readString();
        Linea = in.readString();
        LineaBase = in.readString();
        Articulo = in.readString();
        ArticuloNombre = in.readString();
        UnidadMedida = in.readString();
        Almacen = in.readString();
        AlmacenNombre = in.readString();
        Cantidad = in.readString();
        ListaPrecio = in.readInt();
        PrecioUnitario = in.readString();
        PorcentajeDescuento = in.readString();
        Impuesto = in.readString();
        if (in.readByte() == 0x01) {
            Lotes = new ArrayList<NotaCreditoDetalleLoteBean>();
            in.readList(Lotes, NotaCreditoDetalleLoteBean.class.getClassLoader());
        } else {
            Lotes = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ClaveNotaCredito);
        dest.writeString(Linea);
        dest.writeString(LineaBase);
        dest.writeString(Articulo);
        dest.writeString(ArticuloNombre);
        dest.writeString(UnidadMedida);
        dest.writeString(Almacen);
        dest.writeString(AlmacenNombre);
        dest.writeString(Cantidad);
        dest.writeInt(ListaPrecio);
        dest.writeString(PrecioUnitario);
        dest.writeString(PorcentajeDescuento);
        dest.writeString(Impuesto);
        if (Lotes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(Lotes);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NotaCreditoDetalleBean> CREATOR = new Parcelable.Creator<NotaCreditoDetalleBean>() {
        @Override
        public NotaCreditoDetalleBean createFromParcel(Parcel in) {
            return new NotaCreditoDetalleBean(in);
        }

        @Override
        public NotaCreditoDetalleBean[] newArray(int size) {
            return new NotaCreditoDetalleBean[size];
        }
    };
}