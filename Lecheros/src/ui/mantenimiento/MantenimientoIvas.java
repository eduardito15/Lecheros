/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.mantenimiento;

import dominio.Iva;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.informes.VentanaInformeCheques;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class MantenimientoIvas extends javax.swing.JDialog {

    private final SistemaMantenimiento sis;
    private List<Iva> ivas;
    private Iva iva;
    
    /**
     * Creates new form MantenimientoIvas
     */
    public MantenimientoIvas(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sis = SistemaMantenimiento.getInstance();
        iva = new Iva();
        actualizarListaIvas();
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
        jTextFieldNombreIva = new javax.swing.JTextField();
        jButtonGuardar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListIvas = new javax.swing.JList();
        jButtonEliminar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldPorcentajeIva = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mantenimiento Ivas");

        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });

        jLabel2.setText("Nombre: ");

        jButtonGuardar.setText("Guardar");
        jButtonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setText("Ivas");

        jListIvas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListIvasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jListIvas);

        jButtonEliminar.setText("Eliminar");
        jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarActionPerformed(evt);
            }
        });

        jLabel3.setText("Porcentaje:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButtonGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(64, 64, 64))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldNombreIva, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldPorcentajeIva, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(248, 248, 248))
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
                            .addComponent(jLabel2)
                            .addComponent(jTextFieldNombreIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextFieldPorcentajeIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jButtonGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSalir))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(60, Short.MAX_VALUE))
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
        if("".equals(jTextFieldNombreIva.getText().trim())){
            JOptionPane.showMessageDialog(this, "El iva debe tener un nombre.","Información",JOptionPane.INFORMATION_MESSAGE);
            jTextFieldNombreIva.requestFocus();
            jTextFieldNombreIva.selectAll();
        } else {
            if("".equals(jTextFieldPorcentajeIva.getText().trim())){
                JOptionPane.showMessageDialog(this, "El iva debe tener un porcentaje. Que debe ser un numero entero.","Información",JOptionPane.INFORMATION_MESSAGE);
                jTextFieldPorcentajeIva.requestFocus();
                jTextFieldPorcentajeIva.selectAll();
            } else {
                try {
                    SistemaMantenimiento sm = SistemaMantenimiento.getInstance();
                    if (iva.getNombre() == null) {
                        int porcentaje = Integer.parseInt(jTextFieldPorcentajeIva.getText());
                        if (sm.agregarIva(jTextFieldNombreIva.getText(), porcentaje)) {
                            jTextFieldNombreIva.setText("");
                            jTextFieldPorcentajeIva.setText("");
                            iva = new Iva();
                            actualizarListaIvas();
                        } else {

                        }
                    } else {
                        iva.setNombre(jTextFieldNombreIva.getText());
                        iva.setPorcentaje(Integer.parseInt(jTextFieldPorcentajeIva.getText()));
                        sis.actualizarIva(iva);
                        jTextFieldNombreIva.setText("");
                        jTextFieldPorcentajeIva.setText("");
                        iva = new Iva();
                        actualizarListaIvas();
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this, "El porcentaje de iva debe ser un numero entero", "Información", JOptionPane.INFORMATION_MESSAGE);
                    jTextFieldPorcentajeIva.requestFocus();
                    jTextFieldPorcentajeIva.selectAll();
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(MantenimientoIvas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void jListIvasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListIvasMouseClicked
        // TODO add your handling code here:
        int seleccionado = jListIvas.getSelectedIndex();
        if(seleccionado != -1){
            iva = ivas.get(seleccionado);
            jTextFieldNombreIva.setText(iva.getNombre());
            jTextFieldPorcentajeIva.setText(Integer.toString(iva.getPorcentaje()));
        }
    }//GEN-LAST:event_jListIvasMouseClicked

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
        // TODO add your handling code here:
        try{
            if (iva.getNombre() == null) {
                JOptionPane.showMessageDialog(this, "Para eliminar un iva, primero debes seleccionarlo de la lista.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                sis.eliminarIva(iva);
                jTextFieldNombreIva.setText("");
                jTextFieldPorcentajeIva.setText("");
                iva = new Iva();
                actualizarListaIvas();
            }
        }
        catch (HibernateException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar el iva." + "\n\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar el iva." + "\n\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonEliminarActionPerformed
    
    private void actualizarListaIvas(){
        ivas = sis.devolverIvas();
        
        DefaultListModel modelo = new DefaultListModel();
        for (Iva iv : ivas) {
            modelo.addElement(iv);
        }
        
        jListIvas.setModel(modelo);
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
            java.util.logging.Logger.getLogger(MantenimientoIvas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MantenimientoIvas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MantenimientoIvas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MantenimientoIvas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MantenimientoIvas dialog = new MantenimientoIvas(new javax.swing.JFrame(), true);
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
    private javax.swing.JList jListIvas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldNombreIva;
    private javax.swing.JTextField jTextFieldPorcentajeIva;
    // End of variables declaration//GEN-END:variables
}
