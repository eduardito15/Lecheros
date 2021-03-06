/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.mantenimiento;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import lecheros.Lecheros;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class Respaldar extends javax.swing.JDialog {

    /**
     * Creates new form Respaldar
     */
    public Respaldar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        jLabelEspera.setVisible(false);
        Properties prop = new Properties();
        InputStream input = null;

	try {
            String sSistemaOperativo = System.getProperty("os.name");
            //System.out.println(sSistemaOperativo);
            if(sSistemaOperativo.equals("Mac OS X")) {
                input = new FileInputStream("src/config.properties");
            } else {
                String dir = System.getProperty("user.dir");
                input = new FileInputStream(dir + "\\config.properties");
            }

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            jTextFieldRutaRespaldo.setText(prop.getProperty("rutaRespaldo"));
            //System.out.println(prop.getProperty("dbuser"));
            //System.out.println(prop.getProperty("dbpassword"));

	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
            if (input != null) {
		try {
                    input.close();
		} catch (IOException e) {
                    e.printStackTrace();
		}
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
        jLabel1 = new javax.swing.JLabel();
        jTextFieldRutaRespaldo = new javax.swing.JTextField();
        jButtonCambiarRuta = new javax.swing.JButton();
        jButtonEjecutar = new javax.swing.JButton();
        jLabelEspera = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Respaldar");

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Respaldar");

        jLabel1.setText("Ruta para guardar Respaldo:");

        jTextFieldRutaRespaldo.setEditable(false);

        jButtonCambiarRuta.setText("Cambiar");
        jButtonCambiarRuta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCambiarRutaActionPerformed(evt);
            }
        });

        jButtonEjecutar.setText("Ejecutar");
        jButtonEjecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEjecutarActionPerformed(evt);
            }
        });

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButtonEjecutar)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelEspera))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextFieldRutaRespaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonCambiarRuta))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(231, 231, 231)
                        .addComponent(jLabelTitulo)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldRutaRespaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCambiarRuta))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonEjecutar)
                    .addComponent(jLabelEspera))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCambiarRutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCambiarRutaActionPerformed
        // TODO add your handling code here:
        jFileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jFileChooser1.showOpenDialog(jLabel1);
        jTextFieldRutaRespaldo.setText(jFileChooser1.getSelectedFile().getPath());
    }//GEN-LAST:event_jButtonCambiarRutaActionPerformed

    private void jButtonEjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEjecutarActionPerformed
        // TODO add your handling code here:
        jButtonEjecutar.setEnabled(false);
        jButtonCambiarRuta.setEnabled(false);
        jLabelEspera.setVisible(true);
        Thread worker = new Thread() {
            public void run() {
                try {
                    SistemaMantenimiento.getInstance().respaldarBaseDeDatos(jTextFieldRutaRespaldo.getText().trim());
                    // Report the result using invokeLater().
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            jLabelEspera.setVisible(false);
                            jButtonCambiarRuta.setEnabled(true);
                            jButtonEjecutar.setEnabled(true);
                            setEnabled(true);
                        }
                    });
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(Respaldar.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.start();
    }//GEN-LAST:event_jButtonEjecutarActionPerformed

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
            java.util.logging.Logger.getLogger(Respaldar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Respaldar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Respaldar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Respaldar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Respaldar dialog = new Respaldar(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonCambiarRuta;
    private javax.swing.JButton jButtonEjecutar;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JTextField jTextFieldRutaRespaldo;
    // End of variables declaration//GEN-END:variables
}
