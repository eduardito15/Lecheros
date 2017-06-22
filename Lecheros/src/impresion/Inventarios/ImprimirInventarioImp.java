/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impresion.Inventarios;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import dominio.Inventario;
import dominio.InventarioRenglon;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PageRanges;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.LineSeparator;
import dominio.ConfiguracionGeneral;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import javax.print.PrintServiceLookup;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import sistema.SistemaMantenimiento;

/**
 *
 * @author Edu
 */
public class ImprimirInventarioImp implements ImprimirInventario, Printable {
    
    private PrintService[] printService;

    private DecimalFormat df;
    
    private Inventario inv;
    
    private int numeroDeRenglon;
    
    private int proximoRenglonInventario;
    
    public ImprimirInventarioImp(){
        df = new DecimalFormat("0.00");
        this.printService = PrinterJob.lookupPrintServices();
        numeroDeRenglon = 6;
        proximoRenglonInventario = 1;
    }
    
    @Override
    public void imprimirInventario(Inventario inventario) throws Exception {
        /*this.inv = inventario;
        if(inv.getRenglones().size() < 54) {
            imprimirPagina();
        }
        if(inv.getRenglones().size() > 54 && inv.getRenglones().size() < 114) {
            imprimirPagina();
            
            imprimirPagina();
        }
        if(inv.getRenglones().size() > 114 && inv.getRenglones().size() < 174) {
            imprimirPagina();
            imprimirPagina();
            imprimirPagina();
        }
        if (inv.getRenglones().size() > 174 && inv.getRenglones().size() < 234) {
            imprimirPagina();
            imprimirPagina();
            imprimirPagina();
            imprimirPagina();
        }*/
        
        File file=null;
	FileOutputStream writerStream= null;
	byte[] fileByte=null;
            try {
                file = File.createTempFile("Inventario"+ inventario.getReparto() + "Fecha" + 12, ".tmp");
		
			
		Document document = new Document(PageSize.A4, 36, 36, 36, 60);
			
		PdfWriter writer = null;

		writerStream = new FileOutputStream(file);
		writer = PdfWriter.getInstance(document, writerStream);
			
		document.open();
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("dd-MM-yyyy");
                Paragraph titulo = new Paragraph("Inventario " + inventario.getReparto() + " Fecha: " + formatter.format(inventario.getFecha()), new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD));
			
		titulo.setAlignment(Element.ALIGN_CENTER);

		document.add(titulo);

		// Agregar segunda línea separadora
		LineSeparator sep = new LineSeparator();
		sep.setOffset(-33);

		document.add(sep);
			
			
		document.add(new Paragraph(" "));
                
                PdfPTable pdfTable = new PdfPTable(new float[] { 65f, 305f, 65f, 65f});
                pdfTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfTable.setTotalWidth(500f);
		pdfTable.setLockedWidth(true);
			
		pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			
			//Headers
		pdfTable.addCell(new Phrase("Cod Art", new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD,BaseColor.BLACK)));
		pdfTable.addCell(new Phrase("Artículo", new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD,BaseColor.BLACK)));
		pdfTable.addCell(new Phrase("Cantidad", new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD,BaseColor.BLACK)));
		pdfTable.addCell(new Phrase("Total", new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD,BaseColor.BLACK)));
		
                for(InventarioRenglon ir : inventario.getRenglones()) {
                    pdfTable.completeRow();
		    pdfTable.addCell(new Phrase(Integer.toString(ir.getArticulo().getCodigo()) , new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL,BaseColor.BLACK)));
                    pdfTable.addCell(new Phrase(ir.getArticulo().getDescripcion(), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL)));
		    pdfTable.addCell(new Phrase(Double.toString(ir.getCantidad()), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL)));
                    pdfTable.addCell(new Phrase(df.format(ir.getTotal()).replace(',', '.'), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL)));
                }
               
                document.add(pdfTable);
                
                pdfTable = new PdfPTable(new float[] { 330f, 85f, 85f});
                pdfTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfTable.setTotalWidth(500f);
		pdfTable.setLockedWidth(true);
			
		pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                
                pdfTable.addCell(new Phrase(" " , new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL,BaseColor.BLACK)));
                pdfTable.addCell(new Phrase("Total" , new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL,BaseColor.BLACK)));
                pdfTable.addCell(new Phrase(df.format(inventario.getTotal()).replace(',', '.'), new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL)));
		   
                document.add(pdfTable);
                
                document.close();

                fileByte = Files.readAllBytes(file.toPath());
                
                InputStream inputStream = new ByteArrayInputStream(fileByte);
                
                ConfiguracionGeneral cfg = SistemaMantenimiento.getInstance().devolverConfiguracionGeneral();
                File archivo = new File(cfg.getRutaInforme(),  "Inventario" + inventario.getReparto() + inventario.getFecha() + ".pdf");
                
                guardarPDF(inputStream, archivo);
                
                PDDocument documentToPrint = PDDocument.load(new File(cfg.getRutaInforme(),  "Inventario" + inventario.getReparto() + inventario.getFecha() + ".pdf"));

                PrintService myPrintService = findPrintService("Samsung M2020 Series (USB001)");

                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPageable(new PDFPageable(documentToPrint));
                job.setPrintService(myPrintService);
                job.print();
            } catch (Exception e) {
			
            }finally{
		IOUtils.closeQuietly(writerStream); 
		file.deleteOnExit();
            }
    }
    
    public void guardarPDF(InputStream in, File file) {
	try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
	}
    }
    
    private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }
    
    public void imprimirPagina() throws Exception {
        //this.inv = inventario;
        
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(new PageRanges(1, 1));
        aset.add(new Copies(1));

        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);

        try {
            printJob.setPrintService(printService[0]);
            //index of installed printers on you system
            //not sure if default-printer is always '0'
            printJob.print(aset);
            Thread.sleep(10000);
        } catch (PrinterException err) {
            System.err.println(err);
        } catch (InterruptedException ex) {
            
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        
        /*graphics.setFont(new Font("Fuente", 0, 10));

        graphics.setColor(Color.black);
        
        if(numeroDeRenglon < 60) {
            try {
                

                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("dd-MM-yyyy");

                graphics.drawString("Inventario", 200, 60);
                graphics.drawString("Reparto: " + inv.getReparto().getNombre(), 200, 72);
                graphics.drawString("Fecha: " + formatter.format(inv.getFecha()), 200, 84);

                graphics.drawString("Codigo", 45, 96);
                graphics.drawString("Articulo", 110, 96);
                graphics.drawString("Cantidad", 410, 96);
                graphics.drawString("Total", 450, 96);

                int fila = 108;
                
                for (int i = 0; i < inv.getRenglones().size(); i++) {
                    numeroDeRenglon = numeroDeRenglon + 1;
                    proximoRenglonInventario = proximoRenglonInventario + 1;
                    InventarioRenglon ir = inv.getRenglones().get(i);
                    graphics.drawString(Integer.toString(ir.getArticulo().getCodigo()), 45, fila);
                    graphics.drawString(ir.getArticulo().getDescripcion(), 110, fila);
                    graphics.drawString(Double.toString(ir.getCantidad()), 410, fila);
                    graphics.drawString(df.format(ir.getTotal()), 450, fila);
                    fila = fila + 12;
                }
                
                if(inv.getRenglones().size() < 54) {
                
                    graphics.drawString("Total", 410, fila);
                    graphics.drawString(df.format(inv.getTotal()), 450, fila);
                    
                    numeroDeRenglon = 6;
                    proximoRenglonInventario = 1;
                
                } 

                

            } catch (Exception e) {

                System.out.println("LA IMPRESION HA SIDO CANCELADA...");

            }
            return PAGE_EXISTS;
        } else {
            if(numeroDeRenglon < 120) {
                try {
                graphics.drawString("Codigo", 45, 60);
                graphics.drawString("Articulo", 110, 60);
                graphics.drawString("Cantidad", 410, 60);
                graphics.drawString("Total", 450, 60);
                
                int fila = 72;
                
                for (int i = proximoRenglonInventario ; i<inv.getRenglones().size(); i++) {
                    numeroDeRenglon = numeroDeRenglon + 1;
                    proximoRenglonInventario = proximoRenglonInventario + 1;
                    InventarioRenglon ir = inv.getRenglones().get(i);
                    graphics.drawString(Integer.toString(ir.getArticulo().getCodigo()), 45, fila);
                    graphics.drawString(ir.getArticulo().getDescripcion(), 110, fila);
                    graphics.drawString(Double.toString(ir.getCantidad()), 410, fila);
                    graphics.drawString(df.format(ir.getTotal()), 450, fila);
                    fila = fila + 12;
                }
                
                if(inv.getRenglones().size() > 54 && inv.getRenglones().size() < 114) {
                
                    graphics.drawString("Total", 410, fila);
                    graphics.drawString(df.format(inv.getTotal()), 450, fila);
                    
                    numeroDeRenglon = 6;
                    proximoRenglonInventario = 1;
                
                } 
                } catch (Exception e) {

                System.out.println("LA IMPRESION HA SIDO CANCELADA...");

                }
                
                return PAGE_EXISTS;
            } else {
                if(numeroDeRenglon < 180) {
                    try {
                        graphics.drawString("Codigo", 45, 60);
                        graphics.drawString("Articulo", 110, 60);
                        graphics.drawString("Cantidad", 410, 60);
                        graphics.drawString("Total", 450, 60);
                
                        int fila = 72;
                
                        for (int i = proximoRenglonInventario ; i<inv.getRenglones().size(); i++) {
                            numeroDeRenglon = numeroDeRenglon + 1;
                            proximoRenglonInventario = proximoRenglonInventario + 1;
                            InventarioRenglon ir = inv.getRenglones().get(i);
                            graphics.drawString(Integer.toString(ir.getArticulo().getCodigo()), 45, fila);
                            graphics.drawString(ir.getArticulo().getDescripcion(), 110, fila);
                            graphics.drawString(Double.toString(ir.getCantidad()), 410, fila);
                            graphics.drawString(df.format(ir.getTotal()), 450, fila);
                            fila = fila + 12;
                        }
                
                        if(inv.getRenglones().size() > 114 && inv.getRenglones().size() < 174) {
                
                            graphics.drawString("Total", 410, fila);
                            graphics.drawString(df.format(inv.getTotal()), 450, fila);
                            
                            numeroDeRenglon = 6;
                            proximoRenglonInventario = 1;
                
                        } 
                    } catch (Exception e) {

                        System.out.println("LA IMPRESION HA SIDO CANCELADA...");

                    }
                        return PAGE_EXISTS;
                } else {
                    try {
                        graphics.drawString("Codigo", 45, 60);
                        graphics.drawString("Articulo", 110, 60);
                        graphics.drawString("Cantidad", 410, 60);
                        graphics.drawString("Total", 450, 60);

                        int fila = 72;

                        for (int i = proximoRenglonInventario; i < inv.getRenglones().size(); i++) {
                            numeroDeRenglon = numeroDeRenglon + 1;
                            proximoRenglonInventario = proximoRenglonInventario + 1;
                            InventarioRenglon ir = inv.getRenglones().get(i);
                            graphics.drawString(Integer.toString(ir.getArticulo().getCodigo()), 45, fila);
                            graphics.drawString(ir.getArticulo().getDescripcion(), 110, fila);
                            graphics.drawString(Double.toString(ir.getCantidad()), 410, fila);
                            graphics.drawString(df.format(ir.getTotal()), 450, fila);
                            fila = fila + 12;
                        }

                        if (inv.getRenglones().size() > 174 && inv.getRenglones().size() < 234) {

                            graphics.drawString("Total", 410, fila);
                            graphics.drawString(df.format(inv.getTotal()), 450, fila);

                            numeroDeRenglon = 6;
                            proximoRenglonInventario = 1;

                        }
                    } catch (Exception e) {

                        System.out.println("LA IMPRESION HA SIDO CANCELADA...");

                    }
                    return PAGE_EXISTS;
                }
            }
        }*/
        return PAGE_EXISTS;
    }
    
    
    
}
