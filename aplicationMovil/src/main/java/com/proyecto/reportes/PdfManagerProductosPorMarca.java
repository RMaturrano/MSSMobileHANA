package com.proyecto.reportes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.proyecto.utils.Variables;

@SuppressLint("SimpleDateFormat")
public class PdfManagerProductosPorMarca {

    private static Context mContext;
    private static final String APP_FOLDER_NAME = "com.pragsa.pdf";
    private static final String PRODUCTOS_MARCA = "ProductosXMarca";
    private static Font catFont;
    private static Font subFont;
    private static Font smallBold;
    private static Font smallFont;
    private String FILENAME;

    private static BaseFont unicode;

    public PdfManagerProductosPorMarca(Context context) throws IOException, DocumentException {
        mContext = context;
        unicode = BaseFont.createFont(BaseFont.TIMES_ROMAN, "UTF-8", BaseFont.EMBEDDED);
        catFont = new Font(unicode, 22, Font.BOLD, BaseColor.BLACK);
        subFont = new Font(unicode, 16, Font.BOLD, BaseColor.BLACK);
        smallBold = new Font(unicode, 12, Font.BOLD, BaseColor.BLACK);
        smallFont = new Font(unicode, 12, Font.NORMAL, BaseColor.BLACK);
    }

    public void createPdfDocument(ReportFormatObjectProductoXMarca object) {
        try {

            //Creamos las carpetas en nuestro dispositivo, si existen las eliminamos.
            String fullFileName = createDirectoryAndFileName();

            if (fullFileName.length() > 0) {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(fullFileName));

                document.open();

                addMetaData(document);
                addTitlePage(document, object);

                for (ReportFormatObjectProductoXMarca_Marcas marca : object.getMarcas()) {
                    addSubTitlePage(document, marca.getDescripcion());
                    addContent(document, marca);
                }

                document.close();

                Toast.makeText(mContext, "Archivo PDF generado con éxito.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //SubMethods
    private String createDirectoryAndFileName() {

        FILENAME = "ProductosXMarca.pdf";
        String fullFileName = "";

        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(extStorageDirectory + File.separator + APP_FOLDER_NAME);

        try {
            if (!pdfDir.exists()) {
                pdfDir.mkdir();
            }
            File pdfSubDir = new File(pdfDir.getPath() + File.separator + PRODUCTOS_MARCA);

            if (!pdfSubDir.exists()) {
                pdfSubDir.mkdir();
            }

            fullFileName = Environment.getExternalStorageDirectory()
                    + File.separator + APP_FOLDER_NAME
                    + File.separator + PRODUCTOS_MARCA
                    + File.separator + FILENAME;

            File outputFile = new File(fullFileName);

            if (outputFile.exists()) {
                outputFile.delete();
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return fullFileName;
    }

    private static void addMetaData(Document document) {
        document.addTitle("pragsa PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("seidor.com");
        document.addCreator("seidor.com");
    }

    private static void addTitlePage(Document document, ReportFormatObjectProductoXMarca reportObject)
            throws DocumentException {

        Paragraph preface = new Paragraph();
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

        preface.add(new Paragraph("REPORTE DE PRODUCTOS POR MARCA ", catFont));

        addEmptyLine(preface, 1);

        preface.add(new Paragraph(reportObject.getEmpleado(), smallBold));

        document.add(preface);

        // Si queremos crear una nueva página
        //document.newPage();
    }

    private static void addContent(Document document, ReportFormatObjectProductoXMarca_Marcas reportDetail)
            throws DocumentException {

        Paragraph paragraph = new Paragraph();

        createTable(paragraph, reportDetail);

        document.add(paragraph);

    }


    //Util's
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static void createTable(Paragraph tableSection, ReportFormatObjectProductoXMarca_Marcas detallesReporte)
            throws DocumentException {

        //Primera cabecera

        int TABLE_COLUMNS = 4;
        PdfPTable table = new PdfPTable(TABLE_COLUMNS);

        float[] columnWidths = new float[]{300f, 120f, 120f, 120f};
        table.setWidths(columnWidths);

        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Producto", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Stock", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Precio cobertura", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Precio mayorista", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        table.setHeaderRows(1);

        for (ReportFormatObjectProductoXMarca_Marcas_Detalles detalle : detallesReporte.getDetalles()) {
            createLine(detalle, table);
        }

        tableSection.add(table);
    }

    private static void createLine(ReportFormatObjectProductoXMarca_Marcas_Detalles reporteLine, PdfPTable table) {

        PdfPCell cell = new PdfPCell();

        cell.setPhrase(new Phrase(reporteLine.getDescripcionProducto(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getStock(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getPrecioCobertura(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getPrecioMayorista(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

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

    public void showPdfFile(Context context, String fileName) {

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
