package com.proyecto.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.proyecto.utils.StringDateCast;

import org.json.JSONObject;

public class IncidenciaBean implements Parcelable {

    private Integer id;
    private String claveMovil;
    private String origen;
    private String codigoCliente;
    private String nombreCliente;
    private Integer codigoContacto;
    private String nombreContacto;
    private String codigoDireccion;
    private String descripcionMotivo;
    private String motivo;
    private String comentarios;
    private Integer codigoVendedor;
    private String latitud;
    private String longitud;
    private String fechaCreacion;
    private String horaCreacion;
    private String modoOffline;
    private String claveFactura;
    private String serieFactura;
    private Integer correlativoFactura;
    private String detalleDireccion;
    private String tipoIncidencia;
    private String fechaCompromisoPago;
    private Bitmap foto;
    private String sincronizado;
    private String rango;

    public IncidenciaBean(){};

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCodigoContacto() {
        return codigoContacto;
    }

    public void setCodigoContacto(Integer codigoContacto) {
        this.codigoContacto = codigoContacto;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getCodigoDireccion() {
        return codigoDireccion;
    }

    public void setCodigoDireccion(String codigoDireccion) {
        this.codigoDireccion = codigoDireccion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Integer getCodigoVendedor() {
        return codigoVendedor;
    }

    public void setCodigoVendedor(Integer codigoVendedor) {
        this.codigoVendedor = codigoVendedor;
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

    public String getSerieFactura() {
        return serieFactura;
    }

    public void setSerieFactura(String serieFactura) {
        this.serieFactura = serieFactura;
    }

    public Integer getCorrelativoFactura() {
        return correlativoFactura;
    }

    public void setCorrelativoFactura(Integer correlativoFactura) {
        this.correlativoFactura = correlativoFactura;
    }

    public String getDetalleDireccion() {
        return detalleDireccion;
    }

    public void setDetalleDireccion(String detalleDireccion) {
        this.detalleDireccion = detalleDireccion;
    }

    public String getTipoIncidencia() {
        return tipoIncidencia;
    }

    public void setTipoIncidencia(String tipoIncidencia) {
        this.tipoIncidencia = tipoIncidencia;
    }

    public String getFechaCompromisoPago() {
        return fechaCompromisoPago;
    }

    public void setFechaCompromisoPago(String fechaCompromisoPago) {
        this.fechaCompromisoPago = fechaCompromisoPago;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public String getDescripcionMotivo() {
        return descripcionMotivo;
    }

    public void setDescripcionMotivo(String descripcionMotivo) {
        this.descripcionMotivo = descripcionMotivo;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getClaveFactura() {
        return claveFactura;
    }

    public void setClaveFactura(String claveFactura) {
        this.claveFactura = claveFactura;
    }

    public String getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(String sincronizado) {
        this.sincronizado = sincronizado;
    }

    public String getClaveMovil() {
        return claveMovil;
    }

    public void setClaveMovil(String claveMovil) {
        this.claveMovil = claveMovil;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public static JSONObject transformIncidenciaToJSON(IncidenciaBean bean, String sociedad){
        JSONObject object = new JSONObject();

        try{
            object.put("ClaveMovil", bean.getClaveMovil());
            object.put("Origen", bean.getOrigen());
            object.put("CodigoCliente", bean.getCodigoCliente() != null ? bean.getCodigoCliente() : "");
            object.put("CodigoContacto", String.valueOf(bean.getCodigoContacto() != null ? bean.getCodigoContacto() : ""));
            object.put("CodigoDireccion", String.valueOf(bean.getCodigoDireccion() != null ? bean.getCodigoDireccion() : ""));
            object.put("CodigoMotivo",bean.getMotivo() != null ? Integer.parseInt(bean.getMotivo()) : -1);
            object.put("Comentarios", bean.getComentarios() != null ? bean.getComentarios() : "");
            object.put("Vendedor", bean.getCodigoVendedor());
            object.put("Latitud", bean.getLatitud());
            object.put("Longitud", bean.getLongitud());
            object.put("FechaCreacion", StringDateCast.castDatetoDateWithoutSlash(bean.getFechaCreacion()));
            object.put("HoraCreacion", bean.getHoraCreacion());
            object.put("ModoOffLine", bean.getModoOffline());
            object.put("ClaveFactura", bean.getClaveFactura() != null ? Integer.parseInt(bean.getClaveFactura()) : -1);
            object.put("SerieFactura", bean.getSerieFactura() != null ? bean.getSerieFactura() : "");
            object.put("CorrelativoFactura", bean.getCorrelativoFactura() != null ? bean.getCorrelativoFactura() : -1);
            object.put("TipoIncidencia", bean.getTipoIncidencia() != null ? bean.getTipoIncidencia() : "");
            object.put("FechaPago", bean.getFechaCompromisoPago() != null ?
                    StringDateCast.castDatetoDateWithoutSlash(bean.getFechaCompromisoPago()) : "");
            object.put("Empresa", Integer.parseInt(sociedad));
            object.put("Rango", bean.getRango());

        }catch (Exception e){
            return  null;
        }

        return  object;
    }


    protected IncidenciaBean(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        claveMovil = in.readString();
        origen = in.readString();
        codigoCliente = in.readString();
        nombreCliente = in.readString();
        codigoContacto = in.readByte() == 0x00 ? null : in.readInt();
        nombreContacto = in.readString();
        codigoDireccion = in.readString();
        descripcionMotivo = in.readString();
        motivo = in.readString();
        comentarios = in.readString();
        codigoVendedor = in.readByte() == 0x00 ? null : in.readInt();
        latitud = in.readString();
        longitud = in.readString();
        fechaCreacion = in.readString();
        horaCreacion = in.readString();
        modoOffline = in.readString();
        claveFactura = in.readString();
        serieFactura = in.readString();
        correlativoFactura = in.readByte() == 0x00 ? null : in.readInt();
        detalleDireccion = in.readString();
        tipoIncidencia = in.readString();
        fechaCompromisoPago = in.readString();
        foto = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        sincronizado = in.readString();
        rango = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(claveMovil);
        dest.writeString(origen);
        dest.writeString(codigoCliente);
        dest.writeString(nombreCliente);
        if (codigoContacto == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(codigoContacto);
        }
        dest.writeString(nombreContacto);
        dest.writeString(codigoDireccion);
        dest.writeString(descripcionMotivo);
        dest.writeString(motivo);
        dest.writeString(comentarios);
        if (codigoVendedor == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(codigoVendedor);
        }
        dest.writeString(latitud);
        dest.writeString(longitud);
        dest.writeString(fechaCreacion);
        dest.writeString(horaCreacion);
        dest.writeString(modoOffline);
        dest.writeString(claveFactura);
        dest.writeString(serieFactura);
        if (correlativoFactura == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(correlativoFactura);
        }
        dest.writeString(detalleDireccion);
        dest.writeString(tipoIncidencia);
        dest.writeString(fechaCompromisoPago);
        dest.writeValue(foto);
        dest.writeString(sincronizado);
        dest.writeString(rango);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<IncidenciaBean> CREATOR = new Parcelable.Creator<IncidenciaBean>() {
        @Override
        public IncidenciaBean createFromParcel(Parcel in) {
            return new IncidenciaBean(in);
        }

        @Override
        public IncidenciaBean[] newArray(int size) {
            return new IncidenciaBean[size];
        }
    };
}