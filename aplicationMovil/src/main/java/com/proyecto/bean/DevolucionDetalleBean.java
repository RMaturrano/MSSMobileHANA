package com.proyecto.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class DevolucionDetalleBean implements Parcelable {

    private String ClaveDevolucion;
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
    private List<DevolucionDetalleLoteBean> Lotes;

    public DevolucionDetalleBean(){}

    public String getClaveDevolucion() {
        return ClaveDevolucion;
    }

    public void setClaveDevolucion(String claveDevolucion) {
        ClaveDevolucion = claveDevolucion;
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

    public List<DevolucionDetalleLoteBean> getLotes() {
        return Lotes;
    }

    public void setLotes(List<DevolucionDetalleLoteBean> lotes) {
        Lotes = lotes;
    }


    protected DevolucionDetalleBean(Parcel in) {
        ClaveDevolucion = in.readString();
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
            Lotes = new ArrayList<DevolucionDetalleLoteBean>();
            in.readList(Lotes, DevolucionDetalleLoteBean.class.getClassLoader());
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
        dest.writeString(ClaveDevolucion);
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
    public static final Parcelable.Creator<DevolucionDetalleBean> CREATOR = new Parcelable.Creator<DevolucionDetalleBean>() {
        @Override
        public DevolucionDetalleBean createFromParcel(Parcel in) {
            return new DevolucionDetalleBean(in);
        }

        @Override
        public DevolucionDetalleBean[] newArray(int size) {
            return new DevolucionDetalleBean[size];
        }
    };
}