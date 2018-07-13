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
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.proyecto.utils.Variables;

public class PdfManagerPreCobranza {

    private static Context mContext;
    private static final String APP_FOLDER_NAME = "com.pragsa.pdf";
    private static final String PRE_COBRANZAS = "PreCobranza";
    private static Font catFont;
    private static Font subFont;
    private static Font smallBold;
    private static Font smallFont;

    private static BaseFont unicode;

    public PdfManagerPreCobranza(Context context) throws IOException, DocumentException {
        mContext = context;
        unicode = BaseFont.createFont(BaseFont.TIMES_ROMAN, "UTF-8", BaseFont.EMBEDDED);
        catFont = new Font(unicode, 22, Font.BOLD, BaseColor.BLACK);
        subFont = new Font(unicode, 16, Font.BOLD, BaseColor.BLACK);
        smallBold = new Font(unicode, 12, Font.BOLD, BaseColor.BLACK);
        smallFont = new Font(unicode, 12, Font.NORMAL, BaseColor.BLACK);
    }


    public void createPdfDocument(ReportFormatObjectSaldosVendedor object) {
        try {

            String fullFileName = createDirectoryAndFileName();

            if (fullFileName.length() > 0) {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(fullFileName));

                Rectangle size = new Rectangle(820, 960);
                document.setPageSize(size);
                document.open();

                addMetaData(document);
                addTitlePage(document, object);
                addContent(document, object.getDetalles());
                addInvoiceTotal(document, object);

                document.close();

                Toast.makeText(mContext, "Archivo PDF generado con éxito.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 1.
    private String createDirectoryAndFileName() {


        String FILENAME = "PreCobranza.pdf";
        String fullFileName = "";

        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(extStorageDirectory + File.separator + APP_FOLDER_NAME);

        try {
            if (!pdfDir.exists()) {
                pdfDir.mkdir();
            }
            File pdfSubDir = new File(pdfDir.getPath() + File.separator + PRE_COBRANZAS);

            if (!pdfSubDir.exists()) {
                pdfSubDir.mkdir();
            }

            fullFileName = Environment.getExternalStorageDirectory()
                    + File.separator
                    + APP_FOLDER_NAME
                    + File.separator
                    + PRE_COBRANZAS
                    + File.separator
                    + FILENAME;

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
        document.addTitle("pragsa PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("seidor.com");
        document.addCreator("seidor.com");
    }

    // 3.
    @SuppressLint("SimpleDateFormat")
    private static void addTitlePage(Document document, ReportFormatObjectSaldosVendedor reportObject)
            throws DocumentException {

        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String currentDate = dateFormat.format(date);


        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        final String empresa = pref.getString(Variables.DESCRIPCION_COMPANIA, "No Found");

        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p = new Paragraph("Compañia: " + empresa, subFont);
        p.add(new Chunk(glue));
        p.add("Fecha de impresion: " + currentDate);

        preface.add(p);
        preface.add(new Paragraph("Ruc: 20329432417", subFont));
        preface.add(new Paragraph("Direccion: Calle Tacna N° 330-Iquitos-Maynas-Loreto", subFont));

        addEmptyLine(preface, 1);

        Paragraph titulo = new Paragraph("PRE CANCELACION X VENDEDOR", catFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        preface.add(titulo);

        addEmptyLine(preface, 1);

        //Adicionamos los datos del empleado
        preface.add(new Paragraph(reportObject.getEmpleado(), smallBold));

        //Adicionamos el párrafo creado al documento
        document.add(preface);

        // Si queremos crear una nueva página
        //document.newPage();
    }

    // 4.
    private static void addContent(Document document,
                                   java.util.List<ReportFormatObjectSaldosVendedorDetail> reportDetail)
            throws DocumentException {

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        createInvoiceTable(paragraph, reportDetail);
        document.add(paragraph);

    }

    // 5.
    private static void addInvoiceTotal(Document document, ReportFormatObjectSaldosVendedor invoiceObject) throws DocumentException {

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        createTotalInvoiceTable(paragraph, invoiceObject);
        document.add(paragraph);

    }


    private static void createInvoiceTable(Paragraph tableSection, java.util.List<ReportFormatObjectSaldosVendedorDetail> detallesReporte)
            throws DocumentException {

        int TABLE_COLUMNS = 10;
        PdfPTable table = new PdfPTable(TABLE_COLUMNS);

        float[] columnWidths = new float[]{60f, 130f, 110f, 45f, 120f, 200f, 110f, 110f, 110f, 110f};
        table.setWidths(columnWidths);

        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Clave", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Sunat", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Emision", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Dias", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Ruc/Dni", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nombre", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Importe", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Cobrado", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Saldo", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Recibo", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        table.setHeaderRows(1);

        for (ReportFormatObjectSaldosVendedorDetail detalle : detallesReporte) {
            createLine(detalle, table);
        }

        tableSection.add(table);
    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    private static void createLine(ReportFormatObjectSaldosVendedorDetail reporteLine, PdfPTable table) {
        PdfPCell cell = new PdfPCell();

        cell.setPhrase(new Phrase(reporteLine.getClave(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getSunat(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getEmision(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getDias(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getRuc(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getNombre(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getTotal(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getPagado(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getSaldo(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell.setPhrase(new Phrase(reporteLine.getRecibo(), smallFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

    }


    private static void createTotalInvoiceTable(Paragraph tableSection, ReportFormatObjectSaldosVendedor orderHeaderModel)
            throws DocumentException {

        int TABLE_COLUMNS = 2;
        PdfPTable table = new PdfPTable(TABLE_COLUMNS);

        float[] columnWidths = new float[]{700f, 110f};
        table.setWidths(columnWidths);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Total: ", smallBold));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        double totalRecibo = orderHeaderModel.getTotalRecibo();
        cell = new PdfPCell(new Phrase(String.valueOf(totalRecibo)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(cell);

        tableSection.add(table);

    }


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
