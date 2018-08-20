package com.proyecto.database;

import java.util.ArrayList;

import com.proyect.movil.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBase extends SQLiteOpenHelper {

	// VERSION DE BD
	public static int DATABASE_VERSION = 99;

	// DATABASE NAME
	private static final String DATABASE_NAME = "BD_PRAGSA_SQLITE";
	private Context contexto;
	
	
	/*****************************************************/
	/**************** NOMBRES DE TABLAS ******************/
	/*****************************************************/
	private static final String TABLE_CORRELATIVO = "TB_COR";
	private static final String TABLE_TIPO_PERSONA = "TB_TIPO_PERSONA";
	private static final String TABLE_TIPO_DOCUMENTO = "TB_TIPO_DOC";

	public MyDataBase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
		this.contexto = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {

		/*****************************************************/
		/**************** SOCIOS DE NEGOCIO ******************/
		/*****************************************************/
		// TABLA SOCIO DE NEGOCIO
		String QUERY_CREATE_BP;
		QUERY_CREATE_BP = ("CREATE TABLE "
				+ contexto.getResources().getString(R.string.T_SOCIO_NEGOCIO) 
				+ "("
				+ contexto.getResources().getString(R.string.C_SN_CODIGO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN_TIPO_SOCIO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN_TIPO_PERSONA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_TIPO_DOCUMENTO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_NRO_DOCUMENTO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_NOM_RAZON_SOCIAL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_NOM_COMERCIAL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_APE_PATERNO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_APE_MATERNO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_PRI_NOMBRE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_SEG_NOMBRE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_TLF_UNO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_TLF_DOS) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_TLF_MOV) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_EMAIL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_GRUPO_SN) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_LISTA_PRE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_COND_PAGO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_INDICADOR) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_ZONA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_CREAD_MOVIL) +" TEXT, "	 //Y -> Al Sap creado movil
				+ contexto.getResources().getString(R.string.C_SN_CLAVE_MOVIL) +" TEXT, "	//CLAVE UNICA
				+ contexto.getResources().getString(R.string.C_SN_ESTADO_REGISTRO_MOVIL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_DIRECCION_FISCAL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_TRANSACCION_MOVIL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_POSEE_ACTIVOS) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_PROYECTO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_TIPO_REGISTRO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_NUM_ULT_COMPRA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_FEC_ULT_COMPRA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_MON_ULT_COMPRA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_SALDO_CUENTA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_PERSONA_CONTACTO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_CODIGO_EMPLEADO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN_VALIDO_EN_PEDIDO) +" TEXT)");
		db.execSQL(QUERY_CREATE_BP);

		// TABLA CONTACTOS DE SOCIO DE NEGOCIO
		String QUERY_CREATE_BP_CONT;
		QUERY_CREATE_BP_CONT = ("CREATE TABLE "
				+ contexto.getResources().getString(R.string.TD_CONTACTO_SOCIO_NEGOCIO)
				+ "(" 
				+ contexto.getResources().getString(R.string.C_SN1_CODIGO_SN) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN1_CODIGO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN1_NOMBRE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN1_PRI_NOMBRE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN1_SEG_NOMBRE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN1_APELLIDOS) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN1_DIRECCION) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN1_EMAIL) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN1_TLF_UNO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN1_TLF_DOS) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN1_TLF_MOVIL) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN1_POSICION) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN1_IS_PRINCIPAL) +" TEXT)");
		db.execSQL(QUERY_CREATE_BP_CONT);

		// TABLA DIRECCIONES DE SOCIO DE NEGOCIO
		String QUERY_CREATE_BP_DIRECC;
		QUERY_CREATE_BP_DIRECC = ("CREATE TABLE " 
				+ contexto.getResources().getString(R.string.TD_DIRECCION_SOCIO_NEGOCIO)
				+ "( " 
				+ contexto.getResources().getString(R.string.C_SN2_CODIGO_SN) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN2_CODIGO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN2_PAIS) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN2_DEPARTAMENTO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN2_PROVINCIA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN2_DISTRITO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN2_CALLE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN2_REFERENCIA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN2_TIPO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_SN2_IS_PRINCIPAL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_LATITUD) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_LONGITUD) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_V_LUNES) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_V_MARTES) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_V_MIERCOLES) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_V_JUEVES) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_V_VIERNES) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_V_SABADO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_V_DOMINGO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_V_FRECUENCIA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_RUTA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_ZONA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_CANAL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_GIRO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_FECHA_INICIO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_SN2_VENDEDOR) +" TEXT "
				+ ")");
		db.execSQL(QUERY_CREATE_BP_DIRECC);
		
		

		/***********************************************************/
		/********************* PEDIDO DE CLIENTE *******************/
		/*****************************+*****************************/
		// TABLA ORDEN DE VENTA CABECERA
		String QUERY_CREATE_ORD_VEN;
		QUERY_CREATE_ORD_VEN = ("CREATE TABLE "
				+ contexto.getResources().getString(R.string.T_ORDEN_VENTA)
				+ "(" 
				+ contexto.getResources().getString(R.string.C_OV_TIPO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_CLAVE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_NUMERO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_REFERENCIA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_SOCIO_NEGOCIO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_LISTA_PRE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_CONTACTO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_MONEDA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_EMP_VENTA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_COMENTARIO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_FEC_CONT) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_FEC_VEN) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_DIR_FISCAL) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_DIR_ENTREGA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_COND_PAGO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_INDICADOR) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_SUB_TOTAL) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_IMPUESTO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_TOTAL) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_CREAD_MOVIL) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_CLAVE_MOVIL) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV_ESTADO_REGISTRO_MOVIL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_OV_TRANSACCION_MOVIL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_OV_LATITUD) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_OV_LONGITUD) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_OV_RANGO_CLIENTE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_OV_HORA_CREACION) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_OV_MODO_OFFLINE) +" TEXT )");
		db.execSQL(QUERY_CREATE_ORD_VEN);

		// TABLA ORDEN DE VENTA DETALLE
		String QUERY_CREATE_ORD_VEN_DET;
		QUERY_CREATE_ORD_VEN_DET = ("CREATE TABLE " 
				+ contexto.getResources().getString(R.string.TD_ORDEN_VENTA_DETALLE)
				+ "(" 
				+ contexto.getResources().getString(R.string.C_OV1_CLAVE_OV) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV1_ARTICULO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV1_UNI_MEDIDA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV1_ALMACEN) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV1_CANTIDAD) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV1_LISTA_PRECIO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV1_PRE_UNITARIO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV1_PORC_DESCUENTO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_OV1_IMPUESTO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_OV1_LINEA) +" TEXT)");
		db.execSQL(QUERY_CREATE_ORD_VEN_DET);

		
		
		/***********************************************************/
		/************************ FACTURAS ************************/
		/*****************************+*****************************/
		
		// TABLA FACTURA
		String QUERY_CREATE_FACTURA;
		QUERY_CREATE_FACTURA = ("CREATE TABLE " 
				+ contexto.getResources().getString(R.string.T_FACTURA)
				+ "("
				+ contexto.getResources().getString(R.string.C_FACT_TIPO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_CLAVE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_NUMERO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_REFERENCIA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_SOCIO_NEGOCIO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_LISTA_PRECIO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_CONTACTO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_MONEDA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_EMP_VENTA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_COMENT) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_FEC_CONT) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_FEC_VEN) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_DIR_FISCAL) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_DIR_ENTREGA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_COND_PAGO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_INDICADOR) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_SUB_TOTAL) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_DESCUENTO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_IMPUESTO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_TOTAL) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT_SALDO) +" TEXT, "
				+ "UNIQUE("+ contexto.getResources().getString(R.string.C_FACT_CLAVE) +"))");
		db.execSQL(QUERY_CREATE_FACTURA);
		
		
		// TABLA FACTURA DETALLE
		String QUERY_CREATE_FACTURA_DETALLE;
		QUERY_CREATE_FACTURA_DETALLE = ("CREATE TABLE " 
				+ contexto.getResources().getString(R.string.TD_FACTURA1)
				+ "("
				+ contexto.getResources().getString(R.string.C_FACT1_CLAVE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_FACT1_CODIGO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_FACT1_ART) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_FACT1_UM) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT1_ALMACEN) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT1_CANT) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_FACT1_DISP) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_FACT1_LST_PRE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT1_PRE_UNI) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT1_PORC_DESCUENTO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_FACT1_IMPUESTO) +" TEXT)");
		db.execSQL(QUERY_CREATE_FACTURA_DETALLE);

		// TABLA FACTURA DETALLE - LOTES
		String QUERY_CREATE_FACTURA_DETALLE_LOTES;
		QUERY_CREATE_FACTURA_DETALLE_LOTES = ("CREATE TABLE "
				+ contexto.getResources().getString(R.string.TD_FACTURA2)
				+ "("
				+ contexto.getResources().getString(R.string.C_FACT2_CLAVE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_FACT2_LOTE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_FACT2_CANT) +" NUMERIC, "
				+ contexto.getResources().getString(R.string.C_FACT2_LINEA) +" INTEGER)");
		db.execSQL(QUERY_CREATE_FACTURA_DETALLE_LOTES);


		/***********************************************************/
		/************************ ENTREGAS ************************/
		/*****************************+*****************************/

		// TABLA ENTREGA
		String QUERY_CREATE_ENTREGA;
		QUERY_CREATE_ENTREGA = ("CREATE TABLE "
				+ contexto.getResources().getString(R.string.T_ENTREGA)
				+ "("
				+ contexto.getResources().getString(R.string.C_ENTR_TIPO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_CLAVE) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_ENTR_NUMERO) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_ENTR_REFERENCIA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_SOCIO_NEGOCIO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_LISTA_PRECIO) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_ENTR_CONTACTO) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_ENTR_MONEDA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_EMP_VENTA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_COMENT) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_FEC_CONT) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_FEC_VEN) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_DIR_FISCAL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_DIR_ENTREGA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_COND_PAGO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_INDICADOR) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_SUB_TOTAL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_DESCUENTO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_IMPUESTO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_TOTAL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR_SALDO) +" TEXT, "
				+ "UNIQUE("+ contexto.getResources().getString(R.string.C_ENTR_CLAVE) +"))");
		db.execSQL(QUERY_CREATE_ENTREGA);


		// TABLA ENTREGA DETALLE
		String QUERY_CREATE_ENTREGA_DETALLE;
		QUERY_CREATE_ENTREGA_DETALLE = ("CREATE TABLE "
				+ contexto.getResources().getString(R.string.TD_ENTREGA1)
				+ "("
				+ contexto.getResources().getString(R.string.C_ENTR1_CLAVE) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_ENTR1_CODIGO) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_ENTR1_ART) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR1_UM) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR1_ALMACEN) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR1_CANT) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR1_DISP) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR1_LST_PRE) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_ENTR1_PRE_UNI) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR1_PORC_DESCUENTO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR1_IMPUESTO) +" TEXT)");
		db.execSQL(QUERY_CREATE_ENTREGA_DETALLE);

		// TABLA ENTREGA DETALLE - LOTES
		String QUERY_CREATE_ENTREGA_DETALLE_LOTES;
		QUERY_CREATE_ENTREGA_DETALLE_LOTES = ("CREATE TABLE "
				+ contexto.getResources().getString(R.string.TD_ENTREGA2)
				+ "("
				+ contexto.getResources().getString(R.string.C_ENTR2_CLAVE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR2_LOTE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ENTR2_CANT) +" NUMERIC, "
				+ contexto.getResources().getString(R.string.C_ENTR2_LINEA) +" INTEGER)");
		db.execSQL(QUERY_CREATE_ENTREGA_DETALLE_LOTES);


		/***********************************************************/
		/************************ DEVOLUCIONES ************************/
		/*****************************+*****************************/

		// TABLA DEVOLUCION
		String QUERY_CREATE_DEVOLUCION;
		QUERY_CREATE_DEVOLUCION = ("CREATE TABLE "
				+ contexto.getResources().getString(R.string.T_DEVOLUCION)
				+ "("
				+ contexto.getResources().getString(R.string.C_DEV_TIPO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_CLAVE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_CLAVE_MOVIL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_NUMERO) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_DEV_REFERENCIA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_SOCIO_NEGOCIO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_LISTA_PRECIO) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_DEV_CONTACTO) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_DEV_MONEDA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_EMP_VENTA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_COMENT) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_FEC_CONT) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_FEC_VEN) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_DIR_FISCAL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_DIR_ENTREGA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_COND_PAGO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_INDICADOR) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_SUB_TOTAL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_DESCUENTO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_IMPUESTO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_TOTAL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_SALDO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_ESTADO_MOVIL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV_CLAVE_BASE) +" TEXT " +
				")");
		db.execSQL(QUERY_CREATE_DEVOLUCION);

		// TABLA DEVOLUCION DETALLE
		String QUERY_CREATE_DEVOLUCION_DETALLE;
		QUERY_CREATE_DEVOLUCION_DETALLE = ("CREATE TABLE "
				+ contexto.getResources().getString(R.string.TD_DEVOLUCION1)
				+ "("
				+ contexto.getResources().getString(R.string.C_DEV1_CLAVE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV1_LINEA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV1_LINEA_BASE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV1_ART) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV1_UM) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV1_ALMACEN) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV1_CANT) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV1_LST_PRE) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_DEV1_PRE_UNI) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV1_PORC_DESCUENTO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV1_IMPUESTO) +" TEXT)");
		db.execSQL(QUERY_CREATE_DEVOLUCION_DETALLE);

		// TABLA DEVOLUCION DETALLE - LOTES
		String QUERY_CREATE_DEVOLUCION_DETALLE_LOTES;
		QUERY_CREATE_DEVOLUCION_DETALLE_LOTES = ("CREATE TABLE "
				+ contexto.getResources().getString(R.string.TD_DEVOLUCION2)
				+ "("
				+ contexto.getResources().getString(R.string.C_DEV2_CLAVE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV2_LOTE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_DEV2_CANT) +" NUMERIC, "
				+ contexto.getResources().getString(R.string.C_DEV2_LINEA) +" INTEGER)");
		db.execSQL(QUERY_CREATE_DEVOLUCION_DETALLE_LOTES);

		/***********************************************************/
		/************************ NOTAS CREDITO ********************/
		/*****************************+*****************************/

		// TABLA NOTA CREDITO
		String QUERY_CREATE_NOTA_CREDITO;
		QUERY_CREATE_NOTA_CREDITO = ("CREATE TABLE "
				+ contexto.getResources().getString(R.string.T_NOTA_CREDITO)
				+ "("
				+ contexto.getResources().getString(R.string.C_NOT_TIPO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_CLAVE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_CLAVE_MOVIL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_NUMERO) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_NOT_REFERENCIA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_SOCIO_NEGOCIO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_LISTA_PRECIO) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_NOT_CONTACTO) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_NOT_MONEDA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_EMP_VENTA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_COMENT) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_FEC_CONT) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_FEC_VEN) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_DIR_FISCAL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_DIR_ENTREGA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_COND_PAGO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_INDICADOR) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_SUB_TOTAL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_DESCUENTO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_IMPUESTO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_TOTAL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_SALDO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_ESTADO_MOVIL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT_CLAVE_BASE) +" TEXT, " +
				"Latitud TEXT NULL,  " +
				"Longitud TEXT NULL,  " +
				"FechaCreacion TEXT NOT NULL,  " +
				"HoraCreacion TEXT NOT NULL,  " +
				"ModoOffline TEXT NULL  " +
				")");
		db.execSQL(QUERY_CREATE_NOTA_CREDITO);

		// TABLA NOTA CREDITO DETALLE
		String QUERY_CREATE_NOTA_CREDITO_DETALLE;
		QUERY_CREATE_NOTA_CREDITO_DETALLE = ("CREATE TABLE "
				+ contexto.getResources().getString(R.string.TD_NOTA_CREDITO1)
				+ "("
				+ contexto.getResources().getString(R.string.C_NOT1_CLAVE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT1_LINEA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT1_LINEA_BASE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT1_ART) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT1_UM) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT1_ALMACEN) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT1_CANT) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT1_LST_PRE) +" INTEGER, "
				+ contexto.getResources().getString(R.string.C_NOT1_PRE_UNI) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT1_PORC_DESCUENTO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT1_IMPUESTO) +" TEXT)");
		db.execSQL(QUERY_CREATE_NOTA_CREDITO_DETALLE);

		// TABLA DEVOLUCION DETALLE - LOTES
		String QUERY_CREATE_NOTA_CREDITO_DETALLE_LOTES;
		QUERY_CREATE_NOTA_CREDITO_DETALLE_LOTES = ("CREATE TABLE "
				+ contexto.getResources().getString(R.string.TD_NOTA_CREDITO2)
				+ "("
				+ contexto.getResources().getString(R.string.C_NOT2_CLAVE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT2_LOTE) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_NOT2_CANT) +" NUMERIC, "
				+ contexto.getResources().getString(R.string.C_NOT2_LINEA) +" INTEGER)");
		db.execSQL(QUERY_CREATE_NOTA_CREDITO_DETALLE_LOTES);



		/***********************************************************/
		/************************ ARTICULOS ************************/
		/*****************************+*****************************/
		// TABLA ARTICULOS
		String Q_CREATE_TB_ART;
		Q_CREATE_TB_ART = ("CREATE TABLE " 
				+ contexto.getResources().getString(R.string.T_ARTICULO) 
				+ "(" 
				+ contexto.getResources().getString(R.string.C_ART_COD) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_ART_NOM) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_ART_FABRICANTE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_ART_GRUPO_ART) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_ART_GRUPO_UM) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_ART_UM_VENTA) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_ART_ALM_DEF) +" TEXT, "
				+ "UNIQUE("+ contexto.getResources().getString(R.string.C_ART_COD) +"))");
		db.execSQL(Q_CREATE_TB_ART);
		
		
		
		/********************************************************************/
		/************************** PAGOS RECIBIDOS *************************/
		/*****************************+**************************************/
		// TABLA PAGO RECIBIDO
		String QUERY_CREATE_PAGO_RECIBIDO;
		QUERY_CREATE_PAGO_RECIBIDO = ("CREATE TABLE " 
				+ contexto.getResources().getString(R.string.T_PAGO)
				+ "("
				+ contexto.getResources().getString(R.string.C_PAGO_TIPO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_CLAVE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_NUMERO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_SOCIO_NEGOCIO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_EMP_VENTA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_COMENTARIO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_GLOSA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_FEC_CONTABLE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_MONEDA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_TIPO_PAGO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_TRANSF_CUENTA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_TRANSF_REF) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_TRANSF_IMPORTE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_EFECTIVO_CUENTA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_EFECTIVO_IMPORTE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_CREADO_MOVIL) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_CLAVE_MOVIL) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_ESTADO_REGISTRO_MOVIL) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_PAGO_CHEQUE_CUENTA) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_CHEQUE_BANCO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_CHEQUE_VENCIMIENTO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_CHEQUE_IMPORTE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_CHEQUE_NUMERO) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO_TRANSACCION_MOVIL) +" TEXT)");
		db.execSQL(QUERY_CREATE_PAGO_RECIBIDO);
		
		
		// TABLA PAGO RECIBIDO DETALLE
		String QUERY_CREATE_PAGO_RECIBIDO_DETALLE;
		QUERY_CREATE_PAGO_RECIBIDO_DETALLE = ("CREATE TABLE " 
				+ contexto.getResources().getString(R.string.TD_PAGO1)
				+ "("
				+ contexto.getResources().getString(R.string.C_PAGO1_CLAVE_PAGO) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_PAGO1_FACT_CLIENTE) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAGO1_IMPORTE) +" TEXT)");
		db.execSQL(QUERY_CREATE_PAGO_RECIBIDO_DETALLE);
		
		
		/***********************************************************/
		/************************** PAISES *************************/
		/*****************************+*****************************/
		// TABLA PAIS
		String QUERY_CREATE_PAIS;
		QUERY_CREATE_PAIS = ("CREATE TABLE " 
				+ contexto.getResources().getString(R.string.T_PAIS)
				+ "("
				+ contexto.getResources().getString(R.string.C_PAIS_COD) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PAIS_NOM) +" TEXT, "
				+ "UNIQUE("+ contexto.getResources().getString(R.string.C_PAIS_COD) +"))");
		db.execSQL(QUERY_CREATE_PAIS);
		
		// TABLA DEPARTAMENTO
		String QUERY_CREATE_DEPARTAMENTO;
		QUERY_CREATE_DEPARTAMENTO = ("CREATE TABLE " 
				+ contexto.getResources().getString(R.string.T_DEPARTAMENTO)
				+ "("
				+ contexto.getResources().getString(R.string.C_DEP_PAIS_COD) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_DEP_COD) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_DEP_NOM) +" TEXT, "
				+ "UNIQUE("+ contexto.getResources().getString(R.string.C_DEP_COD) +"))");
		db.execSQL(QUERY_CREATE_DEPARTAMENTO);
		
		
		// TABLA PROVINCIA
		String QUERY_CREATE_PROVINCIA;
		QUERY_CREATE_PROVINCIA = ("CREATE TABLE " 
				+ contexto.getResources().getString(R.string.T_PROVINCIA)
				+ "("
				+ contexto.getResources().getString(R.string.C_PROV_DEP_COD) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PROV_COD) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_PROV_NOM) +" TEXT, "
				+ "UNIQUE("+ contexto.getResources().getString(R.string.C_PROV_COD) +"))");
		db.execSQL(QUERY_CREATE_PROVINCIA);
		
		
		// TABLA DISTRITO
		String QUERY_CREATE_DISTRITO;
		QUERY_CREATE_DISTRITO = ("CREATE TABLE " 
				+ contexto.getResources().getString(R.string.T_DISTRITO)
				+ "("
				+ contexto.getResources().getString(R.string.C_DIST_PROV_COD) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_DIST_COD) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_DIST_NOM) +" TEXT, "
				+ "UNIQUE("+ contexto.getResources().getString(R.string.C_DIST_COD) +"))");
		db.execSQL(QUERY_CREATE_DISTRITO);
		
		
		// TABLA CALLE
		String QUERY_CREATE_CALLE;
		QUERY_CREATE_CALLE = ("CREATE TABLE " 
				+ contexto.getResources().getString(R.string.T_CALLE)
				+ "("
				+ contexto.getResources().getString(R.string.C_CALLE_DIST_COD) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_CALLE_COD) +" TEXT, " 
				+ contexto.getResources().getString(R.string.C_CALLE_NOM) +" TEXT, "
				+ "UNIQUE("+ contexto.getResources().getString(R.string.C_CALLE_COD) +"))");
		db.execSQL(QUERY_CREATE_CALLE);
		
		
		/***********************************************************/
		/************************** UTIL'S *************************/
		/*****************************+*****************************/
		// TABLA ALMACEN
		String Q_CREATE_TB_ALMACEN;
		Q_CREATE_TB_ALMACEN = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_ALMACEN) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_ALMACEN_COD) +" TEXT, " 
					+ contexto.getResources().getString(R.string.C_ALMACEN_NOM) +" TEXT, " 
					+ "UNIQUE("+ contexto.getResources().getString(R.string.C_ALMACEN_COD) +"))");
		db.execSQL(Q_CREATE_TB_ALMACEN);
		
		
		// TABLA CANTIDAD
		String Q_CREATE_TB_CANTIDAD;
		Q_CREATE_TB_CANTIDAD = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_CANTIDAD)
					+ "(" 
					+ contexto.getResources().getString(R.string.C_CANT_ALMACEN) +" TEXT, " 
					+ contexto.getResources().getString(R.string.C_CANT_ARTICULO) +" TEXT, " 
					+ contexto.getResources().getString(R.string.C_CANT_STOCK) +" TEXT, " 
					+ contexto.getResources().getString(R.string.C_CANT_COMPROMETIDO) +" TEXT, " 
					+ contexto.getResources().getString(R.string.C_CANT_SOLICITADO) +" TEXT, " 
					+ contexto.getResources().getString(R.string.C_CANTIDAD_DISPONIBLE) +" TEXT)");
		db.execSQL(Q_CREATE_TB_CANTIDAD);
		
		
		// TABLA CONDICION DE PAGO
		String Q_CREATE_TB_CONDICION_PAGO;
		Q_CREATE_TB_CONDICION_PAGO = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_COND_PAGO) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_CP_COD) +" TEXT, " 
					+ contexto.getResources().getString(R.string.C_CP_NOM) +" TEXT, " 
					+ "UNIQUE("+ contexto.getResources().getString(R.string.C_CP_COD) +"))");
		db.execSQL(Q_CREATE_TB_CONDICION_PAGO);

		
		// TABLA FABRICANTE
		String Q_CREATE_TB_FABRICANTE;
		Q_CREATE_TB_FABRICANTE = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_FABRICANTE) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_FAB_COD) +" TEXT, " 
		        	+ contexto.getResources().getString(R.string.C_FAB_NOM) +" TEXT, " 
					+ "UNIQUE("+ contexto.getResources().getString(R.string.C_FAB_COD) +"))");
		db.execSQL(Q_CREATE_TB_FABRICANTE);		
		
		
		// TABLA GRUPO DE ARTICULOS
		String Q_CREATE_TB_GRUPO_ART;
		Q_CREATE_TB_GRUPO_ART = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_GRUPO_ART) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_GA_CODIGO) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.C_GA_NOMBRE) +" TEXT, " 
					+ "UNIQUE("+ contexto.getResources().getString(R.string.C_GA_CODIGO) +"))");
		db.execSQL(Q_CREATE_TB_GRUPO_ART);		
		
		
		// TABLA GRUPO DE SOCIOS DE NEGOCIO
		String Q_CREATE_TB_GRUPO_SN;
		Q_CREATE_TB_GRUPO_SN = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_GRUPO_SN) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_GRUPO_SN_CODIGO) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.C_GRUPO_SN_NOMBRE) +" TEXT, " 
					+ "UNIQUE("+ contexto.getResources().getString(R.string.C_GRUPO_SN_CODIGO) +"))");
		db.execSQL(Q_CREATE_TB_GRUPO_SN);	
		
		
		// TABLA GRUPO DE UNIDAD DE MEDIDA
		String Q_CREATE_TB_GRUPO_UM;
		Q_CREATE_TB_GRUPO_UM = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_GRUPO_UM) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_GRUPO_UM_COD) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.C_GRUPO_UM_NOM) +" TEXT, " 
					+ "UNIQUE("+ contexto.getResources().getString(R.string.C_GRUPO_UM_COD) +"))");
		db.execSQL(Q_CREATE_TB_GRUPO_UM);
		
			// TABLA GRUPO DE UNIDAD DE MEDIDA DETALLE
				String Q_CREATE_TB_GRUPO_UM_DETALLE;
				Q_CREATE_TB_GRUPO_UM_DETALLE = ("CREATE TABLE " 
							+ contexto.getResources().getString(R.string.TD_GRUPO1_UM) 
							+ "(" 
							+ contexto.getResources().getString(R.string.C_GRUPO1_UM_COD_GRUPO) +" TEXT, " 
							+ contexto.getResources().getString(R.string.C_GRUPO1_UM_COD_UM) +" TEXT)");
				db.execSQL(Q_CREATE_TB_GRUPO_UM_DETALLE);
		
				
		// TABLA UNIDAD DE MEDIDA
		String Q_CREATE_TB_UNIDAD_MEDIDA;
		Q_CREATE_TB_UNIDAD_MEDIDA = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_UNIDAD_MEDIDA) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_UM_COD) +" TEXT, " 
					+ contexto.getResources().getString(R.string.C_UM_NOM) +" TEXT, "
			        + "UNIQUE("+ contexto.getResources().getString(R.string.C_UM_COD) +"))");
		db.execSQL(Q_CREATE_TB_UNIDAD_MEDIDA);
		
		// TABLA IMPUESTO
		String Q_CREATE_TB_IMPUESTO;
		Q_CREATE_TB_IMPUESTO = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_IMPUESTO) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_IMPUESTO_COD) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.C_IMPUESTO_NOM) +" TEXT, "
				+ contexto.getResources().getString(R.string.C_IMPUESTO_TAS) +" NUMERIC, "
				+ "UNIQUE("+ contexto.getResources().getString(R.string.C_IMPUESTO_COD) +"))");
		db.execSQL(Q_CREATE_TB_IMPUESTO);
				
				
		// TABLA INDICADOR
		String Q_CREATE_TB_INDICADOR;
		Q_CREATE_TB_INDICADOR = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_INDICADOR) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_INDICADOR_COD) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.C_INDICADOR_NOM) +" TEXT, " 
					+ "UNIQUE("+ contexto.getResources().getString(R.string.C_INDICADOR_COD) +"))");
		db.execSQL(Q_CREATE_TB_INDICADOR);		
				
				
		
		// TABLA LISTA DE PRECIOS
		String Q_CREATE_TB_LISTA_PRECIO;
		Q_CREATE_TB_LISTA_PRECIO = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_LISTA_PRECIO) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_LST_PRE_COD) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.C_LST_PRE_NOM) +" TEXT, " 
					+ "UNIQUE("+ contexto.getResources().getString(R.string.C_LST_PRE_COD) +"))");
		db.execSQL(Q_CREATE_TB_LISTA_PRECIO);
				
		// TABLA PRECIO
		String Q_CREATE_TB_PRECI;
		Q_CREATE_TB_PRECI = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_PRECIO) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_PRE_COD_LISTA) +" TEXT, "
					+ contexto.getResources().getString(R.string.C_PRE_ART) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.C_PRE_MONEDA) +" TEXT, "
			       	+ contexto.getResources().getString(R.string.C_PRE_PRECIO_VENTA) +" TEXT)");
		db.execSQL(Q_CREATE_TB_PRECI);
		
		
		
		// TABLA MONEDA
		String Q_CREATE_TB_MONEDA;
		Q_CREATE_TB_MONEDA = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_MONEDA) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_MONEDA_COD) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.C_MONEDA_NOM) +" TEXT, " 
					+ "UNIQUE("+ contexto.getResources().getString(R.string.C_MONEDA_COD) +"))");
		db.execSQL(Q_CREATE_TB_MONEDA);
				
	
		
		// TABLA ZONA
		String Q_CREATE_TB_ZONA;
		Q_CREATE_TB_ZONA = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_ZONA) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_ZONA_CODIGO) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.C_ZONA_NOMBRE) +" TEXT, " 
					+ "UNIQUE("+ contexto.getResources().getString(R.string.C_ZONA_CODIGO) +"))");
		db.execSQL(Q_CREATE_TB_ZONA);
		
		
		// TABLA CUENTA
		String Q_CREATE_TB_CUENTA;
		Q_CREATE_TB_CUENTA = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_CUENTA) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_CUENTA_COD) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.C_CUENTA_NOM) +" TEXT, " 
					+ "UNIQUE("+ contexto.getResources().getString(R.string.C_CUENTA_COD) +"))");
		db.execSQL(Q_CREATE_TB_CUENTA);
		
		
		// TABLA BANCO
		String Q_CREATE_TB_BANCO;
		Q_CREATE_TB_BANCO = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_BANCO) 
					+ "(" 
					+ contexto.getResources().getString(R.string.C_BANCO_COD) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.C_BANCO_NOM) +" TEXT, " 
					+ "UNIQUE("+ contexto.getResources().getString(R.string.C_BANCO_COD) +"))");
		db.execSQL(Q_CREATE_TB_BANCO);
		
		
		// TABLA ESTADO CUENTA SOCIO
		String Q_CREATE_TB_EST_CUENTA_SOCIO;
		Q_CREATE_TB_EST_CUENTA_SOCIO = ("CREATE TABLE " 
					+ contexto.getResources().getString(R.string.T_ESTADO_CUENTA_SOCIO) 
					+ "(" 
					+ contexto.getResources().getString(R.string.TipoReporte) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.Cliente) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.Nombre) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.ListaPrecio) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.LineaCredito) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.CondicionPago) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.Clave) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.Sunat) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.Condicion) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.Vendedor) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.Emision) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.Moneda) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.Total) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.Saldo) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.Pago_Fecha) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.Pago_Dias) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.Pago_Moneda) +" TEXT, " 
			       	+ contexto.getResources().getString(R.string.Pagado_Importe) +" TEXT)");
		db.execSQL(Q_CREATE_TB_EST_CUENTA_SOCIO);
				

		
		// TABLA TIPO DE PERSONA
		String Q_CREATE_TB_TIPO_PERSONA;
		Q_CREATE_TB_TIPO_PERSONA = ("CREATE TABLE " + TABLE_TIPO_PERSONA + "(COD_TIP TEXT, DES_TIP TEXT, " +
				"UNIQUE(COD_TIP))");
		db.execSQL(Q_CREATE_TB_TIPO_PERSONA);
		
		// TABLA TIPO DE PERSONA
		String Q_CREATE_TB_TIPO_DIRECCION;
		Q_CREATE_TB_TIPO_DIRECCION = ("CREATE TABLE TB_TIPO_DIRECCION (CODIGO TEXT, NOMBRE TEXT, " +
				"UNIQUE(CODIGO))");
		db.execSQL(Q_CREATE_TB_TIPO_DIRECCION);
		
		// TABLA TIPO DE DOCUMENTO
		String Q_CREATE_TB_TIPO_DOC;
		Q_CREATE_TB_TIPO_DOC = ("CREATE TABLE " + TABLE_TIPO_DOCUMENTO + "(COD_DOC TEXT, DES_DOC TEXT, " +
				"UNIQUE(COD_DOC))");
		db.execSQL(Q_CREATE_TB_TIPO_DOC);
		
			
		// TABLA CORRELATIVO
		String Q_CREATE_TB_CORRELATIVO;
		Q_CREATE_TB_CORRELATIVO = ("CREATE TABLE " + TABLE_CORRELATIVO + "(COD_COR TEXT, NUM_COR INTEGER)");
		db.execSQL(Q_CREATE_TB_CORRELATIVO);

		// TABLA PROYECTO
		String Q_CREATE_TB = ("CREATE TABLE TB_PROYECTO(CODIGO TEXT, DESCRIPCION TEXT, UNIQUE(CODIGO))");
		db.execSQL(Q_CREATE_TB);

		// TABLA CANAL
		Q_CREATE_TB = ("CREATE TABLE TB_CANAL(CODIGO TEXT, DESCRIPCION TEXT, UNIQUE(CODIGO))");
		db.execSQL(Q_CREATE_TB);

		// TABLA GIRO
		Q_CREATE_TB = ("CREATE TABLE TB_GIRO(CODIGO TEXT, DESCRIPCION TEXT, UNIQUE(CODIGO))");
		db.execSQL(Q_CREATE_TB);

		// TABLA REPORTE NC
		String Q_CREATE_TB_REPORTE_MODEL;
		Q_CREATE_TB_REPORTE_MODEL = ("CREATE TABLE TB_REPORTE_MODEL (Clave TEXT , " +
								 "Sunat TEXT," +
								 "Emision TEXT, " +
								 "Dias TEXT , " +
								 "Ruc TEXT , " +
								 "Nombre TEXT , " +
				  			     "Direccion TEXT , " +
								 "Total TEXT , " +
								 "Pagado TEXT , " +
								 "Saldo TEXT) ");
		db.execSQL(Q_CREATE_TB_REPORTE_MODEL);

		//tabla auditoria
		String Q_CREATE_TB_AUDITORIA;
		Q_CREATE_TB_AUDITORIA = ("CREATE TABLE TB_AUDITORIA (Codigo INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
				"FechaInsercion DATETIME NOT NULL  DEFAULT CURRENT_DATE, " +
				"TipoEntidad TEXT NOT NULL , " +
				"Estado BOOL NOT NULL) ");
		db.execSQL(Q_CREATE_TB_AUDITORIA);

		//tabla empresas
		String Q_CREATE_TB_EMPRESAS;
		Q_CREATE_TB_EMPRESAS = ("CREATE TABLE TB_EMPRESA (Id INTEGER PRIMARY KEY NOT NULL, " +
				"FechaInsercion DATETIME NOT NULL DEFAULT CURRENT_DATE, " +
				"Descripcion TEXT, " +
				"BaseDatos TEXT, " +
				"Usuario TEXT, " +
				"Clave TEXT, " +
				"Estado TEXT," +
				"Observacion TEXT) ");
		db.execSQL(Q_CREATE_TB_EMPRESAS);

		//tabla motivos incidencia
		String Q_CREATE_TB_MOTIVOS;
		Q_CREATE_TB_MOTIVOS = ("CREATE TABLE TB_MOTIVO (Id INTEGER PRIMARY KEY NOT NULL, " +
				"Descripcion TEXT, " +
				"ValOrden TEXT, " +
				"ValEntrega TEXT, " +
				"ValFactura TEXT) ");
		db.execSQL(Q_CREATE_TB_MOTIVOS);

		//tabla INCIDENCIAS
		String Q_CREATE_TB_INCIDENCIA;
		Q_CREATE_TB_INCIDENCIA = ("CREATE TABLE TB_INCIDENCIA " +
				"(Id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL, " +
				"ClaveMovil TEXT NOT NULL, " +
				"Origen TEXT NOT NULL, " +
				"IdCliente TEXT NULL, " +
				"NombreCliente TEXT NULL, " +
				"CodigoContacto INTEGER, " +
				"NombreContacto TEXT NULL, " +
				"CodigoDireccion TEXT NULL,  " +
				"DetalleDireccion TEXT NULL,  " +
				"Motivo TEXT NULL,  " +
				"DescripcionMotivo TEXT NULL,  " +
				"Comentarios TEXT NULL,  " +
				"CodigoVendedor INTEGER NOT NULL,  " +
				"Latitud TEXT NULL,  " +
				"Longitud TEXT NULL,  " +
				"FechaCreacion TEXT NOT NULL,  " +
				"HoraCreacion TEXT NOT NULL,  " +
				"ModoOffline TEXT NULL,  " +
				"ClaveFactura TEXT NULL,  " +
				"SerieFactura TEXT NULL,  " +
				"CorrelativoFactura INTEGER,  " +
				"TipoIncidencia TEXT NULL,  " +
				"FechaPago TEXT NULL,  " +
				"Foto BLOB NULL, " +
				"Rango TEXT NULL,  " +
				"Sincronizado TEXT NOT NULL) ");
		db.execSQL(Q_CREATE_TB_INCIDENCIA);

		/************************************************************/
		/************************** INSERTS *************************/
		/*****************************+******************************/
				
		//INSERT PARA GENERAR C�DIGOS (CORRELATIVOS)
		String Q_INS_COR, Q_INS_COR1;
		Q_INS_COR = ("INSERT INTO TB_COR(COD_COR,NUM_COR) VALUES('ORD',1)");
			db.execSQL(Q_INS_COR);
		Q_INS_COR1 = ("INSERT INTO TB_COR(COD_COR,NUM_COR) VALUES('CNT',1)");
			db.execSQL(Q_INS_COR1);
		Q_INS_COR1 = ("INSERT INTO TB_COR(COD_COR,NUM_COR) VALUES('PAG',1)");
			db.execSQL(Q_INS_COR1);
		Q_INS_COR1 = ("INSERT INTO TB_COR(COD_COR,NUM_COR) VALUES('INC',1)");
		db.execSQL(Q_INS_COR1);
		Q_INS_COR1 = ("INSERT INTO TB_COR(COD_COR,NUM_COR) VALUES('DEV',1)");
		db.execSQL(Q_INS_COR1);
		db.execSQL("INSERT INTO TB_COR(COD_COR,NUM_COR) VALUES('NOT',1)");


	   // INSERTS MANUALES (TEMPORAL ART�CULO)
//		String Q_INS_PROD;
//		Q_INS_PROD = ("INSERT INTO TB_ART(COD_PROD,NOM_PROD,STOCK_PROD,PROV_PROD,GRUPO_UM) " +
//						"VALUES('EQM02109','UTILES DE OFICINA','66','MEDTRONIC USA', '-1'), " +
//						"('EQM03301','KIT VARIABLE','92','VITREX MEDICAL', '-1'), " +
//						"('EQM03303','KIT AAAAA','12','VITREX MEDICAL', '-1')");
//		db.execSQL(Q_INS_PROD);
//		
//		
		
		//Insert tipo de persona
		String Q_INS_TIPO_PER = ("INSERT INTO TB_TIPO_PERSONA(COD_TIP,DES_TIP) " +
								 "VALUES('TPJ','Juridica')," +
								 "('TPN','Natural')");
		db.execSQL(Q_INS_TIPO_PER);
		
		//Insert tipo de direcci�n
		String Q_INS_TIPO_DIRECCION = ("INSERT INTO TB_TIPO_DIRECCION(CODIGO,NOMBRE) " +
								 "VALUES('B','Fiscal')," +
								 "('S','Entrega')");
		db.execSQL(Q_INS_TIPO_DIRECCION);
		
		//Insert tipo de documento
		String Q_INS_TIPO_DOC = ("INSERT INTO TB_TIPO_DOC(COD_DOC,DES_DOC) " +
								 "VALUES('0','OTROS TIPOS DE DOCUMENTOS')," +
								 "('1','DOCUMENTO NACIONAL DE IDENTIDAD')," +
								 "('4','CARNET DE EXTRANJERIA')," +
								 "('6','REGISTRO UNICO DE CONTRIBUYENTES')," +
								 "('7','PASAPORTE')");
		db.execSQL(Q_INS_TIPO_DOC);
		
		
		//getDbTableDetails(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		//BORRAR LAS TABLAS QUE EST�N CREADAS CUANDO HAY UNA ACTUALIZACI�N A LA BD
		db.execSQL("DROP TABLE IF EXISTS TB_TIPO_DOC");
		db.execSQL("DROP TABLE IF EXISTS TB_TIPO_PERSONA");
		db.execSQL("DROP TABLE IF EXISTS TB_COR"); 
		db.execSQL("DROP TABLE IF EXISTS TB_TIPO_DIRECCION"); 
		
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_ALMACEN));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_ARTICULO));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_CANTIDAD));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_COND_PAGO));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_FABRICANTE));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_FACTURA));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.TD_FACTURA1));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_GRUPO_ART));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_GRUPO_SN));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_GRUPO_UM));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.TD_GRUPO1_UM));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_IMPUESTO));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_INDICADOR));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_LISTA_PRECIO));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_PRECIO));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_MONEDA));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_ORDEN_VENTA));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.TD_ORDEN_VENTA_DETALLE));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_PAGO));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.TD_PAGO1));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_PAIS));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_DEPARTAMENTO));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_PROVINCIA));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_DISTRITO));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_CALLE));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_SOCIO_NEGOCIO));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.TD_CONTACTO_SOCIO_NEGOCIO));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.TD_DIRECCION_SOCIO_NEGOCIO));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_ZONA));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_UNIDAD_MEDIDA));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_CUENTA));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_BANCO));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_ESTADO_CUENTA_SOCIO));
		db.execSQL("DROP TABLE IF EXISTS TB_AUDITORIA");
		db.execSQL("DROP TABLE IF EXISTS TB_REPORTE_MODEL");
		db.execSQL("DROP TABLE IF EXISTS TB_EMPRESA");
		db.execSQL("DROP TABLE IF EXISTS TB_MOTIVO");
		db.execSQL("DROP TABLE IF EXISTS TB_INCIDENCIA");
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_FACTURA));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.TD_FACTURA1));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.TD_FACTURA2));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_ENTREGA));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.TD_ENTREGA1));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.TD_ENTREGA2));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_DEVOLUCION));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.TD_DEVOLUCION1));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.TD_DEVOLUCION2));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.T_NOTA_CREDITO));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.TD_NOTA_CREDITO1));
		db.execSQL("DROP TABLE IF EXISTS "+contexto.getResources().getString(R.string.TD_NOTA_CREDITO2));
		db.execSQL("DROP TABLE IF EXISTS TB_PROYECTO");
		db.execSQL("DROP TABLE IF EXISTS TB_CANAL");
		db.execSQL("DROP TABLE IF EXISTS TB_GIRO");

		onCreate(db);

	}
	
	
	
	/**
     * Get all table Details from teh sqlite_master table in Db.
     * 
     * @return An ArrayList of table details.
     */
    public ArrayList<String[]> getDbTableDetails(SQLiteDatabase db) {
        Cursor c = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table'", null);
        ArrayList<String[]> result = new ArrayList<String[]>();
        int i = 0;
        result.add(c.getColumnNames());
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String[] temp = new String[c.getColumnCount()];
            for (i = 0; i < temp.length; i++) {
                temp[i] = c.getString(i);
            }
            result.add(temp);
        }

        return result;
    }
	

}
