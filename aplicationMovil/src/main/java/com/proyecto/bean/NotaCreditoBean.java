package com.proyecto.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.proyecto.utils.StringDateCast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotaCreditoBean implements Parcelable {

    private String  Tipo;
    private String  clave;
    private String  claveBase;
    private String referenciaBase;
    private Integer  numero;
    private String  Referencia;
    private String  socioNegocio;
    private String socioNegocioNombre;
    private Integer  ListaPrecio;
    private String ListaPrecioNombre;
    private Integer  Contacto;
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
    private String  CondicionPago;
    private String CondicionPagoNombre;
    private String  Indicador;
    private String IndicadorNombre;
    private String  SubTotal;
    private String  Descuento;
    private String  Impuesto;
    private String  Total;
    private String  Saldo;
    private String claveMovil;
    private String estadoMovil;
    private String latitud;
    private String longitud;
    private String fechaCreacion;
    private String horaCreacion;
    private String modoOffline;

    private List<NotaCreditoDetalleBean> Lineas;

    public NotaCreditoBean(){}

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getClaveBase() {
        return claveBase;
    }

    public void setClaveBase(String claveBase) {
        this.claveBase = claveBase;
    }

    public String getReferenciaBase() {
        return referenciaBase;
    }

    public void setReferenciaBase(String referenciaBase) {
        this.referenciaBase = referenciaBase;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
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

    public String getSocioNegocioNombre() {
        return socioNegocioNombre;
    }

    public void setSocioNegocioNombre(String socioNegocioNombre) {
        this.socioNegocioNombre = socioNegocioNombre;
    }

    public Integer getListaPrecio() {
        return ListaPrecio;
    }

    public void setListaPrecio(Integer listaPrecio) {
        ListaPrecio = listaPrecio;
    }

    public String getListaPrecioNombre() {
        return ListaPrecioNombre;
    }

    public void setListaPrecioNombre(String listaPrecioNombre) {
        ListaPrecioNombre = listaPrecioNombre;
    }

    public Integer getContacto() {
        return Contacto;
    }

    public void setContacto(Integer contacto) {
        Contacto = contacto;
    }

    public String getContactoNombre() {
        return ContactoNombre;
    }

    public void setContactoNombre(String contactoNombre) {
        ContactoNombre = contactoNombre;
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

    public String getDireccionFiscalDescripcion() {
        return DireccionFiscalDescripcion;
    }

    public void setDireccionFiscalDescripcion(String direccionFiscalDescripcion) {
        DireccionFiscalDescripcion = direccionFiscalDescripcion;
    }

    public String getDireccionEntrega() {
        return DireccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        DireccionEntrega = direccionEntrega;
    }

    public String getDireccionEntregaDescripcion() {
        return DireccionEntregaDescripcion;
    }

    public void setDireccionEntregaDescripcion(String direccionEntregaDescripcion) {
        DireccionEntregaDescripcion = direccionEntregaDescripcion;
    }

    public String getCondicionPago() {
        return CondicionPago;
    }

    public void setCondicionPago(String condicionPago) {
        CondicionPago = condicionPago;
    }

    public String getCondicionPagoNombre() {
        return CondicionPagoNombre;
    }

    public void setCondicionPagoNombre(String condicionPagoNombre) {
        CondicionPagoNombre = condicionPagoNombre;
    }

    public String getIndicador() {
        return Indicador;
    }

    public void setIndicador(String indicador) {
        Indicador = indicador;
    }

    public String getIndicadorNombre() {
        return IndicadorNombre;
    }

    public void setIndicadorNombre(String indicadorNombre) {
        IndicadorNombre = indicadorNombre;
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

    public String getClaveMovil() {
        return claveMovil;
    }

    public void setClaveMovil(String claveMovil) {
        this.claveMovil = claveMovil;
    }

    public String getEstadoMovil() {
        return estadoMovil;
    }

    public void setEstadoMovil(String estadoMovil) {
        this.estadoMovil = estadoMovil;
    }

    public List<NotaCreditoDetalleBean> getLineas() {
        return Lineas;
    }

    public void setLineas(List<NotaCreditoDetalleBean> lineas) {
        Lineas = lineas;
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

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getHoraCreacion() {
        return horaCreacion;
    }

    public void setHoraCreacion(String horaCreacion) {
        this.horaCreacion = horaCreacion;
    }

    public String getModoOffline() {
        return modoOffline;
    }

    public void setModoOffline(String modoOffline) {
        this.modoOffline = modoOffline;
    }

    public static JSONObject transformNotaCreditoToJSON(NotaCreditoBean bean, String sociedad){
        JSONObject object = new JSONObject();

        try{
            object.put("ClaveMovil", bean.getClaveMovil());
            object.put("ClaveBase", bean.getClaveBase());
            object.put("SocioNegocio", bean.getSocioNegocio());
            object.put("ListaPrecio", bean.getListaPrecio());
            object.put("CondicionPago", bean.getCondicionPago());
            object.put("Indicador", bean.getIndicador());
            object.put("Referencia", bean.getReferencia());
            object.put("FechaContable", StringDateCast.castDatetoDateWithoutSlash(bean.getFechaContable()));
            object.put("FechaVencimiento",StringDateCast.castDatetoDateWithoutSlash( bean.getFechaVencimiento()));
            //object.put("Contacto", bean.getContacto());
            object.put("Moneda", bean.getMoneda());
            object.put("EmpleadoVenta", bean.getEmpleadoVenta());
            object.put("DireccionFiscal", bean.getDireccionFiscal());
            object.put("DireccionEntrega", bean.getDireccionEntrega());
            object.put("Comentario", bean.getComentario());
            object.put("Empresa", Integer.parseInt(sociedad));

            object.put("Latitud", bean.getLatitud());
            object.put("Longitud", bean.getLongitud());
            object.put("FechaCreacion", bean.getFechaCreacion());
            object.put("HoraCreacion", bean.getHoraCreacion());
            object.put("ModoOffLine", bean.getModoOffline());

            JSONArray lines = new JSONArray();

            for (NotaCreditoDetalleBean line: bean.getLineas()) {
                JSONObject jsonLine = new JSONObject();
                jsonLine.put("ClaveNotaCredito", line.getClaveNotaCredito());
                jsonLine.put("Articulo", line.getArticulo());
                jsonLine.put("UnidadMedida", line.getUnidadMedida());
                jsonLine.put("Almacen", line.getAlmacen());
                jsonLine.put("Cantidad", line.getCantidad());
                jsonLine.put("ListaPrecio", line.getListaPrecio());
                jsonLine.put("PrecioUnitario", line.getPrecioUnitario());
                jsonLine.put("PorcentajeDescuento", line.getPorcentajeDescuento());
                jsonLine.put("Impuesto", line.getImpuesto());
                jsonLine.put("LineaBase", line.getLineaBase());
                jsonLine.put("Linea", line.getLinea());

                if(line.getLotes() != null) {

                    JSONArray lotes = new JSONArray();
                    for (NotaCreditoDetalleLoteBean l : line.getLotes()) {
                        JSONObject lote = new JSONObject();
                        lote.put("ClaveBase", l.getClaveBase());
                        lote.put("LineaBase", l.getLineaBase());
                        lote.put("Lote", l.getLote());
                        lote.put("Cantidad", l.getCantidad());
                        lotes.put(lote);
                    }

                    jsonLine.put("Lotes", lotes);
                }

                lines.put(jsonLine);
            }

            object.put("Lineas", lines);

        }catch (Exception e){
            return  null;
        }

        return  object;
    }


    protected NotaCreditoBean(Parcel in) {
        Tipo = in.readString();
        clave = in.readString();
        claveBase = in.readString();
        referenciaBase = in.readString();
        numero = in.readByte() == 0x00 ? null : in.readInt();
        Referencia = in.readString();
        socioNegocio = in.readString();
        socioNegocioNombre = in.readString();
        ListaPrecio = in.readByte() == 0x00 ? null : in.readInt();
        ListaPrecioNombre = in.readString();
        Contacto = in.readByte() == 0x00 ? null : in.readInt();
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
        CondicionPago = in.readString();
        CondicionPagoNombre = in.readString();
        Indicador = in.readString();
        IndicadorNombre = in.readString();
        SubTotal = in.readString();
        Descuento = in.readString();
        Impuesto = in.readString();
        Total = in.readString();
        Saldo = in.readString();
        claveMovil = in.readString();
        estadoMovil = in.readString();
        latitud = in.readString();
        longitud = in.readString();
        fechaCreacion = in.readString();
        horaCreacion = in.readString();
        modoOffline = in.readString();
        if (in.readByte() == 0x01) {
            Lineas = new ArrayList<NotaCreditoDetalleBean>();
            in.readList(Lineas, NotaCreditoDetalleBean.class.getClassLoader());
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
        dest.writeString(clave);
        dest.writeString(claveBase);
        dest.writeString(referenciaBase);
        if (numero == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(numero);
        }
        dest.writeString(Referencia);
        dest.writeString(socioNegocio);
        dest.writeString(socioNegocioNombre);
        if (ListaPrecio == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(ListaPrecio);
        }
        dest.writeString(ListaPrecioNombre);
        if (Contacto == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(Contacto);
        }
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
        dest.writeString(CondicionPago);
        dest.writeString(CondicionPagoNombre);
        dest.writeString(Indicador);
        dest.writeString(IndicadorNombre);
        dest.writeString(SubTotal);
        dest.writeString(Descuento);
        dest.writeString(Impuesto);
        dest.writeString(Total);
        dest.writeString(Saldo);
        dest.writeString(claveMovil);
        dest.writeString(estadoMovil);
        dest.writeString(latitud);
        dest.writeString(longitud);
        dest.writeString(fechaCreacion);
        dest.writeString(horaCreacion);
        dest.writeString(modoOffline);
        if (Lineas == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(Lineas);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NotaCreditoBean> CREATOR = new Parcelable.Creator<NotaCreditoBean>() {
        @Override
        public NotaCreditoBean createFromParcel(Parcel in) {
            return new NotaCreditoBean(in);
        }

        @Override
        public NotaCreditoBean[] newArray(int size) {
            return new NotaCreditoBean[size];
        }
    };
}