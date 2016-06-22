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

public class DetalleFacturaTabCabeceraFragment extends Fragment implements OnItemClickListener{

	
	private ListView lv_0;
	private ArrayList<FormatCustomListView> searchResults_0 = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
    private Context contexto;
    private static int icon = R.drawable.ic_keyboard_arrow_right_blue_36dp;
    private View v;
    private String idFactura = "";
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.detalle_factura_cliente_tab_cabe_fragment, container, false);
        
        
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
    	lv_0 = (ListView) v.findViewById(R.id.lvLstCabeDetFactura);
		
		//TRAER TODO DE SQLITE
    	DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
    	
//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		
		Cursor rs= db.rawQuery("select DISTINCT Tipo, Numero,Referencia,S.NombreRazonSocial, " +
								"P.Nombre, Contacto,M.NOMBRE, EmpleadoVenta, Comentario, " +
								"FechaContable,FechaVencimiento, SubTotal ,Descuento,Impuesto, Total,Saldo, " +
								"F.SocioNegocio " +
								"FROM TB_FACTURA F LEFT JOIN TB_SOCIO_NEGOCIO S " +
								"ON F.SocioNegocio = S.Codigo LEFT JOIN TB_MONEDA M " +
								"ON F.Moneda = M.CODIGO LEFT JOIN TB_LISTA_PRECIO P " +
								"ON F.ListaPrecio = P.Codigo " +
								"WHERE F.Clave ='"+idFactura+"'", null);
		while (rs.moveToNext()) {			
			
			FormatCustomListView sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Tipo");
		  	if(rs.getString(0).equals("A"))
		  		sr1.setData("Abierta");
		  	else
		  		sr1.setData(rs.getString(0));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Número");
		  	sr1.setData(rs.getString(1));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Referencia");
		  	sr1.setData(rs.getString(2));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Socio de negocio");
		  	sr1.setIcon(icon);
		  	sr1.setData(rs.getString(3));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Lista de precio");
		  	sr1.setData(rs.getString(4));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Contacto");
		  	sr1.setData(rs.getString(5));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Moneda");
		  	sr1.setData(rs.getString(6));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Comentario");
		  	sr1.setData(rs.getString(8));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Fecha contable");
		  	sr1.setData(StringDateCast.castStringtoDate(rs.getString(9)));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Fecha de vencimiento");
		  	sr1.setData(StringDateCast.castStringtoDate(rs.getString(10)));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Sub Total");
		  	sr1.setData(rs.getString(11));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Descuento");
		  	sr1.setData(rs.getString(12));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Impuesto");
		  	sr1.setData(rs.getString(13));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Total");
		  	sr1.setData(rs.getString(14));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Saldo");
		  	sr1.setData(rs.getString(15));
		  	searchResults_0.add(sr1);
		  	
		  	DetalleFacturaMain.idSocioNegocio = rs.getString(16);
		  	
		}
		
		rs.close();
//		db.close();
		
		
		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto, searchResults_0);
		lv_0.setAdapter(adapter);
	  	DynamicHeight.setListViewHeightBasedOnChildren(lv_0);
		
		
	}


	
    
    @Override
	public void onItemClick(AdapterView<?> parent, View v, int position,
			long id) {
    	
		if(position == 3){
			Intent myIntent = new Intent(v.getContext(),
					DetalleSocioNegocioMain.class);
			myIntent.putExtra("id", DetalleFacturaMain.idSocioNegocio);
			startActivity(myIntent);
		}
	
	}
    
	
	
	
}
