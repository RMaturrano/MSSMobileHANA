package com.proyecto.sociosnegocio;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.database.MyDataBase;
import com.proyecto.inventario.ListaArticuloPrecioActivity;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;

public class DetalleSocioNegocioTabCondPago extends Fragment{
	
	
	private View v;
	private ListView lvInfoBasica = null;
	private ArrayList<FormatCustomListView> searchResults = null;
	private Context contexto;
	private String idBP = "";
	private ListViewCustomAdapterTwoLinesAndImg adapter;
	private Button btnVerProductos;
	private String mListaPrecio;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.socio_negocio_detalle_fragment_tab_condpago,
				container, false);

		contexto = v.getContext();
		if (DetalleSocioNegocioMain.idBusinessPartner != null) {

			idBP = DetalleSocioNegocioMain.idBusinessPartner;
			getItemsOfBusinessPartner();

		}

		btnVerProductos = (Button) v.findViewById(R.id.btnVerProductos);
		btnVerProductos.setOnClickListener(verProductosClickListener);

		setHasOptionsMenu(true);
		return v;

	}
	
	
	private void getItemsOfBusinessPartner() {

		searchResults = new ArrayList<FormatCustomListView>();

		lvInfoBasica = (ListView) v.findViewById(R.id.lvEditarSNTabCondPago);

		DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
		
		// TRAER TODO DE SQLITE
//		MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db = cn.getWritableDatabase();

		Cursor rs = db.rawQuery(
				"select  CP.NOMBRE, LP.Nombre, I.Nombre, Z.NOMBRE, IFNULL(BP.ListaPrecio,'') as ListaPrecio "
						+ "from TB_SOCIO_NEGOCIO BP left join TB_CONDICION_PAGO CP "
						+ " on BP.CondicionPago = CP.CODIGO left join TB_LISTA_PRECIO LP" 
						+ " on BP.ListaPrecio = LP.Codigo left join TB_INDICADOR I" 
						+ " on BP.Indicador = I.Codigo left join TB_ZONA Z" 
						+ " on BP.Zona = Z.Codigo "
						+ "WHERE BP.Codigo ='"
						+ idBP + "'", null);

		while (rs.moveToNext()) {
			
			FormatCustomListView sr1 = new FormatCustomListView();
			sr1.setTitulo("Condición de pago");
			sr1.setData(rs.getString(0));
			searchResults.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Lista de precio");
			sr1.setData(rs.getString(1));
			searchResults.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Indicador");
			sr1.setData(rs.getString(2));
			searchResults.add(sr1);
			
			sr1 = new FormatCustomListView();
			sr1.setTitulo("Zona");
			sr1.setData(rs.getString(3));
			searchResults.add(sr1);

			mListaPrecio = rs.getString(rs.getColumnIndex("ListaPrecio"));
		}
		rs.close();
//		db.close();

		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto,
				searchResults);

		lvInfoBasica.setAdapter(adapter);
		DynamicHeight.setListViewHeightBasedOnChildren(lvInfoBasica);

	}

	private View.OnClickListener verProductosClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mListaPrecio != null && !mListaPrecio.equals("")) {
				Intent productos = new Intent(contexto, ListaArticuloPrecioActivity.class);
				productos.putExtra(ListaArticuloPrecioActivity.KEY_PARAM_LISTA_PRECIO, mListaPrecio);
				startActivity(productos);
			}else
				Toast.makeText(contexto, "No se pudo obtener la lista de precio del sn", Toast.LENGTH_LONG).show();
		}
	};

}
