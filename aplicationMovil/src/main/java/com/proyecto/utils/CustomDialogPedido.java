package com.proyecto.utils;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.DialogBean;
import com.proyecto.bean.OrdenVentaBean;
import com.proyecto.conectividad.Connectivity;
import com.proyecto.database.Insert;
import com.proyecto.database.Select;
import com.proyecto.ws.InvocaWS;

public class CustomDialogPedido {

	private Context contexto;
	private String res = "";
	private OrdenVentaBean ov = null;
	
	@SuppressLint("InflateParams") 
	public Dialog CreateGroupDialog(final Context context, final String type, final String key,
			boolean crear, final boolean actualizar, final boolean rechazar, final boolean aprobar)
    {
		contexto = context;
		
		 final boolean wifi = Connectivity.isConnectedWifi(contexto);
	     final boolean movil = Connectivity.isConnectedMobile(contexto);
	     final boolean isConnectionFast = Connectivity.isConnectedFast(contexto);
		
		ArrayList<DialogBean> elements = new ArrayList<DialogBean>();
		DialogBean b = new DialogBean();
		
		b = new DialogBean();
		b.setDescription("Actualizar borrador");
		b.setImage(R.drawable.ic_mode_edit_silver_24dp);
		elements.add(b);

		b = new DialogBean();
		b.setDescription("Rechazar borrador");
		b.setImage(R.drawable.ic_delete_forever_silver_24dp);
		elements.add(b);
		
		b = new DialogBean();
		b.setDescription("Aprobar borrador");
		b.setImage(R.drawable.ic_file_document);
		elements.add(b);
		
		LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.custom_dialog, null);
        
        final Dialog dialog = new Dialog(context);
        
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.dialog_toolbar);
        toolbar.setTitle("Acciones");
        
        ListView listView = (ListView) view.findViewById(R.id.group_listview);
        ListViewCustomAdapterImgAndLine customAdapter = new ListViewCustomAdapterImgAndLine(context, elements);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				if(position == 0){
//					
//					Select select = new Select(context);
//					ov = new OrdenVentaBean();
//					ov = select.obtenerOrdenVenta(key);
//					ov.setTransaccionMovil("1");
//					select.close();
//					
//					 if(wifi || movil && isConnectionFast){
//				        	
//				    	 new TareaActualizarOrd().execute();	
//				    	 Toast.makeText(contexto, "Preparando", Toast.LENGTH_SHORT).show();
//				    	 dialog.dismiss();
//				    	 
//				     }else{
//				    	 Toast.makeText(contexto, "No está conectado a ninguna red de datos", Toast.LENGTH_SHORT).show();
//				    	 dialog.dismiss();
//				     }
//					
//				}else 
				if(position == 0){
					
					if(actualizar){
						
						Select select = new Select(context);
						ov = new OrdenVentaBean();
						ov = select.obtenerOrdenVenta(key);
						ov.setTransaccionMovil("2");
						select.close();
						
						 if(wifi || movil && isConnectionFast){
					        	
					    	 new TareaActualizarOrd().execute();	
					    	 Toast.makeText(contexto, "Preparando", Toast.LENGTH_SHORT).show();
					    	 dialog.dismiss();
					    	 
					     }else{
					    	 Toast.makeText(contexto, "No está conectado a ninguna red de datos", Toast.LENGTH_SHORT).show();
					    	 dialog.dismiss();
					     }
					}else{
						 Toast.makeText(contexto, "No tiene permisos para realizar esta acción", Toast.LENGTH_SHORT).show();
					}
					
				}else if(position == 1){
					if(type.equalsIgnoreCase("ov")){
						
						if(rechazar){
							
							Select select = new Select(context);
							ov = new OrdenVentaBean();
							ov = select.obtenerOrdenVenta(key);
							ov.setTransaccionMovil("3");
							select.close();
							
							 if(wifi || movil && isConnectionFast){
						        	
						    	 new TareaActualizarOrd().execute();	
						    	 Toast.makeText(contexto, "Preparando", Toast.LENGTH_SHORT).show();
						    	 dialog.dismiss();
						    	 
						     }else{
						    	 Toast.makeText(contexto, "No está conectado a ninguna red de datos", Toast.LENGTH_SHORT).show();
						    	 dialog.dismiss();
						     }
						}else{
							 Toast.makeText(contexto, "No tiene permisos para realizar esta acción", Toast.LENGTH_SHORT).show();
						}
						
					}
				}else if(position == 2){
					if(type.equalsIgnoreCase("ov")){
						
						if(aprobar){
							Select select = new Select(context);
							ov = new OrdenVentaBean();
							ov = select.obtenerOrdenVenta(key);
							ov.setTransaccionMovil("4");
							select.close();
							
							 if(wifi || movil && isConnectionFast){
						        	
						    	 new TareaActualizarOrd().execute();	
						    	 Toast.makeText(contexto, "Preparando", Toast.LENGTH_SHORT).show();
						    	 dialog.dismiss();
						    	 
						     }else{
						    	 Toast.makeText(contexto, "No está conectado a ninguna red de datos", Toast.LENGTH_SHORT).show();
						    	 dialog.dismiss();
						     }
						}else{
							 Toast.makeText(contexto, "No tiene permisos para realizar esta acción", Toast.LENGTH_SHORT).show();
						}
						
					}
				}
			}
        	
        });

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        
        return dialog;
    }
	
	
	;
	private class TareaActualizarOrd extends AsyncTask<String, Void, Object>{
		
		@Override
		protected Object doInBackground(String... arg0) {
			
			InvocaWS ws= new InvocaWS(contexto);

				res = ws.createOrder(ov, ov.getDetalles());
	
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {

			if (res == null || res.equalsIgnoreCase("anytype{}")) {

				Insert insert = new Insert(contexto);
				insert.updateEstadoOrdenVenta(ov.getClaveMovil());
				insert.updateEstadoTransaccionOrdenVenta(ov.getClaveMovil(), 
						Integer.parseInt(ov.getTransaccionMovil()));
				insert.close();

				Toast.makeText(contexto,
						"Enviado al servidor",
						Toast.LENGTH_LONG).show();

			} else {

				Toast.makeText(contexto, res, Toast.LENGTH_LONG).show();

			}
			

		}	
			
	}  
	
}
