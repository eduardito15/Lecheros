/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.liquidaciones;

import ui.clientes.VentanaBuscadorClientePorReparto;
import dominio.Cliente;
import dominio.FiadoChofer;
import dominio.FiadoChoferRenglon;
import dominio.Reparto;
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
import java.text.SimpleDateFormat;
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
import javax.swing.table.TableColumn;
import sistema.SistemaLiquidaciones;
import sistema.SistemaLiquidacionesClafer;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.compras.IngresarCompras;
import ui.TableCellListener;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class IngresoFiadoChofer extends javax.swing.JFrame {

    private DefaultTableModel modelo;
    private final SistemaLiquidaciones sis;
    private final SistemaMantenimiento sisMantenimiento;
    private final DecimalFormat df;
    private FiadoChofer fiadoChofer;
    
    private Cliente cliente;
    
    private Date fechaDeHoy;
    private boolean esNuevo = false;
    private boolean mostrarMensajeFechaIncorrecta = true;
    /**
     * Creates new form IngresoInventario
     */
    public IngresoFiadoChofer(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        jLabel12.setForeground(new java.awt.Color(255, 0, 51));
        inicializarTablaRenglones();
        sis = SistemaLiquidaciones.getInstance();
        sisMantenimiento = SistemaMantenimiento.getInstance();
        jComboBoxReparto.addItem("");
        List<Reparto> repartos = sisMantenimiento.devolverRepartos();
        for (Reparto c : repartos) {
            jComboBoxReparto.addItem(c);
        }
        agregarEnterCampoFecha();
        df = new DecimalFormat("0.00");
        jDateChooserFecha.requestFocusInWindow();
    }
    
    public final void agregarEnterCampoFecha(){
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
                        if(f == null){
                            jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");
                        } 
                        else {
                            if(f.after(jDateChooserFecha.getMaxSelectableDate()) || f.before(jDateChooserFecha.getMinSelectableDate())){
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
    
    public final void inicializarTablaRenglones() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column == 2 || column == 3){
                    return true;
                }
                return false;
            }
        };
        modelo.addColumn("Cliente");
        modelo.addColumn("Fecha");
        modelo.addColumn("Fiado");
        modelo.addColumn("Envases");
        jTableRenglones.setModel(modelo);
        
        TableColumn column = null;
        for (int i = 0; i < 4; i++) {
            column = jTableRenglones.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(200); //articulo column is bigger
            } else {
                column.setPreferredWidth(50);
            }
        }
        
        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Eliminar");
        deleteItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FiadoChoferRenglon fcr = fiadoChofer.getRenglones().get(jTableRenglones.getSelectedRow());
                int resp = JOptionPane.showConfirmDialog(null, "Seguro que desea eliminar el fiado del cliente " + fcr.getCliente().getNombre() + " ?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    modelo.removeRow(jTableRenglones.getSelectedRow());
                    fiadoChofer.getRenglones().remove(fcr);
                    //Actualizo el total
                    fiadoChofer.setTotal(fiadoChofer.getTotal() - fcr.getTotal());
                    jLabelTotal.setText(df.format(fiadoChofer.getTotal()).replace(',', '.'));
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
                FiadoChoferRenglon fcr = fiadoChofer.getRenglones().get(tcl.getRow());
                try{
                    if(tcl.getColumn() == 2){
                        double valorAnterior = Double.parseDouble((String) tcl.getOldValue());
                        double valorNuevo = Double.parseDouble((String) tcl.getNewValue());
                        if (valorAnterior != valorNuevo && fcr.getTotal() != valorNuevo) {
                            fiadoChofer.setTotal(fiadoChofer.getTotal() - valorAnterior + valorNuevo);
                            SistemaLiquidacionesClafer sisLiqClafer = SistemaLiquidacionesClafer.getInstance();
                            sisLiqClafer.recalcularFiafoChofer(fiadoChofer);
                            jLabelTotal.setText(df.format(fiadoChofer.getTotal()).replace(',', '.'));
                            fcr.setTotal(valorNuevo);
                            modelo.setValueAt(df.format(fcr.getTotal()).replace(',', '.'), tcl.getRow(), tcl.getColumn());
                            jTextFieldCliente.requestFocus();
                        }
                    }
                    if (tcl.getColumn() == 3) {
                        String[] stringValorAnterior = ((String)tcl.getOldValue()).split(" ");
                        String[] stringValorNuevo = ((String)tcl.getNewValue()).split(" ");
                        int valorAnteriorEnvases = Integer.parseInt(stringValorAnterior[0]);
                        int valorNuevoEnvases = Integer.parseInt(stringValorNuevo[0]);
                        if (valorAnteriorEnvases != valorNuevoEnvases) {
                            //Como son envases tengo que multiplicar por el precio de los envases.
                            if(fiadoChofer.getRenglones().get(tcl.getRow()).getEnvases() != valorNuevoEnvases) {
                                double totalAnteriorEnvases = sis.devolverTotalEnPlataEnvases(valorAnteriorEnvases);
                                double totalNuevoEnvases = sis.devolverTotalEnPlataEnvases(valorNuevoEnvases);
                                fiadoChofer.setTotal(fiadoChofer.getTotal() - totalAnteriorEnvases + totalNuevoEnvases);
                                SistemaLiquidacionesClafer sisLiqClafer = SistemaLiquidacionesClafer.getInstance();
                                sisLiqClafer.recalcularFiafoChofer(fiadoChofer);
                                jLabelTotal.setText(df.format(fiadoChofer.getTotal()).replace(',', '.'));
                                fcr.setEnvases(valorNuevoEnvases);
                                modelo.setValueAt(Integer.toString(fcr.getEnvases()) + "  $ " + totalNuevoEnvases, tcl.getRow(), tcl.getColumn());
                                jTextFieldCliente.requestFocus();
                            }
                        }
                    }
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(IngresoFiadoChofer.this, "Los importes deben ser numericos.","Información", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                    SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                    JOptionPane.showMessageDialog(IngresoFiadoChofer.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
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
        jTextFieldCliente = new javax.swing.JTextField();
        jTextFieldImporteFiado = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButtonGuardar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableRenglones = new javax.swing.JTable();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jDateChooserFecha = new com.toedter.calendar.JDateChooser();
        jButtonRefrescar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fiado que Cobra el Chofer");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Fiado que Cobra el Chofer");

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

        jLabelDescripcionArt.setText("Cliente");

        jLabel4.setText("Cliente:");

        jLabel5.setText("Fiado:");

        jTextFieldCliente.setEnabled(false);
        jTextFieldCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldClienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldClienteFocusLost(evt);
            }
        });
        jTextFieldCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldClienteKeyPressed(evt);
            }
        });

        jTextFieldImporteFiado.setEnabled(false);
        jTextFieldImporteFiado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldImporteFiadoKeyPressed(evt);
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
                "Cliente", "Fecha", "Fiado", "Envases"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableRenglones);

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 51, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabel1.setText("Fecha:");

        jDateChooserFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserFecha.setMaxSelectableDate(new java.util.Date(4102455699000L));
        jDateChooserFecha.setMinSelectableDate(new java.util.Date(946699292000L));

        jButtonRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/liquidaciones/refresh.png"))); // NOI18N
        jButtonRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefrescarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(226, 226, 226)
                                .addComponent(jLabel12))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jLabelDescripcionArt)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldImporteFiado, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAgregar)
                        .addGap(11, 11, 11))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(69, 69, 69)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(58, 58, 58)
                                                    .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel1)
                                                        .addComponent(jLabel2))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGap(57, 57, 57)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jButtonGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jLabelFechaIncorrecta)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGap(355, 355, 355)
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabelTotal)
                                    .addGap(30, 30, 30)))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButtonRefrescar)
                                .addGap(91, 91, 91)
                                .addComponent(jLabelTitulo)))
                        .addGap(0, 273, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator2)
                            .addComponent(jSeparator3))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitulo)
                    .addComponent(jButtonRefrescar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonGuardar)
                        .addComponent(jLabel1))
                    .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonSalir, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelFechaIncorrecta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextFieldCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldImporteFiado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addContainerGap(75, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed
        // TODO add your handling code here:
        if ("".equals(jTextFieldCliente.getText().trim()) || "".equals(jTextFieldImporteFiado.getText().trim())) {

        } else {
            if ("".equals(jTextFieldImporteFiado.getText())) {
            }
            try {
                FiadoChoferRenglon fcr = new FiadoChoferRenglon();
                fcr.setFiadoChofer(fiadoChofer);
                Cliente cli = sisMantenimiento.devolverClientePorCodigo(fiadoChofer.getReparto().getCodigo() + "." + jTextFieldCliente.getText());
                fcr.setCliente(cli);
                fcr.setTotal(Double.parseDouble(jTextFieldImporteFiado.getText().trim()));
                fcr.setEnvases(0);
                if (cli != null) {
                    fiadoChofer.getRenglones().add(fcr);
                    //Cargo la tabla de la interfaz
                    Object[] object = new Object[4];
                    object[0] = fcr.getCliente().getNombre();
                    SimpleDateFormat formatter;
                    formatter = new SimpleDateFormat("dd-MM-yyyy");
                    object[1] = formatter.format(fechaDeHoy);
                    object[2] = df.format(fcr.getTotal()).replace(',', '.');
                    object[3] = Integer.toString(fcr.getEnvases());
                    modelo.addRow(object);
                    //Borro los campos de codigo y cantidad y hago foco en codigo para el nuevo ingreso
                    jTextFieldCliente.setText("");
                    jTextFieldImporteFiado.setText("");
                    jTextFieldCliente.requestFocus();
                    jTextFieldCliente.selectAll();
                    jLabelDescripcionArt.setText("");
                    //Sumo en el total
                    fiadoChofer.setTotal(fiadoChofer.getTotal() + fcr.getTotal());
                    //Actualizo los valores que se muestran en las etiquetas inferiores
                    jLabelTotal.setText(df.format(fiadoChofer.getTotal()).replace(',', '.'));
                } else {
                    JOptionPane.showMessageDialog(this, "No existe el cliente ingresado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    jTextFieldCliente.requestFocus();
                    jTextFieldCliente.selectAll();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El importe debe ser un numero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldCliente.requestFocus();
                jTextFieldCliente.selectAll();
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
    }//GEN-LAST:event_jButtonAgregarKeyPressed

    private void jTextFieldClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldClienteFocusGained
        // TODO add your handling code here:
        //Veo si es una compra nueva
        if (jComboBoxReparto.getSelectedIndex() != 0 && fiadoChofer == null) {
            try {
                jDateChooserFecha.setEnabled(false);
                jComboBoxReparto.setEnabled(false);
                //Calendar fechaSeleccionada = dateChooserComboFecha.getSelectedDate();
                //SimpleDateFormat formatter;
                //formatter = new SimpleDateFormat("yyyy-MM-dd");
                //String f = formatter.format(fechaSeleccionada.getTime());
                Date fechaFiadoChofer = jDateChooserFecha.getDate();
                //Aca me fijo si ya hay un inventario con esa fecha para ese reparto. this.setInventario(new Inventario());
                fiadoChofer = sis.devolverFiadoChoferParaFechaYReparto(fechaFiadoChofer, (Reparto) jComboBoxReparto.getSelectedItem());
                if(fiadoChofer == null){
                    //Es uno nuevo
                    this.setFiadoChofer(new FiadoChofer());
                    this.fiadoChofer.setFecha(fechaFiadoChofer);
                    this.fiadoChofer.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                    sis.cargarRenglonesNuevoFiadoChofer(fiadoChofer);
                    cargarRenglonesFiadoChofer();
                    jLabelTotal.setText(df.format(fiadoChofer.getTotal()).replace(',', '.'));
                    esNuevo = true;
                } else {
                    //Es uno que ya existe
                    esNuevo = false;
                    //this.inventario.setFecha(fechaInventario);
                    //this.inventario.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                    //Cargo los datos en la interfaz
                    cargarRenglonesFiadoChofer();
                    jLabelTotal.setText(df.format(fiadoChofer.getTotal()).replace(',', '.'));
                }  
            } catch (ParseException ex) {
                Logger.getLogger(IngresarCompras.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception exp) {
                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        //Si no es una compra nueva es una compra que se quiere modificar.

    }//GEN-LAST:event_jTextFieldClienteFocusGained

    private void jTextFieldClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldClienteFocusLost
        // TODO add your handling code here:
        if ("".equals(jTextFieldCliente.getText().trim())) {

        } else {
            try {
                Cliente cli = sisMantenimiento.devolverClienteActivoPorCodigo(fiadoChofer.getReparto().getCodigo() + "." + jTextFieldCliente.getText());
                if (cli != null) {
                    jLabelDescripcionArt.setText(cli.getNombre());
                    jLabel12.setText("");
                } else {
                    jLabel12.setText("El codigo de cliente no es valido.");
                    jLabelDescripcionArt.setText("");
                    jTextFieldCliente.requestFocus();
                    jTextFieldCliente.selectAll();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "El codigo debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldCliente.requestFocus();
                jTextFieldCliente.selectAll();
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTextFieldClienteFocusLost

    private void jTextFieldClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldClienteKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if("".equals(jTextFieldCliente.getText().trim())){
                VentanaBuscadorClientePorReparto vbc = new VentanaBuscadorClientePorReparto(this, true);
                vbc.setReparto(fiadoChofer.getReparto());
                vbc.setVisible(true);
                cliente = vbc.getCliente();
                String cod = cliente.getCodigo();
                String[] separado = cod.split("\\.");
                jTextFieldCliente.setText(separado[1]);
                jTextFieldImporteFiado.requestFocus();
                jTextFieldImporteFiado.selectAll();
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
                jTextFieldImporteFiado.requestFocus();
                jTextFieldImporteFiado.selectAll();
            }
        }
        if(evt.getKeyCode() == KeyEvent.VK_F10){
            jButtonGuardar.doClick();
        }
    }//GEN-LAST:event_jTextFieldClienteKeyPressed

    private void jTextFieldImporteFiadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldImporteFiadoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("".equals(jTextFieldImporteFiado.getText().trim())) {
                if (!"".equals(jTextFieldCliente.getText())) {
                    int resp = JOptionPane.showConfirmDialog(this, "Seguro que quiere dejar el importe vacio para el cliente?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resp == JOptionPane.NO_OPTION) {
                        jTextFieldImporteFiado.requestFocus();
                        jTextFieldImporteFiado.selectAll();
                    } else {
                        jTextFieldImporteFiado.setText("0");
                        jButtonAgregar.requestFocus();
                    }
                }
            } else {
                jButtonAgregar.requestFocus();
            }
        }
    }//GEN-LAST:event_jTextFieldImporteFiadoKeyPressed

    private void jComboBoxRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxRepartoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jComboBoxReparto.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un reparto.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jComboBoxReparto.requestFocus();
            } else {
                jTextFieldCliente.setEnabled(true);
                jTextFieldImporteFiado.setEnabled(true);
                jButtonAgregar.setEnabled(true);
                jTableRenglones.setEnabled(true);
                jDateChooserFecha.setEnabled(false);
                jComboBoxReparto.setEnabled(false);
                jTextFieldCliente.requestFocus();
                jTextFieldCliente.selectAll();
            }
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        // TODO add your handling code here:
        try {
            if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadIngresarFiadoChofer)) { 
                if(!esNuevo){
                //compra.setReparto((Reparto)jComboBoxReparto.getSelectedItem());
                //compra.setNumero(Long.parseLong(jTextFieldNumero.getText().trim()));
               // Calendar fechaSeleccionada = dateChooserComboFecha.getSelectedDate();
                //SimpleDateFormat formatter;
                //formatter = new SimpleDateFormat("yyyy-MM-dd");
                //String f = formatter.format(fechaSeleccionada.getTime());
                    if (sis.actualizarFiadoChofer(fiadoChofer)) {
                        this.dispose();
                    }
                //this.compra.setFecha(fechaCompra);
                //sisCompras.actualizarCompra(compra);
                    this.dispose();
                } else {
                    if (sis.guardarFiadoChofer(fiadoChofer)) {
                        this.dispose();
                    } else {

                    }
                }
            } else {
                JOptionPane.showMessageDialog(IngresoFiadoChofer.this, "No tiene permisos para guardar el fiado del chofer.", "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                
            JOptionPane.showMessageDialog(this, "Error al guardar el fiado que cobra el chofer. " + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            IngresoFiadoChofer vinv = new IngresoFiadoChofer(null, false);
            vinv.setVisible(true);
            this.dispose();

        }
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        inicializarTablaRenglones();
        if(fiadoChofer!=null){
            //Calendar fechaFiadoChofer = Calendar.getInstance();
            //fechaFiadoChofer.setTime(fiadoChofer.getFecha());
            jDateChooserFecha.setDate(fiadoChofer.getFecha());
            jComboBoxReparto.setSelectedItem(fiadoChofer.getReparto());
            cargarRenglonesFiadoChofer();
            jLabelTotal.setText(df.format(fiadoChofer.getTotal()).replace(',', '.'));
        } else {
            jDateChooserFecha.setDate(fechaDeHoy);
        }
    }//GEN-LAST:event_formWindowActivated

    private void jButtonRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefrescarActionPerformed
        // TODO add your handling code here:
        IngresoFiadoChofer vifch = new IngresoFiadoChofer(null, false);
        vifch.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButtonRefrescarActionPerformed

    public void cargarRenglonesFiadoChofer() {
        for(FiadoChoferRenglon fcr : this.fiadoChofer.getRenglones()){
            Object[] object = new Object[4];
            object[0] = fcr.getCliente().getNombre();
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat("dd-MM-yyyy");
            object[1] = formatter.format(fcr.getFechaRenglon());
            object[2] = df.format(fcr.getTotal()).replace(',', '.');
            double totalEnvases = 0;
            try {
                totalEnvases = sis.devolverTotalEnPlataEnvases(fcr.getEnvases());
            } catch (Exception ex) {
                Logger.getLogger(IngresoFiadoChofer.class.getName()).log(Level.SEVERE, null, ex);
            }
            object[3] = Integer.toString(fcr.getEnvases()) + ((totalEnvases == 0) ? "" : "  $ " + df.format(totalEnvases));
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
            java.util.logging.Logger.getLogger(IngresoFiadoChofer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresoFiadoChofer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresoFiadoChofer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresoFiadoChofer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IngresoFiadoChofer dialog = new IngresoFiadoChofer(new javax.swing.JFrame(), true);
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
    private javax.swing.JTextField jTextFieldCliente;
    private javax.swing.JTextField jTextFieldImporteFiado;
    // End of variables declaration//GEN-END:variables

    /**
     * @param fiadoChofer the fiadochofer to set
     */
    public void setFiadoChofer(FiadoChofer fiadoChofer) {
        this.fiadoChofer = fiadoChofer;
    }

    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @param fechaDeHoy the fechaDeHoy to set
     */
    public void setFechaDeHoy(Date fechaDeHoy) {
        this.fechaDeHoy = fechaDeHoy;
    }
}
