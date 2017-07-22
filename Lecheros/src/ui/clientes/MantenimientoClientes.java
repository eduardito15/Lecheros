/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.clientes;

import dominio.Cliente;
import dominio.Reparto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.hibernate.HibernateException;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class MantenimientoClientes extends javax.swing.JFrame {

    private final SistemaMantenimiento sis;
    private DefaultTableModel modelo;
    private List<Cliente> clientes;
    
    private boolean yaBusco;
    /**
     * Creates new form MantenimientoClientes
     */
    public MantenimientoClientes(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        sis = SistemaMantenimiento.getInstance();
        jComboBoxReparto.addItem("");
        jLabelEspera.setVisible(false);
        List<Reparto> repartos = sis.devolverRepartos();
        for(Reparto c:repartos){
            jComboBoxReparto.addItem(c);
        }
        inicializarTablaClientes();
        yaBusco = false;
    }
    
    public final void inicializarTablaClientes() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Codigo");
        modelo.addColumn("Nombre");
        modelo.addColumn("Rut");
        modelo.addColumn("Dirección");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Email");
        modelo.addColumn("Prorrateo");
        jTableClientes.setModel(modelo);
        TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = jTableClientes.getColumnModel().getColumn(i);
            if (i == 1 || i == 3) {
                column.setPreferredWidth(180); //articulo column is bigger
            } else {
                if(i == 0){
                    column.setPreferredWidth(35);
                } else {
                    if(i == 2){
                        column.setPreferredWidth(100);
                    } else {
                        column.setPreferredWidth(50);
                    }
                }   
            }
        }
        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem verItem = new JMenuItem("Ver");
        verItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da ver en el menu con click derecho sobre la fila de la tabla
               IngresarCliente ic = new IngresarCliente(MantenimientoClientes.this, false);
               ic.setAccion("Ver");
               Cliente c = clientes.get(jTableClientes.getSelectedRow());
               ic.setCliente(c);
               ic.setVisible(true);
            }

        });
        popupMenu.add(verItem);
        JMenuItem modificarItem = new JMenuItem("Modificar");
        modificarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da modificar en el menu con click derecho sobre la fila de la tabla
                IngresarCliente ic = new IngresarCliente(MantenimientoClientes.this, false);
                ic.setAccion("Modificar");
                Cliente c = clientes.get(jTableClientes.getSelectedRow());
                ic.setCliente(c);
                ic.setVisible(true);
            }

        });
        popupMenu.add(modificarItem);
        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        eliminarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da eliminar en el menu con click derecho sobre la fila de la tabla
                Cliente c = clientes.get(jTableClientes.getSelectedRow());
                int resp = JOptionPane.showConfirmDialog(MantenimientoClientes.this, "Seguro que quiere eliminar el cliente " + c.getNombre() + " ? Recorda que no se eliminara. Se pasara a estado inactivo.", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    try {
                        if (sis.eliminarCliente(c)) {
                            JOptionPane.showMessageDialog(MantenimientoClientes.this, "El cliente se elimino correctamente. Se paso a estado inactivo.", "Información", JOptionPane.INFORMATION_MESSAGE);
                            jButtonBuscar.doClick();
                        }
                    } catch (HibernateException he) {
                        JOptionPane.showMessageDialog(MantenimientoClientes.this, "Error al eliminar el cliente." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(MantenimientoClientes.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        popupMenu.add(eliminarItem);
        jTableClientes.setComponentPopupMenu(popupMenu);
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
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabelTitulo1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxReparto = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jRadioButtonManual = new javax.swing.JRadioButton();
        jRadioButtonProrrateo = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jRadioButtonActivo = new javax.swing.JRadioButton();
        jRadioButtonInActivo = new javax.swing.JRadioButton();
        jButtonBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableClientes = new javax.swing.JTable();
        jButtonSalir = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldNombreCliente = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldCodigoPS = new javax.swing.JTextField();
        jLabelEspera = new javax.swing.JLabel();
        jButtonRefrescar = new javax.swing.JButton();

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Cliente");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mantenimiento Clientes");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabelTitulo1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo1.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo1.setText("Clientes");

        jLabel1.setText("Reparto: ");

        jComboBoxReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxRepartoKeyPressed(evt);
            }
        });

        jLabel2.setText("Tipo:");

        buttonGroup1.add(jRadioButtonManual);
        jRadioButtonManual.setText("Manual");

        buttonGroup1.add(jRadioButtonProrrateo);
        jRadioButtonProrrateo.setText("Prorrateo");

        jLabel3.setText("Estado:");

        buttonGroup2.add(jRadioButtonActivo);
        jRadioButtonActivo.setSelected(true);
        jRadioButtonActivo.setText("Activo");

        buttonGroup2.add(jRadioButtonInActivo);
        jRadioButtonInActivo.setText("In-Activo");

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

        jTableClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Rut", "Dirección", "Teléfono", "Email", "Prorrateo"
            }
        ));
        jTableClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableClientes);

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

        jLabel4.setText("Nombre:");

        jTextFieldNombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNombreClienteKeyPressed(evt);
            }
        });

        jLabel5.setText("* Se buscan los clientes que contienen el texto ingresado.");

        jLabel6.setText("Codigo de PS:");

        jTextFieldCodigoPS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldCodigoPSKeyPressed(evt);
            }
        });

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        jButtonRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/liquidaciones/refresh.png"))); // NOI18N
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButtonRefrescar)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jRadioButtonManual)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jRadioButtonProrrateo))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jRadioButtonActivo)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jRadioButtonInActivo)))
                                .addGap(87, 87, 87)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButtonBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelEspera))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldCodigoPS, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldNombreCliente, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxReparto, javax.swing.GroupLayout.Alignment.LEADING, 0, 200, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelTitulo1)
                                    .addComponent(jLabel5)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 842, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelTitulo1)
                    .addComponent(jButtonRefrescar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jRadioButtonManual)
                                .addComponent(jRadioButtonProrrateo)
                                .addComponent(jButtonBuscar)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jRadioButtonActivo)
                            .addComponent(jRadioButtonInActivo)
                            .addComponent(jButtonSalir))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextFieldNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jTextFieldCodigoPS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabelEspera))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            jTextFieldNombreCliente.requestFocus();
            jTextFieldNombreCliente.selectAll();
        }
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

    private void jButtonBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonBuscarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonBuscar.doClick();
        }
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButtonBuscar.doClick();
        } 
    }//GEN-LAST:event_jButtonBuscarKeyPressed

    private void jButtonSalirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonSalirKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonSalir.doClick();
        }
    }//GEN-LAST:event_jButtonSalirKeyPressed

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
         // TODO add your handling code here:
        //yaBusco = true;
        inicializarTablaClientes();
        clientes = new ArrayList<>();
        if(jComboBoxReparto.getSelectedIndex() == 0){
            JOptionPane.showMessageDialog(this, "Debe seleccionar un reparto para la busqueda.", "Información", JOptionPane.INFORMATION_MESSAGE);
            jComboBoxReparto.requestFocus();
        } else {
            if("".equals(jTextFieldCodigoPS.getText().trim())){
                if("".equals(jTextFieldNombreCliente.getText().trim())){
                    //Es vacio el nombre para la busqueda
                    if (jRadioButtonManual.isSelected() || jRadioButtonProrrateo.isSelected()) {
                        //Busco con el filtro de manual o prorrateo
                        ponerVentanaEnEspera();
                        Thread worker = new Thread() {
                            public void run() {
                                clientes = sis.devolverClientesPorRepartoEstadoYTipo((Reparto) jComboBoxReparto.getSelectedItem(), jRadioButtonActivo.isSelected(), jRadioButtonProrrateo.isSelected());
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {  
                                        sacarVentanaDeEspera();
                                        for (Cliente c : clientes) {
                                            cargarClienteEnTabla(c);
                                            yaBusco = true;
                                        }
                                    }
                                });
                            }
                        };
                        worker.start();
                    } else {
                        //Busco por reparto y estado
                        ponerVentanaEnEspera();
                        Thread worker = new Thread() {
                            public void run() {
                                clientes = sis.devolverClientesPorRepartoYEstado((Reparto) jComboBoxReparto.getSelectedItem(), jRadioButtonActivo.isSelected());
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() { 
                                        sacarVentanaDeEspera();
                                        for (Cliente c : clientes) {
                                            cargarClienteEnTabla(c);
                                            yaBusco = true;
                                        }
                                    }
                                });
                            }
                        };
                        worker.start();
                    } 
                } else {
                    //No es vacio el nombre. Hago la misma busqueda pero por nombre tambien
                    if (jRadioButtonManual.isSelected() || jRadioButtonProrrateo.isSelected()) {
                        //Busco con el filtro de manual o prorrateo y nombre
                        ponerVentanaEnEspera();
                        Thread worker = new Thread() {
                            public void run() {
                                clientes = sis.devolverClientesPorRepartoEstadoTipoYNombre((Reparto) jComboBoxReparto.getSelectedItem(), jRadioButtonActivo.isSelected(), jRadioButtonProrrateo.isSelected(), jTextFieldNombreCliente.getText().trim());
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {  
                                        sacarVentanaDeEspera();
                                        for (Cliente c : clientes) {
                                            cargarClienteEnTabla(c);
                                            yaBusco = true;
                                        }
                                    }
                                });
                            }
                        };
                        worker.start();
                    } else {
                        //Busco por reparto, estado y nombre
                        ponerVentanaEnEspera();
                        Thread worker = new Thread() {
                            public void run() {
                                clientes = sis.devolverClientesPorRepartoEstadoYNombre((Reparto) jComboBoxReparto.getSelectedItem(), jRadioButtonActivo.isSelected(), jTextFieldNombreCliente.getText().trim());
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {  
                                        sacarVentanaDeEspera();
                                        for (Cliente c : clientes) {
                                            cargarClienteEnTabla(c);
                                            yaBusco = true;
                                        }
                                    }
                                });
                            }
                        };
                        worker.start();
                    }
                }
            } else {
                try{
                    long codigoPS = Long.parseLong(jTextFieldCodigoPS.getText().trim());
                    clientes = sis.devolverClientePorCodigoPS(codigoPS);
                    for (Cliente c : clientes) {
                        cargarClienteEnTabla(c);
                        yaBusco = true;
                    }
                } catch (NumberFormatException ne){
                    JOptionPane.showMessageDialog(this, "El codigo PS debe ser un numero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    jTextFieldCodigoPS.requestFocus();
                }
            }
                
        }
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    public void ponerVentanaEnEspera(){
        jLabelEspera.setVisible(true);
        jComboBoxReparto.setEnabled(false);
        jRadioButtonActivo.setEnabled(false);
        jRadioButtonInActivo.setEnabled(false);
        jRadioButtonManual.setEnabled(false);
        jRadioButtonProrrateo.setEnabled(false);
        jTextFieldCodigoPS.setEnabled(false);
        jTextFieldNombreCliente.setEnabled(false);
        jButtonBuscar.setEnabled(false);
        jTableClientes.setEnabled(false);
    }
    
    public void sacarVentanaDeEspera(){
        jLabelEspera.setVisible(false);
        jComboBoxReparto.setEnabled(true);
        jRadioButtonActivo.setEnabled(true);
        jRadioButtonInActivo.setEnabled(true);
        jRadioButtonManual.setEnabled(true);
        jRadioButtonProrrateo.setEnabled(true);
        jTextFieldCodigoPS.setEnabled(true);
        jTextFieldNombreCliente.setEnabled(true);
        jButtonBuscar.setEnabled(true);
        jTableClientes.setEnabled(true);
    }
    
    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        if (yaBusco) {
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_formWindowActivated

    private void jTextFieldNombreClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNombreClienteKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextFieldCodigoPS.requestFocus();
            jTextFieldCodigoPS.selectAll();
        }
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jTextFieldNombreClienteKeyPressed

    private void jTextFieldCodigoPSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCodigoPSKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if("".equals(jTextFieldCodigoPS.getText().trim())){
                jButtonBuscar.requestFocus();
            } else {
                try{
                    Long.parseLong(jTextFieldCodigoPS.getText().trim());
                    jButtonBuscar.requestFocus();
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(this, "El codigo PS debe ser un numero.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    jTextFieldCodigoPS.requestFocus();
                }
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jTextFieldCodigoPSKeyPressed

    private void jButtonRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefrescarActionPerformed
        // TODO add your handling code here:
        MantenimientoClientes vg = new MantenimientoClientes(null, false);
        vg.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButtonRefrescarActionPerformed

    private void jTableClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableClientesMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            if (jTableClientes.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                IngresarCliente ic = new IngresarCliente(MantenimientoClientes.this, false);
                ic.setAccion("Modificar");
                Cliente c = clientes.get(jTableClientes.getSelectedRow());
                ic.setCliente(c);
                ic.setVisible(true);
            }
        }
    }//GEN-LAST:event_jTableClientesMouseClicked

    public void cargarClienteEnTabla(Cliente c){
        Object[] object = new Object[7];
        object[0] = c.getCodigo();
        object[1] = c.getNombre();
        object[2] = c.getRut();
        object[3] = c.getDireccion();
        object[4] = c.getTelefono();
        object[5] = c.getCorreoElectronico();
        if(c.isProrrateo()){
            object[6] = "Si";
        }
        if(!c.isProrrateo()){
            object[6] = "No";
        }
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
            java.util.logging.Logger.getLogger(MantenimientoClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MantenimientoClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MantenimientoClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MantenimientoClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MantenimientoClientes dialog = new MantenimientoClientes(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonRefrescar;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JComboBox jComboBoxReparto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTitulo1;
    private javax.swing.JRadioButton jRadioButtonActivo;
    private javax.swing.JRadioButton jRadioButtonInActivo;
    private javax.swing.JRadioButton jRadioButtonManual;
    private javax.swing.JRadioButton jRadioButtonProrrateo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableClientes;
    private javax.swing.JTextField jTextFieldCodigoPS;
    private javax.swing.JTextField jTextFieldNombreCliente;
    // End of variables declaration//GEN-END:variables
}
