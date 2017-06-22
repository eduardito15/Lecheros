/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;

import dao.GenericDAO;
import dominio.Anep;
import dominio.Articulo;
import dominio.Cheque;
import dominio.Chofer;
import dominio.Cliente;
import dominio.Comision;
import dominio.Compra;
import dominio.CompraRenglon;
import dominio.DocumentoDeCompra;
import dominio.Factura;
import dominio.FacturaRenglon;
import dominio.FamiliaDeProducto;
import dominio.Gasto;
import dominio.GastoRenglon;
import dominio.GrupoCliente;
import dominio.Inau;
import dominio.Inventario;
import dominio.InventarioRenglon;
import dominio.JornalRenglon;
import dominio.Jornales;
import dominio.Liquidacion;
import dominio.Precio;
import dominio.Reparto;
import dominio.Retencion;
import dominio.RubroGasto;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import util.Util;

/**
 *
 * @author Edu
 */
public class SistemaInformes {

    private static SistemaInformes instance;
    SimpleDateFormat formatter;

    /**
     * @return the instance
     */
    public static SistemaInformes getInstance() {
        if (instance == null) {
            instance = new SistemaInformes();
        }
        return instance;
    }

    private SistemaInformes() {
        formatter = new SimpleDateFormat("dd-MM-yyyy");
    }

    public boolean hayInventarioDelDiaAnterior(Date fecha, Reparto r) throws Exception {
        boolean retorno = false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.DATE, -1);
        Inventario i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(cal.getTime(), r);
        if (i != null) {
            retorno = true;
        }
        return retorno;
    }

    public String[][] devolverInformeEnvases(Date fechaDesde, Date fechaHasta, Reparto r) throws Exception {
        String[][] retorno = new String[3][6];
        retorno[0][0] = "320009";
        retorno[1][0] = "320017";
        retorno[2][0] = "320069";

        int ceronueve = 0;
        int diecisiete = 0;
        int sesentaynueve = 0;

        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaDesde);
        cal.add(Calendar.DATE, -1);
        Inventario i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(cal.getTime(), r);
        for (InventarioRenglon ir : i.getRenglones()) {
            if (ir.getArticulo().getCodigo() == 320009) {
                Double cant = ir.getCantidad();
                ceronueve = ceronueve + cant.intValue();
            }
            if (ir.getArticulo().getCodigo() == 320017) {
                Double cant = ir.getCantidad();
                diecisiete = diecisiete + cant.intValue();
            }
            if (ir.getArticulo().getCodigo() == 320069) {
                Double cant = ir.getCantidad();
                sesentaynueve = sesentaynueve + cant.intValue();
            }
        }

        retorno[0][1] = Integer.toString(ceronueve);
        retorno[1][1] = Integer.toString(diecisiete);
        retorno[2][1] = Integer.toString(sesentaynueve);

        ceronueve = 0;
        diecisiete = 0;
        sesentaynueve = 0;

        int cincuentaynueveDev = 0;
        int diecisieteDev = 0;
        int sesentaynueveDev = 0;

        List<Compra> compras = this.devolverComprasEntreFechaDeLiquidacionYReparto(fechaDesde, fechaHasta, r);
        for (Compra c : compras) {
            if (c.getTipoDocumento().isSuma()) {
                for (CompraRenglon cr : c.getRenglones()) {
                    if (cr.getArticulo().getCodigo() == 320009 || cr.getArticulo().getCodigo() == 320017 || cr.getArticulo().getCodigo() == 320069) {
                        if (cr.getArticulo().getCodigo() == 320009) {
                            Double cant = cr.getCantidad();
                            ceronueve = ceronueve + cant.intValue();
                        }
                        if (cr.getArticulo().getCodigo() == 320017) {
                            Double cant = cr.getCantidad();
                            diecisiete = diecisiete + cant.intValue();
                        }
                        if (cr.getArticulo().getCodigo() == 320069) {
                            Double cant = cr.getCantidad();
                            sesentaynueve = sesentaynueve + cant.intValue();
                        }
                    } else {
                        break;
                    }
                }
            }
            if (!c.getTipoDocumento().isSuma()) {
                for (CompraRenglon cr : c.getRenglones()) {
                    if (cr.getArticulo().getCodigo() != 320009 || cr.getArticulo().getCodigo() != 320017 || cr.getArticulo().getCodigo() != 320069) {
                        if (cr.getArticulo().getCodigo() == 320009) {
                            Double cant = cr.getCantidad();
                            cincuentaynueveDev = cincuentaynueveDev + cant.intValue();
                        }
                        if (cr.getArticulo().getCodigo() == 320017) {
                            Double cant = cr.getCantidad();
                            diecisieteDev = diecisieteDev + cant.intValue();
                        }
                        if (cr.getArticulo().getCodigo() == 320069) {
                            Double cant = cr.getCantidad();
                            sesentaynueveDev = sesentaynueveDev + cant.intValue();
                        }
                    } else {
                        break;
                    }

                }
            }
        }

        retorno[0][2] = Integer.toString(ceronueve);
        retorno[1][2] = Integer.toString(diecisiete);
        retorno[2][2] = Integer.toString(sesentaynueve);

        retorno[0][3] = Integer.toString(cincuentaynueveDev);
        retorno[1][3] = Integer.toString(diecisieteDev);
        retorno[2][3] = Integer.toString(sesentaynueveDev);

        Inventario iFinal = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaHasta, r);

        ceronueve = 0;
        diecisiete = 0;
        sesentaynueve = 0;

        if (iFinal != null) {
            for (InventarioRenglon ir : iFinal.getRenglones()) {
                if (ir.getArticulo().getCodigo() == 320009) {
                    Double cant = ir.getCantidad();
                    ceronueve = ceronueve + cant.intValue();
                }
                if (ir.getArticulo().getCodigo() == 320017) {
                    Double cant = ir.getCantidad();
                    diecisiete = diecisiete + cant.intValue();
                }
                if (ir.getArticulo().getCodigo() == 320069) {
                    Double cant = ir.getCantidad();
                    sesentaynueve = sesentaynueve + cant.intValue();
                }
            }
        }

        retorno[0][4] = Integer.toString(ceronueve);
        retorno[1][4] = Integer.toString(diecisiete);
        retorno[2][4] = Integer.toString(sesentaynueve);

        int diferenciaceronueve = Integer.parseInt(retorno[0][1]) + Integer.parseInt(retorno[0][2]) - Integer.parseInt(retorno[0][3]) - Integer.parseInt(retorno[0][4]);
        int diferenciadiecisiete = Integer.parseInt(retorno[1][1]) + Integer.parseInt(retorno[1][2]) - Integer.parseInt(retorno[1][3]) - Integer.parseInt(retorno[1][4]);
        int diferenciasesentaynueve = Integer.parseInt(retorno[2][1]) + Integer.parseInt(retorno[2][2]) - Integer.parseInt(retorno[2][3]) - Integer.parseInt(retorno[2][4]);

        retorno[0][5] = Integer.toString(diferenciaceronueve);
        retorno[1][5] = Integer.toString(diferenciadiecisiete);
        retorno[2][5] = Integer.toString(diferenciasesentaynueve);

        return retorno;
    }

    public List<Compra> devolverComprasEntreFechaDeLiquidacionYReparto(Date fechaDesde, Date fechaHasta, Reparto c) throws Exception {
        List<Compra> retorno = null;
        //SimpleDateFormat formatter;
        //formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaDesde);
        //cal.add(Calendar.DATE, -1);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Compra WHERE (fechaLiquidacion BETWEEN :stDate AND :edDate" + ") and reparto_id = " + c.getId());
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public String[] devolverInformeEnvasesCerramEntreFechasYReparto(Date fechaDesde, Date fechaHasta, Reparto r) throws Exception {
        String[] retorno = new String[3];
        List<Compra> compras = this.devolverComprasEntreFechaDeLiquidacionYReparto(fechaDesde, fechaHasta, r);
        int comps = 0;
        int devs = 0;
        int dif = 0;
        for (Compra c : compras) {
            if ("Factura Cerram".equals(c.getTipoDocumento().getTipoDocumento())) {
                for (CompraRenglon cr : c.getRenglones()) {
                    if (cr.getArticulo().getCodigo() == 320009) {
                        Double cant = cr.getCantidad();
                        comps = comps + cant.intValue();
                    } 
                }
            }
            if ("Nota de Credito Cerram".equals(c.getTipoDocumento().getTipoDocumento())) {
                for (CompraRenglon cr : c.getRenglones()) {
                    if (cr.getArticulo().getCodigo() == 320009) {
                        Double cant = cr.getCantidad();
                        devs = devs + cant.intValue();
                    } 
                }
            }
        }

        dif = comps - devs;

        retorno[0] = Integer.toString(comps);
        retorno[1] = Integer.toString(devs);
        retorno[2] = Integer.toString(dif);

        return retorno;
    }

    public String[][] devolverInformePinchadas(Date fechaDesde, Date fechaHasta, Reparto r) throws Exception {
        String[][] retorno = new String[10][5];
        DecimalFormat df;
        df = new DecimalFormat("0.00");

        FamiliaDeProducto fp = SistemaMantenimiento.getInstance().devolverFamiliaPorCodigo(0);
        List<Articulo> articulos = SistemaMantenimientoArticulos.getInstance().devolverArticulosPorFamilia(fp);
        List<Compra> compras = this.devolverComprasEntreFechaDeLiquidacionYReparto(fechaDesde, fechaHasta, r);
        List<Compra> comprasInternas = new ArrayList<>();
        for(Compra comp : compras) {
            if(comp.getTipoDocumento().isEsDocumentoInterno()){
                comprasInternas.add(comp);
            }
        }
        compras.removeAll(comprasInternas);
        int fila = 0;
        for (Articulo a : articulos) {
            if (a.getCodigo() >= 110 && a.getCodigo() <= 134) {
                double totalCompras = 0;
                double totalRemitos = 0;
                for (Compra c : compras) {
                    if (!"Remito Devolución".equals(c.getTipoDocumento().getTipoDocumento())) {
                        for (CompraRenglon cr : c.getRenglones()) {
                            if (cr.getArticulo().getFamilia().getCodigo() != 0) {
                                break;
                            }
                            if (cr.getArticulo().getCodigo() == a.getCodigo()) {
                                if (c.getTipoDocumento().isSuma()) {
                                    totalCompras = totalCompras + cr.getCantidad();
                                } else if (!c.getTipoDocumento().isSuma()) {
                                    totalCompras = totalCompras - cr.getCantidad();
                                }
                            }
                        }
                    } else if ("Remito Devolución".equals(c.getTipoDocumento().getTipoDocumento())) {
                        for (CompraRenglon cr : c.getRenglones()) {
                            if (cr.getArticulo().getCodigo() == a.getCodigo()) {
                                totalRemitos = totalRemitos + cr.getCantidad();
                            }
                        }
                    }
                }
                if (totalCompras != 0 || totalRemitos != 0) {
                    //Armo el Renglon de la tabla resultado del informe!
                    retorno[fila][0] = Integer.toString(a.getCodigo());
                    retorno[fila][1] = Double.toString(totalCompras);
                    retorno[fila][2] = Double.toString(totalRemitos);
                    //Caluclo 2 * 1000
                    double dosPorMil = (totalCompras * 2) / 1000;
                    double diferencia = totalRemitos - dosPorMil;

                    retorno[fila][3] = df.format(dosPorMil).replace(',', '.');
                    retorno[fila][4] = df.format(diferencia).replace(',', '.');

                    fila++;
                }
            }
        }

        //Calculo los totales y se los cargo a la matriz resultado
        double totalComp = 0;
        double totalPinch = 0;
        double totalDosPorMil = 0;

        for (int i = 0; i < fila; i++) {
            totalComp = totalComp + Double.parseDouble(retorno[i][1]);
            totalPinch = totalPinch + Double.parseDouble(retorno[i][2]);
            totalDosPorMil = totalDosPorMil + Double.parseDouble(retorno[i][3]);
        }
        fila++;
        retorno[fila][0] = "Total";
        retorno[fila][1] = df.format(totalComp).replace(',', '.');
        retorno[fila][2] = df.format(totalPinch).replace(',', '.');;
        retorno[fila][3] = df.format(totalDosPorMil).replace(',', '.');;

        return retorno;
    }

    public boolean esComisionPorPorcentaje(Articulo a) throws Exception {
        boolean retorno = true;
        if (esEnvase(a) || esLeche(a) || a.getCodigo() == 8000) {
            retorno = false;
        }
        return retorno;
    }

    public boolean esEnvase(Articulo a) throws Exception {
        boolean retorno = false;
        /*if (a.getCodigo() == 320009 || a.getCodigo() == 320017 || a.getCodigo() == 320069 || a.getCodigo() == 32101) {
            retorno = true;
        }*/
        retorno = Util.esEnvase(a);
        return retorno;
    }

    public boolean esLeche(Articulo a) throws Exception {
        boolean retorno = false;
        /*if (a.getCodigo() == 110 || a.getCodigo() == 112 || a.getCodigo() == 113 || a.getCodigo() == 114 || a.getCodigo() == 115 || a.getCodigo() == 116 || a.getCodigo() == 119 || a.getCodigo() == 122 || a.getCodigo() == 123 || a.getCodigo() == 128 || a.getCodigo() == 131 || a.getCodigo() == 132 || a.getCodigo() == 133 || a.getCodigo() == 134 || a.getCodigo() == 1337 || a.getCodigo() == 1338) {
            retorno = true;
        }*/
        retorno = Util.esLeche(a);
        return retorno;
    }

    public String[][] informeComision(Date fechaDesde, Date fechaHasta, Chofer chofer) throws Exception {
        String[][] retorno = new String[70][3];
        Comision comision = chofer.getComision();
        List<Liquidacion> liquidaciones = SistemaLiquidaciones.getInstance().devolverLiquidacionesEntreFechasYChofer(fechaDesde, fechaHasta, chofer);
        Date ultimaFechaCalculada = null;
        Reparto ultimoRepartoCalculado = null;
        if (!liquidaciones.isEmpty()) {
            ultimaFechaCalculada = liquidaciones.get(0).getFecha();
            ultimoRepartoCalculado = liquidaciones.get(0).getReparto();
        } 
        int filaInforme = 1;
        double total = 0;
        for (int i = 0; i < liquidaciones.size(); i++) {
            Liquidacion l = liquidaciones.get(i);

            if (i == 0) {
                //Es la primer liquidacion tengo que buscar un inventario del dia anterior
                Calendar fechaInvInicial = Calendar.getInstance();
                fechaInvInicial.setTime(l.getFecha());
                fechaInvInicial.add(Calendar.DATE, -1);
                int diaDeLaSemana = fechaInvInicial.get(Calendar.DAY_OF_WEEK);
                if (diaDeLaSemana == 1) {
                    fechaInvInicial.add(Calendar.DATE, -1);
                }
                Inventario invInicial = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaInvInicial.getTime(), l.getReparto());
                //Calculo la comision del inventario para sumar el inventario inicial
                double comisionInvInicial = 0;
                if (invInicial != null) {
                    for (InventarioRenglon ir : invInicial.getRenglones()) {
                        if (esComisionPorPorcentaje(ir.getArticulo())) {
                            Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(ir.getArticulo(), invInicial.getFecha());
                            double comisionRenglon = ((ir.getSubtotal()) * comision.getPorcentaje()) / 100;
                            comisionInvInicial = comisionInvInicial + comisionRenglon;
                        } else {
                            if (esLeche(ir.getArticulo())) {
                            //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(ir.getArticulo(), invInicial.getFecha());
                                Precio pLeche = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(ir.getArticulo(), invInicial.getFecha());
                                double comisionRenglon = ((ir.getCantidad() * pLeche.getPrecioCompra()) * comision.getPorcentajeLeche()) / 100;
                                comisionInvInicial = comisionInvInicial + comisionRenglon;
                            }
                        }
                    }
                    total = total + comisionInvInicial;
                    retorno[0][0] = "Inventario Inicial";
                    retorno[0][1] = formatter.format(invInicial.getFecha());
                    retorno[0][2] = Double.toString(comisionInvInicial);
                }
            }
            boolean esConsecutiva = false;
            Calendar cal = Calendar.getInstance();
            cal.setTime(ultimaFechaCalculada);
            cal.add(Calendar.DATE, 1);
            Date date = cal.getTime();
            if (l.getFecha().equals(date)) {
                esConsecutiva = true;
            }
            if (!l.getReparto().equals(ultimoRepartoCalculado)) {
                esConsecutiva = false;
            }
            Calendar fechaLiq = Calendar.getInstance();
            fechaLiq.setTime(l.getFecha());
            fechaLiq.add(Calendar.DATE, -1);
            int diaDeLaSemana = fechaLiq.get(Calendar.DAY_OF_WEEK);
            if (l.getFecha() == ultimaFechaCalculada || esConsecutiva || diaDeLaSemana == 1 || sonVaciosLosDiasDeDiferencia(ultimaFechaCalculada, l.getFecha(), l.getReparto())) {
                //La fecha es consecutiva
                List<Compra> compras = SistemaLiquidaciones.getInstance().devolverComprasParaFechaYReparto(l.getFecha(), l.getReparto());
                double comisionCompras = 0;
                double comisionComprasLeche = 0;
                for (Compra c : compras) {
                    if (!c.getTipoDocumento().isEsDocumentoInterno()) {
                        if (c.getTipoDocumento().isSuma()) {
                            for (CompraRenglon cr : c.getRenglones()) {
                                if (esComisionPorPorcentaje(cr.getArticulo())) {
                                    //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(cr.getArticulo(), c.getFecha());
                                    double comisionRenglon = ((cr.getTotalPrecioVentaSinIva()) * comision.getPorcentaje()) / 100;
                                    comisionCompras = comisionCompras + comisionRenglon;
                                } else {
                                    if (esLeche(cr.getArticulo())) {
                                    //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(cr.getArticulo(), c.getFecha());
                                        Precio pLeche = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(cr.getArticulo(), c.getFecha());
                                        double comisionRenglon = ((cr.getCantidad() * pLeche.getPrecioCompra()) * comision.getPorcentajeLeche()) / 100;
                                        comisionComprasLeche = comisionComprasLeche + comisionRenglon;
                                    }
                                }
                            }
                        }
                        if (!c.getTipoDocumento().isSuma()) {
                            for (CompraRenglon cr : c.getRenglones()) {
                                if (esComisionPorPorcentaje(cr.getArticulo())) {
                                    //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(cr.getArticulo(), c.getFecha());
                                    double comisionRenglon = ((cr.getTotalPrecioVentaSinIva()) * comision.getPorcentaje()) / 100;
                                    comisionCompras = comisionCompras - comisionRenglon;
                                } else {
                                    if (esLeche(cr.getArticulo())) {
                                        //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(cr.getArticulo(), c.getFecha());
                                        Precio pLeche = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(cr.getArticulo(), c.getFecha());
                                        double comisionRenglon = ((cr.getCantidad() * pLeche.getPrecioCompra()) * comision.getPorcentajeLeche()) / 100;
                                        comisionComprasLeche = comisionComprasLeche - comisionRenglon;
                                    }
                                }
                            }
                        }
                    }
                }
                if (comisionCompras != 0) {
                    total = total + comisionCompras;
                    retorno[filaInforme][0] = "Compras Productos";
                    retorno[filaInforme][1] = formatter.format(l.getFecha());
                    retorno[filaInforme][2] = Double.toString(comisionCompras);
                    filaInforme++;
                }
                if (comisionComprasLeche != 0) {
                    total = total + comisionComprasLeche;
                    retorno[filaInforme][0] = "Compras Leche";
                    retorno[filaInforme][1] = formatter.format(l.getFecha());
                    retorno[filaInforme][2] = Double.toString(comisionComprasLeche);
                    filaInforme++;
                }

            } else {
                //La fecha no es consecutiva. El inventario de ese dia como el final para restarle y busco el proximo inventario si hay para sumarle
                if (i > 1) {
                    Inventario invFin = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(ultimaFechaCalculada, liquidaciones.get(i - 1).getReparto());

                    double comisionInvFin = 0;
                    for (InventarioRenglon ir : invFin.getRenglones()) {
                        if (esComisionPorPorcentaje(ir.getArticulo())) {
                            //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(ir.getArticulo(), invFin.getFecha());
                            double comisionRenglon = ((ir.getSubtotal()) * comision.getPorcentaje()) / 100;
                            comisionInvFin = comisionInvFin + comisionRenglon;
                        } else { 
                            if (esLeche(ir.getArticulo())) {
                                //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(ir.getArticulo(), invFin.getFecha());
                                Precio pLeche = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(ir.getArticulo(), invFin.getFecha());
                                double comisionRenglon = ((ir.getCantidad() * pLeche.getPrecioCompra()) * comision.getPorcentajeLeche()) / 100;
                                comisionInvFin = comisionInvFin + comisionRenglon;
                            }
                        }
                    }
                    total = total - comisionInvFin;
                    retorno[filaInforme][0] = "Inventario Final";
                    retorno[filaInforme][1] = formatter.format(invFin.getFecha());
                    retorno[filaInforme][2] = "-" + Double.toString(comisionInvFin);
                    filaInforme++;
                }
                Calendar fechaInvInicial = Calendar.getInstance();
                fechaInvInicial.setTime(l.getFecha());
                fechaInvInicial.add(Calendar.DATE, -1);
                Inventario invIni = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaInvInicial.getTime(), l.getReparto());

                //OJO Cuando el invIni es null
                double comisionInvIni = 0;
                for (InventarioRenglon ir : invIni.getRenglones()) {
                    if (esComisionPorPorcentaje(ir.getArticulo())) {
                        //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(ir.getArticulo(), invIni.getFecha());
                        double comisionRenglon = ((ir.getSubtotal()) * comision.getPorcentaje()) / 100;
                        comisionInvIni = comisionInvIni + comisionRenglon;
                    } else {
                        if (esLeche(ir.getArticulo())) {
                            //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(ir.getArticulo(), invIni.getFecha());
                            Precio pLeche = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(ir.getArticulo(), invIni.getFecha());
                            double comisionRenglon = ((ir.getCantidad() * pLeche.getPrecioCompra()) * comision.getPorcentajeLeche()) / 100;
                            comisionInvIni = comisionInvIni + comisionRenglon;
                        }
                    }
                }
                total = total + comisionInvIni;
                retorno[filaInforme][0] = "Inventario Inicial";
                retorno[filaInforme][1] = formatter.format(invIni.getFecha());
                retorno[filaInforme][2] = Double.toString(comisionInvIni);
                filaInforme++;

                List<Compra> compras = SistemaLiquidaciones.getInstance().devolverComprasParaFechaYReparto(l.getFecha(), l.getReparto());
                double comisionCompras = 0;
                double comisionComprasLeche = 0;
                for (Compra c : compras) {
                    if (!c.getTipoDocumento().isEsDocumentoInterno()) {
                        if (c.getTipoDocumento().isSuma()) {
                            for (CompraRenglon cr : c.getRenglones()) {
                                if (esComisionPorPorcentaje(cr.getArticulo())) {
                                    //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(cr.getArticulo(), c.getFecha());
                                    double comisionRenglon = ((cr.getTotalPrecioVentaSinIva()) * comision.getPorcentaje()) / 100;
                                    comisionCompras = comisionCompras + comisionRenglon;
                                } else {
                                    if (esLeche(cr.getArticulo())) {
                                        //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(cr.getArticulo(), c.getFecha());
                                        Precio pLeche = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(cr.getArticulo(), c.getFecha());
                                        double comisionRenglon = ((cr.getCantidad() * pLeche.getPrecioCompra()) * comision.getPorcentajeLeche()) / 100;
                                        comisionComprasLeche = comisionComprasLeche + comisionRenglon;
                                    }
                                }
                            }
                        }
                        if (!c.getTipoDocumento().isSuma()) {
                            for (CompraRenglon cr : c.getRenglones()) {
                                if (esComisionPorPorcentaje(cr.getArticulo())) {
                                    //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(cr.getArticulo(), c.getFecha());
                                    double comisionRenglon = ((cr.getTotalPrecioVentaSinIva()) * comision.getPorcentaje()) / 100;
                                    comisionCompras = comisionCompras - comisionRenglon;
                                } else {
                                    if (esLeche(cr.getArticulo())) {
                                        //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(cr.getArticulo(), c.getFecha());
                                        Precio pLeche = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(cr.getArticulo(), c.getFecha());
                                        double comisionRenglon = ((cr.getCantidad() * pLeche.getPrecioCompra()) * comision.getPorcentajeLeche()) / 100;
                                        comisionComprasLeche = comisionComprasLeche - comisionRenglon;
                                    }
                                }
                            }
                        }
                    }
                }
                if (comisionCompras != 0) {
                    total = total + comisionCompras;
                    retorno[filaInforme][0] = "Compras Productos";
                    retorno[filaInforme][1] = formatter.format(l.getFecha());
                    retorno[filaInforme][2] = Double.toString(comisionCompras);
                    filaInforme++;
                }
                if (comisionComprasLeche != 0) {
                    total = total + comisionComprasLeche;
                    retorno[filaInforme][0] = "Compras Leche";
                    retorno[filaInforme][1] = formatter.format(l.getFecha());
                    retorno[filaInforme][2] = Double.toString(comisionComprasLeche);
                    filaInforme++;
                }
            }
            if (i == liquidaciones.size() - 1) {
                //Es el ultimo dia. Busco el inventario para esa fecha para descontarselo.
                Inventario invFinal = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(l.getFecha(), l.getReparto());
                //Calculo la comision del inventario para restar el inventario final
                double comisionInvFinal = 0;
                if (invFinal != null) {
                    for (InventarioRenglon ir : invFinal.getRenglones()) {
                        if (esComisionPorPorcentaje(ir.getArticulo())) {
                            //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(ir.getArticulo(), invFinal.getFecha());
                            double comisionRenglon = ((ir.getSubtotal()) * comision.getPorcentaje()) / 100;
                            comisionInvFinal = comisionInvFinal + comisionRenglon;
                        } else {
                            if (esLeche(ir.getArticulo())) {
                                //Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(ir.getArticulo(), invFinal.getFecha());
                                Precio pLeche = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(ir.getArticulo(), invFinal.getFecha());
                                double comisionRenglon = ((ir.getCantidad() * pLeche.getPrecioCompra()) * comision.getPorcentajeLeche()) / 100;
                                comisionInvFinal = comisionInvFinal + comisionRenglon;
                            }
                        }
                    }
                    total = total - comisionInvFinal;
                    retorno[filaInforme][0] = "Inventario Final";
                    retorno[filaInforme][1] = formatter.format(invFinal.getFecha());
                    retorno[filaInforme][2] = "-" + Double.toString(comisionInvFinal);
                    filaInforme++;
                }
            }
            ultimaFechaCalculada = l.getFecha();
            ultimoRepartoCalculado = l.getReparto();
        }
        //retorno[filaInforme][0] = "Total";
        //retorno[filaInforme][1] = "";
        //retorno[filaInforme][2] = Double.toString(total);
        return retorno;
    }

    public boolean sonVaciosLosDiasDeDiferencia(Date fechaDesde, Date fechaHasta, Reparto r) throws Exception {
        boolean retorno = false;
        try {
            Calendar calDesde = Calendar.getInstance();
            calDesde.setTime(fechaDesde);
            calDesde.add(Calendar.DATE, 1);
            Calendar calHasta = Calendar.getInstance();
            calHasta.setTime(fechaHasta);
            calHasta.add(Calendar.DATE, -1);
            List<Compra> compras = SistemaLiquidaciones.getInstance().devolverComprasEntreFechasYReparto(calDesde.getTime(), calHasta.getTime(), r);
            if (compras.isEmpty()) {
                retorno = true;
            }
        } catch (ParseException ex) {
            Logger.getLogger(SistemaInformes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retorno;
    }

    public List<Factura> devolverFacturasSinPagarPorGrupoDeCliente(GrupoCliente gc) throws Exception {
        List<Factura> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura where estaPaga = :act and cliente_id in (SELECT id FROM Cliente where grupoCliente_id = " + gc.getId() + ")");
        consulta.setBoolean("act", false);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Factura> devolverFacturasSinPagarPorCliente(Cliente c) throws Exception {
        List<Factura> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura where estaPaga = :act and cliente_id = " + c.getId());
        consulta.setBoolean("act", false);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    //Metodos Cheques
    public List<Cheque> devolverChequesPorCliente(Cliente c) throws Exception {
        List<Cheque> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cheque where cliente_id = " + c.getId());
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public List<Factura> devolverEstadoDeCuentaParaCliente(Date desdeFecha, Date hastaFecha, Cliente cliente) {
        List<Factura> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura where (fecha BETWEEN :stDate AND :edDate) and estaPaga = :act and cliente_id = " + cliente.getId());
        consulta.setBoolean("act", false);
        consulta.setDate("stDate", desdeFecha);
        consulta.setDate("edDate", hastaFecha);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Cheque> devolverChequesEntreFechas(Date fechaDesde, Date fechaHasta) throws Exception {
        List<Cheque> retorno;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaDesde);
        //cal.add(Calendar.DATE, -1);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cheque WHERE fecha BETWEEN :stDate AND :edDate");
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Cheque> devolverChequesEntreFechasVto(Date fechaDesde, Date fechaHasta) throws Exception {
        List<Cheque> retorno;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaDesde);
        //cal.add(Calendar.DATE, -1);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cheque WHERE fechaVencimiento BETWEEN :stDate AND :edDate");
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Cheque> devolverChequesEntreFechasYFechasDeVto(Date fechaDesde, Date fechaHasta, Date fechaDesdeVto, Date fechaHastaVto) throws Exception {
        List<Cheque> retorno;
        //SimpleDateFormat formatter;
        //formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaDesde);
        //cal.add(Calendar.DATE, -1);
        //Calendar cal2 = Calendar.getInstance();
        //cal2.setTime(fechaDesdeVto);
        //cal2.add(Calendar.DATE, -1);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cheque WHERE fecha BETWEEN :stDate AND :edDate and fechaVencimiento BETWEEN :stDateVto AND :edDateVto");
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        consulta.setDate("stDateVto", fechaDesdeVto);
        consulta.setDate("edDateVto", fechaHastaVto);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Cheque> devolverChequesPorClienteEntreFechasVto(Cliente cli, Date fechaDesde, Date fechaHasta) throws Exception {
        List<Cheque> retorno;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaDesde);
        //cal.add(Calendar.DATE, -1);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cheque WHERE fechaVencimiento BETWEEN :stDate AND :edDate and cliente_id = " + cli.getId());
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Cheque> devolverChequesPorClienteEntreFechas(Cliente cli, Date fechaDesde, Date fechaHasta) throws Exception {
        List<Cheque> retorno;
        //SimpleDateFormat formatter;
        //formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaDesde);
        //cal.add(Calendar.DATE, -1);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cheque WHERE fecha BETWEEN :stDate AND :edDate and cliente_id = " + cli.getId());
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Retencion> devolverRetencionesActivasPorReparto(Reparto r) throws Exception {
        List<Retencion> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Retencion WHERE activa = :act and reparto_id = " + r.getId());
        consulta.setBoolean("act", true);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Retencion> devolverRetencionesInActivasPorReparto(Reparto r) throws Exception {
        List<Retencion> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Retencion WHERE activa = :act and reparto_id = " + r.getId());
        consulta.setBoolean("act", false);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Inau> devolverInausEntreFechasYReparto(Date fechaDesde, Date fechaHasta, Reparto r) throws Exception {
        List<Inau> retorno = new ArrayList<>();
        if (r == null) {
            List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
            for (Reparto rep : repartos) {
                Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
                session.beginTransaction();
                Query consulta = session.createQuery("SELECT i FROM Inau i where i.reparto = :r AND i.desdeFecha >= :fechaD AND i.hastaFecha <= :fechaH");
                consulta.setDate("fechaD", fechaDesde);
                consulta.setDate("fechaH", fechaHasta);
                consulta.setEntity("r", rep);
                List<Inau> aux = consulta.list();
                retorno.addAll(aux);
                session.getTransaction().commit();
                session.close();
            }
            return retorno;
        } else {
            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consulta = session.createQuery("SELECT i FROM Inau i where i.reparto = :r AND i.desdeFecha >= :fechaD AND i.hastaFecha <= :fechaH");
            consulta.setDate("fechaD", fechaDesde);
            consulta.setDate("fechaH", fechaHasta);
            consulta.setEntity("r", r);
            retorno = consulta.list();
            session.getTransaction().commit();
            session.close();
            return retorno;
        }
    }

    public List<Anep> devolverAnepsEntreFechasYReparto(Date fechaDesde, Date fechaHasta, Reparto r) throws Exception {
        List<Anep> retorno = new ArrayList<>();
        if (r == null) {
            List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
            for (Reparto rep : repartos) {
                Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
                session.beginTransaction();
                Query consulta = session.createQuery("SELECT a FROM Anep a where a.reparto = :r AND a.desdeFecha >= :fechaD AND a.hastaFecha <= :fechaH");
                consulta.setDate("fechaD", fechaDesde);
                consulta.setDate("fechaH", fechaHasta);
                consulta.setEntity("r", rep);
                List<Anep> aux = consulta.list();
                retorno.addAll(aux);
                session.getTransaction().commit();
                session.close();
            }
            return retorno;
        } else {
            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consulta = session.createQuery("SELECT a FROM Anep a where a.reparto = :r AND a.desdeFecha >= :fechaD AND a.hastaFecha <= :fechaH");
            consulta.setDate("fechaD", fechaDesde);
            consulta.setDate("fechaH", fechaHasta);
            consulta.setEntity("r", r);
            retorno = consulta.list();
            session.getTransaction().commit();
            session.close();
            return retorno;
        }
    }

    public String[][] informeAnalisisComprasDevoluciones(int desdeCodigo, int hastaCodigo, Date desdeFecha, Date hastaFecha, Reparto r) throws Exception {
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consultaArt = session.createQuery("SELECT a FROM Articulo a where a.codigo >= :dCod AND a.codigo <= :hCod");
        consultaArt.setInteger("dCod", desdeCodigo);
        consultaArt.setInteger("hCod", hastaCodigo);
        List<Articulo> articulos = consultaArt.list();
        session.getTransaction().commit();
        session.close();
        
        HashMap<Articulo, Object> comprasPorArticulo = new HashMap<>();
        HashMap<Articulo, Object> devolucionesPorArticulo = new HashMap<>();
        for (Articulo a : articulos) {
            //Compras
            session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consultaCompras = session.createQuery("SELECT sum(cr.cantidad) FROM Compra c,CompraRenglon cr, DocumentoDeCompra dc WHERE c.tipoDocumento = dc AND dc.tipoDocumento != :remito AND dc.esDocumentoInterno = :docInt AND dc.suma = :s AND c.fechaLiquidacion >= :fechaD AND c.fechaLiquidacion <= :fechaH AND c.reparto = :r AND cr.compra = c AND cr.articulo = :art");
            consultaCompras.setEntity("art", a);
            consultaCompras.setEntity("r", r);
            consultaCompras.setDate("fechaD", desdeFecha);
            consultaCompras.setDate("fechaH", hastaFecha);
            consultaCompras.setBoolean("docInt", false);
            consultaCompras.setString("remito", "Remito Devolución");
            consultaCompras.setBoolean("s", true);
            if (consultaCompras.uniqueResult() != null) {
                double cantidadCompras = (double) consultaCompras.uniqueResult();
                comprasPorArticulo.put(a, cantidadCompras);
            }
            session.getTransaction().commit();
            session.close();
            //Devoluciones
            session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consultaDevoluciones = session.createQuery("SELECT sum(cr.cantidad) FROM Compra c,CompraRenglon cr, DocumentoDeCompra dc WHERE c.tipoDocumento = dc AND dc.tipoDocumento != :remito AND dc.esDocumentoInterno = :docInt AND dc.suma = :s AND c.fecha >= :fechaD AND c.fecha <= :fechaH AND c.reparto = :r AND cr.compra = c AND cr.articulo = :art");
            consultaDevoluciones.setEntity("art", a);
            consultaDevoluciones.setEntity("r", r);
            consultaDevoluciones.setDate("fechaD", desdeFecha);
            consultaDevoluciones.setDate("fechaH", hastaFecha);
            consultaDevoluciones.setBoolean("docInt", false);
            consultaDevoluciones.setString("remito", "Remito Devolución");
            consultaDevoluciones.setBoolean("s", false);
            if (consultaDevoluciones.uniqueResult() != null) {
                double cantidadDevoluciones = (double) consultaDevoluciones.uniqueResult();
                devolucionesPorArticulo.put(a, cantidadDevoluciones);
            }
            session.getTransaction().commit();
            session.close();
        }
        int fila = 0;
        String[][] retorno = new String[comprasPorArticulo.size()][4];
        for (Articulo a : articulos) {
            if (comprasPorArticulo.containsKey(a)) {
                retorno[fila][0] = Integer.toString(a.getCodigo());
                retorno[fila][1] = a.getDescripcion();
                retorno[fila][2] = comprasPorArticulo.get(a).toString();
                if (devolucionesPorArticulo.containsKey(a)) {
                    retorno[fila][3] = devolucionesPorArticulo.get(a).toString();
                } else {
                    retorno[fila][3] = "0";
                }
                fila++;
            }

        }
        return retorno;
    }

    public String[][] informeGastosEntreFechasPorRubro(Date fechaDesde, Date fechaHasta, RubroGasto rubro) throws Exception {
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT g FROM Gasto g, GastoRenglon gr WHERE gr.gasto = g AND gr.rubro = :rub AND g.fecha BETWEEN :fechaD AND :fechaH");
        consulta.setDate("fechaD", fechaDesde);
        consulta.setDate("fechaH", fechaHasta);
        consulta.setEntity("rub", rubro);
        List<Gasto> gastos = consulta.list();
        session.getTransaction().commit();
        session.close();
        List<String[]> retAux = new ArrayList<>();
        for (Gasto g : gastos) {
            for (GastoRenglon gr : g.getRenglones()) {
                if (gr.getRubro().equals(rubro)) {
                    String[] tuplaGastoRubro = new String[5];
                    tuplaGastoRubro[0] = gr.getRubro().toString();
                    tuplaGastoRubro[1] = formatter.format(g.getFecha());
                    tuplaGastoRubro[2] = Double.toString(gr.getTotal());
                    tuplaGastoRubro[3] = Double.toString(gr.getHoras());
                    tuplaGastoRubro[4] = Double.toString(gr.getHorasDeDescansoSemanal());
                    retAux.add(tuplaGastoRubro);
                }
            }
        }
        String[][] retorno = new String[retAux.size()][5];
        int fila = 0;
        for(String[] s : retAux) {
            retorno[fila][0] = s[0];
            retorno[fila][1] = s[1];
            retorno[fila][2] = s[2];
            retorno[fila][3] = s[3];
            retorno[fila][4] = s[4];
            fila++;
        }
        return retorno;
    }
    
    public String[][] informeJornalesEntreFechasPorRubro(Date fechaDesde, Date fechaHasta, RubroGasto rubro) throws Exception {
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT distinct j FROM Jornales j, JornalRenglon jr WHERE jr.jornales = j AND jr.rubro = :rub AND j.fecha BETWEEN :fechaD AND :fechaH");
        consulta.setDate("fechaD", fechaDesde);
        consulta.setDate("fechaH", fechaHasta);
        consulta.setEntity("rub", rubro);
        List<Jornales> jornales = consulta.list();
        session.getTransaction().commit();
        session.close();
        List<String[]> retAux = new ArrayList<>();
        for (Jornales j : jornales) {
            for (JornalRenglon jr : j.getRenglones()) {
                if (jr.getRubro().equals(rubro)) {
                    String[] tuplaGastoRubro = new String[6];
                    tuplaGastoRubro[0] = jr.getRubro().toString();
                    tuplaGastoRubro[1] = formatter.format(j.getFecha());
                    tuplaGastoRubro[2] = Double.toString(jr.getTotal());
                    tuplaGastoRubro[3] = Double.toString(jr.getHoras());
                    tuplaGastoRubro[4] = Double.toString(jr.getHorasDeDescansoSemanal());
                    tuplaGastoRubro[5] = Integer.toString(jr.getCantidadDeJornales());
                    retAux.add(tuplaGastoRubro);
                }
            }
        }
        String[][] retorno = new String[retAux.size()][6];
        int fila = 0;
        for(String[] s : retAux) {
            retorno[fila][0] = s[0];
            retorno[fila][1] = s[1];
            retorno[fila][2] = s[2];
            retorno[fila][3] = s[3];
            retorno[fila][4] = s[4];
            retorno[fila][5] = s[5];
            fila++;
        }
        return retorno;
    }

    public double[] informeComprasPorEmpresaTotal(Date fechaDesde, Date fechaHasta) throws Exception {
        double[] retorno = new double[8];
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consultaCerram = session.createQuery("SELECT c FROM Compra c WHERE (c.fecha BETWEEN :fechaD AND :fechaH) AND c.tipoDocumento.tipoDocumento LIKE :busquedaCerram");
        consultaCerram.setDate("fechaD", fechaDesde);
        consultaCerram.setDate("fechaH", fechaHasta);
        consultaCerram.setParameter("busquedaCerram", "%" + "Cerram" + "%");
        List<Compra> comprasCerram = consultaCerram.list();
        session.getTransaction().commit();
        session.close();
        double comprasCerramSinIva = 0;
        double comprasCerramConIva = 0;
        double ventasCerramSinIva = 0;
        double ventasCerramConIva = 0;
        boolean esDeEnvases;
        for (Compra c : comprasCerram) {
            esDeEnvases = false;
            for (CompraRenglon cr : c.getRenglones()) {
                if (esEnvase(cr.getArticulo())) {
                    esDeEnvases = true;
                    break;
                }
            }
            if (!esDeEnvases) {
                if (c.getTipoDocumento().isSuma()) {
                    comprasCerramSinIva = comprasCerramSinIva + c.getSubtotal();
                    comprasCerramConIva = comprasCerramConIva + c.getTotal();
                    ventasCerramSinIva = ventasCerramSinIva + c.getTotalAPrecioDeVentaSinIva();
                    ventasCerramConIva = ventasCerramConIva + c.getTotalAPrecioDeVentaConIva();
                }
                if (!c.getTipoDocumento().isSuma()) {
                    comprasCerramSinIva = comprasCerramSinIva - c.getSubtotal();
                    comprasCerramConIva = comprasCerramConIva - c.getTotal();
                    ventasCerramSinIva = ventasCerramSinIva - c.getTotalAPrecioDeVentaSinIva();
                    ventasCerramConIva = ventasCerramConIva - c.getTotalAPrecioDeVentaConIva();
                }
            }
        }
        retorno[0] = comprasCerramSinIva;
        retorno[1] = comprasCerramConIva;
        retorno[2] = ventasCerramSinIva;
        retorno[3] = ventasCerramConIva;

        //Busco para Relece
        session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consultaRelece = session.createQuery("SELECT c FROM Compra c WHERE (c.fecha BETWEEN :fechaD AND :fechaH) AND c.tipoDocumento.tipoDocumento LIKE :busquedaRelece");
        consultaRelece.setDate("fechaD", fechaDesde);
        consultaRelece.setDate("fechaH", fechaHasta);
        consultaRelece.setParameter("busquedaRelece", "%" + "Relece" + "%");
        List<Compra> comprasRelece = consultaRelece.list();
        session.getTransaction().commit();
        session.close();

        double comprasReleceSinIva = 0;
        double comprasReleceConIva = 0;
        double ventasReleceSinIva = 0;
        double ventasReleceConIva = 0;
        for (Compra c : comprasRelece) {
            esDeEnvases = false;
            for (CompraRenglon cr : c.getRenglones()) {
                if (esEnvase(cr.getArticulo())) {
                    esDeEnvases = true;
                    break;
                }
            }
            if (!esDeEnvases) {
                if (c.getTipoDocumento().isSuma()) {
                    comprasReleceSinIva = comprasReleceSinIva + c.getSubtotal();
                    comprasReleceConIva = comprasReleceConIva + c.getTotal();
                    ventasReleceSinIva = ventasReleceSinIva + c.getTotalAPrecioDeVentaSinIva();
                    ventasReleceConIva = ventasReleceConIva + c.getTotalAPrecioDeVentaConIva();
                }
                if (!c.getTipoDocumento().isSuma()) {
                    comprasReleceSinIva = comprasReleceSinIva - c.getSubtotal();
                    comprasReleceConIva = comprasReleceConIva - c.getTotal();
                    ventasReleceSinIva = ventasReleceSinIva - c.getTotalAPrecioDeVentaSinIva();
                    ventasReleceConIva = ventasReleceConIva - c.getTotalAPrecioDeVentaConIva();
                }
            }
        }
        retorno[4] = comprasReleceSinIva;
        retorno[5] = comprasReleceConIva;
        retorno[6] = ventasReleceSinIva;
        retorno[7] = ventasReleceConIva;
        return retorno;
    }

    public double[] informeComprasPorEmpresaYReparto(Date fechaDesde, Date fechaHasta, Reparto r) throws Exception {
        double[] retorno = new double[8];
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consultaCerram = session.createQuery("SELECT c FROM Compra c WHERE (c.fecha BETWEEN :fechaD AND :fechaH) AND c.tipoDocumento.tipoDocumento LIKE :busquedaCerram AND c.reparto = :r");
        consultaCerram.setDate("fechaD", fechaDesde);
        consultaCerram.setDate("fechaH", fechaHasta);
        consultaCerram.setParameter("busquedaCerram", "%" + "Cerram" + "%");
        consultaCerram.setEntity("r", r);
        List<Compra> comprasCerram = consultaCerram.list();
        session.getTransaction().commit();
        session.close();
        double comprasCerramSinIva = 0;
        double comprasCerramConIva = 0;
        double ventasCerramSinIva = 0;
        double ventasCerramConIva = 0;
        boolean esDeEnvases;
        for (Compra c : comprasCerram) {
            esDeEnvases = false;
            for (CompraRenglon cr : c.getRenglones()) {
                if (esEnvase(cr.getArticulo())) {
                    esDeEnvases = true;
                    break;
                }
            }
            if (!esDeEnvases) {
                if (c.getTipoDocumento().isSuma()) {
                    comprasCerramSinIva = comprasCerramSinIva + c.getSubtotal();
                    comprasCerramConIva = comprasCerramConIva + c.getTotal();
                    ventasCerramSinIva = ventasCerramSinIva + c.getTotalAPrecioDeVentaSinIva();
                    ventasCerramConIva = ventasCerramConIva + c.getTotalAPrecioDeVentaConIva();
                }
                if (!c.getTipoDocumento().isSuma()) {
                    comprasCerramSinIva = comprasCerramSinIva - c.getSubtotal();
                    comprasCerramConIva = comprasCerramConIva - c.getTotal();
                    ventasCerramSinIva = ventasCerramSinIva - c.getTotalAPrecioDeVentaSinIva();
                    ventasCerramConIva = ventasCerramConIva - c.getTotalAPrecioDeVentaConIva();
                }
            }
        }
        retorno[0] = comprasCerramSinIva;
        retorno[1] = comprasCerramConIva;
        retorno[2] = ventasCerramSinIva;
        retorno[3] = ventasCerramConIva;

        //Busco para Relece
        session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consultaRelece = session.createQuery("SELECT c FROM Compra c WHERE (c.fecha BETWEEN :fechaD AND :fechaH) AND c.tipoDocumento.tipoDocumento LIKE :busquedaRelece AND c.reparto = :r");
        consultaRelece.setDate("fechaD", fechaDesde);
        consultaRelece.setDate("fechaH", fechaHasta);
        consultaRelece.setParameter("busquedaRelece", "%" + "Relece" + "%");
        consultaRelece.setEntity("r", r);
        List<Compra> comprasRelece = consultaRelece.list();
        session.getTransaction().commit();
        session.close();

        double comprasReleceSinIva = 0;
        double comprasReleceConIva = 0;
        double ventasReleceSinIva = 0;
        double ventasReleceConIva = 0;
        for (Compra c : comprasRelece) {
            esDeEnvases = false;
            for (CompraRenglon cr : c.getRenglones()) {
                if (esEnvase(cr.getArticulo())) {
                    esDeEnvases = true;
                    break;
                }
            }
            if (!esDeEnvases) {
                if (c.getTipoDocumento().isSuma()) {
                    comprasReleceSinIva = comprasReleceSinIva + c.getSubtotal();
                    comprasReleceConIva = comprasReleceConIva + c.getTotal();
                    ventasReleceSinIva = ventasReleceSinIva + c.getTotalAPrecioDeVentaSinIva();
                    ventasReleceConIva = ventasReleceConIva + c.getTotalAPrecioDeVentaConIva();
                }
                if (!c.getTipoDocumento().isSuma()) {
                    comprasReleceSinIva = comprasReleceSinIva - c.getSubtotal();
                    comprasReleceConIva = comprasReleceConIva - c.getTotal();
                    ventasReleceSinIva = ventasReleceSinIva - c.getTotalAPrecioDeVentaSinIva();
                    ventasReleceConIva = ventasReleceConIva - c.getTotalAPrecioDeVentaConIva();
                }
            }
        }
        retorno[4] = comprasReleceSinIva;
        retorno[5] = comprasReleceConIva;
        retorno[6] = ventasReleceSinIva;
        retorno[7] = ventasReleceConIva;
        return retorno;
    }

    public double[] informeAnalisisIvas(Date fechaDesde, Date fechaHasta) throws Exception {
        double[] retorno = new double[6];
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consultaCerram = session.createQuery("SELECT c FROM Compra c WHERE (c.fecha BETWEEN :fechaD AND :fechaH) AND c.tipoDocumento.tipoDocumento LIKE :busquedaCerram");
        consultaCerram.setDate("fechaD", fechaDesde);
        consultaCerram.setDate("fechaH", fechaHasta);
        consultaCerram.setParameter("busquedaCerram", "%" + "Cerram" + "%");
        List<Compra> comprasCerram = consultaCerram.list();
        session.getTransaction().commit();
        session.close();
        double cerramIvaCompras = 0;
        double cerramIvaVentas = 0;
        boolean esDeEnvases;
        for (Compra c : comprasCerram) {
            esDeEnvases = false;
            for (CompraRenglon cr : c.getRenglones()) {
                if (esEnvase(cr.getArticulo())) {
                    esDeEnvases = true;
                    break;
                }
            }
            if (!esDeEnvases) {
                if (c.getTipoDocumento().isSuma()) {
                    cerramIvaCompras = cerramIvaCompras + c.getTotal() - c.getSubtotal();
                    cerramIvaVentas = cerramIvaVentas + c.getTotalAPrecioDeVentaConIva() - c.getTotalAPrecioDeVentaSinIva();
                }
                if (!c.getTipoDocumento().isSuma()) {
                    cerramIvaCompras = cerramIvaCompras - (c.getTotal() - c.getSubtotal());
                    cerramIvaVentas = cerramIvaVentas - (c.getTotalAPrecioDeVentaConIva() - c.getTotalAPrecioDeVentaSinIva());
                }
            }
        }
        retorno[0] = cerramIvaCompras;
        retorno[1] = cerramIvaVentas;
        retorno[2] = cerramIvaVentas - cerramIvaCompras;

        //Busco para Relece
        session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consultaRelece = session.createQuery("SELECT c FROM Compra c WHERE (c.fecha BETWEEN :fechaD AND :fechaH) AND c.tipoDocumento.tipoDocumento LIKE :busquedaRelece");
        consultaRelece.setDate("fechaD", fechaDesde);
        consultaRelece.setDate("fechaH", fechaHasta);
        consultaRelece.setParameter("busquedaRelece", "%" + "Relece" + "%");
        List<Compra> comprasRelece = consultaRelece.list();
        session.getTransaction().commit();
        session.close();

        double releceIvaCompras = 0;
        double releceIvaVentas = 0;
        for (Compra c : comprasRelece) {
            esDeEnvases = false;
            for (CompraRenglon cr : c.getRenglones()) {
                if (esEnvase(cr.getArticulo())) {
                    esDeEnvases = true;
                    break;
                }
            }
            if (!esDeEnvases) {
                if (c.getTipoDocumento().isSuma()) {
                    releceIvaCompras = releceIvaCompras + c.getTotal() - c.getSubtotal();
                    releceIvaVentas = releceIvaVentas + c.getTotalAPrecioDeVentaConIva() - c.getTotalAPrecioDeVentaSinIva();
                }
                if (!c.getTipoDocumento().isSuma()) {
                    releceIvaCompras = releceIvaCompras - (c.getTotal() - c.getSubtotal());
                    releceIvaVentas = releceIvaVentas - (c.getTotalAPrecioDeVentaConIva() - c.getTotalAPrecioDeVentaSinIva());
                }
            }
        }
        retorno[3] = releceIvaCompras;
        retorno[4] = releceIvaVentas;
        retorno[5] = releceIvaVentas - releceIvaCompras;
        return retorno;
    }

    public void sacarInformeContadora(Date fechaDesde, Date fechaHasta) throws Exception {
        DocumentoDeCompra fc = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Factura Cerram");
        crearExcelInformeContadora(fechaDesde, fechaHasta, fc);
        DocumentoDeCompra ncc = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Nota de Credito Cerram");
        crearExcelInformeContadora(fechaDesde, fechaHasta, ncc);
        DocumentoDeCompra fr = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Factura Relece");
        crearExcelInformeContadora(fechaDesde, fechaHasta, fr);
        DocumentoDeCompra ncr = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Nota de Credito Relece");
        crearExcelInformeContadora(fechaDesde, fechaHasta, ncr);
        DocumentoDeCompra r = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Remito Devolución");
        crearExcelInformeContadoraRemitos(fechaDesde, fechaHasta, r);
        crearInterfazContadorCerram(fechaDesde, fechaHasta);
        crearInterfazContadorRelece(fechaDesde, fechaHasta);
        SistemaFacturas.getInstance().sacarInformeFacturasContadora(fechaDesde, fechaHasta);
    }

    private void crearExcelInformeContadora(Date fechaDesde, Date fechaHasta, DocumentoDeCompra dc) throws Exception {
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT c FROM Compra c WHERE c.fecha BETWEEN :stDate AND :edDate and c.tipoDocumento = :td");
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        consulta.setEntity("td", dc);
        List<Compra> facturasCerram = consulta.list();
        session.getTransaction().commit();
        session.close();
        Workbook workbookEnvases = new XSSFWorkbook();
        Sheet hojaEnvases = workbookEnvases.createSheet(dc.getTipoDocumento() + " Envases");
        Workbook workbookProductos = new XSSFWorkbook();
        Sheet hojaProductos = workbookProductos.createSheet(dc.getTipoDocumento() + " Productos");
        int rowNumEnvases = 0;
        int rowNumProductos = 0;
        agregarEncabezadoInformeContadorExcel(hojaEnvases, rowNumEnvases++);
        agregarEncabezadoInformeContadorExcel(hojaProductos, rowNumProductos++);

        for (Compra c : facturasCerram) {
            boolean esDeEnvases = false;
            double netoExcento = 0;
            double netoBasico = 0;
            double netoMinimo = 0;
            double ventaExcenta = 0;
            double ventaMinimo = 0;
            double ventaBasico = 0;
            for (CompraRenglon cr : c.getRenglones()) {
                Articulo a = cr.getArticulo();
                Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, c.getFecha());
                if (this.esEnvase(a)) {
                    esDeEnvases = true;
                }
                if ("Excento".equals(a.getIva().getNombre())) {
                    netoExcento = netoExcento + cr.getSubtotal();
                    ventaExcenta = ventaExcenta + cr.getTotalPrecioVentaConIva();
                }
                if ("Minimo".equals(a.getIva().getNombre())) {
                    netoMinimo = netoMinimo + cr.getSubtotal();
                    ventaMinimo = ventaMinimo + cr.getTotalPrecioVentaConIva();
                }
                if ("Basico".equals(a.getIva().getNombre())) {
                    netoBasico = netoBasico + cr.getSubtotal();
                    ventaBasico = ventaBasico + cr.getTotalPrecioVentaConIva();
                }
            }
            if (esDeEnvases) {
                agregarCompraEnExcel(hojaEnvases, rowNumEnvases++, c, netoExcento, netoMinimo, netoBasico, ventaExcenta, ventaMinimo, ventaBasico);
            } else {
                agregarCompraEnExcel(hojaProductos, rowNumProductos++, c, netoExcento, netoMinimo, netoBasico, ventaExcenta, ventaMinimo, ventaBasico);
            }

        }

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaDesde);
            int mes = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            String mesS = devolverMesString(mes);
            String nombreCapeta = "PlanillasContador" + mesS + year; 
            File directory = new File(String.valueOf(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta));
            if (!directory.exists()) {
                directory.mkdir();
            }
            
            FileOutputStream outEnvases = new FileOutputStream(new File(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta + "/" + dc.getTipoDocumento().replace(" ", "") + "EnvasesDesde" + formatter.format(fechaDesde).replace("-", "") + "Hasta" + formatter.format(fechaHasta).replace("-", "") + ".xlsx"));
            workbookEnvases.write(outEnvases);
            outEnvases.close();
            FileOutputStream outProductos = new FileOutputStream(new File(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/"  + nombreCapeta + "/" + dc.getTipoDocumento().replace(" ", "") + "ProductosDesde" + formatter.format(fechaDesde).replace("-", "") + "Hasta" + formatter.format(fechaHasta).replace("-", "") + ".xlsx"));
            workbookProductos.write(outProductos);
            outProductos.close();
        } catch (IOException ioexp) {
            int h = 0;
            System.out.println("Error");
        }
    }
    
    public String devolverMesString(int mes) {
        switch (mes){
            case 0: return "Enero"; 
            case 1: return "Febrero";
            case 2: return "Marzo";
            case 3: return "Abril";
            case 4: return "Mayo";
            case 5: return "Junio";
            case 6: return "Julio";
            case 7: return "Agosto";
            case 8: return "Setiembre";
            case 9: return "Octubre";
            case 10: return "Noviembre";
            case 11: return "Diciembre";
            default: return "";
        }
    }

    private void agregarEncabezadoInformeContadorExcel(Sheet hoja, int numero) throws Exception {
        Row row = hoja.createRow(numero);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue("Fecha");
        Cell cell2 = row.createCell(1);
        cell2.setCellValue("tipoDoc");
        Cell cell3 = row.createCell(2);
        cell3.setCellValue("numDoc");
        Cell cell4 = row.createCell(3);
        cell4.setCellValue("neto_ex");
        Cell cell5 = row.createCell(4);
        cell5.setCellValue("neto_bas");
        Cell cell6 = row.createCell(5);
        cell6.setCellValue("neto_min");
        Cell cell7 = row.createCell(6);
        cell7.setCellValue("total_neto");
        Cell cell8 = row.createCell(7);
        cell8.setCellValue("iva_min");
        Cell cell9 = row.createCell(8);
        cell9.setCellValue("iva_bas");
        Cell cell10 = row.createCell(9);
        cell10.setCellValue("total_minimo");
        Cell cell11 = row.createCell(10);
        cell11.setCellValue("total_basico");
        Cell cell12 = row.createCell(11);
        cell12.setCellValue("importe_total");
        Cell cell13 = row.createCell(12);
        cell13.setCellValue("venta_excenta");
        Cell cell14 = row.createCell(13);
        cell14.setCellValue("venta_basico");
        Cell cell15 = row.createCell(14);
        cell15.setCellValue("venta_minimo");
        Cell cell16 = row.createCell(15);
        cell16.setCellValue("venta_total");
        for (int i = 0; i <= 15; i++) {
            hoja.autoSizeColumn(i, true);
        }
    }

    private void agregarCompraEnExcel(Sheet hoja, int numero, Compra c, double netoExcento, double netoMinimo, double netoBasico, double ventaExcento, double ventaMinimo, double ventaBasico) throws Exception {
        DecimalFormat df;
        df = new DecimalFormat("0,00");
        Row row = hoja.createRow(numero);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue(formatter.format(c.getFecha()));
        Cell cell2 = row.createCell(1);
        cell2.setCellValue(c.getTipoDocumento().getTipoDocumento());
        Cell cell3 = row.createCell(2);
        cell3.setCellValue(c.getNumero());
        Cell cell4 = row.createCell(3);
        cell4.setCellValue(netoExcento);
        Cell cell5 = row.createCell(4);
        cell5.setCellValue(netoBasico);
        Cell cell6 = row.createCell(5);
        cell6.setCellValue(netoMinimo);
        Cell cell7 = row.createCell(6);
        cell7.setCellValue(c.getSubtotal());
        Cell cell8 = row.createCell(7);
        cell8.setCellValue(c.getTotalMinimo());
        Cell cell9 = row.createCell(8);
        cell9.setCellValue(c.getTotalBasico());
        Cell cell10 = row.createCell(9);
        cell10.setCellValue(netoMinimo + c.getTotalMinimo());
        Cell cell11 = row.createCell(10);
        cell11.setCellValue(netoBasico + c.getTotalBasico());
        Cell cell12 = row.createCell(11);
        cell12.setCellValue(c.getTotal());
        Cell cell13 = row.createCell(12);
        cell13.setCellValue(ventaExcento);
        Cell cell14 = row.createCell(13);
        cell14.setCellValue(ventaBasico);
        Cell cell15 = row.createCell(14);
        cell15.setCellValue(ventaMinimo);
        Cell cell16 = row.createCell(15);
        cell16.setCellValue(c.getTotalAPrecioDeVentaConIva());
    }

    private void crearExcelInformeContadoraRemitos(Date fechaDesde, Date fechaHasta, DocumentoDeCompra dc) throws Exception {
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT c FROM Compra c WHERE c.fecha BETWEEN :stDate AND :edDate and c.tipoDocumento = :td");
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        consulta.setEntity("td", dc);
        List<Compra> facturas = consulta.list();
        session.getTransaction().commit();
        session.close();
        Workbook workbook = new XSSFWorkbook();
        Sheet hoja = workbook.createSheet(dc.getTipoDocumento());
        int rowNum = 0;
        agregarEncabezadoInformeContadorRemitosExcel(hoja, rowNum++);

        for (Compra c : facturas) {
            boolean esDeEnvases = false;
            double netoExcento = 0;
            double netoBasico = 0;
            double netoMinimo = 0;
            double cant = 0;
            for (CompraRenglon cr : c.getRenglones()) {
                Articulo a = cr.getArticulo();
                Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, c.getFecha());
                cant = cr.getCantidad();
                if (this.esEnvase(a)) {
                    esDeEnvases = true;
                }
                if ("Excento".equals(a.getIva().getNombre())) {
                    netoExcento = netoExcento + cr.getSubtotal();
                }
                if ("Minimo".equals(a.getIva().getNombre())) {
                    netoMinimo = netoMinimo + cr.getSubtotal();
                }
                if ("Basico".equals(a.getIva().getNombre())) {
                    netoBasico = netoBasico + cr.getSubtotal();
                }
            }

            agregarRemitoEnExcel(hoja, rowNum++, c, netoExcento, netoMinimo, netoBasico, cant);

        }

        try {
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaDesde);
            int mes = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            String mesS = devolverMesString(mes);
            String nombreCapeta = "PlanillasContador" + mesS + year; 
            File directory = new File(String.valueOf(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta));
            if (!directory.exists()) {
                directory.mkdir();
            }
            
            FileOutputStream outEnvases = new FileOutputStream(new File(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta + "/" + dc.getTipoDocumento().replace(" ", "") + "Desde" + formatter.format(fechaDesde).replace("-", "") + "Hasta" + formatter.format(fechaHasta).replace("-", "") + ".xlsx"));
            workbook.write(outEnvases);
            outEnvases.close();
        } catch (IOException ioexp) {
            int h = 0;
            System.out.println("Error");
        }
    }

    private void agregarEncabezadoInformeContadorRemitosExcel(Sheet hoja, int numero) throws Exception {
        Row row = hoja.createRow(numero);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue("Fecha");
        Cell cell2 = row.createCell(1);
        cell2.setCellValue("tipoDoc");
        Cell cell3 = row.createCell(2);
        cell3.setCellValue("numDoc");
        Cell cell4 = row.createCell(3);
        cell4.setCellValue("neto_ex");
        Cell cell5 = row.createCell(4);
        cell5.setCellValue("neto_bas");
        Cell cell6 = row.createCell(5);
        cell6.setCellValue("neto_min");
        Cell cell7 = row.createCell(6);
        cell7.setCellValue("total_neto");
        Cell cell8 = row.createCell(7);
        cell8.setCellValue("iva_min");
        Cell cell9 = row.createCell(8);
        cell9.setCellValue("iva_bas");
        Cell cell10 = row.createCell(9);
        cell10.setCellValue("total_minimo");
        Cell cell11 = row.createCell(10);
        cell11.setCellValue("total_basico");
        Cell cell12 = row.createCell(11);
        cell12.setCellValue("importe_total");
        Cell cell13 = row.createCell(12);
        cell13.setCellValue("stock");
        for (int i = 0; i <= 12; i++) {
            hoja.autoSizeColumn(i, true);
        }
    }

    private void agregarRemitoEnExcel(Sheet hoja, int numero, Compra c, double netoExcento, double netoMinimo, double netoBasico, double cant) throws Exception {
        DecimalFormat df;
        df = new DecimalFormat("0,00");
        Row row = hoja.createRow(numero);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue(formatter.format(c.getFecha()));
        Cell cell2 = row.createCell(1);
        cell2.setCellValue(c.getTipoDocumento().getTipoDocumento());
        Cell cell3 = row.createCell(2);
        cell3.setCellValue(c.getNumero());
        Cell cell4 = row.createCell(3);
        cell4.setCellValue(netoExcento);
        Cell cell5 = row.createCell(4);
        cell5.setCellValue(netoBasico);
        Cell cell6 = row.createCell(5);
        cell6.setCellValue(netoMinimo);
        Cell cell7 = row.createCell(6);
        cell7.setCellValue(c.getSubtotal());
        Cell cell8 = row.createCell(7);
        cell8.setCellValue(c.getTotalMinimo());
        Cell cell9 = row.createCell(8);
        cell9.setCellValue(c.getTotalBasico());
        Cell cell10 = row.createCell(9);
        cell10.setCellValue(netoMinimo + c.getTotalMinimo());
        Cell cell11 = row.createCell(10);
        cell11.setCellValue(netoBasico + c.getTotalBasico());
        Cell cell12 = row.createCell(11);
        cell12.setCellValue(c.getTotal());
        Cell cell13 = row.createCell(12);
        cell13.setCellValue(cant);
    }

    private void crearInterfazContadorCerram(Date fechaDesde, Date fechaHasta) throws Exception {
        Writer writer = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaDesde);
            int mes = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            String mesS = devolverMesString(mes);
            String nombreCapeta = "PlanillasContador" + mesS + year; 
            File directory = new File(String.valueOf(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta));
            if (!directory.exists()) {
                directory.mkdir();
            }
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta + "/" + "InterfazCerramDesde" + formatter.format(fechaDesde).replace("-", "") + "Hasta" + formatter.format(fechaHasta).replace("-", "") + ".txt")), "utf-8"));
            DocumentoDeCompra fc = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Factura Cerram");
            DocumentoDeCompra ncc = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Nota de Credito Cerram");
            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consulta = session.createQuery("SELECT c FROM Compra c WHERE c.fecha BETWEEN :stDate AND :edDate and (c.tipoDocumento = :fc OR c.tipoDocumento = :ncc)");
            consulta.setDate("stDate", fechaDesde);
            consulta.setDate("edDate", fechaHasta);
            consulta.setEntity("fc", fc);
            consulta.setEntity("ncc", ncc);
            List<Compra> facturas = consulta.list();
            session.getTransaction().commit();
            session.close();
            for (Compra c : facturas) {
                boolean esDeEnvases = false;
                double netoExcento = 0;
                double netoBasico = 0;
                double netoMinimo = 0;
                double ventaExcenta = 0;
                double ventaMinimo = 0;
                double ventaBasico = 0;
                double ventaIvaMinimo = 0;
                double ventaIvaBasico = 0;
                for (CompraRenglon cr : c.getRenglones()) {
                    Articulo a = cr.getArticulo();
                    Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, c.getFecha());
                    if (this.esEnvase(a)) {
                        esDeEnvases = true;
                    }
                    if ("Excento".equals(a.getIva().getNombre())) {
                        netoExcento = netoExcento + cr.getTotal();
                        ventaExcenta = ventaExcenta + cr.getTotalPrecioVentaConIva();
                    }
                    if ("Minimo".equals(a.getIva().getNombre())) {
                        netoMinimo = netoMinimo + cr.getTotal();
                        ventaMinimo = ventaMinimo + cr.getTotalPrecioVentaConIva();
                        ventaIvaMinimo = ventaIvaMinimo + cr.getTotalPrecioVentaConIva() - cr.getTotalPrecioVentaSinIva();
                    }
                    if ("Basico".equals(a.getIva().getNombre())) {
                        netoBasico = netoBasico + cr.getTotal();
                        ventaBasico = ventaBasico + cr.getTotalPrecioVentaConIva();
                        ventaIvaBasico = ventaIvaBasico + cr.getTotalPrecioVentaConIva() - cr.getTotalPrecioVentaSinIva();
                    }
                }
                Calendar aux = Calendar.getInstance();
                aux.setTime(c.getFecha());
                int dia = aux.get(Calendar.DAY_OF_MONTH);
                if (esDeEnvases) {
                    writer.write(dia + ",");
                    writer.write("\"5105\",");
                    writer.write("\"2111001\",");
                    writer.write("0,0,");
                    if (c.getTipoDocumento().isSuma()) {
                        writer.write("\"FAPC\",");
                    } else {
                        writer.write("\"NCPC\",");
                    }
                    writer.write("\"A\",");
                    writer.write("\"" + c.getNumero() + "\",");
                    writer.write("\"\",");
                    if (c.getTipoDocumento().isSuma()) {
                        writer.write("\"Conaprole F " + c.getNumero() + "\",");
                    } else {
                        writer.write("\"Conaprole NC " + c.getNumero() + "\",");
                    }
                    writer.write("\"\",");
                    writer.write("0,");
                    if (c.getTipoDocumento().isSuma()) {
                        writer.write(c.getTotal() + ",");
                        writer.write("7,");
                        writer.write(c.getTotalBasico() + ",");
                    } else {
                        writer.write("-" + c.getTotal() + ",");
                        writer.write("7,");
                        writer.write("-" + c.getTotalBasico() + ",");
                    }
                    writer.write("0,0.00,0.0000000,\"E\",\"\",\"\", , , , , , , , , , , ,");
                    //writer.write("\n");
                    String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                    writer.write(str);
                } else {
                    //Compras
                    if (netoExcento != 0) {
                        writer.write(dia + ",");
                        writer.write("\"5102\",");
                        writer.write("\"2111001\",");
                        writer.write("0,0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"FAPC\",");
                        } else {
                            writer.write("\"NCPC\",");
                        }
                        writer.write("\"A\",");
                        writer.write("\"" + c.getNumero() + "\",");
                        writer.write("\"\",");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"Conaprole F " + c.getNumero() + "\",");
                        } else {
                            writer.write("\"Conaprole NC " + c.getNumero() + "\",");
                        }
                        writer.write("\"\",");
                        writer.write("0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write(netoExcento + ",");
                        } else {
                            writer.write("-" + netoExcento + ",");
                        }
                        writer.write("0,");
                        writer.write(0.000000 + ",");
                        writer.write("0,0.00,0.0000000,\"E\",\"\",\"\", , , , , , , , , , , ,");
                     
                        //writer.write("\n");
                        String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                        writer.write(str);
                    }
                    if (netoMinimo != 0) {
                        writer.write(dia + ",");
                        writer.write("\"5103\",");
                        writer.write("\"2111001\",");
                        writer.write("0,0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"FAPC\",");
                        } else {
                            writer.write("\"NCPC\",");
                        }
                        writer.write("\"A\",");
                        writer.write("\"" + c.getNumero() + "\",");
                        writer.write("\"\",");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"Conaprole F " + c.getNumero() + "\",");
                        } else {
                            writer.write("\"Conaprole NC " + c.getNumero() + "\",");
                        }
                        writer.write("\"\",");
                        writer.write("0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write(netoMinimo + ",");
                            writer.write("4,");
                            writer.write(c.getTotalMinimo() + ",");
                        } else {
                            writer.write("-" + netoMinimo + ",");
                            writer.write("4,");
                            writer.write("-" + c.getTotalMinimo() + ",");
                        }
                        writer.write("0,0.00,0.0000000,\"E\",\"\",\"\", , , , , , , , , , , ,");
                        //writer.write("\n");
                        String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                        writer.write(str);
                    }
                    if (netoBasico != 0) {
                        writer.write(dia + ",");
                        writer.write("\"5104\",");
                        writer.write("\"2111001\",");
                        writer.write("0,0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"FAPC\",");
                        } else {
                            writer.write("\"NCPC\",");
                        }
                        writer.write("\"A\",");
                        writer.write("\"" + c.getNumero() + "\",");
                        writer.write("\"\",");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"Conaprole F " + c.getNumero() + "\",");
                        } else {
                            writer.write("\"Conaprole NC " + c.getNumero() + "\",");
                        }
                        writer.write("\"\",");
                        writer.write("0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write(netoBasico + ",");
                            writer.write("3,");
                            writer.write(c.getTotalBasico() + ",");
                        } else {
                            writer.write("-" + netoBasico + ",");
                            writer.write("3,");
                            writer.write("-" + c.getTotalBasico() + ",");
                        }
                        writer.write("0,0.00,0.0000000,\"E\",\"\",\"\", , , , , , , , , , , ,");
                        //writer.write("\n");
                        String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                        writer.write(str);
                    }

                    //Ventas
                    if (ventaExcenta != 0) {
                        writer.write(dia + ",");
                        writer.write("\"11111\",");
                        writer.write("\"4112\",");
                        writer.write("0,0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"FAPC\",");
                        } else {
                            writer.write("\"NCPC\",");
                        }
                        writer.write("\"A\",");
                        writer.write("\"" + c.getNumero() + "\",");
                        writer.write("\"\",");
                        writer.write("\"Ventas del Dia" + "\",");
                        writer.write("\"\",");
                        writer.write("0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write(ventaExcenta + ",");
                            writer.write("0,");
                            writer.write(0.000000 + ",");
                        } else {
                            writer.write("-" + ventaExcenta + ",");
                            writer.write("0,");
                            writer.write("-" + 0.000000 + ",");
                        }
                        writer.write("0,0.00,0.0000000,\"I\",\"\",\"\", , , , , , , , , , , ,");
                        //writer.write("\n");
                        String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                        writer.write(str);
                    }
                    if (ventaMinimo != 0) {
                        writer.write(dia + ",");
                        writer.write("\"11111\",");
                        writer.write("\"4113\",");
                        writer.write("0,0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"FAPC\",");
                        } else {
                            writer.write("\"NCPC\",");
                        }
                        writer.write("\"A\",");
                        writer.write("\"" + c.getNumero() + "\",");
                        writer.write("\"\",");
                        writer.write("\"Ventas del Dia" + "\",");
                        writer.write("\"\",");
                        writer.write("0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write(ventaMinimo + ",");
                            writer.write("2,");
                            writer.write(ventaIvaMinimo + ",");
                        } else {
                            writer.write("-" + ventaMinimo + ",");
                            writer.write("2,");
                            writer.write("-" + ventaIvaMinimo + ",");
                        }
                        writer.write("0,0.00,0.0000000,\"I\",\"\",\"\", , , , , , , , , , , ,");
                        //writer.write("\n");
                        String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                        writer.write(str);
                    }
                    if (ventaBasico != 0) {
                        writer.write(dia + ",");
                        writer.write("\"11111\",");
                        writer.write("\"4114\",");
                        writer.write("0,0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"FAPC\",");
                        } else {
                            writer.write("\"NCPC\",");
                        }
                        writer.write("\"A\",");
                        writer.write("\"" + c.getNumero() + "\",");
                        writer.write("\"\",");
                        writer.write("\"Ventas del Dia" + "\",");
                        writer.write("\"\",");
                        writer.write("0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write(ventaBasico + ",");
                            writer.write("1,");
                            writer.write(ventaIvaBasico + ",");
                        } else {
                            writer.write("-" + ventaBasico + ",");
                            writer.write("1,");
                            writer.write("-" + ventaIvaBasico + ",");
                        }
                        writer.write("0,0.00,0.0000000,\"I\",\"\",\"\", , , , , , , , , , , ,");
                        //writer.write("\n");
                        String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                        writer.write(str);
                    }
                }

            }
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/
            }
        }
    }

    private void crearInterfazContadorRelece(Date fechaDesde, Date fechaHasta) throws Exception {
        Writer writer = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaDesde);
            int mes = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            String mesS = devolverMesString(mes);
            String nombreCapeta = "PlanillasContador" + mesS + year; 
            File directory = new File(String.valueOf(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta));
            if (!directory.exists()) {
                directory.mkdir();
            }
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta + "/InterfazReleceDesde" + formatter.format(fechaDesde).replace("-", "") + "Hasta" + formatter.format(fechaHasta).replace("-", "") + ".txt")), "utf-8"));
            DocumentoDeCompra fc = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Factura Relece");
            DocumentoDeCompra ncc = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Nota de Credito Relece");
            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consulta = session.createQuery("SELECT c FROM Compra c WHERE c.fecha BETWEEN :stDate AND :edDate and (c.tipoDocumento = :fc OR c.tipoDocumento = :ncc)");
            consulta.setDate("stDate", fechaDesde);
            consulta.setDate("edDate", fechaHasta);
            consulta.setEntity("fc", fc);
            consulta.setEntity("ncc", ncc);
            List<Compra> facturas = consulta.list();
            session.getTransaction().commit();
            session.close();
            for (Compra c : facturas) {
                boolean esDeEnvases = false;
                double netoExcento = 0;
                double netoBasico = 0;
                double netoMinimo = 0;
                double ventaExcenta = 0;
                double ventaMinimo = 0;
                double ventaBasico = 0;
                double ventaIvaMinimo = 0;
                double ventaIvaBasico = 0;
                for (CompraRenglon cr : c.getRenglones()) {
                    Articulo a = cr.getArticulo();
                    Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, c.getFecha());
                    if (this.esEnvase(a)) {
                        esDeEnvases = true;
                    }
                    if ("Excento".equals(a.getIva().getNombre())) {
                        netoExcento = netoExcento + cr.getTotal();
                        ventaExcenta = ventaExcenta + cr.getTotalPrecioVentaConIva();
                    }
                    if ("Minimo".equals(a.getIva().getNombre())) {
                        netoMinimo = netoMinimo + cr.getTotal();
                        ventaMinimo = ventaMinimo + cr.getTotalPrecioVentaConIva();
                        ventaIvaMinimo = ventaIvaMinimo + cr.getTotalPrecioVentaConIva() - cr.getTotalPrecioVentaSinIva();
                    }
                    if ("Basico".equals(a.getIva().getNombre())) {
                        netoBasico = netoBasico + cr.getTotal();
                        ventaBasico = ventaBasico + cr.getTotalPrecioVentaConIva();
                        ventaIvaBasico = ventaIvaBasico + cr.getTotalPrecioVentaConIva() - cr.getTotalPrecioVentaSinIva();
                    }
                }
                Calendar aux = Calendar.getInstance();
                aux.setTime(c.getFecha());
                int dia = aux.get(Calendar.DAY_OF_MONTH);
                if (esDeEnvases) {
                    writer.write(dia + ",");
                    writer.write("\"5105\",");
                    writer.write("\"2111001\",");
                    writer.write("0,0,");
                    if (c.getTipoDocumento().isSuma()) {
                        writer.write("\"FAPR\",");
                    } else {
                        writer.write("\"NCPR\",");
                    }
                    writer.write("\"A\",");
                    writer.write("\"" + c.getNumero() + "\",");
                    writer.write("\"\",");
                    if (c.getTipoDocumento().isSuma()) {
                        writer.write("\"Conaprole F " + c.getNumero() + "\",");
                    } else {
                        writer.write("\"Conaprole NC " + c.getNumero() + "\",");
                    }
                    writer.write("\"\",");
                    writer.write("0,");
                    if (c.getTipoDocumento().isSuma()) {
                        writer.write(c.getTotal() + ",");
                        writer.write("5,");
                        writer.write(c.getTotalBasico() + ",");
                    } else {
                        writer.write("-" + c.getTotal() + ",");
                        writer.write("5,");
                        writer.write("-" + c.getTotalBasico() + ",");
                    }
                    writer.write("0,0.00,0.0000000,\"E\",\"\",\"\", , , , , , , , , , , ,");
                    //writer.write("\n");
                    String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                    writer.write(str);
                } else {
                    //Compras
                    if (netoExcento != 0) {
                        writer.write(dia + ",");
                        writer.write("\"5102\",");
                        writer.write("\"2111001\",");
                        writer.write("0,0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"FAPR\",");
                        } else {
                            writer.write("\"NCPR\",");
                        }
                        writer.write("\"A\",");
                        writer.write("\"" + c.getNumero() + "\",");
                        writer.write("\"\",");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"Conaprole F " + c.getNumero() + "\",");
                        } else {
                            writer.write("\"Conaprole NC " + c.getNumero() + "\",");
                        }
                        writer.write("\"\",");
                        writer.write("0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write(netoExcento + ",");
                        } else {
                            writer.write("-" + netoExcento + ",");
                        }
                        writer.write("0,");
                        writer.write(0.000000 + ",");
                        writer.write("0,0.00,0.0000000,\"E\",\"\",\"\", , , , , , , , , , , ,");
                        //writer.write("\n");
                        String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                        writer.write(str);
                    }
                    if (netoMinimo != 0) {
                        writer.write(dia + ",");
                        writer.write("\"5103\",");
                        writer.write("\"2111001\",");
                        writer.write("0,0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"FAPR\",");
                        } else {
                            writer.write("\"NCPR\",");
                        }
                        writer.write("\"A\",");
                        writer.write("\"" + c.getNumero() + "\",");
                        writer.write("\"\",");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"Conaprole F " + c.getNumero() + "\",");
                        } else {
                            writer.write("\"Conaprole NC " + c.getNumero() + "\",");
                        }
                        writer.write("\"\",");
                        writer.write("0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write(netoMinimo + ",");
                            writer.write("4,");
                            writer.write(c.getTotalMinimo() + ",");
                        } else {
                            writer.write("-" + netoMinimo + ",");
                            writer.write("4,");
                            writer.write("-" + c.getTotalMinimo() + ",");
                        }
                        writer.write("0,0.00,0.0000000,\"E\",\"\",\"\", , , , , , , , , , , ,");
                        //writer.write("\n");
                        String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                        writer.write(str);
                    }
                    if (netoBasico != 0) {
                        writer.write(dia + ",");
                        writer.write("\"5104\",");
                        writer.write("\"2111001\",");
                        writer.write("0,0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"FAPR\",");
                        } else {
                            writer.write("\"NCPR\",");
                        }
                        writer.write("\"A\",");
                        writer.write("\"" + c.getNumero() + "\",");
                        writer.write("\"\",");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"Conaprole F " + c.getNumero() + "\",");
                        } else {
                            writer.write("\"Conaprole NC " + c.getNumero() + "\",");
                        }
                        writer.write("\"\",");
                        writer.write("0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write(netoBasico + ",");
                            writer.write("3,");
                            writer.write(c.getTotalBasico() + ",");
                        } else {
                            writer.write("-" + netoBasico + ",");
                            writer.write("3,");
                            writer.write("-" + c.getTotalBasico() + ",");
                        }
                        writer.write("0,0.00,0.0000000,\"E\",\"\",\"\", , , , , , , , , , , ,");
                        //writer.write("\n");
                        String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                        writer.write(str);
                    }

                    //Ventas
                    if (ventaExcenta != 0) {
                        writer.write(dia + ",");
                        writer.write("\"11111\",");
                        writer.write("\"4112\",");
                        writer.write("0,0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"FAPR\",");
                        } else {
                            writer.write("\"NCPR\",");
                        }
                        writer.write("\"A\",");
                        writer.write("\"" + c.getNumero() + "\",");
                        writer.write("\"\",");
                        writer.write("\"Ventas del Dia" + "\",");
                        writer.write("\"\",");
                        writer.write("0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write(ventaExcenta + ",");
                            writer.write("0,");
                            writer.write(0.000000 + ",");
                        } else {
                            writer.write("-" + ventaExcenta + ",");
                            writer.write("0,");
                            writer.write("-" + 0.000000 + ",");
                        }
                        writer.write("0,0.00,0.0000000,\"I\",\"\",\"\", , , , , , , , , , , ,");
                        //writer.write("\n");
                        String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                        writer.write(str);
                    }
                    if (ventaMinimo != 0) {
                        writer.write(dia + ",");
                        writer.write("\"11111\",");
                        writer.write("\"4113\",");
                        writer.write("0,0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"FAPR\",");
                        } else {
                            writer.write("\"NCPR\",");
                        }
                        writer.write("\"A\",");
                        writer.write("\"" + c.getNumero() + "\",");
                        writer.write("\"\",");
                        writer.write("\"Ventas del Dia" + "\",");
                        writer.write("\"\",");
                        writer.write("0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write(ventaMinimo + ",");
                            writer.write("2,");
                            writer.write(ventaIvaMinimo + ",");
                        } else {
                            writer.write("-" + ventaMinimo + ",");
                            writer.write("2,");
                            writer.write("-" + ventaIvaMinimo + ",");
                        }
                        writer.write("0,0.00,0.0000000,\"I\",\"\",\"\", , , , , , , , , , , ,");
                        //writer.write("\n");
                        String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                        writer.write(str);
                    }
                    if (ventaBasico != 0) {
                        writer.write(dia + ",");
                        writer.write("\"11111\",");
                        writer.write("\"4114\",");
                        writer.write("0,0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write("\"FAPR\",");
                        } else {
                            writer.write("\"NCPR\",");
                        }
                        writer.write("\"A\",");
                        writer.write("\"" + c.getNumero() + "\",");
                        writer.write("\"\",");
                        writer.write("\"Ventas del Dia" + "\",");
                        writer.write("\"\",");
                        writer.write("0,");
                        if (c.getTipoDocumento().isSuma()) {
                            writer.write(ventaBasico + ",");
                            writer.write("1,");
                            writer.write(ventaIvaBasico + ",");
                        } else {
                            writer.write("-" + ventaBasico + ",");
                            writer.write("1,");
                            writer.write("-" + ventaIvaBasico + ",");
                        }
                        writer.write("0,0.00,0.0000000,\"I\",\"\",\"\", , , , , , , , , , , ,");
                        //writer.write("\n");
                        String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                        writer.write(str);
                    }
                }

            }
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/
            }
        }
    }

    public void generarInformeDeFacturas() throws Exception {
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT f FROM Factura f");
        //consulta.setDate("stDate", fechaDesde);
        //consulta.setDate("edDate", fechaHasta);
        //consulta.setEntity("td", dc);
        List<Factura> facturas = consulta.list();
        session.getTransaction().commit();
        session.close();
        Workbook workbook = new XSSFWorkbook();
        Sheet hojaFacturas = workbook.createSheet("Facturas");
        Sheet hojaFacturasRenglones = workbook.createSheet("Renglones");
        int rowNum = 0;
        int rowNumHoja2 = 0;
        agregarEncabezadoInformeFacturas(hojaFacturas, rowNum++);
        agregarEncabezadoFacturasRenglones(hojaFacturasRenglones, rowNumHoja2++);

        for (Factura f : facturas) {
            agregarFacturaInforme(hojaFacturas, rowNum++, f);
            for (FacturaRenglon fr : f.getRenglones()) {
                agregarFacturasRenglonesInforme(hojaFacturasRenglones, rowNumHoja2++, fr);
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + "Facturas" + ".xlsx"));
            workbook.write(out);
            out.close();
        } catch (IOException ioexp) {
            int h = 0;
            System.out.println("Error");
        }

    }

    private void agregarEncabezadoInformeFacturas(Sheet hoja, int numero) throws Exception {
        Row row = hoja.createRow(numero);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue("Fecha");
        Cell cell2 = row.createCell(1);
        cell2.setCellValue("Numero");
        Cell cell3 = row.createCell(2);
        cell3.setCellValue("Cliente_Id");
        Cell cell4 = row.createCell(3);
        cell4.setCellValue("sub_total");
        Cell cell5 = row.createCell(4);
        cell5.setCellValue("iva_basico");
        Cell cell6 = row.createCell(5);
        cell6.setCellValue("iva_minimo");
        Cell cell7 = row.createCell(6);
        cell7.setCellValue("total");
        Cell cell8 = row.createCell(7);
        cell8.setCellValue("reparto_id");

        for (int i = 0; i <= 7; i++) {
            hoja.autoSizeColumn(i, true);
        }
    }

    private void agregarFacturaInforme(Sheet hoja, int numero, Factura f) throws Exception {
        Row row = hoja.createRow(numero);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue(formatter.format(f.getFecha()));
        Cell cell2 = row.createCell(1);
        cell2.setCellValue(f.getNumero());
        Cell cell3 = row.createCell(2);
        cell3.setCellValue(f.getCliente().getId());
        Cell cell4 = row.createCell(3);
        cell4.setCellValue(f.getSubtotal());
        Cell cell5 = row.createCell(4);
        cell5.setCellValue(f.getTotalBasico());
        Cell cell6 = row.createCell(5);
        cell6.setCellValue(f.getTotalMinimo());
        Cell cell7 = row.createCell(6);
        cell7.setCellValue(f.getTotal());
        Cell cell8 = row.createCell(7);
        cell8.setCellValue(f.getReparto().getId());

        for (int i = 0; i <= 7; i++) {
            hoja.autoSizeColumn(i, true);
        }
    }

    private void agregarEncabezadoFacturasRenglones(Sheet hoja, int numero) throws Exception  {
        Row row = hoja.createRow(numero);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue("numero");
        Cell cell2 = row.createCell(1);
        cell2.setCellValue("articulo_codigo");
        Cell cell3 = row.createCell(2);
        cell3.setCellValue("articulo_descripcion");
        Cell cell4 = row.createCell(3);
        cell4.setCellValue("cantidad");
        Cell cell5 = row.createCell(4);
        cell5.setCellValue("sub_total");
        Cell cell6 = row.createCell(5);
        cell6.setCellValue("iva");
        Cell cell7 = row.createCell(6);
        cell7.setCellValue("total");

        for (int i = 0; i <= 6; i++) {
            hoja.autoSizeColumn(i, true);
        }
    }

    private void agregarFacturasRenglonesInforme(Sheet hoja, int numero, FacturaRenglon fr) throws Exception {
        Row row = hoja.createRow(numero);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue(fr.getFactura().getNumero());
        Cell cell2 = row.createCell(1);
        cell2.setCellValue(fr.getArticulo().getCodigo());
        Cell cell3 = row.createCell(2);
        cell3.setCellValue(fr.getArticulo().getDescripcion());
        Cell cell4 = row.createCell(3);
        cell4.setCellValue(fr.getCantidad());
        Cell cell5 = row.createCell(4);
        cell5.setCellValue(fr.getSubtotal());
        Cell cell6 = row.createCell(5);
        cell6.setCellValue(fr.getIva());
        Cell cell7 = row.createCell(6);
        cell7.setCellValue(fr.getTotal());

        for (int i = 0; i <= 6; i++) {
            hoja.autoSizeColumn(i, true);
        }
    }

    public List<Compra> informeResumenEnvasesRelece(Date desdeFecha, Date hastaFecha) throws Exception {
        List<Compra> compras = new ArrayList<>();
        List<Compra> aux = SistemaCompras.getInstance().devolverComprasEntreFechas(desdeFecha, hastaFecha);
        DocumentoDeCompra fr = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Factura Relece");
        DocumentoDeCompra ncr = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Nota de Credito Relece");
        for (Compra c : aux) {
            if (c.getTipoDocumento().equals(fr) || c.getTipoDocumento().equals(ncr)) {
                for (CompraRenglon cr : c.getRenglones()) {
                    if (esEnvase(cr.getArticulo())) {
                        compras.add(c);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }
        return compras;
    }

    public List<Compra> informeResumenEnvasesCerram(Date desdeFecha, Date hastaFecha) throws Exception {
        List<Compra> compras = new ArrayList<>();
        List<Compra> aux = SistemaCompras.getInstance().devolverComprasEntreFechas(desdeFecha, hastaFecha);
        DocumentoDeCompra fc = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Factura Cerram");
        DocumentoDeCompra ncc = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Nota de Credito Cerram");
        for (Compra c : aux) {
            if (c.getTipoDocumento().equals(fc) || c.getTipoDocumento().equals(ncc)) {
                for (CompraRenglon cr : c.getRenglones()) {
                    if (esEnvase(cr.getArticulo())) {
                        compras.add(c);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }
        return compras;
    }

    public String[][] resumenDeComprasPorCliente(Date desdeFecha, Date hastaFecha, Cliente cliente) throws Exception {

        List<Factura> facturas = SistemaFacturas.getInstance().devolverFacturasEntreFechasYCliente(desdeFecha, hastaFecha, cliente, true);
        List<Factura> factsProrrateo = SistemaFacturas.getInstance().devolverFacturasEntreFechasYCliente(desdeFecha, hastaFecha, cliente, false);
        if (factsProrrateo != null) {
            facturas.addAll(factsProrrateo);
        }

        HashMap<Articulo, Object> resumenPorArticulo = new HashMap<>();
        double total = 0;
        int cantidadDeArticulos = 0;

        for (Factura f : facturas) {
            if (f.getTipoDocumento().isSuma()) {
                for (FacturaRenglon fr : f.getRenglones()) {
                    if (resumenPorArticulo.containsKey(fr.getArticulo())) {
                        String[] aux = (String[]) resumenPorArticulo.get(fr.getArticulo());
                        aux[0] = Double.toString(Double.parseDouble(aux[0]) + fr.getCantidad());
                        aux[2] = Double.toString(Double.parseDouble(aux[2]) + fr.getCantidad());
                    } else {
                        String[] valores = new String[3];
                        valores[0] = Double.toString(fr.getCantidad());
                        valores[1] = "0";
                        valores[2] = Double.toString(fr.getCantidad());
                        resumenPorArticulo.put(fr.getArticulo(), valores);
                    }
                }
                total = total + f.getTotal();
            } else {
                for (FacturaRenglon fr : f.getRenglones()) {
                    if (resumenPorArticulo.containsKey(fr.getArticulo())) {
                        String[] aux = (String[]) resumenPorArticulo.get(fr.getArticulo());
                        aux[1] = Double.toString(Double.parseDouble(aux[1]) + fr.getCantidad());
                        aux[2] = Double.toString(Double.parseDouble(aux[2]) - fr.getCantidad());
                    } else {
                        String[] valores = new String[3];
                        valores[0] = "0";
                        valores[1] = Double.toString(fr.getCantidad());
                        valores[2] = "-" + fr.getCantidad();
                        resumenPorArticulo.put(fr.getArticulo(), valores);
                    }
                }
                total = total - f.getTotal();
            }
        }
        Set<Articulo> arts = resumenPorArticulo.keySet();
        cantidadDeArticulos = arts.size();
        String[][] retorno = new String[cantidadDeArticulos + 1][5];
        int fila = 0;
        for (Articulo a : arts) {
            retorno[fila][0] = Long.toString(a.getCodigo());
            retorno[fila][1] = a.getDescripcion();
            String[] aux = (String[]) resumenPorArticulo.get(a);
            retorno[fila][2] = aux[0];
            retorno[fila][3] = aux[1];
            retorno[fila][4] = aux[2];
            fila++;
        }
        retorno[fila][0] = "Total";
        retorno[fila][1] = Double.toString(total);
        return retorno;
    }
    
    public void exportarEstadoDeCuentaCliente(Date desdeFecha, Date hastaFecha, Cliente cliente, List<Factura> facturas) {
        Workbook workbook = new XSSFWorkbook();
        Sheet hojaEstadoDeCuenta = workbook.createSheet("Estado de Cuenta");
        int rowNum = 1;
        
        agregarDesdeFechaInformeEstadoDeCuenta(desdeFecha, hastaFecha, cliente , hojaEstadoDeCuenta, rowNum++);
        agregarHastaFechaInformeEstadoDeCuenta(desdeFecha, hastaFecha, cliente, hojaEstadoDeCuenta, rowNum++);
        agregarClientenformeEstadoDeCuenta(desdeFecha, hastaFecha, cliente, hojaEstadoDeCuenta, rowNum++);
        rowNum++;
        agregarEncabezadoFacturasInformeEstadoDeCuenta(desdeFecha, hastaFecha, cliente, hojaEstadoDeCuenta, rowNum++);
        double total = 0;
        for (Factura f : facturas) {
            agregarFacturaEstadoDeCuenta(hojaEstadoDeCuenta, rowNum++, f);
            total = total + f.getTotal();
        }
        
        final DecimalFormat df;
        df = new DecimalFormat("0.00");
        
        rowNum = rowNum + 2;
        Row rowTotal = hojaEstadoDeCuenta.createRow(rowNum);
        Cell cellUno = rowTotal.createCell(1);
        cellUno.setCellValue("Total:");
        Cell cellUnoDos = rowTotal.createCell(2);
        cellUnoDos.setCellValue(df.format(total).replace(',', '.'));
        
        try {
            FileOutputStream out = new FileOutputStream(new File(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + "EstadoDeCuenta" + cliente.getNombre() + "Desde" + formatter.format(desdeFecha) + "Hasta" + formatter.format(hastaFecha) + ".xlsx"));
            workbook.write(out);
            out.close();
        } catch (IOException ioexp) {
            int h = 0;
            System.out.println("Error");
        }
    }
    
    private void agregarDesdeFechaInformeEstadoDeCuenta(Date desdeFecha, Date hastaFecha, Cliente cli,Sheet hoja, int numero) {
        Row rowUno = hoja.createRow(numero);
        numero = numero + 1;
        Cell cellUno = rowUno.createCell(0);
        cellUno.setCellValue("Desde Fecha:");
        Cell cellUnoDos = rowUno.createCell(1);
        cellUnoDos.setCellValue(formatter.format(desdeFecha));
    }
    
    private void agregarHastaFechaInformeEstadoDeCuenta(Date desdeFecha, Date hastaFecha, Cliente cli,Sheet hoja, int numero) {
        Row rowDos = hoja.createRow(numero);
        numero = numero + 1;
        Cell cellDos = rowDos.createCell(0);
        cellDos.setCellValue("Hasta Fecha:");
        Cell cellDosDos = rowDos.createCell(1);
        cellDosDos.setCellValue(formatter.format(hastaFecha));
    }
    
    private void agregarClientenformeEstadoDeCuenta(Date desdeFecha, Date hastaFecha, Cliente cli,Sheet hoja, int numero) {
        Row rowTres = hoja.createRow(numero);
        numero = numero + 2;
        Cell cellTres = rowTres.createCell(0);
        cellTres.setCellValue("Cliente: ");
        Cell cellTresDos = rowTres.createCell(1);
        cellTresDos.setCellValue(cli.getNombre());
       
    }
    
    private void agregarEncabezadoFacturasInformeEstadoDeCuenta(Date desdeFecha, Date hastaFecha, Cliente cli,Sheet hoja, int numero) {
        
        Row row = hoja.createRow(numero);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue("Fecha");
        Cell cell2 = row.createCell(1);
        cell2.setCellValue("Numero");
        Cell cell3 = row.createCell(2);
        cell3.setCellValue("Total");

        for (int i = 0; i <= 2; i++) {
            hoja.autoSizeColumn(i, true);
        }
    }

    private void agregarFacturaEstadoDeCuenta(Sheet hoja, int numero, Factura f) {
        Row row = hoja.createRow(numero);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue(formatter.format(f.getFecha()));
        Cell cell2 = row.createCell(1);
        cell2.setCellValue(f.getNumero());
        Cell cell3 = row.createCell(2);
        cell3.setCellValue(f.getTotal());
     
        for (int i = 0; i <= 7; i++) {
            hoja.autoSizeColumn(i, true);
        }
    }
    
}
