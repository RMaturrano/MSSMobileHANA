package com.proyecto.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyect.movil.R;

public class ListViewCustomAdapterTwoLinesAndImgToolbar extends BaseAdapter{
	
	public FormatCustomListView item;

    public Context context;
    public LayoutInflater inflater;
 
    public ListViewCustomAdapterTwoLinesAndImgToolbar(Context context, FormatCustomListView item) {
        super();
 
        this.context = context;
        this.item = item;
 
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 1;
    }
 
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
    	return item;
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
        TextView txtViewBelow;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
 
        ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_row_img_rows_toolbar, null);
 
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtViewTitle);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.txtViewDescription);
            holder.txtViewBelow = (TextView) convertView.findViewById(R.id.txtViewBelow);
            holder.imgViewLogo = (ImageView) convertView.findViewById(R.id.imgViewLogo);
 
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();
        								
        holder.txtViewTitle.setText(item.getTitulo());
        holder.txtViewDescription.setText(item.getData());
        holder.txtViewBelow.setText(item.getExtra());
        holder.imgViewLogo.setImageResource(item.getIcon());
 
        return convertView;
    }
    
	
    
    

}
