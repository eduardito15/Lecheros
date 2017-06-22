/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.liquidaciones;

import dominio.JornalRenglon;
import dominio.Jornales;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import sistema.SistemaLiquidaciones;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class IngresoJornales extends javax.swing.JFrame {

    private DefaultTableModel modelo;
    private final SistemaLiquidaciones sis;
    private final SistemaMantenimiento sisMantenimiento;
    private final DecimalFormat df;
    private Jornales jornales;

    private Integer rubroGasto;

    private Date fechaDeHoy;
    private boolean esNuevo = false;
    private boolean mostrarMensajeFechaIncorrecta = true;
    /**
     * Creates new form IngresoJornales
     */
    public IngresoJornales(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        inicializarTablaRenglones();
        sis = SistemaLiquidaciones.getInstance();
        sisMantenimiento = SistemaMantenimiento.getInstance();
        df = new DecimalFormat("0.00");
        agregarEnterCampoFecha();
        jDateChooserFecha.requestFocusInWindow();
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
                    jTextFieldRubroGasto.setEnabled(true);
                    jTextFieldImporteJornal.setEnabled(true);
                    jTextFieldCantJornales.setEnabled(true);
                    jTextFielHorasExtras.setEnabled(true);
                    jTextFieldHorasDescSemanal.setEnabled(true);
                    jButtonAgregar.setEnabled(true);
                    jTableRenglones.setEnabled(true);
                    jDateChooserFecha.setEnabled(false);
                    jTextFieldRubroGasto.requestFocus();
                    jTextFieldRubroGasto.selectAll();
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
                        jTextFieldRubroGasto.requestFocus();
                        jTextFieldRubroGasto.selectAll();
                        jButtonAgregar.setEnabled(true);jTextFieldRubroGasto.setEnabled(true);
                        jTextFieldImporteJornal.setEnabled(true);
                        jTextFieldCantJornales.setEnabled(true);
                        jTextFielHorasExtras.setEnabled(true);
                        jTextFieldHorasDescSemanal.setEnabled(true);
                        jButtonAgregar.setEnabled(true);
                        jTableRenglones.setEnabled(true);
                        jDateChooserFecha.setEnabled(false);
                        jTextFieldRubroGasto.requestFocus();
                        jTextFieldRubroGasto.selectAll();
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
                
                    return false;
                
            }
        };
        modelo.addColumn("Nombre");
        modelo.addColumn("Importe");
        modelo.addColumn("Cant. Jornales");
        modelo.addColumn("H. Extra");
        modelo.addColumn("Horas Desc Semanal");
        jTableRenglones.setModel(modelo);

        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Eliminar");
        deleteItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JornalRenglon jr = jornales.getRenglones().get(jTableRenglones.getSelectedRow());
                int resp = JOptionPane.showConfirmDialog(null, "Seguro que desea eliminar el rubro " + jr.getRubro().getNombre() + " ?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    modelo.removeRow(jTableRenglones.getSelectedRow());
                    jornales.getRenglones().remove(jr);
                    //Actualizo el total
                    jornales.setTotal(jornales.getTotal() - jr.getTotal());
                    jLabelTotal.setText(df.format(jornales.getTotal()).replace(',', '.'));
                }
            }

        });
        popupMenu.add(deleteItem);
        jTableRenglones.setComponentPopupMenu(popupMenu);
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
        jLabel1 = new javax.swing.JLabel();
        jDateChooserFecha = new com.toedter.calendar.JDateChooser();
        jButtonGuardar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldRubroGasto = new javax.swing.JTextField();
        jTextFieldImporteJornal = new javax.swing.JTextField();
        jButtonAgregar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldCantJornales = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFielHorasExtras = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldHorasDescSemanal = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableRenglones = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabelDescripcionArt = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Jornales (Contabilidad)");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Jornales");

        jLabel1.setText("Fecha:");

        jDateChooserFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserFecha.setMaxSelectableDate(new java.util.Date(4102455675000L));
        jDateChooserFecha.setMinSelectableDate(new java.util.Date(946699272000L));

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

        jSeparator2.setBackground(new java.awt.Color(0, 0, 255));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 255));

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

        jTextFieldImporteJornal.setEnabled(false);
        jTextFieldImporteJornal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldImporteJornalKeyPressed(evt);
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

        jLabel2.setText("Cant. Jornales");

        jTextFieldCantJornales.setText("1");
        jTextFieldCantJornales.setEnabled(false);
        jTextFieldCantJornales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCantJornalesKeyPressed(evt);
            }
        });

        jLabel3.setText("H. Extras:");

        jTextFielHorasExtras.setText("0");
        jTextFielHorasExtras.setEnabled(false);
        jTextFielHorasExtras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFielHorasExtrasKeyPressed(evt);
            }
        });

        jLabel6.setText("Horas Descanso Semanal:");

        jTextFieldHorasDescSemanal.setText("0");
        jTextFieldHorasDescSemanal.setEnabled(false);
        jTextFieldHorasDescSemanal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldHorasDescSemanalKeyPressed(evt);
            }
        });

        jSeparator3.setBackground(new java.awt.Color(0, 0, 255));
        jSeparator3.setForeground(new java.awt.Color(0, 0, 255));

        jTableRenglones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nombre", "Importe", "Cant. Jornales", "H. Extras", "Horas Desc Semanal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableRenglones);

        jLabel7.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel7.setText("Total: ");

        jLabelTotal.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTotal.setText("0.0");

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabelDescripcionArt.setText("Rubro");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(256, 256, 256)
                        .addComponent(jLabelTitulo))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(87, 87, 87))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelFechaIncorrecta)
                                .addGap(44, 44, 44)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldRubroGasto, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                            .addComponent(jTextFielHorasExtras))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelDescripcionArt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addGap(140, 140, 140)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldImporteJornal, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldHorasDescSemanal, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldCantJornales, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButtonAgregar))))
                .addContainerGap(80, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelTotal)
                .addGap(99, 99, 99))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3)
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonSalir))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelFechaIncorrecta)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldRubroGasto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldImporteJornal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDescripcionArt)
                    .addComponent(jLabel5)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldCantJornales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFielHorasExtras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldHorasDescSemanal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAgregar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabelTotal))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        // TODO add your handling code here:
        try {
            if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadIngresarGasto)) {
                if (!esNuevo) {
                    if (sis.actualizarJornales(jornales)) {
                        this.dispose();
                    }
                    //this.compra.setFecha(fechaCompra);
                    //sisCompras.actualizarCompra(compra);
                    this.dispose();
                } else if (sis.guardarJornales(jornales)) {
                    //reiniciarVentana();
                    //this.dispose();
                    //IngresoJornales vij = new IngresoJornales(null, false);
                    //vij.setVisible(true);
                    this.dispose();
                } else {

                }
            } else {
                JOptionPane.showMessageDialog(IngresoJornales.this, "No tiene permisos para guardar jornales.", "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);

            JOptionPane.showMessageDialog(this, "Error al guardar los jornales. " + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            IngresoJornales vij = new IngresoJornales(null, false);
            vij.setVisible(true);
            this.dispose();

        }
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jTextFieldRubroGastoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldRubroGastoFocusGained
        // TODO add your handling code here:
        //Veo si es una compra nueva
        if (jornales == null) {
            try {
                jDateChooserFecha.setEnabled(false);
                
                Date fechaJornales = jDateChooserFecha.getDate();
                //Aca me fijo si ya hay un inventario con esa fecha para ese reparto. this.setInventario(new Inventario());
                jornales = sis.devolverJornalesParaFecha(fechaJornales);
                if (jornales == null) {
                    //Es uno nuevo
                    this.setJornales(new Jornales());
                    this.jornales.setFecha(fechaJornales);
                    esNuevo = true;
                } else {
                    //Es uno que ya existe
                    esNuevo = false;
                    //Cargo los datos en la interfaz
                    cargarRenglonesJornales();
                    jLabelTotal.setText(df.format(jornales.getTotal()).replace(',', '.'));
                }
            }  catch (Exception ex) {
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
                    jTextFieldImporteJornal.requestFocus();
                    jTextFieldImporteJornal.selectAll();
                } else {
                    jTextFieldRubroGasto.requestFocus();
                    jTextFieldRubroGasto.selectAll();
                }
            } else {
                jTextFieldImporteJornal.requestFocus();
                jTextFieldImporteJornal.selectAll();
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F10) {
            jButtonGuardar.doClick();
        }
    }//GEN-LAST:event_jTextFieldRubroGastoKeyPressed

    private void jTextFieldImporteJornalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldImporteJornalKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("".equals(jTextFieldImporteJornal.getText().trim())) {
                if (!"".equals(jTextFieldRubroGasto.getText())) {
                    int resp = JOptionPane.showConfirmDialog(this, "Seguro que quiere dejar el importe vacio para el rubro?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resp == JOptionPane.NO_OPTION) {
                        jTextFieldImporteJornal.requestFocus();
                        jTextFieldImporteJornal.selectAll();
                    } else {
                        jTextFieldImporteJornal.setText("0");
                        jTextFieldCantJornales.requestFocus();
                        jTextFieldCantJornales.selectAll();
                    }
                }
            } else {
                jTextFieldCantJornales.requestFocus();
                jTextFieldCantJornales.selectAll();
            }
        }
    }//GEN-LAST:event_jTextFieldImporteJornalKeyPressed

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed
        // TODO add your handling code here:
        if ("".equals(jTextFieldRubroGasto.getText().trim()) || "".equals(jTextFieldImporteJornal.getText().trim())) {

        } else {
            if ("".equals(jTextFieldImporteJornal.getText())) {
            }
            try {
                JornalRenglon jr = new JornalRenglon();
                jr.setJornales(jornales);
                int codigo = Integer.parseInt(jTextFieldRubroGasto.getText());
                RubroGasto rg = sisMantenimiento.devolverRubroGastoPorCodigo(codigo);
                jr.setRubro(rg);
                jr.setTotal(Double.parseDouble(jTextFieldImporteJornal.getText().trim()));
                jr.setCantidadDeJornales(Integer.parseInt(jTextFieldCantJornales.getText().trim()));
                jr.setHoras(Integer.parseInt(jTextFielHorasExtras.getText().trim()));
                jr.setHorasDeDescansoSemanal(Integer.parseInt(jTextFieldHorasDescSemanal.getText().trim()));
                if (rg != null) {
                    jornales.getRenglones().add(jr);
                    //Cargo la tabla de la interfaz
                    Object[] object = new Object[5];
                    object[0] = jr.getRubro().getNombre();
                    object[1] = df.format(jr.getTotal()).replace(',', '.');
                    object[2] = jr.getCantidadDeJornales();
                    object[3] = jr.getHoras();
                    object[4] = jr.getHorasDeDescansoSemanal();
                    modelo.addRow(object);
                    //Borro los campos de codigo y cantidad y hago foco en codigo para el nuevo ingreso
                    jTextFieldRubroGasto.setText("");
                    jTextFieldImporteJornal.setText("");
                    jTextFieldCantJornales.setText("1");
                    jTextFielHorasExtras.setText("0");
                    jTextFieldHorasDescSemanal.setText("0");
                    jTextFieldRubroGasto.requestFocus();
                    jTextFieldRubroGasto.selectAll();
                    jLabelDescripcionArt.setText("");
                    //Sumo en el total
                    jornales.setTotal(jornales.getTotal() + jr.getTotal());
                    //Actualizo los valores que se muestran en las etiquetas inferiores
                    jLabelTotal.setText(df.format(jornales.getTotal()).replace(',', '.'));
                } else {
                    JOptionPane.showMessageDialog(this, "No existe el rubro ingresado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    jTextFieldRubroGasto.requestFocus();
                    jTextFieldRubroGasto.selectAll();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El rubro, el importe deben ser numeros.", "Información", JOptionPane.INFORMATION_MESSAGE);
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
            jTextFieldImporteJornal.requestFocus();
            jTextFieldImporteJornal.selectAll();
        }
    }//GEN-LAST:event_jButtonAgregarKeyPressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        inicializarTablaRenglones();
        if (jornales != null) {
            Calendar fechaJornales = Calendar.getInstance();
            fechaJornales.setTime(jornales.getFecha());
            jDateChooserFecha.setDate(jornales.getFecha());
            cargarRenglonesJornales();
            jLabelTotal.setText(df.format(jornales.getTotal()).replace(',', '.'));
        } else {
            jDateChooserFecha.setDate(fechaDeHoy);
            jDateChooserFecha.requestFocusInWindow();
        }
    }//GEN-LAST:event_formWindowActivated

    private void jTextFieldCantJornalesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCantJornalesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextFielHorasExtras.requestFocus();
            jTextFielHorasExtras.selectAll();
        }
    }//GEN-LAST:event_jTextFieldCantJornalesKeyPressed

    private void jTextFielHorasExtrasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFielHorasExtrasKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextFieldHorasDescSemanal.requestFocus();
            jTextFieldHorasDescSemanal.selectAll();
        }
    }//GEN-LAST:event_jTextFielHorasExtrasKeyPressed

    private void jTextFieldHorasDescSemanalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldHorasDescSemanalKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonAgregar.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldHorasDescSemanalKeyPressed

    public void cargarRenglonesJornales() {
        for (JornalRenglon jr : this.jornales.getRenglones()) {
            Object[] object = new Object[5];
            object[0] = jr.getRubro().getNombre();
            object[1] = df.format(jr.getTotal()).replace(',', '.');
            object[2] = jr.getCantidadDeJornales();
            object[3] = df.format(jr.getHoras()).replace(',', '.');
            object[4] = df.format(jr.getHorasDeDescansoSemanal()).replace(',', '.');
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
            java.util.logging.Logger.getLogger(IngresoJornales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresoJornales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresoJornales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresoJornales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IngresoJornales dialog = new IngresoJornales(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonSalir;
    private com.toedter.calendar.JDateChooser jDateChooserFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelDescripcionArt;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTableRenglones;
    private javax.swing.JTextField jTextFielHorasExtras;
    private javax.swing.JTextField jTextFieldCantJornales;
    private javax.swing.JTextField jTextFieldHorasDescSemanal;
    private javax.swing.JTextField jTextFieldImporteJornal;
    private javax.swing.JTextField jTextFieldRubroGasto;
    // End of variables declaration//GEN-END:variables
    
    /**
     * @param jornales the gasto to set
     */
    public void setJornales(Jornales jornales) {
        this.jornales = jornales;
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
