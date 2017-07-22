/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.compras;

import dominio.Reparto;
import dominio.Compra;
import dominio.DocumentoDeCompra;
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
import sistema.SistemaCompras;
import sistema.SistemaMantenimiento;
import sistema.SistemaUsuarios;
import ui.liquidaciones.AsignarFechaDeLiquidacionACompra;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class MantenimientoCompras extends javax.swing.JFrame {

    private final SistemaMantenimiento sis;
    private final SistemaCompras sisCompras;
    private List<Compra> compras;
    private DefaultTableModel modelo;
    
    private DecimalFormat df;
    
    private boolean yaBusco;
    private boolean mostrarMensajeFechaIncorrecta = true;
    
    /**
     * Creates new form MantenimientoCompras
     */
    public MantenimientoCompras(java.awt.Frame parent, boolean modal) {
        initComponents();
        sis = SistemaMantenimiento.getInstance();
        sisCompras = SistemaCompras.getInstance();
        jComboBoxReparto.addItem("");
        List<Reparto> repartos = sis.devolverRepartos();
        for(Reparto c:repartos){
            jComboBoxReparto.addItem(c);
        }
        jComboBoxTipoDocumento.addItem("");
        List<DocumentoDeCompra> tiposDocumentos = sis.devolverDocumentosDeCompra();
        for(DocumentoDeCompra dc : tiposDocumentos){
            jComboBoxTipoDocumento.addItem(dc);
        }
        compras = new ArrayList<>();
        inicializarTablaCompras();
        agregarEnterCamposFecha();
        df = new DecimalFormat("0.00");
        Calendar cal = Calendar.getInstance();
        int mes = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        Calendar calPrimerDiaMes = Calendar.getInstance();
        calPrimerDiaMes.set(year, mes, 1);
        jDateChooserDesdeFecha.setDate(calPrimerDiaMes.getTime());
        jDateChooserHastaFecha.setDate(cal.getTime());
        jLabelEspera.setVisible(false);
        yaBusco = false;
    }
    
    public final void agregarEnterCamposFecha(){
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
                    }  else {
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
    
    public final void inicializarTablaCompras() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Documento");
        modelo.addColumn("Numero");
        modelo.addColumn("Fecha");
        modelo.addColumn("Reparto");
        modelo.addColumn("SubTotal");
        modelo.addColumn("Iva Minimo");
        modelo.addColumn("Iva Basico");
        modelo.addColumn("Total");
        jTableCompras.setModel(modelo);
        TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = jTableCompras.getColumnModel().getColumn(i);
            if (i == 0 || i == 3) {
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
               IngresarCompras ic = new IngresarCompras(MantenimientoCompras.this, false);
               ic.setAccion("Ver");
               Compra c = compras.get(jTableCompras.getSelectedRow());
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
                try {
                    if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadIngresarCompras)) {
                        IngresarCompras ic = new IngresarCompras(MantenimientoCompras.this, false);
                        ic.setAccion("Modificar");
                        Compra c = compras.get(jTableCompras.getSelectedRow());
                        ic.setCompra(c);
                        ic.setTipoDoc(c.getTipoDocumento());
                        ic.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
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
                if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadIngresarCompras)) {   
                
                    Compra c = compras.get(jTableCompras.getSelectedRow());
                    int resp = JOptionPane.showConfirmDialog(MantenimientoCompras.this, "Seguro que quiere eliminar la compra " + c.getTipoDocumento().getTipoDocumento() + " con el numero " + c.getNumero()   + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(resp == JOptionPane.YES_OPTION){
                        try{
                            if(sisCompras.eliminarCompra(c)){
                                JOptionPane.showMessageDialog(MantenimientoCompras.this, "La compra se elimino correctamente" , "Información", JOptionPane.INFORMATION_MESSAGE);
                                jButtonBuscar.doClick();
                            } 
                        } 
                        catch (HibernateException he){
                            JOptionPane.showMessageDialog(MantenimientoCompras.this, "Error al eliminar la compra." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception exp) {
                            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                            JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                
                } else {
                    JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception exp) {
                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            } 
        }

        });
        popupMenu.add(eliminarItem);
        JMenuItem agregarFechaLiquidacion = new JMenuItem("Agregar Fecha de Liquidación");
        agregarFechaLiquidacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadAgregarBoletaALiquidacion)) {  
                    Compra c = compras.get(jTableCompras.getSelectedRow());
                    AsignarFechaDeLiquidacionACompra vaflc = new AsignarFechaDeLiquidacionACompra(MantenimientoCompras.this, true);
                    vaflc.setC(c);
                    vaflc.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                }
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        });
        popupMenu.add(agregarFechaLiquidacion);
        JMenuItem sacarDeLiquidacionItem = new JMenuItem("Sacar de Liquidación");
        sacarDeLiquidacionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da sacar de liquidacion en el menu con click derecho sobre la fila de la tabla
                try {
                if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadQuitarBoletaDeLiquidacion)) {  
                    Compra c = compras.get(jTableCompras.getSelectedRow());
                    SimpleDateFormat formatter;
                    formatter = new SimpleDateFormat("dd-MM-yyyy");
                    int resp = JOptionPane.showConfirmDialog(MantenimientoCompras.this, "Seguro que quiere sacar la compra de la liquidación del " + formatter.format(c.getFechaLiquidacion())  + " del reparto:  " + c.getReparto()   + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(resp == JOptionPane.YES_OPTION){
                        try{
                            c.setFechaLiquidacion(null);
                            if(sisCompras.actualizarCompra(c)){
                                JOptionPane.showMessageDialog(MantenimientoCompras.this, "La compra se saco correctamente de la liquidación" , "Información", JOptionPane.INFORMATION_MESSAGE);
                                jButtonBuscar.doClick();
                            } 
                        } 
                        catch (HibernateException he){
                            JOptionPane.showMessageDialog(MantenimientoCompras.this, "Error al sacar la compra de la liquidación." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception exp) {
                            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                            JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } 
                } else {
                    JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception exp) {
                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
            }
        });
        popupMenu.add(sacarDeLiquidacionItem);
        jTableCompras.setComponentPopupMenu(popupMenu);
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
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCompras = new javax.swing.JTable();
        jTextFieldNumero = new javax.swing.JTextField();
        jComboBoxTipoDocumento = new javax.swing.JComboBox();
        jComboBoxReparto = new javax.swing.JComboBox();
        jButtonBuscar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jDateChooserDesdeFecha = new com.toedter.calendar.JDateChooser();
        jDateChooserHastaFecha = new com.toedter.calendar.JDateChooser();
        jLabelFechaIncorrecta = new javax.swing.JLabel();
        jLabelHastaFechaIncorrecta = new javax.swing.JLabel();
        jLabelEspera = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mantenimiento Compras");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLabelTitulo.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitulo.setText("Compras");

        jLabel1.setText("Numero:");

        jLabel3.setText("Tipo Documento:");

        jLabel4.setText("Reparto:");

        jTableCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Documento", "Numero", "Fecha", "Reparto", "SubTotal", "Iva Minimo", "Iva Basico", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableCompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableComprasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableCompras);

        jTextFieldNumero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNumeroKeyPressed(evt);
            }
        });

        jComboBoxTipoDocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxTipoDocumentoKeyPressed(evt);
            }
        });

        jComboBoxReparto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBoxRepartoKeyPressed(evt);
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

        jLabel2.setText("Desde Fecha:");

        jLabel5.setText("Hasta Fecha:");

        jDateChooserDesdeFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserDesdeFecha.setMaxSelectableDate(new java.util.Date(4102455662000L));
        jDateChooserDesdeFecha.setMinSelectableDate(new java.util.Date(946699285000L));

        jDateChooserHastaFecha.setDateFormatString("dd/MM/yyyy");
        jDateChooserHastaFecha.setMaxSelectableDate(new java.util.Date(4102455662000L));
        jDateChooserHastaFecha.setMinSelectableDate(new java.util.Date(946699274000L));

        jLabelFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabelHastaFechaIncorrecta.setForeground(new java.awt.Color(255, 0, 51));
        jLabelHastaFechaIncorrecta.setText("Fecha Incorrecta. Ingrese DiaDiaMesMesAñoAño");

        jLabelEspera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/wait_progress.gif"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1054, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldNumero)
                    .addComponent(jComboBoxTipoDocumento, 0, 200, Short.MAX_VALUE)
                    .addComponent(jComboBoxReparto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitulo)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelFechaIncorrecta)
                            .addComponent(jLabelHastaFechaIncorrecta)
                            .addComponent(jLabelEspera))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabelTitulo)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooserDesdeFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabelFechaIncorrecta)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jDateChooserHastaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jButtonSalir))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxReparto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonBuscar)
                            .addComponent(jLabelHastaFechaIncorrecta))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelEspera)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonBuscarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonBuscar.doClick();
        }
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jButtonBuscarKeyPressed

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
        // TODO add your handling code here:
        yaBusco = true;
        inicializarTablaCompras();
        compras = new ArrayList<>();
        if("".equals(jTextFieldNumero.getText().trim()) && jDateChooserDesdeFecha.getDate() == null && jDateChooserHastaFecha.getDate() == null && jComboBoxTipoDocumento.getSelectedItem()=="" && jComboBoxReparto.getSelectedItem() == ""){
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un filtro para la búsqueda.", "Información", JOptionPane.INFORMATION_MESSAGE);
            jTextFieldNumero.requestFocus();
            jTextFieldNumero.selectAll();
        }
        if(!"".equals(jTextFieldNumero.getText().trim())){
            if(jComboBoxReparto.getSelectedItem() == "" && jComboBoxTipoDocumento.getSelectedItem() == ""){
                //No es vacio el numero. Entonces busco por numero y son vacios todos los demas
                ponerVentanaEnEspera();
                Thread worker = new Thread() {
                    public void run() {
                        
                        try {
                            compras = sisCompras.devolverComprasPorNumero(Long.parseLong(jTextFieldNumero.getText().trim()));
                        } catch (Exception exp) {
                            String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                            SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                            JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                sacarVentanaDeEspera();
                                for (Compra c : compras) {
                                    //Cargo la tabla de la interfaz
                                    cargarCompraEnTabla(c);
                                }
                            }
                        });
                    }
                };
                worker.start();
            } else {
                try {
                if(jComboBoxReparto.getSelectedItem() == ""){
                    //Si es vacio el de reparto es por que no es vacio el de tipo de documento. Busco por numero y tipo de documento
            
                    Compra c = sisCompras.devolverCompraPorNumeroYTipo(Long.parseLong(jTextFieldNumero.getText().trim()), (DocumentoDeCompra)jComboBoxTipoDocumento.getSelectedItem());
                    cargarCompraEnTabla(c);
                } else {
                    if(jComboBoxTipoDocumento.getSelectedItem() == ""){
                        //Si entra aca es vacio el tipo de documento y no el reparto. Busco por numero y reparto. 
                        compras = sisCompras.devolverCompraPorNumeroYReparto(Long.parseLong(jTextFieldNumero.getText().trim()), (Reparto) jComboBoxReparto.getSelectedItem());
                        for (Compra c : compras) {
                            //Cargo la tabla de la interfaz
                            cargarCompraEnTabla(c);
                        }
                    } else {
                        //Busco por los 3 numero, tipoDocumento y reparto
                        //Creo que no es necesario basta solo con los otros dos filtros.
                    }
                }
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            //Es vacio el numero. Entonces tiene que poner filtros de fecha. Verifico que los filtros de fecha estn con datos.
            if(jDateChooserDesdeFecha.getDate() == null || jDateChooserHastaFecha.getDate() == null){
                //Alguna de las fechas es vacia
                JOptionPane.showMessageDialog(this, "Si no se ingresa numero para la búsqueda, se debe ingresar al menos un rango de fechas.", "Información", JOptionPane.INFORMATION_MESSAGE);
                jDateChooserDesdeFecha.requestFocusInWindow();
            } else {
                //El numero es vacio, pero hay rango de fecha para la busqueda.
                //Calendar fechaDesdeC = dateChooserComboDesdeFecha.getSelectedDate();
                //fechaDesdeC.add(Calendar.DATE, -1);
                //Calendar fechaHastaC = dateChooserComboHastaFecha.getSelectedDate();
                if(jComboBoxReparto.getSelectedItem() == "" && jComboBoxTipoDocumento.getSelectedItem() == ""){
                    //Busco solo por el rango de fechas.
                    ponerVentanaEnEspera();
                    Thread worker = new Thread() {
                        public void run() {
                            try {
                                compras = sisCompras.devolverComprasEntreFechas(jDateChooserDesdeFecha.getDate(), jDateChooserHastaFecha.getDate());
                            } catch (Exception exp) {
                                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                                JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {   
                                    sacarVentanaDeEspera();
                                    for (Compra c : compras) {
                                        //Cargo la tabla de la interfaz
                                        cargarCompraEnTabla(c);
                                    }
                                }
                            });
                        }
                    };
                    worker.start();
                } else {
                    if (jComboBoxReparto.getSelectedItem() == "") {
                        //Si es vacio el de reparto es por que no es vacio el de tipo de documento. Busco por fechas y tipo de documento
                        ponerVentanaEnEspera();
                        Thread worker = new Thread() {
                            public void run() {
                                try {
                                    compras = sisCompras.devolverComprasEntreFechasYTipoDeDocumento(jDateChooserDesdeFecha.getDate(), jDateChooserHastaFecha.getDate(), (DocumentoDeCompra)jComboBoxTipoDocumento.getSelectedItem());
                                } catch (Exception exp) {
                                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                                    JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                                }
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {  
                                        sacarVentanaDeEspera();
                                        for (Compra c : compras) {
                                            //Cargo la tabla de la interfaz
                                            cargarCompraEnTabla(c);
                                        }
                                    }
                                });
                            }
                        };
                        worker.start();
                    } else {
                        if (jComboBoxTipoDocumento.getSelectedItem() == "") {
                            //Si entra aca es vacio el tipo de documento y no el reparto. Busco por fechas y reparto. 
                            ponerVentanaEnEspera();
                            Thread worker = new Thread() {
                                public void run() {
                                    try {
                                        compras = sisCompras.devolverComprasEntreFechasYReparto(jDateChooserDesdeFecha.getDate(), jDateChooserHastaFecha.getDate(), (Reparto)jComboBoxReparto.getSelectedItem());
                                    } catch (Exception exp) {
                                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                                        JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                    SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {  
                                            sacarVentanaDeEspera();
                                            for (Compra c : compras) {
                                                //Cargo la tabla de la interfaz
                                                cargarCompraEnTabla(c);
                                            }
                                        }
                                    });
                                }
                            };
                            worker.start();
                        } else {
                            //Busco por los 4 las fechas el reparto y el tipo de documento
                            ponerVentanaEnEspera();
                            Thread worker = new Thread() {
                                public void run() {
                                    try {
                                        compras = sisCompras.devolverComprasEntreFechasYTipoDeDocumentoYRaparto(jDateChooserDesdeFecha.getDate(), jDateChooserHastaFecha.getDate(), (DocumentoDeCompra)jComboBoxTipoDocumento.getSelectedItem() ,(Reparto)jComboBoxReparto.getSelectedItem());
                                    } catch (Exception exp) {
                                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                                        JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                    SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {
                                            sacarVentanaDeEspera();
                                            for (Compra c : compras) {
                                                //Cargo la tabla de la interfaz
                                                cargarCompraEnTabla(c);
                                            }
                                        }
                                    });
                                }
                            };
                            worker.start();
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    public void ponerVentanaEnEspera(){
        jLabelEspera.setVisible(true);
        jTextFieldNumero.setEnabled(false);
        jDateChooserDesdeFecha.setEnabled(false);
        jDateChooserHastaFecha.setEnabled(false);
        jComboBoxReparto.setEnabled(false);
        jComboBoxTipoDocumento.setEnabled(false);
        jButtonBuscar.setEnabled(false);
    }
    
    public void sacarVentanaDeEspera(){
        jLabelEspera.setVisible(false);
        jTextFieldNumero.setEnabled(true);
        jDateChooserDesdeFecha.setEnabled(true);
        jDateChooserHastaFecha.setEnabled(true);
        jComboBoxReparto.setEnabled(true);
        jComboBoxTipoDocumento.setEnabled(true);
        jButtonBuscar.setEnabled(true);
        jButtonBuscar.requestFocus();
    }
    
    private void jTextFieldNumeroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNumeroKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonBuscar.doClick();
        }
        if(evt.getKeyCode() == KeyEvent.VK_F5){
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jTextFieldNumeroKeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_formKeyPressed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        if (yaBusco) {
            jButtonBuscar.doClick();
        } 
    }//GEN-LAST:event_formWindowActivated

    private void jComboBoxTipoDocumentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxTipoDocumentoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_F5){
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jComboBoxTipoDocumentoKeyPressed

    private void jComboBoxRepartoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBoxRepartoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_F5){
            jButtonBuscar.doClick();
        }
    }//GEN-LAST:event_jComboBoxRepartoKeyPressed

    private void jTableComprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableComprasMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
            if (jTableCompras.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una compra.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                try {
                    if (SistemaUsuarios.getInstance().tienePermisos(Constantes.ActividadIngresarCompras)) {
                        IngresarCompras ic = new IngresarCompras(MantenimientoCompras.this, false);
                        ic.setAccion("Modificar");
                        Compra c = compras.get(jTableCompras.getSelectedRow());
                        ic.setCompra(c);
                        ic.setTipoDoc(c.getTipoDocumento());
                        ic.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorDePermisos, "Permisos", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception exp) {
                    String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                    SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                    JOptionPane.showMessageDialog(MantenimientoCompras.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_jTableComprasMouseClicked

    public void cargarCompraEnTabla(Compra c){
        Object[] object = new Object[8];
        object[0] = c.getTipoDocumento().getTipoDocumento();
        object[1] = c.getNumero();
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        object[2] = formatter.format(c.getFecha());
        object[3] = c.getReparto();
        object[4] = df.format(c.getSubtotal()).replace(',', '.');
        object[5] = df.format(c.getTotalMinimo()).replace(',', '.');
        object[6] = df.format(c.getTotalBasico()).replace(',', '.');
        object[7] = df.format(c.getTotal()).replace(',', '.');
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
            java.util.logging.Logger.getLogger(MantenimientoCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MantenimientoCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MantenimientoCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MantenimientoCompras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MantenimientoCompras dialog = new MantenimientoCompras(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox jComboBoxReparto;
    private javax.swing.JComboBox jComboBoxTipoDocumento;
    private com.toedter.calendar.JDateChooser jDateChooserDesdeFecha;
    private com.toedter.calendar.JDateChooser jDateChooserHastaFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelEspera;
    private javax.swing.JLabel jLabelFechaIncorrecta;
    private javax.swing.JLabel jLabelHastaFechaIncorrecta;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableCompras;
    private javax.swing.JTextField jTextFieldNumero;
    // End of variables declaration//GEN-END:variables

}
