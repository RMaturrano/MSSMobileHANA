package com.proyecto.reportes;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;
import java.util.jar.Manifest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;
import com.proyect.movil.R;
import com.proyecto.database.Insert;
import com.proyecto.movil.PlaceholderFragment;
import com.proyecto.utils.ConstruirAlert;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.Variables;
import com.proyecto.ws.InvocaWS;

public class ReporteFragment extends Fragment implements OnItemClickListener{

	private ListView lv_0;
	private ArrayList<FormatCustomListView> searchResults_0 = null;
	private ListViewCustomAdapterTwoLinesAndImg adapter;
    private Context contexto;
    private View v;
    private ConstruirAlert alert;
	private int reporteSel = 0;
	private int iconId = Variables.idIconRightBlue36dp;
	private PdfManagerSaldosVendedor pdfManager = null;
	private PdfManagerPreCobranza pdfManagerPreCobranza = null;
	private PdfManagerProductosPorMarca pdfManagerPM = null;
	private String REPORT_FOLDER = "SaldosVendedor";
    private String FILENAME = "SaldosVendedor.pdf";
    private String tipoReporteSel = "";

	private ProgressDialog mPd;

    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.reporte_saldos_vendedor_fragment, container, false);
        
        
        contexto = v.getContext();
        lv_0 = (ListView) v.findViewById(R.id.lvReporteSaldosVendedor);
        lv_0.setOnItemClickListener(this);
        alert = new ConstruirAlert();
        
        buildFirstAlert();
        
        setHasOptionsMenu(true);
        return v;
        
    }

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
			if(tipoReporteSel.equalsIgnoreCase("Saldos por Cobrar")){
					new GenerarReporte().execute("1");
			}else if(tipoReporteSel.equalsIgnoreCase("Pre Cobranza")){
					new GenerarReporte().execute("3");
			}else if (tipoReporteSel.equalsIgnoreCase("Productos por marca")){
					new GenerarReporte().execute("2");
			}
		}
	}

	private void buildFirstAlert(){
		
		
		//Spinner
		final Spinner spn = new Spinner(contexto);
		
		final String[] tipoPago = new String[4];
		tipoPago[0] = "Saldos por Cobrar";
		tipoPago[1] = "Estado de Cuenta";
		tipoPago[2] = "Productos por marca";
		tipoPago[3] = "Pre Cobranza";
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(contexto, 
				android.R.layout.simple_list_item_1,
				tipoPago);
		spn.setAdapter(adapter);
		spn.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View arg1, int pos, long arg3) {
			
				tipoReporteSel = (String) parent.getItemAtPosition(pos);
				
			}

			@Override
			public void onNothingSelected(
					AdapterView<?> arg0) {
				
			}
		});
		
		
		
		AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		alert.setTitle("Seleccione el reporte");
//		alert.setView(spn);
		alert.setSingleChoiceItems(tipoPago, -1, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				tipoReporteSel = tipoPago[item];
			}
		});
		alert.setCancelable(false);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			
				if(tipoReporteSel.equalsIgnoreCase("Saldos por Cobrar")){
					builDataReporte();
				}else if(tipoReporteSel.equalsIgnoreCase("Estado de Cuenta")){

					Fragment fragment = new BuscarSocioReporteFragment();
					FragmentManager manager = getFragmentManager();
		            FragmentTransaction transaction = manager.beginTransaction();
		            transaction.replace(R.id.main_content, fragment);
		            transaction.commit();

				}else if (tipoReporteSel.equalsIgnoreCase("Productos por marca")){

					if(isStoragePermissionGranted())
						new GenerarReporte().execute("2");

				}else if(tipoReporteSel.equalsIgnoreCase("Pre Cobranza")){
					
					builDataReporte();
					
				}
				
		  }
		});

		alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String title = contexto.getResources().getString(R.string.menu_inicio);
				
				Bundle args = new Bundle();
				args.putString(PlaceholderFragment.ARG_SECTION_TITLE, title);
				
				Fragment fragment = PlaceholderFragment.newInstance(title);
				fragment.setArguments(args);
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.main_content, fragment)
						.commit();
				getActivity().setTitle(title);
				
			}
		});

		alert.show();
		
	}
	
	
	@SuppressLint("SimpleDateFormat") 
	private void builDataReporte(){

		searchResults_0 = new ArrayList<FormatCustomListView>();
		lv_0 = (ListView) v.findViewById(R.id.lvReporteSaldosVendedor);
		
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
     	Date date = new Date();
     	String currentDate = dateFormat.format(date);
         
		FormatCustomListView sr1 = new FormatCustomListView();
	  	sr1.setTitulo("Tipo de reporte");
	  	
	  	if(tipoReporteSel.equalsIgnoreCase("Saldos por Cobrar"))
	  		sr1.setData("Informe Saldos por Cobrar");
	  	else
	  		sr1.setData("Informe de pre cobranza");
	  	//sr1.setIcon(iconId);
	  	searchResults_0.add(sr1);
	  	
	  	sr1 = new FormatCustomListView();
	  	sr1.setTitulo("Fecha inicio");
	  	sr1.setData(currentDate);
	  	sr1.setIcon(iconId);
	  	searchResults_0.add(sr1);
	  	
	  	sr1 = new FormatCustomListView();
	  	sr1.setTitulo("Fecha fin");
	  	sr1.setData(currentDate);
	  	sr1.setIcon(iconId);
	  	searchResults_0.add(sr1);
		  	
		
		adapter = new ListViewCustomAdapterTwoLinesAndImg( contexto, searchResults_0);      
		lv_0.setAdapter(adapter);
	}


	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
			if(position == 1){
				alert.construirAlertDatePicker(contexto, position, "Fecha inicio", searchResults_0, lv_0);
			}else if (position == 2){
				alert.construirAlertDatePicker(contexto, position, "Fecha fin", searchResults_0, lv_0);
			}
		
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			menu.clear();
	    	inflater.inflate(R.menu.menu_art_ordventa, menu);
	    	super.onCreateOptionsMenu(menu, inflater);
	 }
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
			
			case R.id.action_aceptar:
				
				if(tipoReporteSel.equalsIgnoreCase("Saldos por Cobrar")){

					if(isStoragePermissionGranted())
						new GenerarReporte().execute("1");
					
				}else if(tipoReporteSel.equalsIgnoreCase("Pre Cobranza")){

					if(isStoragePermissionGranted())
						new GenerarReporte().execute("3");
					
				}
                
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	      
			}
			
	}


	private boolean isStoragePermissionGranted(){

		if(Build.VERSION.SDK_INT >= 23){
			if(getContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
				return true;
			}else{
				ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
				return false;
			}
		}

		return true;
	}

	;


	private class GenerarReporte extends AsyncTask<String, Void, Boolean> {

		private String mTypeReport;

		@Override
		protected Boolean doInBackground(String... param) {

			mTypeReport = param[0];

			getActivity().runOnUiThread(new TimerTask() {
				@Override
				public void run() {
					mPd = new ProgressDialog(contexto);
					mPd.setTitle("Generando reporte");
					mPd.setMessage("Por favor, espere");
					mPd.show();
				}
			});


			if(mTypeReport.equals("1")){

				try {
					pdfManager = new PdfManagerSaldosVendedor(contexto);
				} catch (IOException | DocumentException e) {
					e.printStackTrace();
					return false;
				}

				QueryReport query = new QueryReport(contexto);

				ReportFormatObjectSaldosVendedor reporte = query.generateReporteSaldoVendedor(searchResults_0.get(1).getData(), searchResults_0.get(2).getData());
				query.close();

				if(reporte != null){
					assert pdfManager != null;
					pdfManager.createPdfDocument(reporte);

					return true;

				}else{

					return false;

				}
			}else if(mTypeReport.equals("2")){

				try {
					pdfManagerPM = new PdfManagerProductosPorMarca(contexto);
				} catch (IOException | DocumentException e) {
					e.printStackTrace();
					return false;
				}

				QueryReport query = new QueryReport(contexto);

				ReportFormatObjectProductoXMarca reporte =
						query.generarReporteProductoXMarca();
				query.close();

				if(reporte != null){

					assert pdfManagerPM != null;
					pdfManagerPM.createPdfDocument(reporte);

					return true;

				}else
					return false;

			}else if(mTypeReport.equals("3")){

				try {
					pdfManagerPreCobranza = new PdfManagerPreCobranza(contexto);
				} catch (IOException | DocumentException e) {
					e.printStackTrace();
					return false;
				}

				QueryReport query = new QueryReport(contexto);

				ReportFormatObjectSaldosVendedor reporte = query.generateReportePreCobranza(searchResults_0.get(1).getData(), searchResults_0.get(2).getData());
				query.close();

				if(reporte != null){
					assert pdfManagerPreCobranza != null;
					pdfManagerPreCobranza.createPdfDocument(reporte);

					return true;

				}else{
					return  false;
				}

			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean succes) {

			mPd.dismiss();
			if(succes){
				if(mTypeReport.equals("1"))
					pdfManager.showPdfFile(REPORT_FOLDER + File.separator + FILENAME, contexto);
				else if(mTypeReport.equals("2"))
					pdfManagerPM.showPdfFile(contexto, "ProductosXMarca" + File.separator + "ProductosXMarca.pdf");
				else if(mTypeReport.equals("3"))
					pdfManagerPreCobranza.showPdfFile("PreCobranza" + File.separator + "PreCobranza.pdf", contexto);
			}
			else
				Toast.makeText(contexto, "No hay datos para mostrar el reporte", Toast.LENGTH_LONG).show();

		}

	}

	
}
