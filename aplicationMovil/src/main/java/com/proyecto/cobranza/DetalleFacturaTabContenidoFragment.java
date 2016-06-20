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
import com.proyecto.facturas.DetalleFacturaLinea;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;

public class DetalleFacturaTabContenidoFragment extends Fragment implements  OnItemClickListener{

	private ListView lv_0;
	private ArrayList<FormatCustomListView> searchResults_0 = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
    private Context contexto;
    private static int icon = R.drawable.ic_keyboard_arrow_right_blue_36dp;
    private View v;
    private String idFactura = "";
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.detalle_factura_cliente_tab_cont_fragment, container, false);
        
        
        contexto = v.getContext();
        if(DetalleFacturaMain.idNroFactura != null){
        	
        	idFactura = DetalleFacturaMain.idNroFactura;
        	builDataFactura();
        	lv_0.setOnItemClickListener(this);
        	
        }
        
        setHasOptionsMenu(true);
        return v;
        
    }
    
    
    private void builDataFactura(){
		
    	searchResults_0 = new ArrayList<FormatCustomListView>();
    	lv_0 = (ListView) v.findViewById(R.id.lvLstContDetFactura);
		
		//TRAER TODO DE SQLITE
    	DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
    	
//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		
		Cursor rs= db.rawQuery("select A.Nombre, Cantidad,Articulo " +
								"FROM TB_FACTURA_DETALLE D LEFT JOIN TB_ARTICULO A " +
								"ON D.Articulo = A.Codigo " +
								"WHERE ClaveFactura ='"+idFactura+"'", null);
		while (rs.moveToNext()) {		
			
			FormatCustomListView sr1 = new FormatCustomListView();
		  	sr1.setTitulo(rs.getString(0));
		  	sr1.setData(rs.getString(1));
		  	sr1.setExtra(rs.getString(2));
		  	sr1.setIcon(icon);
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
				DetalleFacturaLinea.class);
		myIntent.putExtra("idFact", idFactura);
		myIntent.putExtra("idArt", beanSend.getExtra());
		startActivity(myIntent);
		
	}
	
	
}
