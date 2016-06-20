package com.proyecto.utils;

import java.util.ArrayList;

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

public class ListViewCustomAdapterImgAndTwoLinesWithFormat extends BaseAdapter implements Filterable{
	
	public ArrayList<FormatCustomListView> lista;
	public ArrayList<FormatCustomListView> listaFiltrada;
//	private AlphabetIndexer mAlphabetIndexer; // Stores the AlphabetIndexer instance
 //   private TextAppearanceSpan highlightTextSpan; // Stores the highlight text appearance style
    private String mSearchTerm;

    public Context context;
    public LayoutInflater inflater;
    ViewHolder holder;
 
    																					//int[] image
    public ListViewCustomAdapterImgAndTwoLinesWithFormat(Context context,ArrayList<FormatCustomListView> lista) {
        super();
 
        this.context = context;
        this.lista = lista;
        listaFiltrada = lista;
 
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        
     //   final String alphabet = context.getString(R.string.alphabet);
     //   mAlphabetIndexer = new AlphabetIndexer(null, ContactsQuery.SORT_KEY, alphabet);
     //   highlightTextSpan = new TextAppearanceSpan(context, R.style.searchTextHiglight);
        
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
    	return lista.size();
  
    }
 
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
    	return lista.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
 
    public static class ViewHolder
    {
        ImageView imgViewLogo;
        TextView txtViewTitle;
        TextView txtViewDescription;
    }
 
    @SuppressLint("InflateParams") 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
 
        
        if(convertView==null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_row_lstsn, null);
 
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtViewTitle);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.txtViewDescription);
            holder.imgViewLogo = (ImageView) convertView.findViewById(R.id.imgViewLogo);
 
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();
        								
        holder.txtViewTitle.setText(lista.get(position).getTitulo());
        holder.txtViewDescription.setText(lista.get(position).getData());
        holder.imgViewLogo.setImageResource(lista.get(position).getIcon());
 
        return convertView;
    }
    
    
    @Override
   	public Filter getFilter() {
    	
   		Filter filter = new Filter() {

               @SuppressWarnings("unchecked")
               @Override
               protected void publishResults(CharSequence constraint, FilterResults results) {

            	   lista = listaFiltrada;
            	   lista = (ArrayList<FormatCustomListView>) results.values;

            	   notifyDataSetChanged();
  
                   
               }

               @SuppressLint("DefaultLocale") 
               @Override
               protected FilterResults performFiltering(CharSequence constraint) {

            	   mSearchTerm = constraint.toString().toLowerCase();
            	   FilterResults results = new FilterResults();
            	   
            	   if(mSearchTerm.equals("")){
            		   
            		   results.count = listaFiltrada.size();
                       results.values = listaFiltrada;

                       return results;
            		   
            	   }else{
            	   
	                   ArrayList<FormatCustomListView> FilteredArrayList = new ArrayList<FormatCustomListView> ();
	
	                   // perform your search here using the searchConstraint String.
	
	                   for (int i = 0; i < lista.size(); i++) {
	                   	
	                   	FormatCustomListView data = lista.get(i);
	                   //	final int startIndex = indexOfSearchQuery(data.getData());
	                   	
	                       if (data.getData().toLowerCase().startsWith(mSearchTerm.toString())
	                    		   ||data.getTitulo().toLowerCase().startsWith(mSearchTerm))  {
	                    	   
	                    	 //  final SpannableString highlightedName = new SpannableString(data.getData());
	                    	 //  highlightedName.setSpan(highlightTextSpan, startIndex, startIndex + mSearchTerm.length(), 0);
	                    	 //  holder.txtViewDescription.setText(highlightedName);
	                    	   
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
