/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impresion.Facturas;

import dominio.ConfiguracionGeneral;
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
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class ImprimirFacturaGiamo implements ImprimirFactura, Printable, Pageable {

    private DecimalFormat df;

    private Factura factura;

    public ImprimirFacturaGiamo() {
        df = new DecimalFormat("0.00");
    }

    @Override
    public void imprimirFactura(Factura f) throws Exception {
        this.factura = f;
        
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(new PageRanges(1, 1));
            aset.add(new Copies(1));
            //PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            ConfiguracionGeneral cgral = SistemaMantenimiento.getInstance().devolverConfiguracionGeneral();
            PrintService myPrintService = findPrintService(cgral.getNombreImpresora());

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
                Logger.getLogger(ImprimirFacturaGiamo.class.getName()).log(Level.SEVERE, null, ex);
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
        
            g.setFont(new Font("Fuente", 0, 9));

            g.setColor(Color.black);

            try {
                //Tipo Documento
                g.drawString("CONTADO           C      " + this.factura.getNumero() + "     " , 430, 57);
                //Rut
                g.drawString(this.factura.getCliente().getRut(), 260, 108);
                if ("".equals(this.factura.getCliente().getRut())) {
                    g.drawString("X", 410, 108);
                }
                //Fecha
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                g.drawString(formatter.format(this.factura.getFecha()), 470, 108);
                //Nombre
                g.drawString(this.factura.getCliente().getRazonSocial(), 95, 160);
                //Direccion
                g.drawString(this.factura.getCliente().getDireccion(), 95, 180);
                //Detalle 1
                
                int y = 215;
                double totalMinimo = 0;
                double totalBasico = 0;
                for (FacturaRenglon fr : this.factura.getRenglones()) {
                    int largoDesc = fr.getArticulo().getDescripcion().length();
                    if (largoDesc > 50) {
                        largoDesc = 50;
                    }
                    g.drawString(df.format(fr.getCantidad()).replace(',', '.') + " ",  38, y);
                    g.drawString(fr.getArticulo().getDescripcion().substring(0, largoDesc), 95, y);
                    //.drawString(fr.getPrecio() + " " , 420, y);
                    //g.drawString(fr.getDescuento() + " ", 380, y);
                    g.drawString(df.format(fr.getPrecio()).replace(',', '.') + " ", 420, y);
                    g.drawString(df.format(fr.getTotal()).replace(',', '.'), 500, y);
                    if(fr.getArticulo().getIva().getId() == Constantes.ivaExcento) {
                        g.drawString(df.format(fr.getTotal()).replace(',', '.'), 530, 0);
                    }
                    if(fr.getArticulo().getIva().getId() == Constantes.ivaMinimo) {
                        totalMinimo = totalMinimo + fr.getTotal();
                        g.drawString(df.format(fr.getTotal()).replace(',', '.'), 530, fr.getArticulo().getIva().getPorcentaje());
                    }
                    if(fr.getArticulo().getIva().getId() == Constantes.ivaBasico) {
                        totalBasico = totalBasico + fr.getTotal();
                        g.drawString(df.format(fr.getTotal()).replace(',', '.'), 530, fr.getArticulo().getIva().getPorcentaje());
                    }
                    
                    y = y + 12;
                }
                //Total
                
                if(factura.getTotalMinimo() > 0) {
                    g.drawString("IVA 10% " + df.format(totalMinimo).replace(',', '.'), 230, 320);
                    g.drawString(df.format(this.factura.getTotalMinimo()).replace(',', '.'), 350, 320);
                }
                if(factura.getTotalBasico() > 0) {
                    g.drawString("IVA 22% " + df.format(totalBasico).replace(',', '.'), 230, 340);
                    g.drawString(df.format(this.factura.getTotalBasico()).replace(',', '.'), 350, 340);
                }
                
                g.drawString(df.format(this.factura.getTotal()).replace(',', '.'), 500, 360);
                
                //-------------------COPIA ARCHIVO-------------------------------------------------------------------
                //Tipo Documento
                g.drawString("CONTADO           C      " + this.factura.getNumero() + "     " , 430, 470);
                //Rut
                g.drawString(this.factura.getCliente().getRut(), 260, 520);
                if ("".equals(this.factura.getCliente().getRut())) {
                    g.drawString("X", 410, 520);
                }
                //Fecha
                g.drawString(formatter.format(this.factura.getFecha()), 470, 520);
                //Nombre
                g.drawString(this.factura.getCliente().getRazonSocial(), 95, 560);
                //Direccion
                g.drawString(this.factura.getCliente().getDireccion(), 95, 580);
                //Detalle 1
                
                y = 625;
                for (FacturaRenglon fr : this.factura.getRenglones()) {
                    int largoDesc = fr.getArticulo().getDescripcion().length();
                    if (largoDesc > 50) {
                        largoDesc = 50;
                    }
                    g.drawString(df.format(fr.getCantidad()).replace(',', '.') + " ",  38, y);
                    g.drawString(fr.getArticulo().getDescripcion().substring(0, largoDesc), 95, y);
                    //.drawString(fr.getPrecio() + " " , 420, y);
                    //g.drawString(fr.getDescuento() + " ", 380, y);
                    g.drawString(df.format(fr.getPrecio()).replace(',', '.') + " ", 420, y);
                    g.drawString(df.format(fr.getTotal()).replace(',', '.'), 500, y);
                    if(fr.getArticulo().getIva().getId() == Constantes.ivaExcento) {
                        g.drawString(df.format(fr.getTotal()).replace(',', '.'), 550, 0);
                    }
                    if(fr.getArticulo().getIva().getId() == Constantes.ivaMinimo) {
                        totalMinimo = totalMinimo + fr.getTotal();
                        g.drawString(df.format(fr.getTotal()).replace(',', '.'), 550, fr.getArticulo().getIva().getPorcentaje());
                    }
                    if(fr.getArticulo().getIva().getId() == Constantes.ivaBasico) {
                        totalBasico = totalBasico + fr.getTotal();
                        g.drawString(df.format(fr.getTotal()).replace(',', '.'), 550, fr.getArticulo().getIva().getPorcentaje());
                    }
                    
                    y = y + 12;
                }
                //Total
                
                if(factura.getTotalMinimo() > 0) {
                    g.drawString("IVA 10% " + df.format(totalMinimo).replace(',', '.'), 230, 720);
                    g.drawString(df.format(this.factura.getTotalMinimo()).replace(',', '.'), 350, 720);
                }
                if(factura.getTotalBasico() > 0) {
                    g.drawString("IVA 22% " + df.format(totalBasico).replace(',', '.'), 230, 740);
                    g.drawString(df.format(this.factura.getTotalBasico()).replace(',', '.'), 350, 740);
                }
                
                g.drawString(df.format(this.factura.getTotal()).replace(',', '.'), 500, 760);
                

                
            } catch (Exception e) {

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
