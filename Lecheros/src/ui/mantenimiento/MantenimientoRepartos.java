/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.mantenimiento;

import dominio.Reparto;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class MantenimientoRepartos extends javax.swing.JDialog {

    private final SistemaMantenimiento sis;
    private Reparto reparto;
    private List<Reparto> repartos;
    /**
     * Creates new form MantenimientoCamiones
     */
    public MantenimientoRepartos(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sis = SistemaMantenimiento.getInstance();
        reparto = new Reparto();
        actualizarListaCamiones();
        jTextFieldCodigoReparto.requestFocus();
        jTextFieldCodigoReparto.selectAll();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonSalir = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldNombreReparto = new javax.swing.JTextField();
        jButtonGuardar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListCamiones = new javax.swing.JList();
        jButtonEliminar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldCodigoReparto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldCodigoPS = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mantenimiento Repartos");

        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });

        jLabel2.setText("Nombre:");

        jTextFieldNombreReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNombreRepartoKeyPressed(evt);
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

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setText("Repartos");

        jListCamiones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListCamionesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jListCamiones);

        jButtonEliminar.setText("Eliminar");
        jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarActionPerformed(evt);
            }
        });

        jLabel4.setText("Codigo:");

        jTextFieldCodigoReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCodigoRepartoKeyPressed(evt);
            }
        });

        jLabel3.setText("Codigo PS:");

        jTextFieldCodigoPS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCodigoPSKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButtonGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(79, 79, 79))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldNombreReparto, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                            .addComponent(jTextFieldCodigoReparto)
                            .addComponent(jTextFieldCodigoPS))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextFieldCodigoReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextFieldNombreReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldCodigoPS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(35, 35, 35)
                        .addComponent(jButtonGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSalir))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        // TODO add your handling code here:
        //Chequeo que los campos obligatorios no esten vacios
        if("".equals(jTextFieldCodigoReparto.getText().trim())){
            JOptionPane.showMessageDialog(this, "El reparto debe tener un codigo","Información",JOptionPane.INFORMATION_MESSAGE);
            jTextFieldCodigoReparto.requestFocus();
            jTextFieldCodigoReparto.selectAll();
        } else {
            if("".equals(jTextFieldNombreReparto.getText().trim())){
                JOptionPane.showMessageDialog(this, "El reparto debe tener un nombre","Información",JOptionPane.INFORMATION_MESSAGE);
                jTextFieldNombreReparto.requestFocus();
                jTextFieldNombreReparto.selectAll();
            } else {
                try {
                    SistemaMantenimiento sm = SistemaMantenimiento.getInstance();
                    //Agrego un camion nuevo
                    if (reparto.getNombre() == null) {
                        //Chequeo que no exista un camion con ese codigo
                        if(!codigoValido(jTextFieldCodigoReparto.getText())){
                            JOptionPane.showMessageDialog(this, "Ya existe un reparto con ese codigo", "Información", JOptionPane.INFORMATION_MESSAGE);
                            jTextFieldCodigoReparto.requestFocus();
                            jTextFieldCodigoReparto.selectAll();
                        } else {
                            //Si no existe con ese codigo, chequeo que no exista un camion con ese nombre
                            int codigo = Integer.parseInt(jTextFieldCodigoReparto.getText());
                            if(!nombreValido(jTextFieldNombreReparto.getText())){
                                JOptionPane.showMessageDialog(this, "Ya existe un reparto con ese nombre", "Información", JOptionPane.INFORMATION_MESSAGE);
                                jTextFieldNombreReparto.requestFocus();
                                jTextFieldNombreReparto.selectAll();
                            } else {
                                //Si no existe ningun camion con ese codigo ni con ese nombre, lo agrego. 
                                if (sm.agregarReparto(codigo, jTextFieldNombreReparto.getText(), Integer.parseInt(jTextFieldCodigoPS.getText()))) {
                                    vaciarCampos();
                                    reparto = new Reparto();
                                    actualizarListaCamiones();
                                } else {
                                    //Aca llega si no puede agregar el camion, por algun error en la base de datos.
                                    JOptionPane.showMessageDialog(this, "Error al guardar el reparto.", "Información", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                             
                        }
                        
                    } else {
                        //Actualizo un camion ya existente
                        //Veo si intentan actualizar el codigo
                        if(! (reparto.getCodigo() == Integer.parseInt(jTextFieldCodigoReparto.getText()))){
                            //Si intentan actualizar el codigo chequeo que el nuevo codigo sea valido
                            if (!codigoValido(jTextFieldCodigoReparto.getText())) {
                                JOptionPane.showMessageDialog(this, "Ya existe un reparto con ese codigo", "Información", JOptionPane.INFORMATION_MESSAGE);
                                jTextFieldCodigoReparto.requestFocus();
                                jTextFieldCodigoReparto.selectAll();
                            } else {
                                //Si es valido el nuevo codigo, reviso el nombre si ha cambiado
                                if(!(reparto.getNombre().equals(jTextFieldNombreReparto.getText()))){
                                    //Si intentan actualizar el nombre, chequeo que sea un nombre valido el nuevo
                                    if(!nombreValido(jTextFieldNombreReparto.getText())){
                                        JOptionPane.showMessageDialog(this, "Ya existe un reparto con ese nombre", "Información", JOptionPane.INFORMATION_MESSAGE);
                                        jTextFieldNombreReparto.requestFocus();
                                        jTextFieldNombreReparto.selectAll();
                                    } else {
                                        //Actualizo los datos del camion
                                        reparto.setCodigo(Integer.parseInt(jTextFieldCodigoReparto.getText()));
                                        reparto.setNombre(jTextFieldNombreReparto.getText());
                                        reparto.setNumeroVendedorPS(Integer.parseInt(jTextFieldCodigoPS.getText()));
                                        sis.actualizarReparto(reparto);
                                        vaciarCampos();
                                        reparto = new Reparto();
                                        actualizarListaCamiones();
                                    }
                                } else {
                                    reparto.setCodigo(Integer.parseInt(jTextFieldCodigoReparto.getText()));
                                    reparto.setNombre(jTextFieldNombreReparto.getText());
                                    reparto.setNumeroVendedorPS(Integer.parseInt(jTextFieldCodigoPS.getText()));
                                    sis.actualizarReparto(reparto);
                                    vaciarCampos();
                                    reparto = new Reparto();
                                    actualizarListaCamiones();
                                }
                            }
                        } else {
                            //Si no quiere actualizar el codigo de camion, chequeo el cambio de nombre
                            if(!reparto.getNombre().equals(jTextFieldNombreReparto.getText())){
                                //Si intentan actualizar el nombre, chequeo que sea un nombre valido el nuevo
                                if(!nombreValido(jTextFieldNombreReparto.getText())){
                                    JOptionPane.showMessageDialog(this, "Ya existe un reparto con ese nombre", "Información", JOptionPane.INFORMATION_MESSAGE);
                                    jTextFieldNombreReparto.requestFocus();
                                    jTextFieldNombreReparto.selectAll();
                                } else {
                                    //Actualizo los datos del camion
                                    reparto.setCodigo(Integer.parseInt(jTextFieldCodigoReparto.getText()));
                                    reparto.setNombre(jTextFieldNombreReparto.getText());
                                    reparto.setNumeroVendedorPS(Integer.parseInt(jTextFieldCodigoPS.getText()));
                                    sis.actualizarReparto(reparto);
                                    vaciarCampos();
                                    reparto = new Reparto();
                                    actualizarListaCamiones();
                                }
                            } else {
                                //Actualizo los datos del camion
                                reparto.setCodigo(Integer.parseInt(jTextFieldCodigoReparto.getText()));
                                reparto.setNombre(jTextFieldNombreReparto.getText());
                                reparto.setNumeroVendedorPS(Integer.parseInt(jTextFieldCodigoPS.getText()));
                                sis.actualizarReparto(reparto);
                                vaciarCampos();
                                reparto = new Reparto();
                                actualizarListaCamiones();
                            }
                        }  
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this, "El codigo de reparto y codigo PS debe ser un numero entero.","Información",JOptionPane.INFORMATION_MESSAGE);
                    jTextFieldCodigoReparto.requestFocus();
                    jTextFieldCodigoReparto.selectAll();
                } catch (Exception e) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(e);
                    SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                    JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }   
            }
        }  
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void jListCamionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListCamionesMouseClicked
        // TODO add your handling code here:
        int seleccionado = jListCamiones.getSelectedIndex();
        if(seleccionado != -1){
            reparto = repartos.get(seleccionado);
            jTextFieldCodigoReparto.setText(Integer.toString(reparto.getCodigo()));
            jTextFieldNombreReparto.setText(reparto.getNombre());
            jTextFieldCodigoPS.setText(Integer.toString(reparto.getNumeroVendedorPS()));
        }
    }//GEN-LAST:event_jListCamionesMouseClicked

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
        // TODO add your handling code here:
        if(reparto.getNombre() == null){
            JOptionPane.showMessageDialog(this, "Para eliminar un reparto, primero debes seleccionarlo de la lista.","Información",JOptionPane.INFORMATION_MESSAGE);
        } else {
            try{
                sis.eliminarReparto(reparto);
                vaciarCampos();
                reparto = new Reparto();
                actualizarListaCamiones();
            }
            catch (HibernateException e){
                JOptionPane.showMessageDialog(this, "Error al eliminar el reparto." + "\n\n" + e.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace); 
                JOptionPane.showMessageDialog(this, "Error al eliminar el reparto." + "\n\n" + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonEliminarActionPerformed

    private void jTextFieldCodigoRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodigoRepartoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            jTextFieldNombreReparto.requestFocus();
            jTextFieldNombreReparto.selectAll();
        }
    }//GEN-LAST:event_jTextFieldCodigoRepartoKeyPressed

    private void jTextFieldNombreRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNombreRepartoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            //jButtonGuardar.requestFocus();
            jTextFieldCodigoPS.requestFocus();
            jTextFieldCodigoPS.selectAll();
        }
    }//GEN-LAST:event_jTextFieldNombreRepartoKeyPressed

    private void jButtonGuardarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonGuardarKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            jButtonGuardar.doClick();
        }
    }//GEN-LAST:event_jButtonGuardarKeyPressed

    private void jTextFieldCodigoPSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodigoPSKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            jButtonGuardar.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldCodigoPSKeyPressed

    private void actualizarListaCamiones(){
        repartos = sis.devolverRepartos();
        
        DefaultListModel modelo = new DefaultListModel();
        for (Reparto c : repartos) {
            modelo.addElement(c);
        }
        
        jListCamiones.setModel(modelo);
    }
    
    private void vaciarCampos(){
        jTextFieldCodigoReparto.setText("");
        jTextFieldNombreReparto.setText("");
        jTextFieldCodigoPS.setText("");
    }
    
    private boolean codigoValido(String cod){
        boolean retorno = true;
        for(Reparto c:repartos){
            if(Integer.toString(c.getCodigo()).equals(cod)){
                retorno = false;
                break;
            }
        }
        return retorno;
    }
    
    private boolean nombreValido(String nom){
        boolean retorno = true;
        for(Reparto c:repartos){
            if(c.getNombre().equals(nom)){
                retorno = false;
                break;
            }
        }
        return retorno;
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
            java.util.logging.Logger.getLogger(MantenimientoRepartos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MantenimientoRepartos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MantenimientoRepartos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MantenimientoRepartos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MantenimientoRepartos dialog = new MantenimientoRepartos(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jListCamiones;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldCodigoPS;
    private javax.swing.JTextField jTextFieldCodigoReparto;
    private javax.swing.JTextField jTextFieldNombreReparto;
    // End of variables declaration//GEN-END:variables
}
