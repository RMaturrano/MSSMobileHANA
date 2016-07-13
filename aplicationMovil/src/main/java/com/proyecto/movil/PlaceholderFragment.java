package com.proyecto.movil;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.proyect.movil.R;
import com.proyecto.bean.BeanChartProducto;
import com.proyecto.database.Select;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PlaceholderFragment extends Fragment {

	private BarChart mBarChart;
	private PieChart mPieChart;

	public static final String ARG_SECTION_TITLE = "section_number";
	private Select mSelect;
	private String mCurrentDate;

	/**
	 * Crea una instancia prefabricada de {@link PlaceholderFragment}
	 *
	 */
	public static PlaceholderFragment newInstance(String sectionTitle) {
		PlaceholderFragment fragment = new PlaceholderFragment();
		Bundle args = new Bundle();
		args.putString(ARG_SECTION_TITLE, sectionTitle);
		fragment.setArguments(args);
		return fragment;
	}

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.section_fragment, container,
				false);

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		mCurrentDate = dateFormat.format(date);
		mSelect = new Select(view.getContext());

		//Bar Chart
		mBarChart = (BarChart) view.findViewById(R.id.chart);
		buildDataBarChart();
		mBarChart.setOnLongClickListener(mBarChartOnLongClickListener);

		//Pie Chart
		mPieChart = (PieChart) view.findViewById(R.id.chartBestProduct);
		builddataPieChart();
		mPieChart.setOnLongClickListener(mPieChartOnLongClickListener);
		mPieChart.setOnChartValueSelectedListener(mPieChartOnChartValueSelectedListener);

		setHasOptionsMenu(true);
		return view;
	}

	private void buildDataBarChart(){
		ArrayList<BarEntry> entries = new ArrayList<>();
		entries.add(new BarEntry(mSelect.inicioGetTotalPedidosAprobados(), 0));
		entries.add(new BarEntry(mSelect.inicioGetTotalPedidosPendientes(), 1));
		entries.add(new BarEntry(mSelect.inicioGetTotalPagosAprobados(), 2));
		entries.add(new BarEntry(mSelect.inicioGetTotalPagosPendientes(), 3));

		BarDataSet dataset = new BarDataSet(entries, "Monto en soles");
		dataset.setColors(ColorTemplate.COLORFUL_COLORS);

		ArrayList<String> labels = new ArrayList<String>();
		labels.add("Pedidos aprobados");
		labels.add("Pedidos pendientes");
		labels.add("Pagos aprobados");
		labels.add("Pagos pendientes");

		BarData data = new BarData(labels, dataset);
		data.setValueFormatter(new ValueFormatter() {
			DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.0");
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				return "S/. " + decimalFormat.format(value);
			}
		});

		mBarChart.clear();
		mBarChart.setData(data);
		mBarChart.setDescription("Monto acumulado de pedidos y cobranzas");
		mBarChart.animateY(4000);
	}

	private void builddataPieChart(){

		// creating data values
		ArrayList<Entry> entries = new ArrayList<>();
		ArrayList<String> labels = new ArrayList<String>();
		ArrayList<BeanChartProducto> listaProductos = mSelect.inicioGetTopTenProducts();

		if(listaProductos != null){
			for (BeanChartProducto p: listaProductos){
				entries.add(new Entry((float) p.getTotal(),0,p));
				if(p.getDescripcion() != null)
					labels.add(p.getDescripcion());
				else
					return;
			}

			PieDataSet dataset = new PieDataSet(entries,"Articulos");
			dataset.setValueTextColor(Color.BLACK);
			dataset.setValueTextSize(11f);
			dataset.setColors(ColorTemplate.COLORFUL_COLORS);

			PieData data = new PieData(labels, dataset); // initialize Piedata
			data.setValueFormatter(new ValueFormatter() {
				DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.0");
				@Override
				public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
					return "S/. " + decimalFormat.format(value);
				}
			});

			mPieChart.clear();
			mPieChart.setData(data);
			mPieChart.setDescription("Productos mas vendidos");  // set the description
			mPieChart.animateY(4000);
			mPieChart.invalidate();

		}

	}


	//On click BAR CHART
	View.OnLongClickListener mBarChartOnLongClickListener = new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			mBarChart.saveToGallery("montoPedidoCobranza_"+ mCurrentDate,85);
			Toast.makeText(getContext(),"Guardado en la galería", Toast.LENGTH_SHORT).show();
			return true;
		}
	};

	View.OnLongClickListener mPieChartOnLongClickListener = new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			mPieChart.saveToGallery("articuloMasVendido_"+mCurrentDate,85);
			Toast.makeText(getContext(),"Guardado en la galería", Toast.LENGTH_SHORT).show();
			return true;
		}
	};

	//On Chart SELECT Value PIE CHART
	OnChartValueSelectedListener mPieChartOnChartValueSelectedListener = new OnChartValueSelectedListener() {
		@Override
		public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

			if(e == null)
				return;

			BeanChartProducto bean = (BeanChartProducto) e.getData();
			Toast.makeText(getContext(),bean.getDescripcion(),Toast.LENGTH_LONG).show();
		}

		@Override
		public void onNothingSelected() {

		}
	};

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.menu_sincronizar, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_sincronizar:
				buildDataBarChart();
				builddataPieChart();
				Toast.makeText(getContext(),"Registros actualizados...", Toast.LENGTH_SHORT).show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
