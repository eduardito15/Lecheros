/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;

import dao.GenericDAO;
import dominio.Articulo;
import dominio.FiadoChofer;
import dominio.FiadoChoferRenglon;
import dominio.GrupoDeArticulos;
import dominio.Inventario;
import dominio.Liquidacion;
import dominio.LiquidacionClafer;
import dominio.Precio;
import dominio.Reparto;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lecheros.Lecheros;
import org.hibernate.Query;
import org.hibernate.Session;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class SistemaLiquidacionesClafer extends SistemaLiquidaciones {
    
    private static SistemaLiquidacionesClafer instance;
    /**
     * @return the instance
     */
    public static SistemaLiquidacionesClafer getInstance() {
        if (instance == null) {
            instance = new SistemaLiquidacionesClafer();
        }
        return instance;
    }
    
    private SistemaLiquidacionesClafer() {
        super();
    }
    
    public void calcularLiquidacion(Liquidacion l, LiquidacionClafer lcla) throws Exception {
        super.calcularLiquidacion(l);
        GrupoDeArticulos grupoLecheComun = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Común");
        if(!grupoLecheComun.getArticulos().isEmpty()) {
            Articulo lecheComun = grupoLecheComun.getArticulos().get(0);
            Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheComun, lcla.getFecha());
            lcla.setTotalPinchadasPagadas(lcla.getCantPichadasPagadas() * p.getPrecioVenta());
        }
        l.setDeuda(l.getDeuda() - lcla.getTotalPinchadasPagadas() + lcla.getTotalPinchadasCobradas());
        l.setDiferencia(l.getDiferencia() - lcla.getTotalPinchadasPagadas() + lcla.getTotalPinchadasCobradas());
        actualizarLiquidacionClafer(lcla);
    }
    
    public LiquidacionClafer devolverLiquidacionClafer(Date fecha, Reparto r) throws ParseException, Exception {
        
        LiquidacionClafer retorno;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM LiquidacionClafer where fecha = :fechaBusqueda and reparto_id = " + r.getId());
        consulta.setDate("fechaBusqueda", formatter.parse(f));
        retorno = (LiquidacionClafer) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        if(retorno == null) {
            retorno = new LiquidacionClafer();
            retorno.setFecha(fecha);
            retorno.setReparto(r);
            retorno.setCantPichadasPagadas(0);
            retorno.setCantPinchadasCobradas(0);
        } 
        retorno.setCantPinchadasCobradas(this.devolverPinchadasLiquidacionAnterior(fecha, r));
        GrupoDeArticulos grupoLecheComun = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Común");
        if(grupoLecheComun != null && !grupoLecheComun.getArticulos().isEmpty()) {
            Articulo lecheComun = grupoLecheComun.getArticulos().get(0);
            Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheComun, fecha);
            retorno.setTotalPinchadasCobradas(retorno.getCantPinchadasCobradas() * p.getPrecioVenta());
        }
        actualizarLiquidacionClafer(retorno);
        return retorno;
    }
    
    public int devolverPinchadasLiquidacionAnterior(Date fecha, Reparto r) throws Exception {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        String f = formatter.format(fecha);
            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consulta = session.createQuery("SELECT l FROM LiquidacionClafer l where fecha = (SELECT max(ll.fecha) FROM LiquidacionClafer ll where ll.reparto = l.reparto AND ll.fecha != :fechaBusqueda AND ll.fecha < :fechaBusqueda) AND l.reparto = :r");
            consulta.setDate("fechaBusqueda", formatter.parse(f));
            consulta.setParameter("r", r);
            LiquidacionClafer l = (LiquidacionClafer) consulta.uniqueResult();
            session.getTransaction().commit();
            session.close();
            if (l != null) {
                return l.getCantPichadasPagadas();
            } else {
                return 0;
            }
    }

    public boolean actualizarLiquidacionClafer(LiquidacionClafer lcl) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarInventario, "Actualizo el inventario del reparto: " + lcl.getReparto() );
        return GenericDAO.getGenericDAO().actualizar(lcl);
    }
    
    public void recalcularFiafoChofer(FiadoChofer fc) throws Exception {
        if(fc != null) {
            fc.setTotal(0);
            for(FiadoChoferRenglon fcr : fc.getRenglones()) {
                double totalEnvases = 0;
                if(fcr.getEnvases() != 0) {
                    totalEnvases = devolverTotalEnPlataEnvases(fcr.getEnvases());    
                }
                fc.setTotal(fc.getTotal() + fcr.getTotal() + totalEnvases);
            }
            GenericDAO.getGenericDAO().actualizar(fc);
        }
    }
}
