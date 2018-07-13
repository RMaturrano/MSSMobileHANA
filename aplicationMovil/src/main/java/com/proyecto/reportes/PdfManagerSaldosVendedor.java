package com.proyecto.reportes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.proyect.movil.R;
import com.proyecto.utils.Variables;

public class PdfManagerSaldosVendedor {
	
	private static Context mContext;
    private static final String APP_FOLDER_NAME = "com.pragsa.pdf";
    private static final String SALDOS_VENDEDOR = "SaldosVendedor";
    private static Font catFont;
    private static Font subFont ;
    private static Font smallBold ;
    private static Font smallFont ;
    private static Font italicFont ;
    private static Font italicFontBold ;
    
    private static BaseFont unicode;
    
    public PdfManagerSaldosVendedor(Context context) throws IOException, DocumentException {
        mContext = context;
        //Creamos los distintos estilos para nuestro tipo de fuente.
        unicode = BaseFont.createFont(BaseFont.TIMES_ROMAN, "UTF-8", BaseFont.EMBEDDED);
        catFont = new Font(unicode, 22,Font.BOLD, BaseColor.BLACK);
        subFont = new Font(unicode, 16,Font.BOLD, BaseColor.BLACK);
        smallBold = new Font(unicode, 12,Font.BOLD, BaseColor.BLACK);
        smallFont = new Font(unicode, 12,Font.NORMAL, BaseColor.BLACK);
        italicFont = new Font(unicode, 12,Font.ITALIC, BaseColor.BLACK);
        italicFontBold = new Font(unicode, 12,Font.ITALIC|Font.BOLD, BaseColor.BLACK);
    }
    
    
    public void createPdfDocument(ReportFormatObjectSaldosVendedor object) {
        try {
 
            //Creamos las carpetas en nuestro dispositivo, si existen las eliminamos.
            String fullFileName = createDirectoryAndFileName();
 
            if(fullFileName.length()>0){
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(fullFileName));
 
                Rectangle size = new Rectangle(820,980);
                document.setPageSize(size);
                document.open();
 
                //Creamos los metadatos del alchivo
                addMetaData(document);
                //Adicionamos el logo de la empresa
//                addImage(document);
                //Creamos el título del documento
                addTitlePage(document, object);
                //Creamos el contenido en form de tabla del documento
                addInvoiceContent(document,object.getDetalles());
                //Creamos el total del documento
                addInvoiceTotal(document, object);
 
                document.close();
 
                Toast.makeText(mContext, "Archivo PDF generado con éxito.", Toast.LENGTH_SHORT).show();
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 1.
    private String createDirectoryAndFileName(){
   	 
        String FILENAME = "SaldosVendedor.pdf";
        String fullFileName ="";
        //Obtenemos el directorio raiz "/sdcard"
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(extStorageDirectory + File.separator + APP_FOLDER_NAME);
 
        //Creamos la carpeta "com.pragsa.pdf" y la subcarpeta "SaldosVendedor"
        try {
            if (!pdfDir.exists()) {
                pdfDir.mkdir();
            }
            File pdfSubDir = new File(pdfDir.getPath() + File.separator + SALDOS_VENDEDOR);
 
            if (!pdfSubDir.exists()) {
                pdfSubDir.mkdir();
            }
 
            fullFileName = Environment.getExternalStorageDirectory() + File.separator + APP_FOLDER_NAME + File.separator + SALDOS_VENDEDOR + File.separator + FILENAME;
 
            File outputFile = new File(fullFileName);
 
            if (outputFile.exists()) {
                outputFile.delete();
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return fullFileName;
    }
    
    // 2.
    private static void addMetaData(Document document) {
        document.addTitle("pragsa PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("seidor.com");
        document.addCreator("seidor.com");
    }

    // 3.
    private static void addTitlePage(Document document, ReportFormatObjectSaldosVendedor reportObject)
            throws DocumentException {
 
        Paragraph preface = new Paragraph();
        // Adicionamos una línea en blanco
        addEmptyLine(preface, 1);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    	Date date = new Date();
    	String currentDate = dateFormat.format(date);

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        final String empresa = pref.getString(Variables.DESCRIPCION_COMPANIA, "No Found");

        preface.add(new Paragraph("Compañia: " + empresa , subFont));// reportObject.getEmpresa(), subFont));
        //preface.add(new Paragraph("Direccion: " + reportObject.getDireccion(), subFont));
        preface.add(new Paragraph("Fecha de impresion: " + currentDate, subFont));
 
        addEmptyLine(preface, 1);
        
        preface.add(new Paragraph("REPORTE DE SALDOS POR VENDEDOR ", catFont));
 
        addEmptyLine(preface, 1);
        
        //Adicionamos los datos del empleado
        preface.add(new Paragraph(reportObject.getEmpleado(), smallBold));
 
        //Adicionamos el párrafo creado al documento
        document.add(preface);
 
        // Si queremos crear una nueva página
        //document.newPage();
    }
    
    // 4.
    private static void addInvoiceContent(Document document, java.util.List<ReportFormatObjectSaldosVendedorDetail> reportDetail) 
    							throws DocumentException {
    	 
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        // Creamos una tabla con los títulos de las columnas
        createInvoiceTable(paragraph, reportDetail);
        // Adicionamos el párrafo al documento
        document.add(paragraph);
 
    }
    
    // 5.
    private static void addInvoiceTotal(Document document, ReportFormatObjectSaldosVendedor invoiceObject) throws DocumentException {
    	 
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        // Adicionamos la tabla al párrafo
        createTotalInvoiceTable(paragraph, invoiceObject);
        // Adicionamos el párrafo al documento
        document.add(paragraph);
 
    }

    
  //Procedimiento para crear los títulos de las columnas del reporte.
    private static void createInvoiceTable(Paragraph tableSection, java.util.List<ReportFormatObjectSaldosVendedorDetail> detallesReporte)
            throws DocumentException {
 
        int TABLE_COLUMNS = 9;
        //Instaciamos el objeto Pdf Table y creamos una tabla con las columnas definidas en TABLE_COLUMNS
        PdfPTable table = new PdfPTable(TABLE_COLUMNS);// number of table columns
 
        //Definimos el ancho que corresponde a cada una de las 10 columnas
        float[] columnWidths = new float[]{70f, 140f, 120f, 50f, 220f,220f, 100f, 100f, 100f};
        table.setWidths(columnWidths);
 
        //Definimos el ancho de nuestra tabla en %
        table.setWidthPercentage(100);
 
        // otras propiedades que se pueden aplicar a la tabla
        // table.setBorderColor(BaseColor.GRAY);
        // table.setPadding(4);
        // table.setSpacing(4);
        // table.setBorderWidth(1);
 
        //Definimos los títulos para cada una de las 5 columnas
        //PdfPCell cell = new PdfPCell(new Phrase("Doc",smallBold));
        //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la primera columna
        //table.addCell(cell);
 
        PdfPCell cell = new PdfPCell(new Phrase("Clave",smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la segunda columna
        table.addCell(cell);
 
        cell = new PdfPCell(new Phrase("Sunat",smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la tercera columna
        table.addCell(cell);
 
        cell = new PdfPCell(new Phrase("Emision",smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la cuarta columna
        table.addCell(cell);
 
        cell = new PdfPCell(new Phrase("Dias",smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la quinta columna
        table.addCell(cell);
        
        //cell = new PdfPCell(new Phrase("Ruc",smallBold));
        //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la sexta columna
        //table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Nombre",smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Direccion",smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Total",smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la octava columna
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Pagado",smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la novena columna
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Saldo",smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Adicionamos el título de la decima columna
        table.addCell(cell);
 
        //Creamos la fila de la tabla con las cabeceras
        table.setHeaderRows(1);
 
        //Creamos las lineas con los artículos de la factura;
        for (ReportFormatObjectSaldosVendedorDetail detalle : detallesReporte) {
            createInvoiceLine(detalle, table);
        }
 
        tableSection.add(table);
    }

  //Procedimiento para crear una linea vacía
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

  //Procedimiento para adicionar una imagen al documento PDF
    private static void addImage(Document document) throws IOException, DocumentException {
 
        Bitmap bitMap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] bitMapData = stream.toByteArray();
        Image image = Image.getInstance(bitMapData);
        //Posicionamos la imagen el el documento
        image.setAbsolutePosition(400f, 650f);
        document.add(image);
    }
    
  //Procedimiento para crear las líneas del detalle del reporte
    private static void createInvoiceLine(ReportFormatObjectSaldosVendedorDetail reporteLine, PdfPTable table) {
        PdfPCell cell = new PdfPCell();
 
        //cell.setPhrase(new Phrase(reporteLine.getDocumento(),smallFont));
        //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //table.addCell(cell);
        
        cell.setPhrase(new Phrase(reporteLine.getClave(),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
 
        cell.setPhrase(new Phrase(reporteLine.getSunat(),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
 
        cell.setPhrase(new Phrase(reporteLine.getEmision(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
 
        cell.setPhrase(new Phrase(reporteLine.getDias(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        //cell.setPhrase(new Phrase(reporteLine.getRuc(),smallFont));
        //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //table.addCell(cell);
        
        cell.setPhrase(new Phrase(reporteLine.getNombre(),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell.setPhrase(new Phrase(reporteLine.getDireccion(),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
 
        cell.setPhrase(new Phrase(reporteLine.getTotal(),smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
 
        cell.setPhrase(new Phrase(reporteLine.getPagado(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
 
        cell.setPhrase(new Phrase(reporteLine.getSaldo(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
 
    }

  //Procedimiento para crear los totales y subtotales del reporte en forma de tabla.
    //Misma lógica utilizada para crear los títulos de las columnas de la factura
    private static void createTotalInvoiceTable(Paragraph tableSection, ReportFormatObjectSaldosVendedor orderHeaderModel)
            throws DocumentException {
 
        int TABLE_COLUMNS = 4;
        PdfPTable table = new PdfPTable(TABLE_COLUMNS);
 
        float[] columnWidths = new float[]{500f, 60f, 60f, 60f};
        table.setWidths(columnWidths);
        table.setWidthPercentage(100);
 
        //Adicionamos el título de la celda
        PdfPCell cell = new PdfPCell(new Phrase("Total general: ",smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
 
        double subTotal = orderHeaderModel.getTotal();
        //Adicionamos el contenido de la celda con el valor subtotal
        cell = new PdfPCell(new Phrase(String.valueOf(subTotal)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
 
        table.addCell(cell);
        
        double pagado = orderHeaderModel.getPagado();
        //Adicionamos el contenido de la celda con el valor subtotal
        cell = new PdfPCell(new Phrase(String.valueOf(pagado)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
 
        table.addCell(cell);
        
        double saldo = orderHeaderModel.getSaldo();
        //Adicionamos el contenido de la celda con el valor subtotal
        cell = new PdfPCell(new Phrase(String.valueOf(saldo)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
 
        table.addCell(cell);
 
        tableSection.add(table);
 
    }
    
    
  //Procedimiento para mostrar el documento PDF generado
    public void showPdfFile(String fileName, Context context){
 
        String sdCardRoot = Environment.getExternalStorageDirectory().getPath();
        String path = sdCardRoot + File.separator + APP_FOLDER_NAME + File.separator + fileName;
 
        File file = new File(path);
 
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();
        }
    }
    
    //Procedimiento para enviar por email el documento PDF generado
    public void sendPdfByEmail(String fileName, String emailTo, String emailCC, Context context){
 
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Movalink PDF Tutorial email");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Working with PDF files in Android");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
        emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{emailCC});
 
        String sdCardRoot = Environment.getExternalStorageDirectory().getPath();
        String fullFileName = sdCardRoot + File.separator + APP_FOLDER_NAME + File.separator + fileName;
 
        Uri uri = Uri.fromFile(new File(fullFileName));
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.setType("application/pdf");
 
        context.startActivity(Intent.createChooser(emailIntent, "Send email using:"));
    }
    
    
}
