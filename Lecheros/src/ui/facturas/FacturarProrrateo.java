/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.facturas;

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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import lecheros.Lecheros;
import sistema.SistemaFacturas;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class FacturarProrrateo extends javax.swing.JDialog {

    private SistemaMantenimiento sis;
    private SistemaFacturas sisFacturas;

    private String[][] detallesArticulosParaFacturacion;

    private final DecimalFormat df;
    private boolean mostrarMensajeFechaIncorrecta;
    private DefaultTableModel modelo;

    /**
     * Creates new form FacturarProrrateo
     */
    public FacturarProrrateo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sis = SistemaMantenimiento.getInstance();
        sisFacturas = SistemaFacturas.getInstance();
        df = new DecimalFormat("0.00");
        jLabelEspera.setVisible(false);
        jLabelEsperaFacturar.setVisible(false);
        jLabelTotalExcentoFacturado.setVisible(false);
        jTextFieldTotalExcentoFacturado.setVisible(false);
        jLabelTotalMinimoFacturado.setVisible(false);
        jTextFieldTotalMinimoFacturado.setVisible(false);
        jLabelTotalBasicoFacturado.setVisible(false);
        jTextFieldTotalBasicoFacturado.setVisible(false);
        jComboBoxReparto.addItem("");
        List<Reparto> repartos = sis.devolverRepartos();
        for (Reparto c : repartos) {
            jComboBoxReparto.addItem(c);
        }
        if (!Lecheros.nombreEmpresa.equals("Giamo")) {
            //jLabelPorcentajeSinFacturar.setVisible(false);
            //jTextFieldPorcentajeSinFacturar.setVisible(false);
            //jButtonDescontar.setVisible(false);
        }
        inicializarTablaDetalles();
        agregarEnterCamposFecha();
        setearFechasInicio();
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

    public final void inicializarTablaDetalles() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Codigo");
        modelo.addColumn("Artículo");
        modelo.addColumn("Compras - Devoluciones");
        modelo.addColumn("Facturados");
        modelo.addColumn("Para Facturar");
        jTableDetalle.setModel(modelo);
        TableColumn column = null;
        for (int i = 0; i < 4; i++) {
            column = jTableDetalle.getColumnModel().getColumn(i);
            if (i == 1) {
                column.setPreferredWidth(200); //articulo column is bigger
            } else if (i == 2) {
                column.setPreferredWidth(100);
            } else {
                column.setPreferredWidth(50);
            }
        }
        jTableDetalle.setDefaultRenderer(Object.class, new MyCellRenderer());
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
        jButtonObtenerInformacion = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDetalle = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldTotalExcentoFacturar = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldTotalMinimoFacturar = new javax.swing.JTextField();
        jTextFieldTotalBasicoFacturar = new javax.swing.JTextField();
        jButtonFacturar = new javax.swing.JButton();
        jLabelTotalMinimoFacturado = new javax.swing.JLabel();
        jLabelTotalBasicoFacturado = new javax.swing.JLabel();
        jTextFieldTotalMinimoFacturado = new javax.swing.JTextField();
        jTextFieldTotalBasicoFacturado = new javax.swing.JTextField();
        jLabelTotalExcentoFacturado = new javax.swing.JLabel();
        jTextFieldTotalExcentoFacturado = new javax.swing.JTextField();
        jLabelEspera = new javax.swing.JLabel();
        jLabelEsperaFacturar = new javax.swing.JLabel();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jLabelHastaFechaIncorrecta = new javax.swing.JLabel();
        jRadioButtonFacturarLeche = new javax.swing.JRadioButton();
        jRadioButtonFacturarProductos = new javax.swing.JRadioButton();
        jLabelPorcentajeSinFacturar = new javax.swing.JLabel();
        jTextFieldPorcentajeSinFacturar = new javax.swing.JTextField();
        jButtonDescontar = new javax.swing.JButton();

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

        jButtonObtenerInformacion.setText("Obtener Información");
        jButtonObtenerInformacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonObtenerInformacionActionPerformed(evt);
            }
        });
        jButtonObtenerInformacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonObtenerInformacionKeyPressed(evt);
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

        jTableDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Artículo", "Compras - Devoluciones", "Facturados", "Para Facturar"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableDetalle);

        jLabel4.setText("Total Excento para Facturar:");

        jTextFieldTotalExcentoFacturar.setEditable(false);
        jTextFieldTotalExcentoFacturar.setText("0");

        jLabel5.setText("Total Minimo para Facturar:");

        jLabel6.setText("Total Basico para Facturar:");

        jTextFieldTotalMinimoFacturar.setEditable(false);
        jTextFieldTotalMinimoFacturar.setText("0");

        jTextFieldTotalBasicoFacturar.setEditable(false);
        jTextFieldTotalBasicoFacturar.setText("0");

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

        jLabelTotalMinimoFacturado.setText("Total Minimo Facturado:");

        jLabelTotalBasicoFacturado.setText("Total Basico Facturado:");

        jTextFieldTotalMinimoFacturado.setEditable(false);
        jTextFieldTotalMinimoFacturado.setText("0");

        jTextFieldTotalBasicoFacturado.setEditable(false);
        jTextFieldTotalBasicoFacturado.setText("0");

        jLabelTotalExcentoFacturado.setText("Total Excento Facturado:");

        jTextFieldTotalExcentoFacturado.setEditable(false);
        jTextFieldTotalExcentoFacturado.setText("0");

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        jLabelEsperaFacturar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabelHastaFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelHastaFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jRadioButtonFacturarLeche.setSelected(true);
        jRadioButtonFacturarLeche.setText("Facturar Leche");

        jRadioButtonFacturarProductos.setText("Facturar Productos");

        jLabelPorcentajeSinFacturar.setText("Porcentaje para dejar sin facturar:");

        jButtonDescontar.setText("Descontar");
        jButtonDescontar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDescontarActionPerformed(evt);
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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 685, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonFacturar, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelTotalExcentoFacturado, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelTotalMinimoFacturado, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelTotalBasicoFacturado, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldTotalExcentoFacturar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldTotalMinimoFacturar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldTotalBasicoFacturar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jTextFieldTotalExcentoFacturado, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldTotalMinimoFacturado, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldTotalBasicoFacturado, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelEsperaFacturar)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(386, 386, 386)
                        .addComponent(jLabelTitulo1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelPorcentajeSinFacturar)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBoxReparto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButtonObtenerInformacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabelEspera)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelFechaIncorrecta)
                                    .addComponent(jLabelHastaFechaIncorrecta)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(395, 395, 395)
                                .addComponent(jRadioButtonFacturarLeche)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonFacturarProductos))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldPorcentajeSinFacturar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonDescontar)))))
                .addGap(21, 28, Short.MAX_VALUE))
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
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)
                                    .addComponent(jButtonObtenerInformacion))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonSalir))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jRadioButtonFacturarLeche)
                                    .addComponent(jRadioButtonFacturarProductos)))
                            .addComponent(jLabelEspera))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelPorcentajeSinFacturar)
                            .addComponent(jTextFieldPorcentajeSinFacturar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonDescontar))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jTextFieldTotalExcentoFacturar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jTextFieldTotalMinimoFacturar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(jTextFieldTotalBasicoFacturar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelEsperaFacturar)
                                    .addComponent(jButtonFacturar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelTotalExcentoFacturado)
                                    .addComponent(jTextFieldTotalExcentoFacturado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelTotalMinimoFacturado)
                                    .addComponent(jTextFieldTotalMinimoFacturado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelTotalBasicoFacturado)
                                    .addComponent(jTextFieldTotalBasicoFacturado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelFechaIncorrecta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelHastaFechaIncorrecta)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jComboBoxRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxRepartoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonObtenerInformacion.requestFocus();
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

    private void jButtonObtenerInformacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonObtenerInformacionKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonObtenerInformacion.doClick();
        }
    }//GEN-LAST:event_jButtonObtenerInformacionKeyPressed

    private void jButtonSalirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonSalirKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonSalir.doClick();
        }
    }//GEN-LAST:event_jButtonSalirKeyPressed

    private void jButtonFacturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFacturarActionPerformed
        // TODO add your handling code here:
        if (jRadioButtonFacturarLeche.isSelected() || jRadioButtonFacturarProductos.isSelected()) {
            if (jComboBoxReparto.getSelectedIndex() == 0) {
                int resp = JOptionPane.showConfirmDialog(this, "Si no selecciona ningun reparto se hace de todos los repartos. Esto puede generar diferencias analizando cada reparto por separado. Se recomienda hacer por reparto. Desea continuar?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    Date desdeFecha = jDateChooserDesdeFecha.getDate();
                    Date hastaFecha = jDateChooserHastaFecha.getDate();
                    Reparto r;
                    if (jComboBoxReparto.getSelectedIndex() == 0) {
                        r = null;
                    } else {
                        r = (Reparto) jComboBoxReparto.getSelectedItem();
                    }
                    jLabelEsperaFacturar.setVisible(true);
                    jDateChooserDesdeFecha.setEnabled(false);
                    jDateChooserHastaFecha.setEnabled(false);
                    jComboBoxReparto.setEnabled(false);
                    jButtonObtenerInformacion.setEnabled(false);
                    jButtonFacturar.setEnabled(false);
                    Thread worker = new Thread() {
                        public void run() {
                            String[] facturarProrrateo;
                            if (SistemaMantenimiento.getInstance().devolverConfiguracionFacturacion().isSoloLeche()) {
                                facturarProrrateo = sisFacturas.facturarProrrateo(desdeFecha, hastaFecha, r, detallesArticulosParaFacturacion, Double.parseDouble(jTextFieldTotalExcentoFacturar.getText()), Double.parseDouble(jTextFieldTotalMinimoFacturar.getText()), Double.parseDouble(jTextFieldTotalBasicoFacturar.getText()), jRadioButtonFacturarLeche.isSelected(), jRadioButtonFacturarProductos.isSelected());
                            } else {
                                facturarProrrateo = sisFacturas.facturarProrrateo(desdeFecha, hastaFecha, r, detallesArticulosParaFacturacion, Double.parseDouble(jTextFieldTotalExcentoFacturar.getText()), Double.parseDouble(jTextFieldTotalMinimoFacturar.getText()), Double.parseDouble(jTextFieldTotalBasicoFacturar.getText()), jRadioButtonFacturarLeche.isSelected(), jRadioButtonFacturarProductos.isSelected());
                            }

                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    jLabelEsperaFacturar.setVisible(false);
                                    jDateChooserDesdeFecha.setEnabled(true);
                                    jDateChooserHastaFecha.setEnabled(true);
                                    jComboBoxReparto.setEnabled(true);
                                    jButtonObtenerInformacion.setEnabled(true);
                                    jButtonFacturar.setEnabled(true);
                                    //Cargo el Resultado
                                    jLabelTotalExcentoFacturado.setVisible(true);
                                    jTextFieldTotalExcentoFacturado.setVisible(true);
                                    jLabelTotalMinimoFacturado.setVisible(true);
                                    jTextFieldTotalMinimoFacturado.setVisible(true);
                                    jLabelTotalBasicoFacturado.setVisible(true);
                                    jTextFieldTotalBasicoFacturado.setVisible(true);
                                    jTextFieldTotalExcentoFacturado.setText(df.format(Double.parseDouble(facturarProrrateo[0])).replace(',', '.'));
                                    jTextFieldTotalMinimoFacturado.setText(df.format(Double.parseDouble(facturarProrrateo[1])).replace(',', '.'));
                                    jTextFieldTotalBasicoFacturado.setText(df.format(Double.parseDouble(facturarProrrateo[2])).replace(',', '.'));
                                    jButtonSalir.requestFocus();
                                }
                            });

                        }
                    };
                    worker.start();
                }
            } else {
                Date desdeFecha = jDateChooserDesdeFecha.getDate();
                Date hastaFecha = jDateChooserHastaFecha.getDate();
                Reparto r;
                if (jComboBoxReparto.getSelectedIndex() == 0) {
                    r = null;
                } else {
                    r = (Reparto) jComboBoxReparto.getSelectedItem();
                }
                jLabelEsperaFacturar.setVisible(true);
                jDateChooserDesdeFecha.setEnabled(false);
                jDateChooserHastaFecha.setEnabled(false);
                jComboBoxReparto.setEnabled(false);
                jButtonObtenerInformacion.setEnabled(false);
                jButtonFacturar.setEnabled(false);
                Thread worker = new Thread() {
                    public void run() {
                        String[] facturarProrrateo;
                        if (SistemaMantenimiento.getInstance().devolverConfiguracionFacturacion().isSoloLeche()) {
                            facturarProrrateo = sisFacturas.facturarProrrateo(desdeFecha, hastaFecha, r, detallesArticulosParaFacturacion, Double.parseDouble(jTextFieldTotalExcentoFacturar.getText()), Double.parseDouble(jTextFieldTotalMinimoFacturar.getText()), Double.parseDouble(jTextFieldTotalBasicoFacturar.getText()), jRadioButtonFacturarLeche.isSelected(), jRadioButtonFacturarProductos.isSelected());
                        } else {
                            facturarProrrateo = sisFacturas.facturarProrrateo(desdeFecha, hastaFecha, r, detallesArticulosParaFacturacion, Double.parseDouble(jTextFieldTotalExcentoFacturar.getText()), Double.parseDouble(jTextFieldTotalMinimoFacturar.getText()), Double.parseDouble(jTextFieldTotalBasicoFacturar.getText()), jRadioButtonFacturarLeche.isSelected(), jRadioButtonFacturarProductos.isSelected());
                        }

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                jLabelEsperaFacturar.setVisible(false);
                                jDateChooserDesdeFecha.setEnabled(true);
                                jDateChooserHastaFecha.setEnabled(true);
                                jComboBoxReparto.setEnabled(true);
                                jButtonObtenerInformacion.setEnabled(true);
                                jButtonFacturar.setEnabled(true);
                                //Cargo el Resultado
                                jLabelTotalExcentoFacturado.setVisible(true);
                                jTextFieldTotalExcentoFacturado.setVisible(true);
                                jLabelTotalMinimoFacturado.setVisible(true);
                                jTextFieldTotalMinimoFacturado.setVisible(true);
                                jLabelTotalBasicoFacturado.setVisible(true);
                                jTextFieldTotalBasicoFacturado.setVisible(true);
                                jTextFieldTotalExcentoFacturado.setText(df.format(Double.parseDouble(facturarProrrateo[0])).replace(',', '.'));
                                jTextFieldTotalMinimoFacturado.setText(df.format(Double.parseDouble(facturarProrrateo[1])).replace(',', '.'));
                                jTextFieldTotalBasicoFacturado.setText(df.format(Double.parseDouble(facturarProrrateo[2])).replace(',', '.'));
                                jButtonSalir.requestFocus();
                            }
                        });

                    }
                };
                worker.start();
            }
        } else {
            JOptionPane.showMessageDialog(FacturarProrrateo.this, "Debe seleccionar Facturar Leche y/o Facturar Productos. Al menos uno de los dos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonFacturarActionPerformed

    private void jButtonFacturarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonFacturarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonFacturar.doClick();
        }
    }//GEN-LAST:event_jButtonFacturarKeyPressed

    private void jButtonObtenerInformacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonObtenerInformacionActionPerformed
        // TODO add your handling code here:
        int indiceSeleccionado = jComboBoxReparto.getSelectedIndex();
        jTextFieldTotalBasicoFacturado.setText("0");
        jTextFieldTotalBasicoFacturar.setText("0");
        jTextFieldTotalExcentoFacturado.setText("0");
        jTextFieldTotalExcentoFacturar.setText("0");
        jTextFieldTotalMinimoFacturado.setText("0");
        jTextFieldTotalMinimoFacturar.setText("0");
        if (jComboBoxReparto.getSelectedIndex() == 0) {
            int resp = JOptionPane.showConfirmDialog(this, "Si no selecciona ningun reparto se hace de todos los repartos. Esto puede generar diferencias analizando cada reparto por separado. Se recomienda hacer por reparto. Desea continuar?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (resp == JOptionPane.YES_OPTION) {
                inicializarTablaDetalles();
                Date desdeFecha = jDateChooserDesdeFecha.getDate();
                Date hastaFecha = jDateChooserHastaFecha.getDate();
                Reparto r;
                if (jComboBoxReparto.getSelectedIndex() == 0) {
                    r = null;
                } else {
                    r = (Reparto) jComboBoxReparto.getSelectedItem();
                }
                jLabelEspera.setVisible(true);
                jDateChooserDesdeFecha.setEnabled(false);
                jDateChooserHastaFecha.setEnabled(false);
                jComboBoxReparto.setEnabled(false);
                jButtonObtenerInformacion.setEnabled(false);
                jButtonFacturar.setEnabled(false);
                Thread worker = new Thread() {
                    public void run() {
                        try {
                            detallesArticulosParaFacturacion = sisFacturas.detalleArticulosFacturaProrrateo(desdeFecha, hastaFecha, r);
                        } catch (Exception exp) {
                            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                            JOptionPane.showMessageDialog(FacturarProrrateo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                jLabelEspera.setVisible(false);
                                jDateChooserDesdeFecha.setEnabled(true);
                                jDateChooserHastaFecha.setEnabled(true);
                                jComboBoxReparto.setEnabled(true);
                                jButtonObtenerInformacion.setEnabled(true);
                                jButtonFacturar.setEnabled(true);
                                cargarTabla(detallesArticulosParaFacturacion);
                                jButtonFacturar.requestFocus();
                            }
                        });

                    }
                };
                worker.start();
            }
        } else {
            inicializarTablaDetalles();
            Date desdeFecha = jDateChooserDesdeFecha.getDate();
            Date hastaFecha = jDateChooserHastaFecha.getDate();
            Reparto r;
            if (jComboBoxReparto.getSelectedIndex() == 0) {
                r = null;
            } else {
                r = (Reparto) jComboBoxReparto.getSelectedItem();
            }
            jLabelEspera.setVisible(true);
            jDateChooserDesdeFecha.setEnabled(false);
            jDateChooserHastaFecha.setEnabled(false);
            jComboBoxReparto.setEnabled(false);
            jButtonObtenerInformacion.setEnabled(false);
            jButtonFacturar.setEnabled(false);
            Thread worker = new Thread() {
                public void run() {
                    try {
                        detallesArticulosParaFacturacion = sisFacturas.detalleArticulosFacturaProrrateo(desdeFecha, hastaFecha, r);
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(FacturarProrrateo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            jLabelEspera.setVisible(false);
                            jDateChooserDesdeFecha.setEnabled(true);
                            jDateChooserHastaFecha.setEnabled(true);
                            jComboBoxReparto.setEnabled(true);
                            jButtonObtenerInformacion.setEnabled(true);
                            jButtonFacturar.setEnabled(true);
                            cargarTabla(detallesArticulosParaFacturacion);
                            jButtonFacturar.requestFocus();
                        }
                    });

                }
            };
            worker.start();
        }
    }//GEN-LAST:event_jButtonObtenerInformacionActionPerformed

    private void jButtonDescontarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDescontarActionPerformed
        // TODO add your handling code here:
        if (detallesArticulosParaFacturacion == null) {
            JOptionPane.showMessageDialog(this, "Para descontar primero debe obtener la información.", "Información", JOptionPane.INFORMATION_MESSAGE);
            jDateChooserDesdeFecha.requestFocusInWindow();
        } else if (jComboBoxReparto.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Para descontar primero debe seleccionar un reparto.", "Información", JOptionPane.INFORMATION_MESSAGE);
            jComboBoxReparto.requestFocus();
        } else {
            try {
                int porcentajeParaDescontar = Integer.parseInt(jTextFieldPorcentajeSinFacturar.getText());
                Date desdeFecha = jDateChooserDesdeFecha.getDate();
                Date hastaFecha = jDateChooserHastaFecha.getDate();
                Reparto r = (Reparto)jComboBoxReparto.getSelectedItem();
                if (porcentajeParaDescontar != 0) {
                    inicializarTablaDetalles();
                    jLabelEspera.setVisible(true);
                    jDateChooserDesdeFecha.setEnabled(false);
                    jDateChooserHastaFecha.setEnabled(false);
                    jComboBoxReparto.setEnabled(false);
                    jButtonObtenerInformacion.setEnabled(false);
                    jButtonFacturar.setEnabled(false);
                    jButtonDescontar.setEnabled(false);
                    Thread worker = new Thread() {
                        public void run() {
                            try {
                                detallesArticulosParaFacturacion = sisFacturas.descontarPorcentaje(detallesArticulosParaFacturacion, porcentajeParaDescontar, desdeFecha, hastaFecha, r);
                            } catch (Exception exp) {
                                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                                JOptionPane.showMessageDialog(FacturarProrrateo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    jLabelEspera.setVisible(false);
                                    jDateChooserDesdeFecha.setEnabled(true);
                                    jDateChooserHastaFecha.setEnabled(true);
                                    jComboBoxReparto.setEnabled(true);
                                    jButtonObtenerInformacion.setEnabled(true);
                                    jButtonFacturar.setEnabled(true);
                                    jButtonDescontar.setEnabled(true);
                                    cargarTabla(detallesArticulosParaFacturacion);
                                    jButtonFacturar.requestFocus();
                                }
                            });

                        }
                    };
                    worker.start();
                }
            } catch (NumberFormatException ne) {
                ne.printStackTrace();
                JOptionPane.showMessageDialog(this, "El porcentaje para descontar debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldPorcentajeSinFacturar.requestFocus();
                jTextFieldPorcentajeSinFacturar.selectAll();
            }
        }
    }//GEN-LAST:event_jButtonDescontarActionPerformed

    public void cargarTabla(String[][] tabla) {

        for (int i = 0; i < tabla.length; i++) {
            Object[] object = new Object[tabla[i].length];
            for (int j = 0; j < tabla[i].length; j++) {
                if (j == 2 || j == 3 || j == 4) {
                    object[j] = df.format(Double.parseDouble(tabla[i][j])).replace(',', '.');
                }
                String cod = tabla[i][j];
                if (!"Totales".equals(cod)) {
                    object[j] = tabla[i][j];
                } else {
                    jTextFieldTotalExcentoFacturar.setText(df.format(Double.parseDouble(tabla[i][j + 1])).replace(',', '.'));
                    jTextFieldTotalMinimoFacturar.setText(df.format(Double.parseDouble(tabla[i][j + 2])).replace(',', '.'));
                    jTextFieldTotalBasicoFacturar.setText(df.format(Double.parseDouble(tabla[i][j + 3])).replace(',', '.'));
                    break;
                }
            }
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
            java.util.logging.Logger.getLogger(FacturarProrrateo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FacturarProrrateo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FacturarProrrateo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FacturarProrrateo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FacturarProrrateo dialog = new FacturarProrrateo(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonDescontar;
    private javax.swing.JButton jButtonFacturar;
    private javax.swing.JButton jButtonObtenerInformacion;
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
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelEsperaFacturar;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelHastaFechaIncorrecta;
    private javax.swing.JLabel jLabelPorcentajeSinFacturar;
    private javax.swing.JLabel jLabelTitulo1;
    private javax.swing.JLabel jLabelTotalBasicoFacturado;
    private javax.swing.JLabel jLabelTotalExcentoFacturado;
    private javax.swing.JLabel jLabelTotalMinimoFacturado;
    private javax.swing.JRadioButton jRadioButtonFacturarLeche;
    private javax.swing.JRadioButton jRadioButtonFacturarProductos;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableDetalle;
    private javax.swing.JTextField jTextFieldPorcentajeSinFacturar;
    private javax.swing.JTextField jTextFieldTotalBasicoFacturado;
    private javax.swing.JTextField jTextFieldTotalBasicoFacturar;
    private javax.swing.JTextField jTextFieldTotalExcentoFacturado;
    private javax.swing.JTextField jTextFieldTotalExcentoFacturar;
    private javax.swing.JTextField jTextFieldTotalMinimoFacturado;
    private javax.swing.JTextField jTextFieldTotalMinimoFacturar;
    // End of variables declaration//GEN-END:variables
}
