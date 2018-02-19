package com.proyecto.utils;

import java.util.Map;

import android.content.SharedPreferences;

import com.proyect.movil.R;

public class Variables {
	
	public static final int idIconRightBlue36dp = R.drawable.ic_keyboard_arrow_right_blue_36dp;
	static SharedPreferences sharedpreferences;
	static Map<String, ?> editorGet = null;
	
	//Para el webservice
	public static final String HOST="10.33.23.81";
	public static final String NAME_SPACE= "http://tempuri.org/";
	
	public static final String NOMBRE_EMPLEADO = "nombreEmpleadoVentas";
	public static final String CODIGO_EMPLEADO = "codigoEmpleadoVentas";
	public static final String USUARIO_EMPLEADO = "usuarioMovilEmpleadoVentas";
	public static final String PASSWORD_EMPLEADO = "passwordMovilEmpleadoVentas";
	public static final String MOVIL_ACCESA = "MovilAccesa";
	public static final String MOVIL_EDITAR = "MovilEditar";
	public static final String MOVIL_APROBAR = "MovilAprobar";
	public static final String MOVIL_CREAR = "MovilCrear";
	public static final String MOVIL_RECHAZAR = "MovilRechazar";
	public static final String MOVIL_ESCOGER_PRECIO = "MovilEscogerPrecio";
	public static final String PERFIL = "perfilUsuario";

	public static final String MENU_SOCIOS_NEGOCIO = "Socios de negocio";
	public static final String MENU_INVENTARIO = "Inventario";
	public static final String MENU_PEDIDOS = "Pedidos";
	public static final String MENU_FACTURAS = "Facturas";
	public static final String MENU_COBRANZAS = "Cobranzas";
	public static final String MENU_REPORTES = "Reportes";
	public static final String MENU_ACTIVIDADES = "Actividades";

	public static final	String RESPONSE_ERROR ="Error";
	public static final String RESPONSE_SUCCESS = "Success";
	
}
