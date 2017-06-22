/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.informes;

import dominio.RubroGasto;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import sistema.SistemaInformes;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.liquidaciones.VentanaBuscadorRubroGasto;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class VentanaInformeGastosPorRubro extends javax.swing.JFrame {

    private final SistemaMantenimiento sisMantenimiento;
    private final SistemaInformes sisInformes;

    private Integer rubroGasto;
    private boolean mostrarMensajeFechaIncorrecta = true;
    private DefaultTableModel modelo;

    /**
     * Creates new form VentanaInformeGastosPorRubro
     */
    public VentanaInformeGastosPorRubro(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        sisMantenimiento = SistemaMantenimiento.getInstance();
        sisInformes = SistemaInformes.getInstance();
        agregarEnterCamposFecha();
        setearFechasInicio();
        inicializarTabla();
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
                    jTextFieldRubro.requestFocus();
                    jTextFieldRubro.selectAll();
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

    public final void inicializarTabla() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        TableColumn column = null;
        for (int i = 0; i < 4; i++) {
            column = jTableResultado.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(150); //articulo column is bigger
            } else {
                column.setPreferredWidth(50);
            }
        }
        modelo.addColumn("Rubro");
        modelo.addColumn("Fecha");
        modelo.addColumn("Importe");
        modelo.addColumn("Horas");
        modelo.addColumn("Horas D Semanal");
        jTableResultado.setModel(modelo);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jDateChooserDesdeFecha = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jDateChooserHastaFecha = new com.toedter.calendar.JDateChooser();
        jLabelTitulo1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldRubro = new javax.swing.JTextField();
        jLabelDescripcionRubro = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableResultado = new javax.swing.JTable();
        jButtonBuscar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jLabelHastaFechaIncorrecta = new javax.swing.JLabel();
        jLabelEspera = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabelCantDias = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelCantHoras = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabelCantHorasDescansoSemanal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gastos Por Rubro");

        jLabel3.setText("Desde Fecha:");

        jDateChooserDesdeFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserDesdeFecha.setMaxSelectableDate(new java.util.Date(4102455694000L));
        jDateChooserDesdeFecha.setMinSelectableDate(new java.util.Date(946699272000L));

        jLabel4.setText("Hasta Fecha:");

        jDateChooserHastaFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserHastaFecha.setMaxSelectableDate(new java.util.Date(4102455694000L));
        jDateChooserHastaFecha.setMinSelectableDate(new java.util.Date(946699272000L));

        jLabelTitulo1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo1.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo1.setText("Gastos por Rubro");

        jLabel1.setText("Rubro:");

        jTextFieldRubro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldRubroFocusLost(evt);
            }
        });
        jTextFieldRubro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldRubroKeyPressed(evt);
            }
        });

        jLabelDescripcionRubro.setText("Descripción");

        jTableResultado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Rubro", "Fecha", "Importe", "Horas", "Horas D Semanal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableResultado);

        jButtonBuscar.setText("Buscar");
        jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarActionPerformed(evt);
            }
        });
        jButtonBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonBuscarKeyPressed(evt);
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

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabel2.setText("Total:");

        jLabelTotal.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelTotal.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTotal.setText("0");

        jLabel12.setForeground(new java.awt.Color(255, 0, 0));

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabelHastaFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelHastaFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabel5.setText("Cantidad de Dias:");

        jLabelCantDias.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelCantDias.setForeground(new java.awt.Color(0, 0, 255));
        jLabelCantDias.setText("0");

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabel6.setText("Cantidad de Horas:");

        jLabelCantHoras.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelCantHoras.setForeground(new java.awt.Color(0, 0, 255));
        jLabelCantHoras.setText("0");

        jLabel7.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabel7.setText("Cantidad de Horas D Semanal:");

        jLabelCantHorasDescansoSemanal.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelCantHorasDescansoSemanal.setForeground(new java.awt.Color(0, 0, 255));
        jLabelCantHorasDescansoSemanal.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(224, 224, 224)
                                .addComponent(jLabelTitulo1))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabelFechaIncorrecta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelHastaFechaIncorrecta))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel3))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabelTotal)
                                                .addGap(58, 58, 58))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel6)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(jLabel4))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addComponent(jTextFieldRubro, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabelDescripcionRubro)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel12)
                                                        .addGap(87, 87, 87)
                                                        .addComponent(jLabelEspera)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabelCantHoras)
                                                        .addGap(18, 18, 18)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jButtonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabel7)
                                                            .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(39, 39, 39)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelCantDias)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelCantHorasDescansoSemanal)))
                        .addGap(0, 60, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabelTitulo1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelFechaIncorrecta)
                    .addComponent(jLabelHastaFechaIncorrecta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonSalir))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextFieldRubro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)
                                .addComponent(jLabelDescripcionRubro)
                                .addComponent(jLabel12))
                            .addGap(32, 32, 32))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabelTotal)))
                    .addComponent(jLabelEspera))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelCantDias)
                        .addComponent(jLabel6)
                        .addComponent(jLabelCantHoras)
                        .addComponent(jLabel7)
                        .addComponent(jLabelCantHorasDescansoSemanal)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldRubroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldRubroKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("".equals(jTextFieldRubro.getText().trim())) {
                VentanaBuscadorRubroGasto vbrg = new VentanaBuscadorRubroGasto(this, true);
                vbrg.setVisible(true);
                if (vbrg.getRubroSeleccionado() != null) {
                    rubroGasto = vbrg.getRubroSeleccionado();
                    jTextFieldRubro.setText(Integer.toString(rubroGasto));
                    jButtonBuscar.requestFocus();
                } else {
                    jTextFieldRubro.requestFocus();
                    jTextFieldRubro.selectAll();
                }
            } else {
                jButtonBuscar.requestFocus();
            }
        }
    }//GEN-LAST:event_jTextFieldRubroKeyPressed

    private void jTextFieldRubroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldRubroFocusLost
        // TODO add your handling code here:
        if ("".equals(jTextFieldRubro.getText().trim())) {

        } else {
            try {
                int codigo = Integer.parseInt(jTextFieldRubro.getText());
                RubroGasto rg = sisMantenimiento.devolverRubroGastoPorCodigo(codigo);
                if (rg != null) {
                    jLabelDescripcionRubro.setText(rg.getNombre());
                    jLabel12.setText("");
                } else {
                    jLabel12.setText("El codigo de rubro no es valido.");
                    jLabelDescripcionRubro.setText("");
                    jTextFieldRubro.requestFocus();
                    jTextFieldRubro.selectAll();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "El codigo debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldRubro.requestFocus();
                jTextFieldRubro.selectAll();
            }
        }
    }//GEN-LAST:event_jTextFieldRubroFocusLost

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

    private void jButtonBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonBuscarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jButtonBuscarKeyPressed

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
        // TODO add your handling code here:
        inicializarTabla();
        jLabelTotal.setText("0");
        Date desdeFecha = jDateChooserDesdeFecha.getDate();
        Date hastaFecha = jDateChooserHastaFecha.getDate();
        try {
            int codigo = Integer.parseInt(jTextFieldRubro.getText());
            RubroGasto rg = sisMantenimiento.devolverRubroGastoPorCodigo(codigo);
            jLabelEspera.setVisible(true);
            jDateChooserDesdeFecha.setEnabled(false);
            jDateChooserHastaFecha.setEnabled(false);
            jTextFieldRubro.setEnabled(false);
            jButtonBuscar.setEnabled(false);
            Thread worker = new Thread() {
                public void run() {
                    try {
                        String[][] informe = sisInformes.informeGastosEntreFechasPorRubro(desdeFecha, hastaFecha, rg);
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                jLabelEspera.setVisible(false);
                                jDateChooserDesdeFecha.setEnabled(true);
                                jDateChooserHastaFecha.setEnabled(true);
                                jTextFieldRubro.setEnabled(true);
                                jButtonBuscar.setEnabled(true);
                                //jLabelCantDias.setText(Integer.toString(informe.length));
                                jLabelCantDias.setText("0");
                                cargarTabla(informe);
                            }
                        });
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(VentanaInformeGastosPorRubro.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.start();
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(this, "El codigo debe ser un numero.", "Información", JOptionPane.INFORMATION_MESSAGE);
            jTextFieldRubro.requestFocus();
            jTextFieldRubro.selectAll();
        }


    }//GEN-LAST:event_jButtonBuscarActionPerformed

    public void cargarTabla(String[][] tabla) {
        double total = 0;
        double cantHoras = 0;
        double cantHorasDescansoSemanal = 0;
        for (int i = 0; i < tabla.length; i++) {
            Object[] object = new Object[tabla[i].length];
            for (int j = 0; j < tabla[i].length; j++) {
                if (j == 2) {
                    total = total + Double.parseDouble(tabla[i][j]);
                }
                if(j == 3) {
                    cantHoras = cantHoras + Double.parseDouble(tabla[i][j]);
                }
                if(j == 4) {
                    cantHorasDescansoSemanal = cantHorasDescansoSemanal + Double.parseDouble(tabla[i][j]);
                }
                object[j] = tabla[i][j];
            }
            modelo.addRow(object);
        }
        jLabelTotal.setText(Double.toString(total));
        jLabelCantHoras.setText(Double.toString(cantHoras));
        jLabelCantHorasDescansoSemanal.setText(Double.toString(cantHorasDescansoSemanal));
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
            java.util.logging.Logger.getLogger(VentanaInformeGastosPorRubro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaInformeGastosPorRubro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaInformeGastosPorRubro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaInformeGastosPorRubro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaInformeGastosPorRubro dialog = new VentanaInformeGastosPorRubro(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonSalir;
    private com.toedter.calendar.JDateChooser jDateChooserDesdeFecha;
    private com.toedter.calendar.JDateChooser jDateChooserHastaFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelCantDias;
    private javax.swing.JLabel jLabelCantHoras;
    private javax.swing.JLabel jLabelCantHorasDescansoSemanal;
    private javax.swing.JLabel jLabelDescripcionRubro;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelHastaFechaIncorrecta;
    private javax.swing.JLabel jLabelTitulo1;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableResultado;
    private javax.swing.JTextField jTextFieldRubro;
    // End of variables declaration//GEN-END:variables
}
