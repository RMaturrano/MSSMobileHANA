package com.proyecto.cobranza;

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

public class DetalleFacturaTabLogisticaFragment extends Fragment{

	private ListView lv_0;
	private ArrayList<FormatCustomListView> searchResults_0 = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
    private Context contexto;
    private View v;
    private String idFactura = "";
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.detalle_factura_cliente_tab_log_fragment, container, false);
        
        
        contexto = v.getContext();
        if(DetalleFacturaMain.idNroFactura != null){
        	
        	idFactura = DetalleFacturaMain.idNroFactura;
        	builDataFactura();
        	
        }
        
        setHasOptionsMenu(true);
        return v;
        
    }
    
    
    private void builDataFactura(){
		
    	searchResults_0 = new ArrayList<FormatCustomListView>();
    	lv_0 = (ListView) v.findViewById(R.id.lvLstLogDetFactura);
		
		//TRAER TODO DE SQLITE
    	DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
    	
//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		
		Cursor rs= db.rawQuery("select DireccionFiscal, DireccionEntrega," +
								"C.NOMBRE, I.Nombre " +
								"FROM TB_FACTURA F LEFT JOIN TB_CONDICION_PAGO C " +
								"ON F.CondicionPago = C.CODIGO LEFT JOIN TB_INDICADOR I " +
								"ON F.Indicador = I.Codigo " +
								"WHERE Clave ='"+idFactura+"'", null);
		while (rs.moveToNext()) {		
			
			FormatCustomListView sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Dirección Fiscal");
		  	sr1.setData(rs.getString(0));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Dirección Entrega");
		  	sr1.setData(rs.getString(1));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Condición de pago");
		  	sr1.setData(rs.getString(2));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Indicador");
		  	sr1.setData(rs.getString(3));
		  	searchResults_0.add(sr1);
		  	
		}
		
		rs.close();
//		db.close();
		
		
		adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_0);      
		lv_0.setAdapter(adapter);
	  	DynamicHeight.setListViewHeightBasedOnChildren(lv_0);
		
		
	}

	
}
