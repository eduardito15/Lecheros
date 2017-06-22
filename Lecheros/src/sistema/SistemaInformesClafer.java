/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;

import dao.GenericDAO;
import dominio.Anep;
import dominio.Articulo;
import dominio.Chofer;
import dominio.Compra;
import dominio.CompraRenglon;
import dominio.FamiliaDeProducto;
import dominio.Inau;
import dominio.Inventario;
import dominio.InventarioRenglon;
import dominio.Liquidacion;
import dominio.Reparto;
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
import javax.swing.JOptionPane;
import org.hibernate.Query;
import org.hibernate.Session;
import ui.usuarios.Constantes;
import util.Util;

/**
 *
 * @author Edu
 */
public class SistemaInformesClafer {
    
    private static SistemaInformesClafer instance;
    SimpleDateFormat formatter;

    /**
     * @return the instance
     */
    public static SistemaInformesClafer getInstance() {
        if (instance == null) {
            instance = new SistemaInformesClafer();
        }
        return instance;
    }

    private SistemaInformesClafer() {
        formatter = new SimpleDateFormat("dd-MM-yyyy");
    }
    
    public String[][] informeBonificacionPorRoturas(Date desdeFecha, Date hastaFecha, Chofer chofer) throws Exception {
        String[][] retorno = new String[10][5];
        DecimalFormat df;
        df = new DecimalFormat("0.00");

        FamiliaDeProducto fp = SistemaMantenimiento.getInstance().devolverFamiliaPorCodigo(0);
        List<Articulo> articulos = SistemaMantenimientoArticulos.getInstance().devolverArticulosPorFamilia(fp);
        List<Liquidacion> liquidaciones = SistemaLiquidaciones.getInstance().devolverLiquidacionesEntreFechasYChofer(desdeFecha, hastaFecha, chofer);
        List<Compra> compras = new ArrayList<>();
        for(Liquidacion l : liquidaciones){
            try {
                List<Compra> aux = SistemaLiquidaciones.getInstance().devolverComprasParaFechaYReparto(l.getFecha(), l.getReparto());
                compras.addAll(aux);
            } catch (ParseException ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
            }
        }
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
                    
                    
                    double diferencia = totalCompras - totalRemitos;
                    double unoPorMil = diferencia/1000;

                    retorno[fila][3] = df.format(diferencia).replace(',', '.');
                    retorno[fila][4] = df.format(unoPorMil).replace(',', '.');

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
        /*if (a.getCodigo() == 110 || a.getCodigo() == 112 || a.getCodigo() == 113 || a.getCodigo() == 114 || a.getCodigo() == 115 || a.getCodigo() == 116 || a.getCodigo() == 119 || a.getCodigo() == 122 || a.getCodigo() == 123 || a.getCodigo() == 128 || a.getCodigo() == 131 || a.getCodigo() == 132 || a.getCodigo() == 133 || a.getCodigo() == 134) {
            retorno = true;
        }*/
        retorno = Util.esLeche(a);
        return retorno;
    }
    
    public List<Compra> informeResumenEnvases(Date desdeFecha, Date hastaFecha) throws Exception {
        List<Compra> compras = new ArrayList<>();
        List<Compra> aux = SistemaCompras.getInstance().devolverComprasEntreFechas(desdeFecha, hastaFecha);
        for(Compra c : aux){
            for(CompraRenglon cr : c.getRenglones()){
                if(esEnvase(cr.getArticulo())){
                    compras.add(c);
                    break;
                } else {
                    break;
                }
            }
        }
        return compras;
    }
    
    public Object[] informeControlChequesConaprole(Date fecha, Reparto reparto) throws Exception {
        Object[] retorno = new Object[3];
        List<Compra> compras = new ArrayList<>();
        if(reparto == null){
            List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
            for(Reparto r: repartos){
                try {
                    List<Compra> aux = SistemaLiquidaciones.getInstance().devolverComprasParaFechaYReparto(fecha, r);
                    compras.addAll(aux);
                } catch (ParseException ex) {
                   String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                   SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                }
            }
        } else {
            try {
                compras = SistemaLiquidaciones.getInstance().devolverComprasParaFechaYReparto(fecha, reparto);
            } catch (ParseException ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
            }
        }
        List<Compra> comprasLeche = new ArrayList<>();
        List<Compra> comprasEnvases = new ArrayList<>();
        List<Compra> comprasProductos = new ArrayList<>();
        for(Compra c: compras){
            for(CompraRenglon cr: c.getRenglones()){
                if(esEnvase(cr.getArticulo())){
                    comprasEnvases.add(c);
                } else {
                    if(esLeche(cr.getArticulo())){
                        comprasLeche.add(c);
                    } else {
                        comprasProductos.add(c);
                    }
                }
                break;
            }
        }
        retorno[0] = comprasLeche;
        retorno[1] = comprasEnvases;
        retorno[2] = comprasProductos;
        return retorno;
    }
    
    public Object[][] informeControlChequesConaproleDetallado(Date fecha, Reparto reparto) throws Exception {
        Object[][] retorno;
        List<Compra> comprasLeche = new ArrayList<>();
        List<Compra> comprasEnvases = new ArrayList<>();
        List<Compra> comprasProductos = new ArrayList<>();
        int fila = 0;
        //List<Compra> compras = new ArrayList<>();
        if(reparto == null){
            List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
            int cantRenglones = repartos.size() + 2;
            retorno = new Object[cantRenglones][5];
            
            double totalLeche = 0;
            double totalEnvases = 0;
            double totalProductos = 0;
            for(Reparto r: repartos){
                try {
                    double totalLecheReparto = 0;
                    double totalEnvasesReparto = 0;
                    double totalProductosReparto = 0;
                    List<Compra> compras = SistemaLiquidaciones.getInstance().devolverComprasParaFechaYReparto(fecha, r);
                    for(Compra c: compras){
                        for(CompraRenglon cr: c.getRenglones()){
                            if(esEnvase(cr.getArticulo())){
                                comprasEnvases.add(c);
                                totalEnvases = totalEnvases + c.getTotal();
                                totalEnvasesReparto = totalEnvasesReparto + c.getTotal();
                            } else {
                                if(esLeche(cr.getArticulo())){
                                    comprasLeche.add(c);
                                    totalLeche = totalLeche + c.getTotal();
                                    totalLecheReparto = totalLecheReparto + c.getTotal();
                                } else {
                                    comprasProductos.add(c);
                                    totalProductos = totalProductos + c.getTotal();
                                    totalProductosReparto = totalProductosReparto + c.getTotal();
                                }
                            }
                            break;
                        }
                        
                    }
                    //Agrego los totales del reparto a la tabla del resultado
                    retorno[fila][0] = r.getNombre();
                    retorno[fila][1] = totalLecheReparto;
                    retorno[fila][2] = totalEnvasesReparto;
                    retorno[fila][3] = totalProductosReparto;
                    retorno[fila][4] = totalLecheReparto + totalEnvasesReparto + totalProductosReparto;
                    fila++;
                } catch (ParseException ex) {
                   String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                   SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                }
            }
            //Agrego los totales y las listas de boletas al final 
            retorno[fila][0] = "Totales";
            retorno[fila][1] = totalLeche;
            retorno[fila][2] = totalEnvases;
            retorno[fila][3] = totalProductos;
            retorno[fila][4] = totalLeche + totalEnvases + totalProductos;
            fila++;
            retorno[fila][0] = comprasLeche;
            retorno[fila][1] = comprasEnvases;
            retorno[fila][2] = comprasProductos;
        } else {
            retorno = new Object[3][5];
            try {
                int cantRenglones = 1 * 3 + 2;
                retorno = new Object[cantRenglones][5];

                double totalLeche = 0;
                double totalEnvases = 0;
                double totalProductos = 0;
                List<Compra> compras = SistemaLiquidaciones.getInstance().devolverComprasParaFechaYReparto(fecha, reparto);
                for(Compra c: compras){
                        for(CompraRenglon cr: c.getRenglones()){
                            if(esEnvase(cr.getArticulo())){
                                comprasEnvases.add(c);
                                totalEnvases = totalEnvases + c.getTotal();
                                
                            } else {
                                if(esLeche(cr.getArticulo())){
                                    comprasLeche.add(c);
                                    totalLeche = totalLeche + c.getTotal();
                                    
                                } else {
                                    comprasProductos.add(c);
                                    totalProductos = totalProductos + c.getTotal();
                                    
                                }
                            }
                
                        }
                        break;
                    }
                    //Agrego los totales del reparto a la tabla del resultado
                    retorno[fila][0] = reparto.getNombre();
                    retorno[fila][1] = totalLeche;
                    retorno[fila][2] = totalEnvases;
                    retorno[fila][3] = totalProductos;
                    retorno[fila][4] = totalLeche + totalEnvases + totalProductos;
                    fila++;
                    
                    retorno[fila][0] = "Totales";
                    retorno[fila][1] = totalLeche;
                    retorno[fila][2] = totalEnvases;
                    retorno[fila][3] = totalProductos;
                    retorno[fila][4] = totalLeche + totalEnvases + totalProductos;
                    fila++;
                    retorno[fila][0] = comprasLeche;
                    retorno[fila][1] = comprasEnvases;
                    retorno[fila][2] = comprasProductos;
            } catch (ParseException ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
            }
        }

        return retorno;
    }
    
    public List<Compra> devolverComprasEntreFechasDeLiquidacionYReparto(Date fechaDesde, Date fechaHasta, Reparto r) throws Exception {

        List<Compra> retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Compra WHERE fechaLiquidacion BETWEEN :stDate AND :edDate");
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public List<Compra> devolverComprasEntreFechaDeLiquidacionParaChofer(Date fechaDesde, Date fechaHasta, Chofer chofer) throws Exception {
        List<Compra> retorno = new ArrayList<>();
        List<Liquidacion> liquidaciones = SistemaLiquidaciones.getInstance().devolverLiquidacionesEntreFechasYChofer(fechaDesde, fechaHasta, chofer);
        for(Liquidacion l: liquidaciones){
            try {
                List<Compra> aux = SistemaLiquidaciones.getInstance().devolverComprasParaFechaYReparto(l.getFecha(), l.getReparto());
                for(Compra c: aux){
                    for(CompraRenglon cr : c.getRenglones()){
                        if(!esEnvase(cr.getArticulo())&&!esLeche(cr.getArticulo())){
                            retorno.add(c);
                        }
                        break;
                    }
                }
            } catch (ParseException ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
            }
        }
        return retorno;
    }
    
    public String[][] cargaDeLechePorReparto(Date fechaDesde, Date fechaHasta, Reparto reparto) throws Exception {
        String[][] retorno;
        List<Articulo> articulos = SistemaMantenimientoArticulos.getInstance().devolverArticulosEntreCodigos(110,134);
        HashMap<Articulo, Object> comprasPorArticulo = new HashMap<>();
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        for(Articulo a: articulos){
            session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consultaCompras = session.createQuery("SELECT sum(cr.cantidad) FROM Compra c,CompraRenglon cr, DocumentoDeCompra dc WHERE c.tipoDocumento = dc AND dc.suma = :s AND c.fechaLiquidacion >= :fechaD AND c.fechaLiquidacion <= :fechaH AND c.reparto = :r AND cr.compra = c AND cr.articulo = :art");
            consultaCompras.setEntity("art", a);
            consultaCompras.setEntity("r", reparto);
            consultaCompras.setDate("fechaD", fechaDesde);
            consultaCompras.setDate("fechaH", fechaHasta);
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
            Query consultaDevoluciones = session.createQuery("SELECT sum(cr.cantidad) FROM Compra c,CompraRenglon cr, DocumentoDeCompra dc WHERE c.tipoDocumento = dc AND dc.suma = :s AND c.fecha >= :fechaD AND c.fecha <= :fechaH AND c.reparto = :r AND cr.compra = c AND cr.articulo = :art");
            consultaDevoluciones.setEntity("art", a);
            consultaDevoluciones.setEntity("r", reparto);
            consultaDevoluciones.setDate("fechaD", fechaDesde);
            consultaDevoluciones.setDate("fechaH", fechaHasta);
            consultaDevoluciones.setBoolean("s", false);
            if (consultaDevoluciones.uniqueResult() != null) {
                double cantidadDevoluciones = (double) consultaDevoluciones.uniqueResult();
                comprasPorArticulo.replace(a, (double)comprasPorArticulo.get(a) - cantidadDevoluciones);
            }
            session.getTransaction().commit();
            session.close();
        }
        
        //Obtengo Reintegros de Pinchadas
        Articulo reintegroPinchadas = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(170118);
        session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consultaDevoluciones = session.createQuery("SELECT sum(cr.cantidad) FROM Compra c,CompraRenglon cr, DocumentoDeCompra dc WHERE c.tipoDocumento = dc AND c.fecha >= :fechaD AND c.fecha <= :fechaH AND c.reparto = :r AND cr.compra = c AND cr.articulo = :art");
        consultaDevoluciones.setEntity("art", reintegroPinchadas);
        consultaDevoluciones.setEntity("r", reparto);
        consultaDevoluciones.setDate("fechaD", fechaDesde);
        consultaDevoluciones.setDate("fechaH", fechaHasta);
        double cantidadReintegros = 0;
        if(consultaDevoluciones.uniqueResult() != null){
            cantidadReintegros = (double) consultaDevoluciones.uniqueResult();
        }
        session.getTransaction().commit();
        session.close();
        List<Inau> inaus = SistemaInformes.getInstance().devolverInausEntreFechasYReparto(fechaDesde, fechaHasta, reparto);
        int cantidadInau = 0;
        for(Inau i : inaus){
            cantidadInau = cantidadInau + i.getLitrosTotal();
        }
        int cantidadAnep = 0;
        List<Anep> aneps = SistemaInformes.getInstance().devolverAnepsEntreFechasYReparto(fechaDesde, fechaHasta, reparto);
        for(Anep a : aneps){
            cantidadAnep = cantidadAnep + a.getLitrosTotal();
        }
        
        //Cargo el resultado en la matriz
        retorno = new String[comprasPorArticulo.size()+1][7];
        int fila = 0;
        double totalLitros = 0;
        for(Articulo a: articulos){
            if(comprasPorArticulo.containsKey(a)){
                retorno[fila][0] = reparto.toString();
                retorno[fila][1] = Integer.toString(a.getCodigo());
                retorno[fila][2] = comprasPorArticulo.get(a).toString();
                if(a.getCodigo() == 110){
                    retorno[fila][3] = Double.toString(cantidadReintegros);
                    retorno[fila][4] = Integer.toString(cantidadInau);
                    retorno[fila][5] = Integer.toString(cantidadAnep);
                } else {
                    retorno[fila][3] = "0";
                    retorno[fila][4] = "0";
                    retorno[fila][5] = "0";
                }
                retorno[fila][6] = Double.toString(Double.parseDouble(retorno[fila][2]) - Double.parseDouble(retorno[fila][3]) - Double.parseDouble(retorno[fila][4]) - Double.parseDouble(retorno[fila][5]));            
                totalLitros = totalLitros + Double.parseDouble(retorno[fila][6]);
                fila++;
            }
        }
        retorno[fila][0] = "Total";
        retorno[fila][1] = Double.toString(totalLitros);
        
        return retorno;
    }
    
    public String[][] totalDeVentaDeProductosPorReparto(Date fechaDesde, Date fechaHasta, Reparto reparto) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaDesde);
        cal.add(Calendar.DATE, -1);
        Inventario existencia = null;
        List<Compra> compras = null;
        Inventario sobrante = null;
        
        HashMap<Articulo, String[]> matrizResultado = new HashMap<>();
        double totalPlata = 0;
        try {
            existencia = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(cal.getTime(), reparto);
            compras = SistemaLiquidaciones.getInstance().devolverComprasEntreFechasYReparto(fechaDesde, fechaHasta, reparto);
            sobrante = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaHasta, reparto);
        } catch (ParseException ex) {
            Logger.getLogger(SistemaInformesClafer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(existencia != null){
            for(InventarioRenglon ir: existencia.getRenglones()){
                if(!esEnvase(ir.getArticulo())&&!esLeche(ir.getArticulo())){
                    if(matrizResultado.containsKey(ir.getArticulo())){
                        String[] aux = matrizResultado.get(ir.getArticulo());
                        aux[0] = Double.toString(Double.parseDouble(aux[0]) + ir.getCantidad());
                        aux[4] = Double.toString(Double.parseDouble(aux[0]) + Double.parseDouble(aux[1]) - Double.parseDouble(aux[2]) - Double.parseDouble(aux[3]));
                        matrizResultado.replace(ir.getArticulo(), aux);
                    } else {
                        String[] aux = {Double.toString(ir.getCantidad()),"0","0","0","0"};
                        aux[4] = Double.toString(Double.parseDouble(aux[0]) + Double.parseDouble(aux[1]) - Double.parseDouble(aux[2]) - Double.parseDouble(aux[3]));
                        matrizResultado.put(ir.getArticulo(), aux);
                    }
                    totalPlata  = totalPlata + ir.getTotal();
                } 
            }
        }
        if(compras != null){
            for(Compra c : compras){
                if(c.getTipoDocumento().isSuma()){
                    for(CompraRenglon cr: c.getRenglones()){
                        if(esEnvase(cr.getArticulo()) || esLeche(cr.getArticulo())){
                            break;
                        } else {
                            if (matrizResultado.containsKey(cr.getArticulo())) {
                                String[] aux = matrizResultado.get(cr.getArticulo());
                                aux[1] = Double.toString(Double.parseDouble(aux[1]) + cr.getCantidad());
                                aux[4] = Double.toString(Double.parseDouble(aux[0]) + Double.parseDouble(aux[1]) - Double.parseDouble(aux[2]) - Double.parseDouble(aux[3]));
                                matrizResultado.replace(cr.getArticulo(), aux);
                            } else {
                                String[] aux = {"0", Double.toString(cr.getCantidad()), "0", "0", "0"};
                                aux[4] = Double.toString(Double.parseDouble(aux[0]) + Double.parseDouble(aux[1]) - Double.parseDouble(aux[2]) - Double.parseDouble(aux[3]));
                                matrizResultado.put(cr.getArticulo(), aux);
                            }
                            totalPlata = totalPlata + cr.getTotal();
                        }
                    }
                } else {
                    for (CompraRenglon cr : c.getRenglones()) {
                        if (esEnvase(cr.getArticulo()) || esLeche(cr.getArticulo())) {
                            break;
                        } else if (matrizResultado.containsKey(cr.getArticulo())) {
                            String[] aux = matrizResultado.get(cr.getArticulo());
                            aux[2] = Double.toString(Double.parseDouble(aux[2]) + cr.getCantidad());
                            aux[4] = Double.toString(Double.parseDouble(aux[0]) + Double.parseDouble(aux[1]) - Double.parseDouble(aux[2]) - Double.parseDouble(aux[3]));
                            matrizResultado.replace(cr.getArticulo(), aux);
                        } else {
                            String[] aux = {"0", "0", Double.toString(cr.getCantidad()), "0", "0"};
                            aux[4] = Double.toString(Double.parseDouble(aux[0]) + Double.parseDouble(aux[1]) - Double.parseDouble(aux[2]) - Double.parseDouble(aux[3]));
                            matrizResultado.put(cr.getArticulo(), aux);
                        }
                        totalPlata = totalPlata - cr.getSubtotal();
                    }
                }
            }
        }
        if(sobrante != null){
            for (InventarioRenglon ir : sobrante.getRenglones()) {
                if (!esEnvase(ir.getArticulo()) && !esLeche(ir.getArticulo())) {
                    if (matrizResultado.containsKey(ir.getArticulo())) {
                        String[] aux = matrizResultado.get(ir.getArticulo());
                        aux[3] = Double.toString(Double.parseDouble(aux[3]) + ir.getCantidad());
                        aux[4] = Double.toString(Double.parseDouble(aux[0]) + Double.parseDouble(aux[1]) - Double.parseDouble(aux[2]) - Double.parseDouble(aux[3]));
                        matrizResultado.replace(ir.getArticulo(), aux);
                    } else {
                        String[] aux = {"0", "0", "0", Double.toString(ir.getCantidad()), "0"};
                        aux[4] = Double.toString(Double.parseDouble(aux[0]) + Double.parseDouble(aux[1]) - Double.parseDouble(aux[2]) - Double.parseDouble(aux[3]));
                        matrizResultado.put(ir.getArticulo(), aux);
                    }
                    totalPlata = totalPlata - ir.getTotal();
                }
            }
        }
        Set<Articulo> arts = matrizResultado.keySet();
        String[][] retorno = new String[arts.size()+1][7];
        int fila = 0;
        for(Articulo a : arts){
            retorno[fila][0] = Integer.toString(a.getCodigo());
            retorno[fila][1] = a.getDescripcion();
            String[] totalesPorArticulo = matrizResultado.get(a);
            retorno[fila][2] = totalesPorArticulo[0];
            retorno[fila][3] = totalesPorArticulo[1];
            retorno[fila][4] = totalesPorArticulo[2];
            retorno[fila][5] = totalesPorArticulo[3];
            retorno[fila][6] = totalesPorArticulo[4];
            fila++;
        }
        retorno[fila][0] = "Total";
        retorno[fila][1] = Double.toString(totalPlata);
        return retorno;
    }
    
    public List<Liquidacion> informeResumenLiquidaciones(Date fechaDesde, Date fechaHasta, Chofer chofer) throws Exception {
        List<Liquidacion> liquidaciones = SistemaLiquidaciones.getInstance().devolverLiquidacionesEntreFechasYChofer(fechaDesde, fechaHasta, chofer);
        return liquidaciones;
    }
}
