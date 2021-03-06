/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.compras;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import sistema.SistemaCompras;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class IngresarComprasDesdeArchivo extends javax.swing.JDialog {

    private SistemaCompras sisCompras;

    /**
     * Creates new form IngresarComprasDesdeArchivo
     */
    public IngresarComprasDesdeArchivo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sisCompras = SistemaCompras.getInstance();
        jLabelEspera.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jButtonBuscar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldRutaArchivo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButtonIngresar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaResultado = new javax.swing.JTextArea();
        jLabelEspera = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ingresar Compras desde Archivo");

        jButtonBuscar.setText("Buscar");
        jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarActionPerformed(evt);
            }
        });

        jLabel1.setText("Archivo:");

        jTextFieldRutaArchivo.setEditable(false);

        jLabel8.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel8.setText("Ingresar Compras desde Archivo");

        jButtonIngresar.setText("Ingresar");
        jButtonIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIngresarActionPerformed(evt);
            }
        });

        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });

        jTextAreaResultado.setEditable(false);
        jTextAreaResultado.setColumns(20);
        jTextAreaResultado.setForeground(new java.awt.Color(255, 0, 0));
        jTextAreaResultado.setRows(5);
        jTextAreaResultado.setText("Resultado");
        jScrollPane1.setViewportView(jTextAreaResultado);

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(222, 222, 222)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                            .addComponent(jButtonIngresar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabelEspera)))
                .addContainerGap(171, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(69, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldRutaArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonBuscar))
                    .addComponent(jScrollPane1))
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldRutaArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBuscar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonIngresar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSalir))
                    .addComponent(jLabelEspera))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
        // TODO add your handling code here:
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml");
        jFileChooser1.setFileFilter(filter);
        jFileChooser1.showOpenDialog(jLabel1);
        jTextFieldRutaArchivo.setText(jFileChooser1.getSelectedFile().getPath());
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jButtonIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIngresarActionPerformed
        // TODO add your handling code here:
        if ("".equals(jTextFieldRutaArchivo.getText().trim())) {
            //Es vacio el campo que lleva la ruta del archivo
            JOptionPane.showMessageDialog(this, "Debe seleccionar un archivo .xml del cual ingresar las boletas. El archivo debe ser descargado desde el sistema de Conaprole", "Información", JOptionPane.INFORMATION_MESSAGE);
            jButtonBuscar.requestFocus();
        } else {
            //No es vacio la ruta. Asi que llamo a sistema de compras para que ingrese las compras del archivo.
            try {
                jTextFieldRutaArchivo.setEnabled(false);
                jButtonBuscar.setEnabled(false);
                jLabelEspera.setVisible(true);
                Thread worker = new Thread() {
                    public void run() {

                        try {
                            List<String> articulosModificados = sisCompras.ingresarComprasDesdeArchivo(jTextFieldRutaArchivo.getText().trim());

                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    jTextFieldRutaArchivo.setEnabled(true);
                                    jButtonBuscar.setEnabled(true);
                                    jLabelEspera.setVisible(false);
                                    if (articulosModificados.isEmpty()) {
                                        jTextAreaResultado.setText("Compras ingresadas correctamente.");
                                    } else {
                                        String texto = "";
                                        if (!articulosModificados.get(0).equals("Los siguientes numero de boletas no fueron ingresados por que ya existen:\n")) {
                                            texto = "Se modificaron los precios de los artículo: \n";
                                        }
                                        for (int i = 0; i < articulosModificados.size(); i++) {
                                            if (i == articulosModificados.size() - 1) {
                                                String aux = articulosModificados.get(i);
                                                if (!"Los siguientes codigos no tenian precio, (se les agrego el precio de compra de la boleta y el mismo precio de venta):".equals(aux)) {
                                                    texto = "\n" + texto + " " + articulosModificados.get(i);
                                                }
                                            } else {
                                                texto = texto + " " + articulosModificados.get(i);
                                                //Agrego en el string un salto de lina cada 10 articulos.
                                                if (i == 10 || i == 20 || i == 30 || i == 50 || i == 60 || i == 70 || i == 80 || i == 90 || i == 100) {
                                                    texto = texto + " \n ";
                                                }
                                            }
                                        }
                                        jTextAreaResultado.setText(texto);
                                    }
                                }
                            });
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(IngresarComprasDesdeArchivo.this, "Error al ingresar las compras desde el archivo seleccionado." + "\n\n" + ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };
                worker.start();
            } catch (Exception e) {
                String stakTrace = util.Util.obtenerStackTraceEnString(e);
                SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                
                jTextFieldRutaArchivo.setEnabled(true);
                jButtonBuscar.setEnabled(true);
                jLabelEspera.setVisible(false);
                JOptionPane.showMessageDialog(this, "Error al ingresar las compras desde el archivo seleccionado." + "\n\n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonIngresarActionPerformed

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
            java.util.logging.Logger.getLogger(IngresarComprasDesdeArchivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresarComprasDesdeArchivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresarComprasDesdeArchivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresarComprasDesdeArchivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IngresarComprasDesdeArchivo dialog = new IngresarComprasDesdeArchivo(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonIngresar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaResultado;
    private javax.swing.JTextField jTextFieldRutaArchivo;
    // End of variables declaration//GEN-END:variables
}
