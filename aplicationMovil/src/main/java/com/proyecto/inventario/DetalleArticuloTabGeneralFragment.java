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

public class DetalleArticuloTabGeneralFragment extends Fragment{

	
	private ListView lv_0;
	private ArrayList<FormatCustomListView> searchResults_0 = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
    private Context contexto;
    private View v;
    private String idArticulo = "";
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.detalle_articulo_tab_general_fragment, container, false);
        
        
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
    	lv_0 = (ListView) v.findViewById(R.id.lvLstGenDetArticulo);
		
		//TRAER TODO DE SQLITE
    	DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
    	
//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		
		Cursor rs= db.rawQuery("select DISTINCT " +
								"A.Codigo, " +
								"A.Nombre," +
								"F.NOMBRE," +
								"G.NOMBRE, " +
								"GUM.NOMBRE, " +
								"UM.NOMBRE " +
								"FROM TB_ARTICULO A LEFT JOIN TB_FABRICANTE F " +
								"ON F.CODIGO = A.Fabricante LEFT JOIN TB_GRUPO_ARTICULO G " +
								"ON G.CODIGO = A.GrupoArticulo LEFT JOIN TB_GRUPO_UNIDAD_MEDIDA GUM " +
								"ON GUM.Codigo = A.GrupoUnidadMedida LEFT JOIN TB_UNIDAD_MEDIDA UM " +
								"ON UM.Codigo = A.UnidadMedidaVenta " +
								"WHERE A.Codigo ='"+idArticulo+"'", null);
		while (rs.moveToNext()) {			
			
			FormatCustomListView sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Código");
		  	sr1.setData(rs.getString(0));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Descripcion");
		  	sr1.setData(rs.getString(1));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Fabricante");
		  	sr1.setData(rs.getString(2));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Grupo artículo");
		  	sr1.setData(rs.getString(3));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Grupo unidad de medida");
		  	sr1.setData(rs.getString(4));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Unidad medida de venta");
		  	sr1.setData(rs.getString(5));
		  	searchResults_0.add(sr1);
		  	
		}
		
		rs.close();
//		db.close();
		
		
		adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_0);      
		lv_0.setAdapter(adapter);
	  	DynamicHeight.setListViewHeightBasedOnChildren(lv_0);
		
		
	}

	
}
