package com.proyecto.movil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyect.movil.R;

public class ListaMenu extends BaseAdapter{

	private final String[] titulos;
	private final Integer[] imageId;
	
	public Context context;
    public LayoutInflater inflater;
 
    														
    public ListaMenu(Context context, String[] title, Integer[] imagenes) {
        super();
 
        this.context = context;
        this.titulos = title;
        this.imageId = imagenes;
 
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public int getCount() {
        return titulos.length;
    }
 
    @Override
    public Object getItem(int position) {
        return null;
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
    
    public static class ViewHolder
    {
        ImageView imgViewLogo;
        TextView txtViewTitle;
    }
    
	
    @SuppressLint("InflateParams") 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       
        ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.lista_menu, null);
 
            holder.imgViewLogo = (ImageView) convertView.findViewById(R.id.img);
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txt);
 
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();
        						
        holder.imgViewLogo.setImageResource(imageId[position]);
        holder.txtViewTitle.setText(titulos[position]);
 
        return convertView;
    }
  
	

}
