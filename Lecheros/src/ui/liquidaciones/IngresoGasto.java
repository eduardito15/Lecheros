/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.liquidaciones;

import dominio.Gasto;
import dominio.GastoRenglon;
import dominio.Reparto;
import dominio.RubroGasto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import sistema.SistemaLiquidaciones;
import sistema.SistemaMantenimiento;
import sistema.SistemaMantenimientoArticulos;
import sistema.SistemaUsuarios;
import ui.TableCellListener;
import ui.compras.IngresarCompras;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class IngresoGasto extends javax.swing.JFrame {

    private DefaultTableModel modelo;
    private final SistemaLiquidaciones sis;
    private final SistemaMantenimiento sisMantenimiento;
    private final SistemaMantenimientoArticulos sisArticulos;
    private final DecimalFormat df;
    private Gasto gasto;

    private Integer rubroGasto;

    private Date fechaDeHoy;
    private boolean esNuevo = false;
    private boolean mostrarMensajeFechaIncorrecta = true;

    /**
     * Creates new form IngresoInventario
     */
    public IngresoGasto(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        jLabel12.setForeground(new java.awt.Color(255, 0, 51));
        inicializarTablaRenglones();
        sis = SistemaLiquidaciones.getInstance();
        sisMantenimiento = SistemaMantenimiento.getInstance();
        sisArticulos = SistemaMantenimientoArticulos.getInstance();
        jComboBoxReparto.addItem("");
        List<Reparto> repartos = sisMantenimiento.devolverRepartos();
        for (Reparto c : repartos) {
            jComboBoxReparto.addItem(c);
        }
        df = new DecimalFormat("0.00");
        agregarEnterCampoFecha();
        jDateChooserFecha.requestFocusInWindow();
        jButtonEliminar.setVisible(false);
    }

    public final void agregarEnterCampoFecha() {
        jLabelFechaIncorrecta.setVisible(false);
        setFechaDeHoy(new Date());
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
                    IngresoGasto.this.dispose();
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
                        } else if (f.after(jDateChooserFecha.getMaxSelectableDate()) || f.before(jDateChooserFecha.getMinSelectableDate())) {
                            jLabelFechaIncorrecta.setText("La fecha debe ser entre el 2000 y el 2100");
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

    public final void inicializarTablaRenglones() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column == 1 || column == 2 || column == 3){
                    return true;
                } else {
                    return false;
                }
            }
        };
        modelo.addColumn("Rubro");
        modelo.addColumn("Importe");
        modelo.addColumn("Horas Extra");
        modelo.addColumn("Horas Desc Semanal");
        jTableRenglones.setModel(modelo);

        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Eliminar");
        deleteItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                GastoRenglon gr = gasto.getRenglones().get(jTableRenglones.getSelectedRow());
                int resp = JOptionPane.showConfirmDialog(null, "Seguro que desea eliminar el rubro " + gr.getRubro().getNombre() + " ?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    modelo.removeRow(jTableRenglones.getSelectedRow());
                    gasto.getRenglones().remove(gr);
                    //Actualizo el total
                    gasto.setTotal(gasto.getTotal() - gr.getTotal());
                    jLabelTotal.setText(df.format(gasto.getTotal()).replace(',', '.'));
                }
            }

        });
        popupMenu.add(deleteItem);
        jTableRenglones.setComponentPopupMenu(popupMenu);
        
        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                //System.out.println("Row   : " + tcl.getRow());
                //System.out.println("Column: " + tcl.getColumn());
                GastoRenglon gr = gasto.getRenglones().get(tcl.getRow());
                try {
                    if (tcl.getColumn() == 1) {
                        double valorAnterior = Double.parseDouble((String) tcl.getOldValue());
                        double valorNuevo = Double.parseDouble((String) tcl.getNewValue());
                        if (valorAnterior != valorNuevo && gr.getTotal() != valorNuevo) {
                            gasto.setTotal(gasto.getTotal() - valorAnterior + valorNuevo);
                            jLabelTotal.setText(df.format(gasto.getTotal()).replace(',', '.'));
                            gr.setTotal(valorNuevo);
                            modelo.setValueAt(df.format(gr.getTotal()).replace(',', '.'), tcl.getRow(), tcl.getColumn());
                        }
                    }
                    if (tcl.getColumn() == 2) {
                        double valorAnteriorHoras =  Double.parseDouble((String) tcl.getOldValue());
                        double valorNuevoHoras = Double.parseDouble((String) tcl.getNewValue());
                        if (valorAnteriorHoras != valorNuevoHoras) {
                            gr.setHoras(valorNuevoHoras);
                            modelo.setValueAt(df.format(gr.getHoras()).replace(',', '.'), tcl.getRow(), tcl.getColumn());
                        }
                    }
                    if (tcl.getColumn() == 3) {
                        double valorAnteriorHorasDescSemanal =  Double.parseDouble((String) tcl.getOldValue());
                        double valorNuevoHorasDescSemanal = Double.parseDouble((String) tcl.getNewValue());
                        if (valorAnteriorHorasDescSemanal != valorNuevoHorasDescSemanal) {
                            gr.setHorasDeDescansoSemanal(valorNuevoHorasDescSemanal);
                            modelo.setValueAt(df.format(gr.getHorasDeDescansoSemanal()).replace(',', '.'), tcl.getRow(), tcl.getColumn());
                        }
                    }
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(IngresoGasto.this, "Los importes deben ser numericos.", "Información", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                    SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                    JOptionPane.showMessageDialog(IngresoGasto.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
                //System.out.println("Old   : " + tcl.getOldValue());
                //System.out.println("New   : " + tcl.getNewValue());
            }
        };

        TableCellListener tcl = new TableCellListener(jTableRenglones, action);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelTitulo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxReparto = new javax.swing.JComboBox();
        jButtonAgregar = new javax.swing.JButton();
        jLabelDescripcionArt = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldRubroGasto = new javax.swing.JTextField();
        jTextFieldImporteGasto = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButtonGuardar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableRenglones = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jDateChooserFecha = new com.toedter.calendar.JDateChooser();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jButtonRefrescar = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gasto");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Gasto");

        jLabel2.setText("Reparto:");

        jComboBoxReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxRepartoKeyPressed(evt);
            }
        });

        jButtonAgregar.setText("Agregar");
        jButtonAgregar.setEnabled(false);
        jButtonAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarActionPerformed(evt);
            }
        });
        jButtonAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonAgregarKeyPressed(evt);
            }
        });

        jLabelDescripcionArt.setText("Rubro");

        jLabel4.setText("Rubro:");

        jLabel5.setText("Total:");

        jTextFieldRubroGasto.setEnabled(false);
        jTextFieldRubroGasto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldRubroGastoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldRubroGastoFocusLost(evt);
            }
        });
        jTextFieldRubroGasto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldRubroGastoKeyPressed(evt);
            }
        });

        jTextFieldImporteGasto.setEnabled(false);
        jTextFieldImporteGasto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldImporteGastoKeyPressed(evt);
            }
        });

        jSeparator2.setBackground(new java.awt.Color(0, 0, 255));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 255));

        jSeparator3.setBackground(new java.awt.Color(0, 0, 255));
        jSeparator3.setForeground(new java.awt.Color(0, 0, 255));

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel6.setText("Total: ");

        jLabelTotal.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTotal.setText("0.0");

        jButtonGuardar.setText(" Guardar (F10)");
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

        jTableRenglones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Rubro", "Importe", "Horas", "Horas Desc Semanal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableRenglones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableRenglonesKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTableRenglones);

        jLabel1.setText("Fecha:");

        jDateChooserFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserFecha.setMaxSelectableDate(new java.util.Date(4102455675000L));
        jDateChooserFecha.setMinSelectableDate(new java.util.Date(946699272000L));

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jButtonRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/liquidaciones/refresh.png"))); // NOI18N
        jButtonRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefrescarActionPerformed(evt);
            }
        });

        jButtonEliminar.setText("Eliminar");
        jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2)
                            .addComponent(jSeparator3)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(226, 226, 226)
                                .addComponent(jLabel12))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldRubroGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jLabelDescripcionArt)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldImporteGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAgregar)
                        .addGap(11, 11, 11))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jButtonRefrescar)
                            .addGap(19, 19, 19)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelFechaIncorrecta)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel1)
                                                .addComponent(jLabel2))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jComboBoxReparto, 0, 205, Short.MAX_VALUE)
                                                .addComponent(jDateChooserFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(153, 153, 153)
                                            .addComponent(jLabelTitulo)))
                                    .addGap(114, 114, 114)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButtonGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButtonEliminar))))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(429, 429, 429)
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabelTotal))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 45, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonRefrescar)
                    .addComponent(jLabelTitulo))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonGuardar)
                            .addComponent(jLabel1)
                            .addComponent(jButtonEliminar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(4, 4, 4)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelFechaIncorrecta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel12))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextFieldRubroGasto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldImporteGasto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonAgregar)
                            .addComponent(jLabelDescripcionArt)
                            .addComponent(jLabel5))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabelTotal))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed
        // TODO add your handling code here:
        if ("".equals(jTextFieldRubroGasto.getText().trim()) || "".equals(jTextFieldImporteGasto.getText().trim())) {

        } else {
            if ("".equals(jTextFieldImporteGasto.getText())) {
            }
            try {
                GastoRenglon gr = new GastoRenglon();
                gr.setGasto(gasto);
                int codigo = Integer.parseInt(jTextFieldRubroGasto.getText());
                RubroGasto rg = sisMantenimiento.devolverRubroGastoPorCodigo(codigo);
                gr.setRubro(rg);
                gr.setTotal(Double.parseDouble(jTextFieldImporteGasto.getText().trim()));
                gr.setHoras(0.0);
                gr.setHorasDeDescansoSemanal(0.0);
                if (rg != null) {
                    gasto.getRenglones().add(gr);
                    //Cargo la tabla de la interfaz
                    Object[] object = new Object[4];
                    object[0] = gr.getRubro().getNombre();
                    object[1] = df.format(gr.getTotal()).replace(',', '.');
                    object[2] = "0.00";
                    object[3] = "0.00";
                    modelo.addRow(object);
                    //Borro los campos de codigo y cantidad y hago foco en codigo para el nuevo ingreso
                    jTextFieldRubroGasto.setText("");
                    jTextFieldImporteGasto.setText("");
                    jTextFieldRubroGasto.requestFocus();
                    jTextFieldRubroGasto.selectAll();
                    jLabelDescripcionArt.setText("");
                    //Sumo en el total
                    gasto.setTotal(gasto.getTotal() + gr.getTotal());
                    //Actualizo los valores que se muestran en las etiquetas inferiores
                    jLabelTotal.setText(df.format(gasto.getTotal()).replace(',', '.'));
                } else {
                    JOptionPane.showMessageDialog(this, "No existe el rubro ingresado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    jTextFieldRubroGasto.requestFocus();
                    jTextFieldRubroGasto.selectAll();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El rubro y el importe deben ser numeros.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldRubroGasto.requestFocus();
                jTextFieldRubroGasto.selectAll();
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonAgregarActionPerformed

    private void jButtonAgregarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonAgregarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonAgregar.doClick();
        }
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            jTextFieldImporteGasto.requestFocus();
            jTextFieldImporteGasto.selectAll();
        }
    }//GEN-LAST:event_jButtonAgregarKeyPressed

    private void jTextFieldRubroGastoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldRubroGastoFocusGained
        // TODO add your handling code here:
        //Veo si es una compra nueva
        if (jComboBoxReparto.getSelectedIndex() != 0 && gasto == null) {
            try {
                jDateChooserFecha.setEnabled(false);
                jComboBoxReparto.setEnabled(false);
                //Calendar fechaSeleccionada = dateChooserComboFecha.getSelectedDate();
                //SimpleDateFormat formatter;
                //formatter = new SimpleDateFormat("yyyy-MM-dd");
                //String f = formatter.format(fechaSeleccionada.getTime());
                Date fechaGasto = jDateChooserFecha.getDate();
                //Aca me fijo si ya hay un inventario con esa fecha para ese reparto. this.setInventario(new Inventario());
                gasto = sis.devolverGastoParaFechaYReparto(fechaGasto, (Reparto) jComboBoxReparto.getSelectedItem());
                if (gasto == null) {
                    //Es uno nuevo
                    this.setGasto(new Gasto());
                    this.gasto.setFecha(fechaGasto);
                    this.gasto.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                    esNuevo = true;
                } else {
                    //Es uno que ya existe
                    esNuevo = false;
                    //this.inventario.setFecha(fechaInventario);
                    //this.inventario.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                    //Cargo los datos en la interfaz
                    cargarRenglonesGasto();
                    jLabelTotal.setText(df.format(gasto.getTotal()).replace(',', '.'));
                    jButtonEliminar.setVisible(true);
                }
            } catch (ParseException ex) {
                Logger.getLogger(IngresarCompras.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        //Si no es una compra nueva es una compra que se quiere modificar.

    }//GEN-LAST:event_jTextFieldRubroGastoFocusGained

    private void jTextFieldRubroGastoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldRubroGastoFocusLost
        // TODO add your handling code here:
        if ("".equals(jTextFieldRubroGasto.getText().trim())) {

        } else {
            try {
                int codigo = Integer.parseInt(jTextFieldRubroGasto.getText());
                RubroGasto rg = sisMantenimiento.devolverRubroGastoPorCodigo(codigo);
                if (rg != null) {
                    jLabelDescripcionArt.setText(rg.getNombre());
                    jLabel12.setText("");
                } else {
                    jLabel12.setText("El codigo de rubro no es valido.");
                    jLabelDescripcionArt.setText("");
                    jTextFieldRubroGasto.requestFocus();
                    jTextFieldRubroGasto.selectAll();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "El codigo debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldRubroGasto.requestFocus();
                jTextFieldRubroGasto.selectAll();
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTextFieldRubroGastoFocusLost

    private void jTextFieldRubroGastoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldRubroGastoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("".equals(jTextFieldRubroGasto.getText().trim())) {
                VentanaBuscadorRubroGasto vbrg = new VentanaBuscadorRubroGasto(this, true);
                vbrg.setVisible(true);
                if (vbrg.getRubroSeleccionado() != null) {
                    rubroGasto = vbrg.getRubroSeleccionado();
                    jTextFieldRubroGasto.setText(Integer.toString(rubroGasto));
                    jTextFieldImporteGasto.requestFocus();
                    jTextFieldImporteGasto.selectAll();
                } else {
                    jTextFieldRubroGasto.requestFocus();
                    jTextFieldRubroGasto.selectAll();
                }
                /*List<RubroGasto> rubros = sisMantenimiento.devolverRubrosGastos();
                String[] nombresRubros = new String[rubros.size()];
                for(int i = 0; i<rubros.size(); i++){
                    nombresRubros[i] = rubros.get(i).getNombre();
                }
                JList list = new JList(nombresRubros);
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                JOptionPane.showMessageDialog(this, list, "Selecione el Rubro", JOptionPane.PLAIN_MESSAGE);
                int seleccionado = list.getSelectedIndex();
                jTextFieldRubroGasto.setText(Integer.toString(rubros.get(seleccionado).getCodigo()));
                jTextFieldImporteGasto.requestFocus();
                jTextFieldImporteGasto.selectAll();*/
            } else {
                jTextFieldImporteGasto.requestFocus();
                jTextFieldImporteGasto.selectAll();
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F10) {
            jButtonGuardar.doClick();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }//GEN-LAST:event_jTextFieldRubroGastoKeyPressed

    private void jTextFieldImporteGastoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldImporteGastoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("".equals(jTextFieldImporteGasto.getText().trim())) {
                if (!"".equals(jTextFieldRubroGasto.getText())) {
                    int resp = JOptionPane.showConfirmDialog(this, "Seguro que quiere dejar el importe vacio para el rubro?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resp == JOptionPane.NO_OPTION) {
                        jTextFieldImporteGasto.requestFocus();
                        jTextFieldImporteGasto.selectAll();
                    } else {
                        jTextFieldImporteGasto.setText("0");
                        jButtonAgregar.requestFocus();
                    }
                }
            } else {
                jButtonAgregar.requestFocus();
            }
        }
    }//GEN-LAST:event_jTextFieldImporteGastoKeyPressed

    private void jComboBoxRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxRepartoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jComboBoxReparto.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un reparto.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jComboBoxReparto.requestFocus();
            } else {
                jTextFieldRubroGasto.setEnabled(true);
                jTextFieldImporteGasto.setEnabled(true);
                jButtonAgregar.setEnabled(true);
                jTableRenglones.setEnabled(true);
                jDateChooserFecha.setEnabled(false);
                jComboBoxReparto.setEnabled(false);
                jTextFieldRubroGasto.requestFocus();
                jTextFieldRubroGasto.selectAll();
            }
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        // TODO add your handling code here:
        try {
            if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadIngresarGasto)) {  
                if (!esNuevo) {
                //compra.setReparto((Reparto)jComboBoxReparto.getSelectedItem());
                //compra.setNumero(Long.parseLong(jTextFieldNumero.getText().trim()));
                //Calendar fechaSeleccionada = dateChooserComboFecha.getSelectedDate();
                //SimpleDateFormat formatter;
                //formatter = new SimpleDateFormat("yyyy-MM-dd");
                //String f = formatter.format(fechaSeleccionada.getTime());
                //Date fechaGasto = jDateChooserFecha.getDate();
                    if (sis.actualizarGasto(gasto)) {
                        this.dispose();
                    }
                //this.compra.setFecha(fechaCompra);
                //sisCompras.actualizarCompra(compra);
                    this.dispose();
                } else if (sis.guardarGasto(gasto)) {
                    //reiniciarVentana();
                    //this.dispose();
                    IngresoGasto vinv = new IngresoGasto(null, false);
                    vinv.setVisible(true);
                    this.dispose();
                } else {

                }
            } else {
                JOptionPane.showMessageDialog(IngresoGasto.this, "No tiene permisos para guardar el gasto de un reparto.", "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                
            JOptionPane.showMessageDialog(this, "Error al guardar el gasto. " + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            IngresoGasto vinv = new IngresoGasto(null, false);
            vinv.setVisible(true);
            this.dispose();

        }
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    public void reiniciarVentana() {
        jDateChooserFecha.setEnabled(true);
        jComboBoxReparto.setEnabled(true);
        jDateChooserFecha.setDate(fechaDeHoy);
        jComboBoxReparto.setSelectedIndex(0);
        jTextFieldRubroGasto.setText("");
        jTextFieldRubroGasto.setEnabled(false);
        jTextFieldImporteGasto.setText("");
        jTextFieldImporteGasto.setEnabled(false);
        jButtonAgregar.setEnabled(false);
        jLabelTotal.setText("0");
        inicializarTablaRenglones();
        gasto = null;
        rubroGasto = 0;
        jDateChooserFecha.requestFocusInWindow();
    }

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        inicializarTablaRenglones();
        if (gasto != null) {
            Calendar fechaGasto = Calendar.getInstance();
            fechaGasto.setTime(gasto.getFecha());
            jDateChooserFecha.setDate(gasto.getFecha());
            jComboBoxReparto.setSelectedItem(gasto.getReparto());
            cargarRenglonesGasto();
            jLabelTotal.setText(df.format(gasto.getTotal()).replace(',', '.'));
        } else {
            jDateChooserFecha.setDate(fechaDeHoy);
            jDateChooserFecha.requestFocusInWindow();
        }
    }//GEN-LAST:event_formWindowActivated

    private void jButtonRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefrescarActionPerformed
        // TODO add your handling code here:
        IngresoGasto vg = new IngresoGasto(null, false);
        vg.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButtonRefrescarActionPerformed

    private void jTableRenglonesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableRenglonesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F10) {
            jButtonGuardar.doClick();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }//GEN-LAST:event_jTableRenglonesKeyPressed

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
        // TODO add your handling code here:
        if (gasto != null) {
            try {
                if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadIngresarGasto)) {
                    int resp = JOptionPane.showConfirmDialog(this, "Seguro que desea eliminar el gasto? ", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resp == JOptionPane.YES_OPTION) {
                        sis.eliminarGasto(gasto);
                        JOptionPane.showMessageDialog(this, "Gasto eliminado correctamente. ", "Información", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(IngresoGasto.this, "No tiene permisos para eliminar un gasto.", "Permisos", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                String stakTrace = util.Util.obtenerStackTraceEnString(e);
                SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);

                JOptionPane.showMessageDialog(this, "Error al eliminar el gasto. " + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                IngresoInventario vinv = new IngresoInventario(null, false);
                vinv.setVisible(true);
                this.dispose();

            }
        }
    }//GEN-LAST:event_jButtonEliminarActionPerformed

    public void cargarRenglonesGasto() {
        for (GastoRenglon gr : this.gasto.getRenglones()) {
            Object[] object = new Object[4];
            object[0] = gr.getRubro().getNombre();
            object[1] = df.format(gr.getTotal()).replace(',', '.');
            object[2] = df.format(gr.getHoras()).replace(',', '.');
            object[3] = df.format(gr.getHorasDeDescansoSemanal()).replace(',', '.');
            modelo.addRow(object);
        }
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
            java.util.logging.Logger.getLogger(IngresoGasto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresoGasto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresoGasto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresoGasto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IngresoGasto dialog = new IngresoGasto(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonAgregar;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonRefrescar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JComboBox jComboBoxReparto;
    private com.toedter.calendar.JDateChooser jDateChooserFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelDescripcionArt;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTableRenglones;
    private javax.swing.JTextField jTextFieldImporteGasto;
    private javax.swing.JTextField jTextFieldRubroGasto;
    // End of variables declaration//GEN-END:variables

    /**
     * @param gasto the gasto to set
     */
    public void setGasto(Gasto gasto) {
        this.gasto = gasto;
    }

    /**
     * @return the rubroGasto
     */
    public Integer getRubroGasto() {
        return rubroGasto;
    }

    /**
     * @param rubroGasto the rubroGasto to set
     */
    public void setRubroGasto(Integer rubroGasto) {
        this.rubroGasto = rubroGasto;
    }

    /**
     * @param fechaDeHoy the fechaDeHoy to set
     */
    public void setFechaDeHoy(Date fechaDeHoy) {
        this.fechaDeHoy = fechaDeHoy;
    }
}
