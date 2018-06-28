package com.proyecto.inventario;

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
import android.widget.ListView;

import com.proyect.movil.R;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;

public class DetalleArticuloTabPreciosFragment extends Fragment{

	private ListView lv_0;
	private ArrayList<FormatCustomListView> searchResults_0 = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
    private Context contexto;
    private View v;
    private String idArticulo = "";
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.detalle_articulo_tab_precios_fragment, container, false);
        
        
        contexto = v.getContext();
        Intent intent = getActivity().getIntent();
        Bundle args = intent.getExtras();
        
        if(args != null){
        	idArticulo = args.getString("id");
        	builDataArticulo();
        }
        
        setHasOptionsMenu(true);
        return v;
        
    }
    
    
    private void builDataArticulo(){
		
    	searchResults_0 = new ArrayList<FormatCustomListView>();
    	lv_0 = (ListView) v.findViewById(R.id.lvLstPreDetArticulo);
		
		//TRAER TODO DE SQLITE
    	DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
//    	
//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		Cursor rs= db.rawQuery("select " +
									"L.Nombre, " +
									"PrecioVenta, " +
									"IFNULL(M.NOMBRE,'#') " +
								"FROM TB_PRECIO P JOIN TB_LISTA_PRECIO L " +
								"ON P.CodigoLista = L.Codigo LEFT JOIN TB_MONEDA M " +
								"ON P.Moneda = M.CODIGO " +
								"WHERE Articulo ='"+idArticulo+"' " +
								" AND P.CodigoLista IN(SELECT X0.ListaPrecio from TB_SOCIO_NEGOCIO X0)", null);
		while (rs.moveToNext()) {		
			
			FormatCustomListView sr1 = new FormatCustomListView();
		  	sr1.setTitulo(rs.getString(0) + " - "+ rs.getString(2));
		  	sr1.setData(rs.getString(1));
		  	searchResults_0.add(sr1);
		}
		
		rs.close();
//		db.close();
		
		
		adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_0);      
		lv_0.setAdapter(adapter);
	  	DynamicHeight.setListViewHeightBasedOnChildren(lv_0);
		
		
	}

	
}
