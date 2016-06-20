package com.proyecto.utils;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyect.movil.R;

public class ListViewCustomAdapterTwoLinesAndImg extends BaseAdapter{
	
	public ArrayList<FormatCustomListView> lista;

    public Context context;
    public LayoutInflater inflater;
 
    																					//int[] image
    public ListViewCustomAdapterTwoLinesAndImg(Context context,ArrayList<FormatCustomListView> lista) {
        super();
 
        this.context = context;
        this.lista = lista;
 
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
 
        ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_row_tworow_img, null);
 
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
    
	
    
    

}
