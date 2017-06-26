/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impresion.Facturas;

import dominio.DocumentoDeVenta;
import dominio.Factura;
import dominio.FacturaRenglon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PageRanges;
import sistema.SistemaMantenimiento;

/**
 *
 * @author Edu
 */
public class ImprimirFacturaRelece implements ImprimirFactura, Printable, Pageable {

    private DecimalFormat df;

    private Factura factura;
    private String pieClienteArchivo;

    public ImprimirFacturaRelece() {
        df = new DecimalFormat("0.00");
    }

    @Override
    public void imprimirFactura(Factura f) throws Exception {
        this.factura = f;
        DocumentoDeVenta contadoCerram = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Cerram");
        if(f.getTipoDocumento().getId() == contadoCerram.getId()) {
            this.pieClienteArchivo = "1.Cliente";
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(new PageRanges(1, 1));
            aset.add(new Copies(1));
            //PrintService printService = PrintServiceLookup.lookupDefaultPrintService();

            PrintService myPrintService = findPrintService("Samsung M2020 Series (USB001)");

            PrinterJob printJob = PrinterJob.getPrinterJob();

            PageFormat format = printJob.getPageFormat(null);
            Paper paper = format.getPaper();
            //Remove borders from the paper
            paper.setImageableArea(0.0, 0.0, format.getPaper().getWidth(), format.getPaper().getHeight());
            format.setPaper(paper);

            printJob.setPrintable(this, format);

            try {
                printJob.setPrintService(myPrintService);
                //index of installed printers on you system
                //not sure if default-printer is always '0'
                printJob.print(aset);
                Thread.sleep(5000);
            } catch (PrinterException err) {
                System.err.println(err);
            } catch (InterruptedException ex) {
                Logger.getLogger(ImprimirFacturaRelece.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Imprimo la copia
            this.pieClienteArchivo = "2.Archivo";
            aset = new HashPrintRequestAttributeSet();
            aset.add(new PageRanges(1, 1));
            aset.add(new Copies(1));
            //PrintService printService = PrintServiceLookup.lookupDefaultPrintService();

            myPrintService = findPrintService("Samsung M2020 Series (USB001)");

            printJob = PrinterJob.getPrinterJob();

            format = printJob.getPageFormat(null);
            paper = format.getPaper();
            //Remove borders from the paper
            paper.setImageableArea(0.0, 0.0, format.getPaper().getWidth(), format.getPaper().getHeight());
            format.setPaper(paper);

            printJob.setPrintable(this, format);

            try {
                printJob.setPrintService(myPrintService);
                //index of installed printers on you system
                //not sure if default-printer is always '0'
                printJob.print(aset);
                Thread.sleep(10000);
            } catch (PrinterException err) {
                System.err.println(err);
            } catch (InterruptedException ex) {
                Logger.getLogger(ImprimirFacturaRelece.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(new PageRanges(1, 1));
            aset.add(new Copies(1));
            //PrintService printService = PrintServiceLookup.lookupDefaultPrintService();

            PrintService myPrintService = findPrintService("Samsung M2020 Series (USB001)");

            PrinterJob printJob = PrinterJob.getPrinterJob();

            PageFormat format = printJob.getPageFormat(null);
            Paper paper = format.getPaper();
            //Remove borders from the paper
            paper.setImageableArea(0.0, 0.0, format.getPaper().getWidth(), format.getPaper().getHeight());
            format.setPaper(paper);

            printJob.setPrintable(this, format);

            try {
                printJob.setPrintService(myPrintService);
                //index of installed printers on you system
                //not sure if default-printer is always '0'
                printJob.print(aset);
                Thread.sleep(10000);
            } catch (PrinterException err) {
                System.err.println(err);
            } catch (InterruptedException ex) {
                Logger.getLogger(ImprimirFacturaRelece.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    @Override
    public void imprimirListaDeFacturas(List<Factura> facturas) throws Exception {
        for(Factura f: facturas){
            this.imprimirFactura(f); 
        }
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        //DocumentoDeVenta contadoRelece = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Relece");
        DocumentoDeVenta contadoCerram = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Cerram");
        if(!(this.factura.getTipoDocumento().getId() == contadoCerram.getId())) {
            g.setFont(new Font("Fuente", 0, 10));

            g.setColor(Color.black);
        
            try{
            //Tipo Documento
            g.drawString("CONTADO B " + this.factura.getNumero(), 175, 60);
            //Rut
            g.drawString(this.factura.getCliente().getRut(), 20, 110);
            if ("".equals(this.factura.getCliente().getRut())) {
                g.drawString("X", 190, 110);
            }
            //Fecha
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            g.drawString(formatter.format(this.factura.getFecha()), 215, 110);
            //Nombre
            g.drawString(this.factura.getCliente().getRazonSocial(), 55, 135);
            //Direccion
            g.drawString(this.factura.getCliente().getDireccion(), 60, 150);
            //Detalle 1
            /*Articulo lComun = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(11111);
            Articulo lUltra = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(22222);
            for (FacturaRenglon fr : this.factura.getRenglones()) {
            if(fr.getArticulo().equals(lComun)){
                g.drawString(fr.getCantidad() + " Litros Leche Común", 18, 220);
                g.drawString(df.format(fr.getTotal()), 210, 220);
            }
            //Detalle 2
            if (fr.getArticulo().equals(lUltra)) {
                g.drawString(fr.getCantidad() + " Litros Leche Ultra", 18, 240);
                g.drawString(df.format(fr.getTotal()), 210, 240);
            }
            }*/
            int y = 200;
            for(FacturaRenglon fr : this.factura.getRenglones()) {
                int largoDesc = fr.getArticulo().getDescripcion().length();
                if(largoDesc > 25) {
                    largoDesc = 25;
                }
                g.drawString(fr.getCantidad() + " " + fr.getArticulo().getDescripcion().substring(0, largoDesc), 18, y);
                g.drawString(df.format(fr.getTotal()), 210, y);
                y = y + 20;
            }
            //Total
            g.drawString(df.format(this.factura.getTotal()), 200, 380);
            //LineaMedio
            //e.Graphics.DrawString("|", new Font("Arial", 12), Brushes.Black, 400, 44);

            //Copia
            //TipoDocumento Copia
            g.drawString("CONTADO B " + this.factura.getNumero(), 450, 60);
            //RUC Copia
            g.drawString(this.factura.getCliente().getRut(), 350, 115);
            //Consumo Final Copia
            if ("".equals(this.factura.getCliente().getRut())) {
                g.drawString("X", 450, 110);
            }
            //Fecha Copia
            g.drawString(formatter.format(this.factura.getFecha()), 510, 110);
            //Nombre Copia
            g.drawString(this.factura.getCliente().getRazonSocial(), 375, 135);
            //Direccion Copia
            g.drawString(this.factura.getCliente().getDireccion(), 375, 150);
            //Detalle 1 Copia
            /*for (FacturaRenglon fr : this.factura.getRenglones()) {
                if(fr.getArticulo().equals(lComun)){
                    g.drawString(fr.getCantidad() + " Litros Leche Común",320, 220);
                    g.drawString(df.format(fr.getTotal()), 510, 220);
                }
                //Detalle 2
                if (fr.getArticulo().equals(lUltra)) {
                    g.drawString(fr.getCantidad() + " Litros Leche Ultra", 320, 240);
                    g.drawString(df.format(fr.getTotal()), 510, 240);
                }
            }*/
            y = 200;
            for(FacturaRenglon fr : this.factura.getRenglones()) {
                int largoDesc = fr.getArticulo().getDescripcion().length();
                if(largoDesc > 25) {
                    largoDesc = 25;
                }
                g.drawString(fr.getCantidad() + " " + fr.getArticulo().getDescripcion().substring(0, largoDesc), 320, y);
                g.drawString(df.format(fr.getTotal()), 510, y);
                y = y + 20;
            }
        
            //Total Copia
            g.drawString(df.format(this.factura.getTotal()), 500, 380);
            } catch (Exception e){
            
            }
        } else {
            //Es contado Cerram
            g.setFont(new Font("Fuente", 0, 9));

            g.setColor(Color.black);

            try {
                //Tipo Documento
                g.drawString("CONTADO C                          " + this.factura.getNumero(), 390, 85);
                //Rut
                g.drawString(this.factura.getCliente().getRut(), 390, 130);
                if ("".equals(this.factura.getCliente().getRut())) {
                    g.drawString("X", 540, 130);
                }
                //Fecha
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                g.drawString(formatter.format(this.factura.getFecha()), 290, 85);
                //Nombre
                g.drawString(this.factura.getCliente().getRazonSocial(), 75, 110);
                //Direccion
                g.drawString(this.factura.getCliente().getDireccion(), 75, 130);
                //Detalle 1
                /*Articulo lComun = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(11111);
            Articulo lUltra = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(22222);
            for (FacturaRenglon fr : this.factura.getRenglones()) {
            if(fr.getArticulo().equals(lComun)){
                g.drawString(fr.getCantidad() + " Litros Leche Común", 18, 220);
                g.drawString(df.format(fr.getTotal()), 210, 220);
            }
            //Detalle 2
            if (fr.getArticulo().equals(lUltra)) {
                g.drawString(fr.getCantidad() + " Litros Leche Ultra", 18, 240);
                g.drawString(df.format(fr.getTotal()), 210, 240);
            }
            }*/
                int y = 180;
                for (FacturaRenglon fr : this.factura.getRenglones()) {
                    int largoDesc = fr.getArticulo().getDescripcion().length();
                    if (largoDesc > 25) {
                        largoDesc = 25;
                    }
                    g.drawString(fr.getArticulo().getCodigo() + " ",  38, y);
                    g.drawString(fr.getArticulo().getDescripcion().substring(0, largoDesc), 95, y);
                    g.drawString(fr.getCantidad() + " " , 330, y);
                    g.drawString(fr.getDescuento() + " ", 380, y);
                    g.drawString(df.format(fr.getPrecio()).replace(',', '.') + " ", 430, y);
                    g.drawString(df.format(fr.getTotal()).replace(',', '.'), 480, y);
                    y = y + 16;
                }
                //Total
                g.drawString(df.format(this.factura.getSubtotal()).replace(',', '.'), 500, 740);
                g.drawString("0.00", 500, 760);
                g.drawString(df.format(this.factura.getTotalMinimo()).replace(',', '.'), 500, 775);
                g.drawString(df.format(this.factura.getTotalBasico()).replace(',', '.'), 500, 792);
                g.drawString(df.format(this.factura.getTotal()).replace(',', '.'), 500, 810);
                g.drawString(pieClienteArchivo, 320, 805);
                //LineaMedio
                //e.Graphics.DrawString("|", new Font("Arial", 12), Brushes.Black, 400, 44);

                
            } catch (Exception e) {

            }
        }
        return PAGE_EXISTS;
    }

    @Override
    public int getNumberOfPages() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
