package com.proyecto.cobranza;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.proyect.movil.R;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;

public class DetalleCobranzaTabContenidoFragment extends Fragment implements  OnItemClickListener{

	private ListView lv_0;
	private ArrayList<FormatCustomListView> searchResults_0 = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
    private Context contexto;
    private static int icon = R.drawable.ic_keyboard_arrow_right_blue_36dp;
    private View v;
    private String idCobranza = "";
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.detalle_pago_cliente_tab_cont_fragment, container, false);
        
        
        contexto = v.getContext();
        if(DetalleCobranzaMain.idNroCobranza != null){
        	
        	idCobranza = DetalleCobranzaMain.idNroCobranza;
        	builDataOrd();
        	lv_0.setOnItemClickListener(this);
        	
        }
        
        setHasOptionsMenu(true);
        return v;
        
    }
    
    
    private void builDataOrd(){
		
    	searchResults_0 = new ArrayList<FormatCustomListView>();
    	lv_0 = (ListView) v.findViewById(R.id.lvLstContDetPago);
		
		//TRAER TODO DE SQLITE
    	DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
    	
//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		
		Cursor rs= db.rawQuery("select FacturaCliente, Importe,F.Referencia FROM TB_PAGO_DETALLE P " +
								"LEFT JOIN TB_FACTURA F " +
								"ON P.FacturaCliente = F.Clave " +
								"WHERE ClavePago ='"+idCobranza+"'", null);
		while (rs.moveToNext()) {		
			
			FormatCustomListView sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Factura: " + rs.getString(2));
		  	sr1.setData(rs.getString(1));
		  	sr1.setIcon(icon);
		  	sr1.setExtra(rs.getString(0));
		  	searchResults_0.add(sr1);
	
		}
		
		rs.close();
//		db.close();
		
		
		adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_0);      
		lv_0.setAdapter(adapter);
	  	DynamicHeight.setListViewHeightBasedOnChildren(lv_0);
		
		
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		FormatCustomListView beanSend = searchResults_0.get(position);
		
		Intent myIntent = new Intent(contexto,
				DetalleFacturaMain.class);
		myIntent.putExtra("id", beanSend.getExtra());
		startActivity(myIntent);
		
	}
	
	
}
