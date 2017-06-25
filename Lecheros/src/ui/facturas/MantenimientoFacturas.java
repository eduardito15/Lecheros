/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.facturas;

import dominio.Cliente;
import dominio.Factura;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.hibernate.HibernateException;
import sistema.SistemaFacturas;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.clientes.VentanaBuscadorClienteFactura;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class MantenimientoFacturas extends javax.swing.JFrame {

    private final SistemaMantenimiento sis;
    private final SistemaFacturas sisFacturas;
    private List<Factura> facturas;
    private DefaultTableModel modelo;

    private DecimalFormat df;
    private Cliente cliente;

    private boolean yaBusco;
    private boolean mostrarMensajeFechaIncorrecta = true;

    /**
     * Creates new form MantenimientoFacturas
     */
    public MantenimientoFacturas(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        sis = SistemaMantenimiento.getInstance();
        sisFacturas = SistemaFacturas.getInstance();
        jComboBoxReparto.addItem("");
        List<Reparto> repartos = sis.devolverRepartos();
        for (Reparto c : repartos) {
            jComboBoxReparto.addItem(c);
        }

        facturas = new ArrayList<>();
        inicializarTablaFacturas();
        agregarEnterCamposFecha();
        df = new DecimalFormat("0.00");
        Calendar cal = Calendar.getInstance();
        int mes = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        Calendar calPrimerDiaMes = Calendar.getInstance();
        calPrimerDiaMes.set(year, mes, 1);
        jDateChooserDesdeFecha.setDate(calPrimerDiaMes.getTime());
        jDateChooserHastaFecha.setDate(cal.getTime());
        yaBusco = false;
        jLabelEspera.setVisible(false);
        jTextFieldNumero.requestFocus();
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
                if (e.getKeyCode() == KeyEvent.VK_F5) {
                    jButtonBuscar.doClick();
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
                    jButtonBuscar.requestFocus();
                }
                if (e.getKeyCode() == KeyEvent.VK_F5) {
                    jButtonBuscar.doClick();
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

    public final void inicializarTablaFacturas() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Tipo Documento");
        modelo.addColumn("Cliente");
        modelo.addColumn("Numero");
        modelo.addColumn("Fecha");
        modelo.addColumn("Reparto");
        modelo.addColumn("SubTotal");
        modelo.addColumn("Iva Minimo");
        modelo.addColumn("Iva Basico");
        modelo.addColumn("Total");
        jTableFacturas.setModel(modelo);
        TableColumn column = null;
        for (int i = 0; i < 9; i++) {
            column = jTableFacturas.getColumnModel().getColumn(i);
            if (i == 0 || i == 1 || i == 4) {
                column.setPreferredWidth(120);
            } else {
                column.setPreferredWidth(50);
            }
        }
        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem verItem = new JMenuItem("Ver");
        verItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da ver en el menu con click derecho sobre la fila de la tabla
                IngresoFacturas vif = new IngresoFacturas(MantenimientoFacturas.this, false);
                vif.setAccion("Ver");
                Factura f = facturas.get(jTableFacturas.getSelectedRow());
                vif.setFactura(f);
                vif.setTipoDoc(f.getTipoDocumento());
                vif.setVisible(true);
            }
        });
        popupMenu.add(verItem);
        JMenuItem modificarItem = new JMenuItem("Modificar");
        modificarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da modificar en el menu con click derecho sobre la fila de la tabla
                try {
                    if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadIngresarFacturas)) {  
                        IngresoFacturas vif = new IngresoFacturas(MantenimientoFacturas.this, false);
                        vif.setAccion("Modificar");
                        Factura f = facturas.get(jTableFacturas.getSelectedRow());
                        vif.setFactura(f);
                        vif.setTipoDoc(f.getTipoDocumento());
                        vif.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(MantenimientoFacturas.this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(MantenimientoFacturas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        popupMenu.add(modificarItem);
        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        eliminarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da eliminar en el menu con click derecho sobre la fila de la tabla
                try {
                    if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadIngresarFacturas)) {  
                        Factura f = facturas.get(jTableFacturas.getSelectedRow());
                        int resp = JOptionPane.showConfirmDialog(MantenimientoFacturas.this, "Seguro que quiere eliminar la factura con el numero " + f.getNumero() + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (resp == JOptionPane.YES_OPTION) {
                            try {
                                if (sisFacturas.eliminarFactura(f)) {
                                    JOptionPane.showMessageDialog(MantenimientoFacturas.this, "La factura se elimino correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                                    jButtonBuscar.doClick();
                                }
                            } catch (HibernateException he) {
                                JOptionPane.showMessageDialog(MantenimientoFacturas.this, "Error al eliminar la factura." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                            } catch (Exception exp) {
                                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                                JOptionPane.showMessageDialog(MantenimientoFacturas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(MantenimientoFacturas.this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(MantenimientoFacturas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        popupMenu.add(eliminarItem);
        JMenuItem imprimirItem = new JMenuItem("Imprimir");
        imprimirItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Factura f = facturas.get(jTableFacturas.getSelectedRow());
                try {
                    SistemaFacturas.getImpresion().imprimirFactura(f);
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(MantenimientoFacturas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        popupMenu.add(imprimirItem);
        jTableFacturas.setComponentPopupMenu(popupMenu);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabelTitulo = new javax.swing.JLabel();
        jRadioButtonManual = new javax.swing.JRadioButton();
        jRadioButtonProrrateo = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldNumero = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jDateChooserDesdeFecha = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jDateChooserHastaFecha = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxReparto = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldCliente = new javax.swing.JTextField();
        jButtonBuscar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jLabelHastaFechaIncorrecta = new javax.swing.JLabel();
        jLabelEspera = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableFacturas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mantenimiento Facturas");

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Facturas");

        buttonGroup1.add(jRadioButtonManual);
        jRadioButtonManual.setSelected(true);
        jRadioButtonManual.setText("Manual");
        jRadioButtonManual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jRadioButtonManualKeyPressed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonProrrateo);
        jRadioButtonProrrateo.setText("Prorrateo");
        jRadioButtonProrrateo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jRadioButtonProrrateoKeyPressed(evt);
            }
        });

        jLabel3.setText("Tipo:");

        jLabel1.setText("Numero:");

        jTextFieldNumero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNumeroKeyPressed(evt);
            }
        });

        jLabel7.setText("Desde Fecha:");

        jDateChooserDesdeFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserDesdeFecha.setMaxSelectableDate(new java.util.Date(4102455674000L));
        jDateChooserDesdeFecha.setMinSelectableDate(new java.util.Date(946699272000L));

        jLabel8.setText("Hasta Fecha:");

        jDateChooserHastaFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserHastaFecha.setMaxSelectableDate(new java.util.Date(4102455674000L));
        jDateChooserHastaFecha.setMinSelectableDate(new java.util.Date(946699318000L));

        jLabel4.setText("Reparto:");

        jComboBoxReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxRepartoKeyPressed(evt);
            }
        });

        jLabel6.setText("Cliente:");

        jTextFieldCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldClienteKeyPressed(evt);
            }
        });

        jButtonBuscar.setText("Buscar (F5)");
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

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabelHastaFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelHastaFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        jTableFacturas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Tipo Documento", "Cliente", "Numero", "Fecha", "Reparto", "SubTotal", "Iva Minimo", "Iva Basico", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableFacturas);

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
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jRadioButtonManual)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButtonProrrateo))
                            .addComponent(jTextFieldNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelEspera)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelFechaIncorrecta)
                                    .addComponent(jLabelHastaFechaIncorrecta))))
                        .addGap(0, 187, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(332, 332, 332)
                        .addComponent(jLabelTitulo)
                        .addGap(30, 30, 30)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonManual)
                    .addComponent(jRadioButtonProrrateo)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonBuscar)
                            .addComponent(jLabelFechaIncorrecta))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonSalir)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabelHastaFechaIncorrecta))))
                    .addComponent(jLabelEspera))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldNumeroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNumeroKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonBuscar.doClick();
        }
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jTextFieldNumeroKeyPressed

    private void jComboBoxRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxRepartoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonBuscar.requestFocus();
        }
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

    public void ponerVentanaEnEspera() {
        jLabelEspera.setVisible(true);
        jTextFieldNumero.setEnabled(false);
        jDateChooserDesdeFecha.setEnabled(false);
        jDateChooserHastaFecha.setEnabled(false);
        jComboBoxReparto.setEnabled(false);
        jTextFieldCliente.setEnabled(false);
        jButtonBuscar.setEnabled(false);
        jRadioButtonManual.setEnabled(false);
        jRadioButtonProrrateo.setEnabled(false);
    }

    public void sacarVentanaDeEspera() {
        jLabelEspera.setVisible(false);
        jTextFieldNumero.setEnabled(true);
        jDateChooserDesdeFecha.setEnabled(true);
        jDateChooserHastaFecha.setEnabled(true);
        jComboBoxReparto.setEnabled(true);
        jTextFieldCliente.setEnabled(true);
        jButtonBuscar.setEnabled(true);
        jRadioButtonManual.setEnabled(true);
        jRadioButtonProrrateo.setEnabled(true);
        jButtonBuscar.requestFocus();
    }

    private void jTextFieldClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldClienteKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jComboBoxReparto.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Para buscar un cliente debe seleccionar el reparto.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jComboBoxReparto.requestFocus();
            } else if ("".equals(jTextFieldCliente.getText().trim())) {
                //Es vacio. Le habro el buscador
                VentanaBuscadorClienteFactura vbc = new VentanaBuscadorClienteFactura(MantenimientoFacturas.this, true);
                vbc.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                vbc.setEsProrrateo(jRadioButtonProrrateo.isSelected());
                vbc.setVisible(true);
                if (vbc.getCliente() != null) {
                    this.cliente = vbc.getCliente();
                    jTextFieldCliente.setText(cliente.getNombre());
                    jButtonBuscar.requestFocus();
                } else {
                    jTextFieldCliente.requestFocus();
                    jTextFieldCliente.selectAll();
                }
            } else {
                //No es vacio. Busco la cadena en los rut de cliente, si conincide la muestro si no pregunto si quiere volver a ingresarlo o abrir el buscador de clientes.
                String fraccionDelRut = jTextFieldCliente.getText().trim();
                cliente = sis.devolverClientePorFraccionDelRut(fraccionDelRut);
                if (cliente != null) {
                    jTextFieldCliente.setText(cliente.getNombre());
                    jButtonBuscar.requestFocus();
                } else {
                    int resp = JOptionPane.showConfirmDialog(this, "No existe un cliente que contenga en el rut: " + fraccionDelRut + " . Desea abrir el buscador de clientes?  ", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resp == JOptionPane.YES_OPTION) {
                        VentanaBuscadorClienteFactura vbc = new VentanaBuscadorClienteFactura(MantenimientoFacturas.this, true);
                        vbc.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                        vbc.setEsProrrateo(jRadioButtonProrrateo.isSelected());
                        vbc.setVisible(true);
                        if(vbc.getCliente() != null){
                            this.cliente = vbc.getCliente();
                            jTextFieldCliente.setText(cliente.getNombre());
                            jButtonBuscar.requestFocus();
                        } else {
                            jTextFieldCliente.requestFocus();
                            jTextFieldCliente.selectAll();
                        }
                    } else {
                        if (resp == JOptionPane.NO_OPTION) {
                            jTextFieldCliente.requestFocus();
                            jTextFieldCliente.selectAll();
                        }
                    }
                }
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jTextFieldClienteKeyPressed

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
        // TODO add your handling code here:
        yaBusco = true;
        inicializarTablaFacturas();
        facturas = new ArrayList<>();
        if ("".equals(jTextFieldNumero.getText().trim()) && jDateChooserDesdeFecha.getDate() == null && jDateChooserHastaFecha.getDate() == null && "".equals(jTextFieldCliente.getText().trim()) && jComboBoxReparto.getSelectedItem() == "") {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un filtro para la búsqueda.", "Información", JOptionPane.INFORMATION_MESSAGE);
            jTextFieldNumero.requestFocus();
            jTextFieldNumero.selectAll();
        }
        if (!"".equals(jTextFieldNumero.getText().trim())) {
            if (jComboBoxReparto.getSelectedItem() == "" && "".equals(jTextFieldCliente.getText())) {
                try {
                    //No es vacio el numero. Entonces busco por numero y son vacios todos los demas
                    facturas = sisFacturas.devolverFacturasPorNumero(Long.parseLong(jTextFieldNumero.getText().trim()), jRadioButtonManual.isSelected());
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(MantenimientoFacturas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
                for (Factura f : facturas) {
                    //Cargo la tabla de la interfaz
                    cargarFacturaEnTabla(f);
                }
            } else if (jComboBoxReparto.getSelectedItem() == "") {
                try {
                    //Si es vacio el de reparto es por que no es vacio el de cliente. Busco por numero y cliente
                    Factura f = sisFacturas.devolverFacturaPorNumeroYCliente(Long.parseLong(jTextFieldNumero.getText().trim()), cliente, jRadioButtonManual.isSelected());
                    cargarFacturaEnTabla(f);
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(MantenimientoFacturas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if ("".equals(jTextFieldCliente.getText().trim())) {
                try {
                    //Si entra aca es vacio el tipo de documento y no el reparto. Busco por numero y reparto.
                    facturas = sisFacturas.devolverFacturaPorNumeroYReparto(Long.parseLong(jTextFieldNumero.getText().trim()), (Reparto) jComboBoxReparto.getSelectedItem(), jRadioButtonManual.isSelected());
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(MantenimientoFacturas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
                for (Factura f : facturas) {
                    //Cargo la tabla de la interfaz
                    cargarFacturaEnTabla(f);
                }
            } else {
                try {
                    //Busco por los 3 numero, cliente y reparto
                    //Creo que no es necesario basta solo con los otros dos filtros.
                    Factura f = sisFacturas.devolverFacturaPorNumeroYCliente(Long.parseLong(jTextFieldNumero.getText().trim()), cliente, jRadioButtonManual.isSelected());
                    cargarFacturaEnTabla(f);
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(MantenimientoFacturas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else //Es vacio el numero. Entonces tiene que poner filtros de fecha. Verifico que los filtros de fecha estn con datos.
        if (jDateChooserDesdeFecha.getDate() == null || jDateChooserHastaFecha.getDate() == null) {
            //Alguna de las fechas es vacia
            JOptionPane.showMessageDialog(this, "Si no se ingresa numero para la búsqueda, se debe ingresar al menos un rango de fechas.", "Información", JOptionPane.INFORMATION_MESSAGE);
            jDateChooserDesdeFecha.requestFocusInWindow();
        } else //El numero es vacio, pero hay rango de fecha para la busqueda.
        //Calendar fechaDesdeC = dateChooserComboDesdeFecha.getSelectedDate();
        //fechaDesdeC.add(Calendar.DATE, -1);
        //Calendar fechaHastaC = dateChooserComboHastaFecha.getSelectedDate();
        if (jComboBoxReparto.getSelectedItem() == "" && "".equals(jTextFieldCliente.getText().trim())) {
            //Busco solo por el rango de fechas.
            ponerVentanaEnEspera();
            Thread worker = new Thread() {
                public void run() {
                    try {
                        facturas = sisFacturas.devolverFacturasEntreFechas(jDateChooserDesdeFecha.getDate(), jDateChooserHastaFecha.getDate(), jRadioButtonManual.isSelected());
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(MantenimientoFacturas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            sacarVentanaDeEspera();
                            for (Factura f : facturas) {
                                //Cargo la tabla de la interfaz
                                cargarFacturaEnTabla(f);
                            }
                        }
                    });
                }
            };
            worker.start();
        } else if (jComboBoxReparto.getSelectedItem() == "") {
            //Si es vacio el de reparto es por que no es vacio el de cliente. Busco por fechas y cliente
            ponerVentanaEnEspera();
            Thread worker = new Thread() {
                public void run() {
                    try {
                        facturas = sisFacturas.devolverFacturasEntreFechasYCliente(jDateChooserDesdeFecha.getDate(), jDateChooserHastaFecha.getDate(), cliente, jRadioButtonManual.isSelected());
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(MantenimientoFacturas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            sacarVentanaDeEspera();
                            for (Factura f : facturas) {
                                //Cargo la tabla de la interfaz
                                cargarFacturaEnTabla(f);
                            }
                        }
                    });
                }
            };
            worker.start();
        } else if ("".equals(jTextFieldCliente.getText().trim())) {
            //Si entra aca es vacio el tipo de documento y no el reparto. Busco por fechas y reparto.
            ponerVentanaEnEspera();
            Thread worker = new Thread() {
                public void run() {
                    try {
                        facturas = sisFacturas.devolverFacturasEntreFechasYReparto(jDateChooserDesdeFecha.getDate(), jDateChooserHastaFecha.getDate(), (Reparto) jComboBoxReparto.getSelectedItem(), jRadioButtonManual.isSelected());
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(MantenimientoFacturas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            sacarVentanaDeEspera();
                            for (Factura f : facturas) {
                                //Cargo la tabla de la interfaz
                                cargarFacturaEnTabla(f);
                            }
                        }
                    });
                }
            };
            worker.start();
        } else {
            //Busco por los 4 las fechas el reparto y cliente
            ponerVentanaEnEspera();
            Thread worker = new Thread() {
                public void run() {
                    try {
                        facturas = sisFacturas.devolverFacturasEntreFechasYClienteYRaparto(jDateChooserDesdeFecha.getDate(), jDateChooserHastaFecha.getDate(), cliente, (Reparto) jComboBoxReparto.getSelectedItem(), jRadioButtonManual.isSelected());
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(MantenimientoFacturas.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            sacarVentanaDeEspera();
                            for (Factura f : facturas) {
                                //Cargo la tabla de la interfaz
                                cargarFacturaEnTabla(f);
                            }
                        }
                    });
                }
            };
            worker.start();
        }
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    private void jButtonBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonBuscarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonBuscar.doClick();
        }
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jButtonBuscarKeyPressed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jRadioButtonManualKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jRadioButtonManualKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jRadioButtonManualKeyPressed

    private void jRadioButtonProrrateoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jRadioButtonProrrateoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jRadioButtonProrrateoKeyPressed

    public void cargarFacturaEnTabla(Factura f) {
        Object[] object = new Object[9];
        object[0] = f.getTipoDocumento();
        object[1] = f.getCliente();
        object[2] = f.getNumero();
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        object[3] = formatter.format(f.getFecha());
        object[4] = f.getReparto();
        object[5] = df.format(f.getSubtotal()).replace(',', '.');
        object[6] = df.format(f.getTotalMinimo()).replace(',', '.');
        object[7] = df.format(f.getTotalBasico()).replace(',', '.');
        object[8] = df.format(f.getTotal()).replace(',', '.');
        modelo.addRow(object);
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
            java.util.logging.Logger.getLogger(MantenimientoFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MantenimientoFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MantenimientoFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MantenimientoFacturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MantenimientoFacturas dialog = new MantenimientoFacturas(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JComboBox jComboBoxReparto;
    private com.toedter.calendar.JDateChooser jDateChooserDesdeFecha;
    private com.toedter.calendar.JDateChooser jDateChooserHastaFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelHastaFechaIncorrecta;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JRadioButton jRadioButtonManual;
    private javax.swing.JRadioButton jRadioButtonProrrateo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableFacturas;
    private javax.swing.JTextField jTextFieldCliente;
    private javax.swing.JTextField jTextFieldNumero;
    // End of variables declaration//GEN-END:variables
}
