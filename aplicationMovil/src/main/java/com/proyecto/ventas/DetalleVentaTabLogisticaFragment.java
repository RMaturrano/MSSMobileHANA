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

public class DetalleVentaTabLogisticaFragment extends Fragment{

	
	private ListView lv_0;
	private ArrayList<FormatCustomListView> searchResults_0 = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
    private Context contexto;
    private View v;
    private String idOrdVen = "";
	
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.detalle_pedido_cliente_tab_log_fragment, container, false);
        
        
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
    	lv_0 = (ListView) v.findViewById(R.id.lvLstLogDetVenta);
		
		//TRAER TODO DE SQLITE
    	DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
    	
//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		String query = "SELECT IFNULL((SELECT IFNULL(X0.Calle, X0.Referencia) FROM TB_SOCIO_NEGOCIO_DIRECCION X0 " +
				"                           WHERE X0.Codigo = T0.DireccionFiscal AND X0.CodigoSocioNegocio = T0.SocioNegocio),'') " +
				"                           AS DireccionFiscalDescripcion , " +
				"       IFNULL((SELECT IFNULL(X0.Calle, X0.Referencia) FROM TB_SOCIO_NEGOCIO_DIRECCION X0 " +
				"                           WHERE X0.Codigo = T0.DireccionEntrega AND X0.CodigoSocioNegocio = T0.SocioNegocio),'') " +
				"                           AS DireccionEntregaDescripcion " +
				" FROM TB_ORDEN_VENTA T0 where T0.Clave = '"+idOrdVen+"'";
		Cursor rs= db.rawQuery(query, null);
		while (rs.moveToNext()) {		
			
			FormatCustomListView sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Destinatario de entrega");
		  	sr1.setData(rs.getString(1));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Dirección fiscal");
		  	sr1.setData(rs.getString(0));
		  	searchResults_0.add(sr1);
		  	
		}
		
		rs.close();
//		db.close();
		
		
		adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_0);      
		lv_0.setAdapter(adapter);
	  	DynamicHeight.setListViewHeightBasedOnChildren(lv_0);
	  	
		
	}
    
    
	
}
