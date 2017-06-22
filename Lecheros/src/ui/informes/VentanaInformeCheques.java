/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.informes;

import dominio.Cheque;
import dominio.Cliente;
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
import java.util.Calendar;
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
import org.hibernate.HibernateException;
import sistema.SistemaInformes;
import sistema.SistemaLiquidaciones;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.liquidaciones.IngresoCheque;
import ui.clientes.VentanaBuscadorClienteTodos;
import ui.facturas.MantenimientoFacturas;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class VentanaInformeCheques extends javax.swing.JFrame {

    private Cliente cliente;
    private List<Cheque> cheques;
    private final SistemaMantenimiento sisMantenimiento;
    private final SistemaLiquidaciones sisLiquidaciones;
    private final SistemaInformes sisInformes;
    
    private DecimalFormat df;
    private DefaultTableModel modelo;
    private boolean yaBusco;
    private boolean mostrarMensajeFechaIncorrecta = true;
    /**
     * Creates new form VentanaInformeCheques
     */
    public VentanaInformeCheques(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        sisMantenimiento = SistemaMantenimiento.getInstance();
        sisLiquidaciones = SistemaLiquidaciones.getInstance();
        sisInformes = SistemaInformes.getInstance();
        df = new DecimalFormat("0.00");
        yaBusco = false;
        inicializarTablaCheques();
        agregarEnterCampoFecha();
        agregarEnterCampoFechaVto();
        ocultarCliente();
        ocultarFecha();
        ocultarFechaVto();
    }
    
    private void ocultarCliente(){
        jLabelCliente.setVisible(false);
        jTextFieldCliente.setVisible(false);
    }
    
    private void ocultarFecha(){
        jLabelDesdeFecha.setVisible(false);
        jLabelHastaFecha.setVisible(false);
        jDateChooserDesdeFecha.setVisible(false);
        jDateChooserHastaFecha.setVisible(false);
    }
    
    private void ocultarFechaVto(){
        jLabelDesdeFechaVto.setVisible(false);
        jLabelHastaFechaVto.setVisible(false);
        jDateChooserDesdeFechaVto.setVisible(false);
        jDateChooserHastaFechaVto.setVisible(false);
    }
    
    private void mostrarCliente(){
        jLabelCliente.setVisible(true);
        jTextFieldCliente.setVisible(true);
    }
    
    private void mostrarFecha(){
        jLabelDesdeFecha.setVisible(true);
        jLabelHastaFecha.setVisible(true);
        jDateChooserDesdeFecha.setVisible(true);
        jDateChooserHastaFecha.setVisible(true);
    }
    
    private void mostrarFechaVto(){
        jLabelDesdeFechaVto.setVisible(true);
        jLabelHastaFechaVto.setVisible(true);
        jDateChooserDesdeFechaVto.setVisible(true);
        jDateChooserHastaFechaVto.setVisible(true);
    }
    
    public final void inicializarTablaCheques() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Cliente");
        modelo.addColumn("Fecha");
        modelo.addColumn("Fecha Vto");
        modelo.addColumn("Importe");
        jTableCheques.setModel(modelo);
        TableColumn column = null;
        for (int i = 0; i < 4; i++) {
            column = jTableCheques.getColumnModel().getColumn(i);
            if (i == 0 ) {
                column.setPreferredWidth(120); //articulo column is bigger
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
                Cheque c = cheques.get(jTableCheques.getSelectedRow());
                IngresoCheque vic = new IngresoCheque(VentanaInformeCheques.this, true);
                vic.setAccion("Ver");
                vic.setCheque(c);
                vic.setVisible(true);
            }

        });
        popupMenu.add(verItem);
        JMenuItem modificarItem = new JMenuItem("Modificar");
        modificarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da modificar en el menu con click derecho sobre la fila de la tabla
                Cheque c = cheques.get(jTableCheques.getSelectedRow());
                IngresoCheque vic = new IngresoCheque(VentanaInformeCheques.this, false);
                vic.setAccion("Modificar");
                vic.setCheque(c);
                vic.setVisible(true);
                jButtonBuscar.doClick();
            }

        });
        popupMenu.add(modificarItem);
        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        eliminarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da eliminar en el menu con click derecho sobre la fila de la tabla
                Cheque c = cheques.get(jTableCheques.getSelectedRow());
                int resp = JOptionPane.showConfirmDialog(VentanaInformeCheques.this, "Seguro que quiere eliminar el cheque de " + c.getCliente() + " con el importe " + c.getImporte()   + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(resp == JOptionPane.YES_OPTION){
                    try{
                        if(sisLiquidaciones.eliminarCheque(c)){
                            JOptionPane.showMessageDialog(VentanaInformeCheques.this, "El cheque se elimino correctamente" , "Información", JOptionPane.INFORMATION_MESSAGE);
                            jButtonBuscar.doClick();
                        } 
                    } 
                    catch (HibernateException he){
                        JOptionPane.showMessageDialog(VentanaInformeCheques.this, "Error al eliminar el cheque." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(VentanaInformeCheques.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        popupMenu.add(eliminarItem);
        jTableCheques.setComponentPopupMenu(popupMenu);
    }
    
    public final void agregarEnterCampoFecha(){
        jLabelFechaIncorrecta.setVisible(false);
        Date fechaDeHoy = new Date();
        jDateChooserDesdeFecha.setDate(fechaDeHoy);
        jDateChooserHastaFecha.setDate(fechaDeHoy);
        jDateChooserDesdeFecha.getDateEditor().getUiComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jDateChooserHastaFecha.requestFocusInWindow();
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
                    if (f == null) {
                        //JOptionPane.showMessageDialog(IngresarCompras.this, "Fecha Incorrecta. Ingrese una fecha correcta. Debe ingresar los dos numeros del dia, los dos numeros del mes y los dos ultimos numeros del año");
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
        
        jDateChooserHastaFecha.getDateEditor().getUiComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if(jRadioButtonFechaVto.isSelected()){
                        jDateChooserDesdeFechaVto.requestFocusInWindow();
                    } else {
                        jButtonBuscar.requestFocus();
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
                    if (f == null) {
                        //JOptionPane.showMessageDialog(IngresarCompras.this, "Fecha Incorrecta. Ingrese una fecha correcta. Debe ingresar los dos numeros del dia, los dos numeros del mes y los dos ultimos numeros del año");
                        jLabelFechaIncorrecta.setVisible(true);
                        jDateChooserHastaFecha.getDateEditor().getUiComponent().requestFocus();
                    } else {
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
    
    public final void agregarEnterCampoFechaVto(){
        jLabelFechaVtoIncorrecta.setVisible(false);
        Date fechaDeHoy = new Date();
        jDateChooserDesdeFechaVto.setDate(fechaDeHoy);
        jDateChooserHastaFechaVto.setDate(fechaDeHoy);
        jDateChooserDesdeFechaVto.getDateEditor().getUiComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jDateChooserHastaFechaVto.requestFocusInWindow();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Calendar c = new GregorianCalendar();

                String annio = Integer.toString(c.get(Calendar.YEAR));

                switch (((JTextField) jDateChooserDesdeFechaVto.getDateEditor().getUiComponent()).getText().length()) {
                    case 2:
                        ((JTextField) jDateChooserDesdeFechaVto.getDateEditor().getUiComponent()).setText(((JTextField) jDateChooserDesdeFechaVto.getDateEditor().getUiComponent()).getText() + "/");
                        break;
                    case 5:
                        ((JTextField) jDateChooserDesdeFechaVto.getDateEditor().getUiComponent()).setText(((JTextField) jDateChooserDesdeFechaVto.getDateEditor().getUiComponent()).getText() + "/20");
                        break;
                }
            }
        });
        jDateChooserDesdeFechaVto.getDateEditor().getUiComponent().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                mostrarMensajeFechaIncorrecta = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (mostrarMensajeFechaIncorrecta) {
                    Date f = jDateChooserDesdeFechaVto.getDate();
                    if (f == null) {
                        //JOptionPane.showMessageDialog(IngresarCompras.this, "Fecha Incorrecta. Ingrese una fecha correcta. Debe ingresar los dos numeros del dia, los dos numeros del mes y los dos ultimos numeros del año");
                        jLabelFechaVtoIncorrecta.setVisible(true);
                        jDateChooserDesdeFechaVto.getDateEditor().getUiComponent().requestFocus();
                    } else {
                        jLabelFechaVtoIncorrecta.setVisible(false);
                    }
                }
                mostrarMensajeFechaIncorrecta = true;
            }
        });
        jDateChooserDesdeFechaVto.getCalendarButton().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarMensajeFechaIncorrecta = false;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                jDateChooserDesdeFechaVto.getDateEditor().getUiComponent().requestFocus();
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

        jDateChooserHastaFechaVto.getDateEditor().getUiComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jButtonBuscar.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Calendar c = new GregorianCalendar();

                String annio = Integer.toString(c.get(Calendar.YEAR));

                switch (((JTextField) jDateChooserHastaFechaVto.getDateEditor().getUiComponent()).getText().length()) {
                    case 2:
                        ((JTextField) jDateChooserHastaFechaVto.getDateEditor().getUiComponent()).setText(((JTextField) jDateChooserHastaFechaVto.getDateEditor().getUiComponent()).getText() + "/");
                        break;
                    case 5:
                        ((JTextField) jDateChooserHastaFechaVto.getDateEditor().getUiComponent()).setText(((JTextField) jDateChooserHastaFechaVto.getDateEditor().getUiComponent()).getText() + "/20");
                        break;
                }
            }
        });
        jDateChooserHastaFechaVto.getDateEditor().getUiComponent().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                mostrarMensajeFechaIncorrecta = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (mostrarMensajeFechaIncorrecta) {
                    Date f = jDateChooserHastaFechaVto.getDate();
                    if (f == null) {
                        //JOptionPane.showMessageDialog(IngresarCompras.this, "Fecha Incorrecta. Ingrese una fecha correcta. Debe ingresar los dos numeros del dia, los dos numeros del mes y los dos ultimos numeros del año");
                        jLabelFechaVtoIncorrecta.setVisible(true);
                        jDateChooserHastaFechaVto.getDateEditor().getUiComponent().requestFocus();
                    } else {
                        jLabelFechaVtoIncorrecta.setVisible(false);
                    }
                }
                mostrarMensajeFechaIncorrecta = true;
            }
        });
        jDateChooserHastaFechaVto.getCalendarButton().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarMensajeFechaIncorrecta = false;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                jDateChooserHastaFechaVto.getDateEditor().getUiComponent().requestFocus();
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

        jLabelTitulo1 = new javax.swing.JLabel();
        jPanelFiltros = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jRadioButtonCliente = new javax.swing.JRadioButton();
        jRadioButtonFecha = new javax.swing.JRadioButton();
        jRadioButtonFechaVto = new javax.swing.JRadioButton();
        jButtonBuscar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCheques = new javax.swing.JTable();
        jLabelHastaFecha = new javax.swing.JLabel();
        jLabelDesdeFecha = new javax.swing.JLabel();
        jTextFieldCliente = new javax.swing.JTextField();
        jLabelCliente = new javax.swing.JLabel();
        jDateChooserDesdeFecha = new com.toedter.calendar.JDateChooser();
        jDateChooserHastaFecha = new com.toedter.calendar.JDateChooser();
        jLabelDesdeFechaVto = new javax.swing.JLabel();
        jLabelHastaFechaVto = new javax.swing.JLabel();
        jDateChooserDesdeFechaVto = new com.toedter.calendar.JDateChooser();
        jDateChooserHastaFechaVto = new com.toedter.calendar.JDateChooser();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jLabelFechaVtoIncorrecta = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Informe Cheques");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabelTitulo1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo1.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo1.setText("Cheques");

        jLabel1.setText("Filtros");

        jRadioButtonCliente.setText("Cliente");
        jRadioButtonCliente.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButtonClienteStateChanged(evt);
            }
        });

        jRadioButtonFecha.setText("Fecha");
        jRadioButtonFecha.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButtonFechaStateChanged(evt);
            }
        });

        jRadioButtonFechaVto.setText("Fecha Vto");
        jRadioButtonFechaVto.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButtonFechaVtoStateChanged(evt);
            }
        });

        jButtonBuscar.setText("Buscar");
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
        jButtonSalir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButtonSalirKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanelFiltrosLayout = new javax.swing.GroupLayout(jPanelFiltros);
        jPanelFiltros.setLayout(jPanelFiltrosLayout);
        jPanelFiltrosLayout.setHorizontalGroup(
            jPanelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelFiltrosLayout.createSequentialGroup()
                .addGroup(jPanelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButtonCliente)
                    .addComponent(jRadioButtonFecha)
                    .addComponent(jRadioButtonFechaVto))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanelFiltrosLayout.createSequentialGroup()
                .addGroup(jPanelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelFiltrosLayout.setVerticalGroup(
            jPanelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonFecha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonFechaVto)
                .addGap(18, 18, 18)
                .addComponent(jButtonBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTableCheques.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Cliente", "Fecha", "Fecha Vto", "Importe"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableCheques);

        jLabelHastaFecha.setText("Hasta Fecha:");

        jLabelDesdeFecha.setText("Desde Fecha:");

        jTextFieldCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldClienteKeyPressed(evt);
            }
        });

        jLabelCliente.setText("Cliente:");

        jDateChooserDesdeFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserDesdeFecha.setMinSelectableDate(new java.util.Date(946699278000L));

        jDateChooserHastaFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserHastaFecha.setMinSelectableDate(new java.util.Date(946699297000L));

        jLabelDesdeFechaVto.setText("Desde Fecha Vto:");

        jLabelHastaFechaVto.setText("Hasta Fecha Vto:");

        jDateChooserDesdeFechaVto.setDateFormatString("dd/MM/yyyy");
        jDateChooserDesdeFechaVto.setMinSelectableDate(new java.util.Date(946699299000L));

        jDateChooserHastaFechaVto.setDateFormatString("dd/MM/yyyy");
        jDateChooserHastaFechaVto.setMinSelectableDate(new java.util.Date(946699316000L));

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabelFechaVtoIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaVtoIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(259, 259, 259)
                                .addComponent(jLabelTitulo1))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanelFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabelDesdeFecha)
                                                    .addComponent(jLabelCliente)
                                                    .addComponent(jLabelHastaFecha)
                                                    .addComponent(jLabelDesdeFechaVto)
                                                    .addComponent(jLabelHastaFechaVto))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jTextFieldCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                                    .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                                    .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                                    .addComponent(jDateChooserHastaFechaVto, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                                    .addComponent(jDateChooserDesdeFechaVto, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(28, 28, 28)
                                                .addComponent(jLabelFechaIncorrecta))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(43, 43, 43)
                                        .addComponent(jLabelFechaVtoIncorrecta)))))
                        .addGap(0, 120, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCliente)
                            .addComponent(jTextFieldCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDesdeFecha))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelHastaFecha)
                            .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelFechaIncorrecta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelDesdeFechaVto)
                            .addComponent(jDateChooserDesdeFechaVto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelHastaFechaVto)
                            .addComponent(jDateChooserHastaFechaVto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelFechaVtoIncorrecta)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonClienteStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButtonClienteStateChanged
        // TODO add your handling code here:
        if(jRadioButtonCliente.isSelected()){
            mostrarCliente();
            jTextFieldCliente.requestFocus();
        } else {
            ocultarCliente();
        }
            
    }//GEN-LAST:event_jRadioButtonClienteStateChanged

    private void jRadioButtonFechaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButtonFechaStateChanged
        // TODO add your handling code here:
        if(jRadioButtonFecha.isSelected()){
            mostrarFecha();
            jDateChooserDesdeFecha.requestFocusInWindow();
        } else {
            ocultarFecha();
        }
    }//GEN-LAST:event_jRadioButtonFechaStateChanged

    private void jRadioButtonFechaVtoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButtonFechaVtoStateChanged
        // TODO add your handling code here:
        if(jRadioButtonFechaVto.isSelected()){
            mostrarFechaVto();
            jDateChooserDesdeFechaVto.requestFocusInWindow();
        } else {
            ocultarFechaVto();
        }
    }//GEN-LAST:event_jRadioButtonFechaVtoStateChanged

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
        // TODO add your handling code here:
        inicializarTablaCheques();
        yaBusco = true;
        if(jRadioButtonCliente.isSelected() && !jRadioButtonFecha.isSelected() && !jRadioButtonFechaVto.isSelected()){
            try {
                //Busco solo por cliente
                cheques = sisInformes.devolverChequesPorCliente(cliente);
                for(Cheque c : cheques){
                    cargarChequeEnTabla(c);
                }
            } catch (Exception exp) {
                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                JOptionPane.showMessageDialog(VentanaInformeCheques.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(!jRadioButtonCliente.isSelected() && jRadioButtonFecha.isSelected() && !jRadioButtonFechaVto.isSelected()){
            try {
                //Busco solo por fecha
                Date desdeFecha = jDateChooserDesdeFecha.getDate();
                Date hastaFecha = jDateChooserHastaFecha.getDate();
                cheques = sisInformes.devolverChequesEntreFechas(desdeFecha, hastaFecha);
                for(Cheque c : cheques){
                    cargarChequeEnTabla(c);
                }
            } catch (Exception exp) {
                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                JOptionPane.showMessageDialog(VentanaInformeCheques.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(!jRadioButtonCliente.isSelected() && !jRadioButtonFecha.isSelected() && jRadioButtonFechaVto.isSelected()){
            try {
                //Busco solo por fecha de vencimiento
                Date desdeFecha = jDateChooserDesdeFechaVto.getDate();
                Date hastaFecha = jDateChooserHastaFechaVto.getDate();
                cheques = sisInformes.devolverChequesEntreFechasVto(desdeFecha, hastaFecha);
                for(Cheque c : cheques){
                    cargarChequeEnTabla(c);
                }
            } catch (Exception exp) {
                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                JOptionPane.showMessageDialog(VentanaInformeCheques.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(!jRadioButtonCliente.isSelected() && jRadioButtonFecha.isSelected() && jRadioButtonFechaVto.isSelected()){
            try {
                //Busco por fecha y fecha de vencimiento
                Date desdeFecha = jDateChooserDesdeFecha.getDate();
                Date hastaFecha = jDateChooserHastaFecha.getDate();
                Date desdeFechaVto = jDateChooserDesdeFechaVto.getDate();
                Date hastaFechaVto = jDateChooserHastaFechaVto.getDate();
                cheques = sisInformes.devolverChequesEntreFechasYFechasDeVto(desdeFecha, hastaFecha, desdeFechaVto, hastaFechaVto);
                for(Cheque c : cheques){
                    cargarChequeEnTabla(c);
                }
            } catch (Exception exp) {
                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                JOptionPane.showMessageDialog(VentanaInformeCheques.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(jRadioButtonCliente.isSelected() && !jRadioButtonFecha.isSelected() && jRadioButtonFechaVto.isSelected()){
            try {
                //Busco cliente y fecha de vencimiento
                Date desdeFechaVto = jDateChooserDesdeFechaVto.getDate();
                Date hastaFechaVto = jDateChooserHastaFechaVto.getDate();
                cheques = sisInformes.devolverChequesPorClienteEntreFechasVto(cliente, desdeFechaVto, hastaFechaVto);
                for(Cheque c : cheques){
                    cargarChequeEnTabla(c);
                }
            } catch (Exception exp) {
                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                JOptionPane.showMessageDialog(VentanaInformeCheques.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(jRadioButtonCliente.isSelected() && jRadioButtonFecha.isSelected() && !jRadioButtonFechaVto.isSelected()){
            try {
                //Busco cliente y fecha
                Date desdeFecha = jDateChooserDesdeFecha.getDate();
                Date hastaFecha = jDateChooserHastaFecha.getDate();
                cheques = sisInformes.devolverChequesPorClienteEntreFechas(cliente, desdeFecha, hastaFecha);
                for(Cheque c : cheques){
                    cargarChequeEnTabla(c);
                }
            } catch (Exception exp) {
                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                JOptionPane.showMessageDialog(VentanaInformeCheques.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(!jRadioButtonCliente.isSelected() && !jRadioButtonFecha.isSelected() && !jRadioButtonFechaVto.isSelected()){
            //No puso ningun filtro. Seleccione alguno
            JOptionPane.showMessageDialog(VentanaInformeCheques.this, "Debe seleccionar al menos un flitro para la búsqueda." , "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    public void cargarChequeEnTabla(Cheque c){
        Object[] object = new Object[4];
        object[0] = c.getCliente();
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        object[1] = formatter.format(c.getFecha());
        object[2] = formatter.format(c.getFechaVencimiento());
        object[3] = df.format(c.getImporte()).replace(',', '.');
        modelo.addRow(object);
    }
    
    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jTextFieldClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldClienteKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if("".equals(jTextFieldCliente.getText().trim())){
                //Es vacio. Le habro el buscador
                VentanaBuscadorClienteTodos vbc = new VentanaBuscadorClienteTodos(VentanaInformeCheques.this, true);
                vbc.setVisible(true);
                this.cliente = vbc.getCliente();
                jTextFieldCliente.setText(cliente.getNombre());
                jTextFieldCliente.setEditable(false);
                if(jRadioButtonFecha.isSelected()){
                    jDateChooserDesdeFecha.requestFocusInWindow();
                } else {
                    if(jRadioButtonFechaVto.isSelected()){
                        jDateChooserDesdeFechaVto.requestFocusInWindow();
                    } else {
                        jButtonBuscar.requestFocus();
                    }
                }
            } else {
                //No es vacio. Busco la cadena en los rut de cliente, si conincide la muestro si no pregunto si quiere volver a ingresarlo o abrir el buscador de clientes.
                if(cliente != null){
                    if(cliente.getNombre().equals(jTextFieldCliente.getText().trim())){
                        jTextFieldCliente.setText(cliente.getNombre());
                        jTextFieldCliente.setEditable(false);
                        if (jRadioButtonFecha.isSelected()) {
                            jDateChooserDesdeFecha.requestFocusInWindow();
                        } else if (jRadioButtonFechaVto.isSelected()) {
                            jDateChooserDesdeFechaVto.requestFocusInWindow();
                        } else {
                            jButtonBuscar.requestFocus();
                        }
                    }
                } else {
                    String fraccionDelRut = jTextFieldCliente.getText().trim();
                    cliente = sisMantenimiento.devolverClientePorFraccionDelRut(fraccionDelRut);
                    if (cliente != null) {
                        jTextFieldCliente.setText(cliente.getNombre());
                        jTextFieldCliente.setEditable(false);
                        if (jRadioButtonFecha.isSelected()) {
                            jDateChooserDesdeFecha.requestFocusInWindow();
                        } else if (jRadioButtonFechaVto.isSelected()) {
                            jDateChooserDesdeFechaVto.requestFocusInWindow();
                        } else {
                            jButtonBuscar.requestFocus();
                        }
                    } else {
                        int resp = JOptionPane.showConfirmDialog(this, "No existe un cliente que contenga en el rut: " + fraccionDelRut + " . Desea abrir el buscador de clientes?  ", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (resp == JOptionPane.YES_OPTION) {
                            VentanaBuscadorClienteTodos vbc = new VentanaBuscadorClienteTodos(VentanaInformeCheques.this, true);
                            vbc.setVisible(true);
                            this.cliente = vbc.getCliente();
                            jTextFieldCliente.setText(cliente.getNombre());
                            jTextFieldCliente.setEditable(false);
                            if (jRadioButtonFecha.isSelected()) {
                                jDateChooserDesdeFecha.requestFocusInWindow();
                            } else if (jRadioButtonFechaVto.isSelected()) {
                                jDateChooserDesdeFechaVto.requestFocusInWindow();
                            } else {
                                jButtonBuscar.requestFocus();
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

    private void jButtonBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonBuscarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jButtonBuscarKeyPressed

    private void jButtonSalirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonSalirKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonSalir.doClick();
        }
    }//GEN-LAST:event_jButtonSalirKeyPressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        if (yaBusco) {
            jButtonBuscar.doClick();
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
            java.util.logging.Logger.getLogger(VentanaInformeCheques.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaInformeCheques.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaInformeCheques.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaInformeCheques.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaInformeCheques dialog = new VentanaInformeCheques(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonSalir;
    private com.toedter.calendar.JDateChooser jDateChooserDesdeFecha;
    private com.toedter.calendar.JDateChooser jDateChooserDesdeFechaVto;
    private com.toedter.calendar.JDateChooser jDateChooserHastaFecha;
    private com.toedter.calendar.JDateChooser jDateChooserHastaFechaVto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelCliente;
    private javax.swing.JLabel jLabelDesdeFecha;
    private javax.swing.JLabel jLabelDesdeFechaVto;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelFechaVtoIncorrecta;
    private javax.swing.JLabel jLabelHastaFecha;
    private javax.swing.JLabel jLabelHastaFechaVto;
    private javax.swing.JLabel jLabelTitulo1;
    private javax.swing.JPanel jPanelFiltros;
    private javax.swing.JRadioButton jRadioButtonCliente;
    private javax.swing.JRadioButton jRadioButtonFecha;
    private javax.swing.JRadioButton jRadioButtonFechaVto;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableCheques;
    private javax.swing.JTextField jTextFieldCliente;
    // End of variables declaration//GEN-END:variables
}
