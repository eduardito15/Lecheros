/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.informes;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import sistema.SistemaInformes;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class VentanaInformeContadora extends javax.swing.JDialog {

    private boolean mostrarMensajeFechaIncorrecta = true;
    /**
     * Creates new form VentanaInformeContadora
     */
    public VentanaInformeContadora(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        agregarEnterCamposFecha();
        setearFechasInicio();
        jLabelEspera.setVisible(false);
        jDateChooserDesdeFecha.requestFocusInWindow();
    }

    public final void agregarEnterCamposFecha() {
        jLabelFechaIncorrecta.setVisible(false);
        jLabelHastaFechaIncorrecta.setVisible(false);
        jDateChooserDesdeFecha.getDateEditor().getUiComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jDateChooserHastaFecha.getDateEditor().getUiComponent().requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Calendar c = new GregorianCalendar();

                String annio = Integer.toString(c.get(Calendar.YEAR));

                switch (((JTextField) jDateChooserDesdeFecha.getDateEditor().getUiComponent()).getText().length()) {
                    case 2:
                        ((JTextField) jDateChooserDesdeFecha.getDateEditor().getUiComponent()).setText(((JTextField) jDateChooserDesdeFecha.getDateEditor().getUiComponent()).getText() + "/");
                        break;
                    case 5:
                        ((JTextField) jDateChooserDesdeFecha.getDateEditor().getUiComponent()).setText(((JTextField) jDateChooserDesdeFecha.getDateEditor().getUiComponent()).getText() + "/20");
                        break;
                }
            }
        });
        jDateChooserDesdeFecha.getDateEditor().getUiComponent().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                mostrarMensajeFechaIncorrecta = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (mostrarMensajeFechaIncorrecta) {
                    Date f = jDateChooserDesdeFecha.getDate();
                    if (f == null || f.after(jDateChooserDesdeFecha.getMaxSelectableDate()) || f.before(jDateChooserDesdeFecha.getMinSelectableDate())) {
                        //JOptionPane.showMessageDialog(IngresarCompras.this, "Fecha Incorrecta. Ingrese una fecha correcta. Debe ingresar los dos numeros del dia, los dos numeros del mes y los dos ultimos numeros del año");
                        if (f == null) {
                            jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");
                        } else if (f.after(jDateChooserDesdeFecha.getMaxSelectableDate()) || f.before(jDateChooserDesdeFecha.getMinSelectableDate())) {
                            jLabelFechaIncorrecta.setText("La fecha debe ser entre el 2000 y el 2100");
                        }
                        jLabelFechaIncorrecta.setVisible(true);
                        jDateChooserDesdeFecha.getDateEditor().getUiComponent().requestFocus();
                    } else {
                        jLabelFechaIncorrecta.setVisible(false);
                    }
                }
                mostrarMensajeFechaIncorrecta = true;
            }
        });
        jDateChooserDesdeFecha.getCalendarButton().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarMensajeFechaIncorrecta = false;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                jDateChooserDesdeFecha.getDateEditor().getUiComponent().requestFocus();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        //Campo Hasta Fecha
        jDateChooserHastaFecha.getDateEditor().getUiComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jButtonCrear.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Calendar c = new GregorianCalendar();

                String annio = Integer.toString(c.get(Calendar.YEAR));

                switch (((JTextField) jDateChooserHastaFecha.getDateEditor().getUiComponent()).getText().length()) {
                    case 2:
                        ((JTextField) jDateChooserHastaFecha.getDateEditor().getUiComponent()).setText(((JTextField) jDateChooserHastaFecha.getDateEditor().getUiComponent()).getText() + "/");
                        break;
                    case 5:
                        ((JTextField) jDateChooserHastaFecha.getDateEditor().getUiComponent()).setText(((JTextField) jDateChooserHastaFecha.getDateEditor().getUiComponent()).getText() + "/20");
                        break;
                }
            }
        });
        jDateChooserHastaFecha.getDateEditor().getUiComponent().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                mostrarMensajeFechaIncorrecta = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (mostrarMensajeFechaIncorrecta) {
                    Date f = jDateChooserHastaFecha.getDate();
                    if (f == null || f.after(jDateChooserHastaFecha.getMaxSelectableDate()) || f.before(jDateChooserHastaFecha.getMinSelectableDate())) {
                        //JOptionPane.showMessageDialog(IngresarCompras.this, "Fecha Incorrecta. Ingrese una fecha correcta. Debe ingresar los dos numeros del dia, los dos numeros del mes y los dos ultimos numeros del año");
                        if (f == null) {
                            jLabelHastaFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");
                        } else if (f.after(jDateChooserHastaFecha.getMaxSelectableDate()) || f.before(jDateChooserHastaFecha.getMinSelectableDate())) {
                            jLabelHastaFechaIncorrecta.setText("La fecha debe ser entre el 2000 y el 2100");
                        }
                        jLabelHastaFechaIncorrecta.setVisible(true);
                        jDateChooserHastaFecha.getDateEditor().getUiComponent().requestFocus();
                    } else {
                        jLabelHastaFechaIncorrecta.setVisible(false);
                    }
                }
                mostrarMensajeFechaIncorrecta = true;
            }
        });
        jDateChooserHastaFecha.getCalendarButton().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarMensajeFechaIncorrecta = false;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                jDateChooserHastaFecha.getDateEditor().getUiComponent().requestFocus();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

    }

    private void setearFechasInicio() {
        Calendar cal = Calendar.getInstance();
        int mes = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        Calendar calPrimerDiaMes = Calendar.getInstance();
        calPrimerDiaMes.set(year, mes, 1);
        jDateChooserDesdeFecha.setDate(calPrimerDiaMes.getTime());
        jDateChooserHastaFecha.setDate(cal.getTime());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelHastaFechaIncorrecta = new javax.swing.JLabel();
        jDateChooserDesdeFecha = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabelTitulo1 = new javax.swing.JLabel();
        jDateChooserHastaFecha = new com.toedter.calendar.JDateChooser();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButtonCrear = new javax.swing.JButton();
        jLabelEspera = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Informe Contador");

        jLabelHastaFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelHastaFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jDateChooserDesdeFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserDesdeFecha.setMaxSelectableDate(new java.util.Date(4102455704000L));
        jDateChooserDesdeFecha.setMinSelectableDate(new java.util.Date(946699294000L));

        jLabel4.setText("Hasta Fecha:");

        jLabelTitulo1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo1.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo1.setText("Informe Contador");

        jDateChooserHastaFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserHastaFecha.setMaxSelectableDate(new java.util.Date(4102455704000L));
        jDateChooserHastaFecha.setMinSelectableDate(new java.util.Date(946699294000L));

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabel3.setText("Desde Fecha:");

        jButtonCrear.setText("Crear");
        jButtonCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCrearActionPerformed(evt);
            }
        });
        jButtonCrear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonCrearKeyPressed(evt);
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabelFechaIncorrecta))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabelHastaFechaIncorrecta)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelTitulo1)
                                .addGap(223, 223, 223))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(249, 249, 249)
                        .addComponent(jButtonCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(272, 272, 272)
                .addComponent(jLabelEspera)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelFechaIncorrecta)
                    .addComponent(jLabelHastaFechaIncorrecta))
                .addGap(18, 18, 18)
                .addComponent(jButtonCrear)
                .addGap(18, 18, 18)
                .addComponent(jLabelEspera)
                .addContainerGap(81, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCrearActionPerformed
        // TODO add your handling code here:
        Date desdeFecha = jDateChooserDesdeFecha.getDate();
        Date hastaFecha = jDateChooserHastaFecha.getDate();
        jLabelEspera.setVisible(true);
        jButtonCrear.setEnabled(false);
        jDateChooserDesdeFecha.setEnabled(false);
        jDateChooserHastaFecha.setEnabled(false);
        Thread worker = new Thread() {
            public void run() {
                try {
                    SistemaInformes.getInstance().sacarInformeContadora(desdeFecha, hastaFecha);
                    // Report the result using invokeLater().
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            jLabelEspera.setVisible(false);
                            jButtonCrear.setEnabled(true);
                            jDateChooserHastaFecha.setEnabled(true);
                            jDateChooserDesdeFecha.setEnabled(true);
                            jDateChooserDesdeFecha.requestFocusInWindow();
                            setEnabled(true);
                        }
                    });
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(VentanaInformeContadora.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.start();
    }//GEN-LAST:event_jButtonCrearActionPerformed

    private void jButtonCrearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonCrearKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonCrear.doClick();
        }
    }//GEN-LAST:event_jButtonCrearKeyPressed

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
            java.util.logging.Logger.getLogger(VentanaInformeContadora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaInformeContadora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaInformeContadora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaInformeContadora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaInformeContadora dialog = new VentanaInformeContadora(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonCrear;
    private com.toedter.calendar.JDateChooser jDateChooserDesdeFecha;
    private com.toedter.calendar.JDateChooser jDateChooserHastaFecha;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelHastaFechaIncorrecta;
    private javax.swing.JLabel jLabelTitulo1;
    // End of variables declaration//GEN-END:variables
}


