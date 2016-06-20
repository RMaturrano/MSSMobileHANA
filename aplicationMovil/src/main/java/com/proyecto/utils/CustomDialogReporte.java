package com.proyecto.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.itextpdf.text.DocumentException;
import com.proyect.movil.R;
import com.proyecto.bean.DialogBean;
import com.proyecto.reportes.PdfManagerSaldosVendedor;
import com.proyecto.reportes.QueryReport;
import com.proyecto.reportes.ReportFormatObjectSaldosVendedor;

public class CustomDialogReporte {

	private Context contexto;
	private PdfManagerSaldosVendedor pdfManager = null;
	private String REPORT_FOLDER = "SaldosVendedor";
    private String FILENAME = "SaldosVendedor.pdf";
	
	@SuppressLint("InflateParams") 
	public Dialog CreateGroupDialog(final Context context)
    {
		contexto = context;
		
		ArrayList<DialogBean> elements = new ArrayList<DialogBean>();
		DialogBean b = new DialogBean();
		
		b = new DialogBean();
		b.setDescription("Reporte de saldos por vendedor");
		b.setImage(R.drawable.ic_file_document);
		elements.add(b);

		LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.custom_dialog, null);
        
        final Dialog dialog = new Dialog(context);
        
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.dialog_toolbar);
        toolbar.setTitle("Seleccione el reporte");
        
        ListView listView = (ListView) view.findViewById(R.id.group_listview);
        ListViewCustomAdapterImgAndLine customAdapter = new ListViewCustomAdapterImgAndLine(context, elements);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if(position == 0 ){
					
					try {
						pdfManager = new PdfManagerSaldosVendedor(contexto);
					} catch (IOException | DocumentException e) {
						e.printStackTrace();
					}
					
					QueryReport query = new QueryReport(contexto);
					
					ReportFormatObjectSaldosVendedor reporte = query.generateReporteSaldoVendedor("","");
					assert pdfManager != null;
	                pdfManager.createPdfDocument(reporte);
	                pdfManager.showPdfFile(REPORT_FOLDER + File.separator + FILENAME,context);
	                
				    dialog.dismiss();
					
				}
			}
        	
        });

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        
        return dialog;
    }
	
}
