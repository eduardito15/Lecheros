/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.facturas;

import ui.clientes.VentanaBuscadorClienteFactura;
import dominio.Articulo;
import dominio.Cliente;
import dominio.DocumentoDeVenta;
import dominio.Factura;
import dominio.FacturaRenglon;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import sistema.SistemaFacturas;
import sistema.SistemaMantenimiento;
import sistema.SistemaMantenimientoArticulos;
import sistema.SistemaUsuarios;
import ui.clientes.VentanaBuscadorClientePorReparto;
import ui.mantenimiento.VentanaBuscadorArticulos;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class IngresoFacturas extends javax.swing.JFrame {

    private DocumentoDeVenta tipoDoc;
    private final SistemaFacturas sisFacturas;
    private final SistemaMantenimiento sisMantenimiento;
    private final SistemaMantenimientoArticulos sisArticulos;

    private Factura factura;
    private Cliente cliente;
    private long numeroDeFactura;

    private final DecimalFormat df;
    private DefaultTableModel modelo;
    private String accion;
    List<Reparto> repartos;

    private boolean modificoRenglon;
    private boolean eliminoRenglon;
    private boolean yaCargoElNumero;
    private boolean mostrarMensajeFechaIncorrecta;

    /**
     * Creates new form IngresoFacturasManuales
     */
    public IngresoFacturas(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        inicializarTablaRenglones();
        sisFacturas = SistemaFacturas.getInstance();
        sisMantenimiento = SistemaMantenimiento.getInstance();
        sisArticulos = SistemaMantenimientoArticulos.getInstance();
        mostrarMensajeFechaIncorrecta = true;
        jComboBoxReparto.addItem("");
        repartos = sisMantenimiento.devolverRepartos();
        for (Reparto c : repartos) {
            jComboBoxReparto.addItem(c);
        }

        df = new DecimalFormat("0.00");
        modificoRenglon = false;
        eliminoRenglon = false;
        agregarEnterCampoFecha();

        jDateChooserFecha.requestFocusInWindow();

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
                    jTextFieldNumero.requestFocus();
                    jTextFieldNumero.selectAll();
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
                        jTextFieldNumero.requestFocus();
                        jTextFieldNumero.selectAll();
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
                FacturaRenglon fmr = getFactura().getRenglones().get(jTableRenglones.getSelectedRow());
                getFactura().setSubtotal(getFactura().getSubtotal() - fmr.getSubtotal());
                getFactura().setTotal(getFactura().getTotal() - fmr.getTotal());
                if ("Basico".equals(fmr.getArticulo().getIva().getNombre())) {
                    getFactura().setTotalBasico(getFactura().getTotalBasico() - fmr.getIva());
                }
                if ("Minimo".equals(fmr.getArticulo().getIva().getNombre())) {
                    getFactura().setTotalBasico(getFactura().getTotalMinimo() - fmr.getIva());
                }
                jLabelSubTotal.setText(df.format(getFactura().getSubtotal()).replace(',', '.'));
                jLabelIvaMinimo.setText(df.format(getFactura().getTotalMinimo()).replace(',', '.'));
                jLabelIvaBasico.setText(df.format(getFactura().getTotalBasico()).replace(',', '.'));
                jLabelTotal.setText(df.format(getFactura().getTotal()).replace(',', '.'));

                VentanaModificarRenglonFactura vmfmr = new VentanaModificarRenglonFactura(IngresoFacturas.this, true);
                vmfmr.setFacturaRenglon(fmr);
                vmfmr.setVisible(true);
                //Actualizo el objeto de la tabla
                int fila = jTableRenglones.getSelectedRow();
                modelo.setValueAt(fmr.getArticulo().getCodigo(), fila, 0);
                modelo.setValueAt(fmr.getArticulo().getDescripcion(), fila, 1);
                modelo.setValueAt(fmr.getCantidad(), fila, 2);
                modelo.setValueAt(df.format(fmr.getPrecio()).replace(',', '.'), fila, 3);
                modelo.setValueAt(df.format(fmr.getSubtotal()).replace(',', '.'), fila, 4);
                modelo.setValueAt(df.format(fmr.getTotal()).replace(',', '.'), fila, 5);

                getFactura().setSubtotal(getFactura().getSubtotal() + fmr.getSubtotal());
                getFactura().setTotal(getFactura().getTotal() + fmr.getTotal());
                if ("Basico".equals(fmr.getArticulo().getIva().getNombre())) {
                    getFactura().setTotalBasico(getFactura().getTotalBasico() + fmr.getIva());
                }
                if ("Minimo".equals(fmr.getArticulo().getIva().getNombre())) {
                    getFactura().setTotalBasico(getFactura().getTotalMinimo() + fmr.getIva());
                }
                jLabelSubTotal.setText(df.format(getFactura().getSubtotal()).replace(',', '.'));
                jLabelIvaMinimo.setText(df.format(getFactura().getTotalMinimo()).replace(',', '.'));
                jLabelIvaBasico.setText(df.format(getFactura().getTotalBasico()).replace(',', '.'));
                jLabelTotal.setText(df.format(getFactura().getTotal()).replace(',', '.'));
                modificoRenglon = true;
            }

        });
        popupMenu.add(modItem);

        JMenuItem deleteItem = new JMenuItem("Eliminar");
        deleteItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FacturaRenglon fmr = getFactura().getRenglones().get(jTableRenglones.getSelectedRow());
                int resp = JOptionPane.showConfirmDialog(null, "Seguro que desea eliminar el artículo " + fmr.getArticulo().getDescripcion() + " ?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    modelo.removeRow(jTableRenglones.getSelectedRow());
                    getFactura().getRenglones().remove(fmr);
                    //Actualizo los totales y subtotales
                    getFactura().setSubtotal(getFactura().getSubtotal() - fmr.getSubtotal());
                    getFactura().setTotal(getFactura().getTotal() - fmr.getTotal());
                    if ("Basico".equals(fmr.getArticulo().getIva().getNombre())) {
                        getFactura().setTotalBasico(getFactura().getTotalBasico() - fmr.getIva());
                    }
                    if ("Minimo".equals(fmr.getArticulo().getIva().getNombre())) {
                        getFactura().setTotalBasico(getFactura().getTotalMinimo() - fmr.getIva());
                    }
                    jLabelSubTotal.setText(df.format(getFactura().getSubtotal()).replace(',', '.'));
                    jLabelIvaMinimo.setText(df.format(getFactura().getTotalMinimo()).replace(',', '.'));
                    jLabelIvaBasico.setText(df.format(getFactura().getTotalBasico()).replace(',', '.'));
                    jLabelTotal.setText(df.format(getFactura().getTotal()).replace(',', '.'));
                    //inicializarTablaRenglones();
                    //cargarRenglonesInventario();
                    eliminoRenglon = true;
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
        jButtonGuardar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabelTitulo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabelSubTotal = new javax.swing.JLabel();
        jLabelIvaMinimo = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableRenglones = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabelIvaBasico = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldCliente = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldNumero = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jDateChooserFecha = new com.toedter.calendar.JDateChooser();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jButtonRefrescar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabelDescuento = new javax.swing.JLabel();
        jButtonGuardarEImprimir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ingresar Factura Manual");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jComboBoxReparto.setEnabled(false);
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

        jButtonGuardar.setText(" Guardar (F10)");
        jButtonGuardar.setEnabled(false);
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

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Factura");

        jLabel2.setText("Reparto:");

        jLabel8.setText("Sub Total:");

        jLabelSubTotal.setText("0.0");

        jLabelIvaMinimo.setText("0.0");

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
        jTableRenglones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableRenglonesMouseClicked(evt);
            }
        });
        jTableRenglones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableRenglonesKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTableRenglones);

        jLabel10.setText("Iva Basico:");

        jLabelIvaBasico.setText("0.0");

        jLabel3.setText("Cliente:");

        jTextFieldCliente.setEnabled(false);
        jTextFieldCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldClienteKeyPressed(evt);
            }
        });

        jLabel12.setForeground(new java.awt.Color(255, 0, 0));

        jLabel7.setText("Numero:");

        jTextFieldNumero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNumeroKeyPressed(evt);
            }
        });

        jLabel1.setText("Fecha:");

        jDateChooserFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserFecha.setMaxSelectableDate(new java.util.Date(4102455710000L));
        jDateChooserFecha.setMinSelectableDate(new java.util.Date(946699295000L));

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jButtonRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/liquidaciones/refresh.png"))); // NOI18N
        jButtonRefrescar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonRefrescar.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jButtonRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefrescarActionPerformed(evt);
            }
        });

        jLabel11.setText("Descuento:");

        jLabelDescuento.setText("0.0");

        jButtonGuardarEImprimir.setText("Guardar e Imprimir (F11)");
        jButtonGuardarEImprimir.setEnabled(false);
        jButtonGuardarEImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarEImprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldCodigoArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelDescripcionArt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldCantidadRenglon, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAgregar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSeparator2)
                            .addComponent(jSeparator3)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 962, Short.MAX_VALUE))))
                .addGap(19, 19, 19))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(403, 403, 403)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelDescuento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 233, Short.MAX_VALUE)
                        .addComponent(jLabel9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelIvaMinimo)
                    .addComponent(jLabelIvaBasico)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelSubTotal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 227, Short.MAX_VALUE)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelTotal)
                .addGap(181, 181, 181))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonRefrescar)
                .addGap(333, 333, 333)
                .addComponent(jLabelTitulo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabelFechaIncorrecta)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldNumero, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                            .addComponent(jDateChooserFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldCliente)
                            .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonGuardarEImprimir)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitulo)
                    .addComponent(jButtonRefrescar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)
                                .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonGuardar)
                                .addComponent(jButtonGuardarEImprimir))
                            .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7)
                            .addComponent(jTextFieldNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonSalir)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelFechaIncorrecta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldCantidadRenglon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jButtonAgregar)
                    .addComponent(jLabelDescripcionArt)
                    .addComponent(jTextFieldCodigoArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabelSubTotal)
                    .addComponent(jLabel6)
                    .addComponent(jLabelTotal))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelIvaMinimo)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11)
                    .addComponent(jLabelDescuento))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabelIvaBasico))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxRepartoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jComboBoxReparto.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un reparto.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jComboBoxReparto.requestFocus();
            } else {
                jTextFieldCliente.setEnabled(true);
                jTextFieldCliente.setEditable(true);
                jTextFieldCliente.requestFocus();
                jTextFieldCliente.selectAll();
                /*jTextFieldCodigoArticulo.setEnabled(true);
                jTextFieldCantidadRenglon.setEnabled(true);
                jButtonAgregar.setEnabled(true);
                jTableRenglones.setEnabled(true);
                dateChooserComboFecha.setEnabled(false);
                jComboBoxReparto.setEnabled(false);
                jTextFieldCodigoArticulo.requestFocus();
                jTextFieldCodigoArticulo.selectAll();*/
            }
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed
        // TODO add your handling code here:
        if ("".equals(jTextFieldCodigoArticulo.getText().trim()) || "".equals(jTextFieldCantidadRenglon.getText().trim())) {

        } else {
            if ("".equals(jTextFieldCantidadRenglon.getText())) {
            }
            try {
                FacturaRenglon fmr = new FacturaRenglon();
                fmr.setFactura(getFactura());
                int codigo = Integer.parseInt(jTextFieldCodigoArticulo.getText());
                Articulo a = sisArticulos.devolverArticuloPorCodigo(codigo);
                fmr.setArticulo(a);
                fmr.setCantidad(Double.parseDouble(jTextFieldCantidadRenglon.getText().trim()));
                Precio p = sisArticulos.devolverPrecioParaFechaPorArticulo(a, getFactura().getFecha());
                if (p != null) {
                    //Cargo el objeto renglon de compra
                    fmr.setPrecio(p.getPrecioVenta());
                    fmr.setSubtotal(p.getPrecioVenta() * fmr.getCantidad());
                    fmr.setIva((fmr.getSubtotal() * a.getIva().getPorcentaje() / 100));
                    fmr.setTotal(fmr.getSubtotal() + fmr.getIva());
                    getFactura().getRenglones().add(fmr);
                    //Cargo la tabla de la interfaz
                    Object[] object = new Object[6];
                    object[0] = fmr.getArticulo().getCodigo();
                    object[1] = fmr.getArticulo().getDescripcion();
                    object[2] = fmr.getCantidad();
                    object[3] = df.format(fmr.getPrecio()).replace(',', '.');
                    object[4] = df.format(fmr.getSubtotal()).replace(',', '.');
                    object[5] = df.format(fmr.getTotal()).replace(',', '.');
                    modelo.addRow(object);
                    jTableRenglones.changeSelection(getFactura().getRenglones().size() - 1, 0, false, false);
                    //Borro los campos de codigo y cantidad y hago foco en codigo para el nuevo ingreso
                    jTextFieldCodigoArticulo.setText("");
                    jTextFieldCantidadRenglon.setText("");
                    jTextFieldCodigoArticulo.requestFocus();
                    jTextFieldCodigoArticulo.selectAll();
                    jLabelDescripcionArt.setText("");
                    //Sumo en los subtotales de la compra los valores del renglon
                    getFactura().setSubtotal(getFactura().getSubtotal() + fmr.getSubtotal());
                    getFactura().setTotal(getFactura().getTotal() + fmr.getTotal());
                    if (a.getIva().getId() == 2) {
                        getFactura().setTotalMinimo(getFactura().getTotalMinimo() + fmr.getIva());
                    }
                    if (a.getIva().getId() == 3) {
                        getFactura().setTotalBasico(getFactura().getTotalBasico() + fmr.getIva());
                    }
                    //Actualizo los valores que se muestran en las etiquetas inferiores
                    jLabelSubTotal.setText(df.format(getFactura().getSubtotal()).replace(',', '.'));
                    jLabelIvaMinimo.setText(df.format(getFactura().getTotalMinimo()).replace(',', '.'));
                    jLabelIvaBasico.setText(df.format(getFactura().getTotalBasico()).replace(',', '.'));
                    jLabelTotal.setText(df.format(getFactura().getTotal()).replace(',', '.'));
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
        if (!"".equals(jTextFieldNumero.getText().trim()) && jComboBoxReparto.getSelectedIndex() != 0 && cliente != null && getFactura() == null) {
            try {
                jTextFieldNumero.setEnabled(false);
                jDateChooserFecha.setEnabled(false);
                jComboBoxReparto.setEnabled(false);
                this.setFactura(new Factura());
                this.factura.setTipoDocumento(tipoDoc);
                this.factura.setNumero(Long.parseLong(jTextFieldNumero.getText().trim()));
                //Calendar fechaSeleccionada = dateChooserComboFecha.getSelectedDate();
                //SimpleDateFormat formatter;
                //formatter = new SimpleDateFormat("yyyy-MM-dd");
                //String f = formatter.format(fechaSeleccionada.getTime());
                Date fechaCompra = jDateChooserFecha.getDate();
                this.factura.setFecha(fechaCompra);
                this.factura.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                this.factura.setCliente(cliente);
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
                VentanaBuscadorArticulos vba = new VentanaBuscadorArticulos(this, true);
                vba.setVisible(true);
                if(vba.getArticuloSeleccionado() != null) {
                    jTextFieldCodigoArticulo.setText(Long.toString(vba.getArticuloSeleccionado().getCodigo()));
                }
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
        if (evt.getKeyCode() == KeyEvent.VK_F11) {
            jButtonGuardarEImprimir.doClick();
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

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        // TODO add your handling code here:
        try {
            //if (factura.isEsManual()) {
                if ("Modificar".equals(this.getAccion())) {
                    factura.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                    factura.setNumero(Long.parseLong(jTextFieldNumero.getText().trim()));
                    //Calendar fechaSeleccionada = dateChooserComboFecha.getSelectedDate();
                    //SimpleDateFormat formatter;
                    //formatter = new SimpleDateFormat("yyyy-MM-dd");
                    //String f = formatter.format(fechaSeleccionada.getTime());
                    Date fechaFact = jDateChooserFecha.getDate();
                    this.factura.setFecha(fechaFact);
                    sisFacturas.actualizarFacturaManual(factura);
                    this.dispose();
                } else if (sisFacturas.guardarFacturaManual(factura)) {
                    //Se guardo bien, habro de nuevo la misma ventana, dejo la fecha, el reparto y aumento 1 en el numero, borro el cliente.
                    long proximoNumero = this.factura.getNumero() + 1;
                    //this.factura = null;
                    //this.cliente = null;
                    //jTextFieldCliente.setText("");
                    //jTextFieldNumero.setText(Long.toString(proximoNumero));
                    //inicializarTablaRenglones();
                    //jLabelIvaBasico.setText("0");
                    //jLabelIvaMinimo.setText("0");
                    //jLabelSubTotal.setText("0");
                    //jLabelTotal.setText("0");
                    //jTextFieldNumero.setEnabled(true);
                    //inhabilitarCampos();
                    //habilitarCampos();
                    //jTextFieldNumero.requestFocus();
                    //jTextFieldNumero.selectAll();
                    IngresoFacturas vif = new IngresoFacturas(null, false);
                    vif.setAccion("NuevaDesdeFacturaAnterior");
                    vif.setTipoDoc(factura.getTipoDocumento());
                    vif.setNumeroDeFactura(proximoNumero);
                    vif.setVisible(true);
                    this.dispose();
                } else {

                }
            //}
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                
            JOptionPane.showMessageDialog(this, "Error al guardar la factura. " + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            IngresoFacturas vinv = new IngresoFacturas(null, false);
            vinv.setTipoDoc(factura.getTipoDocumento());
            vinv.setVisible(true);
            this.dispose();

        }
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jTextFieldNumeroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNumeroKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!"".equals(jTextFieldNumero.getText().trim())) {
                try {
                    if (factura == null) {
                        //Si la factura manual en null seguro que es una factura manual nueva, entonces si o si verifico que el numero sea valido
                        if (sisFacturas.numeroDeFacturaManualValido(Long.parseLong(jTextFieldNumero.getText().trim()), tipoDoc)) {
                            habilitarCampos();
                            jComboBoxReparto.requestFocus();
                        } else {
                            JOptionPane.showMessageDialog(this, "Ya existe una Factura Manual con el numero ingresado. Por favor ingrese un nuevo numero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                            jTextFieldNumero.requestFocus();
                            jTextFieldNumero.selectAll();
                        }
                    } else {//Si la compra no es null es por que es una mofificacion. Entonces solo verifico que sea valido si modifico el numero
                        Long numCampo = Long.parseLong(jTextFieldNumero.getText());
                        if (factura.getNumero() != numCampo) {
                            if (sisFacturas.numeroDeFacturaManualValido(Long.parseLong(jTextFieldNumero.getText().trim()), tipoDoc)) {
                                habilitarCampos();
                                jComboBoxReparto.requestFocus();
                            } else {
                                JOptionPane.showMessageDialog(this, "Ya existe una Factura Manual con el numero ingresado. Por favor ingrese un nuevo numero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                                jTextFieldNumero.requestFocus();
                                jTextFieldNumero.selectAll();
                            }
                        } else {
                            jComboBoxReparto.requestFocus();
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "El numero debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    jTextFieldNumero.requestFocus();
                    jTextFieldNumero.selectAll();
                } catch (Exception ex) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                    SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                    JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_jTextFieldNumeroKeyPressed

    private void jTextFieldClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldClienteKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("".equals(jTextFieldCliente.getText().trim())) {
                //Es vacio. Le habro el buscador
                VentanaBuscadorClientePorReparto vbc = new VentanaBuscadorClientePorReparto(IngresoFacturas.this, true);
                vbc.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                vbc.setVisible(true);
                if (vbc.getCliente() != null) {
                    this.cliente = vbc.getCliente();
                    jTextFieldCliente.setText(cliente.getNombre());
                    jTextFieldCodigoArticulo.setEnabled(true);
                    jTextFieldCantidadRenglon.setEnabled(true);
                    jButtonAgregar.setEnabled(true);
                    jTableRenglones.setEnabled(true);
                    jDateChooserFecha.setEnabled(false);
                    jComboBoxReparto.setEnabled(false);
                    jTextFieldCliente.setEditable(false);
                    jTextFieldCodigoArticulo.requestFocus();
                    jTextFieldCodigoArticulo.selectAll();
                } else {
                    jTextFieldCliente.requestFocus();
                    jTextFieldCliente.selectAll();
                }
            } else //No es vacio. Busco la cadena en los rut de cliente, si conincide la muestro si no pregunto si quiere volver a ingresarlo o abrir el buscador de clientes.
            {
                if (cliente != null) {
                    jTextFieldCliente.setEditable(false);
                    jTextFieldCodigoArticulo.setEnabled(true);
                    jTextFieldCantidadRenglon.setEnabled(true);
                    jButtonAgregar.setEnabled(true);
                    jTableRenglones.setEnabled(true);
                    jDateChooserFecha.setEnabled(false);
                    jComboBoxReparto.setEnabled(false);
                    jTextFieldCliente.setEditable(false);
                    jTextFieldCodigoArticulo.requestFocus();
                    jTextFieldCodigoArticulo.selectAll();
                    if (cliente.getNombre().equals(jTextFieldCliente.getText().trim())) {
                        jTextFieldCliente.setText(cliente.getNombre());
                        jTextFieldCodigoArticulo.setEnabled(true);
                        jTextFieldCantidadRenglon.setEnabled(true);
                        jButtonAgregar.setEnabled(true);
                        jTableRenglones.setEnabled(true);
                        jDateChooserFecha.setEnabled(false);
                        jComboBoxReparto.setEnabled(false);
                        jTextFieldCliente.setEditable(false);
                        jTextFieldCodigoArticulo.requestFocus();
                        jTextFieldCodigoArticulo.selectAll();
                    }
                } else {
                    String fraccionDelRut = jTextFieldCliente.getText().trim();
                    cliente = sisMantenimiento.devolverClientePorFraccionDelRut(fraccionDelRut);
                    if (cliente != null) {
                        jTextFieldCliente.setText(cliente.getNombre());
                        jTextFieldCodigoArticulo.setEnabled(true);
                        jTextFieldCantidadRenglon.setEnabled(true);
                        jButtonAgregar.setEnabled(true);
                        jTableRenglones.setEnabled(true);
                        jDateChooserFecha.setEnabled(false);
                        jComboBoxReparto.setEnabled(false);
                        jTextFieldCliente.setEditable(false);
                        jTextFieldCodigoArticulo.requestFocus();
                        jTextFieldCodigoArticulo.selectAll();
                    } else {
                        int resp = JOptionPane.showConfirmDialog(this, "No existe un cliente que contenga en el rut: " + fraccionDelRut + " . Desea abrir el buscador de clientes?  ", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (resp == JOptionPane.YES_OPTION) {
                            VentanaBuscadorClienteFactura vbc = new VentanaBuscadorClienteFactura(IngresoFacturas.this, true);
                            vbc.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                            vbc.setVisible(true);
                            if (vbc.getCliente() != null) {
                                this.cliente = vbc.getCliente();
                                jTextFieldCliente.setText(cliente.getNombre());
                                jTextFieldCodigoArticulo.setEnabled(true);
                                jTextFieldCantidadRenglon.setEnabled(true);
                                jButtonAgregar.setEnabled(true);
                                jTableRenglones.setEnabled(true);
                                jDateChooserFecha.setEnabled(false);
                                jComboBoxReparto.setEnabled(false);
                                jTextFieldCliente.setEditable(false);
                                jTextFieldCodigoArticulo.requestFocus();
                                jTextFieldCodigoArticulo.selectAll();
                            } else {
                                jTextFieldCliente.requestFocus();
                                jTextFieldCliente.selectAll();
                            }
                        } else if (resp == JOptionPane.NO_OPTION) {
                            jTextFieldCliente.requestFocus();
                            jTextFieldCliente.selectAll();
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_jTextFieldClienteKeyPressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        jLabelTitulo.setText(tipoDoc.getTipoDocumento());
        if ("Ver".equals(this.getAccion()) && factura != null && jTableRenglones.getRowCount() == 0) {
            inhabilitarCampos();
            //jTableRenglones.setEnabled(false);
            jTextFieldNumero.setEnabled(false);
            jTextFieldNumero.setText(Long.toString(factura.getNumero()));
            //Calendar fechaComp = Calendar.getInstance();
            //fechaComp.setTime(factura.getFecha());
            jDateChooserFecha.setEnabled(true);
            jDateChooserFecha.setDate(factura.getFecha());
            jDateChooserFecha.setEnabled(false);
            int indiceReparto = this.devolverIndiceReparto(factura.getReparto());
            jComboBoxReparto.setSelectedIndex(indiceReparto);
            jTextFieldCliente.setText(factura.getCliente().toString());
            for (FacturaRenglon fmr : factura.getRenglones()) {
                cargarRenglon(fmr);
            }
            //Cargo los totales
            jLabelDescuento.setText(df.format(factura.getDescuento()).replace(',', '.'));
            jLabelSubTotal.setText(df.format(factura.getSubtotal()).replace(',', '.'));
            jLabelIvaMinimo.setText(df.format(factura.getTotalMinimo()).replace(',', '.'));
            jLabelIvaBasico.setText(df.format(factura.getTotalBasico()).replace(',', '.'));
            jLabelTotal.setText(df.format(factura.getTotal()).replace(',', '.'));
            jTableRenglones.requestFocus();
        }
        if ("Modificar".equals(this.getAccion()) && factura != null && jTableRenglones.getRowCount() == 0) {
            jDateChooserFecha.setEnabled(true);
            jComboBoxReparto.setEnabled(true);
            jButtonGuardar.setEnabled(true);
            jButtonGuardarEImprimir.setEnabled(true);
            jTextFieldCodigoArticulo.setEnabled(false);
            jTextFieldCantidadRenglon.setEnabled(false);
            jButtonAgregar.setEnabled(false);
            jTableRenglones.setEnabled(false);
            jTextFieldNumero.setText(Long.toString(factura.getNumero()));
            //Calendar fechaComp = Calendar.getInstance();
            //fechaComp.setTime(factura.getFecha());
            jDateChooserFecha.setDate(factura.getFecha());
            int indiceReparto = this.devolverIndiceReparto(factura.getReparto());
            jComboBoxReparto.setSelectedIndex(indiceReparto);
            this.cliente = factura.getCliente();
            jTextFieldCliente.setText(factura.getCliente().toString());
            for (FacturaRenglon fmr : factura.getRenglones()) {
                cargarRenglon(fmr);
            }
            //Cargo los totales
            jLabelDescuento.setText(df.format(factura.getDescuento()).replace(',', '.'));
            jLabelSubTotal.setText(df.format(factura.getSubtotal()).replace(',', '.'));
            jLabelIvaMinimo.setText(df.format(factura.getTotalMinimo()).replace(',', '.'));
            jLabelIvaBasico.setText(df.format(factura.getTotalBasico()).replace(',', '.'));
            jLabelTotal.setText(df.format(factura.getTotal()).replace(',', '.'));
            try {
                sisFacturas.borrarDeudaDeFacturaManualCliente(factura);
            } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
            }
            jTextFieldNumero.requestFocus();
            jTextFieldNumero.selectAll();
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
        if(numeroDeFactura != 0) {
            
            if("NuevaDesdeFacturaAnterior".equals(this.getAccion()) && !yaCargoElNumero) {
                yaCargoElNumero = true;
                jTextFieldNumero.setText(Long.toString(numeroDeFactura));
                jTextFieldNumero.requestFocus();
                jTextFieldNumero.selectAll();
            }
        }
    }//GEN-LAST:event_formWindowActivated

    private void jTableRenglonesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRenglonesMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
            if (jTableRenglones.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un renglon.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                FacturaRenglon fmr = getFactura().getRenglones().get(jTableRenglones.getSelectedRow());
                getFactura().setSubtotal(getFactura().getSubtotal() - fmr.getSubtotal());
                getFactura().setTotal(getFactura().getTotal() - fmr.getTotal());
                if ("Basico".equals(fmr.getArticulo().getIva().getNombre())) {
                    getFactura().setTotalBasico(getFactura().getTotalBasico() - fmr.getIva());
                }
                if ("Minimo".equals(fmr.getArticulo().getIva().getNombre())) {
                    getFactura().setTotalBasico(getFactura().getTotalMinimo() - fmr.getIva());
                }
                jLabelSubTotal.setText(df.format(getFactura().getSubtotal()).replace(',', '.'));
                jLabelIvaMinimo.setText(df.format(getFactura().getTotalMinimo()).replace(',', '.'));
                jLabelIvaBasico.setText(df.format(getFactura().getTotalBasico()).replace(',', '.'));
                jLabelTotal.setText(df.format(getFactura().getTotal()).replace(',', '.'));

                VentanaModificarRenglonFactura vmfmr = new VentanaModificarRenglonFactura(IngresoFacturas.this, true);
                vmfmr.setFacturaRenglon(fmr);
                vmfmr.setVisible(true);
                //Actualizo el objeto de la tabla
                int fila = jTableRenglones.getSelectedRow();
                modelo.setValueAt(fmr.getArticulo().getCodigo(), fila, 0);
                modelo.setValueAt(fmr.getArticulo().getDescripcion(), fila, 1);
                modelo.setValueAt(fmr.getCantidad(), fila, 2);
                modelo.setValueAt(df.format(fmr.getPrecio()).replace(',', '.'), fila, 3);
                modelo.setValueAt(df.format(fmr.getSubtotal()).replace(',', '.'), fila, 4);
                modelo.setValueAt(df.format(fmr.getTotal()).replace(',', '.'), fila, 5);

                getFactura().setSubtotal(getFactura().getSubtotal() + fmr.getSubtotal());
                getFactura().setTotal(getFactura().getTotal() + fmr.getTotal());
                if ("Basico".equals(fmr.getArticulo().getIva().getNombre())) {
                    getFactura().setTotalBasico(getFactura().getTotalBasico() + fmr.getIva());
                }
                if ("Minimo".equals(fmr.getArticulo().getIva().getNombre())) {
                    getFactura().setTotalBasico(getFactura().getTotalMinimo() + fmr.getIva());
                }
                jLabelSubTotal.setText(df.format(getFactura().getSubtotal()).replace(',', '.'));
                jLabelIvaMinimo.setText(df.format(getFactura().getTotalMinimo()).replace(',', '.'));
                jLabelIvaBasico.setText(df.format(getFactura().getTotalBasico()).replace(',', '.'));
                jLabelTotal.setText(df.format(getFactura().getTotal()).replace(',', '.'));
                modificoRenglon = true;
            }
        }
    }//GEN-LAST:event_jTableRenglonesMouseClicked

    private void jTableRenglonesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableRenglonesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F10) {
            jButtonGuardar.doClick();
        }
        if (evt.getKeyCode() == KeyEvent.VK_F11) {
            jButtonGuardarEImprimir.doClick();
        }
    }//GEN-LAST:event_jTableRenglonesKeyPressed

    private void jButtonRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefrescarActionPerformed
        // TODO add your handling code here:
        IngresoFacturas vif = new IngresoFacturas(null, false);
        vif.setTipoDoc(tipoDoc);
        vif.setAccion("Nuevo");
        vif.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButtonRefrescarActionPerformed

    private void jButtonGuardarEImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarEImprimirActionPerformed
        // TODO add your handling code here:
        try {
            //if (factura.isEsManual()) {
                if ("Modificar".equals(this.getAccion())) {
                    factura.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                    factura.setNumero(Long.parseLong(jTextFieldNumero.getText().trim()));
                    
                    Date fechaFact = jDateChooserFecha.getDate();
                    this.factura.setFecha(fechaFact);
                    sisFacturas.actualizarFacturaManual(factura);
                    try {
                        SistemaFacturas.getImpresion().imprimirFactura(factura);
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(IngresoFacturas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    this.dispose();
                } else if (sisFacturas.guardarFacturaManual(factura)) {
                    //Se guardo bien, habro de nuevo la misma ventana, dejo la fecha, el reparto y aumento 1 en el numero, borro el cliente.
                    long proximoNumero = this.factura.getNumero() + 1;
                    try {
                        SistemaFacturas.getImpresion().imprimirFactura(factura);
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(IngresoFacturas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    IngresoFacturas vif = new IngresoFacturas(null, false);
                    vif.setAccion("NuevaDesdeFacturaAnterior");
                    vif.setTipoDoc(factura.getTipoDocumento());
                    vif.setNumeroDeFactura(proximoNumero);
                    vif.setVisible(true);
                    this.dispose();
                } else {

                }
            //}
        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);

            JOptionPane.showMessageDialog(this, "Error al guardar la factura. " + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            IngresoFacturas vinv = new IngresoFacturas(null, false);
            vinv.setTipoDoc(factura.getTipoDocumento());
            vinv.setVisible(true);
            this.dispose();

        }
    }//GEN-LAST:event_jButtonGuardarEImprimirActionPerformed

    private void cargarRenglon(FacturaRenglon fmr) {
        Object[] object = new Object[6];
        object[0] = fmr.getArticulo().getCodigo();
        object[1] = fmr.getArticulo().getDescripcion();
        object[2] = fmr.getCantidad();
        object[3] = df.format(fmr.getPrecio()).replace(',', '.');
        object[4] = df.format(fmr.getSubtotal()).replace(',', '.');
        object[5] = df.format(fmr.getTotal()).replace(',', '.');
        modelo.addRow(object);
    }

    public void habilitarCampos() {
        jDateChooserFecha.setEnabled(true);
        jComboBoxReparto.setEnabled(true);
        jButtonGuardar.setEnabled(true);
        jButtonGuardarEImprimir.setEnabled(true);
    }

    public void inhabilitarCampos() {
        jDateChooserFecha.setEnabled(false);
        jComboBoxReparto.setEnabled(false);
        jButtonGuardar.setEnabled(false);
        jButtonGuardarEImprimir.setEnabled(false);
        jTextFieldCodigoArticulo.setEnabled(false);
        jTextFieldCantidadRenglon.setEnabled(false);
        jButtonAgregar.setEnabled(false);
    }

    private int devolverIndiceReparto(Reparto reparto) {
        for (int i = 0; i < this.repartos.size(); i++) {
            Reparto r = this.repartos.get(i);
            if (r.equals(reparto)) {
                return i + 1;
            }
        }
        return 0;
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
            java.util.logging.Logger.getLogger(IngresoFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresoFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresoFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresoFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IngresoFacturas dialog = new IngresoFacturas(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonGuardarEImprimir;
    private javax.swing.JButton jButtonRefrescar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JComboBox jComboBoxReparto;
    private com.toedter.calendar.JDateChooser jDateChooserFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelDescripcionArt;
    private javax.swing.JLabel jLabelDescuento;
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
    private javax.swing.JTextField jTextFieldCliente;
    private javax.swing.JTextField jTextFieldCodigoArticulo;
    private javax.swing.JTextField jTextFieldNumero;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the factura
     */
    public Factura getFactura() {
        return factura;
    }

    /**
     * @param factura the factura to set
     */
    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    /**
     * @param accion the accion to set
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }

    /**
     * @return the accion
     */
    public String getAccion() {
        return accion;
    }

    /**
     * @return the tipoDoc
     */
    public DocumentoDeVenta getTipoDoc() {
        return tipoDoc;
    }

    /**
     * @param tipoDoc the tipoDoc to set
     */
    public void setTipoDoc(DocumentoDeVenta tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    /**
     * @return the numeroDeFactura
     */
    public long getNumeroDeFactura() {
        return numeroDeFactura;
    }

    /**
     * @param numeroDeFactura the numeroDeFactura to set
     */
    public void setNumeroDeFactura(long numeroDeFactura) {
        this.numeroDeFactura = numeroDeFactura;
    }
}
