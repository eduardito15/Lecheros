/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.clientes;

import dominio.Cliente;
import dominio.Reparto;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import sistema.SistemaMantenimiento;

/**
 *
 * @author Edu
 */
public class VentanaBuscadorClientePorReparto extends javax.swing.JDialog {

    private SistemaMantenimiento sis;
    private List<Cliente> clientes;
    private String textoBusqueda;
    private Cliente clienteSeleccionado;
    
    private Reparto reparto;
    /**
     * Creates new form VentanaBuscador
     */
    public VentanaBuscadorClientePorReparto(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        sis = SistemaMantenimiento.getInstance();
        clientes = sis.devolverClientesActivos();
        textoBusqueda = "";
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
        jTextFieldBuscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListResultados = new javax.swing.JList();
        jButtonSeleccionar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Buscar Cliente");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel1.setText("Buscar Cliente");

        jTextFieldBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldBuscarKeyPressed(evt);
            }
        });

        jListResultados.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListResultados.setToolTipText("");
        jListResultados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListResultadosMouseClicked(evt);
            }
        });
        jListResultados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jListResultadosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jListResultados);

        jButtonSeleccionar.setText("Seleccionar");
        jButtonSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeleccionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonSeleccionar)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)))
                .addContainerGap(7, Short.MAX_VALUE))
            .addComponent(jTextFieldBuscar, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(59, 59, 59))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSeleccionar)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBuscarKeyPressed
        // TODO add your handling code here:
        char ch = evt.getKeyChar();
        if(Character.isLetter(ch) || Character.isDigit(ch) || Character.isSpaceChar(ch)){
            if(!textoBusqueda.isEmpty()) {
                if(textoBusqueda.charAt(0) != jTextFieldBuscar.getText().trim().charAt(0)) {
                    textoBusqueda = jTextFieldBuscar.getText().trim() + ch;
                } else {
                    textoBusqueda = textoBusqueda + ch;
                }
            } else {
                textoBusqueda = textoBusqueda + ch;
            }
            DefaultListModel modelo = new DefaultListModel();
            for(Cliente c : clientes){
                if(c.getNombre().toLowerCase().indexOf(textoBusqueda.toLowerCase()) != -1 || c.getRazonSocial().toLowerCase().indexOf(textoBusqueda.toLowerCase()) != -1){
                     modelo.addElement(c);
                }
            }
            jListResultados.setModel(modelo);
            jListResultados.setSelectedIndex(0);
        } else {
            if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                if(jTextFieldBuscar.getText().trim().equals("")) {
                    textoBusqueda = "";
                }
                textoBusqueda = jTextFieldBuscar.getText().trim().substring(0, (jTextFieldBuscar.getText().trim().length()-1));
                if(!"".equals(textoBusqueda)){
                    DefaultListModel modelo = new DefaultListModel();
                    for (Cliente c : clientes) {
                        if (c.getNombre().toLowerCase().indexOf(textoBusqueda.toLowerCase()) != -1 || c.getRazonSocial().toLowerCase().indexOf(textoBusqueda.toLowerCase()) != -1) {
                            modelo.addElement(c);
                        }
                    }
                    jListResultados.setModel(modelo);
                    jListResultados.setSelectedIndex(0);
                }
            }
            if(evt.getKeyCode() == KeyEvent.VK_ENTER){
                 jListResultados.requestFocus();
            }
            if(evt.getKeyCode() == KeyEvent.VK_DOWN) {
                jListResultados.requestFocus();
                jListResultados.setSelectedIndex(0);
            }
        }
        
    }//GEN-LAST:event_jTextFieldBuscarKeyPressed

    private void jButtonSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeleccionarActionPerformed
        // TODO add your handling code here:
        if(jListResultados.getSelectedIndex()==-1){
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            clienteSeleccionado = ((Cliente)jListResultados.getSelectedValue());
            this.dispose();
        }
    }//GEN-LAST:event_jButtonSeleccionarActionPerformed

    private void jListResultadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListResultadosMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
            if (jListResultados.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                clienteSeleccionado = ((Cliente) jListResultados.getSelectedValue());
                this.dispose();
            }
        }
    }//GEN-LAST:event_jListResultadosMouseClicked

    private void jListResultadosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jListResultadosKeyPressed
        // TODO add your handling code here:
        if(jListResultados.getSelectedIndex() != -1){
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                jButtonSeleccionar.doClick();
            }
            if(jListResultados.getSelectedIndex() == 0) {
                if(evt.getKeyCode() == KeyEvent.VK_UP) {
                    jTextFieldBuscar.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_jListResultadosKeyPressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        setClientes(sis.devolverClientesPorRepartoYEstado(this.reparto, true));
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
            java.util.logging.Logger.getLogger(VentanaBuscadorClientePorReparto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaBuscadorClientePorReparto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaBuscadorClientePorReparto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaBuscadorClientePorReparto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaBuscadorClientePorReparto dialog = new VentanaBuscadorClientePorReparto(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButtonSeleccionar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jListResultados;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldBuscar;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the rubroSeleccionado
     */
    public Cliente getCliente() {
        return clienteSeleccionado;
    }

    /**
     * @param reparto the reparto to set
     */
    public void setReparto(Reparto reparto) {
        this.reparto = reparto;
    }

    /**
     * @param clientes the clientes to set
     */
    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
}
