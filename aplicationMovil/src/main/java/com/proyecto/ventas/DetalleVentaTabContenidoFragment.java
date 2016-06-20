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
import com.proyecto.bean.OrdenVentaDetalleBean;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.DoubleRound;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.ListViewCustomAdapterFourRowAndImgDET_ORD;

public class DetalleVentaTabContenidoFragment extends Fragment implements  OnItemClickListener{

	private ListView lv_0;
	private ArrayList<OrdenVentaDetalleBean> searchResults_0 = null;
	private OrdenVentaDetalleBean bean = null;
	private ListViewCustomAdapterFourRowAndImgDET_ORD adapter;
    private Context contexto;
    private static int icon = R.drawable.ic_keyboard_arrow_right_blue_36dp;
    private View v;
    private String idOrdVen = "";
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.detalle_pedido_cliente_tab_cont_fragment, container, false);
        
        
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
		
    	searchResults_0 = new ArrayList<OrdenVentaDetalleBean>();
    	lv_0 = (ListView) v.findViewById(R.id.lvLstContDetVenta);
		
		//TRAER TODO DE SQLITE
    	DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
    	
//    	MyDataBase cn = new MyDataBase(contexto, null, null, MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db= cn.getWritableDatabase();
		
		
		Cursor rs= db.rawQuery("select A.Nombre,o.Articulo,Cantidad," +
				"((PrecioUnitario*Cantidad)-(PrecioUnitario * cantidad) * PorcentajeDescuento) as Total " +
								"from TB_ORDEN_VENTA_DETALLE O LEFT JOIN TB_ARTICULO A " +
								"on o.Articulo = A.Codigo " +
								"WHERE O.ClaveMovil ='"+idOrdVen+"'", null);
		while (rs.moveToNext()) {		
			
			bean = new OrdenVentaDetalleBean();
			bean.setDescripcion(rs.getString(0));
			bean.setCodArt(rs.getString(1));
			bean.setCantidad(rs.getDouble(2));		
			bean.setTotal(DoubleRound.round(rs.getDouble(3),2));
			bean.setUtilIcon(icon);
		  	searchResults_0.add(bean);
		  	
		}
		
		rs.close();
//		db.close();
		
		
		adapter = new ListViewCustomAdapterFourRowAndImgDET_ORD( contexto, searchResults_0);      
		lv_0.setAdapter(adapter);
		
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		OrdenVentaDetalleBean beanSend = searchResults_0.get(position);
		
		Intent myIntent = new Intent(contexto,
				DetalleVentaLinea.class);
		myIntent.putExtra("idOv", idOrdVen);
		myIntent.putExtra("idArt", beanSend.getCodArt());
		startActivity(myIntent);
		
	}
	
	
}
