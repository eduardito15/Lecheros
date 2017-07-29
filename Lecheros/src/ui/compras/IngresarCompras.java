/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.compras;

import dominio.Articulo;
import dominio.Reparto;
import dominio.Compra;
import dominio.CompraRenglon;
import dominio.DocumentoDeCompra;
import dominio.Precio;
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
import sistema.SistemaCompras;
import sistema.SistemaMantenimiento;
import sistema.SistemaMantenimientoArticulos;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class IngresarCompras extends javax.swing.JFrame {

    private DocumentoDeCompra tipoDoc;
    private final SistemaMantenimiento sis;
    private final SistemaCompras sisCompras;
    private final SistemaMantenimientoArticulos sisArticulos;
    private DefaultTableModel modelo;
    private Compra compra;
    private String accion;
    
    private final DecimalFormat df;
    List<Reparto> repartos;
    
    private boolean modificoRenglon;
    private boolean eliminoRenglon;
    public boolean mostrarMensajeFechaIncorrecta;

    /**
     * Creates new form IngresarCompras
     */
    public IngresarCompras(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        inicializarTablaRenglones();
        sis = SistemaMantenimiento.getInstance();
        sisCompras = SistemaCompras.getInstance();
        sisArticulos = SistemaMantenimientoArticulos.getInstance();
        mostrarMensajeFechaIncorrecta = true;
        jComboBoxReparto.addItem("");
        repartos = sis.devolverRepartos();
        for (Reparto c : repartos) {
            jComboBoxReparto.addItem(c);
        }
        df = new DecimalFormat("0.00");
        modificoRenglon = false;
        eliminoRenglon = false;
        agregarEnterCampoFecha();
    }
    
    public final void agregarEnterCampoFecha(){
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
                return false;
            }
        };
        modelo.addColumn("Codigo");
        modelo.addColumn("Artículo");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio");
        modelo.addColumn("SubTotal");
        modelo.addColumn("Total Precio Venta");
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
                CompraRenglon cr = compra.getRenglones().get(jTableRenglones.getSelectedRow());
                compra.setSubtotal(compra.getSubtotal() - cr.getSubtotal());
                compra.setTotal(compra.getTotal() - cr.getTotal());
                compra.setTotalAPrecioDeVentaSinIva(compra.getTotalAPrecioDeVentaSinIva() - cr.getTotalPrecioVentaSinIva());
                compra.setTotalAPrecioDeVentaConIva(compra.getTotalAPrecioDeVentaConIva() - cr.getTotalPrecioVentaConIva());
                if ("Basico".equals(cr.getArticulo().getIva().getNombre())) {
                    compra.setTotalBasico(compra.getTotalBasico() - cr.getIva());
                }
                if ("Minimo".equals(cr.getArticulo().getIva().getNombre())) {
                    compra.setTotalBasico(compra.getTotalMinimo() - cr.getIva());
                }
                jLabelSubTotal.setText(df.format(compra.getSubtotal()).replace(',', '.'));
                jLabelIvaMinimo.setText(df.format(compra.getTotalMinimo()).replace(',', '.'));
                jLabelIvaBasico.setText(df.format(compra.getTotalBasico()).replace(',', '.'));
                jLabelTotalAPrecioDeVentaSinIva.setText(df.format(compra.getTotalAPrecioDeVentaSinIva()).replace(',', '.'));
                jLabelTotalAPrecioDeVentaConIva.setText(df.format(compra.getTotalAPrecioDeVentaConIva()).replace(',', '.'));
                jLabelTotal.setText(df.format(compra.getTotal()).replace(',', '.'));
                VentanaModificarRenglonCompra vmcr = new VentanaModificarRenglonCompra(IngresarCompras.this, true);
                vmcr.setCompraRenglon(cr);
                vmcr.setVisible(true);

                //Actualizo el objeto de la tabla
                int fila = jTableRenglones.getSelectedRow();
                modelo.setValueAt(cr.getArticulo().getCodigo(), fila, 0);
                modelo.setValueAt(cr.getArticulo().getDescripcion(), fila, 1);
                modelo.setValueAt(cr.getCantidad(), fila, 2);
                modelo.setValueAt(df.format(cr.getPrecio()).replace(',', '.'), fila, 3);
                modelo.setValueAt(df.format(cr.getSubtotal()).replace(',', '.'), fila, 4);
                modelo.setValueAt(df.format(cr.getTotalPrecioVentaSinIva()).replace(',', '.'), fila, 5);

                compra.setSubtotal(compra.getSubtotal() + cr.getSubtotal());
                compra.setTotal(compra.getTotal() + cr.getTotal());
                compra.setTotalAPrecioDeVentaSinIva(compra.getTotalAPrecioDeVentaSinIva() + cr.getTotalPrecioVentaSinIva());
                compra.setTotalAPrecioDeVentaConIva(compra.getTotalAPrecioDeVentaConIva() + cr.getTotalPrecioVentaConIva());
                if ("Basico".equals(cr.getArticulo().getIva().getNombre())) {
                    compra.setTotalBasico(compra.getTotalBasico() + cr.getIva());
                }
                if ("Minimo".equals(cr.getArticulo().getIva().getNombre())) {
                    compra.setTotalBasico(compra.getTotalMinimo() + cr.getIva());
                }
                jLabelSubTotal.setText(df.format(compra.getSubtotal()).replace(',', '.'));
                jLabelIvaMinimo.setText(df.format(compra.getTotalMinimo()).replace(',', '.'));
                jLabelIvaBasico.setText(df.format(compra.getTotalBasico()).replace(',', '.'));
                jLabelTotalAPrecioDeVentaSinIva.setText(df.format(compra.getTotalAPrecioDeVentaSinIva()).replace(',', '.'));
                jLabelTotalAPrecioDeVentaConIva.setText(df.format(compra.getTotalAPrecioDeVentaConIva()).replace(',', '.'));
                jLabelTotal.setText(df.format(compra.getTotal()).replace(',', '.'));

                modificoRenglon = true;

            }

        });
        popupMenu.add(modItem);
        
        JMenuItem deleteItem = new JMenuItem("Eliminar");
        deleteItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                CompraRenglon cr = compra.getRenglones().get(jTableRenglones.getSelectedRow());
                int resp = JOptionPane.showConfirmDialog(null, "Seguro que desea eliminar el artículo " + cr.getArticulo().getDescripcion() + " ?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    modelo.removeRow(jTableRenglones.getSelectedRow());
                    compra.getRenglones().remove(cr);
                    //Actualizo los totales y subtotales
                    compra.setSubtotal(compra.getSubtotal() - cr.getSubtotal());
                    compra.setTotal(compra.getTotal() - cr.getTotal());
                    compra.setTotalAPrecioDeVentaSinIva(compra.getTotalAPrecioDeVentaSinIva() - cr.getTotalPrecioVentaSinIva());
                    compra.setTotalAPrecioDeVentaConIva(compra.getTotalAPrecioDeVentaConIva() - cr.getTotalPrecioVentaConIva());
                    if("Basico".equals(cr.getArticulo().getIva().getNombre())){
                        compra.setTotalBasico(compra.getTotalBasico() - cr.getIva());
                    }
                    if("Minimo".equals(cr.getArticulo().getIva().getNombre())){
                        compra.setTotalBasico(compra.getTotalMinimo() - cr.getIva());
                    }
                    jLabelSubTotal.setText(df.format(compra.getSubtotal()).replace(',', '.'));
                    jLabelIvaMinimo.setText(df.format(compra.getTotalMinimo()).replace(',', '.'));
                    jLabelIvaBasico.setText(df.format(compra.getTotalBasico()).replace(',', '.'));
                    jLabelTotalAPrecioDeVentaSinIva.setText(df.format(compra.getTotalAPrecioDeVentaSinIva()).replace(',', '.'));
                    jLabelTotalAPrecioDeVentaConIva.setText(df.format(compra.getTotalAPrecioDeVentaConIva()).replace(',', '.'));
                    jLabelTotal.setText(df.format(compra.getTotal()).replace(',', '.'));
                    
                    eliminoRenglon = true;
                }
            }

        });
        popupMenu.add(deleteItem);
        
        JMenuItem cambiarPrecioItem = new JMenuItem("Cambiar Precio");
        cambiarPrecioItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CompraRenglon cr = compra.getRenglones().get(jTableRenglones.getSelectedRow());
                    compra.setSubtotal(compra.getSubtotal() - cr.getSubtotal());
                    compra.setTotal(compra.getTotal() - cr.getTotal());
                    compra.setTotalAPrecioDeVentaSinIva(compra.getTotalAPrecioDeVentaSinIva() - cr.getTotalPrecioVentaSinIva());
                    compra.setTotalAPrecioDeVentaConIva(compra.getTotalAPrecioDeVentaConIva() - cr.getTotalPrecioVentaConIva());
                    if ("Basico".equals(cr.getArticulo().getIva().getNombre())) {
                        compra.setTotalBasico(compra.getTotalBasico() - cr.getIva());
                    }
                    if ("Minimo".equals(cr.getArticulo().getIva().getNombre())) {
                        compra.setTotalBasico(compra.getTotalMinimo() - cr.getIva());
                    }
                    jLabelSubTotal.setText(df.format(compra.getSubtotal()).replace(',', '.'));
                    jLabelIvaMinimo.setText(df.format(compra.getTotalMinimo()).replace(',', '.'));
                    jLabelIvaBasico.setText(df.format(compra.getTotalBasico()).replace(',', '.'));
                    jLabelTotalAPrecioDeVentaSinIva.setText(df.format(compra.getTotalAPrecioDeVentaSinIva()).replace(',', '.'));
                    jLabelTotalAPrecioDeVentaConIva.setText(df.format(compra.getTotalAPrecioDeVentaConIva()).replace(',', '.'));
                    jLabelTotal.setText(df.format(compra.getTotal()).replace(',', '.'));
                    
                    MantenimientoPreciosDesdeCompras vmpdc = new MantenimientoPreciosDesdeCompras(IngresarCompras.this, true);
                    vmpdc.setArticulo(cr.getArticulo());
                    vmpdc.setFecha(compra.getFecha());
                    vmpdc.setVisible(true);
                    
                    //Actualizo la informacion del renglon
                    //cr.setArticulo(a);
                    //cr.setCantidad(Double.parseDouble(jTextFieldCantidadRenglon.getText().trim()));
                    Precio p = sisArticulos.devolverPrecioParaFechaPorArticulo(cr.getArticulo(), compra.getFecha());
                    if (p != null) {
                        //Cargo el objeto renglon de compra
                        cr.setPrecio(p.getPrecioCompra());
                        cr.setSubtotal(p.getPrecioCompra() * cr.getCantidad());
                        cr.setIva((cr.getSubtotal() * cr.getArticulo().getIva().getPorcentaje() / 100));
                        cr.setTotal(cr.getSubtotal() + cr.getIva());
                        cr.setTotalPrecioVentaSinIva(p.getPrecioVenta() * cr.getCantidad());
                        cr.setTotalPrecioVentaConIva(cr.getTotalPrecioVentaSinIva() + ((p.getPrecioVenta() * cr.getCantidad()) * cr.getArticulo().getIva().getPorcentaje() / 100));
                    }
                    //Actualizo el objeto de la tabla
                    int fila = jTableRenglones.getSelectedRow();
                    modelo.setValueAt(cr.getArticulo().getCodigo(), fila, 0);
                    modelo.setValueAt(cr.getArticulo().getDescripcion(), fila, 1);
                    modelo.setValueAt(cr.getCantidad(), fila, 2);
                    modelo.setValueAt(df.format(cr.getPrecio()).replace(',', '.'), fila, 3);
                    modelo.setValueAt(df.format(cr.getSubtotal()).replace(',', '.'), fila, 4);
                    modelo.setValueAt(df.format(cr.getTotalPrecioVentaSinIva()).replace(',', '.'), fila, 5);
                    
                    compra.setSubtotal(compra.getSubtotal() + cr.getSubtotal());
                    compra.setTotal(compra.getTotal() + cr.getTotal());
                    compra.setTotalAPrecioDeVentaSinIva(compra.getTotalAPrecioDeVentaSinIva() + cr.getTotalPrecioVentaSinIva());
                    compra.setTotalAPrecioDeVentaConIva(compra.getTotalAPrecioDeVentaConIva() + cr.getTotalPrecioVentaConIva());
                    if ("Basico".equals(cr.getArticulo().getIva().getNombre())) {
                        compra.setTotalBasico(compra.getTotalBasico() + cr.getIva());
                    }
                    if ("Minimo".equals(cr.getArticulo().getIva().getNombre())) {
                        compra.setTotalBasico(compra.getTotalMinimo() + cr.getIva());
                    }
                    jLabelSubTotal.setText(df.format(compra.getSubtotal()).replace(',', '.'));
                    jLabelIvaMinimo.setText(df.format(compra.getTotalMinimo()).replace(',', '.'));
                    jLabelIvaBasico.setText(df.format(compra.getTotalBasico()).replace(',', '.'));
                    jLabelTotalAPrecioDeVentaSinIva.setText(df.format(compra.getTotalAPrecioDeVentaSinIva()).replace(',', '.'));
                    jLabelTotalAPrecioDeVentaConIva.setText(df.format(compra.getTotalAPrecioDeVentaConIva()).replace(',', '.'));
                    jLabelTotal.setText(df.format(compra.getTotal()).replace(',', '.'));
                    
                    modificoRenglon = true;
                } catch (Exception ex) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                    SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                    JOptionPane.showMessageDialog(IngresarCompras.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }

            }

        });
        popupMenu.add(cambiarPrecioItem);
        
        jTableRenglones.setComponentPopupMenu(popupMenu);
    }
    
    private void cargarRenglon(CompraRenglon cr){
        Object[] object = new Object[6];
        object[0] = cr.getArticulo().getCodigo();
        object[1] = cr.getArticulo().getDescripcion();
        object[2] = cr.getCantidad();
        object[3] = df.format(cr.getPrecio()).replace(',', '.');
        object[4] = df.format(cr.getSubtotal()).replace(',', '.');
        object[5] = df.format(cr.getTotalPrecioVentaSinIva()).replace(',', '.');
        modelo.addRow(object);
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
        jLabel3 = new javax.swing.JLabel();
        jTextFieldNumero = new javax.swing.JTextField();
        jComboBoxReparto = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldCodigoArticulo = new javax.swing.JTextField();
        jTextFieldCantidadRenglon = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableRenglones = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabelSubTotal = new javax.swing.JLabel();
        jLabelIvaMinimo = new javax.swing.JLabel();
        jLabelIvaBasico = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabelTotalAPrecioDeVentaSinIva = new javax.swing.JLabel();
        jButtonAgregar = new javax.swing.JButton();
        jButtonGuardar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabelDescripcionArt = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabelTotalAPrecioDeVentaConIva = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jDateChooserFecha = new com.toedter.calendar.JDateChooser();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jButtonRefrescar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Compras");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Compra");

        jLabel1.setText("Numero:");

        jLabel3.setText("Reparto:");

        jTextFieldNumero.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldNumeroFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldNumeroFocusLost(evt);
            }
        });
        jTextFieldNumero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNumeroKeyPressed(evt);
            }
        });

        jComboBoxReparto.setToolTipText("");
        jComboBoxReparto.setAlignmentX(0.0F);
        jComboBoxReparto.setEnabled(false);
        jComboBoxReparto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jComboBoxRepartoFocusLost(evt);
            }
        });
        jComboBoxReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxRepartoKeyPressed(evt);
            }
        });

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

        jTableRenglones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Artículo", "Cantidad", "Precio", "SubTotal", "Total Precio Venta"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableRenglones.setEnabled(false);
        jTableRenglones.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel6.setText("Total: ");

        jLabelTotal.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTotal.setText("0.0");

        jLabel9.setText("Iva Minimo:");

        jLabel10.setText("Iva Basico:");

        jLabel8.setText("Sub Total:");

        jLabelSubTotal.setText("0.0");

        jLabelIvaMinimo.setText("0.0");

        jLabelIvaBasico.setText("0.0");

        jLabel7.setText("Total a Precio de Venta S/Iva: ");

        jLabelTotalAPrecioDeVentaSinIva.setText("0.0");

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
        jButtonSalir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonSalirKeyPressed(evt);
            }
        });

        jLabelDescripcionArt.setText("Artículo");

        jLabel11.setText("Total a Precio de Venta C/Iva: ");

        jLabelTotalAPrecioDeVentaConIva.setText("0.0");

        jLabel12.setForeground(new java.awt.Color(255, 0, 51));

        jLabel2.setText("Fecha:");

        jDateChooserFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserFecha.setEnabled(false);
        jDateChooserFecha.setMaxSelectableDate(new java.util.Date(4102455665000L));
        jDateChooserFecha.setMinSelectableDate(new java.util.Date(946699261000L));

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 51, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jButtonRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/liquidaciones/refresh.png"))); // NOI18N
        jButtonRefrescar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonRefrescar.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
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
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSubTotal)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelIvaMinimo)
                            .addComponent(jLabelIvaBasico))
                        .addGap(101, 101, 101)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelTotalAPrecioDeVentaSinIva))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelTotalAPrecioDeVentaConIva)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelTotal)
                .addGap(154, 154, 154))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonRefrescar)
                .addGap(258, 258, 258)
                .addComponent(jLabelTitulo)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldCodigoArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelDescripcionArt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldCantidadRenglon, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonAgregar)
                                .addGap(13, 13, 13))
                            .addComponent(jSeparator3))
                        .addGap(67, 67, 67)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxReparto, 0, 200, Short.MAX_VALUE)
                    .addComponent(jTextFieldNumero)
                    .addComponent(jDateChooserFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addComponent(jLabelFechaIncorrecta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(134, 134, 134))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelTitulo)
                    .addComponent(jButtonRefrescar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonGuardar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonSalir)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(jLabelFechaIncorrecta)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabelTotal)
                            .addComponent(jLabel8)
                            .addComponent(jLabelSubTotal))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabelIvaMinimo)
                            .addComponent(jLabel7)
                            .addComponent(jLabelTotalAPrecioDeVentaSinIva))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabelIvaBasico)
                            .addComponent(jLabel11)
                            .addComponent(jLabelTotalAPrecioDeVentaConIva)))
                    .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        jLabelTitulo.setText(tipoDoc.getTipoDocumento());
        jTextFieldNumero.requestFocus();
        jTextFieldNumero.selectAll();
        if ("Ver".equals(this.getAccion()) && compra != null && jTableRenglones.getRowCount() == 0) {
            inhabilitarCampos();
            //jTableRenglones.setEnabled(false);
            jTextFieldNumero.setEnabled(false);
            jTextFieldNumero.setText(Long.toString(compra.getNumero()));
            //Calendar fechaComp = Calendar.getInstance();
            //fechaComp.setTime(compra.getFecha());
            jDateChooserFecha.setEnabled(true);
            jDateChooserFecha.setDate(compra.getFecha());
            jDateChooserFecha.setEnabled(false);
            int indiceReparto = this.devolverIndiceReparto(compra.getReparto());
            jComboBoxReparto.setSelectedIndex(indiceReparto);
            Collections.sort(compra.getRenglones());
            for (CompraRenglon cr : compra.getRenglones()) {
                cargarRenglon(cr);
            }
            //Cargo los totales
            jLabelSubTotal.setText(df.format(compra.getSubtotal()).replace(',', '.'));
            jLabelIvaMinimo.setText(df.format(compra.getTotalMinimo()).replace(',', '.'));
            jLabelIvaBasico.setText(df.format(compra.getTotalBasico()).replace(',', '.'));
            jLabelTotalAPrecioDeVentaSinIva.setText(df.format(compra.getTotalAPrecioDeVentaSinIva()).replace(',', '.'));
            jLabelTotalAPrecioDeVentaConIva.setText(df.format(compra.getTotalAPrecioDeVentaConIva()).replace(',', '.'));
            jLabelTotal.setText(df.format(compra.getTotal()).replace(',', '.'));
            //jTableRenglones.requestFocus();
            jButtonSalir.requestFocus();
        }
        if ("Modificar".equals(this.getAccion()) && compra != null && jTableRenglones.getRowCount() == 0) {
            jDateChooserFecha.setEnabled(true);
            jComboBoxReparto.setEnabled(true);
            jButtonGuardar.setEnabled(true);
            jTextFieldCodigoArticulo.setEnabled(false);
            jTextFieldCantidadRenglon.setEnabled(false);
            jButtonAgregar.setEnabled(false);
            jTableRenglones.setEnabled(false);
            jTextFieldNumero.setText(Long.toString(compra.getNumero()));
            //Calendar fechaComp = Calendar.getInstance();
            //fechaComp.setTime(compra.getFecha());
            jDateChooserFecha.setDate(compra.getFecha());
            int indiceReparto = this.devolverIndiceReparto(compra.getReparto());
            jComboBoxReparto.setSelectedIndex(indiceReparto);
            for (CompraRenglon cr : compra.getRenglones()) {
                cargarRenglon(cr);
            }
            //Cargo los totales
            jLabelSubTotal.setText(df.format(compra.getSubtotal()).replace(',', '.'));
            jLabelIvaMinimo.setText(df.format(compra.getTotalMinimo()).replace(',', '.'));
            jLabelIvaBasico.setText(df.format(compra.getTotalBasico()).replace(',', '.'));
            jLabelTotalAPrecioDeVentaSinIva.setText(df.format(compra.getTotalAPrecioDeVentaSinIva()).replace(',', '.'));
            jLabelTotalAPrecioDeVentaConIva.setText(df.format(compra.getTotalAPrecioDeVentaConIva()).replace(',', '.'));
            jLabelTotal.setText(df.format(compra.getTotal()).replace(',', '.'));
            jTextFieldNumero.requestFocus();
            jTextFieldNumero.selectAll();
        }
        if(modificoRenglon){
            jTextFieldCodigoArticulo.requestFocus();
            jTextFieldCodigoArticulo.selectAll();
            modificoRenglon = false;
        }
        
        if(eliminoRenglon){
            jTextFieldCodigoArticulo.requestFocus();
            jTextFieldCodigoArticulo.selectAll();
            eliminoRenglon = false;
        }
    }//GEN-LAST:event_formWindowActivated

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jTextFieldNumeroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNumeroKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!"".equals(jTextFieldNumero.getText().trim())) {
                try {
                    if (compra == null) {
                        //Si la compra en null seguro que es una compra nueva, entonces si o si verifico que el numero sea valido
                        if (sisCompras.numeroDeCompraValido(tipoDoc, Long.parseLong(jTextFieldNumero.getText().trim()))) {
                            habilitarCampos();
                            jDateChooserFecha.getDateEditor().getUiComponent().requestFocus();
                        } else {
                            JOptionPane.showMessageDialog(this, "Ya existe una " + tipoDoc.getTipoDocumento() + " con el numero ingresado. Por favor ingrese un nuevo numero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                            jTextFieldNumero.requestFocus();
                            jTextFieldNumero.selectAll();
                        }
                    } else //Si la compra no es null es por que es una mofificacion. Entonces solo verifico que sea valido si modifico el numero
                    if (compra.getNumero() != Long.parseLong(jTextFieldNumero.getText())) {
                        if (sisCompras.numeroDeCompraValido(tipoDoc, Long.parseLong(jTextFieldNumero.getText().trim()))) {
                            habilitarCampos();
                            jDateChooserFecha.getDateEditor().getUiComponent().requestFocus();
                        } else {
                            JOptionPane.showMessageDialog(this, "Ya existe una " + tipoDoc.getTipoDocumento() + " con el numero ingresado. Por favor ingrese un nuevo numero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                            jTextFieldNumero.requestFocus();
                            jTextFieldNumero.selectAll();
                        }
                    } else {
                        habilitarCampos();
                        jDateChooserFecha.getDateEditor().getUiComponent().requestFocus();
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
                jTextFieldCodigoArticulo.requestFocus();
                jTextFieldCodigoArticulo.selectAll();
            }
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

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
        if(evt.getKeyCode() == KeyEvent.VK_F10){
            jButtonGuardar.doClick();
        }
        if(evt.getKeyCode() == KeyEvent.VK_UP){
            if(jTableRenglones.getSelectedRow() >= 1){
                jTableRenglones.changeSelection(jTableRenglones.getSelectedRow()-1, 0, false, false);
            }
        }
        if(evt.getKeyCode() == KeyEvent.VK_DOWN){
            if(jTableRenglones.getSelectedRow() < jTableRenglones.getRowCount()-1){
                jTableRenglones.changeSelection(jTableRenglones.getSelectedRow()+1, 0, false, false);
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

    private void jTextFieldNumeroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldNumeroFocusLost
        // TODO add your handling code here:
        /*if (!"".equals(jTextFieldNumero.getText().trim())) {
            try {
                if(compra == null){
                    //Si la compra en null seguro que es una compra nueva, entonces si o si verifico que el numero sea valido
                    if (sisCompras.numeroDeCompraValido(tipoDoc, Long.parseLong(jTextFieldNumero.getText().trim()))) {
                        habilitarCampos();
                        jDateChooserFecha.getDateEditor().getUiComponent().requestFocus();
                    } else {
                        JOptionPane.showMessageDialog(this, "Ya existe una " + tipoDoc.getTipoDocumento() + " con el numero ingresado. Por favor ingrese un nuevo numero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        jTextFieldNumero.requestFocus();
                        jTextFieldNumero.selectAll();
                    }
                } else {
                    //Si la compra no es null es por que es una mofificacion. Entonces solo verifico que sea valido si modifico el numero
                    if(compra.getNumero()!=Long.parseLong(jTextFieldNumero.getText())){
                        if (sisCompras.numeroDeCompraValido(tipoDoc, Long.parseLong(jTextFieldNumero.getText().trim()))) {
                            habilitarCampos();
                            jDateChooserFecha.getDateEditor().getUiComponent().requestFocus();
                        } else {
                            JOptionPane.showMessageDialog(this, "Ya existe una " + tipoDoc.getTipoDocumento() + " con el numero ingresado. Por favor ingrese un nuevo numero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                            jTextFieldNumero.requestFocus();
                            jTextFieldNumero.selectAll();
                        }
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "El numero debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldNumero.requestFocus();
                jTextFieldNumero.selectAll();
            }
        }*/
    }//GEN-LAST:event_jTextFieldNumeroFocusLost

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

    private void jButtonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarActionPerformed
        // TODO add your handling code here:
        if ("".equals(jTextFieldCodigoArticulo.getText().trim()) || "".equals(jTextFieldCantidadRenglon.getText().trim())) {

        } else {
            if ("".equals(jTextFieldCantidadRenglon.getText())) {
            }
            try {
                CompraRenglon cr = new CompraRenglon();
                cr.setCompra(this.compra);
                int codigo = Integer.parseInt(jTextFieldCodigoArticulo.getText());
                Articulo a = sisArticulos.devolverArticuloPorCodigo(codigo);
                cr.setArticulo(a);
                cr.setCantidad(Double.parseDouble(jTextFieldCantidadRenglon.getText().trim()));
                Precio p = sisArticulos.devolverPrecioParaFechaPorArticulo(a, compra.getFecha());
                if (p != null) {
                    //Cargo el objeto renglon de compra
                    cr.setPrecio(p.getPrecioCompra());
                    cr.setSubtotal(p.getPrecioCompra() * cr.getCantidad());
                    cr.setIva((cr.getSubtotal() * a.getIva().getPorcentaje() / 100));
                    cr.setTotal(cr.getSubtotal() + cr.getIva());
                    cr.setTotalPrecioVentaSinIva(p.getPrecioVenta() * cr.getCantidad());
                    cr.setTotalPrecioVentaConIva(cr.getTotalPrecioVentaSinIva() + ((p.getPrecioVenta() * cr.getCantidad())*a.getIva().getPorcentaje()/100));
                    compra.getRenglones().add(cr);
                    //Cargo la tabla de la interfaz
                    Object[] object = new Object[6];
                    object[0] = cr.getArticulo().getCodigo();
                    object[1] = cr.getArticulo().getDescripcion();
                    object[2] = cr.getCantidad();
                    object[3] = df.format(cr.getPrecio()).replace(',', '.');
                    object[4] = df.format(cr.getSubtotal()).replace(',', '.');
                    object[5] = df.format(cr.getTotalPrecioVentaSinIva()).replace(',', '.');
                    modelo.addRow(object);
                    jTableRenglones.changeSelection(compra.getRenglones().size()-1, 0, false, false);
                    //Borro los campos de codigo y cantidad y hago foco en codigo para el nuevo ingreso
                    jTextFieldCodigoArticulo.setText("");
                    jTextFieldCantidadRenglon.setText("");
                    jTextFieldCodigoArticulo.requestFocus();
                    jTextFieldCodigoArticulo.selectAll();
                    jLabelDescripcionArt.setText("");
                    //Sumo en los subtotales de la compra los valores del renglon
                    compra.setSubtotal(compra.getSubtotal() + cr.getSubtotal());
                    compra.setTotal(compra.getTotal() + cr.getTotal());
                    compra.setTotalAPrecioDeVentaSinIva(compra.getTotalAPrecioDeVentaSinIva() + cr.getTotalPrecioVentaSinIva());
                    compra.setTotalAPrecioDeVentaConIva(compra.getTotalAPrecioDeVentaConIva() + cr.getTotalPrecioVentaConIva());
                    if (a.getIva().getId() == 2) {
                        compra.setTotalMinimo(compra.getTotalMinimo() + cr.getIva());
                    }
                    if (a.getIva().getId() == 3) {
                        compra.setTotalBasico(compra.getTotalBasico() + cr.getIva());
                    }
                    //Actualizo los valores que se muestran en las etiquetas inferiores
                    jLabelSubTotal.setText(df.format(compra.getSubtotal()).replace(',', '.'));
                    jLabelIvaMinimo.setText(df.format(compra.getTotalMinimo()).replace(',', '.'));
                    jLabelIvaBasico.setText(df.format(compra.getTotalBasico()).replace(',', '.'));
                    jLabelTotal.setText(df.format(compra.getTotal()).replace(',', '.'));
                    jLabelTotalAPrecioDeVentaSinIva.setText(df.format(compra.getTotalAPrecioDeVentaSinIva()).replace(',', '.'));
                    jLabelTotalAPrecioDeVentaConIva.setText(df.format(compra.getTotalAPrecioDeVentaConIva()).replace(',', '.'));
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

    private void jTextFieldCodigoArticuloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldCodigoArticuloFocusGained
        // TODO add your handling code here:
        //Veo si es una compra nueva
        if (!"".equals(jTextFieldNumero.getText().trim()) && jComboBoxReparto.getSelectedIndex() != 0 && compra == null) {
            try {
                jTextFieldNumero.setEnabled(false);
                jDateChooserFecha.setEnabled(false);
                jComboBoxReparto.setEnabled(false);
                this.setCompra(new Compra());
                this.compra.setNumero(Long.parseLong(jTextFieldNumero.getText().trim()));
                this.compra.setTipoDocumento(tipoDoc);
                //Calendar fechaSeleccionada = dateChooserComboFecha.getSelectedDate();
                //SimpleDateFormat formatter;
                //formatter = new SimpleDateFormat("yyyy-MM-dd");
                //String f = formatter.format(fechaSeleccionada.getTime());
                //Date fechaCompra = formatter.parse(f);
                Date fechaCompra = jDateChooserFecha.getDate();
                this.compra.setFecha(fechaCompra);
                this.compra.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
            } catch (Exception ex) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                    SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                    JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        //Si no es una compra nueva es una compra que se quiere modificar.
        if(compra!=null){
            jTextFieldNumero.setEnabled(false);
            jDateChooserFecha.setEnabled(false);
            jComboBoxReparto.setEnabled(false);
        }
    }//GEN-LAST:event_jTextFieldCodigoArticuloFocusGained

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

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        // TODO add your handling code here:
        try {
            if("Modificar".equals(this.getAccion())){
                compra.setReparto((Reparto)jComboBoxReparto.getSelectedItem());
                compra.setNumero(Long.parseLong(jTextFieldNumero.getText().trim()));
                //Calendar fechaSeleccionada = dateChooserComboFecha.getSelectedDate();
                //SimpleDateFormat formatter;
                //formatter = new SimpleDateFormat("yyyy-MM-dd");
                //String f = formatter.format(fechaSeleccionada.getTime());
                //Date fechaCompra = formatter.parse(f);
                Date fechaCompra = jDateChooserFecha.getDate();
                this.compra.setFecha(fechaCompra);
                sisCompras.actualizarCompra(compra);
                this.dispose();
            } else {
                if (sisCompras.guardarCompra(compra)) {
                    IngresarCompras vic = new IngresarCompras(null, false);
                    vic.setTipoDoc(tipoDoc);
                    vic.setVisible(true);
                    this.dispose();
                } else {

                }
            }

        } catch (Exception e) {
            String stakTrace = util.Util.obtenerStackTraceEnString(e);
            SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                
            JOptionPane.showMessageDialog(this, "Error al guardar la compra. " + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            IngresarCompras vic = new IngresarCompras(null, false);
            vic.setTipoDoc(tipoDoc);
            vic.setVisible(true);
            this.dispose();

        }
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F10) {
            JOptionPane.showMessageDialog(this, "Toco la tecla F10");
            if (compra != null) {
                jButtonGuardar.doClick();
            } else {
                JOptionPane.showMessageDialog(this, "No se puede guardar una compra vacia. Ingrese un numero de boleta y el reparto al que pertenece.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_formKeyPressed

    private void jComboBoxRepartoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBoxRepartoFocusLost
        // TODO add your handling code here:
        /*   if (jComboBoxReparto.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un reparto.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jComboBoxReparto.requestFocus();
            } else {
                jTextFieldCodigoArticulo.setEnabled(true);
                jTextFieldCantidadRenglon.setEnabled(true);
                jButtonAgregar.setEnabled(true);
                jTableRenglones.setEnabled(true);
                jTextFieldCodigoArticulo.requestFocus();
                jTextFieldCodigoArticulo.selectAll();
            }
        
        */
    }//GEN-LAST:event_jComboBoxRepartoFocusLost

    private void jTextFieldNumeroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldNumeroFocusGained
        // TODO add your handling code here:
        inhabilitarCampos();
    }//GEN-LAST:event_jTextFieldNumeroFocusGained

    private void jTableRenglonesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableRenglonesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F10) {
            jButtonGuardar.doClick();
        }
    }//GEN-LAST:event_jTableRenglonesKeyPressed

    private void jTableRenglonesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRenglonesMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
            if (jTableRenglones.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un renglon.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                CompraRenglon cr = compra.getRenglones().get(jTableRenglones.getSelectedRow());
                compra.setSubtotal(compra.getSubtotal() - cr.getSubtotal());
                compra.setTotal(compra.getTotal() - cr.getTotal());
                compra.setTotalAPrecioDeVentaSinIva(compra.getTotalAPrecioDeVentaSinIva() - cr.getTotalPrecioVentaSinIva());
                compra.setTotalAPrecioDeVentaConIva(compra.getTotalAPrecioDeVentaConIva() - cr.getTotalPrecioVentaConIva());
                if ("Basico".equals(cr.getArticulo().getIva().getNombre())) {
                    compra.setTotalBasico(compra.getTotalBasico() - cr.getIva());
                }
                if ("Minimo".equals(cr.getArticulo().getIva().getNombre())) {
                    compra.setTotalBasico(compra.getTotalMinimo() - cr.getIva());
                }
                jLabelSubTotal.setText(df.format(compra.getSubtotal()).replace(',', '.'));
                jLabelIvaMinimo.setText(df.format(compra.getTotalMinimo()).replace(',', '.'));
                jLabelIvaBasico.setText(df.format(compra.getTotalBasico()).replace(',', '.'));
                jLabelTotalAPrecioDeVentaSinIva.setText(df.format(compra.getTotalAPrecioDeVentaSinIva()).replace(',', '.'));
                jLabelTotalAPrecioDeVentaConIva.setText(df.format(compra.getTotalAPrecioDeVentaConIva()).replace(',', '.'));
                jLabelTotal.setText(df.format(compra.getTotal()).replace(',', '.'));
                VentanaModificarRenglonCompra vmcr = new VentanaModificarRenglonCompra(IngresarCompras.this, true);
                vmcr.setCompraRenglon(cr);
                vmcr.setVisible(true);

                //Actualizo el objeto de la tabla
                int fila = jTableRenglones.getSelectedRow();
                modelo.setValueAt(cr.getArticulo().getCodigo(), fila, 0);
                modelo.setValueAt(cr.getArticulo().getDescripcion(), fila, 1);
                modelo.setValueAt(cr.getCantidad(), fila, 2);
                modelo.setValueAt(df.format(cr.getPrecio()).replace(',', '.'), fila, 3);
                modelo.setValueAt(df.format(cr.getSubtotal()).replace(',', '.'), fila, 4);
                modelo.setValueAt(df.format(cr.getTotalPrecioVentaSinIva()).replace(',', '.'), fila, 5);

                compra.setSubtotal(compra.getSubtotal() + cr.getSubtotal());
                compra.setTotal(compra.getTotal() + cr.getTotal());
                compra.setTotalAPrecioDeVentaSinIva(compra.getTotalAPrecioDeVentaSinIva() + cr.getTotalPrecioVentaSinIva());
                compra.setTotalAPrecioDeVentaConIva(compra.getTotalAPrecioDeVentaConIva() + cr.getTotalPrecioVentaConIva());
                if ("Basico".equals(cr.getArticulo().getIva().getNombre())) {
                    compra.setTotalBasico(compra.getTotalBasico() + cr.getIva());
                }
                if ("Minimo".equals(cr.getArticulo().getIva().getNombre())) {
                    compra.setTotalBasico(compra.getTotalMinimo() + cr.getIva());
                }
                jLabelSubTotal.setText(df.format(compra.getSubtotal()).replace(',', '.'));
                jLabelIvaMinimo.setText(df.format(compra.getTotalMinimo()).replace(',', '.'));
                jLabelIvaBasico.setText(df.format(compra.getTotalBasico()).replace(',', '.'));
                jLabelTotalAPrecioDeVentaSinIva.setText(df.format(compra.getTotalAPrecioDeVentaSinIva()).replace(',', '.'));
                jLabelTotalAPrecioDeVentaConIva.setText(df.format(compra.getTotalAPrecioDeVentaConIva()).replace(',', '.'));
                jLabelTotal.setText(df.format(compra.getTotal()).replace(',', '.'));

                modificoRenglon = true;

            
            }
        }
    }//GEN-LAST:event_jTableRenglonesMouseClicked

    private void jButtonSalirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonSalirKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonSalir.doClick();
        }
    }//GEN-LAST:event_jButtonSalirKeyPressed

    private void jButtonRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefrescarActionPerformed
        // TODO add your handling code here:
        IngresarCompras vic = new IngresarCompras(null, false);
        vic.setTipoDoc(tipoDoc);
        vic.setAccion("Nuevo");
        vic.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButtonRefrescarActionPerformed

    private void cargarModeloRenglones() {
        TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = jTableRenglones.getColumnModel().getColumn(i);
            if (i == 1) {
                column.setPreferredWidth(300); //articulo column is bigger
            } else {
                column.setPreferredWidth(50);
            }
        }
    }

    public void habilitarCampos() {
        jDateChooserFecha.setEnabled(true);
        jComboBoxReparto.setEnabled(true);
        jButtonGuardar.setEnabled(true);
        jTableRenglones.setEnabled(true);
        //jTextFieldCodigoArticulo.setEnabled(true);
        //jTextFieldCantidadRenglon.setEnabled(true);
        //jButtonAgregar.setEnabled(true);
    }

    public void inhabilitarCampos() {
        jDateChooserFecha.setEnabled(false);
        jComboBoxReparto.setEnabled(false);
        jButtonGuardar.setEnabled(false);
        jTextFieldCodigoArticulo.setEnabled(false);
        jTextFieldCantidadRenglon.setEnabled(false);
        jButtonAgregar.setEnabled(false);
        jTableRenglones.setEnabled(false);
    }
    
    private int devolverIndiceReparto(Reparto reparto){
        for(int i = 0; i < this.repartos.size(); i ++){
            Reparto r = this.repartos.get(i);
            if(r.equals(reparto)){
                return i+1;
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
            java.util.logging.Logger.getLogger(IngresarCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresarCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresarCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresarCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IngresarCompras dialog = new IngresarCompras(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelIvaBasico;
    private javax.swing.JLabel jLabelIvaMinimo;
    private javax.swing.JLabel jLabelSubTotal;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JLabel jLabelTotalAPrecioDeVentaConIva;
    private javax.swing.JLabel jLabelTotalAPrecioDeVentaSinIva;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTableRenglones;
    private javax.swing.JTextField jTextFieldCantidadRenglon;
    private javax.swing.JTextField jTextFieldCodigoArticulo;
    private javax.swing.JTextField jTextFieldNumero;
    // End of variables declaration//GEN-END:variables

    /**
     * @param tipoDoc the tipoDoc to set
     */
    public void setTipoDoc(DocumentoDeCompra tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    /**
     * @return the accion
     */
    public String getAccion() {
        return accion;
    }

    /**
     * @param accion the accion to set
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }

    /**
     * @param compra the compra to set
     */
    public void setCompra(Compra compra) {
        this.compra = compra;
    }
}
