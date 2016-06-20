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
import com.proyecto.sociosnegocio.DetalleSocioNegocioMain;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.StringDateCast;

public class DetalleCobranzaTabCabeceraFragment extends Fragment implements OnItemClickListener{

	
	private ListView lv_0;
	private ArrayList<FormatCustomListView> searchResults_0 = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
    private Context contexto;
    private static int icon = R.drawable.ic_keyboard_arrow_right_blue_36dp;
    private View v;
    private String idCobranza = "";
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.detalle_pago_cliente_tab_cabe_fragment, container, false);
        
        
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
    	lv_0 = (ListView) v.findViewById(R.id.lvLstCabeDetPago);
		
		//TRAER TODO DE SQLITE
    	DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
		
//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		
		Cursor rs= db.rawQuery("select DISTINCT Tipo, Numero,S.NombreRazonSocial, " +
								"Comentario, Glosa," +
								"FechaContable, M.NOMBRE, P.SocioNegocio " +
								"FROM TB_PAGO P JOIN TB_SOCIO_NEGOCIO S " +
								"ON P.SocioNegocio = S.Codigo JOIN TB_MONEDA M " +
								"ON P.Moneda = M.CODIGO " +
								"WHERE P.Clave ='"+idCobranza+"'", null);
		while (rs.moveToNext()) {			
			
			FormatCustomListView sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Tipo");
		  	if(rs.getString(0).equals("P"))
		  		sr1.setData("Pendiente");
		  	else
		  		sr1.setData("Aprobado");
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Número");
		  	sr1.setData(rs.getString(1));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Socio de negocio");
		  	sr1.setIcon(icon);
		  	sr1.setData(rs.getString(2));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Comentario");
		  	sr1.setData(rs.getString(3));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Glosa");
		  	sr1.setData(rs.getString(4));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Fecha contable");
		  	sr1.setData(StringDateCast.castStringtoDate(rs.getString(5)));
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Moneda");
		  	sr1.setData(rs.getString(6));
		  	searchResults_0.add(sr1);
		  	
		  	DetalleCobranzaMain.idSocioNegocio = rs.getString(7);
		  	
		}
		
		rs.close();
//		db.close();
		
		
		adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_0);      
		lv_0.setAdapter(adapter);
	  	DynamicHeight.setListViewHeightBasedOnChildren(lv_0);
		
		
	}


	
    
    @Override
	public void onItemClick(AdapterView<?> parent, View v, int position,
			long id) {
    	
		if(position == 2){
			Intent myIntent = new Intent(v.getContext(),
					DetalleSocioNegocioMain.class);
			myIntent.putExtra("id", DetalleCobranzaMain.idSocioNegocio);
			startActivity(myIntent);
		}
	
	}
    
	
	
	
}
