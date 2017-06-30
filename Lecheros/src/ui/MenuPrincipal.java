/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dominio.ConfiguracionFacturacion;
import ui.mantenimiento.IngresoAutomaticoDePrecios;
import ui.compras.IngresarComprasDesdeArchivo;
import ui.compras.IngresarCompras;
import ui.facturas.IngresarFacturasMovil;
import ui.facturas.FacturarProrrateo;
import ui.facturas.IngresoFacturas;
import ui.informes.clafer.InformeBonificacionPorRoturas;
import ui.informes.clafer.InformeCargaDeLechePorReparto;
import ui.informes.clafer.InformeResumenDeEnvases;
import ui.clientes.IngresarCliente;
import ui.liquidaciones.IngresoInau;
import ui.liquidaciones.IngresoFiadoChofer;
import ui.liquidaciones.IngresoInventario;
import ui.liquidaciones.IngresoRetencion;
import ui.liquidaciones.IngresoGasto;
import ui.liquidaciones.IngresoAnep;
import ui.liquidaciones.IngresoCheque;
import ui.facturas.VentanaVerificarFacturacion;
import ui.facturas.VentanaGenerarArchivoDeFacturas;
import ui.clientes.VentanaIngresarPagoCliente;
import ui.clientes.VentanaConsultaDeudaCliente;
import ui.config.VentanaConfiguracionLiquidacion;
import ui.config.VentanaConfiguracionGeneral;
import ui.liquidaciones.VentanaLiquidacion;
import ui.compras.MantenimientoCompras;
import ui.mantenimiento.MantenimientoDoumentosDeVenta;
import ui.mantenimiento.MantenimientoRubroGasto;
import ui.mantenimiento.MantenimientoArticulos;
import ui.clientes.MantenimientoClientes;
import ui.mantenimiento.MantenimientoIvas;
import ui.mantenimiento.MantenimientoGruposDeClientes;
import ui.mantenimiento.MantenimientoCamiones;
import ui.mantenimiento.MantenimientoRepartos;
import ui.mantenimiento.MantenimientoComisiones;
import ui.mantenimiento.MantenimientoFamiliaDeProductos;
import ui.mantenimiento.MantenimientoChoferes;
import ui.mantenimiento.MantenimientoDestinatariosReparto;
import ui.mantenimiento.MantenimientoDoumentosDeCompra;
import ui.informes.VentanaInformeAnep;
import ui.informes.VentanaInformeAnalisisComprasDevoluciones;
import ui.informes.VentanaInformeAnalisisIvas;
import ui.informes.VentanaInformeCheques;
import ui.informes.VentanaInformeGastosPorRubro;
import ui.informes.VentanaInformePinchadas;
import ui.informes.VentanaInformeComision;
import ui.informes.VentanaInformeControlDeEnvasesPorEmpresa;
import ui.informes.VentanaInformeControlDeEnvases;
import ui.informes.VentanaInformeInau;
import ui.informes.VentanaInformeContadora;
import ui.informes.VentanaInformeComprasPorEmpresa;
import ui.informes.VentanaInformeRetenciones;
import dominio.ConfiguracionLiquidacion;
import dominio.DocumentoDeCompra;
import dominio.DocumentoDeVenta;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import lecheros.Lecheros;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.clientes.ResumenDeComprasPorCliente;
import ui.clientes.VentanaEstadoDeCuenta;
import ui.config.VentanaConfiguracionFacturacion;
import ui.facturas.ImprimirGrupoDeFacturas;
import ui.facturas.IngresarFacturaManualGiamo;
import ui.facturas.IngresarFacturasManualesGiamoDesdeArchivo;
import ui.facturas.IngresarFacturasMovilRelece;
import ui.facturas.MantenimientoFacturas;
import ui.facturas.VentanaInformeFacturasContadora;
import ui.informes.VentanaInformeJornales;
import ui.informes.VentanaInformeResumenDeEnvases;
import ui.informes.clafer.InformeChoferTotalProductos;
import ui.informes.clafer.InformeControlChequesConaprole;
import ui.informes.clafer.InformeResumenLiquidaciones;
import ui.informes.clafer.InformeTotalVentaPorductosPorReparto;
import ui.informes.giamo.InformeResumenDeEnvasesGiamo;
import ui.informes.giamo.VentanaInformeContadoraGiamo;
import ui.liquidaciones.IngresoInventarioGiamo;
import ui.liquidaciones.IngresoJornales;
import ui.liquidaciones.VentanaLiquidacionClafer;
import ui.liquidaciones.VentanaLiquidacionGiamo;
import ui.mantenimiento.IngresoAutomaticoDePreciosDePromociones;
import ui.mantenimiento.MantenimientoCoeficientesUtilidadCompras;
import ui.mantenimiento.MantenimientoGruposDeArticulos;
import ui.mantenimiento.MantenimientoRepartosCompuestos;
import ui.usuarios.Constantes;
import ui.usuarios.ConsultarLogDeOperaciones;
import ui.usuarios.MantenimientoRoles;
import ui.usuarios.MantenimientoUsuarios;

/**
 *
 * @author Edu
 */
public class MenuPrincipal extends javax.swing.JFrame {

    private String empresa;

    private SistemaUsuarios sisUsuarios;

    /**
     * Creates new form MenuPrincipal
     */
    public MenuPrincipal() {
        initComponents();
        sisUsuarios = SistemaUsuarios.getInstance();
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        cargarMenuCompras();
        cargarMenuFacturas();
        cargarMenuLiquidaciones();
        cargarMenuInformes(Lecheros.nombreEmpresa);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem37 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem44 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem45 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem41 = new javax.swing.JMenuItem();
        jMenuItemCoeficientes = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItemFiadoChofer = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItemCheques = new javax.swing.JMenuItem();
        jMenuItemInau = new javax.swing.JMenuItem();
        jMenuItemAnep = new javax.swing.JMenuItem();
        jMenuItemRetenciones = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItemInvGiamo = new javax.swing.JMenuItem();
        jMenuItem39 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItemControlDeEnvasesPorEmpresa = new javax.swing.JMenuItem();
        jMenuItemResumenDeEnvasesReleceYCerram = new javax.swing.JMenuItem();
        jMenuItemInformePinchadas = new javax.swing.JMenuItem();
        jMenuItemInformeComisionMama = new javax.swing.JMenuItem();
        jMenuItemInfCheques = new javax.swing.JMenuItem();
        jMenuItemRete = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItemInformeAnep = new javax.swing.JMenuItem();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenuItemComprasPorEmpresaMama = new javax.swing.JMenuItem();
        jMenuItemAnalisisIvasMama = new javax.swing.JMenuItem();
        jMenuItemContadorMama = new javax.swing.JMenuItem();
        jMenuItemBonificacionPorRoturasClafer = new javax.swing.JMenuItem();
        jMenuItemResumenDeEnvasesClafer = new javax.swing.JMenuItem();
        jMenuItemControlDeChequesConaprole = new javax.swing.JMenuItem();
        jMenuItemCargaDeLechePorReparto = new javax.swing.JMenuItem();
        jMenuItemInformeTotalVentaProductos = new javax.swing.JMenuItem();
        jMenuItemChoferTotalProductosClafer = new javax.swing.JMenuItem();
        jMenuItemResumenDeLiquidaciones = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItemResumenDeEnvases = new javax.swing.JMenuItem();
        jMenuItemImportarFacturasManualesGiamo = new javax.swing.JMenuItem();
        jMenuItem46 = new javax.swing.JMenuItem();
        jMenuItemInformeContadorGiamo = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItemIngresarFacturasMovil = new javax.swing.JMenuItem();
        jMenuItemIngresarFacturasMovilCerram = new javax.swing.JMenuItem();
        jMenuItemIngresarFacturasMovilRelece = new javax.swing.JMenuItem();
        jMenuItem38 = new javax.swing.JMenuItem();
        jMenuItem42 = new javax.swing.JMenuItem();
        jMenuItem43 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenuItemIngresarFacturasManualesGiamoDesdeArchivo = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu Principal Lecheros");

        jMenu1.setText("Mantenimiento");

        jMenuItem1.setText("Artículos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem37.setText("Grupos de Artículos");
        jMenuItem37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem37ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem37);

        jMenu5.setText("Clientes");

        jMenuItem14.setText("Ingresar");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem14);

        jMenuItem15.setText("Modificar/Eliminar");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem15);

        jMenuItem16.setText("Grupos de Clientes");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem16);

        jMenuItem27.setText("Consultar Deuda Cliente");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem27);

        jMenuItem33.setText("Estados de Cuenta");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem33);

        jMenuItem28.setText("Ingresar Pago");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem28);

        jMenu1.add(jMenu5);

        jMenuItem12.setText("Rubros de Gastos");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem12);

        jMenuItem5.setText("Choferes");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem21.setText("Comisiones");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem21);

        jMenuItem6.setText("Ingresar Precios Automático");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem44.setText("Ingresar Precios Promociones");
        jMenuItem44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem44ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem44);

        jMenuItem3.setText("Familias de Productos");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("Repartos");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem36.setText("Repartos Compuestos");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem36);

        jMenuItem31.setText("Camiones");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem31);

        jMenuItem7.setText("Documentos de Compra");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem45.setText("Documentos de Venta");
        jMenuItem45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem45ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem45);

        jMenuItem9.setText("Destinatario Conaprole - Reparto");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem9);

        jMenuItem2.setText("Ivas");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem41.setText("Configuración General");
        jMenuItem41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem41ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem41);

        jMenuItemCoeficientes.setText("Coeficientes Utilidad Compras");
        jMenuItemCoeficientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCoeficientesActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemCoeficientes);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Compras");

        jMenuItem8.setText("Buscar");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem10.setText("Ingresar desde Archivo");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Liquidaciones");

        jMenuItem11.setText("Inventario");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem11);

        jMenuItem13.setText("Gastos");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem13);

        jMenuItemFiadoChofer.setText("Fiado Chofer");
        jMenuItemFiadoChofer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFiadoChoferActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemFiadoChofer);

        jMenuItem17.setText("Liquidación");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem17);

        jMenuItemCheques.setText("Cheques");
        jMenuItemCheques.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemChequesActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemCheques);

        jMenuItemInau.setText("Inau");
        jMenuItemInau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemInauActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemInau);

        jMenuItemAnep.setText("Anep");
        jMenuItemAnep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAnepActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemAnep);

        jMenuItemRetenciones.setText("Retenciones");
        jMenuItemRetenciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRetencionesActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemRetenciones);

        jMenuItem30.setText("Configuración");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem30);

        jMenuItemInvGiamo.setText("Inventario para Facturación");
        jMenuItemInvGiamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemInvGiamoActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemInvGiamo);

        jMenuItem39.setText("Jornales (Contabilidad)");
        jMenuItem39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem39ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem39);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Informes");

        jMenuItem19.setText("Control de Envases");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem19);

        jMenuItemControlDeEnvasesPorEmpresa.setText("Control de Envases Por Empresa");
        jMenuItemControlDeEnvasesPorEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemControlDeEnvasesPorEmpresaActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemControlDeEnvasesPorEmpresa);

        jMenuItemResumenDeEnvasesReleceYCerram.setText("Resumen de Envases");
        jMenuItemResumenDeEnvasesReleceYCerram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemResumenDeEnvasesReleceYCerramActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemResumenDeEnvasesReleceYCerram);

        jMenuItemInformePinchadas.setText("Informe Pinchadas");
        jMenuItemInformePinchadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemInformePinchadasActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemInformePinchadas);

        jMenuItemInformeComisionMama.setText("Comisión");
        jMenuItemInformeComisionMama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemInformeComisionMamaActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemInformeComisionMama);

        jMenuItemInfCheques.setText("Cheques");
        jMenuItemInfCheques.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemInfChequesActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemInfCheques);

        jMenuItemRete.setText("Retenciónes");
        jMenuItemRete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReteActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemRete);

        jMenuItem22.setText("Inau");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem22);

        jMenuItemInformeAnep.setText("Anep");
        jMenuItemInformeAnep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemInformeAnepActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemInformeAnep);

        jMenuItem32.setText("Análisis Compras-Devoluciones");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem32);

        jMenuItem34.setText("Gastos por Rubro");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem34);

        jMenuItemComprasPorEmpresaMama.setText("Compras por Empresa");
        jMenuItemComprasPorEmpresaMama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemComprasPorEmpresaMamaActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemComprasPorEmpresaMama);

        jMenuItemAnalisisIvasMama.setText("Analisis Ivas");
        jMenuItemAnalisisIvasMama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAnalisisIvasMamaActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemAnalisisIvasMama);

        jMenuItemContadorMama.setText("Contador");
        jMenuItemContadorMama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemContadorMamaActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemContadorMama);

        jMenuItemBonificacionPorRoturasClafer.setText("Bonificación por Roturas");
        jMenuItemBonificacionPorRoturasClafer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBonificacionPorRoturasClaferActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemBonificacionPorRoturasClafer);

        jMenuItemResumenDeEnvasesClafer.setText("Resumen de Envases");
        jMenuItemResumenDeEnvasesClafer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemResumenDeEnvasesClaferActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemResumenDeEnvasesClafer);

        jMenuItemControlDeChequesConaprole.setText("Control de Cheques Conaprole");
        jMenuItemControlDeChequesConaprole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemControlDeChequesConaproleActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemControlDeChequesConaprole);

        jMenuItemCargaDeLechePorReparto.setText("Carga de Leche por Reparto");
        jMenuItemCargaDeLechePorReparto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCargaDeLechePorRepartoActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemCargaDeLechePorReparto);

        jMenuItemInformeTotalVentaProductos.setText("Total de Venta de Productos por Reparto");
        jMenuItemInformeTotalVentaProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemInformeTotalVentaProductosActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemInformeTotalVentaProductos);

        jMenuItemChoferTotalProductosClafer.setText("Chofer - Total Productos");
        jMenuItemChoferTotalProductosClafer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemChoferTotalProductosClaferActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemChoferTotalProductosClafer);

        jMenuItemResumenDeLiquidaciones.setText("Resumen de Liquidaciones");
        jMenuItemResumenDeLiquidaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemResumenDeLiquidacionesActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemResumenDeLiquidaciones);

        jMenuItem23.setText("Resumen de Compras Por Cliente");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem23);

        jMenuItemResumenDeEnvases.setText("Resumen Envases");
        jMenuItemResumenDeEnvases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemResumenDeEnvasesActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemResumenDeEnvases);

        jMenuItemImportarFacturasManualesGiamo.setText("Importar Facturas Manuales desde Excel");
        jMenuItemImportarFacturasManualesGiamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemImportarFacturasManualesGiamoActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemImportarFacturasManualesGiamo);

        jMenuItem46.setText("Consulta Jornales");
        jMenuItem46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem46ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem46);

        jMenuItemInformeContadorGiamo.setText("Contador");
        jMenuItemInformeContadorGiamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemInformeContadorGiamoActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemInformeContadorGiamo);

        jMenuBar1.add(jMenu4);

        jMenu6.setText("Facturas");

        jMenu7.setText("Ingreso Manual");
        jMenu6.add(jMenu7);

        jMenuItem26.setText("Buscar");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem26);

        jMenuItemIngresarFacturasMovil.setText("Ingresar Facturas Movil");
        jMenuItemIngresarFacturasMovil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemIngresarFacturasMovilActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItemIngresarFacturasMovil);

        jMenuItemIngresarFacturasMovilCerram.setText("Ingresar Facturas Movil Cerram");
        jMenuItemIngresarFacturasMovilCerram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemIngresarFacturasMovilCerramActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItemIngresarFacturasMovilCerram);

        jMenuItemIngresarFacturasMovilRelece.setText("Ingresar Facturas Movil Relece");
        jMenuItemIngresarFacturasMovilRelece.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemIngresarFacturasMovilReleceActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItemIngresarFacturasMovilRelece);

        jMenuItem38.setText("Generar Interface PS");
        jMenuItem38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem38ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem38);

        jMenuItem42.setText("Facturar Prorrateo");
        jMenuItem42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem42ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem42);

        jMenuItem43.setText("Verificar Facturación");
        jMenuItem43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem43ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem43);

        jMenuItem20.setText("Imprimir Grupo de Facturas");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem20);

        jMenuItem18.setText("Configuración");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem18);

        jMenuItem35.setText("Generar Informe Contador");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem35);

        jMenuItemIngresarFacturasManualesGiamoDesdeArchivo.setText("Ingresar Manuales desde Archivo");
        jMenuItemIngresarFacturasManualesGiamoDesdeArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemIngresarFacturasManualesGiamoDesdeArchivoActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItemIngresarFacturasManualesGiamoDesdeArchivo);

        jMenuBar1.add(jMenu6);

        jMenu8.setText("Usuarios");

        jMenuItem24.setText("Usuarios");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem24);

        jMenuItem25.setText("Roles");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem25);

        jMenuItem29.setText("Log de Operaciones");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem29);

        jMenuBar1.add(jMenu8);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1140, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 485, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoArticulos)) {
                MantenimientoArticulos ma = new MantenimientoArticulos(this, true);
                ma.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoIvas)) {
                MantenimientoIvas mi = new MantenimientoIvas(this, true);
                mi.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoArticulos)) {
                MantenimientoFamiliaDeProductos mfp = new MantenimientoFamiliaDeProductos(this, true);
                mfp.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoRepartos)) {
                MantenimientoRepartos mc = new MantenimientoRepartos(this, true);
                mc.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoChoferes)) {
                MantenimientoChoferes mc = new MantenimientoChoferes(this, true);
                mc.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarPreciosAutomaticamente)) {
                IngresoAutomaticoDePrecios iap = new IngresoAutomaticoDePrecios(this, true);
                iap.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoDocumentosDeCompra)) {
                MantenimientoDoumentosDeCompra mdc = new MantenimientoDoumentosDeCompra(this, true);
                mdc.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadBuscarCompras)) {
                MantenimientoCompras mc = new MantenimientoCompras(this, false);
                mc.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoDestinatariosConaprole)) {
                MantenimientoDestinatariosReparto mdr = new MantenimientoDestinatariosReparto(this, true);
                mdr.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarCompras)) {
                IngresarComprasDesdeArchivo ica = new IngresarComprasDesdeArchivo(this, false);
                ica.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarInventario)) {
                IngresoInventario iinv = new IngresoInventario(this, false);
                iinv.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoRubroGasto)) {
                MantenimientoRubroGasto mrg = new MantenimientoRubroGasto(this, true);
                mrg.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarGasto)) {
                IngresoGasto vig = new IngresoGasto(this, false);
                vig.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarCliente)) {
                IngresarCliente ic = new IngresarCliente(this, false);
                ic.setAccion("Nuevo");
                ic.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadModEliCli)) {
                MantenimientoClientes vmc = new MantenimientoClientes(this, false);
                vmc.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoGrupoDeClientes)) {
                MantenimientoGruposDeClientes vmgc = new MantenimientoGruposDeClientes(this, true);
                vmgc.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItemFiadoChoferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFiadoChoferActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarFiadoChofer)) {
                IngresoFiadoChofer ifc = new IngresoFiadoChofer(this, false);
                ifc.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jMenuItemFiadoChoferActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        // TODO add your handling code here:
        try {
            if ("Relece".equals(empresa)) {
                VentanaLiquidacion vl = new VentanaLiquidacion(this, true);
                vl.setVisible(true);
            }
            if ("Clafer".equals(empresa)) {
                VentanaLiquidacionClafer vlc = new VentanaLiquidacionClafer(this, true);
                vlc.setVisible(true);
            }
            if ("Giamo".equals(empresa)) {
                VentanaLiquidacionGiamo vl = new VentanaLiquidacionGiamo(this, true);
                vl.setVisible(true);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        // TODO add your handling code here:
        VentanaInformeControlDeEnvases vice = new VentanaInformeControlDeEnvases(this, false);
        vice.setVisible(true);
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItemControlDeEnvasesPorEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemControlDeEnvasesPorEmpresaActionPerformed
        // TODO add your handling code here:
        VentanaInformeControlDeEnvasesPorEmpresa vicepe = new VentanaInformeControlDeEnvasesPorEmpresa(this, false);
        vicepe.setVisible(true);
    }//GEN-LAST:event_jMenuItemControlDeEnvasesPorEmpresaActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoComisiones)) {
                MantenimientoComisiones vmc = new MantenimientoComisiones(this, true);
                vmc.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItemInauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemInauActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarInau)) {
                IngresoInau ii = new IngresoInau(this, false);
                ii.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemInauActionPerformed

    private void jMenuItemInformePinchadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemInformePinchadasActionPerformed
        // TODO add your handling code here:
        VentanaInformePinchadas vip = new VentanaInformePinchadas(this, false);
        vip.setVisible(true);
    }//GEN-LAST:event_jMenuItemInformePinchadasActionPerformed

    private void jMenuItemInformeComisionMamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemInformeComisionMamaActionPerformed
        // TODO add your handling code here:
        VentanaInformeComision vic = new VentanaInformeComision(this, false);
        vic.setVisible(true);
    }//GEN-LAST:event_jMenuItemInformeComisionMamaActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadBuscarFacturas)) {
                MantenimientoFacturas vmf = new MantenimientoFacturas(this, false);
                vmf.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadConsultarDeudaDeCliente)) {
                VentanaConsultaDeudaCliente vcdc = new VentanaConsultaDeudaCliente(this, false);
                vcdc.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarPago)) {
                VentanaIngresarPagoCliente vipc = new VentanaIngresarPagoCliente(this, false);
                vipc.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItemAnepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAnepActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarAnep)) {
                IngresoAnep via = new IngresoAnep(this, false);
                via.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemAnepActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadConfiguracionLiquidaciones)) {
                VentanaConfiguracionLiquidacion vcl = new VentanaConfiguracionLiquidacion(this, true);
                vcl.setVisible(true);
                cargarMenuLiquidaciones();
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoArticulos)) {
                MantenimientoCamiones vmc = new MantenimientoCamiones(this, true);
                vmc.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jMenuItemChequesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemChequesActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarCheque)) {
                IngresoCheque vic = new IngresoCheque(this, false);
                vic.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemChequesActionPerformed

    private void jMenuItemInfChequesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemInfChequesActionPerformed
        // TODO add your handling code here:
        VentanaInformeCheques vic = new VentanaInformeCheques(this, false);
        vic.setVisible(true);
    }//GEN-LAST:event_jMenuItemInfChequesActionPerformed

    private void jMenuItemRetencionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRetencionesActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarRetenciones)) {
                IngresoRetencion ir = new IngresoRetencion(this, true);
                ir.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemRetencionesActionPerformed

    private void jMenuItemReteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReteActionPerformed
        // TODO add your handling code here:
        VentanaInformeRetenciones vir = new VentanaInformeRetenciones(this, false);
        vir.setVisible(true);
    }//GEN-LAST:event_jMenuItemReteActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        // TODO add your handling code here:
        VentanaInformeInau vii = new VentanaInformeInau(this, false);
        vii.setVisible(true);
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItemInformeAnepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemInformeAnepActionPerformed
        // TODO add your handling code here:
        VentanaInformeAnep via = new VentanaInformeAnep(this, false);
        via.setVisible(true);
    }//GEN-LAST:event_jMenuItemInformeAnepActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        // TODO add your handling code here:
        VentanaInformeAnalisisComprasDevoluciones viacd = new VentanaInformeAnalisisComprasDevoluciones(this, false);
        viacd.setVisible(true);
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        // TODO add your handling code here:
        VentanaInformeGastosPorRubro vigr = new VentanaInformeGastosPorRubro(this, false);
        vigr.setVisible(true);
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jMenuItemComprasPorEmpresaMamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemComprasPorEmpresaMamaActionPerformed
        // TODO add your handling code here:
        VentanaInformeComprasPorEmpresa vicpe = new VentanaInformeComprasPorEmpresa(this, false);
        vicpe.setVisible(true);
    }//GEN-LAST:event_jMenuItemComprasPorEmpresaMamaActionPerformed

    private void jMenuItemAnalisisIvasMamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAnalisisIvasMamaActionPerformed
        // TODO add your handling code here:
        VentanaInformeAnalisisIvas viai = new VentanaInformeAnalisisIvas(this, false);
        viai.setVisible(true);
    }//GEN-LAST:event_jMenuItemAnalisisIvasMamaActionPerformed

    private void jMenuItemContadorMamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemContadorMamaActionPerformed
        // TODO add your handling code here:
        VentanaInformeContadora vic = new VentanaInformeContadora(this, false);
        vic.setVisible(true);
    }//GEN-LAST:event_jMenuItemContadorMamaActionPerformed

    private void jMenuItem38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem38ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadGenerarInformeFacturas)) {
                VentanaGenerarArchivoDeFacturas vgaf = new VentanaGenerarArchivoDeFacturas(this, true);
                vgaf.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem38ActionPerformed

    private void jMenuItem41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem41ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadConfiguracionGeneral)) {
                VentanaConfiguracionGeneral vcg = new VentanaConfiguracionGeneral(this, true);
                vcg.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem41ActionPerformed

    private void jMenuItemIngresarFacturasMovilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemIngresarFacturasMovilActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarFacturasMovil)) {
                IngresarFacturasMovil vifm = new IngresarFacturasMovil(this, false);
                vifm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemIngresarFacturasMovilActionPerformed

    private void jMenuItem42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem42ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadFacturarProrrateo)) {
                FacturarProrrateo vfp = new FacturarProrrateo(this, false);
                vfp.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem42ActionPerformed

    private void jMenuItem43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem43ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadVerificarProrrateo)) {
                VentanaVerificarFacturacion vvf = new VentanaVerificarFacturacion(this, false);
                vvf.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem43ActionPerformed

    private void jMenuItem45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem45ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoDocumentosDeVenta)) {
                MantenimientoDoumentosDeVenta vmdv = new MantenimientoDoumentosDeVenta(this, true);
                vmdv.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem45ActionPerformed

    private void jMenuItemBonificacionPorRoturasClaferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemBonificacionPorRoturasClaferActionPerformed
        // TODO add your handling code here:
        InformeBonificacionPorRoturas vibpr = new InformeBonificacionPorRoturas(this, false);
        vibpr.setVisible(true);
    }//GEN-LAST:event_jMenuItemBonificacionPorRoturasClaferActionPerformed

    private void jMenuItemResumenDeEnvasesClaferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemResumenDeEnvasesClaferActionPerformed
        // TODO add your handling code here:
        InformeResumenDeEnvases vire = new InformeResumenDeEnvases(this, false);
        vire.setVisible(true);
    }//GEN-LAST:event_jMenuItemResumenDeEnvasesClaferActionPerformed

    private void jMenuItemControlDeChequesConaproleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemControlDeChequesConaproleActionPerformed
        // TODO add your handling code here:
        InformeControlChequesConaprole viccc = new InformeControlChequesConaprole(this, false);
        viccc.setVisible(true);
    }//GEN-LAST:event_jMenuItemControlDeChequesConaproleActionPerformed

    private void jMenuItemCargaDeLechePorRepartoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCargaDeLechePorRepartoActionPerformed
        // TODO add your handling code here:
        InformeCargaDeLechePorReparto viclpr = new InformeCargaDeLechePorReparto(this, false);
        viclpr.setVisible(true);
    }//GEN-LAST:event_jMenuItemCargaDeLechePorRepartoActionPerformed

    private void jMenuItemInformeTotalVentaProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemInformeTotalVentaProductosActionPerformed
        // TODO add your handling code here:
        InformeTotalVentaPorductosPorReparto vitvp = new InformeTotalVentaPorductosPorReparto(this, false);
        vitvp.setVisible(true);
    }//GEN-LAST:event_jMenuItemInformeTotalVentaProductosActionPerformed

    private void jMenuItemChoferTotalProductosClaferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemChoferTotalProductosClaferActionPerformed
        // TODO add your handling code here:
        InformeChoferTotalProductos victp = new InformeChoferTotalProductos(this, false);
        victp.setVisible(true);
    }//GEN-LAST:event_jMenuItemChoferTotalProductosClaferActionPerformed

    private void jMenuItemResumenDeLiquidacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemResumenDeLiquidacionesActionPerformed
        // TODO add your handling code here:
        InformeResumenLiquidaciones virl = new InformeResumenLiquidaciones(this, false);
        virl.setVisible(true);
    }//GEN-LAST:event_jMenuItemResumenDeLiquidacionesActionPerformed

    private void jMenuItemResumenDeEnvasesReleceYCerramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemResumenDeEnvasesReleceYCerramActionPerformed
        // TODO add your handling code here:
        VentanaInformeResumenDeEnvases vire = new VentanaInformeResumenDeEnvases(this, false);
        vire.setVisible(true);
    }//GEN-LAST:event_jMenuItemResumenDeEnvasesReleceYCerramActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadConfiguracionFacturacion)) {
                VentanaConfiguracionFacturacion vcf = new VentanaConfiguracionFacturacion(this, true);
                vcf.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadImprimirFacturas)) {
                ImprimirGrupoDeFacturas vigf = new ImprimirGrupoDeFacturas(this, false);
                vigf.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        // TODO add your handling code here:
        ResumenDeComprasPorCliente vrdcpc = new ResumenDeComprasPorCliente(this, false);
        vrdcpc.setVisible(true);
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoRoles)) {
                MantenimientoRoles vmr = new MantenimientoRoles(this, false);
                vmr.setVisible(true);
            } else {
               JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoUsuarios)) {
                MantenimientoUsuarios vnu = new MantenimientoUsuarios(this, false);
                vnu.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
        // TODO add your handling code here:
        VentanaInformeFacturasContadora vifc = new VentanaInformeFacturasContadora(this, false);
        vifc.setVisible(true);
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadConsultarLogDeOperaciones)) {
                ConsultarLogDeOperaciones vclo = new ConsultarLogDeOperaciones(this, false);
                vclo.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadEstadoDeCuentaCliente)) {
                VentanaEstadoDeCuenta vec = new VentanaEstadoDeCuenta(this, false);
                vec.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadMantenimientoRepartosCompuestos)) {
                MantenimientoRepartosCompuestos vmrc = new MantenimientoRepartosCompuestos(this, true);
                vmrc.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItemResumenDeEnvasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemResumenDeEnvasesActionPerformed
        // TODO add your handling code here:
        InformeResumenDeEnvasesGiamo vireg = new InformeResumenDeEnvasesGiamo(this, false);
        vireg.setVisible(true);
    }//GEN-LAST:event_jMenuItemResumenDeEnvasesActionPerformed

    private void jMenuItemImportarFacturasManualesGiamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemImportarFacturasManualesGiamoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemImportarFacturasManualesGiamoActionPerformed

    private void jMenuItem37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem37ActionPerformed
        // TODO add your handling code here:
        MantenimientoGruposDeArticulos mga = new MantenimientoGruposDeArticulos(this, false);
        mga.setVisible(true);
    }//GEN-LAST:event_jMenuItem37ActionPerformed

    private void jMenuItemInvGiamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemInvGiamoActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarInventario)) {
                IngresoInventarioGiamo iinv = new IngresoInventarioGiamo(this, false);
                iinv.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemInvGiamoActionPerformed

    private void jMenuItem39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem39ActionPerformed
        // TODO add your handling code here:
        IngresoJornales vij = new IngresoJornales(this, false);
        vij.setVisible(true);
    }//GEN-LAST:event_jMenuItem39ActionPerformed

    private void jMenuItem44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem44ActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarPreciosAutomaticamente)) {
                IngresoAutomaticoDePreciosDePromociones iap = new IngresoAutomaticoDePreciosDePromociones(this, true);
                iap.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem44ActionPerformed

    private void jMenuItem46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem46ActionPerformed
        // TODO add your handling code here:
        VentanaInformeJornales vij = new VentanaInformeJornales(this, false);
        vij.setVisible(true);
    }//GEN-LAST:event_jMenuItem46ActionPerformed

    private void jMenuItemCoeficientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCoeficientesActionPerformed
        // TODO add your handling code here:
        MantenimientoCoeficientesUtilidadCompras vmcuc = new MantenimientoCoeficientesUtilidadCompras(this, true);
        vmcuc.setVisible(true);
    }//GEN-LAST:event_jMenuItemCoeficientesActionPerformed

    private void jMenuItemInformeContadorGiamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemInformeContadorGiamoActionPerformed
        // TODO add your handling code here:
        VentanaInformeContadoraGiamo vicg = new VentanaInformeContadoraGiamo(this, false);
        vicg.setVisible(true);
    }//GEN-LAST:event_jMenuItemInformeContadorGiamoActionPerformed

    private void jMenuItemIngresarFacturasMovilCerramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemIngresarFacturasMovilCerramActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarFacturasMovil)) {
                IngresarFacturasMovil vifm = new IngresarFacturasMovil(this, false);
                vifm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemIngresarFacturasMovilCerramActionPerformed

    private void jMenuItemIngresarFacturasMovilReleceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemIngresarFacturasMovilReleceActionPerformed
        // TODO add your handling code here:
        try {
            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarFacturasMovil)) {
                IngresarFacturasMovilRelece vifm = new IngresarFacturasMovilRelece(this, false);
                vifm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemIngresarFacturasMovilReleceActionPerformed

    private void jMenuItemIngresarFacturasManualesGiamoDesdeArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemIngresarFacturasManualesGiamoDesdeArchivoActionPerformed
        // TODO add your handling code here:
        IngresarFacturasManualesGiamoDesdeArchivo vifmgda = new IngresarFacturasManualesGiamoDesdeArchivo(this, false);
        vifmgda.setVisible(true);
    }//GEN-LAST:event_jMenuItemIngresarFacturasManualesGiamoDesdeArchivoActionPerformed

    private void cargarMenuCompras() {
        //jMenuItem1 = new javax.swing.JMenuItem();
        List<DocumentoDeCompra> tiposDocs;
        try {
            tiposDocs = SistemaMantenimiento.getInstance().devolverDocumentosDeCompra();
            for (DocumentoDeCompra d : tiposDocs) {
                JMenuItem menuItemComp = new javax.swing.JMenuItem();
                menuItemComp.setText(d.getTipoDocumento());
                menuItemComp.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        try {
                            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarCompras)) {
                                IngresarCompras ic = new IngresarCompras(MenuPrincipal.this, false);
                                ic.setTipoDoc(d);
                                ic.setAccion("Nuevo");
                                ic.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(MenuPrincipal.this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception e) {
                            String stakTrace = util.Util.obtenerStackTraceEnString(e);
                            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
                            JOptionPane.showMessageDialog(MenuPrincipal.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                jMenu2.add(menuItemComp);
            }
        } catch (Exception ex) {
            String stakTrace = util.Util.obtenerStackTraceEnString(ex);
            sisUsuarios.registrarExcepcion(ex.toString(), stakTrace);
            JOptionPane.showMessageDialog(MenuPrincipal.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void cargarMenuFacturas() {
        //jMenuItem1 = new javax.swing.JMenuItem();
        try {
            List<DocumentoDeVenta> tiposDocs = SistemaMantenimiento.getInstance().devolverDocumentosDeVenta();
            for (DocumentoDeVenta d : tiposDocs) {
                JMenuItem menuItemVent = new javax.swing.JMenuItem();
                menuItemVent.setText(d.getTipoDocumento());
                menuItemVent.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        try {
                            if (sisUsuarios.tienePermisos(Constantes.ActividadIngresarFacturas)) {
                                ConfiguracionFacturacion cf = SistemaMantenimiento.getInstance().devolverConfiguracionFacturacion();
                                if(!cf.isDetalladaPorArticulo() && "Giamo".equals(empresa)) {
                                    IngresarFacturaManualGiamo vifmg = new IngresarFacturaManualGiamo(MenuPrincipal.this, false);
                                    vifmg.setVisible(true);
                                } else {
                                    IngresoFacturas vif = new IngresoFacturas(MenuPrincipal.this, false);
                                    vif.setTipoDoc(d);
                                    vif.setAccion("Nuevo");
                                    vif.setVisible(true);
                                }
                            } else {
                                JOptionPane.showMessageDialog(MenuPrincipal.this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception e) {
                            String stakTrace = util.Util.obtenerStackTraceEnString(e);
                            sisUsuarios.registrarExcepcion(e.toString(), stakTrace);
                            JOptionPane.showMessageDialog(MenuPrincipal.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                jMenu7.add(menuItemVent);
            }
        } catch (Exception ex) {
            String stakTrace = util.Util.obtenerStackTraceEnString(ex);
            sisUsuarios.registrarExcepcion(ex.toString(), stakTrace);
            JOptionPane.showMessageDialog(MenuPrincipal.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void cargarMenuLiquidaciones() {
        try {
            ConfiguracionLiquidacion cl = SistemaMantenimiento.getInstance().devolverConfiguracionLiquidacion();
            if (!cl.isMostrarAnep()) {
                jMenuItemAnep.setVisible(false);
            } else {
                jMenuItemAnep.setVisible(true);
            }
            if (!cl.isMostrarInau()) {
                jMenuItemInau.setVisible(false);
            } else {
                jMenuItemInau.setVisible(true);
            }
            if (!cl.isRetenciones()) {
                jMenuItemRetenciones.setVisible(false);
            } else {
                jMenuItemRetenciones.setVisible(true);
            }
            if (!cl.isMostrarFiadoClientesCobraChofer()) {
                jMenuItemFiadoChofer.setVisible(false);
            } else {
                jMenuItemFiadoChofer.setVisible(true);
            }
        } catch (Exception ex) {
            String stakTrace = util.Util.obtenerStackTraceEnString(ex);
            sisUsuarios.registrarExcepcion(ex.toString(), stakTrace);
            JOptionPane.showMessageDialog(MenuPrincipal.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    public final void cargarMenuInformes(String empresa) {
        this.empresa = empresa;
        if ("Relece".equals(empresa)) {
            jMenuItemBonificacionPorRoturasClafer.setVisible(false);
            jMenuItemResumenDeEnvasesClafer.setVisible(false);
            jMenuItemInau.setVisible(false);
            jMenuItemInfCheques.setVisible(false);
            jMenuItemAnep.setVisible(false);
            jMenuItemRete.setVisible(false);
            jMenuItemControlDeChequesConaprole.setVisible(false);
            jMenuItemCargaDeLechePorReparto.setVisible(false);
            jMenuItemInformeTotalVentaProductos.setVisible(false);
            jMenuItemChoferTotalProductosClafer.setVisible(false);
            jMenuItemResumenDeLiquidaciones.setVisible(false);
            jMenuItemResumenDeEnvases.setVisible(false);
            jMenuItemImportarFacturasManualesGiamo.setVisible(false);
            //jMenuItemInvGiamo.setVisible(false);
            jMenuItemCoeficientes.setVisible(false);
            jMenuItemInformeContadorGiamo.setVisible(false);
            jMenuItemIngresarFacturasMovil.setVisible(false);
            jMenuItemIngresarFacturasManualesGiamoDesdeArchivo.setVisible(false);
        }
        if ("Clafer".equals(empresa)) {
            jMenuItemControlDeEnvasesPorEmpresa.setVisible(false);
            jMenuItemInformePinchadas.setVisible(false);
            jMenuItemInformeComisionMama.setVisible(false);
            jMenuItemComprasPorEmpresaMama.setVisible(false);
            jMenuItemAnalisisIvasMama.setVisible(false);
            jMenuItemContadorMama.setVisible(false);
            jMenuItemResumenDeEnvasesReleceYCerram.setVisible(false);
            jMenuItemResumenDeEnvases.setVisible(false);
            jMenuItemImportarFacturasManualesGiamo.setVisible(false);
            jMenuItemInvGiamo.setVisible(false);
            jMenuItemCoeficientes.setVisible(false);
            jMenuItemInformeContadorGiamo.setVisible(false);
            jMenuItemIngresarFacturasMovilCerram.setVisible(false);
            jMenuItemIngresarFacturasMovilRelece.setVisible(false);
            jMenuItemIngresarFacturasManualesGiamoDesdeArchivo.setVisible(false);
        }
        if ("Giamo".equals(empresa)) {
            jMenuItemBonificacionPorRoturasClafer.setVisible(false);
            jMenuItemResumenDeEnvasesClafer.setVisible(false);
            jMenuItemResumenDeEnvasesReleceYCerram.setVisible(false);
            jMenuItemInau.setVisible(false);
            jMenuItemAnep.setVisible(false);
            jMenuItemRete.setVisible(false);
            jMenuItemControlDeChequesConaprole.setVisible(false);
            jMenuItemCargaDeLechePorReparto.setVisible(false);
            jMenuItemInformeTotalVentaProductos.setVisible(false);
            jMenuItemChoferTotalProductosClafer.setVisible(false);
            jMenuItemResumenDeLiquidaciones.setVisible(false);
            jMenuItemControlDeEnvasesPorEmpresa.setVisible(false);
            jMenuItemComprasPorEmpresaMama.setVisible(false);
            jMenuItemAnalisisIvasMama.setVisible(false);
            jMenuItemContadorMama.setVisible(false);
            jMenuItemResumenDeEnvasesReleceYCerram.setVisible(false);
            jMenuItemIngresarFacturasMovilCerram.setVisible(false);
            jMenuItemIngresarFacturasMovilRelece.setVisible(false);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem37;
    private javax.swing.JMenuItem jMenuItem38;
    private javax.swing.JMenuItem jMenuItem39;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem41;
    private javax.swing.JMenuItem jMenuItem42;
    private javax.swing.JMenuItem jMenuItem43;
    private javax.swing.JMenuItem jMenuItem44;
    private javax.swing.JMenuItem jMenuItem45;
    private javax.swing.JMenuItem jMenuItem46;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItemAnalisisIvasMama;
    private javax.swing.JMenuItem jMenuItemAnep;
    private javax.swing.JMenuItem jMenuItemBonificacionPorRoturasClafer;
    private javax.swing.JMenuItem jMenuItemCargaDeLechePorReparto;
    private javax.swing.JMenuItem jMenuItemCheques;
    private javax.swing.JMenuItem jMenuItemChoferTotalProductosClafer;
    private javax.swing.JMenuItem jMenuItemCoeficientes;
    private javax.swing.JMenuItem jMenuItemComprasPorEmpresaMama;
    private javax.swing.JMenuItem jMenuItemContadorMama;
    private javax.swing.JMenuItem jMenuItemControlDeChequesConaprole;
    private javax.swing.JMenuItem jMenuItemControlDeEnvasesPorEmpresa;
    private javax.swing.JMenuItem jMenuItemFiadoChofer;
    private javax.swing.JMenuItem jMenuItemImportarFacturasManualesGiamo;
    private javax.swing.JMenuItem jMenuItemInau;
    private javax.swing.JMenuItem jMenuItemInfCheques;
    private javax.swing.JMenuItem jMenuItemInformeAnep;
    private javax.swing.JMenuItem jMenuItemInformeComisionMama;
    private javax.swing.JMenuItem jMenuItemInformeContadorGiamo;
    private javax.swing.JMenuItem jMenuItemInformePinchadas;
    private javax.swing.JMenuItem jMenuItemInformeTotalVentaProductos;
    private javax.swing.JMenuItem jMenuItemIngresarFacturasManualesGiamoDesdeArchivo;
    private javax.swing.JMenuItem jMenuItemIngresarFacturasMovil;
    private javax.swing.JMenuItem jMenuItemIngresarFacturasMovilCerram;
    private javax.swing.JMenuItem jMenuItemIngresarFacturasMovilRelece;
    private javax.swing.JMenuItem jMenuItemInvGiamo;
    private javax.swing.JMenuItem jMenuItemResumenDeEnvases;
    private javax.swing.JMenuItem jMenuItemResumenDeEnvasesClafer;
    private javax.swing.JMenuItem jMenuItemResumenDeEnvasesReleceYCerram;
    private javax.swing.JMenuItem jMenuItemResumenDeLiquidaciones;
    private javax.swing.JMenuItem jMenuItemRete;
    private javax.swing.JMenuItem jMenuItemRetenciones;
    // End of variables declaration//GEN-END:variables
}
