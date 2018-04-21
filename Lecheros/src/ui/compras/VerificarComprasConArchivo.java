/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.compras;

import dominio.Reparto;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import sistema.SistemaCompras;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.liquidaciones.VentanaLiquidacion;

/**
 *
 * @author Edu
 */
public class VerificarComprasConArchivo extends javax.swing.JDialog {

    private DefaultTableModel modelo;
    private final DecimalFormat df;
    
    private List<String[]> resultado;
    
    private SistemaCompras sisCompras;
    private SistemaMantenimiento sisMantenimiento;
    
    private boolean mostrarMensajeFechaIncorrecta = true;
    /**
     * Creates new form VerificarComprasConArchivo
     */
    public VerificarComprasConArchivo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sisCompras = SistemaCompras.getInstance();
        sisMantenimiento = SistemaMantenimiento.getInstance();
        jLabelFechaIncorrecta.setVisible(false);
        jLabelEspera.setVisible(false);
        inicializarTableResultado();
        df = new DecimalFormat("0.00");
        jComboBoxReparto.addItem("");
        List<Reparto> repartos = sisMantenimiento.devolverRepartos();
        for (Reparto c : repartos) {
            jComboBoxReparto.addItem(c);
        }
    }

    public final void inicializarTableResultado() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Fecha");
        modelo.addColumn("Tipo Documento");
        modelo.addColumn("Numero Archivo");
        modelo.addColumn("Numero Sistema");
        modelo.addColumn("Total Archivo");
        modelo.addColumn("Total Sistema");
        modelo.addColumn("Resultado");
        jTableResultado.setModel(modelo);
        TableColumn column = null;
        for (int i = 0; i < 7; i++) {
            column = jTableResultado.getColumnModel().getColumn(i);
            if (i == 6) {
                column.setPreferredWidth(250); //articulo column is bigger
            } else {
                if(i == 1) {
                    column.setPreferredWidth(50);
                } else {
                    column.setPreferredWidth(40);
                }
            }
        }
    }
    
    public final void agregarEnterCampoFecha() {
        jLabelFechaIncorrecta.setVisible(false);
        Date fechaDeHoy = new Date();
        jDateChooserFecha.setDate(fechaDeHoy);
        jDateChooserFecha.getDateEditor().getUiComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jComboBoxReparto.requestFocus();
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    VerificarComprasConArchivo.this.dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Calendar c = new GregorianCalendar();

                String annio = Integer.toString(c.get(Calendar.YEAR));

                switch (((JTextField) jDateChooserFecha.getDateEditor().getUiComponent()).getText().length()) {
                    case 2:
                        ((JTextField) jDateChooserFecha.getDateEditor().getUiComponent()).setText(((JTextField) jDateChooserFecha.getDateEditor().getUiComponent()).getText() + "/");
                        break;
                    case 5:
                        ((JTextField) jDateChooserFecha.getDateEditor().getUiComponent()).setText(((JTextField) jDateChooserFecha.getDateEditor().getUiComponent()).getText() + "/20");
                        break;
                }
            }
        });
        jDateChooserFecha.getDateEditor().getUiComponent().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                mostrarMensajeFechaIncorrecta = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (mostrarMensajeFechaIncorrecta) {
                    Date f = jDateChooserFecha.getDate();
                    if (f == null || f.after(jDateChooserFecha.getMaxSelectableDate()) || f.before(jDateChooserFecha.getMinSelectableDate())) {
                        //JOptionPane.showMessageDialog(IngresarCompras.this, "Fecha Incorrecta. Ingrese una fecha correcta. Debe ingresar los dos numeros del dia, los dos numeros del mes y los dos ultimos numeros del año");
                        if (f == null) {
                            jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");
                        } else {
                            if (f.after(jDateChooserFecha.getMaxSelectableDate()) || f.before(jDateChooserFecha.getMinSelectableDate())) {
                                jLabelFechaIncorrecta.setText("La fecha debe ser entre el 2000 y el 2100");
                            }
                        }
                        jLabelFechaIncorrecta.setVisible(true);
                        jDateChooserFecha.getDateEditor().getUiComponent().requestFocus();
                    } else {
                        jLabelFechaIncorrecta.setVisible(false);
                        jComboBoxReparto.requestFocus();
                    }
                }
                mostrarMensajeFechaIncorrecta = true;
            }
        });
        jDateChooserFecha.getCalendarButton().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarMensajeFechaIncorrecta = false;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                jDateChooserFecha.getDateEditor().getUiComponent().requestFocus();
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
        jButtonBuscarRelece = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldRutaArchivoRelece = new javax.swing.JTextField();
        jButtonBuscarCerram = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldRutaArchivoCerram = new javax.swing.JTextField();
        jLabelEspera = new javax.swing.JLabel();
        jButtonVerificar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jDateChooserFecha = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxReparto = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableResultado = new javax.swing.JTable();
        jLabelFechaIncorrecta = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Verificar Compras con Archivo");

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Verificar Compras con Archivo");

        jButtonBuscarRelece.setText("Buscar");
        jButtonBuscarRelece.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarReleceActionPerformed(evt);
            }
        });

        jLabel1.setText("Archivo Relece:");

        jTextFieldRutaArchivoRelece.setEditable(false);

        jButtonBuscarCerram.setText("Buscar");
        jButtonBuscarCerram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarCerramActionPerformed(evt);
            }
        });

        jLabel2.setText("Archivo Cerram:");

        jTextFieldRutaArchivoCerram.setEditable(false);

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        jButtonVerificar.setText("Verificar");
        jButtonVerificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerificarActionPerformed(evt);
            }
        });

        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });

        jLabel3.setText("Fecha Liq:");

        jDateChooserFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserFecha.setMaxSelectableDate(new java.util.Date(4102455689000L));
        jDateChooserFecha.setMinSelectableDate(new java.util.Date(946699289000L));

        jLabel4.setText("Reparto:");

        jComboBoxReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxRepartoKeyPressed(evt);
            }
        });

        jTableResultado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Fecha", "Tipo Documento", "Numero Archivo", "Numero Sistema", "Total Archivo", "Total Sistema", "Resultado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableResultado);

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(309, 309, 309)
                                .addComponent(jLabelTitulo))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldRutaArchivoRelece, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonBuscarRelece))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel4))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jDateChooserFecha, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jTextFieldRutaArchivoCerram, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                                            .addComponent(jComboBoxReparto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButtonBuscarCerram)
                                            .addComponent(jLabelFechaIncorrecta)))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(244, 244, 244)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonVerificar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabelEspera)))
                        .addGap(0, 458, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabelTitulo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldRutaArchivoRelece, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBuscarRelece))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldRutaArchivoCerram, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBuscarCerram))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelFechaIncorrecta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonVerificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSalir))
                    .addComponent(jLabelEspera))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBuscarReleceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarReleceActionPerformed
        // TODO add your handling code here:
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML","xml");
        jFileChooser1.setFileFilter(filter);
        jFileChooser1.showOpenDialog(jLabel1);
        jTextFieldRutaArchivoRelece.setText(jFileChooser1.getSelectedFile().getPath());
    }//GEN-LAST:event_jButtonBuscarReleceActionPerformed

    private void jButtonBuscarCerramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarCerramActionPerformed
        // TODO add your handling code here:
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML","xml");
        jFileChooser1.setFileFilter(filter);
        jFileChooser1.showOpenDialog(jLabel1);
        jTextFieldRutaArchivoCerram.setText(jFileChooser1.getSelectedFile().getPath());
    }//GEN-LAST:event_jButtonBuscarCerramActionPerformed

    private void jButtonVerificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerificarActionPerformed
        // TODO add your handling code here:
        inicializarTableResultado();
        if("".equals(jTextFieldRutaArchivoCerram.getText().trim())){
            //Es vacio el campo que lleva la ruta del archivo
            JOptionPane.showMessageDialog(this, "Debe seleccionar un archivo .xml de cerram con el cual verificar las boletas. El archivo debe ser descargado de Conaprole", "Información", JOptionPane.INFORMATION_MESSAGE);
            jButtonBuscarCerram.requestFocus();
        } else {
            if("".equals(jTextFieldRutaArchivoRelece.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un archivo .xml de relece con el cual verificar las boletas. El archivo debe ser descargado de Conaprole", "Información", JOptionPane.INFORMATION_MESSAGE);
                jButtonBuscarRelece.requestFocus();
            } else {
            //No es vacio la ruta. Asi que llamo a sistema de compras para que ingrese las compras del archivo.
            try{
                jTextFieldRutaArchivoCerram.setEnabled(false);
                jTextFieldRutaArchivoRelece.setEnabled(false);
                jButtonBuscarCerram.setEnabled(false);
                jButtonBuscarRelece.setEnabled(false);
                jLabelEspera.setVisible(true);
                jButtonSalir.setEnabled(false);
                jButtonVerificar.setEnabled(false);
                jDateChooserFecha.setEnabled(false);
                Thread worker = new Thread() {
                    public void run() {

                        try {
                            resultado = sisCompras.verificarComprasConArchivo(jTextFieldRutaArchivoCerram.getText().trim(), jTextFieldRutaArchivoRelece.getText().trim(), jComboBoxReparto.getSelectedIndex() != 0 ? (Reparto)jComboBoxReparto.getSelectedItem():null, jDateChooserFecha.getDate());

                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    jTextFieldRutaArchivoCerram.setEnabled(true);
                                    jTextFieldRutaArchivoRelece.setEnabled(true);
                                    jButtonBuscarCerram.setEnabled(true);
                                    jButtonBuscarRelece.setEnabled(true);
                                    jLabelEspera.setVisible(false);
                                    jButtonSalir.setEnabled(true);
                                    jButtonVerificar.setEnabled(true);
                                    jDateChooserFecha.setEnabled(true);
                                    if(!resultado.isEmpty()){
                                        String[] cantIngresadasCorrectamente = resultado.get(resultado.size()-1);
                                        //jTextAreaResultado.setText("Facturas ingresadas correctamente.");
                                        for(int i = 0; i < resultado.size(); i++) {
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
                            JOptionPane.showMessageDialog(VerificarComprasConArchivo.this, "Error al verificar las compras." + "\n\n" + ex.toString(),"Error",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };
                worker.start();
            }
            catch(Exception e){
                String stakTrace = util.Util.obtenerStackTraceEnString(e);
                SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);

                JOptionPane.showMessageDialog(this, "Error al verificar las compras." + "\n\n" + e.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            }
            
            }
        }
    }//GEN-LAST:event_jButtonVerificarActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jComboBoxRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxRepartoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonVerificar.requestFocus();
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

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
            java.util.logging.Logger.getLogger(VerificarComprasConArchivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VerificarComprasConArchivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VerificarComprasConArchivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VerificarComprasConArchivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VerificarComprasConArchivo dialog = new VerificarComprasConArchivo(new javax.swing.JFrame(), true);
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
    
    private void cargarRenglon(String[] r) {
        Object[] object = new Object[7];
        object[0] = r[0];
        object[1] = r[1];
        object[2] = r[2];
        object[3] = r[3];
        try {
            object[4] = df.format(Double.parseDouble(r[4])).replace(',', '.');
            object[5] = df.format(Double.parseDouble(r[5])).replace(',', '.');
        } catch(NumberFormatException ne) {
            object[4] = r[4];
            object[5] = r[5];
        }
        object[6] = r[6];
        modelo.addRow(object);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBuscarCerram;
    private javax.swing.JButton jButtonBuscarRelece;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JButton jButtonVerificar;
    private javax.swing.JComboBox jComboBoxReparto;
    private com.toedter.calendar.JDateChooser jDateChooserFecha;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableResultado;
    private javax.swing.JTextField jTextFieldRutaArchivoCerram;
    private javax.swing.JTextField jTextFieldRutaArchivoRelece;
    // End of variables declaration//GEN-END:variables
}
