package com.proyecto.database;

import java.util.ArrayList;

import com.proyect.movil.R;
import com.proyecto.bean.OrdenVentaBean;
import com.proyecto.bean.PagoBean;
import com.proyecto.bean.SocioNegocioBean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Delete {
	
	private Context contexto;
	private SQLiteDatabase db;
	
	private DataBaseHelper helper;
	
	public Delete(Context contexto) {
		this.contexto = contexto;
		
		helper = DataBaseHelper.getHelper(contexto);
		db = helper.getDataBase();
		
	}
	
	
	public void deleteSocioNegocioLead(ArrayList<SocioNegocioBean> lista){
		
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_SOCIO_NEGOCIO) + " where EstadoMovil <> 'L' and TipoSocio = 'L' ");
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_CONTACTO_SOCIO_NEGOCIO)
				 				 + " where CodigoSocioNegocio not in (select Codigo from TB_SOCIO_NEGOCIO where EstadoMovil = 'L' and TipoSocio = 'C')");
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_DIRECCION_SOCIO_NEGOCIO)
								 + " where CodigoSocioNegocio not in (select Codigo from TB_SOCIO_NEGOCIO where EstadoMovil = 'L' and TipoSocio = 'C')");
		
	}


	public void deleteSocioNegocio(ArrayList<SocioNegocioBean> lista){

		db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_SOCIO_NEGOCIO) + " where EstadoMovil <> 'L'");
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_CONTACTO_SOCIO_NEGOCIO)
				+ " where CodigoSocioNegocio not in (select Codigo from TB_SOCIO_NEGOCIO where EstadoMovil = 'L')");
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_DIRECCION_SOCIO_NEGOCIO)
				+ " where CodigoSocioNegocio not in (select Codigo from TB_SOCIO_NEGOCIO where EstadoMovil = 'L')");

		for (SocioNegocioBean socio : lista) {

			if(socio.getClaveMovil() != null){
				if(!socio.getClaveMovil().equals("") && !socio.getClaveMovil().equals("anyType{}")){
					db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_SOCIO_NEGOCIO) + " where ClaveMovil = '"+socio.getClaveMovil()+"' ");
					db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_CONTACTO_SOCIO_NEGOCIO)
							+ " where CodigoSocioNegocio = '"+socio.getCodigo()+"' ");
					db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_DIRECCION_SOCIO_NEGOCIO)
							+ " where CodigoSocioNegocio = '"+socio.getCodigo()+"' ");

				}
			}

		}

	}


	
	public void deleteDetalleSocioNegocio(String claveMovil){
		
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_CONTACTO_SOCIO_NEGOCIO)
				 				 + " where CodigoSocioNegocio = " +
					 				 "(select Codigo from TB_SOCIO_NEGOCIO " +
					 				 "where ClaveMovil = '"+claveMovil+"')");
		
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_DIRECCION_SOCIO_NEGOCIO)
								 + " where CodigoSocioNegocio = " +
									 "(select Codigo from TB_SOCIO_NEGOCIO " +
									 "where ClaveMovil = '"+claveMovil+"')");
		
	}	
	
	public void deleteOrdenVenta(ArrayList<OrdenVentaBean> lista){
		
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_ORDEN_VENTA) + " where EstadoMovil <> 'L'");
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_ORDEN_VENTA_DETALLE)
								 + " where ClaveMovil not in (select Clave from TB_ORDEN_VENTA where EstadoMovil = 'L')");
		
		for (OrdenVentaBean ordenVentaBean : lista) {
			
			if(ordenVentaBean.getClaveMovil() != null){
				if(!ordenVentaBean.getClaveMovil().equals("") && !ordenVentaBean.getClaveMovil().equals("anyType{}")){
					db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_ORDEN_VENTA) + " where ClaveMovil = '"+ordenVentaBean.getClaveMovil()+"' ");
					db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_ORDEN_VENTA_DETALLE)
							 + " where ClaveMovil ='"+ordenVentaBean.getClave()+"'");
				}
			}
			
		}
		
	}
	
	public void deleteDetalleOrdenVenta(String codigoVenta){
		
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_ORDEN_VENTA_DETALLE)
								 + " where ClaveMovil = '"+codigoVenta+"' ");
		
	}
	
	public void deleteDetalleOrdenVenta(String codigoVenta, String claveMovil){
		
		if(!claveMovil.equals("")){
			db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_ORDEN_VENTA_DETALLE)
					 + " where ClaveMovil = (select Clave from TB_ORDEN_VENTA " +
					 							"where ClaveMovil = '"+claveMovil+"' ) ");
		}else{
			db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_ORDEN_VENTA_DETALLE)
					 + " where ClaveMovil = '"+codigoVenta+"' ");
		}
		
	}
	
	public void deleteArticulo(){
		
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_ARTICULO));
		
	}
	
	public void deleteCantidad(){
		
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_CANTIDAD));
		
	}
	
	public void deleteFacturas(){
		
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_FACTURA));
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_FACTURA1));
		
	}
	
	public void deletePagos(ArrayList<PagoBean> lista){
		
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_PAGO) + " where EstadoMovil <> 'L'");
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_PAGO1)
							     + " where ClavePago not in (select Clave from TB_PAGO where EstadoMovil = 'L')");
		
		for (PagoBean pago : lista) {
			
			if(pago.getClaveMovil() != null){
				if(!pago.getClaveMovil().equals("") && !pago.getClaveMovil().equals("anyType{}")){
					db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_PAGO) + " where ClaveMovil = '"+pago.getClaveMovil()+"' ");
					db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_PAGO1)
							 + " where ClavePago ='"+pago.getClave()+"'");
				}
			}
			
		}
		
	}
	
	public void deleteDetallePagos(String codigoPago){
		
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_PAGO1)
								 + " where ClavePago = '"+codigoPago+"' ");
		
	}
	
	public void deleteDetallePagos(String codigoPago, String claveMovil){
		
		if(!claveMovil.equals("")){
			db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_PAGO1)
					 + " where ClavePago = (select Clave from TB_PAGO " +
					 							"where ClaveMovil = '"+claveMovil+"' ) ");
		}else{
			db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_PAGO1)
					 + " where ClavePago = '"+codigoPago+"' ");
		}
		
	}
	
	public void deleteGrupoUnidadMedida(){
		
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_GRUPO_UM));
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_GRUPO1_UM));
		
	}
	
	public void deleteListaPrecios(){
		
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_LISTA_PRECIO));
		
	}
	
	public void deletePrecios(){
		
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_PRECIO));
		
	}
	
	public void deleteRegistroEstadoCuenta(){
		
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_ESTADO_CUENTA_SOCIO));
		
	}

	public void deleteRegistroNC(){
		db.execSQL("delete from TB_REPORTE_MODEL");
	}
	
	
	public void close(){}
	
	public void deleteAll(){
		
		deleteArticulo();
		deleteCantidad();
		deleteFacturas();
		deleteListaPrecios();
		deletePrecios();
		deleteOrdenVentaFull();
		deletePagosFull();
		deleteSocioNegocioFull();
		deleteRegistroEstadoCuenta();
		
	}
	
	
	private void deleteOrdenVentaFull(){
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_ORDEN_VENTA));
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_ORDEN_VENTA_DETALLE));
	}
	
	private void deletePagosFull(){
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_PAGO));
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_PAGO1));
	}
	
	public void deleteSocioNegocioFull(){
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.T_SOCIO_NEGOCIO));
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_CONTACTO_SOCIO_NEGOCIO));
		db.execSQL("delete from "+ contexto.getResources().getString(R.string.TD_DIRECCION_SOCIO_NEGOCIO));
	}

}
