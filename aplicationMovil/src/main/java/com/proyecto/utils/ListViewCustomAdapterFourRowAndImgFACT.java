package com.proyecto.utils;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyect.movil.R;
import com.proyecto.bean.FacturaBean;

public class ListViewCustomAdapterFourRowAndImgFACT extends BaseAdapter implements Filterable{
	
	
	public ArrayList<FacturaBean> lista;
	public ArrayList<FacturaBean> listaFiltrada;
    public Context context;
    public LayoutInflater inflater;
    private String mSearchTerm;
    
    
    public ListViewCustomAdapterFourRowAndImgFACT(Context context,ArrayList<FacturaBean> lista) {
        super();
        this.context = context;
        this.lista = lista;
        listaFiltrada = lista;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
	
	@Override
	public int getCount() {
		return lista.size();
	}

	@Override
	public Object getItem(int position) {
		return lista.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
	public static class ViewHolder
    {
        ImageView imgViewLogo;
        TextView txtViewTitle;
        TextView txtViewDescription;
        TextView txtViewTitleRight;
        TextView txtViewDescriptionRight;
    }
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_four_row_and_img_det_ord, null);
 
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtViewTitle);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.txtViewDescription);
            holder.imgViewLogo = (ImageView) convertView.findViewById(R.id.imgViewLogo);
            holder.txtViewTitleRight = (TextView) convertView.findViewById(R.id.txtViewTitleRight);
            holder.txtViewDescriptionRight = (TextView) convertView.findViewById(R.id.txtViewDescriptionRight);
 
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();
        								
        holder.txtViewTitle.setText(lista.get(position).getReferencia());
        holder.txtViewDescription.setText(String.valueOf(lista.get(position).getFechaContable()));
        holder.imgViewLogo.setImageResource(lista.get(position).getUtilIcon());
        holder.txtViewTitleRight.setText("Saldo: "+ String.valueOf(lista.get(position).getSaldo()));
        holder.txtViewDescriptionRight.setText("Pago: "+ String.valueOf(lista.get(position).getUtilPagoTotal()));
 
        return convertView;
		
		
	}
	
	
	
	@Override
   	public Filter getFilter() {
    	
   		Filter filter = new Filter() {

               @SuppressWarnings("unchecked")
               @Override
               protected void publishResults(CharSequence constraint, FilterResults results) {

            	   lista = listaFiltrada;
            	   lista = (ArrayList<FacturaBean>) results.values;

            	   notifyDataSetChanged();
  
                   
               }

               @Override
               protected FilterResults performFiltering(CharSequence constraint) {

            	   mSearchTerm = constraint.toString().toLowerCase();
            	   FilterResults results = new FilterResults();
            	   
            	   if(mSearchTerm.equals("")){
            		   
            		   results.count = listaFiltrada.size();
                       results.values = listaFiltrada;

                       return results;
            		   
            	   }else{
            	   
	                   ArrayList<FacturaBean> FilteredArrayList = new ArrayList<FacturaBean> ();
	
	                   // perform your search here using the searchConstraint String.
	
	                   for (int i = 0; i < lista.size(); i++) {
	                   	
	                	   FacturaBean data = lista.get(i);
	                   	
	                       if (data.getSocioNegocio().toLowerCase().startsWith(mSearchTerm.toString())
	                    		   ||data.getClave().toLowerCase().startsWith(mSearchTerm))  {
	       
	                       		FilteredArrayList.add(data);
	                       }
	                       
	                   }
	
	                   
	                   results.count = FilteredArrayList.size();
	                   results.values = FilteredArrayList;
	
	                   return results;
            	   }
               }
           };

           return filter;
   		
   		
   	}
	
	

}
