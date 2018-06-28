package com.proyecto.ws;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.proyecto.bean.AlmacenBean;
import com.proyecto.bean.ArticuloBean;
import com.proyecto.bean.BancoBean;
import com.proyecto.bean.CalleBean;
import com.proyecto.bean.CantidadBean;
import com.proyecto.bean.CondicionPagoBean;
import com.proyecto.bean.ContactoBean;
import com.proyecto.bean.CuentaBean;
import com.proyecto.bean.DepartamentoBean;
import com.proyecto.bean.DireccionBean;
import com.proyecto.bean.DistritoBean;
import com.proyecto.bean.EmpleadoBean;
import com.proyecto.bean.FabricanteBean;
import com.proyecto.bean.FacturaBean;
import com.proyecto.bean.FacturaDetalleBean;
import com.proyecto.bean.GrupoArticuloBean;
import com.proyecto.bean.GrupoSocioNegocioBean;
import com.proyecto.bean.GrupoUnidadMedidaBean;
import com.proyecto.bean.ImpuestoBean;
import com.proyecto.bean.IndicadorBean;
import com.proyecto.bean.ListaPrecioBean;
import com.proyecto.bean.MonedaBean;
import com.proyecto.bean.OrdenVentaBean;
import com.proyecto.bean.OrdenVentaDetalleBean;
import com.proyecto.bean.PagoBean;
import com.proyecto.bean.PagoDetalleBean;
import com.proyecto.bean.PaisBean;
import com.proyecto.bean.PrecioBean;
import com.proyecto.bean.ProvinciaBean;
import com.proyecto.bean.ReporteModel;
import com.proyecto.bean.SocioNegocioBean;
import com.proyecto.bean.UnidadMedidaBean;
import com.proyecto.bean.ZonaBean;
import com.proyecto.database.Insert;
import com.proyecto.database.Select;
import com.proyecto.reportes.ReporteEstadoCuenta;
import com.proyecto.utils.Constantes;
import com.proyecto.utils.StringCast;

public class InvocaWS {

	private String NAMESPACE = "http://pragsa.org/";
	private String URL = "";
	private Context contexto;
	private int timeOut = 420000;

	public InvocaWS(Context contexto) {
		this.contexto = contexto;
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(contexto);

		//   10.194.203.47
		String ip = pref.getString("ipServidor", Constantes.DEFAULT_IP);
		String port = pref.getString("puertoServidor", Constantes.DEFAULT_PORT);

		URL = "http://" + ip + ":" + port + "/" + "WebServPragsaSoap/"
				+ "ServicioPragsa.asmx";

	}

	/**********************************************************************************/
	/***************************** METODO PRINCIPAL LOGIN *****************************/
	/**********************************************************************************/
	public EmpleadoBean ObtenerVendedor(String codigoVendedor,
										String passwordVendedor, String idMovil) {

		EmpleadoBean bean = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerVendedor");
		soap.addProperty("CodigoVendedor", codigoVendedor);
		soap.addProperty("PasswordVendedor", passwordVendedor);
		soap.addProperty("IDMovilVendedor", idMovil);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerVendedor", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();

			if (resSoap != null) {
				bean = new EmpleadoBean();
				bean.setCodigoVendedor(resSoap.getProperty(0).toString());
				bean.setNombreVendedor(resSoap.getProperty(1).toString());
				bean.setCodigoUsuario(resSoap.getProperty(2).toString());
				bean.setClaveUsuario(resSoap.getProperty(3).toString());
				bean.setMovilId(resSoap.getProperty(4).toString());
				bean.setMovilEditar(resSoap.getProperty(5).toString());
				bean.setMovilAprobar(resSoap.getProperty(6).toString());
				bean.setMovilCrear(resSoap.getProperty(7).toString());
				bean.setMovilRechazar(resSoap.getProperty(8).toString());
				bean.setMovilEscogerPrecio(resSoap.getProperty(9).toString());
				PreferenceManager.getDefaultSharedPreferences(contexto).edit().putInt("MaxLineas",Integer.parseInt(resSoap.getProperty("MaximoLineas").toString())).commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return bean;

	}

	/**************************************************************************/
	/***************************** METODOS ENVIAR *****************************/
	/**************************************************************************/
	public String EnviarSociosNegocio(String codVendedor){
		ArrayList<SocioNegocioBean> Lista = new ArrayList<SocioNegocioBean>();
		Select select = new Select(contexto);
		Lista = select.listaSocioNegocio();
		select.close();
		String Texto = "";

		if(Lista.size() > 0){

			Texto = AgregarSocioNegocioLista(codVendedor, Lista);
			if(Texto == null || Texto.equalsIgnoreCase("anytype{}")){
				for (int i = 0; i < Lista.size() ; i++){

					Insert insert = new Insert(contexto);
					insert.updateEstadoSocioNegocio(Lista.get(i).getClaveMovil());
					insert.close();

				}
			}

		}

		return Texto;
	}

	public String EnviarPedidoCliente(String codVendedor){
		ArrayList<OrdenVentaBean> Lista = new ArrayList<OrdenVentaBean>();
		Select select = new Select(contexto);
		Lista = select.listaOrdenesVentas();
		select.close();
		String Texto = "";
		if(Lista.size() > 0){

			Texto = createOrderLista(codVendedor, Lista);

			if (Texto == null || Texto.equalsIgnoreCase("anytype{}")) {

				for (int i = 0; i < Lista.size() ; i++){
					Insert insert = new Insert(contexto);
					insert.updateEstadoOrdenVenta(Lista.get(i).getClaveMovil());
					insert.close();
				}

			}

		}

		return Texto;
	}

	public String EnviarPagoCliente(String codVendedor){
		ArrayList<PagoBean> Lista = new ArrayList<PagoBean>();
		Select select = new Select(contexto);
		Lista = select.listaPagosRecibidos();
		select.close();
		String Texto = "";
		if(Lista.size() > 0){

			Texto = AgregarPagoRecibidoLista(codVendedor, Lista);

			if (Texto == null || Texto.equalsIgnoreCase("anytype{}")) {

				for (int i = 0; i < Lista.size() ; i++){
					Insert insert = new Insert(contexto);
					insert.updateEstadoPago(Lista.get(i).getClaveMovil());
					insert.close();
				}

			}

		}

		return Texto;
	}


	/**************************************************************************/
	/***************************** METODOS AGREGAR *****************************/
	/**************************************************************************/

	public String AgregarSocioNegocio(SocioNegocioBean bean,
									  ArrayList<ContactoBean> listaContactos,
									  ArrayList<DireccionBean> listaDirecciones) {

		String res = "";

		// 0. m�todo
		SoapObject soap = new SoapObject(NAMESPACE, "AgregarSocioNegocio");
		soap.addProperty("CodigoVendedor", bean.getEmpleadoVentas());

		// 1. enviar el objeto socio de negocio
		SoapObject soapBP = new SoapObject(NAMESPACE, "SocioNegocio");
		soapBP.addProperty("Codigo", bean.getCodigo());
		soapBP.addProperty("TipoSocio", bean.getTipoCliente());
		soapBP.addProperty("TipoPersona", bean.getTipoPersona());
		soapBP.addProperty("TipoDocumento", bean.getTipoDoc());
		soapBP.addProperty("NumeroDocumento", bean.getNroDoc());
		soapBP.addProperty("NombreRazonSocial", bean.getNombRazSoc());
		soapBP.addProperty("NombreComercial", bean.getNomCom());
		soapBP.addProperty("ApellidoPaterno", bean.getApePat());
		soapBP.addProperty("ApellidoMaterno", bean.getApeMat());
		soapBP.addProperty("PrimerNombre", bean.getPriNom());
		soapBP.addProperty("SegundoNombre", bean.getSegNom());
		soapBP.addProperty("Telefono1", bean.getTlf1());
		soapBP.addProperty("Telefono2", bean.getTlf2());
		soapBP.addProperty("TelefonoMovil", bean.getTlfMov());
		soapBP.addProperty("CorreoElectronico", bean.getCorreo());
		soapBP.addProperty("GrupoSocio", bean.getGrupo());
		soapBP.addProperty("ListaPrecio", bean.getListaPrecio());
		soapBP.addProperty("CondicionPago", bean.getCondPago());
		soapBP.addProperty("Indicador", bean.getIndicador());
		soapBP.addProperty("Zona", bean.getZona());
		soapBP.addProperty("CreadMovil", bean.getCreadoMovil());
		soapBP.addProperty("ClaveMovil", bean.getClaveMovil());
		soapBP.addProperty("DireccionFiscal", bean.getDireccionFiscal());
		soapBP.addProperty("TransaccionMovil", bean.getTransaccionMovil());
		soapBP.addProperty("ValidoenPedido", bean.getValidoenPedido());

		// 1.1 enlazar los contactos al objeto
		SoapObject soapContacts = new SoapObject(NAMESPACE, "Contacto");
		SoapObject soapContact = null;

		if(listaContactos != null){
			for (ContactoBean c : listaContactos) {

				soapContact = new SoapObject(NAMESPACE, "Contacto");
				soapContact.addProperty("Codigo", c.getIdCon());
				soapContact.addProperty("Nombre", c.getNomCon());
				soapContact.addProperty("PrimerNombre", c.getPrimerNombre());
				soapContact.addProperty("SegundoNombre", c.getSegNomCon());
				soapContact.addProperty("Apellidos", c.getApeCon());
				soapContact.addProperty("Direccion", c.getDireccion());
				soapContact.addProperty("CorreoElectronico", c.getEmailCon());
				soapContact.addProperty("Telefono1", c.getTel1Con());
				soapContact.addProperty("Telefono2", c.getTel2Con());
				soapContact.addProperty("TelefonoMovil", c.getTelMovCon());
				soapContact.addProperty("Posicion", c.getPosicion());

				soapContacts.addProperty("Contacto", soapContact);

			}
		}

		soapBP.addProperty("Contacto", soapContacts);

		// 1.2 enlazar las direcciones al objeto
		SoapObject soapDirections = new SoapObject(NAMESPACE, "Direccion");
		SoapObject soapDirection = null;

		if(listaDirecciones != null){
			for (DireccionBean d : listaDirecciones) {

				soapDirection = new SoapObject(NAMESPACE,
						"Direccion");
				soapDirection.addProperty("Codigo", d.getIDDireccion());
				soapDirection.addProperty("Pais", d.getPais());
				soapDirection.addProperty("Departamento", d.getDepartamento());
				soapDirection.addProperty("Provincia", d.getProvincia());
				soapDirection.addProperty("Distrito", d.getDistrito());
				soapDirection.addProperty("Calle", d.getCalle());
				soapDirection.addProperty("Referencia", d.getReferencia());
				soapDirection.addProperty("Tipo", d.getTipoDireccion());

				soapDirections.addProperty("Direccion",
						soapDirection);

			}
		}

		soapBP.addProperty("Direccion", soapDirections);

		// 2. A�adir el objeto al m�todo
		soap.addSoapObject(soapBP);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = soap;
		envelope.dotNet = true;
		envelope.encodingStyle = SoapSerializationEnvelope.XSD;

		HttpTransportSE transporte = null;
		try {

			transporte = new HttpTransportSE(URL);
			transporte.debug = true;
			transporte
					.call("http://pragsa.org/AgregarSocioNegocio", envelope);

			res = envelope.getResponse().toString();

		} catch (Exception e) {
			res = e.getMessage();
		}

		return res;

		/*********************/

	}

	public String AgregarSocioNegocioLista(String codVendedor, ArrayList<SocioNegocioBean> lista) {

		String res = "";

		// 0. m�todo
		SoapObject soap = new SoapObject(NAMESPACE, "AgregarSocioNegocioLista");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapObject soapListBP = new SoapObject(NAMESPACE, "SocioNegocio");

		for (SocioNegocioBean bean : lista) {

			SoapObject soapBP = new SoapObject(NAMESPACE, "SocioNegocio");
			soapBP.addProperty("Codigo", bean.getCodigo());
			soapBP.addProperty("TipoSocio", bean.getTipoCliente());
			soapBP.addProperty("TipoPersona", bean.getTipoPersona());
			soapBP.addProperty("TipoDocumento", bean.getTipoDoc());
			soapBP.addProperty("NumeroDocumento", bean.getNroDoc());
			soapBP.addProperty("NombreRazonSocial", bean.getNombRazSoc());
			soapBP.addProperty("NombreComercial", bean.getNomCom());
			soapBP.addProperty("ApellidoPaterno", bean.getApePat());
			soapBP.addProperty("ApellidoMaterno", bean.getApeMat());
			soapBP.addProperty("PrimerNombre", bean.getPriNom());
			soapBP.addProperty("SegundoNombre", bean.getSegNom());
			soapBP.addProperty("Telefono1", bean.getTlf1());
			soapBP.addProperty("Telefono2", bean.getTlf2());
			soapBP.addProperty("TelefonoMovil", bean.getTlfMov());
			soapBP.addProperty("CorreoElectronico", bean.getCorreo());
			soapBP.addProperty("GrupoSocio", bean.getGrupo());
			soapBP.addProperty("ListaPrecio", bean.getListaPrecio());
			soapBP.addProperty("CondicionPago", bean.getCondPago());
			soapBP.addProperty("Indicador", bean.getIndicador());
			soapBP.addProperty("Zona", bean.getZona());
			soapBP.addProperty("CreadMovil", bean.getCreadoMovil());
			soapBP.addProperty("ClaveMovil", bean.getClaveMovil());
			soapBP.addProperty("DireccionFiscal", bean.getDireccionFiscal());
			soapBP.addProperty("TransaccionMovil", bean.getTransaccionMovil());
			soapBP.addProperty("ValidoenPedido", bean.getValidoenPedido());

			// 1.1 enlazar los contactos al objeto
			SoapObject soapContacts = new SoapObject(NAMESPACE, "Contacto");
			SoapObject soapContact = null;

			if(bean.getContactos() != null){
				for (ContactoBean c : bean.getContactos()) {

					soapContact = new SoapObject(NAMESPACE, "Contacto");
					soapContact.addProperty("Codigo", c.getIdCon());
					soapContact.addProperty("Nombre", c.getNomCon());
					soapContact.addProperty("PrimerNombre", c.getPrimerNombre());
					soapContact.addProperty("SegundoNombre", c.getSegNomCon());
					soapContact.addProperty("Apellidos", c.getApeCon());
					soapContact.addProperty("Direccion", c.getDireccion());
					soapContact.addProperty("CorreoElectronico", c.getEmailCon());
					soapContact.addProperty("Telefono1", c.getTel1Con());
					soapContact.addProperty("Telefono2", c.getTel2Con());
					soapContact.addProperty("TelefonoMovil", c.getTelMovCon());
					soapContact.addProperty("Posicion", c.getPosicion());

					soapContacts.addProperty("Contacto", soapContact);

				}
			}

			soapBP.addProperty("Contacto", soapContacts);

			// 1.2 enlazar las direcciones al objeto
			SoapObject soapDirections = new SoapObject(NAMESPACE, "Direccion");
			SoapObject soapDirection = null;

			if(bean.getDirecciones() != null){
				for (DireccionBean d : bean.getDirecciones()) {

					soapDirection = new SoapObject(NAMESPACE,
							"Direccion");
					soapDirection.addProperty("Codigo", d.getIDDireccion());
					soapDirection.addProperty("Pais", d.getPais());
					soapDirection.addProperty("Departamento", d.getDepartamento());
					soapDirection.addProperty("Provincia", d.getProvincia());
					soapDirection.addProperty("Distrito", d.getDistrito());
					soapDirection.addProperty("Calle", d.getCalle());
					soapDirection.addProperty("Referencia", d.getReferencia());
					soapDirection.addProperty("Tipo", d.getTipoDireccion());

					soapDirections.addProperty("Direccion",
							soapDirection);

				}
			}

			soapBP.addProperty("Direccion", soapDirections);

			soapListBP.addProperty("SocioNegocio",soapBP);

		}

		soap.addSoapObject(soapListBP);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = soap;
		envelope.dotNet = true;
		envelope.encodingStyle = SoapSerializationEnvelope.XSD;

		HttpTransportSE transporte = null;
		try {

			transporte = new HttpTransportSE(URL);
			transporte.debug = true;
			transporte
					.call("http://pragsa.org/AgregarSocioNegocioLista", envelope);

			res = envelope.getResponse().toString();

		} catch (Exception e) {
			res = e.getMessage();
		}

		return res;

		/*********************/

	}



	public String createOrder(OrdenVentaBean cabecera,
							  ArrayList<OrdenVentaDetalleBean> detalles) {

		String res = "";
		SoapObject soap = new SoapObject(NAMESPACE, "AgregarPedidoCliente");
		SoapObject soapOrder = new SoapObject(NAMESPACE, "PedidoCliente");
		soapOrder.addProperty("Tipo", cabecera.getTipoDoc());
		soapOrder.addProperty("Clave", cabecera.getClave());
		soapOrder.addProperty("Numero", cabecera.getNumero());
		soapOrder.addProperty("Referencia", cabecera.getReferencia());
		soapOrder.addProperty("SocioNegocio", cabecera.getCodSN());
		soapOrder.addProperty("ListaPrecio", cabecera.getListaPrecio());
		soapOrder.addProperty("Contacto", cabecera.getContacto());
		soapOrder.addProperty("Moneda", cabecera.getMoneda());
		soapOrder.addProperty("EmpleadoVenta", cabecera.getEmpVentas());
		soapOrder.addProperty("Comentario", cabecera.getComentario());
		soapOrder.addProperty("FechaContable", cabecera.getFecContable());
		soapOrder.addProperty("FechaVencimiento", cabecera.getFecVen());
		soapOrder.addProperty("DireccionFiscal", cabecera.getDirFiscal());
		soapOrder.addProperty("DireccionEntrega", cabecera.getDirEntrega());
		soapOrder.addProperty("CondicionPago", cabecera.getCondPago());
		soapOrder.addProperty("Indicador", cabecera.getIndicador());
		soapOrder.addProperty("SubTotal", cabecera.getSubTotal());
		soapOrder.addProperty("Impuesto", cabecera.getImpuesto());
		soapOrder.addProperty("Total", cabecera.getTotal());
		soapOrder.addProperty("CreadMovil", cabecera.getCreadoMovil());
		soapOrder.addProperty("ClaveMovil", cabecera.getClaveMovil());
		soapOrder.addProperty("TransaccionMovil", cabecera.getTransaccionMovil());

		SoapObject soapLines = new SoapObject(NAMESPACE, "Lineas");
		SoapObject soapDet = null;
		for (OrdenVentaDetalleBean d : detalles) {
			soapDet = new SoapObject(NAMESPACE, "Pedido_Detalle");
			soapDet.addProperty("Articulo", d.getCodArt());
			soapDet.addProperty("UnidadMedida", d.getCodUM());
			soapDet.addProperty("Almacen", d.getAlmacen());
			soapDet.addProperty("Cantidad", String.valueOf(d.getCantidad()));
			soapDet.addProperty("ListaPrecio", d.getListaPrecio());
			soapDet.addProperty("PrecioUnitario", String.valueOf(d.getPrecio()));
			soapDet.addProperty("PorcentajeDescuento",String.valueOf(d.getDescuento()));
			soapDet.addProperty("Impuesto", d.getCodImp());
			soapLines.addProperty("Pedido_Detalle", soapDet);
		}
		soapOrder.addProperty("Lineas", soapLines);
		soap.addSoapObject(soapOrder);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = soap;
		envelope.dotNet = true;
		envelope.encodingStyle = SoapSerializationEnvelope.XSD;
		HttpTransportSE transporte = null;
		try {
			transporte = new HttpTransportSE(URL);
			transporte.debug = true;
			transporte.call("http://pragsa.org/AgregarPedidoCliente", envelope);
			res = envelope.getResponse().toString();
		} catch (Exception e) {
			res = e.getMessage();
		}

		return res;
	}


	public String createOrderLista(String codVendedor, ArrayList<OrdenVentaBean> lista) {

		String res = "";
		SoapObject soap = new SoapObject(NAMESPACE, "AgregarPedidoClienteLista");
		SoapObject soapOrderList = new SoapObject(NAMESPACE, "PedidoCliente");

		for (OrdenVentaBean ordenVentaBean : lista) {

			SoapObject soapOrder = new SoapObject(NAMESPACE, "Pedido");
			soapOrder.addProperty("Tipo", ordenVentaBean.getTipoDoc());
			soapOrder.addProperty("Clave", ordenVentaBean.getClave());
			soapOrder.addProperty("Numero", ordenVentaBean.getNumero());
			soapOrder.addProperty("Referencia", ordenVentaBean.getReferencia());
			soapOrder.addProperty("SocioNegocio", ordenVentaBean.getCodSN());
			soapOrder.addProperty("ListaPrecio", ordenVentaBean.getListaPrecio());
			soapOrder.addProperty("Contacto", ordenVentaBean.getContacto());
			soapOrder.addProperty("Moneda", ordenVentaBean.getMoneda());
			soapOrder.addProperty("EmpleadoVenta", ordenVentaBean.getEmpVentas());
			soapOrder.addProperty("Comentario", ordenVentaBean.getComentario());
			soapOrder.addProperty("FechaContable", ordenVentaBean.getFecContable());
			soapOrder.addProperty("FechaVencimiento", ordenVentaBean.getFecVen());
			soapOrder.addProperty("DireccionFiscal", ordenVentaBean.getDirFiscal());
			soapOrder.addProperty("DireccionEntrega", ordenVentaBean.getDirEntrega());
			soapOrder.addProperty("CondicionPago", ordenVentaBean.getCondPago());
			soapOrder.addProperty("Indicador", ordenVentaBean.getIndicador());
			soapOrder.addProperty("SubTotal", ordenVentaBean.getSubTotal());
			soapOrder.addProperty("Impuesto", ordenVentaBean.getImpuesto());
			soapOrder.addProperty("Total", ordenVentaBean.getTotal());
			soapOrder.addProperty("CreadMovil", ordenVentaBean.getCreadoMovil());
			soapOrder.addProperty("ClaveMovil", ordenVentaBean.getClaveMovil());
			soapOrder.addProperty("TransaccionMovil", ordenVentaBean.getTransaccionMovil());

			SoapObject soapLines = new SoapObject(NAMESPACE, "Lineas");
			SoapObject soapDet = null;
			for (OrdenVentaDetalleBean d : ordenVentaBean.getDetalles()) {
				soapDet = new SoapObject(NAMESPACE, "Pedido_Detalle");
				soapDet.addProperty("Articulo", d.getCodArt());
				soapDet.addProperty("UnidadMedida", d.getCodUM());
				soapDet.addProperty("Almacen", d.getAlmacen());
				soapDet.addProperty("Cantidad", String.valueOf(d.getCantidad()));
				soapDet.addProperty("ListaPrecio", d.getListaPrecio());
				soapDet.addProperty("PrecioUnitario", String.valueOf(d.getPrecio()));
				soapDet.addProperty("PorcentajeDescuento",String.valueOf(d.getDescuento()));
				soapDet.addProperty("Impuesto", d.getCodImp());
				soapLines.addProperty("Pedido_Detalle", soapDet);
			}
			soapOrder.addProperty("Lineas", soapLines);

			soapOrderList.addProperty("Pedido",soapOrder);

		}

		soap.addSoapObject(soapOrderList);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = soap;
		envelope.dotNet = true;
		envelope.encodingStyle = SoapSerializationEnvelope.XSD;
		HttpTransportSE transporte = null;
		try {
			transporte = new HttpTransportSE(URL);
			transporte.debug = true;
			transporte.call("http://pragsa.org/AgregarPedidoClienteLista", envelope);
			res = envelope.getResponse().toString();
		} catch (Exception e) {
			res = e.getMessage();
		}

		return res;
	}


	public String AgregarPagoRecibido(PagoBean pago) {

		String res = "";
		SoapObject soap = new SoapObject(NAMESPACE, "AgregarPagoRecibido");
		SoapObject soapOrder = new SoapObject(NAMESPACE, "PagoRecibido");
		soapOrder.addProperty("Tipo", pago.getTipo());
		soapOrder.addProperty("Clave", pago.getClave());
		soapOrder.addProperty("Numero", pago.getNumero());
		soapOrder.addProperty("SocioNegocio", pago.getSocioNegocio());
		soapOrder.addProperty("EmpleadoVenta", pago.getEmpleadoVenta());
		soapOrder.addProperty("Comentario", pago.getComentario());
		soapOrder.addProperty("Glosa", pago.getGlosa());
		soapOrder.addProperty("FechaContable", pago.getFechaContable());
		soapOrder.addProperty("Moneda", pago.getMoneda());
		soapOrder.addProperty("TipoPago", pago.getTipoPago());
		soapOrder.addProperty("TransferenciaCuenta", pago.getTransferenciaCuenta());
		soapOrder.addProperty("TransferenciaReferencia", pago.getTransferenciaReferencia());
		soapOrder.addProperty("TransferenciaImporte", pago.getTransferenciaImporte());
		soapOrder.addProperty("EfectivoCuenta", pago.getEfectivoCuenta());
		soapOrder.addProperty("EfectivoImporte", pago.getEfectivoImporte());
		soapOrder.addProperty("CreadMovil", pago.getCreadoMovil());
		soapOrder.addProperty("ClaveMovil", pago.getClaveMovil());
		soapOrder.addProperty("ChequeCuenta", pago.getChequeCuenta());
		soapOrder.addProperty("ChequeBanco", pago.getChequeBanco());
		soapOrder.addProperty("ChequeVencimiento", pago.getChequeVencimiento());
		soapOrder.addProperty("ChequeImporte", pago.getChequeImporte());
		soapOrder.addProperty("ChequeNumero", pago.getChequeNumero());
		soapOrder.addProperty("TransaccionMovil", pago.getTransaccionMovil());

		SoapObject soapLines = new SoapObject(NAMESPACE, "Lineas");
		SoapObject soapDet = null;
		for (PagoDetalleBean d : pago.getLineas()) {
			soapDet = new SoapObject(NAMESPACE, "Pago_Detalle");
			soapDet.addProperty("FacturaCliente", d.getFacturaCliente());
			soapDet.addProperty("Importe", d.getImporte());
			soapLines.addProperty("Pago_Detalle", soapDet);
		}
		soapOrder.addProperty("Lineas", soapLines);
		soap.addSoapObject(soapOrder);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = soap;
		envelope.dotNet = true;
		envelope.encodingStyle = SoapSerializationEnvelope.XSD;
		HttpTransportSE transporte = null;
		try {
			transporte = new HttpTransportSE(URL);
			transporte.debug = true;
			transporte.call("http://pragsa.org/AgregarPagoRecibido", envelope);
			res = envelope.getResponse().toString();
		} catch (Exception e) {
			res = e.getMessage();
		}

		return res;
	}


	public String AgregarPagoRecibidoLista(String codVendedor, ArrayList<PagoBean> lista) {

		String res = "";
		SoapObject soap = new SoapObject(NAMESPACE, "AgregarPagoRecibidoLista");

		SoapObject soapPagoLista = new SoapObject(NAMESPACE, "PagoRecibido");

		for (PagoBean pago : lista) {

			SoapObject soapOrder = new SoapObject(NAMESPACE, "Pago");
			soapOrder.addProperty("Tipo", pago.getTipo());
			soapOrder.addProperty("Clave", pago.getClave());
			soapOrder.addProperty("Numero", pago.getNumero());
			soapOrder.addProperty("SocioNegocio", pago.getSocioNegocio());
			soapOrder.addProperty("EmpleadoVenta", pago.getEmpleadoVenta());
			soapOrder.addProperty("Comentario", pago.getComentario());
			soapOrder.addProperty("Glosa", pago.getGlosa());
			soapOrder.addProperty("FechaContable", pago.getFechaContable());
			soapOrder.addProperty("Moneda", pago.getMoneda());
			soapOrder.addProperty("TipoPago", pago.getTipoPago());
			soapOrder.addProperty("TransferenciaCuenta", pago.getTransferenciaCuenta());
			soapOrder.addProperty("TransferenciaReferencia", pago.getTransferenciaReferencia());
			soapOrder.addProperty("TransferenciaImporte", pago.getTransferenciaImporte());
			soapOrder.addProperty("EfectivoCuenta", pago.getEfectivoCuenta());
			soapOrder.addProperty("EfectivoImporte", pago.getEfectivoImporte());
			soapOrder.addProperty("CreadMovil", pago.getCreadoMovil());
			soapOrder.addProperty("ClaveMovil", pago.getClaveMovil());
			soapOrder.addProperty("ChequeCuenta", pago.getChequeCuenta());
			soapOrder.addProperty("ChequeBanco", pago.getChequeBanco());
			soapOrder.addProperty("ChequeVencimiento", pago.getChequeVencimiento());
			soapOrder.addProperty("ChequeImporte", pago.getChequeImporte());
			soapOrder.addProperty("ChequeNumero", pago.getChequeNumero());
			soapOrder.addProperty("TransaccionMovil", pago.getTransaccionMovil());

			SoapObject soapLines = new SoapObject(NAMESPACE, "Lineas");
			SoapObject soapDet = null;
			for (PagoDetalleBean d : pago.getLineas()) {
				soapDet = new SoapObject(NAMESPACE, "Pago_Detalle");
				soapDet.addProperty("FacturaCliente", d.getFacturaCliente());
				soapDet.addProperty("Importe", d.getImporte());
				soapLines.addProperty("Pago_Detalle", soapDet);
			}
			soapOrder.addProperty("Lineas", soapLines);

			soapPagoLista.addProperty("Pago", soapOrder);

		}

		soap.addSoapObject(soapPagoLista);


		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = soap;
		envelope.dotNet = true;
		envelope.encodingStyle = SoapSerializationEnvelope.XSD;
		HttpTransportSE transporte = null;
		try {
			transporte = new HttpTransportSE(URL);
			transporte.debug = true;
			transporte.call("http://pragsa.org/AgregarPagoRecibidoLista", envelope);
			res = envelope.getResponse().toString();
		} catch (Exception e) {
			res = e.getMessage();
		}

		return res;
	}


	/*********************************************************************/
	/************************** CARGA DE DATOS ***************************/
	/*********************************************************************/

	//WebApi
	public ArrayList<SocioNegocioBean> getBusinessPartnerApi(String codVendedor) {

		ArrayList<SocioNegocioBean> lista = null;
		ArrayList<ContactoBean> listaContacts = null;
		ArrayList<DireccionBean> listaDirections = null;

		try {
			URL url = new URL("http://"+PreferenceManager.getDefaultSharedPreferences(contexto).getString("ipServidor",Constantes.DEFAULT_IP)+
					"/WebApiPragsa/api/socionegocio?codigoVendedor="+codVendedor);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			try {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuilder stringBuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuilder.append(line).append("\n");
				}
				bufferedReader.close();

				SocioNegocioBean bean;
				ContactoBean contact;
				DireccionBean direction;

				lista = new ArrayList<SocioNegocioBean>();

				JSONArray jsonArray = new JSONArray(stringBuilder.toString());
				JSONObject object;

				for (int i = 0; i < jsonArray.length(); i++) {

					object = jsonArray.getJSONObject(i);
					bean = new SocioNegocioBean();
					bean.setCodigo(object.getString("Codigo"));
					bean.setTipoCliente(object.getString("TipoSocio"));
					bean.setTipoPersona(object.getString("TipoPersona"));
					bean.setTipoDoc(object.getString("TipoDocumento"));
					bean.setNroDoc(object.getString("NumeroDocumento"));
					bean.setNombRazSoc(object.getString("NombreRazonSocial"));
					bean.setNomCom(object.getString("NombreComercial"));
					bean.setApePat(object.getString("ApellidoPaterno"));
					bean.setApeMat(object.getString("ApellidoMaterno"));
					bean.setPriNom(object.getString("PrimerNombre"));
					bean.setSegNom(object.getString("SegundoNombre"));
					bean.setTlf1(object.getString("Telefono1"));
					bean.setTlf2(object.getString("Telefono2"));
					bean.setTlfMov(object.getString("TelefonoMovil"));
					bean.setCorreo(object.getString("CorreoElectronico"));
					bean.setGrupo(object.getString("GrupoSocio"));
					bean.setListaPrecio(object.getString("ListaPrecio"));
					bean.setCondPago(object.getString("CondicionPago"));
					bean.setIndicador(object.getString("Indicador"));
					bean.setZona(object.getString("Zona"));
					bean.setCreadoMovil(object.getString("CreadMovil"));
					bean.setClaveMovil(object.getString("ClaveMovil"));
					bean.setDireccionFiscal(object.getString("DireccionFiscal"));
					bean.setTransaccionMovil(object.getString("TransaccionMovil"));
					bean.setValidoenPedido(object.getString("ValidoenPedido"));

					//Obtener los contactos
				/*	try {

						URL urlContacts = new URL("http://"+PreferenceManager.getDefaultSharedPreferences(contexto).getString("ipServidor","200.10.84.66")+"/WebApiPragsa/api/contacto?clave="+object.getString("Codigo"));
						HttpURLConnection urlConnectionContacts = (HttpURLConnection) urlContacts.openConnection();
						try {

							BufferedReader brContacts = new BufferedReader(new InputStreamReader(urlConnectionContacts.getInputStream()));
							StringBuilder sbContacts = new StringBuilder();
							String lineResponse;
							while ((lineResponse = brContacts.readLine()) != null){
								sbContacts.append(lineResponse).append("\n");
							}
							brContacts.close();

							listaContacts = new ArrayList<>();
							JSONArray jsonArrayContacts = new JSONArray(sbContacts.toString());
							JSONObject jsonContact;
							for (int j = 0; j < jsonArrayContacts.length(); j++) {
								jsonContact = jsonArrayContacts.getJSONObject(j);
								contact = new ContactoBean();
								contact.setIdCon(jsonContact.getString("Codigo"));
								contact.setNomCon(jsonContact.getString("Nombre"));
								contact.setPrimerNombre(jsonContact.getString("PrimerNombre"));
								contact.setSegNomCon(jsonContact.getString("SegundoNombre"));
								contact.setApeCon(jsonContact.getString("Apellidos"));
								contact.setDireccion(jsonContact.getString("Direccion"));
								contact.setEmailCon(jsonContact.getString("CorreoElectronico"));
								contact.setTel1Con(jsonContact.getString("Telefono1"));
								contact.setTel2Con(jsonContact.getString("Telefono2"));
								contact.setTelMovCon(jsonContact.getString("TelefonoMovil"));
								contact.setPosicion(jsonContact.getString("Posicion"));
								listaContacts.add(contact);
							}
							bean.setContactos(listaContacts);

						}finally {
							urlConnectionContacts.disconnect();
						}


					}catch (Exception e){
						Log.e("ERROR CONTACTS", e.getMessage(), e);
					}		*/


					//Obtener las direcciones
				/*	try {

						URL urlDirections = new URL("http://"+PreferenceManager.getDefaultSharedPreferences(contexto).getString("ipServidor","200.10.84.66")+
								"/WebApiPragsa/api/direccion?clave="+object.getString("Codigo"));
						HttpURLConnection urlConnectionDirection = (HttpURLConnection) urlDirections.openConnection();
						try {

							BufferedReader brDirections = new BufferedReader(new InputStreamReader(urlConnectionDirection.getInputStream()));
							StringBuilder sbDirections = new StringBuilder();
							String lineResponse;
							while ((lineResponse = brDirections.readLine()) != null){
								sbDirections.append(lineResponse).append("\n");
							}
							brDirections.close();

							listaDirections = new ArrayList<>();
							JSONArray jsonArrayDirections = new JSONArray(sbDirections.toString());
							JSONObject jsonDirection;
							for (int j = 0; j < jsonArrayDirections.length(); j++) {
								jsonDirection = jsonArrayDirections.getJSONObject(j);

								direction = new DireccionBean();
								direction.setIDDireccion(jsonDirection.getString("Codigo"));
								direction.setPais(jsonDirection.getString("Pais"));
								direction.setDepartamento(jsonDirection.getString("Departamento"));
								direction.setProvincia(jsonDirection.getString("Provincia"));
								direction.setDistrito(jsonDirection.getString("Distrito"));
								direction.setCalle(jsonDirection.getString("Calle"));
								direction.setReferencia(jsonDirection.getString("Referencia"));
								direction.setTipoDireccion(jsonDirection.getString("Tipo"));
								listaDirections.add(direction);
							}
							bean.setDirecciones(listaDirections);

						}finally {
							urlConnectionDirection.disconnect();
						}


					}catch (Exception e){
						Log.e("ERROR CONTACTS", e.getMessage(), e);
					}
					*/

					lista.add(bean);

				}

			}
			finally{
				urlConnection.disconnect();
			}
		}
		catch(Exception e) {
			Log.e("ERROR", e.getMessage(), e);
			return null;
		}

		return lista;

	}


	public ArrayList<SocioNegocioBean> getBusinessPartners(String codVendedor) {

		ArrayList<SocioNegocioBean> lista = null;
		ArrayList<ContactoBean> listaContacts = null;
		ArrayList<DireccionBean> listaDirections = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerSocioNegocios");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerSocioNegocios", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			SoapObject rowContactOrDirection = null;
			SoapObject rowDirection = null;

			SocioNegocioBean bean;
			ContactoBean contact;
			DireccionBean direction;

			lista = new ArrayList<SocioNegocioBean>();

			if(resSoap != null){
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new SocioNegocioBean();

					bean.setCodigo(StringCast.eval(row.getProperty(0).toString()));
					bean.setTipoCliente(StringCast.eval(row.getProperty(1).toString()));
					bean.setTipoPersona(StringCast.eval(row.getProperty(2).toString()));
					bean.setTipoDoc(StringCast.eval(row.getProperty(3).toString()));
					bean.setNroDoc(StringCast.eval(row.getProperty(4).toString()));
					bean.setNombRazSoc(StringCast.eval(row.getProperty(5).toString()));
					bean.setNomCom(StringCast.eval(row.getProperty(6).toString()));
					bean.setApePat(StringCast.eval(row.getProperty(7).toString()));
					bean.setApeMat(StringCast.eval(row.getProperty(8).toString()));
					bean.setPriNom(StringCast.eval(row.getProperty(9).toString()));
					bean.setSegNom(StringCast.eval(row.getProperty(10).toString()));
					bean.setTlf1(StringCast.eval(row.getPropertyAsString(11).toString()));
					bean.setTlf2(StringCast.eval(row.getProperty(12).toString()));
					bean.setTlfMov(StringCast.eval(row.getProperty(13).toString()));
					bean.setCorreo(StringCast.eval(row.getProperty(14).toString()));
					bean.setGrupo(StringCast.eval(row.getProperty(15).toString()));
					bean.setListaPrecio(StringCast.eval(row.getProperty(16).toString()));
					bean.setCondPago(StringCast.eval(row.getProperty(17).toString()));
					bean.setIndicador(StringCast.eval(row.getProperty(18).toString()));
					bean.setZona(StringCast.eval(row.getProperty(19).toString()));
					bean.setCreadoMovil(StringCast.eval(row.getProperty(20).toString()));
					bean.setClaveMovil(StringCast.eval(row.getProperty(21).toString()));
					bean.setDireccionFiscal(StringCast.eval(row.getProperty(22).toString()));
					bean.setTransaccionMovil(StringCast.eval(row.getProperty(23).toString()));
					bean.setValidoenPedido(StringCast.eval(row.getProperty(24).toString()));

					// OBTENER LA LISTA DE CONTACTOS
					SoapObject subRow = (SoapObject) row.getPropertySafely("Contacto", null);

					if(subRow != null){
						listaContacts = new ArrayList<ContactoBean>();
						listaDirections = new ArrayList<DireccionBean>();
						for (int j = 0; j < subRow.getPropertyCount(); j++) {

							rowContactOrDirection = (SoapObject) subRow.getProperty(j);

							contact = new ContactoBean();
							contact.setIdCon(StringCast.eval(rowContactOrDirection.getProperty(0).toString()));
							contact.setNomCon(StringCast.eval(rowContactOrDirection.getProperty(1).toString()));
							contact.setPrimerNombre(StringCast.eval(rowContactOrDirection.getProperty(2).toString()));
							contact.setSegNomCon(StringCast.eval(rowContactOrDirection.getProperty(3).toString()));
							contact.setApeCon(StringCast.eval(rowContactOrDirection.getProperty(4).toString()));
							contact.setDireccion(StringCast.eval(rowContactOrDirection.getProperty(5).toString()));
							contact.setEmailCon(StringCast.eval(rowContactOrDirection.getProperty(6).toString()));
							contact.setTel1Con(StringCast.eval(rowContactOrDirection.getProperty(7).toString()));
							contact.setTel2Con(StringCast.eval(rowContactOrDirection.getProperty(8).toString()));
							contact.setTelMovCon(StringCast.eval(rowContactOrDirection.getProperty(9).toString()));
							contact.setPosicion(StringCast.eval(rowContactOrDirection.getProperty(10).toString()));
							listaContacts.add(contact);

						}
						bean.setContactos(listaContacts);
					}


					// OBTENER LA LISTA DE DIRECCIONES
					SoapObject subRow2 = (SoapObject) row.getPropertySafely("Direccion", null);

					if(subRow2 != null){
						listaDirections = new ArrayList<DireccionBean>();
						for (int k = 0; k < subRow2.getPropertyCount(); k++) {

							rowDirection = (SoapObject) subRow2.getProperty(k);

							direction = new DireccionBean();
							direction.setIDDireccion(StringCast.eval(rowDirection.getProperty(0).toString()));
							direction.setPais(StringCast.eval(rowDirection.getProperty(1).toString()));
							direction.setDepartamento(StringCast.eval(rowDirection.getProperty(2).toString()));
							direction.setProvincia(StringCast.eval(rowDirection.getProperty(3).toString()));
							direction.setDistrito(StringCast.eval(rowDirection.getProperty(4).toString()));
							direction.setCalle(StringCast.eval(rowDirection.getProperty(5).toString()));
							direction.setReferencia(StringCast.eval(rowDirection.getProperty(6).toString()));
							direction.setTipoDireccion(StringCast.eval(rowDirection.getProperty(7).toString()));
							listaDirections.add(direction);

						}
						bean.setDirecciones(listaDirections);
					}

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}


	public ArrayList<SocioNegocioBean> getBusinessPartnersLead(String codVendedor) {

		ArrayList<SocioNegocioBean> lista = null;
		ArrayList<ContactoBean> listaContacts = null;
		ArrayList<DireccionBean> listaDirections = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerSocioNegociosLeads");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerSocioNegociosLeads", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			SoapObject rowContactOrDirection = null;
			SoapObject rowDirection = null;

			SocioNegocioBean bean;
			ContactoBean contact;
			DireccionBean direction;

			lista = new ArrayList<SocioNegocioBean>();

			if(resSoap != null){
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new SocioNegocioBean();

					bean.setCodigo(StringCast.eval(row.getProperty(0).toString()));
					bean.setTipoCliente(StringCast.eval(row.getProperty(1).toString()));
					bean.setTipoPersona(StringCast.eval(row.getProperty(2).toString()));
					bean.setTipoDoc(StringCast.eval(row.getProperty(3).toString()));
					bean.setNroDoc(StringCast.eval(row.getProperty(4).toString()));
					bean.setNombRazSoc(StringCast.eval(row.getProperty(5).toString()));
					bean.setNomCom(StringCast.eval(row.getProperty(6).toString()));
					bean.setApePat(StringCast.eval(row.getProperty(7).toString()));
					bean.setApeMat(StringCast.eval(row.getProperty(8).toString()));
					bean.setPriNom(StringCast.eval(row.getProperty(9).toString()));
					bean.setSegNom(StringCast.eval(row.getProperty(10).toString()));
					bean.setTlf1(StringCast.eval(row.getPropertyAsString(11).toString()));
					bean.setTlf2(StringCast.eval(row.getProperty(12).toString()));
					bean.setTlfMov(StringCast.eval(row.getProperty(13).toString()));
					bean.setCorreo(StringCast.eval(row.getProperty(14).toString()));
					bean.setGrupo(StringCast.eval(row.getProperty(15).toString()));
					bean.setListaPrecio(StringCast.eval(row.getProperty(16).toString()));
					bean.setCondPago(StringCast.eval(row.getProperty(17).toString()));
					bean.setIndicador(StringCast.eval(row.getProperty(18).toString()));
					bean.setZona(StringCast.eval(row.getProperty(19).toString()));
					bean.setCreadoMovil(StringCast.eval(row.getProperty(20).toString()));
					bean.setClaveMovil(StringCast.eval(row.getProperty(21).toString()));
					bean.setDireccionFiscal(StringCast.eval(row.getProperty(22).toString()));
					bean.setTransaccionMovil(StringCast.eval(row.getProperty(23).toString()));
					bean.setValidoenPedido(StringCast.eval(row.getProperty(24).toString()));

					// OBTENER LA LISTA DE CONTACTOS
					SoapObject subRow = (SoapObject) row.getPropertySafely("Contacto", null);

					if(subRow != null){
						listaContacts = new ArrayList<ContactoBean>();
						listaDirections = new ArrayList<DireccionBean>();
						for (int j = 0; j < subRow.getPropertyCount(); j++) {

							rowContactOrDirection = (SoapObject) subRow.getProperty(j);

							contact = new ContactoBean();
							contact.setIdCon(StringCast.eval(rowContactOrDirection.getProperty(0).toString()));
							contact.setNomCon(StringCast.eval(rowContactOrDirection.getProperty(1).toString()));
							contact.setPrimerNombre(StringCast.eval(rowContactOrDirection.getProperty(2).toString()));
							contact.setSegNomCon(StringCast.eval(rowContactOrDirection.getProperty(3).toString()));
							contact.setApeCon(StringCast.eval(rowContactOrDirection.getProperty(4).toString()));
							contact.setDireccion(StringCast.eval(rowContactOrDirection.getProperty(5).toString()));
							contact.setEmailCon(StringCast.eval(rowContactOrDirection.getProperty(6).toString()));
							contact.setTel1Con(StringCast.eval(rowContactOrDirection.getProperty(7).toString()));
							contact.setTel2Con(StringCast.eval(rowContactOrDirection.getProperty(8).toString()));
							contact.setTelMovCon(StringCast.eval(rowContactOrDirection.getProperty(9).toString()));
							contact.setPosicion(StringCast.eval(rowContactOrDirection.getProperty(10).toString()));
							listaContacts.add(contact);

						}
						bean.setContactos(listaContacts);
					}


					// OBTENER LA LISTA DE DIRECCIONES
					SoapObject subRow2 = (SoapObject) row.getPropertySafely("Direccion", null);

					if(subRow2 != null){
						listaDirections = new ArrayList<DireccionBean>();
						for (int k = 0; k < subRow2.getPropertyCount(); k++) {

							rowDirection = (SoapObject) subRow2.getProperty(k);

							direction = new DireccionBean();
							direction.setIDDireccion(StringCast.eval(rowDirection.getProperty(0).toString()));
							direction.setPais(StringCast.eval(rowDirection.getProperty(1).toString()));
							direction.setDepartamento(StringCast.eval(rowDirection.getProperty(2).toString()));
							direction.setProvincia(StringCast.eval(rowDirection.getProperty(3).toString()));
							direction.setDistrito(StringCast.eval(rowDirection.getProperty(4).toString()));
							direction.setCalle(StringCast.eval(rowDirection.getProperty(5).toString()));
							direction.setReferencia(StringCast.eval(rowDirection.getProperty(6).toString()));
							direction.setTipoDireccion(StringCast.eval(rowDirection.getProperty(7).toString()));
							listaDirections.add(direction);

						}
						bean.setDirecciones(listaDirections);
					}

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}


	public ArrayList<OrdenVentaBean> getOrders(String codVendedor) {

		ArrayList<OrdenVentaBean> lista = null;
		ArrayList<OrdenVentaDetalleBean> listaDetalle = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerPedidoClientes");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerPedidoClientes", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			SoapObject rowDetails = null;
			SoapObject rowLine = null;

			OrdenVentaBean bean;
			OrdenVentaDetalleBean detail;

			lista = new ArrayList<OrdenVentaBean>();

			if(resSoap != null){
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new OrdenVentaBean();

					bean.setTipoDoc(StringCast.eval(row.getProperty(0).toString()));
					bean.setClave(StringCast.eval(row.getProperty(1).toString()));
					bean.setNumero(StringCast.eval(row.getProperty(2).toString()));
					bean.setReferencia(StringCast.eval(row.getProperty(3).toString()));
					bean.setCodSN(StringCast.eval(row.getProperty(4).toString()));
					bean.setListaPrecio(StringCast.eval(row.getProperty(5).toString()));
					bean.setContacto(StringCast.eval(row.getProperty(6).toString()));
					bean.setMoneda(StringCast.eval(row.getProperty(7).toString()));
					bean.setEmpVentas(StringCast.eval(row.getProperty(8).toString()));
					bean.setComentario(StringCast.eval(row.getProperty(9).toString()));
					bean.setFecContable(StringCast.eval(row.getProperty(10).toString()));
					bean.setFecVen(StringCast.eval(row.getProperty(11).toString()));
					bean.setDirFiscal(StringCast.eval(row.getProperty(12).toString()));
					bean.setDirEntrega(StringCast.eval(row.getProperty(13).toString()));
					bean.setCondPago(StringCast.eval(row.getProperty(14).toString()));
					bean.setIndicador(StringCast.eval(row.getProperty(15).toString()));
					bean.setSubTotal(StringCast.eval(row.getProperty(16).toString()));
					bean.setImpuesto(StringCast.eval(row.getProperty(17).toString()));
					bean.setTotal(StringCast.eval(row.getProperty(18).toString()));
					bean.setCreadoMovil(StringCast.eval(row.getProperty(19).toString()));
					bean.setClaveMovil(StringCast.eval(row.getProperty(20).toString()));
					bean.setTransaccionMovil(StringCast.eval(row.getProperty(21).toString()));

					// OBTENER LA LISTA DE DETALLES
					rowDetails = (SoapObject) row.getProperty(22);
					listaDetalle = new ArrayList<OrdenVentaDetalleBean>();
					for (int j = 0; j < rowDetails.getPropertyCount(); j++) {

						rowLine = (SoapObject) rowDetails.getProperty(j);

						detail = new OrdenVentaDetalleBean();
						detail.setCodArt(StringCast.eval(rowLine.getProperty(0).toString()));
						detail.setCodUM(StringCast.eval(rowLine.getProperty(1).toString()));
						detail.setAlmacen(StringCast.eval(rowLine.getProperty(2).toString()));
						detail.setCantidad(Double.parseDouble(rowLine.getProperty(3).toString()));
						detail.setListaPrecio(StringCast.eval(rowLine.getProperty(4).toString()));
						detail.setPrecio(Double.parseDouble(rowLine.getProperty(5).toString()));
						detail.setDescuento(Double.parseDouble(rowLine.getProperty(6).toString()));
						detail.setCodImp(StringCast.eval(rowLine.getProperty(7).toString()));
						detail.setLinea(StringCast.eval(rowLine.getProperty(8).toString()));
						listaDetalle.add(detail);

					}
					bean.setDetalles(listaDetalle);
					lista.add(bean);
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener Pagos de cliente
	public ArrayList<PagoBean> getPagoCliente(String codVendedor) {

		ArrayList<PagoBean> lista = null;
		ArrayList<PagoDetalleBean> lineas = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerPagoClientes");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerPagoClientes", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			SoapObject subRows = null;
			SoapObject subRow = null;
			PagoBean bean;
			PagoDetalleBean detalle;

			if (resSoap != null) {
				lista = new ArrayList<PagoBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new PagoBean();

					bean.setTipo(StringCast.eval(row.getProperty(0).toString()));
					bean.setClave(StringCast.eval(row.getProperty(1).toString()));
					bean.setNumero(StringCast.eval(row.getProperty(2).toString()));
					bean.setSocioNegocio(StringCast.eval(row.getProperty(3).toString()));
					bean.setEmpleadoVenta(StringCast.eval(row.getProperty(4).toString()));
					bean.setComentario(StringCast.eval(row.getProperty(5).toString()));
					bean.setGlosa(StringCast.eval(row.getProperty(6).toString()));
					bean.setFechaContable(StringCast.eval(row.getProperty(7).toString()));
					bean.setMoneda(StringCast.eval(row.getProperty(8).toString()));
					bean.setTipoPago(StringCast.eval(row.getProperty(9).toString()));
					bean.setTransferenciaCuenta(StringCast.eval(row.getProperty(10).toString()));
					bean.setTransferenciaReferencia(StringCast.eval(row.getProperty(11).toString()));
					bean.setTransferenciaImporte(StringCast.eval(row.getProperty(12).toString()));
					bean.setEfectivoCuenta(StringCast.eval(row.getProperty(13).toString()));
					bean.setEfectivoImporte(StringCast.eval(row.getProperty(14).toString()));
					bean.setCreadoMovil(StringCast.eval(row.getProperty(15).toString()));
					bean.setClaveMovil(StringCast.eval(row.getProperty(16).toString()));
					bean.setChequeCuenta(StringCast.eval(row.getProperty(17).toString()));
					bean.setChequeBanco(StringCast.eval(row.getProperty(18).toString()));
					bean.setChequeVencimiento(StringCast.eval(row.getProperty(19).toString()));
					bean.setChequeImporte(StringCast.eval(row.getProperty(20).toString()));
					bean.setChequeNumero(StringCast.eval(row.getProperty(21).toString()));
					bean.setTransaccionMovil(StringCast.eval(row.getProperty(22).toString()));

					// OBTENER LINEAS
					subRows = (SoapObject) row.getProperty(23);
					lineas = new ArrayList<PagoDetalleBean>();
					for (int j = 0; j < subRows.getPropertyCount(); j++) {

						subRow = (SoapObject) subRows.getProperty(j);

						detalle = new PagoDetalleBean();
						detalle.setFacturaCliente(subRow.getProperty(0)
								.toString());
						detalle.setImporte(subRow.getProperty(1).toString());
						lineas.add(detalle);

					}
					bean.setLineas(lineas);

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}


	// Obtener almacenes
	public ArrayList<AlmacenBean> getAlmacen(String codigoVendedor) {

		ArrayList<AlmacenBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerAlmacens");
		soap.addProperty("CodigoVendedor", codigoVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL, timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerAlmacens", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			AlmacenBean bean;

			if (resSoap != null) {
				lista = new ArrayList<AlmacenBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new AlmacenBean();
					bean.setCodigo(StringCast.eval(row.getProperty(0).toString()));
					bean.setDescripcion(StringCast.eval(row.getProperty(1).toString()));

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener Articulos
	public ArrayList<ArticuloBean> getArticulos(String codVendedor) {

		ArrayList<ArticuloBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerArticulos");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerArticulos", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			ArticuloBean bean;

			if (resSoap != null) {
				lista = new ArrayList<ArticuloBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new ArticuloBean();
					bean.setCod(StringCast.eval(row.getProperty(0).toString()));
					bean.setDesc(StringCast.eval(row.getProperty(1).toString()));
					bean.setFabricante(StringCast.eval(row.getProperty(2).toString()));
					bean.setGrupoArticulo(StringCast.eval(row.getProperty(3).toString()));
					bean.setCodUM(StringCast.eval(row.getProperty(4).toString()));
					bean.setUnidadMedidaVenta(StringCast.eval(row.getProperty(5).toString()));

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener Cantidads
	public ArrayList<CantidadBean> getCantidades(String codVendedor) {

		ArrayList<CantidadBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerCantidads");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerCantidads", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			CantidadBean bean;

			if (resSoap != null) {
				lista = new ArrayList<CantidadBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new CantidadBean();
					bean.setAlmacen(StringCast.eval(row.getProperty(0).toString()));
					bean.setArticulo(StringCast.eval(row.getProperty(1).toString()));
					bean.setStock(StringCast.eval(row.getProperty(2).toString()));
					bean.setComprometido(StringCast.eval(row.getProperty(3).toString()));
					bean.setSolicitado(StringCast.eval(row.getProperty(4).toString()));
					bean.setDisponible(StringCast.eval(row.getProperty(5).toString()));

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener Condicion Pago
	public ArrayList<CondicionPagoBean> getCondicionPago(String codVendedor) {

		ArrayList<CondicionPagoBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerCondicionPagos");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte
					.call("http://pragsa.org/ObtenerCondicionPagos", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			CondicionPagoBean bean;

			if (resSoap != null) {
				lista = new ArrayList<CondicionPagoBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new CondicionPagoBean();

					bean.setNumeroCondicion(StringCast.eval(row.getProperty(0).toString()));
					bean.setDescripcionCondicion(StringCast.eval(row.getProperty(1).toString()));

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener Fabricantes
	public ArrayList<FabricanteBean> getFabricantes(String codVendedor) {

		ArrayList<FabricanteBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerFabricantes");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerFabricantes", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			FabricanteBean bean;

			if (resSoap != null) {
				lista = new ArrayList<FabricanteBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new FabricanteBean();

					bean.setCodigo(row.getProperty(0).toString());
					bean.setNombre(row.getProperty(1).toString());

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener facturas
	public ArrayList<FacturaBean> getFacturas(String codVendedor) {

		ArrayList<FacturaBean> lista = null;
		ArrayList<FacturaDetalleBean> lineas = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerFacturaClientes");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerFacturaClientes",
					envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			SoapObject subRows = null;
			SoapObject subRow = null;
			FacturaBean bean;
			FacturaDetalleBean detalle;

			if (resSoap != null) {
				lista = new ArrayList<FacturaBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new FacturaBean();

					bean.setTipo(StringCast.eval(row.getProperty(0).toString()));
					bean.setClave(StringCast.eval(row.getProperty(1).toString()));
					bean.setNumero(StringCast.eval(row.getProperty(2).toString()));
					bean.setReferencia(StringCast.eval(row.getProperty(3).toString()));
					bean.setSocioNegocio(StringCast.eval(row.getProperty(4).toString()));
					bean.setListaPrecio(StringCast.eval(row.getProperty(5).toString()));
					bean.setContacto(StringCast.eval(row.getProperty(6).toString()));
					bean.setMoneda(StringCast.eval(row.getProperty(7).toString()));
					bean.setEmpleadoVenta(StringCast.eval(row.getProperty(8).toString()));
					bean.setComentario(StringCast.eval(row.getProperty(9).toString()));
					bean.setFechaContable(StringCast.eval(row.getProperty(10).toString()));
					bean.setFechaVencimiento(StringCast.eval(row.getProperty(11).toString()));
					bean.setDireccionFiscal(StringCast.eval(row.getProperty(12).toString()));
					bean.setDireccionEntrega(StringCast.eval(row.getProperty(13).toString()));
					bean.setCondicionPago(StringCast.eval(row.getProperty(14).toString()));
					bean.setIndicador(StringCast.eval(row.getProperty(15).toString()));
					bean.setSubTotal(StringCast.eval(row.getProperty(16).toString()));
					bean.setDescuento(StringCast.eval(row.getPropertySafely("Descuento","0").toString()));
					bean.setImpuesto(StringCast.eval(row.getProperty(18).toString()));
					bean.setTotal(StringCast.eval(row.getProperty(19).toString()));
					bean.setSaldo(StringCast.eval(row.getProperty(20).toString()));

					// OBTENER LINEAS
//					
//					if(row.getProperty(21) != null){
//						subRows = (SoapObject) row.getProperty(21);
//						lineas = new ArrayList<FacturaDetalleBean>();
//						for (int j = 0; j < subRows.getPropertyCount(); j++) {
//
//							subRow = (SoapObject) subRows.getProperty(j);
//
//							detalle = new FacturaDetalleBean();
//							detalle.setArticulo(StringCast.eval(subRow.getProperty(0).toString()));
//							detalle.setUnidadMedida(StringCast.eval(subRow.getProperty(1).toString()));
//							detalle.setAlmacen(StringCast.eval(subRow.getProperty(2).toString()));
//							detalle.setCantidad(StringCast.eval(subRow.getProperty(3).toString()));
//							detalle.setListaPrecio(StringCast.eval(subRow.getProperty(4).toString()));
//							detalle.setPrecioUnitario(StringCast.eval(subRow.getProperty(5).toString()));
//							detalle.setPorcentajeDescuento(StringCast.eval(subRow.getProperty(6).toString()));
//							detalle.setImpuesto(StringCast.eval(subRow.getProperty(7).toString()));
//							lineas.add(detalle);
//
//						}
//						bean.setLineas(lineas);
//					}


					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener grupo de articulos
	public ArrayList<GrupoArticuloBean> getGrupoArticulo(String codigoVendedor) {

		ArrayList<GrupoArticuloBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerGrupoArticulos");
		soap.addProperty("CodigoVendedor", codigoVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte
					.call("http://pragsa.org/ObtenerGrupoArticulos", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			GrupoArticuloBean bean;

			if (resSoap != null) {
				lista = new ArrayList<GrupoArticuloBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new GrupoArticuloBean();
					bean.setCodigo(row.getProperty(0).toString());
					bean.setNombre(row.getProperty(1).toString());

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener grupo de socio de negocio
	public ArrayList<GrupoSocioNegocioBean> getGrupoSocioNegocio(
			String codigoVendedor) {

		ArrayList<GrupoSocioNegocioBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerGrupoSocios");
		soap.addProperty("CodigoVendedor", codigoVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerGrupoSocios", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			GrupoSocioNegocioBean bean;

			if (resSoap != null) {
				lista = new ArrayList<GrupoSocioNegocioBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new GrupoSocioNegocioBean();
					bean.setGroupCode(row.getProperty(0).toString());
					bean.setGroupName(row.getProperty(1).toString());

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener grupo de unidad de medida
	public ArrayList<GrupoUnidadMedidaBean> getGrupoUnidadMedida(
			String codigoVendedor) {

		ArrayList<GrupoUnidadMedidaBean> lista = null;
		ArrayList<UnidadMedidaBean> lineas = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerGrupoUnidadMedidas");
		soap.addProperty("CodigoVendedor", codigoVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerGrupoUnidadMedidas",
					envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			SoapObject subRows = null;
			SoapObject subRow = null;
			GrupoUnidadMedidaBean bean;
			UnidadMedidaBean detalle;

			if (resSoap != null) {
				lista = new ArrayList<GrupoUnidadMedidaBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new GrupoUnidadMedidaBean();

					bean.setCodigo(row.getProperty(0).toString());
					bean.setNombre(row.getProperty(1).toString());

					// OBTENER UNIDADES DE MEDIDA DEL GRUPO
					subRows = (SoapObject) row.getPropertySafely("UnidadMedida", null);
					if(subRows != null){
						lineas = new ArrayList<UnidadMedidaBean>();
						for (int j = 0; j < subRows.getPropertyCount(); j++) {

							subRow = (SoapObject) subRows.getProperty(j);

							detalle = new UnidadMedidaBean();
							detalle.setCodigo(subRow.getProperty(0).toString());
							detalle.setNombre(subRow.getProperty(1).toString());
							lineas.add(detalle);

						}
						bean.setUnidadMedida(lineas);
					}

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener Impuesto
	public ArrayList<ImpuestoBean> getImpuesto(String codVendedor) {

		ArrayList<ImpuestoBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerImpuestos");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerImpuestos", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			ImpuestoBean bean;

			if (resSoap != null) {
				lista = new ArrayList<ImpuestoBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new ImpuestoBean();

					bean.setCodigo(row.getProperty(0).toString());
					bean.setNombre(row.getProperty(1).toString());

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener Indicadores
	public ArrayList<IndicadorBean> getIndicador(String codVendedor) {

		ArrayList<IndicadorBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerIndicadors");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerIndicadors", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			IndicadorBean bean;

			if (resSoap != null) {
				lista = new ArrayList<IndicadorBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new IndicadorBean();

					bean.setCodigo(row.getProperty(0).toString());
					bean.setNombre(row.getProperty(1).toString());

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener lista Precios
	public ArrayList<ListaPrecioBean> getListaPrecio(String codVendedor) {

		ArrayList<ListaPrecioBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerListaPrecios");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerListaPrecios", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			ListaPrecioBean bean;

			if (resSoap != null) {
				lista = new ArrayList<ListaPrecioBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new ListaPrecioBean();

					bean.setCodigo(row.getProperty(0).toString());
					bean.setNombre(row.getProperty(1).toString());

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener Precios
	public ArrayList<PrecioBean> ObtenerPrecios(String codVendedor) {

		ArrayList<PrecioBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerPrecios");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerPrecios", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			PrecioBean bean;

			if (resSoap != null) {
				lista = new ArrayList<PrecioBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new PrecioBean();

					bean.setListaPrecio(row.getProperty(0).toString());
					bean.setArticulo(row.getProperty(1).toString());
					bean.setMoneda(row.getProperty(2).toString());
					bean.setPrecioVenta(row.getProperty(3).toString());

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}


	// Obtener Monedas
	public ArrayList<MonedaBean> getMoneda(String codVendedor) {

		ArrayList<MonedaBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerMonedas");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerMonedas", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			MonedaBean bean;

			if (resSoap != null) {
				lista = new ArrayList<MonedaBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new MonedaBean();

					bean.setCodigo(row.getProperty(0).toString());
					bean.setDescripcion(row.getProperty(1).toString());

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}


	// Obtener paises
	public ArrayList<PaisBean> getPais(String codVendedor) {

		ArrayList<PaisBean> paises = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerPaiss");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerPaiss", envelope);


			// Nivel pais
			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;

			PaisBean pais;

			if (resSoap != null) {
				paises = new ArrayList<PaisBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					pais = new PaisBean();

					pais.setCodigo(row.getProperty(0).toString());
					pais.setNombre(row.getProperty(1).toString());

					paises.add(pais);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return paises;

	}


	// Obtener DEPARTAMENTOS
	public ArrayList<DepartamentoBean> ObtenerDepartamentos(String codVendedor) {

		ArrayList<DepartamentoBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerDepartamentos");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerDepartamentos", envelope);


			// Nivel pais
			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;

			DepartamentoBean object;

			if (resSoap != null) {
				lista = new ArrayList<DepartamentoBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					object = new DepartamentoBean();

					object.setCodigo(row.getProperty(0).toString());
					object.setNombre(row.getProperty(1).toString());
					object.setPais(row.getProperty(2).toString());

					lista.add(object);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}


	// Obtener PROVINCIAS
	public ArrayList<ProvinciaBean> ObtenerProvincias(String codVendedor) {

		ArrayList<ProvinciaBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerProvincias");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL, timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerProvincias", envelope);


			// Nivel pais
			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;

			ProvinciaBean object;

			if (resSoap != null) {
				lista = new ArrayList<ProvinciaBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					object = new ProvinciaBean();

					object.setCodigo(row.getProperty(0).toString());
					object.setNombre(row.getProperty(1).toString());
					object.setDepartamento(row.getProperty(2).toString());

					lista.add(object);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}


	// Obtener distritos
	public ArrayList<DistritoBean> ObtenerDistritos(String codVendedor) {

		ArrayList<DistritoBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerDistritos");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL, timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerDistritos", envelope);


			// Nivel pais
			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;

			DistritoBean object;

			if (resSoap != null) {
				lista = new ArrayList<DistritoBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					object = new DistritoBean();

					object.setCodigo(row.getProperty(0).toString());
					object.setNombre(row.getProperty(1).toString());
					object.setProvincia(row.getProperty(2).toString());

					lista.add(object);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener calles
	public ArrayList<CalleBean> ObtenerCalles(String codVendedor) {

		ArrayList<CalleBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerCalles");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL,timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerCalles", envelope);

			// Nivel pais
			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;

			CalleBean object;

			if (resSoap != null) {
				lista = new ArrayList<CalleBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					object = new CalleBean();

					object.setCodigo(row.getProperty(0).toString());
					object.setNombre(row.getProperty(1).toString());
					object.setDistrito(row.getProperty(2).toString());

					lista.add(object);



				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}


	// Obtener Unidades de medida
	public ArrayList<UnidadMedidaBean> getUnidadMedida(String codVendedor) {

		ArrayList<UnidadMedidaBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerUnidadMedidas");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL , timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerUnidadMedidas", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			UnidadMedidaBean bean;

			if (resSoap != null) {
				lista = new ArrayList<UnidadMedidaBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new UnidadMedidaBean();

					bean.setCodigo(row.getProperty(0).toString());
					bean.setNombre(row.getProperty(1).toString());

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	// Obtener Zonas
	public ArrayList<ZonaBean> getZona(String codVendedor) {

		ArrayList<ZonaBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerZonas");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL, timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerZonas", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			ZonaBean bean;

			if (resSoap != null) {
				lista = new ArrayList<ZonaBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new ZonaBean();

					bean.setCodigo(row.getProperty(0).toString());
					bean.setNombre(row.getProperty(1).toString());

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}


	// Obtener Cuentas
	public ArrayList<CuentaBean> ObtenerCuentas(String codVendedor) {

		ArrayList<CuentaBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerCuentas");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL, timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerCuentas", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			CuentaBean bean;

			if (resSoap != null) {
				lista = new ArrayList<CuentaBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new CuentaBean();

					bean.setCodigo(row.getProperty(0).toString());
					bean.setNombre(row.getProperty(1).toString());

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}


	// Obtener Bancos
	public ArrayList<BancoBean> ObtenerBancos(String codVendedor) {

		ArrayList<BancoBean> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerBancos");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL, timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerBancos", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			BancoBean bean;

			if (resSoap != null) {
				lista = new ArrayList<BancoBean>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new BancoBean();

					bean.setCodigo(row.getProperty(0).toString());
					bean.setNombre(row.getProperty(1).toString());

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}



	// Obtener Reportes
	public ArrayList<ReporteEstadoCuenta> ObtenerEstadoCuentaSocios(String codVendedor) {

		ArrayList<ReporteEstadoCuenta> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerEstadoCuentaSocios");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL, timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerEstadoCuentaSocios", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			ReporteEstadoCuenta bean;

			if (resSoap != null) {
				lista = new ArrayList<ReporteEstadoCuenta>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new ReporteEstadoCuenta();

					bean.setTipoReporte(row.getProperty(0).toString());
					bean.setCliente(row.getProperty(1).toString());
					bean.setNombre(row.getProperty(2).toString());
					bean.setListaPrecio(row.getProperty(3).toString());
					bean.setLineaCredito(row.getProperty(4).toString());
					bean.setCondicionPago(row.getProperty(5).toString());
					bean.setClave(row.getProperty(6).toString());
					bean.setSunat(row.getProperty(7).toString());
					bean.setCondicion(row.getProperty(8).toString());
					bean.setVendedor(row.getProperty(9).toString());
					bean.setEmision(row.getProperty(10).toString());
					bean.setMoneda(row.getProperty(11).toString());
					bean.setTotal(row.getProperty(12).toString());
					bean.setSaldo(row.getProperty(13).toString());
					bean.setPago_Fecha(row.getProperty(14).toString());
					bean.setPago_Dias(row.getProperty(15).toString());
					bean.setPago_Moneda(row.getProperty(16).toString());
					bean.setPagado_Importe(row.getProperty(17).toString());

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}

	public ArrayList<ReporteModel> ObtenerReporteNotaCredito(String codVendedor) {

		ArrayList<ReporteModel> lista = null;

		SoapObject soap = new SoapObject(NAMESPACE, "ObtenerReporteNotaCredito");
		soap.addProperty("CodigoVendedor", codVendedor);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soap);

		HttpTransportSE transporte = null;

		try {

			transporte = new HttpTransportSE(URL, timeOut);
			transporte.debug = true;

			transporte.call("http://pragsa.org/ObtenerReporteNotaCredito", envelope);

			SoapObject resSoap = (SoapObject) envelope.getResponse();
			SoapObject row = null;
			ReporteModel bean;

			if (resSoap != null) {
				lista = new ArrayList<ReporteModel>();
				for (int i = 0; i < resSoap.getPropertyCount(); i++) {

					row = (SoapObject) resSoap.getProperty(i);
					bean = new ReporteModel();

					bean.setClave(row.getProperty("clave").toString());
					bean.setSunat(row.getProperty("sunat").toString());
					bean.setEmision(row.getProperty("emision").toString());
					bean.setDias(row.getProperty("dias").toString());
					bean.setRuc(row.getProperty("ruc").toString());
					bean.setNombre(row.getProperty("nombre").toString());
					bean.setDireccion(row.getProperty("direccion").toString());
					bean.setTotal(row.getProperty("total").toString());
					bean.setPagado(row.getProperty("pagado").toString());
					bean.setSaldo(row.getProperty("saldo").toString());

					lista.add(bean);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return lista;

	}


}
