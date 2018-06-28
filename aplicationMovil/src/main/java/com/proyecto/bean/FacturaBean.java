package com.proyecto.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class FacturaBean implements Parcelable {

	private String  tipo;
	private String  clave;
	private String  numero;
	private String  referencia;
	private String  socioNegocio;
	private String socioNegocioNombre;
	private String  nombreSocio;
	private String  ListaPrecio;
	private String  contacto;
	private String  moneda;
	private String  empleadoVenta;
	private String  comentario;
	private String  fechaContable;
	private String  fechaDocumento;
	private String  fechaVencimiento;
	private String  direccionFiscal;
	private String  direccionEntrega;
	private String  condicionPago;
	private String  indicador;
	private String  subTotal;
	private String  descuento;
	private String  impuesto;
	private String  total;
	private String  saldo;
	private String  utilPagoTotal;
	private String ListaPrecioNombre;
	private String ContactoNombre;
	private String DireccionFiscalDescripcion;
	private String DireccionEntregaDescripcion;
	private String DireccionEntregaLatitud;
	private String DireccionEntregaLongitud;
	private String CondicionPagoNombre;
	private String IndicadorNombre;
	private int utilIcon;
	private boolean isSelected;
	private ArrayList<FacturaDetalleBean> lineas;

	public FacturaBean(){};

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getSocioNegocio() {
		return socioNegocio;
	}

	public void setSocioNegocio(String socioNegocio) {
		this.socioNegocio = socioNegocio;
	}

	public String getListaPrecio() {
		return ListaPrecio;
	}

	public void setListaPrecio(String listaPrecio) {
		ListaPrecio = listaPrecio;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getEmpleadoVenta() {
		return empleadoVenta;
	}

	public void setEmpleadoVenta(String empleadoVenta) {
		this.empleadoVenta = empleadoVenta;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getFechaContable() {
		return fechaContable;
	}

	public void setFechaContable(String fechaContable) {
		this.fechaContable = fechaContable;
	}

	public String getFechaDocumento() {
		return fechaDocumento;
	}

	public void setFechaDocumento(String fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	public String getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getDireccionFiscal() {
		return direccionFiscal;
	}

	public void setDireccionFiscal(String direccionFiscal) {
		this.direccionFiscal = direccionFiscal;
	}

	public String getDireccionEntrega() {
		return direccionEntrega;
	}

	public void setDireccionEntrega(String direccionEntrega) {
		this.direccionEntrega = direccionEntrega;
	}

	public String getCondicionPago() {
		return condicionPago;
	}

	public void setCondicionPago(String condicionPago) {
		this.condicionPago = condicionPago;
	}

	public String getIndicador() {
		return indicador;
	}

	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

	public String getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getSaldo() {
		return saldo;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

	public ArrayList<FacturaDetalleBean> getLineas() {
		return lineas;
	}

	public void setLineas(ArrayList<FacturaDetalleBean> lineas) {
		this.lineas = lineas;
	}

	public int getUtilIcon() {
		return utilIcon;
	}

	public void setUtilIcon(int utilIcon) {
		this.utilIcon = utilIcon;
	}

	public String getUtilPagoTotal() {
		return utilPagoTotal;
	}

	public void setUtilPagoTotal(String utilPagoTotal) {
		this.utilPagoTotal = utilPagoTotal;
	}

	public String getNombreSocio() {
		return nombreSocio;
	}

	public void setNombreSocio(String nombreSocio) {
		this.nombreSocio = nombreSocio;
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

	protected FacturaBean(Parcel in) {
		tipo = in.readString();
		clave = in.readString();
		numero = in.readString();
		referencia = in.readString();
		socioNegocio = in.readString();
		socioNegocioNombre = in.readString();
		nombreSocio = in.readString();
		ListaPrecio = in.readString();
		contacto = in.readString();
		moneda = in.readString();
		empleadoVenta = in.readString();
		comentario = in.readString();
		fechaContable = in.readString();
		fechaDocumento = in.readString();
		fechaVencimiento = in.readString();
		direccionFiscal = in.readString();
		direccionEntrega = in.readString();
		condicionPago = in.readString();
		indicador = in.readString();
		subTotal = in.readString();
		descuento = in.readString();
		impuesto = in.readString();
		total = in.readString();
		saldo = in.readString();
		utilPagoTotal = in.readString();
		ListaPrecioNombre = in.readString();
		ContactoNombre = in.readString();
		DireccionFiscalDescripcion = in.readString();
		DireccionEntregaDescripcion = in.readString();
		DireccionEntregaLatitud = in.readString();
		DireccionEntregaLongitud = in.readString();
		CondicionPagoNombre = in.readString();
		IndicadorNombre = in.readString();
		utilIcon = in.readInt();
		isSelected = in.readByte() != 0x00;
		if (in.readByte() == 0x01) {
			lineas = new ArrayList<FacturaDetalleBean>();
			in.readList(lineas, FacturaDetalleBean.class.getClassLoader());
		} else {
			lineas = null;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(tipo);
		dest.writeString(clave);
		dest.writeString(numero);
		dest.writeString(referencia);
		dest.writeString(socioNegocio);
		dest.writeString(socioNegocioNombre);
		dest.writeString(nombreSocio);
		dest.writeString(ListaPrecio);
		dest.writeString(contacto);
		dest.writeString(moneda);
		dest.writeString(empleadoVenta);
		dest.writeString(comentario);
		dest.writeString(fechaContable);
		dest.writeString(fechaDocumento);
		dest.writeString(fechaVencimiento);
		dest.writeString(direccionFiscal);
		dest.writeString(direccionEntrega);
		dest.writeString(condicionPago);
		dest.writeString(indicador);
		dest.writeString(subTotal);
		dest.writeString(descuento);
		dest.writeString(impuesto);
		dest.writeString(total);
		dest.writeString(saldo);
		dest.writeString(utilPagoTotal);
		dest.writeString(ListaPrecioNombre);
		dest.writeString(ContactoNombre);
		dest.writeString(DireccionFiscalDescripcion);
		dest.writeString(DireccionEntregaDescripcion);
		dest.writeString(DireccionEntregaLatitud);
		dest.writeString(DireccionEntregaLongitud);
		dest.writeString(CondicionPagoNombre);
		dest.writeString(IndicadorNombre);
		dest.writeInt(utilIcon);
		dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
		if (lineas == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(lineas);
		}
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<FacturaBean> CREATOR = new Parcelable.Creator<FacturaBean>() {
		@Override
		public FacturaBean createFromParcel(Parcel in) {
			return new FacturaBean(in);
		}

		@Override
		public FacturaBean[] newArray(int size) {
			return new FacturaBean[size];
		}
	};
}