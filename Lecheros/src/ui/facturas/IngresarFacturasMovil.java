/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.facturas;

import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import sistema.SistemaFacturas;
import sistema.SistemaUsuarios;

/**
 *
 * @author Edu
 */
public class IngresarFacturasMovil extends javax.swing.JDialog {

    private SistemaFacturas sisFacturas;
    
    private DefaultTableModel modelo;
    private final DecimalFormat df;
    
    private List<String[]> resultado;
    /**
     * Creates new form IngresarFacturasMovil
     */
    public IngresarFacturasMovil(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        jLabelEspera.setVisible(false);
        sisFacturas = SistemaFacturas.getInstance();
        inicializarTableResultado();
        df = new DecimalFormat("0.00");
    }

    public final void inicializarTableResultado() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Fecha");
        modelo.addColumn("Numero");
        modelo.addColumn("Cliente");
        modelo.addColumn("Reparto");
        modelo.addColumn("Total");
        modelo.addColumn("Mensaje");
        jTableResultado.setModel(modelo);
        TableColumn column = null;
        for (int i = 0; i < 6; i++) {
            column = jTableResultado.getColumnModel().getColumn(i);
            if (i == 5) {
                column.setPreferredWidth(250); //articulo column is bigger
            } else {
                column.setPreferredWidth(50);
            }
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

        jFileChooser1 = new javax.swing.JFileChooser();
        jLabelTitulo = new javax.swing.JLabel();
        jLabelEspera = new javax.swing.JLabel();
        jButtonBuscar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldRutaArchivo = new javax.swing.JTextField();
        jButtonIngresar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableResultado = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabelCantiIngresadasCorrectamente = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ingresar Facturas del Movil");

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Ingresar Facturas del Movil");

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        jButtonBuscar.setText("Buscar");
        jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarActionPerformed(evt);
            }
        });

        jLabel1.setText("Archivo:");

        jTextFieldRutaArchivo.setEditable(false);

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

        jTableResultado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Fecha", "Numero", "Cliente", "Reparto", "Total", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableResultado);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel2.setText("Cantidad Ingresada Correctamente: ");

        jLabelCantiIngresadasCorrectamente.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabelCantiIngresadasCorrectamente.setForeground(new java.awt.Color(0, 153, 0));
        jLabelCantiIngresadasCorrectamente.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(165, 165, 165)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                                    .addComponent(jButtonIngresar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabelEspera))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(127, 127, 127)
                                .addComponent(jLabelTitulo))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelCantiIngresadasCorrectamente))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldRutaArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonBuscar)))))
                        .addGap(0, 296, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo)
                .addGap(18, 18, 18)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabelCantiIngresadasCorrectamente))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
        // TODO add your handling code here:
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TSV","tsv");
        jFileChooser1.setFileFilter(filter);
        jFileChooser1.showOpenDialog(jLabel1);
        jTextFieldRutaArchivo.setText(jFileChooser1.getSelectedFile().getPath());
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    private void jButtonIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIngresarActionPerformed
        // TODO add your handling code here:
        inicializarTableResultado();
        if("".equals(jTextFieldRutaArchivo.getText().trim())){
            //Es vacio el campo que lleva la ruta del archivo
            JOptionPane.showMessageDialog(this, "Debe seleccionar un archivo .xml del cual ingresar las boletas. El archivo debe ser descargado desde el sistema de PS", "Información", JOptionPane.INFORMATION_MESSAGE);
            jButtonBuscar.requestFocus();
        } else {
            //No es vacio la ruta. Asi que llamo a sistema de compras para que ingrese las compras del archivo.
            try{
                jTextFieldRutaArchivo.setEnabled(false);
                jButtonBuscar.setEnabled(false);
                jLabelEspera.setVisible(true);
                jButtonSalir.setEnabled(false);
                jButtonIngresar.setEnabled(false);
                Thread worker = new Thread() {
                    public void run() {

                        try {
                            resultado = sisFacturas.ingresarFacturasDesdeArchivo(jTextFieldRutaArchivo.getText().trim());

                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    jTextFieldRutaArchivo.setEnabled(true);
                                    jButtonBuscar.setEnabled(true);
                                    jLabelEspera.setVisible(false);
                                    jButtonSalir.setEnabled(true);
                                    jButtonIngresar.setEnabled(true);
                                    if(!resultado.isEmpty()){
                                        String[] cantIngresadasCorrectamente = resultado.get(resultado.size()-1);
                                        jLabelCantiIngresadasCorrectamente.setText(cantIngresadasCorrectamente[0]);
                                        //jTextAreaResultado.setText("Facturas ingresadas correctamente.");
                                        for(int i = 0; i < resultado.size()-1; i++) {
                                            cargarRenglon(resultado.get(i));
                                        }
                                    } else {
                                        String texto = "";
                                        //Armar la respuesta texto dependiendo del resultado. 
                                        //jTextAreaResultado.setText(texto);
                                    }
                                }
                            });
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(IngresarFacturasMovil.this, "Error al ingresar las facturas desde el archivo seleccionado." + "\n\n" + ex.toString(),"Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };
                worker.start();
            }
            catch(Exception e){
                String stakTrace = util.Util.obtenerStackTraceEnString(e);
                SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                
                JOptionPane.showMessageDialog(this, "Error al ingresar las facturas desde el archivo seleccionado." + "\n\n" + e.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonIngresarActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void cargarRenglon(String[] r) {
        Object[] object = new Object[6];
        object[0] = r[0];
        object[1] = r[1];
        object[2] = r[2];
        object[3] = r[3];
        try {
            object[4] = df.format(Double.parseDouble(r[4])).replace(',', '.');
        } catch(NumberFormatException ne) {
            object[4] = r[4];
        }
        object[5] = r[5];
        modelo.addRow(object);
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
            java.util.logging.Logger.getLogger(IngresarFacturasMovil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresarFacturasMovil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresarFacturasMovil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresarFacturasMovil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IngresarFacturasMovil dialog = new IngresarFacturasMovil(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelCantiIngresadasCorrectamente;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableResultado;
    private javax.swing.JTextField jTextFieldRutaArchivo;
    // End of variables declaration//GEN-END:variables
}
