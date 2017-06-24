/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.usuarios;

import dominio.usuarios.Rol;
import dominio.usuarios.Usuario;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sistema.SistemaUsuarios;
import ui.informes.VentanaInformeCheques;

/**
 *
 * @author Edu
 */
public class MantenimientoUsuarios extends javax.swing.JDialog {

    private final SistemaUsuarios sisUsuarios;

    private final List<Rol> roles;
    
    private List<Usuario> usuarios;

    private Usuario usuario;

    /**
     * Creates new form NuevoUsuario
     */
    public MantenimientoUsuarios(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        
            initComponents();
            sisUsuarios = SistemaUsuarios.getInstance();
           
            roles = sisUsuarios.devolverRoles();
            for (Rol r : roles) {
                jComboBoxRoles.addItem(r);
            }
            jComboBoxUsuarios.addItem("");
            usuarios = sisUsuarios.devolverUsuarios();
            for(Usuario u : usuarios){
                jComboBoxUsuarios.addItem(u);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabelTitulo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldNombre = new javax.swing.JTextField();
        jPasswordField = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxRoles = new javax.swing.JComboBox();
        jButtonGuardar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxUsuarios = new javax.swing.JComboBox();
        jButtonModificar = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nuevo Usuario");

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Usuarios");

        jLabel1.setText("Nombre:");

        jLabel2.setText("Contraseña:");

        jTextFieldNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNombreKeyPressed(evt);
            }
        });

        jPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordFieldKeyPressed(evt);
            }
        });

        jLabel3.setText("Rol:");

        jComboBoxRoles.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxRolesKeyPressed(evt);
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
        jButtonSalir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonSalirKeyPressed(evt);
            }
        });

        jLabel4.setText("Usuarios:");

        jComboBoxUsuarios.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxUsuariosKeyPressed(evt);
            }
        });

        jButtonModificar.setText("Modificar");
        jButtonModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarActionPerformed(evt);
            }
        });

        jButtonEliminar.setText("Eliminar");
        jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldNombre)
                            .addComponent(jPasswordField)
                            .addComponent(jComboBoxRoles, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(jLabelTitulo))
                            .addComponent(jComboBoxUsuarios, 0, 200, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonModificar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonEliminar)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButtonGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabelTitulo)
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBoxUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonModificar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBoxRoles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButtonGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSalir)
                .addGap(38, 38, 38))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNombreKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jPasswordField.requestFocus();
            jPasswordField.selectAll();
        }
    }//GEN-LAST:event_jTextFieldNombreKeyPressed

    private void jPasswordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordFieldKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jComboBoxRoles.requestFocus();
        }
    }//GEN-LAST:event_jPasswordFieldKeyPressed

    private void jComboBoxRolesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxRolesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonGuardar.requestFocus();
        }
    }//GEN-LAST:event_jComboBoxRolesKeyPressed

    private void jButtonGuardarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonGuardarKeyPressed
        // TODO add your handling code here
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonGuardar.doClick();
        }
    }//GEN-LAST:event_jButtonGuardarKeyPressed

    private void jButtonSalirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonSalirKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonSalir.doClick();
        }
    }//GEN-LAST:event_jButtonSalirKeyPressed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        // TODO add your handling code here:
        if (usuario == null) {
            //Es un usuario nuevo
            try {
                if (sisUsuarios.nombreDeUsuarioValido(jTextFieldNombre.getText().trim())) {
                    if (sisUsuarios.agregarUsuario(jTextFieldNombre.getText().trim(), jPasswordField.getText(), (Rol) jComboBoxRoles.getSelectedItem())) {
                        JOptionPane.showMessageDialog(this, "Usuario creado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Ya existe un usuario con ese nombre.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    jTextFieldNombre.requestFocus();
                    jTextFieldNombre.selectAll();
                }
            } catch (Exception e) {
                String stakTrace = util.Util.obtenerStackTraceEnString(e);
                SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                
                JOptionPane.showMessageDialog(this, "Error al guardar el usuario.", "Información", JOptionPane.INFORMATION_MESSAGE);

            }
        } else {
            //Es una modificacion.
            if(jTextFieldNombre.getText().trim().equals(usuario.getNombreDeUsuario())){
                try {
                    //No Modifica el nombre
                    usuario.setContrasena(jPasswordField.getText());
                    usuario.setRol((Rol)jComboBoxRoles.getSelectedItem());
                    if(sisUsuarios.actualizarUsuario(usuario)){
                        JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar el usuario.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        
                    }
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(MantenimientoUsuarios.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                //Modifica el nombre
                try {
                    if (sisUsuarios.nombreDeUsuarioValido(jTextFieldNombre.getText().trim())) {
                        usuario.setNombreDeUsuario(jTextFieldNombre.getText().trim());
                        usuario.setContrasena(jPasswordField.getText());
                        usuario.setRol((Rol) jComboBoxRoles.getSelectedItem());
                        if (sisUsuarios.actualizarUsuario(usuario)) {
                            JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
                            this.dispose();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Ya existe un usuario con ese nombre.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        jTextFieldNombre.requestFocus();
                        jTextFieldNombre.selectAll();
                    }
                } catch (Exception e) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(e);
                    SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                    JOptionPane.showMessageDialog(this, "Error al guardar el usuario.", "Información", JOptionPane.INFORMATION_MESSAGE);

                }
            }
        }
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void jButtonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarActionPerformed
        // TODO add your handling code here:
        int resp = JOptionPane.showConfirmDialog(this, "Atención!!! Al modificar un usuario debe ingresar de nuevo la contraseña. Desea continuar ?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (resp == JOptionPane.YES_OPTION) {
            if (jComboBoxUsuarios.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un usuarios para modificar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                usuario = (Usuario) jComboBoxUsuarios.getSelectedItem();
                cargarUsuario();
            }
        } else {
            usuario = null;
        }
        
    }//GEN-LAST:event_jButtonModificarActionPerformed

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
        // TODO add your handling code here:
        if (jComboBoxUsuarios.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuarios para eliminar.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                usuario = (Usuario) jComboBoxUsuarios.getSelectedItem();
                if (sisUsuarios.eliminarUsuario(usuario)) {
                    JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    reiniciarVentana();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el usuario.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    reiniciarVentana();
                }
            } catch (Exception exp) {
                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                JOptionPane.showMessageDialog(MantenimientoUsuarios.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonEliminarActionPerformed

    private void jComboBoxUsuariosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxUsuariosKeyPressed
        // TODO add your handling code here:
        if(jComboBoxUsuarios.getSelectedIndex() != 0) {
            jButtonModificar.requestFocus();
        } else {
            jTextFieldNombre.requestFocus();
            jTextFieldNombre.selectAll();
        }
    }//GEN-LAST:event_jComboBoxUsuariosKeyPressed

    private void cargarUsuario() {
        jTextFieldNombre.setText(usuario.getNombreDeUsuario());
        jPasswordField.setText("");
        int indiceRol = indiceRol(usuario.getRol());
        jComboBoxRoles.setSelectedIndex(indiceRol);
        jTextFieldNombre.requestFocus();
        jTextFieldNombre.selectAll();
    }

    private int indiceRol(Rol rol) {
        int retorno = 0;
        for (int i = 0; i < roles.size(); i++) {
            Rol r = roles.get(i);
            if (r.equals(rol)) {
                retorno = i;
            }
        }
        return retorno;
    }

    private void reiniciarVentana() {
        try {
            jComboBoxUsuarios.setSelectedIndex(0);
            jComboBoxUsuarios.removeAllItems();
            jComboBoxUsuarios.addItem("");
            usuarios = sisUsuarios.devolverUsuarios();
            for (Usuario u : usuarios) {
                jComboBoxUsuarios.addItem(u);
            }
            jTextFieldNombre.setText("");
            jPasswordField.setText("");
            jComboBoxRoles.setSelectedIndex(0);
            jComboBoxUsuarios.requestFocus();
            usuario = null;
        } catch (Exception exp) {
            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
            JOptionPane.showMessageDialog(MantenimientoUsuarios.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
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
            java.util.logging.Logger.getLogger(MantenimientoUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MantenimientoUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MantenimientoUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MantenimientoUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MantenimientoUsuarios dialog = new MantenimientoUsuarios(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonModificar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JComboBox jComboBoxRoles;
    private javax.swing.JComboBox jComboBoxUsuarios;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JTextField jTextFieldNombre;
    // End of variables declaration//GEN-END:variables
}