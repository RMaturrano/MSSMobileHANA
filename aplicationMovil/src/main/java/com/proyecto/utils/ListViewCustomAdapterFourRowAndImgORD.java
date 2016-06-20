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
import com.proyecto.bean.OrdenVentaBean;

public class ListViewCustomAdapterFourRowAndImgORD extends BaseAdapter implements Filterable{
	
	
	public ArrayList<OrdenVentaBean> lista;
	public ArrayList<OrdenVentaBean> listaFiltrada;
    public Context context;
    public LayoutInflater inflater;
    private String mSearchTerm;
    
    
    public ListViewCustomAdapterFourRowAndImgORD(Context context) {
        super();
        this.context = context;
        lista = new ArrayList<>();
        listaFiltrada = new ArrayList<>();
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void clearAndAddAll(ArrayList<OrdenVentaBean> lista){
        this.lista.clear();
        this.lista.addAll(lista);
        listaFiltrada.clear();
        listaFiltrada.addAll(lista);
        notifyDataSetChanged();
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
        ImageView imgViewLogo1;
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
            convertView = inflater.inflate(R.layout.custom_four_row_and_img_ord, null);
 
            holder.imgViewLogo1 = (ImageView) convertView.findViewById(R.id.imgViewLogo1);
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtViewTitle);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.txtViewDescription);
            holder.imgViewLogo = (ImageView) convertView.findViewById(R.id.imgViewLogo);
            holder.txtViewTitleRight = (TextView) convertView.findViewById(R.id.txtViewTitleRight);
            holder.txtViewDescriptionRight = (TextView) convertView.findViewById(R.id.txtViewDescriptionRight);
 
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();
        
        holder.imgViewLogo1.setImageResource(lista.get(position).getUtilIcon2());
        holder.txtViewTitle.setText(lista.get(position).getNomSN());
        holder.txtViewDescription.setText(String.valueOf(lista.get(position).getTotGeneral()));
        holder.imgViewLogo.setImageResource(lista.get(position).getUtilIcon());
        if(lista.get(position).getNroDocOrdV().equals(lista.get(position).getClaveMovil()))
        	holder.txtViewTitleRight.setText(String.valueOf(lista.get(position).getClaveMovil()));
        else
        	holder.txtViewTitleRight.setText(String.valueOf(lista.get(position).getNroDocOrdV()));
        holder.txtViewDescriptionRight.setText(String.valueOf(lista.get(position).getFecDoc()));
 
        return convertView;
		
		
	}
	
	
	
	@Override
   	public Filter getFilter() {
    	
   		Filter filter = new Filter() {

               @SuppressWarnings("unchecked")
               @Override
               protected void publishResults(CharSequence constraint, FilterResults results) {

            	   lista = listaFiltrada;
            	   lista = (ArrayList<OrdenVentaBean>) results.values;

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
            	   
	                   ArrayList<OrdenVentaBean> FilteredArrayList = new ArrayList<OrdenVentaBean> ();
	
	                   // perform your search here using the searchConstraint String.
	
	                   for (int i = 0; i < lista.size(); i++) {
	                   	
	                   	OrdenVentaBean data = lista.get(i);
	                   	
	                       if (data.getNomSN().toLowerCase().startsWith(mSearchTerm.toString())
	                    		   ||data.getNroDocOrdV().toLowerCase().startsWith(mSearchTerm))  {
	       
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
