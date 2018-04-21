/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;

import dao.GenericDAO;
import dominio.Articulo;
import dominio.Reparto;
import dominio.Compra;
import dominio.CompraRenglon;
import dominio.DocumentoDeCompra;
import dominio.ObjetoPersistente;
import dominio.Precio;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lecheros.Lecheros;
import org.hibernate.Query;
import org.hibernate.Session;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class SistemaCompras {
    
    private static SistemaCompras instance;

    /**
     * @return the instance
     */
    public static SistemaCompras getInstance() {
        if(instance == null){
            instance = new SistemaCompras();
        }
        return instance;
    }
    
    private SistemaCompras(){
        
    }
    
    public boolean numeroDeCompraValido(DocumentoDeCompra dc, long numero) throws Exception {
        boolean retorno = true;
        ObjetoPersistente compra;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Compra WHERE tipoDocumento_id = " + dc.getId() + " and numero =" + numero);
        compra = (ObjetoPersistente)consulta.uniqueResult();
        if(compra != null){
            retorno = false;
        }
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public boolean guardarCompra(Compra c) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarCompras, "Guardo la compra numero: " + c.getNumero() );
        agregarFechaDeLiquidacion(c);
        return GenericDAO.getGenericDAO().guardar(c);
    }
    
    public void agregarFechaDeLiquidacion(Compra c) throws Exception {
        boolean esCodigoDeLiquidacion = true;
        Date fechaDocsQueNoVanEnLiquidacion = new Date(1900, 01, 01);
        for(CompraRenglon cr : c.getRenglones()){
            if (cr.getArticulo().getCodigo() == 170118 || cr.getArticulo().getCodigo() == 170080 || cr.getArticulo().getCodigo() == 170081 || cr.getArticulo().getCodigo() == 170082 || cr.getArticulo().getCodigo() == 170084 || cr.getArticulo().getCodigo() == 170126 || cr.getArticulo().getCodigo() == 170185) {
                esCodigoDeLiquidacion = false;
                break;
            }
        }
        if(c.getFechaLiquidacion() != null){
            if(c.getFechaLiquidacion().equals(fechaDocsQueNoVanEnLiquidacion)){
                c.setFechaLiquidacion(null); 
                esCodigoDeLiquidacion = false;
            }
        }
        if(esCodigoDeLiquidacion){
            Date fechaDeHoy  = new Date();
            c.setFechaLiquidacion(fechaDeHoy);
            
            //Si la empresa el giamo y es domingo le pongo la fecha el Domingo. 
            if(Lecheros.nombreEmpresa.equals("Giamo")) {
                Calendar fechaLiq = Calendar.getInstance();
                fechaLiq.setTime(fechaDeHoy);
                int diaDeLaSemana = fechaLiq.get(Calendar.DAY_OF_WEEK);
                if(diaDeLaSemana == 1) {
                    c.setFechaLiquidacion(c.getFecha());
                }
            }
        }
    }
    
    public List<Compra> devolverComprasPorNumero(long numero) throws Exception {
        List<Compra> retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Compra WHERE numero = " + numero);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public Compra devolverCompraPorNumeroYTipo(long numero, DocumentoDeCompra dc) throws Exception {
        Compra retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Compra WHERE numero = " + numero + " and tipoDocumento_id = " + dc.getId());
        retorno = (Compra)consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public List<Compra> devolverCompraPorNumeroYReparto(long numero, Reparto r) throws Exception {
        List<Compra> retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Compra WHERE numero = " + numero + " and reparto_id = " + r.getId());
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public List<Compra> devolverComprasEntreFechas(Date fechaDesde, Date fechaHasta) throws Exception {
        
        List<Compra> retorno = null;
        //SimpleDateFormat formatter;
        //formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaDesde);
        //cal.add(Calendar.DATE, -1);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Compra WHERE fecha BETWEEN :stDate AND :edDate" );
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public List<Compra> devolverComprasEntreFechasYTipoDeDocumento(Date fechaDesde, Date fechaHasta, DocumentoDeCompra dc) throws Exception {
        List<Compra> retorno = null;
        //SimpleDateFormat formatter;
        //formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaDesde);
        //cal.add(Calendar.DATE, -1);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Compra WHERE (fecha BETWEEN :stDate AND :edDate" + ") and tipoDocumento_id = " + dc.getId());
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public List<Compra> devolverComprasEntreFechasYReparto(Date fechaDesde, Date fechaHasta, Reparto c) throws Exception {
        List<Compra> retorno = null;
        //SimpleDateFormat formatter;
        //formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaDesde);
        //cal.add(Calendar.DATE, -1);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Compra WHERE (fecha BETWEEN :stDate AND :edDate" + ") and reparto_id = " + c.getId());
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public List<Compra> devolverComprasEntreFechasYTipoDeDocumentoYRaparto(Date fechaDesde, Date fechaHasta, DocumentoDeCompra dc, Reparto c) throws Exception {
        List<Compra> retorno = null;
        //SimpleDateFormat formatter;
        //formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Calendar cal = Calendar.getInstance();
        //cal.setTime(fechaDesde);
        //cal.add(Calendar.DATE, -1);
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Compra WHERE (fecha BETWEEN :stDate AND :edDate" + ") and tipoDocumento_id = " + dc.getId() + " and reparto_id = " + c.getId());
        consulta.setDate("stDate", fechaDesde);
        consulta.setDate("edDate", fechaHasta);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public boolean eliminarCompra(Compra c) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarCompras, "Elimino la compra numero: " + c.getNumero());
        return GenericDAO.getGenericDAO().borrar(c);
    }
    
    public boolean actualizarCompra(Compra c) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarCompras, "Modifico la compra numero: " + c.getNumero());
        return GenericDAO.getGenericDAO().actualizar(c);
    }
    
    public boolean recalcularUtilidadCompra(Compra c) throws Exception {
        if(c != null) {
            for(CompraRenglon cr : c.getRenglones()) {
                //Resto el renglon
                c.setSubtotal(c.getSubtotal() - cr.getSubtotal());
                c.setTotal(c.getTotal() - cr.getTotal());
                c.setTotalAPrecioDeVentaSinIva(c.getTotalAPrecioDeVentaSinIva() - cr.getTotalPrecioVentaSinIva());
                c.setTotalAPrecioDeVentaConIva(c.getTotalAPrecioDeVentaConIva() - cr.getTotalPrecioVentaConIva());
                if (cr.getArticulo().getIva().getId() == Constantes.ivaMinimo) {
                    c.setTotalMinimo(c.getTotalMinimo() - cr.getIva());
                }
                if (cr.getArticulo().getIva().getId() == Constantes.ivaBasico) {
                    c.setTotalBasico(c.getTotalBasico() - cr.getIva());
                }
                
                Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(cr.getArticulo(), c.getFecha());
                //double nuevaUtilidad = cr.getCantidad()*p.getPrecioVenta() - cr.getCantidad()*p.getPrecioCompra();
                cr.setPrecio(p.getPrecioCompra());
                cr.setSubtotal(p.getPrecioCompra() * cr.getCantidad());
                cr.setIva((cr.getSubtotal() * cr.getArticulo().getIva().getPorcentaje() / 100));
                cr.setTotal(cr.getSubtotal() + cr.getIva());
                cr.setTotalPrecioVentaSinIva(p.getPrecioVenta() * cr.getCantidad());
                cr.setTotalPrecioVentaConIva(cr.getTotalPrecioVentaSinIva() + ((p.getPrecioVenta() * cr.getCantidad())*cr.getArticulo().getIva().getPorcentaje()/100));
                    
                //Sumo el renglon
                c.setSubtotal(c.getSubtotal() + cr.getSubtotal());
                c.setTotal(c.getTotal() + cr.getTotal());
                c.setTotalAPrecioDeVentaSinIva(c.getTotalAPrecioDeVentaSinIva() + cr.getTotalPrecioVentaSinIva());
                c.setTotalAPrecioDeVentaConIva(c.getTotalAPrecioDeVentaConIva() + cr.getTotalPrecioVentaConIva());
                if (cr.getArticulo().getIva().getId() == Constantes.ivaMinimo) {
                    c.setTotalMinimo(c.getTotalMinimo() + cr.getIva());
                }
                if (cr.getArticulo().getIva().getId() == Constantes.ivaBasico) {
                    c.setTotalBasico(c.getTotalBasico() + cr.getIva());
                }
            }
            SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadRecalculoDeUtilidadDeCompra, "Recalculo la utilidad de la compra: " + c.getNumero());
            return GenericDAO.getGenericDAO().actualizar(c);
        }
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadRecalculoDeUtilidadDeCompra, "Recalculo la ultilidad de la compra: ");
        return false;
    }
    
    public List<String> ingresarComprasDesdeArchivo(String rutaArchivo) throws ParserConfigurationException, SAXException, IOException, ParseException, Exception{
        //En el retorno que es una lista de String cargo los codigos de los articulos a los que se le modifico el precio.
        List<String> retorno = new ArrayList<>();
        String numerosInvalidos = "";
        String codigosSinPrecio = "\n Los siguientes codigos no tenian precio, (se les agrego el precio de compra \n de la boleta y el mismo precio de venta):";
        File fXmlFile = new File(rutaArchivo);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        
        doc.getDocumentElement().normalize();
        
        NodeList nList = doc.getElementsByTagName("Sdt_FacSap");
        DecimalFormat df;
        df = new DecimalFormat("0.000");
        
        int cantDeNumeroInvalidos = 0;
        
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Compra c = new Compra();
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                String numero = eElement.getElementsByTagName("FacNro").item(0).getTextContent();
                String fecha = eElement.getElementsByTagName("FacFec").item(0).getTextContent();
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaCompra = formatter.parse(fecha);
                String cliCod = eElement.getElementsByTagName("DesMerCod").item(0).getTextContent();
                long cliCodSinCeros = Long.parseLong(cliCod);
                String tipoDoc = eElement.getElementsByTagName("TipDocDsc").item(0).getTextContent();
                DocumentoDeCompra tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNumeroDeDestinatarioyTipoDocConaprole(cliCodSinCeros, tipoDoc);
                if(tipoDocumento != null) {
                Reparto reparto = SistemaMantenimiento.getInstance().devolverCamionPorNumeroDeDestinatario(cliCodSinCeros);
                
                //Verifico el numero de compra, que sea valido, que no exista una compra con este numero.
                if(!this.numeroDeCompraValido(tipoDocumento, Long.parseLong(numero))){
                    cantDeNumeroInvalidos++;
                    if((cantDeNumeroInvalidos%4)==0){
                        numerosInvalidos = numerosInvalidos + numero + "\n";
                    } else {
                        numerosInvalidos = numerosInvalidos + numero + " , ";
                    }
                } else {
                    double subTotal = Double.parseDouble(eElement.getElementsByTagName("ImpNeto").item(0).getTextContent());
                    double total = Double.parseDouble(eElement.getElementsByTagName("ImpTotal").item(0).getTextContent());

                    double ivaMinimo = 0;
                    double ivaBasico = 0;

                    double totalPrecioDeVentaSinIva = 0;
                    double totalPrecioDeVentaConIva = 0;

                    c.setTipoDocumento(tipoDocumento);
                    c.setNumero(Long.parseLong(numero));
                    c.setFecha(fechaCompra);
                    c.setReparto(reparto);
                    //c.setReparto(reparto);

                //c.setTotalMinimo(ivaMinimo);
                    //c.setTotalBasico(ivaBasico);
                    c.setSubtotal(subTotal);
                    c.setTotal(total);
                //c.setTotalAPrecioDeVentaSinIva(totalPrecioDeVentaSinIva);
                    //c.setTotalAPrecioDeVentaConIva(totalPrecioDeVentaConIva);

                    NodeList listaRenglones = eElement.getElementsByTagName("Sdt_FacSap.Lineas");
                    for (int temp2 = 0; temp2 < listaRenglones.getLength(); temp2++) {
                        CompraRenglon cr = new CompraRenglon();
                        Node nNodeCR = listaRenglones.item(temp2);
                        if (nNodeCR.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElementCR = (Element) nNodeCR;
                            String codArt = eElementCR.getElementsByTagName("MatCod").item(0).getTextContent();
                            String cantidad = eElementCR.getElementsByTagName("MatCnt").item(0).getTextContent();
                            String ivaLetraRenglon = eElementCR.getElementsByTagName("IvaLet").item(0).getTextContent();
                            Articulo a = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(Integer.parseInt(codArt));
                            if(a == null){
                                a = new Articulo();
                                a.setCodigo(Integer.parseInt(codArt));
                                String descripcion = eElementCR.getElementsByTagName("MatDes").item(0).getTextContent();
                                a.setDescripcion(descripcion);
                                if ("B".equals(ivaLetraRenglon)) {
                                    //Es iva basico
                                    a.setIva(SistemaMantenimiento.getInstance().devovlerIvaPorNombre("Basico"));
                                }
                                if ("M".equals(ivaLetraRenglon)) {
                                    //Es iva minimo
                                    a.setIva(SistemaMantenimiento.getInstance().devovlerIvaPorNombre("Minimo"));
                                }
                                if ("E".equals(ivaLetraRenglon)) {
                                    //Es excento
                                    a.setIva(SistemaMantenimiento.getInstance().devovlerIvaPorNombre("Excento"));
                                }
                                a.setFamilia(SistemaMantenimiento.getInstance().devolverFamiliaPorCodigo(100));
                                SistemaMantenimientoArticulos.getInstance().actualizarArticulo(a);
                            }
                            //if(a.getFamilia().getCodigo() == 24 || a.getFamilia().getCodigo() == 25 || a.getFamilia().getCodigo() == 26 || a.getCodigo() == 921){
                                if(a.getCodigo() != 849 && a.getCodigo() != 835 && a.getCodigo() != 1816 && a.getCodigo() != 868 && a.getCodigo() != 921){
                                    cantidad = eElementCR.getElementsByTagName("Peso").item(0).getTextContent();
                                } 
                            //}
                            //Aca me fijo si es una boleta con codigo especial que va sin reparto.
                            if("170118".equals(codArt) || "170080".equals(codArt) || "170081".equals(codArt) || "170082".equals(codArt) || "170084".equals(codArt) || "170126".equals(codArt) || "170185".equals(codArt)  ){
                                Reparto r = SistemaMantenimiento.getInstance().devolverRepartoPorCodigo(0);
                                c.setReparto(r);
                            }
                            double ivaRenglon = 0;
                            String subTotalRenglon = eElementCR.getElementsByTagName("ImpNeto").item(0).getTextContent();
                            double totalRenglon = 0;
                            if ("B".equals(ivaLetraRenglon)) {
                                //Es iva basico
                                ivaRenglon = (Double.parseDouble(subTotalRenglon) * a.getIva().getPorcentaje()) / 100;
                                totalRenglon = Double.parseDouble(subTotalRenglon) + ivaRenglon;
                                ivaBasico = ivaBasico + ivaRenglon;
                            }
                            if ("M".equals(ivaLetraRenglon)) {
                                //Es iva minimo
                                ivaRenglon = (Double.parseDouble(subTotalRenglon) * a.getIva().getPorcentaje()) / 100;
                                totalRenglon = Double.parseDouble(subTotalRenglon) + ivaRenglon;
                                ivaMinimo = ivaMinimo + ivaRenglon;
                            }
                            if ("E".equals(ivaLetraRenglon)) {
                                //Es excento
                                ivaRenglon = 0;
                                totalRenglon = Double.parseDouble(subTotalRenglon);
                            }

                        //Hasta aca obtuve los datos del renglon de la factura.
                            //Traigo el precio del articulo. 
                            Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, fechaCompra);
                            double precioDeLaBoleta = Double.parseDouble(subTotalRenglon) / Double.parseDouble(cantidad);
                            
                            if(p == null){
                                codigosSinPrecio = codigosSinPrecio + ", " + a.getCodigo();
                                SistemaMantenimientoArticulos.getInstance().guardarPrecio(a, fechaCompra, precioDeLaBoleta, precioDeLaBoleta);
                                p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, fechaCompra);
                            }
                            
                            //Verifico el precio para los codigos especiales
                            if (a.getCodigo() == 849) {
                                double diferencia = precioDeLaBoleta - p.getPrecioCompra();
                                if(diferencia > 0.05 || diferencia < -0.05){
                                    double cant = Double.parseDouble(cantidad);
                                    cant = cant / 3;
                                    cantidad = Double.toString(cant);
                                    precioDeLaBoleta = Double.parseDouble(subTotalRenglon) / Double.parseDouble(cantidad);
                                    double diferencia2 = precioDeLaBoleta - p.getPrecioCompra();
                                    if(diferencia2 > 0.20 || diferencia2 < -0.20){
                                        //En este caso es un cambio de precio, tengo que ver bien con cual me quedo, con que cantidad.
                                        cant = Double.parseDouble(cantidad);
                                        cant = cant * 3;
                                        cantidad = Double.toString(cant);
                                        precioDeLaBoleta = Double.parseDouble(subTotalRenglon) / Double.parseDouble(cantidad);
                                    }
                                }
                            }
                            if (a.getCodigo() == 835) {
                                double diferencia = precioDeLaBoleta - p.getPrecioCompra();
                                if (diferencia > 0.05 || diferencia < -0.05) {
                                    double cant = Double.parseDouble(cantidad);
                                    cant = cant / 5;
                                    cantidad = Double.toString(cant);
                                    precioDeLaBoleta = Double.parseDouble(subTotalRenglon) / Double.parseDouble(cantidad);
                                    double diferencia2 = precioDeLaBoleta - p.getPrecioCompra();
                                    if (diferencia2 > 0.05 || diferencia2 < -0.05) {
                                        //En este caso es un cambio de precio, tengo que ver bien con cual me quedo, con que cantidad.
                                        cant = Double.parseDouble(cantidad);
                                        cant = cant * 5;
                                        cantidad = Double.toString(cant);
                                        precioDeLaBoleta = Double.parseDouble(subTotalRenglon) / Double.parseDouble(cantidad);
                                    }
                                }
                            }
                            if (a.getCodigo() == 1816) {
                                double diferencia = precioDeLaBoleta - p.getPrecioCompra();
                                if (diferencia > 0.05 || diferencia < -0.05) {
                                    double cant = Double.parseDouble(cantidad);
                                    cant = cant / 3;
                                    cantidad = Double.toString(cant);
                                    precioDeLaBoleta = Double.parseDouble(subTotalRenglon) / Double.parseDouble(cantidad);
                                    double diferencia2 = precioDeLaBoleta - p.getPrecioCompra();
                                    if (diferencia2 > 0.05 || diferencia2 < -0.05) {
                                        //En este caso es un cambio de precio, tengo que ver bien con cual me quedo, con que cantidad.
                                        cant = Double.parseDouble(cantidad);
                                        cant = cant * 3;
                                        cantidad = Double.toString(cant);
                                        precioDeLaBoleta = Double.parseDouble(subTotalRenglon) / Double.parseDouble(cantidad);
                                    }
                                }
                            }
                            if (a.getCodigo() == 921) {
                                double diferencia = precioDeLaBoleta - p.getPrecioCompra();
                                if (diferencia > 0.05 || diferencia < -0.05) {
                                    double cant = Double.parseDouble(cantidad);
                                    cant = cant / 4;
                                    cantidad = Double.toString(cant);
                                    precioDeLaBoleta = Double.parseDouble(subTotalRenglon) / Double.parseDouble(cantidad);
                                    double diferencia2 = precioDeLaBoleta - p.getPrecioCompra();
                                    if (diferencia2 > 0.05 || diferencia2 < -0.05) {
                                        //En este caso es un cambio de precio, tengo que ver bien con cual me quedo, con que cantidad.
                                        cant = Double.parseDouble(cantidad);
                                        cant = cant * 4;
                                        cantidad = Double.toString(cant);
                                        precioDeLaBoleta = Double.parseDouble(subTotalRenglon) / Double.parseDouble(cantidad);
                                    }
                                }
                            }
                            
                            if(precioDeLaBoleta != p.getPrecioCompra()){
                                if(precioDeLaBoleta>p.getPrecioCompra()){
                                    double diferencia = precioDeLaBoleta - p.getPrecioCompra();
                                    if(diferencia > 0.05){
                                        //Actualizo el precio de compra, por el nuevo precio. 
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(fechaCompra);
                                        SistemaMantenimientoArticulos.getInstance().guardarPrecio(p.getArticulo(), fechaCompra, precioDeLaBoleta, p.getPrecioVenta());
                                        retorno.add(Integer.toString(p.getArticulo().getCodigo()));
                                    }
                                } else {
                                    double diferencia =  p.getPrecioCompra() - precioDeLaBoleta;
                                    if (diferencia > 0.05) {
                                        //Actualizo el precio de compra, por el nuevo precio. 
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(fechaCompra);
                                        SistemaMantenimientoArticulos.getInstance().guardarPrecio(p.getArticulo(), fechaCompra, precioDeLaBoleta, p.getPrecioVenta());
                                        retorno.add(Integer.toString(p.getArticulo().getCodigo()));
                                    }
                                }      
                            }
                            
                            /*if (!df.format(p.getPrecioCompra()).equals(df.format(precioDeLaBoleta))) {
                                //Actualizo el precio de compra, por el nuevo precio. 
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(fechaCompra);
                                SistemaMantenimientoArticulos.getInstance().guardarPrecio(p.getArticulo(), cal, precioDeLaBoleta, p.getPrecioVenta());
                                retorno.add(Integer.toString(p.getArticulo().getCodigo()));
                            }*/

                            double totalRenglonVentaSinIva = p.getPrecioVenta() * Double.parseDouble(cantidad);
                            double totalRenglonVentaConIva =  totalRenglonVentaSinIva + ((p.getPrecioVenta()*Double.parseDouble(cantidad)) * p.getArticulo().getIva().getPorcentaje() / 100) ;

                            totalPrecioDeVentaSinIva = totalPrecioDeVentaSinIva + totalRenglonVentaSinIva;
                            totalPrecioDeVentaConIva = totalPrecioDeVentaConIva + totalRenglonVentaConIva;

                            cr.setCompra(c);
                            cr.setArticulo(a);
                            cr.setCantidad(Double.parseDouble(cantidad));
                            cr.setPrecio(precioDeLaBoleta);
                            cr.setSubtotal(Double.parseDouble(subTotalRenglon));
                            cr.setIva(ivaRenglon);
                            cr.setTotal(totalRenglon);
                            cr.setTotalPrecioVentaSinIva(totalRenglonVentaSinIva);
                            cr.setTotalPrecioVentaConIva(totalRenglonVentaConIva);
                            c.getRenglones().add(cr);

                            int para = 0;

                        }
                    }
                    c.setTotalMinimo(ivaMinimo);
                    c.setTotalBasico(ivaBasico);
                    c.setTotalAPrecioDeVentaSinIva(totalPrecioDeVentaSinIva);
                    c.setTotalAPrecioDeVentaConIva(totalPrecioDeVentaConIva);
                    
                    //Guardo la compra
                    this.guardarCompra(c);
                    }
                }
                }
        }
        if(!"".equals(numerosInvalidos)){
            retorno.add("\nLos siguientes numero de boletas no fueron ingresados por que ya existen:\n");
            retorno.add(numerosInvalidos);
            retorno.add(codigosSinPrecio);
        }
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarCompras, "Ingreso Compras desde Archivo");
        return retorno;
    }
    
    public List<String[]> verificarComprasConArchivo(String rutaArchivoCerram, String rutaArchivoRelece, Reparto rep, Date fechaLiq) throws SAXException, IOException, ParseException, Exception {
        List<String[]> retorno = new ArrayList<String[]>();
        List<String[]> retornoCerram = verificarFacturasConArchivo(rutaArchivoCerram, rep, fechaLiq);
        for(String[] s : retornoCerram) {
            if(!s[1].equals(Constantes.FACTURA_RELECE) && !s[1].equals(Constantes.NOTA_DE_CREDITO_RELECE) && !s[1].equals(Constantes.REMITO_DEVOLUCION)) {
                retorno.add(s);
            }
        }
        List<String[]> retornoRelece = verificarFacturasConArchivo(rutaArchivoRelece, rep, fechaLiq);
        for(String[] s : retornoRelece) {
            if(!s[1].equals(Constantes.FACTURA_CERRAM) && !s[1].equals(Constantes.NOTA_DE_CREDITO_CERRAM) && !s[1].equals(Constantes.FACTURA) && !s[1].equals(Constantes.NOTA_DE_CREDITO)) {
                retorno.add(s);
            }
        }
        return retorno;
    }
    
    public List<String[]> verificarFacturasConArchivo(String rutaArchivo, Reparto rep, Date fechaLiq) throws ParserConfigurationException, SAXException, IOException, ParseException, Exception {
        List<String[]> retorno = new ArrayList<String[]>();
        File fXmlFile = new File(rutaArchivo);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("Sdt_FacSap");
        DecimalFormat df;
        df = new DecimalFormat("0.000");
        
        List<Compra> comprasArchivo = new ArrayList<>();
        
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Compra c = new Compra();
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                String numero = eElement.getElementsByTagName("FacNro").item(0).getTextContent();
                String fecha = eElement.getElementsByTagName("FacFec").item(0).getTextContent();
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaCompra = formatter.parse(fecha);
                String cliCod = eElement.getElementsByTagName("DesMerCod").item(0).getTextContent();
                long cliCodSinCeros = Long.parseLong(cliCod);
                String tipoDoc = eElement.getElementsByTagName("TipDocDsc").item(0).getTextContent();
                DocumentoDeCompra tipoDocumento = SistemaMantenimiento.getInstance().devolverDocumentoDeCompraPorNumeroDeDestinatarioyTipoDocConaprole(cliCodSinCeros, tipoDoc);
                if (tipoDocumento != null) {
                    Reparto reparto = SistemaMantenimiento.getInstance().devolverCamionPorNumeroDeDestinatario(cliCodSinCeros);
                    
                    if(rep != null && reparto.equals(rep)) {
                    
                    double subTotal = Double.parseDouble(eElement.getElementsByTagName("ImpNeto").item(0).getTextContent());
                    double total = Double.parseDouble(eElement.getElementsByTagName("ImpTotal").item(0).getTextContent());

                    c.setTipoDocumento(tipoDocumento);
                    c.setNumero(Long.parseLong(numero));
                    c.setFecha(fechaCompra);
                    c.setReparto(reparto);
                    c.setSubtotal(subTotal);
                    c.setTotal(total);
                    
                    double ivaMinimo = 0;
                    double ivaBasico = 0;

                    double totalPrecioDeVentaSinIva = 0;
                    double totalPrecioDeVentaConIva = 0;
                    NodeList listaRenglones = eElement.getElementsByTagName("Sdt_FacSap.Lineas");
                    for (int temp2 = 0; temp2 < listaRenglones.getLength(); temp2++) {
                        CompraRenglon cr = new CompraRenglon();
                        Node nNodeCR = listaRenglones.item(temp2);
                        if (nNodeCR.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElementCR = (Element) nNodeCR;
                            String codArt = eElementCR.getElementsByTagName("MatCod").item(0).getTextContent();
                            String cantidad = eElementCR.getElementsByTagName("MatCnt").item(0).getTextContent();
                            String ivaLetraRenglon = eElementCR.getElementsByTagName("IvaLet").item(0).getTextContent();
                            Articulo a = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(Integer.parseInt(codArt));
                            if (a == null) {
                                /*a = new Articulo();
                                a.setCodigo(Integer.parseInt(codArt));
                                String descripcion = eElementCR.getElementsByTagName("MatDes").item(0).getTextContent();
                                a.setDescripcion(descripcion);
                                if ("B".equals(ivaLetraRenglon)) {
                                    //Es iva basico
                                    a.setIva(SistemaMantenimiento.getInstance().devovlerIvaPorNombre("Basico"));
                                }
                                if ("M".equals(ivaLetraRenglon)) {
                                    //Es iva minimo
                                    a.setIva(SistemaMantenimiento.getInstance().devovlerIvaPorNombre("Minimo"));
                                }
                                if ("E".equals(ivaLetraRenglon)) {
                                    //Es excento
                                    a.setIva(SistemaMantenimiento.getInstance().devovlerIvaPorNombre("Excento"));
                                }
                                a.setFamilia(SistemaMantenimiento.getInstance().devolverFamiliaPorCodigo(100));
                                SistemaMantenimientoArticulos.getInstance().actualizarArticulo(a);*/
                            }
                            if (a.getFamilia().getCodigo() == 24 || a.getFamilia().getCodigo() == 25 || a.getFamilia().getCodigo() == 26 || a.getCodigo() == 921) {
                                if (a.getCodigo() != 849 && a.getCodigo() != 835 && a.getCodigo() != 1816 && a.getCodigo() != 868 && a.getCodigo() != 921) {
                                    cantidad = eElementCR.getElementsByTagName("Peso").item(0).getTextContent();
                                }
                            }
                            //Aca me fijo si es una boleta con codigo especial que va sin reparto.
                            if ("170118".equals(codArt) || "170080".equals(codArt) || "170081".equals(codArt) || "170082".equals(codArt) || "170084".equals(codArt) || "170126".equals(codArt) || "170185".equals(codArt)) {
                                //Reparto r = SistemaMantenimiento.getInstance().devolverRepartoPorCodigo(0);
                                //c.setReparto(r);
                            }
                            double ivaRenglon = 0;
                            String subTotalRenglon = eElementCR.getElementsByTagName("ImpNeto").item(0).getTextContent();
                            double totalRenglon = 0;
                            if ("B".equals(ivaLetraRenglon)) {
                                //Es iva basico
                                ivaRenglon = (Double.parseDouble(subTotalRenglon) * a.getIva().getPorcentaje()) / 100;
                                totalRenglon = Double.parseDouble(subTotalRenglon) + ivaRenglon;
                                ivaBasico = ivaBasico + ivaRenglon;
                            }
                            if ("M".equals(ivaLetraRenglon)) {
                                //Es iva minimo
                                ivaRenglon = (Double.parseDouble(subTotalRenglon) * a.getIva().getPorcentaje()) / 100;
                                totalRenglon = Double.parseDouble(subTotalRenglon) + ivaRenglon;
                                ivaMinimo = ivaMinimo + ivaRenglon;
                            }
                            if ("E".equals(ivaLetraRenglon)) {
                                //Es excento
                                ivaRenglon = 0;
                                totalRenglon = Double.parseDouble(subTotalRenglon);
                            }
                            
                            Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(a, fechaCompra);
                            double precioDeLaBoleta = Double.parseDouble(subTotalRenglon) / Double.parseDouble(cantidad);

                            double totalRenglonVentaSinIva = 0;
                            double totalRenglonVentaConIva = 0;
                                        
                            if(p != null) {
                                totalRenglonVentaSinIva = p.getPrecioVenta() * Double.parseDouble(cantidad);
                                totalRenglonVentaConIva = totalRenglonVentaSinIva + ((p.getPrecioVenta() * Double.parseDouble(cantidad)) * p.getArticulo().getIva().getPorcentaje() / 100);

                                totalPrecioDeVentaSinIva = totalPrecioDeVentaSinIva + totalRenglonVentaSinIva;
                                totalPrecioDeVentaConIva = totalPrecioDeVentaConIva + totalRenglonVentaConIva;
                            }
                            
                            cr.setCompra(c);
                            cr.setArticulo(a);
                            cr.setCantidad(Double.parseDouble(cantidad));
                            cr.setPrecio(precioDeLaBoleta);
                            cr.setSubtotal(Double.parseDouble(subTotalRenglon));
                            cr.setIva(ivaRenglon);
                            cr.setTotal(totalRenglon);
                            cr.setTotalPrecioVentaSinIva(totalRenglonVentaSinIva);
                            cr.setTotalPrecioVentaConIva(totalRenglonVentaConIva);
                            c.getRenglones().add(cr);

                        }
                    }
                    
                    c.setTotalMinimo(ivaMinimo);
                    c.setTotalBasico(ivaBasico);
                    c.setTotalAPrecioDeVentaSinIva(totalPrecioDeVentaSinIva);
                    c.setTotalAPrecioDeVentaConIva(totalPrecioDeVentaConIva);
                    
                    comprasArchivo.add(c);
                    
                    }   
                }
            }
        }
        
        List<Compra> comprasDeLiquidaciones = new ArrayList<>();
        if(rep == null) {
            //Cargo las de todos lo repartos
            List<Reparto> repartos = new ArrayList<Reparto>();
            repartos = SistemaMantenimiento.getInstance().devolverRepartos();
            for(Reparto repa : repartos) {
                comprasDeLiquidaciones.addAll(SistemaLiquidaciones.getInstance().devolverComprasParaFechaYReparto(fechaLiq, repa));
            }
            //sisLiquidaciones.devolverComprasParaFechaYReparto(l.getFecha(), l.getReparto());
        } else {
            //Cargo solo las del reparto recibido por parametro
            comprasDeLiquidaciones.addAll(SistemaLiquidaciones.getInstance().devolverComprasParaFechaYReparto(fechaLiq, rep));
        }
        
        //List<Compra> deRepDeArchivo = comprasArchivo.stream().filter(c -> c.getReparto().equals(rep)).collect(Collectors.toList());
        
        retorno = compararComprasDeReparto(comprasArchivo, comprasDeLiquidaciones);
        
        return retorno;
    }
    
    public List<String[]> compararComprasDeReparto(List<Compra> comprasArchivo, List<Compra> comprasLiquidaciones) {
        List<String[]> retorno = new ArrayList<String[]>();
        
        double minimaDiferenciaAceptada = 2;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyyy");
        
        for(Compra ca : comprasArchivo) {
            Optional<Compra> compraLiq = comprasLiquidaciones.stream().filter(c -> c.getTipoDocumento().equals(ca.getTipoDocumento()) && (ca.getTotal() - c.getTotal() > 0 ? ca.getTotal() - c.getTotal() <= minimaDiferenciaAceptada : c.getTotal() - ca.getTotal() <=minimaDiferenciaAceptada)).findFirst();
            if(compraLiq.isPresent()) {
                //Encontro en las compras de la liquidacion una con el mismo tipo y precio.
                Compra comp = compraLiq.get();
                String[] resp = new String[7];
                resp[0] = formatter.format(ca.getFecha());
                resp[1] = comp.getTipoDocumento().getTipoDocumento();
                resp[2] = Long.toString(ca.getNumero());
                resp[3] = Long.toString(comp.getNumero());
                resp[4] = Double.toString(ca.getTotal());
                resp[5] = Double.toString(comp.getTotal());
                resp[6] = "Ok";
                comprasLiquidaciones.remove(comp);
                retorno.add(resp);
            } else {
                //No se encontro en la liquidacion una compra de este tipo y precio. 
                String[] resp = new String[7];
                resp[0] = formatter.format(ca.getFecha());
                resp[1] = ca.getTipoDocumento().getTipoDocumento();
                resp[2] = Long.toString(ca.getNumero());
                resp[3] = "";
                resp[4] = Double.toString(ca.getTotal());
                resp[5] = "";
                resp[6] = "La boleta se encuentra en el archivo pero no en las liquidaciones.";
                retorno.add(resp);
            }
        }
        //Por ultimo agrego las que estan en la liquidacion pero no en el archivo.
        for(Compra ca : comprasLiquidaciones) {
            String[] resp = new String[7];
            resp[0] = formatter.format(ca.getFecha());
            resp[1] = ca.getTipoDocumento().getTipoDocumento();
            resp[2] = "";
            resp[3] = Long.toString(ca.getNumero());
            resp[4] = "";
            resp[5] = Double.toString(ca.getTotal());
            resp[6] = "La boleta se encuentra en en las liquidaciones pero no en el archivo.";
            retorno.add(resp);
        }
        return retorno;
    }
}
