package com.proyecto.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.proyect.movil.R;
import com.proyecto.bean.AlmacenBean;
import com.proyecto.bean.ArticuloBean;
import com.proyecto.bean.BancoBean;
import com.proyecto.bean.BeanChartProducto;
import com.proyecto.bean.CalleBean;
import com.proyecto.bean.CanalBean;
import com.proyecto.bean.CantidadBean;
import com.proyecto.bean.CondicionPagoBean;
import com.proyecto.bean.ContactoBean;
import com.proyecto.bean.CuentaBean;
import com.proyecto.bean.DepartamentoBean;
import com.proyecto.bean.DireccionBean;
import com.proyecto.bean.DistritoBean;
import com.proyecto.bean.FabricanteBean;
import com.proyecto.bean.FacturaBean;
import com.proyecto.bean.FacturaDetalleBean;
import com.proyecto.bean.GiroBean;
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
import com.proyecto.bean.ProvinciaBean;
import com.proyecto.bean.ProyectoBean;
import com.proyecto.bean.SocioNegocioBean;
import com.proyecto.bean.UnidadMedidaBean;
import com.proyecto.bean.ZonaBean;
import com.proyecto.cobranza.CobranzaFragment;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.StringDateCast;
import com.proyecto.utils.Variables;

public class Select {

	private Context contexto;
	private SQLiteDatabase db;
	private DataBaseHelper helper;
	
	private ArrayList<SocioNegocioBean> listaSociosNegocio = null;
	private ArrayList<OrdenVentaBean> listaOrdenesVenta = null;
	private ArrayList<AlmacenBean> listaAlmacen = null;
	private ArrayList<ArticuloBean> listaArticulos = null;
	private ArrayList<CantidadBean> listaCantidades = null;
	private ArrayList<CondicionPagoBean> listaCondicionPago = null;
	private ArrayList<FabricanteBean> listaFabricantes = null;
	private ArrayList<GrupoArticuloBean> listaGruposArticulo = null;
	private ArrayList<GrupoSocioNegocioBean> listaGruposSocioNegocio = null;
//	private ArrayList<GrupoUnidadMedidaBean> listaGruposUnidadMedida = null;
	private ArrayList<ImpuestoBean> listaImpuesto = null;
	private ArrayList<IndicadorBean> listaIndicador = null;
	private ArrayList<ListaPrecioBean> listaPrecios = null;
//	private ArrayList<PrecioBean> precios = null;
	private ArrayList<MonedaBean> listaMonedas = null;
	private ArrayList<PagoBean> listaPagoCliente = null;
	private ArrayList<PaisBean> listaPais = null;
	private ArrayList<DepartamentoBean> listaDepartamentos = null;
	private ArrayList<ProvinciaBean> listaProvincias = null;
	private ArrayList<DistritoBean> listaDistritos = null;
	private ArrayList<CalleBean> listaCalles = null;
	private ArrayList<UnidadMedidaBean> listaUnidadMedida = null;
	private ArrayList<ZonaBean> listaZona = null;
	private ArrayList<CuentaBean> listaCuenta = null;
	private ArrayList<BancoBean> listaBanco = null;
	
	//Lista personalizada
	private ArrayList<FormatCustomListView> listaPersonalizada = null;
	
	//Preferencias
	private SharedPreferences pref;
	private String codigoEmpleado;

	public Select(Context contexto) {
		this.contexto = contexto;
		
//		cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		db = cn.getWritableDatabase();
		
		helper = DataBaseHelper.getHelper(contexto);
		db = helper.getDataBase();
		
		pref = PreferenceManager
				.getDefaultSharedPreferences(contexto);
		codigoEmpleado = pref.getString(Variables.CODIGO_EMPLEADO, "");
		
	}
	
	public ArrayList<SocioNegocioBean> listaSocioNegocio(){
		
		listaSociosNegocio = new ArrayList<SocioNegocioBean>();
		SocioNegocioBean socio;
		ArrayList<ContactoBean> contactos;
		ContactoBean contacto;
		ArrayList<DireccionBean> direcciones;
		DireccionBean direccion;

		Cursor data= db.rawQuery(" SELECT " +
								 " A.Codigo, " +
								 " A.TipoSocio, " +
								 " A.TipoPersona, " +
								 " A.TipoDocumento, " +
								 " A.NumeroDocumento, " +
								 " A.NombreRazonSocial, " +
								 " A.NombreComercial, " +
								 " A.ApellidoPaterno, " +
								 " A.ApellidoMaterno, " +
								 " A.PrimerNombre, " +
								 " A.SegundoNombre, " +
								 " A.Telefono1, " +
								 " A.Telefono2, " +
								 " A.TelefonoMovil, " +
								 " A.CorreoElectronico, " +
								 " A.GrupoSocio, " +
								 " A.ListaPrecio, " +
								 " A.CondicionPago, " +
								 " A.Indicador, " +
								 " A.Zona, " +
								 " A.CreadMovil, " +
								 " A.ClaveMovil, " +
								 " A.DireccionFiscal, " +
								 " A.EstadoMovil, " +
								 " A.PoseeActivos, " +
								 " A.TransaccionMovil "+
								 " FROM TB_SOCIO_NEGOCIO A " +
								 " WHERE A.EstadoMovil = 'L'", null);
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				socio = new SocioNegocioBean();
				socio.setCodigo(data.getString(0));
				socio.setTipoCliente(data.getString(1));
				socio.setTipoPersona(data.getString(2));
				socio.setTipoDoc(data.getString(3));
				socio.setNroDoc(data.getString(4));
				socio.setNombRazSoc(data.getString(5));
				socio.setNomCom(data.getString(6));
				socio.setApePat(data.getString(7));
				socio.setApeMat(data.getString(8));
				socio.setPriNom(data.getString(9));
				socio.setSegNom(data.getString(10));
				socio.setTlf1(data.getString(11));
				socio.setTlf2(data.getString(12));
				socio.setTlfMov(data.getString(13));
				socio.setCorreo(data.getString(14));
				socio.setGrupo(data.getString(15));
				socio.setListaPrecio(data.getString(16));
				socio.setCondPago(data.getString(17));
				socio.setIndicador(data.getString(18));
				socio.setZona(data.getString(19));
				socio.setCreadoMovil(data.getString(20));
				socio.setClaveMovil(data.getString(21));
				socio.setDireccionFiscal(data.getString(22));
				socio.setEstadoRegistroMovil(data.getString(23));
				socio.setTransaccionMovil(data.getString(24));
				socio.setEmpleadoVentas(codigoEmpleado);
				socio.setPoseeActivos(data.getString(data.getColumnIndex("PoseeActivos")));
				socio.setMoneda("#");
				
				Cursor dataContacts = db.rawQuery(" SELECT " +
						 " Codigo, " +
						 " Nombre, " +
						 " PrimerNombre, " +
						 " SegundoNombre, " +
						 " Apellidos, " +
						 " Direccion, " +
						 " CorreoElectronico, " +
						 " Telefono1, " +
						 " Telefono2, " +
						 " TelefonoMovil, " +
						 " Posicion " +
						 " FROM TB_SOCIO_NEGOCIO_CONTACTO " +
						 " WHERE CodigoSocioNegocio = '"+socio.getCodigo()+"'", null); 
				
				if(dataContacts.getCount()>0)
				{
					contactos = new ArrayList<ContactoBean> ();
					while(dataContacts.moveToNext()){
						contacto = new ContactoBean();
						contacto.setIdCon(dataContacts.getString(0));
						contacto.setNomCon(dataContacts.getString(1));
						contacto.setPrimerNombre(dataContacts.getString(2));
						contacto.setSegNomCon(dataContacts.getString(3));
						contacto.setApeCon(dataContacts.getString(4));
						contacto.setDireccion(dataContacts.getString(5));
						contacto.setEmailCon(dataContacts.getString(6));
						contacto.setTel1Con(dataContacts.getString(7));
						contacto.setTel2Con(dataContacts.getString(8));
						contacto.setTelMovCon(dataContacts.getString(9));
						contacto.setPosicion(dataContacts.getString(10));
						contactos.add(contacto);
					}
					dataContacts.close();
					socio.setContactos(contactos);
				}
				
				Cursor dataDirections = db.rawQuery(" SELECT " +
						 " Codigo, " +
						 " Pais, " +
						 " Departamento, " +
						 " Provincia, " +
						 " Distrito, " +
						 " Calle, " +
						 " Referencia, " +
						 " Tipo, " +
						" Latitud, " +
						" Longitud " +
						 " FROM TB_SOCIO_NEGOCIO_DIRECCION " +
						 " WHERE CodigoSocioNegocio = '"+socio.getCodigo()+"'", null); 
				
				if(dataDirections.getCount()>0)
				{
					direcciones = new ArrayList<DireccionBean> ();
					while(dataDirections.moveToNext()){
						direccion = new DireccionBean();
						direccion.setIDDireccion(dataDirections.getString(0));
						direccion.setPais(dataDirections.getString(1));
						direccion.setDepartamento(dataDirections.getString(2));
						direccion.setProvincia(dataDirections.getString(3));
						direccion.setDistrito(dataDirections.getString(4));
						direccion.setCalle(dataDirections.getString(5));
						direccion.setReferencia(dataDirections.getString(6));
						direccion.setTipoDireccion(dataDirections.getString(7));
						direccion.setLatitud(dataDirections.getString(8));
						direccion.setLongitud(dataDirections.getString(9));
						direcciones.add(direccion);
					}
					dataDirections.close();
					socio.setDirecciones(direcciones);
				}
				
				listaSociosNegocio.add(socio);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaSociosNegocio;
		
	}
	
	public SocioNegocioBean obtenerSocioNegocio(String clave){
		
		SocioNegocioBean socio = null;
		ArrayList<ContactoBean> contactos;
		ContactoBean contacto;
		ArrayList<DireccionBean> direcciones;
		DireccionBean direccion;

		Cursor data= db.rawQuery(" SELECT " +
								 " A.Codigo, " +
								 " A.TipoSocio, " +
								 " A.TipoPersona, " +
								 " A.TipoDocumento, " +
								 " A.NumeroDocumento, " +
								 " A.NombreRazonSocial, " +
								 " A.NombreComercial, " +
								 " A.ApellidoPaterno, " +
								 " A.ApellidoMaterno, " +
								 " A.PrimerNombre, " +
								 " A.SegundoNombre, " +
								 " A.Telefono1, " +
								 " A.Telefono2, " +
								 " A.TelefonoMovil, " +
								 " A.CorreoElectronico, " +
								 " A.GrupoSocio, " +
								 " A.ListaPrecio, " +
								 " A.CondicionPago, " +
								 " A.Indicador, " +
								 " A.Zona, " +
								 " A.CreadMovil, " +
								 " A.ClaveMovil, " +
								 " A.DireccionFiscal, " +
								 " A.EstadoMovil, " +
								 " A.TransaccionMovil "+
								 " FROM TB_SOCIO_NEGOCIO A " +
								 " WHERE A.Codigo = '"+clave+"' OR A.ClaveMovil = '"+clave+"' ", null);
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				socio = new SocioNegocioBean();
				socio.setCodigo(data.getString(0));
				socio.setTipoCliente(data.getString(1));
				socio.setTipoPersona(data.getString(2));
				socio.setTipoDoc(data.getString(3));
				socio.setNroDoc(data.getString(4));
				socio.setNombRazSoc(data.getString(5));
				socio.setNomCom(data.getString(6));
				socio.setApePat(data.getString(7));
				socio.setApeMat(data.getString(8));
				socio.setPriNom(data.getString(9));
				socio.setSegNom(data.getString(10));
				socio.setTlf1(data.getString(11));
				socio.setTlf2(data.getString(12));
				socio.setTlfMov(data.getString(13));
				socio.setCorreo(data.getString(14));
				socio.setGrupo(data.getString(15));
				socio.setListaPrecio(data.getString(16));
				socio.setCondPago(data.getString(17));
				socio.setIndicador(data.getString(18));
				socio.setZona(data.getString(19));
				socio.setCreadoMovil(data.getString(20));
				socio.setClaveMovil(data.getString(21));
				socio.setDireccionFiscal(data.getString(22));
				socio.setEstadoRegistroMovil(data.getString(23));
				socio.setTransaccionMovil(data.getString(24));
				
				
				Cursor dataContacts = db.rawQuery(" SELECT " +
						 " Codigo, " +
						 " Nombre, " +
						 " PrimerNombre, " +
						 " SegundoNombre, " +
						 " Apellidos, " +
						 " Direccion, " +
						 " CorreoElectronico, " +
						 " Telefono1, " +
						 " Telefono2, " +
						 " TelefonoMovil, " +
						 " Posicion " +
						 " FROM TB_SOCIO_NEGOCIO_CONTACTO " +
						 " WHERE CodigoSocioNegocio = '"+socio.getCodigo()+"'", null); 
				
				if(dataContacts.getCount()>0)
				{
					contactos = new ArrayList<ContactoBean> ();
					while(dataContacts.moveToNext()){
						contacto = new ContactoBean();
						contacto.setIdCon(dataContacts.getString(0));
						contacto.setNomCon(dataContacts.getString(1));
						contacto.setPrimerNombre(dataContacts.getString(2));
						contacto.setSegNomCon(dataContacts.getString(3));
						contacto.setApeCon(dataContacts.getString(4));
						contacto.setDireccion(dataContacts.getString(5));
						contacto.setEmailCon(dataContacts.getString(6));
						contacto.setTel1Con(dataContacts.getString(7));
						contacto.setTel2Con(dataContacts.getString(8));
						contacto.setTelMovCon(dataContacts.getString(9));
						contacto.setPosicion(dataContacts.getString(10));
						contactos.add(contacto);
					}
					dataContacts.close();
					socio.setContactos(contactos);
				}
				
				Cursor dataDirections = db.rawQuery(" SELECT " +
						 " Codigo, " +
						 " Pais, " +
						 " Departamento, " +
						 " Provincia, " +
						 " Distrito, " +
						 " Calle, " +
						 " Referencia, " +
						 " Tipo " +
						 " FROM TB_SOCIO_NEGOCIO_DIRECCION " +
						 " WHERE CodigoSocioNegocio = '"+socio.getCodigo()+"'", null); 
				
				if(dataDirections.getCount()>0)
				{
					direcciones = new ArrayList<DireccionBean> ();
					while(dataDirections.moveToNext()){
						direccion = new DireccionBean();
						direccion.setIDDireccion(dataDirections.getString(0));
						direccion.setPais(dataDirections.getString(1));
						direccion.setDepartamento(dataDirections.getString(2));
						direccion.setProvincia(dataDirections.getString(3));
						direccion.setDistrito(dataDirections.getString(4));
						direccion.setCalle(dataDirections.getString(5));
						direccion.setReferencia(dataDirections.getString(6));
						direccion.setTipoDireccion(dataDirections.getString(7));
						direcciones.add(direccion);
					}
					dataDirections.close();
					socio.setDirecciones(direcciones);
				}
				
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return socio;
		
	}
	
	
	public ArrayList<OrdenVentaBean> listaOrdenesVentas(){
		
		listaOrdenesVenta = new ArrayList<OrdenVentaBean>();
		OrdenVentaBean  venta;
		OrdenVentaDetalleBean detalle;
		ArrayList<OrdenVentaDetalleBean> detalles;

		Cursor data= db.rawQuery(" SELECT " +
								 " Tipo, " +
								 " Clave, " +
								 " Numero, " +
								 " Referencia, " +
								 " SocioNegocio, " +
								 " ListaPrecio, " +
								 " Contacto, " +
								 " Moneda, " +
								 " EmpleadoVenta, " +
								 " Comentario, " +
								 " FechaContable, " +
								 " FechaVencimiento, " +
								 " DireccionFiscal, " +
								 " DireccionEntrega, " +
								 " CondicionPago, " +
								 " Indicador, " +
								 " SubTotal, " +
								 " Impuesto, " +
								 " Total, " +
								 " CreadMovil, " +
								 " ClaveMovil, " +
								 " EstadoMovil, " +
								 " TransaccionMovil "+
								 " FROM TB_ORDEN_VENTA " +
								 " WHERE EstadoMovil = 'L'", null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				venta = new OrdenVentaBean();
				venta.setTipoDoc(data.getString(0));
				venta.setClave(data.getString(1));
				venta.setNumero(data.getString(2));
				venta.setReferencia(data.getString(3));
				venta.setCodSN(data.getString(4));
				venta.setListaPrecio(data.getString(5));
				venta.setContacto(data.getString(6));
				venta.setMoneda(data.getString(7));
				venta.setEmpVentas(data.getString(8));
				venta.setComentario(data.getString(9));
				venta.setFecContable(data.getString(10));
				venta.setFecVen(data.getString(11));
				venta.setDirFiscal(data.getString(12));
				venta.setDirEntrega(data.getString(13));
				venta.setCondPago(data.getString(14));
				venta.setIndicador(data.getString(15));
				venta.setSubTotal(data.getString(16));
				venta.setImpuesto(data.getString(17));
				venta.setTotal(data.getString(18));
				venta.setCreadoMovil(data.getString(19));
				venta.setClaveMovil(data.getString(20));
				venta.setEstadoRegistroMovil(data.getString(21));
				venta.setTransaccionMovil(data.getString(22));
				venta.setEmpVentas(codigoEmpleado);
				
				Cursor dataDetails = db.rawQuery(" SELECT " +
						 " Articulo, " +
						 " UnidadMedida, " +
						 " Almacen, " +
						 " Cantidad, " +
						 " ListaPrecio, " +
						 " PrecioUnitario, " +
						 " PorcentajeDescuento, " +
						 " Impuesto, " +
						 " Linea " +
						 " FROM TB_ORDEN_VENTA_DETALLE " +
						 " WHERE ClaveMovil = '"+venta.getClave()+"'", null); 
				
				if(dataDetails.getCount()>0)
				{
					detalles = new ArrayList<OrdenVentaDetalleBean> ();
					while(dataDetails.moveToNext()){
						detalle = new OrdenVentaDetalleBean();
						detalle.setCodArt(dataDetails.getString(0));
						detalle.setCodUM(dataDetails.getString(1));
						detalle.setAlmacen(dataDetails.getString(2));
						detalle.setCantidad(Double.parseDouble(dataDetails.getString(3)));
						detalle.setListaPrecio(dataDetails.getString(4));
						detalle.setPrecio(Double.parseDouble(dataDetails.getString(5)));
						detalle.setDescuento(Double.parseDouble(dataDetails.getString(6)));
						detalle.setCodImp(dataDetails.getString(7));
						detalle.setLinea(dataDetails.getString(8));
						detalles.add(detalle);
					}
					dataDetails.close();
					venta.setDetalles(detalles);
				}
				listaOrdenesVenta.add(venta);
			}
			//Cerrar el cursor
			data.close();
		}
		
		return listaOrdenesVenta;
		
	}
	
	public OrdenVentaBean obtenerOrdenVenta(String clave){
		
		OrdenVentaBean  venta = null;
		OrdenVentaDetalleBean detalle;
		ArrayList<OrdenVentaDetalleBean> detalles;

		Cursor data= db.rawQuery(" SELECT " +
								 " Tipo, " +
								 " Clave, " +
								 " Numero, " +
								 " Referencia, " +
								 " SocioNegocio, " +
								 " ListaPrecio, " +
								 " Contacto, " +
								 " Moneda, " +
								 " EmpleadoVenta, " +
								 " Comentario, " +
								 " FechaContable, " +
								 " FechaVencimiento, " +
								 " DireccionFiscal, " +
								 " DireccionEntrega, " +
								 " CondicionPago, " +
								 " Indicador, " +
								 " SubTotal, " +
								 " Impuesto, " +
								 " Total, " +
								 " CreadMovil, " +
								 " ClaveMovil, " +
								 " EstadoMovil, " +
								 " TransaccionMovil "+
								 " FROM TB_ORDEN_VENTA " +
								 " WHERE Clave = '"+clave+"'", null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				venta = new OrdenVentaBean();
				venta.setTipoDoc(data.getString(0));
				venta.setClave(data.getString(1));
				venta.setNumero(data.getString(2));
				venta.setReferencia(data.getString(3));
				venta.setCodSN(data.getString(4));
				venta.setListaPrecio(data.getString(5));
				venta.setContacto(data.getString(6));
				venta.setMoneda(data.getString(7));
				venta.setEmpVentas(data.getString(8));
				venta.setComentario(data.getString(9));
				venta.setFecContable(data.getString(10));
				venta.setFecVen(data.getString(11));
				venta.setDirFiscal(data.getString(12));
				venta.setDirEntrega(data.getString(13));
				venta.setCondPago(data.getString(14));
				venta.setIndicador(data.getString(15));
				venta.setSubTotal(data.getString(16));
				venta.setImpuesto(data.getString(17));
				venta.setTotal(data.getString(18));
				venta.setCreadoMovil(data.getString(19));
				venta.setClaveMovil(data.getString(20));
				venta.setEstadoRegistroMovil(data.getString(21));
				venta.setTransaccionMovil(data.getString(22));
				
				Cursor dataDetails = db.rawQuery(" SELECT " +
						 " Articulo, " +
						 " UnidadMedida, " +
						 " Almacen, " +
						 " Cantidad, " +
						 " ListaPrecio, " +
						 " PrecioUnitario, " +
						 " PorcentajeDescuento, " +
						 " Impuesto, " +
						 " Linea " +
						 " FROM TB_ORDEN_VENTA_DETALLE " +
						 " WHERE ClaveMovil = '"+venta.getClave()+"'", null); 
				
				if(dataDetails.getCount()>0)
				{
					detalles = new ArrayList<OrdenVentaDetalleBean> ();
					while(dataDetails.moveToNext()){
						detalle = new OrdenVentaDetalleBean();
						detalle.setCodArt(dataDetails.getString(0));
						detalle.setCodUM(dataDetails.getString(1));
						detalle.setAlmacen(dataDetails.getString(2));
						detalle.setCantidad(Double.parseDouble(dataDetails.getString(3)));
						detalle.setListaPrecio(dataDetails.getString(4));
						detalle.setPrecio(Double.parseDouble(dataDetails.getString(5)));
						detalle.setDescuento(Double.parseDouble(dataDetails.getString(6)));
						detalle.setCodImp(dataDetails.getString(7));
						detalle.setLinea(dataDetails.getString(8));
						detalles.add(detalle);
					}
					dataDetails.close();
					venta.setDetalles(detalles);
				}
			
			}
			//Cerrar el cursor
			data.close();
		}
		
		return venta;
		
	}
	
	
	public ArrayList<PagoBean> listaPagosRecibidos(){
		
		listaPagoCliente = new ArrayList<PagoBean>();
		PagoBean  pago;
		PagoDetalleBean detalle;
		ArrayList<PagoDetalleBean> detalles;

		Cursor data= db.rawQuery(" SELECT " +
								 " Tipo, " +
								 " Clave, " +
								 " Numero, " +
								 " SocioNegocio, " +
								 " EmpleadoVenta, " +
								 " Comentario, " +
								 " Glosa, " +
								 " FechaContable, " +
								 " Moneda, " +
								 " TipoPago, " +
								 " TransferenciaCuenta, " +
								 " TransferenciaReferencia, " +
								 " TransferenciaImporte, " +
								 " EfectivoCuenta, " +
								 " EfectivoImporte, " +
								 " CreadMovil, " +
								 " ClaveMovil, " +
								 " EstadoMovil, " +
								 " TransaccionMovil " +
								 " FROM TB_PAGO " +
								 " WHERE EstadoMovil = 'L'", null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				pago = new PagoBean();
				pago.setTipo(data.getString(0));
				pago.setClave(data.getString(1));
				pago.setNumero(data.getString(2));
				pago.setSocioNegocio(data.getString(3));
				pago.setEmpleadoVenta(data.getString(4));
				pago.setComentario(data.getString(5));
				pago.setGlosa(data.getString(6));
				pago.setFechaContable(data.getString(7));
				pago.setMoneda(data.getString(8));
				pago.setTipoPago(data.getString(9));
				pago.setTransferenciaCuenta(data.getString(10));
				pago.setTransferenciaReferencia(data.getString(11));
				pago.setTransferenciaImporte(data.getString(12));
				pago.setEfectivoCuenta(data.getString(13));
				pago.setEfectivoImporte(data.getString(14));
				pago.setCreadoMovil(data.getString(15));
				pago.setClaveMovil(data.getString(16));
				pago.setEstadoRegistroMovil(data.getString(17));
				pago.setTransaccionMovil(data.getString(18));
				pago.setEmpleadoVenta(codigoEmpleado);
				
				Cursor dataDetails = db.rawQuery(" SELECT " +
						 " FacturaCliente, " +
						 " Importe " +
						 " FROM TB_PAGO_DETALLE " +
						 " WHERE ClavePago = '"+pago.getClave()+"'", null); 
				
				if(dataDetails.getCount()>0)
				{
					detalles = new ArrayList<PagoDetalleBean> ();
					while(dataDetails.moveToNext()){
						detalle = new PagoDetalleBean();
						detalle.setFacturaCliente(dataDetails.getString(0));
						detalle.setImporte(dataDetails.getString(1));
						detalles.add(detalle);
					}
					dataDetails.close();
					pago.setLineas(detalles);
				}
				listaPagoCliente.add(pago);
			}
			//Cerrar el cursor
			data.close();
		}
		
		return listaPagoCliente;
		
	}
		
	
	public PagoBean obtenerPagoRecibido(String clave){
		
		PagoBean  pago = null;
		PagoDetalleBean detalle;
		ArrayList<PagoDetalleBean> detalles;

		Cursor data= db.rawQuery(" SELECT " +
								 " Tipo, " +
								 " Clave, " +
								 " Numero, " +
								 " SocioNegocio, " +
								 " EmpleadoVenta, " +
								 " Comentario, " +
								 " Glosa, " +
								 " FechaContable, " +
								 " Moneda, " +
								 " TipoPago, " +
								 " TransferenciaCuenta, " +
								 " TransferenciaReferencia, " +
								 " TransferenciaImporte, " +
								 " EfectivoCuenta, " +
								 " EfectivoImporte, " +
								 " CreadMovil, " +
								 " ClaveMovil, " +
								 " EstadoMovil, " +
								 " TransaccionMovil, " +
								 " ChequeCuenta, " +
								 " ChequeBanco, " +
								 " ChequeVencimiento, " +
								 " ChequeImporte, " +
								 " ChequeNumero " +
								 " FROM TB_PAGO " +
								 " WHERE Clave = '"+clave+"'", null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				pago = new PagoBean();
				pago.setTipo(data.getString(0));
				pago.setClave(data.getString(1));
				pago.setNumero(data.getString(2));
				pago.setSocioNegocio(data.getString(3));
				pago.setEmpleadoVenta(data.getString(4));
				pago.setComentario(data.getString(5));
				pago.setGlosa(data.getString(6));
				pago.setFechaContable(data.getString(7));
				pago.setMoneda(data.getString(8));
				pago.setTipoPago(data.getString(9));
				pago.setTransferenciaCuenta(data.getString(10));
				pago.setTransferenciaReferencia(data.getString(11));
				pago.setTransferenciaImporte(data.getString(12));
				pago.setEfectivoCuenta(data.getString(13));
				pago.setEfectivoImporte(data.getString(14));
				pago.setCreadoMovil(data.getString(15));
				pago.setClaveMovil(data.getString(16));
				pago.setEstadoRegistroMovil(data.getString(17));
				pago.setTransaccionMovil(data.getString(18));
				pago.setChequeCuenta(data.getString(19));
				pago.setChequeBanco(data.getString(20));
				pago.setChequeVencimiento(data.getString(21));
				pago.setChequeImporte(data.getString(22));
				pago.setChequeNumero(data.getString(23));
				
				Cursor dataDetails = db.rawQuery(" SELECT " +
						 " FacturaCliente, " +
						 " Importe " +
						 " FROM TB_PAGO_DETALLE " +
						 " WHERE ClavePago = '"+pago.getClave()+"'", null); 
				
				if(dataDetails.getCount()>0)
				{
					detalles = new ArrayList<PagoDetalleBean> ();
					while(dataDetails.moveToNext()){
						detalle = new PagoDetalleBean();
						detalle.setFacturaCliente(dataDetails.getString(0));
						detalle.setImporte(dataDetails.getString(1));
						detalles.add(detalle);
					}
					dataDetails.close();
					pago.setLineas(detalles);
				}
			
			}
			//Cerrar el cursor
			data.close();
		}
		
		return pago;
		
	}
	
	
	public FacturaBean obtenerFacturaPorCodigo(String clave){
		
		FacturaBean  bean = null;
		Cursor rsFacts = db.rawQuery(
				"select Tipo,Clave, Numero, Referencia, SocioNegocio,ListaPrecio," +
				"Contacto,M.NOMBRE, EmpleadoVenta, Comentario, FechaContable,FechaVencimiento," +
				"DireccionFiscal,DireccionEntrega, cp.nombre, I.NOMBRE, SubTotal," +
				"Descuento, Impuesto,Total, Saldo "
						+ "from TB_FACTURA F left join TB_CONDICION_PAGO CP " +
						" on F.CondicionPago = cp.CODIGO left join TB_INDICADOR I " +
						" on F.Indicador = I.CODIGO left join TB_MONEDA M " +
						" on F.Moneda = M.CODIGO " 
						+ "where Clave ='"
						+ clave + "' " , null);
		
		if(rsFacts.getCount() > 0){

			while (rsFacts.moveToNext()) {
				
				bean = new FacturaBean();
				  	bean.setTipo(rsFacts.getString(0));
					bean.setClave(rsFacts.getString(1));
					bean.setNumero(rsFacts.getString(2));
					bean.setReferencia(rsFacts.getString(3));
					bean.setSocioNegocio(rsFacts.getString(4));
					bean.setListaPrecio(rsFacts.getString(5));
					bean.setContacto(rsFacts.getString(6));
					bean.setMoneda(rsFacts.getString(7));
					bean.setEmpleadoVenta(rsFacts.getString(8));
					bean.setComentario(rsFacts.getString(9));
					bean.setFechaContable(rsFacts.getString(10));
					bean.setFechaVencimiento(rsFacts.getString(11));
					bean.setDireccionFiscal(rsFacts.getString(12));
					bean.setDireccionEntrega(rsFacts.getString(13));
					bean.setCondicionPago(rsFacts.getString(14));
					bean.setIndicador(rsFacts.getString(15));
					bean.setSubTotal(rsFacts.getString(16));
					bean.setDescuento(rsFacts.getString(17));
					bean.setImpuesto(rsFacts.getString(18));
					bean.setTotal(rsFacts.getString(19));
					bean.setSaldo(rsFacts.getString(20));
					bean.setUtilPagoTotal(rsFacts.getString(20));
					
					Cursor rsDet = db.rawQuery(
							"select Articulo, UnidadMedida, Almacen, Cantidad,ListaPrecio," +
									"PrecioUnitario,PorcentajeDescuento, Impuesto "
											+ "from TB_FACTURA_DETALLE where ClaveFactura ='"
											+ bean.getClave() + "'", null);
					ArrayList<FacturaDetalleBean> listaDetalle 
										= new ArrayList<FacturaDetalleBean>();
					FacturaDetalleBean detalle = null;
					while (rsDet.moveToNext()) {
						detalle = new FacturaDetalleBean();
						detalle.setArticulo(rsDet.getString(0));
						detalle.setUnidadMedida(rsDet.getString(1));
						detalle.setAlmacen(rsDet.getString(2));
						detalle.setCantidad(rsDet.getString(3));
						detalle.setListaPrecio(rsDet.getString(4));
						detalle.setPrecioUnitario(rsDet.getString(5));
						detalle.setPorcentajeDescuento(rsDet.getString(6));
						detalle.setImpuesto(rsDet.getString(7));
						listaDetalle.add(detalle);
					}
					rsDet.close();
					
					bean.setLineas(listaDetalle);

			}
		}
		
		return bean;
		
	}
	
	
	public ArrayList<FacturaBean> listaFacturasPorSocio(String clave, String clavePago){
		
		ArrayList<FacturaBean> facturas = null;

		String castDate = StringDateCast.castDatetoDateWithoutSlash(CobranzaFragment.currentDate);
		Cursor rsFacts = db.rawQuery(
				"select Tipo,Clave, Numero, Referencia, SocioNegocio,ListaPrecio," +
				"Contacto,M.NOMBRE, EmpleadoVenta, Comentario, FechaContable,FechaVencimiento," +
				"DireccionFiscal,DireccionEntrega, cp.nombre, I.NOMBRE, SubTotal," +
				"Descuento, Impuesto,Total, Saldo "
						+ "from TB_FACTURA F left join TB_CONDICION_PAGO CP " +
						" on F.CondicionPago = cp.CODIGO left join TB_INDICADOR I " +
						" on F.Indicador = I.CODIGO left join TB_MONEDA M " +
						" on F.Moneda = M.CODIGO " 
						+ "where SocioNegocio ='"
						+ clave + "' " 
						+ "and FechaContable <= '"+castDate+"' " 
						+ "and CAST(IFNULL(Saldo,'0') as NUMERIC) > 0 " 
						//+ "and (select count(*) from TB_PAGO_DETALLE where FacturaCliente = F.Clave and ClavePago <> '"+clavePago+"') <= 0"
						, null);
		FacturaBean bean = null;
		
		if(rsFacts.getCount() > 0){
			facturas = new ArrayList<>();
			while (rsFacts.moveToNext()) {
						
				bean = new FacturaBean();
				  	bean.setTipo(rsFacts.getString(0));
					bean.setClave(rsFacts.getString(1));
					bean.setNumero(rsFacts.getString(2));
					bean.setReferencia(rsFacts.getString(3));
					bean.setSocioNegocio(rsFacts.getString(4));
					bean.setListaPrecio(rsFacts.getString(5));
					bean.setContacto(rsFacts.getString(6));
					bean.setMoneda(rsFacts.getString(7));
					bean.setEmpleadoVenta(rsFacts.getString(8));
					bean.setComentario(rsFacts.getString(9));
					bean.setFechaContable(rsFacts.getString(10));
					bean.setFechaVencimiento(rsFacts.getString(11));
					bean.setDireccionFiscal(rsFacts.getString(12));
					bean.setDireccionEntrega(rsFacts.getString(13));
					bean.setCondicionPago(rsFacts.getString(14));
					bean.setIndicador(rsFacts.getString(15));
					bean.setSubTotal(rsFacts.getString(16));
					bean.setDescuento(rsFacts.getString(17));
					bean.setImpuesto(rsFacts.getString(18));
					bean.setTotal(rsFacts.getString(19));
					bean.setSaldo(rsFacts.getString(20));
					bean.setUtilPagoTotal(rsFacts.getString(20));
					
					Cursor rsDet = db.rawQuery(
							"select Articulo, UnidadMedida, Almacen, Cantidad,ListaPrecio," +
									"PrecioUnitario,PorcentajeDescuento, Impuesto "
											+ "from TB_FACTURA_DETALLE where ClaveFactura ='"
											+ bean.getClave() + "'", null);
					ArrayList<FacturaDetalleBean> listaDetalle 
										= new ArrayList<FacturaDetalleBean>();
					FacturaDetalleBean detalle = null;
					while (rsDet.moveToNext()) {
						detalle = new FacturaDetalleBean();
						detalle.setArticulo(rsDet.getString(0));
						detalle.setUnidadMedida(rsDet.getString(1));
						detalle.setAlmacen(rsDet.getString(2));
						detalle.setCantidad(rsDet.getString(3));
						detalle.setListaPrecio(rsDet.getString(4));
						detalle.setPrecioUnitario(rsDet.getString(5));
						detalle.setPorcentajeDescuento(rsDet.getString(6));
						detalle.setImpuesto(rsDet.getString(7));
						listaDetalle.add(detalle);
					}
					rsDet.close();
					
					bean.setLineas(listaDetalle);
					facturas.add(bean);
			}
			
		}
		rsFacts.close();
		
		return facturas;
		
	}
	
	
	
	public ArrayList<AlmacenBean> listaAlmacen(){
		
		listaAlmacen = new ArrayList<AlmacenBean>();
		AlmacenBean almacen;

		Cursor data= db.rawQuery("select "
							+ contexto.getResources().getString(R.string.C_ALMACEN_COD) +","
							+ contexto.getResources().getString(R.string.C_ALMACEN_NOM) +" "
							+ "from " 
							+ contexto.getResources().getString(R.string.T_ALMACEN) , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				almacen = new AlmacenBean();
				almacen.setCodigo(data.getString(0));
				almacen.setDescripcion(data.getString(1));
				listaAlmacen.add(almacen);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaAlmacen;
		
	}
	
	
	public ArrayList<ListaPrecioBean> listaPrecios(){
		
		listaPrecios = new ArrayList<ListaPrecioBean>();
		ListaPrecioBean objeto;

		Cursor data= db.rawQuery("select "
							+ contexto.getResources().getString(R.string.C_LST_PRE_COD) +","
							+ contexto.getResources().getString(R.string.C_LST_PRE_NOM) +" "
							+ "from " 
							+ contexto.getResources().getString(R.string.T_LISTA_PRECIO) , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new ListaPrecioBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaPrecios.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaPrecios;
		
	}
	
	
	public ArrayList<ArticuloBean> listaArticulos(){
		
		listaArticulos = new ArrayList<ArticuloBean>();
		ArticuloBean objeto;

		Cursor data= db.rawQuery("select " +
					"A.Codigo, A.Nombre, A.Fabricante, A.GrupoArticulo, A.GrupoUnidadMedida, A.UnidadMedidaVenta, " +
					"F.NOMBRE, GUM.Nombre, UM.Nombre" +
								" FROM TB_ARTICULO A JOIN  TB_FABRICANTE F " +
								" ON A.Fabricante = F.CODIGO JOIN TB_GRUPO_UNIDAD_MEDIDA GUM " +
								" ON A.GrupoUnidadMedida = GUM.Codigo JOIN TB_UNIDAD_MEDIDA UM " +
								" ON A.UnidadMedidaVenta = UM.Codigo", null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new ArticuloBean();
				objeto.setCod(data.getString(0));
				objeto.setDesc(data.getString(1));
				objeto.setFabricante(data.getString(2));
				objeto.setGrupoArticulo(data.getString(3));
				objeto.setCodUM(data.getString(4));
				objeto.setUnidadMedidaVenta(data.getString(5));
				objeto.setNombreFabricante(data.getString(6));
				objeto.setNombreUnidadMedida(data.getString(7));
				objeto.setNombreUnidadMedidaVenta(data.getString(8));
				listaArticulos.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaArticulos;
		
	}
	
	
	public ArrayList<CantidadBean> listaCantidades(){
		
		listaCantidades = new ArrayList<CantidadBean>();
		CantidadBean objeto;

		Cursor data= db.rawQuery("select " +
					"c.ALMACEN, c.ARTICULO, c.STOCK, c.COMPROMETIDO, c.SOLICITADO, c.DISPONIBLE, " +
					"A.NOMBRE, ART.Nombre" +
								" FROM TB_CANTIDAD c JOIN  TB_ALMACEN A " +
								" ON A.CODIGO = C.ALMACEN JOIN TB_ARTICULO ART " +
								" ON ART.Codigo = C.ARTICULO", null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new CantidadBean();
				objeto.setAlmacen(data.getString(0));
				objeto.setArticulo(data.getString(1));
				objeto.setStock(data.getString(2));
				objeto.setComprometido(data.getString(3));
				objeto.setSolicitado(data.getString(4));
				objeto.setDisponible(data.getString(5));
				objeto.setNombreAlmacen(data.getString(6));
				objeto.setNombreArticulo(data.getString(7));
				listaCantidades.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaCantidades;
		
	}
	
	
	public ArrayList<CondicionPagoBean> listaCondicionPago(){
		
		listaCondicionPago = new ArrayList<CondicionPagoBean>();
		CondicionPagoBean objeto;

		Cursor data= db.rawQuery("select CODIGO, NOMBRE from TB_CONDICION_PAGO" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new CondicionPagoBean();
				objeto.setNumeroCondicion(data.getString(0));
				objeto.setDescripcionCondicion(data.getString(1));
				listaCondicionPago.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaCondicionPago;
		
	}
	
	
	public ArrayList<FabricanteBean> listaFabricante(){
		
		listaFabricantes = new ArrayList<FabricanteBean>();
		FabricanteBean objeto;

		Cursor data= db.rawQuery("select CODIGO, NOMBRE from TB_FABRICANTE" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new FabricanteBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaFabricantes.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaFabricantes;
		
	}
	
	
	public ArrayList<IndicadorBean> listaIndicadores(){
		
		listaIndicador = new ArrayList<IndicadorBean>();
		IndicadorBean objeto;

		Cursor data= db.rawQuery("select CODIGO, NOMBRE from TB_INDICADOR" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new IndicadorBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaIndicador.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaIndicador;
		
	}

	public List<ProyectoBean> listaProyectos(){

		List lst = new ArrayList<ProyectoBean>();
		ProyectoBean objeto;

		Cursor data= db.rawQuery("select CODIGO, DESCRIPCION from TB_PROYECTO" , null);
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {
			objeto = new ProyectoBean();
			objeto.setCodigo(data.getString(data.getColumnIndex("CODIGO")));
			objeto.setDescripcion(data.getString(data.getColumnIndex("DESCRIPCION")));
			lst.add(objeto);
		}

			data.close();
		}

		return lst;
	}

	public List<CanalBean> listaCanales(){

		List lst = new ArrayList<CanalBean>();
		CanalBean objeto;

		Cursor data= db.rawQuery("select CODIGO, DESCRIPCION from TB_CANAL" , null);
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {
				objeto = new CanalBean();
				objeto.setCodigo(data.getString(data.getColumnIndex("CODIGO")));
				objeto.setDescripcion(data.getString(data.getColumnIndex("DESCRIPCION")));
				lst.add(objeto);
			}

			data.close();
		}

		return lst;
	}

	public List<GiroBean> listaGiro(){

		List lst = new ArrayList<GiroBean>();
		GiroBean objeto;

		Cursor data= db.rawQuery("select CODIGO, DESCRIPCION from TB_GIRO" , null);
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {
				objeto = new GiroBean();
				objeto.setCodigo(data.getString(data.getColumnIndex("CODIGO")));
				objeto.setDescripcion(data.getString(data.getColumnIndex("DESCRIPCION")));
				lst.add(objeto);
			}

			data.close();
		}

		return lst;
	}
	
	public ArrayList<GrupoSocioNegocioBean> listaGrupoSocioNegocio(){
		
		listaGruposSocioNegocio = new ArrayList<GrupoSocioNegocioBean>();
		GrupoSocioNegocioBean objeto;

		Cursor data= db.rawQuery("select CODIGO, NOMBRE from TB_GRUPO_SOCIO_NEGOCIO" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new GrupoSocioNegocioBean();
				objeto.setGroupCode(data.getString(0));
				objeto.setGroupName(data.getString(1));
				listaGruposSocioNegocio.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaGruposSocioNegocio;
		
	}
	
	
	public ArrayList<MonedaBean> listaMonedas(){
		
		listaMonedas = new ArrayList<MonedaBean>();
		MonedaBean objeto;

		Cursor data= db.rawQuery("select CODIGO, NOMBRE from TB_MONEDA" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new MonedaBean();
				objeto.setCodigo(data.getString(0));
				objeto.setDescripcion(data.getString(1));
				listaMonedas.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaMonedas;
		
	}
	
	
	public ArrayList<ZonaBean> listaZona(){
		
		listaZona = new ArrayList<ZonaBean>();
		ZonaBean objeto;

		Cursor data= db.rawQuery("select CODIGO, NOMBRE from TB_ZONA" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new ZonaBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaZona.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaZona;
		
	}
	
	
	
	public ArrayList<PaisBean> listaPais(){
		
		listaPais = new ArrayList<PaisBean>();
		PaisBean objeto;

		Cursor data= db.rawQuery("select CODIGO, NOMBRE from TB_PAIS" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new PaisBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaPais.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaPais;
		
	}
	
	
	public ArrayList<DepartamentoBean> listaDepartamentos(String codPais){
		
		listaDepartamentos = new ArrayList<DepartamentoBean>();
		DepartamentoBean objeto;

		Cursor data= db.rawQuery("select CODIGO, NOMBRE from TB_DEPARTAMENTO " +
									"where CODIGO_PAIS = '"+codPais+"'" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new DepartamentoBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaDepartamentos.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaDepartamentos;
		
	}
	
	
	public ArrayList<ProvinciaBean> listaProvincias(String codDepartamento){
		
		listaProvincias = new ArrayList<ProvinciaBean>();
		ProvinciaBean objeto;

		Cursor data= db.rawQuery("select CODIGO, NOMBRE from TB_PROVINCIA " +
									"where CODIGO_DEPARTAMENTO = '"+codDepartamento+"'" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new ProvinciaBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaProvincias.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaProvincias;
		
	}
	
	
	
	public ArrayList<DistritoBean> listaDistritos(String codProvincia){
		
		listaDistritos = new ArrayList<DistritoBean>();
		DistritoBean objeto;

		Cursor data= db.rawQuery("select CODIGO, NOMBRE from TB_DISTRITO " +
									"where CODIGO_PROVINCIA = '"+codProvincia+"'" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new DistritoBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaDistritos.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaDistritos;
		
	}
	
	
	public ArrayList<CalleBean> listaCalles(String codDistrito){
		
		listaCalles = new ArrayList<CalleBean>();
		CalleBean objeto;

		Cursor data= db.rawQuery("select CODIGO, NOMBRE from TB_CALLE " +
									"where CODIGO_DISTRITO = '"+codDistrito+"' " +
										 "order by NOMBRE" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new CalleBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaCalles.add(objeto);
			}
			
		}
		data.close();
		
		return listaCalles;
		
	}
	
	
	public ArrayList<ImpuestoBean> listaImpuesto(){
		
		listaImpuesto = new ArrayList<ImpuestoBean>();
		ImpuestoBean objeto;

		Cursor data= db.rawQuery("select CODIGO, NOMBRE from TB_IMPUESTO " , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new ImpuestoBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaImpuesto.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaImpuesto;
		
	}
	
	
	public ArrayList<CuentaBean> listaCuentas(){
		
		listaCuenta = new ArrayList<CuentaBean>();
		CuentaBean objeto;

		Cursor data= db.rawQuery("select Codigo, Nombre from TB_CUENTA " , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new CuentaBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaCuenta.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaCuenta;
		
	}
	
	
	public ArrayList<BancoBean> listaBancos(){
		
		listaBanco = new ArrayList<BancoBean>();
		BancoBean objeto;

		Cursor data= db.rawQuery("select Codigo, Nombre from TB_BANCO " , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new BancoBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaBanco.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaBanco;
		
	}
	
	
	
	public ArrayList<UnidadMedidaBean> listaUnidadesDeMedida(String param){
		
		listaUnidadMedida = new ArrayList<UnidadMedidaBean>();
		UnidadMedidaBean objeto;
		
		Cursor data= db.rawQuery("select U.Codigo, U.NOMBRE from TB_GRUPO_UNIDAD_MEDIDA_DETALLE G " +
				"join TB_UNIDAD_MEDIDA U ON G.CodigoUnidadMedida = U.Codigo " +
				"WHERE G.CodigoGrupo = '"+param+"' " , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new UnidadMedidaBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaUnidadMedida.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaUnidadMedida;
		
	}
	
	
	
	public ArrayList<GrupoUnidadMedidaBean> listaUnidadesDeMedidapRUEBA(){
		
		ArrayList<GrupoUnidadMedidaBean> lista = new ArrayList<GrupoUnidadMedidaBean>();
		GrupoUnidadMedidaBean objeto;

		Cursor data= db.rawQuery("select CodigoGrupo, U.NOMBRE from TB_GRUPO_UNIDAD_MEDIDA_DETALLE G " +
				"join TB_UNIDAD_MEDIDA U ON G.CodigoUnidadMedida = U.Codigo" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new GrupoUnidadMedidaBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				lista.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return lista;
		
	}
	
	
	public ArrayList<UnidadMedidaBean> listaUnidadesDeMedida(){
		
		listaUnidadMedida = new ArrayList<UnidadMedidaBean>();
		UnidadMedidaBean objeto;

		Cursor data= db.rawQuery("select Codigo, Nombre from TB_UNIDAD_MEDIDA" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new UnidadMedidaBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaUnidadMedida.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return listaUnidadMedida;
		
	}
	
	
	public ArrayList<GrupoArticuloBean> listaGrupoArticulo(){
		
		listaGruposArticulo = new ArrayList<GrupoArticuloBean>();
		GrupoArticuloBean objeto;

		Cursor data= db.rawQuery("select Codigo, Nombre from TB_GRUPO_ARTICULO " +
								 "where Codigo in (Select GrupoArticulo from TB_ARTICULO)" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				objeto = new GrupoArticuloBean();
				objeto.setCodigo(data.getString(0));
				objeto.setNombre(data.getString(1));
				listaGruposArticulo.add(objeto);
			}
			//Cerrar el cursor
			data.close();
		}
		
		return listaGruposArticulo;
		
	}
	
	
	public ArrayList<ContactoBean> listaContactosOV(String codigoSN){
		
		ArrayList<ContactoBean> lista = new ArrayList<ContactoBean>();
		ContactoBean b;

		Cursor rs = db.rawQuery(
				"select Codigo,Nombre, PrimerNombre, Apellidos, Principal "
						+ "from TB_SOCIO_NEGOCIO_CONTACTO where CodigoSocioNegocio ='"
						+ codigoSN + "'", null);
		
		if(rs.getCount()>0)
		{
			while (rs.moveToNext()) {

				b = new ContactoBean();
				b.setIdCon(rs.getString(0));
				b.setNomCon(rs.getString(1));
				b.setPrimerNombre(rs.getString(2));
				b.setApeCon(rs.getString(3));
				if (rs.getString(4).equals("1"))
					b.setPrincipal(true);
				else
					b.setPrincipal(false);
				lista.add(b);
				
			}
									
		}
		
		return lista;
		
	}
	
	
	public ArrayList<DireccionBean> listaDireccionesOV(String codigoSN){
		
		ArrayList<DireccionBean> lista = new ArrayList<DireccionBean>();
		DireccionBean d;

		Cursor rs2 = db.rawQuery(
				"select D.Tipo,D.Codigo, ifnull(C.NOMBRE, D.Referencia) from TB_SOCIO_NEGOCIO_DIRECCION D " +
						"left join TB_CALLE C ON D.Calle = C.CODIGO "
						+ "where CodigoSocioNegocio ='"
						+ codigoSN + "'", null);
		
		if(rs2.getCount()>0)
		{
			while (rs2.moveToNext()) {

				d = new DireccionBean();
				d.setTipoDireccion(rs2.getString(0));
				d.setIDDireccion(rs2.getString(1));
				d.setCalle(rs2.getString(2));

				lista.add(d);
			}					
		}
		
		return lista;
		
	}
	
	
	public String selectPrecioArticulo(String listaPrecio, String codigoArticulo){

		String res = "";
		
		Cursor data= db.rawQuery("select PrecioVenta from TB_PRECIO " +
								 "where CodigoLista='"+listaPrecio+"' " +
								 "and Articulo ='"+codigoArticulo+"' " , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				res = data.getString(0);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return res;
		
	}
	
	
	public GrupoUnidadMedidaBean selectGrupoUMArticulo(String codigoArticulo){

		GrupoUnidadMedidaBean bean = null;
		
		Cursor data= db.rawQuery("select G.Codigo, G.Nombre from TB_ARTICULO A " +
								 "join  TB_GRUPO_UNIDAD_MEDIDA G " +
								 "ON A.GrupoUnidadMedida = G.Codigo " +
								 "where A.Codigo='"+codigoArticulo+"' " , null); 
		
		if(data.getCount()>0)
		{
			bean = new GrupoUnidadMedidaBean();
			while (data.moveToNext()) {		
				bean.setCodigo(data.getString(0));
				bean.setNombre(data.getString(1));
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return bean;
		
	}
	
	
	public String selectnombreUMArticulo(String codigoArticulo){

		String res = "";
		
		Cursor data= db.rawQuery("select UM.Nombre from TB_ORDEN_VENTA_DETALLE A " +
								 "join  TB_UNIDAD_MEDIDA UM " +
								 "ON A.UnidadMedida = UM.Codigo "  +
								 "where A.Articulo='"+codigoArticulo+"' " , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				res = data.getString(0);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return res;
		
	}


	public String selectnombreListaPrecioArticulo(String codigoLP){

		String res = "";

		Cursor data= db.rawQuery("select Nombre from TB_LISTA_PRECIO "  +
				"where Codigo='"+codigoLP+"' " , null);

		if(data.getCount()>0)
		{
			while (data.moveToNext()) {
				res = data.getString(0);
			}

			//Cerrar el cursor
			data.close();

		}

		return res;

	}
	
	
	
	public ArrayList<FormatCustomListView> customList(String customName, String param){
		
		listaPersonalizada = new ArrayList<FormatCustomListView>();
		FormatCustomListView objeto;
		
		Cursor rs = null;
		if(customName.equals("detalleSNdirecciones")){
//			rs = db.rawQuery(
//					"select SN.Tipo,SN.Codigo, IFNULL(P.NOMBRE,''), " +
//					"IFNULL(D.NOMBRE,''), IFNULL(PR.NOMBRE,'')," +
//					"IFNULL(DT.NOMBRE,''), IFNULL(C.NOMBRE,'null'), IFNULL(SN.Referencia,'null') "
//							+ " from TB_SOCIO_NEGOCIO_DIRECCION SN left join TB_PAIS P"
//							+ " ON SN.Pais = P.CODIGO left join TB_DEPARTAMENTO D"
//							+ " ON SN.Departamento = D.CODIGO left join TB_PROVINCIA PR"
//							+ " ON SN.Provincia = PR.CODIGO left join TB_DISTRITO DT"
//							+ " ON SN.Distrito = DT.CODIGO left join TB_CALLE C"
//							+ " ON SN.Calle = C.CODIGO "
//							+ " WHERE SN.CodigoSocioNegocio ='" + param + "'",
//					null);

			rs = db.rawQuery(
					"select SN.Tipo as \"Tipo\",SN.Codigo, IFNULL(P.NOMBRE,''), " +
							"IFNULL(D.NOMBRE,''), IFNULL(SN.Provincia,'')," +
							"IFNULL(SN.Distrito,''), IFNULL(SN.Calle,'null'), IFNULL(SN.Referencia,'null'),"
							+ " IFNULL(SN.Latitud,'') as \"Latitud\", IFNULL(SN.Longitud,'') as \"Longitud\", "
							+ " IFNULL(SN.Zona, '') AS Zona, "
							+ " IFNULL(SN.Ruta, '') AS Ruta, "
							+ " IFNULL(X0.DESCRIPCION, '') AS Canal, "
							+ " IFNULL(X1.DESCRIPCION, '') AS Giro, "
							+ " IFNULL(SN.Vendedor, '') AS Vendedor "
							+ " from TB_SOCIO_NEGOCIO_DIRECCION SN  left join TB_PAIS P "
							+ " ON SN.Pais = P.CODIGO left join TB_DEPARTAMENTO D "
							+ " ON SN.Departamento = D.CODIGO left join TB_CANAL X0 "
							+ " ON SN.Canal = X0.CODIGO LEFT JOIN TB_GIRO X1 "
							+ " ON SN.Giro = X1.CODIGO "
							+ " WHERE SN.CodigoSocioNegocio ='" + param + "'",
					null);
			
			while (rs.moveToNext()) {

				objeto = new FormatCustomListView();

				objeto.setTipo(rs.getString(rs.getColumnIndex("Tipo")));
				objeto.setLatitud(rs.getString((rs.getColumnIndex("Latitud"))));
				objeto.setLongitud(rs.getString((rs.getColumnIndex("Longitud"))));
				objeto.setVendedor(rs.getString(rs.getColumnIndex("Vendedor")));
				objeto.setZona(rs.getString(rs.getColumnIndex("Zona")));
				objeto.setRuta(rs.getString(rs.getColumnIndex("Ruta")));
				objeto.setCanal(rs.getString(rs.getColumnIndex("Canal")));
				objeto.setGiro(rs.getString(rs.getColumnIndex("Giro")));
				objeto.setTitulo(rs.getString(1));
				if(rs.getString(6) != null)
					objeto.setData(rs.getString(6));
				else
					objeto.setData(rs.getString(7));
				 
				String val = "null";
				if(rs.getString(6).isEmpty()){
					objeto.setExtra(rs.getString(2)+";"+ rs.getString(3)+";"+ rs.getString(4)+";"
							   +rs.getString(5)+";"+ val+";"+ rs.getString(7));
				}else if(rs.getString(7).isEmpty()){
					objeto.setExtra(rs.getString(2)+";"+ rs.getString(3)+";"+ rs.getString(4)+";"
							   +rs.getString(5)+";"+ rs.getString(6)+";"+ val);
				}else{
					objeto.setExtra(rs.getString(2)+";"+ rs.getString(3)+";"+ rs.getString(4)+";"
							   +rs.getString(5)+";"+ rs.getString(6)+";"+  rs.getString(7));
				}

				listaPersonalizada.add(objeto);
			}
		
		}

		if(!rs.isClosed())
			rs.close();
		return listaPersonalizada;
		
	}
	
	
	
	public int numeroCorrelativoRegistro(String tipoRegistro){
		
		int res = 0;
		Cursor data= db.rawQuery("select NUM_COR  from TB_COR " +
									"where COD_COR = '"+tipoRegistro+"'" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				res = data.getInt(0);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return res;
		
	}
	
	
	public String obtenerNombreSocioNegocio(String codigo){
		
		String res = "";
		Cursor data= db.rawQuery("select NombreRazonSocial  from TB_SOCIO_NEGOCIO " +
									"where Codigo = '"+codigo+"'" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				res = data.getString(0);
			}
			
			data.close();
									
		}
		
		return res;
		
	}
	
	
	public String obtenerNombreContacto(String codigoSN, String codigo){
		
		String res = "";
		Cursor data= db.rawQuery("select Nombre from TB_SOCIO_NEGOCIO_CONTACTO " +
									"where CodigoSocioNegocio = '"+codigoSN+"' " +
											"and Codigo = '"+codigo+"'" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				res = data.getString(0);
			}
			
			data.close();
									
		}
		
		return res;
		
	}
	
	
	public String[] obtenerDescripcionDireccion(String codigoSN, String codigo){
		
		String[] res = null;
		Cursor data= db.rawQuery("select C.NOMBRE,Referencia from TB_SOCIO_NEGOCIO_DIRECCION D " +
								 "LEFT JOIN TB_CALLE C ON D.Calle = C.CODIGO " +
									"where D.CodigoSocioNegocio = '"+codigoSN+"' " +
											"and D.Codigo = '"+codigo+"'" , null); 
		
		if(data.getCount()>0)
		{
			res = new String[2];
			while (data.moveToNext()) {		
				res[0] = data.getString(0);
				res[1] = data.getString(1);
			}
			
			data.close();
									
		}
		
		return res;
		
	}
	
	public String obtenerNombreArticulo(String codigo){
		
		String res = "";
		Cursor data= db.rawQuery("select Nombre from TB_ARTICULO " +
									"where Codigo = '"+codigo+"'" , null); 
		
		if(data.getCount()>0)
		{
			while (data.moveToNext()) {		
				res = data.getString(0);
			}
			
			data.close();
									
		}
		
		return res;
		
	}

	public float inicioGetTotalPedidosAprobados(){

		float monto = 0;

		Cursor data = db.rawQuery("select IFNULL(sum(cast(Total as numeric)),0) as 'Total' from TB_ORDEN_VENTA where Tipo = 'A'", null);
		if(data.moveToFirst()){
			do{
				monto = data.getFloat(data.getColumnIndex("Total"));
			}while(data.moveToNext());
		}

		if(data != null && !data.isClosed())
			data.close();

		return monto;
	}

	public float inicioGetTotalPedidosPendientes(){

		float monto = 0;

		Cursor data = db.rawQuery("select IFNULL(sum(cast(Total as numeric)),0) as 'Total' from TB_ORDEN_VENTA where Tipo = 'P'", null);
		if(data.moveToFirst()){
			do{
				monto = data.getFloat(data.getColumnIndex("Total"));
			}while(data.moveToNext());
		}

		if(data != null && !data.isClosed())
			data.close();

		return monto;
	}

	public float inicioGetTotalPagosAprobados(){

		float monto = 0;

		Cursor data = db.rawQuery("select IFNULL(sum(cast(T0.Importe as numeric)),0) as 'Total' from TB_PAGO_DETALLE T0  JOIN TB_PAGO T1 ON T0.ClavePago =T1.Clave  AND T1.Tipo = 'A'", null);
		if(data.moveToFirst()){
			do{
				monto = data.getFloat(data.getColumnIndex("Total"));
			}while(data.moveToNext());
		}

		if(data != null && !data.isClosed())
			data.close();

		return monto;
	}

	public float inicioGetTotalPagosPendientes(){

		float monto = 0;

		Cursor data = db.rawQuery("select IFNULL(sum(cast(T0.Importe as numeric)),0) as 'Total' from TB_PAGO_DETALLE T0  JOIN TB_PAGO T1 ON T0.ClavePago =T1.Clave  AND T1.Tipo = 'P'", null);
		if(data.moveToFirst()){
			do{
				monto = data.getFloat(data.getColumnIndex("Total"));
			}while(data.moveToNext());
		}

		if(data != null && !data.isClosed())
			data.close();

		return monto;
	}

	public ArrayList<BeanChartProducto> inicioGetTopTenProducts(){

		ArrayList<BeanChartProducto> list = null;
		BeanChartProducto bean;

		Cursor data = db.rawQuery("select Articulo," +
				"(select Nombre from TB_ARTICULO where Codigo = Articulo) as 'Descripcion' , " +
				"sum(Cantidad) as 'Cantidad', " +
				"sum(PrecioUnitario * Cantidad) AS 'Total' " +
				"from TB_ORDEN_VENTA_DETALLE " +
				"GROUP BY Articulo " +
				"ORDER BY Total DESC LIMIT 5", null);
		if(data.moveToFirst()){
			list = new ArrayList<>();
			do{
				bean = new BeanChartProducto();
				bean.setId(data.getString(data.getColumnIndex("Articulo")));
				bean.setDescripcion(data.getString(data.getColumnIndex("Descripcion")));
				bean.setCantidad(data.getDouble(data.getColumnIndex("Cantidad")));
				bean.setTotal(data.getDouble(data.getColumnIndex("Total")));
				list.add(bean);

			}while(data.moveToNext());
		}

		if(data != null && !data.isClosed())
			data.close();

		return list;
	}

	
	
	public void close(){
		
//		db.close();
//		cn.close();
		
	}
	
	
}
