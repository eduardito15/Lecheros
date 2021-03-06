/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.config;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import dominio.ConfiguracionFacturacion;
import javax.swing.JOptionPane;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class VentanaConfiguracionFacturacion extends javax.swing.JDialog {
    
    private SistemaMantenimiento sis;
    private ConfiguracionFacturacion configuracionFacturacion;
    
    
    /**
     * Creates new form VentanaConfiguracionFacturacion
     */
    public VentanaConfiguracionFacturacion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sis = SistemaMantenimiento.getInstance();
        jRadioButtonSoloLeche.setVisible(false);
        configuracionFacturacion = sis.devolverConfiguracionFacturacion();
        jRadioButtonModoDetallado.setSelected(configuracionFacturacion.isDetalladaPorArticulo());
        jRadioButtonModoNoDetallado.setSelected(!configuracionFacturacion.isDetalladaPorArticulo());
        jRadioButtonSoloLeche.setSelected(configuracionFacturacion.isSoloLeche());
        jRadioButtonIngresarPreciosAlIngresarFacturas.setSelected(configuracionFacturacion.isIngresarPreciosDeFacturasAlIngesarLasFacturas());
        jRadioButtonDetalladoPorArt.setSelected(!configuracionFacturacion.isDetalladoPorGrupoDeArticulo());
        jRadioButtonDetalladoPorGrupo.setSelected(configuracionFacturacion.isDetalladoPorGrupoDeArticulo());
        jTextFieldProximoNumeroDeFactura.setText(""+configuracionFacturacion.getUltimoNumeroFactura());
        jTextFieldporcentajeFacturacion.setText("" + configuracionFacturacion.getPorcentajeFacturacion());
        jTextFieldMaximoBoletaConsumoFinal.setText("" + configuracionFacturacion.getMaximoBoletaContadoFinal());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    // Generated using JFormDesigner Evaluation license - Eduardo Vecino
    private void initComponents() {
        jLabel1 = new JLabel();
        jRadioButtonModoDetallado = new JRadioButton();
        jRadioButtonModoNoDetallado = new JRadioButton();
        jButtonGuardar = new JButton();
        jButtonSalir = new JButton();
        jRadioButtonSoloLeche = new JRadioButton();
        jLabel2 = new JLabel();
        jTextFieldProximoNumeroDeFactura = new JTextField();
        jLabel3 = new JLabel();
        jTextFieldporcentajeFacturacion = new JTextField();
        jLabel4 = new JLabel();
        jTextFieldMaximoBoletaConsumoFinal = new JTextField();
        jLabel5 = new JLabel();
        jRadioButtonDetalladoPorArt = new JRadioButton();
        jRadioButtonDetalladoPorGrupo = new JRadioButton();
        jRadioButtonIngresarPreciosAlIngresarFacturas = new JRadioButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configuraci\u00f3n de Facturaci\u00f3n");
        Container contentPane = getContentPane();

        //---- jLabel1 ----
        jLabel1.setFont(new Font("Lucida Grande", Font.BOLD, 14));
        jLabel1.setText("Configuraci\u00f3n de Facturaci\u00f3n");

        //---- jRadioButtonModoDetallado ----
        jRadioButtonModoDetallado.setSelected(true);
        jRadioButtonModoDetallado.setText("Detallado");

        //---- jRadioButtonModoNoDetallado ----
        jRadioButtonModoNoDetallado.setText("No Detallado ");

        //---- jButtonGuardar ----
        jButtonGuardar.setText("Guardar");
        jButtonGuardar.addActionListener(e -> jButtonGuardarActionPerformed(e));

        //---- jButtonSalir ----
        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(e -> jButtonSalirActionPerformed(e));

        //---- jRadioButtonSoloLeche ----
        jRadioButtonSoloLeche.setText("Solo Leche");

        //---- jLabel2 ----
        jLabel2.setText("Proximo Numero de Factura:");

        //---- jTextFieldProximoNumeroDeFactura ----
        jTextFieldProximoNumeroDeFactura.setText("1");

        //---- jLabel3 ----
        jLabel3.setText("Porcentaje de Facturaci\u00f3n:");

        //---- jTextFieldporcentajeFacturacion ----
        jTextFieldporcentajeFacturacion.setText("95");

        //---- jLabel4 ----
        jLabel4.setText("%");

        //---- jLabel5 ----
        jLabel5.setText("Maximo boleta CF:");

        //---- jRadioButtonDetalladoPorArt ----
        jRadioButtonDetalladoPorArt.setText("Por Art\u00edculo");

        //---- jRadioButtonDetalladoPorGrupo ----
        jRadioButtonDetalladoPorGrupo.setText("Por Grupo de Art\u00edculo");

        //---- jRadioButtonIngresarPreciosAlIngresarFacturas ----
        jRadioButtonIngresarPreciosAlIngresarFacturas.setText("Ingresar Precios de Venta al ingresar Facturas");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(39, 39, 39)
                            .addComponent(jLabel1))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(96, 96, 96)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButtonGuardar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonSalir, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(29, 29, 29)
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addComponent(jRadioButtonDetalladoPorArt)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jRadioButtonDetalladoPorGrupo))
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addComponent(jRadioButtonModoDetallado)
                                    .addGap(18, 18, 18)
                                    .addComponent(jRadioButtonModoNoDetallado))))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(jRadioButtonIngresarPreciosAlIngresarFacturas)
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel5)
                                        .addComponent(jRadioButtonSoloLeche)
                                        .addComponent(jLabel3))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextFieldMaximoBoletaConsumoFinal)
                                        .addComponent(jTextFieldProximoNumeroDeFactura)
                                        .addComponent(jTextFieldporcentajeFacturacion, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel4)))))
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jRadioButtonModoDetallado)
                        .addComponent(jRadioButtonModoNoDetallado))
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jRadioButtonDetalladoPorArt)
                        .addComponent(jRadioButtonDetalladoPorGrupo))
                    .addGap(18, 18, 18)
                    .addComponent(jRadioButtonSoloLeche)
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jTextFieldProximoNumeroDeFactura, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTextFieldporcentajeFacturacion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldMaximoBoletaConsumoFinal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jRadioButtonIngresarPreciosAlIngresarFacturas)
                    .addGap(18, 22, Short.MAX_VALUE)
                    .addComponent(jButtonGuardar)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButtonSalir)
                    .addGap(20, 20, 20))
        );
        pack();
        setLocationRelativeTo(null);

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(jRadioButtonModoDetallado);
        buttonGroup1.add(jRadioButtonModoNoDetallado);

        //---- buttonGroup2 ----
        ButtonGroup buttonGroup2 = new ButtonGroup();
        buttonGroup2.add(jRadioButtonDetalladoPorArt);
        buttonGroup2.add(jRadioButtonDetalladoPorGrupo);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        // TODO add your handling code here:
        configuracionFacturacion.setDetalladaPorArticulo(jRadioButtonModoDetallado.isSelected());
        configuracionFacturacion.setSoloLeche(jRadioButtonSoloLeche.isSelected());
        configuracionFacturacion.setIngresarPreciosDeFacturasAlIngesarLasFacturas(jRadioButtonIngresarPreciosAlIngresarFacturas.isSelected());
        configuracionFacturacion.setDetalladoPorGrupoDeArticulo(jRadioButtonDetalladoPorGrupo.isSelected());
        try{
            Long numFact = Long.parseLong(jTextFieldProximoNumeroDeFactura.getText().trim());
            Double porcentajeFacturacion = Double.parseDouble(jTextFieldporcentajeFacturacion.getText().trim());
            Double maximoBoletaContadoFinal = Double.parseDouble(jTextFieldMaximoBoletaConsumoFinal.getText().trim());
            configuracionFacturacion.setUltimoNumeroFactura(numFact);
            configuracionFacturacion.setPorcentajeFacturacion(porcentajeFacturacion);
            configuracionFacturacion.setMaximoBoletaContadoFinal(maximoBoletaContadoFinal);
            sis.guardarConfiguracionDeFacturacion(configuracionFacturacion);
        } catch (NumberFormatException ne){
            JOptionPane.showMessageDialog(this, "El numero y el porcentaje debe ser un numero. Tambien el maximo de una boleta de consumo final.", "Información", JOptionPane.INFORMATION_MESSAGE);
            jTextFieldProximoNumeroDeFactura.requestFocus();
            jTextFieldProximoNumeroDeFactura.selectAll();
        } catch (Exception ex) {
            String stakTrace = util.Util.obtenerStackTraceEnString(ex);
            SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
        this.dispose();
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

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
            java.util.logging.Logger.getLogger(VentanaConfiguracionFacturacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaConfiguracionFacturacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaConfiguracionFacturacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaConfiguracionFacturacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaConfiguracionFacturacion dialog = new VentanaConfiguracionFacturacion(new javax.swing.JFrame(), true);
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
    // Generated using JFormDesigner Evaluation license - Eduardo Vecino
    private JLabel jLabel1;
    private JRadioButton jRadioButtonModoDetallado;
    private JRadioButton jRadioButtonModoNoDetallado;
    private JButton jButtonGuardar;
    private JButton jButtonSalir;
    private JRadioButton jRadioButtonSoloLeche;
    private JLabel jLabel2;
    private JTextField jTextFieldProximoNumeroDeFactura;
    private JLabel jLabel3;
    private JTextField jTextFieldporcentajeFacturacion;
    private JLabel jLabel4;
    private JTextField jTextFieldMaximoBoletaConsumoFinal;
    private JLabel jLabel5;
    private JRadioButton jRadioButtonDetalladoPorArt;
    private JRadioButton jRadioButtonDetalladoPorGrupo;
    private JRadioButton jRadioButtonIngresarPreciosAlIngresarFacturas;
    // End of variables declaration//GEN-END:variables
}
