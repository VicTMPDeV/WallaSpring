package com.victormoralesperez.wallaspring.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.victormoralesperez.wallaspring.models.Compra;
import com.victormoralesperez.wallaspring.models.Producto;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Una idea de como hacer PDFS usando la librería iText
 */
public class GeneradorPDF {

    /**
     * Factura en PDF, falta mejorar el formato
     *
     * @param compra
     * @param productos
     * @param total
     * @return
     */
    public static ByteArrayInputStream factura2PDF(Compra compra, List<Producto> productos, Float total) {
        Document documento = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            // Ahora sería darle formato con todo esto
            PdfWriter.getInstance(documento, out);
            documento.open();
            documento.add(new Paragraph("Factura: " + compra.getId(),
                    FontFactory.getFont("arial",   			// fuente
                            22,                            // tamaño
                            Font.ITALIC,                   // estilo
                            BaseColor.CYAN)));             // color
            documento.add(new Paragraph("Comprado por: " + compra.getComprador().getNombre() + " " + compra.getComprador().getApellidos()));
            documento.add(new Paragraph("Correo electrónico: " + compra.getComprador().getEmail()));
            documento.add(new Paragraph("Fecha de la compra: " + compra.getFechaCompra()));
            documento.add(new Paragraph("Listados de productos:"));
            documento.add(new Paragraph(""));

            PdfPTable tabla = new PdfPTable(2);
            tabla.addCell("Producto");
            tabla.addCell("Precio");
            for (Producto producto : productos) {
                tabla.addCell(producto.getNombre());
                tabla.addCell(Float.toString(producto.getPrecio()) + " €");
            }
            documento.add(tabla);

            documento.add(new Paragraph("Total: " + total + " €",
                    FontFactory.getFont("arial",   // fuente
                            14,                            // tamaño
                            Font.BOLD,                   // estilo
                            BaseColor.BLACK)));             // color

            documento.close();


        } catch (DocumentException ex) {

        }

        return new ByteArrayInputStream(out.toByteArray());
    }

}
