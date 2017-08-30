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
import sistema.SistemaFacturas;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class VentanaVerificarFacturacion extends javax.swing.JDialog {

    private SistemaMantenimiento sis;
    private SistemaFacturas sisFacturas;

    private String[][] detallesArticulos;

    private final DecimalFormat df;
    private boolean mostrarMensajeFechaIncorrecta;
    private DefaultTableModel modelo;
    /**
     * Creates new form VentanaVerificarFacturacion
     */
    public VentanaVerificarFacturacion(java.awt.Frame parent, boolean modal) {
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
        modelo.addColumn("Facturados a Mano");
        modelo.addColumn("Facturados Prorrateo");
        modelo.addColumn("Diferencia");
        jTableResultado.setModel(modelo);
        TableColumn column = null;
        for (int i = 0; i < 4; i++) {
            column = jTableResultado.getColumnModel().getColumn(i);
            if (i == 1) {
                column.setPreferredWidth(200); //articulo column is bigger
            } else if (i == 2) {
                column.setPreferredWidth(100);
            } else {
                column.setPreferredWidth(50);
            }
        }
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableResultado = new javax.swing.JTable();
        jButtonSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabelEspera = new javax.swing.JLabel();
        jDateChooserDesdeFecha = new com.toedter.calendar.JDateChooser();
        jDateChooserHastaFecha = new com.toedter.calendar.JDateChooser();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabelHastaFechaIncorrecta = new javax.swing.JLabel();
        jComboBoxReparto = new javax.swing.JComboBox();
        jButtonVerificar = new javax.swing.JButton();
        jPanelManuales = new javax.swing.JPanel();
        jLabelTitulo2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelExcentoManuales = new javax.swing.JLabel();
        jLabelMinimoManuales = new javax.swing.JLabel();
        jLabelBasicoManuales = new javax.swing.JLabel();
        jPanelProrrateo = new javax.swing.JPanel();
        jLabelTitulo3 = new javax.swing.JLabel();
        jLabelMinimoProrrateo = new javax.swing.JLabel();
        jLabelBasicoProrrateo = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabelExcentoProrrateo = new javax.swing.JLabel();
        jPanelTotal = new javax.swing.JPanel();
        jLabelTitulo4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabelExcentoTotal = new javax.swing.JLabel();
        jLabelMinimoTotal = new javax.swing.JLabel();
        jLabelBasicoTotal = new javax.swing.JLabel();
        jPanelDiferencia = new javax.swing.JPanel();
        jLabelTitulo5 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabelExcentoDiferencia = new javax.swing.JLabel();
        jLabelMinimoDiferencia = new javax.swing.JLabel();
        jLabelBasicoDiferencia = new javax.swing.JLabel();
        jPanelDiferencia1 = new javax.swing.JPanel();
        jLabelTitulo6 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabelExcentoPorcentaje = new javax.swing.JLabel();
        jLabelMinimoPorcentaje = new javax.swing.JLabel();
        jLabelBasicoPorcentaje = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Verificar Facturación");

        jLabelTitulo1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo1.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo1.setText("Verificar Facturación");

        jTableResultado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Artículo", "Compras - Devoluciones", "Facturados A Mano", "Facturados Prorrateo", "Diferencia"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableResultado);

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

        jLabel1.setText("Desde Fecha:");

        jLabel2.setText("Hasta Fecha:");

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        jDateChooserDesdeFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserDesdeFecha.setMaxSelectableDate(new java.util.Date(4102455686000L));
        jDateChooserDesdeFecha.setMinSelectableDate(new java.util.Date(946699286000L));

        jDateChooserHastaFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserHastaFecha.setMaxSelectableDate(new java.util.Date(4102455689000L));
        jDateChooserHastaFecha.setMinSelectableDate(new java.util.Date(946699289000L));

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabel3.setText("Reparto:");

        jLabelHastaFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelHastaFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jComboBoxReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxRepartoKeyPressed(evt);
            }
        });

        jButtonVerificar.setText("Verificar");
        jButtonVerificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerificarActionPerformed(evt);
            }
        });
        jButtonVerificar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonVerificarKeyPressed(evt);
            }
        });

        jLabelTitulo2.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo2.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo2.setText("Manuales");

        jLabel4.setText("Excento:");

        jLabel5.setText("Mínimo:");

        jLabel6.setText("Básico:");

        jLabelExcentoManuales.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelExcentoManuales.setText("0");

        jLabelMinimoManuales.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelMinimoManuales.setText("0");

        jLabelBasicoManuales.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelBasicoManuales.setText("0");

        javax.swing.GroupLayout jPanelManualesLayout = new javax.swing.GroupLayout(jPanelManuales);
        jPanelManuales.setLayout(jPanelManualesLayout);
        jPanelManualesLayout.setHorizontalGroup(
            jPanelManualesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelManualesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelManualesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitulo2)
                    .addGroup(jPanelManualesLayout.createSequentialGroup()
                        .addGroup(jPanelManualesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelManualesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelExcentoManuales)
                            .addComponent(jLabelMinimoManuales)
                            .addComponent(jLabelBasicoManuales))))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanelManualesLayout.setVerticalGroup(
            jPanelManualesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelManualesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelManualesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabelExcentoManuales))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelManualesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabelMinimoManuales))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelManualesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabelBasicoManuales))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabelTitulo3.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo3.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo3.setText("Prorrateo");

        jLabelMinimoProrrateo.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelMinimoProrrateo.setText("0");

        jLabelBasicoProrrateo.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelBasicoProrrateo.setText("0");

        jLabel7.setText("Excento:");

        jLabel8.setText("Mínimo:");

        jLabel9.setText("Básico:");

        jLabelExcentoProrrateo.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelExcentoProrrateo.setText("0");

        javax.swing.GroupLayout jPanelProrrateoLayout = new javax.swing.GroupLayout(jPanelProrrateo);
        jPanelProrrateo.setLayout(jPanelProrrateoLayout);
        jPanelProrrateoLayout.setHorizontalGroup(
            jPanelProrrateoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProrrateoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProrrateoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitulo3)
                    .addGroup(jPanelProrrateoLayout.createSequentialGroup()
                        .addGroup(jPanelProrrateoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelProrrateoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelExcentoProrrateo)
                            .addComponent(jLabelMinimoProrrateo)
                            .addComponent(jLabelBasicoProrrateo))))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanelProrrateoLayout.setVerticalGroup(
            jPanelProrrateoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProrrateoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelProrrateoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabelExcentoProrrateo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelProrrateoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabelMinimoProrrateo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelProrrateoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabelBasicoProrrateo))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabelTitulo4.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo4.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo4.setText("Total");

        jLabel10.setText("Excento:");

        jLabel11.setText("Mínimo:");

        jLabel12.setText("Básico:");

        jLabelExcentoTotal.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelExcentoTotal.setText("0");

        jLabelMinimoTotal.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelMinimoTotal.setText("0");

        jLabelBasicoTotal.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelBasicoTotal.setText("0");

        javax.swing.GroupLayout jPanelTotalLayout = new javax.swing.GroupLayout(jPanelTotal);
        jPanelTotal.setLayout(jPanelTotalLayout);
        jPanelTotalLayout.setHorizontalGroup(
            jPanelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTotalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitulo4)
                    .addGroup(jPanelTotalLayout.createSequentialGroup()
                        .addGroup(jPanelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelExcentoTotal)
                            .addComponent(jLabelMinimoTotal)
                            .addComponent(jLabelBasicoTotal))))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanelTotalLayout.setVerticalGroup(
            jPanelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTotalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabelExcentoTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabelMinimoTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabelBasicoTotal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabelTitulo5.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo5.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo5.setText("Diferencia");

        jLabel13.setText("Excento:");

        jLabel14.setText("Mínimo:");

        jLabel15.setText("Básico:");

        jLabelExcentoDiferencia.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelExcentoDiferencia.setForeground(new java.awt.Color(0, 0, 255));
        jLabelExcentoDiferencia.setText("0");

        jLabelMinimoDiferencia.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelMinimoDiferencia.setForeground(new java.awt.Color(0, 0, 255));
        jLabelMinimoDiferencia.setText("0");

        jLabelBasicoDiferencia.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelBasicoDiferencia.setForeground(new java.awt.Color(0, 0, 255));
        jLabelBasicoDiferencia.setText("0");

        javax.swing.GroupLayout jPanelDiferenciaLayout = new javax.swing.GroupLayout(jPanelDiferencia);
        jPanelDiferencia.setLayout(jPanelDiferenciaLayout);
        jPanelDiferenciaLayout.setHorizontalGroup(
            jPanelDiferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDiferenciaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDiferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitulo5)
                    .addGroup(jPanelDiferenciaLayout.createSequentialGroup()
                        .addGroup(jPanelDiferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDiferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelExcentoDiferencia)
                            .addComponent(jLabelMinimoDiferencia)
                            .addComponent(jLabelBasicoDiferencia))))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        jPanelDiferenciaLayout.setVerticalGroup(
            jPanelDiferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDiferenciaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDiferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabelExcentoDiferencia))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDiferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabelMinimoDiferencia))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDiferenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabelBasicoDiferencia))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jLabelTitulo6.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo6.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo6.setText("Porcentaje");

        jLabel16.setText("Excento:");

        jLabel17.setText("Mínimo:");

        jLabel18.setText("Básico:");

        jLabelExcentoPorcentaje.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelExcentoPorcentaje.setForeground(new java.awt.Color(0, 0, 255));
        jLabelExcentoPorcentaje.setText("0");

        jLabelMinimoPorcentaje.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelMinimoPorcentaje.setForeground(new java.awt.Color(0, 0, 255));
        jLabelMinimoPorcentaje.setText("0");

        jLabelBasicoPorcentaje.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelBasicoPorcentaje.setForeground(new java.awt.Color(0, 0, 255));
        jLabelBasicoPorcentaje.setText("0");

        javax.swing.GroupLayout jPanelDiferencia1Layout = new javax.swing.GroupLayout(jPanelDiferencia1);
        jPanelDiferencia1.setLayout(jPanelDiferencia1Layout);
        jPanelDiferencia1Layout.setHorizontalGroup(
            jPanelDiferencia1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDiferencia1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDiferencia1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitulo6)
                    .addGroup(jPanelDiferencia1Layout.createSequentialGroup()
                        .addGroup(jPanelDiferencia1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelDiferencia1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelExcentoPorcentaje)
                            .addComponent(jLabelMinimoPorcentaje)
                            .addComponent(jLabelBasicoPorcentaje))))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanelDiferencia1Layout.setVerticalGroup(
            jPanelDiferencia1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDiferencia1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDiferencia1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabelExcentoPorcentaje))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDiferencia1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabelMinimoPorcentaje))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDiferencia1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabelBasicoPorcentaje))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1)
                .addGap(49, 49, 49))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelTitulo1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jPanelManuales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanelProrrateo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonVerificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(14, 14, 14)
                        .addComponent(jLabelEspera)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelFechaIncorrecta)
                            .addComponent(jLabelHastaFechaIncorrecta))
                        .addGap(0, 317, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jPanelDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanelDiferencia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabelTitulo1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jButtonVerificar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonSalir))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabelEspera)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelFechaIncorrecta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelHastaFechaIncorrecta)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelDiferencia1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelDiferencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelManuales, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelProrrateo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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

    private void jComboBoxRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxRepartoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonVerificar.requestFocus();
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

    private void jButtonVerificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerificarActionPerformed
        // TODO add your handling code here:
        inicializarTablaDetalles();
        Date desdeFecha = jDateChooserDesdeFecha.getDate();
        Date hastaFecha = jDateChooserHastaFecha.getDate();
        Reparto r;
        if(jComboBoxReparto.getSelectedIndex() == 0){
            r = null;
        } else {
            r = (Reparto)jComboBoxReparto.getSelectedItem();
        }
        jLabelEspera.setVisible(true);
        jDateChooserDesdeFecha.setEnabled(false);
        jDateChooserHastaFecha.setEnabled(false);
        jComboBoxReparto.setEnabled(false);
        jButtonVerificar.setEnabled(false);
        Thread worker = new Thread() {
            public void run() {
                try {
                    detallesArticulos = sisFacturas.detalleArticulosVerificarFacturacion(desdeFecha, hastaFecha, r);
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(VentanaVerificarFacturacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        jLabelEspera.setVisible(false);
                        jDateChooserDesdeFecha.setEnabled(true);
                        jDateChooserHastaFecha.setEnabled(true);
                        jComboBoxReparto.setEnabled(true);
                        jButtonVerificar.setEnabled(true);
                        cargarTabla(detallesArticulos);
                        jButtonSalir.requestFocus();
                    }
                });

            }
        };
        worker.start();

    }//GEN-LAST:event_jButtonVerificarActionPerformed

    private void jButtonVerificarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonVerificarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonVerificar.doClick();
        }
    }//GEN-LAST:event_jButtonVerificarKeyPressed

    public void cargarTabla(String[][] tabla) {

        for (int i = 0; i < tabla.length; i++) {
            Object[] object = new Object[tabla[i].length];
            for (int j = 0; j < tabla[i].length; j++) {
                if (j == 2 || j == 3 || j == 4 || j == 5) {
                    object[j] = df.format(Double.parseDouble(tabla[i][j])).replace(',', '.');
                }
                String cod = tabla[i][j];
                if (!"Totales Manuales".equals(cod)) {
                    if("Totales Prorrateo".equals(cod) || "Totales".equals(cod) || "Diferencias".equals(cod) || "Porcentajes".equals(cod)){
                        break;
                    }
                    object[j] = tabla[i][j];
                } else {
                    //Cargo los totales.
                    int uno = j + 1;
                    int dos = j + 2;
                    int tres = j + 3;
                    jLabelExcentoManuales.setText(df.format(Double.parseDouble(tabla[i][uno])).replace(',', '.'));
                    jLabelMinimoManuales.setText(df.format(Double.parseDouble(tabla[i][dos])).replace(',', '.'));
                    jLabelBasicoManuales.setText(df.format(Double.parseDouble(tabla[i][tres])).replace(',', '.'));
                    
                    jLabelExcentoProrrateo.setText(df.format(Double.parseDouble(tabla[i+1][uno])).replace(',', '.'));
                    jLabelMinimoProrrateo.setText(df.format(Double.parseDouble(tabla[i+1][dos])).replace(',', '.'));
                    jLabelBasicoProrrateo.setText(df.format(Double.parseDouble(tabla[i+1][tres])).replace(',', '.'));
                    
                    jLabelExcentoTotal.setText(df.format(Double.parseDouble(tabla[i+2][uno])).replace(',', '.'));
                    jLabelMinimoTotal.setText(df.format(Double.parseDouble(tabla[i+2][dos])).replace(',', '.'));
                    jLabelBasicoTotal.setText(df.format(Double.parseDouble(tabla[i+2][tres])).replace(',', '.'));
                    
                    jLabelExcentoDiferencia.setText(df.format(Double.parseDouble(tabla[i+3][uno])).replace(',', '.'));
                    jLabelMinimoDiferencia.setText(df.format(Double.parseDouble(tabla[i+3][dos])).replace(',', '.'));
                    jLabelBasicoDiferencia.setText(df.format(Double.parseDouble(tabla[i+3][tres])).replace(',', '.'));
                    
                    jLabelExcentoPorcentaje.setText(df.format(Double.parseDouble(tabla[i+4][uno])).replace(',', '.') + "%");
                    jLabelMinimoPorcentaje.setText(df.format(Double.parseDouble(tabla[i+4][dos])).replace(',', '.') + "%");
                    jLabelBasicoPorcentaje.setText(df.format(Double.parseDouble(tabla[i+4][tres])).replace(',', '.') + "%");
                    
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
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());         }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaVerificarFacturacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaVerificarFacturacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaVerificarFacturacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaVerificarFacturacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaVerificarFacturacion dialog = new VentanaVerificarFacturacion(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JButton jButtonVerificar;
    private javax.swing.JComboBox jComboBoxReparto;
    private com.toedter.calendar.JDateChooser jDateChooserDesdeFecha;
    private com.toedter.calendar.JDateChooser jDateChooserHastaFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelBasicoDiferencia;
    private javax.swing.JLabel jLabelBasicoManuales;
    private javax.swing.JLabel jLabelBasicoPorcentaje;
    private javax.swing.JLabel jLabelBasicoProrrateo;
    private javax.swing.JLabel jLabelBasicoTotal;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelExcentoDiferencia;
    private javax.swing.JLabel jLabelExcentoManuales;
    private javax.swing.JLabel jLabelExcentoPorcentaje;
    private javax.swing.JLabel jLabelExcentoProrrateo;
    private javax.swing.JLabel jLabelExcentoTotal;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelHastaFechaIncorrecta;
    private javax.swing.JLabel jLabelMinimoDiferencia;
    private javax.swing.JLabel jLabelMinimoManuales;
    private javax.swing.JLabel jLabelMinimoPorcentaje;
    private javax.swing.JLabel jLabelMinimoProrrateo;
    private javax.swing.JLabel jLabelMinimoTotal;
    private javax.swing.JLabel jLabelTitulo1;
    private javax.swing.JLabel jLabelTitulo2;
    private javax.swing.JLabel jLabelTitulo3;
    private javax.swing.JLabel jLabelTitulo4;
    private javax.swing.JLabel jLabelTitulo5;
    private javax.swing.JLabel jLabelTitulo6;
    private javax.swing.JPanel jPanelDiferencia;
    private javax.swing.JPanel jPanelDiferencia1;
    private javax.swing.JPanel jPanelManuales;
    private javax.swing.JPanel jPanelProrrateo;
    private javax.swing.JPanel jPanelTotal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableResultado;
    // End of variables declaration//GEN-END:variables
}
