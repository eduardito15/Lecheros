/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;

import dao.GenericDAO;
import dominio.Camion;
import dominio.Reparto;
import dominio.Chofer;
import dominio.Cliente;
import dominio.Comision;
import dominio.ConfiguracionFacturacion;
import dominio.ConfiguracionGeneral;
import dominio.ConfiguracionLiquidacion;
import dominio.DestinatarioConaproleReparto;
import dominio.DocumentoDeCompra;
import dominio.DocumentoDeVenta;
import dominio.FamiliaDeProducto;
import dominio.GrupoCliente;
import dominio.Iva;
import dominio.ProductoClienteProrrateo;
import dominio.RepartoCompuesto;
import dominio.RubroGasto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class SistemaMantenimiento {

    private static SistemaMantenimiento instance;
    
    private Logger logger = Logger.getLogger(SistemaMantenimiento.class.getName());

    /**
     * @return the instance
     */
    public static SistemaMantenimiento getInstance() {
        if (instance == null) {
            instance = new SistemaMantenimiento();
        }
        return instance;
    }

    private SistemaMantenimiento() {
    }

    //Metodos Choferes
    public boolean agregarChofer(int codigo, String nombre, Comision c) throws Exception {

        Chofer ch = new Chofer();
        ch.setCodigo(codigo);
        ch.setNombre(nombre);
        ch.setComision(c);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoChoferes, "Ingreo el chofer  :  " + nombre );
        return GenericDAO.getGenericDAO().guardar(ch);
    }

    public List<Chofer> devolverChoferes() {
        List<Chofer> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Chofer");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarChofer(Chofer ch) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoChoferes, "Actualizo el chofer  :  " + ch.getNombre() );
        return GenericDAO.getGenericDAO().actualizar(ch);
    }

    public boolean eliminarChofer(Chofer ch) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoChoferes, "Elimino el chofer  :  " + ch.getNombre() );
        return GenericDAO.getGenericDAO().borrar(ch);
    }

    //Metodos Iva
    public boolean agregarIva(String nombre, int porcentaje) throws Exception {

        Iva iva = new Iva();
        iva.setNombre(nombre);
        iva.setPorcentaje(porcentaje);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoIvas, "Ingreo el iva  :  " + nombre );
        return GenericDAO.getGenericDAO().guardar(iva);
    }

    public Iva devovlerIvaPorNombre(String nombre) {
        Iva retorno = null;
        List<Iva> ivas = this.devolverIvas();
        for (Iva i : ivas) {
            if (i.getNombre().equals(nombre)) {
                retorno = i;
            }
        }
        return retorno;
    }

    public List<Iva> devolverIvas() {
        List<Iva> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Iva");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarIva(Iva iva) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoIvas, "Actualizo el iva  :  " + iva.getNombre() );
        return GenericDAO.getGenericDAO().actualizar(iva);
    }

    public boolean eliminarIva(Iva iva) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoIvas, "Elimino el iva  :  " + iva.getNombre() );
        return GenericDAO.getGenericDAO().borrar(iva);
    }

    //Metodos Repartos
    public boolean agregarReparto(int codigo, String nombre, int codigoPS) throws Exception {

        Reparto c = new Reparto();
        c.setCodigo(codigo);
        c.setNombre(nombre);
        c.setNumeroVendedorPS(codigoPS);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoRepartos, "Ingreso el reparto  :  " + nombre );
        return GenericDAO.getGenericDAO().guardar(c);
    }

    public List<Reparto> devolverRepartos() {
        List<Reparto> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Reparto");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarReparto(Reparto c) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoRepartos, "Actualizo el reparto  :  " + c );
        return GenericDAO.getGenericDAO().actualizar(c);
    }

    public boolean eliminarReparto(Reparto c) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoRepartos, "Elimino el reparto  :  " + c );
        return GenericDAO.getGenericDAO().borrar(c);
    }

    public Reparto devolverCamionPorNumeroDeDestinatario(long numeroDeDestinatario) {
        Reparto retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM DestinatarioConaproleReparto WHERE numeroDestinatario = " + numeroDeDestinatario);
        List<DestinatarioConaproleReparto> dests = consulta.list();
        session.getTransaction().commit();
        session.close();
        for (DestinatarioConaproleReparto dcr : dests) {
            retorno = dcr.getReparto();
        }
        return retorno;
    }

    public Reparto devolverRepartoPorCodigo(int codigo) {
        Reparto retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Reparto WHERE codigo = " + codigo);
        retorno = (Reparto) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public Reparto devolverRepartoPorNumeroVendedorPS(int numeroVandedorPS) {
        Reparto retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Reparto WHERE numeroVendedorPS = " + numeroVandedorPS);
        retorno = (Reparto) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    //Metodos Repartos Compuestos
    public boolean agregarRepartoCompuesto(int codigo, String nombre, Reparto reparto1, Reparto reparto2) throws Exception {

        RepartoCompuesto rc = new RepartoCompuesto();
        rc.setCodigo(codigo);
        rc.setNombre(nombre);
        rc.setReparto1(reparto1);
        rc.setReparto2(reparto2);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoRepartosCompuestos, "Ingreso el reparto compuesto :  " + nombre );
        return GenericDAO.getGenericDAO().guardar(rc);
    }

    public List<RepartoCompuesto> devolverRepartosCompuestos() {
        List<RepartoCompuesto> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM RepartoCompuesto");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarRepartoCompuesto(RepartoCompuesto rc) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoRepartosCompuestos, "Actualizo el reparto  compuesto:  " + rc );
        return GenericDAO.getGenericDAO().actualizar(rc);
    }

    public boolean eliminarRepartoCompuesto(RepartoCompuesto rc) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoRepartosCompuestos, "Elimino el reparto compuesto :  " + rc );
        return GenericDAO.getGenericDAO().borrar(rc);
    }

    public RepartoCompuesto devolverRepartoCompuestoPorCodigo(int codigo) {
        RepartoCompuesto retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM RepartoCompuesto WHERE codigo = " + codigo);
        retorno = (RepartoCompuesto) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    //Metodos Camiones
    public boolean agregarCamion(String matricula) throws Exception {

        Camion c = new Camion();
        c.setMatricula(matricula);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoCamiones, "Ingreo el camion  :  " + matricula );
        return GenericDAO.getGenericDAO().guardar(c);
    }

    public List<Camion> devolverCamiones() {
        List<Camion> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Camion");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarCamion(Camion c) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoCamiones, "Actualizo el camion  :  " + c );
        return GenericDAO.getGenericDAO().actualizar(c);
    }

    public boolean eliminarCamion(Camion c) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoCamiones, "Elimino el camion  :  " + c );
        return GenericDAO.getGenericDAO().borrar(c);
    }

    // Metodos Familia de Productos
    public boolean agregarFamiliaDeProductos(int codigo, String nombre) throws Exception {

        FamiliaDeProducto fp = new FamiliaDeProducto();
        fp.setCodigo(codigo);
        fp.setNombre(nombre);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoFamiliasDeProductos, "Ingreo la familia de productos  :  " + nombre );
        return GenericDAO.getGenericDAO().guardar(fp);
    }

    public FamiliaDeProducto devolverFamiliaPorCodigo(int codigo) {
        FamiliaDeProducto retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM FamiliaDeProducto where codigo = " + codigo);
        retorno = (FamiliaDeProducto) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<FamiliaDeProducto> devolverFamiliaDeProductos() {
        List<FamiliaDeProducto> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM FamiliaDeProducto");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarFamiliaDeProductos(FamiliaDeProducto fp) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoFamiliasDeProductos, "Actualizo la familia de productos  :  " + fp.getNombre() );
        return GenericDAO.getGenericDAO().actualizar(fp);
    }

    public boolean eliminarFamiliaDeProductos(FamiliaDeProducto fp) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoFamiliasDeProductos, "Elimino la familia de productos  :  " + fp.getNombre() );
        return GenericDAO.getGenericDAO().borrar(fp);
    }

    //Metodos Docuementos de Compra
    public boolean agregarDocumentoDeCompra(String tipoDoc, boolean suma, boolean esDocumentoInterno) throws Exception {

        DocumentoDeCompra dc = new DocumentoDeCompra();
        dc.setTipoDocumento(tipoDoc);
        dc.setEsDocumentoInterno(esDocumentoInterno);
        dc.setSuma(suma);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoDocumentosDeCompra, "Ingreo el documento de compra  :  " + tipoDoc );
        return GenericDAO.getGenericDAO().guardar(dc);
    }

    public List<DocumentoDeCompra> devolverDocumentosDeCompra() {
        List<DocumentoDeCompra> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM DocumentoDeCompra");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarDocumentoDeCompra(DocumentoDeCompra dc) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoDocumentosDeCompra, "Actualizo el documento de compra  :  " + dc.getTipoDocumento() );
        return GenericDAO.getGenericDAO().actualizar(dc);
    }

    public boolean eliminarDocumentoDeCompra(DocumentoDeCompra dc) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoDocumentosDeCompra, "Elimino el documento de compra  :  " + dc.getTipoDocumento() );
        return GenericDAO.getGenericDAO().borrar(dc);
    }

    public DocumentoDeCompra devolverDocumentoDeCompraPorNumeroDeDestinatarioyTipoDocConaprole(long numeroDestinatario, String tipoDocConaprole) {
        DocumentoDeCompra retorno = null;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM DestinatarioConaproleReparto WHERE numeroDestinatario = " + numeroDestinatario);
        List<DestinatarioConaproleReparto> destsDocs = consulta.list();
        session.getTransaction().commit();
        session.close();
        for (DestinatarioConaproleReparto dcr : destsDocs) {
            if (tipoDocConaprole.equals("FACTURA")) {
                if (dcr.getTipoDocumento().isSuma()) {
                    retorno = dcr.getTipoDocumento();
                }
            }
            if (tipoDocConaprole.equals("NOTA DE CREDITO")) {
                if (!dcr.getTipoDocumento().isSuma()) {
                    retorno = dcr.getTipoDocumento();
                }
            }
        }
        return retorno;
    }

    public DocumentoDeCompra devolverDocumentoDeCompraPorNombre(String nombre) {
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT dc FROM DocumentoDeCompra dc WHERE dc.tipoDocumento = :td");
        consulta.setParameter("td", nombre);
        DocumentoDeCompra retorno = (DocumentoDeCompra) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    //Metodos Documentos de Venta
    public boolean agregarDocumentoDeVenta(String tipoDoc, boolean suma) throws Exception {

        DocumentoDeVenta dv = new DocumentoDeVenta();
        dv.setTipoDocumento(tipoDoc);
        dv.setSuma(suma);
        dv.setActivo(true);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoDocumentosDeVenta, "Ingreo el documento de venta  :  " + tipoDoc );
        return GenericDAO.getGenericDAO().guardar(dv);
    }

    public List<DocumentoDeVenta> devolverDocumentosDeVenta() {
        List<DocumentoDeVenta> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM DocumentoDeVenta");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public DocumentoDeVenta devolverDocumentoDeVentaPorNombre(String nombre) {
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("SELECT dv FROM DocumentoDeVenta dv WHERE dv.tipoDocumento = :td");
        consulta.setParameter("td", nombre);
        DocumentoDeVenta retorno = (DocumentoDeVenta) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarDocumentoDeVenta(DocumentoDeVenta dv) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoDocumentosDeVenta, "Actualizo el documento de venta  :  " + dv.getTipoDocumento() );
        return GenericDAO.getGenericDAO().actualizar(dv);
    }

    public boolean eliminarDocumentoDeVenta(DocumentoDeVenta dv) throws Exception{
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoDocumentosDeVenta, "Elimino el documento de venta :  " + dv.getTipoDocumento() );
        return GenericDAO.getGenericDAO().borrar(dv);
    }

    //Metodos DestinatarioConaprole - Reparto
    public boolean agregarDestConRep(long dest, DocumentoDeCompra dc, Reparto rep) throws Exception {

        DestinatarioConaproleReparto dcr = new DestinatarioConaproleReparto();
        dcr.setNumeroDestinatario(dest);
        dcr.setReparto(rep);
        dcr.setTipoDocumento(dc);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoDestinatariosConaprole, "Ingreo el destinatario conparole  :  " + dest + " para el reparto: " + rep );
        return GenericDAO.getGenericDAO().guardar(dcr);
    }

    public List<DestinatarioConaproleReparto> devolverDestConRep() {
        List<DestinatarioConaproleReparto> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM DestinatarioConaproleReparto");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarDestConRep(DestinatarioConaproleReparto dcr) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoDestinatariosConaprole, "Actualizo el destinatario conaprole  :  " + dcr.toString() );
        return GenericDAO.getGenericDAO().actualizar(dcr);
    }

    public boolean eliminarDestConRep(DestinatarioConaproleReparto dcr) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoDestinatariosConaprole, "Eliminao el destinatario conaprole  :  " + dcr.toString() );
        return GenericDAO.getGenericDAO().borrar(dcr);
    }

    public boolean numeroDeDestinatarioValido(long numero, Reparto reparto, DocumentoDeCompra dc) throws Exception {
        boolean retorno = true;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM DestinatarioConaproleReparto WHERE numeroDestinatario = " + numero + " and reparto_id = " + reparto.getId() + " and tipoDocumento_id = " + dc.getId());
        DestinatarioConaproleReparto dcr = (DestinatarioConaproleReparto) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        if (dcr != null) {
            retorno = false;
        }
        return retorno;
    }

    //Metodos Rubros de Gastos
    public boolean agregarRubroGasto(int codigo, String nombre) throws Exception {

        RubroGasto rg = new RubroGasto();
        rg.setCodigo(codigo);
        rg.setNombre(nombre);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoRubroGasto, "Ingreo el rubro gasto  :  " + nombre );
        return GenericDAO.getGenericDAO().guardar(rg);
    }

    public RubroGasto devolverRubroGastoPorCodigo(int codigo) {
        RubroGasto retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM RubroGasto where codigo = " + codigo);
        retorno = (RubroGasto) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<RubroGasto> devolverRubrosGastos() {
        List<RubroGasto> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM RubroGasto");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarRubroGasto(RubroGasto rg) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoRubroGasto, "Actualizo el rubro gasto  :  " + rg.getNombre() );
        return GenericDAO.getGenericDAO().actualizar(rg);
    }

    public boolean eliminarRubroGasto(RubroGasto rg) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoRubroGasto, "Elimino el rubro chofer  :  " + rg.getNombre() );
        return GenericDAO.getGenericDAO().borrar(rg);
    }

    //Metodos Relacionados con Clientes
    public boolean agregarCliente(boolean prorrateo, boolean activo, boolean cobraChofer, GrupoCliente gc, Reparto rep, String nombre, String razonSocial, String rut, String direccion, String telefono, String email, int litrosComun, int litrosUltra, int litrosDeslactosada, String frecFactLeche, double productos, String frecFactProds, long codigoPS, int sucursalPS, List<ProductoClienteProrrateo> prodsCli, String frecFactLecheUltra, String frecFactLecheDeslactosada) throws Exception {
        //Primero guardo los productos del cliente
        if(prodsCli != null) {
            for(ProductoClienteProrrateo pcp : prodsCli) {
                GenericDAO.getGenericDAO().guardar(pcp);
            }
        }
        Cliente c = new Cliente();
        c.setProrrateo(prorrateo);
        c.setActivo(activo);
        c.setCobraChofer(cobraChofer);
        c.setCodigoPS(codigoPS);
        c.setSucursalPS(sucursalPS);
        c.setReparto(rep);
        c.setNombre(nombre);
        c.setRazonSocial(razonSocial);
        c.setRut(rut);
        c.setDireccion(direccion);
        c.setTelefono(telefono);
        c.setCorreoElectronico(email);
        c.setLitrosComun(litrosComun);
        c.setLitrosUltra(litrosUltra);
        c.setLitrosDeslactosada(litrosDeslactosada);
        c.setFrecuenciaFacturacionLeche(frecFactLeche);
        c.setFrecuenciaFacturacionProductos(frecFactProds);
        c.setProductos(productos);
        c.setProductosClienteProrrateo(prodsCli);
        c.setFrecuenciaFacturacionLecheUltra(frecFactLecheUltra);
        c.setFrecuenciaFacturacionLecheDeslactosada(frecFactLecheDeslactosada);
        String codigo = devolverCodigoClienteParaReparto(rep);
        if (codigoPS == 0) {
            String[] aux = codigo.split("\\.");
            String codPS = "";
            for (int i = 0; i < aux.length; i++) {
                codPS = codPS + aux[i];
            }
            c.setCodigoPS(Long.parseLong(codPS));
        }
        c.setCodigo(codigo);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarCliente, "Ingreo el cliente  :  " + c );
        return GenericDAO.getGenericDAO().guardar(c);
    }
    
    //Metodos Relacionados con Clientes
    public boolean agregarClienteConCodigo(String codigo,boolean prorrateo, boolean activo, boolean cobraChofer, GrupoCliente gc, Reparto rep, String nombre, String razonSocial, String rut, String direccion, String telefono, String email, int litrosComun, int litrosUltra, int litrosDeslactosada, String frecFactLeche, double productos, String frecFactProds, long codigoPS, List<ProductoClienteProrrateo> prodsCli, String frecFactLecheUltra, String frecFactLecheDeslactosada) throws Exception {
        //Primero guardo los productos del cliente
        if(prodsCli != null) {
            for(ProductoClienteProrrateo pcp : prodsCli) {
                GenericDAO.getGenericDAO().guardar(pcp);
            }
        }
        Cliente c = new Cliente();
        c.setProrrateo(prorrateo);
        c.setActivo(activo);
        c.setCobraChofer(cobraChofer);
        c.setCodigoPS(codigoPS);
        c.setReparto(rep);
        c.setNombre(nombre);
        c.setRazonSocial(razonSocial);
        c.setRut(rut);
        c.setDireccion(direccion);
        c.setTelefono(telefono);
        c.setCorreoElectronico(email);
        c.setLitrosComun(litrosComun);
        c.setLitrosUltra(litrosUltra);
        c.setLitrosDeslactosada(litrosDeslactosada);
        c.setFrecuenciaFacturacionLeche(frecFactLeche);
        c.setFrecuenciaFacturacionProductos(frecFactProds);
        c.setProductos(productos);
        c.setProductosClienteProrrateo(prodsCli);
        c.setFrecuenciaFacturacionLecheUltra(frecFactLecheUltra);
        c.setFrecuenciaFacturacionLecheDeslactosada(frecFactLecheDeslactosada);
        if (codigoPS == 0) {
            String[] aux = codigo.split("\\.");
            String codPS = "";
            for (int i = 0; i < aux.length; i++) {
                codPS = codPS + aux[i];
            }
            c.setCodigoPS(Long.parseLong(codPS));
        }
        c.setCodigo(codigo);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadIngresarCliente, "Ingreo el cliente  :  " + c );
        return GenericDAO.getGenericDAO().guardar(c);
    }

    private String devolverCodigoClienteParaReparto(Reparto r) {
        String retorno = r.getCodigo() + ".";
        int ultimoCodigo = 1;
        List<Cliente> clientesReparto = devolverClientesParaReparto(r);
        for (Cliente c : clientesReparto) {
            String cod = c.getCodigo();
            String[] separado = cod.split("\\.");
            if (Integer.parseInt(separado[1]) > ultimoCodigo) {
                ultimoCodigo = Integer.parseInt(separado[1]);
            }
        }
        retorno = retorno + (ultimoCodigo + 1);
        return retorno;
    }

    public List<Cliente> devolverClientes() {
        List<Cliente> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cliente");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Cliente> devolverClientesActivos()  {
        List<Cliente> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cliente where activo = :act ");
        consulta.setBoolean("act", true);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Cliente> devolverClientesParaReparto(Reparto r) {
        List<Cliente> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cliente where reparto_id = " + r.getId());
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarCliente(Cliente c) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadModEliCli, "Actualizo el cliente  :  " + c );
        return GenericDAO.getGenericDAO().actualizar(c);
    }

    public boolean eliminarCliente(Cliente c) throws Exception {
        c.setActivo(false);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadModEliCli, "Elimino el cliente  :  " + c );
        return GenericDAO.getGenericDAO().actualizar(c);
    }

    public List<Cliente> devolverClientesPorRepartoYEstado(Reparto r, boolean estado) {
        List<Cliente> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cliente where reparto_id = " + r.getId() + " and activo = :est");
        consulta.setBoolean("est", estado);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Cliente> devolverClientesPorRepartoEstadoYNombre(Reparto r, boolean estado, String nombre) {
        List<Cliente> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cliente where reparto_id = " + r.getId() + " and activo = :est and (nombre LIKE :fracNombre or razonSocial LIKE :fracNombre)");
        consulta.setBoolean("est", estado);
        consulta.setParameter("fracNombre", "%" + nombre + "%");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Cliente> devolverClientesPorRepartoEstadoYTipo(Reparto r, boolean estado, boolean prorrateo) {
        List<Cliente> retorno = new ArrayList<>();
        if (r == null) {
            List<Reparto> repartos = this.devolverRepartos();
            for(Reparto rep: repartos){
                List<Cliente> aux;
                Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
                session.beginTransaction();
                Query consulta = session.createQuery("FROM Cliente where reparto_id = " + rep.getId() + " and activo = :est and prorrateo = :tipo");
                consulta.setBoolean("est", estado);
                consulta.setBoolean("tipo", prorrateo);
                aux = consulta.list();
                retorno.addAll(aux);
                session.getTransaction().commit();
                session.close();
            }
            return retorno;
        } else {
            Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
            session.beginTransaction();
            Query consulta = session.createQuery("FROM Cliente where reparto_id = " + r.getId() + " and activo = :est and prorrateo = :tipo");
            consulta.setBoolean("est", estado);
            consulta.setBoolean("tipo", prorrateo);
            retorno = consulta.list();
            session.getTransaction().commit();
            session.close();
            return retorno;
        }
    }

    public List<Cliente> devolverClientesPorRepartoEstadoTipoYNombre(Reparto r, boolean estado, boolean prorrateo, String nombre) {
        List<Cliente> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cliente where reparto_id = " + r.getId() + " and activo = :est and prorrateo = :tipo and (nombre LIKE :fracNombre or razonSocial LIKE :fracNombre)");
        consulta.setBoolean("est", estado);
        consulta.setBoolean("tipo", prorrateo);
        consulta.setParameter("fracNombre", "%" + nombre + "%");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Cliente> devolverClientesPorEstadoTipoYNombre(boolean estado, boolean prorrateo, String nombre) {
        List<Cliente> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cliente where activo = :est and prorrateo = :tipo and (nombre LIKE :fracNombre or razonSocial LIKE :fracNombre)");
        consulta.setBoolean("est", estado);
        consulta.setBoolean("tipo", prorrateo);
        consulta.setParameter("fracNombre", "%" + nombre + "%");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public Cliente devolverClientePorCodigo(String codigo) {
        Cliente retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cliente where codigo = :cod");
        consulta.setString("cod", codigo);
        retorno = (Cliente) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public Cliente devolverClienteActivoPorCodigo(String codigo) {
        Cliente retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cliente where codigo = " + codigo + "AND activo = :est");
        consulta.setBoolean("est", true);
        retorno = (Cliente) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public Cliente devolverClienteActivoPorCodigoPS(long codigoPS) {
        Cliente retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cliente where codigoPS = " + codigoPS + "AND activo = :est");
        consulta.setBoolean("est", true);
        retorno = (Cliente) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }
    
    public Cliente devolverClienteActivoPorCodigoYSucursalPS(long codigoPS, int sucursalPS) {
        Cliente retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cliente where codigoPS = " + codigoPS + " AND sucursalPS = " + sucursalPS +  " AND activo = :est");
        consulta.setBoolean("est", true);
        retorno = (Cliente) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public Cliente devolverClientePorFraccionDelRut(String fraccionRut) {
        Cliente retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cliente where rut LIKE :fraccionBusqueda and activo = :act and prorrateo = :tipo");
        consulta.setParameter("fraccionBusqueda", "%" + fraccionRut + "%");
        consulta.setBoolean("act", true);
        consulta.setBoolean("tipo", false);
        retorno = (Cliente) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public List<Cliente> devolverClientePorCodigoPS(long codigoPs) {
        List<Cliente> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Cliente where codigoPS = :cPS");
        consulta.setLong("cPS", codigoPs);
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    //Metodos de Productos de clientes de prorrateo.
    //Metodos Grupos de Clientes
    public boolean agregarGrupoCliente(String nombre) throws Exception {

        GrupoCliente gc = new GrupoCliente();
        gc.setNombre(nombre);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoGrupoDeClientes, "Ingreo el grupo de cliente  :  " + nombre);
        return GenericDAO.getGenericDAO().guardar(gc);

    }

    public List<GrupoCliente> devolverGrupoCliente() {
        List<GrupoCliente> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM GrupoCliente");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarGrupoCliente(GrupoCliente gc) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoGrupoDeClientes, "Actualizo el grupo de cliente  :  " + gc );
        return GenericDAO.getGenericDAO().actualizar(gc);
    }

    public boolean eliminarGrupoCliente(GrupoCliente gc) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoGrupoDeClientes, "Elimino el grupo de cliente  :  " + gc );
        return GenericDAO.getGenericDAO().borrar(gc);
    }

    //Configuracion de la liquidacion
    public boolean guardarConfiguracionDeLiquidacion(ConfiguracionLiquidacion cl) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadConfiguracionLiquidaciones, "Guardo la configuracion de liquidacion " );
        return GenericDAO.getGenericDAO().actualizar(cl);
    }

    public ConfiguracionLiquidacion devolverConfiguracionLiquidacion() {
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM ConfiguracionLiquidacion");
        //List<ConfiguracionLiquidacion> configs = consulta.list();
        //ConfiguracionLiquidacion cl = configs.get(0);
        ConfiguracionLiquidacion cl = (ConfiguracionLiquidacion) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        if (cl == null) {
            return new ConfiguracionLiquidacion();
        } else {
            return cl;
        }
    }

    //Configuracion General
    public boolean guardarConfiguracionGeneral(ConfiguracionGeneral cg) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadConfiguracionGeneral, "Guardo la configuracion general." );
        return GenericDAO.getGenericDAO().actualizar(cg);
    }

    public ConfiguracionGeneral devolverConfiguracionGeneral() {
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM ConfiguracionGeneral");
        //List<ConfiguracionLiquidacion> configs = consulta.list();
        //ConfiguracionLiquidacion cl = configs.get(0);
        ConfiguracionGeneral cg = (ConfiguracionGeneral) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        if (cg == null) {
            return new ConfiguracionGeneral();
        } else {
            return cg;
        }
    }

    //Configuracion de la facturacion
    public boolean guardarConfiguracionDeFacturacion(ConfiguracionFacturacion cf) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadConfiguracionFacturacion, "Guardo la configuracion de facturacion." );
        return GenericDAO.getGenericDAO().actualizar(cf);
    }

    public ConfiguracionFacturacion devolverConfiguracionFacturacion() {
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM ConfiguracionFacturacion");
        //List<ConfiguracionLiquidacion> configs = consulta.list();
        //ConfiguracionLiquidacion cl = configs.get(0);
        ConfiguracionFacturacion cf = (ConfiguracionFacturacion) consulta.uniqueResult();
        session.getTransaction().commit();
        session.close();
        if (cf == null) {
            return new ConfiguracionFacturacion();
        } else {
            return cf;
        }
    }

    //Metodos Comision
    public boolean agregarComision(String nombre, double porcentaje, double porcentajeLeche) throws Exception {

        Comision c = new Comision();
        c.setNombre(nombre);
        c.setPorcentaje(porcentaje);
        c.setPorcentajeLeche(porcentajeLeche);
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoComisiones, "Ingreo la comision  :  " + nombre );
        return GenericDAO.getGenericDAO().guardar(c);
    }

    public Comision devovlerComisionPorNombre(String nombre) throws Exception {
        Comision retorno = null;
        List<Comision> comisiones = this.devolverComisiones();
        for (Comision c : comisiones) {
            if (c.getNombre().equals(nombre)) {
                retorno = c;
            }
        }
        return retorno;
    }

    public List<Comision> devolverComisiones() {
        List<Comision> retorno;
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Comision");
        retorno = consulta.list();
        session.getTransaction().commit();
        session.close();
        return retorno;
    }

    public boolean actualizarComision(Comision c) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoComisiones, "Actualizo la comision  :  " + c.getNombre() );
        return GenericDAO.getGenericDAO().actualizar(c);
    }

    public boolean eliminarComision(Comision c) throws Exception {
        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoComisiones, "Elimino la comision  :  " + c.getNombre() );
        return GenericDAO.getGenericDAO().borrar(c);
    }
    
    public List<String[]> ingresarClientesDesdePowerStreet(String rutaArchivo) throws Exception {
        List<String[]> retorno = new ArrayList<>();
        BufferedReader br = null;
        String line;
        String cvsSplitBy = "\\t";
        
        File fileDir = new File(rutaArchivo);

        int totalClientesIngresados = 0;

        try {

            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "ISO-8859-1"));
            //Leo la linea de los encabezados.
            line = br.readLine();
            logger.info("-------------------------------INICIO INGRESO DE CLIENTES--------------------------------------------");
            while ((line = br.readLine()) != null) {
                String[] retornoPorFactura = new String[6];

                String[] data = line.split(cvsSplitBy);
                
                String Vendedor = data[0];
                //String Ruta = data[1];
                //String orden = data[2];
                //String UltimaVenta = data[3];
                //String UltimaVisita = data[4];
                String Cuenta = data[5];
                String codAlt = data[6];
                String Nombre = data[7];
                String Razon = data[8];
                String Direccion = "";
                String Rut = "";
                
                if(data.length >= 10) {
                    Direccion = data[9];
                }
                if(data.length >=11) {
                    Rut = data[10];
                }
                
                retornoPorFactura[0] = Cuenta;
                retornoPorFactura[1] = codAlt;
                retornoPorFactura[2] = Nombre;
                retornoPorFactura[3] = Razon;
                retornoPorFactura[4] = Direccion;
                retornoPorFactura[5] = "";
                
           
                Reparto r = null;

                try {
                    String[] primerSplit = Vendedor.split(": ");
                    if(primerSplit.length >= 2) {
                        String[] segundoSplit = primerSplit[1].split(" -");
                        int numeroVendedorPS = Integer.parseInt(segundoSplit[0]);
                        r = SistemaMantenimiento.getInstance().devolverRepartoPorNumeroVendedorPS(numeroVendedorPS);
                    }
                    if (r == null) {
                        retornoPorFactura[5] = "No existe el vendedor con ese número.";
                        //throw new Exception(retornoPorFactura[5]);
                    }
                } catch (NumberFormatException ne) {
                    retornoPorFactura[5] = "El número de vendedor debe ser un número.";
                    //throw new Exception(retornoPorFactura[5]);
                }
                
                int codigoAlternativo = 0;
                int codigoSuc = 1;
                
                try{
                    String[] splitCodigo = Cuenta.split("-");
                    if(splitCodigo.length == 2) {
                        //codigoAlternativo = Integer.parseInt(splitCodigo[0]);
                        codigoSuc = Integer.parseInt(splitCodigo[1]);
                    }
                    
                } catch (NumberFormatException ne) {
                    retornoPorFactura[5] = "El codigo debe ser número un guion y un número.";
                    //throw new Exception(retornoPorFactura[5]);
                }
                
                
                try {
                    codigoAlternativo = Integer.parseInt(codAlt);
                } catch (NumberFormatException ne) {
                    retornoPorFactura[5] = "El codigo altenativo debe ser un número.";
                    //throw new Exception(retornoPorFactura[5]);
                }
                
                Cliente aux = SistemaMantenimiento.getInstance().devolverClienteActivoPorCodigoYSucursalPS(codigoAlternativo, codigoSuc);
                if(aux == null) {
                    //No existe el cliente.
                    //Si no existe lo agrego.
                    if(retornoPorFactura[5].equals("")) {
                        if(SistemaMantenimiento.getInstance().agregarCliente(false, true, true, null, r, Nombre, Razon, Rut, Direccion, "", "", 0, 0, 0, "", 0, "", codigoAlternativo, codigoSuc, null, "", "")) {
                            totalClientesIngresados++;
                            retornoPorFactura[5] = "Ingresado correctamente.";
                        }
                    }
                } else {
                    //Es un cliente que ya existe.
                    retornoPorFactura[5] = "Ya existe un cliente con ese codigo y sucursal.";
                }
                
                logger.info("Cliente : " + retornoPorFactura[0] + " " + retornoPorFactura[1] + " " + retornoPorFactura[2] + " " + retornoPorFactura[3] + " " + retornoPorFactura[4] + " " + retornoPorFactura[5] + " ");
                
                retorno.add(retornoPorFactura);
            }
            
            logger.info("-----------------------------------------------------------------------------------------------------------");
            
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
        retCantIngresadas[0] = "Ingresados correctamente : " + totalClientesIngresados;
        retorno.add(retCantIngresadas);
        return retorno;
    }
}
