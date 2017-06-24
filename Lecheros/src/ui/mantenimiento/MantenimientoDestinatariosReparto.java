/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.mantenimiento;

import dominio.Reparto;
import dominio.DestinatarioConaproleReparto;
import dominio.DocumentoDeCompra;
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
public class MantenimientoDestinatariosReparto extends javax.swing.JDialog {

    private SistemaMantenimiento sis;
    private DestinatarioConaproleReparto destConRep;
    private List<DestinatarioConaproleReparto> destsConReps;
    /**
     * Creates new form MantenimientoDestinatariosReparto
     */
    public MantenimientoDestinatariosReparto(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sis = SistemaMantenimiento.getInstance();
        jComboBoxReparto.addItem("");
        List<Reparto> repartos = sis.devolverRepartos();
        for (Reparto c : repartos) {
            jComboBoxReparto.addItem(c);
        }
        actualizarListaDests();
        jComboBoxTipoDocumento.addItem("");
        List<DocumentoDeCompra> tiposDocumentos = sis.devolverDocumentosDeCompra();
        for(DocumentoDeCompra dc: tiposDocumentos){
            jComboBoxTipoDocumento.addItem(dc);
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListDestConRep = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButtonGuardar = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jTextFieldDestinatario = new javax.swing.JTextField();
        jComboBoxReparto = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxTipoDocumento = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mantenimiento Destinatarios Conaprole - Reparto");

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setText("Destinatario Conaprole - Reparto");

        jListDestConRep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListDestConRepMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jListDestConRep);

        jLabel2.setText("Numero Destinatario:");

        jLabel3.setText("Reparto:");

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

        jButtonEliminar.setText("Eliminar");
        jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarActionPerformed(evt);
            }
        });

        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });

        jTextFieldDestinatario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldDestinatarioKeyPressed(evt);
            }
        });

        jComboBoxReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxRepartoKeyPressed(evt);
            }
        });

        jLabel4.setText("Tipo de Documento:");

        jComboBoxTipoDocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxTipoDocumentoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButtonEliminar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jButtonGuardar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldDestinatario)
                            .addComponent(jComboBoxTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(50, 50, 50))
            .addGroup(layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextFieldDestinatario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jComboBoxTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addComponent(jButtonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSalir))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        // TODO add your handling code here:
        if("".equals(jTextFieldDestinatario.getText().trim()) || jComboBoxReparto.getSelectedIndex() == 0|| jComboBoxTipoDocumento.getSelectedIndex() == 0){
            //Faltan ingresar datos
            JOptionPane.showMessageDialog(this, "Para ingresar esta relación debes ingresar el numero de destinatario de conaprole, seleccionar el tipo de documento que genera y seleccionar el reparto al cual pertenece.", "Información", JOptionPane.INFORMATION_MESSAGE);
            jTextFieldDestinatario.requestFocus();
            jTextFieldDestinatario.selectAll();
        } else {
            //Si todos los datos estan ingresados, me fijo si es una modificacion o uno nuevo
            try {
                if (destConRep == null) {
                    //Es uno nuevo. Verifico que el numero de destinatario ingresado no tenga una relacion
                    if (sis.numeroDeDestinatarioValido(Long.parseLong(jTextFieldDestinatario.getText().trim()),(Reparto)jComboBoxReparto.getSelectedItem(),(DocumentoDeCompra)jComboBoxTipoDocumento.getSelectedItem())) {
                        //Numero de destinatario valido
                        sis.agregarDestConRep(Long.parseLong(jTextFieldDestinatario.getText().trim()),(DocumentoDeCompra)jComboBoxTipoDocumento.getSelectedItem() ,(Reparto)jComboBoxReparto.getSelectedItem());
                        jTextFieldDestinatario.setText("");
                        jComboBoxReparto.setSelectedIndex(0);
                        jComboBoxTipoDocumento.setSelectedIndex(0);
                        actualizarListaDests();
                    } else {
                        //Nro de destinatario invalido
                        JOptionPane.showMessageDialog(this, "Ya existe una relación para ese numero de destinatario.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        jTextFieldDestinatario.requestFocus();
                        jTextFieldDestinatario.selectAll();
                    }
                } else {
                    //Es una modificacion. Veo si quiere modificar el numero
                    if(destConRep.getNumeroDestinatario()!=Long.parseLong(jTextFieldDestinatario.getText().trim()) || destConRep.getReparto() != (Reparto)jComboBoxReparto.getSelectedItem() || destConRep.getTipoDocumento() != (DocumentoDeCompra)jComboBoxTipoDocumento.getSelectedItem()){
                        //Quiere modificar el numero. Verifico si es un numero valido
                        if (sis.numeroDeDestinatarioValido(Long.parseLong(jTextFieldDestinatario.getText().trim()),(Reparto)jComboBoxReparto.getSelectedItem(),(DocumentoDeCompra)jComboBoxTipoDocumento.getSelectedItem())) {
                            //Numero de destinatario valido
                            destConRep.setNumeroDestinatario(Long.parseLong(jTextFieldDestinatario.getText().trim()));
                            destConRep.setReparto((Reparto)jComboBoxReparto.getSelectedItem());
                            destConRep.setTipoDocumento((DocumentoDeCompra)jComboBoxTipoDocumento.getSelectedItem());
                            sis.actualizarDestConRep(destConRep);
                            jTextFieldDestinatario.setText("");
                            jComboBoxReparto.setSelectedIndex(0);
                            jComboBoxTipoDocumento.setSelectedIndex(0);
                            actualizarListaDests();
                            destConRep = null;
                        } else {
                            //Nro de destinatario invalido
                            JOptionPane.showMessageDialog(this, "Ya existe una relación para ese numero de destinatario.", "Información", JOptionPane.INFORMATION_MESSAGE);
                            jTextFieldDestinatario.requestFocus();
                            jTextFieldDestinatario.selectAll();
                        }
                    } else {
                        //No quiere modificar el numero. Asi que todo bien, lo modifico.
                        destConRep.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                        destConRep.setTipoDocumento((DocumentoDeCompra)jComboBoxTipoDocumento.getSelectedItem());
                        sis.actualizarDestConRep(destConRep);
                        jTextFieldDestinatario.setText("");
                        jComboBoxReparto.setSelectedIndex(0);
                        jComboBoxTipoDocumento.setSelectedIndex(0);
                        actualizarListaDests();
                        destConRep = null;
                    }
                }
            }
            catch (NumberFormatException nfe){
                JOptionPane.showMessageDialog(this, "El numero de destinatario debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldDestinatario.requestFocus();
                jTextFieldDestinatario.selectAll();
            } catch (Exception e) {
                String stakTrace = util.Util.obtenerStackTraceEnString(e);
                SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            } 
            
        }
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void jListDestConRepMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListDestConRepMouseClicked
        // TODO add your handling code here:
        int seleccionado = jListDestConRep.getSelectedIndex();
        if(seleccionado != -1){
            destConRep = destsConReps.get(seleccionado);
            jTextFieldDestinatario.setText(Long.toString(destConRep.getNumeroDestinatario()));
            jComboBoxReparto.setSelectedItem(destConRep.getReparto());
        }
    }//GEN-LAST:event_jListDestConRepMouseClicked

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
        // TODO add your handling code here:
        if (destConRep == null) {
            JOptionPane.showMessageDialog(this, "Para eliminar un relación, primero debes seleccionarla de la lista.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                sis.eliminarDestConRep(destConRep);
                jTextFieldDestinatario.setText("");
                jComboBoxReparto.setSelectedIndex(0);
                destConRep = null;
                actualizarListaDests();
            } catch (HibernateException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el camión." + "\n\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, "Error al eliminar el camión." + "\n\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonEliminarActionPerformed

    private void jTextFieldDestinatarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDestinatarioKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jComboBoxTipoDocumento.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldDestinatarioKeyPressed

    private void jComboBoxRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxRepartoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonGuardar.requestFocus();
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

    private void jButtonGuardarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonGuardarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonGuardar.doClick();
        }
    }//GEN-LAST:event_jButtonGuardarKeyPressed

    private void jComboBoxTipoDocumentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxTipoDocumentoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jComboBoxReparto.requestFocus();
        }
    }//GEN-LAST:event_jComboBoxTipoDocumentoKeyPressed

    private void actualizarListaDests() {
        destsConReps = sis.devolverDestConRep();

        DefaultListModel modelo = new DefaultListModel();
        for (DestinatarioConaproleReparto dcr : destsConReps) {
            modelo.addElement(dcr);
        }

        jListDestConRep.setModel(modelo);
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
            java.util.logging.Logger.getLogger(MantenimientoDestinatariosReparto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MantenimientoDestinatariosReparto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MantenimientoDestinatariosReparto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MantenimientoDestinatariosReparto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MantenimientoDestinatariosReparto dialog = new MantenimientoDestinatariosReparto(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox jComboBoxReparto;
    private javax.swing.JComboBox jComboBoxTipoDocumento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jListDestConRep;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldDestinatario;
    // End of variables declaration//GEN-END:variables
}