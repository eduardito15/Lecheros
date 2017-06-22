/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.liquidaciones;

import dominio.Articulo;
import dominio.Inventario;
import dominio.InventarioRenglon;
import dominio.Precio;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import sistema.SistemaLiquidaciones;
import sistema.SistemaMantenimiento;
import sistema.SistemaMantenimientoArticulos;
import sistema.SistemaUsuarios;
import ui.compras.IngresarCompras;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class IngresoInventario extends javax.swing.JFrame {

    private DefaultTableModel modelo;
    private final SistemaLiquidaciones sis;
    private final SistemaMantenimiento sisMantenimiento;
    private final SistemaMantenimientoArticulos sisArticulos;
    private final DecimalFormat df;
    private Inventario inventario;

    private boolean esNuevo = false;

    private Date fechaDeHoy;
    private boolean modificoRenglon;
    private boolean eliminoRenglon;
    private boolean mostrarMensajeFechaIncorrecta = true;

    /**
     * Creates new form IngresoInventario
     */
    public IngresoInventario(java.awt.Frame parent, boolean modal) {
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
        modificoRenglon = false;
        eliminoRenglon = false;
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
                return false;
            }
        };
        modelo.addColumn("Codigo");
        modelo.addColumn("Artículo");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio");
        modelo.addColumn("SubTotal");
        modelo.addColumn("Total");
        jTableRenglones.setModel(modelo);
        TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = jTableRenglones.getColumnModel().getColumn(i);
            if (i == 1) {
                column.setPreferredWidth(300); //articulo column is bigger
            } else {
                column.setPreferredWidth(50);
            }
        }
        final JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem modItem = new JMenuItem("Modificar");
        modItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jTableRenglones.isEnabled()) {
                    InventarioRenglon ir = inventario.getRenglones().get(jTableRenglones.getSelectedRow());
                    inventario.setSubtotal(inventario.getSubtotal() - ir.getSubtotal());
                    inventario.setTotal(inventario.getTotal() - ir.getTotal());
                    if ("Basico".equals(ir.getArticulo().getIva().getNombre())) {
                        inventario.setTotalBasico(inventario.getTotalBasico() - ir.getIva());
                    }
                    if ("Minimo".equals(ir.getArticulo().getIva().getNombre())) {
                        inventario.setTotalBasico(inventario.getTotalMinimo() - ir.getIva());
                    }
                    jLabelSubTotal.setText(df.format(inventario.getSubtotal()).replace(',', '.'));
                    jLabelIvaMinimo.setText(df.format(inventario.getTotalMinimo()).replace(',', '.'));
                    jLabelIvaBasico.setText(df.format(inventario.getTotalBasico()).replace(',', '.'));
                    jLabelTotal.setText(df.format(inventario.getTotal()).replace(',', '.'));

                    VentanaModificarRenglonInventario vmir = new VentanaModificarRenglonInventario(IngresoInventario.this, true);
                    vmir.setInvRenglon(ir);
                    vmir.setVisible(true);

                    //Actualizo el objeto de la tabla
                    int fila = jTableRenglones.getSelectedRow();
                    modelo.setValueAt(ir.getArticulo().getCodigo(), fila, 0);
                    modelo.setValueAt(ir.getArticulo().getDescripcion(), fila, 1);
                    modelo.setValueAt(ir.getCantidad(), fila, 2);
                    modelo.setValueAt(df.format(ir.getPrecio()).replace(',', '.'), fila, 3);
                    modelo.setValueAt(df.format(ir.getSubtotal()).replace(',', '.'), fila, 4);
                    modelo.setValueAt(df.format(ir.getTotal()).replace(',', '.'), fila, 5);

                    inventario.setSubtotal(inventario.getSubtotal() + ir.getSubtotal());
                    inventario.setTotal(inventario.getTotal() + ir.getTotal());
                    if ("Basico".equals(ir.getArticulo().getIva().getNombre())) {
                        inventario.setTotalBasico(inventario.getTotalBasico() + ir.getIva());
                    }
                    if ("Minimo".equals(ir.getArticulo().getIva().getNombre())) {
                        inventario.setTotalBasico(inventario.getTotalMinimo() + ir.getIva());
                    }
                    jLabelSubTotal.setText(df.format(inventario.getSubtotal()).replace(',', '.'));
                    jLabelIvaMinimo.setText(df.format(inventario.getTotalMinimo()).replace(',', '.'));
                    jLabelIvaBasico.setText(df.format(inventario.getTotalBasico()).replace(',', '.'));
                    jLabelTotal.setText(df.format(inventario.getTotal()).replace(',', '.'));
                    modificoRenglon = true;
                } else {
                    JOptionPane.showMessageDialog(IngresoInventario.this, "Primero ingrese la fecha y el reparto.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    jDateChooserFecha.requestFocusInWindow();
                }
            } 
        });
        popupMenu.add(modItem);

        JMenuItem deleteItem = new JMenuItem("Eliminar");
        deleteItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jTableRenglones.isEnabled()) {
                    InventarioRenglon ir = inventario.getRenglones().get(jTableRenglones.getSelectedRow());
                    int resp = JOptionPane.showConfirmDialog(null, "Seguro que desea eliminar el artículo " + ir.getArticulo().getDescripcion() + " ?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resp == JOptionPane.YES_OPTION) {
                        modelo.removeRow(jTableRenglones.getSelectedRow());
                        inventario.getRenglones().remove(ir);
                        //Actualizo los totales y subtotales
                        inventario.setSubtotal(inventario.getSubtotal() - ir.getSubtotal());
                        inventario.setTotal(inventario.getTotal() - ir.getTotal());
                        if ("Basico".equals(ir.getArticulo().getIva().getNombre())) {
                            inventario.setTotalBasico(inventario.getTotalBasico() - ir.getIva());
                        }
                        if ("Minimo".equals(ir.getArticulo().getIva().getNombre())) {
                            inventario.setTotalBasico(inventario.getTotalMinimo() - ir.getIva());
                        }
                        jLabelSubTotal.setText(df.format(inventario.getSubtotal()).replace(',', '.'));
                        jLabelIvaMinimo.setText(df.format(inventario.getTotalMinimo()).replace(',', '.'));
                        jLabelIvaBasico.setText(df.format(inventario.getTotalBasico()).replace(',', '.'));
                        jLabelTotal.setText(df.format(inventario.getTotal()).replace(',', '.'));
                        //inicializarTablaRenglones();
                        //cargarRenglonesInventario();
                        eliminoRenglon = true;
                    } 
                }
                else {
                    JOptionPane.showMessageDialog(IngresoInventario.this, "Primero ingrese la fecha y el reparto.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    jDateChooserFecha.requestFocusInWindow();
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
        jLabel2 = new javax.swing.JLabel();
        jComboBoxReparto = new javax.swing.JComboBox();
        jButtonAgregar = new javax.swing.JButton();
        jLabelDescripcionArt = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldCodigoArticulo = new javax.swing.JTextField();
        jTextFieldCantidadRenglon = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabelSubTotal = new javax.swing.JLabel();
        jLabelIvaMinimo = new javax.swing.JLabel();
        jLabelIvaBasico = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableRenglones = new javax.swing.JTable();
        jButtonGuardar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jDateChooserFecha = new com.toedter.calendar.JDateChooser();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jButtonImprimir = new javax.swing.JButton();
        jButtonRefrescar2 = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Inventario");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Inventario");

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

        jLabelDescripcionArt.setText("Artículo");

        jLabel4.setText("Codigo:");

        jLabel5.setText("Cantidad:");

        jTextFieldCodigoArticulo.setEnabled(false);
        jTextFieldCodigoArticulo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldCodigoArticuloFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldCodigoArticuloFocusLost(evt);
            }
        });
        jTextFieldCodigoArticulo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCodigoArticuloKeyPressed(evt);
            }
        });

        jTextFieldCantidadRenglon.setEnabled(false);
        jTextFieldCantidadRenglon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCantidadRenglonKeyPressed(evt);
            }
        });

        jSeparator2.setBackground(new java.awt.Color(0, 0, 255));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 255));

        jSeparator3.setBackground(new java.awt.Color(0, 0, 255));
        jSeparator3.setForeground(new java.awt.Color(0, 0, 255));

        jLabel9.setText("Iva Minimo:");

        jLabel10.setText("Iva Basico:");

        jLabel8.setText("Sub Total:");

        jLabelSubTotal.setText("0.0");

        jLabelIvaMinimo.setText("0.0");

        jLabelIvaBasico.setText("0.0");

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel6.setText("Total: ");

        jLabelTotal.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTotal.setText("0.0");

        jTableRenglones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Artículo", "Cantidad", "Precio", "SubTotal", "Total"
            }
        ));
        jTableRenglones.setEnabled(false);
        jTableRenglones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableRenglonesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableRenglones);

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

        jLabel1.setText("Fecha:");

        jDateChooserFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserFecha.setMaxSelectableDate(new java.util.Date(4102455707000L));
        jDateChooserFecha.setMinSelectableDate(new java.util.Date(946699308000L));

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 51, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jButtonImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/printer_icon_small27772.jpg"))); // NOI18N
        jButtonImprimir.setText("Imprimir");
        jButtonImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImprimirActionPerformed(evt);
            }
        });

        jButtonRefrescar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/liquidaciones/refresh.png"))); // NOI18N
        jButtonRefrescar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefrescar2ActionPerformed(evt);
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(404, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelIvaMinimo)
                    .addComponent(jLabelIvaBasico)
                    .addComponent(jLabelSubTotal))
                .addGap(229, 229, 229)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelTotal)
                .addGap(179, 179, 179))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelFechaIncorrecta)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel1))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jComboBoxReparto, 0, 205, Short.MAX_VALUE)
                                            .addComponent(jDateChooserFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(114, 114, 114)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jButtonGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonImprimir)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonEliminar))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButtonRefrescar2)
                                .addGap(310, 310, 310)
                                .addComponent(jLabelTitulo)))
                        .addContainerGap(242, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldCodigoArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jLabelDescripcionArt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldCantidadRenglon, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonAgregar)))
                        .addGap(19, 19, 19))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelTitulo)
                    .addComponent(jButtonRefrescar2))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonGuardar)
                        .addComponent(jLabel1)
                        .addComponent(jButtonImprimir)
                        .addComponent(jButtonEliminar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSalir, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelFechaIncorrecta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldCodigoArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCantidadRenglon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAgregar)
                    .addComponent(jLabelDescripcionArt)
                    .addComponent(jLabel5)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabelSubTotal)
                    .addComponent(jLabel6)
                    .addComponent(jLabelTotal))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabelIvaMinimo))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabelIvaBasico))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed
        // TODO add your handling code here:
        if ("".equals(jTextFieldCodigoArticulo.getText().trim()) || "".equals(jTextFieldCantidadRenglon.getText().trim())) {

        } else {
            if ("".equals(jTextFieldCantidadRenglon.getText())) {
            }
            try {
                InventarioRenglon ir = new InventarioRenglon();
                ir.setInventario(inventario);
                int codigo = Integer.parseInt(jTextFieldCodigoArticulo.getText());
                Articulo a = sisArticulos.devolverArticuloPorCodigo(codigo);
                ir.setArticulo(a);
                ir.setCantidad(Double.parseDouble(jTextFieldCantidadRenglon.getText().trim()));
                Precio p = sisArticulos.devolverPrecioParaFechaPorArticulo(a, inventario.getFecha());
                if (p != null) {
                    //Cargo el objeto renglon de compra
                    ir.setPrecio(p.getPrecioVenta());
                    ir.setSubtotal(p.getPrecioVenta() * ir.getCantidad());
                    ir.setIva((ir.getSubtotal() * a.getIva().getPorcentaje() / 100));
                    ir.setTotal(ir.getSubtotal() + ir.getIva());
                    inventario.getRenglones().add(ir);
                    //Cargo la tabla de la interfaz
                    Object[] object = new Object[6];
                    object[0] = ir.getArticulo().getCodigo();
                    object[1] = ir.getArticulo().getDescripcion();
                    object[2] = ir.getCantidad();
                    object[3] = df.format(ir.getPrecio()).replace(',', '.');
                    object[4] = df.format(ir.getSubtotal()).replace(',', '.');
                    object[5] = df.format(ir.getTotal()).replace(',', '.');
                    modelo.addRow(object);
                    jTableRenglones.changeSelection(inventario.getRenglones().size() - 1, 0, false, false);
                    //Borro los campos de codigo y cantidad y hago foco en codigo para el nuevo ingreso
                    jTextFieldCodigoArticulo.setText("");
                    jTextFieldCantidadRenglon.setText("");
                    jTextFieldCodigoArticulo.requestFocus();
                    jTextFieldCodigoArticulo.selectAll();
                    jLabelDescripcionArt.setText("");
                    //Sumo en los subtotales de la compra los valores del renglon
                    inventario.setSubtotal(inventario.getSubtotal() + ir.getSubtotal());
                    inventario.setTotal(inventario.getTotal() + ir.getTotal());
                    if (a.getIva().getId() == 2) {
                        inventario.setTotalMinimo(inventario.getTotalMinimo() + ir.getIva());
                    }
                    if (a.getIva().getId() == 3) {
                        inventario.setTotalBasico(inventario.getTotalBasico() + ir.getIva());
                    }
                    //Actualizo los valores que se muestran en las etiquetas inferiores
                    jLabelSubTotal.setText(df.format(inventario.getSubtotal()).replace(',', '.'));
                    jLabelIvaMinimo.setText(df.format(inventario.getTotalMinimo()).replace(',', '.'));
                    jLabelIvaBasico.setText(df.format(inventario.getTotalBasico()).replace(',', '.'));
                    jLabelTotal.setText(df.format(inventario.getTotal()).replace(',', '.'));
                } else {
                    JOptionPane.showMessageDialog(this, "No existen precios para ese articulo.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    jTextFieldCodigoArticulo.requestFocus();
                    jTextFieldCodigoArticulo.selectAll();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El codigo y la cantidad deben ser numeros.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldCodigoArticulo.requestFocus();
                jTextFieldCodigoArticulo.selectAll();
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
            jTextFieldCantidadRenglon.requestFocus();
            jTextFieldCantidadRenglon.selectAll();
        }
    }//GEN-LAST:event_jButtonAgregarKeyPressed

    private void jTextFieldCodigoArticuloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldCodigoArticuloFocusGained
        // TODO add your handling code here:
        //Veo si es una compra nueva
        if (jComboBoxReparto.getSelectedIndex() != 0 && inventario == null) {
            try {
                jDateChooserFecha.setEnabled(false);
                jComboBoxReparto.setEnabled(false);
                //Calendar fechaSeleccionada = dateChooserComboFecha.getSelectedDate();
                //SimpleDateFormat formatter;
                //formatter = new SimpleDateFormat("yyyy-MM-dd");
                //String f = formatter.format(fechaSeleccionada.getTime());
                Date fechaInventario = jDateChooserFecha.getDate();
                //Aca me fijo si ya hay un inventario con esa fecha para ese reparto. this.setInventario(new Inventario());
                inventario = sis.devolverInventarioParaFechaYReparto(fechaInventario, (Reparto) jComboBoxReparto.getSelectedItem());
                if (inventario == null) {
                    //Es uno nuevo
                    this.setInventario(new Inventario());
                    this.inventario.setFecha(fechaInventario);
                    this.inventario.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                    esNuevo = true;
                } else {
                    //Es uno que ya existe
                    esNuevo = false;
                    //this.inventario.setFecha(fechaInventario);
                    //this.inventario.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                    //Cargo los datos en la interfaz
                    cargarRenglonesInventario();
                    jLabelSubTotal.setText(df.format(inventario.getSubtotal()).replace(',', '.'));
                    jLabelIvaMinimo.setText(df.format(inventario.getTotalMinimo()).replace(',', '.'));
                    jLabelIvaBasico.setText(df.format(inventario.getTotalBasico()).replace(',', '.'));
                    jLabelTotal.setText(df.format(inventario.getTotal()).replace(',', '.'));
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

    }//GEN-LAST:event_jTextFieldCodigoArticuloFocusGained

    private void jTextFieldCodigoArticuloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldCodigoArticuloFocusLost
        // TODO add your handling code here:
        /*if ("".equals(jTextFieldCodigoArticulo.getText().trim())) {
            jLabel12.setText("El codigo de producto no es valido.");
            jLabelDescripcionArt.setText("");
            jTextFieldCodigoArticulo.requestFocus();
            jTextFieldCodigoArticulo.selectAll();
        } else {
            try {
                int codigo = Integer.parseInt(jTextFieldCodigoArticulo.getText());
                Articulo a = sisArticulos.devolverArticuloPorCodigo(codigo);
                if (a != null) {
                    jLabelDescripcionArt.setText(a.getDescripcion());
                    jLabel12.setText("");
                } else {
                    jLabel12.setText("El codigo de producto no es valido.");
                    jLabelDescripcionArt.setText("");
                    jTextFieldCodigoArticulo.requestFocus();
                    jTextFieldCodigoArticulo.selectAll();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "El codigo debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldCodigoArticulo.requestFocus();
                jTextFieldCodigoArticulo.selectAll();
            }
        }*/
    }//GEN-LAST:event_jTextFieldCodigoArticuloFocusLost

    private void jTextFieldCodigoArticuloKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodigoArticuloKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //jTextFieldCantidadRenglon.requestFocus();
            //jTextFieldCantidadRenglon.selectAll();
            if ("".equals(jTextFieldCodigoArticulo.getText().trim())) {
                jLabel12.setText("El codigo de producto no es valido.");
                jLabelDescripcionArt.setText("");
                jTextFieldCodigoArticulo.requestFocus();
                jTextFieldCodigoArticulo.selectAll();
            } else {
                try {
                    int codigo = Integer.parseInt(jTextFieldCodigoArticulo.getText());
                    Articulo a = sisArticulos.devolverArticuloPorCodigo(codigo);
                    if (a != null) {
                        jLabelDescripcionArt.setText(a.getDescripcion());
                        jLabel12.setText("");
                        jTextFieldCantidadRenglon.requestFocus();
                        jTextFieldCantidadRenglon.selectAll();
                    } else {
                        jLabel12.setText("El codigo de producto no es valido.");
                        jLabelDescripcionArt.setText("");
                        jTextFieldCodigoArticulo.requestFocus();
                        jTextFieldCodigoArticulo.selectAll();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "El codigo debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    jTextFieldCodigoArticulo.requestFocus();
                    jTextFieldCodigoArticulo.selectAll();
                } catch (Exception ex) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                    SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                    JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F10) {
            jButtonGuardar.doClick();
        }
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            if (jTableRenglones.getSelectedRow() >= 1) {
                jTableRenglones.changeSelection(jTableRenglones.getSelectedRow() - 1, 0, false, false);
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            if (jTableRenglones.getSelectedRow() < jTableRenglones.getRowCount() - 1) {
                jTableRenglones.changeSelection(jTableRenglones.getSelectedRow() + 1, 0, false, false);
            }
        }
    }//GEN-LAST:event_jTextFieldCodigoArticuloKeyPressed

    private void jTextFieldCantidadRenglonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCantidadRenglonKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("".equals(jTextFieldCantidadRenglon.getText().trim())) {
                if (!"".equals(jTextFieldCodigoArticulo.getText())) {
                    int resp = JOptionPane.showConfirmDialog(this, "Seguro que quiere dejar la cantidad vacia para el artículo?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resp == JOptionPane.NO_OPTION) {
                        jTextFieldCantidadRenglon.requestFocus();
                        jTextFieldCantidadRenglon.selectAll();
                    } else {
                        jTextFieldCantidadRenglon.setText("0");
                        jButtonAgregar.requestFocus();
                    }
                }
            } else {
                jButtonAgregar.requestFocus();
            }
        }
    }//GEN-LAST:event_jTextFieldCantidadRenglonKeyPressed

    private void jComboBoxRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxRepartoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jComboBoxReparto.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un reparto.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jComboBoxReparto.requestFocus();
            } else {
                jTextFieldCodigoArticulo.setEnabled(true);
                jTextFieldCantidadRenglon.setEnabled(true);
                jButtonAgregar.setEnabled(true);
                jTableRenglones.setEnabled(true);
                jDateChooserFecha.setEnabled(false);
                jComboBoxReparto.setEnabled(false);
                jTableRenglones.setEnabled(true);
                jTextFieldCodigoArticulo.requestFocus();
                jTextFieldCodigoArticulo.selectAll();
            }
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        // TODO add your handling code here:
        try {
            if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadIngresarCompras)) { 
                if (!esNuevo) {
                //compra.setReparto((Reparto)jComboBoxReparto.getSelectedItem());
                //compra.setNumero(Long.parseLong(jTextFieldNumero.getText().trim()));
                //Calendar fechaSeleccionada = dateChooserComboFecha.getSelectedDate();
                //SimpleDateFormat formatter;
                //formatter = new SimpleDateFormat("yyyy-MM-dd");
                //String f = formatter.format(fechaSeleccionada.getTime());
                //Date fechaCompra = jDateChooserFecha.getDate();
                    if (sis.actualizarInventario(inventario)) {
                        this.dispose();
                    }
                //this.compra.setFecha(fechaCompra);
                //sisCompras.actualizarCompra(compra);
                    this.dispose();
                } else if (sis.guardarInventario(inventario)) {
                    this.dispose();
                } else {

                }
            } else {
                JOptionPane.showMessageDialog(IngresoInventario.this, "No tiene permisos para guardar un inventario.", "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                
            JOptionPane.showMessageDialog(this, "Error al guardar el inventario. " + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            IngresoInventario vinv = new IngresoInventario(null, false);
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
        if (inventario != null) {
            //Calendar fechaInv = Calendar.getInstance();
            //fechaInv.setTime(inventario.getFecha());
            jDateChooserFecha.setDate(inventario.getFecha());
            jComboBoxReparto.setSelectedItem(inventario.getReparto());
            cargarRenglonesInventario();
            jLabelTotal.setText(df.format(inventario.getTotal()).replace(',', '.'));
        } else {
            jDateChooserFecha.setDate(fechaDeHoy);
        }
        if (modificoRenglon) {
            jTextFieldCodigoArticulo.requestFocus();
            jTextFieldCodigoArticulo.selectAll();
            modificoRenglon = false;
        }
        if (eliminoRenglon) {
            jTextFieldCodigoArticulo.requestFocus();
            jTextFieldCodigoArticulo.selectAll();
            eliminoRenglon = false;
        }

    }//GEN-LAST:event_formWindowActivated

    private void jButtonImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImprimirActionPerformed
        // TODO add your handling code here:
        if (inventario == null) {
            JOptionPane.showMessageDialog(this, "Para imprimir primero debe estar en un inventario", "Información", JOptionPane.INFORMATION_MESSAGE);
            jComboBoxReparto.requestFocus();
        } else {
            try {
                SistemaLiquidaciones.getImpresionInventario().imprimirInventario(inventario);
                //jTableRenglones.print();
                /*PrintJob pj;
                Graphics pagina;
                pj = Toolkit.getDefaultToolkit().getPrintJob(IngresoInventario.this, "Inventario", null);
                try {
                
                pagina = pj.getGraphics();

                pagina.setFont(new Font("Fuente", 0, 10));

                pagina.setColor(Color.black);

                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("dd-MM-yyyy");

                pagina.drawString("Inventario", 200, 60);
                pagina.drawString("Reparto: " + inventario.getReparto().getNombre(), 200, 72);
                pagina.drawString("Fecha: " + formatter.format(inventario.getFecha()), 200, 84);

                pagina.drawString("Codigo", 45, 96);
                pagina.drawString("Articulo", 110, 96);
                pagina.drawString("Cantidad", 410, 96);
                pagina.drawString("Total", 450, 96);

                int fila = 108;
                int renglones = 0;
                for (InventarioRenglon ir : inventario.getRenglones()) {
                renglones = renglones + 1;
                
                if((renglones%60)==0){
                pagina.dispose();
                pj.end();
                pj = Toolkit.getDefaultToolkit().getPrintJob(IngresoInventario.this, "Inventario", null);
                pagina = pj.getGraphics();
                pagina.setFont(new Font("Fuente", 0, 10));
                pagina.setColor(Color.black);
                
                pagina.drawString("Codigo", 45, 60);
                pagina.drawString("Articulo", 110, 60);
                pagina.drawString("Cantidad", 410, 60);
                pagina.drawString("Total", 450, 60);
                
                fila = 72;
                }
                
                pagina.drawString(Integer.toString(ir.getArticulo().getCodigo()), 45, fila);
                pagina.drawString(ir.getArticulo().getDescripcion(), 110, fila);
                pagina.drawString(Double.toString(ir.getCantidad()), 410, fila);
                pagina.drawString(df.format(ir.getTotal()), 450, fila);
                fila = fila + 12;
                }
                
                pagina.drawString("Total", 410, fila);
                pagina.drawString(df.format(inventario.getTotal()), 450, fila);

                pagina.dispose();
                pj.end();
                
                } catch (Exception e) {
                
                System.out.println("LA IMPRESION HA SIDO CANCELADA...");
                
                }*/
            } catch (Exception ex) {
                System.out.println("LA IMPRESION HA SIDO CANCELADA...");
            }
        }
    }//GEN-LAST:event_jButtonImprimirActionPerformed

    private void jTableRenglonesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRenglonesMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            if (jTableRenglones.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un renglon.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                InventarioRenglon ir = inventario.getRenglones().get(jTableRenglones.getSelectedRow());
                inventario.setSubtotal(inventario.getSubtotal() - ir.getSubtotal());
                inventario.setTotal(inventario.getTotal() - ir.getTotal());
                if ("Basico".equals(ir.getArticulo().getIva().getNombre())) {
                    inventario.setTotalBasico(inventario.getTotalBasico() - ir.getIva());
                }
                if ("Minimo".equals(ir.getArticulo().getIva().getNombre())) {
                    inventario.setTotalBasico(inventario.getTotalMinimo() - ir.getIva());
                }
                jLabelSubTotal.setText(df.format(inventario.getSubtotal()).replace(',', '.'));
                jLabelIvaMinimo.setText(df.format(inventario.getTotalMinimo()).replace(',', '.'));
                jLabelIvaBasico.setText(df.format(inventario.getTotalBasico()).replace(',', '.'));
                jLabelTotal.setText(df.format(inventario.getTotal()).replace(',', '.'));

                VentanaModificarRenglonInventario vmir = new VentanaModificarRenglonInventario(IngresoInventario.this, true);
                vmir.setInvRenglon(ir);
                vmir.setVisible(true);

                //Actualizo el objeto de la tabla
                int fila = jTableRenglones.getSelectedRow();
                modelo.setValueAt(ir.getArticulo().getCodigo(), fila, 0);
                modelo.setValueAt(ir.getArticulo().getDescripcion(), fila, 1);
                modelo.setValueAt(ir.getCantidad(), fila, 2);
                modelo.setValueAt(df.format(ir.getPrecio()).replace(',', '.'), fila, 3);
                modelo.setValueAt(df.format(ir.getSubtotal()).replace(',', '.'), fila, 4);
                modelo.setValueAt(df.format(ir.getTotal()).replace(',', '.'), fila, 5);

                inventario.setSubtotal(inventario.getSubtotal() + ir.getSubtotal());
                inventario.setTotal(inventario.getTotal() + ir.getTotal());
                if ("Basico".equals(ir.getArticulo().getIva().getNombre())) {
                    inventario.setTotalBasico(inventario.getTotalBasico() + ir.getIva());
                }
                if ("Minimo".equals(ir.getArticulo().getIva().getNombre())) {
                    inventario.setTotalBasico(inventario.getTotalMinimo() + ir.getIva());
                }
                jLabelSubTotal.setText(df.format(inventario.getSubtotal()).replace(',', '.'));
                jLabelIvaMinimo.setText(df.format(inventario.getTotalMinimo()).replace(',', '.'));
                jLabelIvaBasico.setText(df.format(inventario.getTotalBasico()).replace(',', '.'));
                jLabelTotal.setText(df.format(inventario.getTotal()).replace(',', '.'));
                modificoRenglon = true;
            }
        }
    }//GEN-LAST:event_jTableRenglonesMouseClicked

    private void jButtonRefrescar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefrescar2ActionPerformed
        // TODO add your handling code here:
        IngresoInventario vg = new IngresoInventario(null, false);
        vg.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButtonRefrescar2ActionPerformed

    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
        // TODO add your handling code here:
        if (inventario != null) {
            try {
                if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadIngresarCompras)) {
                    int resp = JOptionPane.showConfirmDialog(this, "Seguro que desea eliminar el inventario? ", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resp == JOptionPane.YES_OPTION) {
                        sis.eliminarInventario(inventario);
                        JOptionPane.showMessageDialog(this, "Inventario eliminado correctamente. ", "Información", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(IngresoInventario.this, "No tiene permisos para eliminar un inventario.", "Permisos", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                String stakTrace = util.Util.obtenerStackTraceEnString(e);
                SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);

                JOptionPane.showMessageDialog(this, "Error al eliminar el inventario. " + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                IngresoInventario vinv = new IngresoInventario(null, false);
                vinv.setVisible(true);
                this.dispose();

            }
        }
    }//GEN-LAST:event_jButtonEliminarActionPerformed

    public void cargarRenglonesInventario() {
        Collections.sort(this.inventario.getRenglones());
        for (InventarioRenglon ir : this.inventario.getRenglones()) {
            Object[] object = new Object[6];
            object[0] = ir.getArticulo().getCodigo();
            object[1] = ir.getArticulo().getDescripcion();
            object[2] = ir.getCantidad();
            object[3] = ir.getPrecio();
            object[4] = df.format(ir.getSubtotal()).replace(',', '.');
            object[5] = df.format(ir.getTotal()).replace(',', '.');
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
            java.util.logging.Logger.getLogger(IngresoInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresoInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresoInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresoInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IngresoInventario dialog = new IngresoInventario(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonImprimir;
    private javax.swing.JButton jButtonRefrescar2;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JComboBox jComboBoxReparto;
    private com.toedter.calendar.JDateChooser jDateChooserFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelDescripcionArt;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelIvaBasico;
    private javax.swing.JLabel jLabelIvaMinimo;
    private javax.swing.JLabel jLabelSubTotal;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTableRenglones;
    private javax.swing.JTextField jTextFieldCantidadRenglon;
    private javax.swing.JTextField jTextFieldCodigoArticulo;
    // End of variables declaration//GEN-END:variables

    /**
     * @param inventario the inventario to set
     */
    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    /**
     * @param fechaDeHoy the fechaDeHoy to set
     */
    public void setFechaDeHoy(Date fechaDeHoy) {
        this.fechaDeHoy = fechaDeHoy;
    }
}
