package com.proyecto.ventas;

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
import com.proyecto.utils.Constantes;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.StringDateCast;

public class DetalleVentaTabCabeceraFragment extends Fragment implements OnItemClickListener{

	
	private ListView lv_0;
	private ListView lv_1;
	private ListView lv_2;
	private ArrayList<FormatCustomListView> searchResults_0 = null;
	private ArrayList<FormatCustomListView> searchResults_1 = null;
	private ArrayList<FormatCustomListView> searchResults_2 = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
    private Context contexto;
    private static int icon = R.drawable.ic_keyboard_arrow_right_blue_36dp;
    private View v;
    private String idOrdVen = "";
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.detalle_pedido_cliente_tab_cabe_fragment, container, false);
        
        
        contexto = v.getContext();
        if(DetalleVentaMain.idNroVenta != null){
        	
        	idOrdVen = DetalleVentaMain.idNroVenta;
        	builDataOrd();
        	
        	lv_0.setOnItemClickListener(this);
        	
        }
        
        setHasOptionsMenu(true);
        return v;
        
    }
    
    
    private void builDataOrd(){
		
    	searchResults_0 = new ArrayList<FormatCustomListView>();
    	searchResults_1 = new ArrayList<FormatCustomListView>();
    	searchResults_2 = new ArrayList<FormatCustomListView>();
    	lv_0 = (ListView) v.findViewById(R.id.lvLstCabeDetVenta);
    	lv_1 = (ListView) v.findViewById(R.id.lvLstContDetVenta);
    	lv_2 = (ListView) v.findViewById(R.id.lvLstLogDetVenta);
		
		//TRAER TODO DE SQLITE
    	DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
    	
//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		
		Cursor rs= db.rawQuery("select DISTINCT OV.Clave,SN.NombreRazonSocial, " +
								"(select Nombre from TB_SOCIO_NEGOCIO_CONTACTO where Codigo = OV.Contacto " +
								"AND CodigoSocioNegocio = SN.Codigo) ," +
								"M.NOMBRE," +
								"FechaContable,FechaVencimiento,Comentario, " +
								"SubTotal, Impuesto,Total, OV.SocioNegocio, " +
								" IFNULL(OV.HoraCreacion,''), IFNULL(OV.ModoOffline,'N'), "+
								"IFNULL(OV.Latitud,'') AS Latitud ,"+
								"IFNULL(OV.Longitud, '') AS Longitud,"+
								"IFNULL(OV.RangoCliente, '03') AS RangoCliente "+
								"from TB_ORDEN_VENTA OV left join TB_SOCIO_NEGOCIO_CONTACTO SNC  " +
								"on OV.Contacto = SNC.Codigo left join TB_SOCIO_NEGOCIO SN " +
								"on OV.SocioNegocio = SN.Codigo left join TB_MONEDA M " +
								"on OV.Moneda = M.CODIGO " +
								"WHERE OV.Clave ='"+idOrdVen+"'", null);
		while (rs.moveToNext()) {			
			
			FormatCustomListView sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Número de documento");
		  	sr1.setData(rs.getString(0));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Socio de negocio");
		  	sr1.setIcon(icon);
		  	sr1.setData(rs.getString(1));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Persona de contacto");
		  	sr1.setData(rs.getString(2));
		  	searchResults_0.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Moneda");
		  	sr1.setData(rs.getString(3));
		  	searchResults_0.add(sr1);

		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Fecha contable");
		  	sr1.setData(StringDateCast.castStringtoDate(rs.getString(4)));
		  	searchResults_1.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Fecha de vencimiento");
		  	sr1.setData(StringDateCast.castStringtoDate(rs.getString(5)));
		  	searchResults_1.add(sr1);

		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Comentarios");
		  	sr1.setData(rs.getString(6));
		  	searchResults_1.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Total antes del descuento");
		  	sr1.setData(rs.getString(7));
		  	searchResults_2.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Impuesto");
		  	sr1.setData(rs.getString(8));
		  	searchResults_2.add(sr1);
		  	
		  	sr1 = new FormatCustomListView();
		  	sr1.setTitulo("Total");
		  	sr1.setData(rs.getString(9));
		  	searchResults_2.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Hora Creación");
			sr1.setData(rs.getString(11));
			searchResults_2.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("¿Modo OffLine?");
			sr1.setData(rs.getString(12).equals("N") ? "NO":"SI");
			searchResults_2.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Rango dirección");
			sr1.setData(rs.getString(rs.getColumnIndex("RangoCliente"))
					.equals(Constantes.RANGO_NO_DISPONIBLE) ? "No disponible":
					(rs.getString(rs.getColumnIndex("RangoCliente"))
							.equals(Constantes.DENTRO_DE_RANGO) ? "Dentro de rango" : "Fuera de rango"));
			searchResults_2.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Latitud");
			sr1.setData(rs.getString(rs.getColumnIndex("Latitud")));
			searchResults_2.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Longitud");
			sr1.setData(rs.getString(rs.getColumnIndex("Longitud")));
			searchResults_2.add(sr1);
		  	
		  	DetalleVentaMain.idSocioNegocio = rs.getString(10);
		  	
		}
		
		rs.close();
//		db.close();
		
		
		adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_0);      
		lv_0.setAdapter(adapter);
	  	DynamicHeight.setListViewHeightBasedOnChildren(lv_0);
	  	
	  	adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_1);      
		lv_1.setAdapter(adapter);
	  	DynamicHeight.setListViewHeightBasedOnChildren(lv_1);
	  	
	  	adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_2);      
		lv_2.setAdapter(adapter);
	  	DynamicHeight.setListViewHeightBasedOnChildren(lv_2);
		
		
	}


	
    
    @Override
	public void onItemClick(AdapterView<?> parent, View v, int position,
			long id) {
    	
		if(position == 1){
			Intent myIntent = new Intent(v.getContext(),
					DetalleSocioNegocioMain.class);
			myIntent.putExtra("id", DetalleVentaMain.idSocioNegocio);
			startActivity(myIntent);
		}
	
	}
    
	
	
	
}
