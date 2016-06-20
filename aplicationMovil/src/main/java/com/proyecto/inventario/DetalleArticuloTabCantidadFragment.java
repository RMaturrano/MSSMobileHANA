package com.proyecto.inventario;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.proyect.movil.R;
import com.proyecto.bean.AlmacenBean;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.database.Select;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.Variables;

public class DetalleArticuloTabCantidadFragment extends Fragment{

	private ListView lv_0;
	private ArrayList<FormatCustomListView> searchResults_0 = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
    private Context contexto;
    private View v;
    private String idArticulo = "";
    private int iconId = Variables.idIconRightBlue36dp;
    private ArrayList<AlmacenBean> listaAlmacen = null;
    private AlmacenBean almacenSel = null;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.detalle_articulo_tab_cant_fragment, container, false);
        
        
        contexto = v.getContext();
        Intent intent = getActivity().getIntent();
        Bundle args = intent.getExtras();
        cargarListas();
        
        if(args != null){
        	idArticulo = args.getString("id");
        	llenarListaInicial();
        }
        
        lv_0.setOnItemClickListener(itemClick);
        
        setHasOptionsMenu(true);
        return v;
        
    }
    
    private OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			if(position == 0){
				//Spinner
				final Spinner spn = new Spinner(contexto);
				
				ArrayAdapter<AlmacenBean> adapter = new ArrayAdapter<AlmacenBean>(contexto, 
						android.R.layout.simple_list_item_1,
						listaAlmacen);
				spn.setAdapter(adapter);
				spn.setOnItemSelectedListener(new OnItemSelectedListener() {
					
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int pos, long arg3) {
						almacenSel = (AlmacenBean) parent.getItemAtPosition(pos);
					}

					@Override
					public void onNothingSelected(
							AdapterView<?> arg0) {
					}
				});
				
				
				AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
				alert.setTitle("Almacenes");
				alert.setView(spn);
				alert.setCancelable(true);
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
						llenarListaInicial();
				  }
				});


				alert.show();
			}
			
		}
	};
    
    private void cargarListas(){
		
		listaAlmacen = new ArrayList<>();
		
		Select select = new Select(contexto);
		listaAlmacen = select.listaAlmacen();
		
		if(listaAlmacen.size()>1)
			almacenSel = listaAlmacen.get(1);
		else
			almacenSel = listaAlmacen.get(0);
		
		select.close();
		
	}
    
	
    private void llenarListaInicial() {

    	searchResults_0 = new ArrayList<FormatCustomListView>();
    	lv_0 = (ListView) v.findViewById(R.id.lvLstCantDetArticulo);
		
    	FormatCustomListView sr = new FormatCustomListView();
		sr.setTitulo("Almacén");
		sr.setIcon(iconId);
		sr.setData(almacenSel.getDescripcion());
		searchResults_0.add(sr);
		
		DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();

//		MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		Cursor rs= db.rawQuery("select " +
				"ALMACEN, " +
				"IFNULL(CAST(STOCK AS NUMERIC),0), " +
				"IFNULL(CAST(COMPROMETIDO AS NUMERIC),0)," +
				"IFNULL(CAST(SOLICITADO AS NUMERIC),0), " +
				"IFNULL(CAST(DISPONIBLE AS NUMERIC),0) " +
			"FROM TB_CANTIDAD " +
			"WHERE ARTICULO ='"+idArticulo+"' " +
					"AND ALMACEN = '"+almacenSel.getCodigo()+"'", null);
		
		while (rs.moveToNext()) {		
			
			FormatCustomListView sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Stock");
		  	sr1.setData(rs.getString(1));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Comprometido");
		  	sr1.setData(rs.getString(2));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Solicitado");
		  	sr1.setData(rs.getString(3));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Disponible");
		  	sr1.setData(rs.getString(4));
		  	searchResults_0.add(sr1);
		  	
		}
		
		rs.close();
//		db.close();
		
		
		adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_0);      
		lv_0.setAdapter(adapter);
	  	DynamicHeight.setListViewHeightBasedOnChildren(lv_0);
		
		
	}
    
}
