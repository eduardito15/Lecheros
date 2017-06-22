/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.config;

import dominio.ConfiguracionLiquidacion;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.clientes.MantenimientoClientes;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class VentanaConfiguracionLiquidacion extends javax.swing.JDialog {

    private SistemaMantenimiento sis;
    private final ConfiguracionLiquidacion configuracionLiquidacion;
    /**
     * Creates new form ConfiguracionLiquidacion
     */
    public VentanaConfiguracionLiquidacion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sis = SistemaMantenimiento.getInstance();
        configuracionLiquidacion = sis.devolverConfiguracionLiquidacion();
        jRadioButtonModoAcumulativo.setSelected(configuracionLiquidacion.isModoAcumulativo());
        jRadioButtonModoNoAcumulativo.setSelected(!configuracionLiquidacion.isModoAcumulativo());
        jRadioButtonCamion.setSelected(configuracionLiquidacion.isMostrarCamion());
        jRadioButtonCorrecionDeDiferencia.setSelected(configuracionLiquidacion.isMostrarCorrecionDeDiferencia());
        jRadioButtonEntregaCheques.setSelected(configuracionLiquidacion.isMostrarEntregaCheques());
        jRadioButtonFiadoChofer.setSelected(configuracionLiquidacion.isMostrarFiadoClientesCobraChofer());
        jRadioButtonFiadoEmpresa.setSelected(configuracionLiquidacion.isMostrarFiadoClientesCobraEmpresa());
        jRadioButtonInau.setSelected(configuracionLiquidacion.isMostrarInau());
        jRadioButtonAnep.setSelected(configuracionLiquidacion.isMostrarAnep());
        jRadioButtonRetenciones.setSelected(configuracionLiquidacion.isRetenciones());
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
        jLabel1 = new javax.swing.JLabel();
        jRadioButtonModoAcumulativo = new javax.swing.JRadioButton();
        jRadioButtonModoNoAcumulativo = new javax.swing.JRadioButton();
        jRadioButtonCorrecionDeDiferencia = new javax.swing.JRadioButton();
        jRadioButtonEntregaCheques = new javax.swing.JRadioButton();
        jRadioButtonFiadoChofer = new javax.swing.JRadioButton();
        jRadioButtonFiadoEmpresa = new javax.swing.JRadioButton();
        jRadioButtonInau = new javax.swing.JRadioButton();
        jRadioButtonRetenciones = new javax.swing.JRadioButton();
        jButtonGuardar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jRadioButtonCamion = new javax.swing.JRadioButton();
        jRadioButtonAnep = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setText("Configuración de la Liquidación");

        buttonGroup1.add(jRadioButtonModoAcumulativo);
        jRadioButtonModoAcumulativo.setText("Modo Acumulativo");

        buttonGroup1.add(jRadioButtonModoNoAcumulativo);
        jRadioButtonModoNoAcumulativo.setText("Modo No Acumulativo");

        jRadioButtonCorrecionDeDiferencia.setText("Mostrar Corrección de Diferencia");

        jRadioButtonEntregaCheques.setText("Mostrar Entrega Cheques");

        jRadioButtonFiadoChofer.setText("Mostar Fiado de los Clientes que Cobra el Chofer");

        jRadioButtonFiadoEmpresa.setText("Mostrar Fiado de los Clientes que Cobra la Empresa");

        jRadioButtonInau.setText("Mostar Inau");

        jRadioButtonRetenciones.setText("Mostrar Retenciones de Sueldo");

        jButtonGuardar.setText("Guardar");
        jButtonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarActionPerformed(evt);
            }
        });

        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });

        jRadioButtonCamion.setText("Mostrar Camion");

        jRadioButtonAnep.setText("Mostrar Anep");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jRadioButtonModoAcumulativo)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButtonModoNoAcumulativo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButtonEntregaCheques)
                            .addComponent(jRadioButtonCorrecionDeDiferencia)
                            .addComponent(jRadioButtonFiadoChofer)
                            .addComponent(jRadioButtonFiadoEmpresa)
                            .addComponent(jRadioButtonInau)
                            .addComponent(jRadioButtonRetenciones)
                            .addComponent(jRadioButtonCamion)
                            .addComponent(jRadioButtonAnep)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonModoAcumulativo)
                    .addComponent(jRadioButtonModoNoAcumulativo))
                .addGap(32, 32, 32)
                .addComponent(jRadioButtonCamion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonCorrecionDeDiferencia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonEntregaCheques)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonFiadoChofer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonFiadoEmpresa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonInau)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonAnep)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonRetenciones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jButtonGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSalir)
                .addGap(51, 51, 51))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        try {
            // TODO add your handling code here:
            configuracionLiquidacion.setModoAcumulativo(jRadioButtonModoAcumulativo.isSelected());
            configuracionLiquidacion.setMostrarCamion(jRadioButtonCamion.isSelected());
            configuracionLiquidacion.setMostrarCorrecionDeDiferencia(jRadioButtonCorrecionDeDiferencia.isSelected());
            configuracionLiquidacion.setMostrarEntregaCheques(jRadioButtonEntregaCheques.isSelected());
            configuracionLiquidacion.setMostrarFiadoClientesCobraChofer(jRadioButtonFiadoChofer.isSelected());
            configuracionLiquidacion.setMostrarFiadoClientesCobraEmpresa(jRadioButtonFiadoEmpresa.isSelected());
            configuracionLiquidacion.setMostrarInau(jRadioButtonInau.isSelected());
            configuracionLiquidacion.setMostrarAnep(jRadioButtonAnep.isSelected());
            configuracionLiquidacion.setRetenciones(jRadioButtonRetenciones.isSelected());
            sis.guardarConfiguracionDeLiquidacion(configuracionLiquidacion);
            this.dispose();
        } catch (Exception exp) {
            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
            JOptionPane.showMessageDialog(VentanaConfiguracionLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonGuardarActionPerformed

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
            java.util.logging.Logger.getLogger(VentanaConfiguracionLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaConfiguracionLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaConfiguracionLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaConfiguracionLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaConfiguracionLiquidacion dialog = new VentanaConfiguracionLiquidacion(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton jRadioButtonAnep;
    private javax.swing.JRadioButton jRadioButtonCamion;
    private javax.swing.JRadioButton jRadioButtonCorrecionDeDiferencia;
    private javax.swing.JRadioButton jRadioButtonEntregaCheques;
    private javax.swing.JRadioButton jRadioButtonFiadoChofer;
    private javax.swing.JRadioButton jRadioButtonFiadoEmpresa;
    private javax.swing.JRadioButton jRadioButtonInau;
    private javax.swing.JRadioButton jRadioButtonModoAcumulativo;
    private javax.swing.JRadioButton jRadioButtonModoNoAcumulativo;
    private javax.swing.JRadioButton jRadioButtonRetenciones;
    // End of variables declaration//GEN-END:variables
}
