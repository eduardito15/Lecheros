/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.compras;

import ui.mantenimiento.*;
import dao.GenericDAO;
import dominio.Articulo;
import dominio.Precio;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import sistema.SistemaMantenimientoArticulos;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class MantenimientoPreciosDesdeCompras extends javax.swing.JDialog {

    private SistemaMantenimientoArticulos sis;
    private Articulo articulo;
    private Date fecha;
    
    private boolean mostrarMensajeFechaIncorrecta = true;
    /**
     * Creates new form MantenimientoPrecios
     */
    public MantenimientoPreciosDesdeCompras(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sis = SistemaMantenimientoArticulos.getInstance();
        jTextFieldPrecioCompra.requestFocus();
        jTextFieldPrecioVenta.selectAll();
        mostrarMensajeFechaIncorrecta=false;
        jLabelFechaIncorrecta.setVisible(false);
        //agregarEnterCampoFecha();
        //cargarPreciosParaFecha();
    }
    
    public final void agregarEnterCampoFecha(){
        jLabelFechaIncorrecta.setVisible(false);
        jDateChooserFecha.setDate(fecha);
        jDateChooserFecha.getDateEditor().getUiComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jTextFieldPrecioCompra.requestFocus();
                    jTextFieldPrecioCompra.selectAll();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Calendar c = new GregorianCalendar();

                String annio = Integer.toString(c.get(Calendar.YEAR));

                switch (((JTextField) jDateChooserFecha.getDateEditor().getUiComponent()).getText().length()) {
                    case 2:
                        ((JTextField) jDateChooserFecha.getDateEditor().getUiComponent()).setText(((JTextField) jDateChooserFecha.getDateEditor().getUiComponent()).getText() + "/");
                        break;
                    case 5:
                        ((JTextField) jDateChooserFecha.getDateEditor().getUiComponent()).setText(((JTextField) jDateChooserFecha.getDateEditor().getUiComponent()).getText() + "/20");
                        break;
                }
            }
        });
        jDateChooserFecha.getDateEditor().getUiComponent().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                mostrarMensajeFechaIncorrecta = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (mostrarMensajeFechaIncorrecta) {
                    Date f = jDateChooserFecha.getDate();
                    if (f == null || f.after(jDateChooserFecha.getMaxSelectableDate()) || f.before(jDateChooserFecha.getMinSelectableDate())) {
                        //JOptionPane.showMessageDialog(IngresarCompras.this, "Fecha Incorrecta. Ingrese una fecha correcta. Debe ingresar los dos numeros del dia, los dos numeros del mes y los dos ultimos numeros del año");
                        if (f == null) {
                            jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");
                        } else if (f.after(jDateChooserFecha.getMaxSelectableDate()) || f.before(jDateChooserFecha.getMinSelectableDate())) {
                            jLabelFechaIncorrecta.setText("La fecha debe ser entre el 2000 y el 2100");
                        }
                        jLabelFechaIncorrecta.setVisible(true);
                        jDateChooserFecha.getDateEditor().getUiComponent().requestFocus();
                    } else {
                        jLabelFechaIncorrecta.setVisible(false);
                        jTextFieldPrecioCompra.requestFocus();
                        jTextFieldPrecioCompra.selectAll();
                    }
                }
                mostrarMensajeFechaIncorrecta = true;
            }
        });
        jDateChooserFecha.getCalendarButton().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarMensajeFechaIncorrecta = false;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                jDateChooserFecha.getDateEditor().getUiComponent().requestFocus();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
    public void cargarPreciosParaFecha() {
        try {
            Precio p = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(articulo, fecha);
            jDateChooserFecha.setDate(fecha);
            jTextFieldPrecioCompra.setText(Double.toString(p.getPrecioCompra()));
            jTextFieldPrecioVenta.setText(Double.toString(p.getPrecioVenta()));
        } catch (Exception ex) {
            String stakTrace = util.Util.obtenerStackTraceEnString(ex);
            SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooserDialog1 = new datechooser.beans.DateChooserDialog();
        dateChooserDialog2 = new datechooser.beans.DateChooserDialog();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldPrecioCompra = new javax.swing.JTextField();
        jTextFieldPrecioVenta = new javax.swing.JTextField();
        jButtonGuardar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jDateChooserFecha = new com.toedter.calendar.JDateChooser();
        jLabelFechaIncorrecta = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Precios");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabel1.setText("Nuevo Precio");

        jLabel3.setText("Precio Compra:");

        jLabel4.setText("Precio Venta:");

        jTextFieldPrecioCompra.setText("0.0");
        jTextFieldPrecioCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldPrecioCompraKeyPressed(evt);
            }
        });

        jTextFieldPrecioVenta.setEditable(false);
        jTextFieldPrecioVenta.setText("0.0");
        jTextFieldPrecioVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldPrecioVentaKeyPressed(evt);
            }
        });

        jButtonGuardar.setText("Guardar");
        jButtonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarActionPerformed(evt);
            }
        });
        jButtonGuardar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonGuardarKeyPressed(evt);
            }
        });

        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });

        jLabel2.setText("Fecha:");

        jDateChooserFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserFecha.setEnabled(false);
        jDateChooserFecha.setMaxSelectableDate(new java.util.Date(4102455708000L));
        jDateChooserFecha.setMinSelectableDate(new java.util.Date(946699266000L));

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelFechaIncorrecta)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDateChooserFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jTextFieldPrecioVenta))))))
                .addGap(70, 70, 70))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelFechaIncorrecta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(jButtonGuardar)
                .addGap(16, 16, 16)
                .addComponent(jButtonSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        // TODO add your handling code here:
        //Calendar fechaSeleccionada = dateChooserComboFecha.getSelectedDate();
        //GenericDAO.getGenericDAO().actualizar(articulo);
        Date fechaSeleccionada = jDateChooserFecha.getDate();
        try{
            Double precioCompra = Double.parseDouble(jTextFieldPrecioCompra.getText());
            Double precioVenta = Double.parseDouble(jTextFieldPrecioVenta.getText());
            if("0.0".equals(jTextFieldPrecioCompra.getText().trim())){
                //Es vacio el precio de Compra
                int resp = JOptionPane.showConfirmDialog(this, "Queres dejar el precio de compra en 0? ","Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(resp == JOptionPane.YES_OPTION){
                    if("0.0".equals(jTextFieldPrecioVenta.getText().trim())||"".equals(jTextFieldPrecioVenta.getText().trim())){
                        JOptionPane.showMessageDialog(this, "El precio de venta no puede ser ni vacio ni ser 0","Información",JOptionPane.INFORMATION_MESSAGE);
                        jTextFieldPrecioVenta.requestFocus();
                        jTextFieldPrecioVenta.selectAll();
                    } else {
                        //Si esta todo bien con los precios, reviso si hay un precio por esa fecha para ese articulo.
                        if(precioCompra>precioVenta){
                            JOptionPane.showMessageDialog(this, "El precio de Compra no puede ser mayor que el precio de Venta.","Información",JOptionPane.INFORMATION_MESSAGE);
                            jTextFieldPrecioCompra.requestFocus();
                            jTextFieldPrecioCompra.selectAll();
                        } else {
                            Precio precioViejo = sis.devolverPrecioPorArticuloyFecha(articulo, fechaSeleccionada);
                            if (precioViejo == null) {
                                if (sis.guardarPrecio(articulo, fechaSeleccionada, precioCompra, precioVenta)) {
                                    SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoArticulos, "Actualizo el precio del articulo " + articulo.getCodigo() + "a Precio de Compra: " + precioCompra + " y Precio Venta " + precioVenta);
                                    this.dispose();
                                } else {
                                    JOptionPane.showMessageDialog(this, "Error al guardar el precio.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                int respuesta = JOptionPane.showConfirmDialog(this, "Ya existe una combinacion de precios para esa fecha. Queres modificarla? ", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                if (respuesta == JOptionPane.YES_OPTION) {
                                    precioViejo.setPrecioCompra(precioCompra);
                                    precioViejo.setPrecioVenta(precioVenta);
                                    if (sis.actualizarPrecio(precioViejo)) {
                                        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoArticulos, "Actualizo el precio del articulo " + articulo.getCodigo() + "a Precio de Compra: " + precioCompra + " y Precio Venta " + precioVenta);
                                        this.dispose();
                                    } else {
                                        JOptionPane.showMessageDialog(this, "Error al guardar el precio.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    jTextFieldPrecioCompra.requestFocus();
                    jTextFieldPrecioCompra.selectAll();
                }
            } else {
                //No es vacio el precio de compra
                if ("0.0".equals(jTextFieldPrecioVenta.getText().trim()) || "".equals(jTextFieldPrecioVenta.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "El precio de venta no puede ser ni vacio ni ser 0", "Información", JOptionPane.INFORMATION_MESSAGE);
                    jTextFieldPrecioVenta.requestFocus();
                    jTextFieldPrecioVenta.selectAll();
                } else {
                    //Si esta todo bien con los precios, reviso si hay un precio por esa fecha para ese articulo.
                    if(precioCompra>precioVenta){
                            JOptionPane.showMessageDialog(this, "El precio de Compra no puede ser mayor que el precio de Venta.","Información",JOptionPane.INFORMATION_MESSAGE);
                            jTextFieldPrecioCompra.requestFocus();
                            jTextFieldPrecioCompra.selectAll();
                    } else {
                        sis.actualizarArticulo(articulo);
                        SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoArticulos, "Actualizo el precio del articulo " + articulo.getCodigo());
                        Precio precioViejo = sis.devolverPrecioPorArticuloyFecha(articulo, fechaSeleccionada);
                        if (precioViejo == null) {
                            if (sis.guardarPrecio(articulo, fechaSeleccionada, precioCompra, precioVenta)) {
                                SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoArticulos, "Actualizo el precio del articulo " + articulo.getCodigo() + "a Precio de Compra: " + precioCompra + " y Precio Venta " + precioVenta);
                                this.dispose();
                            } else {
                                JOptionPane.showMessageDialog(this, "Error al guardar el precio.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            int respuesta = JOptionPane.showConfirmDialog(this, "Ya existe una combinacion de precios para esa fecha. Queres modificarla? ", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (respuesta == JOptionPane.YES_OPTION) {
                                precioViejo.setPrecioCompra(precioCompra);
                                precioViejo.setPrecioVenta(precioVenta);
                                if (sis.actualizarPrecio(precioViejo)) {
                                    SistemaUsuarios.getInstance().registrarOperacion(Constantes.ActividadMantenimientoArticulos, "Actualizo el precio del articulo " + articulo.getCodigo() + "a Precio de Compra: " + precioCompra + " y Precio Venta " + precioVenta);
                                    this.dispose();
                                } else {
                                    JOptionPane.showMessageDialog(this, "Error al guardar el precio.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Los precios deben ser numericos.","Información", JOptionPane.INFORMATION_MESSAGE);
            jTextFieldPrecioCompra.requestFocus();
            jTextFieldPrecioCompra.selectAll();
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jButtonGuardarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonGuardarKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            jButtonGuardar.doClick();
        }
    }//GEN-LAST:event_jButtonGuardarKeyPressed

    private void jTextFieldPrecioCompraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPrecioCompraKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            jButtonGuardar.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldPrecioCompraKeyPressed

    private void jTextFieldPrecioVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPrecioVentaKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            jButtonGuardar.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldPrecioVentaKeyPressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        cargarPreciosParaFecha();
    }//GEN-LAST:event_formWindowActivated

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
            java.util.logging.Logger.getLogger(MantenimientoPreciosDesdeCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MantenimientoPreciosDesdeCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MantenimientoPreciosDesdeCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MantenimientoPreciosDesdeCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MantenimientoPreciosDesdeCompras dialog = new MantenimientoPreciosDesdeCompras(new javax.swing.JFrame(), true);
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
    private datechooser.beans.DateChooserDialog dateChooserDialog1;
    private datechooser.beans.DateChooserDialog dateChooserDialog2;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonSalir;
    private com.toedter.calendar.JDateChooser jDateChooserFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JTextField jTextFieldPrecioCompra;
    private javax.swing.JTextField jTextFieldPrecioVenta;
    // End of variables declaration//GEN-END:variables

    /**
     * @param articulo the articulo to set
     */
    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
}
