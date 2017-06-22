/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.liquidaciones;

import dominio.Camion;
import dominio.Chofer;
import dominio.ConfiguracionLiquidacion;
import dominio.FiadoChofer;
import dominio.Gasto;
import dominio.Inventario;
import dominio.Liquidacion;
import dominio.Reparto;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.Toolkit;
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
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import sistema.SistemaLiquidaciones;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class VentanaLiquidacionClafer1 extends javax.swing.JFrame {

    private final SistemaMantenimiento sisMantenimiento;
    private final SistemaLiquidaciones sisLiquidaciones;
    private Liquidacion liquidacion;

    private final DecimalFormat df;
    private boolean mostrarMensajeFechaIncorrecta = true;

    private boolean tocoVerCheques = false;
    private boolean tocoVerInventario = false;
    private boolean tocoVerGastos = false;
    private boolean tocoVerFiadoChofer = false;
    private boolean tocoVerRetenciones = false;
    private boolean tocoVerFiadoEmpresa = false;
    private boolean tocoVerInau = false;
    private boolean tocoVerAnep = false;

    /**
     * Creates new form VentanaLiquidacionClafer
     */
    public VentanaLiquidacionClafer1(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        sisMantenimiento = SistemaMantenimiento.getInstance();
        sisLiquidaciones = SistemaLiquidaciones.getInstance();
        inicializarMuestraLiquidacion();
        agregarEnterCampoFecha();
        jComboBoxReparto.addItem("");
        List<Reparto> repartos = sisMantenimiento.devolverRepartos();
        for (Reparto c : repartos) {
            jComboBoxReparto.addItem(c);
        }
        jComboBoxChoferes.addItem("");
        List<Chofer> choferes = sisMantenimiento.devolverChoferes();
        for (Chofer c : choferes) {
            jComboBoxChoferes.addItem(c);
        }
        jComboBoxCamiones.addItem("");
        List<Camion> camiones = sisMantenimiento.devolverCamiones();
        for (Camion c : camiones) {
            jComboBoxCamiones.addItem(c);
        }
        //AutoCompletion.enable(jComboBoxChoferes);
        jComboBoxReparto.requestFocus();

        df = new DecimalFormat("0.00");

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

    private void inicializarMuestraLiquidacion() {
        ConfiguracionLiquidacion cl = sisMantenimiento.devolverConfiguracionLiquidacion();
        if (!cl.isMostrarCamion()) {
            jLabelCamion.setVisible(false);
            jComboBoxCamiones.setVisible(false);
        }
        if (!cl.isMostrarEntregaCheques()) {
            jLabelEntregaCheques.setVisible(false);
            jTextFieldEntregaCheques.setVisible(false);
            jButtonVerCheques.setVisible(false);
        }
        if (!cl.isMostrarCorrecionDeDiferencia()) {
            jLabelCoreecionDeDiferencia.setVisible(false);
            jTextFieldCorrecionDeDiferencia.setVisible(false);
        }
        if (!cl.isMostrarFiadoClientesCobraChofer()) {
            jLabelFiadoChofer.setVisible(false);
            jTextFieldFiadoChofer.setVisible(false);
            jButtonVerFiadoChofer.setVisible(false);
        }
        if (!cl.isMostrarFiadoClientesCobraEmpresa()) {
            jLabelFiadoEmpresa.setVisible(false);
            jTextFieldFiadoEmpresa.setVisible(false);
            jButtonVerFiadoEmpresa.setVisible(false);
        }
        if (!cl.isMostrarInau()) {
            jLabelInau.setVisible(false);
            jTextFieldInau.setVisible(false);
            jButtonVerInau.setVisible(false);
        }
        if (!cl.isMostrarAnep()) {
            jLabelAnep.setVisible(false);
            jTextFieldAnep.setVisible(false);
            jButtonVerAnep.setVisible(false);
        }
        if (!cl.isRetenciones()) {
            jLabelRetenciones.setVisible(false);
            jTextFieldRetenciones.setVisible(false);
            jButtonVerRetenciones.setVisible(false);
        }

    }

    private void cargarLiquidacion() throws ParseException {
        try {
            habilitarEdicionCamposEntrega();
            //Calendar fechaLiq = dateChooserComboFecha.getSelectedDate();
            Date fechaLiq = jDateChooserFecha.getDate();
            Reparto repartoLiq = (Reparto) jComboBoxReparto.getSelectedItem();
            Chofer chofer = (Chofer) jComboBoxChoferes.getSelectedItem();
            liquidacion = sisLiquidaciones.devolverLiquidacion(fechaLiq, repartoLiq, chofer);
            if (jComboBoxCamiones.getSelectedIndex() == 0) {
                liquidacion.setCamion(null);
            } else {
                liquidacion.setCamion((Camion) jComboBoxCamiones.getSelectedItem());
            }
            //jTextFieldSaldoAnterior.setText(df.format(liquidacion.getSaldoAnterior()).replace(',', '.'));
            
            Inventario i = sisLiquidaciones.devolverUltimoInventario(liquidacion.getFecha(), liquidacion.getReparto());
            if(i!=null){
                jTextFieldInventarioAnterior.setText(df.format(i.getTotal()).replace(',', '.'));
            } else {
                jTextFieldInventarioAnterior.setText("0.00");
            }
            if (SistemaMantenimiento.getInstance().devolverConfiguracionLiquidacion().isMostrarFiadoClientesCobraChofer()) {
                FiadoChofer fc = sisLiquidaciones.devolverUltimoFiadoChofer(liquidacion.getFecha(), liquidacion.getReparto());
                if(fc!=null){
                    jTextFieldFiadoChoferAnterior.setText(df.format(fc.getTotal()).replace(',', '.'));
                } else {
                    jTextFieldFiadoChoferAnterior.setText("0.00");
                }
                
            }
            
            jTextFieldCompras.setText(df.format(liquidacion.getCompras()).replace(',', '.'));
            jTextFieldUtilidad.setText(df.format(liquidacion.getUtilidad()).replace(',', '.'));
            jTextFieldRemitoPinchadas.setText(df.format(liquidacion.getRemitoPinchadas()).replace(',', '.'));
            jTextFieldGastos.setText(df.format(liquidacion.getGastos()).replace(',', '.'));
            jTextFieldEntregaEfectivo.setText(df.format(liquidacion.getEntregaEfectivo()).replace(',', '.'));
            jTextFieldEntregaCheques.setText(df.format(liquidacion.getEntregaCheques()).replace(',', '.'));
            jTextFieldCorrecionDeDiferencia.setText(df.format(liquidacion.getCorrecionDeDiferencia()).replace(',', '.'));
            jTextFieldDeuda.setText(df.format(liquidacion.getDeuda()).replace(',', '.'));
            jTextFieldDiferencia.setText(df.format(liquidacion.getDiferencia()).replace(',', '.'));
            jTextFieldFiadoChofer.setText(df.format(liquidacion.getFiadoChofer()).replace(',', '.'));
            jTextFieldFiadoEmpresa.setText(df.format(liquidacion.getFiadoEmpresa()).replace(',', '.'));
            jTextFieldInau.setText(df.format(liquidacion.getInau()).replace(',', '.'));
            jTextFieldAnep.setText(df.format(liquidacion.getAnep()).replace(',', '.'));
            jTextFieldInventario.setText(df.format(liquidacion.getInventario()).replace(',', '.'));
            jTextFieldRetenciones.setText(df.format(liquidacion.getRetenciones()).replace(',', '.'));
        } catch (Exception exp) {
            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
            JOptionPane.showMessageDialog(VentanaLiquidacionClafer1.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarLiquidacionExistente(Chofer chofer) throws ParseException {
        try {
            ConfiguracionLiquidacion cl = sisMantenimiento.devolverConfiguracionLiquidacion();
            //Calendar fechaLiq = dateChooserComboFecha.getSelectedDate();
            Date fechaLiq = jDateChooserFecha.getDate();
            Reparto repartoLiq = (Reparto) jComboBoxReparto.getSelectedItem();
            liquidacion = sisLiquidaciones.devolverLiquidacion(fechaLiq, repartoLiq, chofer);
            List<Chofer> choferes = sisMantenimiento.devolverChoferes();
            for (int i = 0; i < choferes.size(); i++) {
                Chofer c = choferes.get(i);
                if (c.getNombre().equals(chofer.getNombre())) {
                    jComboBoxChoferes.setSelectedIndex(i + 1);
                }
            }
            if (cl.isMostrarCamion()) {
                int indiceCamion = this.devolverIndiceCamion(liquidacion.getCamion());
                jComboBoxCamiones.setSelectedIndex(indiceCamion);
            }
            //jTextFieldSaldoAnterior.setText(df.format(liquidacion.getSaldoAnterior()).replace(',',
            Inventario i = sisLiquidaciones.devolverUltimoInventario(liquidacion.getFecha(), liquidacion.getReparto());
            if(i!=null){
                jTextFieldInventarioAnterior.setText(df.format(i.getTotal()).replace(',', '.'));
            } else {
                jTextFieldInventarioAnterior.setText("0.00");
            }
            if (SistemaMantenimiento.getInstance().devolverConfiguracionLiquidacion().isMostrarFiadoClientesCobraChofer()) {
                FiadoChofer fc = sisLiquidaciones.devolverUltimoFiadoChofer(liquidacion.getFecha(), liquidacion.getReparto());
                if(fc!=null){
                    jTextFieldFiadoChoferAnterior.setText(df.format(fc.getTotal()).replace(',', '.'));
                } else {
                    jTextFieldFiadoChoferAnterior.setText("0.00");
                }
                
            }
            jTextFieldCompras.setText(df.format(liquidacion.getCompras()).replace(',', '.'));
            jTextFieldUtilidad.setText(df.format(liquidacion.getUtilidad()).replace(',', '.'));
            jTextFieldRemitoPinchadas.setText(df.format(liquidacion.getRemitoPinchadas()).replace(',', '.'));
            jTextFieldGastos.setText(df.format(liquidacion.getGastos()).replace(',', '.'));
            jTextFieldEntregaEfectivo.setText(df.format(liquidacion.getEntregaEfectivo()).replace(',', '.'));
            jTextFieldEntregaCheques.setText(df.format(liquidacion.getEntregaCheques()).replace(',', '.'));
            jTextFieldCorrecionDeDiferencia.setText(df.format(liquidacion.getCorrecionDeDiferencia()).replace(',', '.'));
            jTextFieldDeuda.setText(df.format(liquidacion.getDeuda()).replace(',', '.'));
            jTextFieldDiferencia.setText(df.format(liquidacion.getDiferencia()).replace(',', '.'));
            jTextFieldFiadoChofer.setText(df.format(liquidacion.getFiadoChofer()).replace(',', '.'));
            jTextFieldFiadoEmpresa.setText(df.format(liquidacion.getFiadoEmpresa()).replace(',', '.'));
            jTextFieldInau.setText(df.format(liquidacion.getInau()).replace(',', '.'));
            jTextFieldAnep.setText(df.format(liquidacion.getAnep()).replace(',', '.'));
            jTextFieldInventario.setText(df.format(liquidacion.getInventario()).replace(',', '.'));
            jTextFieldRetenciones.setText(df.format(liquidacion.getRetenciones()).replace(',', '.'));
        } catch (Exception exp) {
            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
            JOptionPane.showMessageDialog(VentanaLiquidacionClafer1.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int devolverIndiceCamion(Camion camion) {
        List<Camion> camiones = sisMantenimiento.devolverCamiones();
        for (int i = 0; i < camiones.size(); i++) {
            Camion c = camiones.get(i);
            if (c.equals(camion)) {
                return i + 1;
            }
        }
        return 0;
    }

    private void calcularLiquidacion() throws ParseException, NumberFormatException {

        try {
            liquidacion.setEntregaEfectivo(Double.parseDouble(jTextFieldEntregaEfectivo.getText().trim()));
            
            if (jTextFieldCorrecionDeDiferencia.isVisible()) {
                liquidacion.setCorrecionDeDiferencia(Double.parseDouble(jTextFieldCorrecionDeDiferencia.getText().trim()));
            }
            sisLiquidaciones.calcularLiquidacion(liquidacion);
            jTextFieldDeuda.setText(df.format(liquidacion.getDeuda()).replace(',', '.'));
            jTextFieldInventario.setText(df.format(liquidacion.getInventario()).replace(',', '.'));
            if (jTextFieldFiadoChofer.isVisible()) {
                jTextFieldFiadoChofer.setText(df.format(liquidacion.getFiadoChofer()).replace(',', '.'));
            }
            if (jTextFieldFiadoEmpresa.isVisible()) {
                jTextFieldFiadoEmpresa.setText(df.format(liquidacion.getFiadoEmpresa()).replace(',', '.'));
            }
            if (jTextFieldInau.isVisible()) {
                jTextFieldInau.setText(df.format(liquidacion.getInau()).replace(',', '.'));
            }
            if (jTextFieldAnep.isVisible()) {
                jTextFieldAnep.setText(df.format(liquidacion.getAnep()).replace(',', '.'));
            }
            if (jTextFieldEntregaCheques.isVisible()) {
                jTextFieldEntregaCheques.setText(df.format(liquidacion.getEntregaCheques()).replace(',', '.'));
            }
            if (jTextFieldRetenciones.isVisible()) {
                jTextFieldRetenciones.setText(df.format(liquidacion.getRetenciones()).replace(',', '.'));
            }
            jTextFieldDiferencia.setText(df.format(liquidacion.getDiferencia()).replace(',', '.'));
        } catch (Exception exp) {
            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
            JOptionPane.showMessageDialog(VentanaLiquidacionClafer1.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
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

        jLabelTitulo = new javax.swing.JLabel();
        jComboBoxCamiones = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jDateChooserFecha = new com.toedter.calendar.JDateChooser();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jButtonImprimir = new javax.swing.JButton();
        jButtonGuardar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxReparto = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxChoferes = new javax.swing.JComboBox();
        jLabelCamion = new javax.swing.JLabel();
        jLabelTitulo1 = new javax.swing.JLabel();
        jLabelTitulo2 = new javax.swing.JLabel();
        jButtonVerCompras = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldCompras = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldUtilidad = new javax.swing.JTextField();
        jTextFieldRemitoPinchadas = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldGastos = new javax.swing.JTextField();
        jButtonVerGastos = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldEntregaEfectivo = new javax.swing.JTextField();
        jLabelEntregaCheques = new javax.swing.JLabel();
        jTextFieldEntregaCheques = new javax.swing.JTextField();
        jButtonVerCheques = new javax.swing.JButton();
        jLabelRemitoPinchadas = new javax.swing.JLabel();
        jLabelCoreecionDeDiferencia = new javax.swing.JLabel();
        jTextFieldCorrecionDeDiferencia = new javax.swing.JTextField();
        jLabelRetenciones = new javax.swing.JLabel();
        jTextFieldRetenciones = new javax.swing.JTextField();
        jButtonVerRetenciones = new javax.swing.JButton();
        jLabelDeuda = new javax.swing.JLabel();
        jTextFieldDeuda = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldInventario = new javax.swing.JTextField();
        jButtonVerInventario = new javax.swing.JButton();
        jLabelFiadoChofer = new javax.swing.JLabel();
        jTextFieldFiadoChofer = new javax.swing.JTextField();
        jButtonVerFiadoChofer = new javax.swing.JButton();
        jLabelFiadoEmpresa = new javax.swing.JLabel();
        jTextFieldFiadoEmpresa = new javax.swing.JTextField();
        jButtonVerFiadoEmpresa = new javax.swing.JButton();
        jLabelInau = new javax.swing.JLabel();
        jTextFieldInau = new javax.swing.JTextField();
        jButtonVerInau = new javax.swing.JButton();
        jLabelAnep = new javax.swing.JLabel();
        jTextFieldAnep = new javax.swing.JTextField();
        jButtonVerAnep = new javax.swing.JButton();
        jLabelDiderencia = new javax.swing.JLabel();
        jTextFieldDiferencia = new javax.swing.JTextField();
        jTextFieldInventarioAnterior = new javax.swing.JTextField();
        jTextFieldFiadoChoferAnterior = new javax.swing.JTextField();
        jButtonVerInventario1 = new javax.swing.JButton();
        jButtonVerFiadoChofer1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabelFiadoChofer1 = new javax.swing.JLabel();
        jLabelRepartoCompuesto = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Liquidación");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Liquidación");

        jComboBoxCamiones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxCamionesKeyPressed(evt);
            }
        });

        jLabel1.setText("Fecha:");

        jDateChooserFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserFecha.setMaxSelectableDate(new java.util.Date(4102455690000L));
        jDateChooserFecha.setMinSelectableDate(new java.util.Date(946699264000L));

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jButtonImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/printer_icon_small27772.jpg"))); // NOI18N
        jButtonImprimir.setText("Imprimir");
        jButtonImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImprimirActionPerformed(evt);
            }
        });

        jButtonGuardar.setText("Guardar");
        jButtonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarActionPerformed(evt);
            }
        });
        jButtonGuardar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonGuardarKeyPressed(evt);
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

        jLabel2.setText("Reparto:");

        jComboBoxReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxRepartoKeyPressed(evt);
            }
        });

        jLabel3.setText("Chofer:");

        jComboBoxChoferes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxChoferesKeyPressed(evt);
            }
        });

        jLabelCamion.setText("Camion:");

        jLabelTitulo1.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelTitulo1.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo1.setText("Suma");

        jLabelTitulo2.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelTitulo2.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo2.setText("Resta");

        jButtonVerCompras.setText("Ver Compras");
        jButtonVerCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerComprasActionPerformed(evt);
            }
        });

        jLabel5.setText("Compras: ");

        jTextFieldCompras.setEditable(false);

        jLabel6.setText("Utilidad:");

        jTextFieldUtilidad.setEditable(false);

        jTextFieldRemitoPinchadas.setEditable(false);

        jLabel7.setText("Gastos:");

        jTextFieldGastos.setEditable(false);

        jButtonVerGastos.setText("Ver Gastos");
        jButtonVerGastos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerGastosActionPerformed(evt);
            }
        });

        jLabel8.setText("Entrega Efectivo:");

        jTextFieldEntregaEfectivo.setEditable(false);
        jTextFieldEntregaEfectivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldEntregaEfectivoKeyPressed(evt);
            }
        });

        jLabelEntregaCheques.setText("Entrega Cheques:");

        jTextFieldEntregaCheques.setEditable(false);

        jButtonVerCheques.setText("Ver Cheques");
        jButtonVerCheques.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerChequesActionPerformed(evt);
            }
        });

        jLabelRemitoPinchadas.setText("Remito Pinchadas:");

        jLabelCoreecionDeDiferencia.setText("Correccion de Diferencia:");

        jTextFieldCorrecionDeDiferencia.setEditable(false);
        jTextFieldCorrecionDeDiferencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCorrecionDeDiferenciaKeyPressed(evt);
            }
        });

        jLabelRetenciones.setText("Retenciones:");

        jTextFieldRetenciones.setEditable(false);

        jButtonVerRetenciones.setText("Ver Retenciones");
        jButtonVerRetenciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerRetencionesActionPerformed(evt);
            }
        });

        jLabelDeuda.setText("Deuda:");

        jTextFieldDeuda.setEditable(false);

        jLabel12.setText("Inventario:");

        jTextFieldInventario.setEditable(false);

        jButtonVerInventario.setText("Ver Inventario");
        jButtonVerInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerInventarioActionPerformed(evt);
            }
        });

        jLabelFiadoChofer.setText("Fiado Chofer:");

        jTextFieldFiadoChofer.setEditable(false);

        jButtonVerFiadoChofer.setText("Ver Fiado Chofer");
        jButtonVerFiadoChofer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerFiadoChoferActionPerformed(evt);
            }
        });

        jLabelFiadoEmpresa.setText("Fiado Empresa:");

        jTextFieldFiadoEmpresa.setEditable(false);

        jButtonVerFiadoEmpresa.setText("Ver Fiado Empresa");
        jButtonVerFiadoEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerFiadoEmpresaActionPerformed(evt);
            }
        });

        jLabelInau.setText("Inau:");

        jTextFieldInau.setEditable(false);
        jTextFieldInau.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldInauKeyPressed(evt);
            }
        });

        jButtonVerInau.setText("Ver Inau");
        jButtonVerInau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerInauActionPerformed(evt);
            }
        });

        jLabelAnep.setText("Anep:");

        jTextFieldAnep.setEditable(false);

        jButtonVerAnep.setText("Ver Anep");
        jButtonVerAnep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerAnepActionPerformed(evt);
            }
        });

        jLabelDiderencia.setText("Diferencia:");

        jTextFieldDiferencia.setEditable(false);

        jTextFieldInventarioAnterior.setEditable(false);

        jTextFieldFiadoChoferAnterior.setEditable(false);

        jButtonVerInventario1.setText("Ver Inventario");
        jButtonVerInventario1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerInventario1ActionPerformed(evt);
            }
        });

        jButtonVerFiadoChofer1.setText("Ver Fiado Chofer");
        jButtonVerFiadoChofer1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerFiadoChofer1ActionPerformed(evt);
            }
        });

        jLabel9.setText("Inventario:");

        jLabelFiadoChofer1.setText("Fiado Chofer:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jLabel9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelDeuda, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelFiadoChofer1, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabelCamion)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jComboBoxReparto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jComboBoxChoferes, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jComboBoxCamiones, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelRepartoCompuesto))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelFechaIncorrecta)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 178, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelTitulo1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelTitulo2)
                                .addGap(106, 106, 106))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jTextFieldInventarioAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jButtonVerInventario1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jTextFieldCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jButtonVerCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jTextFieldFiadoChoferAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jButtonVerFiadoChofer1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(72, 72, 72)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabelRemitoPinchadas, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelRetenciones, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelFiadoChofer, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelFiadoEmpresa, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelInau, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelCoreecionDeDiferencia, javax.swing.GroupLayout.Alignment.TRAILING)))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jTextFieldUtilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabelEntregaCheques))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGap(486, 486, 486)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButtonGuardar, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelAnep, javax.swing.GroupLayout.Alignment.TRAILING)))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelDiderencia)
                                        .addComponent(jTextFieldDeuda, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextFieldDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldAnep, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                            .addComponent(jTextFieldEntregaCheques)
                            .addComponent(jTextFieldRemitoPinchadas)
                            .addComponent(jTextFieldGastos)
                            .addComponent(jTextFieldEntregaEfectivo)
                            .addComponent(jTextFieldCorrecionDeDiferencia)
                            .addComponent(jTextFieldRetenciones)
                            .addComponent(jTextFieldInventario)
                            .addComponent(jTextFieldFiadoChofer)
                            .addComponent(jTextFieldFiadoEmpresa)
                            .addComponent(jTextFieldInau)
                            .addComponent(jButtonImprimir))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonVerRetenciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonVerCheques, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonVerGastos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonVerInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonVerFiadoChofer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonVerFiadoEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonVerInau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonVerAnep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(47, 47, 47))
            .addGroup(layout.createSequentialGroup()
                .addGap(393, 393, 393)
                .addComponent(jLabelTitulo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelFechaIncorrecta)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jButtonGuardar)
                    .addComponent(jButtonImprimir)
                    .addComponent(jLabelRepartoCompuesto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBoxChoferes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSalir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCamion)
                    .addComponent(jComboBoxCamiones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitulo2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelTitulo1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelRemitoPinchadas)
                    .addComponent(jTextFieldRemitoPinchadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldInventarioAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jButtonVerInventario1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextFieldGastos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonVerGastos)
                    .addComponent(jLabelFiadoChofer1)
                    .addComponent(jTextFieldFiadoChoferAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonVerFiadoChofer1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextFieldEntregaEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldCompras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonVerCompras))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldEntregaCheques, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelEntregaCheques)
                            .addComponent(jButtonVerCheques))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldCorrecionDeDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelCoreecionDeDiferencia))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldRetenciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonVerRetenciones)
                            .addComponent(jLabelRetenciones))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonVerInventario)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldFiadoChofer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonVerFiadoChofer)
                            .addComponent(jLabelFiadoChofer))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldFiadoEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonVerFiadoEmpresa)
                            .addComponent(jLabelFiadoEmpresa))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldInau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelInau)
                            .addComponent(jButtonVerInau))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldAnep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonVerAnep)
                            .addComponent(jLabelAnep)
                            .addComponent(jTextFieldDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDiderencia)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jTextFieldUtilidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelDeuda)
                            .addComponent(jTextFieldDeuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(94, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxCamionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxCamionesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jComboBoxCamiones.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Debes seleccionar un camion para la liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
                jComboBoxCamiones.requestFocus();
            } else {
                try {
                    Date fecha = new Date();
                    Date fechaSeleccionada = jDateChooserFecha.getDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaSeleccionadaS = sdf.format(fechaSeleccionada);
                    String fechaS = sdf.format(fecha);
                    if(fechaSeleccionadaS.equals(fechaS)){
                        if(SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadVerLiquidacionDelDia)){
                            cargarLiquidacion();
                        } else {
                            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                            this.dispose();
                        }
                        jTextFieldEntregaEfectivo.requestFocus();
                        jTextFieldEntregaEfectivo.selectAll();
                    } else {
                        if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadVerLiquidacionVieja)) {
                            cargarLiquidacion();
                        } else {
                            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                            this.dispose();
                        }
                    }
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Error al cargar la liquidacion. " + "\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                    SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                    JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }

            }

        }
    }//GEN-LAST:event_jComboBoxCamionesKeyPressed

    private void jButtonImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImprimirActionPerformed
        // TODO add your handling code here:
        if (liquidacion == null) {
            JOptionPane.showMessageDialog(this, "Para imprimir primero debe estar en una liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
            jComboBoxReparto.requestFocus();
        } else {
            Font fuenteNegrita = new Font("Dialog", Font.BOLD, 10);
            PrintJob pj;
            Graphics pagina;
            pj = Toolkit.getDefaultToolkit().getPrintJob(VentanaLiquidacionClafer1.this, "Liquidación", null);

            try {

                pagina = pj.getGraphics();

                pagina.setFont(fuenteNegrita);

                pagina.setColor(Color.black);

                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("dd-MM-yyyy");

                pagina.drawString("Liquidación Resumida", 60, 60);
                pagina.drawString("Chofer: " + liquidacion.getChofer(), 60, 72);
                pagina.drawString("Reparto: " + liquidacion.getReparto().getNombre(), 60, 84);
                pagina.drawString("Fecha: " + formatter.format(liquidacion.getFecha()), 60, 96);

                Font fuente = new Font("Dialog", Font.PLAIN, 10);
                pagina.setFont(fuente);

                pagina.drawString("Saldo Anterior: ", 60, 125);
                pagina.drawString("Compras: ", 60, 137);
                pagina.drawString("Utilidad: ", 60, 149);
                pagina.drawString("Remito Pinchadas", 60, 161);
                pagina.drawString("Gastos: ", 60, 173);
                pagina.drawString("Entrega Vendedor: ", 60, 185);
                pagina.drawString("Corrección de Diferencia: ", 60, 197);
                pagina.drawString("Fiado cobra la Empresa: ", 60, 209);
                pagina.drawString("Deuda: ", 60, 221);
                pagina.drawString("Inventario: ", 60, 233);
                pagina.drawString("Diferencia: ", 60, 245);

                pagina.drawString(df.format(liquidacion.getSaldoAnterior()).replace(',', '.'), 190, 125);
                pagina.drawString(df.format(liquidacion.getCompras()).replace(',', '.'), 190, 137);
                pagina.drawString(df.format(liquidacion.getUtilidad()).replace(',', '.'), 190, 149);
                pagina.drawString(df.format(liquidacion.getRemitoPinchadas()).replace(',', '.'), 190, 161);
                pagina.drawString(df.format(liquidacion.getGastos()), 190, 173);
                pagina.drawString(df.format(liquidacion.getEntregaEfectivo()).replace(',', '.'), 190, 185);
                pagina.drawString(df.format(liquidacion.getCorrecionDeDiferencia()).replace(',', '.'), 190, 197);
                pagina.drawString(df.format(liquidacion.getFiadoEmpresa()).replace(',', '.'), 190, 209);
                pagina.drawString(df.format(liquidacion.getDeuda()).replace(',', '.'), 190, 221);
                pagina.drawString(df.format(liquidacion.getInventario()).replace(',', '.'), 190, 233);
                pagina.setFont(fuenteNegrita);
                pagina.drawString(df.format(liquidacion.getDiferencia()).replace(',', '.'), 190, 245);
                pagina.setFont(fuente);

                pagina.dispose();

                pj.end();

            } catch (Exception e) {
                String stakTrace = util.Util.obtenerStackTraceEnString(e);
                SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);

                System.out.println("LA IMPRESION HA SIDO CANCELADA...");

            }
        }
    }//GEN-LAST:event_jButtonImprimirActionPerformed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        try {
            // TODO add your handling code here:
            sisLiquidaciones.guardarLiquidacion(liquidacion);
            JOptionPane.showMessageDialog(this, "Liquidación Guardada", "Información", JOptionPane.INFORMATION_MESSAGE);
            jDateChooserFecha.requestFocusInWindow();
            vaciarCampos();
            inHabilitarEdicionCamposEntrega();
        } catch (Exception exp) {
            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
            JOptionPane.showMessageDialog(VentanaLiquidacionClafer1.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    public void vaciarCampos() {
        jComboBoxCamiones.setSelectedIndex(0);
        jComboBoxChoferes.setSelectedItem(null);
        jTextFieldCompras.setText("");
        jTextFieldCorrecionDeDiferencia.setText("0");
        jTextFieldDeuda.setText("");
        jTextFieldDiferencia.setText("");
        jTextFieldEntregaCheques.setText("0");
        jTextFieldEntregaEfectivo.setText("0");
        jTextFieldFiadoChofer.setText("");
        jTextFieldFiadoEmpresa.setText("");
        jTextFieldGastos.setText("");
        jTextFieldInau.setText("");
        jTextFieldAnep.setText("");
        jTextFieldInventario.setText("");
        jTextFieldRemitoPinchadas.setText("");
        jTextFieldRetenciones.setText("0");
        //jTextFieldSaldoAnterior.setText("");
        jTextFieldUtilidad.setText("");

    }

    private void jButtonGuardarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonGuardarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonGuardar.doClick();
        }
    }//GEN-LAST:event_jButtonGuardarKeyPressed

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
            if (jComboBoxReparto.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Debes seleccionar un reparto para la liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
                jComboBoxReparto.requestFocus();
            } else {
                //Calendar fechaLiq = dateChooserComboFecha.getSelectedDate();
                Date fechaLiq = jDateChooserFecha.getDate();
                Reparto repartoLiq = (Reparto) jComboBoxReparto.getSelectedItem();
                Reparto repartoCompartido = sisLiquidaciones.tieneRepartoCompartido(repartoLiq);
                if(repartoCompartido == null) {
                    jLabelRepartoCompuesto.setVisible(false);
                } else {
                    jLabelRepartoCompuesto.setVisible(true);
                    jLabelRepartoCompuesto.setText( "Y " + repartoCompartido.getNombre());
                }
                try {
                    Chofer c = sisLiquidaciones.devolverChofer(fechaLiq, repartoLiq);
                    if (c != null) {
                        Date fecha = new Date();
                        Date fechaSeleccionada = jDateChooserFecha.getDate();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaSeleccionadaS = sdf.format(fechaSeleccionada);
                        String fechaS = sdf.format(fecha);
                        if (fechaSeleccionadaS.equals(fechaS)) {
                            if(SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadVerLiquidacionDelDia)){
                                cargarLiquidacionExistente(c);
                            } else {
                                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                                this.dispose();
                            }
                        } else {
                            if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadVerLiquidacionVieja)) {
                                cargarLiquidacionExistente(c);
                            } else {
                                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                                this.dispose();
                            }
                        } 
                    } else {
                        vaciarCampos();
                    }
                    jComboBoxChoferes.requestFocus();
                } catch (ParseException ex) {
                } catch (Exception ex) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                    SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                    JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

    private void jComboBoxChoferesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxChoferesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jComboBoxChoferes.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Debes seleccionar un chofer para la liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
                jComboBoxChoferes.requestFocus();
            } else if (jComboBoxCamiones.isVisible()) {
                jComboBoxCamiones.requestFocus();
            } else {
                try {
                    Date fecha = new Date();
                    Date fechaSeleccionada = jDateChooserFecha.getDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaSeleccionadaS = sdf.format(fechaSeleccionada);
                    String fechaS = sdf.format(fecha);
                    if(fechaSeleccionadaS.equals(fechaS)){
                        if(SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadVerLiquidacionDelDia)){
                            cargarLiquidacion();
                        } else {
                            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                            this.dispose();
                        }
                        jTextFieldEntregaEfectivo.requestFocus();
                        jTextFieldEntregaEfectivo.selectAll();
                    } else {
                        if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadVerLiquidacionVieja)) {
                            cargarLiquidacion();
                        } else {
                            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                            this.dispose();
                        }
                    } 
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Error al cargar la liquidacion. " + "\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                    SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                    JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }
    }//GEN-LAST:event_jComboBoxChoferesKeyPressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        if (tocoVerCheques || tocoVerGastos || tocoVerInventario || tocoVerFiadoChofer || tocoVerRetenciones || tocoVerFiadoEmpresa || tocoVerInau || tocoVerAnep) {
            try {
                calcularLiquidacion();
                sisLiquidaciones.guardarLiquidacion(liquidacion);
                tocoVerCheques = false;
                tocoVerGastos = false;
                tocoVerInventario = false;
                tocoVerFiadoChofer = false;
                tocoVerRetenciones = false;
                tocoVerFiadoEmpresa = false;
                tocoVerInau = false;
                tocoVerAnep = false;
            } catch (ParseException | NumberFormatException ex) {

            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_formWindowActivated

    private void jButtonVerComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerComprasActionPerformed
        // TODO add your handling code here:
        if (liquidacion == null) {
            JOptionPane.showMessageDialog(this, "Para ver compras primero debe estar en una liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
            jComboBoxReparto.requestFocus();
        } else {
            VerComprasDeLiquidacion vcdl = new VerComprasDeLiquidacion();
            vcdl.setL(liquidacion);
            vcdl.setVisible(true);
        }
    }//GEN-LAST:event_jButtonVerComprasActionPerformed

    private void jButtonVerGastosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerGastosActionPerformed
        if (liquidacion == null) {
            JOptionPane.showMessageDialog(this, "Para ver gastos primero debe estar en una liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
            jComboBoxReparto.requestFocus();
        } else {
            try {
                // TODO add your handling code here:
                Gasto g = sisLiquidaciones.devolverGastoParaFechaYReparto(liquidacion.getFecha(), liquidacion.getReparto());
                IngresoGasto ig = new IngresoGasto(this, true);
                ig.setGasto(g);
                ig.setFechaDeHoy(liquidacion.getFecha());
                ig.setVisible(true);
                tocoVerGastos = true;
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar el gasto. " + "\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonVerGastosActionPerformed

    private void jTextFieldEntregaEfectivoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldEntregaEfectivoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("".equals(jTextFieldEntregaEfectivo.getText().trim())) {
                jTextFieldEntregaEfectivo.setText("0");
            }
            if (jTextFieldCorrecionDeDiferencia.isVisible()) {
                jTextFieldCorrecionDeDiferencia.requestFocus();
                jTextFieldCorrecionDeDiferencia.selectAll();
            } else {
                try {
                    calcularLiquidacion();
                    sisLiquidaciones.guardarLiquidacion(liquidacion);
                    jButtonGuardar.requestFocus();
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Error al calcular la liquidacion. " + "\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(this, "Los campos para la cuenta de la liquidacion deben ser numericos. " + "\n" + ne.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    jTextFieldEntregaEfectivo.requestFocus();
                    jTextFieldEntregaEfectivo.selectAll();
                } catch (Exception ex) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                    SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                    JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }
    }//GEN-LAST:event_jTextFieldEntregaEfectivoKeyPressed

    private void jButtonVerChequesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerChequesActionPerformed
        // TODO add your handling code here:
        if (liquidacion == null) {
            JOptionPane.showMessageDialog(this, "Para ver cheques primero debe estar en una liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
            jComboBoxReparto.requestFocus();
        } else {
            // TODO add your handling code here:
            VerChequesDeLiquidacion vvcl = new VerChequesDeLiquidacion(this, true);
            vvcl.setL(liquidacion);
            vvcl.setVisible(true);
            tocoVerCheques = true;
            jButtonGuardar.requestFocus();
        }
    }//GEN-LAST:event_jButtonVerChequesActionPerformed

    private void jTextFieldCorrecionDeDiferenciaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCorrecionDeDiferenciaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("".equals(jTextFieldCorrecionDeDiferencia.getText().trim())) {
                jTextFieldCorrecionDeDiferencia.setText("0");
            }

            try {
                calcularLiquidacion();
                sisLiquidaciones.guardarLiquidacion(liquidacion);
                jButtonGuardar.requestFocus();
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Error al calcular la liquidacion. " + "\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ne) {
                JOptionPane.showMessageDialog(this, "Los campos para la cuenta de la liquidacion deben ser numericos. " + "\n" + ne.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                jTextFieldEntregaEfectivo.requestFocus();
                jTextFieldEntregaEfectivo.selectAll();
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_jTextFieldCorrecionDeDiferenciaKeyPressed

    private void jButtonVerRetencionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerRetencionesActionPerformed
        // TODO add your handling code here:
        if (liquidacion == null) {
            JOptionPane.showMessageDialog(this, "Para ver retenciones primero debe estar en una liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
            jComboBoxReparto.requestFocus();
        } else {
            // TODO add your handling code here:
            VerRetencionesDeLiquidacion vvrl = new VerRetencionesDeLiquidacion(this, true);
            vvrl.setL(liquidacion);
            vvrl.setVisible(true);
            tocoVerRetenciones = true;
            jButtonGuardar.requestFocus();
        }
    }//GEN-LAST:event_jButtonVerRetencionesActionPerformed

    private void jButtonVerInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerInventarioActionPerformed
        if (liquidacion == null) {
            JOptionPane.showMessageDialog(this, "Para ver inventario primero debe estar en una liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
            jComboBoxReparto.requestFocus();
        } else {
            try {
                // TODO add your handling code here:
                IngresoInventario vii = new IngresoInventario(this, true);
                Inventario i = sisLiquidaciones.devolverInventarioParaFechaYReparto(liquidacion.getFecha(), liquidacion.getReparto());
                vii.setInventario(i);
                vii.setFechaDeHoy(liquidacion.getFecha());
                vii.setVisible(true);
                tocoVerInventario = true;
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar el inventario." + "\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonVerInventarioActionPerformed

    private void jButtonVerFiadoChoferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerFiadoChoferActionPerformed
        // TODO add your handling code here:
        if (liquidacion == null) {
            JOptionPane.showMessageDialog(this, "Para ver fiado chofer primero debe estar en una liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
            jComboBoxReparto.requestFocus();
        } else {
            try {
                // TODO add your handling code here:
                IngresoFiadoChofer vifc = new IngresoFiadoChofer(this, true);
                FiadoChofer fc = sisLiquidaciones.devolverFiadoChoferParaFechaYReparto(liquidacion.getFecha(), liquidacion.getReparto());
                vifc.setFiadoChofer(fc);
                vifc.setFechaDeHoy(liquidacion.getFecha());
                vifc.setVisible(true);
                tocoVerFiadoChofer = true;
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar el fiado chofer." + "\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonVerFiadoChoferActionPerformed

    private void jButtonVerFiadoEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerFiadoEmpresaActionPerformed
        // TODO add your handling code here:
        if (liquidacion == null) {
            JOptionPane.showMessageDialog(this, "Para ver las facturas del fiado de la empresa primero debe estar en una liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
            jComboBoxReparto.requestFocus();
        } else {
            // TODO add your handling code here:
            VerFiadoEmpresaDeLiquidacion vfel = new VerFiadoEmpresaDeLiquidacion(this, true);
            vfel.setL(liquidacion);
            vfel.setVisible(true);
            tocoVerFiadoEmpresa = true;
            jButtonGuardar.requestFocus();
        }
    }//GEN-LAST:event_jButtonVerFiadoEmpresaActionPerformed

    private void jButtonVerInauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerInauActionPerformed
        // TODO add your handling code here:
        if (liquidacion == null) {
            JOptionPane.showMessageDialog(this, "Para ver inau primero debe estar en una liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
            jComboBoxReparto.requestFocus();
        } else {
            VerInauDeLiquidacion vvidl = new VerInauDeLiquidacion(this, true);
            vvidl.setL(liquidacion);
            vvidl.setVisible(true);
            tocoVerInau = true;
            jButtonGuardar.requestFocus();
        }
    }//GEN-LAST:event_jButtonVerInauActionPerformed

    private void jButtonVerAnepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerAnepActionPerformed
        // TODO add your handling code here:
        if (liquidacion == null) {
            JOptionPane.showMessageDialog(this, "Para ver anep primero debe estar en una liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
            jComboBoxReparto.requestFocus();
        } else {
            VerAnepDeLiquidacion vvadl = new VerAnepDeLiquidacion(this, true);
            vvadl.setL(liquidacion);
            vvadl.setVisible(true);
            tocoVerAnep = true;
            jButtonGuardar.requestFocus();
        }
    }//GEN-LAST:event_jButtonVerAnepActionPerformed

    private void jButtonVerInventario1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerInventario1ActionPerformed
        // TODO add your handling code here:
        if (liquidacion == null) {
            JOptionPane.showMessageDialog(this, "Para ver inventario primero debe estar en una liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                // TODO add your handling code here:
                IngresoInventario vii = new IngresoInventario(this, true);
                Inventario i = sisLiquidaciones.devolverUltimoInventario(liquidacion.getFecha(), liquidacion.getReparto());
                vii.setInventario(i);
                vii.setVisible(true);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar el inventario." + "\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonVerInventario1ActionPerformed

    private void jButtonVerFiadoChofer1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerFiadoChofer1ActionPerformed
        // TODO add your handling code here:
        if (liquidacion == null) {
            JOptionPane.showMessageDialog(this, "Para ver inventario primero debe estar en una liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                // TODO add your handling code here:
                IngresoFiadoChofer vfc = new IngresoFiadoChofer(this, true);
                FiadoChofer fc = sisLiquidaciones.devolverUltimoFiadoChofer(liquidacion.getFecha(), liquidacion.getReparto());
                vfc.setFiadoChofer(fc);
                vfc.setVisible(true);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar el inventario." + "\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonVerFiadoChofer1ActionPerformed

    private void jTextFieldInauKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldInauKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldInauKeyPressed

    private void habilitarEdicionCamposEntrega() {
        jTextFieldEntregaEfectivo.setEditable(true);
        if (jTextFieldCorrecionDeDiferencia.isVisible()) {
            jTextFieldCorrecionDeDiferencia.setEditable(true);
        }
    }

    private void inHabilitarEdicionCamposEntrega() {
        jTextFieldEntregaEfectivo.setEditable(false);
        if (jTextFieldCorrecionDeDiferencia.isVisible()) {
            jTextFieldCorrecionDeDiferencia.setEditable(false);
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
            java.util.logging.Logger.getLogger(VentanaLiquidacionClafer1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaLiquidacionClafer1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaLiquidacionClafer1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaLiquidacionClafer1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaLiquidacionClafer1 dialog = new VentanaLiquidacionClafer1(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonImprimir;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JButton jButtonVerAnep;
    private javax.swing.JButton jButtonVerCheques;
    private javax.swing.JButton jButtonVerCompras;
    private javax.swing.JButton jButtonVerFiadoChofer;
    private javax.swing.JButton jButtonVerFiadoChofer1;
    private javax.swing.JButton jButtonVerFiadoEmpresa;
    private javax.swing.JButton jButtonVerGastos;
    private javax.swing.JButton jButtonVerInau;
    private javax.swing.JButton jButtonVerInventario;
    private javax.swing.JButton jButtonVerInventario1;
    private javax.swing.JButton jButtonVerRetenciones;
    private javax.swing.JComboBox jComboBoxCamiones;
    private javax.swing.JComboBox jComboBoxChoferes;
    private javax.swing.JComboBox jComboBoxReparto;
    private com.toedter.calendar.JDateChooser jDateChooserFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAnep;
    private javax.swing.JLabel jLabelCamion;
    private javax.swing.JLabel jLabelCoreecionDeDiferencia;
    private javax.swing.JLabel jLabelDeuda;
    private javax.swing.JLabel jLabelDiderencia;
    private javax.swing.JLabel jLabelEntregaCheques;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelFiadoChofer;
    private javax.swing.JLabel jLabelFiadoChofer1;
    private javax.swing.JLabel jLabelFiadoEmpresa;
    private javax.swing.JLabel jLabelInau;
    private javax.swing.JLabel jLabelRemitoPinchadas;
    private javax.swing.JLabel jLabelRepartoCompuesto;
    private javax.swing.JLabel jLabelRetenciones;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTitulo1;
    private javax.swing.JLabel jLabelTitulo2;
    private javax.swing.JTextField jTextFieldAnep;
    private javax.swing.JTextField jTextFieldCompras;
    private javax.swing.JTextField jTextFieldCorrecionDeDiferencia;
    private javax.swing.JTextField jTextFieldDeuda;
    private javax.swing.JTextField jTextFieldDiferencia;
    private javax.swing.JTextField jTextFieldEntregaCheques;
    private javax.swing.JTextField jTextFieldEntregaEfectivo;
    private javax.swing.JTextField jTextFieldFiadoChofer;
    private javax.swing.JTextField jTextFieldFiadoChoferAnterior;
    private javax.swing.JTextField jTextFieldFiadoEmpresa;
    private javax.swing.JTextField jTextFieldGastos;
    private javax.swing.JTextField jTextFieldInau;
    private javax.swing.JTextField jTextFieldInventario;
    private javax.swing.JTextField jTextFieldInventarioAnterior;
    private javax.swing.JTextField jTextFieldRemitoPinchadas;
    private javax.swing.JTextField jTextFieldRetenciones;
    private javax.swing.JTextField jTextFieldUtilidad;
    // End of variables declaration//GEN-END:variables
}
