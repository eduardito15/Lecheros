/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.informes;

import ui.clientes.*;
import dominio.Cliente;
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
import sistema.SistemaInformes;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class VentanaResumenDescuentos extends javax.swing.JFrame {

    private final SistemaMantenimiento sisMantenimiento;
    private final SistemaInformes sisInformes;

    private List<String[]> resultado;

    private Cliente cliente;

    private final DecimalFormat df;
    private DefaultTableModel modelo;
    private boolean mostrarMensajeFechaIncorrecta = true;

    /**
     * Creates new form ResumenDeComprasPorCliente
     */
    public VentanaResumenDescuentos(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        sisMantenimiento = SistemaMantenimiento.getInstance();
        sisInformes = SistemaInformes.getInstance();
        df = new DecimalFormat("0.00");
        jLabelEspera.setVisible(false);
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
                    jTextFieldCliente.requestFocus();
                    jTextFieldCliente.selectAll();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelTitulo1 = new javax.swing.JLabel();
        jLabelEspera = new javax.swing.JLabel();
        jButtonRefrescar = new javax.swing.JButton();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jLabelHastaFechaIncorrecta = new javax.swing.JLabel();
        jButtonSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jDateChooserDesdeFecha = new com.toedter.calendar.JDateChooser();
        jDateChooserHastaFecha = new com.toedter.calendar.JDateChooser();
        jButtonVer = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldCliente = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableResultado = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabelTotalCliente = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Resumen de Compras Por Cliente");

        jLabelTitulo1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo1.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo1.setText("Resumen de Descuentos");

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        jButtonRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/liquidaciones/refresh.png"))); // NOI18N
        jButtonRefrescar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonRefrescar.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jButtonRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefrescarActionPerformed(evt);
            }
        });

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabelHastaFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelHastaFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

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

        jDateChooserDesdeFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserDesdeFecha.setMaxSelectableDate(new java.util.Date(4102455686000L));
        jDateChooserDesdeFecha.setMinSelectableDate(new java.util.Date(946699286000L));

        jDateChooserHastaFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserHastaFecha.setMaxSelectableDate(new java.util.Date(4102455689000L));
        jDateChooserHastaFecha.setMinSelectableDate(new java.util.Date(946699289000L));

        jButtonVer.setText("Consultar");
        jButtonVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVerActionPerformed(evt);
            }
        });
        jButtonVer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonVerKeyPressed(evt);
            }
        });

        jLabel4.setText("Cliente:");

        jTextFieldCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldClienteKeyPressed(evt);
            }
        });

        jTableResultado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Fecha", "Numero", "Total", "Descuento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableResultado);

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabel3.setText("Total:");

        jLabelTotalCliente.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelTotalCliente.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTotalCliente.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldCliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButtonVer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabelEspera)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelFechaIncorrecta)
                                    .addComponent(jLabelHastaFechaIncorrecta)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(519, 519, 519)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelTotalCliente)
                                .addGap(215, 215, 215))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 798, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonRefrescar)
                        .addGap(149, 149, 149)
                        .addComponent(jLabelTitulo1)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitulo1)
                    .addComponent(jButtonRefrescar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jButtonVer))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonSalir)))
                    .addComponent(jLabelEspera)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelFechaIncorrecta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelHastaFechaIncorrecta)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabelTotalCliente))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
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

    private void jButtonVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerActionPerformed
        // TODO add your handling code here:
        //if (!"".equals(jTextFieldCliente.getText().trim())) {
            inicializarTabla();
            Date desdeFecha = jDateChooserDesdeFecha.getDate();
            Date hastaFecha = jDateChooserHastaFecha.getDate();

            jLabelEspera.setVisible(true);
            jDateChooserDesdeFecha.setEnabled(false);
            jDateChooserHastaFecha.setEnabled(false);
            jButtonVer.setEnabled(false);
            Thread worker = new Thread() {
                public void run() {
                    try {
                        resultado = sisInformes.consultarDescuentosRealizados(desdeFecha, hastaFecha, cliente);
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(VentanaResumenDescuentos.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            jLabelEspera.setVisible(false);
                            jDateChooserDesdeFecha.setEnabled(true);
                            jDateChooserHastaFecha.setEnabled(true);
                            jButtonVer.setEnabled(true);
                            cargarTabla(resultado);
                            jButtonSalir.requestFocus();
                        }
                    });

                }
            };
            worker.start();
        //} else {
            //JOptionPane.showMessageDialog(this, "Debe seleccionar un Cliente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            //jTextFieldCliente.requestFocus();
            jTextFieldCliente.selectAll();
        //}
    }//GEN-LAST:event_jButtonVerActionPerformed

    private void inicializarTabla() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Fecha");
        modelo.addColumn("Numero");
        modelo.addColumn("Total");
        modelo.addColumn("Descuento");
        jTableResultado.setModel(modelo);
        TableColumn column = null;
        for (int i = 0; i < 4; i++) {
            column = jTableResultado.getColumnModel().getColumn(i);
                column.setPreferredWidth(50);
        }
    }

    private void jButtonVerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonVerKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonVer.doClick();
        }
    }//GEN-LAST:event_jButtonVerKeyPressed

    private void jTextFieldClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldClienteKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("".equals(jTextFieldCliente.getText().trim())) {
                //Es vacio. Le habro el buscador
                VentanaBuscadorClienteTodos vbc = new VentanaBuscadorClienteTodos(VentanaResumenDescuentos.this, true);
                vbc.setVisible(true);
                this.cliente = vbc.getCliente();
                jTextFieldCliente.setText(cliente.getNombre());
                jTextFieldCliente.setEditable(false);
                jButtonVer.requestFocus();
            } else //No es vacio. Busco la cadena en los rut de cliente, si conincide la muestro si no pregunto si quiere volver a ingresarlo o abrir el buscador de clientes.
             if (cliente != null) {
                    if (cliente.getNombre().equals(jTextFieldCliente.getText().trim())) {
                        jTextFieldCliente.setText(cliente.getNombre());

                        jTextFieldCliente.setEditable(false);
                        jButtonVer.requestFocus();
                    }
                } else {
                    String fraccionDelRut = jTextFieldCliente.getText().trim();
                    cliente = sisMantenimiento.devolverClientePorFraccionDelRut(fraccionDelRut);
                    if (cliente != null) {
                        jTextFieldCliente.setText(cliente.getNombre());

                        jTextFieldCliente.setEditable(false);
                        jButtonVer.requestFocus();
                    } else {
                        int resp = JOptionPane.showConfirmDialog(this, "No existe un cliente que contenga en el rut: " + fraccionDelRut + " . Desea abrir el buscador de clientes?  ", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (resp == JOptionPane.YES_OPTION) {
                            VentanaBuscadorClienteTodos vbc = new VentanaBuscadorClienteTodos(VentanaResumenDescuentos.this, true);
                            vbc.setVisible(true);
                            this.cliente = vbc.getCliente();
                            jTextFieldCliente.setText(cliente.getNombre());

                            jTextFieldCliente.setEditable(false);
                            jButtonVer.requestFocus();
                        } else if (resp == JOptionPane.NO_OPTION) {
                            jTextFieldCliente.requestFocus();
                            jTextFieldCliente.selectAll();
                        }
                    }
                }
        }
    }//GEN-LAST:event_jTextFieldClienteKeyPressed

    private void jButtonRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefrescarActionPerformed
        // TODO add your handling code here:
        VentanaResumenDescuentos vrdcpc = new VentanaResumenDescuentos(null, false);
        vrdcpc.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButtonRefrescarActionPerformed

    private void cargarTabla(List<String[]> tabla) {
        for (int i = 0; i < tabla.size() - 1; i++) {
            Object[] object = new Object[4];
            object[0] = tabla.get(i)[0];
            object[1] = tabla.get(i)[1];
            object[2] = df.format(Double.parseDouble(tabla.get(i)[2])).replace(',', '.');
            object[3] = df.format(Double.parseDouble(tabla.get(i)[3])).replace(',', '.');
            modelo.addRow(object);
        }
        String[] totales = tabla.get(tabla.size()-1);
        jLabelTotalCliente.setText(df.format(Double.parseDouble(totales[1])).replace(',', '.'));
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
            java.util.logging.Logger.getLogger(VentanaResumenDescuentos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaResumenDescuentos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaResumenDescuentos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaResumenDescuentos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaResumenDescuentos dialog = new VentanaResumenDescuentos(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonRefrescar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JButton jButtonVer;
    private com.toedter.calendar.JDateChooser jDateChooserDesdeFecha;
    private com.toedter.calendar.JDateChooser jDateChooserHastaFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelHastaFechaIncorrecta;
    private javax.swing.JLabel jLabelTitulo1;
    private javax.swing.JLabel jLabelTotalCliente;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableResultado;
    private javax.swing.JTextField jTextFieldCliente;
    // End of variables declaration//GEN-END:variables
}
