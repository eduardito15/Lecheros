/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.liquidaciones;

import dominio.Inau;
import dominio.Reparto;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
public class IngresoInau extends javax.swing.JDialog {

    private SistemaMantenimiento sis;
    private SistemaLiquidaciones sisLiquidaciones;
    private Inau inau;
    
    private String accion;
    
    private boolean mostrarMensajeFechaIncorrecta = true;
    /**
     * Creates new form IngresoInau
     */
    public IngresoInau(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sis = SistemaMantenimiento.getInstance();
        sisLiquidaciones = SistemaLiquidaciones.getInstance();
        jComboBoxReparto.addItem("");
        List<Reparto> repartos = sis.devolverRepartos();
        for(Reparto r:repartos){
            jComboBoxReparto.addItem(r);
        }
        setearFechaDesde();
        agregarEnterCampoFecha();
        jComboBoxReparto.requestFocus();
        
    }
    
    public final void agregarEnterCampoFecha(){
        jLabelFechaIncorrecta.setVisible(false);
        jLabelErrorFechaDesde.setVisible(false);
        jLabelHastaFechaIncorrecta.setVisible(false);
        jDateChooserDesdeFecha.getDateEditor().getUiComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (jComboBoxReparto.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(IngresoInau.this, "Debe seleccionar un reparto.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        jComboBoxReparto.requestFocus();
                    } else {
                        jDateChooserHastaFecha.getDateEditor().getUiComponent().requestFocus();
                    }
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
                        Calendar fechaSeleccionada = Calendar.getInstance();
                        fechaSeleccionada.setTime(jDateChooserDesdeFecha.getDate());
                        int diaDeLaSemana = fechaSeleccionada.get(fechaSeleccionada.DAY_OF_WEEK);
                        if (diaDeLaSemana == 2) {
                            //Le sumo siete dias a la fecha que selecciono que es lunes o sea hasta el Domingo para la fecha hasta
                            fechaSeleccionada.add(Calendar.DATE, 6);
                            jDateChooserHastaFecha.setDate(fechaSeleccionada.getTime());
                            fechaSeleccionada.add(Calendar.DATE, -6);
                            jLabelFechaIncorrecta.setVisible(false);
                            jLabelErrorFechaDesde.setVisible(false);
                            jDateChooserHastaFecha.getDateEditor().getUiComponent().requestFocus();
                        } else {
                            jLabelErrorFechaDesde.setVisible(true);
                            jDateChooserDesdeFecha.getDateEditor().getUiComponent().requestFocus();
                        }

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
                    if (jComboBoxReparto.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(IngresoInau.this, "Debe seleccionar un reparto.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        jComboBoxReparto.requestFocus();
                    } else {
                        try {
                            //Aca me fijo si ya existe un documento con esa fecha para recuperarlo o creo uno nuevo

                            Date fechaDesde = jDateChooserDesdeFecha.getDate();
                            Date fechaHasta = jDateChooserHastaFecha.getDate();
                            Inau i = sisLiquidaciones.devolverInauPorRepartoEntreFechas(fechaDesde, fechaHasta, (Reparto) jComboBoxReparto.getSelectedItem());
                            if (i != null) {
                                setInau(i);
                                cargarInau(inau);
                            }
                            habilitarCamposDias();
                        } catch (ParseException ex) {
                            JOptionPane.showMessageDialog(IngresoInau.this, "Error al cargar el documento de anep. " + "\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception exp) {
                            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                            JOptionPane.showMessageDialog(IngresoInau.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
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
                        Date fechaDesde = jDateChooserDesdeFecha.getDate();
                        Date fechaHasta = jDateChooserHastaFecha.getDate();
                        Calendar fDesde = Calendar.getInstance();
                        fDesde.setTime(fechaDesde);
                        fDesde.add(Calendar.DATE, 6);
                        if (fechaHasta.equals(fDesde.getTime())) {
                            jLabelHastaFechaIncorrecta.setVisible(false);
                        } else {
                            jLabelHastaFechaIncorrecta.setVisible(true);
                            jDateChooserHastaFecha.setDate(fDesde.getTime());
                            jDateChooserHastaFecha.getDateEditor().getUiComponent().requestFocus();
                        }
                        jLabelFechaIncorrecta.setVisible(false);
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
    
    private void setearFechaDesde(){
        Calendar fecha = Calendar.getInstance();
        while (!(fecha.get(fecha.DAY_OF_WEEK) == 2)) {
            fecha.add(Calendar.DATE, -1);
        }
        jDateChooserDesdeFecha.setDate(fecha.getTime());
        Calendar fecha2 = fecha;
        fecha2.add(Calendar.DATE, 6);
        jDateChooserHastaFecha.setDate(fecha2.getTime());
    }
    
    public void habilitarCamposDias(){
        jTextFieldLunes.setEnabled(true);
        jTextFieldMartes.setEnabled(true);
        jTextFieldMiercoles.setEnabled(true);
        jTextFieldJueves.setEnabled(true);
        jTextFieldViernes.setEnabled(true);
        jTextFieldSabado.setEnabled(true);
        jTextFieldDomingo.setEnabled(true);
        jTextFieldLunes.requestFocus();
        jTextFieldLunes.selectAll();
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
        jComboBoxReparto = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldLunes = new javax.swing.JTextField();
        jTextFieldMartes = new javax.swing.JTextField();
        jTextFieldMiercoles = new javax.swing.JTextField();
        jTextFieldJueves = new javax.swing.JTextField();
        jTextFieldViernes = new javax.swing.JTextField();
        jTextFieldSabado = new javax.swing.JTextField();
        jTextFieldDomingo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButtonGuardar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jDateChooserDesdeFecha = new com.toedter.calendar.JDateChooser();
        jDateChooserHastaFecha = new com.toedter.calendar.JDateChooser();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jLabelErrorFechaDesde = new javax.swing.JLabel();
        jLabelHastaFechaIncorrecta = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ingreso Inau");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabelTitulo1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo1.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo1.setText("Inau");

        jLabel1.setText("Reparto: ");

        jComboBoxReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxRepartoKeyPressed(evt);
            }
        });

        jLabel4.setText("Lunes");

        jLabel5.setText("Martes");

        jTextFieldLunes.setText("0");
        jTextFieldLunes.setEnabled(false);
        jTextFieldLunes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldLunesKeyPressed(evt);
            }
        });

        jTextFieldMartes.setText("0");
        jTextFieldMartes.setEnabled(false);
        jTextFieldMartes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldMartesKeyPressed(evt);
            }
        });

        jTextFieldMiercoles.setText("0");
        jTextFieldMiercoles.setEnabled(false);
        jTextFieldMiercoles.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldMiercolesKeyPressed(evt);
            }
        });

        jTextFieldJueves.setText("0");
        jTextFieldJueves.setEnabled(false);
        jTextFieldJueves.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldJuevesKeyPressed(evt);
            }
        });

        jTextFieldViernes.setText("0");
        jTextFieldViernes.setEnabled(false);
        jTextFieldViernes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldViernesKeyPressed(evt);
            }
        });

        jTextFieldSabado.setText("0");
        jTextFieldSabado.setEnabled(false);
        jTextFieldSabado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldSabadoKeyPressed(evt);
            }
        });

        jTextFieldDomingo.setText("0");
        jTextFieldDomingo.setEnabled(false);
        jTextFieldDomingo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldDomingoKeyPressed(evt);
            }
        });

        jLabel6.setText("Miercoles");

        jLabel7.setText("Jueves");

        jLabel8.setText("Viernes");

        jLabel9.setText("Sabado");

        jLabel10.setText("Domingo");

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

        jLabel11.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel11.setText("Total:");

        jLabelTotal.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabelTotal.setText("0");

        jLabel3.setText("Hasta Fecha:");

        jLabel2.setText("Desde Fecha:");

        jDateChooserDesdeFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserDesdeFecha.setMinSelectableDate(new java.util.Date(4102455715000L));

        jDateChooserHastaFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserHastaFecha.setMaxSelectableDate(new java.util.Date(4102455687000L));
        jDateChooserHastaFecha.setMinSelectableDate(new java.util.Date(946699317000L));

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 51, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabelErrorFechaDesde.setForeground(new java.awt.Color(255, 51, 51));
        jLabelErrorFechaDesde.setText("La fecha Desde debe ser Lunes");

        jLabelHastaFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 0));
        jLabelHastaFechaIncorrecta.setText("Hasta Fecha es 6 dias mas que desde fecha");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(jTextFieldLunes, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(jTextFieldMartes, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(jLabel4)
                                .addGap(53, 53, 53)
                                .addComponent(jLabel5)))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(45, 45, 45)
                                .addComponent(jLabel7))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextFieldMiercoles, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(jTextFieldJueves, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(jTextFieldViernes, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jLabel8)))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldSabado, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jTextFieldDomingo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(310, 310, 310)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelTotal))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButtonGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelHastaFechaIncorrecta)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(32, 32, 32)
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(42, 42, 42))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addComponent(jLabelErrorFechaDesde)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelFechaIncorrecta)))
                        .addGap(2, 2, 2)))
                .addContainerGap(45, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(348, 348, 348)
                        .addComponent(jLabelTitulo1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFechaIncorrecta)
                    .addComponent(jLabelHastaFechaIncorrecta)
                    .addComponent(jLabelErrorFechaDesde))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLunes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMartes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMiercoles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldJueves, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldViernes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldSabado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDomingo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabelTotal))
                .addGap(18, 18, 18)
                .addComponent(jButtonGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSalir)
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
            if(jComboBoxReparto.getSelectedIndex() == 0){
                JOptionPane.showMessageDialog(this, "Debe seleccionar un reparto." , "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                //jDateChooserDesdeFecha.requestFocusInWindow();
                try {
                    Date fechaDesde = jDateChooserDesdeFecha.getDate();
                    Date fechaHasta = jDateChooserHastaFecha.getDate();
                    Inau i = sisLiquidaciones.devolverInauPorRepartoEntreFechas(fechaDesde, fechaHasta, (Reparto)jComboBoxReparto.getSelectedItem());
                    if(i != null){
                        setInau(i);
                        cargarInau(i);
                        habilitarCamposDias();
                    } else {
                        jDateChooserDesdeFecha.requestFocusInWindow();
                    }
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Error al cargar el documento de inau. " + "\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

    public void cargarInau(Inau i){
        jTextFieldLunes.setText(Integer.toString(i.getLitrosLunes()));
        jTextFieldMartes.setText(Integer.toString(i.getLitrosMartes()));
        jTextFieldMiercoles.setText(Integer.toString(i.getLitrosMiercoles()));
        jTextFieldJueves.setText(Integer.toString(i.getLitrosJueves()));
        jTextFieldViernes.setText(Integer.toString(i.getLitrosViernes()));
        jTextFieldSabado.setText(Integer.toString(i.getLitrosSabado()));
        jTextFieldDomingo.setText(Integer.toString(i.getLitrosDomingo()));
        jLabelTotal.setText(Integer.toString(i.getLitrosTotal()));
    }
    
    private void jTextFieldLunesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLunesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try{
                Integer.parseInt(jTextFieldLunes.getText().trim());
                int totalLitros = Integer.parseInt(jTextFieldLunes.getText().trim()) + Integer.parseInt(jTextFieldMartes.getText().trim())+ Integer.parseInt(jTextFieldMiercoles.getText().trim()) + Integer.parseInt(jTextFieldJueves.getText().trim()) + Integer.parseInt(jTextFieldViernes.getText().trim())+ Integer.parseInt(jTextFieldSabado.getText().trim()) + Integer.parseInt(jTextFieldDomingo.getText().trim());
                jLabelTotal.setText(Integer.toString(totalLitros));
                jTextFieldMartes.requestFocus();
                jTextFieldMartes.selectAll();
            } catch (NumberFormatException ne){
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un numero entero." , "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTextFieldLunesKeyPressed

    private void jTextFieldMartesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldMartesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try{
                Integer.parseInt(jTextFieldMartes.getText().trim());
                int totalLitros = Integer.parseInt(jTextFieldLunes.getText().trim()) + Integer.parseInt(jTextFieldMartes.getText().trim())+ Integer.parseInt(jTextFieldMiercoles.getText().trim()) + Integer.parseInt(jTextFieldJueves.getText().trim()) + Integer.parseInt(jTextFieldViernes.getText().trim())+ Integer.parseInt(jTextFieldSabado.getText().trim()) + Integer.parseInt(jTextFieldDomingo.getText().trim());
                jLabelTotal.setText(Integer.toString(totalLitros));
                jTextFieldMiercoles.requestFocus();
                jTextFieldMiercoles.selectAll();
            } catch (NumberFormatException ne){
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un numero entero." , "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTextFieldMartesKeyPressed

    private void jTextFieldMiercolesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldMiercolesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try{
                Integer.parseInt(jTextFieldMiercoles.getText().trim());
                int totalLitros = Integer.parseInt(jTextFieldLunes.getText().trim()) + Integer.parseInt(jTextFieldMartes.getText().trim())+ Integer.parseInt(jTextFieldMiercoles.getText().trim()) + Integer.parseInt(jTextFieldJueves.getText().trim()) + Integer.parseInt(jTextFieldViernes.getText().trim())+ Integer.parseInt(jTextFieldSabado.getText().trim()) + Integer.parseInt(jTextFieldDomingo.getText().trim());
                jLabelTotal.setText(Integer.toString(totalLitros));
                jTextFieldJueves.requestFocus();
                jTextFieldJueves.selectAll();
            } catch (NumberFormatException ne){
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un numero entero." , "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTextFieldMiercolesKeyPressed

    private void jTextFieldJuevesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldJuevesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try{
                Integer.parseInt(jTextFieldJueves.getText().trim());
                int totalLitros = Integer.parseInt(jTextFieldLunes.getText().trim()) + Integer.parseInt(jTextFieldMartes.getText().trim())+ Integer.parseInt(jTextFieldMiercoles.getText().trim()) + Integer.parseInt(jTextFieldJueves.getText().trim()) + Integer.parseInt(jTextFieldViernes.getText().trim())+ Integer.parseInt(jTextFieldSabado.getText().trim()) + Integer.parseInt(jTextFieldDomingo.getText().trim());
                jLabelTotal.setText(Integer.toString(totalLitros));
                jTextFieldViernes.requestFocus();
                jTextFieldViernes.selectAll();
            } catch (NumberFormatException ne){
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un numero entero." , "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTextFieldJuevesKeyPressed

    private void jTextFieldViernesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldViernesKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try{
                Integer.parseInt(jTextFieldViernes.getText().trim());
                int totalLitros = Integer.parseInt(jTextFieldLunes.getText().trim()) + Integer.parseInt(jTextFieldMartes.getText().trim())+ Integer.parseInt(jTextFieldMiercoles.getText().trim()) + Integer.parseInt(jTextFieldJueves.getText().trim()) + Integer.parseInt(jTextFieldViernes.getText().trim())+ Integer.parseInt(jTextFieldSabado.getText().trim()) + Integer.parseInt(jTextFieldDomingo.getText().trim());
                jLabelTotal.setText(Integer.toString(totalLitros));
                jTextFieldSabado.requestFocus();
                jTextFieldSabado.selectAll();
            } catch (NumberFormatException ne){
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un numero entero." , "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTextFieldViernesKeyPressed

    private void jTextFieldSabadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSabadoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try{
                Integer.parseInt(jTextFieldSabado.getText().trim());
                int totalLitros = Integer.parseInt(jTextFieldLunes.getText().trim()) + Integer.parseInt(jTextFieldMartes.getText().trim())+ Integer.parseInt(jTextFieldMiercoles.getText().trim()) + Integer.parseInt(jTextFieldJueves.getText().trim()) + Integer.parseInt(jTextFieldViernes.getText().trim())+ Integer.parseInt(jTextFieldSabado.getText().trim()) + Integer.parseInt(jTextFieldDomingo.getText().trim());
                jLabelTotal.setText(Integer.toString(totalLitros));
                jTextFieldDomingo.requestFocus();
                jTextFieldDomingo.selectAll();
            } catch (NumberFormatException ne){
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un numero entero." , "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTextFieldSabadoKeyPressed

    private void jTextFieldDomingoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDomingoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try{
                Integer.parseInt(jTextFieldDomingo.getText().trim());
                int totalLitros = Integer.parseInt(jTextFieldLunes.getText().trim()) + Integer.parseInt(jTextFieldMartes.getText().trim())+ Integer.parseInt(jTextFieldMiercoles.getText().trim()) + Integer.parseInt(jTextFieldJueves.getText().trim()) + Integer.parseInt(jTextFieldViernes.getText().trim())+ Integer.parseInt(jTextFieldSabado.getText().trim()) + Integer.parseInt(jTextFieldDomingo.getText().trim());
                jLabelTotal.setText(Integer.toString(totalLitros));
                jButtonGuardar.requestFocus();
            } catch (NumberFormatException ne){
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un numero entero." , "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTextFieldDomingoKeyPressed

    private void jButtonGuardarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonGuardarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonGuardar.doClick();
        }
    }//GEN-LAST:event_jButtonGuardarKeyPressed

    private void jButtonSalirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonSalirKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonSalir.doClick();
        }
    }//GEN-LAST:event_jButtonSalirKeyPressed

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        // TODO add your handling code here:
        if(inau == null){
            //Es uno nuevo
            try{
                Date fechaDesde = jDateChooserDesdeFecha.getDate();
                Date fechaHasta = jDateChooserHastaFecha.getDate();
                if(sisLiquidaciones.guardarInau(fechaDesde, fechaHasta, (Reparto)jComboBoxReparto.getSelectedItem(),Integer.parseInt(jTextFieldLunes.getText().trim()), Integer.parseInt(jTextFieldMartes.getText().trim()), Integer.parseInt(jTextFieldMiercoles.getText().trim()), Integer.parseInt(jTextFieldJueves.getText().trim()), Integer.parseInt(jTextFieldViernes.getText().trim()), Integer.parseInt(jTextFieldSabado.getText().trim()), Integer.parseInt(jTextFieldDomingo.getText().trim()), Integer.parseInt(jLabelTotal.getText().trim()))){
                    JOptionPane.showMessageDialog(this, "El documento de Inau se guardo correctamente." , "Información", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                }
            } catch (Exception e){
                String stakTrace = util.Util.obtenerStackTraceEnString(e);
                SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                
                JOptionPane.showMessageDialog(this, "Error al guardar el documento de inau. " + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            //Es una inau que ya existe hay que actualizarlo
            boolean actualizar = true;
            try{
                Reparto r = (Reparto)jComboBoxReparto.getSelectedItem();
                Date fechaDesde = jDateChooserDesdeFecha.getDate();
                Date fechaHasta = jDateChooserHastaFecha.getDate();
                if(!r.equals(inau.getReparto())){
                    //Aca quiere modificar el reparto
                    Inau i = sisLiquidaciones.devolverInauPorRepartoEntreFechas(fechaDesde, fechaHasta, r);
                    if(i == null){
                        setInau(new Inau());
                        inau.setReparto(r);
                        inau.setDesdeFecha(fechaDesde);
                        inau.setHastaFecha(fechaHasta);
                        inau.setLitrosLunes(Integer.parseInt(jTextFieldLunes.getText().trim()));
                        inau.setLitrosMartes(Integer.parseInt(jTextFieldMartes.getText().trim()));
                        inau.setLitrosMiercoles(Integer.parseInt(jTextFieldMiercoles.getText().trim()));
                        inau.setLitrosJueves(Integer.parseInt(jTextFieldJueves.getText().trim()));
                        inau.setLitrosViernes(Integer.parseInt(jTextFieldViernes.getText().trim()));
                        inau.setLitrosSabado(Integer.parseInt(jTextFieldSabado.getText().trim()));
                        inau.setLitrosDomingo(Integer.parseInt(jTextFieldDomingo.getText().trim()));
                        inau.setLitrosTotal(Integer.parseInt(jLabelTotal.getText().trim()));
                    } else {
                        int resp = JOptionPane.showConfirmDialog(this, "Quiere modificar el reparto por uno que ya existe en otro documento de inau para ese reparto en esas fechas. Desea Abrirlo? Si elige que no se cambiara nuevamente el reparto al que habia antes de cambiarlo.", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(resp == JOptionPane.YES_OPTION){
                            cargarInau(i);
                            habilitarCamposDias();
                            actualizar = false;
                        } else {
                            jComboBoxReparto.setSelectedItem(inau.getReparto());
                            actualizar = false;
                        }
                    }
                } else {
                    //No quiere modificar la fecha
                    SimpleDateFormat formatter;
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String fD = formatter.format(fechaDesde);
                    String fH = formatter.format(fechaHasta);
                    if(!fD.equals(formatter.format(inau.getDesdeFecha()))){
                        //Quiere cambiar la fecha
                        Inau i = sisLiquidaciones.devolverInauPorRepartoEntreFechas(fechaDesde, fechaHasta, r);
                        if(i == null){
                            setInau(new Inau());
                            inau.setReparto((Reparto)jComboBoxReparto.getSelectedItem());
                            inau.setDesdeFecha(fechaDesde);
                            inau.setHastaFecha(fechaHasta);
                            inau.setLitrosLunes(Integer.parseInt(jTextFieldLunes.getText().trim()));
                            inau.setLitrosMartes(Integer.parseInt(jTextFieldMartes.getText().trim()));
                            inau.setLitrosMiercoles(Integer.parseInt(jTextFieldMiercoles.getText().trim()));
                            inau.setLitrosJueves(Integer.parseInt(jTextFieldJueves.getText().trim()));
                            inau.setLitrosViernes(Integer.parseInt(jTextFieldViernes.getText().trim()));
                            inau.setLitrosSabado(Integer.parseInt(jTextFieldSabado.getText().trim()));
                            inau.setLitrosDomingo(Integer.parseInt(jTextFieldDomingo.getText().trim()));
                            inau.setLitrosTotal(Integer.parseInt(jLabelTotal.getText().trim()));
                        } else {
                            int resp = JOptionPane.showConfirmDialog(this, "Quiere modificar las fechas por unas que ya existen en otro documento de inau para ese reparto en esas fechas. Desea Abrirlo? Si elige que no se cambiara nuevamente a las fechas que habia antes de cambiarlas.", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (resp == JOptionPane.YES_OPTION) {
                                cargarInau(i);
                                habilitarCamposDias();
                                actualizar = false;
                            } else {
                                //Calendar fDes = Calendar.getInstance();
                                //fDes.setTime(inau.getDesdeFecha());
                                //Calendar fHas = Calendar.getInstance();
                                //fHas.setTime(inau.getHastaFecha());
                                //dateChooserComboDesdeFecha.setSelectedDate(fDes);
                                jDateChooserDesdeFecha.setDate(inau.getDesdeFecha());
                                jDateChooserHastaFecha.setDate(inau.getHastaFecha());
                                //dateChooserComboHastaFecha.setSelectedDate(fHas);
                                actualizar = false;
                            }
                        }
                    } else {
                        //No quiere modificar ni el reparto ni las fechas, actualizo solo los litros de los dias
                        inau.setLitrosLunes(Integer.parseInt(jTextFieldLunes.getText().trim()));
                        inau.setLitrosMartes(Integer.parseInt(jTextFieldMartes.getText().trim()));
                        inau.setLitrosMiercoles(Integer.parseInt(jTextFieldMiercoles.getText().trim()));
                        inau.setLitrosJueves(Integer.parseInt(jTextFieldJueves.getText().trim()));
                        inau.setLitrosViernes(Integer.parseInt(jTextFieldViernes.getText().trim()));
                        inau.setLitrosSabado(Integer.parseInt(jTextFieldSabado.getText().trim()));
                        inau.setLitrosDomingo(Integer.parseInt(jTextFieldDomingo.getText().trim()));
                        inau.setLitrosTotal(Integer.parseInt(jLabelTotal.getText().trim()));
                    }
                }
                if(actualizar){
                    if (sisLiquidaciones.actualizarInau(inau)) {
                        JOptionPane.showMessageDialog(this, "Documento de Inau actualizado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                    }
                }
            } catch (Exception e) {
                String stakTrace = util.Util.obtenerStackTraceEnString(e);
                SistemaUsuarios.getInstance().registrarExcepcion(e.toString(), stakTrace);
                
                JOptionPane.showMessageDialog(this, "Error al guardar el documento de inau. " + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        if(inau!=null && accion.equals("Modificar")){
            int indiceReparto = devolverIndiceReparto(inau.getReparto());
            jComboBoxReparto.setSelectedIndex(indiceReparto);
            jDateChooserDesdeFecha.setDate(inau.getDesdeFecha());
            jDateChooserHastaFecha.setDate(inau.getHastaFecha());
            cargarInau(inau);
            habilitarCamposDias();
        }
        if (inau != null && accion.equals("Ver")) {
            jComboBoxReparto.setEnabled(false);
            jDateChooserDesdeFecha.setEnabled(false);
            jDateChooserHastaFecha.setEnabled(false);
            int indiceReparto = devolverIndiceReparto(inau.getReparto());
            jComboBoxReparto.setSelectedIndex(indiceReparto);
            jDateChooserDesdeFecha.setDate(inau.getDesdeFecha());
            jDateChooserHastaFecha.setDate(inau.getHastaFecha());
            cargarInau(inau);
            jButtonGuardar.setEnabled(false);
            jButtonSalir.requestFocus();
        }
    }//GEN-LAST:event_formWindowActivated

    private int devolverIndiceReparto(Reparto reparto) {
        List<Reparto> repartos = sis.devolverRepartos();
        for (int i = 0; i < repartos.size(); i++) {
            Reparto r = repartos.get(i);
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
            java.util.logging.Logger.getLogger(IngresoInau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresoInau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresoInau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresoInau.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IngresoInau dialog = new IngresoInau(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JComboBox jComboBoxReparto;
    private com.toedter.calendar.JDateChooser jDateChooserDesdeFecha;
    private com.toedter.calendar.JDateChooser jDateChooserHastaFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelErrorFechaDesde;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelHastaFechaIncorrecta;
    private javax.swing.JLabel jLabelTitulo1;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JTextField jTextFieldDomingo;
    private javax.swing.JTextField jTextFieldJueves;
    private javax.swing.JTextField jTextFieldLunes;
    private javax.swing.JTextField jTextFieldMartes;
    private javax.swing.JTextField jTextFieldMiercoles;
    private javax.swing.JTextField jTextFieldSabado;
    private javax.swing.JTextField jTextFieldViernes;
    // End of variables declaration//GEN-END:variables

    /**
     * @param inau the inau to set
     */
    public void setInau(Inau inau) {
        this.inau = inau;
    }

    /**
     * @param accion the accion to set
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }
}
