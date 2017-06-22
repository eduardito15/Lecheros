/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.facturas;

import dao.GenericDAO;
import dominio.Articulo;
import dominio.Cliente;
import dominio.DocumentoDeVenta;
import dominio.Factura;
import dominio.FacturaRenglon;
import dominio.Precio;
import dominio.Reparto;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import sistema.SistemaFacturas;
import sistema.SistemaMantenimiento;
import sistema.SistemaMantenimientoArticulos;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;
import ui.clientes.VentanaBuscadorClienteFactura;


/**
 *
 * @author Edu
 */
public class IngresarFacturaManualGiamo extends javax.swing.JFrame {

    private final SistemaFacturas sisFacturas;
    private final SistemaMantenimiento sisMantenimiento;
    private final SistemaMantenimientoArticulos sisArticulos;

    private Factura factura;
    private Cliente cliente;
    private long numeroDeFactura;
    List<Reparto> repartos;

    private final DecimalFormat df;
    
    
    private boolean mostrarMensajeFechaIncorrecta;
    
    /**
     * Creates new form IngresarFacturaManualGiamo
     */
    public IngresarFacturaManualGiamo(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        sisFacturas = SistemaFacturas.getInstance();
        sisMantenimiento = SistemaMantenimiento.getInstance();
        sisArticulos = SistemaMantenimientoArticulos.getInstance();
        mostrarMensajeFechaIncorrecta = true;
        jComboBoxReparto.addItem("");
        repartos = sisMantenimiento.devolverRepartos();
        for (Reparto c : repartos) {
            jComboBoxReparto.addItem(c);
        }
        jButton2.setVisible(false);
        jButton3.setVisible(false);
        df = new DecimalFormat("0.00");
        agregarEnterCampoFecha();
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
                    factura.setFecha(jDateChooserFecha.getDate());
                    jTextFieldCliente.requestFocus();
                    jTextFieldCliente.selectAll();
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
                        factura.setFecha(jDateChooserFecha.getDate());
                        jTextFieldCliente.requestFocus();
                        jTextFieldCliente.selectAll();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelTitulo = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldNumero = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxReparto = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jDateChooserFecha = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldCliente = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldLecheComun = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldLecheUltra = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldLecheDeslactosada = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldMinimo = new javax.swing.JTextField();
        jTextFieldBasico = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButtonIngresar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabelFechaIncorrecta = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ingreso Factura Manual ");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Factura Manual");

        jLabel7.setText("Numero:");

        jTextFieldNumero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNumeroKeyPressed(evt);
            }
        });

        jLabel2.setText("Reparto:");

        jComboBoxReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxRepartoKeyPressed(evt);
            }
        });

        jLabel1.setText("Fecha:");

        jDateChooserFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserFecha.setMaxSelectableDate(new java.util.Date(4102455710000L));
        jDateChooserFecha.setMinSelectableDate(new java.util.Date(946699295000L));

        jLabel3.setText("Cliente:");

        jTextFieldCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldClienteKeyPressed(evt);
            }
        });

        jLabel4.setText("Leche Común:");

        jTextFieldLecheComun.setText("0");
        jTextFieldLecheComun.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldLecheComunKeyPressed(evt);
            }
        });

        jLabel5.setText("Leche Ultra:");

        jTextFieldLecheUltra.setText("0");
        jTextFieldLecheUltra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldLecheUltraKeyPressed(evt);
            }
        });

        jLabel6.setText("Leche Deslactosada:");

        jTextFieldLecheDeslactosada.setText("0");
        jTextFieldLecheDeslactosada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldLecheDeslactosadaKeyPressed(evt);
            }
        });

        jLabel8.setText("Minimo:");

        jTextFieldMinimo.setText("0");
        jTextFieldMinimo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldMinimoKeyPressed(evt);
            }
        });

        jTextFieldBasico.setText("0");
        jTextFieldBasico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldBasicoKeyPressed(evt);
            }
        });

        jLabel9.setText("Basico:");

        jButtonIngresar.setText("Ingresar");
        jButtonIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIngresarActionPerformed(evt);
            }
        });
        jButtonIngresar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonIngresarKeyPressed(evt);
            }
        });

        jButton2.setText("Modificar");

        jButton3.setText("Eliminar");

        jLabel10.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel10.setText("Total:");

        jLabelTotal.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTotal.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTotal.setText("0");

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelFechaIncorrecta)
                    .addComponent(jLabelTitulo)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldBasico)
                            .addComponent(jTextFieldNumero)
                            .addComponent(jComboBoxReparto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooserFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(jTextFieldCliente)
                            .addComponent(jTextFieldLecheComun)
                            .addComponent(jTextFieldLecheUltra)
                            .addComponent(jTextFieldLecheDeslactosada)
                            .addComponent(jTextFieldMinimo))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonIngresar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabelTotal))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextFieldNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelFechaIncorrecta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLecheComun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLecheUltra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jButtonIngresar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldLecheDeslactosada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldBasico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabelTotal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldNumeroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNumeroKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!"".equals(jTextFieldNumero.getText().trim())) {
                try {
                    if (factura == null) {
                        //Si la factura manual en null seguro que es una factura manual nueva, entonces si o si verifico que el numero sea valido
                        if (sisFacturas.numeroDeFacturaManualValido(Long.parseLong(jTextFieldNumero.getText().trim()))) {
                            //habilitarCampos();
                            factura = new Factura();
                            factura.setRenglones(new ArrayList<>());
                            factura.setNumero(Long.parseLong(jTextFieldNumero.getText().trim()));
                            jComboBoxReparto.requestFocus();
                        } else {
                            JOptionPane.showMessageDialog(this, "Ya existe una Factura Manual con el numero ingresado. Por favor ingrese un nuevo numero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                            jTextFieldNumero.requestFocus();
                            jTextFieldNumero.selectAll();
                        }
                    } else {//Si la compra no es null es por que es una mofificacion. Entonces solo verifico que sea valido si modifico el numero
                        Long numCampo = Long.parseLong(jTextFieldNumero.getText());
                        if (factura.getNumero() != numCampo) {
                            if (sisFacturas.numeroDeFacturaManualValido(Long.parseLong(jTextFieldNumero.getText().trim()))) {
                                //habilitarCampos();
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

    private void jComboBoxRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxRepartoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jComboBoxReparto.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un reparto.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jComboBoxReparto.requestFocus();
            } else {
                factura.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                jDateChooserFecha.requestFocusInWindow();
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

    private void jTextFieldClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldClienteKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("".equals(jTextFieldCliente.getText().trim())) {
                //Es vacio. Le habro el buscador
                VentanaBuscadorClienteFactura vbc = new VentanaBuscadorClienteFactura(IngresarFacturaManualGiamo.this, true);
                vbc.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                vbc.setVisible(true);
                if (vbc.getCliente() != null) {
                    this.cliente = vbc.getCliente();
                    jTextFieldCliente.setText(cliente.getNombre());
                    
                    factura.setCliente(cliente);
                    jTextFieldLecheComun.requestFocus();
                    jTextFieldLecheComun.selectAll();
                } else {
                    jTextFieldCliente.requestFocus();
                    jTextFieldCliente.selectAll();
                }
            } else //No es vacio. Busco la cadena en los rut de cliente, si conincide la muestro si no pregunto si quiere volver a ingresarlo o abrir el buscador de clientes.
            {
                if (cliente != null) {
                    if (cliente.getNombre().equals(jTextFieldCliente.getText().trim())) {
                        jTextFieldCliente.setText(cliente.getNombre());
                        
                        factura.setCliente(cliente);
                       jTextFieldLecheComun.requestFocus();
                       jTextFieldLecheComun.selectAll();
                    }
                } else {
                    String fraccionDelRut = jTextFieldCliente.getText().trim();
                    cliente = sisMantenimiento.devolverClientePorFraccionDelRut(fraccionDelRut);
                    if (cliente != null) {
                        jTextFieldCliente.setText(cliente.getNombre());
                        factura.setCliente(cliente);
                        jTextFieldLecheComun.requestFocus();
                        jTextFieldLecheComun.selectAll();
                    } else {
                        int resp = JOptionPane.showConfirmDialog(this, "No existe un cliente que contenga en el rut: " + fraccionDelRut + " . Desea abrir el buscador de clientes?  ", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (resp == JOptionPane.YES_OPTION) {
                            VentanaBuscadorClienteFactura vbc = new VentanaBuscadorClienteFactura(IngresarFacturaManualGiamo.this, true);
                            vbc.setReparto((Reparto) jComboBoxReparto.getSelectedItem());
                            vbc.setVisible(true);
                            if (vbc.getCliente() != null) {
                                this.cliente = vbc.getCliente();
                                jTextFieldCliente.setText(cliente.getNombre());
                                factura.setCliente(cliente);
                                jTextFieldLecheComun.requestFocus();
                                jTextFieldLecheComun.selectAll();
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

    private void jTextFieldLecheComunKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLecheComunKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                int litrosComun = Integer.parseInt(jTextFieldLecheComun.getText().trim());
                if(litrosComun != 0) {
                Articulo lecheComun = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(11111);
                Precio pLecheComun = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheComun, factura.getFecha());
                
                double totalComun = litrosComun * pLecheComun.getPrecioVenta();
                factura.setSubtotal(factura.getSubtotal() + totalComun);
                factura.setTotal(factura.getTotal() + totalComun);
                FacturaRenglon fr = new FacturaRenglon();
                fr.setFactura(factura);
                fr.setArticulo(lecheComun);
                fr.setCantidad(litrosComun);
                fr.setPrecio(pLecheComun.getPrecioVenta());
                fr.setSubtotal(totalComun);
                fr.setTotal(totalComun);
                factura.getRenglones().add(fr);
                jLabelTotal.setText(df.format(factura.getTotal()).replace(',', '.'));
                }
                jTextFieldLecheUltra.requestFocus();
                jTextFieldLecheUltra.selectAll();
            } catch (NumberFormatException ne) {
                JOptionPane.showMessageDialog(this, "El numero debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldLecheComun.requestFocus();
                jTextFieldLecheUltra.selectAll();
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                jTextFieldLecheComun.requestFocus();
                jTextFieldLecheUltra.selectAll();
            }
        }
    }//GEN-LAST:event_jTextFieldLecheComunKeyPressed

    private void jTextFieldLecheUltraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLecheUltraKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                int litrosUltra = Integer.parseInt(jTextFieldLecheUltra.getText().trim());
                if(litrosUltra != 0) { 
                Articulo lecheUltra = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(22222);
                Precio pLecheUltra = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheUltra, factura.getFecha());
                
                double totalUltra = litrosUltra * pLecheUltra.getPrecioVenta();
                factura.setSubtotal(factura.getSubtotal() + totalUltra);
                factura.setTotal(factura.getTotal() + totalUltra);
                FacturaRenglon fr = new FacturaRenglon();
                fr.setFactura(factura);
                fr.setArticulo(lecheUltra);
                fr.setCantidad(litrosUltra);
                fr.setPrecio(pLecheUltra.getPrecioVenta());
                fr.setSubtotal(totalUltra);
                fr.setTotal(totalUltra);
                factura.getRenglones().add(fr);
                jLabelTotal.setText(df.format(factura.getTotal()).replace(',', '.'));
                }
                jTextFieldLecheDeslactosada.requestFocus();
                jTextFieldLecheDeslactosada.selectAll();
            } catch (NumberFormatException ne) {
                JOptionPane.showMessageDialog(this, "El numero debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldLecheComun.requestFocus();
                jTextFieldLecheUltra.selectAll();
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                jTextFieldLecheComun.requestFocus();
                jTextFieldLecheUltra.selectAll();
            }
        }
    }//GEN-LAST:event_jTextFieldLecheUltraKeyPressed

    private void jTextFieldLecheDeslactosadaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldLecheDeslactosadaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                int litrosDeslactosada = Integer.parseInt(jTextFieldLecheDeslactosada.getText().trim());
                if(litrosDeslactosada != 0) {
                Articulo lecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(33333);
                Precio pLecheDeslactosada = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(lecheDeslactosada, factura.getFecha());
                
                double totalDeslactosada = litrosDeslactosada * pLecheDeslactosada.getPrecioVenta();
                factura.setSubtotal(factura.getSubtotal() + totalDeslactosada);
                factura.setTotal(factura.getTotal() + totalDeslactosada);
                FacturaRenglon fr = new FacturaRenglon();
                fr.setFactura(factura);
                fr.setArticulo(lecheDeslactosada);
                fr.setCantidad(litrosDeslactosada);
                fr.setPrecio(pLecheDeslactosada.getPrecioVenta());
                fr.setSubtotal(totalDeslactosada);
                fr.setTotal(totalDeslactosada);
                factura.getRenglones().add(fr);
                jLabelTotal.setText(df.format(factura.getTotal()).replace(',', '.'));
                }
                jTextFieldMinimo.requestFocus();
                jTextFieldMinimo.selectAll();
            } catch (NumberFormatException ne) {
                JOptionPane.showMessageDialog(this, "El numero debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldLecheComun.requestFocus();
                jTextFieldLecheUltra.selectAll();
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                jTextFieldLecheComun.requestFocus();
                jTextFieldLecheUltra.selectAll();
            }
        }
    }//GEN-LAST:event_jTextFieldLecheDeslactosadaKeyPressed

    private void jTextFieldMinimoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldMinimoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                double productosMinimo = Double.parseDouble(jTextFieldMinimo.getText().trim());
                if(productosMinimo != 0) {
                Articulo prodsMinimo = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(44444);
                Precio pMinimo = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(prodsMinimo, factura.getFecha());
                
                factura.setSubtotal(factura.getSubtotal() + productosMinimo);
                double ivaMinimo = productosMinimo * (prodsMinimo.getIva().getPorcentaje() / 100);
                factura.setTotalMinimo(ivaMinimo);
                factura.setTotal(factura.getTotal() + productosMinimo + ivaMinimo);
                
                FacturaRenglon fr = new FacturaRenglon();
                fr.setFactura(factura);
                fr.setArticulo(prodsMinimo);
                fr.setCantidad(1);
                fr.setPrecio(pMinimo.getPrecioVenta());
                fr.setSubtotal(productosMinimo);
                fr.setIva(ivaMinimo);
                fr.setTotal(productosMinimo + ivaMinimo);
                factura.getRenglones().add(fr);
                jLabelTotal.setText(df.format(factura.getTotal()).replace(',', '.'));
                }
                jTextFieldBasico.requestFocus();
                jTextFieldBasico.selectAll();
            } catch (NumberFormatException ne) {
                JOptionPane.showMessageDialog(this, "El numero debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldLecheComun.requestFocus();
                jTextFieldLecheUltra.selectAll();
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                jTextFieldLecheComun.requestFocus();
                jTextFieldLecheUltra.selectAll();
            }
        }
    }//GEN-LAST:event_jTextFieldMinimoKeyPressed

    private void jTextFieldBasicoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBasicoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                double productosBasico = Double.parseDouble(jTextFieldBasico.getText().trim());
                if(productosBasico != 0) {
                Articulo prodsBasico = SistemaMantenimientoArticulos.getInstance().devolverArticuloPorCodigo(55555);
                Precio pBasico = SistemaMantenimientoArticulos.getInstance().devolverPrecioParaFechaPorArticulo(prodsBasico, factura.getFecha());
                
                factura.setSubtotal(factura.getSubtotal() + productosBasico);
                double ivaBasico = productosBasico * (prodsBasico.getIva().getPorcentaje() / 100);
                factura.setTotalBasico(productosBasico);
                factura.setTotal(factura.getTotal() + productosBasico + ivaBasico);
                
                FacturaRenglon fr = new FacturaRenglon();
                fr.setFactura(factura);
                fr.setArticulo(prodsBasico);
                fr.setCantidad(1);
                fr.setPrecio(pBasico.getPrecioVenta());
                fr.setSubtotal(productosBasico);
                fr.setIva(ivaBasico);
                fr.setTotal(productosBasico + ivaBasico);
                factura.getRenglones().add(fr);
                jLabelTotal.setText(df.format(factura.getTotal()).replace(',', '.'));
                }
                jButtonIngresar.requestFocus();
            } catch (NumberFormatException ne) {
                JOptionPane.showMessageDialog(this, "El numero debe ser un numero entero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jTextFieldLecheComun.requestFocus();
                jTextFieldLecheUltra.selectAll();
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                jTextFieldLecheComun.requestFocus();
                jTextFieldLecheUltra.selectAll();
            }
        }
    }//GEN-LAST:event_jTextFieldBasicoKeyPressed

    private void jButtonIngresarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonIngresarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonIngresar.doClick();
        }
    }//GEN-LAST:event_jButtonIngresarKeyPressed

    private void jButtonIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIngresarActionPerformed
        try {
            // TODO add your handling code here:
            DocumentoDeVenta dv = SistemaMantenimiento.getInstance().devolverDocumentoDeVentaPorNombre("Contado");
            factura.setTipoDocumento(dv);
            if (sisFacturas.guardarFacturaManual(factura)) {
                //Se guardo bien, habro de nuevo la misma ventana, dejo la fecha, el reparto y aumento 1 en el numero, borro el cliente.
                long proximoNumero = this.factura.getNumero() + 1;
                IngresarFacturaManualGiamo vif = new IngresarFacturaManualGiamo(null, false);
                vif.setNumeroDeFactura(proximoNumero);
                vif.setVisible(true);
                this.dispose();
            } else {
                
            }
        } catch (Exception ex) {
            Logger.getLogger(IngresarFacturaManualGiamo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonIngresarActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        if(numeroDeFactura != 0) {
            jTextFieldNumero.setText("" + numeroDeFactura);
        }
    }//GEN-LAST:event_formWindowActivated

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
            java.util.logging.Logger.getLogger(IngresarFacturaManualGiamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresarFacturaManualGiamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresarFacturaManualGiamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresarFacturaManualGiamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IngresarFacturaManualGiamo dialog = new IngresarFacturaManualGiamo(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonIngresar;
    private javax.swing.JComboBox jComboBoxReparto;
    private com.toedter.calendar.JDateChooser jDateChooserFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JTextField jTextFieldBasico;
    private javax.swing.JTextField jTextFieldCliente;
    private javax.swing.JTextField jTextFieldLecheComun;
    private javax.swing.JTextField jTextFieldLecheDeslactosada;
    private javax.swing.JTextField jTextFieldLecheUltra;
    private javax.swing.JTextField jTextFieldMinimo;
    private javax.swing.JTextField jTextFieldNumero;
    // End of variables declaration//GEN-END:variables

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
