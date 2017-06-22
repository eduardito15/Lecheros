/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.liquidaciones;

import dominio.Camion;
import dominio.Chofer;
import dominio.ConfiguracionLiquidacion;
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
import javax.swing.SwingUtilities;
import lecheros.Lecheros;
import sistema.SistemaLiquidaciones;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class VentanaLiquidacionGiamo extends javax.swing.JFrame {

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
     * Creates new form VentanaLiquidacionDialog
     */
    public VentanaLiquidacionGiamo(java.awt.Frame parent, boolean modal) {
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
        /*jComboBoxCamiones.addItem("");
        List<Camion> camiones = sisMantenimiento.devolverCamiones();
        for (Camion c : camiones) {
            jComboBoxCamiones.addItem(c);
        }*/
        //AutoCompletion.enable(jComboBoxChoferes);
        jComboBoxReparto.requestFocus();

        df = new DecimalFormat("0.00");
        jButtonAbrirLiquidacion.setVisible(false);
        jButtonCerrarLiquidacion.setVisible(false);
        jLabelEspera.setVisible(false);
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

    private void inicializarMuestraLiquidacion() {
        ConfiguracionLiquidacion cl = sisMantenimiento.devolverConfiguracionLiquidacion();
        if (!cl.isMostrarCamion()) {
            //jLabelCamion.setVisible(false);
            //jComboBoxCamiones.setVisible(false);
        }
        if (!cl.isMostrarEntregaCheques()) {
            jLabelEntregaCheques.setVisible(false);
            jTextFieldEntregaCheques.setVisible(false);
            //jButtonVerCheques.setVisible(false);
        }
        if (!cl.isMostrarCorrecionDeDiferencia()) {
            jLabelCoreecionDeDiferencia.setVisible(false);
            jTextFieldCorrecionDeDiferencia.setVisible(false);
        }
        if (!cl.isMostrarFiadoClientesCobraChofer()) {
            //jLabelFiadoChofer.setVisible(false);
            //jTextFieldFiadoChofer.setVisible(false);
            //jButtonVerFiadoChofer.setVisible(false);
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
            
        }
        if (cl.isModoAcumulativo()) {
            
        }
    }

    private void cargarLiquidacion() throws ParseException {
        try {
            habilitarEdicionCamposEntrega();
            //Calendar fechaLiq = dateChooserComboFecha.getSelectedDate();
            
            /*if (jComboBoxCamiones.getSelectedIndex() == 0) {
                liquidacion.setCamion(null);
            } else {
                liquidacion.setCamion((Camion) jComboBoxCamiones.getSelectedItem());
            }*/
            liquidacion.setCamion(null);
            jTextFieldSaldoAnterior.setText(df.format(liquidacion.getSaldoAnterior()).replace(',', '.'));
            jTextFieldCompras.setText(df.format(liquidacion.getCompras()).replace(',', '.'));
            jTextFieldUtilidad.setText(df.format(liquidacion.getUtilidad()).replace(',', '.'));
            jTextFieldRemitoPinchadas.setText(df.format(liquidacion.getRemitoPinchadas()).replace(',', '.'));
            jTextFieldLiquidacion.setText(df.format(liquidacion.getUtilidad()-liquidacion.getRemitoPinchadas() - liquidacion.getGastos()).replace(',', '.'));
            jTextFieldGastos.setText(df.format(liquidacion.getGastos()).replace(',', '.'));
            jTextFieldEntregaEfectivo.setText(df.format(liquidacion.getEntregaEfectivo()).replace(',', '.'));
            jTextFieldEntregaCheques.setText(df.format(liquidacion.getEntregaCheques()).replace(',', '.'));
            jTextFieldCorrecionDeDiferencia.setText(df.format(liquidacion.getCorrecionDeDiferencia()).replace(',', '.'));
            jTextFieldDeuda.setText(df.format(liquidacion.getDeuda()).replace(',', '.'));
            jTextFieldDiferencia.setText(df.format(liquidacion.getDiferencia()).replace(',', '.'));
            //jTextFieldFiadoChofer.setText(df.format(liquidacion.getFiadoChofer()).replace(',', '.'));
            jTextFieldFiadoEmpresa.setText(df.format(liquidacion.getFiadoEmpresa()).replace(',', '.'));
            jTextFieldInau.setText(df.format(liquidacion.getInau()).replace(',', '.'));
            jTextFieldAnep.setText(df.format(liquidacion.getAnep()).replace(',', '.'));
            jTextFieldInventario.setText(df.format(liquidacion.getInventario()).replace(',', '.'));
            //jTextFieldRetenciones.setText(df.format(liquidacion.getRetenciones()).replace(',', '.'));
            if(liquidacion.getCerrada()) {
                jButtonAbrirLiquidacion.setVisible(true);
                jButtonCerrarLiquidacion.setVisible(false);
            } else {
                jButtonCerrarLiquidacion.setVisible(true); 
                jButtonAbrirLiquidacion.setVisible(false);
            }
        } catch (Exception exp) {
            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
            JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarLiquidacionExistente(Chofer chofer) throws ParseException {
        habilitarEdicionCamposEntrega();
        try {
            ConfiguracionLiquidacion cl = sisMantenimiento.devolverConfiguracionLiquidacion();
            //Calendar fechaLiq = dateChooserComboFecha.getSelectedDate();
            
            if(liquidacion.getCerrada()){ 
                if(SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadVerLiquidacionCerrada)) {
                    List<Chofer> choferes = sisMantenimiento.devolverChoferes();
                    for (int i = 0; i < choferes.size(); i++) {
                        Chofer c = choferes.get(i);
                        if (c.getNombre().equals(chofer.getNombre())) {
                            jComboBoxChoferes.setSelectedIndex(i + 1);
                        }
                    }
                    if (cl.isMostrarCamion()) {
                        //int indiceCamion = this.devolverIndiceCamion(liquidacion.getCamion());
                        //jComboBoxCamiones.setSelectedIndex(indiceCamion);
                    }
                    jTextFieldSaldoAnterior.setText(df.format(liquidacion.getSaldoAnterior()).replace(',', '.'));
                    jTextFieldCompras.setText(df.format(liquidacion.getCompras()).replace(',', '.'));
                    jTextFieldUtilidad.setText(df.format(liquidacion.getUtilidad()).replace(',', '.'));
                    jTextFieldRemitoPinchadas.setText(df.format(liquidacion.getRemitoPinchadas()).replace(',', '.'));
                    jTextFieldLiquidacion.setText(df.format(liquidacion.getUtilidad()-liquidacion.getRemitoPinchadas()-liquidacion.getGastos()).replace(',', '.'));
                    jTextFieldGastos.setText(df.format(liquidacion.getGastos()).replace(',', '.'));
                    jTextFieldEntregaEfectivo.setText(df.format(liquidacion.getEntregaEfectivo()).replace(',', '.'));
                    jTextFieldEntregaCheques.setText(df.format(liquidacion.getEntregaCheques()).replace(',', '.'));
                    jTextFieldCorrecionDeDiferencia.setText(df.format(liquidacion.getCorrecionDeDiferencia()).replace(',', '.'));
                    jTextFieldDeuda.setText(df.format(liquidacion.getDeuda()).replace(',', '.'));
                    jTextFieldDiferencia.setText(df.format(liquidacion.getDiferencia()).replace(',', '.'));
                    //jTextFieldFiadoChofer.setText(df.format(liquidacion.getFiadoChofer()).replace(',', '.'));
                    jTextFieldFiadoEmpresa.setText(df.format(liquidacion.getFiadoEmpresa()).replace(',', '.'));
                    jTextFieldInau.setText(df.format(liquidacion.getInau()).replace(',', '.'));
                    jTextFieldAnep.setText(df.format(liquidacion.getAnep()).replace(',', '.'));
                    jTextFieldInventario.setText(df.format(liquidacion.getInventario()).replace(',', '.'));
                    //jTextFieldRetenciones.setText(df.format(liquidacion.getRetenciones()).replace(',', '.'));
                    if(liquidacion.getCerrada()) {
                        jButtonAbrirLiquidacion.setVisible(true);
                        jButtonCerrarLiquidacion.setVisible(false);
                    } else {
                        jButtonCerrarLiquidacion.setVisible(true); 
                        jButtonAbrirLiquidacion.setVisible(false);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                }
            } else {
                    List<Chofer> choferes = sisMantenimiento.devolverChoferes();
                    for (int i = 0; i < choferes.size(); i++) {
                        Chofer c = choferes.get(i);
                        if (c.getNombre().equals(chofer.getNombre())) {
                            jComboBoxChoferes.setSelectedIndex(i + 1);
                        }
                    }
                    if (cl.isMostrarCamion()) {
                        //int indiceCamion = this.devolverIndiceCamion(liquidacion.getCamion());
                        //jComboBoxCamiones.setSelectedIndex(indiceCamion);
                    }
                    jTextFieldSaldoAnterior.setText(df.format(liquidacion.getSaldoAnterior()).replace(',', '.'));
                    jTextFieldCompras.setText(df.format(liquidacion.getCompras()).replace(',', '.'));
                    jTextFieldUtilidad.setText(df.format(liquidacion.getUtilidad()).replace(',', '.'));
                    jTextFieldRemitoPinchadas.setText(df.format(liquidacion.getRemitoPinchadas()).replace(',', '.'));
                    jTextFieldLiquidacion.setText(df.format(liquidacion.getUtilidad()-liquidacion.getRemitoPinchadas()-liquidacion.getGastos()).replace(',', '.'));
                    jTextFieldGastos.setText(df.format(liquidacion.getGastos()).replace(',', '.'));
                    jTextFieldEntregaEfectivo.setText(df.format(liquidacion.getEntregaEfectivo()).replace(',', '.'));
                    jTextFieldEntregaCheques.setText(df.format(liquidacion.getEntregaCheques()).replace(',', '.'));
                    jTextFieldCorrecionDeDiferencia.setText(df.format(liquidacion.getCorrecionDeDiferencia()).replace(',', '.'));
                    jTextFieldDeuda.setText(df.format(liquidacion.getDeuda()).replace(',', '.'));
                    jTextFieldDiferencia.setText(df.format(liquidacion.getDiferencia()).replace(',', '.'));
                    //jTextFieldFiadoChofer.setText(df.format(liquidacion.getFiadoChofer()).replace(',', '.'));
                    jTextFieldFiadoEmpresa.setText(df.format(liquidacion.getFiadoEmpresa()).replace(',', '.'));
                    jTextFieldInau.setText(df.format(liquidacion.getInau()).replace(',', '.'));
                    jTextFieldAnep.setText(df.format(liquidacion.getAnep()).replace(',', '.'));
                    jTextFieldInventario.setText(df.format(liquidacion.getInventario()).replace(',', '.'));
                    //jTextFieldRetenciones.setText(df.format(liquidacion.getRetenciones()).replace(',', '.'));
                    if(liquidacion.getCerrada()) {
                        jButtonAbrirLiquidacion.setVisible(true);
                        jButtonCerrarLiquidacion.setVisible(false);
                    } else {
                        jButtonCerrarLiquidacion.setVisible(true); 
                        jButtonAbrirLiquidacion.setVisible(false);
                    }
            }
        } catch (Exception exp) {
            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
            JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
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
            if(Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaGiamo)) {
                if("".equals(jTextFieldEntregaCheques.getText().trim())) {
                    liquidacion.setEntregaCheques(0);
                } else {
                    liquidacion.setEntregaCheques(Double.parseDouble(jTextFieldEntregaCheques.getText().trim()));
                }
            }
            
            if (jTextFieldCorrecionDeDiferencia.isVisible()) {
                liquidacion.setCorrecionDeDiferencia(Double.parseDouble(jTextFieldCorrecionDeDiferencia.getText().trim()));
            }
            sisLiquidaciones.calcularLiquidacion(liquidacion);
            jTextFieldDeuda.setText(df.format(liquidacion.getDeuda()).replace(',', '.'));
            jTextFieldInventario.setText(df.format(liquidacion.getInventario()).replace(',', '.'));
            /*if (jTextFieldFiadoChofer.isVisible()) {
                jTextFieldFiadoChofer.setText(df.format(liquidacion.getFiadoChofer()).replace(',', '.'));
            }*/
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
            /*if (jTextFieldRetenciones.isVisible()) {
                jTextFieldRetenciones.setText(df.format(liquidacion.getRetenciones()).replace(',', '.'));
            }*/
            jTextFieldDiferencia.setText(df.format(liquidacion.getDiferencia()).replace(',', '.'));
        } catch (Exception exp) {
            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
            JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
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

        jButtonGuardar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabelTitulo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxReparto = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxChoferes = new javax.swing.JComboBox();
        jTextFieldSaldoAnterior = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButtonVerCompras = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldCompras = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldUtilidad = new javax.swing.JTextField();
        jLabelRemitoPinchadas = new javax.swing.JLabel();
        jTextFieldRemitoPinchadas = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldGastos = new javax.swing.JTextField();
        jButtonVerGastos = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldEntregaEfectivo = new javax.swing.JTextField();
        jLabelEntregaCheques = new javax.swing.JLabel();
        jTextFieldEntregaCheques = new javax.swing.JTextField();
        jLabelCoreecionDeDiferencia = new javax.swing.JLabel();
        jTextFieldCorrecionDeDiferencia = new javax.swing.JTextField();
        jLabelDeuda = new javax.swing.JLabel();
        jTextFieldDeuda = new javax.swing.JTextField();
        jButtonVerInventario = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldInventario = new javax.swing.JTextField();
        jLabelFiadoEmpresa = new javax.swing.JLabel();
        jTextFieldFiadoEmpresa = new javax.swing.JTextField();
        jButtonVerFiadoEmpresa = new javax.swing.JButton();
        jLabelInau = new javax.swing.JLabel();
        jTextFieldInau = new javax.swing.JTextField();
        jButtonVerInau = new javax.swing.JButton();
        jLabelDiderencia = new javax.swing.JLabel();
        jTextFieldDiferencia = new javax.swing.JTextField();
        jLabelAnep = new javax.swing.JLabel();
        jTextFieldAnep = new javax.swing.JTextField();
        jButtonVerAnep = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jDateChooserFecha = new com.toedter.calendar.JDateChooser();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jButtonImprimir = new javax.swing.JButton();
        jButtonCerrarLiquidacion = new javax.swing.JButton();
        jButtonAbrirLiquidacion = new javax.swing.JButton();
        jTextFieldLiquidacion = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabelEspera = new javax.swing.JLabel();
        jTextFieldLiquidacion1 = new javax.swing.JTextField();
        jLabelCoreecionDeDiferencia1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Liquidación");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
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

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Liquidación");

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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jComboBoxChoferesKeyReleased(evt);
            }
        });

        jTextFieldSaldoAnterior.setEditable(false);

        jLabel4.setText("Saldo Anterior:");

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

        jLabelRemitoPinchadas.setText("Remito Pinchadas:");

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
        jTextFieldEntregaCheques.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldEntregaChequesKeyPressed(evt);
            }
        });

        jLabelCoreecionDeDiferencia.setText("                               Otro:");

        jTextFieldCorrecionDeDiferencia.setEditable(false);
        jTextFieldCorrecionDeDiferencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCorrecionDeDiferenciaKeyPressed(evt);
            }
        });

        jLabelDeuda.setText("Deuda:");

        jTextFieldDeuda.setEditable(false);

        jButtonVerInventario.setText("Ver Inventario");
        jButtonVerInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerInventarioActionPerformed(evt);
            }
        });

        jLabel12.setText("Inventario:");

        jTextFieldInventario.setEditable(false);

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

        jButtonVerInau.setText("Ver Inau");
        jButtonVerInau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerInauActionPerformed(evt);
            }
        });

        jLabelDiderencia.setText("Diferencia:");

        jTextFieldDiferencia.setEditable(false);

        jLabelAnep.setText("Anep:");

        jTextFieldAnep.setEditable(false);

        jButtonVerAnep.setText("Ver Anep");
        jButtonVerAnep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerAnepActionPerformed(evt);
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

        jButtonCerrarLiquidacion.setText("Cerrar");
        jButtonCerrarLiquidacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCerrarLiquidacionActionPerformed(evt);
            }
        });

        jButtonAbrirLiquidacion.setText("Abrir");
        jButtonAbrirLiquidacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAbrirLiquidacionActionPerformed(evt);
            }
        });

        jTextFieldLiquidacion.setEditable(false);

        jLabel9.setText("Liquidacion:");

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        jTextFieldLiquidacion1.setEditable(false);

        jLabelCoreecionDeDiferencia1.setText("                               Otro:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(437, 437, 437)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonImprimir))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabelRemitoPinchadas)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabelEntregaCheques)
                            .addComponent(jLabelDeuda)
                            .addComponent(jLabel12)
                            .addComponent(jLabelFiadoEmpresa)
                            .addComponent(jLabelInau)
                            .addComponent(jLabelDiderencia)
                            .addComponent(jLabelAnep)
                            .addComponent(jLabel4)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabelCoreecionDeDiferencia))
                            .addComponent(jLabel1)
                            .addComponent(jLabel9)
                            .addComponent(jLabelCoreecionDeDiferencia1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelTitulo)
                                    .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabelFechaIncorrecta))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldLiquidacion, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxReparto, javax.swing.GroupLayout.Alignment.LEADING, 0, 215, Short.MAX_VALUE)
                                    .addComponent(jComboBoxChoferes, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldSaldoAnterior, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldCompras, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldUtilidad)
                                    .addComponent(jTextFieldRemitoPinchadas, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldGastos, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldEntregaEfectivo, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldEntregaCheques, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldCorrecionDeDiferencia, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldDeuda, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldInventario, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldFiadoEmpresa, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldInau, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldDiferencia, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldAnep, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldLiquidacion1, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(143, 143, 143)
                                        .addComponent(jButtonCerrarLiquidacion)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonAbrirLiquidacion))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelEspera)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jButtonVerCompras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButtonVerGastos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButtonVerInau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButtonVerFiadoEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButtonVerAnep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButtonVerInventario, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)))))))))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabelTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelFechaIncorrecta)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jButtonGuardar)
                    .addComponent(jButtonImprimir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBoxChoferes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSalir)
                    .addComponent(jButtonCerrarLiquidacion)
                    .addComponent(jButtonAbrirLiquidacion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldSaldoAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldCompras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jButtonVerCompras))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldUtilidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldRemitoPinchadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelRemitoPinchadas))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldGastos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jButtonVerGastos))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldLiquidacion1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelCoreecionDeDiferencia1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldLiquidacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldEntregaEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldEntregaCheques, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelEntregaCheques))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldCorrecionDeDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelCoreecionDeDiferencia))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldDeuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDeuda))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(jButtonVerInventario))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldFiadoEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelFiadoEmpresa)
                            .addComponent(jButtonVerFiadoEmpresa))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldInau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelInau)
                            .addComponent(jButtonVerInau))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelAnep)
                            .addComponent(jTextFieldAnep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonVerAnep))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldDiferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDiderencia)))
                    .addComponent(jLabelEspera))
                .addContainerGap(55, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        try {
            // TODO add your handling code here:
            if(!liquidacion.getCerrada()) {
                sisLiquidaciones.guardarLiquidacion(liquidacion);
                JOptionPane.showMessageDialog(this, "Liquidación Guardada", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
            jDateChooserFecha.requestFocusInWindow();
            vaciarCampos();
            inHabilitarEdicionCamposEntrega();
        } catch (Exception exp) {
            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
            JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    public void vaciarCampos() {
        //jComboBoxCamiones.setSelectedIndex(0);
        jComboBoxChoferes.setSelectedItem(null);
        jTextFieldCompras.setText("");
        jTextFieldCorrecionDeDiferencia.setText("0");
        jTextFieldDeuda.setText("");
        jTextFieldDiferencia.setText("");
        jTextFieldEntregaCheques.setText("0");
        jTextFieldEntregaEfectivo.setText("0");
        //jTextFieldFiadoChofer.setText("");
        jTextFieldFiadoEmpresa.setText("");
        jTextFieldGastos.setText("");
        jTextFieldInau.setText("");
        jTextFieldAnep.setText("");
        jTextFieldInventario.setText("");
        jTextFieldRemitoPinchadas.setText("");
        jTextFieldLiquidacion.setText("");
        //jTextFieldRetenciones.setText("0");
        jTextFieldSaldoAnterior.setText("");
        jTextFieldUtilidad.setText("");
        jButtonAbrirLiquidacion.setVisible(false);
        jButtonCerrarLiquidacion.setVisible(false);
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
                try {
                    Chofer c = sisLiquidaciones.devolverChofer(fechaLiq, repartoLiq);
                    boolean esNueva = true;
                    if (c != null) {
                        esNueva = false;
                        Date fecha = new Date();
                        Date fechaSeleccionada = jDateChooserFecha.getDate();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaSeleccionadaS = sdf.format(fechaSeleccionada);
                        String fechaS = sdf.format(fecha);
                        if(fechaSeleccionadaS.equals(fechaS)){
                            if(SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadVerLiquidacionDelDia)){
                                ponerVentanaEnEspera();
                                Thread worker = new Thread() {
                                    public void run() {

                                        try {
                                            Date fechaLiq = jDateChooserFecha.getDate();
                                            Reparto repartoLiq = (Reparto) jComboBoxReparto.getSelectedItem();
                                            liquidacion = sisLiquidaciones.devolverLiquidacion(fechaLiq, repartoLiq, c);
                                            //cargarLiquidacionExistente(c);
                                        } catch (Exception exp) {
                                            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                                            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                                            JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                                        }

                                        SwingUtilities.invokeLater(new Runnable() {
                                            public void run() {
                                                try {
                                                    cargarLiquidacionExistente(c);
                                                    sacarVentanaDeEspera();
                                                    jTextFieldEntregaEfectivo.requestFocus();
                                                    jTextFieldEntregaEfectivo.selectAll();
                                                } catch (ParseException ex) {
                                                    String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                                                    SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                                                    JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                                                }
                                            }
                                        });
                                    }
                                };
                                worker.start();
//cargarLiquidacionExistente(c);
                            } else {
                                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                                this.dispose();
                            }
                        } else {
                            if(SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadVerLiquidacionVieja)){
                                ponerVentanaEnEspera();
                                Thread worker = new Thread() {
                                    public void run() {

                                        try {
                                            Date fechaLiq = jDateChooserFecha.getDate();
                                            Reparto repartoLiq = (Reparto) jComboBoxReparto.getSelectedItem();
                                            liquidacion = sisLiquidaciones.devolverLiquidacion(fechaLiq, repartoLiq, c);
                                            //cargarLiquidacionExistente(c);
                                        } catch (Exception exp) {
                                            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                                            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                                            JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                                        }

                                        SwingUtilities.invokeLater(new Runnable() {
                                            public void run() {
                                                try {
                                                    cargarLiquidacionExistente(c);
                                                    sacarVentanaDeEspera();
                                                    jTextFieldEntregaEfectivo.requestFocus();
                                                    jTextFieldEntregaEfectivo.selectAll();
                                                } catch (ParseException ex) {
                                                    String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                                                    SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                                                    JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                                                }
                                            }
                                        });
                                    }
                                };
                                worker.start();
                                //cargarLiquidacionExistente(c);
                            } else {
                                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                                this.dispose();
                            }
                        }
                        
                        //jTextFieldEntregaEfectivo.requestFocus();
                        //jTextFieldEntregaEfectivo.selectAll();
                    } else {
                        vaciarCampos();
                        esNueva = true;
                        jComboBoxChoferes.requestFocus();
                    }
                    if(esNueva) {
                        jComboBoxChoferes.requestFocus();
                    } else {
                        jTextFieldEntregaEfectivo.requestFocus();
                        jTextFieldEntregaEfectivo.selectAll();
                    }
                    //jComboBoxChoferes.requestFocus();
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

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
                tocoVerGastos = true;
                IngresoGasto ig = new IngresoGasto(this, true);
                ig.setGasto(g);
                ig.setFechaDeHoy(liquidacion.getFecha());
                ig.setVisible(true);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar el gasto. " + "\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception exp) {
                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
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
            if(Lecheros.nombreEmpresa.equals(Constantes.nombreEmpresaGiamo)) {
                jTextFieldEntregaCheques.setEditable(true);
                if("".equals(jTextFieldEntregaCheques.getText().trim())) {
                    jTextFieldEntregaCheques.setText("0");
                } else {
                    try {
                        double entregaEfectivo = Double.parseDouble(jTextFieldEntregaEfectivo.getText().trim());
                    } catch (NumberFormatException ne) {
                        JOptionPane.showMessageDialog(this, "Los campos para la cuenta de la liquidacion deben ser numericos. " + "\n" + ne.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        jTextFieldEntregaEfectivo.requestFocus();
                        jTextFieldEntregaEfectivo.selectAll();
                    }
                }
                jTextFieldEntregaCheques.requestFocus();
                jTextFieldEntregaCheques.selectAll();
            } else {
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
        }
    }//GEN-LAST:event_jTextFieldEntregaEfectivoKeyPressed

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

    private void jComboBoxChoferesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxChoferesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jComboBoxChoferes.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Debes seleccionar un chofer para la liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
                jComboBoxChoferes.requestFocus();
            } else {
                try {
                    Date fecha = new Date();
                    Date fechaSeleccionada = jDateChooserFecha.getDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaSeleccionadaS = sdf.format(fechaSeleccionada);
                    String fechaS = sdf.format(fecha);
                    if(fechaSeleccionadaS.equals(fechaS)){
                        if(SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadVerLiquidacionDelDia)){
                            ponerVentanaEnEspera();
                            Date fechaLiq = jDateChooserFecha.getDate();
                            Reparto repartoLiq = (Reparto) jComboBoxReparto.getSelectedItem();
                            Chofer chofer = (Chofer) jComboBoxChoferes.getSelectedItem();
                            Thread worker = new Thread() {
                                public void run() {

                                    try {
                                        
                                        liquidacion = sisLiquidaciones.devolverLiquidacion(fechaLiq, repartoLiq, chofer);
                                        //cargarLiquidacion();
                                    } catch (Exception exp) {
                                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                                        JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                                    }

                                    SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {
                                            try {
                                                cargarLiquidacion();
                                                sacarVentanaDeEspera();
                                                jTextFieldEntregaEfectivo.requestFocus();
                                                jTextFieldEntregaEfectivo.selectAll();
                                            } catch (ParseException ex) {
                                                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                                                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                                                JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                                            }
                                        }
                                    });
                                }
                            };
                            worker.start();
                            //cargarLiquidacion();
                        } else {
                            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                            this.dispose();
                        }
                    } else {
                        if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadVerLiquidacionVieja)) {
                            ponerVentanaEnEspera();
                            Date fechaLiq = jDateChooserFecha.getDate();
                            Reparto repartoLiq = (Reparto) jComboBoxReparto.getSelectedItem();
                            Chofer chofer = (Chofer) jComboBoxChoferes.getSelectedItem();
                            Thread worker = new Thread() {
                                public void run() {

                                    try {
                                        
                                        liquidacion = sisLiquidaciones.devolverLiquidacion(fechaLiq, repartoLiq, chofer);
                                        //cargarLiquidacion();
                                    } catch (Exception exp) {
                                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                                        JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                                    }

                                    SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {
                                            try {
                                                cargarLiquidacion();
                                                sacarVentanaDeEspera();
                                                jTextFieldEntregaEfectivo.requestFocus();
                                                jTextFieldEntregaEfectivo.selectAll();
                                            } catch (ParseException ex) {
                                                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                                                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                                                JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                                            }
                                        }
                                    });
                                }
                            };
                            worker.start();
                            //cargarLiquidacion();
                        } else {
                            JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                            this.dispose();
                        }
                    }
                    jTextFieldEntregaEfectivo.requestFocus();
                    jTextFieldEntregaEfectivo.selectAll();
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

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        if (tocoVerCheques || tocoVerGastos || tocoVerInventario || tocoVerFiadoChofer || tocoVerRetenciones || tocoVerFiadoEmpresa || tocoVerInau || tocoVerAnep) {
            System.out.println("ENTRO EN EL IF DEL FORM WINDWS Activates");
            try {
                cargarLiquidacion();
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

    private void jButtonImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImprimirActionPerformed
        // TODO add your handling code here:
        if (liquidacion == null) {
            JOptionPane.showMessageDialog(this, "Para imprimir primero debe estar en una liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
            jComboBoxReparto.requestFocus();
        } else {
            Font fuenteNegrita = new Font("Dialog", Font.BOLD, 10);
            PrintJob pj;
            Graphics pagina;
            pj = Toolkit.getDefaultToolkit().getPrintJob(VentanaLiquidacionGiamo.this, "Liquidación", null);
            
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

    private void jButtonCerrarLiquidacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerrarLiquidacionActionPerformed
        // TODO add your handling code here:
        try {
            if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadCerrarLiquidacion)) {
                boolean liqCerrada = sisLiquidaciones.cerrarLiquidacion(liquidacion);
                if(liqCerrada) {
                    JOptionPane.showMessageDialog(this, "Liquidación Cerrada", "Información", JOptionPane.INFORMATION_MESSAGE);
                    jDateChooserFecha.requestFocusInWindow();
                    vaciarCampos();
                    inHabilitarEdicionCamposEntrega();
                }
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception exp) {
            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
            JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonCerrarLiquidacionActionPerformed

    private void jButtonAbrirLiquidacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAbrirLiquidacionActionPerformed
        // TODO add your handling code here:
        try {
            if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadAbrirLiquidacion)) {
                sisLiquidaciones.abrirLiquidacion(liquidacion);
                cargarLiquidacion();
            } else {
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception exp) {
            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
            JOptionPane.showMessageDialog(VentanaLiquidacionGiamo.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonAbrirLiquidacionActionPerformed

    private void jTextFieldEntregaChequesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldEntregaChequesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jTextFieldCorrecionDeDiferencia.isVisible()) {
                jTextFieldCorrecionDeDiferencia.requestFocus();
                jTextFieldCorrecionDeDiferencia.selectAll();
            } else {
                try {
                    double cheques = Double.parseDouble(jTextFieldEntregaCheques.getText().trim());
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
    }//GEN-LAST:event_jTextFieldEntregaChequesKeyPressed

    private void jComboBoxChoferesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxChoferesKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxChoferesKeyReleased

    private void ponerVentanaEnEspera() {
        jLabelEspera.setVisible(true);
        jDateChooserFecha.setEnabled(false);
        jComboBoxReparto.setEnabled(false);
        jComboBoxChoferes.setEnabled(false);
        jTextFieldEntregaEfectivo.setEnabled(false);
        jTextFieldEntregaCheques.setEnabled(false);
        jButtonGuardar.setEnabled(false);
    }
    
    private void sacarVentanaDeEspera() {
        jLabelEspera.setVisible(false);
        jDateChooserFecha.setEnabled(true);
        jComboBoxReparto.setEnabled(true);
        jComboBoxChoferes.setEnabled(true);
        jTextFieldEntregaEfectivo.setEnabled(true);
        jTextFieldEntregaCheques.setEnabled(true);
        jButtonGuardar.setEnabled(true);
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
            java.util.logging.Logger.getLogger(VentanaLiquidacionGiamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaLiquidacionGiamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaLiquidacionGiamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaLiquidacionGiamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaLiquidacionGiamo dialog = new VentanaLiquidacionGiamo(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonAbrirLiquidacion;
    private javax.swing.JButton jButtonCerrarLiquidacion;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonImprimir;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JButton jButtonVerAnep;
    private javax.swing.JButton jButtonVerCompras;
    private javax.swing.JButton jButtonVerFiadoEmpresa;
    private javax.swing.JButton jButtonVerGastos;
    private javax.swing.JButton jButtonVerInau;
    private javax.swing.JButton jButtonVerInventario;
    private javax.swing.JComboBox jComboBoxChoferes;
    private javax.swing.JComboBox jComboBoxReparto;
    private com.toedter.calendar.JDateChooser jDateChooserFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAnep;
    private javax.swing.JLabel jLabelCoreecionDeDiferencia;
    private javax.swing.JLabel jLabelCoreecionDeDiferencia1;
    private javax.swing.JLabel jLabelDeuda;
    private javax.swing.JLabel jLabelDiderencia;
    private javax.swing.JLabel jLabelEntregaCheques;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelFiadoEmpresa;
    private javax.swing.JLabel jLabelInau;
    private javax.swing.JLabel jLabelRemitoPinchadas;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JTextField jTextFieldAnep;
    private javax.swing.JTextField jTextFieldCompras;
    private javax.swing.JTextField jTextFieldCorrecionDeDiferencia;
    private javax.swing.JTextField jTextFieldDeuda;
    private javax.swing.JTextField jTextFieldDiferencia;
    private javax.swing.JTextField jTextFieldEntregaCheques;
    private javax.swing.JTextField jTextFieldEntregaEfectivo;
    private javax.swing.JTextField jTextFieldFiadoEmpresa;
    private javax.swing.JTextField jTextFieldGastos;
    private javax.swing.JTextField jTextFieldInau;
    private javax.swing.JTextField jTextFieldInventario;
    private javax.swing.JTextField jTextFieldLiquidacion;
    private javax.swing.JTextField jTextFieldLiquidacion1;
    private javax.swing.JTextField jTextFieldRemitoPinchadas;
    private javax.swing.JTextField jTextFieldSaldoAnterior;
    private javax.swing.JTextField jTextFieldUtilidad;
    // End of variables declaration//GEN-END:variables
}
