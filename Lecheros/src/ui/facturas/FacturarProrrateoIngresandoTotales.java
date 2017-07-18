/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.facturas;

import dominio.Articulo;
import dominio.GrupoDeArticulos;
import dominio.Precio;
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
import sistema.SistemaFacturas;
import sistema.SistemaMantenimiento;
import sistema.SistemaMantenimientoArticulos;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class FacturarProrrateoIngresandoTotales extends javax.swing.JDialog {

    private SistemaMantenimiento sis;
    private SistemaFacturas sisFacturas;

    private String[][] detallesArticulosParaFacturacion;

    private final DecimalFormat df;
    private boolean mostrarMensajeFechaIncorrecta;
    
    /**
     * Creates new form FacturarProrrateoIngresandoTotales
     */
    public FacturarProrrateoIngresandoTotales(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sis = SistemaMantenimiento.getInstance();
        sisFacturas = SistemaFacturas.getInstance();
        df = new DecimalFormat("0.00");
        jLabelEspera.setVisible(false);
        jComboBoxReparto.addItem("");
        List<Reparto> repartos = sis.devolverRepartos();
        for (Reparto c : repartos) {
            jComboBoxReparto.addItem(c);
        }
        agregarEnterCamposFecha();
        setearFechasInicio();
        detallesArticulosParaFacturacion = new String[5][5];
        jLabelEspera.setVisible(false);
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
                    jComboBoxReparto.requestFocus();
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

    public final void setearFechasInicio() {
        Calendar cal = Calendar.getInstance();
        jDateChooserDesdeFecha.setDate(cal.getTime());
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

        jLabelTitulo1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jDateChooserDesdeFecha = new com.toedter.calendar.JDateChooser();
        jDateChooserHastaFecha = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxReparto = new javax.swing.JComboBox();
        jLabelEspera = new javax.swing.JLabel();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jLabelHastaFechaIncorrecta = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldLitrosComun = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldLitrosUltra = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldLitrosUltraDiferenciada = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldLitrosDeslactosada = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldProductosMinimo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldProductosBasico = new javax.swing.JTextField();
        jButtonFacturar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Facturar Prorrateo");

        jLabelTitulo1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo1.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo1.setText("Facturar Prorrateo");

        jLabel1.setText("Desde Fecha:");

        jLabel2.setText("Hasta Fecha:");

        jDateChooserDesdeFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserDesdeFecha.setMaxSelectableDate(new java.util.Date(4102455686000L));
        jDateChooserDesdeFecha.setMinSelectableDate(new java.util.Date(946699286000L));

        jDateChooserHastaFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserHastaFecha.setMaxSelectableDate(new java.util.Date(4102455689000L));
        jDateChooserHastaFecha.setMinSelectableDate(new java.util.Date(946699289000L));

        jLabel3.setText("Reparto:");

        jComboBoxReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxRepartoKeyPressed(evt);
            }
        });

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabelHastaFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelHastaFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabel4.setText("Litros Común:");

        jTextFieldLitrosComun.setText("0");
        jTextFieldLitrosComun.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldLitrosComunKeyPressed(evt);
            }
        });

        jLabel5.setText("Litros Ultra:");

        jTextFieldLitrosUltra.setText("0");
        jTextFieldLitrosUltra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldLitrosUltraKeyPressed(evt);
            }
        });

        jLabel6.setText("Litros Ultra Diferenciada:");

        jTextFieldLitrosUltraDiferenciada.setText("0");
        jTextFieldLitrosUltraDiferenciada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldLitrosUltraDiferenciadaKeyPressed(evt);
            }
        });

        jLabel7.setText("Litros Deslactosada:");

        jTextFieldLitrosDeslactosada.setText("0");
        jTextFieldLitrosDeslactosada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldLitrosDeslactosadaKeyPressed(evt);
            }
        });

        jLabel8.setText("Productos Minimo:");

        jTextFieldProductosMinimo.setText("0");
        jTextFieldProductosMinimo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldProductosMinimoKeyPressed(evt);
            }
        });

        jLabel9.setText("Productos Basico:");

        jTextFieldProductosBasico.setText("0");
        jTextFieldProductosBasico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldProductosBasicoKeyPressed(evt);
            }
        });

        jButtonFacturar.setText("Facturar");
        jButtonFacturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFacturarActionPerformed(evt);
            }
        });
        jButtonFacturar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonFacturarKeyPressed(evt);
            }
        });

        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });
        jButtonSalir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonSalirKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jLabelTitulo1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabelEspera)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelFechaIncorrecta)
                                    .addComponent(jLabelHastaFechaIncorrecta)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldLitrosDeslactosada, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldLitrosComun, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldProductosMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel9))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addComponent(jLabel5)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTextFieldLitrosUltra, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel6))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jButtonSalir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButtonFacturar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldProductosBasico)
                                    .addComponent(jTextFieldLitrosUltraDiferenciada, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))))))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabelEspera)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelFechaIncorrecta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelHastaFechaIncorrecta)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldLitrosComun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldLitrosUltra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldLitrosUltraDiferenciada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextFieldLitrosDeslactosada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jTextFieldProductosMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jTextFieldProductosBasico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButtonFacturar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSalir)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxRepartoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextFieldLitrosComun.requestFocus();
            jTextFieldLitrosComun.selectAll();
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

    private void jTextFieldLitrosComunKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLitrosComunKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextFieldLitrosUltra.requestFocus();
            jTextFieldLitrosUltra.selectAll();
        }
    }//GEN-LAST:event_jTextFieldLitrosComunKeyPressed

    private void jTextFieldLitrosUltraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLitrosUltraKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextFieldLitrosUltraDiferenciada.requestFocus();
            jTextFieldLitrosUltraDiferenciada.selectAll();
        }
    }//GEN-LAST:event_jTextFieldLitrosUltraKeyPressed

    private void jTextFieldLitrosUltraDiferenciadaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLitrosUltraDiferenciadaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextFieldLitrosDeslactosada.requestFocus();
            jTextFieldLitrosDeslactosada.selectAll();
        }
    }//GEN-LAST:event_jTextFieldLitrosUltraDiferenciadaKeyPressed

    private void jTextFieldLitrosDeslactosadaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLitrosDeslactosadaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextFieldProductosMinimo.requestFocus();
            jTextFieldProductosMinimo.selectAll();
        }
    }//GEN-LAST:event_jTextFieldLitrosDeslactosadaKeyPressed

    private void jTextFieldProductosMinimoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldProductosMinimoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextFieldProductosBasico.requestFocus();
            jTextFieldProductosBasico.selectAll();
        }
    }//GEN-LAST:event_jTextFieldProductosMinimoKeyPressed

    private void jTextFieldProductosBasicoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldProductosBasicoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonFacturar.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldProductosBasicoKeyPressed

    private void jButtonFacturarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonFacturarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonFacturar.doClick();
        }
    }//GEN-LAST:event_jButtonFacturarKeyPressed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jButtonSalirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonSalirKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonSalir.doClick();
        }
    }//GEN-LAST:event_jButtonSalirKeyPressed

    private void jButtonFacturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFacturarActionPerformed
        // TODO add your handling code here:
        try {
            int litrosComun = Integer.parseInt(jTextFieldLitrosComun.getText().trim());
            int litrosUltra = Integer.parseInt(jTextFieldLitrosUltra.getText().trim());
            int litrosUltraDiferenciada = Integer.parseInt(jTextFieldLitrosUltraDiferenciada.getText().trim());
            int litrosDeslactosada = Integer.parseInt(jTextFieldLitrosDeslactosada.getText().trim());
            double productosMinimo = Double.parseDouble(jTextFieldProductosMinimo.getText().trim());
            double productosBasico = Double.parseDouble(jTextFieldProductosBasico.getText().trim());
            
            GrupoDeArticulos grupoLecheComun = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Común");

            GrupoDeArticulos grupoLecheUtra = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra");

            GrupoDeArticulos grupoLecheUltraDiferenciada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Ultra Diferenciada");

            GrupoDeArticulos grupoLecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverGrupoDeArticuloPorNombre("Leche Deslactosada");
            
            double totalExcentoParaFacturar = 0;
            double porcentajeDeNoFacturacion = 100 - SistemaMantenimiento.getInstance().devolverConfiguracionFacturacion().getPorcentajeFacturacion();

            Articulo lecheComun = null;
            Precio pLecheComun = null;
            if (!grupoLecheComun.getArticulos().isEmpty()) {
                lecheComun = grupoLecheComun.getArticulos().get(0);
                pLecheComun = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheComun, jDateChooserHastaFecha.getDate());
                totalExcentoParaFacturar = totalExcentoParaFacturar + pLecheComun.getPrecioVenta()*litrosComun;
                detallesArticulosParaFacturacion[0][0] = Integer.toString(lecheComun.getCodigo());
                Double cantidadTotal = litrosComun - ((litrosComun * porcentajeDeNoFacturacion) / 100);
                detallesArticulosParaFacturacion[0][4] = Integer.toString(cantidadTotal.intValue());
            }

            Articulo lecheUltra = null;
            Precio pLecheUltra = null;
            if (!grupoLecheUtra.getArticulos().isEmpty()) {
                lecheUltra = grupoLecheUtra.getArticulos().get(0);
                pLecheUltra = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheUltra, jDateChooserHastaFecha.getDate());
                totalExcentoParaFacturar = totalExcentoParaFacturar + pLecheUltra.getPrecioVenta()*litrosUltra;
                detallesArticulosParaFacturacion[1][0] = Integer.toString(lecheUltra.getCodigo());
                Double cantidadTotal = litrosUltra - ((litrosUltra * porcentajeDeNoFacturacion) / 100);
                detallesArticulosParaFacturacion[1][4] = Integer.toString(cantidadTotal.intValue());
            }

            Articulo lecheUltraDiferenciada = null;
            Precio pLecheUltraDiferenciada = null;
            if ((grupoLecheUltraDiferenciada != null ? !grupoLecheUltraDiferenciada.getArticulos().isEmpty() : false)) {
                lecheUltraDiferenciada = grupoLecheUltraDiferenciada.getArticulos().get(0);
                pLecheUltraDiferenciada = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheUltraDiferenciada, jDateChooserHastaFecha.getDate());
                totalExcentoParaFacturar = totalExcentoParaFacturar + pLecheUltraDiferenciada.getPrecioVenta() * litrosUltraDiferenciada;
                detallesArticulosParaFacturacion[2][0] = Integer.toString(lecheUltraDiferenciada.getCodigo());
                Double cantidadTotal = litrosUltraDiferenciada - ((litrosUltraDiferenciada * porcentajeDeNoFacturacion) / 100);
                detallesArticulosParaFacturacion[2][4] = Integer.toString(cantidadTotal.intValue());
            }

            Articulo lecheDeslactosada = null;
            Precio pLecheDeslactosada = null;
            if (!grupoLecheDeslactosada.getArticulos().isEmpty()) {
                lecheDeslactosada = grupoLecheDeslactosada.getArticulos().get(0);
                pLecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheDeslactosada, jDateChooserHastaFecha.getDate());
                totalExcentoParaFacturar = totalExcentoParaFacturar + pLecheDeslactosada.getPrecioVenta()*litrosDeslactosada;
                detallesArticulosParaFacturacion[3][0] = Integer.toString(lecheDeslactosada.getCodigo());
                Double cantidadTotal = litrosDeslactosada - ((litrosDeslactosada * porcentajeDeNoFacturacion) / 100);
                detallesArticulosParaFacturacion[3][4] = Integer.toString(cantidadTotal.intValue());
            }
            
            detallesArticulosParaFacturacion[4][0] = "Totales";
            detallesArticulosParaFacturacion[4][1] = Double.toString(totalExcentoParaFacturar);
            detallesArticulosParaFacturacion[4][2] = Double.toString(productosMinimo);
            detallesArticulosParaFacturacion[4][3] = Double.toString(productosBasico);
            
            Reparto r;
            if (jComboBoxReparto.getSelectedIndex() == 0) {
                r = null;
            } else {
                r = (Reparto) jComboBoxReparto.getSelectedItem();
            }
            
            jDateChooserDesdeFecha.setEnabled(false);
            jDateChooserHastaFecha.setEnabled(false);
            jComboBoxReparto.setEnabled(false);
            jButtonFacturar.setEnabled(false);
            jLabelEspera.setVisible(true);
            
            double ivaMinimo = (productosMinimo * 9.1) / 100;
            double ivaBasico = (productosBasico * 18.03) / 100;
            
            final double totExcento = totalExcentoParaFacturar;
            final double prodsMinimo = productosMinimo - ivaMinimo;
            final double prodsBasico = productosBasico - ivaBasico;
            
            Thread worker = new Thread() {
                public void run() {
                    String[] facturarProrrateo;
                    
                    if (SistemaMantenimiento.getInstance().devolverConfiguracionFacturacion().isSoloLeche()) {
                        facturarProrrateo = sisFacturas.facturarProrrateoIngresandoTotales(jDateChooserDesdeFecha.getDate(), jDateChooserHastaFecha.getDate(), r, detallesArticulosParaFacturacion, totExcento , prodsMinimo, prodsBasico, true, true);
                    } else {
                        facturarProrrateo = sisFacturas.facturarProrrateoIngresandoTotales(jDateChooserDesdeFecha.getDate(), jDateChooserHastaFecha.getDate(), r, detallesArticulosParaFacturacion, totExcento, prodsMinimo, prodsBasico, true, true);
                    }

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            jLabelEspera.setVisible(false);
                            jDateChooserDesdeFecha.setEnabled(true);
                            jDateChooserHastaFecha.setEnabled(true);
                            jComboBoxReparto.setEnabled(true);
                            jButtonFacturar.setEnabled(true);
                            //Cargo el Resultado
                            jButtonSalir.requestFocus();
                        }
                    });

                }
            };
            worker.start();
            
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(this, "Los litros y totales de productos deben ser números.", "Información", JOptionPane.INFORMATION_MESSAGE);
            jTextFieldLitrosComun.requestFocus();
            jTextFieldLitrosComun.selectAll();
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonFacturarActionPerformed

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
            java.util.logging.Logger.getLogger(FacturarProrrateoIngresandoTotales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FacturarProrrateoIngresandoTotales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FacturarProrrateoIngresandoTotales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FacturarProrrateoIngresandoTotales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FacturarProrrateoIngresandoTotales dialog = new FacturarProrrateoIngresandoTotales(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonFacturar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JComboBox jComboBoxReparto;
    private com.toedter.calendar.JDateChooser jDateChooserDesdeFecha;
    private com.toedter.calendar.JDateChooser jDateChooserHastaFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelHastaFechaIncorrecta;
    private javax.swing.JLabel jLabelTitulo1;
    private javax.swing.JTextField jTextFieldLitrosComun;
    private javax.swing.JTextField jTextFieldLitrosDeslactosada;
    private javax.swing.JTextField jTextFieldLitrosUltra;
    private javax.swing.JTextField jTextFieldLitrosUltraDiferenciada;
    private javax.swing.JTextField jTextFieldProductosBasico;
    private javax.swing.JTextField jTextFieldProductosMinimo;
    // End of variables declaration//GEN-END:variables
}
