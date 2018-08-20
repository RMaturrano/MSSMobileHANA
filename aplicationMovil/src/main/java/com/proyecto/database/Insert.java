package com.proyecto.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.proyect.movil.R;
import com.proyecto.bean.AlmacenBean;
import com.proyecto.bean.ArticuloBean;
import com.proyecto.bean.BancoBean;
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
import com.proyecto.bean.FacturaDetalleLoteBean;
import com.proyecto.bean.GiroBean;
import com.proyecto.bean.GrupoArticuloBean;
import com.proyecto.bean.GrupoSocioNegocioBean;
import com.proyecto.bean.GrupoUnidadMedidaBean;
import com.proyecto.bean.ImpuestoBean;
import com.proyecto.bean.IndicadorBean;
import com.proyecto.bean.ListaPrecioBean;
import com.proyecto.bean.MonedaBean;
import com.proyecto.bean.MotivoBean;
import com.proyecto.bean.OrdenVentaBean;
import com.proyecto.bean.OrdenVentaDetalleBean;
import com.proyecto.bean.PagoBean;
import com.proyecto.bean.PagoDetalleBean;
import com.proyecto.bean.PaisBean;
import com.proyecto.bean.PrecioBean;
import com.proyecto.bean.ProvinciaBean;
import com.proyecto.bean.ProyectoBean;
import com.proyecto.bean.ReporteModel;
import com.proyecto.bean.SocioNegocioBean;
import com.proyecto.bean.UnidadMedidaBean;
import com.proyecto.bean.ZonaBean;
import com.proyecto.reportes.ReporteEstadoCuenta;

public class Insert {

	private Context contexto;
	public SQLiteDatabase db;
	
	private DataBaseHelper helper;
	private Delete delete;

	public Insert(Context contexto) {
		this.contexto = contexto;
		
		helper = DataBaseHelper.getHelper(contexto);
		db = helper.getDataBase();
		delete = new Delete(contexto);
		
	}

	
	//Registro de socios de negocio
	public boolean insertSocioNegocio(ArrayList<SocioNegocioBean> lista, String type){
		
		boolean res = false;
		
		if(lista != null){

			if(lista.size()>0){
				res = true;

				if(type.equalsIgnoreCase("A"))
					delete.deleteSocioNegocio(lista);
				else if(type.equalsIgnoreCase("L"))
					delete.deleteSocioNegocioLead(lista);
				else
					delete.deleteSocioNegocioAll(lista);

				db.beginTransaction();
				
				for (SocioNegocioBean socioNegocio : lista) {

					// Registrar socio de negocio->
					ContentValues objetForInsert = new ContentValues();
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_CODIGO), socioNegocio.getCodigo());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TIPO_SOCIO), socioNegocio.getTipoCliente());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TIPO_PERSONA), socioNegocio.getTipoPersona());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TIPO_DOCUMENTO), socioNegocio.getTipoDoc());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_NRO_DOCUMENTO), socioNegocio.getNroDoc());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_NOM_RAZON_SOCIAL), socioNegocio.getNombRazSoc());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_NOM_COMERCIAL), socioNegocio.getNomCom());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_APE_PATERNO), socioNegocio.getApePat());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_APE_MATERNO), socioNegocio.getApeMat());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_PRI_NOMBRE), socioNegocio.getPriNom());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_SEG_NOMBRE), socioNegocio.getSegNom());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TLF_UNO), socioNegocio.getTlf1());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TLF_DOS), socioNegocio.getTlf2());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TLF_MOV), socioNegocio.getTlfMov());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_EMAIL), socioNegocio.getCorreo());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_GRUPO_SN), socioNegocio.getGrupo());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_LISTA_PRE), socioNegocio.getListaPrecio());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_COND_PAGO), socioNegocio.getCondPago());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_INDICADOR), socioNegocio.getIndicador());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_ZONA), socioNegocio.getZona());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_CREAD_MOVIL), socioNegocio.getCreadoMovil());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_CLAVE_MOVIL), socioNegocio.getClaveMovil());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_ESTADO_REGISTRO_MOVIL), contexto.getResources().getString(R.string.FROM_SERVICE));
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_DIRECCION_FISCAL), socioNegocio.getDireccionFiscal());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TRANSACCION_MOVIL), socioNegocio.getTransaccionMovil());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_VALIDO_EN_PEDIDO), socioNegocio.getValidoenPedido());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_POSEE_ACTIVOS), socioNegocio.getPoseeActivos());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_PROYECTO), socioNegocio.getCodProyecto());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TIPO_REGISTRO), socioNegocio.getTipoRegistro());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_NUM_ULT_COMPRA), socioNegocio.getNumUltimaCompra());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_FEC_ULT_COMPRA), socioNegocio.getFecUtimaCompra());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_MON_ULT_COMPRA), socioNegocio.getMontoUltCompra());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_PERSONA_CONTACTO), socioNegocio.getPersonaContacto());
					objetForInsert.put(contexto.getResources().getString(R.string.C_SN_SALDO_CUENTA), socioNegocio.getSaldoCuenta());

					long respuestaInsert = db.insert(contexto.getResources().getString(R.string.T_SOCIO_NEGOCIO), 
														null, objetForInsert);

					if (respuestaInsert != -1) {

						if(socioNegocio.getContactos() != null){
							// Registrar contactos->
							for (ContactoBean contacto : socioNegocio
									.getContactos()) {

								if (!contacto.getIdCon().equalsIgnoreCase(
										"anytype{}")) {
									db.execSQL(
											"insert into "
														+contexto.getResources().getString(R.string.TD_CONTACTO_SOCIO_NEGOCIO)+
											" values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
											new Object[] {
													socioNegocio.getCodigo(),
													contacto.getIdCon(),
													contacto.getNomCon(),
													contacto.getPrimerNombre(),
													contacto.getSegNomCon(),
													contacto.getApeCon(),
													contacto.getDireccion(),
													contacto.getEmailCon(),
													contacto.getTel1Con(),
													contacto.getTel2Con(),
													contacto.getTelMovCon(),
													contacto.getPosicion(),
													"0"});

								}

							}
							//->
						}
						
						if(socioNegocio.getDirecciones() != null){
							// Registrar direcciones->
							for (DireccionBean direccion : socioNegocio
									.getDirecciones()) {

								if (!direccion.getIDDireccion().equalsIgnoreCase(
										"anytype{}")) {

									db.execSQL(
											"insert into "
													+contexto.getResources().getString(R.string.TD_DIRECCION_SOCIO_NEGOCIO)+
											" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
											new Object[] {
													socioNegocio.getCodigo(),
													direccion.getIDDireccion(),
													direccion.getPais(),
													direccion.getDepartamento(),
													direccion.getProvincia(),
													direccion.getDistrito(),
													direccion.getCalle(),
													direccion.getReferencia(), 
													direccion.getTipoDireccion(),
													"0",
													direccion.getLatitud(),
													direccion.getLongitud(),
											direccion.getVisitaLunes(),
											direccion.getVisitaMartes(),
											direccion.getVisitaMiercoles(),
											direccion.getVisitaJueves(),
											direccion.getVisitaViernes(),
											direccion.getVisitaSabado(),
											direccion.getVisitaDomingo(),
											direccion.getFrecuenciaVisita(),
											direccion.getRuta(),
											direccion.getZona(),
													direccion.getCanal(),
													direccion.getGiro(),
											direccion.getFechaInicioVisitas(),
											direccion.getVendedor()});

								}

							}
							//->
						}
						
						res = true;
					}else
						res = false;

				}

				db.setTransactionSuccessful();
				db.endTransaction();

			}
			
		}
		
		return res;
		
	}
	
	
	
	//Registro de socios de negocio (UNO)
	public boolean insertSocioNegocio(SocioNegocioBean socioNegocio){
		
		boolean res = false;
		
		if(socioNegocio.getClaveMovil().equals("")){
			Cursor data= db.rawQuery("select count(*) from TB_SOCIO_NEGOCIO " +
					"where ClaveMovil = '"+socioNegocio.getClaveMovil()+"'" , null); 

			while (data.moveToNext()) {
				if(data.getInt(0)>0){
					data.close();
					return res;
				}
			}
			data.close();
		}
		
		
		Cursor data2= db.rawQuery("select count(*) from TB_SOCIO_NEGOCIO " +
				"where Codigo = '"+socioNegocio.getCodigo()+"'" , null); 

		while (data2.moveToNext()) {
			if(!data2.getString(0).equals("0")){
				data2.close();
				return res;
			}
		}
	
	
		// Registrar socio de negocio->
		ContentValues objetForInsert = new ContentValues();
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_CODIGO), socioNegocio.getCodigo());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TIPO_SOCIO), socioNegocio.getTipoCliente());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TIPO_PERSONA), socioNegocio.getTipoPersona());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TIPO_DOCUMENTO), socioNegocio.getTipoDoc());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_NRO_DOCUMENTO), socioNegocio.getNroDoc());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_NOM_RAZON_SOCIAL), socioNegocio.getNombRazSoc());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_NOM_COMERCIAL), socioNegocio.getNomCom());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_APE_PATERNO), socioNegocio.getApePat());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_APE_MATERNO), socioNegocio.getApeMat());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_PRI_NOMBRE), socioNegocio.getPriNom());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_SEG_NOMBRE), socioNegocio.getSegNom());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TLF_UNO), socioNegocio.getTlf1());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TLF_DOS), socioNegocio.getTlf2());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TLF_MOV), socioNegocio.getTlfMov());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_EMAIL), socioNegocio.getCorreo());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_GRUPO_SN), socioNegocio.getGrupo());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_LISTA_PRE), socioNegocio.getListaPrecio());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_COND_PAGO), socioNegocio.getCondPago());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_INDICADOR), socioNegocio.getIndicador());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_ZONA), socioNegocio.getZona());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_CREAD_MOVIL), socioNegocio.getCreadoMovil());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_CLAVE_MOVIL), socioNegocio.getClaveMovil());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_ESTADO_REGISTRO_MOVIL), socioNegocio.getEstadoRegistroMovil());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_DIRECCION_FISCAL), socioNegocio.getDireccionFiscal());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TRANSACCION_MOVIL), socioNegocio.getTransaccionMovil());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_VALIDO_EN_PEDIDO), socioNegocio.getValidoenPedido());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_POSEE_ACTIVOS), socioNegocio.getPoseeActivos());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_CODIGO_EMPLEADO), socioNegocio.getEmpleadoVentas());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_PROYECTO), socioNegocio.getCodProyecto());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TIPO_REGISTRO), socioNegocio.getTipoRegistro());

		long respuestaInsert = db.insert(contexto.getResources().getString(R.string.T_SOCIO_NEGOCIO), 
											null, objetForInsert);

		if (respuestaInsert != -1) {

			if(socioNegocio.getContactos() != null){
				// Registrar contactos->
				for (ContactoBean contacto : socioNegocio
						.getContactos()) {

					if (!contacto.getIdCon().equalsIgnoreCase(
							"anytype{}")) {
						db.execSQL(
								"insert into "
											+contexto.getResources().getString(R.string.TD_CONTACTO_SOCIO_NEGOCIO)+
								" values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
								new Object[] {
										socioNegocio.getCodigo(),
										contacto.getUtilId(),
										contacto.getNomCon(),
										contacto.getPrimerNombre(),
										contacto.getSegNomCon(),
										contacto.getApeCon(),
										contacto.getDireccion(),
										contacto.getEmailCon(),
										contacto.getTel1Con(),
										contacto.getTel2Con(),
										contacto.getTelMovCon(),
										contacto.getPosicion(),
										contacto.isPrincipal()});

					}

				}
				//->
			}
			
			if(socioNegocio.getDirecciones() != null){
				// Registrar direcciones->
				for (DireccionBean direccion : socioNegocio
						.getDirecciones()) {

						db.execSQL(
								"insert into "
										+contexto.getResources().getString(R.string.TD_DIRECCION_SOCIO_NEGOCIO)+
								" values(?,?,?,?,?,?,?,?,?,?,?,?, " +
										"'N','N','N','N','N','N','N', " +
										"'101',?,?,?,?,'', -1)",
								new Object[] {
										socioNegocio.getCodigo(),
										direccion.getIDDireccion(),
										direccion.getPais(),
										direccion.getDepartamento(),
										direccion.getProvincia(),
										direccion.getDistrito(),
										direccion.getCalle(),
										direccion.getReferencia(), 
										direccion.getTipoDireccion(),
										direccion.isPrincipal(),
										direccion.getLatitud(),
										direccion.getLongitud(),
										direccion.getRuta(),
										direccion.getZona(),
								direccion.getCanal(),
								direccion.getGiro()});


				}
				//->
			}
			
			res = true;
		}else
			res = false;

		data2.close();
		
		return res;
		
	}
	
	
	//Registrar o Actualizar socios de negocio
	public boolean insertOrUpdateSocioNegocio(ArrayList<SocioNegocioBean> lista){
		
		boolean res = false;
		if(lista != null){
			
			if(lista.size() > 0){
				res = true;
				
				Update update = new Update(contexto);
				Delete delete = new Delete(contexto);
//				delete.deleteSocioNegocio();
				
				for (SocioNegocioBean socio : lista) {
					
					socio.setEstadoRegistroMovil(contexto.getResources().getString(R.string.FROM_SERVICE));

					if(!insertSocioNegocio(socio)){
						
						delete.deleteDetalleSocioNegocio(socio.getClaveMovil());
						update.updateSocioNegocio(socio);
					}
					
				}
				
			}
			
		}
		
		return res;
		
	}
	
	
	//Registro de �rdenes de ventas
	public boolean insertOrdenVenta(ArrayList<OrdenVentaBean> lista){
		
		boolean res = false;
		
		if(lista != null){
			delete.deleteOrdenVenta(lista);
			if(lista.size()>0){
				res = true;

				for (OrdenVentaBean ordenVenta : lista) {

					// Registrar orden de venta->
					ContentValues objetForInsert = new ContentValues();
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_TIPO), ordenVenta.getTipoDoc());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_CLAVE), ordenVenta.getClave());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_NUMERO), ordenVenta.getNumero());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_REFERENCIA), ordenVenta.getReferencia());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_SOCIO_NEGOCIO), ordenVenta.getCodSN());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_LISTA_PRE), ordenVenta.getListaPrecio());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_CONTACTO), ordenVenta.getContacto());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_MONEDA), ordenVenta.getMoneda());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_EMP_VENTA), ordenVenta.getEmpVentas());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_COMENTARIO), ordenVenta.getComentario());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_FEC_CONT), ordenVenta.getFecContable());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_FEC_VEN), ordenVenta.getFecVen());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_DIR_FISCAL), ordenVenta.getDirFiscal());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_DIR_ENTREGA), ordenVenta.getDirEntrega());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_COND_PAGO), ordenVenta.getCondPago());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_INDICADOR), ordenVenta.getIndicador());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_SUB_TOTAL), ordenVenta.getSubTotal());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_IMPUESTO), ordenVenta.getImpuesto());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_TOTAL), ordenVenta.getTotal());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_CREAD_MOVIL), ordenVenta.getCreadoMovil());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_CLAVE_MOVIL), ordenVenta.getClaveMovil());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_ESTADO_REGISTRO_MOVIL), contexto.getResources().getString(R.string.FROM_SERVICE));
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_TRANSACCION_MOVIL), ordenVenta.getTransaccionMovil());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_MODO_OFFLINE), ordenVenta.getModoOffLine());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_LATITUD), ordenVenta.getLatitud());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_LONGITUD), ordenVenta.getLongitud());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_HORA_CREACION), ordenVenta.getHoraCreacion());
					objetForInsert.put(contexto.getResources().getString(R.string.C_OV_RANGO_CLIENTE), ordenVenta.getRangoDireccion());

					long respuestaInsert = db.insert(contexto.getResources().getString(R.string.T_ORDEN_VENTA), 
														null, objetForInsert);

					if (respuestaInsert != -1) {

						// Registrar detalles->
						for (OrdenVentaDetalleBean detalle : ordenVenta
								.getDetalles()) {

							if (!detalle.getCodArt().equalsIgnoreCase(
									"anytype{}")) {
								
								db.execSQL(
										"insert into "
													+contexto.getResources().getString(R.string.TD_ORDEN_VENTA_DETALLE)+
										" values(?,?,?,?,?,?,?,?,?,?)",
										new Object[] {
												ordenVenta.getClave(),
												detalle.getCodArt(),
												detalle.getCodUM(),
												detalle.getAlmacen(),
												detalle.getCantidad(),
												detalle.getListaPrecio(),
												detalle.getPrecio(),
												detalle.getDescuento(),
												detalle.getCodImp(),
												detalle.getLinea()});

							}

						}
						//->
						res = true;
					}else
						res = false;

				}
			}
			
		}
		
		return res;
		
	}
	
	
	//Registro de �rden de venta
	public boolean insertOrdenVenta(OrdenVentaBean ordenVenta){
		
		boolean res = false;
		
		Cursor data= db.rawQuery("select count(*) from TB_ORDEN_VENTA " +
				"where ClaveMovil = '"+ordenVenta.getClaveMovil()+"'" , null); 

		while (data.moveToNext()) {
			if(!data.getString(0).equals("0")){
				data.close();
				return res;
			}
		}
		
		Cursor data2= db.rawQuery("select count(*) from TB_ORDEN_VENTA " +
				"where Clave = '"+ordenVenta.getClave()+"'" , null); 

		while (data2.moveToNext()) {
			if(!data2.getString(0).equals("0")){
				data2.close();
				return res;
			}
		}
	
			
		// Registrar orden de venta->
		ContentValues objetForInsert = new ContentValues();
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_TIPO), ordenVenta.getTipoDoc());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_CLAVE), ordenVenta.getClave());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_NUMERO), ordenVenta.getNumero());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_REFERENCIA), ordenVenta.getReferencia());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_SOCIO_NEGOCIO), ordenVenta.getCodSN());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_LISTA_PRE), ordenVenta.getListaPrecio());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_CONTACTO), ordenVenta.getContacto());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_MONEDA), ordenVenta.getMoneda());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_EMP_VENTA), ordenVenta.getEmpVentas());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_COMENTARIO), ordenVenta.getComentario());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_FEC_CONT), ordenVenta.getFecContable());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_FEC_VEN), ordenVenta.getFecVen());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_DIR_FISCAL), ordenVenta.getDirFiscal());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_DIR_ENTREGA), ordenVenta.getDirEntrega());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_COND_PAGO), ordenVenta.getCondPago());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_INDICADOR), ordenVenta.getIndicador());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_SUB_TOTAL), ordenVenta.getSubTotal());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_IMPUESTO), ordenVenta.getImpuesto());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_TOTAL), ordenVenta.getTotal());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_CREAD_MOVIL), ordenVenta.getCreadoMovil());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_CLAVE_MOVIL), ordenVenta.getClaveMovil());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_ESTADO_REGISTRO_MOVIL), ordenVenta.getEstadoRegistroMovil());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_TRANSACCION_MOVIL), ordenVenta.getTransaccionMovil());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_HORA_CREACION), ordenVenta.getHoraCreacion());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_MODO_OFFLINE), ordenVenta.getModoOffLine());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_LATITUD), ordenVenta.getLatitud());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_LONGITUD), ordenVenta.getLongitud());
		objetForInsert.put(contexto.getResources().getString(R.string.C_OV_RANGO_CLIENTE), ordenVenta.getRangoDireccion());


		long respuestaInsert = db.insert(contexto.getResources().getString(R.string.T_ORDEN_VENTA), 
											null, objetForInsert);

		if (respuestaInsert != -1) {

			// Registrar detalles->
			for (OrdenVentaDetalleBean detalle : ordenVenta
					.getDetalles()) {

				if (!detalle.getCodArt().equalsIgnoreCase(
						"anytype{}")) {
					
					db.execSQL(
							"insert into "
										+contexto.getResources().getString(R.string.TD_ORDEN_VENTA_DETALLE)+
							" values(?,?,?,?,?,?,?,?,?,?)",
							new Object[] {
									ordenVenta.getClave(),
									detalle.getCodArt(),
									detalle.getCodUM(),
									detalle.getAlmacen(),
									detalle.getCantidad(),
									detalle.getListaPrecio(),
									detalle.getPrecio(),
									detalle.getDescuento(),
									detalle.getCodImp(),
									detalle.getLinea()});

				}

			}
			//->
			res = true;
		}else
			res = false;

		return res;
		
	}
	
	
	//Registrar o Actualizar orden de venta
	public boolean insertOrUpdateOrdenVenta(ArrayList<OrdenVentaBean> lista){
		
		boolean res = false;
		if(lista != null){
			
			if(lista.size()>0){
				res = true;
				
				Update update = null;
				Delete delete = null;
				
				for (OrdenVentaBean venta : lista) {
					
					venta.setEstadoRegistroMovil(contexto.getResources().getString(R.string.FROM_SERVICE));

					if(!insertOrdenVenta(venta)){
						update = new Update(contexto);
						delete = new Delete(contexto);
						
						delete.deleteDetalleOrdenVenta(venta.getClave(),venta.getClaveMovil());
						delete.close();
						
						update.updateOrdenVentaServicio(venta);
						update.close();
					}
					
				}
			}
			
		}
		return res;
	}
	
	
	//Registro de almacenes
	public boolean insertAlmacen(List<AlmacenBean> lista){
		
		boolean res = false;
		
		if(lista != null){
			
			if(lista.size() > 0){
				res = true;
				
				for (AlmacenBean almacenBean : lista) {

					db.execSQL(
							"INSERT or IGNORE into "
									+contexto.getResources().getString(R.string.T_ALMACEN)+
							" values(?,?)",
							new Object[] { almacenBean.getCodigo(),
										   almacenBean.getDescripcion()});
					

				}
			}
			
		}
		
		return res;
	}
	
	
	//Registro de articulos
	public boolean insertArticulo(List<ArticuloBean> lista){
		
		boolean res = false;
		
		if(lista != null){
			
			if(lista.size()>0){
				res = true;
				db.beginTransaction();
				for (ArticuloBean bean : lista) {

					db.execSQL(
							"INSERT or IGNORE into "
									+contexto.getResources().getString(R.string.T_ARTICULO)+
							" values(?,?,?,?,?,?,?)",
							new Object[] { 	bean.getCod(),
											bean.getDesc(),
											bean.getFabricante(),
											bean.getGrupoArticulo(),
											bean.getCodUM(),
											bean.getUnidadMedidaVenta(),
											bean.getAlmacenDefecto()});
					
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		
		return res;
	}
	
	
	//Registro de cantidades
	public boolean insertCantidad(List<CantidadBean> lista){
		
		boolean res = false;
		if(lista != null){
			
			if(lista.size() > 0){
				res = true;
				delete.deleteCantidad();
				db.beginTransaction();
				for (CantidadBean bean : lista) {

					db.execSQL(
							"INSERT or IGNORE into "
									+contexto.getResources().getString(R.string.T_CANTIDAD)+
							" values(?,?,?,?,?,?)",
							new Object[] { 	bean.getAlmacen(),
											bean.getArticulo(),
											bean.getStock(),
											bean.getComprometido(),
											bean.getSolicitado(),
											bean.getDisponible()});
					
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		return res;
		
	}
	
	
	//Registro de condiciones de pago
	public boolean insertCondicionPago(List<CondicionPagoBean> lista){
		
		boolean res = false;
		
		if(lista != null){
			
			if(lista.size()>0){
				res = true;
				db.beginTransaction();
				for (CondicionPagoBean bean : lista) {

					db.execSQL(
							"INSERT or IGNORE into "
									+contexto.getResources().getString(R.string.T_COND_PAGO)+
							" values(?,?)",
							new Object[] { 	bean.getNumeroCondicion(),
											bean.getDescripcionCondicion()});
					
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		
		return res;
		
	}
	
	
	//Registro de fabricantes
	public boolean insertFabricante(List<FabricanteBean> lista){
		
		boolean res = false;
		if(lista != null){
			
			if(lista.size() >0){
				res = true;
				db.beginTransaction();
				for (FabricanteBean bean : lista) {

					db.execSQL(
							"INSERT or IGNORE into "
									+contexto.getResources().getString(R.string.T_FABRICANTE)+
							" values(?,?)",
							new Object[] { 	bean.getCodigo(),
											bean.getNombre()});
					
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		
		return res;
		
	}

	//Registro de motivos incidencia
	public boolean insertMotivo(List<MotivoBean> lista){

		boolean res = false;
		if(lista != null){

			db.execSQL("DELETE FROM TB_MOTIVO");

			String tabla = "TB_MOTIVO";
			if(lista.size() >0){
				res = true;
				db.beginTransaction();
				for (MotivoBean bean : lista) {

					db.execSQL(
							"INSERT or IGNORE into " +tabla+ " values(?,?,?,?,?)",
							new Object[] { 	bean.getId(),
									bean.getDescripcion(),
									bean.getValOrden(),
									bean.getValEntrega(),
									bean.getValFactura()});

				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}

		}

		return res;
	}

	//Registro de proyectos
	public boolean insertProyectos(List<ProyectoBean> lista){

		boolean res = false;
		if(lista != null){

			String tabla = "TB_PROYECTO";
			if(lista.size() >0){
				res = true;
				db.beginTransaction();
				for (ProyectoBean bean : lista) {

					db.execSQL(
							"INSERT or IGNORE into " +tabla+ " values(?,?)",
							new Object[] { 	bean.getCodigo(),
									bean.getDescripcion()});
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
		}

		return res;
	}

	//Registro de canales
	public boolean insertCanales(List<CanalBean> lista){

		boolean res = false;
		if(lista != null){

			String tabla = "TB_CANAL";
			if(lista.size() >0){
				res = true;
				db.beginTransaction();
				for (CanalBean bean : lista) {

					db.execSQL(
							"INSERT or IGNORE into " +tabla+ " values(?,?)",
							new Object[] { 	bean.getCodigo(),
									bean.getDescripcion()});
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
		}

		return res;
	}

	//Registro de giros
	public boolean insertGiros(List<GiroBean> lista){

		boolean res = false;
		if(lista != null){

			String tabla = "TB_GIRO";
			if(lista.size() >0){
				res = true;
				db.beginTransaction();
				for (GiroBean bean : lista) {

					db.execSQL(
							"INSERT or IGNORE into " +tabla+ " values(?,?)",
							new Object[] { 	bean.getCodigo(),
									bean.getDescripcion()});
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
		}

		return res;
	}
	
	//Registro de facturas
	public boolean insertFacturas(ArrayList<FacturaBean> lista){
		
		boolean res = false;
		
		if(lista != null){

			delete.deleteFacturas();

			if(lista.size()>0){
				res = true;

				db.beginTransaction();
				for (FacturaBean factura : lista) {

					// Registrar orden de venta->
					ContentValues objetForInsert = new ContentValues();
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_TIPO), factura.getTipo());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_CLAVE), factura.getClave());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_NUMERO), factura.getNumero());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_REFERENCIA), factura.getReferencia());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_SOCIO_NEGOCIO), factura.getSocioNegocio());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_LISTA_PRECIO), factura.getListaPrecio());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_CONTACTO), factura.getContacto());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_MONEDA), factura.getMoneda());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_EMP_VENTA), factura.getEmpleadoVenta());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_COMENT), factura.getComentario());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_FEC_CONT), factura.getFechaContable());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_FEC_VEN), factura.getFechaVencimiento());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_DIR_FISCAL), factura.getDireccionFiscal());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_DIR_ENTREGA), factura.getDireccionEntrega());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_COND_PAGO), factura.getCondicionPago());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_INDICADOR), factura.getIndicador());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_SUB_TOTAL), factura.getSubTotal());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_DESCUENTO), factura.getDescuento());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_IMPUESTO), factura.getImpuesto());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_TOTAL), factura.getTotal());
					objetForInsert.put(contexto.getResources().getString(R.string.C_FACT_SALDO), factura.getSaldo());


					long respuestaInsert = db.insert(contexto.getResources().getString(R.string.T_FACTURA), 
														null, objetForInsert);

					if (respuestaInsert != -1) {
						
						if(factura.getLineas() != null){
							// Registrar detalles->
							for (FacturaDetalleBean detalle : factura
									.getLineas()) {

								if (!detalle.getArticulo().equalsIgnoreCase(
										"anytype{}")) {
									db.execSQL(
											"insert into "
														+contexto.getResources().getString(R.string.TD_FACTURA1)+
											" values(?,?,?,?,?,?,?,?,?,?,?)",
											new Object[] {
													factura.getClave(),
													detalle.getLinea(),
													detalle.getArticulo(),
													detalle.getUnidadMedida(),
													detalle.getAlmacen(),
													detalle.getCantidad(),
													detalle.getDiponible(),
													detalle.getListaPrecio(),
													detalle.getPrecioUnitario(),
													detalle.getPorcentajeDescuento(),
													detalle.getImpuesto()});

									if(detalle.getLotes() != null &&
											detalle.getLotes().size() > 0){
										for (FacturaDetalleLoteBean lote: detalle.getLotes()) {
											db.execSQL(
													"insert into "
															+contexto.getResources().getString(R.string.TD_FACTURA2)+
															" values(?,?,?,?)",
													new Object[] {
															factura.getClave(),
															lote.getLote(),
															lote.getCantidad(),
															detalle.getLinea()});
										}
									}

								}

							}
							//->
						}

						res = true;
					}else
						res = false;

				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		
		return res;
		
	}
		
	
	//Registro de grupos de articulo
	public boolean insertGruposArticulo(List<GrupoArticuloBean> lista){
		
		boolean res = false;
		if(lista != null){
			
			if(lista.size()>0){
				res = true;
				db.beginTransaction();
				for (GrupoArticuloBean bean : lista) {

					db.execSQL(
							"INSERT or IGNORE into "
									+contexto.getResources().getString(R.string.T_GRUPO_ART)+
							" values(?,?)",
							new Object[] { 	bean.getCodigo(),
											bean.getNombre()});
					
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		
		return res;
		
	}
		
	
	//Registro de grupos de socio de negocio
	public boolean insertGruposSocioNegocio(List<GrupoSocioNegocioBean> lista){
		
		boolean res = false;
		if(lista != null){
			
			if(lista.size() > 0){
				res = true;
				db.beginTransaction();
				for (GrupoSocioNegocioBean bean : lista) {

					db.execSQL(
							"INSERT or IGNORE into "
									+contexto.getResources().getString(R.string.T_GRUPO_SN)+
							" values(?,?)",
							new Object[] { 	bean.getGroupCode(),
											bean.getGroupName()});
					
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		
		return res;
		
	}
	
	
	//Registro de grupos de unidad de medida
	public boolean insertGruposUnidadMedida(List<GrupoUnidadMedidaBean> lista){
		
		boolean res = false;
		
		if(lista != null){
			
			if(lista.size() > 0){
				
				delete.deleteGrupoUnidadMedida();
				db.beginTransaction();
				for (GrupoUnidadMedidaBean bean : lista) {

					// Registrar GRUPO UNIDAD DE MEDIDA->
					ContentValues objetForInsert = new ContentValues();
					objetForInsert.put(contexto.getResources().getString(R.string.C_GRUPO_UM_COD), bean.getCodigo());
					objetForInsert.put(contexto.getResources().getString(R.string.C_GRUPO_UM_NOM), bean.getNombre());

					long respuestaInsert = db.insert(contexto.getResources().getString(R.string.T_GRUPO_UM), 
														null, objetForInsert);

					if (respuestaInsert != -1) {

						if(bean.getUnidadMedida() != null){
							// Registrar detalles->
							for (UnidadMedidaBean detalle : bean
									.getUnidadMedida()) {

								if (!detalle.getCodigo().equalsIgnoreCase(
										"anytype{}")) {
									db.execSQL(
											"INSERT into "
														+contexto.getResources().getString(R.string.TD_GRUPO1_UM)+
											" values(?,?)",
											new Object[] {
													bean.getCodigo(),
													detalle.getCodigo()});

								}

							}
							//->
						}
						
						res = true;
					}else
						res = false;

				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		
		return res;
		
	}
		
	
	//Registro de impuesto
	public boolean insertImpuesto(List<ImpuestoBean> lista){
		
		boolean res = false;
		if(lista != null){
			
			if(lista.size() > 0){
				res = true;
				for (ImpuestoBean bean : lista) {

					db.execSQL(
							"INSERT or IGNORE into "
									+contexto.getResources().getString(R.string.T_IMPUESTO)+
							" values(?,?,?)",
							new Object[] { 	bean.getCodigo(),
											bean.getNombre(),
											bean.getTasa()});
					
				}
			}
			
		}
		return res;
	}
	
	
	//Registro de indicadores
	public boolean insertIndicador(List<IndicadorBean> lista){
		
		boolean res = false;
		if(lista != null){
			
			if(lista.size() > 0){
				res = true;
				db.beginTransaction();
				for (IndicadorBean bean : lista) {
	
					db.execSQL(
							"INSERT or IGNORE into "
									+contexto.getResources().getString(R.string.T_INDICADOR)+
							" values(?,?)",
							new Object[] { 	bean.getCodigo(),
											bean.getNombre()});
					
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		
		return res;
		
	}

	
	//Registro de listas de precios
	public boolean insertListaPrecio(List<ListaPrecioBean> lista){
		
		boolean res = false;
		
		if(lista != null){
			
			if(lista.size() > 0){
				delete.deleteListaPrecios();
				db.beginTransaction();
				for (ListaPrecioBean bean : lista) {
	
					// Registrar lista de precio->
					ContentValues objetForInsert = new ContentValues();
					objetForInsert.put(contexto.getResources().getString(R.string.C_LST_PRE_COD), bean.getCodigo());
					objetForInsert.put(contexto.getResources().getString(R.string.C_LST_PRE_NOM), bean.getNombre());
	
					long respuestaInsert = db.insert(contexto.getResources().getString(R.string.T_LISTA_PRECIO), 
														null, objetForInsert);
	
					if (respuestaInsert != -1) {
						res = true;
					}else
						res = false;
	
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		
		return res;
		
	}
	
	
	//Registro de listas de precios
	public boolean insertPrecios(List<PrecioBean> lista){
		
		boolean res = false;
		
		if(lista != null){
			
			if(lista.size()>0){
				delete.deletePrecios();
				db.beginTransaction();
				for (PrecioBean bean : lista) {

					// Registrar lista de precio->
					ContentValues objetForInsert = new ContentValues();
					objetForInsert.put(contexto.getResources().getString(R.string.C_PRE_COD_LISTA), bean.getListaPrecio());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PRE_ART), bean.getArticulo());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PRE_MONEDA), bean.getMoneda());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PRE_PRECIO_VENTA), bean.getPrecioVenta());

					long respuestaInsert = db.insert(contexto.getResources().getString(R.string.T_PRECIO), 
														null, objetForInsert);

					if (respuestaInsert != -1) {
						res = true;
					}else
						res = false;

				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		
		
		return res;
		
	}
	
		
	
	//Registro de monedas
	public boolean insertMoneda(List<MonedaBean> lista){
		
		boolean res = false;
		if(lista != null){
			
			if(lista.size() > 0){
				res = true;
				db.beginTransaction();
				for (MonedaBean bean : lista) {
	
					db.execSQL(
							"INSERT or IGNORE into "
									+contexto.getResources().getString(R.string.T_MONEDA)+
							" values(?,?)",
							new Object[] { 	bean.getCodigo(),
											bean.getDescripcion()});
					
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		
		return res;
		
	}

	
	//Registro de estados de cuenta de socio
		public boolean insertEstadoCuentaCliente(ArrayList<ReporteEstadoCuenta> lista){
			
			boolean res = false;
			
			if(lista != null){

				delete.deleteRegistroEstadoCuenta();
				
				if(lista.size()>0){
					res = true;
					db.beginTransaction();
					for (ReporteEstadoCuenta pago : lista) {

						ContentValues objetForInsert = new ContentValues();
						objetForInsert.put(contexto.getResources().getString(R.string.TipoReporte), pago.getTipoReporte());
						objetForInsert.put(contexto.getResources().getString(R.string.Cliente), pago.getCliente());
						objetForInsert.put(contexto.getResources().getString(R.string.Nombre), pago.getNombre());
						objetForInsert.put(contexto.getResources().getString(R.string.ListaPrecio), pago.getListaPrecio());
						objetForInsert.put(contexto.getResources().getString(R.string.LineaCredito), pago.getLineaCredito());
						objetForInsert.put(contexto.getResources().getString(R.string.CondicionPago), pago.getCondicionPago());
						objetForInsert.put(contexto.getResources().getString(R.string.Clave), pago.getClave());
						objetForInsert.put(contexto.getResources().getString(R.string.Sunat), pago.getSunat());
						objetForInsert.put(contexto.getResources().getString(R.string.Condicion), pago.getCondicion());
						objetForInsert.put(contexto.getResources().getString(R.string.Vendedor), pago.getVendedor());
						objetForInsert.put(contexto.getResources().getString(R.string.Emision), pago.getEmision());
						objetForInsert.put(contexto.getResources().getString(R.string.Moneda), pago.getMoneda());
						objetForInsert.put(contexto.getResources().getString(R.string.Total), pago.getTotal());
						objetForInsert.put(contexto.getResources().getString(R.string.Saldo), pago.getSaldo());
						objetForInsert.put(contexto.getResources().getString(R.string.Pago_Fecha), pago.getPago_Fecha());
						objetForInsert.put(contexto.getResources().getString(R.string.Pago_Dias), pago.getPago_Dias());
						objetForInsert.put(contexto.getResources().getString(R.string.Pago_Moneda), pago.getPago_Moneda());
						objetForInsert.put(contexto.getResources().getString(R.string.Pagado_Importe), pago.getPagado_Importe());

						long respuestaInsert = db.insert(contexto.getResources().getString(R.string.T_ESTADO_CUENTA_SOCIO), 
															null, objetForInsert);

						if (respuestaInsert != -1) {
							res = true;
						}
					}
					db.setTransactionSuccessful();
					db.endTransaction();
				}
				
			}
			
			return res;
			
		}


	//Registro de ntas de credito para reporte de saldos x Vendedor
	public boolean insertNotaCredito(ArrayList<ReporteModel> lista){

		boolean res = false;

		if(lista != null){

			delete.deleteRegistroNC();

			if(lista.size()>0){
				res = true;
				db.beginTransaction();
				for (ReporteModel nc : lista) {

					ContentValues objetForInsert = new ContentValues();
					objetForInsert.put("Clave", nc.getClave());
					objetForInsert.put("Sunat", nc.getSunat());
					objetForInsert.put("Emision", nc.getEmision());
					objetForInsert.put("Dias", nc.getDias());
					objetForInsert.put("Ruc", nc.getRuc());
					objetForInsert.put("Nombre", nc.getNombre());
					objetForInsert.put("Direccion", nc.getDireccion());
					objetForInsert.put("Total", nc.getTotal());
					objetForInsert.put("Pagado", nc.getPagado());
					objetForInsert.put("Saldo", nc.getSaldo());

					long respuestaInsert = db.insert("TB_REPORTE_MODEL",
							null, objetForInsert);

					if (respuestaInsert != -1) {
						res = true;
					}
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}

		}

		return res;

	}

	
	//Registro de pagos
	public boolean insertPagoCliente(ArrayList<PagoBean> lista){
		
		boolean res = false;
		
		if(lista != null){

			delete.deletePagos(lista);

			if(lista.size()>0){
				res = true;

				for (PagoBean pago : lista) {

					// Registrar opago->
					ContentValues objetForInsert = new ContentValues();
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_TIPO), pago.getTipo());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CLAVE), pago.getClave());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_NUMERO), pago.getNumero());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_SOCIO_NEGOCIO), pago.getSocioNegocio());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_EMP_VENTA), pago.getEmpleadoVenta());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_COMENTARIO), pago.getComentario());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_GLOSA), pago.getGlosa());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_FEC_CONTABLE), pago.getFechaContable());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_MONEDA), pago.getMoneda());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_TIPO_PAGO), pago.getTipoPago());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_TRANSF_CUENTA), pago.getTransferenciaCuenta());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_TRANSF_REF), pago.getTransferenciaReferencia());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_TRANSF_IMPORTE), pago.getTransferenciaImporte());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_EFECTIVO_CUENTA), pago.getEfectivoCuenta());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_EFECTIVO_IMPORTE), pago.getEfectivoImporte());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CREADO_MOVIL), pago.getCreadoMovil());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CLAVE_MOVIL), pago.getClaveMovil());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_ESTADO_REGISTRO_MOVIL), 
											contexto.getResources().getString(R.string.FROM_SERVICE));

					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CHEQUE_CUENTA), pago.getChequeCuenta());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CHEQUE_BANCO), pago.getChequeBanco());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CHEQUE_VENCIMIENTO), pago.getChequeVencimiento());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CHEQUE_IMPORTE), pago.getChequeImporte());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CHEQUE_NUMERO), pago.getChequeNumero());
					objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_TRANSACCION_MOVIL), pago.getTransaccionMovil());
					

					long respuestaInsert = db.insert(contexto.getResources().getString(R.string.T_PAGO), 
														null, objetForInsert);

					if (respuestaInsert != -1) {

						// Registrar detalles->
						for (PagoDetalleBean detalle : pago
								.getLineas()) {

							if (!detalle.getFacturaCliente().equalsIgnoreCase(
									"anytype{}")) {
								db.execSQL(
										"insert into "
													+contexto.getResources().getString(R.string.TD_PAGO1)+
										" values(?,?,?)",
										new Object[] {
												pago.getClave(),
												detalle.getFacturaCliente(),
												detalle.getImporte()});

							}

						}
						//->
						res = true;
					}else
						res = false;

				}
			}
			
		}
		
		return res;
		
	}
	
	
	//Registro de pago (UNO)
	public boolean insertPagoCliente(PagoBean pago){
		
		boolean res = false;
		
		Cursor data= db.rawQuery("select count(*) from TB_PAGO " +
				"where ClaveMovil = '"+pago.getClaveMovil()+"'" , null); 

		while (data.moveToNext()) {
			if(!data.getString(0).equals("0")){
				data.close();
				return res;
			}
		}
		
		Cursor data2= db.rawQuery("select count(*) from TB_PAGO " +
				"where Clave = '"+pago.getClave()+"'" , null); 

		while (data2.moveToNext()) {
			if(!data2.getString(0).equals("0")){
				data2.close();
				return res;
			}
		}
	
			// Registrar opago->
			ContentValues objetForInsert = new ContentValues();
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_TIPO), pago.getTipo());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CLAVE), pago.getClave());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_NUMERO), pago.getNumero());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_SOCIO_NEGOCIO), pago.getSocioNegocio());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_EMP_VENTA), pago.getEmpleadoVenta());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_COMENTARIO), pago.getComentario());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_GLOSA), pago.getGlosa());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_FEC_CONTABLE), pago.getFechaContable());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_MONEDA), pago.getMoneda());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_TIPO_PAGO), pago.getTipoPago());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_TRANSF_CUENTA), pago.getTransferenciaCuenta());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_TRANSF_REF), pago.getTransferenciaReferencia());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_TRANSF_IMPORTE), pago.getTransferenciaImporte());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_EFECTIVO_CUENTA), pago.getEfectivoCuenta());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_EFECTIVO_IMPORTE), pago.getEfectivoImporte());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CREADO_MOVIL), pago.getCreadoMovil());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CLAVE_MOVIL), pago.getClaveMovil());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_ESTADO_REGISTRO_MOVIL),pago.getEstadoRegistroMovil());

			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CHEQUE_CUENTA), pago.getChequeCuenta());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CHEQUE_BANCO), pago.getChequeBanco());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CHEQUE_VENCIMIENTO), pago.getChequeVencimiento());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CHEQUE_IMPORTE), pago.getChequeImporte());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_CHEQUE_NUMERO), pago.getChequeNumero());
			objetForInsert.put(contexto.getResources().getString(R.string.C_PAGO_TRANSACCION_MOVIL), pago.getTransaccionMovil());
			
			
			long respuestaInsert = db.insert(contexto.getResources().getString(R.string.T_PAGO), 
												null, objetForInsert);

			if (respuestaInsert != -1) {

				// Registrar detalles->
				for (PagoDetalleBean detalle : pago
						.getLineas()) {

					if (!detalle.getFacturaCliente().equalsIgnoreCase(
							"anytype{}")) {
						db.execSQL(
								"insert into "
											+contexto.getResources().getString(R.string.TD_PAGO1)+
								" values(?,?,?)",
								new Object[] {
										pago.getClave(),
										detalle.getFacturaCliente(),
										detalle.getImporte()});

					}

				}
				//->
				res = true;
			}else
				res = false;

		return res;
		
	}
	
	
	//Registrar o Actualizar pagos
	public boolean insertOrUpdatePagoCliente(ArrayList<PagoBean> lista){
		
		boolean res = false;
		if(lista != null){
			
			if(lista.size()>0){
				res = true;
				
				Update update = null;
				Delete delete = null;
				
				for (PagoBean pago : lista) {
					
					pago.setEstadoRegistroMovil(contexto.getResources().getString(R.string.FROM_SERVICE));

					if(!insertPagoCliente(pago)){
						update = new Update(contexto);
						delete = new Delete(contexto);
						
						delete.deleteDetallePagos(pago.getClave(),pago.getClaveMovil());
						delete.close();
						
						update.updatePagoClienteServicio(pago);
						update.close();
					}
					
				}
				
			}
			
		}
		
		return res;
		
	}
			
	
	//Registro de pais
	public boolean insertPais(List<PaisBean> lista){
		
		boolean res = false;
		
		if(lista!= null){
			if(lista.size() > 0){
				res = true;
				for (PaisBean pais : lista) {
					db.execSQL(
							"insert or ignore into "
										+contexto.getResources().getString(R.string.T_PAIS)+
							" values(?,?)",
							new Object[] {
											pais.getCodigo(),
											pais.getNombre()});
					
				}
			}
		}
		
		return res;
		
	}
	
	
	//Registro de departamento
	public boolean insertDepartamento(List<DepartamentoBean> lista){

		boolean res = false;
		if(lista != null){
			
			if(lista.size() > 0){
				res = true;
				for (DepartamentoBean departamento : lista) {
					db.execSQL(
							"insert or ignore into "
										+contexto.getResources().getString(R.string.T_DEPARTAMENTO)+
							" values(?,?,?)",
							new Object[] {
									departamento.getPais(),
									departamento.getCodigo(),
									departamento.getNombre()});
	
				}
			}
		
		}
		
		return res;
	}
	
	
	//Registro de provincias
	public boolean insertProvincias(List<ProvinciaBean> lista){

		boolean res = false;
		if(lista != null){
			
			if(lista.size() > 0){
				res = true;
				for (ProvinciaBean provincia : lista) {
					db.execSQL(
							"insert or ignore into "
										+contexto.getResources().getString(R.string.T_PROVINCIA)+
							" values(?,?,?)",
							new Object[] {
									provincia.getDepartamento(),
									provincia.getCodigo(),
									provincia.getNombre()});
			
				}
			}
			
		}

		return res;
	}
	
	
	//Registro de distritos
	public boolean insertDistritos(List<DistritoBean> lista){

		boolean res = false;
		if(lista != null){
			
			if(lista.size() > 0){
				res = true;
				db.beginTransaction();
				for (DistritoBean distrito : lista) {
					db.execSQL(
							"insert or ignore into "
										+contexto.getResources().getString(R.string.T_DISTRITO)+
							" values(?,?,?)",
							new Object[] {
									distrito.getProvincia(),
									distrito.getCodigo(),
									distrito.getNombre()});
	
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		return res;
	}
	
	
	//Registro de calles
	public boolean insertCalles(ArrayList<CalleBean> lista){

		boolean res = false;
		if(lista != null){

			String table = "TB_CALLE";

			if(lista.size() > 0){
				res = true;
				db.beginTransaction();
					for (CalleBean calle :lista) {
						
						db.execSQL("INSERT OR IGNORE INTO " +table+ " (CODIGO_DISTRITO,CODIGO,NOMBRE) VALUES(?,?,?)" ,
								new Object[] {
										calle.getDistrito(),
										calle.getCodigo(),
										calle.getNombre()});
						
					
					}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
		
		}
		return res;
	}
	
		
	
	//Registro de unidades de medida
	public boolean insertUnidadMedida(List<UnidadMedidaBean> lista){
		
		boolean res = false;
		if(lista != null){
			
			if(lista.size() > 0){
				res = true;
				db.beginTransaction();
				for (UnidadMedidaBean bean : lista) {
	
					db.execSQL(
							"INSERT or IGNORE into "
									+contexto.getResources().getString(R.string.T_UNIDAD_MEDIDA)+
							"(Codigo,Nombre) values(?,?)",
							new Object[] {bean.getCodigo(),
										  bean.getNombre()});
					
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		
		return res;
		
	}
	
	
	//Registro de zonas
	public boolean insertZonas(List<ZonaBean> lista){
		
		boolean res = false;
		if(lista != null){
			
			if(lista.size() > 0){
				res = true;
				db.beginTransaction();
				for (ZonaBean bean : lista) {
	
					db.execSQL(
							"INSERT or IGNORE into "
									+contexto.getResources().getString(R.string.T_ZONA)+
							"(Codigo,Nombre) values(?,?)",
							new Object[] { bean.getCodigo(),
											bean.getNombre()});
					
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			
		}
		return res;
	}
	
	
	//Registro de cuentas
	public boolean insertCuentas(List<CuentaBean> lista){
		
		boolean res = false;
		if(lista != null){
			
			if(lista.size() > 0){
				res = true;
				for (CuentaBean bean : lista) {
	
					db.execSQL(
							"INSERT or IGNORE into "
									+contexto.getResources().getString(R.string.T_CUENTA)+
							"(Codigo,Nombre) values(?,?)",
							new Object[] { bean.getCodigo(),
											bean.getNombre()});
					
				}
			}
			
		}
		return res;
		
	}
	
	
	//Registro de bancos
	public boolean insertBancos(List<BancoBean> lista){
		
		boolean res = false;
		if(lista != null){
			
			if(lista.size() > 0){
				res = true;
				for (BancoBean bean : lista) {
	
					db.execSQL(
							"INSERT or IGNORE into "
									+contexto.getResources().getString(R.string.T_BANCO)+
							" values(?,?)",
							new Object[] { bean.getCodigo(),
											bean.getNombre()});
					
				}
			}
			
		}
		return res;
	}
	
	
	//Actualizar Correlativo
	public void updateCorrelativo(String tipoRegistro){
		
			db.execSQL(" UPDATE TB_COR " +
					"SET NUM_COR = NUM_COR + 1 " +
					"WHERE COD_COR = '"+tipoRegistro+"' ");
	
	}
	
	
	//Actualizar Estado del socio de negocio
	public void updateEstadoSocioNegocio(String claveMovil){
		
			db.execSQL("UPDATE TB_SOCIO_NEGOCIO SET EstadoMovil='"
						+ contexto.getResources().getString(R.string.SINCRONIZADO) 
						+"' WHERE ClaveMovil ='"
						+ claveMovil + "'");
	
	}
	
	public void updateEstadoTransaccionSocioNegocio(String claveMovil, String estadoTransaccion){
		
		db.execSQL("UPDATE TB_SOCIO_NEGOCIO SET TransaccionMovil='"
					+ estadoTransaccion
					+"' WHERE ClaveMovil ='"
					+ claveMovil + "'");
	}
	
	
	//Actualizar Estado del socio de negocio
	public void updateEstadoPagoRecibido(String claveMovil){
		
			db.execSQL("UPDATE TB_PAGO SET EstadoMovil='"
						+ contexto.getResources().getString(R.string.SINCRONIZADO) 
						+"' WHERE ClaveMovil ='"
						+ claveMovil + "'");
	
	}
	
	public void updateEstadoTransaccionPagoRecibido(String claveMovil, String estadoTransaccion){
		
		db.execSQL("UPDATE TB_PAGO SET TransaccionMovil='"
					+ estadoTransaccion
					+"' WHERE ClaveMovil ='"
					+ claveMovil + "'");

	}
	
	//Actualizar saldo de factura
	public void updateSaldoFactura(String clave, String saldo){
		
		db.execSQL("UPDATE TB_FACTURA SET Saldo='"
					+ saldo+ "' WHERE Clave ='"
					+ clave + "'");

	}
	
	
	//Actualizar Estado de la orden de venta
	public void updateEstadoOrdenVenta(String claveMovil){
		
			db.execSQL("UPDATE TB_ORDEN_VENTA SET EstadoMovil='"
						+ contexto.getResources().getString(R.string.SINCRONIZADO) 
						+"' WHERE ClaveMovil ='"
						+ claveMovil + "'");
	
	}
	
	//Actualizar Estado de la transaccion
	public void updateEstadoTransaccionOrdenVenta(String claveMovil, int estado){
		
		String estadoTransaccion = "";
		switch (estado) {
		case 1:
			estadoTransaccion =  contexto.getResources().getString(R.string.CREAR_BORRADOR);
			break;
		case 2:
			estadoTransaccion =  contexto.getResources().getString(R.string.ACTUALIZAR_BORRADOR);
			break;
		case 3:
			estadoTransaccion =  contexto.getResources().getString(R.string.RECHAZAR_BORRADOR);
			break;
		case 4:
			estadoTransaccion =  contexto.getResources().getString(R.string.CREAR_TRANSACCION);
			break;

		default:
			break;
		}
		
			db.execSQL("UPDATE TB_ORDEN_VENTA SET TransaccionMovil='"
						+ estadoTransaccion
						+"' WHERE ClaveMovil ='"
						+ claveMovil + "'");
	
	}
	
	
	//Actualizar Estado de la orden de venta
	public void updateEstadoPago(String claveMovil){
		
			db.execSQL("UPDATE TB_PAGO SET EstadoMovil='"
						+ contexto.getResources().getString(R.string.SINCRONIZADO) 
						+"' WHERE ClaveMovil ='"
						+ claveMovil + "'");
	
	}

	
	public void close(){
		
//		db.close();
//		cn.close();
		
	}
	
}
