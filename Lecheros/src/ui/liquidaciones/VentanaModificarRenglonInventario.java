/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.liquidaciones;

import dominio.Articulo;
import dominio.InventarioRenglon;
import dominio.Precio;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import sistema.SistemaMantenimientoArticulos;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class VentanaModificarRenglonInventario extends javax.swing.JDialog {

    private InventarioRenglon invRenglon;
    private final SistemaMantenimientoArticulos sisArticulos;
    /**
     * Creates new form VentanaModificarRenglonInventario
     */
    public VentanaModificarRenglonInventario(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sisArticulos = SistemaMantenimientoArticulos.getInstance();
        jTextFieldCodigoArticuloMod.requestFocus();
        jTextFieldCodigoArticuloMod.selectAll();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelTitulo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldCodigoArticuloMod = new javax.swing.JTextField();
        jLabelDescripcionArtMod = new javax.swing.JLabel();
        jTextFieldCantidadArticuloMod = new javax.swing.JTextField();
        jButtonAceptar = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Modificar Renglon");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Modificar Renglon");

        jLabel1.setText("Artículo:");

        jLabel2.setText("Cantidad:");

        jTextFieldCodigoArticuloMod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldCodigoArticuloModFocusLost(evt);
            }
        });
        jTextFieldCodigoArticuloMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCodigoArticuloModKeyPressed(evt);
            }
        });

        jLabelDescripcionArtMod.setText("Descripción");

        jTextFieldCantidadArticuloMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCantidadArticuloModKeyPressed(evt);
            }
        });

        jButtonAceptar.setText("Aceptar");
        jButtonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAceptarActionPerformed(evt);
            }
        });
        jButtonAceptar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonAceptarKeyPressed(evt);
            }
        });

        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jLabel12.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabelDescripcionArtMod)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel12))
                                .addComponent(jTextFieldCodigoArticuloMod)
                                .addComponent(jTextFieldCantidadArticuloMod, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButtonAceptar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonCancelar, javax.swing.GroupLayout.Alignment.LEADING))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelTitulo)
                        .addGap(14, 14, 14)))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabelTitulo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldCodigoArticuloMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDescripcionArtMod)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldCantidadArticuloMod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButtonAceptar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCancelar)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldCodigoArticuloModFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldCodigoArticuloModFocusLost
        // TODO add your handling code here:
        if ("".equals(jTextFieldCodigoArticuloMod.getText().trim())) {
            jLabel12.setText("El codigo de producto no es valido.");
            jLabelDescripcionArtMod.setText("");
            jTextFieldCodigoArticuloMod.requestFocus();
            jTextFieldCodigoArticuloMod.selectAll();
        } else {
            try {
                int codigo = Integer.parseInt(jTextFieldCodigoArticuloMod.getText());
                Articulo a = sisArticulos.devolverArticuloPorCodigo(codigo);
                if (a != null) {
                    jLabelDescripcionArtMod.setText(a.getDescripcion());
                    jLabel12.setText("");
                } else {
                    jLabel12.setText("El codigo de producto no es valido.");
                    jLabelDescripcionArtMod.setText("");
                    jTextFieldCodigoArticuloMod.requestFocus();
                    jTextFieldCodigoArticuloMod.selectAll();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "El codigo debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldCodigoArticuloMod.requestFocus();
                jTextFieldCodigoArticuloMod.selectAll();
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTextFieldCodigoArticuloModFocusLost

    private void jTextFieldCodigoArticuloModKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodigoArticuloModKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextFieldCantidadArticuloMod.requestFocus();
            jTextFieldCantidadArticuloMod.selectAll();
        }
    }//GEN-LAST:event_jTextFieldCodigoArticuloModKeyPressed

    private void jTextFieldCantidadArticuloModKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCantidadArticuloModKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonAceptar.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldCantidadArticuloModKeyPressed

    private void jButtonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAceptarActionPerformed
        // TODO add your handling code here:
        try{
            int codigo = Integer.parseInt(jTextFieldCodigoArticuloMod.getText().trim());
            Articulo a = sisArticulos.devolverArticuloPorCodigo(codigo);
            double cantidad = Double.parseDouble(jTextFieldCantidadArticuloMod.getText().trim());
            invRenglon.setArticulo(a);
            invRenglon.setCantidad(cantidad);
            Precio p = sisArticulos.devolverPrecioParaFechaPorArticulo(a, invRenglon.getInventario().getFecha());
            if (p != null) {
                //Cargo el objeto renglon de compra
                invRenglon.setPrecio(p.getPrecioVenta());
                invRenglon.setSubtotal(p.getPrecioVenta() * invRenglon.getCantidad());
                invRenglon.setIva((invRenglon.getSubtotal() * a.getIva().getPorcentaje() / 100));
                invRenglon.setTotal(invRenglon.getSubtotal() + invRenglon.getIva());

            } else {
                JOptionPane.showMessageDialog(this, "No existen precios para ese articulo.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldCodigoArticuloMod.requestFocus();
                jTextFieldCodigoArticuloMod.selectAll();
            }
            this.dispose();
        } catch (NumberFormatException ne){
            JOptionPane.showMessageDialog(this, "El codigo y la cantidad deben ser numeros.", "Información", JOptionPane.INFORMATION_MESSAGE);
            jTextFieldCodigoArticuloMod.requestFocus();
            jTextFieldCodigoArticuloMod.selectAll();
        } catch (Exception ex) {
            String stakTrace = util.Util.obtenerStackTraceEnString(ex);
            SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonAceptarActionPerformed

    private void jButtonAceptarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonAceptarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonAceptar.doClick();
        }
    }//GEN-LAST:event_jButtonAceptarKeyPressed

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        jTextFieldCodigoArticuloMod.setText(Integer.toString(invRenglon.getArticulo().getCodigo()));
        jLabelDescripcionArtMod.setText(invRenglon.getArticulo().getDescripcion());
        jTextFieldCantidadArticuloMod.setText(Double.toString(invRenglon.getCantidad()));
        jTextFieldCodigoArticuloMod.requestFocus();
        jTextFieldCodigoArticuloMod.selectAll();
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
            java.util.logging.Logger.getLogger(VentanaModificarRenglonInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaModificarRenglonInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaModificarRenglonInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaModificarRenglonInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaModificarRenglonInventario dialog = new VentanaModificarRenglonInventario(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelDescripcionArtMod;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JTextField jTextFieldCantidadArticuloMod;
    private javax.swing.JTextField jTextFieldCodigoArticuloMod;
    // End of variables declaration//GEN-END:variables

    /**
     * @param invRenglon the invRenglon to set
     */
    public void setInvRenglon(InventarioRenglon invRenglon) {
        this.invRenglon = invRenglon;
    }
}
