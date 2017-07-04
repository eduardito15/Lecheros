/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;

import dao.GenericDAO;
import dominio.Anep;
import dominio.Articulo;
import dominio.Cliente;
import dominio.Compra;
import dominio.CompraRenglon;
import dominio.ConfiguracionFacturacion;
import dominio.ConfiguracionLiquidacion;
import dominio.DocumentoDeVenta;
import dominio.Factura;
import dominio.FacturaRenglon;
import dominio.FiadoChofer;
import dominio.FiadoChoferRenglon;
import dominio.GrupoDeArticulos;
import dominio.Inau;
import dominio.Inventario;
import dominio.InventarioGiamo;
import dominio.InventarioGiamoRenglon;
import dominio.InventarioRenglon;
import dominio.Precio;
import dominio.ProductoClienteProrrateo;
import dominio.Reparto;
import impresion.Facturas.ImprimirFactura;
import impresion.Facturas.ImprimirFacturaGiamo;
import impresion.Facturas.ImprimirFacturaRelece;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lecheros.Lecheros;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import ui.usuarios.Constantes;
import util.Ref;
import util.Util;

/**
 *
 * @author Edu
 */
public class SistemaFacturas {

    private static SistemaFacturas instance;

    private static ImprimirFactura impresion;

    SimpleDateFormat formatter;

    DecimalFormat df;
    DecimalFormat df8decimales;
    
    private Logger logger = Logger.getLogger(SistemaFacturas.class.getName());

    /**
     * @return the instance
     */
    public static SistemaFacturas getInstance() {
        if (instance == null) {
            instance = new SistemaFacturas();
            asignarImpresion();
        }
        return instance;
    }

    public SistemaFacturas() {
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        df = new DecimalFormat("0.00");
        df8decimales = new DecimalFormat("0.00000000");
    }

    /**
     * @return the impresion
     */
    public static ImprimirFactura getImpresion() {
        return impresion;
    }

    private static void asignarImpresion() {
        if ("Relece".equals(Lecheros.nombreEmpresa)) {
            impresion = new ImprimirFacturaRelece();
        }
        if (Constantes.nombreEmpresaGiamo.equals(Lecheros.nombreEmpresa)) {
            impresion = new ImprimirFacturaGiamo();
        }
    }

    /**
     * Este metodo se fija el numero de boleta entre todos los numeros aunque el
     * encabezado dice factura manual. 
     *
     */
    public boolean numeroDeFacturaManualValido(long numero, DocumentoDeVenta tipoDoc) throws Exception {
        boolean retorno = true;
        Factura fact;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura WHERE numero = " + numero + " and tipoDocumento = :tipo");
        consulta.setEntity("tipo", tipoDoc);
        fact = (Factura) consulta.uniqueResult();
        if (fact != null) {
            retorno = false;
        }
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    /**
     * Este metodo se fija el numero de boleta entre todos los numeros de
     * boeltas marcadas como manuales. Aunque el encabezado dice movil. Es por
     * que las boletas manuales son las que se hacen con el aparato de PS
     *
     * @param numero
     * @param tipoDocVenta
     * @return 
     * @throws java.lang.Exception
     */
    public boolean numeroDeFacturaMovilValido(long numero, DocumentoDeVenta tipoDocVenta) throws Exception {
        System.out.println("Numero:" + numero);
        boolean retorno = true;
        Factura fact;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura WHERE numero = " + numero + " and esManual = :man and tipoDocumento = :tipo");
        consulta.setBoolean("man", true);
        consulta.setEntity("tipo", tipoDocVenta);
        fact = (Factura) consulta.uniqueResult();
        if (fact != null) {
            retorno = false;
        }
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean guardarFacturaManual(Factura fm) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarFacturas, "Ingreso la factura numero: " + fm.getNumero());
        if (!fm.getCliente().isCobraChofer()) {
            fm.getCliente().setDeuda(fm.getCliente().getDeuda() + fm.getTotal());
            SistemaMantenimiento.getInstance().actualizarCliente(fm.getCliente());
            fm.setEstaPaga(false);
        }
        boolean retorno = GenericDAO.getGenericDAO().guardar(fm);
        ConfiguracionLiquidacion configLiq = SistemaMantenimiento.getInstance().devolverConfiguracionLiquidacion();
        if (configLiq.isMostrarFiadoClientesCobraChofer()) {
            if (fm.getCliente().isCobraChofer()) {
                FiadoChofer fiadoChofer = SistemaLiquidaciones.getInstance().devolverFiadoChoferParaFechaYReparto(fm.getFecha(), fm.getReparto());
                if (fiadoChofer == null) {
                    //Es un fiado chofer nuevo
                    fiadoChofer = new FiadoChofer();
                    fiadoChofer.setFecha(fm.getFecha());
                    fiadoChofer.setReparto(fm.getReparto());
                    SistemaLiquidaciones.getInstance().cargarRenglonesNuevoFiadoChofer(fiadoChofer);
                    boolean estaEnRenglon = false;
                    for (FiadoChoferRenglon fc : fiadoChofer.getRenglones()) {
                        if (fc.getCliente().equals(fm.getCliente())) {
                            estaEnRenglon = true;
                            double totalViejoCliente = fc.getTotal();
                            fc.setTotal(fm.getTotal());
                            double totalViejo = fiadoChofer.getTotal();
                            fiadoChofer.setTotal(totalViejo - totalViejoCliente + fm.getTotal());
                            break;
                        }
                    }
                    if (!estaEnRenglon) {
                        FiadoChoferRenglon fcr = new FiadoChoferRenglon();
                        fcr.setCliente(fm.getCliente());
                        fcr.setEnvases(0);
                        fcr.setTotal(fm.getTotal());
                        fcr.setFiadoChofer(fiadoChofer);
                        fiadoChofer.getRenglones().add(fcr);
                        fiadoChofer.setTotal(fiadoChofer.getTotal() + fm.getTotal());
                    }
                    SistemaLiquidaciones.getInstance().guardarFiadoChofer(fiadoChofer);
                } else {
                    //Es un fiado chofer que ya existe
                    boolean estaEnRenglon = false;
                    for (FiadoChoferRenglon fc : fiadoChofer.getRenglones()) {
                        if (fc.getCliente().equals(fm.getCliente())) {
                            estaEnRenglon = true;
                            double totalViejoCliente = fc.getTotal();
                            fc.setTotal(fm.getTotal());
                            double totalViejo = fiadoChofer.getTotal();
                            fiadoChofer.setTotal(totalViejo - totalViejoCliente + fm.getTotal());
                            break;
                        }
                    }
                    if (!estaEnRenglon) {
                        FiadoChoferRenglon fcr = new FiadoChoferRenglon();
                        fcr.setCliente(fm.getCliente());
                        fcr.setEnvases(0);
                        fcr.setTotal(fm.getTotal());
                        fcr.setFiadoChofer(fiadoChofer);
                        fiadoChofer.getRenglones().add(fcr);
                        fiadoChofer.setTotal(fiadoChofer.getTotal() + fm.getTotal());
                    }
                    SistemaLiquidaciones.getInstance().actualizarFiadoChofer(fiadoChofer);
                }
            }
        }
        return retorno;
    }

    public boolean eliminarFactura(Factura fm) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarFacturas, "Elimino la factura numero: " + fm.getNumero());
        if (!fm.getCliente().isCobraChofer()) {
            fm.getCliente().setDeuda(fm.getCliente().getDeuda() - fm.getTotal());
            SistemaMantenimiento.getInstance().actualizarCliente(fm.getCliente());
        }
        return GenericDAO.getGenericDAO().borrar(fm);
    }

    public boolean actualizarFacturaManual(Factura fm) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarFacturas, "Actualizo la factura numero: " + fm.getNumero());
        if (!fm.getCliente().isCobraChofer()) {
            if (!fm.isEstaPaga()) {
                fm.getCliente().setDeuda(fm.getCliente().getDeuda() + fm.getTotal());
                SistemaMantenimiento.getInstance().actualizarCliente(fm.getCliente());
            }
        }
        return GenericDAO.getGenericDAO().actualizar(fm);
    }

    public void borrarDeudaDeFacturaManualCliente(Factura fm) throws Exception {
        if (!fm.getCliente().isCobraChofer()) {
            if (!fm.isEstaPaga()) {
                fm.getCliente().setDeuda(fm.getCliente().getDeuda() - fm.getTotal());
                SistemaMantenimiento.getInstance().actualizarCliente(fm.getCliente());
            }
        }
    }

    public List<Factura> devolverFacturasPorNumero(long numero, boolean esManual) throws Exception {
        List<Factura> retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura WHERE numero = " + numero + " and esManual = :man");
        consulta.setBoolean("man", esManual);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public Factura devolverFacturaPorNumeroYCliente(long numero, Cliente c, boolean esManual) throws Exception {
        Factura retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura WHERE numero = " + numero + " and cliente_id = " + c.getId() + " and esManual = :man");
        consulta.setBoolean("man", esManual);
        retorno = (Factura) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Factura> devolverFacturaPorNumeroYReparto(long numero, Reparto r, boolean esManual) throws Exception {
        List<Factura> retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura WHERE numero = " + numero + " and reparto_id = " + r.getId() + " and esManual = :man");
        consulta.setBoolean("man", esManual);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public Factura devolverFacturaPorNumeroYTipo(long numero, boolean esManual) throws Exception {
        Factura retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura WHERE numero = " + numero + " and esManual = :man");
        consulta.setBoolean("man", esManual);
        retorno = (Factura)consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public Factura devolverFacturaPorNumeroTipoDeDocumentoDeVentaYTipo(long numero, DocumentoDeVenta tipoDoc, boolean esManual) throws Exception {
        Factura retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura WHERE numero = " + numero + " and esManual = :man and tipoDocumento = :tipo");
        consulta.setBoolean("man", esManual);
        consulta.setEntity("tipo", tipoDoc);
        retorno = (Factura)consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Factura> devolverFacturasEntreFechas(Date fechaDesde, Date fechaHasta, boolean esManual) throws Exception {

        List<Factura> retorno = null;
        //SimpleDateFormat formatter;
        //formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaDesde);
        //cal.add(Calendar.DATE, -1);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura WHERE (fecha BETWEEN :stDate AND :edDate" + ") and esManual = :man");
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        consulta.setBoolean("man", esManual);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Factura> devolverFacturasEntreFechasYCliente(Date fechaDesde, Date fechaHasta, Cliente c, boolean esManual) throws Exception {
        List<Factura> retorno = null;
        //SimpleDateFormat formatter;
        //formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaDesde);
        //cal.add(Calendar.DATE, -1);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura WHERE (fecha BETWEEN :stDate AND :edDate" + ") and cliente_id = " + c.getId() + " and esManual = :man");
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        consulta.setBoolean("man", esManual);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Factura> devolverFacturasEntreFechasYReparto(Date fechaDesde, Date fechaHasta, Reparto r, boolean esManual) throws Exception {
        List<Factura> retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura WHERE (fecha BETWEEN :stDate AND :edDate" + ") and reparto_id = " + r.getId() + " and esManual = :man");
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        consulta.setBoolean("man", esManual);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Factura> devolverFacturasEntreFechasYClienteYRaparto(Date fechaDesde, Date fechaHasta, Cliente c, Reparto r, boolean esManual) throws Exception {
        List<Factura> retorno = null;
        //SimpleDateFormat formatter;
        //formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaDesde);
        //cal.add(Calendar.DATE, -1);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura WHERE (fecha BETWEEN :stDate AND :edDate" + ") and cliente_id = " + c.getId() + " and reparto_id = " + r.getId() + " and esManual = :man");
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        consulta.setBoolean("man", esManual);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<String[]> ingresarFacturasDesdeArchivo(String ruta) throws Exception {
        List<String[]> retorno = new ArrayList<>();
        BufferedReader br = null;
        String line;
        String cvsSplitBy = "\\t";

        int totalBoletasIngresadas = 0;

        try {

            br = new BufferedReader(new FileReader(ruta));

            String ultimoNumeroLeido = "";
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] retornoPorFactura = new String[6];

                String[] data = line.split(cvsSplitBy);

                String FechaS = data[0];
                String Vendedor = data[1];
                //String Vendedor_desc  = data[2];
                String Documento = data[3];
                String Numero = data[4];
                String Cuenta = data[5];
                String Razon_social = data[6];
                //String Nombre = data[7];
                String Articulo = data[8];
                //String Articulo_descrip  = data[9];
                String Cantidad = data[10];
                String Subtotal = data[11];
                String Descuentos = data[12];
                //String Impuesto_desc  = data[13];
                String Impuesto = data[14];
                String Total = data[15];

                if (ultimoNumeroLeido.equals(Vendedor + Numero)) {
                    //Es una que ya existe
                    retornoPorFactura = retorno.get(retorno.size()-1);
                    if(retornoPorFactura[5].equals("Ingresada correctamente.")) {
                        //Es valida
                        long numeroFactura = 0;
                        try {
                            String num = Vendedor + Numero;
                            numeroFactura = Long.parseLong(num);
                        } catch (NumberFormatException ne) {
                            retornoPorFactura[5] = "El número de factura debe ser un número.";
                            throw new Exception(retornoPorFactura[5]);
                        }
                        
                        DocumentoDeVenta tipoDocumento = null;

                        switch (Documento) {
                            case "CONTADO.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado");
                                if (Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "CONTADO": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado");
                                if (Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "CREDITO.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Credito");
                                if (Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Credito Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "CREDITO": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Credito");
                                if (Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Credito Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE DEVOLUCION": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Devolución");
                                if (Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Devolución Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE DEVOLUCION.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Devolución");
                                if (Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Devolución Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE CREDITO": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Credito");
                                if (Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Credito Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE CREDITO.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Credito");
                                if (Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Credito Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                        }
                        
                        Factura f = this.devolverFacturaPorNumeroTipoDeDocumentoDeVentaYTipo(numeroFactura, tipoDocumento, true);
                        if(f == null) {
                            //No es valida la boleta continuo
                        } else {
                            //Es valida, ya existe y le agregamos el renglon
                            try {
                                Articulo art = null;
                                try {
                                    int codArt = Integer.parseInt(Articulo);
                                    art = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(codArt);
                                    if (art == null) {
                                        retornoPorFactura[5] = "No existe un artículo con el codigo " + Articulo + ".";
                                        throw new Exception(retornoPorFactura[5]);
                                    }
                                } catch (NumberFormatException ne) {
                                    retornoPorFactura[5] = "El codigo de artículo debe ser un número.";
                                    throw new Exception(retornoPorFactura[5]);
                                }

                                Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(art, f.getFecha());
                                if (p == null) {
                                    retornoPorFactura[5] = "No existe un precio para el artículo con el codigo " + Articulo + ".";
                                    throw new Exception(retornoPorFactura[5]);
                                }

                                double cantidad = 0;
                                try {
                                    cantidad = Double.parseDouble(Cantidad);
                                } catch (NumberFormatException ne) {
                                    retornoPorFactura[5] = "La cantidad debe ser un número.";
                                    throw new Exception(retornoPorFactura[5]);
                                }

                                double subtotal = 0;
                                try {
                                    subtotal = Double.parseDouble(Subtotal);
                                } catch (NumberFormatException ne) {
                                    retornoPorFactura[5] = "El subtotal debe ser un número.";
                                    throw new Exception(retornoPorFactura[5]);
                                }

                                double descuento = 0;
                                try {
                                    descuento = Double.parseDouble(Descuentos);
                                } catch (NumberFormatException ne) {
                                    retornoPorFactura[5] = "El descuento debe ser un número.";
                                    throw new Exception(retornoPorFactura[5]);
                                }

                                double precioDeLaBoleta = subtotal / cantidad;

                                //Que hacemos con el precio?? Que verificamos.
                                if (descuento == 0) {
                                    //Me fijo si el precio es igual al guardado.
                                    double diferencia = precioDeLaBoleta - p.getPrecioVenta();
                                    if (diferencia > 0.05 || diferencia < -0.05) {
                                        retornoPorFactura[5] = "El precio en el sistema para el artículo: " + art.getCodigo() + " difiere del de la boleta. Revisarlo y volver a intentar.";
                                        throw new Exception(retornoPorFactura[5]);
                                    }
                                }

                                double impuesto = 0;
                                try {
                                    impuesto = Double.parseDouble(Impuesto);
                                } catch (NumberFormatException ne) {
                                    retornoPorFactura[5] = "El impuesto debe ser un número.";
                                    throw new Exception(retornoPorFactura[5]);
                                }

                                double total = 0;
                                try {
                                    total = Double.parseDouble(Total);
                                } catch (NumberFormatException ne) {
                                    retornoPorFactura[5] = "El total debe ser un número.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                
                                FacturaRenglon fr = new FacturaRenglon();
                                fr.setArticulo(art);
                                fr.setCantidad(cantidad);
                                fr.setDescuento(descuento);
                                fr.setFactura(f);
                                fr.setIva(impuesto);
                                fr.setPrecio(precioDeLaBoleta);
                                fr.setSubtotal(subtotal);
                                fr.setTotal(total);
                                
                                f.getRenglones().add(fr);
                                
                                f.setTotal(f.getTotal() + fr.getTotal());
                                f.setSubtotal(f.getSubtotal() + fr.getSubtotal());
                                f.setDescuento(f.getDescuento() + fr.getDescuento());
                                if(art.getIva().getId() == Constantes.ivaBasico) {
                                    f.setTotalBasico(f.getTotalBasico() + fr.getIva());
                                }
                                if(art.getIva().getId() == Constantes.ivaMinimo) {
                                    f.setTotalMinimo(f.getTotalMinimo() + fr.getIva());
                                }
                                
                                GenericDAO.getGenericDAO().actualizar(f);
                            
                            }catch (Exception e) {
                                //Hay algun error en el renglon, hay que borrar lo que ya ingresamos, guardar el mensaje y restal al total de boletas ingresadas
                                GenericDAO.getGenericDAO().borrar(f);
                                if(retornoPorFactura[5] == null) {
                                    retornoPorFactura[5] = "Ocurrio un error.";
                                }
                                retorno.add(retornoPorFactura);
                                totalBoletasIngresadas--;
                            }
                        }
                    } else {
                        //Es una boleta invalida ya fallo en el paso anterior, no hay que hacer nada por ahora
                    }
                    
                } else {
                    try {
                        ultimoNumeroLeido = Vendedor + Numero;
                        //Es una nueva

                        retornoPorFactura[0] = FechaS;
                        retornoPorFactura[1] = Numero;
                        retornoPorFactura[2] = Razon_social;
                        retornoPorFactura[3] = Vendedor;
                        retornoPorFactura[4] = Total;

                        SimpleDateFormat formatterPS = new SimpleDateFormat("dd/MM/yyyy");
                        Date fecha = formatterPS.parse(FechaS);

                        Reparto r = null;

                        try {
                            int numeroVendedorPS = Integer.parseInt(Vendedor);
                            r = SistemaMantenimiento.getInstance().devolverRepartoPorNumeroVendedorPS(numeroVendedorPS);
                            if (r == null) {
                                retornoPorFactura[5] = "No existe el vendedor con ese número.";
                                throw new Exception(retornoPorFactura[5]);
                            }
                        } catch (NumberFormatException ne) {
                            retornoPorFactura[5] = "El número de vendedor debe ser un número.";
                            throw new Exception(retornoPorFactura[5]);
                        }
                        
                        retornoPorFactura[3] = r.toString();

                        DocumentoDeVenta tipoDocumento = null;

                        switch (Documento) {
                            case "CONTADO.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado");
                                if(Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "CONTADO": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado");
                                if(Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "CREDITO.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Credito");
                                if(Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Credito Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "CREDITO": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Credito");
                                if(Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Credito Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE DEVOLUCION": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Devolución");
                                if(Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Devolución Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE DEVOLUCION.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Devolución");
                                if(Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Devolución Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE CREDITO": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Credito");
                                if(Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Credito Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE CREDITO.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Credito");
                                if(Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaRelece)) {
                                    tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Credito Cerram");
                                }
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                        }

                        long numeroFactura = 0;
                        try {
                            String num = Vendedor + Numero;
                            numeroFactura = Long.parseLong(num);
                        } catch (NumberFormatException ne) {
                            retornoPorFactura[5] = "El número de factura debe ser un número.";
                            throw new Exception(retornoPorFactura[5]);
                        }

                        //Verifico si existe una boleta manual con ese numero. 
                        
                        if (this.numeroDeFacturaMovilValido(numeroFactura, tipoDocumento)) {
                            Cliente c;

                            try {
                                Long codigoClientePS = Long.parseLong(Cuenta);
                                c = SistemaMantenimiento.getInstance().devolverClienteActivoPorCodigoPS(codigoClientePS);
                                if (c == null) {
                                    retornoPorFactura[5] = "No existe el cliente con ese codigo.";
                                    throw new Exception(retornoPorFactura[5]);
                                } else {
                                    //Existe el cliente, procedo a crear la factura y seguir leyendo la informacion para crear el renglon
                                    Articulo art = null;
                                    try {
                                        int codArt = Integer.parseInt(Articulo);
                                        art = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(codArt);
                                        if (art == null) {
                                            retornoPorFactura[5] = "No existe un artículo con el codigo " + Articulo + ".";
                                            throw new Exception(retornoPorFactura[5]);
                                        }
                                    } catch (NumberFormatException ne) {
                                        retornoPorFactura[5] = "El codigo de artículo debe ser un número.";
                                        throw new Exception(retornoPorFactura[5]);
                                    }

                                    Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(art, fecha);
                                    if (p == null) {
                                        retornoPorFactura[5] = "No existe un precio para el artículo con el codigo " + Articulo + ".";
                                        throw new Exception(retornoPorFactura[5]);
                                    }

                                    double cantidad = 0;
                                    try {
                                        cantidad = Double.parseDouble(Cantidad);
                                    } catch (NumberFormatException ne) {
                                        retornoPorFactura[5] = "La cantidad debe ser un número.";
                                        throw new Exception(retornoPorFactura[5]);
                                    }

                                    double subtotal = 0;
                                    try {
                                        subtotal = Double.parseDouble(Subtotal);
                                    } catch (NumberFormatException ne) {
                                        retornoPorFactura[5] = "El subtotal debe ser un número.";
                                        throw new Exception(retornoPorFactura[5]);
                                    }

                                    double descuento = 0;
                                    try {
                                        descuento = Double.parseDouble(Descuentos);
                                    } catch (NumberFormatException ne) {
                                        retornoPorFactura[5] = "El descuento debe ser un número.";
                                        throw new Exception(retornoPorFactura[5]);
                                    }

                                    double precioDeLaBoleta = subtotal / cantidad;

                                    //Que hacemos con el precio?? Que verificamos.
                                    if (descuento == 0) {
                                        //Me fijo si el precio es igual al guardado.
                                        double diferencia = precioDeLaBoleta - p.getPrecioVenta();
                                        if (diferencia > 0.05 || diferencia < -0.05) {
                                            retornoPorFactura[5] = "El precio en el sistema para el artículo: " + art.getCodigo() + " difiere del de la boleta. Revisarlo y volver a intentar.";
                                            throw new Exception(retornoPorFactura[5]);
                                        }
                                    }

                                    double impuesto = 0;
                                    try {
                                        impuesto = Double.parseDouble(Impuesto);
                                    } catch (NumberFormatException ne) {
                                        retornoPorFactura[5] = "El impuesto debe ser un número.";
                                        throw new Exception(retornoPorFactura[5]);
                                    }

                                    double total = 0;
                                    try {
                                        total = Double.parseDouble(Total);
                                    } catch (NumberFormatException ne) {
                                        retornoPorFactura[5] = "El total debe ser un número.";
                                        throw new Exception(retornoPorFactura[5]);
                                    }

                                    Factura f = new Factura();
                                    f.setFecha(fecha);
                                    f.setCliente(c);
                                    f.setDescuento(descuento);
                                    f.setTotal(total);
                                    f.setEsManual(true);
                                    if (c.isCobraChofer()) {
                                        f.setEstaPaga(true);
                                    } else {
                                        f.setEstaPaga(false);
                                    }
                                    f.setNumero(numeroFactura);
                                    f.setReparto(r);
                                    f.setSubtotal(subtotal);
                                    f.setTipoDocumento(tipoDocumento);
                                    if (art.getIva().getId() == Constantes.ivaMinimo) {
                                        f.setTotalMinimo(impuesto);
                                    }
                                    if (art.getIva().getId() == Constantes.ivaBasico) {
                                        f.setTotalBasico(impuesto);
                                    }

                                    List<FacturaRenglon> renglones = new ArrayList<>();
                                    FacturaRenglon fr = new FacturaRenglon();
                                    fr.setArticulo(art);
                                    fr.setCantidad(cantidad);
                                    fr.setDescuento(descuento);
                                    fr.setFactura(f);
                                    fr.setIva(impuesto);
                                    fr.setPrecio(precioDeLaBoleta);
                                    fr.setSubtotal(subtotal);
                                    fr.setTotal(total);
                                    renglones.add(fr);

                                    f.setRenglones(renglones);

                                    GenericDAO.getGenericDAO().guardar(f);
                                    
                                    retornoPorFactura[5] = "Ingresada correctamente.";

                                }
                            } catch (NumberFormatException ne) {
                                retornoPorFactura[5] = "El número de cliente debe ser un número.";
                                throw new Exception(retornoPorFactura[5]);
                            }

                        } else {
                            retornoPorFactura[5] = "Ya existe una factura con el número " + Numero + ".";
                            throw new Exception(retornoPorFactura[5]);
                        }
                        

                    } catch (Exception e) {
                        //Hubo alguna excepcion. Algun error en los datos.
                        if(retornoPorFactura[5] == null) {
                            retornoPorFactura[5] = e.getMessage();
                        }
                        
                    }
                    
                    /*if (retornoPorFactura[5] != null) {
                        retorno.add(retornoPorFactura);
                    } else {
                        totalBoletasIngresadas++;
                    }*/
                    
                    retorno.add(retornoPorFactura);
                    if(retornoPorFactura[5].equals("Ingresada correctamente.")) {
                        totalBoletasIngresadas++;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
        } catch (IOException e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(e);
                    SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                }
            }
        }
        
        String[] retCantIngresadas = new String[1];
        retCantIngresadas[0] = "Ingresadas correctamente : " + totalBoletasIngresadas;
        retorno.add(retCantIngresadas);
        return retorno;
    }
    
    public List<String[]> ingresarFacturasReleceDesdeArchivo(String ruta) throws Exception {
        List<String[]> retorno = new ArrayList<>();
        BufferedReader br = null;
        String line;
        String cvsSplitBy = "\\t";

        int totalBoletasIngresadas = 0;

        try {

            br = new BufferedReader(new FileReader(ruta));

            String ultimoNumeroLeido = "";
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] retornoPorFactura = new String[6];

                String[] data = line.split(cvsSplitBy);

                String FechaS = data[0];
                String Vendedor = data[1];
                //String Vendedor_desc  = data[2];
                String Documento = data[3];
                String Numero = data[4];
                String Cuenta = data[5];
                String Razon_social = data[6];
                //String Nombre = data[7];
                String Articulo = data[8];
                //String Articulo_descrip  = data[9];
                String Cantidad = data[10];
                String Subtotal = data[11];
                String Descuentos = data[12];
                //String Impuesto_desc  = data[13];
                String Impuesto = data[14];
                String Total = data[15];

                if (ultimoNumeroLeido.equals(Numero)) {
                    //Es una que ya existe
                    retornoPorFactura = retorno.get(retorno.size()-1);
                    if(retornoPorFactura[5].equals("Ingresada correctamente.")) {
                        //Es valida
                        long numeroFactura = 0;
                        try {
                            numeroFactura = Long.parseLong(Numero);
                        } catch (NumberFormatException ne) {
                            retornoPorFactura[5] = "El número de factura debe ser un número.";
                            throw new Exception(retornoPorFactura[5]);
                        }
                        
                        DocumentoDeVenta tipoDocumento = null;

                        switch (Documento) {
                            case "CONTADO.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "CONTADO": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "CREDITO.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Credito Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "CREDITO": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Credito Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE DEVOLUCION": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Devolución Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE DEVOLUCION.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Devolución Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE CREDITO": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Credito Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE CREDITO.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Credito Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                        }
                        
                        Factura f = this.devolverFacturaPorNumeroTipoDeDocumentoDeVentaYTipo(numeroFactura, tipoDocumento, true);
                        if(f == null) {
                            //No es valida la boleta continuo
                        } else {
                            //Es valida, ya existe y le agregamos el renglon
                            try {
                                Articulo art = null;
                                try {
                                    int codArt = Integer.parseInt(Articulo);
                                    art = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(codArt);
                                    if (art == null) {
                                        retornoPorFactura[5] = "No existe un artículo con el codigo " + Articulo + ".";
                                        throw new Exception(retornoPorFactura[5]);
                                    }
                                } catch (NumberFormatException ne) {
                                    retornoPorFactura[5] = "El codigo de artículo debe ser un número.";
                                    throw new Exception(retornoPorFactura[5]);
                                }

                                Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(art, f.getFecha());
                                if (p == null) {
                                    retornoPorFactura[5] = "No existe un precio para el artículo con el codigo " + Articulo + ".";
                                    throw new Exception(retornoPorFactura[5]);
                                }

                                double cantidad = 0;
                                try {
                                    cantidad = Double.parseDouble(Cantidad);
                                } catch (NumberFormatException ne) {
                                    retornoPorFactura[5] = "La cantidad debe ser un número.";
                                    throw new Exception(retornoPorFactura[5]);
                                }

                                double subtotal = 0;
                                try {
                                    subtotal = Double.parseDouble(Subtotal);
                                } catch (NumberFormatException ne) {
                                    retornoPorFactura[5] = "El subtotal debe ser un número.";
                                    throw new Exception(retornoPorFactura[5]);
                                }

                                double descuento = 0;
                                try {
                                    descuento = Double.parseDouble(Descuentos);
                                } catch (NumberFormatException ne) {
                                    retornoPorFactura[5] = "El descuento debe ser un número.";
                                    throw new Exception(retornoPorFactura[5]);
                                }

                                double precioDeLaBoleta = subtotal / cantidad;

                                //Que hacemos con el precio?? Que verificamos.
                                if (descuento == 0) {
                                    //Me fijo si el precio es igual al guardado.
                                    double diferencia = precioDeLaBoleta - p.getPrecioVenta();
                                    if (diferencia > 0.05 || diferencia < -0.05) {
                                        retornoPorFactura[5] = "El precio en el sistema para el artículo: " + art.getCodigo() + " difiere del de la boleta. Revisarlo y volver a intentar.";
                                        throw new Exception(retornoPorFactura[5]);
                                    }
                                }

                                double impuesto = 0;
                                try {
                                    impuesto = Double.parseDouble(Impuesto);
                                } catch (NumberFormatException ne) {
                                    retornoPorFactura[5] = "El impuesto debe ser un número.";
                                    throw new Exception(retornoPorFactura[5]);
                                }

                                double total = 0;
                                try {
                                    total = Double.parseDouble(Total);
                                } catch (NumberFormatException ne) {
                                    retornoPorFactura[5] = "El total debe ser un número.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                
                                FacturaRenglon fr = new FacturaRenglon();
                                fr.setArticulo(art);
                                fr.setCantidad(cantidad);
                                fr.setDescuento(descuento);
                                fr.setFactura(f);
                                fr.setIva(impuesto);
                                fr.setPrecio(precioDeLaBoleta);
                                fr.setSubtotal(subtotal);
                                fr.setTotal(total);
                                
                                f.getRenglones().add(fr);
                                
                                f.setTotal(f.getTotal() + fr.getTotal());
                                f.setSubtotal(f.getSubtotal() + fr.getSubtotal());
                                f.setDescuento(f.getDescuento() + fr.getDescuento());
                                if(art.getIva().getId() == Constantes.ivaBasico) {
                                    f.setTotalBasico(f.getTotalBasico() + fr.getIva());
                                }
                                if(art.getIva().getId() == Constantes.ivaMinimo) {
                                    f.setTotalMinimo(f.getTotalMinimo() + fr.getIva());
                                }
                                
                                GenericDAO.getGenericDAO().actualizar(f);
                            
                            }catch (Exception e) {
                                //Hay algun error en el renglon, hay que borrar lo que ya ingresamos, guardar el mensaje y restal al total de boletas ingresadas
                                GenericDAO.getGenericDAO().borrar(f);
                                if(retornoPorFactura[5] == null) {
                                    retornoPorFactura[5] = "Ocurrio un error.";
                                }
                                retorno.add(retornoPorFactura);
                                totalBoletasIngresadas--;
                            }
                        }
                    } else {
                        //Es una boleta invalida ya fallo en el paso anterior, no hay que hacer nada por ahora
                    }
                    
                } else {
                    try {
                        ultimoNumeroLeido = Numero;
                        //Es una nueva

                        retornoPorFactura[0] = FechaS;
                        retornoPorFactura[1] = Numero;
                        retornoPorFactura[2] = Razon_social;
                        retornoPorFactura[3] = Vendedor;
                        retornoPorFactura[4] = Total;

                        SimpleDateFormat formatterPS = new SimpleDateFormat("dd/MM/yyyy");
                        Date fecha = formatterPS.parse(FechaS);

                        Reparto r = null;

                        try {
                            int numeroVendedorPS = Integer.parseInt(Vendedor);
                            r = SistemaMantenimiento.getInstance().devolverRepartoPorNumeroVendedorPS(numeroVendedorPS);
                            if (r == null) {
                                retornoPorFactura[5] = "No existe el vendedor con ese número.";
                                throw new Exception(retornoPorFactura[5]);
                            }
                        } catch (NumberFormatException ne) {
                            retornoPorFactura[5] = "El número de vendedor debe ser un número.";
                            throw new Exception(retornoPorFactura[5]);
                        }
                        
                        retornoPorFactura[3] = r.toString();

                        DocumentoDeVenta tipoDocumento = null;

                        switch (Documento) {
                            case "CONTADO.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "CONTADO": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "CREDITO.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Credito Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "CREDITO": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Credito Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE DEVOLUCION": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Devolución Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE DEVOLUCION.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Devolución Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE CREDITO": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Credito Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                            case "NOTA DE CREDITO.": {
                                tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Credito Relece");
                                if (tipoDocumento == null) {
                                    retornoPorFactura[5] = "No existe el tipo de documento.";
                                    throw new Exception(retornoPorFactura[5]);
                                }
                                break;
                            }
                        }

                        long numeroFactura = 0;
                        try {
                            numeroFactura = Long.parseLong(Numero);
                        } catch (NumberFormatException ne) {
                            retornoPorFactura[5] = "El número de factura debe ser un número.";
                            throw new Exception(retornoPorFactura[5]);
                        }

                        //Verifico si existe una boleta manual con ese numero. 
                        
                        if (this.numeroDeFacturaMovilValido(numeroFactura, tipoDocumento)) {
                            Cliente c;

                            try {
                                Long codigoClientePS = Long.parseLong(Cuenta);
                                c = SistemaMantenimiento.getInstance().devolverClienteActivoPorCodigoPS(codigoClientePS);
                                if (c == null) {
                                    retornoPorFactura[5] = "No existe el cliente con ese codigo.";
                                    throw new Exception(retornoPorFactura[5]);
                                } else {
                                    //Existe el cliente, procedo a crear la factura y seguir leyendo la informacion para crear el renglon
                                    Articulo art = null;
                                    try {
                                        int codArt = Integer.parseInt(Articulo);
                                        art = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(codArt);
                                        if (art == null) {
                                            retornoPorFactura[5] = "No existe un artículo con el codigo " + Articulo + ".";
                                            throw new Exception(retornoPorFactura[5]);
                                        }
                                    } catch (NumberFormatException ne) {
                                        retornoPorFactura[5] = "El codigo de artículo debe ser un número.";
                                        throw new Exception(retornoPorFactura[5]);
                                    }

                                    Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(art, fecha);
                                    if (p == null) {
                                        retornoPorFactura[5] = "No existe un precio para el artículo con el codigo " + Articulo + ".";
                                        throw new Exception(retornoPorFactura[5]);
                                    }

                                    double cantidad = 0;
                                    try {
                                        cantidad = Double.parseDouble(Cantidad);
                                    } catch (NumberFormatException ne) {
                                        retornoPorFactura[5] = "La cantidad debe ser un número.";
                                        throw new Exception(retornoPorFactura[5]);
                                    }

                                    double subtotal = 0;
                                    try {
                                        subtotal = Double.parseDouble(Subtotal);
                                    } catch (NumberFormatException ne) {
                                        retornoPorFactura[5] = "El subtotal debe ser un número.";
                                        throw new Exception(retornoPorFactura[5]);
                                    }

                                    double descuento = 0;
                                    try {
                                        descuento = Double.parseDouble(Descuentos);
                                    } catch (NumberFormatException ne) {
                                        retornoPorFactura[5] = "El descuento debe ser un número.";
                                        throw new Exception(retornoPorFactura[5]);
                                    }

                                    double precioDeLaBoleta = subtotal / cantidad;

                                    //Que hacemos con el precio?? Que verificamos.
                                    if (descuento == 0) {
                                        //Me fijo si el precio es igual al guardado.
                                        double diferencia = precioDeLaBoleta - p.getPrecioVenta();
                                        if (diferencia > 0.05 || diferencia < -0.05) {
                                            retornoPorFactura[5] = "El precio en el sistema para el artículo: " + art.getCodigo() + " difiere del de la boleta. Revisarlo y volver a intentar.";
                                            throw new Exception(retornoPorFactura[5]);
                                        }
                                    }

                                    double impuesto = 0;
                                    try {
                                        impuesto = Double.parseDouble(Impuesto);
                                    } catch (NumberFormatException ne) {
                                        retornoPorFactura[5] = "El impuesto debe ser un número.";
                                        throw new Exception(retornoPorFactura[5]);
                                    }

                                    double total = 0;
                                    try {
                                        total = Double.parseDouble(Total);
                                    } catch (NumberFormatException ne) {
                                        retornoPorFactura[5] = "El total debe ser un número.";
                                        throw new Exception(retornoPorFactura[5]);
                                    }

                                    Factura f = new Factura();
                                    f.setFecha(fecha);
                                    f.setCliente(c);
                                    f.setDescuento(descuento);
                                    f.setTotal(total);
                                    f.setEsManual(true);
                                    if (c.isCobraChofer()) {
                                        f.setEstaPaga(true);
                                    } else {
                                        f.setEstaPaga(false);
                                    }
                                    f.setNumero(numeroFactura);
                                    f.setReparto(r);
                                    f.setSubtotal(subtotal);
                                    f.setTipoDocumento(tipoDocumento);
                                    if (art.getIva().getId() == Constantes.ivaMinimo) {
                                        f.setTotalMinimo(impuesto);
                                    }
                                    if (art.getIva().getId() == Constantes.ivaBasico) {
                                        f.setTotalBasico(impuesto);
                                    }

                                    List<FacturaRenglon> renglones = new ArrayList<>();
                                    FacturaRenglon fr = new FacturaRenglon();
                                    fr.setArticulo(art);
                                    fr.setCantidad(cantidad);
                                    fr.setDescuento(descuento);
                                    fr.setFactura(f);
                                    fr.setIva(impuesto);
                                    fr.setPrecio(precioDeLaBoleta);
                                    fr.setSubtotal(subtotal);
                                    fr.setTotal(total);
                                    renglones.add(fr);

                                    f.setRenglones(renglones);

                                    GenericDAO.getGenericDAO().guardar(f);
                                    
                                    retornoPorFactura[5] = "Ingresada correctamente.";

                                }
                            } catch (NumberFormatException ne) {
                                retornoPorFactura[5] = "El número de cliente debe ser un número.";
                                throw new Exception(retornoPorFactura[5]);
                            }

                        } else {
                            retornoPorFactura[5] = "Ya existe una factura con el número " + Numero + ".";
                            throw new Exception(retornoPorFactura[5]);
                        }
                        

                    } catch (Exception e) {
                        //Hubo alguna excepcion. Algun error en los datos.
                        if(retornoPorFactura[5] == null) {
                            retornoPorFactura[5] = e.getMessage();
                        }
                        
                    }
                    
                    /*if (retornoPorFactura[5] != null) {
                        retorno.add(retornoPorFactura);
                    } else {
                        totalBoletasIngresadas++;
                    }*/
                    
                    retorno.add(retornoPorFactura);
                    if(retornoPorFactura[5].equals("Ingresada correctamente.")) {
                        totalBoletasIngresadas++;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
        } catch (IOException e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(e);
                    SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                }
            }
        }
        
        String[] retCantIngresadas = new String[1];
        retCantIngresadas[0] = "Ingresadas corractamente : " + totalBoletasIngresadas;
        retorno.add(retCantIngresadas);
        return retorno;
    }

    public boolean esEnvase(Articulo a) throws Exception {
        boolean retorno = false;
        /*if (a.getCodigo() == 320009 || a.getCodigo() == 320017 || a.getCodigo() == 320069) {
            retorno = true;
        }*/
        retorno = Util.esEnvase(a);
        return retorno;
    }

    public boolean esLeche(Articulo a) throws Exception {
        boolean retorno = false;
        /*if (a.getCodigo() == 110 || a.getCodigo() == 112 || a.getCodigo() == 113 || a.getCodigo() == 114 || a.getCodigo() == 115 || a.getCodigo() == 116 || a.getCodigo() == 122 || a.getCodigo() == 123 || a.getCodigo() == 128 || a.getCodigo() == 131 || a.getCodigo() == 132 || a.getCodigo() == 133 || a.getCodigo() == 134) {
            retorno = true;
        }*/
        retorno = Util.esLeche(a);
        return retorno;
    }

    public boolean esCodigoEspecial(Articulo a) throws Exception {
        boolean retorno = false;
        /*if (a.getCodigo() == 170118 || a.getCodigo() == 170080 || a.getCodigo() == 170081 || a.getCodigo() == 170082 || a.getCodigo() == 170084 || a.getCodigo() == 170126 || a.getCodigo() == 170185 || a.getCodigo() == 8000) {
            retorno = true;
        }*/
        retorno = Util.esCodigoEspecial(a);
        return retorno;
    }

    public String[][] detalleArticulosFacturaProrrateo(Date fechaDesde, Date fechaHasta, Reparto r) throws Exception {
        int cantidadArticulos = 0;
        ConfiguracionFacturacion configFact = SistemaMantenimiento.getInstance().devolverConfiguracionFacturacion();
        HashMap<Articulo, Object> resumenPorArticulo = new HashMap<>();
        //HashMap<Articulo, Object> facturadosPorArticulo = new HashMap<>();
        List<Compra> compras;
        List<Factura> facturas;
        double totalExcentoCompras = 0;
        double totalMinimoCompras = 0;
        double totalBasicoCompras = 0;
        if (r == null) {
            compras = new ArrayList<>();
            facturas = new ArrayList<>();
            List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
            for (Reparto rep : repartos) {
                compras.addAll(SistemaCompras.getInstance().devolverComprasEntreFechasYReparto(fechaDesde, fechaHasta, rep));
                facturas.addAll(this.devolverFacturasEntreFechasYReparto(fechaDesde, fechaHasta, rep, true));
            }
        } else {
            compras = SistemaCompras.getInstance().devolverComprasEntreFechasYReparto(fechaDesde, fechaHasta, r);
            facturas = this.devolverFacturasEntreFechasYReparto(fechaDesde, fechaHasta, r, true);
        }
        for (Compra c : compras) {
            if (!c.getTipoDocumento().isEsDocumentoInterno()) {
                if (c.getTipoDocumento().isSuma()) {
                    for (CompraRenglon cr : c.getRenglones()) {
                        if (!esEnvase(cr.getArticulo()) && !esCodigoEspecial(cr.getArticulo())) {
                            if ("Excento".equals(cr.getArticulo().getIva().getNombre())) {
                                totalExcentoCompras = totalExcentoCompras + cr.getTotalPrecioVentaSinIva();
                            }
                            if ("Minimo".equals(cr.getArticulo().getIva().getNombre())) {
                                totalMinimoCompras = totalMinimoCompras + cr.getTotalPrecioVentaSinIva();
                            }
                            if ("Basico".equals(cr.getArticulo().getIva().getNombre())) {
                                totalBasicoCompras = totalBasicoCompras + cr.getTotalPrecioVentaSinIva();
                            }
                            if (resumenPorArticulo.containsKey(cr.getArticulo())) {
                                //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                String[] aux = (String[]) resumenPorArticulo.get(cr.getArticulo());
                                aux[0] = Double.toString(Double.parseDouble(aux[0]) + cr.getCantidad());
                                aux[2] = Double.toString(Double.parseDouble(aux[2]) + cr.getCantidad());
                                resumenPorArticulo.replace(cr.getArticulo(), aux);
                                //}
                            } else {
                                if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                    cantidadArticulos++;
                                    String[] aux = {Double.toString(cr.getCantidad()), "0", Double.toString(cr.getCantidad())};
                                    resumenPorArticulo.put(cr.getArticulo(), aux);
                                } else {
                                    //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                                    GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(cr.getArticulo());
                                    if(ga != null) {
                                        boolean encontroAlgunArticuloDelGrupo = false;
                                        for(Articulo a : ga.getArticulos()) {
                                            if (resumenPorArticulo.containsKey(a)) {
                                                //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                                encontroAlgunArticuloDelGrupo = true;
                                                String[] aux = (String[]) resumenPorArticulo.get(a);
                                                aux[0] = Double.toString(Double.parseDouble(aux[0]) + cr.getCantidad());
                                                aux[2] = Double.toString(Double.parseDouble(aux[2]) + cr.getCantidad());
                                                resumenPorArticulo.replace(a, aux);
                                                break;
                                                //}
                                            }
                                        }
                                        if(!encontroAlgunArticuloDelGrupo) {
                                            cantidadArticulos++;
                                            String[] aux = {Double.toString(cr.getCantidad()), "0", Double.toString(cr.getCantidad())};
                                            resumenPorArticulo.put(cr.getArticulo(), aux);
                                        }
                                    } else {
                                        cantidadArticulos++;
                                        String[] aux = {Double.toString(cr.getCantidad()), "0", Double.toString(cr.getCantidad())};
                                        resumenPorArticulo.put(cr.getArticulo(), aux);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (CompraRenglon cr : c.getRenglones()) {
                        if (!esEnvase(cr.getArticulo()) && !esCodigoEspecial(cr.getArticulo())) {
                            if ("Excento".equals(cr.getArticulo().getIva().getNombre())) {
                                totalExcentoCompras = totalExcentoCompras - cr.getTotalPrecioVentaSinIva();
                            }
                            if ("Minimo".equals(cr.getArticulo().getIva().getNombre())) {
                                totalMinimoCompras = totalMinimoCompras - cr.getTotalPrecioVentaSinIva();
                            }
                            if ("Basico".equals(cr.getArticulo().getIva().getNombre())) {
                                totalBasicoCompras = totalBasicoCompras - cr.getTotalPrecioVentaSinIva();
                            }
                            if (resumenPorArticulo.containsKey(cr.getArticulo())) {
                                String[] aux = (String[]) resumenPorArticulo.get(cr.getArticulo());
                                aux[0] = Double.toString(Double.parseDouble(aux[0]) - cr.getCantidad());
                                aux[2] = Double.toString(Double.parseDouble(aux[2]) - cr.getCantidad());
                                resumenPorArticulo.replace(cr.getArticulo(), aux);
                            } else {
                                if (!configFact.isDetalladoPorGrupoDeArticulo()) {
                                    cantidadArticulos++;
                                    String[] aux = {"-" + Double.toString(cr.getCantidad()), "0", "-" + Double.toString(cr.getCantidad())};
                                    resumenPorArticulo.put(cr.getArticulo(), aux);
                                } else {
                                    //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                                    GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(cr.getArticulo());
                                    if (ga != null) {
                                        boolean encontroAlgunArticuloDelGrupo = false;
                                        for (Articulo a : ga.getArticulos()) {
                                            if (resumenPorArticulo.containsKey(a)) {
                                                //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                                encontroAlgunArticuloDelGrupo = true;
                                                String[] aux = (String[]) resumenPorArticulo.get(a);
                                                aux[0] = Double.toString(Double.parseDouble(aux[0]) - cr.getCantidad());
                                                aux[2] = Double.toString(Double.parseDouble(aux[2]) - cr.getCantidad());
                                                resumenPorArticulo.replace(a, aux);
                                                break;
                                                //}
                                            }
                                        }
                                        if (!encontroAlgunArticuloDelGrupo) {
                                            cantidadArticulos++;
                                            String[] aux = {"-" + Double.toString(cr.getCantidad()), "0", "-" + Double.toString(cr.getCantidad())};
                                            resumenPorArticulo.put(cr.getArticulo(), aux);
                                        }
                                    } else {
                                        cantidadArticulos++;
                                        String[] aux = {"-" + Double.toString(cr.getCantidad()), "0", "-" + Double.toString(cr.getCantidad())};
                                        resumenPorArticulo.put(cr.getArticulo(), aux);
                                    }
                                }
                                //cantidadArticulos++;
                                //String[] aux = {"-" + Double.toString(cr.getCantidad()), "0", "-" + Double.toString(cr.getCantidad())};
                                //resumenPorArticulo.put(cr.getArticulo(), aux);
                            }
                        }
                    }
                }
            }
        }

        //Sumo los Inventarios del dia anterior
        List<Inventario> inventarios = new ArrayList<>();
        Calendar fechaDiaAnterior = Calendar.getInstance();
        fechaDiaAnterior.setTime(fechaDesde);
        fechaDiaAnterior.add(Calendar.DATE, -1);
        int diaDeLaSemana = fechaDiaAnterior.get(Calendar.DAY_OF_WEEK);
        
        if (r == null) {
            List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
            for (Reparto rep : repartos) {
                Inventario i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaDiaAnterior.getTime(), rep);
                if (i != null) {
                    inventarios.add(i);
                } else {
                    if (diaDeLaSemana == 1) {
                        fechaDiaAnterior.add(Calendar.DATE, -1);
                        i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaDiaAnterior.getTime(), rep);
                        if (i != null) {
                            inventarios.add(i);
                        }
                    } else {
                        List<Compra> compasDelDia = SistemaCompras.getInstance().devolverComprasEntreFechasYReparto(fechaDiaAnterior.getTime(), fechaDiaAnterior.getTime(), rep);
                        if(compasDelDia.isEmpty()) {
                            fechaDiaAnterior.add(Calendar.DATE, -1);
                            i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaDiaAnterior.getTime(), rep);
                            if (i != null) {
                                inventarios.add(i);
                            } else {
                                
                            }
                        }
                    }
                }
            }
        } else {
            Inventario i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaDiaAnterior.getTime(), r);
            if (i != null) {
                inventarios.add(i);
            } else {
                if (diaDeLaSemana == 1) {
                    fechaDiaAnterior.add(Calendar.DATE, -1);
                    i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaDiaAnterior.getTime(), r);
                    if (i != null) {
                        inventarios.add(i);
                    }
                } else {
                    List<Compra> compasDelDia = SistemaCompras.getInstance().devolverComprasEntreFechasYReparto(fechaDiaAnterior.getTime(), fechaDiaAnterior.getTime(), r);
                    if(compasDelDia.isEmpty()) {
                        fechaDiaAnterior.add(Calendar.DATE, -1);
                        i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaDiaAnterior.getTime(), r);
                        if (i != null) {
                            inventarios.add(i);
                        } else {
                            
                        }
                    }
                }
            }
        }
        for (Inventario i : inventarios) {
            for (InventarioRenglon ir : i.getRenglones()) {
                if (!esEnvase(ir.getArticulo()) && !esCodigoEspecial(ir.getArticulo())) {
                    if ("Excento".equals(ir.getArticulo().getIva().getNombre())) {
                        totalExcentoCompras = totalExcentoCompras + ir.getSubtotal();
                    }
                    if ("Minimo".equals(ir.getArticulo().getIva().getNombre())) {
                        totalMinimoCompras = totalMinimoCompras + ir.getSubtotal();
                    }
                    if ("Basico".equals(ir.getArticulo().getIva().getNombre())) {
                        totalBasicoCompras = totalBasicoCompras + ir.getSubtotal();
                    }
                    if (resumenPorArticulo.containsKey(ir.getArticulo())) {
                        String[] aux = (String[]) resumenPorArticulo.get(ir.getArticulo());
                        aux[0] = Double.toString(Double.parseDouble(aux[0]) + ir.getCantidad());
                        aux[2] = Double.toString(Double.parseDouble(aux[2]) + ir.getCantidad());
                        resumenPorArticulo.replace(ir.getArticulo(), aux);
                    } else {
                        //cantidadArticulos++;
                        //String[] aux = {Double.toString(ir.getCantidad()), "0", Double.toString(ir.getCantidad())};
                        //resumenPorArticulo.put(ir.getArticulo(), aux);
                        if (!configFact.isDetalladoPorGrupoDeArticulo()) {
                            cantidadArticulos++;
                            String[] aux = {Double.toString(ir.getCantidad()), "0", Double.toString(ir.getCantidad())};
                            resumenPorArticulo.put(ir.getArticulo(), aux);
                        } else {
                            //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                            GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(ir.getArticulo());
                            if (ga != null) {
                                boolean encontroAlgunArticuloDelGrupo = false;
                                for (Articulo a : ga.getArticulos()) {
                                    if (resumenPorArticulo.containsKey(a)) {
                                        //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                        encontroAlgunArticuloDelGrupo = true;
                                        String[] aux = (String[]) resumenPorArticulo.get(a);
                                        aux[0] = Double.toString(Double.parseDouble(aux[0]) + ir.getCantidad());
                                        aux[2] = Double.toString(Double.parseDouble(aux[2]) + ir.getCantidad());
                                        resumenPorArticulo.replace(a, aux);
                                        break;
                                        //}
                                    }
                                }
                                if (!encontroAlgunArticuloDelGrupo) {
                                    cantidadArticulos++;
                                    String[] aux = {Double.toString(ir.getCantidad()), "0", Double.toString(ir.getCantidad())};
                                    resumenPorArticulo.put(ir.getArticulo(), aux);
                                }
                            } else {
                                cantidadArticulos++;
                                String[] aux = {Double.toString(ir.getCantidad()), "0", Double.toString(ir.getCantidad())};
                                resumenPorArticulo.put(ir.getArticulo(), aux);
                            }
                        }
                    }
                }
            }
        }
        inventarios = new ArrayList<>();
        //Resto el inventario del ultimo dia
        if (r == null) {
            List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
            for (Reparto rep : repartos) {
                Inventario i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaHasta, rep);
                if (i != null) {
                    inventarios.add(i);
                }
            }
        } else {
            Inventario i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaHasta, r);
            if (i != null) {
                inventarios.add(i);
            }
        }
        for (Inventario i : inventarios) {

            for (InventarioRenglon ir : i.getRenglones()) {
                if (!esEnvase(ir.getArticulo()) && !esCodigoEspecial(ir.getArticulo())) {
                    if ("Excento".equals(ir.getArticulo().getIva().getNombre())) {
                        totalExcentoCompras = totalExcentoCompras - ir.getSubtotal();
                    }
                    if ("Minimo".equals(ir.getArticulo().getIva().getNombre())) {
                        totalMinimoCompras = totalMinimoCompras - ir.getSubtotal();
                    }
                    if ("Basico".equals(ir.getArticulo().getIva().getNombre())) {
                        totalBasicoCompras = totalBasicoCompras - ir.getSubtotal();
                    }
                    if (resumenPorArticulo.containsKey(ir.getArticulo())) {
                        String[] aux = (String[]) resumenPorArticulo.get(ir.getArticulo());
                        aux[0] = Double.toString(Double.parseDouble(aux[0]) - ir.getCantidad());
                        aux[2] = Double.toString(Double.parseDouble(aux[2]) - ir.getCantidad());
                        resumenPorArticulo.replace(ir.getArticulo(), aux);
                    } else {
                        //cantidadArticulos++;
                        //String[] aux = {"-" + Double.toString(ir.getCantidad()), "0", "-" + Double.toString(ir.getCantidad())};
                        //resumenPorArticulo.put(ir.getArticulo(), aux);
                        if (!configFact.isDetalladoPorGrupoDeArticulo()) {
                            cantidadArticulos++;
                            String[] aux = {"-" + Double.toString(ir.getCantidad()), "0", "-" + Double.toString(ir.getCantidad())};
                            resumenPorArticulo.put(ir.getArticulo(), aux);
                        } else {
                            //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                            GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(ir.getArticulo());
                            if (ga != null) {
                                boolean encontroAlgunArticuloDelGrupo = false;
                                for (Articulo a : ga.getArticulos()) {
                                    if (resumenPorArticulo.containsKey(a)) {
                                        //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                        encontroAlgunArticuloDelGrupo = true;
                                        String[] aux = (String[]) resumenPorArticulo.get(a);
                                        aux[0] = Double.toString(Double.parseDouble(aux[0]) - ir.getCantidad());
                                        aux[2] = Double.toString(Double.parseDouble(aux[2]) - ir.getCantidad());
                                        resumenPorArticulo.replace(a, aux);
                                        break;
                                        //}
                                    }
                                }
                                if (!encontroAlgunArticuloDelGrupo) {
                                    cantidadArticulos++;
                                    String[] aux = {"-" + Double.toString(ir.getCantidad()), "0", "-" + Double.toString(ir.getCantidad())};
                                    resumenPorArticulo.put(ir.getArticulo(), aux);
                                }
                            } else {
                                cantidadArticulos++;
                                String[] aux = {"-" + Double.toString(ir.getCantidad()), "0", "-" + Double.toString(ir.getCantidad())};
                                resumenPorArticulo.put(ir.getArticulo(), aux);
                            }
                        }
                    }
                }
            }

        }

        if (Lecheros.nombreEmpresa.equals("Giamo") || Lecheros.nombreEmpresa.equals("Relece")) {
            //Sumo los Inventarios giamo del dia anterior
            List<InventarioGiamo> inventariosGiamo = new ArrayList<>();
            Calendar fechaDiaAnteriorGiamo = Calendar.getInstance();
            fechaDiaAnteriorGiamo.setTime(fechaDesde);
            fechaDiaAnteriorGiamo.add(Calendar.DATE, -1);
            if (r == null) {
                List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
                for (Reparto rep : repartos) {
                    InventarioGiamo i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaDiaAnteriorGiamo.getTime(), rep);
                    if (i != null) {
                        inventariosGiamo.add(i);
                    } else {
                        if (diaDeLaSemana == 1) {
                            fechaDiaAnterior.add(Calendar.DATE, -1);
                            i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaDiaAnterior.getTime(), rep);
                            if (i != null) {
                                inventariosGiamo.add(i);
                            }
                        } else {
                            List<Compra> compasDelDia = SistemaCompras.getInstance().devolverComprasEntreFechasYReparto(fechaDiaAnterior.getTime(), fechaDiaAnterior.getTime(), rep);
                            if(compasDelDia.isEmpty()) {
                                fechaDiaAnterior.add(Calendar.DATE, -1);
                                i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaDiaAnterior.getTime(), rep);
                                if (i != null) {
                                    inventariosGiamo.add(i);
                                } else {
                                    
                                }
                            }
                        }
                    }
                }
            } else {
                InventarioGiamo i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaDiaAnteriorGiamo.getTime(), r);
                if (i != null) {
                    inventariosGiamo.add(i);
                } else {
                    if (diaDeLaSemana == 1) {
                        fechaDiaAnterior.add(Calendar.DATE, -1);
                        i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaDiaAnterior.getTime(), r);
                        if (i != null) {
                            inventariosGiamo.add(i);
                        }
                    } else {
                        List<Compra> compasDelDia = SistemaCompras.getInstance().devolverComprasEntreFechasYReparto(fechaDiaAnterior.getTime(), fechaDiaAnterior.getTime(), r);
                        if(compasDelDia.isEmpty()) {
                            fechaDiaAnterior.add(Calendar.DATE, -1);
                            i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaDiaAnterior.getTime(), r);
                            if (i != null) {
                                inventariosGiamo.add(i);
                            } else {
                                
                            }
                        }
                    }
                }
            }
            for (InventarioGiamo i : inventariosGiamo) {
                for (InventarioGiamoRenglon ir : i.getRenglones()) {
                    if (!esEnvase(ir.getArticulo()) && !esCodigoEspecial(ir.getArticulo())) {
                        if ("Excento".equals(ir.getArticulo().getIva().getNombre())) {
                            totalExcentoCompras = totalExcentoCompras + ir.getSubtotal();
                        }
                        if ("Minimo".equals(ir.getArticulo().getIva().getNombre())) {
                            totalMinimoCompras = totalMinimoCompras + ir.getSubtotal();
                        }
                        if ("Basico".equals(ir.getArticulo().getIva().getNombre())) {
                            totalBasicoCompras = totalBasicoCompras + ir.getSubtotal();
                        }
                        if (resumenPorArticulo.containsKey(ir.getArticulo())) {
                            String[] aux = (String[]) resumenPorArticulo.get(ir.getArticulo());
                            aux[0] = Double.toString(Double.parseDouble(aux[0]) + ir.getCantidad());
                            aux[2] = Double.toString(Double.parseDouble(aux[2]) + ir.getCantidad());
                            resumenPorArticulo.replace(ir.getArticulo(), aux);
                        } else {
                            //cantidadArticulos++;
                            //String[] aux = {Double.toString(ir.getCantidad()), "0", Double.toString(ir.getCantidad())};
                            //resumenPorArticulo.put(ir.getArticulo(), aux);
                            if (!configFact.isDetalladoPorGrupoDeArticulo()) {
                                cantidadArticulos++;
                                String[] aux = {Double.toString(ir.getCantidad()), "0", Double.toString(ir.getCantidad())};
                                resumenPorArticulo.put(ir.getArticulo(), aux);
                            } else {
                                //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                                GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(ir.getArticulo());
                                if (ga != null) {
                                    boolean encontroAlgunArticuloDelGrupo = false;
                                    for (Articulo a : ga.getArticulos()) {
                                        if (resumenPorArticulo.containsKey(a)) {
                                            //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                            encontroAlgunArticuloDelGrupo = true;
                                            String[] aux = (String[]) resumenPorArticulo.get(a);
                                            aux[0] = Double.toString(Double.parseDouble(aux[0]) + ir.getCantidad());
                                            aux[2] = Double.toString(Double.parseDouble(aux[2]) + ir.getCantidad());
                                            resumenPorArticulo.replace(a, aux);
                                            break;
                                            //}
                                        }
                                    }
                                    if (!encontroAlgunArticuloDelGrupo) {
                                        cantidadArticulos++;
                                        String[] aux = {Double.toString(ir.getCantidad()), "0", Double.toString(ir.getCantidad())};
                                        resumenPorArticulo.put(ir.getArticulo(), aux);
                                    }
                                } else {
                                    cantidadArticulos++;
                                    String[] aux = {Double.toString(ir.getCantidad()), "0", Double.toString(ir.getCantidad())};
                                    resumenPorArticulo.put(ir.getArticulo(), aux);
                                }
                            }
                        }
                    }
                }
            }
            inventariosGiamo = new ArrayList<>();
            //Resto el inventario giamo del ultimo dia
            if (r == null) {
                List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
                for (Reparto rep : repartos) {
                    InventarioGiamo i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaHasta, rep);
                    if (i != null) {
                        inventariosGiamo.add(i);
                    }
                }
            } else {
                InventarioGiamo i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaHasta, r);
                if (i != null) {
                    inventariosGiamo.add(i);
                }
            }
            for (InventarioGiamo i : inventariosGiamo) {

                for (InventarioGiamoRenglon ir : i.getRenglones()) {
                    if (!esEnvase(ir.getArticulo()) && !esCodigoEspecial(ir.getArticulo())) {
                        if ("Excento".equals(ir.getArticulo().getIva().getNombre())) {
                            totalExcentoCompras = totalExcentoCompras - ir.getSubtotal();
                        }
                        if ("Minimo".equals(ir.getArticulo().getIva().getNombre())) {
                            totalMinimoCompras = totalMinimoCompras - ir.getSubtotal();
                        }
                        if ("Basico".equals(ir.getArticulo().getIva().getNombre())) {
                            totalBasicoCompras = totalBasicoCompras - ir.getSubtotal();
                        }
                        if (resumenPorArticulo.containsKey(ir.getArticulo())) {
                            String[] aux = (String[]) resumenPorArticulo.get(ir.getArticulo());
                            aux[0] = Double.toString(Double.parseDouble(aux[0]) - ir.getCantidad());
                            aux[2] = Double.toString(Double.parseDouble(aux[2]) - ir.getCantidad());
                            resumenPorArticulo.replace(ir.getArticulo(), aux);
                        } else {
                            //cantidadArticulos++;
                            //String[] aux = {"-" + Double.toString(ir.getCantidad()), "0", "-" + Double.toString(ir.getCantidad())};
                            //resumenPorArticulo.put(ir.getArticulo(), aux);
                            if (!configFact.isDetalladoPorGrupoDeArticulo()) {
                                cantidadArticulos++;
                                String[] aux = {"-" + Double.toString(ir.getCantidad()), "0", "-" + Double.toString(ir.getCantidad())};
                                resumenPorArticulo.put(ir.getArticulo(), aux);
                            } else {
                                //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                                GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(ir.getArticulo());
                                if (ga != null) {
                                    boolean encontroAlgunArticuloDelGrupo = false;
                                    for (Articulo a : ga.getArticulos()) {
                                        if (resumenPorArticulo.containsKey(a)) {
                                            //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                            encontroAlgunArticuloDelGrupo = true;
                                            String[] aux = (String[]) resumenPorArticulo.get(a);
                                            aux[0] = Double.toString(Double.parseDouble(aux[0]) - ir.getCantidad());
                                            aux[2] = Double.toString(Double.parseDouble(aux[2]) - ir.getCantidad());
                                            resumenPorArticulo.replace(a, aux);
                                            break;
                                            //}
                                        }
                                    }
                                    if (!encontroAlgunArticuloDelGrupo) {
                                        cantidadArticulos++;
                                        String[] aux = {"-" + Double.toString(ir.getCantidad()), "0", "-" + Double.toString(ir.getCantidad())};
                                        resumenPorArticulo.put(ir.getArticulo(), aux);
                                    }
                                } else {
                                    cantidadArticulos++;
                                    String[] aux = {"-" + Double.toString(ir.getCantidad()), "0", "-" + Double.toString(ir.getCantidad())};
                                    resumenPorArticulo.put(ir.getArticulo(), aux);
                                }
                            }
                        }
                    }
                }

            }
        }

        //Calculo los total de las facturas
        double totalExcentoFacturas = 0;
        double totalMinimoFacturas = 0;
        double totalBasicoFacturas = 0;
        for (Factura f : facturas) {
            if (f.getTipoDocumento().isSuma()) {
                for (FacturaRenglon fr : f.getRenglones()) {
                    if (!esEnvase(fr.getArticulo())) {
                        if ("Excento".equals(fr.getArticulo().getIva().getNombre())) {
                            totalExcentoFacturas = totalExcentoFacturas + fr.getSubtotal();
                        }
                        if ("Minimo".equals(fr.getArticulo().getIva().getNombre())) {
                            totalMinimoFacturas = totalMinimoFacturas + fr.getSubtotal();
                        }
                        if ("Basico".equals(fr.getArticulo().getIva().getNombre())) {
                            totalBasicoFacturas = totalBasicoFacturas + fr.getSubtotal();
                        }
                        if (resumenPorArticulo.containsKey(fr.getArticulo())) {
                            String[] aux = (String[]) resumenPorArticulo.get(fr.getArticulo());
                            aux[1] = Double.toString(Double.parseDouble(aux[1]) + fr.getCantidad());
                            aux[2] = Double.toString(Double.parseDouble(aux[2]) - fr.getCantidad());
                            resumenPorArticulo.replace(fr.getArticulo(), aux);
                        } else {
                            //cantidadArticulos++;
                            //String[] aux = {"0", Double.toString(fr.getCantidad()), "-" + fr.getCantidad()};
                            //resumenPorArticulo.put(fr.getArticulo(), aux);
                            if (!configFact.isDetalladoPorGrupoDeArticulo()) {
                                cantidadArticulos++;
                                String[] aux = {"0", Double.toString(fr.getCantidad()), "-" + fr.getCantidad()};
                                resumenPorArticulo.put(fr.getArticulo(), aux);
                            } else {
                                //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                                GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(fr.getArticulo());
                                if (ga != null) {
                                    boolean encontroAlgunArticuloDelGrupo = false;
                                    for (Articulo a : ga.getArticulos()) {
                                        if (resumenPorArticulo.containsKey(a)) {
                                            //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                            encontroAlgunArticuloDelGrupo = true;
                                            String[] aux = (String[]) resumenPorArticulo.get(a);
                                            aux[1] = Double.toString(Double.parseDouble(aux[1]) + fr.getCantidad());
                                            aux[2] = Double.toString(Double.parseDouble(aux[2]) - fr.getCantidad());
                                            resumenPorArticulo.replace(a, aux);
                                            break;
                                            //}
                                        }
                                    }
                                    if (!encontroAlgunArticuloDelGrupo) {
                                        cantidadArticulos++;
                                        String[] aux = {"0", Double.toString(fr.getCantidad()), "-" + fr.getCantidad()};
                                        resumenPorArticulo.put(fr.getArticulo(), aux);
                                    }
                                } else {
                                    cantidadArticulos++;
                                    String[] aux = {"0", Double.toString(fr.getCantidad()), "-" + fr.getCantidad()};
                                    resumenPorArticulo.put(fr.getArticulo(), aux);
                                }
                            }
                        }
                    }
                }
            } else {
                for (FacturaRenglon fr : f.getRenglones()) {
                    if (!esEnvase(fr.getArticulo())) {
                        if ("Excento".equals(fr.getArticulo().getIva().getNombre())) {
                            totalExcentoFacturas = totalExcentoFacturas - fr.getSubtotal();
                        }
                        if ("Minimo".equals(fr.getArticulo().getIva().getNombre())) {
                            totalMinimoFacturas = totalMinimoFacturas - fr.getSubtotal();
                        }
                        if ("Basico".equals(fr.getArticulo().getIva().getNombre())) {
                            totalBasicoFacturas = totalBasicoFacturas - fr.getSubtotal();
                        }
                        if (resumenPorArticulo.containsKey(fr.getArticulo())) {
                            String[] aux = (String[]) resumenPorArticulo.get(fr.getArticulo());
                            aux[1] = Double.toString(Double.parseDouble(aux[1]) - fr.getCantidad());
                            aux[2] = Double.toString(Double.parseDouble(aux[2]) + fr.getCantidad());
                            resumenPorArticulo.replace(fr.getArticulo(), aux);
                        } else {
                            //cantidadArticulos++;
                            //String[] aux = {"0", "-" + Double.toString(fr.getCantidad()), Double.toString(fr.getCantidad())};
                            //resumenPorArticulo.put(fr.getArticulo(), aux);
                            if (!configFact.isDetalladoPorGrupoDeArticulo()) {
                                cantidadArticulos++;
                                String[] aux = {"0", "-" + Double.toString(fr.getCantidad()), Double.toString(fr.getCantidad())};
                                resumenPorArticulo.put(fr.getArticulo(), aux);
                            } else {
                                //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                                GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(fr.getArticulo());
                                if (ga != null) {
                                    boolean encontroAlgunArticuloDelGrupo = false;
                                    for (Articulo a : ga.getArticulos()) {
                                        if (resumenPorArticulo.containsKey(a)) {
                                            //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                            encontroAlgunArticuloDelGrupo = true;
                                            String[] aux = (String[]) resumenPorArticulo.get(a);
                                            aux[1] = Double.toString(Double.parseDouble(aux[1]) - fr.getCantidad());
                                            aux[2] = Double.toString(Double.parseDouble(aux[2]) + fr.getCantidad());
                                            resumenPorArticulo.replace(a, aux);
                                            break;
                                            //}
                                        }
                                    }
                                    if (!encontroAlgunArticuloDelGrupo) {
                                        cantidadArticulos++;
                                        String[] aux = {"0", "-" + Double.toString(fr.getCantidad()), Double.toString(fr.getCantidad())};
                                        resumenPorArticulo.put(fr.getArticulo(), aux);
                                    }
                                } else {
                                    cantidadArticulos++;
                                    String[] aux = {"0", "-" + Double.toString(fr.getCantidad()), Double.toString(fr.getCantidad())};
                                    resumenPorArticulo.put(fr.getArticulo(), aux);
                                }
                            }
                        }
                    }
                }
            }
        }

        //Resto los remitos de pinchadas
        //List<Compra> remitos = SistemaLiquidaciones.getInstance().devolverRemitosPinchadasEntreFechasYReparto(fechaDesde, fechaHasta, r);
        Articulo leche = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(110);
        /*for (Compra c : remitos) {
            for (CompraRenglon cr : c.getRenglones()) {
                if (resumenPorArticulo.containsKey(leche)) {
                    String[] aux = (String[]) resumenPorArticulo.get(leche);
                    aux[0] = Double.toString(Double.parseDouble(aux[0]) - cr.getCantidad());
                    aux[2] = Double.toString(Double.parseDouble(aux[2]) - cr.getCantidad());
                    resumenPorArticulo.replace(leche, aux);
                }
            }
            totalExcentoCompras = totalExcentoCompras - c.getTotalAPrecioDeVentaSinIva();
        }*/

        Articulo lecheComun = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(110);

        //Resto los inau
        List<Inau> inaus = SistemaInformes.getInstance().devolverInausEntreFechasYReparto(fechaDesde, fechaHasta, r);
        for (Inau i : inaus) {
            if (resumenPorArticulo.containsKey(leche)) {
                String[] aux = (String[]) resumenPorArticulo.get(leche);
                aux[0] = Double.toString(Double.parseDouble(aux[0]) - i.getLitrosTotal());
                aux[2] = Double.toString(Double.parseDouble(aux[2]) - i.getLitrosTotal());
                resumenPorArticulo.replace(leche, aux);
            }
            Precio pLComun = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheComun, i.getHastaFecha());
            double total = i.getLitrosTotal() * pLComun.getPrecioVenta();
            totalExcentoCompras = totalExcentoCompras - total;
        }

        //Resto los anep
        List<Anep> aneps = SistemaInformes.getInstance().devolverAnepsEntreFechasYReparto(fechaDesde, fechaHasta, r);
        for (Anep a : aneps) {
            if (resumenPorArticulo.containsKey(leche)) {
                String[] aux = (String[]) resumenPorArticulo.get(leche);
                aux[0] = Double.toString(Double.parseDouble(aux[0]) - a.getLitrosTotal());
                aux[2] = Double.toString(Double.parseDouble(aux[2]) - a.getLitrosTotal());
                resumenPorArticulo.replace(leche, aux);
            }
            Precio pLComun = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheComun, a.getHastaFecha());
            double total = a.getLitrosTotal() * pLComun.getPrecioVenta();
            totalExcentoCompras = totalExcentoCompras - total;
        }

        double porcentajeDeNoFacturacion = 100 - SistemaMantenimiento.getInstance().devolverConfiguracionFacturacion().getPorcentajeFacturacion();

        String[][] retorno = new String[cantidadArticulos + 1][5];
        int fila = 0;
        //double totalExcentoPorArticulo = 0;
        //double totalMinimoPorArticulo = 0;
        //double totalBasicoPorArticulo = 0;
        for (Articulo a : resumenPorArticulo.keySet()) {
            retorno[fila][0] = Integer.toString(a.getCodigo());
            retorno[fila][1] = a.getDescripcion();
            String[] resumen = (String[]) resumenPorArticulo.get(a);
            retorno[fila][2] = resumen[0];
            retorno[fila][3] = resumen[1];
            Double cantidadTotal = Double.parseDouble(resumen[2]) - ((Double.parseDouble(resumen[2]) * porcentajeDeNoFacturacion) / 100);
            //if(cantidadTotal > 0) {
                //int cant = cantidadTotal.intValue();
                retorno[fila][4] = Integer.toString(cantidadTotal.intValue());
                /*if(a.getIva().getId() == Constantes.ivaBasico) {
                    Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, fechaHasta);
                    if(p != null) {
                        totalBasicoPorArticulo = totalBasicoPorArticulo + p.getPrecioVenta()*cant;
                    } else {
                        System.out.println("ARTICULO CON PRECIO EN NULLLLL" + a.getCodigo());
                    }
                }
                if(a.getIva().getId() == Constantes.ivaMinimo) {
                    Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, fechaHasta);
                    if(p != null) {
                        totalMinimoPorArticulo = totalMinimoPorArticulo + p.getPrecioVenta()*cant;
                    } else {
                        System.out.println("ARTICULO CON PRECIO EN NULLLLL" + a.getCodigo());
                    }
                }
                if(a.getIva().getId() == Constantes.ivaExcento) {
                    Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, fechaHasta);
                    if(p != null) {
                        totalExcentoPorArticulo = totalExcentoPorArticulo + p.getPrecioVenta()*cant;
                    } else {
                        System.out.println("ARTICULO CON PRECIO EN NULLLLL" + a.getCodigo());
                    }
                    
                }
            } else {
                retorno[fila][4] = Integer.toString(0);
            }*/
            fila++;
        }

        double totalExcento = totalExcentoCompras - totalExcentoFacturas;
        totalExcento = totalExcento - ((totalExcento * porcentajeDeNoFacturacion) / 100);

        double totalMinimo = totalMinimoCompras - totalMinimoFacturas;
        totalMinimo = totalMinimo - ((totalMinimo * porcentajeDeNoFacturacion) / 100);

        double totalBasico = totalBasicoCompras - totalBasicoFacturas;
        totalBasico = totalBasico - ((totalBasico * porcentajeDeNoFacturacion) / 100);
        
        /*double totalExcento = totalExcentoPorArticulo;
        double totalMinimo = totalMinimoPorArticulo;
        double totalBasico = totalBasicoPorArticulo;*/

        retorno[fila][0] = "Totales";
        retorno[fila][1] = Double.toString(totalExcento);
        retorno[fila][2] = Double.toString(totalMinimo);
        retorno[fila][3] = Double.toString(totalBasico);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadFacturarProrrateo, "Consulto el detalle para facturar el prorrateo.");
        return retorno;
    }

    public String[] facturarProrrateo(Date fechaDesde, Date fechaHasta, Reparto r, String[][] detallesPorArticuloParaFacturar, Double totalExcentoParaFacturar, Double totalMinimoParaFacturar, Double totalBasicoParaFacturar, boolean facturarLeche, boolean facturarProductos) {
        String[] retorno = {""};
        try {

            List<Factura> facturasProrrateoViejas = this.devolverFacturasProrrateoEntreFechasYReparto(fechaDesde, fechaHasta, r);
            if (!facturasProrrateoViejas.isEmpty()) {
                for (Factura f : facturasProrrateoViejas) {
                    GenericDAO.getGenericDAO().borrar(f);
                }
            }
            ConfiguracionFacturacion cf = SistemaMantenimiento.getInstance().devolverConfiguracionFacturacion();
            if (cf.isDetalladaPorArticulo()) {
                retorno = this.facturarProrrateoDetallado(fechaHasta, r, detallesPorArticuloParaFacturar, totalExcentoParaFacturar, totalMinimoParaFacturar, totalBasicoParaFacturar, facturarLeche, facturarProductos);
            } else {
                retorno = this.facturarProrrateoNoDetallado(fechaHasta, r, detallesPorArticuloParaFacturar, totalExcentoParaFacturar, totalMinimoParaFacturar, totalBasicoParaFacturar, facturarLeche, facturarProductos);
            }
            SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadFacturarProrrateo, "Facturo el prorrateo ");
        } catch (Exception exp) {
            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
        }
        return retorno;
    }

     public String[] facturarProrrateoDetallado(Date fechaHasta, Reparto r, String[][] detallesPorArticuloParaFacturar, Double totalExcentoParaFacturar, Double totalMinimoParaFacturar, Double totalBasicoParaFacturar, boolean facturarLeche, boolean facturarProductos) throws Exception {
        String[] retorno = new String[3];
        Date fecha;
        fecha = fechaHasta;
        
        logger.info("-----------------------------------------------------------------------------------------------------------------------------------------------");
        logger.info("Comienza Facturacion Prorrateo para el reparto: " + r.getNombre() + " con la fecha: " + formatter.format(fecha));
        boolean hayCantidadesDeProductosNegativos = false;
        
        double totalLitrosComunFacturar = 0;
        double totalLitrosUltraFacturar = 0;
        double totalLitrosUltraDiferenciadaFacturar = 0;
        double totalLitrosDeslactosadaFacturar = 0;

        GrupoDeArticulos grupoLecheComun = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Común");

        GrupoDeArticulos grupoLecheUtra = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra");

        GrupoDeArticulos grupoLecheUltraDiferenciada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra Diferenciada");

        GrupoDeArticulos grupoLecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Deslactosada");

        HashMap<Articulo, Double> articuloCantidadParaFacturar = new HashMap<>();

        HashMap<GrupoDeArticulos, Double> articuloCantidadTotalParaFacturar = new HashMap<>();

        for (int i = 0; i < detallesPorArticuloParaFacturar.length-1; i++) {
            //for (int j = 0; j < detallesPorArticuloParaFacturar[i].length; j++) {
                boolean esLeche = false;
                if (grupoLecheComun.getArticulos().contains(SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(Integer.parseInt(detallesPorArticuloParaFacturar[i][0])))) {
                    totalLitrosComunFacturar = totalLitrosComunFacturar + Double.parseDouble(detallesPorArticuloParaFacturar[i][4]);
                    esLeche = true;
                }

                if (grupoLecheUtra.getArticulos().contains(SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(Integer.parseInt(detallesPorArticuloParaFacturar[i][0])))) {
                    totalLitrosUltraFacturar = totalLitrosUltraFacturar + Double.parseDouble(detallesPorArticuloParaFacturar[i][4]);
                    esLeche = true;
                }
                if ((grupoLecheUltraDiferenciada != null ? grupoLecheUltraDiferenciada.getArticulos().contains(SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(Integer.parseInt(detallesPorArticuloParaFacturar[i][0]))) : false)) {
                    totalLitrosUltraDiferenciadaFacturar = totalLitrosUltraDiferenciadaFacturar + Double.parseDouble(detallesPorArticuloParaFacturar[i][4]);
                    esLeche = true;
                }
                if (grupoLecheDeslactosada.getArticulos().contains(SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(Integer.parseInt(detallesPorArticuloParaFacturar[i][0])))) {
                    totalLitrosDeslactosadaFacturar = totalLitrosDeslactosadaFacturar + Double.parseDouble(detallesPorArticuloParaFacturar[i][4]);
                    esLeche = true;
                }
                
                if (!esLeche) {
                    Articulo a = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(Integer.parseInt(detallesPorArticuloParaFacturar[i][0]));
                    articuloCantidadParaFacturar.put(a, Double.parseDouble(detallesPorArticuloParaFacturar[i][4]));
                    GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(a);
                    if (ga != null) {
                        if (articuloCantidadTotalParaFacturar.containsKey(ga)) {
                            double cantTotal = articuloCantidadTotalParaFacturar.get(ga) + Double.parseDouble(detallesPorArticuloParaFacturar[i][4]);
                            articuloCantidadTotalParaFacturar.replace(ga, cantTotal);
                            if(!hayCantidadesDeProductosNegativos && cantTotal < 0) {
                                hayCantidadesDeProductosNegativos = true;
                            }
                        } else {
                            articuloCantidadTotalParaFacturar.put(ga, Double.parseDouble(detallesPorArticuloParaFacturar[i][4]));
                            Double cant = Double.parseDouble(detallesPorArticuloParaFacturar[i][4]);
                            if(!hayCantidadesDeProductosNegativos && cant < 0) {
                                hayCantidadesDeProductosNegativos = true;
                            }
                        }
                    }
                }
            //}
        }

        //Si hay ultra diferenciada me fijo cual es el porcentaje
        double porcentajeUltraDiferenciada = 0;
        if (totalLitrosUltraDiferenciadaFacturar != 0) {
            double totalUltrasSumadas = totalLitrosUltraFacturar + totalLitrosUltraDiferenciadaFacturar;
            porcentajeUltraDiferenciada = (totalLitrosUltraDiferenciadaFacturar * 100) / totalUltrasSumadas;
        }

        HashMap<Cliente, Boolean> clientesFacturaLeche = new HashMap<>();
        HashMap<Cliente, Boolean> clientesFacturaLecheUltra = new HashMap<>();
        HashMap<Cliente, Boolean> clientesFacturaLecheDeslactosada = new HashMap<>();
        HashMap<Cliente, Boolean> clientesFacturaProductos = new HashMap<>();
        HashMap<Cliente, List<Articulo>> productosAFacturarPorCliente = new HashMap<>();
        HashMap<Cliente, Double> totalProductosEnPesosAFacturarPorCliente = new HashMap<>();
        HashMap<Articulo, List<Cliente>> clientesPorArticulo = new HashMap<>();
        //Obtengo los clientes
        List<Cliente> clientes = SistemaMantenimiento.getInstance().devolverClientesPorRepartoEstadoYTipo(r, true, true);
        double totalLitrosComunProrrateo = 0;
        double totalLitrosUltraProrrateo = 0;
        double totalLitrosDeslactosadaProrrateo = 0;
        double productosProrrateo = 0;
        for (Cliente c : clientes) {
            if (facturarLeche) {
                if(c.getLitrosComun() != 0) {
                    boolean facturarLecheAlCliente = facturarLechaACliente(c, fecha);
                    clientesFacturaLeche.put(c, facturarLecheAlCliente);
                    if (facturarLecheAlCliente) {
                        System.out.println("Cliente para facturar leche comun: " + c.getNombre() + " " + c.getRazonSocial() + " " + c.getLitrosComun() + " Litros");
                        logger.info("Cliente para facturar leche comun: " + c.getNombre() + " " + c.getRazonSocial() + " " + c.getLitrosComun() + " Litros");
                        totalLitrosComunProrrateo = totalLitrosComunProrrateo + c.getLitrosComun();
                    
                    }
                }
                if(c.getLitrosUltra() != 0) {
                    boolean facturarLecheUltraAlCliente = facturarLechaUltraACliente(c, fecha);
                    clientesFacturaLecheUltra.put(c, facturarLecheUltraAlCliente);
                    if (facturarLecheUltraAlCliente) {
                        System.out.println("Cliente para facturar leche ultra: " + c.getNombre() + " " + c.getRazonSocial() + " " + c.getLitrosUltra()+ " Litros");
                        logger.info("Cliente para facturar leche ultra: " + c.getNombre() + " " + c.getRazonSocial() + " " + c.getLitrosUltra()+ " Litros");
                        totalLitrosUltraProrrateo = totalLitrosUltraProrrateo + c.getLitrosUltra();
                    
                    }
                }
                if(c.getLitrosDeslactosada() != 0) {
                    boolean facturarLecheDeslactosadaAlCliente = facturarLechaDeslactosadaACliente(c, fecha);
                    clientesFacturaLecheDeslactosada.put(c, facturarLecheDeslactosadaAlCliente);
                    if (facturarLecheDeslactosadaAlCliente) {
                        System.out.println("Cliente para facturar leche deslactosada: " + c.getNombre() + " " + c.getRazonSocial());
                        logger.info("Cliente para facturar leche deslactosada: " + c.getNombre() + " " + c.getRazonSocial());
                        totalLitrosDeslactosadaProrrateo = totalLitrosDeslactosadaProrrateo + c.getLitrosDeslactosada();
                    
                    }
                }
            }
            if (facturarProductos) {
                if(c.getProductos() != 0) {
                    Ref<Double> totalAFacturarAlCliente = new Ref<Double>(0.0);
                    boolean facturarProductosAlCliente = facturarProductosACliente(c, fecha, totalAFacturarAlCliente);
                    clientesFacturaProductos.put(c, facturarProductosAlCliente);
                    double totalParaHash = totalAFacturarAlCliente.get();
                    totalProductosEnPesosAFacturarPorCliente.put(c, totalParaHash);
                    if (facturarProductosAlCliente) {
                        logger.info("Cliente para facturar productos: " + c.getNombre() + " " + c.getRazonSocial());
                        productosProrrateo = productosProrrateo + c.getProductos();
                        List<Articulo> articulosAFacturarAlCliente = articulosParaFacturarAlCliente(c, fecha);
                        if (!articulosAFacturarAlCliente.isEmpty()) {
                            productosAFacturarPorCliente.put(c, articulosAFacturarAlCliente);
                            for (Articulo a : articulosAFacturarAlCliente) {
                                logger.info("Se le puede facturar al cliente : " + c.getNombre() + " " + c.getRazonSocial() + " del articulo " + a.getCodigo() + " " + a.getDescripcion() );
                                if (clientesPorArticulo.containsKey(a)) {
                                    clientesPorArticulo.get(a).add(c);
                                } else {
                                    List<Cliente> clientesParaArt = new ArrayList<>();
                                    clientesParaArt.add(c);
                                    clientesPorArticulo.put(a, clientesParaArt);
                                }
                            }
                        }
                    }
                }
            }
            
        }
        
        //List<Cliente> clientesParaMostrarFacturarLeche = clientesFacturaLeche.keySet().stream().filter(c -> clientesFacturaLeche.get(c)).collect(Collectors.toList());
        
        //Para Etapa de Pruebas
        System.out.println("Cantidad de productos para Facturar : " + articuloCantidadParaFacturar.keySet().size());
        logger.info("Cantidad de productos para Facturar : " + articuloCantidadParaFacturar.keySet().size());
        int cantImprimir = 0;
        for(Cliente c : clientesFacturaLeche.keySet()) {
            if(clientesFacturaLeche.get(c)) {
                cantImprimir++;
            }  
        }
        System.out.println("Total de Litros Comun para Facturar : " + totalLitrosComunFacturar);
        System.out.println("Cantidad de Clientes para Facturar Leche Comun : " + cantImprimir);
        logger.info("Total de Litros Comun para Facturar : " + totalLitrosComunFacturar);
        logger.info("Cantidad de Clientes para Facturar Leche Comun : " + cantImprimir);
        cantImprimir = 0;
        for(Cliente c : clientesFacturaLecheUltra.keySet()) {
            if (clientesFacturaLecheUltra.get(c)) {
                cantImprimir++;
            }
        }
        System.out.println("Total de Litros Ultra para Facturar : " + totalLitrosUltraFacturar);
        System.out.println("Cantidad de Clientes para Facturar Leche Ultra : " + cantImprimir);
        logger.info("Total de Litros Ultra para Facturar : " + totalLitrosUltraFacturar);
        logger.info("Cantidad de Clientes para Facturar Leche Ultra : " + cantImprimir);
        cantImprimir = 0;
        for(Cliente c : clientesFacturaLecheDeslactosada.keySet()) {
            if (clientesFacturaLecheDeslactosada.get(c)) {
                cantImprimir++;
            }
            
        }
        System.out.println("Total de Litros Deslactosada para Facturar : " + totalLitrosDeslactosadaFacturar);
        System.out.println("Cantidad de Clientes para Facturar Leche Deslactosada : " + cantImprimir);
        logger.info("Total de Litros Deslactosada para Facturar : " + totalLitrosDeslactosadaFacturar);
        logger.info("Cantidad de Clientes para Facturar Leche Deslactosada : " + cantImprimir);
        cantImprimir = 0;
        for(Cliente c : clientesFacturaProductos.keySet()) {
            if (clientesFacturaProductos.get(c)) {
                cantImprimir++;
            }
            
        }
        System.out.println("Cantidad de Clientes para Facturar Productos : " + cantImprimir);
        System.out.println("Cantidad de Articulos para Cliente : " + clientesPorArticulo.keySet().size());
        logger.info("Cantidad de Clientes para Facturar Productos : " + cantImprimir);
        logger.info("Cantidad de Articulos para Cliente : " + clientesPorArticulo.keySet().size());
        //

        logger.info("Entre todos lo clientes a los que hay que facturarle leche comun suman : " + totalLitrosComunProrrateo + " para calcular el porcentaje." );
        logger.info("Entre todos lo clientes a los que hay que facturarle leche ultra suman : " + totalLitrosUltraProrrateo + " para calcular el porcentaje." );
        logger.info("Entre todos lo clientes a los que hay que facturarle leche deslactosada suman : " + totalLitrosDeslactosadaProrrateo + " para calcular el porcentaje." );
        
        Articulo lecheComun = null;
        Precio pLecheComun = null;
        if (!grupoLecheComun.getArticulos().isEmpty()) {
            lecheComun = grupoLecheComun.getArticulos().get(0);
            pLecheComun = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheComun, fecha);
        }

        Articulo lecheUltra = null;
        Precio pLecheUltra = null;
        if (!grupoLecheUtra.getArticulos().isEmpty()) {
            lecheUltra = grupoLecheUtra.getArticulos().get(0);
            pLecheUltra = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheUltra, fecha);
        }

        Articulo lecheUltraDiferenciada = null;
        Precio pLecheUltraDiferenciada = null;
        if ((grupoLecheUltraDiferenciada != null ? !grupoLecheUltraDiferenciada.getArticulos().isEmpty() : false)) {
            lecheUltraDiferenciada = grupoLecheUltraDiferenciada.getArticulos().get(0);
            pLecheUltraDiferenciada = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheUltraDiferenciada, fecha);
        }

        Articulo lecheDeslactosada = null;
        Precio pLecheDeslactosada = null;
        if (!grupoLecheDeslactosada.getArticulos().isEmpty()) {
            lecheDeslactosada = grupoLecheDeslactosada.getArticulos().get(0);
            pLecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheDeslactosada, fecha);
        }

        ConfiguracionFacturacion configFact = SistemaMantenimiento.getInstance().devolverConfiguracionFacturacion();

        double totalExcentoFacturado = 0;
        double totalMinimoFacturado = 0;
        double totalBasicoFacturado = 0;

        for (Cliente c : clientes) {

            //Creo la nueva factura
            Factura f = new Factura();
            if (!Lecheros.nombreEmpresa.equals("Relece")) {
                DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado");
                f.setTipoDocumento(tipoDocVenta);
            }
            f.setFecha(fecha);
            f.setCliente(c);
            f.setReparto(c.getReparto());
            f.setEsManual(false);
            f.setEstaPaga(true);
            Long numero = configFact.getUltimoNumeroFactura();
            numero++;
            configFact.setUltimoNumeroFactura(numero);
            f.setNumero(numero);
            List<FacturaRenglon> renglones = new ArrayList<>();
            f.setRenglones(renglones);

            //Si hay que facturar la leche comun, agrego la leche comun
            if (clientesFacturaLeche.get(c) != null && clientesFacturaLeche.get(c)) {

                if (Lecheros.nombreEmpresa.equals("Relece") && f.getTipoDocumento() == null) {
                    DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Relece");
                    f.setTipoDocumento(tipoDocVenta);
                }

                double porcentajeComunCliente = (c.getLitrosComun() * 100) / totalLitrosComunProrrateo;
                

                int litrosComunFacturar = (int) (porcentajeComunCliente * totalLitrosComunFacturar) / 100;
                
                if (litrosComunFacturar > 0) {
                    boolean esNumeroMultiploDeCantidadParaFacturar = litrosComunFacturar % grupoLecheComun.getFacturarDeA() == 0;
                    while (!esNumeroMultiploDeCantidadParaFacturar) {
                        litrosComunFacturar = litrosComunFacturar - 1;
                        esNumeroMultiploDeCantidadParaFacturar = litrosComunFacturar % grupoLecheComun.getFacturarDeA() == 0;
                    }
                }
                
                if (litrosComunFacturar > 0 && lecheComun != null && pLecheComun != null) {
                    FacturaRenglon fr = new FacturaRenglon();
                    fr.setFactura(f);
                    fr.setArticulo(lecheComun);
                    fr.setCantidad(litrosComunFacturar);
                    fr.setPrecio(pLecheComun.getPrecioVenta());
                    fr.setSubtotal(litrosComunFacturar * pLecheComun.getPrecioVenta());
                    fr.setIva(0);
                    fr.setTotal(litrosComunFacturar * pLecheComun.getPrecioVenta());
                    renglones.add(fr);
                }

                double totalLecheExcento = (pLecheComun != null ? litrosComunFacturar * pLecheComun.getPrecioVenta() : 0) + (pLecheUltra != null ? 0 * pLecheUltra.getPrecioVenta() : 0) + (pLecheUltraDiferenciada != null ? 0 * pLecheUltraDiferenciada.getPrecioVenta() : 0) + (pLecheDeslactosada != null ? 0 * pLecheDeslactosada.getPrecioVenta() : 0);

                totalExcentoFacturado = totalExcentoFacturado + totalLecheExcento;

                double subTotal = totalLecheExcento;
                double total = subTotal;

                f.setSubtotal(subTotal);
                f.setTotal(total);

            }

            //Me fijo si hay que facturar leche ultra, si hay que facturar leche ultra la agrego
            if (clientesFacturaLecheUltra.get(c) != null && clientesFacturaLecheUltra.get(c)) {

                if (Lecheros.nombreEmpresa.equals("Relece") && f.getTipoDocumento() == null) {
                    DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Relece");
                    f.setTipoDocumento(tipoDocVenta);
                }

                //double porcentajeComunCliente = (c.getLitrosComun() * 100) / totalLitrosComunProrrateo;
                double porcentajeUltraCliente = (c.getLitrosUltra() * 100) / totalLitrosUltraProrrateo;
                

                //int litrosComunFacturar = (int) (porcentajeComunCliente * totalLitrosComunFacturar) / 100;
                int litrosUltraFacturar = (int) (porcentajeUltraCliente * (totalLitrosUltraFacturar + totalLitrosUltraDiferenciadaFacturar)) / 100;
                int litrosUltraDiferenciadaFacturar = 0;
                //int litrosDeslactosadaFacturar = (int) (porcentajeDeslactosadaCliente * totalLitrosDeslactosadaFacturar) / 100;

                if (litrosUltraFacturar > 0) {
                    boolean esNumeroMultiploDeCantidadParaFacturar = litrosUltraFacturar % grupoLecheUtra.getFacturarDeA() == 0;
                    while (!esNumeroMultiploDeCantidadParaFacturar) {
                        litrosUltraFacturar = litrosUltraFacturar - 1;
                        esNumeroMultiploDeCantidadParaFacturar = litrosUltraFacturar % grupoLecheUtra.getFacturarDeA() == 0;
                    }
                }
                
                //Si hay ultra diferenciada calculo cuanto es de cada una
                if (litrosUltraFacturar > 0) {
                    litrosUltraDiferenciadaFacturar = (int) (porcentajeUltraDiferenciada * litrosUltraFacturar) / 100;
                    litrosUltraFacturar = (int) ((100 - porcentajeUltraDiferenciada) * litrosUltraFacturar) / 100;
                    
                    if (litrosUltraDiferenciadaFacturar > 0) {
                        boolean esNumeroMultiploDeCantidadParaFacturar = litrosUltraDiferenciadaFacturar % grupoLecheUltraDiferenciada.getFacturarDeA() == 0;
                        while (!esNumeroMultiploDeCantidadParaFacturar) {
                            litrosUltraDiferenciadaFacturar = litrosUltraDiferenciadaFacturar - 1;
                            esNumeroMultiploDeCantidadParaFacturar = litrosUltraDiferenciadaFacturar % grupoLecheUltraDiferenciada.getFacturarDeA() == 0;
                        }
                    }
                    if (litrosUltraFacturar > 0) {
                        boolean esNumeroMultiploDeCantidadParaFacturar = litrosUltraFacturar % grupoLecheUtra.getFacturarDeA() == 0;
                        while (!esNumeroMultiploDeCantidadParaFacturar) {
                            litrosUltraFacturar = litrosUltraFacturar - 1;
                            esNumeroMultiploDeCantidadParaFacturar = litrosUltraFacturar % grupoLecheUtra.getFacturarDeA() == 0;
                        }
                    }
                }

                
                if (litrosUltraFacturar > 0 && lecheUltra != null && pLecheUltra != null) {
                    FacturaRenglon fr = new FacturaRenglon();
                    fr.setFactura(f);
                    fr.setArticulo(lecheUltra);
                    fr.setCantidad(litrosUltraFacturar);
                    fr.setPrecio(pLecheUltra.getPrecioVenta());
                    fr.setSubtotal(litrosUltraFacturar * pLecheUltra.getPrecioVenta());
                    fr.setIva(0);
                    fr.setTotal(litrosUltraFacturar * pLecheUltra.getPrecioVenta());
                    renglones.add(fr);
                }
                if (litrosUltraDiferenciadaFacturar > 0 && lecheUltraDiferenciada != null && pLecheUltraDiferenciada != null) {
                    FacturaRenglon fr = new FacturaRenglon();
                    fr.setFactura(f);
                    fr.setArticulo(lecheUltraDiferenciada);
                    fr.setCantidad(litrosUltraDiferenciadaFacturar);
                    fr.setPrecio(pLecheUltraDiferenciada.getPrecioVenta());
                    fr.setSubtotal(litrosUltraDiferenciadaFacturar * pLecheUltraDiferenciada.getPrecioVenta());
                    fr.setIva(0);
                    fr.setTotal(litrosUltraDiferenciadaFacturar * pLecheUltraDiferenciada.getPrecioVenta());
                    renglones.add(fr);
                }
                

                //f.setRenglones(renglones);
                double totalLecheExcento = (pLecheComun != null ? 0 * pLecheComun.getPrecioVenta() : 0) + (pLecheUltra != null ? litrosUltraFacturar * pLecheUltra.getPrecioVenta() : 0) + (pLecheUltraDiferenciada != null ? litrosUltraDiferenciadaFacturar * pLecheUltraDiferenciada.getPrecioVenta() : 0) + (pLecheDeslactosada != null ? 0 * pLecheDeslactosada.getPrecioVenta() : 0);

                totalExcentoFacturado = totalExcentoFacturado + totalLecheExcento;

                double subTotal = totalLecheExcento;
                double total = subTotal;

                f.setSubtotal(f.getSubtotal() + subTotal);
                f.setTotal(f.getTotal() + total);

            }

            //Me fijo si hay que facturar leche deslactosada, si hay que facturar leche ultra la agrego
            if (clientesFacturaLecheDeslactosada.get(c) != null && clientesFacturaLecheDeslactosada.get(c)) {

                if (Lecheros.nombreEmpresa.equals("Relece") && f.getTipoDocumento() == null) {
                    DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Relece");
                    f.setTipoDocumento(tipoDocVenta);
                }

                double porcentajeDeslactosadaCliente = 0;
                if (totalLitrosDeslactosadaProrrateo != 0) {
                    porcentajeDeslactosadaCliente = (c.getLitrosDeslactosada() * 100) / totalLitrosDeslactosadaProrrateo;
                }

                int litrosDeslactosadaFacturar = (int) (porcentajeDeslactosadaCliente * totalLitrosDeslactosadaFacturar) / 100;

                if (litrosDeslactosadaFacturar > 0) {
                    boolean esNumeroMultiploDeCantidadParaFacturar = litrosDeslactosadaFacturar % grupoLecheDeslactosada.getFacturarDeA() == 0;
                    while (!esNumeroMultiploDeCantidadParaFacturar) {
                        litrosDeslactosadaFacturar = litrosDeslactosadaFacturar - 1;
                        esNumeroMultiploDeCantidadParaFacturar = litrosDeslactosadaFacturar % grupoLecheDeslactosada.getFacturarDeA() == 0;
                    }
                }
               
                if (litrosDeslactosadaFacturar > 0 && lecheDeslactosada != null && pLecheDeslactosada != null) {
                    FacturaRenglon fr = new FacturaRenglon();
                    fr.setFactura(f);
                    fr.setArticulo(lecheDeslactosada);
                    fr.setCantidad(litrosDeslactosadaFacturar);
                    fr.setPrecio(pLecheDeslactosada.getPrecioVenta());
                    fr.setSubtotal(litrosDeslactosadaFacturar * pLecheDeslactosada.getPrecioVenta());
                    fr.setIva(0);
                    fr.setTotal(litrosDeslactosadaFacturar * pLecheDeslactosada.getPrecioVenta());
                    renglones.add(fr);
                }

                //f.setRenglones(renglones);
                double totalLecheExcento = (pLecheComun != null ? 0 * pLecheComun.getPrecioVenta() : 0) + (pLecheUltra != null ? 0 * pLecheUltra.getPrecioVenta() : 0) + (pLecheUltraDiferenciada != null ? 0 * pLecheUltraDiferenciada.getPrecioVenta() : 0) + (pLecheDeslactosada != null ? 0 * pLecheDeslactosada.getPrecioVenta() : 0);

                totalExcentoFacturado = totalExcentoFacturado + totalLecheExcento;

                double subTotal = totalLecheExcento;
                double total = subTotal;

                f.setSubtotal(f.getSubtotal() + subTotal);
                f.setTotal(f.getTotal() + total);
            }

            if (Lecheros.nombreEmpresa.equals("Relece")) {
                if (!f.getRenglones().isEmpty()) {
                    GenericDAO.getGenericDAO().guardar(f);

                    f = new Factura();
                    //if (Lecheros.nombreEmpresa.equals("Relece")) {
                    DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado");
                    f.setTipoDocumento(tipoDocVenta);
                    //} 
                    f.setFecha(fecha);
                    f.setCliente(c);
                    f.setReparto(c.getReparto());
                    f.setEsManual(false);
                    f.setEstaPaga(true);
                    numero = configFact.getUltimoNumeroFactura();
                    numero++;
                    configFact.setUltimoNumeroFactura(numero);
                    f.setNumero(numero);
                    renglones = new ArrayList<>();
                    f.setRenglones(renglones);
                }
            }

            //Si hay que facturar productos agrego los productos
            if (clientesFacturaProductos.get(c) != null && clientesFacturaProductos.get(c)) {
                /*if (f.getTipoDocumento() == null) {
                    DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado");
                    f.setTipoDocumento(tipoDocVenta);
                }*/
                double totalQueQuedaParaFacturarAlCliente = totalProductosEnPesosAFacturarPorCliente.get(c);
                double totalProdsFact = 0;
                if(productosAFacturarPorCliente.get(c) != null){
                    for (Articulo art : productosAFacturarPorCliente.get(c)) {
                        boolean hayParaFacturar = false;
                        if (articuloCantidadParaFacturar.containsKey(art) && articuloCantidadParaFacturar.get(art) > 0) {
                            hayParaFacturar = true;
                        } else {
                            GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(art);
                            if (ga != null && !ga.getArticulos().isEmpty()) {
                                for (Articulo artGrupo : ga.getArticulos()) {
                                    if (articuloCantidadParaFacturar.containsKey(artGrupo) && articuloCantidadParaFacturar.get(artGrupo) > 0) {
                                        art = artGrupo;
                                        hayParaFacturar = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (hayParaFacturar) {

                            if (Lecheros.nombreEmpresa.equals("Relece")) {
                                DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Cerram");
                                f.setTipoDocumento(tipoDocVenta);
                            }

                            double cantArtCli = devolverCantidadDeArticuloPorCliente(c, art);

                            GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(art);

                            /*double totalDeTodosLosClientesParaElArticulo = 0;
                            if(clientesPorArticulo.get(art) != null) {
                                for (Cliente cli : clientesPorArticulo.get(art)) {
                                    totalDeTodosLosClientesParaElArticulo = totalDeTodosLosClientesParaElArticulo + devolverCantidadDeArticuloPorCliente(cli, art);
                                }
                            }

                            //Si el articulo tiene un grupo me fijo que otros clientes tienen los otros codigos que hay para facturar para sacar el total del porcentaje del cliente
                            if (ga != null) {
                                for (Articulo artCli : ga.getArticulos()) {
                                    if (!artCli.equals(art)) {
                                        if (articuloCantidadParaFacturar.containsKey(artCli)) {
                                            if(clientesPorArticulo.get(artCli) != null) {
                                                for (Cliente cli : clientesPorArticulo.get(artCli)) {
                                                    totalDeTodosLosClientesParaElArticulo = totalDeTodosLosClientesParaElArticulo + devolverCantidadDeArticuloPorCliente(cli, artCli);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            double porcentajeArticuloCliente = (cantArtCli * 100) / totalDeTodosLosClientesParaElArticulo;

                            double cantidadTotalParaFacturarDelArticulo = articuloCantidadParaFacturar.get(art);

                            if (ga != null) {
                                cantidadTotalParaFacturarDelArticulo = articuloCantidadTotalParaFacturar.get(ga);
                            }

                            double cantidadPorPorcentaje = (int) (porcentajeArticuloCliente * cantidadTotalParaFacturarDelArticulo) / 100;

                            double cantArt = cantidadPorPorcentaje;*/
                            
                            double cantArt = cantArtCli;

                            if (cantArt != 0 && totalProdsFact < totalQueQuedaParaFacturarAlCliente) {
                                FacturaRenglon fr = new FacturaRenglon();
                                fr.setFactura(f);
                                fr.setArticulo(art);
                                if(articuloCantidadParaFacturar.get(art) != null) {
                                    if(articuloCantidadParaFacturar.get(art) < cantArt) {
                                        cantArt = articuloCantidadParaFacturar.get(art);
                                    }
                                    
                                }
                                //Me fijo si se factura de a uno, de a dos o de a cuantos
                                if (ga != null) {
                                    if (cantArt > 0) {
                                        boolean esNumeroMultiploDeCantidadParaFacturar = cantArt % ga.getFacturarDeA() == 0;
                                        while (!esNumeroMultiploDeCantidadParaFacturar) {
                                            cantArt = cantArt - 1;
                                            esNumeroMultiploDeCantidadParaFacturar = cantArt % ga.getFacturarDeA() == 0;
                                        }
                                    }
                                }
                                fr.setCantidad(cantArt);
                                Precio precio = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(art, f.getFecha());
                                fr.setPrecio(precio.getPrecioVenta());
                                fr.setSubtotal(cantArt * precio.getPrecioVenta());
                                fr.setIva((fr.getSubtotal() * art.getIva().getPorcentaje() / 100));
                                fr.setTotal(fr.getSubtotal() + fr.getIva());

                                if(cantArt > 0) {
                                    //Agrego el renglon a la factura
                                    f.getRenglones().add(fr);

                                    //Agrego los totales
                                    totalProdsFact = totalProdsFact + fr.getTotal();
                                    totalQueQuedaParaFacturarAlCliente = totalQueQuedaParaFacturarAlCliente - fr.getTotal();
                                    f.setTotal(f.getTotal() + fr.getTotal());
                                    f.setSubtotal(f.getSubtotal() + fr.getSubtotal());
                                    if ("Minimo".equals(art.getIva().getNombre())) {
                                        f.setTotalMinimo(f.getTotalMinimo() + fr.getIva());
                                        totalMinimoFacturado = totalMinimoFacturado + fr.getSubtotal();

                                    }
                                    if ("Basico".equals(art.getIva().getNombre())) {
                                        f.setTotalBasico(f.getTotalBasico() + fr.getIva());
                                        totalBasicoFacturado = totalBasicoFacturado + fr.getSubtotal();
                                    }

                                    //Actualizo el hash en el que llevo la cantidad de articulos para facturar de cada uno.
                                    Double cantQueQueda = articuloCantidadParaFacturar.get(art) - cantArt;
                                    articuloCantidadParaFacturar.put(art, cantQueQueda);
                                }
                            }
                        }

                        //Me fijo si no se paso del total en plata para facturarle el proximo articulo al cliente
                        if (f.getTotal() > totalQueQuedaParaFacturarAlCliente) {
                            break;
                        }
                    }
                }
            }

            if (!f.getRenglones().isEmpty()) {
                GenericDAO.getGenericDAO().guardar(f);
            }
        }

        //Con el sobrante hago las boletas de consumo final que sean necesarias
        /*double faltaFacturarExcento = totalExcentoParaFacturar - totalExcentoFacturado;
        double faltaFacturarMinimo = totalMinimoParaFacturar - totalMinimoFacturado;
        double faltaFacturarBasico = totalBasicoParaFacturar - totalBasicoFacturado;
        double faltaFacturar = faltaFacturarBasico + faltaFacturarMinimo;

        int enCuantasBoletas = (int) (faltaFacturar / configFact.getMaximoBoletaContadoFinal());
        enCuantasBoletas++;

        for (int i = 0; i < enCuantasBoletas; i++) {
            //Creo la factura nueva a consumo final
            Factura fcf = new Factura();
            fcf.setEsManual(false);
            fcf.setFecha(fechaHasta);
            fcf.setEstaPaga(true);
            long numeroCF = configFact.getUltimoNumeroFactura();
            numeroCF++;
            configFact.setUltimoNumeroFactura(numeroCF);
            fcf.setNumero(numeroCF);
            List<FacturaRenglon> renglonesCF = new ArrayList<>();
            fcf.setRenglones(renglonesCF);
            Cliente cli = SistemaMantenimiento.getInstance().devolverClientePorCodigo("0.1");
            fcf.setCliente(cli);
            if (r != null) {
                fcf.setReparto(r);
            } else {
                fcf.setReparto(cli.getReparto());
            }
            if (Lecheros.nombreEmpresa.equals("Relece")) {
                DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Cerram");
                fcf.setTipoDocumento(tipoDocVenta);
            } else {
                DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado");
                fcf.setTipoDocumento(tipoDocVenta);
            }
            for (Articulo a : articuloCantidadParaFacturar.keySet()) {
                GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(a);
                int cantArt = 0;
                //Me fijo si se factura de a uno, de a dos o de a cuantos
                Double cant = (articuloCantidadParaFacturar.get(a) / enCuantasBoletas);
                //Double cant = (articuloCantidadParaFacturar.get(a));
                cantArt = cant.intValue();
                if (ga != null) {
                    boolean esNumeroMultiploDeCantidadParaFacturar = cantArt % ga.getFacturarDeA() == 0;
                    while (!esNumeroMultiploDeCantidadParaFacturar) {
                        cantArt = cantArt - 1;
                        esNumeroMultiploDeCantidadParaFacturar = cantArt % ga.getFacturarDeA() == 0;
                    }
                }
                if(cantArt > 0) {
                    FacturaRenglon fr = new FacturaRenglon();
                    fr.setFactura(fcf);
                    fr.setArticulo(a);
                    fr.setCantidad(cantArt);
                    Precio precio = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, fcf.getFecha());
                    fr.setPrecio(precio.getPrecioVenta());
                    fr.setSubtotal(cantArt * precio.getPrecioVenta());
                    fr.setIva((fr.getSubtotal() * a.getIva().getPorcentaje() / 100));
                    fr.setTotal(fr.getSubtotal() + fr.getIva());

                    //Agrego el renglon a la factura
                    fcf.getRenglones().add(fr);

                    //Agrego los totales
                    fcf.setTotal(fcf.getTotal() + fr.getTotal());
                    fcf.setSubtotal(fcf.getSubtotal() + fr.getSubtotal());
                    if ("Minimo".equals(a.getIva().getNombre())) {
                        fcf.setTotalMinimo(fcf.getTotalMinimo() + fr.getIva());
                        totalMinimoFacturado = totalMinimoFacturado + fr.getSubtotal();

                    }
                    if ("Basico".equals(a.getIva().getNombre())) {
                        fcf.setTotalBasico(fcf.getTotalBasico() + fr.getIva());
                        totalBasicoFacturado = totalBasicoFacturado + fr.getSubtotal();
                    }
                }
            }

            if (!fcf.getRenglones().isEmpty()) {
                GenericDAO.getGenericDAO().guardar(fcf);
            }
        }*/
        if(facturarProductos) {
        int ultimoIndiceLeido = 0;
        Object[] arts = articuloCantidadParaFacturar.keySet().toArray();
        while(ultimoIndiceLeido < arts.length -1) {
            //Creo la factura nueva a consumo final
            Factura fcf = new Factura();
            fcf.setEsManual(false);
            fcf.setFecha(fechaHasta);
            fcf.setEstaPaga(true);
            long numeroCF = configFact.getUltimoNumeroFactura();
            numeroCF++;
            configFact.setUltimoNumeroFactura(numeroCF);
            fcf.setNumero(numeroCF);
            List<FacturaRenglon> renglonesCF = new ArrayList<>();
            fcf.setRenglones(renglonesCF);
            Cliente cli = SistemaMantenimiento.getInstance().devolverClientePorCodigo("0.1");
            fcf.setCliente(cli);
            if (r != null) {
                fcf.setReparto(r);
            } else {
                fcf.setReparto(cli.getReparto());
            }
            if (Lecheros.nombreEmpresa.equals("Relece")) {
                DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Cerram");
                fcf.setTipoDocumento(tipoDocVenta);
            } else {
                DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado");
                fcf.setTipoDocumento(tipoDocVenta);
            }
            
            for (int i = ultimoIndiceLeido; i<arts.length; i++) {
                Articulo a = (Articulo)arts[i];
                    GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(a);
                    int cantArt = 0;
                    //Me fijo si se factura de a uno, de a dos o de a cuantos
                    Double cant = (articuloCantidadParaFacturar.get(a));
                    //Double cant = (articuloCantidadParaFacturar.get(a));
                    cantArt = cant.intValue();
                    if (ga != null) {
                        boolean esNumeroMultiploDeCantidadParaFacturar = cantArt % ga.getFacturarDeA() == 0;
                        while (!esNumeroMultiploDeCantidadParaFacturar) {
                            cantArt = cantArt - 1;
                            esNumeroMultiploDeCantidadParaFacturar = cantArt % ga.getFacturarDeA() == 0;
                        }
                    }
                    if (cantArt > 0) {
                        FacturaRenglon fr = new FacturaRenglon();
                        fr.setFactura(fcf);
                        fr.setArticulo(a);
                        fr.setCantidad(cantArt);
                        Precio precio = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, fcf.getFecha());
                        fr.setPrecio(precio.getPrecioVenta());
                        fr.setSubtotal(cantArt * precio.getPrecioVenta());
                        fr.setIva((fr.getSubtotal() * a.getIva().getPorcentaje() / 100));
                        fr.setTotal(fr.getSubtotal() + fr.getIva());

                        //Agrego el renglon a la factura
                        if((fcf.getTotal() + fr.getTotal()) < configFact.getMaximoBoletaContadoFinal()) {
                        
                            ultimoIndiceLeido++;
                            fcf.getRenglones().add(fr);

                            //Agrego los totales
                            fcf.setTotal(fcf.getTotal() + fr.getTotal());
                            fcf.setSubtotal(fcf.getSubtotal() + fr.getSubtotal());
                            if ("Minimo".equals(a.getIva().getNombre())) {
                                fcf.setTotalMinimo(fcf.getTotalMinimo() + fr.getIva());
                                totalMinimoFacturado = totalMinimoFacturado + fr.getSubtotal();

                            }
                            if ("Basico".equals(a.getIva().getNombre())) {
                                fcf.setTotalBasico(fcf.getTotalBasico() + fr.getIva());
                                totalBasicoFacturado = totalBasicoFacturado + fr.getSubtotal();
                            }
                    
                        } else {
                            if(fcf.getRenglones().isEmpty()){
                                Double cantMaxima = configFact.getMaximoBoletaContadoFinal()/(precio.getPrecioVenta() + (precio.getPrecioVenta()* a.getIva().getPorcentaje() / 100));
                                cantArt = cantMaxima.intValue();
                                fr.setCantidad(cantArt);
                                //precio = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, fcf.getFecha());
                                fr.setPrecio(precio.getPrecioVenta());
                                fr.setSubtotal(cantArt * precio.getPrecioVenta());
                                fr.setIva((fr.getSubtotal() * a.getIva().getPorcentaje() / 100));
                                fr.setTotal(fr.getSubtotal() + fr.getIva());
                                
                                //Agrego el renglon a la factura
                                if ((fcf.getTotal() + fr.getTotal()) < configFact.getMaximoBoletaContadoFinal()) {

                                    //ultimoIndiceLeido++;
                                    articuloCantidadParaFacturar.replace(a, articuloCantidadParaFacturar.get(a) - cantArt);
                                    fcf.getRenglones().add(fr);

                                    //Agrego los totales
                                    fcf.setTotal(fcf.getTotal() + fr.getTotal());
                                    fcf.setSubtotal(fcf.getSubtotal() + fr.getSubtotal());
                                    if ("Minimo".equals(a.getIva().getNombre())) {
                                        fcf.setTotalMinimo(fcf.getTotalMinimo() + fr.getIva());
                                        totalMinimoFacturado = totalMinimoFacturado + fr.getSubtotal();

                                    }
                                    if ("Basico".equals(a.getIva().getNombre())) {
                                        fcf.setTotalBasico(fcf.getTotalBasico() + fr.getIva());
                                        totalBasicoFacturado = totalBasicoFacturado + fr.getSubtotal();
                                    }

                                }
                                break;
                            } else {
                                break;
                            }
                        }
                    } else {
                        ultimoIndiceLeido++;
                    }
            }
            
            if (!fcf.getRenglones().isEmpty()) {
                GenericDAO.getGenericDAO().guardar(fcf);
            }
        }
        }
        
        /*HashMap<Articulo, Boolean> articulosFacturadosCF = new HashMap<>();
        while(articuloCantidadParaFacturar.size() != articulosFacturadosCF.size()) {
            //Creo la factura nueva a consumo final
            Factura fcf = new Factura();
            fcf.setEsManual(false);
            fcf.setFecha(fechaHasta);
            fcf.setEstaPaga(true);
            long numeroCF = configFact.getUltimoNumeroFactura();
            numeroCF++;
            configFact.setUltimoNumeroFactura(numeroCF);
            fcf.setNumero(numeroCF);
            List<FacturaRenglon> renglonesCF = new ArrayList<>();
            fcf.setRenglones(renglonesCF);
            Cliente cli = SistemaMantenimiento.getInstance().devolverClientePorCodigo("0.1");
            fcf.setCliente(cli);
            if (r != null) {
                fcf.setReparto(r);
            } else {
                fcf.setReparto(cli.getReparto());
            }
            if (Lecheros.nombreEmpresa.equals("Relece")) {
                DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado Cerram");
                fcf.setTipoDocumento(tipoDocVenta);
            } else {
                DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado");
                fcf.setTipoDocumento(tipoDocVenta);
            }
            for (Articulo a : articuloCantidadParaFacturar.keySet()) {
                if(!articulosFacturadosCF.containsKey(a)) {
                    GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(a);
                    int cantArt = 0;
                    //Me fijo si se factura de a uno, de a dos o de a cuantos
                    Double cant = (articuloCantidadParaFacturar.get(a));
                    //Double cant = (articuloCantidadParaFacturar.get(a));
                    cantArt = cant.intValue();
                    if (ga != null) {
                        boolean esNumeroMultiploDeCantidadParaFacturar = cantArt % ga.getFacturarDeA() == 0;
                        while (!esNumeroMultiploDeCantidadParaFacturar) {
                            cantArt = cantArt - 1;
                            esNumeroMultiploDeCantidadParaFacturar = cantArt % ga.getFacturarDeA() == 0;
                        }
                    }
                    if (cantArt > 0) {
                        FacturaRenglon fr = new FacturaRenglon();
                        fr.setFactura(fcf);
                        fr.setArticulo(a);
                        fr.setCantidad(cantArt);
                        Precio precio = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, fcf.getFecha());
                        fr.setPrecio(precio.getPrecioVenta());
                        fr.setSubtotal(cantArt * precio.getPrecioVenta());
                        fr.setIva((fr.getSubtotal() * a.getIva().getPorcentaje() / 100));
                        fr.setTotal(fr.getSubtotal() + fr.getIva());

                        //Agrego el renglon a la factura
                        if((fcf.getTotal() + fr.getTotal()) < configFact.getMaximoBoletaContadoFinal()) {
                        
                            articulosFacturadosCF.put(a, true);
                            fcf.getRenglones().add(fr);

                            //Agrego los totales
                            fcf.setTotal(fcf.getTotal() + fr.getTotal());
                            fcf.setSubtotal(fcf.getSubtotal() + fr.getSubtotal());
                            if ("Minimo".equals(a.getIva().getNombre())) {
                                fcf.setTotalMinimo(fcf.getTotalMinimo() + fr.getIva());
                                totalMinimoFacturado = totalMinimoFacturado + fr.getSubtotal();

                            }
                            if ("Basico".equals(a.getIva().getNombre())) {
                                fcf.setTotalBasico(fcf.getTotalBasico() + fr.getIva());
                                totalBasicoFacturado = totalBasicoFacturado + fr.getSubtotal();
                            }
                    
                        }
                    } else {
                        articulosFacturadosCF.put(a, true);
                    }
                }

                if (!fcf.getRenglones().isEmpty()) {
                    GenericDAO.getGenericDAO().guardar(fcf);
                }
            }
        }*/
        if(facturarProductos) {
        //Notas de Credito por lo valores negativos.
        if(hayCantidadesDeProductosNegativos) {
            Factura f = new Factura();
            f.setEsManual(false);
            f.setFecha(fechaHasta);
            f.setEstaPaga(true);
            long numero = configFact.getUltimoNumeroFactura();
            numero++;
            configFact.setUltimoNumeroFactura(numero);
            f.setNumero(numero);
            List<FacturaRenglon> renglones = new ArrayList<>();
            f.setRenglones(renglones);
            Cliente cli = SistemaMantenimiento.getInstance().devolverClientePorCodigo("0.1");
            f.setCliente(cli);
            if (r != null) {
                f.setReparto(r);
            } else {
                f.setReparto(cli.getReparto());
            }
            if (Lecheros.nombreEmpresa.equals("Relece")) {
                DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Devolución");
                f.setTipoDocumento(tipoDocVenta);
            } else {
                DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Nota de Devolución");
                f.setTipoDocumento(tipoDocVenta);
            }
            for (Articulo a : articuloCantidadParaFacturar.keySet()) {
                Double cant = articuloCantidadParaFacturar.get(a);
                if (cant < 0) {
                    FacturaRenglon fr = new FacturaRenglon();
                    fr.setFactura(f);
                    fr.setArticulo(a);
                    fr.setCantidad(-cant);
                    Precio precio = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, f.getFecha());
                    fr.setPrecio(precio.getPrecioVenta());
                    fr.setSubtotal((-cant) * precio.getPrecioVenta());
                    fr.setIva((fr.getSubtotal() * a.getIva().getPorcentaje() / 100));
                    fr.setTotal(fr.getSubtotal() + fr.getIva());

                    //Agrego el renglon a la factura
                    f.getRenglones().add(fr);

                    //Agrego los totales
                    f.setTotal(f.getTotal() + fr.getTotal());
                    f.setSubtotal(f.getSubtotal() + fr.getSubtotal());
                    if ("Minimo".equals(a.getIva().getNombre())) {
                        f.setTotalMinimo(f.getTotalMinimo() + fr.getIva());
                        totalMinimoFacturado = totalMinimoFacturado - fr.getSubtotal();

                    }
                    if ("Basico".equals(a.getIva().getNombre())) {
                        f.setTotalBasico(f.getTotalBasico() + fr.getIva());
                        totalBasicoFacturado = totalBasicoFacturado - fr.getSubtotal();
                    }
                }
            }
            
            if (!f.getRenglones().isEmpty()) {
                GenericDAO.getGenericDAO().guardar(f);
            }
        }
        }
        
        GenericDAO.getGenericDAO().actualizar(configFact);
        //Me fijo si quedo sobrante y si quedo lo divido en n facturas a consumo final. Ademas calculo el porcentaje de facturacion a consumo final.
        //Para hacer lo de las boletas de consumo final.
        retorno[0] = Double.toString(totalExcentoFacturado);
        retorno[1] = Double.toString(totalMinimoFacturado);
        retorno[2] = Double.toString(totalBasicoFacturado);
        
        logger.info(("Total excento para facturar: " + totalExcentoParaFacturar));
        logger.info("Total minimo para facturar: " + totalMinimoParaFacturar);
        logger.info("Total Basico para Facturar: " + totalBasicoParaFacturar);
        logger.info(("Total excento facturado: " + totalExcentoFacturado));
        logger.info("Total minimo facturado: " + totalMinimoFacturado);
        logger.info("Total Basico Facturado: " + totalBasicoFacturado);
        logger.info("-----------------------------------------------------------------------------------------------------------------------------------------------");
        
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadFacturarProrrateo, "Facturo el prorrateo detallado. ");
        return retorno;
    }

    public double devolverCantidadDeArticuloPorCliente(Cliente c, Articulo a) {
        double retorno = 0;
        GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(a);
        for (ProductoClienteProrrateo pcp : c.getProductosClienteProrrateo()) {
            if (pcp.getArticulo().equals(a)) {
                retorno = pcp.getCantidad();
                break;
            } else {
                if(ga != null) {
                    for(Articulo art : ga.getArticulos()) {
                        if (pcp.getArticulo().equals(art)) {
                            retorno = pcp.getCantidad();
                            break;
                        }
                    }
                }
            }
        }
        return retorno;
    }

    public boolean esLecheComun(Articulo a) {
        boolean retorno = false;
        GrupoDeArticulos grupoLecheComun = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Común");
        if (grupoLecheComun.getArticulos().contains(a)) {
            retorno = true;
        }
        return retorno;
    }

    public boolean esLecheUltra(Articulo a) {
        boolean retorno = false;
        GrupoDeArticulos grupoLecheUtra = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra");
        GrupoDeArticulos grupoLecheUltraDiferenciada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra Diferenciada");
        if (grupoLecheUtra.getArticulos().contains(a) || grupoLecheUltraDiferenciada.getArticulos().contains(a)) {
            retorno = true;
        }
        return retorno;
    }

    public boolean esLecheDeslactosada(Articulo a) {
        boolean retorno = false;
        GrupoDeArticulos grupoLecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Deslactosada");
        if (grupoLecheDeslactosada.getArticulos().contains(a)) {
            retorno = true;
        }
        return retorno;
    }

    public boolean facturarLechaACliente(Cliente cli, Date fecha) throws Exception {
        boolean retorno = true;
        Calendar hace6Dias = Calendar.getInstance();
        hace6Dias.setTime(fecha);
        hace6Dias.add(Calendar.DATE, -6);
        List<Factura> facturasCliente = devolverFacturasEntreFechasYCliente(hace6Dias.getTime(), fecha, cli, false);
        switch (cli.getFrecuenciaFacturacionLeche()) {
            case "1 Vez por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheComun(fr.getArticulo())) {
                                retorno = false;
                                break;
                            }
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "2 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheComun(fr.getArticulo())) {
                                cantVeces++;
                                //retorno = false;
                                break;
                            }
                        }
                        if(cantVeces >= 2) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "3 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheComun(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 3) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "4 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheComun(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 4) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "5 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheComun(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 5) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "6 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheComun(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 6) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "7 Veces por Semana": {
                return retorno;
            }
            case "Cada 15 Dias": {
                Calendar hace14Dias = Calendar.getInstance();
                hace14Dias.setTime(fecha);
                hace14Dias.add(Calendar.DATE, -14);
                facturasCliente = devolverFacturasEntreFechasYCliente(hace14Dias.getTime(), fecha, cli, false);
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheComun(fr.getArticulo())) {
                                retorno = false;
                                break;
                            }
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "1 vez por Mes": {
                Calendar hace30Dias = Calendar.getInstance();
                hace30Dias.setTime(fecha);
                hace30Dias.add(Calendar.DATE, -30);
                facturasCliente = devolverFacturasEntreFechasYCliente(hace30Dias.getTime(), fecha, cli, false);
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheComun(fr.getArticulo())) {
                                retorno = false;
                                break;
                            }
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
        }
        return retorno;
    }

    public boolean facturarLechaUltraACliente(Cliente cli, Date fecha) throws Exception {
        boolean retorno = true;
        Calendar hace6Dias = Calendar.getInstance();
        hace6Dias.setTime(fecha);
        hace6Dias.add(Calendar.DATE, -6);
        List<Factura> facturasCliente = devolverFacturasEntreFechasYCliente(hace6Dias.getTime(), fecha, cli, false);
        switch (cli.getFrecuenciaFacturacionLeche()) {
            case "1 Vez por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheUltra(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 1) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "2 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheUltra(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 2) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "3 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheUltra(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 3) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "4 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheUltra(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 4) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "5 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheUltra(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 5) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "6 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheUltra(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 6) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "7 Veces por Semana": {
                return retorno;
            }
            case "Cada 15 Dias": {
                Calendar hace14Dias = Calendar.getInstance();
                hace14Dias.setTime(fecha);
                hace14Dias.add(Calendar.DATE, -14);
                facturasCliente = devolverFacturasEntreFechasYCliente(hace14Dias.getTime(), fecha, cli, false);
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheUltra(fr.getArticulo())) {
                                retorno = false;
                                break;
                            }
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "1 vez por Mes": {
                Calendar hace30Dias = Calendar.getInstance();
                hace30Dias.setTime(fecha);
                hace30Dias.add(Calendar.DATE, -30);
                facturasCliente = devolverFacturasEntreFechasYCliente(hace30Dias.getTime(), fecha, cli, false);
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheUltra(fr.getArticulo())) {
                                retorno = false;
                                break;
                            }
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
        }
        return retorno;
    }

    public boolean facturarLechaDeslactosadaACliente(Cliente cli, Date fecha) throws Exception {
        boolean retorno = true;
        Calendar hace6Dias = Calendar.getInstance();
        hace6Dias.setTime(fecha);
        hace6Dias.add(Calendar.DATE, -6);
        List<Factura> facturasCliente = devolverFacturasEntreFechasYCliente(hace6Dias.getTime(), fecha, cli, false);
        switch (cli.getFrecuenciaFacturacionLeche()) {
            case "1 Vez por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheDeslactosada(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 1) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "2 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheDeslactosada(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 2) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "3 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheDeslactosada(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 3) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "4 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheDeslactosada(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 4) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "5 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheDeslactosada(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 5) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "6 Veces por Semana": {
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    int cantVeces = 0;
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheDeslactosada(fr.getArticulo())) {
                                //retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(cantVeces >= 6) {
                            retorno = false;
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "7 Veces por Semana": {
                return retorno;
            }
            case "Cada 15 Dias": {
                Calendar hace14Dias = Calendar.getInstance();
                hace14Dias.setTime(fecha);
                hace14Dias.add(Calendar.DATE, -14);
                facturasCliente = devolverFacturasEntreFechasYCliente(hace14Dias.getTime(), fecha, cli, false);
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheDeslactosada(fr.getArticulo())) {
                                retorno = false;
                                break;
                            }
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
            case "1 vez por Mes": {
                Calendar hace30Dias = Calendar.getInstance();
                hace30Dias.setTime(fecha);
                hace30Dias.add(Calendar.DATE, -30);
                facturasCliente = devolverFacturasEntreFechasYCliente(hace30Dias.getTime(), fecha, cli, false);
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    for (Factura f : facturasCliente) {
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (esLecheDeslactosada(fr.getArticulo())) {
                                retorno = false;
                                break;
                            }
                        }
                        if (!retorno) {
                            break;
                        }
                    }
                }
                return retorno;
            }
        }
        return retorno;
    }

    public boolean facturarProductosACliente(Cliente cli, Date fecha, Ref<Double> totalAFacturarAlCliente) throws Exception {
        boolean retorno = true;
        totalAFacturarAlCliente.set(cli.getProductos());
        switch (cli.getFrecuenciaFacturacionProductos()) {
            case "1 Vez por Semana": {
                Calendar hace6Dias = Calendar.getInstance();
                hace6Dias.setTime(fecha);
                hace6Dias.add(Calendar.DATE, -6);
                List<Factura> facturasCliente = devolverFacturasEntreFechasYCliente(hace6Dias.getTime(), fecha, cli, false);
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    double totalFacturado = 0;
                    for (Factura f : facturasCliente) {
                        boolean retornoParcial = true;
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (!esLeche(fr.getArticulo()) && !esEnvase(fr.getArticulo())) {
                                retornoParcial = false;
                                //retorno = false;
                                break;
                            }
                        }
                        if(!retornoParcial) {
                            totalFacturado = totalFacturado + f.getTotal();
                            totalAFacturarAlCliente.set(totalAFacturarAlCliente.get() - f.getTotal());
                            if(totalFacturado >= cli.getProductos()) {
                                retorno = false;
                            }
                        }
                        /*if (!retorno) {
                            break;
                        }*/
                    }
                }
                return retorno;
            }
            case "2 Veces por Semana": {
                Calendar hace6Dias = Calendar.getInstance();
                hace6Dias.setTime(fecha);
                hace6Dias.add(Calendar.DATE, -6);
                List<Factura> facturasCliente = devolverFacturasEntreFechasYCliente(hace6Dias.getTime(), fecha, cli, false);
                int cantVeces = 0;
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    double totalFacturado = 0;
                    for (Factura f : facturasCliente) {
                        boolean retornoParcial = true;
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (!esLeche(fr.getArticulo()) && !esEnvase(fr.getArticulo())) {
                                //retorno = false;
                                retornoParcial = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(!retornoParcial && cantVeces >=2) {
                            totalFacturado = totalFacturado + f.getTotal();
                            totalAFacturarAlCliente.set(totalAFacturarAlCliente.get() - f.getTotal());
                            if(totalFacturado >= cli.getProductos()) {
                                retorno = false;
                            }
                        }
                        /*if (!retorno) {
                            break;
                        }*/
                    }
                }
                return retorno;
            }
            case "3 Veces por Semana": {
                Calendar hace6Dias = Calendar.getInstance();
                hace6Dias.setTime(fecha);
                hace6Dias.add(Calendar.DATE, -6);
                List<Factura> facturasCliente = devolverFacturasEntreFechasYCliente(hace6Dias.getTime(), fecha, cli, false);
                int cantVeces = 0;
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    double totalFacturado = 0;
                    for (Factura f : facturasCliente) {
                        boolean retornoParcial = true;
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (!esLeche(fr.getArticulo()) && !esEnvase(fr.getArticulo())) {
                                //retorno = false;
                                retornoParcial = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if(!retornoParcial && cantVeces >= 3) {
                            totalFacturado = totalFacturado + f.getTotal();
                            totalAFacturarAlCliente.set(totalAFacturarAlCliente.get() - f.getTotal());
                            if(totalFacturado >= cli.getProductos()) {
                                retorno = false;
                            }
                        }
                        /*if (!retorno) {
                            break;
                        }*/
                    }
                }
                return retorno;
            }
            case "4 Veces por Semana": {
                Calendar hace6Dias = Calendar.getInstance();
                hace6Dias.setTime(fecha);
                hace6Dias.add(Calendar.DATE, -6);
                List<Factura> facturasCliente = devolverFacturasEntreFechasYCliente(hace6Dias.getTime(), fecha, cli, false);
                int cantVeces = 0;
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    double totalFacturado = 0;
                    for (Factura f : facturasCliente) {
                        boolean retornoParcial = true;
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (!esLeche(fr.getArticulo()) && !esEnvase(fr.getArticulo())) {
                                //retorno = false;
                                retornoParcial = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if (!retornoParcial  && cantVeces >= 4) {
                            totalFacturado = totalFacturado + f.getTotal();
                            totalAFacturarAlCliente.set(totalAFacturarAlCliente.get() - f.getTotal());
                            if (totalFacturado >= cli.getProductos()) {
                                retorno = false;
                            }
                        }
                        /*if (!retorno) {
                            break;
                        }*/
                    }
                }
                return retorno;
            }
            case "5 Veces por Semana": {
                Calendar hace6Dias = Calendar.getInstance();
                hace6Dias.setTime(fecha);
                hace6Dias.add(Calendar.DATE, -6);
                List<Factura> facturasCliente = devolverFacturasEntreFechasYCliente(hace6Dias.getTime(), fecha, cli, false);
                int cantVeces = 0;
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    double totalFacturado = 0;
                    for (Factura f : facturasCliente) {
                        boolean retornoParcial = true;
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (!esLeche(fr.getArticulo()) && !esEnvase(fr.getArticulo())) {
                                //retorno = false;
                                retornoParcial = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if (!retornoParcial  && cantVeces >= 5) {
                            totalFacturado = totalFacturado + f.getTotal();
                            totalAFacturarAlCliente.set(totalAFacturarAlCliente.get() - f.getTotal());
                            if (totalFacturado >= cli.getProductos()) {
                                retorno = false;
                            }
                        }
                        /*if (!retorno) {
                            break;
                        }*/
                    }
                }
                return retorno;
            }
            case "6 Veces por Semana": {
                Calendar hace6Dias = Calendar.getInstance();
                hace6Dias.setTime(fecha);
                hace6Dias.add(Calendar.DATE, -6);
                List<Factura> facturasCliente = devolverFacturasEntreFechasYCliente(hace6Dias.getTime(), fecha, cli, false);
                int cantVeces = 0;
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    double totalFacturado = 0;
                    for (Factura f : facturasCliente) {
                        boolean retornoParcial = true;
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (!esLeche(fr.getArticulo()) && !esEnvase(fr.getArticulo())) {
                                retorno = false;
                                cantVeces++;
                                break;
                            }
                        }
                        if (!retornoParcial  && cantVeces >= 6) {
                            totalFacturado = totalFacturado + f.getTotal();
                            totalAFacturarAlCliente.set(totalAFacturarAlCliente.get() - f.getTotal());
                            if (totalFacturado >= cli.getProductos()) {
                                retorno = false;
                            }
                        }
                    }
                }
                return retorno;
            }
            case "7 Veces por Semana": {
                return retorno;
            }
            case "Cada 15 Dias": {
                Calendar hace14Dias = Calendar.getInstance();
                hace14Dias.setTime(fecha);
                hace14Dias.add(Calendar.DATE, -14);
                List<Factura> facturasCliente = devolverFacturasEntreFechasYCliente(hace14Dias.getTime(), fecha, cli, false);
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    double totalFacturado = 0;
                    for (Factura f : facturasCliente) {
                        boolean retornoParcial = true;
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (!esLeche(fr.getArticulo()) && !esEnvase(fr.getArticulo())) {
                                retorno = false;
                                break;
                            }
                        }
                        if (!retornoParcial) {
                            totalFacturado = totalFacturado + f.getTotal();
                            totalAFacturarAlCliente.set(totalAFacturarAlCliente.get() - f.getTotal());
                            if (totalFacturado >= cli.getProductos()) {
                                retorno = false;
                            }
                        }
                        /*if (!retorno) {
                            break;
                        }*/
                    }
                }
                return retorno;
            }
            case "1 vez por Mes": {
                Calendar hace30Dias = Calendar.getInstance();
                hace30Dias.setTime(fecha);
                hace30Dias.add(Calendar.DATE, -30);
                List<Factura> facturasCliente = devolverFacturasEntreFechasYCliente(hace30Dias.getTime(), fecha, cli, false);
                if (facturasCliente != null && !facturasCliente.isEmpty()) {
                    double totalFacturado = 0;
                    for (Factura f : facturasCliente) {
                        boolean retornoParcial = true;
                        for (FacturaRenglon fr : f.getRenglones()) {
                            if (!esLeche(fr.getArticulo()) && !esEnvase(fr.getArticulo())) {
                                retorno = false;
                                break;
                            }
                        }
                        if (!retornoParcial) {
                            totalFacturado = totalFacturado + f.getTotal();
                            totalAFacturarAlCliente.set(totalAFacturarAlCliente.get() - f.getTotal());
                            if (totalFacturado >= cli.getProductos()) {
                                retorno = false;
                            }
                        }
                        /*if (!retorno) {
                            break;
                        }*/
                    }
                }
                return retorno;
            }
        }
        return retorno;
    }

    public List<Articulo> articulosParaFacturarAlCliente(Cliente cli, Date fecha) throws Exception {
        List<Articulo> retorno = new ArrayList<>();
        for (ProductoClienteProrrateo pcp : cli.getProductosClienteProrrateo()) {
            Calendar hace6Dias = Calendar.getInstance();
            hace6Dias.setTime(fecha);
            hace6Dias.add(Calendar.DATE, -6);
            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consulta = session.createQuery("SELECT f FROM Factura f, FacturaRenglon fr WHERE f.fecha BETWEEN :stDate AND :edDate and fr.factura = f and fr.articulo.codigo = " + pcp.getArticulo().getCodigo() + " and f.cliente.id = " + cli.getId());
            consulta.setDate("stDate", hace6Dias.getTime());
            consulta.setDate("edDate", fecha);
            List<Factura> factsCliArt = consulta.list();
            session.getTransaction().commit();
            session.close();
            switch (pcp.getFrecuencia()) {
                case "1 Vez por Semana": {
                    if (factsCliArt.isEmpty()) {
                        retorno.add(pcp.getArticulo());
                        //break;
                    }
                    break;
                }
                case "2 Veces por Semana": {
                    if (factsCliArt.isEmpty() || factsCliArt.size() == 1) {
                        retorno.add(pcp.getArticulo());
                        //break;
                    }
                    break;
                }
                case "3 Veces por Semana": {
                    if (factsCliArt.isEmpty() || factsCliArt.size() == 2) {
                        retorno.add(pcp.getArticulo());
                        //break;
                    }
                    break;
                }
                case "4 Veces por Semana": {
                    if (factsCliArt.isEmpty() || factsCliArt.size() == 3) {
                        retorno.add(pcp.getArticulo());
                        //break;
                    }
                    break;
                }
                case "5 Veces por Semana": {
                    if (factsCliArt.isEmpty() || factsCliArt.size() == 4) {
                        retorno.add(pcp.getArticulo());
                        //break;
                    }
                    break;
                }
                case "6 Veces por Semana": {
                    if (factsCliArt.isEmpty() || factsCliArt.size() == 5) {
                        retorno.add(pcp.getArticulo());
                        //break;
                    }
                    break;
                    
                }
                case "7 Veces por Semana": {
                    //return retorno;
                    retorno.add(pcp.getArticulo());
                    //break;
                }
                case "Cada 15 Dias": {
                    Calendar hace14Dias = Calendar.getInstance();
                    hace14Dias.setTime(fecha);
                    hace14Dias.add(Calendar.DATE, -14);
                    session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
                    session.beginTransaction();
                    consulta = session.createQuery("SELECT f FROM Factura f, FacturaRenglon fr WHERE f.fecha BETWEEN :stDate AND :edDate and fr.factura = f and fr.articulo.codigo = " + pcp.getArticulo().getCodigo() + " and f.cliente.id = "  + cli.getId());
                    consulta.setDate("stDate", hace14Dias.getTime());
                    consulta.setDate("edDate", fecha);
                    factsCliArt = consulta.list();
                    session.getTransaction().commit();
                    session.close();
                    if (factsCliArt.isEmpty()) {
                        retorno.add(pcp.getArticulo());
                        //break;
                    }
                    break;
                }
                case "1 vez por Mes": {
                    Calendar hace30Dias = Calendar.getInstance();
                    hace30Dias.setTime(fecha);
                    hace30Dias.add(Calendar.DATE, -30);
                    session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
                    session.beginTransaction();
                    consulta = session.createQuery("SELECT f FROM Factura f, FacturaRenglon fr WHERE f.fecha BETWEEN :stDate AND :edDate and fr.factura = f and fr.articulo.codigo = " + pcp.getArticulo().getCodigo() + " and f.cliente.id = "  + cli.getId());
                    consulta.setDate("stDate", hace30Dias.getTime());
                    consulta.setDate("edDate", fecha);
                    factsCliArt = consulta.list();
                    session.getTransaction().commit();
                    session.close();
                    if (factsCliArt.isEmpty()) {
                        retorno.add(pcp.getArticulo());
                        //break;
                    }
                    break;
                }
            }
        }
        return retorno;
    }

    public String[] facturarProrrateoNoDetallado(Date fechaHasta, Reparto r, String[][] detallesPorArticuloParaFacturar, Double totalExcentoParaFacturar, Double totalMinimoParaFacturar, Double totalBasicoParaFacturar, boolean facturarLeche, boolean facturarProductos) throws Exception {
        String[] retorno = new String[3];
        Date fecha;
        fecha = fechaHasta;
        double totalLitrosComunFacturar = 0;
        double totalLitrosUltraFacturar = 0;
        double totalLitrosUltraDiferenciadaFacturar = 0;
        double totalLitrosDeslactosadaFacturar = 0;

        GrupoDeArticulos grupoLecheComun = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Común");

        GrupoDeArticulos grupoLecheUtra = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra");

        GrupoDeArticulos grupoLecheUltraDiferenciada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra Diferenciada");

        GrupoDeArticulos grupoLecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Deslactosada");

        for (int i = 0; i < detallesPorArticuloParaFacturar.length - 1; i++) {
            //for (int j = 0; j < detallesPorArticuloParaFacturar[i].length; j++) {
            if (grupoLecheComun.getArticulos().contains(SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(Integer.parseInt(detallesPorArticuloParaFacturar[i][0])))) {
                totalLitrosComunFacturar = totalLitrosComunFacturar + Double.parseDouble(detallesPorArticuloParaFacturar[i][4]);
            }

            if (grupoLecheUtra.getArticulos().contains(SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(Integer.parseInt(detallesPorArticuloParaFacturar[i][0])))) {
                totalLitrosUltraFacturar = totalLitrosUltraFacturar + Double.parseDouble(detallesPorArticuloParaFacturar[i][4]);
            }
            if ((grupoLecheUltraDiferenciada != null ? grupoLecheUltraDiferenciada.getArticulos().contains(SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(Integer.parseInt(detallesPorArticuloParaFacturar[i][0]))) : false)) {
                totalLitrosUltraDiferenciadaFacturar = totalLitrosUltraDiferenciadaFacturar + Double.parseDouble(detallesPorArticuloParaFacturar[i][4]);
            }
            if (grupoLecheDeslactosada.getArticulos().contains(SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(Integer.parseInt(detallesPorArticuloParaFacturar[i][0])))) {
                totalLitrosDeslactosadaFacturar = totalLitrosDeslactosadaFacturar + Double.parseDouble(detallesPorArticuloParaFacturar[i][4]);
            }
            //}
        }

        //Si hay ultra diferenciada me fijo cual es el porcentaje
        double porcentajeUltraDiferenciada = 0;
        if (totalLitrosUltraDiferenciadaFacturar != 0) {
            double totalUltrasSumadas = totalLitrosUltraFacturar + totalLitrosUltraDiferenciadaFacturar;
            porcentajeUltraDiferenciada = (totalLitrosUltraDiferenciadaFacturar * 100) / totalUltrasSumadas;
        }

        //Obtengo los clientes
        List<Cliente> clientes = SistemaMantenimiento.getInstance().devolverClientesPorRepartoEstadoYTipo(r, true, true);
        double totalLitrosComunProrrateo = 0;
        double totalLitrosUltraProrrateo = 0;
        double totalLitrosDeslactosadaProrrateo = 0;
        double productosMinimoProrrateo = 0;
        double productosBasicoProrrateo = 0;
        for (Cliente c : clientes) {
            totalLitrosComunProrrateo = totalLitrosComunProrrateo + c.getLitrosComun();
            totalLitrosUltraProrrateo = totalLitrosUltraProrrateo + c.getLitrosUltra();
            totalLitrosDeslactosadaProrrateo = totalLitrosDeslactosadaProrrateo + c.getLitrosDeslactosada();
            productosMinimoProrrateo = productosMinimoProrrateo + 0;
            productosBasicoProrrateo = productosBasicoProrrateo + c.getProductos();
        }

        Articulo lecheComun = null;
        Precio pLecheComun = null;
        if (!grupoLecheComun.getArticulos().isEmpty()) {
            lecheComun = grupoLecheComun.getArticulos().get(0);
            pLecheComun = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheComun, fecha);
        }

        Articulo lecheUltra = null;
        Precio pLecheUltra = null;
        if (!grupoLecheUtra.getArticulos().isEmpty()) {
            lecheUltra = grupoLecheUtra.getArticulos().get(0);
            pLecheUltra = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheUltra, fecha);
        }

        Articulo lecheUltraDiferenciada = null;
        Precio pLecheUltraDiferenciada = null;
        if ((grupoLecheUltraDiferenciada != null ? !grupoLecheUltraDiferenciada.getArticulos().isEmpty() : false)) {
            lecheUltraDiferenciada = grupoLecheUltraDiferenciada.getArticulos().get(0);
            pLecheUltraDiferenciada = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheUltraDiferenciada, fecha);
        }

        Articulo lecheDeslactosada = null;
        Precio pLecheDeslactosada = null;
        if (!grupoLecheDeslactosada.getArticulos().isEmpty()) {
            lecheDeslactosada = grupoLecheDeslactosada.getArticulos().get(0);
            pLecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheDeslactosada, fecha);
        }

        Articulo prodMinimo = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(44444);
        Articulo prodBasico = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(55555);

        ConfiguracionFacturacion configFact = SistemaMantenimiento.getInstance().devolverConfiguracionFacturacion();

        double totalExcentoFacturado = 0;
        double totalMinimoFacturado = 0;
        double totalBasicoFacturado = 0;

        for (Cliente c : clientes) {
            double porcentajeComunCliente = (c.getLitrosComun() * 100) / totalLitrosComunProrrateo;
            double porcentajeUltraCliente = (c.getLitrosUltra() * 100) / totalLitrosUltraProrrateo;
            double porcentajeDeslactosadaCliente = 0;
            if (totalLitrosDeslactosadaProrrateo != 0) {
                porcentajeDeslactosadaCliente = (c.getLitrosDeslactosada() * 100) / totalLitrosDeslactosadaProrrateo;
            }

            //double porcentajeMinimoCliente = Convert.ToDouble((cfp.ProductosMinimo * 100) / productosMinimoProrrateo);
            //double porcentajeBasicoCliente = Convert.ToDouble((cfp.ProductosBasico * 100) / productosBasicoProrrateo);
            //Otra forma de ver los porcentajes del minimo y del basico.
            //productosBasicoProrrateo tiene el total de productos en caso de ingresar un solo valor
            //Entonchhyyes lo separo en base al porcentaje que dio despues de restar lo que ya se facturo
            double productosMinimoFacturar = 0;
            double productosBasicoFacturar = 0;
            if (facturarProductos) {
                double totalProductosAFacturar = totalMinimoParaFacturar + totalBasicoParaFacturar;
                if (totalProductosAFacturar != 0 && productosBasicoProrrateo != 0) {
                    double porcentajeMinimoAFacturar = (totalMinimoParaFacturar * 100) / totalProductosAFacturar;
                    double porcentajeBasicoAFacturar = (totalBasicoParaFacturar * 100) / totalProductosAFacturar;
                    //double porcentajeMinimoCliente = Convert.ToDouble((((porcentajeMinimoAFacturar * cfp.ProductosBasico) / 100) * 100) / productosBasicoProrrateo);
                    double porcentajeCliente = (100 * c.getProductos()) / productosBasicoProrrateo;
                    double productosAFacturar = (porcentajeCliente * totalProductosAFacturar) / 100;

                    productosMinimoFacturar = (porcentajeMinimoAFacturar * productosAFacturar) / 100;
                    productosBasicoFacturar = (porcentajeBasicoAFacturar * productosAFacturar) / 100;
                } else {
                    productosMinimoFacturar = 0;
                    productosBasicoFacturar = 0;
                }
            }

            int litrosComunFacturar = 0;
            int litrosUltraFacturar = 0;
            int litrosUltraDiferenciadaFacturar = 0;
            int litrosDeslactosadaFacturar = 0;

            if (facturarLeche) {
                litrosComunFacturar = (int) (porcentajeComunCliente * totalLitrosComunFacturar) / 100;
                litrosUltraFacturar = (int) (porcentajeUltraCliente * (totalLitrosUltraFacturar + totalLitrosUltraDiferenciadaFacturar)) / 100;
                litrosUltraDiferenciadaFacturar = 0;
                litrosDeslactosadaFacturar = (int) (porcentajeDeslactosadaCliente * totalLitrosDeslactosadaFacturar) / 100;

                //Si hay ultra diferenciada calculo cuanto es de cada una
                if (litrosUltraFacturar != 0) {
                    litrosUltraDiferenciadaFacturar = (int) (porcentajeUltraDiferenciada * litrosUltraFacturar) / 100;
                    litrosUltraFacturar = (int) ((100 - porcentajeUltraDiferenciada) * litrosUltraFacturar) / 100;
                }
            }

            //Crear la factura.
            //double totalExcentoFacturar = (litrosComunFacturar * pLecheComun.getPrecioVenta()) + (litrosUltraFacturar * pLecheUltra.getPrecioVenta()) + (litrosDeslactosadaFacturar * pLecheDeslactosada.getPrecioVenta());
            Factura f = new Factura();
            DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado");
            f.setTipoDocumento(tipoDocVenta);
            f.setFecha(fecha);
            f.setCliente(c);
            f.setReparto(c.getReparto());
            f.setEsManual(false);
            f.setEstaPaga(true);
            Long numero = configFact.getUltimoNumeroFactura();
            numero++;
            configFact.setUltimoNumeroFactura(numero);
            f.setNumero(numero);
            List<FacturaRenglon> renglones = new ArrayList<>();
            if (litrosComunFacturar != 0 && lecheComun != null && pLecheComun != null) {
                FacturaRenglon fr = new FacturaRenglon();
                fr.setFactura(f);
                fr.setArticulo(lecheComun);
                fr.setCantidad(litrosComunFacturar);
                fr.setPrecio(pLecheComun.getPrecioVenta());
                fr.setSubtotal(litrosComunFacturar * pLecheComun.getPrecioVenta());
                fr.setIva(0);
                fr.setTotal(litrosComunFacturar * pLecheComun.getPrecioVenta());
                renglones.add(fr);
            }
            if (litrosUltraFacturar != 0 && lecheUltra != null && pLecheUltra != null) {
                FacturaRenglon fr = new FacturaRenglon();
                fr.setFactura(f);
                fr.setArticulo(lecheUltra);
                fr.setCantidad(litrosUltraFacturar);
                fr.setPrecio(pLecheUltra.getPrecioVenta());
                fr.setSubtotal(litrosUltraFacturar * pLecheUltra.getPrecioVenta());
                fr.setIva(0);
                fr.setTotal(litrosUltraFacturar * pLecheUltra.getPrecioVenta());
                renglones.add(fr);
            }
            if (litrosUltraDiferenciadaFacturar != 0 && lecheUltraDiferenciada != null && pLecheUltraDiferenciada != null) {
                FacturaRenglon fr = new FacturaRenglon();
                fr.setFactura(f);
                fr.setArticulo(lecheUltraDiferenciada);
                fr.setCantidad(litrosUltraDiferenciadaFacturar);
                fr.setPrecio(pLecheUltraDiferenciada.getPrecioVenta());
                fr.setSubtotal(litrosUltraDiferenciadaFacturar * pLecheUltraDiferenciada.getPrecioVenta());
                fr.setIva(0);
                fr.setTotal(litrosUltraDiferenciadaFacturar * pLecheUltraDiferenciada.getPrecioVenta());
                renglones.add(fr);
            }
            if (litrosDeslactosadaFacturar != 0 && lecheDeslactosada != null && pLecheDeslactosada != null) {
                FacturaRenglon fr = new FacturaRenglon();
                fr.setFactura(f);
                fr.setArticulo(lecheDeslactosada);
                fr.setCantidad(litrosDeslactosadaFacturar);
                fr.setPrecio(pLecheDeslactosada.getPrecioVenta());
                fr.setSubtotal(litrosDeslactosadaFacturar * pLecheDeslactosada.getPrecioVenta());
                fr.setIva(0);
                fr.setTotal(litrosDeslactosadaFacturar * pLecheDeslactosada.getPrecioVenta());
                renglones.add(fr);
            }
            if (productosMinimoFacturar != 0) {
                FacturaRenglon fr = new FacturaRenglon();
                fr.setFactura(f);
                fr.setArticulo(prodMinimo);
                fr.setCantidad(productosMinimoFacturar);
                fr.setPrecio(1);
                double iva = (productosMinimoFacturar * 9.1) / 100;
                fr.setIva(iva);
                fr.setTotal(productosMinimoFacturar);
                fr.setSubtotal(productosMinimoFacturar - iva);
                renglones.add(fr);
            }
            if (productosBasicoFacturar != 0) {
                FacturaRenglon fr = new FacturaRenglon();
                fr.setFactura(f);
                fr.setArticulo(prodBasico);
                fr.setCantidad(productosBasicoFacturar);
                fr.setPrecio(1);
                double iva = (productosBasicoFacturar * 18.03) / 100;
                fr.setIva(iva);
                fr.setTotal(productosBasicoFacturar);
                fr.setSubtotal(productosBasicoFacturar - iva);
                renglones.add(fr);
            }

            f.setRenglones(renglones);

            double totalLecheExcento = (pLecheComun != null ? litrosComunFacturar * pLecheComun.getPrecioVenta() : 0) + (pLecheUltra != null ? litrosUltraFacturar * pLecheUltra.getPrecioVenta() : 0) + (pLecheUltraDiferenciada != null ? litrosUltraDiferenciadaFacturar * pLecheUltraDiferenciada.getPrecioVenta() : 0) + (pLecheDeslactosada != null ? litrosDeslactosadaFacturar * pLecheDeslactosada.getPrecioVenta() : 0);
            double ivaMinimo = (productosMinimoFacturar * 9.1) / 100;
            double totalMinimo = productosMinimoFacturar - ivaMinimo;
            double ivaBasico = (productosBasicoFacturar * 18.03) / 100;
            double totalBasico = productosBasicoFacturar - ivaBasico;

            totalExcentoFacturado = totalExcentoFacturado + totalLecheExcento;
            totalMinimoFacturado = totalMinimoFacturado + totalMinimo;
            totalBasicoFacturado = totalBasicoFacturado + totalBasico;

            double subTotal = totalLecheExcento + totalMinimo + totalBasico;
            double ivaTotal = ivaMinimo + ivaBasico;
            double total = subTotal + ivaTotal;

            f.setSubtotal(subTotal);
            f.setTotalMinimo(ivaMinimo);
            f.setTotalBasico(ivaBasico);
            f.setTotal(total);
            if (!f.getRenglones().isEmpty()) {
                GenericDAO.getGenericDAO().guardar(f);
            }
        }
        SistemaMantenimiento.getInstance().guardarConfiguracionDeFacturacion(configFact);
        retorno[0] = Double.toString(totalExcentoFacturado);
        retorno[1] = Double.toString(totalMinimoFacturado);
        retorno[2] = Double.toString(totalBasicoFacturado);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadFacturarProrrateo, "Facturo el prorrateo no detallado. ");
        return retorno;
    }

    public List<Factura> devolverFacturasProrrateoEntreFechasYReparto(Date fechaDesde, Date fechaHasta, Reparto reparto) throws Exception {
        List<Factura> retorno = new ArrayList<>();
        if (reparto == null) {
            List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
            for (Reparto rep : repartos) {
                Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
                session.beginTransaction();
                Query consulta = session.createQuery("FROM Factura WHERE (fecha BETWEEN :stDate AND :edDate" + ") and reparto_id = " + rep.getId() + " and esManual = :man");
                consulta.setDate("stDate", fechaDesde);
                consulta.setDate("edDate", fechaHasta);
                consulta.setBoolean("man", false);
                List<Factura> aux = consulta.list();
                retorno.addAll(aux);
                session.getTransaction().commit();
                session.close();
            }
            return retorno;
        } else {
            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consulta = session.createQuery("FROM Factura WHERE (fecha BETWEEN :stDate AND :edDate" + ") and reparto_id = " + reparto.getId() + " and esManual = :man");
            consulta.setDate("stDate", fechaDesde);
            consulta.setDate("edDate", fechaHasta);
            consulta.setBoolean("man", false);
            retorno = consulta.list();
            session.getTransaction().commit();
            session.close();
            return retorno;
        }
    }

    public String[][] detalleArticulosVerificarFacturacion(Date fechaDesde, Date fechaHasta, Reparto r) throws Exception {
        int cantidadArticulos = 0;
        ConfiguracionFacturacion configFact = SistemaMantenimiento.getInstance().devolverConfiguracionFacturacion();
        HashMap<Articulo, Object> resumenPorArticulo = new HashMap<>();
        //HashMap<Articulo, Object> facturadosPorArticulo = new HashMap<>();
        List<Compra> compras;
        List<Factura> facturas;
        double totalExcentoCompras = 0;
        double totalMinimoCompras = 0;
        double totalBasicoCompras = 0;
        if (r == null) {
            compras = new ArrayList<>();
            facturas = new ArrayList<>();
            List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
            for (Reparto rep : repartos) {
                compras.addAll(SistemaCompras.getInstance().devolverComprasEntreFechasYReparto(fechaDesde, fechaHasta, rep));
                facturas.addAll(this.devolverFacturasEntreFechasYReparto(fechaDesde, fechaHasta, rep, true));
                facturas.addAll(this.devolverFacturasEntreFechasYReparto(fechaDesde, fechaHasta, rep, false));
            }
        } else {
            compras = SistemaCompras.getInstance().devolverComprasEntreFechasYReparto(fechaDesde, fechaHasta, r);
            facturas = this.devolverFacturasEntreFechasYReparto(fechaDesde, fechaHasta, r, true);
            facturas.addAll(this.devolverFacturasEntreFechasYReparto(fechaDesde, fechaHasta, r, false));
        }
        for (Compra c : compras) {
            if (!c.getTipoDocumento().isEsDocumentoInterno()) {
                if (c.getTipoDocumento().isSuma()) {
                    for (CompraRenglon cr : c.getRenglones()) {
                        if (!esEnvase(cr.getArticulo()) && !esCodigoEspecial(cr.getArticulo())) {
                            if ("Excento".equals(cr.getArticulo().getIva().getNombre())) {
                                totalExcentoCompras = totalExcentoCompras + cr.getTotalPrecioVentaSinIva();
                            }
                            if ("Minimo".equals(cr.getArticulo().getIva().getNombre())) {
                                totalMinimoCompras = totalMinimoCompras + cr.getTotalPrecioVentaSinIva();
                            }
                            if ("Basico".equals(cr.getArticulo().getIva().getNombre())) {
                                totalBasicoCompras = totalBasicoCompras + cr.getTotalPrecioVentaSinIva();
                            }
                            if (resumenPorArticulo.containsKey(cr.getArticulo())) {
                                String[] aux = (String[]) resumenPorArticulo.get(cr.getArticulo());
                                aux[0] = Double.toString(Double.parseDouble(aux[0]) + cr.getCantidad());
                                aux[3] = Double.toString(Double.parseDouble(aux[3]) + cr.getCantidad());
                                resumenPorArticulo.replace(cr.getArticulo(), aux);
                            } else {
                                if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                    cantidadArticulos++;
                                    String[] aux = {Double.toString(cr.getCantidad()), "0", "0", Double.toString(cr.getCantidad())};
                                    resumenPorArticulo.put(cr.getArticulo(), aux);
                                } else {
                                    //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                                    GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(cr.getArticulo());
                                    if (ga != null) {
                                        boolean encontroAlgunArticuloDelGrupo = false;
                                        for (Articulo a : ga.getArticulos()) {
                                            if (resumenPorArticulo.containsKey(a)) {
                                                //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                                encontroAlgunArticuloDelGrupo = true;
                                                String[] aux = (String[]) resumenPorArticulo.get(a);
                                                aux[0] = Double.toString(Double.parseDouble(aux[0]) + cr.getCantidad());
                                                aux[3] = Double.toString(Double.parseDouble(aux[3]) + cr.getCantidad());
                                                resumenPorArticulo.replace(a, aux);
                                                break;
                                                //}
                                            }
                                        }
                                        if (!encontroAlgunArticuloDelGrupo) {
                                            cantidadArticulos++;
                                            String[] aux = {Double.toString(cr.getCantidad()), "0", "0", Double.toString(cr.getCantidad())};
                                            resumenPorArticulo.put(cr.getArticulo(), aux);

                                        }
                                    } else {
                                        cantidadArticulos++;
                                        String[] aux = {Double.toString(cr.getCantidad()), "0", "0", Double.toString(cr.getCantidad())};
                                        resumenPorArticulo.put(cr.getArticulo(), aux);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (CompraRenglon cr : c.getRenglones()) {
                        if (!esEnvase(cr.getArticulo()) && !esCodigoEspecial(cr.getArticulo())) {
                            if ("Excento".equals(cr.getArticulo().getIva().getNombre())) {
                                totalExcentoCompras = totalExcentoCompras - cr.getTotalPrecioVentaSinIva();
                            }
                            if ("Minimo".equals(cr.getArticulo().getIva().getNombre())) {
                                totalMinimoCompras = totalMinimoCompras - cr.getTotalPrecioVentaSinIva();
                            }
                            if ("Basico".equals(cr.getArticulo().getIva().getNombre())) {
                                totalBasicoCompras = totalBasicoCompras - cr.getTotalPrecioVentaSinIva();
                            }
                            if (resumenPorArticulo.containsKey(cr.getArticulo())) {
                                String[] aux = (String[]) resumenPorArticulo.get(cr.getArticulo());
                                aux[0] = Double.toString(Double.parseDouble(aux[0]) - cr.getCantidad());
                                aux[3] = Double.toString(Double.parseDouble(aux[3]) - cr.getCantidad());
                                resumenPorArticulo.replace(cr.getArticulo(), aux);
                            } else {
                                if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                    cantidadArticulos++;
                                    String[] aux = {"-" + Double.toString(cr.getCantidad()), "0", "0", "-" + Double.toString(cr.getCantidad())};
                                    resumenPorArticulo.put(cr.getArticulo(), aux);
                                } else {
                                    //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                                    GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(cr.getArticulo());
                                    if (ga != null) {
                                        boolean encontroAlgunArticuloDelGrupo = false;
                                        for (Articulo a : ga.getArticulos()) {
                                            if (resumenPorArticulo.containsKey(a)) {
                                                //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                                encontroAlgunArticuloDelGrupo = true;
                                                String[] aux = (String[]) resumenPorArticulo.get(a);
                                                aux[0] = Double.toString(Double.parseDouble(aux[0]) - cr.getCantidad());
                                                aux[3] = Double.toString(Double.parseDouble(aux[3]) - cr.getCantidad());
                                                resumenPorArticulo.replace(a, aux);
                                                break;
                                                //}
                                            }
                                        }
                                        if (!encontroAlgunArticuloDelGrupo) {
                                            cantidadArticulos++;
                                            String[] aux = {"-" + Double.toString(cr.getCantidad()), "0", "0", "-" + Double.toString(cr.getCantidad())};
                                            resumenPorArticulo.put(cr.getArticulo(), aux);

                                        }
                                    } else {
                                        cantidadArticulos++;
                                        String[] aux = {"-" + Double.toString(cr.getCantidad()), "0", "0", "-" + Double.toString(cr.getCantidad())};
                                        resumenPorArticulo.put(cr.getArticulo(), aux);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Sumo los Inventarios del dia anterior
        List<Inventario> inventarios = new ArrayList<>();
        Calendar fechaDiaAnterior = Calendar.getInstance();
        fechaDiaAnterior.setTime(fechaDesde);
        fechaDiaAnterior.add(Calendar.DATE, -1);
        int diaDeLaSemana = fechaDiaAnterior.get(Calendar.DAY_OF_WEEK);
        if (r == null) {
            List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
            for (Reparto rep : repartos) {
                Inventario i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaDiaAnterior.getTime(), rep);
                if (i != null) {
                    inventarios.add(i);
                } else {
                    if (diaDeLaSemana == 1) {
                        fechaDiaAnterior.add(Calendar.DATE, -1);
                        i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaDiaAnterior.getTime(), rep);
                        if (i != null) {
                            inventarios.add(i);
                        }
                    } else {
                        List<Compra> compasDelDia = SistemaCompras.getInstance().devolverComprasEntreFechasYReparto(fechaDiaAnterior.getTime(), fechaDiaAnterior.getTime(), rep);
                        if(compasDelDia.isEmpty()) {
                            fechaDiaAnterior.add(Calendar.DATE, -1);
                            i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaDiaAnterior.getTime(), rep);
                            if (i != null) {
                                inventarios.add(i);
                            } else {
                                
                            }
                        }
                    }
                }
            } 
        } else {
            Inventario i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaDiaAnterior.getTime(), r);
            if (i != null) {
                inventarios.add(i);
            } else {
                    if (diaDeLaSemana == 1) {
                        fechaDiaAnterior.add(Calendar.DATE, -1);
                        i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaDiaAnterior.getTime(), r);
                        if (i != null) {
                            inventarios.add(i);
                        }
                    } else {
                        List<Compra> compasDelDia = SistemaCompras.getInstance().devolverComprasEntreFechasYReparto(fechaDiaAnterior.getTime(), fechaDiaAnterior.getTime(), r);
                        if(compasDelDia.isEmpty()) {
                            fechaDiaAnterior.add(Calendar.DATE, -1);
                            i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaDiaAnterior.getTime(), r);
                            if (i != null) {
                                inventarios.add(i);
                            } else {
                                
                            }
                        }
                    }
                }
        }
        for (Inventario i : inventarios) {
            for (InventarioRenglon ir : i.getRenglones()) {
                if (!esEnvase(ir.getArticulo()) && !esCodigoEspecial(ir.getArticulo())) {
                    if ("Excento".equals(ir.getArticulo().getIva().getNombre())) {
                        totalExcentoCompras = totalExcentoCompras + ir.getSubtotal();
                    }
                    if ("Minimo".equals(ir.getArticulo().getIva().getNombre())) {
                        totalMinimoCompras = totalMinimoCompras + ir.getSubtotal();
                    }
                    if ("Basico".equals(ir.getArticulo().getIva().getNombre())) {
                        totalBasicoCompras = totalBasicoCompras + ir.getSubtotal();
                    }
                    if (resumenPorArticulo.containsKey(ir.getArticulo())) {
                        String[] aux = (String[]) resumenPorArticulo.get(ir.getArticulo());
                        aux[0] = Double.toString(Double.parseDouble(aux[0]) + ir.getCantidad());
                        aux[3] = Double.toString(Double.parseDouble(aux[3]) + ir.getCantidad());
                        resumenPorArticulo.replace(ir.getArticulo(), aux);
                    } else {
                        if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                            cantidadArticulos++;
                            String[] aux = {Double.toString(ir.getCantidad()), "0", "0", Double.toString(ir.getCantidad())};
                            resumenPorArticulo.put(ir.getArticulo(), aux);
                        } else {
                            //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                            GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(ir.getArticulo());
                            if (ga != null) {
                                boolean encontroAlgunArticuloDelGrupo = false;
                                for (Articulo a : ga.getArticulos()) {
                                    if (resumenPorArticulo.containsKey(a)) {
                                        //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                        encontroAlgunArticuloDelGrupo = true;
                                        String[] aux = (String[]) resumenPorArticulo.get(a);
                                        aux[0] = Double.toString(Double.parseDouble(aux[0]) + ir.getCantidad());
                                        aux[3] = Double.toString(Double.parseDouble(aux[3]) + ir.getCantidad());
                                        resumenPorArticulo.replace(a, aux);
                                        break;
                                        //}
                                    }
                                }
                                if (!encontroAlgunArticuloDelGrupo) {
                                    cantidadArticulos++;
                                    String[] aux = {Double.toString(ir.getCantidad()), "0", "0", Double.toString(ir.getCantidad())};
                                    resumenPorArticulo.put(ir.getArticulo(), aux);

                                }
                            } else {
                                cantidadArticulos++;
                                String[] aux = {Double.toString(ir.getCantidad()), "0", "0", Double.toString(ir.getCantidad())};
                                resumenPorArticulo.put(ir.getArticulo(), aux);
                            }
                        }
                    }
                }
            }
        }
        inventarios = new ArrayList<>();
        //Resto el inventario del ultimo dia
        if (r == null) {
            List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
            for (Reparto rep : repartos) {
                Inventario i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaHasta, rep);
                if (i != null) {
                    inventarios.add(i);
                }
            }
        } else {
            Inventario i = SistemaLiquidaciones.getInstance().devolverInventarioParaFechaYReparto(fechaHasta, r);
            if (i != null) {
                inventarios.add(i);
            }
        }
        for (Inventario i : inventarios) {

            for (InventarioRenglon ir : i.getRenglones()) {
                if (!esEnvase(ir.getArticulo()) && !esCodigoEspecial(ir.getArticulo())) {
                    if ("Excento".equals(ir.getArticulo().getIva().getNombre())) {
                        totalExcentoCompras = totalExcentoCompras - ir.getSubtotal();
                    }
                    if ("Minimo".equals(ir.getArticulo().getIva().getNombre())) {
                        totalMinimoCompras = totalMinimoCompras - ir.getSubtotal();
                    }
                    if ("Basico".equals(ir.getArticulo().getIva().getNombre())) {
                        totalBasicoCompras = totalBasicoCompras - ir.getSubtotal();
                    }
                    if (resumenPorArticulo.containsKey(ir.getArticulo())) {
                        String[] aux = (String[]) resumenPorArticulo.get(ir.getArticulo());
                        aux[0] = Double.toString(Double.parseDouble(aux[0]) - ir.getCantidad());
                        aux[3] = Double.toString(Double.parseDouble(aux[3]) - ir.getCantidad());
                        resumenPorArticulo.replace(ir.getArticulo(), aux);
                    } else {
                        if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                            cantidadArticulos++;
                            String[] aux = {"-" + Double.toString(ir.getCantidad()), "0", "0", "-" + Double.toString(ir.getCantidad())};
                            resumenPorArticulo.put(ir.getArticulo(), aux);
                        } else {
                            //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                            GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(ir.getArticulo());
                            if (ga != null) {
                                boolean encontroAlgunArticuloDelGrupo = false;
                                for (Articulo a : ga.getArticulos()) {
                                    if (resumenPorArticulo.containsKey(a)) {
                                        //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                        encontroAlgunArticuloDelGrupo = true;
                                        String[] aux = (String[]) resumenPorArticulo.get(a);
                                        aux[0] = Double.toString(Double.parseDouble(aux[0]) - ir.getCantidad());
                                        aux[3] = Double.toString(Double.parseDouble(aux[3]) - ir.getCantidad());
                                        resumenPorArticulo.replace(a, aux);
                                        break;
                                        //}
                                    }
                                }
                                if (!encontroAlgunArticuloDelGrupo) {
                                    cantidadArticulos++;
                                    String[] aux = {"-" + Double.toString(ir.getCantidad()), "0", "0", "-" + Double.toString(ir.getCantidad())};
                                    resumenPorArticulo.put(ir.getArticulo(), aux);

                                }
                            } else {
                                cantidadArticulos++;
                                String[] aux = {"-" + Double.toString(ir.getCantidad()), "0", "0", "-" + Double.toString(ir.getCantidad())};
                                resumenPorArticulo.put(ir.getArticulo(), aux);
                            }
                        }
                    }
                }
            }
        }
        
        if (Lecheros.nombreEmpresa.equals("Giamo") || Lecheros.nombreEmpresa.equals("Relece")) {
            //Sumo los Inventarios giamo del dia anterior
            List<InventarioGiamo> inventariosGiamo = new ArrayList<>();
            Calendar fechaDiaAnteriorGiamo = Calendar.getInstance();
            fechaDiaAnteriorGiamo.setTime(fechaDesde);
            fechaDiaAnteriorGiamo.add(Calendar.DATE, -1);
            if (r == null) {
                List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
                for (Reparto rep : repartos) {
                    InventarioGiamo i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaDiaAnteriorGiamo.getTime(), rep);
                    if (i != null) {
                        inventariosGiamo.add(i);
                    } else if (diaDeLaSemana == 1) {
                        fechaDiaAnterior.add(Calendar.DATE, -1);
                        i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaDiaAnterior.getTime(), rep);
                        if (i != null) {
                            inventariosGiamo.add(i);
                        }
                    } else {
                        List<Compra> compasDelDia = SistemaCompras.getInstance().devolverComprasEntreFechasYReparto(fechaDiaAnterior.getTime(), fechaDiaAnterior.getTime(), rep);
                        if (compasDelDia.isEmpty()) {
                            fechaDiaAnterior.add(Calendar.DATE, -1);
                            i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaDiaAnterior.getTime(), rep);
                            if (i != null) {
                                inventariosGiamo.add(i);
                            } else {

                            }
                        }
                    }
                }
            } else {
                InventarioGiamo i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaDiaAnteriorGiamo.getTime(), r);
                if (i != null) {
                    inventariosGiamo.add(i);
                } else if (diaDeLaSemana == 1) {
                    fechaDiaAnterior.add(Calendar.DATE, -1);
                    i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaDiaAnterior.getTime(), r);
                    if (i != null) {
                        inventariosGiamo.add(i);
                    }
                } else {
                    List<Compra> compasDelDia = SistemaCompras.getInstance().devolverComprasEntreFechasYReparto(fechaDiaAnterior.getTime(), fechaDiaAnterior.getTime(), r);
                    if (compasDelDia.isEmpty()) {
                        fechaDiaAnterior.add(Calendar.DATE, -1);
                        i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaDiaAnterior.getTime(), r);
                        if (i != null) {
                            inventariosGiamo.add(i);
                        } else {

                        }
                    }
                }
            }
            for (InventarioGiamo i : inventariosGiamo) {
                for (InventarioGiamoRenglon ir : i.getRenglones()) {
                    if (!esEnvase(ir.getArticulo()) && !esCodigoEspecial(ir.getArticulo())) {
                        if ("Excento".equals(ir.getArticulo().getIva().getNombre())) {
                            totalExcentoCompras = totalExcentoCompras + ir.getSubtotal();
                        }
                        if ("Minimo".equals(ir.getArticulo().getIva().getNombre())) {
                            totalMinimoCompras = totalMinimoCompras + ir.getSubtotal();
                        }
                        if ("Basico".equals(ir.getArticulo().getIva().getNombre())) {
                            totalBasicoCompras = totalBasicoCompras + ir.getSubtotal();
                        }
                        if (resumenPorArticulo.containsKey(ir.getArticulo())) {
                            String[] aux = (String[]) resumenPorArticulo.get(ir.getArticulo());
                            aux[0] = Double.toString(Double.parseDouble(aux[0]) + ir.getCantidad());
                            aux[3] = Double.toString(Double.parseDouble(aux[3]) + ir.getCantidad());
                            resumenPorArticulo.replace(ir.getArticulo(), aux);
                        } else {
                            if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                cantidadArticulos++;
                                String[] aux = {Double.toString(ir.getCantidad()), "0", "0", Double.toString(ir.getCantidad())};
                                resumenPorArticulo.put(ir.getArticulo(), aux);
                            } else {
                                //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                                GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(ir.getArticulo());
                                if (ga != null) {
                                    boolean encontroAlgunArticuloDelGrupo = false;
                                    for (Articulo a : ga.getArticulos()) {
                                        if (resumenPorArticulo.containsKey(a)) {
                                            //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                            encontroAlgunArticuloDelGrupo = true;
                                            String[] aux = (String[]) resumenPorArticulo.get(a);
                                            aux[0] = Double.toString(Double.parseDouble(aux[0]) + ir.getCantidad());
                                            aux[3] = Double.toString(Double.parseDouble(aux[3]) + ir.getCantidad());
                                            resumenPorArticulo.replace(a, aux);
                                            break;
                                            //}
                                        }
                                    }
                                    if (!encontroAlgunArticuloDelGrupo) {
                                        cantidadArticulos++;
                                        String[] aux = {Double.toString(ir.getCantidad()), "0", "0", Double.toString(ir.getCantidad())};
                                        resumenPorArticulo.put(ir.getArticulo(), aux);

                                    }
                                } else {
                                    cantidadArticulos++;
                                    String[] aux = {Double.toString(ir.getCantidad()), "0", "0", Double.toString(ir.getCantidad())};
                                    resumenPorArticulo.put(ir.getArticulo(), aux);
                                }
                            }
                        }
                    }
                }
            }
            inventariosGiamo = new ArrayList<>();
            //Resto el inventario giamo del ultimo dia
            if (r == null) {
                List<Reparto> repartos = SistemaMantenimiento.getInstance().devolverRepartos();
                for (Reparto rep : repartos) {
                    InventarioGiamo i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaHasta, rep);
                    if (i != null) {
                        inventariosGiamo.add(i);
                    }
                }
            } else {
                InventarioGiamo i = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaHasta, r);
                if (i != null) {
                    inventariosGiamo.add(i);
                }
            }
            for (InventarioGiamo i : inventariosGiamo) {

                for (InventarioGiamoRenglon ir : i.getRenglones()) {
                    if (!esEnvase(ir.getArticulo()) && !esCodigoEspecial(ir.getArticulo())) {
                        if ("Excento".equals(ir.getArticulo().getIva().getNombre())) {
                            totalExcentoCompras = totalExcentoCompras - ir.getSubtotal();
                        }
                        if ("Minimo".equals(ir.getArticulo().getIva().getNombre())) {
                            totalMinimoCompras = totalMinimoCompras - ir.getSubtotal();
                        }
                        if ("Basico".equals(ir.getArticulo().getIva().getNombre())) {
                            totalBasicoCompras = totalBasicoCompras - ir.getSubtotal();
                        }
                        if (resumenPorArticulo.containsKey(ir.getArticulo())) {
                            String[] aux = (String[]) resumenPorArticulo.get(ir.getArticulo());
                            aux[0] = Double.toString(Double.parseDouble(aux[0]) - ir.getCantidad());
                            aux[3] = Double.toString(Double.parseDouble(aux[3]) - ir.getCantidad());
                            resumenPorArticulo.replace(ir.getArticulo(), aux);
                        } else {
                            if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                cantidadArticulos++;
                                String[] aux = {"-" + Double.toString(ir.getCantidad()), "0", "0", "-" + Double.toString(ir.getCantidad())};
                                resumenPorArticulo.put(ir.getArticulo(), aux);
                            } else {
                                //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                                GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(ir.getArticulo());
                                if (ga != null) {
                                    boolean encontroAlgunArticuloDelGrupo = false;
                                    for (Articulo a : ga.getArticulos()) {
                                        if (resumenPorArticulo.containsKey(a)) {
                                            //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                            encontroAlgunArticuloDelGrupo = true;
                                            String[] aux = (String[]) resumenPorArticulo.get(a);
                                            aux[0] = Double.toString(Double.parseDouble(aux[0]) - ir.getCantidad());
                                            aux[3] = Double.toString(Double.parseDouble(aux[3]) - ir.getCantidad());
                                            resumenPorArticulo.replace(a, aux);
                                            break;
                                            //}
                                        }
                                    }
                                    if (!encontroAlgunArticuloDelGrupo) {
                                        cantidadArticulos++;
                                        String[] aux = {"-" + Double.toString(ir.getCantidad()), "0", "0", "-" + Double.toString(ir.getCantidad())};
                                        resumenPorArticulo.put(ir.getArticulo(), aux);

                                    }
                                } else {
                                    cantidadArticulos++;
                                    String[] aux = {"-" + Double.toString(ir.getCantidad()), "0", "0", "-" + Double.toString(ir.getCantidad())};
                                    resumenPorArticulo.put(ir.getArticulo(), aux);
                                }
                            }
                        }
                    }
                }

            }
        }

        //Calculo los total de las facturas
        double totalExcentoFacturasManuales = 0;
        double totalMinimoFacturasManuales = 0;
        double totalBasicoFacturasManuales = 0;

        double totalExcentoFacturasProrrateo = 0;
        double totalMinimoFacturasProrrateo = 0;
        double totalBasicoFacturasProrrateo = 0;

        for (Factura f : facturas) {
            if (f.isEsManual()) {
                //Es una factura manual
                if (f.getTipoDocumento().isSuma()) {
                    for (FacturaRenglon fr : f.getRenglones()) {
                        if (!esEnvase(fr.getArticulo())) {
                            if ("Excento".equals(fr.getArticulo().getIva().getNombre())) {
                                totalExcentoFacturasManuales = totalExcentoFacturasManuales + fr.getSubtotal();
                            }
                            if ("Minimo".equals(fr.getArticulo().getIva().getNombre())) {
                                totalMinimoFacturasManuales = totalMinimoFacturasManuales + fr.getSubtotal();
                            }
                            if ("Basico".equals(fr.getArticulo().getIva().getNombre())) {
                                totalBasicoFacturasManuales = totalBasicoFacturasManuales + fr.getSubtotal();
                            }
                            if (resumenPorArticulo.containsKey(fr.getArticulo())) {
                                String[] aux = (String[]) resumenPorArticulo.get(fr.getArticulo());
                                aux[1] = Double.toString(Double.parseDouble(aux[1]) + fr.getCantidad());
                                aux[3] = Double.toString(Double.parseDouble(aux[3]) - fr.getCantidad());
                                resumenPorArticulo.replace(fr.getArticulo(), aux);
                            } else {
                                if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                    cantidadArticulos++;
                                    String[] aux = {"0", Double.toString(fr.getCantidad()), "0", "-" + fr.getCantidad()};
                                    resumenPorArticulo.put(fr.getArticulo(), aux);
                                } else {
                                    //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                                    GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(fr.getArticulo());
                                    if (ga != null) {
                                        boolean encontroAlgunArticuloDelGrupo = false;
                                        for (Articulo a : ga.getArticulos()) {
                                            if (resumenPorArticulo.containsKey(a)) {
                                                //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                                encontroAlgunArticuloDelGrupo = true;
                                                String[] aux = (String[]) resumenPorArticulo.get(a);
                                                aux[1] = Double.toString(Double.parseDouble(aux[1]) + fr.getCantidad());
                                                aux[3] = Double.toString(Double.parseDouble(aux[3]) - fr.getCantidad());
                                                resumenPorArticulo.replace(a, aux);
                                                break;
                                                //}
                                            }
                                        }
                                        if (!encontroAlgunArticuloDelGrupo) {
                                            cantidadArticulos++;
                                            String[] aux = {"0", Double.toString(fr.getCantidad()), "0", "-" + fr.getCantidad()};
                                            resumenPorArticulo.put(fr.getArticulo(), aux);

                                        }
                                    } else {
                                        cantidadArticulos++;
                                        String[] aux = {"0", Double.toString(fr.getCantidad()), "0", "-" + fr.getCantidad()};
                                        resumenPorArticulo.put(fr.getArticulo(), aux);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (FacturaRenglon fr : f.getRenglones()) {
                        if (!esEnvase(fr.getArticulo())) {
                            if ("Excento".equals(fr.getArticulo().getIva().getNombre())) {
                                totalExcentoFacturasManuales = totalExcentoFacturasManuales - fr.getSubtotal();
                            }
                            if ("Minimo".equals(fr.getArticulo().getIva().getNombre())) {
                                totalMinimoFacturasManuales = totalMinimoFacturasManuales - fr.getSubtotal();
                            }
                            if ("Basico".equals(fr.getArticulo().getIva().getNombre())) {
                                totalBasicoFacturasManuales = totalBasicoFacturasManuales - fr.getSubtotal();
                            }
                            if (resumenPorArticulo.containsKey(fr.getArticulo())) {
                                String[] aux = (String[]) resumenPorArticulo.get(fr.getArticulo());
                                aux[1] = Double.toString(Double.parseDouble(aux[1]) - fr.getCantidad());
                                aux[3] = Double.toString(Double.parseDouble(aux[3]) + fr.getCantidad());
                                resumenPorArticulo.replace(fr.getArticulo(), aux);
                            } else {
                                if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                    cantidadArticulos++;
                                    String[] aux = {"0", "-" + Double.toString(fr.getCantidad()), "0", Double.toString(fr.getCantidad())};
                                    resumenPorArticulo.put(fr.getArticulo(), aux);
                                } else {
                                    //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                                    GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(fr.getArticulo());
                                    if (ga != null) {
                                        boolean encontroAlgunArticuloDelGrupo = false;
                                        for (Articulo a : ga.getArticulos()) {
                                            if (resumenPorArticulo.containsKey(a)) {
                                                //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                                encontroAlgunArticuloDelGrupo = true;
                                                String[] aux = (String[]) resumenPorArticulo.get(a);
                                                aux[1] = Double.toString(Double.parseDouble(aux[1]) - fr.getCantidad());
                                                aux[3] = Double.toString(Double.parseDouble(aux[3]) + fr.getCantidad());
                                                resumenPorArticulo.replace(a, aux);
                                                break;
                                                //}
                                            }
                                        }
                                        if (!encontroAlgunArticuloDelGrupo) {
                                            cantidadArticulos++;
                                            String[] aux = {"0", "-" + Double.toString(fr.getCantidad()), "0", Double.toString(fr.getCantidad())};
                                            resumenPorArticulo.put(fr.getArticulo(), aux);

                                        }
                                    } else {
                                        cantidadArticulos++;
                                        String[] aux = {"0", "-" + Double.toString(fr.getCantidad()), "0", Double.toString(fr.getCantidad())};
                                        resumenPorArticulo.put(fr.getArticulo(), aux);
                                    }
                                }
                            }
                        }
                    }
                }
            } else //Es una factura de prorrateo.
             if (f.getTipoDocumento().isSuma()) {
                    for (FacturaRenglon fr : f.getRenglones()) {
                        if (!esEnvase(fr.getArticulo()) && !esCodigoEspecial(fr.getArticulo())) {
                            if ("Excento".equals(fr.getArticulo().getIva().getNombre())) {
                                totalExcentoFacturasProrrateo = totalExcentoFacturasProrrateo + fr.getSubtotal();
                            }
                            if ("Minimo".equals(fr.getArticulo().getIva().getNombre())) {
                                totalMinimoFacturasProrrateo = totalMinimoFacturasProrrateo + fr.getSubtotal();
                            }
                            if ("Basico".equals(fr.getArticulo().getIva().getNombre())) {
                                totalBasicoFacturasProrrateo = totalBasicoFacturasProrrateo + fr.getSubtotal();
                            }
                            if (resumenPorArticulo.containsKey(fr.getArticulo())) {
                                String[] aux = (String[]) resumenPorArticulo.get(fr.getArticulo());
                                aux[2] = Double.toString(Double.parseDouble(aux[2]) + fr.getCantidad());
                                aux[3] = Double.toString(Double.parseDouble(aux[3]) - fr.getCantidad());
                                resumenPorArticulo.replace(fr.getArticulo(), aux);
                            } else {
                                if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                    cantidadArticulos++;
                                    String[] aux = {"0", "0", Double.toString(fr.getCantidad()), "-" + Double.toString(fr.getCantidad())};
                                    resumenPorArticulo.put(fr.getArticulo(), aux);
                                } else {
                                    //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                                    GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(fr.getArticulo());
                                    if (ga != null) {
                                        boolean encontroAlgunArticuloDelGrupo = false;
                                        for (Articulo a : ga.getArticulos()) {
                                            if (resumenPorArticulo.containsKey(a)) {
                                                //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                                encontroAlgunArticuloDelGrupo = true;
                                                String[] aux = (String[]) resumenPorArticulo.get(a);
                                                aux[2] = Double.toString(Double.parseDouble(aux[2]) + fr.getCantidad());
                                                aux[3] = Double.toString(Double.parseDouble(aux[3]) - fr.getCantidad());
                                                resumenPorArticulo.replace(a, aux);
                                                break;
                                                //}
                                            }
                                        }
                                        if (!encontroAlgunArticuloDelGrupo) {
                                            cantidadArticulos++;
                                            String[] aux = {"0", "0", Double.toString(fr.getCantidad()), "-" + Double.toString(fr.getCantidad())};
                                            resumenPorArticulo.put(fr.getArticulo(), aux);

                                        }
                                    } else {
                                        cantidadArticulos++;
                                        String[] aux = {"0", "0", Double.toString(fr.getCantidad()), "-" + Double.toString(fr.getCantidad())};
                                        resumenPorArticulo.put(fr.getArticulo(), aux);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (FacturaRenglon fr : f.getRenglones()) {
                        if (!esEnvase(fr.getArticulo()) && !esCodigoEspecial(fr.getArticulo())) {
                            if ("Excento".equals(fr.getArticulo().getIva().getNombre())) {
                                totalExcentoFacturasProrrateo = totalExcentoFacturasProrrateo - fr.getSubtotal();
                            }
                            if ("Minimo".equals(fr.getArticulo().getIva().getNombre())) {
                                totalMinimoFacturasProrrateo = totalMinimoFacturasProrrateo - fr.getSubtotal();
                            }
                            if ("Basico".equals(fr.getArticulo().getIva().getNombre())) {
                                totalBasicoFacturasProrrateo = totalBasicoFacturasProrrateo - fr.getSubtotal();
                            }
                            if (resumenPorArticulo.containsKey(fr.getArticulo())) {
                                String[] aux = (String[]) resumenPorArticulo.get(fr.getArticulo());
                                aux[2] = Double.toString(Double.parseDouble(aux[2]) - fr.getCantidad());
                                aux[3] = Double.toString(Double.parseDouble(aux[3]) + fr.getCantidad());
                                resumenPorArticulo.replace(fr.getArticulo(), aux);
                            } else {
                                if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                    cantidadArticulos++;
                                    String[] aux = {"0", "0", "-" + Double.toString(fr.getCantidad()), Double.toString(fr.getCantidad())};
                                    resumenPorArticulo.put(fr.getArticulo(), aux);
                                } else {
                                    //Es detallada por grupo de articulo, me fijo si en el resumen hay algun producto del grupo.
                                    GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(fr.getArticulo());
                                    if (ga != null) {
                                        boolean encontroAlgunArticuloDelGrupo = false;
                                        for (Articulo a : ga.getArticulos()) {
                                            if (resumenPorArticulo.containsKey(a)) {
                                                //if(!configFact.isDetalladoPorGrupoDeArticulo()) {
                                                encontroAlgunArticuloDelGrupo = true;
                                                String[] aux = (String[]) resumenPorArticulo.get(a);
                                                aux[2] = Double.toString(Double.parseDouble(aux[2]) - fr.getCantidad());
                                                aux[3] = Double.toString(Double.parseDouble(aux[3]) + fr.getCantidad());
                                                resumenPorArticulo.replace(a, aux);
                                                break;
                                                //}
                                            }
                                        }
                                        if (!encontroAlgunArticuloDelGrupo) {
                                            cantidadArticulos++;
                                            String[] aux = {"0", "0", "-" + Double.toString(fr.getCantidad()), Double.toString(fr.getCantidad())};
                                            resumenPorArticulo.put(fr.getArticulo(), aux);

                                        }
                                    } else {
                                        cantidadArticulos++;
                                        String[] aux = {"0", "0", "-" + Double.toString(fr.getCantidad()), Double.toString(fr.getCantidad())};
                                        resumenPorArticulo.put(fr.getArticulo(), aux);
                                    }
                                }
                            }
                        }
                    }
                }
        }

        //Resto los remitos de pinchadas
        //List<Compra> remitos = SistemaLiquidaciones.getInstance().devolverRemitosPinchadasEntreFechasYReparto(fechaDesde, fechaHasta, r);
        Articulo leche = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(110);
        /*for (Compra c : remitos) {
            for (CompraRenglon cr : c.getRenglones()) {
                if (resumenPorArticulo.containsKey(leche)) {
                    String[] aux = (String[]) resumenPorArticulo.get(leche);
                    aux[0] = Double.toString(Double.parseDouble(aux[0]) - cr.getCantidad());
                    aux[2] = Double.toString(Double.parseDouble(aux[2]) - cr.getCantidad());
                    resumenPorArticulo.replace(leche, aux);
                }
            }
            totalExcentoCompras = totalExcentoCompras - c.getTotalAPrecioDeVentaSinIva();
        }*/

        Articulo lComun = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(110);

        //Resto los inau
        List<Inau> inaus = SistemaInformes.getInstance().devolverInausEntreFechasYReparto(fechaDesde, fechaHasta, r);
        for (Inau i : inaus) {
            if (resumenPorArticulo.containsKey(leche)) {
                String[] aux = (String[]) resumenPorArticulo.get(leche);
                aux[0] = Double.toString(Double.parseDouble(aux[0]) - i.getLitrosTotal());
                aux[3] = Double.toString(Double.parseDouble(aux[3]) - i.getLitrosTotal());
                resumenPorArticulo.replace(leche, aux);
            }
            Precio pLComun = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lComun, i.getHastaFecha());
            double total = i.getLitrosTotal() * pLComun.getPrecioVenta();
            totalExcentoCompras = totalExcentoCompras - total;
        }

        //Resto los anep
        List<Anep> aneps = SistemaInformes.getInstance().devolverAnepsEntreFechasYReparto(fechaDesde, fechaHasta, r);
        for (Anep a : aneps) {
            if (resumenPorArticulo.containsKey(leche)) {
                String[] aux = (String[]) resumenPorArticulo.get(leche);
                aux[0] = Double.toString(Double.parseDouble(aux[0]) - a.getLitrosTotal());
                aux[3] = Double.toString(Double.parseDouble(aux[3]) - a.getLitrosTotal());
                resumenPorArticulo.replace(leche, aux);
            }
            Precio pLComun = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lComun, a.getHastaFecha());
            double total = a.getLitrosTotal() * pLComun.getPrecioVenta();
            totalExcentoCompras = totalExcentoCompras - total;
        }

        String[][] retorno = new String[cantidadArticulos + 5][6];
        int fila = 0;
        for (Articulo a : resumenPorArticulo.keySet()) {
            retorno[fila][0] = Integer.toString(a.getCodigo());
            retorno[fila][1] = a.getDescripcion();
            String[] resumen = (String[]) resumenPorArticulo.get(a);
            retorno[fila][2] = resumen[0];
            retorno[fila][3] = resumen[1];
            retorno[fila][4] = resumen[2];
            retorno[fila][5] = resumen[3];
            fila++;
        }
        retorno[fila][0] = "Totales Manuales";
        retorno[fila][1] = Double.toString(totalExcentoFacturasManuales);
        retorno[fila][2] = Double.toString(totalMinimoFacturasManuales);
        retorno[fila][3] = Double.toString(totalBasicoFacturasManuales);

        fila++;
        retorno[fila][0] = "Totales Prorrateo";
        retorno[fila][1] = Double.toString(totalExcentoFacturasProrrateo);
        retorno[fila][2] = Double.toString(totalMinimoFacturasProrrateo);
        retorno[fila][3] = Double.toString(totalBasicoFacturasProrrateo);

        fila++;
        retorno[fila][0] = "Totales";
        retorno[fila][1] = Double.toString(totalExcentoCompras);
        retorno[fila][2] = Double.toString(totalMinimoCompras);
        retorno[fila][3] = Double.toString(totalBasicoCompras);

        fila++;
        retorno[fila][0] = "Diferencias";
        retorno[fila][1] = Double.toString(totalExcentoCompras - totalExcentoFacturasManuales - totalExcentoFacturasProrrateo);
        retorno[fila][2] = Double.toString(totalMinimoCompras - totalMinimoFacturasManuales - totalMinimoFacturasProrrateo);
        retorno[fila][3] = Double.toString(totalBasicoCompras - totalBasicoFacturasManuales - totalBasicoFacturasProrrateo);
        
        fila++;
        retorno[fila][0] = "Porcentajes";
        
        double porcentajeExcento = (((totalExcentoFacturasManuales + totalExcentoFacturasProrrateo)*100)/totalExcentoCompras);
        double porcentajeMinimo = (((totalMinimoFacturasManuales + totalMinimoFacturasProrrateo)*100)/totalMinimoCompras);
        double porcentajeBasico = (((totalBasicoFacturasManuales + totalBasicoFacturasProrrateo)*100)/totalBasicoCompras);
        
        retorno[fila][1] = Double.toString(porcentajeExcento);
        retorno[fila][2] = Double.toString(porcentajeMinimo);
        retorno[fila][3] = Double.toString(porcentajeBasico);

        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadVerificarProrrateo, "Verifico el prorrateo. ");

        return retorno;
    }

    public void sacarInformeFacturasContadora(Date desdeFecha, Date hastaFecha) throws Exception {

        //Creo el Archivo de Facturas Manuales
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT f FROM Factura f WHERE (f.fecha BETWEEN :stDate AND :edDate) and f.esManual = :man");
        consulta.setDate("stDate", desdeFecha);
        consulta.setDate("edDate", hastaFecha);
        consulta.setBoolean("man", true);
        List<Factura> facturas = consulta.list();
        session.getTransaction().commit();
        session.close();
        Workbook workbook = new XSSFWorkbook();
        Sheet hojaFacturas = workbook.createSheet("FacturasManuales");
        int rowNum = 0;
        agregarEncabezadoInformeFacturas(hojaFacturas, rowNum++);

        for (Factura f : facturas) {
            agregarFacturaInforme(hojaFacturas, rowNum++, f);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(desdeFecha);
        int mes = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        String mesS = SistemaInformes.getInstance().devolverMesString(mes);
        String nombreCapeta = "PlanillasContador" + mesS + year;
        File directory = new File(String.valueOf(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta));
        if (!directory.exists()) {
            directory.mkdir();
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta + "/" + "FacturasManualesReleceDesde" + formatter.format(desdeFecha).replace("-", "") + "Hasta" + formatter.format(hastaFecha).replace("-", "") + ".xlsx"));
            workbook.write(out);
            out.close();
        } catch (IOException ioexp) {
            int h = 0;
            System.out.println("Error");
        }

        //Creo el Archivo de Facturas Prorrateo
        session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        consulta = session.createQuery("SELECT f FROM Factura f WHERE (f.fecha BETWEEN :stDate AND :edDate) and f.esManual = :man");
        consulta.setDate("stDate", desdeFecha);
        consulta.setDate("edDate", hastaFecha);
        consulta.setBoolean("man", false);
        facturas = consulta.list();
        session.getTransaction().commit();
        session.close();
        Workbook workbookFactsProrrateo = new XSSFWorkbook();
        Sheet hojaFacturasProrrateo = workbookFactsProrrateo.createSheet("Facturas");
        rowNum = 0;
        agregarEncabezadoInformeFacturas(hojaFacturasProrrateo, rowNum++);

        for (Factura f : facturas) {
            agregarFacturaInforme(hojaFacturasProrrateo, rowNum++, f);
        }

        try {
            FileOutputStream outPro = new FileOutputStream(new File(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta + "/" + "FacturasReleceDesde" + formatter.format(desdeFecha).replace("-", "") + "Hasta" + formatter.format(hastaFecha).replace("-", "") + ".xlsx"));
            workbookFactsProrrateo.write(outPro);
            outPro.close();
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
        cell3.setCellValue("Cliente_Nombre");
        Cell cell4 = row.createCell(3);
        cell4.setCellValue("Cliente_RUT");
        Cell cell5 = row.createCell(4);
        cell5.setCellValue("Cliente_Direccion");
        Cell cell6 = row.createCell(5);
        cell6.setCellValue("sub_total");
        Cell cell7 = row.createCell(6);
        cell7.setCellValue("iva_basico");
        Cell cell8 = row.createCell(7);
        cell8.setCellValue("iva_minimo");
        Cell cell9 = row.createCell(8);
        cell9.setCellValue("total");

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
        cell3.setCellValue(f.getCliente().getNombre());
        Cell cell4 = row.createCell(3);
        cell4.setCellValue(f.getCliente().getRut());
        Cell cell5 = row.createCell(4);
        cell5.setCellValue(f.getCliente().getDireccion());
        Cell cell6 = row.createCell(5);
        cell6.setCellValue(f.getSubtotal());
        Cell cell7 = row.createCell(6);
        cell7.setCellValue(f.getTotalBasico());
        Cell cell8 = row.createCell(7);
        cell8.setCellValue(f.getTotalMinimo());
        Cell cell9 = row.createCell(8);
        cell9.setCellValue(f.getTotal());

        for (int i = 0; i <= 7; i++) {
            hoja.autoSizeColumn(i, true);
        }
    }

    public void generarInterfaceFacturasPS(Date desdeFecha, Date hastaFecha, Reparto r) throws Exception {
        List<Factura> facturas = new ArrayList<>();
        if (r == null) {

            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consulta = session.createQuery("SELECT f FROM Factura f WHERE (f.fecha BETWEEN :stDate AND :edDate) and f.esManual = :man");
            consulta.setDate("stDate", desdeFecha);
            consulta.setDate("edDate", hastaFecha);
            consulta.setBoolean("man", false);
            List<Factura> aux = consulta.list();
            session.getTransaction().commit();
            session.close();
            facturas.addAll(aux);

        } else {
            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consulta = session.createQuery("SELECT f FROM Factura f WHERE (f.fecha BETWEEN :stDate AND :edDate) and f.esManual = :man and f.reparto = :rep");
            consulta.setDate("stDate", desdeFecha);
            consulta.setDate("edDate", hastaFecha);
            consulta.setBoolean("man", true);
            consulta.setParameter("rep", r);
            facturas = consulta.list();
            session.getTransaction().commit();
            session.close();
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet hojaFacturas = workbook.createSheet("Facturas");
        int rowNum = 0;
        agregarEncabezadoInterfacePSFacturas(hojaFacturas, rowNum++);

        for (Factura f : facturas) {
            for (FacturaRenglon fr : f.getRenglones()) {
                agregarFacturaInterfacePS(hojaFacturas, rowNum++, fr);
            }
        }

        try {

            Calendar cal = Calendar.getInstance();
            cal.setTime(desdeFecha);
            int mes = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            String mesS = SistemaInformes.getInstance().devolverMesString(mes);
            String nombreCapeta = "PlanillasContador" + mesS + year;
            File directory = new File(String.valueOf(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta));
            if (!directory.exists()) {
                directory.mkdir();
            }

            FileOutputStream out = new FileOutputStream(new File(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + nombreCapeta + "/" + "InterfacePowerStreetDesde" + formatter.format(desdeFecha).replace("-", "") + "Hasta" + formatter.format(hastaFecha).replace("-", "") + ".xlsx"));
            workbook.write(out);
            out.close();
        } catch (IOException ioexp) {
            int h = 0;
            System.out.println("Error");
        }
    }

    private void agregarEncabezadoInterfacePSFacturas(Sheet hoja, int numero) throws Exception {
        Row row = hoja.createRow(numero);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue("Fecha");
        Cell cell2 = row.createCell(1);
        cell2.setCellValue("Tipo");
        Cell cell3 = row.createCell(2);
        cell3.setCellValue("nrodoc");
        Cell cell4 = row.createCell(3);
        cell4.setCellValue("vendedor");
        Cell cell5 = row.createCell(4);
        cell5.setCellValue("codigo_cliente");
        Cell cell6 = row.createCell(5);
        cell6.setCellValue("codigo_articulo");
        Cell cell7 = row.createCell(6);
        cell7.setCellValue("cantidad");
        Cell cell8 = row.createCell(7);
        cell8.setCellValue("precio_unitario");

        for (int i = 0; i <= 7; i++) {
            hoja.autoSizeColumn(i, true);
        }
    }

    private void agregarFacturaInterfacePS(Sheet hoja, int numero, FacturaRenglon fr) throws Exception {
        Row row = hoja.createRow(numero);
        Cell cell1 = row.createCell(0);
        SimpleDateFormat formatterPS = new SimpleDateFormat("dd/MM/yyyy");
        cell1.setCellValue(formatterPS.format(fr.getFactura().getFecha()));
        Cell cell2 = row.createCell(1);
        cell2.setCellValue("FCD");
        Cell cell3 = row.createCell(2);
        cell3.setCellValue(fr.getFactura().getNumero());
        Cell cell4 = row.createCell(3);
        cell4.setCellValue("0000");
        Cell cell5 = row.createCell(4);
        cell5.setCellValue(fr.getFactura().getCliente().getCodigoPS());
        Cell cell6 = row.createCell(5);
        cell6.setCellValue(fr.getArticulo().getCodigo());
        Cell cell7 = row.createCell(6);
        cell7.setCellValue(df.format(fr.getCantidad()).replace(',', '.'));
        Cell cell8 = row.createCell(7);
        cell8.setCellValue(df8decimales.format(fr.getPrecio()).replace(',', '.'));

        for (int i = 0; i <= 6; i++) {
            hoja.autoSizeColumn(i, true);
        }
    }

    public void generarInterfaceFacturasPS2(Date desdeFecha, Date hastaFecha, Reparto r) throws Exception {
        List<Factura> facturas = new ArrayList<>();
        if (r == null) {

            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consulta = session.createQuery("SELECT f FROM Factura f WHERE (f.fecha BETWEEN :stDate AND :edDate) and f.esManual = :man");
            consulta.setDate("stDate", desdeFecha);
            consulta.setDate("edDate", hastaFecha);
            consulta.setBoolean("man", false);
            List<Factura> aux = consulta.list();
            session.getTransaction().commit();
            session.close();
            facturas.addAll(aux);

        } else {
            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consulta = session.createQuery("SELECT f FROM Factura f WHERE (f.fecha BETWEEN :stDate AND :edDate) and f.esManual = :man and f.reparto = :rep");
            consulta.setDate("stDate", desdeFecha);
            consulta.setDate("edDate", hastaFecha);
            consulta.setBoolean("man", false);
            consulta.setParameter("rep", r);
            facturas = consulta.list();
            session.getTransaction().commit();
            session.close();
        }

        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getRutaInforme() + "/" + "InterfacePowerStreetDesde" + formatter.format(desdeFecha).replace("-", "") + "Hasta" + formatter.format(hastaFecha).replace("-", "") +  ".txt")), "utf-8"));

            for (Factura f : facturas) {
                for (FacturaRenglon fr : f.getRenglones()) {
                    if ("Relece".equals(Lecheros.nombreEmpresa)) {
                        if (fr.getArticulo().getCodigo() == 110 || fr.getArticulo().getCodigo() == 112 || fr.getArticulo().getCodigo() == 113 || fr.getArticulo().getCodigo() == 114 || fr.getArticulo().getCodigo() == 115 || fr.getArticulo().getCodigo() == 116 || fr.getArticulo().getCodigo() == 122 || fr.getArticulo().getCodigo() == 123 || fr.getArticulo().getCodigo() == 128 || fr.getArticulo().getCodigo() == 131 || fr.getArticulo().getCodigo() == 132 || fr.getArticulo().getCodigo() == 133 || fr.getArticulo().getCodigo() == 134 || fr.getArticulo().getCodigo() == 119 || fr.getArticulo().getCodigo() == 1337 || fr.getArticulo().getCodigo() == 1338) {
                            writer.write("" + SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getNumeroEmpresa2PS() + "	");
                        } else {
                            writer.write("" + SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getNumeroEmpresaPS() + "	");
                        }
                    } else {
                        writer.write("" + SistemaMantenimiento.getInstance().devolverConfiguracionGeneral().getNumeroEmpresaPS() + "	");
                    }
                    SimpleDateFormat formatterPS = new SimpleDateFormat("dd/MM/yyyy");
                    writer.write(formatterPS.format(fr.getFactura().getFecha()) + "	");
                    if(f.getTipoDocumento().isSuma()) {
                        writer.write("FCD" + "	");
                    } else {
                        writer.write("FDD" + "	");
                    }
                    writer.write(fr.getFactura().getNumero() + "	");
                    /*if("Relece".equals(Lecheros.nombreEmpresa)){
                        if(fr.getArticulo().getCodigo() == 110 || fr.getArticulo().getCodigo() == 112 || fr.getArticulo().getCodigo() == 113 || fr.getArticulo().getCodigo() == 114 || fr.getArticulo().getCodigo() == 115 || fr.getArticulo().getCodigo() == 116 || fr.getArticulo().getCodigo() == 122 || fr.getArticulo().getCodigo() == 123 || fr.getArticulo().getCodigo() == 128 || fr.getArticulo().getCodigo() == 131 || fr.getArticulo().getCodigo() == 132 || fr.getArticulo().getCodigo() == 133 || fr.getArticulo().getCodigo() == 134 || fr.getArticulo().getCodigo() == 119 || fr.getArticulo().getCodigo() == 1337 || fr.getArticulo().getCodigo() == 1338) {
                            writer.write("1273" + "	");
                        } else {
                            writer.write("1273" + "	");
                        }
                    }*/
                    writer.write(fr.getFactura().getReparto().getNumeroVendedorPS() + "	");
                    writer.write(fr.getFactura().getCliente().getCodigoPS() + "	");
                    writer.write(fr.getArticulo().getCodigo() + "	");
                    writer.write(df.format(fr.getCantidad()).replace(',', '.') + "	");
                    writer.write(df8decimales.format(fr.getPrecio()).replace(',', '.'));
                    //writer.write("\n");
                    String str = new String(System.getProperty("line.separator").getBytes(), StandardCharsets.UTF_8);
                    writer.write(str);
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

    public String[][] descontarPorcentaje(String[][] detalleArticulos, int porcentajeADescontar, Date fechaDesde, Date fechaHasta, Reparto r) {
        try {
            //Me creo el inventario de giamo con el porcentaje de descuento por articulo y vuelvo a llamar al metodo obtener informaion asi le recalcula los totales tambien
            InventarioGiamo ig = SistemaLiquidaciones.getInstance().devolverInventarioGiamoParaFechaYReparto(fechaHasta, r);
            boolean inventarioGiamoNuevo = false;
            if (ig == null) {
                inventarioGiamoNuevo = true;
                ig = new InventarioGiamo();
                ig.setFecha(fechaHasta);
                ig.setReparto(r);
                List<InventarioGiamoRenglon> renglones = new ArrayList<>();
                ig.setRenglones(renglones);
            }
            for (int i = 0; i < detalleArticulos.length - 3; i++) {
                // j == 0 tiene el codigo
                // j == 4 tiene la cantidad
                try {
                    int codigoArticulo = Integer.parseInt(detalleArticulos[i][0]);
                    Articulo a = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(codigoArticulo);
                    int cantidadArticulo = 0;
                    double cantidadTotalDelArticulo = Double.parseDouble(detalleArticulos[i][4]);
                    int cantidadADescontar = (int) (cantidadTotalDelArticulo * porcentajeADescontar) / 100;
                    cantidadArticulo = (int) cantidadTotalDelArticulo - (int) cantidadADescontar;

                    if (cantidadADescontar > 0) {

                        InventarioGiamoRenglon igr = new InventarioGiamoRenglon();
                        igr.setArticulo(a);
                        igr.setInventario(ig);

                        boolean existeRenglon = false;

                        if (ig.getRenglones().contains(igr)) {
                            existeRenglon = true;
                            Optional<InventarioGiamoRenglon> optigr = ig.getRenglones().stream().filter(p -> p.getArticulo().equals(a)).findFirst();
                            if (optigr.isPresent()) {
                                existeRenglon = true;
                                ig.setSubtotal(ig.getSubtotal() - optigr.get().getSubtotal());
                                if ("Excento".equals(optigr.get().getArticulo().getIva().getNombre())) {

                                }
                                if ("Minimo".equals(optigr.get().getArticulo().getIva().getNombre())) {
                                    ig.setTotalMinimo(ig.getTotalMinimo() - optigr.get().getIva());
                                }
                                if ("Basico".equals(optigr.get().getArticulo().getIva().getNombre())) {
                                    ig.setTotalBasico(ig.getTotalBasico() - optigr.get().getIva());
                                }
                                ig.setTotal(ig.getTotal() - optigr.get().getTotal());
                                igr = optigr.get();

                            }
                        }

                        Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, fechaHasta);

                        igr.setCantidad(cantidadADescontar);
                        igr.setPrecio(p.getPrecioVenta());
                        igr.setSubtotal(p.getPrecioVenta() * igr.getCantidad());
                        igr.setIva((igr.getSubtotal() * a.getIva().getPorcentaje() / 100));
                        igr.setTotal(igr.getSubtotal() + igr.getIva());

                        if (!existeRenglon) {
                            ig.getRenglones().add(igr);
                        }

                        ig.setSubtotal(ig.getSubtotal() + igr.getSubtotal());
                        if ("Excento".equals(igr.getArticulo().getIva().getNombre())) {

                        }
                        if ("Minimo".equals(igr.getArticulo().getIva().getNombre())) {
                            ig.setTotalMinimo(ig.getTotalMinimo() + igr.getIva());
                        }
                        if ("Basico".equals(igr.getArticulo().getIva().getNombre())) {
                            ig.setTotalBasico(ig.getTotalBasico() + igr.getIva());
                        }
                        ig.setTotal(ig.getTotal() + igr.getTotal());
                    }

                } catch (NumberFormatException ne) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(ne);
                    SistemaUsuarios.getInstance().registrarExcepcion(ne.toString(), stakTrace);
                }
            }

            if (inventarioGiamoNuevo) {
                GenericDAO.getGenericDAO().guardar(ig);
            } else {
                GenericDAO.getGenericDAO().actualizar(ig);
            }

            detalleArticulos = this.detalleArticulosFacturaProrrateo(fechaDesde, fechaHasta, r);
        } catch (Exception ex) {
            String stakTrace = util.Util.obtenerStackTraceEnString(ex);
            SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
        }
        return detalleArticulos;
    }
    
    public void actualizarComprasDeProductosDeClientesProrrateo(Date desdeFecha, Date hastaFecha, Reparto r) {
        try {
            List<Factura> facturas = devolverFacturasEntreFechasYReparto(desdeFecha, hastaFecha, r, true);
            List<ProductoClienteProrrateo> productosCliente;
            int cantVecesEnLaSemana = 0;
            HashMap<Articulo, int[]> cantArts = new HashMap<>();
            List<Cliente> clientesReparto = SistemaMantenimiento.getInstance().devolverClientesPorRepartoEstadoYTipo(r, true, true);
            for(Cliente c : clientesReparto) {
                cantVecesEnLaSemana = 0;
                productosCliente = c.getProductosClienteProrrateo();
                cantArts = new HashMap<>();
                List<Factura> facturasDelCliente = facturas.stream().filter(f -> f.getCliente().equals(c)).collect(Collectors.toList());
                for(Factura f: facturasDelCliente) {
                    boolean yaSumoLaCantidadGeneral = false;
                    for(FacturaRenglon fr : f.getRenglones()) {
                        if(esLeche(fr.getArticulo()) || esEnvase(fr.getArticulo())) {
                            break;
                        } else {
                            if(!yaSumoLaCantidadGeneral) {
                                cantVecesEnLaSemana++;
                                yaSumoLaCantidadGeneral = true;
                            }
                            //Es una factura de productos actualizo las cantidades.
                            if(cantArts.containsKey(fr.getArticulo())) {
                                int[] cants = cantArts.get(fr.getArticulo());
                                cants[0] = cants[0] + (int)fr.getCantidad();
                                cants[1] = cants[1]++;
                                cantArts.replace(fr.getArticulo(), cants);
                            } else {
                                int[] cants = new int[2];
                                cants[0] = (int)fr.getCantidad();
                                cants[1] = 1;
                                cantArts.put(fr.getArticulo(), cants);
                            }
                        }
                    }
                }
                //Ya recorrio todos las facturas y armo el resumen. Actualizamos la informacion del cliente.
                for(Articulo a : cantArts.keySet()) {
                    Optional<ProductoClienteProrrateo> pcp = productosCliente.stream().filter(p -> p.getArticulo().equals(a)).findFirst();
                    if(pcp.isPresent()) {
                        ProductoClienteProrrateo pclip = pcp.get();
                        int[] nuevasCantidades = cantArts.get(a);
                        String frecuencia = devolverTextoFrecuenciaPorCantidadDeVeces(nuevasCantidades[1]);
                        pclip.setFrecuencia(frecuencia);
                        int cantidad = nuevasCantidades[0]/nuevasCantidades[1];
                        GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(a);
                        if(ga != null) {
                            boolean esNumeroMultiploDeCantidadParaFacturar = cantidad % ga.getFacturarDeA() == 0;
                            while (!esNumeroMultiploDeCantidadParaFacturar) {
                                cantidad = cantidad - 1;
                                esNumeroMultiploDeCantidadParaFacturar = cantVecesEnLaSemana % ga.getFacturarDeA() == 0;
                            }
                        }
                        pclip.setCantidad(cantidad);
                        GenericDAO.getGenericDAO().actualizar(pclip);
                    } else {
                        //Es un articulo nuevo.
                        ProductoClienteProrrateo pclip = new ProductoClienteProrrateo();
                        pclip.setArticulo(a);
                        int[] nuevasCantidades = cantArts.get(a);
                        String frecuencia = devolverTextoFrecuenciaPorCantidadDeVeces(nuevasCantidades[1]);
                        pclip.setFrecuencia(frecuencia);
                        int cantidad = nuevasCantidades[0] / nuevasCantidades[1];
                        GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(a);
                        if (ga != null) {
                            boolean esNumeroMultiploDeCantidadParaFacturar = cantidad % ga.getFacturarDeA() == 0;
                            while (!esNumeroMultiploDeCantidadParaFacturar) {
                                cantidad = cantidad - 1;
                                esNumeroMultiploDeCantidadParaFacturar = cantVecesEnLaSemana % ga.getFacturarDeA() == 0;
                            }
                        }
                        pclip.setCantidad(cantidad);
                        GenericDAO.getGenericDAO().guardar(pclip);
                    }
                }
                
                
            }
        } catch (Exception ex) {
            String stakTrace = util.Util.obtenerStackTraceEnString(ex);
            SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
        }
    }
    
    public String devolverTextoFrecuenciaPorCantidadDeVeces(int cantVeces) {
        String retorno = "";
        switch(cantVeces) {
            case 1 : {retorno = "1 Vez por Semana"; break;}
            case 2 : {retorno = "2 Veces por Semana"; break;}
            case 3 : {retorno = "3 Veces por Semana"; break;}
            case 4 : {retorno = "4 Veces por Semana"; break;}
            case 5 : {retorno = "5 Veces por Semana"; break;}
            case 6 : {retorno = "6 Veces por Semana"; break;}
            case 7 : {retorno = "7 Veces por Semana"; break;}
        }
        
        return retorno;
    }
    
    public List<String[]> ingresarFacturasManualesGiamoDesdeArchivo(int desdeFilaCli, int hastaFilaCli, String rutaArchivoClientes, int desdeFilaFact, int hastaFilaFact, String rutaArchivoFacturas) {
        List<String[]> retorno = new ArrayList<>();
        try {
            //Me cargo en la memoria los clientes manuales para saber a que cliente pertenece la factura manual
            HashMap<Long, Cliente> clientesFacturaLeche = new HashMap<>();
            //HashMap<Long, Boolean> oidsClientesNoEncontrados = new HashMap<>();
            List<Long> oidsNoEncontrados = new ArrayList<>();
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(new File(rutaArchivoClientes));
            Workbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet hoja = (XSSFSheet) workbook.getSheetAt(0);
            int clientesEncontrados = 0;
            int clientesNoEncontrados = 0;
            int cantEncontradosActivo = 0;
            int cantEncontradosInactivos = 0;
            int cantClientesIngresados = 0;
            for (int i = desdeFilaCli; i <= hastaFilaCli; i++) {
                Row fila = hoja.getRow(i - 1);
                long oid = (long) fila.getCell(0).getNumericCellValue();
                String codigo = fila.getCell(1).getStringCellValue();
                Cliente cli = SistemaMantenimiento.getInstance().devolverClientePorCodigo(codigo);
                if (cli != null) {
                    clientesFacturaLeche.put(oid, cli);
                    System.out.println("Encontro el cliente : " + cli.getCodigo() + " y esta " + cli.isActivo());
                    if(cli.isActivo()) {
                        cantEncontradosActivo++;
                    } else {
                        cantEncontradosInactivos++;
                    }
                    clientesEncontrados++;
                } else {
                    clientesNoEncontrados++;
                    oidsNoEncontrados.add(oid);
                    //String codigo = fila.getCell(1).getStringCellValue();
                    //Cliente cli = SistemaMantenimiento.getInstance().devolverClientePorCodigo(codigo);
                    int numRep = (int) fila.getCell(2).getNumericCellValue();
                    String nombre = fila.getCell(3).getStringCellValue();
                    String razonSocial = "";
                    try {
                        razonSocial = fila.getCell(4).getStringCellValue();
                    } catch (NullPointerException e) {

                    }
                    String direccion = "";
                    try {
                        direccion = fila.getCell(5).getStringCellValue();
                    } catch (NullPointerException e) {

                    }
                    String rut = "";
                    String auxText = "";
                    try {
                        auxText = fila.getCell(6).getStringCellValue();
                    } catch (NullPointerException ne) {

                    }
                    if (!"".equals(auxText)) {
                        if ('-' == auxText.charAt(auxText.length() - 1)) {
                            rut = auxText.substring(0, auxText.length() - 1);
                        } else {
                            rut = auxText;
                        }
                    }

                    String telefono = "";
                    try {
                        telefono = fila.getCell(7).getStringCellValue();
                    } catch (NullPointerException e) {

                    }
                    String email = "";
                    try {
                        email = fila.getCell(8).getStringCellValue();
                    } catch (NullPointerException e) {

                    }

                    boolean activo = fila.getCell(9).getBooleanCellValue();

                    //int litrosComun = (int) fila.getCell(6).getNumericCellValue();
                    //int litrosUltra = (int) fila.getCell(7).getNumericCellValue();
                    Reparto r = SistemaMantenimiento.getInstance().devolverRepartoPorCodigo(numRep);

                    if (cli == null) {
                        SistemaMantenimiento.getInstance().agregarClienteConCodigo(codigo, false, activo, true, null, r, nombre, razonSocial, rut, direccion, "" + telefono, email, 0, 0, 0, "", 0.0, "", 0, new ArrayList<>(), "", "");
                        cantClientesIngresados++;
                        cli = SistemaMantenimiento.getInstance().devolverClientePorCodigo(codigo);
                        if (cli != null) {
                            clientesFacturaLeche.put(oid, cli);
                            System.out.println("Encontro el cliente : " + cli.getCodigo() + " y esta " + cli.isActivo());
                            if (cli.isActivo()) {
                                cantEncontradosActivo++;
                            } else {
                                cantEncontradosInactivos++;
                            }
                            clientesEncontrados++;
                        }
                    } /*else {
                        cli.setActivo(activo);
                        cli.setProrrateo(false);
                        cli.setCobraChofer(true);
                        cli.setCorreoElectronico(email);
                        cli.setReparto(r);
                        cli.setNombre(nombre);
                        cli.setRazonSocial(razonSocial);
                        cli.setDireccion(direccion);
                        cli.setRut(rut);
                        cli.setTelefono(telefono);
                        SistemaMantenimiento.getInstance().actualizarCliente(cli);

                    }*/
                }
            }

            System.out.println("Cant Encontrados: " + clientesEncontrados);
            System.out.println("Cant Encontrados Activos: " + cantEncontradosActivo);
            System.out.println("Cant Encontrados Inactivos: " + cantEncontradosInactivos);
            System.out.println("Cant No Encontrados: " + clientesNoEncontrados);
            System.out.println("Cant clientes ingresados: " + cantClientesIngresados);
            inputStream = null;
            inputStream = new FileInputStream(new File(rutaArchivoFacturas));
            workbook = new XSSFWorkbook(inputStream);
            hoja = (XSSFSheet) workbook.getSheetAt(0);
            int cantFacturas = 0;
            int cantFacturasConClienteOk = 0;
            int cantFacturasSinCliente = 0;
            int cantFacturasDeClientesNoEncontrados = 0;
            for (int i = desdeFilaFact; i <= hastaFilaFact; i++) {
                Row fila = hoja.getRow(i - 1);
                
                String[] retornoFactura = new String[3];

                long numero = (long) fila.getCell(0).getNumericCellValue();
                Date fecha = fila.getCell(1).getDateCellValue();
                long oid = (long) fila.getCell(2).getNumericCellValue();
                Cliente cli = clientesFacturaLeche.get(oid);
                
                retornoFactura[0] = formatter.format(fecha);
                retornoFactura[1] = Long.toString(numero);

                double totalLecheComun = Double.parseDouble(fila.getCell(3).getStringCellValue().replace(',', '.'));//fila.getCell(3).getNumericCellValue();
                double totalLecheUltra = Double.parseDouble(fila.getCell(4).getStringCellValue().replace(',', '.'));//fila.getCell(4).getNumericCellValue();
                double totalLecheUltraDiferenciada = Double.parseDouble(fila.getCell(5).getStringCellValue().replace(',', '.'));//fila.getCell(5).getNumericCellValue();
                double totalLecheDeslactosada = Double.parseDouble(fila.getCell(6).getStringCellValue().replace(',', '.'));//fila.getCell(6).getNumericCellValue();
                double totalProductosMinimo = Double.parseDouble(fila.getCell(7).getStringCellValue().replace(',', '.'));//fila.getCell(7).getNumericCellValue();
                double totalProductosBasico = Double.parseDouble(fila.getCell(8).getStringCellValue().replace(',', '.'));//fila.getCell(8).getNumericCellValue();

                GrupoDeArticulos grupoLecheComun = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Común");

                GrupoDeArticulos grupoLecheUtra = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra");

                GrupoDeArticulos grupoLecheUltraDiferenciada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra Diferenciada");

                GrupoDeArticulos grupoLecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Deslactosada");

                Articulo lecheComun = null;
                Precio pLecheComun = null;
                if (!grupoLecheComun.getArticulos().isEmpty()) {
                    lecheComun = grupoLecheComun.getArticulos().get(0);
                    pLecheComun = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheComun, fecha);
                }

                Articulo lecheUltra = null;
                Precio pLecheUltra = null;
                if (!grupoLecheUtra.getArticulos().isEmpty()) {
                    lecheUltra = grupoLecheUtra.getArticulos().get(0);
                    pLecheUltra = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheUltra, fecha);
                }

                Articulo lecheUltraDiferenciada = null;
                Precio pLecheUltraDiferenciada = null;
                if ((grupoLecheUltraDiferenciada != null ? !grupoLecheUltraDiferenciada.getArticulos().isEmpty() : false)) {
                    lecheUltraDiferenciada = grupoLecheUltraDiferenciada.getArticulos().get(0);
                    pLecheUltraDiferenciada = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheUltraDiferenciada, fecha);
                }

                Articulo lecheDeslactosada = null;
                Precio pLecheDeslactosada = null;
                if (!grupoLecheDeslactosada.getArticulos().isEmpty()) {
                    lecheDeslactosada = grupoLecheDeslactosada.getArticulos().get(0);
                    pLecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheDeslactosada, fecha);
                }

                Articulo prodMinimo = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(44444);
                Articulo prodBasico = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(55555);
                
                boolean todoOk = true;
                if(cli == null) {
                    todoOk = false;
                    cantFacturasSinCliente = cantFacturasSinCliente + 1;
                    cantFacturas++;
                    retornoFactura[2] = "Cliente no encontrado.";
                    retorno.add(retornoFactura);
                    if(oidsNoEncontrados.contains(oid)) {
                        cantFacturasDeClientesNoEncontrados++;
                    }
                }

                if(todoOk) {
                    Factura f = new Factura();
                    DocumentoDeVenta tipoDocVenta = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado");
                    f.setTipoDocumento(tipoDocVenta);
                    f.setFecha(fecha);
                    f.setCliente(cli);
                    f.setReparto(cli.getReparto());
                    f.setEsManual(true);
                    f.setEstaPaga(true);
                    f.setNumero(numero);

                    List<FacturaRenglon> renglones = new ArrayList<>();
                    if (totalLecheComun != 0 && lecheComun != null && pLecheComun != null) {
                        FacturaRenglon fr = new FacturaRenglon();
                        fr.setFactura(f);
                        fr.setArticulo(lecheComun);

                        int cantidad = (int) (totalLecheComun / pLecheComun.getPrecioVenta());

                        fr.setCantidad(cantidad);
                        fr.setPrecio(pLecheComun.getPrecioVenta());
                        fr.setSubtotal(totalLecheComun);
                        fr.setIva(0);
                        fr.setTotal(totalLecheComun);
                        renglones.add(fr);
                    }
                    if (totalLecheUltra != 0 && lecheUltra != null && pLecheUltra != null) {
                        FacturaRenglon fr = new FacturaRenglon();
                        fr.setFactura(f);
                        fr.setArticulo(lecheUltra);

                        int cantidad = (int) (totalLecheUltra / pLecheUltra.getPrecioVenta());

                        fr.setCantidad(cantidad);
                        fr.setPrecio(pLecheUltra.getPrecioVenta());
                        fr.setSubtotal(totalLecheUltra);
                        fr.setIva(0);
                        fr.setTotal(totalLecheUltra);
                        renglones.add(fr);
                    }
                    if (totalLecheUltraDiferenciada != 0 && lecheUltraDiferenciada != null && pLecheUltraDiferenciada != null) {
                        FacturaRenglon fr = new FacturaRenglon();
                        fr.setFactura(f);
                        fr.setArticulo(lecheUltraDiferenciada);

                        int cantidad = (int) (totalLecheUltraDiferenciada / pLecheUltraDiferenciada.getPrecioVenta());

                        fr.setCantidad(cantidad);
                        fr.setPrecio(pLecheUltraDiferenciada.getPrecioVenta());
                        fr.setSubtotal(totalLecheUltraDiferenciada);
                        fr.setIva(0);
                        fr.setTotal(totalLecheUltraDiferenciada);
                        renglones.add(fr);
                    }
                    if (totalLecheDeslactosada != 0 && lecheDeslactosada != null && pLecheDeslactosada != null) {
                        FacturaRenglon fr = new FacturaRenglon();
                        fr.setFactura(f);
                        fr.setArticulo(lecheDeslactosada);

                        int cantidad = (int) (totalLecheDeslactosada / pLecheDeslactosada.getPrecioVenta());

                        fr.setCantidad(cantidad);
                        fr.setPrecio(pLecheDeslactosada.getPrecioVenta());
                        fr.setSubtotal(totalLecheDeslactosada);
                        fr.setIva(0);
                        fr.setTotal(totalLecheDeslactosada);
                        renglones.add(fr);
                    }
                    if (totalProductosMinimo != 0) {
                        FacturaRenglon fr = new FacturaRenglon();
                        fr.setFactura(f);
                        fr.setArticulo(prodMinimo);
                        fr.setCantidad(totalProductosMinimo);
                        fr.setPrecio(1);
                        double iva = (totalProductosMinimo * 9.1) / 100;
                        fr.setIva(iva);
                        fr.setTotal(totalProductosMinimo);
                        fr.setSubtotal(totalProductosMinimo - iva);
                        renglones.add(fr);
                    }
                    if (totalProductosBasico != 0) {
                        FacturaRenglon fr = new FacturaRenglon();
                        fr.setFactura(f);
                        fr.setArticulo(prodBasico);
                        fr.setCantidad(totalProductosBasico);
                        fr.setPrecio(1);
                        double iva = (totalProductosBasico * 18.03) / 100;
                        fr.setIva(iva);
                        fr.setTotal(totalProductosBasico);
                        fr.setSubtotal(totalProductosBasico - iva);
                        renglones.add(fr);
                    }

                    f.setRenglones(renglones);

                    double totalLecheExcento = totalLecheComun + totalLecheUltra + totalLecheUltraDiferenciada + totalLecheDeslactosada;
                    double ivaMinimo = (totalProductosMinimo * 9.1) / 100;
                    double totalMinimo = totalProductosMinimo - ivaMinimo;
                    double ivaBasico = (totalProductosBasico * 18.03) / 100;
                    double totalBasico = totalProductosBasico - ivaBasico;

                    double subTotal = totalLecheExcento + totalMinimo + totalBasico;
                    double ivaTotal = ivaMinimo + ivaBasico;
                    double total = subTotal + ivaTotal;

                    f.setSubtotal(subTotal);
                    f.setTotalMinimo(ivaMinimo);
                    f.setTotalBasico(ivaBasico);
                    f.setTotal(total);
                    if (!f.getRenglones().isEmpty()) {
                        GenericDAO.getGenericDAO().guardar(f);
                        cantFacturasConClienteOk++;
                        cantFacturas++;
                        retornoFactura[2] = "Ingresada correctamente.";
                        retorno.add(retornoFactura);
                    }

                }
            }
            
            System.out.println("Cant Facturas : " + cantFacturas);
            System.out.println("Cant Facturas con Cliente Ok: " + cantFacturasConClienteOk);
            System.out.println("Cant Facturas sin Cliente: " + cantFacturasSinCliente);
            System.out.println("Cant facturas de clientes no encontrados: " + cantFacturasDeClientesNoEncontrados);

        } catch (FileNotFoundException ex) {
            //Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            //Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
