/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.liquidaciones;

import dominio.Anep;
import dominio.Liquidacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import org.hibernate.HibernateException;
import sistema.SistemaLiquidaciones;
import sistema.SistemaUsuarios;
import ui.informes.VentanaInformeCheques;
import ui.usuarios.Constantes;

/**
 *
 * @author Edu
 */
public class VerAnepDeLiquidacion extends javax.swing.JFrame {

    private List<Anep> aneps;
    private final SistemaLiquidaciones sisLiquidaciones;

    private Liquidacion l;

    private DefaultTableModel modelo;
    SimpleDateFormat formatter;
    /**
     * Creates new form VerInauDeLiquidacion
     */
    public VerAnepDeLiquidacion(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        sisLiquidaciones = SistemaLiquidaciones.getInstance();
    }
    
    public final void inicializarTablaAnep() {
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Reparto");
        modelo.addColumn("Desde Fecha");
        modelo.addColumn("Hasta Fecha");
        modelo.addColumn("Total Litros");
        jTableAnep.setModel(modelo);
        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem verItem = new JMenuItem("Ver");
        verItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da ver en el menu con click derecho sobre la fila de la tabla
                Anep a = aneps.get(jTableAnep.getSelectedRow());
                IngresoAnep via = new IngresoAnep(VerAnepDeLiquidacion.this, true);
                via.setAnep(a);
                via.setAccion("Ver");
                via.setVisible(true);
            }

        });
        popupMenu.add(verItem);
        JMenuItem modificarItem = new JMenuItem("Modificar");
        modificarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da modificar en el menu con click derecho sobre la fila de la tabla
                Anep a = aneps.get(jTableAnep.getSelectedRow());
                IngresoAnep via = new IngresoAnep(VerAnepDeLiquidacion.this, true);
                via.setAnep(a);
                via.setAccion("Modificar");
                via.setVisible(true);
            }

        });
        popupMenu.add(modificarItem);
        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        eliminarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Que hacer cuando da eliminar en el menu con click derecho sobre la fila de la tabla
                Anep a = aneps.get(jTableAnep.getSelectedRow());
                int resp = JOptionPane.showConfirmDialog(VerAnepDeLiquidacion.this, "Seguro que quiere eliminar el anep de " + a.getReparto() + " con las fechas: " + formatter.format(a.getDesdeFecha()) + " : " + formatter.format(a.getHastaFecha()) + " ?", "Pregunta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    try {
                        if (sisLiquidaciones.eliminarAnep(a)) {
                            JOptionPane.showMessageDialog(VerAnepDeLiquidacion.this, "El cheque se elimino correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (HibernateException he) {
                        JOptionPane.showMessageDialog(VerAnepDeLiquidacion.this, "Error al eliminar el cheque." + "\n\n" + he.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exp) {
                        String stakTrace = util.Util.obtenerStackTraceEnString(exp);
                        SistemaUsuarios.getInstance().registrarExcepcion(exp.toString(), stakTrace);
                        JOptionPane.showMessageDialog(VerAnepDeLiquidacion.this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });
        popupMenu.add(eliminarItem);
        jTableAnep.setComponentPopupMenu(popupMenu);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelFecha = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabelReparto = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableAnep = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ver Anep de Liquidación");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabelFecha.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelFecha.setForeground(new java.awt.Color(0, 0, 255));
        jLabelFecha.setText("Fecha");

        jLabel2.setText("Reparto:");

        jLabelReparto.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelReparto.setForeground(new java.awt.Color(0, 0, 255));
        jLabelReparto.setText("Reparto");

        jLabel3.setText("Total Litros:");

        jLabelTotal.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelTotal.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTotal.setText("0");

        jLabel1.setText("Fecha:");

        jTableAnep.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Reparto", "Desde Fecha", "Hasta Fecha", "Total Litros"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableAnep);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
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
                            .addComponent(jLabelFecha)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        inicializarTablaAnep();
        if (l != null) {
            try {
                int total = 0;
                aneps = new ArrayList<>();
                Anep an = sisLiquidaciones.devolverAnepParaFechaYReparto(l.getFecha(), l.getReparto());
                if(an!=null){
                    aneps.add(an);
                }
                for (Anep a : aneps) {
                    total = total + a.getLitrosTotal();
                    cargarAnepEnTabla(a);
                }
                jLabelFecha.setText(formatter.format(l.getFecha()));
                jLabelReparto.setText(l.getReparto().toString());
                jLabelTotal.setText(Integer.toString(total));
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(VerAnepDeLiquidacion.this, "Error al cargar el inau." + "\n\n" + ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                String stakTrace = util.Util.obtenerStackTraceEnString(ex);
                SistemaUsuarios.getInstance().registrarExcepcion(ex.toString(), stakTrace);
                JOptionPane.showMessageDialog(this, Constantes.MensajeDeErrorGenerico, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_formWindowActivated

    public void cargarAnepEnTabla(Anep a) {
        Object[] object = new Object[4];
        object[0] = a.getReparto().getNombre();
        object[1] = formatter.format(a.getDesdeFecha());
        object[2] = formatter.format(a.getHastaFecha());
        object[3] = Integer.toString(a.getLitrosTotal());
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
            java.util.logging.Logger.getLogger(VerAnepDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VerAnepDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VerAnepDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VerAnepDeLiquidacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VerAnepDeLiquidacion dialog = new VerAnepDeLiquidacion(new javax.swing.JFrame(), true);
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
    private javax.swing.JTable jTableAnep;
    // End of variables declaration//GEN-END:variables

    /**
     * @param l the l to set
     */
    public void setL(Liquidacion l) {
        this.l = l;
    }
}
