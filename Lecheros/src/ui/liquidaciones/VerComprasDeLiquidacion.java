/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.liquidaciones;

import dominio.CoeficienteUtilidadCompras;
import dominio.Compra;
import dominio.CompraRenglon;
import dominio.Liquidacion;
import dominio.Reparto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import lecheros.Lecheros;
import org.hibernate.HibernateException;
import sistema.SistemaCompras;
import sistema.SistemaLiquidaciones;
import sistema.SistemaMantenimientoArticulos;
import sistema.SistemaUsuarios;
import ui.compras.IngresarCompras;
import ui.usuarios.Constantes;
import util.Util;

/**
 *
 * @author Edu
 */
public class VerComprasDeLiquidacion extends javax.swing.JFrame {

    private SistemaCompras sisCompras;
    private final SistemaLiquidaciones sisLiquidaciones;
    private Liquidacion l;
    List<Compra> compras;
    
    private DefaultTableModel modelo;
    
    private DecimalFormat df;
    /**
     * Creates new form VerComprasDeLiquidacion
     */
    public VerComprasDeLiquidacion() {
        //super(parent, modal);
        initComponents();
        sisCompras = SistemaCompras.getInstance();
        sisLiquidaciones = SistemaLiquidaciones.getInstance();
        df = new DecimalFormat("0.00");
        jLabelEspera.setVisible(false);
        inicializarTablaCompras();
    }
    
    public final void inicializarTablaCompras() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Tipo");
        modelo.addColumn("Numero");
        modelo.addColumn("Total");
        modelo.addColumn("Total a Precio de Venta");
        modelo.addColumn("Utilidad");
        
        jTableCompras.setModel(modelo);
        
        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem verItem = new JMenuItem("Ver");
        verItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da ver en el menu con click derecho sobre la fila de la tabla
               IngresarCompras ic = new IngresarCompras(VerComprasDeLiquidacion.this, false);
               ic.setAccion("Ver");
               Compra c = compras.get(jTableCompras.getSelectedRow());
               ic.setCompra(c);
               ic.setTipoDoc(c.getTipoDocumento());
               ic.setVisible(true);
            }

        });
        popupMenu.add(verItem);
        JMenuItem modificarItem = new JMenuItem("Modificar");
        modificarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadIngresarCompras)) {
                        //Que hacer cuando da modificar en el menu con click derecho sobre la fila de la tabla
                        IngresarCompras ic = new IngresarCompras(VerComprasDeLiquidacion.this, false);
                        ic.setAccion("Modificar");
                        Compra c = compras.get(jTableCompras.getSelectedRow());
                        ic.setCompra(c);
                        ic.setTipoDoc(c.getTipoDocumento());
                        ic.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        popupMenu.add(modificarItem);
        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        eliminarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da eliminar en el menu con click derecho sobre la fila de la tabla
                try {
                    if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadIngresarCompras)) {   
                        Compra c = compras.get(jTableCompras.getSelectedRow());
                        int resp = JOptionPane.showConfirmDialog(VerComprasDeLiquidacion.this, "Seguro que quiere eliminar la compra " + c.getTipoDocumento().getTipoDocumento() + " con el numero " + c.getNumero()   + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(resp == JOptionPane.YES_OPTION){
                            try{
                                if(sisCompras.eliminarCompra(c)){
                                    JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, "La compra se elimino correctamente" , "Información", JOptionPane.INFORMATION_MESSAGE);
                                    //jButtonBuscar.doClick();
                                } 
                            } catch (HibernateException he){
                                JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, "Error al eliminar la compra." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                            } catch (Exception ex) {
                                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                                JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                } 
            }

        });
        popupMenu.add(eliminarItem);
        JMenuItem agregarFechaLiquidacion = new JMenuItem("Agregar Fecha de Liquidación");
        agregarFechaLiquidacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadAgregarBoletaALiquidacion)) {  
                    Compra c = compras.get(jTableCompras.getSelectedRow());
                    AsignarFechaDeLiquidacionACompra vaflc = new AsignarFechaDeLiquidacionACompra(VerComprasDeLiquidacion.this, true);
                    vaflc.setC(c);
                    vaflc.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                }
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        });
        popupMenu.add(agregarFechaLiquidacion);
        JMenuItem sacarDeLiquidacionItem = new JMenuItem("Sacar de Liquidación");
        sacarDeLiquidacionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da sacar de liquidacion en el menu con click derecho sobre la fila de la tabla
                try {
                if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadQuitarBoletaDeLiquidacion)) {  
                    Compra c = compras.get(jTableCompras.getSelectedRow());
                    SimpleDateFormat formatter;
                    formatter = new SimpleDateFormat("dd-MM-yyyy");
                    int resp = JOptionPane.showConfirmDialog(VerComprasDeLiquidacion.this, "Seguro que quiere sacar la compra de la liquidación del " + formatter.format(c.getFechaLiquidacion())  + " del reparto:  " + c.getReparto()   + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(resp == JOptionPane.YES_OPTION){
                        try{
                            c.setFechaLiquidacion(null);
                            if(sisCompras.actualizarCompra(c)){
                                JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, "La compra se saco correctamente de la liquidación" , "Información", JOptionPane.INFORMATION_MESSAGE);
                            } 
                        } 
                        catch (HibernateException he){
                            JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, "Error al sacar la compra de la liquidación." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception exp) {
                            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                            JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } 
                } else {
                    JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception exp) {
                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
            }
        });
        popupMenu.add(sacarDeLiquidacionItem);
        JMenuItem recalcularUtilidadItem = new JMenuItem("Recalcular Utilidad");
        recalcularUtilidadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da eliminar en el menu con click derecho sobre la fila de la tabla
                try {
                    if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadIngresarCompras)) {   
                        Compra c = compras.get(jTableCompras.getSelectedRow());
                        int resp = JOptionPane.showConfirmDialog(VerComprasDeLiquidacion.this, "Seguro que quiere recalcular la utilidad de la compra " + c.getTipoDocumento().getTipoDocumento() + " con el numero " + c.getNumero()   + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(resp == JOptionPane.YES_OPTION){
                            try{
                                if(sisCompras.recalcularUtilidadCompra(c)){
                                    JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, "La utilidad de la compra se recalculo correctamente." , "Información", JOptionPane.INFORMATION_MESSAGE);
                                    //jButtonBuscar.doClick();
                                } 
                            } catch (HibernateException he){
                                JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, "Error al recalcular la utilidad de la compra." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                            } catch (Exception ex) {
                                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                                JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                } 
            }

        });
        popupMenu.add(recalcularUtilidadItem);
        jTableCompras.setComponentPopupMenu(popupMenu);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCompras = new javax.swing.JTable();
        jLabelFecha = new javax.swing.JLabel();
        jLabelReparto = new javax.swing.JLabel();
        jLabelTotalPrecioDeCompra = new javax.swing.JLabel();
        jLabelTotalPrecioDeVenta = new javax.swing.JLabel();
        jLabelTotalUtilidad = new javax.swing.JLabel();
        jLabelEspera = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ver Compras de Liquidación");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabel1.setText("Fecha:");

        jLabel2.setText("Reparto:");

        jLabel3.setText("Total Precio de Compra:");

        jLabel4.setText("Total Precio de Venta:");

        jLabel5.setText("Utilidad:");

        jTableCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tipo", "Numero", "Total", "Total a Precio de Venta", "Utilidad"
            }
        ));
        jTableCompras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableComprasKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTableCompras);

        jLabelFecha.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelFecha.setForeground(new java.awt.Color(0, 0, 255));
        jLabelFecha.setText("Fecha");

        jLabelReparto.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelReparto.setForeground(new java.awt.Color(0, 0, 255));
        jLabelReparto.setText("Reparto");

        jLabelTotalPrecioDeCompra.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelTotalPrecioDeCompra.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTotalPrecioDeCompra.setText("0");

        jLabelTotalPrecioDeVenta.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelTotalPrecioDeVenta.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTotalPrecioDeVenta.setText("0");

        jLabelTotalUtilidad.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelTotalUtilidad.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTotalUtilidad.setText("0");

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelTotalPrecioDeCompra)
                        .addGap(107, 107, 107)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTotalPrecioDeVenta)
                        .addGap(118, 118, 118)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTotalUtilidad))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelReparto)
                            .addComponent(jLabelFecha))
                        .addGap(18, 18, 18)
                        .addComponent(jLabelEspera)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabelFecha))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabelReparto)))
                    .addComponent(jLabelEspera, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabelTotalPrecioDeCompra)
                    .addComponent(jLabelTotalPrecioDeVenta)
                    .addComponent(jLabelTotalUtilidad))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        jLabelEspera.setVisible(true);
        Thread worker = new Thread() {
            public void run() {

                inicializarTablaCompras();
                if (l != null) {
                    try {
                        compras = sisLiquidaciones.devolverComprasParaFechaYReparto(l.getFecha(), l.getReparto());
                    } catch (ParseException ex) {
                        Logger.getLogger(VerComprasDeLiquidacion.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                        SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                        JOptionPane.showMessageDialog(VerComprasDeLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    for (Compra c : compras) {
                        cargarCompraEnTabla(c);
                    }
                    SimpleDateFormat formatter;
                    formatter = new SimpleDateFormat("dd-MM-yyyy");
                    jLabelFecha.setText(formatter.format(l.getFecha()));
                    jLabelReparto.setText(l.getReparto().toString());
                    Reparto repartoCompartido = sisLiquidaciones.tieneRepartoCompartido(l.getReparto());
                    if (repartoCompartido != null) {
                        jLabelReparto.setText(l.getReparto().getNombre() + " Y " + repartoCompartido.getNombre());
                    }
                    jLabelTotalPrecioDeCompra.setText(df.format(l.getCompras()).replace(',', '.'));
                    jLabelTotalPrecioDeVenta.setText(df.format(l.getCompras() + l.getUtilidad()).replace(',', '.'));
                    jLabelTotalUtilidad.setText(df.format(l.getUtilidad()).replace(',', '.'));
                }

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        jLabelEspera.setVisible(false);
                    }
                });
            }
        };
        worker.start();
        
    }//GEN-LAST:event_formWindowActivated

    private void jTableComprasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableComprasKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }//GEN-LAST:event_jTableComprasKeyPressed

    public void cargarCompraEnTabla(Compra c){
        Object[] object = new Object[5];
        if(c.getTipoDocumento().isSuma()){
            object[0] = c.getTipoDocumento().getTipoDocumento();
            object[1] = c.getNumero();
            object[2] = df.format(c.getTotal()).replace(',', '.');
            object[3] = df.format(c.getTotalAPrecioDeVentaConIva()).replace(',', '.');
            if(!Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaGiamo)) {
                //No es GIAMO
                object[4] = df.format(c.getTotalAPrecioDeVentaConIva()-c.getTotal()).replace(',', '.');
            } else {
                //ES GIAMO
                double utilidadGiamo = 0;
                if (Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaGiamo)) {
                    for (CompraRenglon cr : c.getRenglones()) {
                        if (Util.esLeche(cr.getArticulo()) || Util.esEnvase(cr.getArticulo())) {
                            utilidadGiamo = Util.round(utilidadGiamo + (cr.getTotalPrecioVentaConIva() - cr.getTotal()),2);
                        } else {
                            CoeficienteUtilidadCompras cuc = SistemaMantenimientoArticulos.getInstance().devolverCoeficienteUtilidadPorArticulo(cr.getArticulo());
                            if (cuc != null) {
                                utilidadGiamo = Util.round(utilidadGiamo + cr.getTotal() * cuc.getCoeficiente(),2);
                            } else {
                                utilidadGiamo = Util.round(utilidadGiamo + (cr.getTotalPrecioVentaConIva() - cr.getTotal()),2);
                            }
                        }
                    }
                }
                object[4] = df.format(utilidadGiamo).replace(',', '.');
            }
        } else {
            object[0] = c.getTipoDocumento().getTipoDocumento();
            object[1] = c.getNumero();
            object[2] = "-" + df.format(c.getTotal()).replace(',', '.');
            object[3] = "-" + df.format(c.getTotalAPrecioDeVentaConIva()).replace(',', '.');
            if(!Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaGiamo)) {
                //No es GIAMO
                object[4] = df.format(c.getTotalAPrecioDeVentaConIva()-c.getTotal()).replace(',', '.');
            } else {
                //Es GIAMO
                double utilidadGiamo = 0;
                if (Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaGiamo)) {
                    for (CompraRenglon cr : c.getRenglones()) {
                        if (Util.esLeche(cr.getArticulo()) || Util.esEnvase(cr.getArticulo())) {
                            double cantARestar = cr.getTotalPrecioVentaConIva() - cr.getTotal();
                            utilidadGiamo = Util.round(utilidadGiamo - cantARestar,2);
                        } else {
                            CoeficienteUtilidadCompras cuc = SistemaMantenimientoArticulos.getInstance().devolverCoeficienteUtilidadPorArticulo(cr.getArticulo());
                            if (cuc != null) {
                                double cantARestar = cr.getTotal() * cuc.getCoeficiente();
                                utilidadGiamo = Util.round(utilidadGiamo - cantARestar,2);
                            } else {
                                double cantARestar = cr.getTotalPrecioVentaConIva() - cr.getTotal();
                                utilidadGiamo = Util.round(utilidadGiamo - cantARestar, 2);
                            }
                        }
                    }
                }
                object[4] = df.format(utilidadGiamo).replace(',', '.');
            }
        }
        modelo.addRow(object);
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
            java.util.logging.Logger.getLogger(VerComprasDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VerComprasDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VerComprasDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VerComprasDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VerComprasDeLiquidacion dialog = new VerComprasDeLiquidacion();
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelReparto;
    private javax.swing.JLabel jLabelTotalPrecioDeCompra;
    private javax.swing.JLabel jLabelTotalPrecioDeVenta;
    private javax.swing.JLabel jLabelTotalUtilidad;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableCompras;
    // End of variables declaration//GEN-END:variables

    /**
     * @param l the l to set
     */
    public void setL(Liquidacion l) {
        this.l = l;
    }
}
