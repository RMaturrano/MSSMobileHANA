package com.proyecto.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class EntregaDetalleBean implements Parcelable {

    private int ClaveEntrega;
    private int Linea;
    private String Articulo;
    private String ArticuloNombre;
    private String UnidadMedida;
    private String Almacen;
    private String AlmacenNombre;
    private String Cantidad;
    private String Diponible;
    private int ListaPrecio;
    private String PrecioUnitario;
    private String PorcentajeDescuento;
    private String Impuesto;
    boolean selected;
    private String CantidadTemp;
    private List<EntregaDetalleLoteBean> Lotes;

    public EntregaDetalleBean(){};

    public String getArticulo() {
        return Articulo;
    }

    public void setArticulo(String articulo) {
        Articulo = articulo;
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

    public String getAlmacenNombre() {
        return AlmacenNombre;
    }

    public void setAlmacenNombre(String almacenNombre) {
        AlmacenNombre = almacenNombre;
    }

    public String getArticuloNombre() {
        return ArticuloNombre;
    }

    public void setArticuloNombre(String articuloNombre) {
        ArticuloNombre = articuloNombre;
    }

    public int getClaveEntrega() {
        return ClaveEntrega;
    }

    public void setClaveEntrega(int claveEntrega) {
        ClaveEntrega = claveEntrega;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCantidadTemp() {
        return CantidadTemp;
    }

    public void setCantidadTemp(String cantidadTemp) {
        CantidadTemp = cantidadTemp;
    }

    public int getLinea() {
        return Linea;
    }

    public void setLinea(int linea) {
        Linea = linea;
    }

    public List<EntregaDetalleLoteBean> getLotes() {
        return Lotes;
    }

    public void setLotes(List<EntregaDetalleLoteBean> lotes) {
        Lotes = lotes;
    }

    public String getDiponible() {
        return Diponible;
    }

    public void setDiponible(String diponible) {
        Diponible = diponible;
    }


    protected EntregaDetalleBean(Parcel in) {
        ClaveEntrega = in.readInt();
        Linea = in.readInt();
        Articulo = in.readString();
        ArticuloNombre = in.readString();
        UnidadMedida = in.readString();
        Almacen = in.readString();
        AlmacenNombre = in.readString();
        Cantidad = in.readString();
        Diponible = in.readString();
        ListaPrecio = in.readInt();
        PrecioUnitario = in.readString();
        PorcentajeDescuento = in.readString();
        Impuesto = in.readString();
        selected = in.readByte() != 0x00;
        CantidadTemp = in.readString();
        if (in.readByte() == 0x01) {
            Lotes = new ArrayList<EntregaDetalleLoteBean>();
            in.readList(Lotes, EntregaDetalleLoteBean.class.getClassLoader());
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
        dest.writeInt(ClaveEntrega);
        dest.writeInt(Linea);
        dest.writeString(Articulo);
        dest.writeString(ArticuloNombre);
        dest.writeString(UnidadMedida);
        dest.writeString(Almacen);
        dest.writeString(AlmacenNombre);
        dest.writeString(Cantidad);
        dest.writeString(Diponible);
        dest.writeInt(ListaPrecio);
        dest.writeString(PrecioUnitario);
        dest.writeString(PorcentajeDescuento);
        dest.writeString(Impuesto);
        dest.writeByte((byte) (selected ? 0x01 : 0x00));
        dest.writeString(CantidadTemp);
        if (Lotes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(Lotes);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EntregaDetalleBean> CREATOR = new Parcelable.Creator<EntregaDetalleBean>() {
        @Override
        public EntregaDetalleBean createFromParcel(Parcel in) {
            return new EntregaDetalleBean(in);
        }

        @Override
        public EntregaDetalleBean[] newArray(int size) {
            return new EntregaDetalleBean[size];
        }
    };
}