/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.liquidaciones;

import dominio.Factura;
import dominio.Liquidacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.hibernate.HibernateException;
import sistema.SistemaFacturas;
import sistema.SistemaLiquidaciones;
import sistema.SistemaUsuarios;
import ui.facturas.IngresoFacturas;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class VerFiadoEmpresaDeLiquidacion extends javax.swing.JFrame {

    private List<Factura> facturas;
    private final SistemaLiquidaciones sisLiquidaciones;
    private final SistemaFacturas sisFacturas;

    private Liquidacion l;

    private DecimalFormat df;
    private DefaultTableModel modelo;
    /**
     * Creates new form VerFiadoEmpresaDeLiquidacion
     */
    public VerFiadoEmpresaDeLiquidacion(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        sisLiquidaciones = SistemaLiquidaciones.getInstance();
        sisFacturas = SistemaFacturas.getInstance();
        df = new DecimalFormat("0.00");
    }
    
    public final void inicializarTablaFacturas() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
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
        for (int i = 0; i < 5; i++) {
            column = jTableFacturas.getColumnModel().getColumn(i);
            if (i == 0 || i == 3) {
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
                IngresoFacturas vif = new IngresoFacturas(VerFiadoEmpresaDeLiquidacion.this, false);
                vif.setAccion("Ver");
                Factura f = facturas.get(jTableFacturas.getSelectedRow());
                vif.setFactura(f);
                vif.setVisible(true);
            }
        });
        popupMenu.add(verItem);
        JMenuItem modificarItem = new JMenuItem("Modificar");
        modificarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da modificar en el menu con click derecho sobre la fila de la tabla
                IngresoFacturas vif = new IngresoFacturas(VerFiadoEmpresaDeLiquidacion.this, false);
                vif.setAccion("Modificar");
                Factura f = facturas.get(jTableFacturas.getSelectedRow());
                vif.setFactura(f);
                vif.setVisible(true);
            }

        });
        popupMenu.add(modificarItem);
        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        eliminarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da eliminar en el menu con click derecho sobre la fila de la tabla
                Factura f = facturas.get(jTableFacturas.getSelectedRow());
                int resp = JOptionPane.showConfirmDialog(VerFiadoEmpresaDeLiquidacion.this, "Seguro que quiere eliminar la factura con el numero " + f.getNumero() + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    try {
                        if (sisFacturas.eliminarFactura(f)) {
                            JOptionPane.showMessageDialog(VerFiadoEmpresaDeLiquidacion.this, "La factura se elimino correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (HibernateException he) {
                        JOptionPane.showMessageDialog(VerFiadoEmpresaDeLiquidacion.this, "Error al eliminar la factura." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                        SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                        JOptionPane.showMessageDialog(VerFiadoEmpresaDeLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        popupMenu.add(eliminarItem);
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

        jLabelReparto = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabelFecha = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableFacturas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ver Facturas de Fiados Que Cobra La Empresa");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabelReparto.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelReparto.setForeground(new java.awt.Color(0, 0, 255));
        jLabelReparto.setText("Reparto");

        jLabel3.setText("Total:");

        jLabelTotal.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelTotal.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTotal.setText("0");

        jLabel1.setText("Fecha:");

        jLabelFecha.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelFecha.setForeground(new java.awt.Color(0, 0, 255));
        jLabelFecha.setText("Fecha");

        jLabel2.setText("Reparto:");

        jTableFacturas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Cliente", "Numero", "Fecha", "Reparto", "SubTotal", "Iva Minimo", "Iva Basico", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, true, false
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
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelReparto)
                        .addGap(50, 50, 50)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTotal))
                    .addComponent(jLabelFecha))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabelFecha))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabelReparto)
                    .addComponent(jLabel3)
                    .addComponent(jLabelTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        inicializarTablaFacturas();
        if (l != null) {
            try {
                double total = 0;
                facturas = sisLiquidaciones.devolverFacturasFiadoEmpresaParaLiquidacion(l);
                for (Factura f : facturas) {
                    total = total + f.getTotal();
                    cargarFacturaEnTabla(f);
                }
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("dd-MM-yyyy");
                jLabelFecha.setText(formatter.format(l.getFecha()));
                jLabelReparto.setText(l.getReparto().toString());
                jLabelTotal.setText(df.format(total).replace(',', '.'));
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(VerFiadoEmpresaDeLiquidacion.this, "Error al cargar las factura." + "\n\n" + ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_formWindowActivated

    public void cargarFacturaEnTabla(Factura f) {
        Object[] object = new Object[8];
        object[0] = f.getCliente();
        object[1] = f.getNumero();
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        object[2] = formatter.format(f.getFecha());
        object[3] = f.getReparto();
        object[4] = df.format(f.getSubtotal()).replace(',', '.');
        object[5] = df.format(f.getTotalMinimo()).replace(',', '.');
        object[6] = df.format(f.getTotalBasico()).replace(',', '.');
        object[7] = df.format(f.getTotal()).replace(',', '.');
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
            java.util.logging.Logger.getLogger(VerFiadoEmpresaDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VerFiadoEmpresaDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VerFiadoEmpresaDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VerFiadoEmpresaDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VerFiadoEmpresaDeLiquidacion dialog = new VerFiadoEmpresaDeLiquidacion(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelReparto;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableFacturas;
    // End of variables declaration//GEN-END:variables

    /**
     * @param l the l to set
     */
    public void setL(Liquidacion l) {
        this.l = l;
    }
}
