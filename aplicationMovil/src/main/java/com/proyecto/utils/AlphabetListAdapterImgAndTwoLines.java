package com.proyecto.utils;

import java.util.ArrayList;
import java.util.LinkedList;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.proyect.movil.R;

public class AlphabetListAdapterImgAndTwoLines extends BaseAdapter implements
		Filterable {

	public static abstract class Row {}

	private String mSearchTerm;
	private ClientFilter mClientFilter;
	public Context context;
	
	public static final class Section extends Row {
		public final String letter;

		public Section(String letter) {
			this.letter = letter;
		}
	}
	
	
	public static final class Item extends Row {
		public final FormatCustomListView element;

		public Item(FormatCustomListView element) {
			this.element = element;
		}
	}
	
	private List<Row> rows;
	private List<Row> rowsFilterBase;
	public void setRows(List<Row> rows) {
		this.rows = rows;
		rowsFilterBase = rows;
	}
	

	@Override
	public int getCount() {
		if(rows != null)
			return rows.size();
		else return 0;
	}

	@Override
	public Object getItem(int position) {
		return rows.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		if (getItem(position) instanceof Section) {
			return 1;
		} else {
			return 0;
		}
	}


	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (getItemViewType(position) == 0) { // Item
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) parent.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = (RelativeLayout) inflater.inflate(R.layout.lv_buscar_articulo,
						parent, false);
			}
			
			Item item = (Item) getItem(position);
			TextView txtViewDescription = (TextView) view.findViewById(R.id.txtViewDescription);
			TextView txtViewTitle = (TextView) view.findViewById(R.id.txtViewTitle);
			TextView txtViewGrupo = (TextView) view.findViewById(R.id.txtViewGrupo);
			ImageView imgViewLogo = (ImageView) view.findViewById(R.id.imgViewLogo);
			
			
				txtViewDescription.setText(item.element.getData());
				txtViewTitle.setText(item.element.getTitulo());
				imgViewLogo.setImageResource(item.element.getIcon());
				txtViewGrupo.setText(item.element.getGrupo());
			
			
		}else{ // Section
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) parent.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = (LinearLayout) inflater.inflate(R.layout.row_section,
						parent, false);
			}

			Section section = (Section) getItem(position);
			TextView textView = (TextView) view.findViewById(R.id.textView1);
			textView.setText(section.letter);
		}

		return view;
	}

	private class ClientFilter extends Filter{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			if(!constraint.toString().trim().equals("")){
				if(mSearchTerm.length() > constraint.toString().length())
					rows = rowsFilterBase;
			}

			mSearchTerm = constraint.toString().toLowerCase();
			FilterResults results = new FilterResults();

			if (mSearchTerm.equals("") || mSearchTerm.length() == 0) {

				results.count = rowsFilterBase.size();
				results.values = rowsFilterBase;

				return results;

			} else {

				List<Row> rowsFilterFinal = new ArrayList<Row>();

				if(rows != null && rows.size() > 0){
					for (int i = 0; i < rows.size(); i++) {

						if(getItemViewType(i)!= 1){

							Item item = (Item) getItem(i);

							if(item.element != null){
								if (item.element.getData().toLowerCase()
										.contains(mSearchTerm.toString())
										|| item.element.getTitulo().toLowerCase()
										.contains(mSearchTerm)) {
									rowsFilterFinal.add(item);
								}
							}


						}else{

							Section section = (Section) getItem(i);

							if(section.letter != null && section.letter.length() >= 1 &&
									mSearchTerm.length() > 0){
								if(section.letter.toLowerCase().startsWith(mSearchTerm.toString().substring(0,1))){
									rowsFilterFinal.add(section);
								}
							}


						}

					}
				}


				results.count = rowsFilterFinal.size();
				results.values = rowsFilterFinal;

				return results;
			}
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			rows =  (List<Row>) results.values;
			notifyDataSetChanged();
		}
	}

	@Override
	public Filter getFilter() {

		if(mClientFilter == null)
			mClientFilter = new ClientFilter();
		return mClientFilter;

	}

}
