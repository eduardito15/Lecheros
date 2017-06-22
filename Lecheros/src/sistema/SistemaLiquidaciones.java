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
import dominio.CoeficienteUtilidadCompras;
import dominio.Compra;
import dominio.CompraRenglon;
import dominio.ConfiguracionLiquidacion;
import dominio.DocumentoDeCompra;
import dominio.Factura;
import dominio.FiadoChofer;
import dominio.FiadoChoferRenglon;
import dominio.Gasto;
import dominio.GrupoDeArticulos;
import dominio.Inau;
import dominio.Inventario;
import dominio.InventarioGiamo;
import dominio.Jornales;
import dominio.Liquidacion;
import dominio.Precio;
import dominio.Reparto;
import dominio.RepartoCompuesto;
import dominio.Retencion;
import impresion.Inventarios.ImprimirInventario;
import impresion.Inventarios.ImprimirInventarioImp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lecheros.Lecheros;
import org.hibernate.Query;
import org.hibernate.Session;
import ui.usuarios.Constantes;
import util.Util;

/**
 *
 * @author Edu
 */
public class SistemaLiquidaciones {

    private static SistemaLiquidaciones instance;
    
    private static ImprimirInventario impresionInventario;

    /**
     * @return the instance
     */
    public static SistemaLiquidaciones getInstance() {
        if (instance == null) {
            instance = new SistemaLiquidaciones();
            asignarImpresion();
        }
        return instance;
    }

    /**
     * @return the impresionInventario
     */
    public static ImprimirInventario getImpresionInventario() {
        return impresionInventario;
    }

    /**
     * @param aImpresionInventario the impresionInventario to set
     */
    public static void setImpresionInventario(ImprimirInventario aImpresionInventario) {
        impresionInventario = aImpresionInventario;
    }

    private SistemaLiquidaciones() {

    }
    
    private static void asignarImpresion() {
        if ("Relece".equals(Lecheros.nombreEmpresa)) {
            setImpresionInventario(new ImprimirInventarioImp());
        }
    }

    //Metodos Inventario
    public Inventario devolverInventarioParaFechaYReparto(Date fecha, Reparto r) throws Exception {
        Inventario retorno;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Inventario where fecha = :fechaBusqueda and reparto_id = " + r.getId());
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        retorno = (Inventario) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean guardarInventario(Inventario i) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarInventario, "Ingreo el inventario del reparto :  " + i.getReparto() );
        return GenericDAO.getGenericDAO().guardar(i);
    }

    public boolean actualizarInventario(Inventario i) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarInventario, "Actualizo el inventario del reparto: " + i.getReparto() );
        return GenericDAO.getGenericDAO().actualizar(i);
    }
    
    public boolean eliminarInventario(Inventario i) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarInventario, "Elimino el inventario del reparto: " + i.getReparto() );
        return GenericDAO.getGenericDAO().borrar(i);
    }

    //METODOS INVENTARIO GIAMO / USADO PARA DEJAR FACTURACION PARA EL PROXIMO DIA EN EMPRESA GIAMO QUE NO HACE INVENTARIO
    
    public boolean guardarInventarioGiamo(InventarioGiamo i) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarInventario, "Ingreo el inventario giamo del reparto :  " + i.getReparto() );
        return GenericDAO.getGenericDAO().guardar(i);
    }

    public boolean actualizarInventarioGiamo(InventarioGiamo i) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarInventario, "Actualizo el inventario giamo del reparto: " + i.getReparto() );
        return GenericDAO.getGenericDAO().actualizar(i);
    }
    
    public InventarioGiamo devolverInventarioGiamoParaFechaYReparto(Date fecha, Reparto r) throws Exception {
        InventarioGiamo retorno;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM InventarioGiamo where fecha = :fechaBusqueda and reparto_id = " + r.getId());
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        retorno = (InventarioGiamo) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    //FIN METODOS INVENTARIO GIAMO --------------------------------------------------------------------------------------
    
    //Metodos Gastos
    public Gasto devolverGastoParaFechaYReparto(Date fecha, Reparto r) throws Exception {
        Gasto retorno;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Gasto where fecha = :fechaBusqueda and reparto_id = " + r.getId());
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        retorno = (Gasto) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean guardarGasto(Gasto g) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarGasto, "Ingreso el gasto para el reparto : " + g.getReparto() );
        return GenericDAO.getGenericDAO().guardar(g);
    }

    public boolean actualizarGasto(Gasto g) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarGasto, "Ingreso el gasto para el reparto : " + g.getReparto() );
        return GenericDAO.getGenericDAO().actualizar(g);
    }
    
    public boolean eliminarGasto(Gasto g) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarGasto, "Elimino el gasto para el reparto : " + g.getReparto() );
        return GenericDAO.getGenericDAO().borrar(g);
    }
    
    //Metodos para Jornales
    public Jornales devolverJornalesParaFecha(Date fecha) throws Exception {
        Jornales retorno;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Jornales where fecha = :fechaBusqueda ");
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        retorno = (Jornales) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean guardarJornales(Jornales j) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarGasto, "Ingreso los jornales de la fecha para el reparto : " + j.getFecha());
        return GenericDAO.getGenericDAO().guardar(j);
    }

    public boolean actualizarJornales(Jornales j) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarGasto, "Ingreso los jornales de la fecha : " + j.getFecha());
        return GenericDAO.getGenericDAO().actualizar(j);
    }


    //Metodos que usa la liquidacion sobre las compras
    public List<Compra> devolverComprasParaFechaYReparto(Date fecha, Reparto r) throws Exception {
        List<Compra> retorno;
        Reparto repartoCompartido = this.tieneRepartoCompartido(r);
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Compra where fechaLiquidacion = :fechaBusqueda and reparto_id = " + r.getId());
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        if(repartoCompartido != null){
            session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            consulta = session.createQuery("FROM Compra where fechaLiquidacion = :fechaBusqueda and reparto_id = " + repartoCompartido.getId());
            consulta.setDate("fechaBusqueda", formatter.parse(f));
            List<Compra> comprasRepartoCompartido = consulta.list();
            session.getTransaction().commit();
            session.close();
            if(comprasRepartoCompartido != null) {
                if(retorno != null) {
                    retorno.addAll(comprasRepartoCompartido);
                } else {
                    retorno = comprasRepartoCompartido;
                }
            }
        }
        return retorno;
    }
    
    public Reparto tieneRepartoCompartido(Reparto r) {
        Reparto retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM RepartoCompuesto where reparto1 = :reparto or reparto2 = :reparto" );
        consulta.setParameter("reparto", r);
        RepartoCompuesto rc = (RepartoCompuesto) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        if(rc != null) {
            if(rc.getReparto1().equals(r)){
                retorno = rc.getReparto2();
            } else {
                retorno = rc.getReparto1();
            }
        }
        return retorno;
    }

    //Metodos que usa la liquidacion sobre las compras
    public List<Compra> devolverComprasEntreFechasYReparto(Date fechaDesde, Date fechaHasta, Reparto r) throws Exception {
        List<Compra> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Compra where (fechaLiquidacion BETWEEN :fechaBusquedaDesde AND :fechaBusquedaHasta) and reparto_id = " + r.getId());
        consulta.setDate("fechaBusquedaDesde", fechaDesde);
        consulta.setDate("fechaBusquedaHasta", fechaHasta);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public double devolverSaldoAnteriorParaFechaYReparto(Date fecha, Reparto r) throws Exception {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        if (SistemaMantenimiento.getInstance().devolverConfiguracionLiquidacion().isModoAcumulativo()) {
            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consulta = session.createQuery("SELECT l FROM Liquidacion l where fecha = (SELECT max(ll.fecha) FROM Liquidacion ll where ll.reparto = l.reparto AND ll.fecha != :fechaBusqueda AND ll.fecha < :fechaBusqueda) AND l.reparto = :r");
            consulta.setDate("fechaBusqueda", formatter.parse(f));
            consulta.setParameter("r", r);
            Liquidacion l = (Liquidacion) consulta.uniqueResult();
            session.getTransaction().commit();
            session.close();
            if (l != null) {
                return l.getDeuda();
            } else {
                session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
                session.beginTransaction();
                Query consultaDos = session.createQuery("SELECT i FROM Inventario i where fecha = (SELECT max(ii.fecha) FROM Inventario ii where ii.reparto = i.reparto AND ii.fecha != :fechaBusqueda AND ii.fecha < :fechaBusqueda) AND i.reparto = :r");
                consultaDos.setDate("fechaBusqueda", formatter.parse(f));
                consultaDos.setParameter("r", r);
                Inventario i = (Inventario) consultaDos.uniqueResult();
                session.getTransaction().commit();
                session.close();
                if (i != null) {
                    return i.getTotal();
                } else {
                    return 0;
                }
            }
        } else {
            //No es modo acumulativo. Asi que para el saldo anterior busco el inventario y el fiado de chofer si existe, y los sumo.
            Inventario i = this.devolverUltimoInventario(fecha, r);
            //Busco el Fiado Chofer si existe
            if (SistemaMantenimiento.getInstance().devolverConfiguracionLiquidacion().isMostrarFiadoClientesCobraChofer()) {
                FiadoChofer fc = this.devolverUltimoFiadoChofer(fecha, r);
                if (fc == null) {
                    if (i != null) {
                        return i.getTotal();
                    } else {
                        return 0;
                    }
                } else {

                    return i.getTotal() + fc.getTotal();
                }
            } else {
                return i.getTotal();
            }
        }
    }

    public Inventario devolverUltimoInventario(Date fecha, Reparto r) throws Exception {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT i FROM Inventario i where fecha = (SELECT max(ii.fecha) FROM Inventario ii where ii.reparto = i.reparto AND ii.fecha != :fechaBusqueda AND ii.fecha < :fechaBusqueda) AND i.reparto = :r");
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        consulta.setParameter("r", r);
        Inventario i = (Inventario) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return i;
    }

    public FiadoChofer devolverUltimoFiadoChofer(Date fecha, Reparto r) throws Exception {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT fc FROM FiadoChofer fc where fecha = (SELECT max(ii.fecha) FROM FiadoChofer ii where ii.reparto = fc.reparto AND ii.fecha != :fechaBusqueda AND ii.fecha < :fechaBusqueda) AND fc.reparto = :r");
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        consulta.setParameter("r", r);
        FiadoChofer fc = (FiadoChofer) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return fc;
    }

    public void cargarComprasYUtilidadEnLiquidacion(Liquidacion l) throws Exception {
        List<Compra> compras = this.devolverComprasParaFechaYReparto(l.getFecha(), l.getReparto());
        double total = 0;
        double totalPrecioVenta = 0;
        double utilidadGiamo = 0;
        for (Compra c : compras) {
            if (!"Remito Devolución".equals(c.getTipoDocumento().getTipoDocumento())) {
                if (c.getTipoDocumento().isSuma()) {
                    total = total + c.getTotal();
                    totalPrecioVenta = totalPrecioVenta + c.getTotalAPrecioDeVentaConIva();
                    if(Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaGiamo)) {
                        for(CompraRenglon cr : c.getRenglones()) {
                            if(Util.esLeche(cr.getArticulo()) || Util.esEnvase(cr.getArticulo())) {
                                utilidadGiamo = utilidadGiamo + (cr.getTotalPrecioVentaConIva()- cr.getTotal());
                                //utilidadGiamo = Util.round(utilidadGiamo,2);
                            } else {
                                CoeficienteUtilidadCompras cuc = SistemaMantenimientoArticulos.getInstance().devolverCoeficienteUtilidadPorArticulo(cr.getArticulo());
                                if(cuc != null) {
                                    utilidadGiamo = utilidadGiamo + cr.getTotal()*cuc.getCoeficiente();
                                    //utilidadGiamo = Util.round(utilidadGiamo,2);
                                } else {
                                    System.out.println("Articulo sin coeficiente: " + cr.getArticulo());
                                    utilidadGiamo = utilidadGiamo + (cr.getTotalPrecioVentaConIva() - cr.getTotal());
                                    //utilidadGiamo = Util.round(utilidadGiamo, 2);
                                }
                            }
                        }
                    }
                } else if (!c.getTipoDocumento().isSuma()) {
                    total = total - c.getTotal();
                    totalPrecioVenta = totalPrecioVenta - c.getTotalAPrecioDeVentaConIva();
                    if(Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaGiamo)) {
                        for(CompraRenglon cr : c.getRenglones()) {
                            if(Util.esLeche(cr.getArticulo()) || Util.esEnvase(cr.getArticulo())) {
                                double cantARestar = cr.getTotalPrecioVentaConIva()- cr.getTotal();
                                utilidadGiamo = utilidadGiamo - cantARestar;
                                //utilidadGiamo = Util.round(utilidadGiamo,2);
                            } else {
                                CoeficienteUtilidadCompras cuc = SistemaMantenimientoArticulos.getInstance().devolverCoeficienteUtilidadPorArticulo(cr.getArticulo());
                                if(cuc != null) {
                                    double cantARestar = cr.getTotal()*cuc.getCoeficiente();
                                    utilidadGiamo = utilidadGiamo - cantARestar;
                                    //utilidadGiamo = Util.round(utilidadGiamo,2);
                                } else {
                                    System.out.println("Articulo sin coeficiente: " + cr.getArticulo());
                                    double cantARestar = cr.getTotalPrecioVentaConIva() - cr.getTotal();
                                    utilidadGiamo = utilidadGiamo - cantARestar;
                                    //utilidadGiamo = Util.round(utilidadGiamo, 2);
                                }
                            }
                        }
                    }
                }
            }
        }
        double utilidad = 0;
        if(!Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaGiamo)) {
            //No es Giamo, entonces la utilidad se calcula como la diferencia de precio.
            utilidad = totalPrecioVenta - total;
        } else {
            //Es giamo, se calcula en base a los coeficientes
            utilidad = Util.round(utilidadGiamo,2);
        }
        l.setCompras(total);
        l.setUtilidad(utilidad);
    }

    public double devolverRemitoPinchadasParaFechaYReparto(Date fecha, Reparto r) throws Exception {
        List<Compra> compras;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Compra where fechaLiquidacion = :fechaBusqueda AND reparto_id = " + r.getId());
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        compras = consulta.list();
        session.getTransaction().commit();
        session.close();
        double retorno = 0;
        if(Lecheros.nombreEmpresa.equals("Giamo")) {
            for (Compra c : compras) {
                if ("Remito Devolución".equals(c.getTipoDocumento().getTipoDocumento())) {
                    double cantTotal = 0;
                    for(CompraRenglon cr : c.getRenglones()) {
                        cantTotal = cantTotal + cr.getCantidad();
                    }
                    int porciento = (int) Util.round(((this.cantidadDeUltraEnElDia(fecha, r, compras) * 2) / 100),0);
                    if (cantTotal > porciento) {
                        int cantidadComun = (int) (cantTotal - porciento);
                        GrupoDeArticulos gaComun = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Común");
                        if (gaComun != null && !gaComun.getArticulos().isEmpty()) {
                            Precio precioComun = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(gaComun.getArticulos().get(0), c.getFecha());
                            if (precioComun != null) {
                                retorno = retorno + cantidadComun * precioComun.getPrecioVenta();
                            }
                        }
                        //Precio precioComun = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(cr.getArticulo(), c.getFecha());
                        GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra");
                        if (ga != null && !ga.getArticulos().isEmpty()) {
                            Precio precioUltra = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(ga.getArticulos().get(0), c.getFecha());
                            if (precioUltra != null) {
                                retorno = retorno + porciento * precioUltra.getPrecioVenta();
                            }
                        }
                    } else {
                        GrupoDeArticulos gaComun = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Común");
                        if (gaComun != null && !gaComun.getArticulos().isEmpty()) {
                            Precio precioComun = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(gaComun.getArticulos().get(0), c.getFecha());
                            if (precioComun != null) {
                                retorno = retorno + cantTotal * precioComun.getPrecioVenta();
                            }
                        }
                    }
                    
                }
            }
        } else {
            for (Compra c : compras) {
                if ("Remito Devolución".equals(c.getTipoDocumento().getTipoDocumento())) {
                    retorno = retorno + c.getTotal();
                }
            }
        }
        return retorno;
    }
    
    public double cantidadDeUltraEnElDia(Date fecha, Reparto r, List<Compra> compras) throws Exception {
        double cantUltra = 0;
        GrupoDeArticulos lecheUltra = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra");         
        for(Compra c : compras) {
            if (!"Remito Devolución".equals(c.getTipoDocumento().getTipoDocumento())) {
                for (CompraRenglon cr : c.getRenglones()) {
                    if(Util.esLeche(cr.getArticulo())) {
                        if(lecheUltra != null) {
                            if(lecheUltra.getArticulos().contains(cr.getArticulo())) {
                                cantUltra = cantUltra + cr.getCantidad();
                            }
                        }
                    }
                }
            }
            
        }
        return cantUltra;
    }

    public List<Compra> devolverRemitosPinchadasEntreFechasYReparto(Date fechaDesde, Date fechaHasta, Reparto r) throws Exception {
        List<Compra> retorno = new ArrayList<>();
        if (r == null) {
            List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
            for (Reparto rep : repartos) {
                Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
                session.beginTransaction();
                DocumentoDeCompra tipoDoc = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Remito Devolución");
                Query consulta = session.createQuery("FROM Compra where (fechaLiquidacion BETWEEN :fechaBusquedaDesde AND :fechaBusquedaHasta) and tipoDocumento = :td and reparto_id = " + rep.getId());
                consulta.setDate("fechaBusquedaDesde", fechaDesde);
                consulta.setDate("fechaBusquedaHasta", fechaHasta);
                consulta.setEntity("td", tipoDoc);
                List<Compra> aux = consulta.list();
                retorno.addAll(aux);
                session.getTransaction().commit();
                session.close();
            }
            return retorno;
        } else {
            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            DocumentoDeCompra tipoDoc = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNombre("Remito Devolución");
            Query consulta = session.createQuery("FROM Compra where (fechaLiquidacion BETWEEN :fechaBusquedaDesde AND :fechaBusquedaHasta) and tipoDocumento = :td and reparto_id = " + r.getId());
            consulta.setDate("fechaBusquedaDesde", fechaDesde);
            consulta.setDate("fechaBusquedaHasta", fechaHasta);
            consulta.setEntity("td", tipoDoc);
            retorno = consulta.list();
            session.getTransaction().commit();
            session.close();
            return retorno;
        }
    }

    public double devolverGastosParaFechaYReparto(Date fecha, Reparto r) throws Exception {
        double retorno = 0;
        List<Gasto> gastos;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Gasto where fecha = :fechaBusqueda AND reparto_id = " + r.getId());
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        gastos = consulta.list();
        session.getTransaction().commit();
        session.close();
        for (Gasto g : gastos) {
            retorno = retorno + g.getTotal();
        }
        return retorno;
    }

    public Chofer devolverChofer(Date fecha, Reparto r) throws Exception {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Liquidacion where fecha = :fechaBusqueda and reparto_id = " + r.getId());
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        Liquidacion l = (Liquidacion) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        if (l != null) {
            return l.getChofer();
        } else {
            return null;
        }
    }

    public Liquidacion devolverLiquidacion(Date fecha, Reparto r, Chofer c) throws Exception {

        Liquidacion retorno;
        ConfiguracionLiquidacion cl = SistemaMantenimiento.getInstance().devolverConfiguracionLiquidacion();
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Liquidacion where fecha = :fechaBusqueda and reparto_id = " + r.getId());
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        retorno = (Liquidacion) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        if (retorno == null) {
            //Es una liquidacion nueva
            retorno = new Liquidacion();
            retorno.setFecha(fecha);
            retorno.setReparto(r);
            retorno.setChofer(c);
            //retorno.setEntregaCheques(0);
            //retorno.setCorrecionDeDiferencia(0);
            //retorno.setFiadoChofer(0);
            //retorno.setFiadoEmpresa(0);
            //retorno.setRetenciones(0);
            //retorno.setInau(0);
            retorno.setSaldoAnterior(this.devolverSaldoAnteriorParaFechaYReparto(fecha, r));
            cargarComprasYUtilidadEnLiquidacion(retorno);
            retorno.setRemitoPinchadas(this.devolverRemitoPinchadasParaFechaYReparto(fecha, r));
            retorno.setGastos(this.devolverGastosParaFechaYReparto(fecha, r));
            //SistemaUsuarios.getInstance().registrarOperacion(Constantes.Actividad, "Facturo el prorrateo detallado. " );
            retorno.setCerrada(false);
            return retorno;
        } else {
            //Es una liquidacion que ya esta guardada.
            if(!retorno.getCerrada()) {
                retorno.setSaldoAnterior(this.devolverSaldoAnteriorParaFechaYReparto(fecha, r));
                retorno.setChofer(c);
                cargarComprasYUtilidadEnLiquidacion(retorno);
                retorno.setRemitoPinchadas(this.devolverRemitoPinchadasParaFechaYReparto(fecha, r));
                retorno.setGastos(this.devolverGastosParaFechaYReparto(fecha, r));
                calcularLiquidacion(retorno);
            }
            return retorno;
        }
    }

    public void calcularLiquidacion(Liquidacion l) throws Exception {
        Inventario i = this.devolverInventarioParaFechaYReparto(l.getFecha(), l.getReparto());
        if (i != null) {
            l.setInventario(i.getTotal());
        } else {
            l.setInventario(0);
        }
        FiadoChofer fc = this.devolverFiadoChoferParaFechaYReparto(l.getFecha(), l.getReparto());
        if (fc != null) {
            l.setFiadoChofer(fc.getTotal());
        } else {
            l.setFiadoChofer(0);
        }
        double valorInau = this.devolverValorInauParaFechaYReparto(l.getFecha(), l.getReparto());
        l.setInau(valorInau);
        double valorAnep = this.devolverValorAnepParaFechaYReparto(l.getFecha(), l.getReparto());
        l.setAnep(valorAnep);
        if (SistemaMantenimiento.getInstance().devolverConfiguracionLiquidacion().isMostrarFiadoClientesCobraEmpresa()) {
            double fiadoCobraEmpresa = this.devolverFiadoEmpresa(l.getFecha(), l.getReparto());
            l.setFiadoEmpresa(fiadoCobraEmpresa);
        }
        if (SistemaMantenimiento.getInstance().devolverConfiguracionLiquidacion().isMostrarEntregaCheques()) {
            if(!Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaGiamo)) {
                double cheques = this.devolverTotalChequesParaLiquidacion(l);
                l.setEntregaCheques(cheques);
            } 
        }
        if (SistemaMantenimiento.getInstance().devolverConfiguracionLiquidacion().isRetenciones()) {
            double retenciones = this.devolverTotalRetencionesParaLiquidacion(l);
            l.setRetenciones(retenciones);
        }
        l.setDeuda(l.getSaldoAnterior() + l.getCompras() + l.getUtilidad() - l.getRemitoPinchadas() - l.getGastos() - l.getEntregaEfectivo() - l.getEntregaCheques() - l.getCorrecionDeDiferencia() - l.getRetenciones() - l.getFiadoEmpresa() - l.getInau() - l.getAnep());
        l.setDiferencia(l.getInventario() + l.getFiadoChofer() - l.getDeuda());
    }

    public void guardarLiquidacion(Liquidacion l) throws Exception{
        GenericDAO.getGenericDAO().actualizar(l);
    }
    
    public boolean cerrarLiquidacion(Liquidacion l) throws Exception {
        l.setCerrada(true);
        return GenericDAO.getGenericDAO().actualizar(l);
    }
    
    public Liquidacion abrirLiquidacion(Liquidacion l) throws Exception {
        l.setCerrada(false);
        GenericDAO.getGenericDAO().actualizar(l);
        return this.devolverLiquidacion(l.getFecha(), l.getReparto(), l.getChofer());
    }

    public Date devolverFechaDelUltimoInvetario(Reparto r, Date fechaHasta) throws Exception {
        Date retorno = null;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fechaHasta);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT i FROM Inventario i where fecha = (SELECT max(ii.fecha) FROM Inventario ii where ii.reparto = i.reparto and ii.fecha < :fechaHastaParam) AND i.reparto = :r");
        consulta.setDate("fechaHastaParam", formatter.parse(f));
        consulta.setParameter("r", r);
        retorno = ((Inventario) consulta.uniqueResult()).getFecha();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    //Metodos para los fiados que cobra el chofer
    public FiadoChofer devolverFiadoChoferParaFechaYReparto(Date fecha, Reparto r) throws Exception {
        FiadoChofer retorno;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM FiadoChofer where fecha = :fechaBusqueda and reparto_id = " + r.getId());
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        retorno = (FiadoChofer) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean guardarFiadoChofer(FiadoChofer fc) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarFiadoChofer, "Ingreo el fiado chofer del reparto :  " + fc.getReparto() );
        for (FiadoChoferRenglon fcr : fc.getRenglones()) {
            fcr.getCliente().setDeuda(fcr.getTotal());
            SistemaMantenimiento.getInstance().actualizarCliente(fcr.getCliente());
        }
        return GenericDAO.getGenericDAO().guardar(fc);
    }

    public boolean actualizarFiadoChofer(FiadoChofer fc) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarFiadoChofer, "Actualizo el fiado chofer del reparto :  " + fc.getReparto() );
        for (FiadoChoferRenglon fcr : fc.getRenglones()) {
            fcr.getCliente().setDeuda(fcr.getTotal());
            SistemaMantenimiento.getInstance().actualizarCliente(fcr.getCliente());
        }
        return GenericDAO.getGenericDAO().actualizar(fc);
    }

    public void cargarRenglonesNuevoFiadoChofer(FiadoChofer fc) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fc.getFecha());
        cal.add(Calendar.DATE, -1);
        FiadoChofer fiadoChoferDiaAnterior = this.devolverFiadoChoferParaFechaYReparto(cal.getTime(), fc.getReparto());
        if (fiadoChoferDiaAnterior != null) {
            for (FiadoChoferRenglon fcr : fiadoChoferDiaAnterior.getRenglones()) {
                FiadoChoferRenglon fcrnuevo = new FiadoChoferRenglon();
                fcrnuevo.setCliente(fcr.getCliente());
                fcrnuevo.setFiadoChofer(fc);
                fcrnuevo.setTotal(fcr.getTotal());
                fc.getRenglones().add(fcrnuevo);
            }
            fc.setTotal(fiadoChoferDiaAnterior.getTotal());
        }
    }

    public List<Liquidacion> devolverLiquidacionesEntreFechasYChofer(Date fechaDesde, Date fechaHasta, Chofer chofer) throws Exception {
        List<Liquidacion> retorno = null;
        //SimpleDateFormat formatter;
        //formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaDesde);
        //cal.add(Calendar.DATE, -1);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Liquidacion WHERE fecha BETWEEN :stDate AND :edDate and chofer_id = " + chofer.getId());
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public double devolverFiadoEmpresa(Date fecha, Reparto r) throws Exception {
        double retorno = 0;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT sum(f.total) FROM Factura f where f.estaPaga = :estaPaga and f.fecha = :fechaBusqueda and f.reparto = :r and f.cliente in (SELECT cli FROM Cliente cli where cli.reparto = :r and cobraChofer = :cobraChofer)");
        consulta.setBoolean("estaPaga", false);
        consulta.setBoolean("cobraChofer", false);
        consulta.setEntity("r", r);
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        try {
            retorno = (double) consulta.uniqueResult();
        } catch (NullPointerException exp) {
            retorno = 0;
        }
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Factura> devolverFacturasFiadoEmpresaParaLiquidacion(Liquidacion l) throws Exception {
        List<Factura> retorno;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(l.getFecha());
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT f FROM Factura f where f.fecha = :fechaBusqueda and f.reparto = :r and f.cliente in (SELECT cli FROM Cliente cli where cli.reparto = :r and cli.cobraChofer = :cobraChofer)");
        consulta.setBoolean("cobraChofer", false);
        consulta.setEntity("r", l.getReparto());
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    //Metodos Inau
    public boolean guardarInau(Date fechaDesde, Date fechaHasta, Reparto r, int litrosLunes, int litrosMartes, int litrosMiercoles, int litrosJueves, int litrosViernes, int litrosSabado, int litrosDomingo, int litrosTotal) throws Exception {
        boolean retorno = false;
        Inau inau = new Inau();
        inau.setDesdeFecha(fechaDesde);
        inau.setHastaFecha(fechaHasta);
        inau.setReparto(r);
        inau.setLitrosLunes(litrosLunes);
        inau.setLitrosMartes(litrosMartes);
        inau.setLitrosMiercoles(litrosMiercoles);
        inau.setLitrosJueves(litrosJueves);
        inau.setLitrosViernes(litrosViernes);
        inau.setLitrosSabado(litrosSabado);
        inau.setLitrosDomingo(litrosDomingo);
        inau.setLitrosTotal(litrosTotal);
        retorno = GenericDAO.getGenericDAO().guardar(inau);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarInau, "Ingreo el inau del reparto :  " + r );
        return retorno;
    }

    public boolean actualizarInau(Inau i) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarInau, "Actualizo el inau del reparto :  " + i.getReparto() );
        return GenericDAO.getGenericDAO().actualizar(i);
    }

    public boolean eliminarInau(Inau i) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarInau, "Elimino el inau del reparto :  " + i.getReparto() );
        return GenericDAO.getGenericDAO().borrar(i);
    }

    public Inau devolverInauPorRepartoEntreFechas(Date fechaDesde, Date fechaHasta, Reparto r) throws Exception {
        Inau retorno;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fD = formatter.format(fechaDesde);
        String fH = formatter.format(fechaHasta);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Inau where desdeFecha = :fechaD and hastaFecha = :fechaH and reparto_id = " + r.getId());
        consulta.setDate("fechaD", formatter.parse(fD));
        consulta.setDate("fechaH", formatter.parse(fH));
        retorno = (Inau) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public double devolverValorInauParaFechaYReparto(Date fecha, Reparto r) throws Exception {
        double retorno = 0;
        Inau inau;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fD = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Inau where :fecha >= desdeFecha  and :fecha <= hastaFecha and reparto_id = " + r.getId());
        consulta.setDate("fecha", formatter.parse(fD));
        inau = (Inau) consulta.uniqueResult();
        Articulo a = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(110);
        Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, fecha);
        if (inau != null) {
            Calendar aux = Calendar.getInstance();
            aux.setTime(fecha);
            int diaDeLaSemana = aux.get(Calendar.DAY_OF_WEEK);
            switch (diaDeLaSemana) {
                case 1: {
                    retorno = inau.getLitrosDomingo();
                    break;
                }
                case 2: {
                    retorno = inau.getLitrosLunes();
                    break;
                }
                case 3: {
                    retorno = inau.getLitrosMartes();
                    break;
                }
                case 4: {
                    retorno = inau.getLitrosMiercoles();
                    break;
                }
                case 5: {
                    retorno = inau.getLitrosJueves();
                    break;
                }
                case 6: {
                    retorno = inau.getLitrosViernes();
                    break;
                }
                case 7: {
                    retorno = inau.getLitrosSabado();
                    break;
                }
            }
        }
        session.getTransaction().commit();
        session.close();
        retorno = retorno * p.getPrecioVenta();
        return retorno;
    }

    public Inau devolverInauParaFechaYReparto(Date fecha, Reparto r) throws Exception {
        Inau retorno;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fD = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Inau where :fecha >= desdeFecha  and :fecha <= hastaFecha and reparto_id = " + r.getId());
        consulta.setDate("fecha", formatter.parse(fD));
        retorno = (Inau) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public Anep devolverAnepParaFechaYReparto(Date fecha, Reparto r) throws Exception {
        Anep retorno;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fD = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Anep where :fecha >= desdeFecha  and :fecha <= hastaFecha and reparto_id = " + r.getId());
        consulta.setDate("fecha", formatter.parse(fD));
        retorno = (Anep) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public double devolverValorAnepParaFechaYReparto(Date fecha, Reparto r) throws Exception {
        double retorno = 0;
        Anep anep;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fD = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Anep where :fecha >= desdeFecha  and :fecha <= hastaFecha and reparto_id = " + r.getId());
        consulta.setDate("fecha", formatter.parse(fD));
        anep = (Anep) consulta.uniqueResult();
        Articulo a = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(110);
        Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, fecha);
        if (anep != null) {
            Calendar aux = Calendar.getInstance();
            aux.setTime(fecha);
            int diaDeLaSemana = aux.get(Calendar.DAY_OF_WEEK);
            switch (diaDeLaSemana) {
                case 1: {
                    retorno = anep.getLitrosDomingo();
                    break;
                }
                case 2: {
                    retorno = anep.getLitrosLunes();
                    break;
                }
                case 3: {
                    retorno = anep.getLitrosMartes();
                    break;
                }
                case 4: {
                    retorno = anep.getLitrosMiercoles();
                    break;
                }
                case 5: {
                    retorno = anep.getLitrosJueves();
                    break;
                }
                case 6: {
                    retorno = anep.getLitrosViernes();
                    break;
                }
                case 7: {
                    retorno = anep.getLitrosSabado();
                    break;
                }
            }
        }
        session.getTransaction().commit();
        session.close();
        retorno = retorno * p.getPrecioVenta();
        return retorno;
    }

    //Metodos Anep
    public boolean guardarAnep(Date fechaDesde, Date fechaHasta, Reparto r, int litrosLunes, int litrosMartes, int litrosMiercoles, int litrosJueves, int litrosViernes, int litrosSabado, int litrosDomingo, int litrosTotal) throws Exception {
        boolean retorno = false;
        Anep anep = new Anep();
        anep.setDesdeFecha(fechaDesde);
        anep.setHastaFecha(fechaHasta);
        anep.setReparto(r);
        anep.setLitrosLunes(litrosLunes);
        anep.setLitrosMartes(litrosMartes);
        anep.setLitrosMiercoles(litrosMiercoles);
        anep.setLitrosJueves(litrosJueves);
        anep.setLitrosViernes(litrosViernes);
        anep.setLitrosSabado(litrosSabado);
        anep.setLitrosDomingo(litrosDomingo);
        anep.setLitrosTotal(litrosTotal);
        retorno = GenericDAO.getGenericDAO().guardar(anep);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarAnep, "Ingreo el anep del reparto :  " + r );
        return retorno;
    }

    public boolean actualizarAnep(Anep a) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarAnep, "Actualizo el anep del reparto :  " + a.getReparto() );
        return GenericDAO.getGenericDAO().actualizar(a);
    }

    public boolean eliminarAnep(Anep a) throws Exception{
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarAnep, "Elimino el anep del reparto :  " + a.getReparto() );
        return GenericDAO.getGenericDAO().borrar(a);
    }

    public Anep devolverAnepPorRepartoEntreFechas(Date fechaDesde, Date fechaHasta, Reparto r) throws Exception {
        Anep retorno;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fD = formatter.format(fechaDesde);
        String fH = formatter.format(fechaHasta);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Anep where desdeFecha = :fechaD and hastaFecha = :fechaH and reparto_id = " + r.getId());
        consulta.setDate("fechaD", formatter.parse(fD));
        consulta.setDate("fechaH", formatter.parse(fH));
        retorno = (Anep) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean guardarCheque(Cliente cli, Date fecha, Date fechaVencimiento, double importe) throws Exception {

        Cheque cheque = new Cheque();
        cheque.setCliente(cli);
        cheque.setFecha(fecha);
        cheque.setFechaVencimiento(fechaVencimiento);
        cheque.setImporte(importe);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarCheque, "Ingreo un cheque del cliente :  " + cli );
        return GenericDAO.getGenericDAO().guardar(cheque);
    }

    public boolean actualizarCheque(Cheque cheque) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarCheque, "Actualizo un cheque del cliente :  " + cheque.getCliente() );
        return GenericDAO.getGenericDAO().actualizar(cheque);
    }

    public boolean eliminarCheque(Cheque c) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarCheque, "Elimino un cheque del cliente :  " + c.getCliente() );
        return GenericDAO.getGenericDAO().borrar(c);
    }

    public boolean guardarRetencion(Reparto r, String nombre, String aQueCorresponde, double importe) throws Exception {

        Retencion retencion = new Retencion();
        retencion.setReparto(r);
        retencion.setNombre(nombre);
        retencion.setaQueCorresponde(aQueCorresponde);
        retencion.setImporte(importe);
        retencion.setActiva(true);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarRetenciones, "Ingreo una retencion para  :  " + nombre );
        return GenericDAO.getGenericDAO().guardar(retencion);
    }

    public boolean actualizarRetencion(Retencion r) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarRetenciones, "Actualizon una retencion para  :  " + r.getNombre() );
        return GenericDAO.getGenericDAO().actualizar(r);
    }

    public boolean eliminarRetencion(Retencion r) throws Exception {
        r.setActiva(false);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarRetenciones, "Elimino una retencion para  :  " + r.getNombre() );
        return GenericDAO.getGenericDAO().actualizar(r);
    }

    public boolean quitarRetencionDeLiquidacion(Liquidacion l, Retencion r) throws Exception {
        boolean retorno;
        r.getFechasCanceladas().add(l.getFecha());
        retorno = GenericDAO.getGenericDAO().actualizar(r);
        return retorno;
    }

    public boolean activarRetencion(Retencion r) throws Exception {
        r.setActiva(true);
        return GenericDAO.getGenericDAO().actualizar(r);
    }

    public List<Cheque> devolverChequesParaLiquidacion(Liquidacion l) throws Exception {
        List<Cheque> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT c FROM Cheque c where c.fecha = :f and c.cliente in (SELECT cli FROM Cliente cli where cli.reparto = :r" + ")");
        consulta.setDate("f", l.getFecha());
        consulta.setEntity("r", l.getReparto());
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public double devolverTotalChequesParaLiquidacion(Liquidacion l) throws Exception {
        List<Cheque> cheques;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT c FROM Cheque c where c.fecha = :f and c.cliente in (SELECT cli FROM Cliente cli where cli.reparto = :r" + ")");
        consulta.setDate("f", l.getFecha());
        consulta.setEntity("r", l.getReparto());
        cheques = consulta.list();
        session.getTransaction().commit();
        session.close();
        double retorno = 0;
        for (Cheque c : cheques) {
            retorno = retorno + c.getImporte();
        }
        return retorno;
    }

    public List<Retencion> devolverRetencionesParaLiquidacion(Liquidacion l) throws Exception {
        List<Retencion> retorno = new ArrayList<>();
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT r FROM Retencion r where r.reparto = :r and activa = :act");
        consulta.setEntity("r", l.getReparto());
        consulta.setBoolean("act", true);
        List<Retencion> aux = consulta.list();
        session.getTransaction().commit();
        session.close();
        for (Retencion r : aux) {
            if (!r.getFechasCanceladas().contains(l.getFecha())) {
                retorno.add(r);
            }
        }
        return retorno;
    }

    public double devolverTotalRetencionesParaLiquidacion(Liquidacion l) throws Exception {
        List<Retencion> retenciones;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT r FROM Retencion r where r.reparto = :r and activa = :act");
        consulta.setEntity("r", l.getReparto());
        consulta.setBoolean("act", true);
        retenciones = consulta.list();
        session.getTransaction().commit();
        session.close();
        double retorno = 0;
        for (Retencion r : retenciones) {
            if (!r.getFechasCanceladas().contains(l.getFecha())) {
                retorno = retorno + r.getImporte();
            }
        }
        return retorno;
    }
}
