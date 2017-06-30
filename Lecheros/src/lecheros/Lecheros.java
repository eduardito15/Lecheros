/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecheros;

import dao.GenericDAO;
import dominio.Articulo;
import dominio.Cliente;
import dominio.DocumentoDeVenta;
import dominio.Factura;
import dominio.FacturaRenglon;
import dominio.GrupoDeArticulos;
import dominio.Precio;
import dominio.Reparto;
import dominio.usuarios.Actividad;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import sistema.SistemaFacturas;
import sistema.SistemaMantenimiento;
import sistema.SistemaMantenimientoArticulos;
import ui.usuarios.Login;

/**
 *
 * @author Edu
 */
public class Lecheros {

    public static String nombreEmpresa = "Relece";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            //cargarClientesRelece();
            //cargarFacturasManualesRelece();
            //borrarFacturasProrrateo();
            //cargarActividades();
            //cargarActividades2();
            //Bienvenida vb = new Bienvenida();
            //vb.setVisible(true);
            
            //cargarClientesManualesGiamo();
            //cargarClientesProrrateoGiamo();
            
            //cargarFacturasManualesGiamo();
            
            //cargarClientesClaferReparto8();
            
            //cargarClienteReleceNuevos();
            
            //cargarCodigosPS();  
            
            //chequearSiExisten();
            
            //codigoCualquiera();
            
        } catch (Exception ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        /*Articulo a;
        try {
            a = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(3284);
            GrupoDeArticulos ga = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorArticulo(a);
            int i = 0;
            i = i + 1;
        } catch (Exception ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        Login login = new Login();
        login.setVisible(true);
    }
    
    public static void codigoCualquiera() {
        Reparto r = SistemaMantenimiento.getInstance().devolverRepartoPorCodigo(2);
        List<Cliente> clientesRelece2 = SistemaMantenimiento.getInstance().devolverClientesPorRepartoYEstado(r , false);
        for(Cliente c : clientesRelece2)
        {
            if(c.getFrecuenciaFacturacionLeche().equals("")) {
                c.setActivo(true);
                GenericDAO.getGenericDAO().actualizar(c);
            }
        }
    }
    
    public static void cargarActividades2(){
        

    }
    
    public static void cargarActividades(){
        Actividad manArt = new Actividad();
        manArt.setNombre("Mantenimiento Articulos");
        GenericDAO.getGenericDAO().guardar(manArt);
        Actividad manCli = new Actividad();
        manCli.setNombre("Mantenimiento Clientes");
        GenericDAO.getGenericDAO().guardar(manCli);
        Actividad manRubroGasto = new Actividad();
        manRubroGasto.setNombre("Mantenimiento Rubro Gasto");
        GenericDAO.getGenericDAO().guardar(manRubroGasto);
        Actividad manChoferes = new Actividad();
        manChoferes.setNombre("Mantenimiento Choferes");
        GenericDAO.getGenericDAO().guardar(manChoferes);
        Actividad manComisiones = new Actividad();
        manComisiones.setNombre("Mantenimiento Comisiones");
        GenericDAO.getGenericDAO().guardar(manComisiones);
        Actividad ingresarPreciosAutomatico = new Actividad();
        ingresarPreciosAutomatico.setNombre("Ingreso Automatico De Precios");
        GenericDAO.getGenericDAO().guardar(ingresarPreciosAutomatico);
        Actividad manFamiliaDeProducto = new Actividad();
        manFamiliaDeProducto.setNombre("Mantenimiento Familias De Productos");
        GenericDAO.getGenericDAO().guardar(manFamiliaDeProducto);
        Actividad manRepartos = new Actividad();
        manRepartos.setNombre("Mantenimiento Repartos");
        GenericDAO.getGenericDAO().guardar(manRepartos);
        Actividad manCamiones = new Actividad();
        manCamiones.setNombre("Mantenimiento Camiones");
        GenericDAO.getGenericDAO().guardar(manCamiones);
        Actividad manDocCompra = new Actividad();
        manDocCompra.setNombre("Mantenimiento Documentos De Compra");
        GenericDAO.getGenericDAO().guardar(manDocCompra);
        Actividad manDocVenta = new Actividad();
        manDocVenta.setNombre("Mantenimiento Documentos De Venta");
        GenericDAO.getGenericDAO().guardar(manDocVenta);
        Actividad manDestCon = new Actividad();
        manDestCon.setNombre("Mantenimiento Destinatario Conaprole");
        GenericDAO.getGenericDAO().guardar(manDestCon);
        Actividad configGral = new Actividad();
        configGral.setNombre("Configuración General");
        GenericDAO.getGenericDAO().guardar(configGral);
        Actividad ingresarCompras = new Actividad();
        ingresarCompras.setNombre("Ingresar Compras");
        GenericDAO.getGenericDAO().guardar(ingresarCompras);
        Actividad buscarCompras = new Actividad();
        buscarCompras.setNombre("Buscar Compras");
        GenericDAO.getGenericDAO().guardar(buscarCompras);
        Actividad ingresarGasto = new Actividad();
        ingresarGasto.setNombre("Ingresar Gasto");
        GenericDAO.getGenericDAO().guardar(ingresarGasto);
        Actividad ingresarInventario = new Actividad();
        ingresarInventario.setNombre("Ingresar Inventario");
        GenericDAO.getGenericDAO().guardar(ingresarInventario);
        Actividad ingresarFiadoChofer = new Actividad();
        ingresarFiadoChofer.setNombre("Ingresar Fiado Chofer");
        GenericDAO.getGenericDAO().guardar(ingresarFiadoChofer);
        Actividad ingresarCheques = new Actividad();
        ingresarCheques.setNombre("Ingresar Cheques");
        GenericDAO.getGenericDAO().guardar(ingresarCheques);
        Actividad ingresarInau = new Actividad();
        ingresarInau.setNombre("Ingresar Inau");
        GenericDAO.getGenericDAO().guardar(ingresarInau);
        Actividad ingresarAnep = new Actividad();
        ingresarAnep.setNombre("Ingresar Anep");
        GenericDAO.getGenericDAO().guardar(ingresarAnep);
        Actividad ingresarRetenciones = new Actividad();
        ingresarRetenciones.setNombre("Ingresar Retenciones");
        GenericDAO.getGenericDAO().guardar(ingresarRetenciones);
        Actividad configLiq = new Actividad();
        configLiq.setNombre("Configuración Liquidación");
        GenericDAO.getGenericDAO().guardar(configLiq);
        Actividad verLiquidacionesViejas = new Actividad();
        verLiquidacionesViejas.setNombre("Ver Liquidación Vieja");
        GenericDAO.getGenericDAO().guardar(verLiquidacionesViejas);
        
        Actividad ingresarFacturas = new Actividad();
        ingresarFacturas.setNombre("Ingresar Facturas");
        GenericDAO.getGenericDAO().guardar(ingresarFacturas);
        Actividad buscarFacturas = new Actividad();
        buscarFacturas.setNombre("Buscar Facturas");
        GenericDAO.getGenericDAO().guardar(buscarFacturas);
        Actividad ingresarFacturasMovil = new Actividad();
        ingresarFacturasMovil.setNombre("Ingresar Facturas Movil");
        GenericDAO.getGenericDAO().guardar(ingresarFacturasMovil);
        Actividad generarInformeFacturas = new Actividad();
        generarInformeFacturas.setNombre("Generar Informe Facturas");
        GenericDAO.getGenericDAO().guardar(generarInformeFacturas);
        Actividad facturarProrrateo = new Actividad();
        facturarProrrateo.setNombre("Facturar Prorrateo");
        GenericDAO.getGenericDAO().guardar(facturarProrrateo);
        Actividad verificarFacturacion = new Actividad();
        verificarFacturacion.setNombre("Verificar Facturacion");
        GenericDAO.getGenericDAO().guardar(verificarFacturacion);
        Actividad imprimirFacturar = new Actividad();
        imprimirFacturar.setNombre("Imprimir Facturas");
        GenericDAO.getGenericDAO().guardar(imprimirFacturar);
        Actividad configFact = new Actividad();
        configFact.setNombre("Configuración Facturación");
        GenericDAO.getGenericDAO().guardar(configFact);
        Actividad mantUsuarios = new Actividad();
        
        
        mantUsuarios.setNombre("Mantenimiento Usuarios");
        GenericDAO.getGenericDAO().guardar(mantUsuarios);
        Actividad mantRoles = new Actividad();
        mantRoles.setNombre("Mantenimiento Roles");
        GenericDAO.getGenericDAO().guardar(mantRoles);
        Actividad logOp = new Actividad();
        logOp.setNombre("Consultar Log de Operaciones");
        GenericDAO.getGenericDAO().guardar(logOp);
        
        Actividad verLiqDia = new Actividad();
        verLiqDia.setNombre("Liquidacion del Dia");
        GenericDAO.getGenericDAO().guardar(verLiqDia);
        
        Actividad ingresarCliente = new Actividad();
        ingresarCliente.setNombre("Ingresar Cliente");
        GenericDAO.getGenericDAO().guardar(ingresarCliente);
        
        Actividad manCliNuevo = new Actividad();
        manCli.setNombre("Modificar/Eliminar Cliente");
        GenericDAO.getGenericDAO().guardar(manCliNuevo);
        
        Actividad manGrupoCli = new Actividad();
        manGrupoCli.setNombre("Mantenimiento Grupos de Clientes");
        GenericDAO.getGenericDAO().guardar(manGrupoCli);
        
        Actividad consDeuda = new Actividad();
        consDeuda.setNombre("Consultar Deuda Cliente");
        GenericDAO.getGenericDAO().guardar(consDeuda);
        
        Actividad ingresarPago = new Actividad();
        ingresarPago.setNombre("Ingresar Pago");
        GenericDAO.getGenericDAO().guardar(ingresarPago);
        
        Actividad manObtEstadoDeCuenta = new Actividad();
        manObtEstadoDeCuenta.setNombre("Obtener Estado de Cuenta Cliente");
        GenericDAO.getGenericDAO().guardar(manObtEstadoDeCuenta);
        
        Actividad agregarBoletaALiq = new Actividad();
        agregarBoletaALiq.setNombre("Agregar Boleta a Liquidación");
        GenericDAO.getGenericDAO().guardar(agregarBoletaALiq);
        Actividad quitarBoletaALiq = new Actividad();
        quitarBoletaALiq.setNombre("Quitar Boleta de Liquidación");
        GenericDAO.getGenericDAO().guardar(quitarBoletaALiq);
        
        Actividad manRepCom = new Actividad();
        manRepCom.setNombre("Mantenimiento Repartos Compuestos");
        GenericDAO.getGenericDAO().guardar(manRepCom);
        
        Actividad abrirLiq = new Actividad();
        abrirLiq.setNombre("Abrir Liquidacion");
        GenericDAO.getGenericDAO().guardar(abrirLiq);
        
        Actividad cerrarLiq = new Actividad();
        cerrarLiq.setNombre("Cerrar Liquidacion");
        GenericDAO.getGenericDAO().guardar(cerrarLiq);
        
        Actividad verLiqCerrada = new Actividad();
        verLiqCerrada.setNombre("Ver Liquidacion Cerrada");
        GenericDAO.getGenericDAO().guardar(verLiqCerrada);
    }

    public void cargarFechaDeLiquidacionEnCompras() {
        /*Date fechaDesde = new Date(116, 0, 1);
            Date fechaHasta = new Date(116, 11, 31);
            List<Compra> compras = SistemaCompras.getInstance().devolverComprasEntreFechas(fechaDesde, fechaHasta);
            for(Compra c : compras){
                boolean esCodigoDeLiquidacion = true;
                for (CompraRenglon cr : c.getRenglones()) {
                    if (cr.getArticulo().getCodigo() == 170118 || cr.getArticulo().getCodigo() == 170080 || cr.getArticulo().getCodigo() == 170081 || cr.getArticulo().getCodigo() == 170082 || cr.getArticulo().getCodigo() == 170084 || cr.getArticulo().getCodigo() == 170126 || cr.getArticulo().getCodigo() == 170185) {
                        esCodigoDeLiquidacion = false;
                        c.setFechaLiquidacion(null);
                        break;
                    }
                }
                if (esCodigoDeLiquidacion) {
                    c.setFechaLiquidacion(c.getFecha());
                }
                GenericDAO.getGenericDAO().actualizar(c);
            }*/
    }

    public static void cargarClientesRelece() throws Exception{
        try {
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(new File("/Users/Edu1/Desktop/Cerram/ClientesRelece1ParaCargarLecheros.xlsx"));
            Workbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet hoja = (XSSFSheet) workbook.getSheetAt(0);
            for (int i = 2; i <= 467; i++) {
                Row fila = hoja.getRow(i - 1);
                int numRep = (int) fila.getCell(1).getNumericCellValue();
                String nombre = fila.getCell(2).getStringCellValue();
                String razonSocial = fila.getCell(3).getStringCellValue();
                String direccion = fila.getCell(4).getStringCellValue();
                String rut = "";
                String auxText = fila.getCell(5).getStringCellValue();
                if (!"".equals(auxText)) {
                    if ('-' == auxText.charAt(auxText.length()-1)) {
                        rut = auxText.substring(0, auxText.length() - 1);
                    } else {
                        rut = auxText;
                    }
                }

                //int litrosComun = (int) fila.getCell(6).getNumericCellValue();
                //int litrosUltra = (int) fila.getCell(7).getNumericCellValue();

                Reparto r = SistemaMantenimiento.getInstance().devolverRepartoPorCodigo(numRep);
                SistemaMantenimiento.getInstance().agregarCliente(true, true, true, null, r, nombre, razonSocial, rut, direccion, "", "", 0, 0, 0, "", 0.0, "", 0, new ArrayList<>(), "","");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void cargarFacturasManualesRelece() throws Exception {
        try {
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(new File("/Users/Edu1/Documents/FacturasEchasAManoReleceAgosto2016.xlsx"));
            Workbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet hoja = (XSSFSheet) workbook.getSheetAt(0);
            long numeroFactura = SistemaMantenimiento.getInstance().devolverConfiguracionFacturacion().getUltimoNumeroFactura();
            for (int i = 352; i <= 308; i++) {
                Row fila = hoja.getRow(i - 1);
                String nombre = fila.getCell(1).getStringCellValue();
                Date fecha = fila.getCell(2).getDateCellValue();
                int litrosComun = (int) fila.getCell(3).getNumericCellValue();
                int  litrosUltra = (int) fila.getCell(4).getNumericCellValue();
                
                List<Cliente> clientes = SistemaMantenimiento.getInstance().devolverClientesPorEstadoTipoYNombre(true, false, nombre);
                if(!clientes.isEmpty()){
                    Cliente cli = clientes.get(0);
                    
                    //Obtengo el Documento de Venta para el documento.
                    DocumentoDeVenta dv = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado");
                    
                    //Obtengo el numero.
                    numeroFactura++;
                    
                    
                    Factura f = new Factura();
                    f.setCliente(cli);
                    f.setEsManual(true);
                    f.setEstaPaga(true);
                    f.setNumero(numeroFactura);
                    f.setReparto(cli.getReparto());
                    f.setTipoDocumento(dv);
                    List<FacturaRenglon> renglones = new ArrayList<>();
                    
                    double total = 0;
                    
                    FacturaRenglon lecheComun = new FacturaRenglon();
                    lecheComun.setFactura(f);
                    Articulo lcomun = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(110);
                    lecheComun.setArticulo(lcomun);
                    lecheComun.setCantidad(litrosComun);
                    lecheComun.setIva(0);
                    Precio pLComun = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lcomun, fecha);
                    lecheComun.setSubtotal(lecheComun.getCantidad()*pLComun.getPrecioVenta());
                    lecheComun.setTotal(lecheComun.getCantidad()*pLComun.getPrecioVenta());
                    renglones.add(lecheComun);
                    total = total + lecheComun.getTotal();
                    
                    FacturaRenglon lecheUltra = new FacturaRenglon();
                    lecheUltra.setFactura(f);
                    Articulo lUltra = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(113);
                    lecheUltra.setArticulo(lUltra);
                    lecheUltra.setCantidad(litrosUltra);
                    lecheUltra.setIva(0);
                    Precio pLUltra = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lUltra, fecha);
                    lecheUltra.setSubtotal(lecheUltra.getCantidad()*pLUltra.getPrecioVenta());
                    lecheUltra.setTotal(lecheUltra.getCantidad()*pLUltra.getPrecioVenta());
                    renglones.add(lecheUltra);
                    total = total + lecheUltra.getTotal();
                    
                    f.setRenglones(renglones);
                    f.setTotalBasico(0);
                    f.setTotalMinimo(0);
                    f.setSubtotal(total);
                    f.setTotal(total);
                    
                    GenericDAO.getGenericDAO().guardar(f);
                }


                //Reparto r = SistemaMantenimiento.getInstance().devolverRepartoPorCodigo(numRep);
                //SistemaMantenimiento.getInstance().agregarCliente(true, true, true, null, r, nombre, razonSocial, rut, direccion, "", "", litrosComun, litrosUltra, 0, "", 0.0, "", 0);
            }
            SistemaMantenimiento.getInstance().devolverConfiguracionFacturacion().setUltimoNumeroFactura(numeroFactura);
            SistemaMantenimiento.getInstance().guardarConfiguracionDeFacturacion(SistemaMantenimiento.getInstance().devolverConfiguracionFacturacion());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void borrarFacturasProrrateo() throws Exception {
        Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
        session.beginTransaction();
        Query consulta = session.createQuery("FROM Factura WHERE esManual = :tipo");
        consulta.setBoolean("tipo", false);
        List<Factura> facturas = consulta.list();
        session.getTransaction().commit();
        session.close();
        for(Factura f : facturas){
            SistemaFacturas.getInstance().eliminarFactura(f);
        }
    }
    
    public static void cargarClientesManualesGiamo() {
        try {
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(new File("/Users/Edu1/Documents/Clientes_Facturas_Manuales.xlsx"));
            Workbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet hoja = (XSSFSheet) workbook.getSheetAt(0);
            for (int i = 2; i <= 612; i++) {
                Row fila = hoja.getRow(i - 1);
                String codigo = fila.getCell(1).getStringCellValue();
                Cliente cli = SistemaMantenimiento.getInstance().devolverClientePorCodigo(codigo);
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
                    if ('-' == auxText.charAt(auxText.length()-1)) {
                        rut = auxText.substring(0, auxText.length() - 1);
                    } else {
                        rut = auxText;
                    }
                }
                
                String telefono = "";
                try {
                    telefono =  fila.getCell(7).getStringCellValue();
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
                
                if(cli == null) {
                    SistemaMantenimiento.getInstance().agregarClienteConCodigo(codigo,false, activo, true, null, r, nombre, razonSocial, rut, direccion, "" + telefono, email, 0, 0, 0, "", 0.0, "", 0, new ArrayList<>(), "", "");
                } else {
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
               
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void cargarClientesProrrateoGiamo() {
        try {
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(new File("/Users/Edu1/Documents/Clientes_Facturas_Prorroteo.xlsx"));
            Workbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet hoja = (XSSFSheet) workbook.getSheetAt(0);
            for (int i = 2; i <= 459; i++) {
                Row fila = hoja.getRow(i - 1);
                String codigo = fila.getCell(1).getStringCellValue();
                Cliente cli = SistemaMantenimiento.getInstance().devolverClientePorCodigo(codigo);
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
                    if ('-' == auxText.charAt(auxText.length()-1)) {
                        rut = auxText.substring(0, auxText.length() - 1);
                    } else {
                        rut = auxText;
                    }
                }
                
                String telefono = "";
                try {
                    telefono =  fila.getCell(7).getStringCellValue();
                } catch (NullPointerException e) {
                    
                }
                String email = "";
                try {
                    email = fila.getCell(8).getStringCellValue();
                } catch (NullPointerException e) {
                    
                }
                
                int litrosComun = 0;
                try {
                    litrosComun = (int) fila.getCell(9).getNumericCellValue();
                } catch (NullPointerException e) {
                    
                }
                
                int litrosUltra = 0;
                try {
                    litrosUltra = (int) fila.getCell(10).getNumericCellValue();
                } catch (NullPointerException e) {
                    
                }
                
                int litrosDeslactosada = 0;
                try {
                    litrosDeslactosada = (int) fila.getCell(11).getNumericCellValue();
                } catch (NullPointerException e) {
                    
                }
                
                double prodsMinimo = 0;
                try {
                    prodsMinimo = fila.getCell(12).getNumericCellValue();
                } catch (NullPointerException e) {
                    
                }
                
                double prodsBasico = 0;
                try {
                    prodsBasico = (int) fila.getCell(13).getNumericCellValue();
                } catch (NullPointerException e) {
                    
                }
                
                double prodTotal = prodsMinimo + prodsBasico;
                
                boolean activo = fila.getCell(14).getBooleanCellValue();

                //int litrosComun = (int) fila.getCell(6).getNumericCellValue();
                //int litrosUltra = (int) fila.getCell(7).getNumericCellValue();

                Reparto r = SistemaMantenimiento.getInstance().devolverRepartoPorCodigo(numRep);
                
                if(cli == null) {
                    SistemaMantenimiento.getInstance().agregarClienteConCodigo(codigo,true, activo, true, null, r, nombre, razonSocial, rut, direccion, "" + telefono, email, litrosComun, litrosUltra, litrosDeslactosada, "", prodTotal, "", 0, new ArrayList<>(), "", "");
            
                } else {
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
                    cli.setLitrosComun(litrosComun);
                    cli.setLitrosUltra(litrosUltra);
                    cli.setLitrosDeslactosada(litrosDeslactosada);
                    cli.setProductos(prodTotal);
                    SistemaMantenimiento.getInstance().actualizarCliente(cli);
               
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void cargarClientesClaferReparto8() {
        try {
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(new File("/Users/Edu1/Desktop/Clafer/RTO8.xlsx"));
            Workbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet hoja = (XSSFSheet) workbook.getSheetAt(1);
            for (int i = 6; i <= 166; i++) {
                Row fila = hoja.getRow(i - 1);
                int numRep = 8;
                String nombre = fila.getCell(1).getStringCellValue();
                String razonSocial = "";
                try { 
                    razonSocial = fila.getCell(2).getStringCellValue(); 
                } catch (NullPointerException ne) {
                    
                }
                String direccion = "";
                try {
                    direccion = fila.getCell(3).getStringCellValue();
                } catch(NullPointerException ne) {
                    
                }
                String telefono = "";
                try{
                    if((i >= 7 && i<=15) || (i >=17 && i <= 24) ) {
                        int tel = (int)fila.getCell(5).getNumericCellValue();
                        telefono = Integer.toString(tel);
                    } else {
                        telefono = fila.getCell(5).getStringCellValue();
                    }
                } catch(NullPointerException ne){
                    
                }
                String rut = "";
                String auxText = "";
                try {
                    auxText = fila.getCell(6).getStringCellValue();
                } catch (NullPointerException ne) {
                    
                }
                if (!"".equals(auxText)) {
                    if ('-' == auxText.charAt(auxText.length()-1)) {
                        rut = auxText.substring(0, auxText.length() - 1);
                    } else {
                        rut = auxText;
                    }
                }
                int litrosComun = 0;
                try {
                    litrosComun = (int)(fila.getCell(7).getNumericCellValue());
                    int litrosDescremada = 0;
                    litrosDescremada = (int)(fila.getCell(9).getNumericCellValue());
                    litrosComun = litrosComun + litrosDescremada;
                } catch (NullPointerException ne) {
                    
                }
                int litrosUltra = 0;
                try {
                    litrosUltra = (int) fila.getCell(8).getNumericCellValue();
                } catch (NullPointerException ne) {
                    
                }

                Reparto r = SistemaMantenimiento.getInstance().devolverRepartoPorCodigo(numRep);
                SistemaMantenimiento.getInstance().agregarCliente(true, true, true, null, r, nombre, razonSocial, rut, direccion, "", "", litrosComun, litrosUltra, 0, "", 0.0, "", 0, new ArrayList<>(), "", "");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void cargarFacturasManualesGiamo() {
        try {
            //Me cargo en la memoria los clientes manuales para saber a que cliente pertenece la factura manual
            HashMap<Long, Cliente> clientesFacturaLeche = new HashMap<>();
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(new File("/Users/Edu1/Documents/Clientes_Facturas_Manuales.xlsx"));
            Workbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet hoja = (XSSFSheet) workbook.getSheetAt(0);
            for (int i = 2; i <= 612; i++) {
                Row fila = hoja.getRow(i - 1);
                long oid = (long)fila.getCell(0).getNumericCellValue();
                String codigo = fila.getCell(1).getStringCellValue();
                Cliente cli = SistemaMantenimiento.getInstance().devolverClientePorCodigo(codigo);
                if(cli != null) {
                    clientesFacturaLeche.put(oid, cli);
                }
            }
            
            inputStream = null;
            inputStream = new FileInputStream(new File("/Users/Edu1/Documents/AnalisisFacturasManuales2301201731012017.xlsx"));
            workbook = new XSSFWorkbook(inputStream);
            hoja = (XSSFSheet) workbook.getSheetAt(0);
            for (int i = 2; i <= 459; i++) {
                Row fila = hoja.getRow(i - 1);
                
                long numero = (long)fila.getCell(0).getNumericCellValue();
                Date fecha = fila.getCell(1).getDateCellValue();
                long oid = (long)fila.getCell(2).getNumericCellValue();
                Cliente cli = clientesFacturaLeche.get(oid);
                
                double totalLecheComun = fila.getCell(3).getNumericCellValue();
                double totalLecheUltra = fila.getCell(4).getNumericCellValue();
                double totalLecheUltraDiferenciada = fila.getCell(5).getNumericCellValue();
                double totalLecheDeslactosada = fila.getCell(6).getNumericCellValue();
                double totalProductosMinimo = fila.getCell(7).getNumericCellValue();
                double totalProductosBasico = fila.getCell(8).getNumericCellValue();
                
                GrupoDeArticulos grupoLecheComun = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Común");
        
                GrupoDeArticulos grupoLecheUtra = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra");
        
                GrupoDeArticulos grupoLecheUltraDiferenciada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra Diferenciada");
        
                GrupoDeArticulos grupoLecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Deslactosada");
        
                Articulo lecheComun = null;
                Precio pLecheComun = null;
                if(!grupoLecheComun.getArticulos().isEmpty()) {
                    lecheComun = grupoLecheComun.getArticulos().get(0);
                    pLecheComun = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheComun, fecha);
                }
        
                Articulo lecheUltra = null;
                Precio pLecheUltra = null;
                if(!grupoLecheUtra.getArticulos().isEmpty()) {
                    lecheUltra = grupoLecheUtra.getArticulos().get(0);
                    pLecheUltra = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheUltra, fecha);
                }
        
                Articulo lecheUltraDiferenciada = null;
                Precio pLecheUltraDiferenciada = null;
                if((grupoLecheUltraDiferenciada!=null?!grupoLecheUltraDiferenciada.getArticulos().isEmpty():false)) {
                    lecheUltraDiferenciada = grupoLecheUltraDiferenciada.getArticulos().get(0);
                    pLecheUltraDiferenciada = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheUltraDiferenciada, fecha);
                }
        
                Articulo lecheDeslactosada = null;
                Precio pLecheDeslactosada = null;
                if(!grupoLecheDeslactosada.getArticulos().isEmpty()) {
                    lecheDeslactosada = grupoLecheDeslactosada.getArticulos().get(0);
                    pLecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheDeslactosada, fecha);
                }
                
                Articulo prodMinimo = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(44444);
                Articulo prodBasico = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(55555);
                
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
                    
                    int cantidad = (int)(totalLecheComun/pLecheComun.getPrecioVenta());

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
                    
                    int cantidad = (int)(totalLecheUltra/pLecheUltra.getPrecioVenta());
                    
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
                    
                    int cantidad = (int)(totalLecheUltraDiferenciada/pLecheUltraDiferenciada.getPrecioVenta());
                    
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
                    
                    int cantidad = (int) (totalLecheDeslactosada/pLecheDeslactosada.getPrecioVenta());
                    
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
                }
            }
        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void cargarClienteReleceNuevos() {
        try {
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(new File("/Users/Edu1/Desktop/Cerram/ClientesRelece4ParaCargarLecheros.xlsx"));
            Workbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet hoja = (XSSFSheet) workbook.getSheetAt(0);
            for (int i = 2; i <= 120; i++) {
                Row fila = hoja.getRow(i - 1);
                int numRep = 6;
                String razonSocial = "";
                try { 
                    razonSocial = fila.getCell(2).getStringCellValue(); 
                } catch (NullPointerException ne) {
                    
                }
                String nombre = "";
                try {
                    nombre = fila.getCell(1).getStringCellValue();
                } catch(NullPointerException ne) {
                    
                }
                String direccion = "";
                try{
                    direccion = fila.getCell(3).getStringCellValue();
                } catch(NullPointerException ne){
                    
                }
                String rut = "";
                int auxText = 0;
                try {
                    if(fila.getCell(4).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        fila.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
                    }
                    rut = fila.getCell(4).getStringCellValue();
                    //auxText = (int)fila.getCell(4).getNumericCellValue();
                } catch (NullPointerException ne) {
                    
                }
                if(auxText != 0) {
                    rut = Integer.toString(auxText);
                }

                Reparto r = SistemaMantenimiento.getInstance().devolverRepartoPorCodigo(numRep);
                SistemaMantenimiento.getInstance().agregarCliente(true, true, true, null, r, nombre, razonSocial, rut, direccion, "", "", 0, 0, 0, "", 0.0, "", 0, new ArrayList<>(), "", "");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void cargarCodigosPS() {
        try {
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(new File("/Users/Edu1/Desktop/Giamo/Rutas Giamo.xlsx"));
            Workbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet hoja = (XSSFSheet) workbook.getSheetAt(7);
            
            Workbook workbookClis = new XSSFWorkbook();
            Sheet hojaFacturasProrrateo = workbookClis.createSheet("Clientes Giamo 8");
            
            int filaArch = 1;
            
            Row row = hojaFacturasProrrateo.createRow(filaArch);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue("Codigo");
            Cell cell2 = row.createCell(1);
            cell2.setCellValue("Nombre");
            Cell cell3 = row.createCell(2);
            cell3.setCellValue("Razon");
            Cell cell4 = row.createCell(3);
            cell4.setCellValue("Direccion");
            Cell cell5 = row.createCell(4);
            cell5.setCellValue("RUT");
            Cell cell6 = row.createCell(5);
            cell6.setCellValue("Codigo Alternativo");
            
            filaArch++;
            
            for (int i = 4; i <= 48; i++) {
                Row fila = hoja.getRow(i - 1);
                int codPS = 0;
                try { 
                    codPS = (int)fila.getCell(0).getNumericCellValue(); 
                    //codPS = Integer.parseInt(fila.getCell(0).getStringCellValue());
                } catch (NullPointerException ne) {
                    
                }
                String nombre = "";
                try { 
                    nombre = fila.getCell(1).getStringCellValue(); 
                } catch (NullPointerException ne) {
                    
                }
                String razonSocial = "";
                try {
                    razonSocial = fila.getCell(2).getStringCellValue();
                } catch(NullPointerException ne) {
                    
                }
                String direccion = "";
                try{
                    direccion = fila.getCell(3).getStringCellValue();
                } catch(NullPointerException ne){
                    
                }
                
                
                
                //Consulto por cliente con esa direccion
                Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
                session.beginTransaction();
                Query consulta = session.createQuery("FROM Cliente WHERE direccion = :dir or nombre = :nom or razonSocial = :nom or nombre = :rs or razonSocial = :rs and activo = :act and reparto_id = " + 9);
                consulta.setString("dir", direccion);
                consulta.setString("nom", nombre);
                consulta.setString("rs", razonSocial);
                consulta.setBoolean("act", true);
                List<Cliente> retorno = consulta.list();
                session.getTransaction().commit();
                session.close();
                
                if(retorno != null && !retorno.isEmpty()) {
                    Cliente c = retorno.get(0);
                    
                    Row r = hojaFacturasProrrateo.createRow(filaArch);
                    Cell cell10 = r.createCell(0);
                    cell10.setCellValue(codPS);
                    Cell cell20 = r.createCell(1);
                    cell20.setCellValue(nombre);
                    Cell cell30 = r.createCell(2);
                    cell30.setCellValue(razonSocial);
                    Cell cell40 = r.createCell(3);
                    cell40.setCellValue(direccion);
                    Cell cell50 = r.createCell(4);
                    cell50.setCellValue("");
                    Cell cell60 = r.createCell(5);
                    cell60.setCellValue(c.getCodigoPS());
                    
                    for (int j = 0; j <= 7; j++) {
                        hoja.autoSizeColumn(j, true);
                    }
                    
                    filaArch++;
                    
                }
            }
            
            try {
                FileOutputStream outPro = new FileOutputStream(new File("/Users/Edu1/Desktop/Giamo/ClientesGiamoReparto8PSConCodigoAlternativo.xlsx"));
                workbookClis.write(outPro);
                outPro.close();
            } catch (IOException ioexp) {
                int h = 0;
                System.out.println("Error");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void chequearSiExisten() {
        try {
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(new File("/Users/Edu1/Desktop/Cerram/ClientesRelece1ParaVerNuevos.xlsx"));
            Workbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet hoja = (XSSFSheet) workbook.getSheetAt(0);
            
            //Workbook workbookClis = new XSSFWorkbook();
            //Sheet hojaFacturasProrrateo = workbookClis.createSheet("Clientes Giamo 8");
            
            int filaArch = 1;
            
            /*Row row = hojaFacturasProrrateo.createRow(filaArch);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue("Codigo");
            Cell cell2 = row.createCell(1);
            cell2.setCellValue("Nombre");
            Cell cell3 = row.createCell(2);
            cell3.setCellValue("Razon");
            Cell cell4 = row.createCell(3);
            cell4.setCellValue("Direccion");
            Cell cell5 = row.createCell(4);
            cell5.setCellValue("Codigo Alternativo");*/
            
            filaArch++;
            
            int cantEncontados = 0;
            int cantNoEncontrados = 0;
            
            for (int i = 2; i <= 126; i++) {
                Row fila = hoja.getRow(i - 1);
                int codPS = 0;
                try { 
                    codPS = (int)fila.getCell(0).getNumericCellValue(); 
                } catch (NullPointerException ne) {
                    
                }
                String nombre = "";
                try { 
                    nombre = fila.getCell(1).getStringCellValue(); 
                } catch (NullPointerException ne) {
                    
                }
                String razonSocial = "";
                try {
                    razonSocial = fila.getCell(2).getStringCellValue();
                } catch(NullPointerException ne) {
                    
                }
                String direccion = "";
                try{
                    direccion = fila.getCell(3).getStringCellValue();
                } catch(NullPointerException ne){
                    
                }
                
                //Consulto por cliente con esa direccion
                Session session = GenericDAO.getGenericDAO().getSessionFactory().openSession();
                session.beginTransaction();
                Query consulta = session.createQuery("FROM Cliente WHERE direccion = :dir and activo = :act and reparto_id = " + 10);
                consulta.setString("dir", direccion);
                consulta.setBoolean("act", true);
                List<Cliente> retorno = consulta.list();
                session.getTransaction().commit();
                session.close();
                
                
                
                if(retorno != null && !retorno.isEmpty()) {
                    Cliente c = retorno.get(0);
                    
                    /*Row r = hojaFacturasProrrateo.createRow(filaArch);
                    Cell cell10 = r.createCell(0);
                    cell10.setCellValue(codPS);
                    Cell cell20 = r.createCell(1);
                    cell20.setCellValue(nombre);
                    Cell cell30 = r.createCell(2);
                    cell30.setCellValue(razonSocial);
                    Cell cell40 = r.createCell(3);
                    cell40.setCellValue(direccion);
                    Cell cell50 = r.createCell(4);
                    cell50.setCellValue(c.getCodigoPS());
                    
                    for (int j = 0; j <= 7; j++) {
                        hoja.autoSizeColumn(j, true);
                    }*/
                    cantEncontados++;
                    filaArch++;
                    
                } else {
                    cantNoEncontrados++;
                    System.out.println("Cliente no encontrado: " + nombre + " ,Razon Social : " + razonSocial + " ,Dierccion: " + direccion);
                }
            }
            
            System.out.println("Cant Encontrados: " + cantEncontados);
            System.out.println("Cant No Encontrados: " + cantNoEncontrados);
            
            /*try {
                FileOutputStream outPro = new FileOutputStream(new File("/Users/Edu1/Desktop/Giamo/ClientesGiamoReparto8PSConCodigoAlternativo.xlsx"));
                workbookClis.write(outPro);
                outPro.close();
            } catch (IOException ioexp) {
                int h = 0;
                System.out.println("Error");
            }*/
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Lecheros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
