package com.proyecto.reportes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.DoubleRound;
import com.proyecto.utils.StringDateCast;
import com.proyecto.utils.Variables;

import java.util.ArrayList;
import java.util.List;

public class QueryReport {
	
	private Context contexto;
	private SQLiteDatabase db;
	private SharedPreferences pref;
	
	private DataBaseHelper helper;
	
	public QueryReport(Context context) {
		this.contexto = context;
		
		helper = DataBaseHelper.getHelper(contexto);
		db = helper.getDataBase();
		
		pref = PreferenceManager
				.getDefaultSharedPreferences(contexto);
	}
	
	
	@SuppressLint("SimpleDateFormat") 
	public ReportFormatObjectSaldosVendedor generateReporteSaldoVendedor(String fecIni, String fecFin){
		
		ReportFormatObjectSaldosVendedor reporte = null;
		ReportFormatObjectSaldosVendedorDetail detalle = null;
		List<ReportFormatObjectSaldosVendedorDetail> detalles;

		String empleado =  pref.getString(Variables.NOMBRE_EMPLEADO, "");

		Cursor data= db.rawQuery(" SELECT " +
								 " F.Tipo, " +
								 " F.Clave, " +
								 " F.Referencia, " +
								 " F.FechaContable, " +
								 " (julianday(CURRENT_DATE)-julianday(substr(F.FechaContable,1,4)||'-'||substr(F.FechaContable,5,2)||'-'||substr(F.FechaContable,7,2))) 'difference' ,"+
								 " IFNULL((SELECT NumeroDocumento FROM TB_SOCIO_NEGOCIO WHERE Codigo = F.SocioNegocio),''), " +
								 " IFNULL((SELECT NombreRazonSocial FROM TB_SOCIO_NEGOCIO WHERE Codigo = F.SocioNegocio),''), " +
								 " IFNULL((SELECT IFNULL(P.NOMBRE,'') || ' - ' || DE.NOMBRE || ' - '|| PV.NOMBRE " +
								 					  "|| ' - '|| DT.NOMBRE || ' / ' || IFNULL(CA.NOMBRE,'') " +
								 					  "FROM TB_SOCIO_NEGOCIO_DIRECCION D LEFT JOIN TB_PAIS P " +
								 					  		"ON D.Pais = P.CODIGO LEFT JOIN TB_DEPARTAMENTO DE " +
								 					  		"ON D.Departamento = DE.CODIGO LEFT JOIN TB_PROVINCIA PV " +
								 					  		"ON D.Provincia = PV.CODIGO LEFT JOIN TB_DISTRITO DT " +
								 					  		"ON D.Distrito = DT.CODIGO LEFT JOIN TB_CALLE CA " +
								 					  		"ON D.Calle = CA.CODIGO " +
								 		  "WHERE D.CodigoSocioNegocio = F.SocioNegocio " +
								 		  "AND D.Codigo = F.DireccionFiscal ),''), " +
								 " F.Total, " +
								 " ifnull((select sum(cast(X1.Importe as numeric)) from TB_PAGO_DETALLE X1 join TB_PAGO X2 ON X1.ClavePago = X2.Clave AND X2.Tipo = 'A'  where X1.FacturaCliente = F.Clave) +(F.Total - F.Saldo) , (F.Total - F.Saldo)) as 'Cobrado'," +
								 " IFNULL((select F.Total - sum(cast(X1.Importe as numeric)) - (F.Total - F.Saldo)   from TB_PAGO_DETALLE X1 join TB_PAGO X2 ON X1.ClavePago = X2.Clave AND X2.Tipo = 'A'  where X1.FacturaCliente = F.Clave) ,F.Saldo) as 'Saldo' " +
								 " FROM TB_FACTURA F " +
								 " WHERE F.FechaContable between '"+
								 StringDateCast.castDatetoDateWithoutSlash(fecIni)+
								 "' and '"+
								 StringDateCast.castDatetoDateWithoutSlash(fecFin)+
								 "'  " +
				"union all " +
				 			"select 'NC' as 'Tipo'," +
								"X0.Clave," +
								"X0.Sunat," +
								"X0.Emision," +
								"X0.Dias," +
								"X0.Ruc," +
								"X0.Nombre," +
								"X0.Direccion," +
								"X0.Total," +
								"X0.Pagado," +
								"X0.Saldo " +
								"from TB_REPORTE_MODEL X0 where X0.Emision between '"+StringDateCast.castDatetoDateWithoutSlash(fecIni)+
																			"' and '"+StringDateCast.castDatetoDateWithoutSlash(fecFin)+"'", null);

		if(data.getCount()>0)
		{
			
			detalles = new ArrayList<ReportFormatObjectSaldosVendedorDetail>();
			reporte = new ReportFormatObjectSaldosVendedor();
			reporte.setEmpresa("Productos Alimentos Golosinas S.A.C.");
			reporte.setDireccion("Calle Tacna N° 330-Iquitos-Maynas-Loreto");
			reporte.setEmpleado(empleado);
			double totalGen = 0;
			double totalPag = 0;
			double totalSal = 0;
			
			while (data.moveToNext()) {		
				
				detalle = new ReportFormatObjectSaldosVendedorDetail();
				String tipoDocumento = "";
				if(data.getString(2) != null && !data.getString(2).trim().startsWith("any")) {
					if (data.getString(2).substring(0, 1).equalsIgnoreCase("F"))
						tipoDocumento = "Factura";
					if (data.getString(2).substring(0, 1).equalsIgnoreCase("B"))
						tipoDocumento = "Boleta";
					detalle.setSunat(data.getString(2));
				}else {
					tipoDocumento = "Sin referencia";
					detalle.setSunat(tipoDocumento);
				}

				detalle.setDocumento(tipoDocumento);
				
				detalle.setClave(data.getString(1));
				detalle.setEmision(StringDateCast.castStringtoDate(data.getString(3)));
				detalle.setDias(data.getString(4));
				detalle.setRuc(data.getString(5));
				detalle.setNombre(data.getString(6));
				detalle.setDireccion(data.getString(7));
				
				double total = DoubleRound.round(Double.parseDouble(data.getString(8)),3);
				detalle.setTotal(String.valueOf(total));
				totalGen += total;
				
				double pago = DoubleRound.round(data.getDouble(9),3);
				detalle.setPagado(String.valueOf(pago));
				totalPag += pago;
				
				double saldo = DoubleRound.round(Double.parseDouble(data.getString(10)),3);
				detalle.setSaldo(String.valueOf(saldo));
				totalSal += saldo;
				
				detalles.add(detalle);
				
			}
			
			reporte.setTotal(DoubleRound.round(totalGen, 3));
			reporte.setPagado(DoubleRound.round(totalPag, 3));
			reporte.setSaldo(DoubleRound.round(totalSal, 3));
			reporte.setDetalles(detalles);
			
			//Cerrar el cursor
			data.close();
		}
		
		return reporte;
		
	}
	
	
	@SuppressLint("SimpleDateFormat") 
	public ReportFormatObjectSaldosVendedor generateReportePreCobranza(String fecIni, String fecFin){
		
		ReportFormatObjectSaldosVendedor reporte = null;
		ReportFormatObjectSaldosVendedorDetail detalle = null;
		List<ReportFormatObjectSaldosVendedorDetail> detalles;

		String empleado =  pref.getString(Variables.NOMBRE_EMPLEADO, "");
     	
     	Cursor data= db.rawQuery("SELECT " +
				"F.Tipo, "+
				"F.Clave, "+
				"F.Referencia as 'Sunat', "+
				"F.FechaContable as 'Emision', "+
				"(julianday(CURRENT_DATE)-julianday(substr(F.FechaContable,1,4)||'-'||substr(F.FechaContable,5,2)||'-'||substr(F.FechaContable,7,2))) 'Dias' , "+
				"IFNULL(S.NumeroDocumento,'') 'Ruc/Dni', "+
				"IFNULL(S.NombreRazonSocial,'') 'Nombre', "+
				"F.Total as 'Importe', "+
				"IFNULL((select sum(cast(X1.Importe as numeric)) from TB_PAGO_DETALLE X1 join TB_PAGO X2 ON X1.ClavePago = X2.Clave AND X2.Tipo = 'A'  where X1.FacturaCliente = F.Clave) +(F.Total - F.Saldo) , (F.Total - F.Saldo)) as 'Cobrado',"+
				"IFNULL((select F.Total - sum(cast(X1.Importe as numeric)) - (F.Total - F.Saldo)   from TB_PAGO_DETALLE X1 join TB_PAGO X2 ON X1.ClavePago = X2.Clave AND X2.Tipo = 'A'  where X1.FacturaCliente = F.Clave) ,F.Saldo) as 'Saldo', "+
				"sum(cast(D.Importe as numeric)) as 'Recibo' "+
		"FROM TB_FACTURA F "+
		"INNER JOIN  TB_PAGO_DETALLE D ON D.FacturaCliente = F.Clave "+
		"INNER JOIN TB_PAGO P ON P.Clave = D.ClavePago "+
		"INNER JOIN TB_SOCIO_NEGOCIO S ON S.Codigo = F.SocioNegocio "+
                " WHERE P.FechaContable between '"+
                StringDateCast.castDatetoDateWithoutSlash(fecIni)+
                "' and '"+
                StringDateCast.castDatetoDateWithoutSlash(fecFin)+
                "' " +
				"GROUP BY F.CLAVE", null);

		if(data.getCount()>0)
		{
			
			detalles = new ArrayList<ReportFormatObjectSaldosVendedorDetail>();
			reporte = new ReportFormatObjectSaldosVendedor();
			reporte.setEmpresa("Productos Alimentos Golosinas S.A.C.");
			reporte.setDireccion("Calle Tacna N� 330-Iquitos-Maynas-Loreto");
			reporte.setEmpleado(empleado);
			double totalGen = 0;
			double totalPag = 0;
			double totalSal = 0;
			double totalRecibo = 0;
			
			while (data.moveToNext()) {		
				
				detalle = new ReportFormatObjectSaldosVendedorDetail();
				String tipoDocumento = "";
				if(data.getString(2).substring(0, 1).equalsIgnoreCase("F"))
					tipoDocumento = "Factura";
				if(data.getString(2).substring(0, 1).equalsIgnoreCase("B"))
					tipoDocumento = "Boleta";
				
				detalle.setDocumento(tipoDocumento);
				
				detalle.setClave(data.getString(1));
				detalle.setSunat(data.getString(2));
				detalle.setEmision(StringDateCast.castStringtoDate(data.getString(3)));
				detalle.setDias(data.getString(4));
				detalle.setRuc(data.getString(5));
				detalle.setNombre(data.getString(6));
				
				double total = DoubleRound.round(Double.parseDouble(data.getString(7)),3);
				detalle.setTotal(String.valueOf(total));
				totalGen += total;
				
				double pago = DoubleRound.round(data.getDouble(8),3);
				detalle.setPagado(String.valueOf(pago));
				totalPag += pago;
				
				double saldo = DoubleRound.round(Double.parseDouble(data.getString(9)),3);
				detalle.setSaldo(String.valueOf(saldo));
				totalSal += saldo;
				
				detalle.setRecibo(String.valueOf(data.getDouble(10)));
				totalRecibo += data.getDouble(10);
				
				detalles.add(detalle);
				
			}
			
			reporte.setTotal(DoubleRound.round(totalGen, 3));
			reporte.setPagado(DoubleRound.round(totalPag, 3));
			reporte.setSaldo(DoubleRound.round(totalSal, 3));
			reporte.setTotalRecibo(DoubleRound.round(totalRecibo, 4));
			reporte.setDetalles(detalles);
			
			//Cerrar el cursor
			data.close();
		}
		
		return reporte;
		
	}
	
	
	public ArrayList<ReporteEstadoCuenta> generarReporteCuentasSocios(String claveSocio){
		
		ArrayList<ReporteEstadoCuenta> lista = null;
		ReporteEstadoCuenta objeto;

		Cursor data= db.rawQuery("select * from TB_ESTADO_CUENTA_SOCIO where Cliente = '"+claveSocio+"' " , null); 
		
		if(data.getCount()>0)
		{
			lista = new ArrayList<ReporteEstadoCuenta>();
			while (data.moveToNext()) {		
				objeto = new ReporteEstadoCuenta();
				objeto.setTipoReporte(data.getString(0));
				objeto.setCliente(data.getString(1));
				objeto.setNombre(data.getString(2));
				objeto.setListaPrecio(data.getString(3));
				objeto.setLineaCredito(data.getString(4));
				objeto.setCondicionPago(data.getString(5));
				objeto.setClave(data.getString(6));
				objeto.setSunat(data.getString(7));
				objeto.setCondicion(data.getString(8));
				objeto.setVendedor(data.getString(9));
				objeto.setEmision(data.getString(10));
				objeto.setMoneda(data.getString(11));
				objeto.setTotal(data.getString(12));
				objeto.setSaldo(data.getString(13));
				objeto.setPago_Fecha(data.getString(14));
				objeto.setPago_Dias(data.getString(15));
				objeto.setPago_Moneda(data.getString(16));
				objeto.setPagado_Importe(data.getString(17));
				lista.add(objeto);
			}
			
			//Cerrar el cursor
			data.close();
									
		}
		
		return lista;
		
	}
	
	
	public ReportFormatObjectProductoXMarca generarReporteProductoXMarca(){
		
		ReportFormatObjectProductoXMarca reporte = null;
		List<ReportFormatObjectProductoXMarca_Marcas> listaMarcas = null;
		ReportFormatObjectProductoXMarca_Marcas marca = null;
		List<ReportFormatObjectProductoXMarca_Marcas_Detalles> listaDetalles = null;
		ReportFormatObjectProductoXMarca_Marcas_Detalles detalle = null;
		
		Cursor data= db.rawQuery("select CODIGO, NOMBRE from TB_FABRICANTE " , null); 
		
		if(data.getCount() > 0){
			
			reporte = new ReportFormatObjectProductoXMarca();
			reporte.setEmpleado(pref.getString(Variables.NOMBRE_EMPLEADO, ""));
			listaMarcas = new ArrayList<ReportFormatObjectProductoXMarca_Marcas>();
			
			while(data.moveToNext()){
				
				marca = new ReportFormatObjectProductoXMarca_Marcas();
				marca.setDescripcion(data.getString(1));
				
				listaDetalles = new ArrayList<ReportFormatObjectProductoXMarca_Marcas_Detalles>();
				
				Cursor data2= db.rawQuery("select " +
											" A.Nombre," +
											" (SELECT SUM(CAST(STOCK AS NUMERIC)) FROM TB_CANTIDAD" +
													" where ARTICULO = A.Codigo)," +
											" A.Codigo " +
										"from TB_ARTICULO A " +
										"WHERE Fabricante = '"+data.getString(0)+"' " +
										"ORDER BY A.Nombre" , null); 
				if(data2.getCount() > 0){
					
					while(data2.moveToNext()){
						
						detalle = new ReportFormatObjectProductoXMarca_Marcas_Detalles();
						detalle.setDescripcionProducto(data2.getString(0));
						detalle.setStock(data2.getString(1));
						
						Cursor data3 = db.rawQuery("select " +
														"PrecioVenta " +
													"from TB_PRECIO " +
													"where Articulo = '"+data2.getString(2)+"' " +
													"and CodigoLista = '1'" +
													"order by CodigoLista", null);
						
						while(data3.moveToNext()){
							detalle.setPrecioCobertura(data3.getString(0));
						}
						
						Cursor data4 = db.rawQuery("select " +
														"PrecioVenta " +
													"from TB_PRECIO " +
													"where Articulo = '"+data2.getString(2)+"' " +
													"and CodigoLista = '2' " +
													"order by CodigoLista", null);

						while(data4.moveToNext()){
							detalle.setPrecioMayorista(data4.getString(0));
						}
						
						listaDetalles.add(detalle);
						data3.close();
						data4.close();
						
					}
					
					marca.setDetalles(listaDetalles);
					data2.close();
				}
				
				listaMarcas.add(marca);
			}
			
			reporte.setMarcas(listaMarcas);
			data.close();
			
		}
		
		
		return reporte;
		
	}

	
	public void close(){
		
//		db.close();
//		cn.close();
		
	}

}
