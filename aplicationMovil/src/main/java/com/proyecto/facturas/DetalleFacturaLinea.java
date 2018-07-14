package com.proyecto.facturas;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.proyect.movil.R;
import com.proyecto.database.MyDataBase;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;

public class DetalleFacturaLinea extends AppCompatActivity{
	
	//LIST VIEW PRINCIPAL QUE CONTIENE A TODO
	private ListView lvPrincipal = null;
	private ArrayList<FormatCustomListView> searchResults = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
	private FormatCustomListView customListObject = null;
	private String idDetalle, idArticulo;
	private Context contexto;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalle_pedido_cliente_articulo);
		contexto = this;
		
		lvPrincipal = (ListView) findViewById(R.id.lvArticuloPedido);
		Intent myIntent = getIntent(); // gets the previously created intent

		if (myIntent.getStringExtra("idFact") != null) {
			idDetalle = myIntent.getStringExtra("idFact");
		}
		
		if (myIntent.getStringExtra("idArt") != null) {
			idArticulo = myIntent.getStringExtra("idArt");
		}

		// TOOLBAR
		Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(myToolbar);

		// Get a support ActionBar corresponding to this toolbar
		ActionBar ab = getSupportActionBar();

		// Enable the Up button
		ab.setDisplayHomeAsUpEnabled(true);
		// TOOLBAR

		buildDataDetail();


	}

	
	private void buildDataDetail() {

		searchResults = new ArrayList<FormatCustomListView>();

		// TRAER TODO DE SQLITE
		MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
		SQLiteDatabase db = cn.getWritableDatabase();

		Cursor rs = db.rawQuery("select A.Nombre,U.Nombre, AL.NOMBRE," +
								"Cantidad, LP.Nombre, PrecioUnitario," +
								"PorcentajeDescuento,I.Nombre "
								+ "from TB_FACTURA_DETALLE D JOIN TB_ARTICULO A " 
								+ "ON D.Articulo = A.Codigo JOIN TB_UNIDAD_MEDIDA U " 
								+ "ON D.UnidadMedida = U.Nombre JOIN TB_ALMACEN AL "
								+ "ON D.Almacen = AL.CODIGO LEFT JOIN TB_LISTA_PRECIO LP " 
								+ "ON D.ListaPrecio = LP.Codigo JOIN TB_IMPUESTO I " 
								+ "ON D.Impuesto = I.Codigo " 
								+ "WHERE ClaveFactura = '"+idDetalle+"' " 
								+ "AND Articulo = '"+idArticulo+"' " ,
								null);
		
		while (rs.moveToNext()) {

			customListObject = new FormatCustomListView();
			customListObject.setTitulo("Artículo");
			customListObject.setData(rs.getString(0));
			searchResults.add(customListObject);
			
			customListObject = new FormatCustomListView();
			customListObject.setTitulo("Unidad de medida");
			customListObject.setData(rs.getString(1));
			searchResults.add(customListObject);
			
			customListObject = new FormatCustomListView();
			customListObject.setTitulo("Almacén");
			customListObject.setData(rs.getString(2));
			searchResults.add(customListObject);
			
			customListObject = new FormatCustomListView();
			customListObject.setTitulo("Cantidad");
			customListObject.setData(rs.getString(3));
			searchResults.add(customListObject);
			
			customListObject = new FormatCustomListView();
			customListObject.setTitulo("Lista de precios");
			customListObject.setData(rs.getString(4));
			searchResults.add(customListObject);
			
			customListObject = new FormatCustomListView();
			customListObject.setTitulo("Precio unitario");
			customListObject.setData(rs.getString(5));
			searchResults.add(customListObject);
			
			double porcentajeDescuento = Double.parseDouble(rs.getString(6)) * 100;
			customListObject = new FormatCustomListView();
			customListObject.setTitulo("Porcentaje de descuento");
			customListObject.setData(porcentajeDescuento+" %");
			searchResults.add(customListObject);
			
			customListObject = new FormatCustomListView();
			customListObject.setTitulo("Impuesto");
			customListObject.setData(rs.getString(7));
			searchResults.add(customListObject);

		}

		rs.close();
		db.close();
		
		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto, searchResults);      
		lvPrincipal.setAdapter(adapter);
	  	//DynamicHeight.setListViewHeightBasedOnChildren(lvPrincipal);

	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 16908332:			
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
}
