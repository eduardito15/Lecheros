/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.usuarios;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import lecheros.Lecheros;
import sistema.SistemaUsuarios;
import ui.MenuPrincipal;

/**
 *
 * @author Edu
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        jLabelVersion.setText(Lecheros.nombreEmpresa + " Versión 3.0");
        jLabelEspera.setVisible(false);
        jTextFieldNombre.requestFocus();
        jTextFieldNombre.selectAll();
        cargarUsuario();
    }

    private void cargarUsuario(){
        jLabelEspera.setVisible(true);
        jTextFieldNombre.setEnabled(false);
        jPasswordField.setEnabled(false);
        jButtonIngresar.setEnabled(false);
        jButtonSalir.requestFocus();
        Thread worker = new Thread() {
            public void run() {
                try {
                    String nombreUsuario = SistemaUsuarios.getInstance().devolverNombrePorDefecto();
                    jTextFieldNombre.setText(nombreUsuario);
                } catch (IOException ex) {
                    
                } catch (Exception ex) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                    SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                    JOptionPane.showMessageDialog(Login.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        jLabelEspera.setVisible(false);
                        jTextFieldNombre.setEnabled(true);
                        jPasswordField.setEnabled(true);
                        jButtonIngresar.setEnabled(true);
                        jTextFieldNombre.requestFocus();
                        jTextFieldNombre.selectAll();
                    }
                });
            }
        };
        worker.start();
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
        jTextFieldNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPasswordField = new javax.swing.JPasswordField();
        jButtonIngresar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabelEspera = new javax.swing.JLabel();
        jLabelVersion = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ingresar");
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Ingresar");

        jLabel1.setText("Nombre:");

        jTextFieldNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNombreKeyPressed(evt);
            }
        });

        jLabel2.setText("Contraseña:");

        jPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordFieldKeyPressed(evt);
            }
        });

        jButtonIngresar.setText("Ingresar");
        jButtonIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIngresarActionPerformed(evt);
            }
        });
        jButtonIngresar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonIngresarKeyPressed(evt);
            }
        });

        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });
        jButtonSalir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonSalirKeyPressed(evt);
            }
        });

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        jLabelVersion.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabelVersion.setForeground(new java.awt.Color(0, 0, 255));
        jLabelVersion.setText("Versión");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelVersion)
                    .addComponent(jLabelTitulo)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextFieldNombre)
                        .addComponent(jPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonIngresar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelEspera)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelEspera)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelTitulo)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonIngresar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSalir)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelVersion)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIngresarActionPerformed
        try {
            // TODO add your handling code here:
            String nombre = jTextFieldNombre.getText().trim();
            String contrasena = jPasswordField.getText();
            if(SistemaUsuarios.getInstance().existeUsuario(nombre, contrasena)){
                MenuPrincipal mp = new MenuPrincipal();
                mp.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Nombre de usuario y/o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                jTextFieldNombre.requestFocus();
                jTextFieldNombre.selectAll();
            }
        } catch (Exception exp) {
            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
            JOptionPane.showMessageDialog(Login.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
        
    }//GEN-LAST:event_jButtonIngresarActionPerformed

    private void jButtonIngresarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonIngresarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonIngresar.doClick();
        }
    }//GEN-LAST:event_jButtonIngresarKeyPressed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        //this.dispose();
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jButtonSalirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonSalirKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonSalir.doClick();
        }
    }//GEN-LAST:event_jButtonSalirKeyPressed

    private void jTextFieldNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNombreKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jPasswordField.requestFocus();
            jPasswordField.selectAll();
        }
    }//GEN-LAST:event_jTextFieldNombreKeyPressed

    private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_formFocusLost

    private void jPasswordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordFieldKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonIngresar.requestFocus();
        }
    }//GEN-LAST:event_jPasswordFieldKeyPressed

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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonIngresar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelVersion;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JTextField jTextFieldNombre;
    // End of variables declaration//GEN-END:variables
}
