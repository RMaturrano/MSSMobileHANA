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
import com.proyecto.utils.StringDateCast;

public class DetalleCobranzaTabTipoPagoFragment extends Fragment{

	
	private ListView lv_0;
	private ArrayList<FormatCustomListView> searchResults_0 = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
    private Context contexto;
    private View v;
    private String idCobranza = "";
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.detalle_pago_cliente_tab_tipo_pago_fragment, container, false);
        
        
        contexto = v.getContext();
        if(DetalleCobranzaMain.idNroCobranza != null){
        	
        	idCobranza = DetalleCobranzaMain.idNroCobranza;
        	builDataOrd();
        	
        }
        
        setHasOptionsMenu(true);
        return v;
        
    }
    
    
    private void builDataOrd(){
		
    	searchResults_0 = new ArrayList<FormatCustomListView>();
    	lv_0 = (ListView) v.findViewById(R.id.lvLstCabeDetTipoPago);
		
		//TRAER TODO DE SQLITE
    	DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
    	
//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		
		Cursor rs= db.rawQuery("select DISTINCT TipoPago, " +
								"(SELECT Nombre FROM TB_CUENTA WHERE Codigo = TransferenciaCuenta)," +
								"TransferenciaReferencia,TransferenciaImporte, " +
								"(SELECT Nombre FROM TB_CUENTA WHERE Codigo = EfectivoCuenta), " +
								"EfectivoImporte, " +
								"(SELECT Nombre FROM TB_CUENTA WHERE Codigo = ChequeCuenta), " +
								"(SELECT Nombre FROM TB_BANCO WHERE Codigo = ChequeBanco), " +
								"ChequeVencimiento,ChequeImporte,ChequeNumero " +
								"FROM TB_PAGO " +
								"WHERE Clave ='"+idCobranza+"'", null);
		while (rs.moveToNext()) {
			
			FormatCustomListView sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Tipo de pago");
		  		
			if(rs.getString(0).equals("F")){
				
				sr1.setData("Efectivo");
				searchResults_0.add(sr1);
				
				sr1 = new FormatCustomListView();
			  	sr1.setTitulo("Efectivo cuenta");
			  	sr1.setData(rs.getString(4));
			  	searchResults_0.add(sr1);
			  	
			  	sr1 = new FormatCustomListView();
			  	sr1.setTitulo("Efectivo importe");
			  	sr1.setData(rs.getString(5));
			  	searchResults_0.add(sr1);
				
			}else if(rs.getString(0).equalsIgnoreCase("T")){
				
				sr1.setData("Transferencia");
				searchResults_0.add(sr1);
				
				sr1 = new FormatCustomListView();
			  	sr1.setTitulo("Transferencia cuenta");
			  	sr1.setData(rs.getString(1));
			  	searchResults_0.add(sr1);
			  	
			  	sr1 = new FormatCustomListView();
			  	sr1.setTitulo("Transferencia referencia");
			  	sr1.setData(rs.getString(2));
			  	searchResults_0.add(sr1);
			  	
			  	sr1 = new FormatCustomListView();
			  	sr1.setTitulo("Transferencia importe");
			  	sr1.setData(rs.getString(3));
			  	searchResults_0.add(sr1);
			  	
			}else if(rs.getString(0).equalsIgnoreCase("C")){
				
				sr1.setData("Cheque");
				searchResults_0.add(sr1);
				
				sr1 = new FormatCustomListView();
			  	sr1.setTitulo("Cheque cuenta");
			  	sr1.setData(rs.getString(6));
			  	searchResults_0.add(sr1);
			  	
			  	sr1 = new FormatCustomListView();
			  	sr1.setTitulo("Cheque banco");
			  	sr1.setData(rs.getString(7));
			  	searchResults_0.add(sr1);
			  	
			  	sr1 = new FormatCustomListView();
			  	sr1.setTitulo("Cheque vencimiento");
			  	if(!rs.getString(8).equals(""))
			  		sr1.setData(StringDateCast.castStringtoDate(rs.getString(8)));
			  	searchResults_0.add(sr1);
			  	
			  	sr1 = new FormatCustomListView();
			  	sr1.setTitulo("Cheque importe");
			  	sr1.setData(rs.getString(9));
			  	searchResults_0.add(sr1);
			  	
			  	sr1 = new FormatCustomListView();
			  	sr1.setTitulo("Cheque número");
			  	sr1.setData(rs.getString(10));
			  	searchResults_0.add(sr1);
			}

		  	
		}
		
		rs.close();
//		db.close();
		
		
		adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_0);      
		lv_0.setAdapter(adapter);
	  	DynamicHeight.setListViewHeightBasedOnChildren(lv_0);
		
		
	}
	
	
	
}
