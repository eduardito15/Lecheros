/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.liquidaciones;

import dominio.Liquidacion;
import dominio.Retencion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.hibernate.HibernateException;
import sistema.SistemaLiquidaciones;
import sistema.SistemaUsuarios;
import ui.informes.VentanaInformeCheques;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class VerRetencionesDeLiquidacion extends javax.swing.JFrame {

    private List<Retencion> retenciones;
    private final SistemaLiquidaciones sisLiquidaciones;

    private Liquidacion l;

    private DecimalFormat df;
    private DefaultTableModel modelo;
    /**
     * Creates new form VerRetencionesDeLiquidacion
     */
    public VerRetencionesDeLiquidacion(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        sisLiquidaciones = SistemaLiquidaciones.getInstance();
        df = new DecimalFormat("0.00");
    }
    
    public final void inicializarTablaRetenciones() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Nombre");
        modelo.addColumn("A Que Corresponde?");
        modelo.addColumn("Importe");
        jTableRetenciones.setModel(modelo);
        TableColumn column = null;
        for (int i = 0; i < 3; i++) {
            column = jTableRetenciones.getColumnModel().getColumn(i);
            if (i == 1) {
                column.setPreferredWidth(120); //articulo column is bigger
            } else {
                column.setPreferredWidth(50);
            }
        }
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem verItem = new JMenuItem("Ver");
        verItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da ver en el menu con click derecho sobre la fila de la tabla
                Retencion r = retenciones.get(jTableRetenciones.getSelectedRow());
                IngresoRetencion vir = new IngresoRetencion(VerRetencionesDeLiquidacion.this, true);
                vir.setAccion("Ver");
                vir.setRetencion(r);
                vir.setVisible(true);
            }

        });
        popupMenu.add(verItem);
        JMenuItem modificarItem = new JMenuItem("Modificar");
        modificarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da modificar en el menu con click derecho sobre la fila de la tabla
                Retencion r = retenciones.get(jTableRetenciones.getSelectedRow());
                IngresoRetencion vir = new IngresoRetencion(VerRetencionesDeLiquidacion.this, true);
                vir.setAccion("Modificar");
                vir.setRetencion(r);
                vir.setVisible(true);
            }

        });
        popupMenu.add(modificarItem);
        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        eliminarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da eliminar en el menu con click derecho sobre la fila de la tabla
                Retencion r = retenciones.get(jTableRetenciones.getSelectedRow());
                int resp = JOptionPane.showConfirmDialog(VerRetencionesDeLiquidacion.this, "Seguro que quiere eliminar la retención de " + r.getNombre() + " con el importe " + r.getImporte() + "?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    try {
                        if (sisLiquidaciones.eliminarRetencion(r)) {
                            JOptionPane.showMessageDialog(VerRetencionesDeLiquidacion.this, "La retención se elimino correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (HibernateException he) {
                        JOptionPane.showMessageDialog(VerRetencionesDeLiquidacion.this, "Error al eliminar la retención." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                        SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                        JOptionPane.showMessageDialog(VerRetencionesDeLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        popupMenu.add(eliminarItem);
        
        JMenuItem quitarItem = new JMenuItem("Quitar de Liquidación");
        quitarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da eliminar en el menu con click derecho sobre la fila de la tabla
                Retencion r = retenciones.get(jTableRetenciones.getSelectedRow());
                int resp = JOptionPane.showConfirmDialog(VerRetencionesDeLiquidacion.this, "Seguro que quiere quitar la retención de " + r.getNombre() + " con el importe " + r.getImporte() + " de la liquidación?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    try {
                        if (sisLiquidaciones.quitarRetencionDeLiquidacion(l,r)) {
                            JOptionPane.showMessageDialog(VerRetencionesDeLiquidacion.this, "La retención se quito correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (HibernateException he) {
                        JOptionPane.showMessageDialog(VerRetencionesDeLiquidacion.this, "Error al quitar la retención." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(VerRetencionesDeLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        popupMenu.add(quitarItem);
        jTableRetenciones.setComponentPopupMenu(popupMenu);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabelFecha = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabelReparto = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableRetenciones = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Retenciónes de Liquidación");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabel1.setText("Fecha:");

        jLabelFecha.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelFecha.setForeground(new java.awt.Color(0, 0, 255));
        jLabelFecha.setText("Fecha");

        jLabel2.setText("Reparto:");

        jLabelReparto.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelReparto.setForeground(new java.awt.Color(0, 0, 255));
        jLabelReparto.setText("Reparto");

        jLabel3.setText("Total:");

        jLabelTotal.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelTotal.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTotal.setText("0");

        jTableRetenciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nombre", "A Que Corresponde", "Importe"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableRetenciones);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
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
                .addContainerGap(144, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        inicializarTablaRetenciones();
        if (l != null) {
            try {
                double total = 0;
                retenciones = sisLiquidaciones.devolverRetencionesParaLiquidacion(l);
                for (Retencion r : retenciones) {
                    total = total + r.getImporte();
                    cargarRetencionEnTabla(r);
                }
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("dd-MM-yyyy");
                jLabelFecha.setText(formatter.format(l.getFecha()));
                jLabelReparto.setText(l.getReparto().toString());
                jLabelTotal.setText(df.format(total).replace(',', '.'));
            } catch (Exception exp) {
                String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                JOptionPane.showMessageDialog(VerRetencionesDeLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_formWindowActivated

    public void cargarRetencionEnTabla(Retencion r) {
        Object[] object = new Object[3];
        object[0] = r.getNombre();
        object[1] = r.getaQueCorresponde();
        object[2] = df.format(r.getImporte()).replace(',', '.');
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
            java.util.logging.Logger.getLogger(VerRetencionesDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VerRetencionesDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VerRetencionesDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VerRetencionesDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VerRetencionesDeLiquidacion dialog = new VerRetencionesDeLiquidacion(new javax.swing.JFrame(), true);
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
    private javax.swing.JTable jTableRetenciones;
    // End of variables declaration//GEN-END:variables

    /**
     * @param l the l to set
     */
    public void setL(Liquidacion l) {
        this.l = l;
    }
}
