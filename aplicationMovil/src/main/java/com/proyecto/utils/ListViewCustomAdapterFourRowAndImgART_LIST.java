package com.proyecto.utils;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
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
import com.proyecto.bean.ArticuloBean;

public class ListViewCustomAdapterFourRowAndImgART_LIST extends BaseAdapter implements Filterable{
	
	
	private List<ArticuloBean> lista;
	private List<ArticuloBean> listaFiltrada;
	private Context context;
	private LayoutInflater inflater;
    private String mSearchTerm;
    
    
    public ListViewCustomAdapterFourRowAndImgART_LIST(Context context) {
        super();
        this.context = context;
        lista = new ArrayList<>();
		listaFiltrada = new ArrayList<>();
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

	public void clearAndAddAll(List<ArticuloBean> lista){
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
        TextView txtViewTitle;
        TextView txtViewDescription;
        TextView txtViewTitleRight;
        TextView txtViewDescriptionRight;
    }
	

	@SuppressLint("InflateParams") 
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
        								
        holder.txtViewTitle.setText(lista.get(position).getCod());
        holder.txtViewDescription.setText(String.valueOf(lista.get(position).getDesc()));
        holder.imgViewLogo.setImageResource(R.drawable.ic_keyboard_arrow_right_blue_36dp);
        holder.txtViewDescriptionRight.setText(String.valueOf(lista.get(position).getStock()));
        holder.txtViewTitleRight.setText(String.valueOf(lista.get(position).getGrupoArticulo()));
 
        return convertView;
		
		
	}
	
	
	
	@SuppressLint("DefaultLocale") 
	@Override
   	public Filter getFilter() {
    	
   		Filter filter = new Filter() {

               @SuppressWarnings("unchecked")
               @Override
               protected void publishResults(CharSequence constraint, FilterResults results) {

            	   lista = (ArrayList<ArticuloBean>) results.values;
            	   notifyDataSetChanged();

               }

               @Override
               protected FilterResults performFiltering(CharSequence constraint) {

            	   if(!constraint.toString().equals("")){
   					if(mSearchTerm.length() > constraint.toString().length())
   						lista = listaFiltrada;
   				   }
            	   
            	   mSearchTerm = constraint.toString().toLowerCase();
            	   FilterResults results = new FilterResults();
            	   
            	   if(mSearchTerm.equals("")){
            		   
            		   results.count = listaFiltrada.size();
                       results.values = listaFiltrada;

                       return results;
            		   
            	   }else{
            	   
	                   ArrayList<ArticuloBean> FilteredArrayList = new ArrayList<ArticuloBean> ();
	
	                   // perform your search here using the searchConstraint String.
	
	                   for (int i = 0; i < lista.size(); i++) {
	                   	
	                	   ArticuloBean data = lista.get(i);
	                   	
	                       if (data.getCod().toLowerCase().contains(mSearchTerm.toString())
	                    		   || data.getDesc().toLowerCase().contains(mSearchTerm.toString()))  {
	       
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
