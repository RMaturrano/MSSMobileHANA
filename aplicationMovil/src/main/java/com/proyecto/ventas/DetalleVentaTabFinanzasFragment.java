package com.proyecto.ventas;

import java.util.ArrayList;

import android.content.Context;
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

public class DetalleVentaTabFinanzasFragment extends Fragment{

	
	private ListView lv_0;
	private ArrayList<FormatCustomListView> searchResults_0;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
    private Context contexto;
    private View v;
    private String idOrdVen = "";
	
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.detalle_pedido_cliente_tab_finanza_fragment, container, false);
        
        
        contexto = v.getContext();
        if(DetalleVentaMain.idNroVenta != null){
        	
        	
        	idOrdVen = DetalleVentaMain.idNroVenta;
        	
        	builDataOrd();
        	
        }
        
        setHasOptionsMenu(true);
        return v;
        
    }
    
    
    private void builDataOrd(){
		
    	searchResults_0 = new ArrayList<FormatCustomListView>();
    	lv_0 = (ListView) v.findViewById(R.id.lvLstFinDetVenta);
		
		//TRAER TODO DE SQLITE
    	DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
    	
//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		
		Cursor rs= db.rawQuery("select CP.NOMBRE, I.Nombre, LP.Nombre " +
								"from TB_ORDEN_VENTA O JOIN TB_CONDICION_PAGO CP " +
								"ON O.CondicionPago = CP.CODIGO JOIN TB_INDICADOR I " +
								"ON O.Indicador = I.Codigo JOIN TB_LISTA_PRECIO LP " +
								"ON O.ListaPrecio = LP.Codigo " +
								"WHERE Clave ='"+idOrdVen+"'", null);
		while (rs.moveToNext()) {		
			
			FormatCustomListView sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Condición de pago");
		  	sr1.setData(rs.getString(0));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Indicador");
		  	sr1.setData(rs.getString(1));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Lista de precio");
		  	sr1.setData(rs.getString(2));
		  	searchResults_0.add(sr1);
		  	
		}
		rs.close();
//		db.close();
		
		
		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto,searchResults_0);      
		lv_0.setAdapter(adapter);
	  	DynamicHeight.setListViewHeightBasedOnChildren(lv_0);
	  	
		
	}
    
    
	
}
