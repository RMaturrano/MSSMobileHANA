package com.proyecto.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.proyect.movil.R;
import com.proyecto.bean.ContactoBean;
import com.proyecto.bean.DireccionBean;
import com.proyecto.bean.OrdenVentaBean;
import com.proyecto.bean.OrdenVentaDetalleBean;
import com.proyecto.bean.PagoBean;
import com.proyecto.bean.PagoDetalleBean;
import com.proyecto.bean.SocioNegocioBean;

public class Update {
	
	private Context contexto;
	private MyDataBase cn;
	private SQLiteDatabase db;
	
	private DataBaseHelper helper;

	public Update(Context contexto) {
		this.contexto = contexto;
		
//		cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		db = cn.getWritableDatabase();
		
		helper = DataBaseHelper.getHelper(contexto);
		db = helper.getDataBase();
		
	}
	
	
	public boolean updateSocioNegocio(SocioNegocioBean socioNegocio){
		
		boolean res = false;
	
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
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_ESTADO_REGISTRO_MOVIL), 
													socioNegocio.getEstadoRegistroMovil());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_DIRECCION_FISCAL), socioNegocio.getDireccionFiscal());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_TRANSACCION_MOVIL), socioNegocio.getTransaccionMovil());
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_VALIDO_EN_PEDIDO), socioNegocio.getValidoenPedido());
		
		
		long respuestaInsert;
		if(socioNegocio.getClaveMovil().equals("")){
			respuestaInsert = db.update(contexto.getResources().getString(R.string.T_SOCIO_NEGOCIO),
					objetForInsert,
					"Codigo = ?",
					new String[]{socioNegocio.getCodigo()});
		}else{
			respuestaInsert = db.update(contexto.getResources().getString(R.string.T_SOCIO_NEGOCIO),
					objetForInsert,
					"ClaveMovil = ?",
					new String[]{socioNegocio.getClaveMovil()});
		}

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
								" values(?,?,?,?,?,?,?,?,?,?)",
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
										direccion.isPrincipal() });


				}
				//->
			}
			
			res = true;
		}else
			res = false;

		
		return res;
		
	}
	
	
	//Registro de órdenes de ventas
	public boolean updateOrdenVenta(OrdenVentaBean ordenVenta){
		
		boolean res = false;
			
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
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_ESTADO_REGISTRO_MOVIL), 
													ordenVenta.getEstadoRegistroMovil());

		int rowsAffected = db.update("TB_ORDEN_VENTA", 
										objetForInsert, 
										"ClaveMovil = ?", 
										new String[]{ordenVenta.getClaveMovil()});
		if (rowsAffected != 0) {

			Delete delete = new Delete(contexto);
			delete.deleteDetalleOrdenVenta(ordenVenta.getClave());
			delete.close();
			
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
	
	public boolean updateOrdenVentaServicio(OrdenVentaBean ordenVenta){
		
		boolean res = false;
			
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
		objetForInsert.put(contexto.getResources().getString(R.string.C_SN_ESTADO_REGISTRO_MOVIL), 
													ordenVenta.getEstadoRegistroMovil());

		
		int respuestaInsert;
		if(ordenVenta.getClaveMovil().equals("")){
			respuestaInsert = db.update("TB_ORDEN_VENTA",
										objetForInsert,
										"Clave = ?", 
										new String[]{ordenVenta.getClave()});
		}else{
			respuestaInsert = db.update("TB_ORDEN_VENTA", 
										objetForInsert, 
										"ClaveMovil = ?", 
										new String[]{ordenVenta.getClaveMovil()});
		}
		
		if (respuestaInsert != 0) {
			
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
	
	
	public boolean updatePagoCliente(PagoBean pago){
		
		boolean res = false;
	
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
			
			
			
			int rowsAffected = db.update("TB_PAGO", 
					objetForInsert, 
					"ClaveMovil = ?", 
					new String[]{pago.getClaveMovil()});
			

			if (rowsAffected != 0) {
				
				Delete delete = new Delete(contexto);
				delete.deleteDetallePagos(pago.getClave());
				delete.close();

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
	
	
	public boolean updatePagoClienteServicio(PagoBean pago){
		
		boolean res = false;
	
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
			
			
			int rowsAffected;
			if(pago.getClaveMovil().equals("")){
				rowsAffected = db.update("TB_PAGO",
											objetForInsert,
											"Clave = ?", 
											new String[]{pago.getClave()});
			}else{
				rowsAffected = db.update("TB_PAGO", 
											objetForInsert, 
											"ClaveMovil = ?", 
											new String[]{pago.getClaveMovil()});
			}
			

			if (rowsAffected != 0) {

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
	
	public void close(){
		
//		db.close();
//		cn.close();
//		
	}

}
