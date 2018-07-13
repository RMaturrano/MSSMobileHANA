package com.proyecto.reportes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.proyecto.utils.DoubleRound;
import com.proyecto.utils.StringDateCast;
import com.proyecto.utils.Variables;

public class PdfManagerEstadoCuentaSocio {

    private static Context mContext;
    private static final String APP_FOLDER_NAME = "com.pragsa.pdf";
    private static final String ESTADO_CUENTA = "EstadoCuentaSocio";
    private static Font catFont;
    private static Font subFont;
    private static Font smallBold;
    private static Font smallFont;
    private static double promedioDias = 0;

    private static BaseFont unicode;

    public PdfManagerEstadoCuentaSocio(Context context) throws IOException, DocumentException {
        mContext = context;
        //Creamos los distintos estilos para nuestro tipo de fuente.
        unicode = BaseFont.createFont(BaseFont.TIMES_ROMAN, "UTF-8", BaseFont.EMBEDDED);
        catFont = new Font(unicode, 22, Font.BOLD, BaseColor.BLACK);
        subFont = new Font(unicode, 16, Font.BOLD, BaseColor.BLACK);
        smallBold = new Font(unicode, 12, Font.BOLD, BaseColor.BLACK);
        smallFont = new Font(unicode, 12, Font.NORMAL, BaseColor.BLACK);

    }


    public void createPdfDocument(ArrayList<ReporteEstadoCuenta> object) {
        try {

            //Creamos las carpetas en nuestro dispositivo, si existen las eliminamos.
            String fullFileName = createDirectoryAndFileName();

            if (fullFileName.length() > 0) {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(fullFileName));

                document.open();

                //Creamos los metadatos del alchivo
                addMetaData(document);

                //Añadir el título (cabecera)
                addTitlePage(document, object);

                //Añadir subtitulo 1 (Ventas)
                addSubTitlePage(document, "Ventas");

                //Contenido 1 (ventas)
                double total1 = addContent(document, object);

                addTotal(document, total1);

                addSubTitlePage(document, "Cobranzas");

                double total2 = addContent2(document, object);

                addTotal2(document, total2);

                document.close();

                Toast.makeText(mContext, "Archivo PDF generado con éxito.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 1.
    private String createDirectoryAndFileName() {

        String FILENAME = "EstadoCuentaSocio.pdf";
        String fullFileName = "";
        //Obtenemos el directorio raiz "/sdcard"
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(extStorageDirectory + File.separator + APP_FOLDER_NAME);

        //Creamos la carpeta "com.pragsa.pdf" y la subcarpeta "EstadoCuentaSocio"
        try {
            if (!pdfDir.exists()) {
                pdfDir.mkdir();
            }
            File pdfSubDir = new File(pdfDir.getPath() + File.separator + ESTADO_CUENTA);

            if (!pdfSubDir.exists()) {
                pdfSubDir.mkdir();
            }

            fullFileName = Environment.getExternalStorageDirectory() + File.separator + APP_FOLDER_NAME + File.separator + ESTADO_CUENTA + File.separator + FILENAME;

            File outputFile = new File(fullFileName);

            if (outputFile.exists()) {
                outputFile.delete();
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return fullFileName;
    }

    // 2.
    private static void addMetaData(Document document) {
        document.addTitle("pragsa PDF estado cuenta socio");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("seidor.com");
        document.addCreator("seidor.com");
    }

    // 3.
    @SuppressLint("SimpleDateFormat")
    private static void addTitlePage(Document document, ArrayList<ReporteEstadoCuenta> reportObject)
            throws DocumentException {

        Paragraph preface = new Paragraph();
        // Adicionamos una línea en blanco
        addEmptyLine(preface, 1);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String currentDate = dateFormat.format(date);

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        final String empresa = pref.getString(Variables.DESCRIPCION_COMPANIA, "No Found");

        preface.add(new Paragraph("Compañia: " + empresa, subFont));
        //preface.add(new Paragraph("Direccion: Calle Tacna N° 330-Iquitos-Maynas-Loreto" , subFont));
        preface.add(new Paragraph("Fecha de impresion: " + currentDate, subFont));

        addEmptyLine(preface, 1);

        preface.add(new Paragraph("REPORTE DE ESTADO DE CUENTA ", catFont));

        addEmptyLine(preface, 1);

        preface.add(new Paragraph("Cliente: " + reportObject.get(0).getCliente()
                + " - " + reportObject.get(0).getNombre(), subFont));
        preface.add(new Paragraph("Lista precio: " + reportObject.get(0).getListaPrecio(), subFont));
        preface.add(new Paragraph("Limite credito: " + reportObject.get(0).getLineaCredito(), subFont));
        preface.add(new Paragraph("Condicion pago: " + reportObject.get(0).getCondicionPago(), subFont));

        //Adicionamos el párrafo creado al documento
        document.add(preface);

        // Si queremos crear una nueva página
        //document.newPage();
    }


    private static void addSubTitlePage(Document document, String subtitulo)
            throws DocumentException {

        Paragraph preface = new Paragraph();
        // Adicionamos una línea en blanco
        addEmptyLine(preface, 1);

        preface.add(new Paragraph(subtitulo, catFont));

        addEmptyLine(preface, 1);

        //Adicionamos el párrafo creado al documento
        document.add(preface);

    }


    // 4.
    private static double addContent(Document document, ArrayList<ReporteEstadoCuenta> reporte)
            throws DocumentException {

        Paragraph paragraph = new Paragraph();
        // Creamos una tabla con los títulos de las columnas
        double total = crearTablaCuerpoVentas(paragraph, reporte);
        // Adicionamos el párrafo al documento
        document.add(paragraph);

        return total;

    }


    private static double addContent2(Document document, ArrayList<ReporteEstadoCuenta> reporte)
            throws DocumentException {

        Paragraph paragraph = new Paragraph();
        // Creamos una tabla con los títulos de las columnas
        double total = crearTablaCuerpoCobranzas(paragraph, reporte);
        // Adicionamos el párrafo al documento
        document.add(paragraph);

        return total;

    }

    // 5.
    private static void addTotal(Document document, double total) throws DocumentException {

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        // Adicionamos la tabla al párrafo
        createTotalVentas(paragraph, total);
        // Adicionamos el párrafo al documento
        document.add(paragraph);

    }

    private static void addTotal2(Document document, double total) throws DocumentException {

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        // Adicionamos la tabla al párrafo
        createTotalVentas2(paragraph, total);
        // Adicionamos el párrafo al documento
        document.add(paragraph);

    }


    //Procedimiento para crear los títulos de las columnas del reporte.
    private static double crearTablaCuerpoVentas(Paragraph tableSection, ArrayList<ReporteEstadoCuenta> listaObjecto)
            throws DocumentException {

        int TABLE_COLUMNS = 8;
        PdfPTable table = new PdfPTable(TABLE_COLUMNS);
        float[] columnWidths = new float[]{60f, 110f, 90f, 100f, 85f, 60f, 120f, 120f};
        table.setWidths(columnWidths);

        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Clave", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        //cell = new PdfPCell(new Phrase("Tipo",smallBold));
        //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //table.addCell(cell);

        cell = new PdfPCell(new Phrase("Sunat", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Condicion", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Vendedor", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Emision", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Moneda", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Total", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Saldo", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        table.setHeaderRows(1);

        double total = 0;
        //Creamos las lineas con los artículos de la factura;
        for (ReporteEstadoCuenta detalle : listaObjecto) {
            if (detalle.getTipoReporte().equalsIgnoreCase("EstadoCuenta1")) {
                crearLineaCuerpoVenta(detalle, table);
                total += Double.parseDouble(detalle.getSaldo());
            }
        }

        tableSection.add(table);

        return total;
    }


    private static double crearTablaCuerpoCobranzas(Paragraph tableSection, ArrayList<ReporteEstadoCuenta> listaObjecto)
            throws DocumentException {

        int TABLE_COLUMNS = 9;
        PdfPTable table = new PdfPTable(TABLE_COLUMNS);
        float[] columnWidths = new float[]{70f, 70f, 90f, 90f, 80f, 50f, 50f, 50f, 60f};
        table.setWidths(columnWidths);

        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Clave", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        //cell = new PdfPCell(new Phrase("Tipo",smallBold));
        //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //table.addCell(cell);

        cell = new PdfPCell(new Phrase("Sunat", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Condicion", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Vendedor", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Emision", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Fecha pago", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nro de dias", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Moneda pago", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Pagado", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        table.setHeaderRows(1);

        int c = 0;
        double importePagado = 0;
        //Creamos las lineas con los artículos de la factura;
        for (ReporteEstadoCuenta detalle : listaObjecto) {
            if (detalle.getTipoReporte().equalsIgnoreCase("EstadoCuenta2")) {
                crearLineaCuerpoVenta2(detalle, table);
                promedioDias += Integer.parseInt(detalle.getPago_Dias());
                c++;
                importePagado += Double.parseDouble(detalle.getPagado_Importe());
            }
        }

        if (c > 0 && promedioDias > 0) {
            promedioDias = DoubleRound.round(promedioDias / c, 2);
        }

        tableSection.add(table);

        return importePagado;
    }


    //Procedimiento para crear una linea vacía
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    //Procedimiento para crear las líneas del detalle del reporte
    private static void crearLineaCuerpoVenta(ReporteEstadoCuenta reporteLine, PdfPTable table) {
        PdfPCell cell = new PdfPCell();

        cell.setPhrase(new Phrase(reporteLine.getClave(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        //cell.setPhrase(new Phrase(reporteLine.getTipoReporte(),smallFont));
        //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getSunat(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getCondicion(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getVendedor(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(StringDateCast.castStringtoDate(reporteLine.getEmision()), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getMoneda(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getTotal(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getSaldo(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

    }


    private static void crearLineaCuerpoVenta2(ReporteEstadoCuenta reporteLine, PdfPTable table) {
        PdfPCell cell = new PdfPCell();

        cell.setPhrase(new Phrase(reporteLine.getClave(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        //cell.setPhrase(new Phrase(reporteLine.getTipoReporte(),smallFont));
        //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getSunat(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getCondicion(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getVendedor(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(StringDateCast.castStringtoDate(reporteLine.getEmision()), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(StringDateCast.castStringtoDate(reporteLine.getPago_Fecha()), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getPago_Dias(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getPago_Moneda(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getPagado_Importe(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

    }


    private static void createTotalVentas(Paragraph tableSection, double total)
            throws DocumentException {

        int TABLE_COLUMNS = 2;
        PdfPTable table = new PdfPTable(TABLE_COLUMNS);

        float[] columnWidths = new float[]{320f, 60f};
        table.setWidths(columnWidths);
        table.setWidthPercentage(100);

        //Adicionamos el título de la celda
        PdfPCell cell = new PdfPCell(new Phrase("Total deuda: ", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(String.valueOf(total)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(cell);

        tableSection.add(table);

    }


    private static void createTotalVentas2(Paragraph tableSection, double total)
            throws DocumentException {

        int TABLE_COLUMNS = 4;
        PdfPTable table = new PdfPTable(TABLE_COLUMNS);

        float[] columnWidths = new float[]{200f, 60f, 80f, 60f};
        table.setWidths(columnWidths);
        table.setWidthPercentage(100);

        //Adicionamos el título de la celda
        PdfPCell cell = new PdfPCell(new Phrase("Promedio dias: ", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(String.valueOf(promedioDias)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Importe: ", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(String.valueOf(total)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        tableSection.add(table);

    }


    //Procedimiento para mostrar el documento PDF generado
    public void showPdfFile(String fileName, Context context) {

        String sdCardRoot = Environment.getExternalStorageDirectory().getPath();
        String path = sdCardRoot + File.separator + APP_FOLDER_NAME + File.separator + fileName;

        File file = new File(path);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();
        }
    }


}
