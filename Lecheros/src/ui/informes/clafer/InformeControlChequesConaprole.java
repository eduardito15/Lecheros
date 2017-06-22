/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.informes.clafer;

import ui.liquidaciones.AsignarFechaDeLiquidacionACompra;
import dominio.Compra;
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
import org.hibernate.HibernateException;
import sistema.SistemaCompras;
import sistema.SistemaInformesClafer;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.compras.IngresarCompras;
import ui.informes.VentanaInformeCheques;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class InformeControlChequesConaprole extends javax.swing.JFrame {

    private final SistemaMantenimiento sisMantenimiento;
    private final SistemaInformesClafer sisInformes;
    private final SistemaCompras sisCompras;
    
    private List<Compra> comprasLeche;
    private List<Compra> comprasEnvases;
    private List<Compra> comprasProductos;
    
    private DefaultTableModel modeloComprasLeche;
    private DefaultTableModel modeloComprasEnvases;
    private DefaultTableModel modeloComprasProductos;
    private DefaultTableModel modeloDetalle;
    
    private DecimalFormat df;
    private boolean mostrarMensajeFechaIncorrecta = true;
    /**
     * Creates new form InformeControlChequesConaprole
     */
    public InformeControlChequesConaprole(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        sisMantenimiento = SistemaMantenimiento.getInstance();
        sisInformes = SistemaInformesClafer.getInstance();
        sisCompras = SistemaCompras.getInstance();
        df = new DecimalFormat("0.00");
        jLabelEspera.setVisible(false);
        jComboBoxReparto.addItem("");
        List<Reparto> repartos = sisMantenimiento.devolverRepartos();
        for (Reparto c : repartos) {
            jComboBoxReparto.addItem(c);
        }
        agregarEnterCampoFecha();
        inicializarTablaComprasLeche();
        inicializarTablaComprasEnvases();
        inicializarTablaComprasProductos();
        inicializarTablaDetalles();
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
    
    public final void inicializarTablaComprasLeche() {
        modeloComprasLeche = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloComprasLeche.addColumn("Tipo");
        modeloComprasLeche.addColumn("Numero");
        modeloComprasLeche.addColumn("Fecha");
        modeloComprasLeche.addColumn("Total");
        jTableComprasLeche.setModel(modeloComprasLeche);
        
        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem verItem = new JMenuItem("Ver");
        verItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da ver en el menu con click derecho sobre la fila de la tabla
                IngresarCompras ic = new IngresarCompras(InformeControlChequesConaprole.this, false);
                ic.setAccion("Ver");
                Compra c = comprasLeche.get(jTableComprasLeche.getSelectedRow());
                ic.setCompra(c);
                ic.setTipoDoc(c.getTipoDocumento());
                ic.setVisible(true);
            }

        });
        popupMenu.add(verItem);
        JMenuItem modificarItem = new JMenuItem("Modificar");
        modificarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da modificar en el menu con click derecho sobre la fila de la tabla
                IngresarCompras ic = new IngresarCompras(InformeControlChequesConaprole.this, false);
                ic.setAccion("Modificar");
                Compra c = comprasLeche.get(jTableComprasLeche.getSelectedRow());
                ic.setCompra(c);
                ic.setTipoDoc(c.getTipoDocumento());
                ic.setVisible(true);
            }

        });
        popupMenu.add(modificarItem);
        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        eliminarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da eliminar en el menu con click derecho sobre la fila de la tabla
                Compra c = comprasLeche.get(jTableComprasLeche.getSelectedRow());
                int resp = JOptionPane.showConfirmDialog(InformeControlChequesConaprole.this, "Seguro que quiere eliminar la compra " + c.getTipoDocumento().getTipoDocumento() + " con el numero " + c.getNumero() + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    try {
                        if (sisCompras.eliminarCompra(c)) {
                            JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, "La compra se elimino correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                            jButtonVer.doClick();
                        }
                    } catch (HibernateException he) {
                        JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, "Error al eliminar la compra." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        popupMenu.add(eliminarItem);
        JMenuItem agregarFechaLiquidacion = new JMenuItem("Agregar Fecha de Liquidación");
        agregarFechaLiquidacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Compra c = comprasLeche.get(jTableComprasLeche.getSelectedRow());
                AsignarFechaDeLiquidacionACompra vaflc = new AsignarFechaDeLiquidacionACompra(InformeControlChequesConaprole.this, true);
                vaflc.setC(c);
                vaflc.setVisible(true);
            }

        });
        popupMenu.add(agregarFechaLiquidacion);
        JMenuItem sacarDeLiquidacionItem = new JMenuItem("Sacar de Liquidación");
        sacarDeLiquidacionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da sacar de liquidacion en el menu con click derecho sobre la fila de la tabla
                Compra c = comprasLeche.get(jTableComprasLeche.getSelectedRow());
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("dd-MM-yyyy");
                int resp = JOptionPane.showConfirmDialog(InformeControlChequesConaprole.this, "Seguro que quiere sacar la compra de la liquidación del " + formatter.format(c.getFechaLiquidacion()) + " del reparto:  " + c.getReparto() + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    try {
                        c.setFechaLiquidacion(null);
                        if (sisCompras.actualizarCompra(c)) {
                            JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, "La compra se saco correctamente de la liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
                            jButtonVer.doClick();
                        }
                    } catch (HibernateException he) {
                        JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, "Error al sacar la compra de la liquidación." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        popupMenu.add(sacarDeLiquidacionItem);
        jTableComprasLeche.setComponentPopupMenu(popupMenu);
    }
    
    public final void inicializarTablaComprasEnvases() {
        modeloComprasEnvases = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloComprasEnvases.addColumn("Tipo");
        modeloComprasEnvases.addColumn("Numero");
        modeloComprasEnvases.addColumn("Fecha");
        modeloComprasEnvases.addColumn("Total");
        jTableComprasEnvases.setModel(modeloComprasEnvases);

        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem verItem = new JMenuItem("Ver");
        verItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da ver en el menu con click derecho sobre la fila de la tabla
                IngresarCompras ic = new IngresarCompras(InformeControlChequesConaprole.this, false);
                ic.setAccion("Ver");
                Compra c = comprasEnvases.get(jTableComprasEnvases.getSelectedRow());
                ic.setCompra(c);
                ic.setTipoDoc(c.getTipoDocumento());
                ic.setVisible(true);
            }

        });
        popupMenu.add(verItem);
        JMenuItem modificarItem = new JMenuItem("Modificar");
        modificarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da modificar en el menu con click derecho sobre la fila de la tabla
                IngresarCompras ic = new IngresarCompras(InformeControlChequesConaprole.this, false);
                ic.setAccion("Modificar");
                Compra c = comprasEnvases.get(jTableComprasEnvases.getSelectedRow());
                ic.setCompra(c);
                ic.setTipoDoc(c.getTipoDocumento());
                ic.setVisible(true);
            }

        });
        popupMenu.add(modificarItem);
        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        eliminarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da eliminar en el menu con click derecho sobre la fila de la tabla
                Compra c = comprasEnvases.get(jTableComprasEnvases.getSelectedRow());
                int resp = JOptionPane.showConfirmDialog(InformeControlChequesConaprole.this, "Seguro que quiere eliminar la compra " + c.getTipoDocumento().getTipoDocumento() + " con el numero " + c.getNumero() + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    try {
                        if (sisCompras.eliminarCompra(c)) {
                            JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, "La compra se elimino correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                            jButtonVer.doClick();
                        }
                    } catch (HibernateException he) {
                        JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, "Error al eliminar la compra." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        popupMenu.add(eliminarItem);
        JMenuItem agregarFechaLiquidacion = new JMenuItem("Agregar Fecha de Liquidación");
        agregarFechaLiquidacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Compra c = comprasEnvases.get(jTableComprasEnvases.getSelectedRow());
                AsignarFechaDeLiquidacionACompra vaflc = new AsignarFechaDeLiquidacionACompra(InformeControlChequesConaprole.this, true);
                vaflc.setC(c);
                vaflc.setVisible(true);
            }

        });
        popupMenu.add(agregarFechaLiquidacion);
        JMenuItem sacarDeLiquidacionItem = new JMenuItem("Sacar de Liquidación");
        sacarDeLiquidacionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da sacar de liquidacion en el menu con click derecho sobre la fila de la tabla
                Compra c = comprasEnvases.get(jTableComprasEnvases.getSelectedRow());
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("dd-MM-yyyy");
                int resp = JOptionPane.showConfirmDialog(InformeControlChequesConaprole.this, "Seguro que quiere sacar la compra de la liquidación del " + formatter.format(c.getFechaLiquidacion()) + " del reparto:  " + c.getReparto() + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    try {
                        c.setFechaLiquidacion(null);
                        if (sisCompras.actualizarCompra(c)) {
                            JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, "La compra se saco correctamente de la liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
                            jButtonVer.doClick();
                        }
                    } catch (HibernateException he) {
                        JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, "Error al sacar la compra de la liquidación." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        popupMenu.add(sacarDeLiquidacionItem);
        jTableComprasEnvases.setComponentPopupMenu(popupMenu);
    }
    
    public final void inicializarTablaComprasProductos() {
        modeloComprasProductos = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloComprasProductos.addColumn("Tipo");
        modeloComprasProductos.addColumn("Numero");
        modeloComprasProductos.addColumn("Fecha");
        modeloComprasProductos.addColumn("Total");
        jTableComprasProductos.setModel(modeloComprasProductos);

        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem verItem = new JMenuItem("Ver");
        verItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da ver en el menu con click derecho sobre la fila de la tabla
                IngresarCompras ic = new IngresarCompras(InformeControlChequesConaprole.this, false);
                ic.setAccion("Ver");
                Compra c = comprasProductos.get(jTableComprasProductos.getSelectedRow());
                ic.setCompra(c);
                ic.setTipoDoc(c.getTipoDocumento());
                ic.setVisible(true);
            }

        });
        popupMenu.add(verItem);
        JMenuItem modificarItem = new JMenuItem("Modificar");
        modificarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da modificar en el menu con click derecho sobre la fila de la tabla
                IngresarCompras ic = new IngresarCompras(InformeControlChequesConaprole.this, false);
                ic.setAccion("Modificar");
                Compra c = comprasProductos.get(jTableComprasProductos.getSelectedRow());
                ic.setCompra(c);
                ic.setTipoDoc(c.getTipoDocumento());
                ic.setVisible(true);
            }

        });
        popupMenu.add(modificarItem);
        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        eliminarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da eliminar en el menu con click derecho sobre la fila de la tabla
                Compra c = comprasProductos.get(jTableComprasProductos.getSelectedRow());
                int resp = JOptionPane.showConfirmDialog(InformeControlChequesConaprole.this, "Seguro que quiere eliminar la compra " + c.getTipoDocumento().getTipoDocumento() + " con el numero " + c.getNumero() + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    try {
                        if (sisCompras.eliminarCompra(c)) {
                            JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, "La compra se elimino correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                            jButtonVer.doClick();
                        }
                    } catch (HibernateException he) {
                        JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, "Error al eliminar la compra." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        popupMenu.add(eliminarItem);
        JMenuItem agregarFechaLiquidacion = new JMenuItem("Agregar Fecha de Liquidación");
        agregarFechaLiquidacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Compra c = comprasProductos.get(jTableComprasProductos.getSelectedRow());
                AsignarFechaDeLiquidacionACompra vaflc = new AsignarFechaDeLiquidacionACompra(InformeControlChequesConaprole.this, true);
                vaflc.setC(c);
                vaflc.setVisible(true);
            }

        });
        popupMenu.add(agregarFechaLiquidacion);
        JMenuItem sacarDeLiquidacionItem = new JMenuItem("Sacar de Liquidación");
        sacarDeLiquidacionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da sacar de liquidacion en el menu con click derecho sobre la fila de la tabla
                Compra c = comprasProductos.get(jTableComprasProductos.getSelectedRow());
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("dd-MM-yyyy");
                int resp = JOptionPane.showConfirmDialog(InformeControlChequesConaprole.this, "Seguro que quiere sacar la compra de la liquidación del " + formatter.format(c.getFechaLiquidacion()) + " del reparto:  " + c.getReparto() + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    try {
                        c.setFechaLiquidacion(null);
                        if (sisCompras.actualizarCompra(c)) {
                            JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, "La compra se saco correctamente de la liquidación", "Información", JOptionPane.INFORMATION_MESSAGE);
                            jButtonVer.doClick();
                        }
                    } catch (HibernateException he) {
                        JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, "Error al sacar la compra de la liquidación." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        popupMenu.add(sacarDeLiquidacionItem);
        jTableComprasProductos.setComponentPopupMenu(popupMenu);
    }
    
    private void inicializarTablaDetalles(){
        modeloDetalle = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloDetalle.addColumn("Concepto");
        modeloDetalle.addColumn("Leche");
        modeloDetalle.addColumn("Envases");
        modeloDetalle.addColumn("Productos");
        modeloDetalle.addColumn("Total");
        jTableDetalles.setModel(modeloDetalle);
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
        jLabel1 = new javax.swing.JLabel();
        jDateChooserFecha = new com.toedter.calendar.JDateChooser();
        jComboBoxReparto = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jButtonVer = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabelEspera = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabelTotalLeche = new javax.swing.JLabel();
        jLabelTotalEnvases = new javax.swing.JLabel();
        jLabelTotalProductos = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableComprasLeche = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableComprasEnvases = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableComprasProductos = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableDetalles = new javax.swing.JTable();
        jLabelTitulo1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Control de Cheques Conaprole");

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Contros de Cheques Conaprole");

        jLabel1.setText("Fecha:");

        jDateChooserFecha.setDateFormatString("dd/MM/yyyy");

        jComboBoxReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxRepartoKeyPressed(evt);
            }
        });

        jLabel2.setText("Reparto:");

        jButtonVer.setText("Ver");
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

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        jLabel3.setText("Total Leche:");

        jLabel4.setText("Total Envases:");

        jLabel5.setText("Total Productos:");

        jLabelTotalLeche.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelTotalLeche.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTotalLeche.setText("0");

        jLabelTotalEnvases.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelTotalEnvases.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTotalEnvases.setText("0");

        jLabelTotalProductos.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelTotalProductos.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTotalProductos.setText("0");

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel6.setText("Total:");

        jLabelTotal.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTotal.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTotal.setText("0");

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jTableComprasLeche.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tipo", "Numero", "Fecha", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableComprasLeche);

        jTableComprasEnvases.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tipo", "Numero", "Fecha", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableComprasEnvases);

        jTableComprasProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tipo", "Numero", "Fecha", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTableComprasProductos);

        jTableDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Concepto", "Leche", "Envases", "Productos", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTableDetalles);
        if (jTableDetalles.getColumnModel().getColumnCount() > 0) {
            jTableDetalles.getColumnModel().getColumn(0).setResizable(false);
            jTableDetalles.getColumnModel().getColumn(1).setResizable(false);
            jTableDetalles.getColumnModel().getColumn(2).setResizable(false);
            jTableDetalles.getColumnModel().getColumn(3).setResizable(false);
            jTableDetalles.getColumnModel().getColumn(4).setResizable(false);
        }

        jLabelTitulo1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabelTitulo1.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo1.setText("Detalle por Reparto");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelTotalProductos))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelTotalEnvases)))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabelTitulo)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBoxReparto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jDateChooserFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonVer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabelEspera))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelFechaIncorrecta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelTitulo1)
                                .addGap(188, 188, 188)))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelTotalLeche)
                .addGap(105, 105, 105)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelTotal)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelEspera)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelTitulo)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jDateChooserFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonVer))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jButtonSalir))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelFechaIncorrecta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabelTotalLeche)
                            .addComponent(jLabel6)
                            .addComponent(jLabelTotal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabelTotalEnvases))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabelTotalProductos))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabelTitulo1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxRepartoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonVer.requestFocus();
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

    private void jButtonVerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonVerKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonVer.doClick();
        }
    }//GEN-LAST:event_jButtonVerKeyPressed

    private void jButtonSalirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonSalirKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonSalir.requestFocus();
        }
    }//GEN-LAST:event_jButtonSalirKeyPressed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jButtonVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVerActionPerformed
        // TODO add your handling code here:
        inicializarTablaComprasLeche();
        inicializarTablaComprasEnvases();
        inicializarTablaComprasProductos();
        jLabelTotalLeche.setText("0");
        jLabelTotalEnvases.setText("0");
        jLabelTotalProductos.setText("0");
        jLabelTotal.setText("0");
        Date fecha = jDateChooserFecha.getDate();
        Reparto reparto;
        if(jComboBoxReparto.getSelectedIndex()==0){
            reparto = null;
        } else {
            reparto = (Reparto)jComboBoxReparto.getSelectedItem();
        }
        jLabelEspera.setVisible(true);
        jDateChooserFecha.setEnabled(false);
        jComboBoxReparto.setEnabled(false);
        jButtonVer.setEnabled(false);
        Thread worker = new Thread() {
                    public void run() {
                        try {
                            Object[][] informe = sisInformes.informeControlChequesConaproleDetallado(fecha, reparto);
                            cargarDetalles(informe);
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    comprasLeche = (List<Compra>)informe[informe.length-1][0];
                                    comprasEnvases = (List<Compra>)informe[informe.length-1][1];
                                    comprasProductos = (List<Compra>)informe[informe.length-1][2];
                                    double totalLeche = 0;
                                    double totalProductos = 0;
                                    double totalEnvases = 0;
                                    for(Compra c: comprasLeche){
                                        totalLeche = totalLeche + c.getTotal();
                                        cargarCompraLecheEnTabla(c);
                                    }
                                    for(Compra c: comprasEnvases){
                                        totalEnvases = totalEnvases + c.getTotal();
                                        cargarCompraEnvaseEnTabla(c);
                                    }
                                    for(Compra c: comprasProductos){
                                        totalProductos = totalProductos + c.getTotal();
                                        cargarCompraProductosEnTabla(c);
                                    }
                                    jLabelTotalLeche.setText(df.format(totalLeche).replace(',', '.'));
                                    jLabelTotalEnvases.setText(df.format(totalEnvases).replace(',', '.'));
                                    jLabelTotalProductos.setText(df.format(totalProductos).replace(',', '.'));
                                    double total = totalLeche + totalEnvases + totalProductos;
                                    jLabelTotal.setText(df.format(total).replace(',', '.'));
                                    jLabelEspera.setVisible(false);
                                    jDateChooserFecha.setEnabled(true);
                                    jComboBoxReparto.setEnabled(true);
                                    jButtonVer.setEnabled(true);
                                }
                            });
                        } catch (Exception exp) {
                            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                            JOptionPane.showMessageDialog(InformeControlChequesConaprole.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
        };
        worker.start();
    }//GEN-LAST:event_jButtonVerActionPerformed

    public void cargarDetalles(Object[][] informe) {
        for (int i = 0; i < informe.length-1; i++) {
            Object[] object = new Object[informe[i].length];
            for (int j = 0; j < informe[i].length; j++) {
                if(j>0) {
                    object[j] = df.format(informe[i][j]).replace(',', '.');
                } else {
                    if(j == 0) {
                        object[j] = informe[i][j];
                    }
                }
            }
            modeloDetalle.addRow(object);
        }
    }
    
    public void cargarCompraLecheEnTabla(Compra c){
        Object[] object = new Object[4];
        object[0] = c.getTipoDocumento().getTipoDocumento();
        object[1] = c.getNumero();
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        object[2] = formatter.format(c.getFecha());
        if(c.getTipoDocumento().isSuma()){
            object[3] = df.format(c.getTotal()).replace(',', '.');
        } else {
            object[3] = "-" + df.format(c.getTotal()).replace(',', '.');
        }
        modeloComprasLeche.addRow(object);
    }
    
    public void cargarCompraEnvaseEnTabla(Compra c){
        Object[] object = new Object[4];
        object[0] = c.getTipoDocumento().getTipoDocumento();
        object[1] = c.getNumero();
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        object[2] = formatter.format(c.getFecha());
        if(c.getTipoDocumento().isSuma()){
            object[3] = df.format(c.getTotal()).replace(',', '.');
        } else {
            object[3] = "-" + df.format(c.getTotal()).replace(',', '.');
        }
        modeloComprasEnvases.addRow(object);
    }
    
    public void cargarCompraProductosEnTabla(Compra c){
        Object[] object = new Object[4];
        object[0] = c.getTipoDocumento().getTipoDocumento();
        object[1] = c.getNumero();
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        object[2] = formatter.format(c.getFecha());
        if(c.getTipoDocumento().isSuma()){
            object[3] = df.format(c.getTotal()).replace(',', '.');
        } else {
            object[3] = "- " + df.format(c.getTotal()).replace(',', '.');
        }
        modeloComprasProductos.addRow(object);
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
            java.util.logging.Logger.getLogger(InformeControlChequesConaprole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InformeControlChequesConaprole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InformeControlChequesConaprole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InformeControlChequesConaprole.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InformeControlChequesConaprole dialog = new InformeControlChequesConaprole(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonVer;
    private javax.swing.JComboBox jComboBoxReparto;
    private com.toedter.calendar.JDateChooser jDateChooserFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTitulo1;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JLabel jLabelTotalEnvases;
    private javax.swing.JLabel jLabelTotalLeche;
    private javax.swing.JLabel jLabelTotalProductos;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTableComprasEnvases;
    private javax.swing.JTable jTableComprasLeche;
    private javax.swing.JTable jTableComprasProductos;
    private javax.swing.JTable jTableDetalles;
    // End of variables declaration//GEN-END:variables
}
