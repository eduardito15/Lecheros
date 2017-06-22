/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.facturas;

import java.awt.Color;

/**
 *
 * @author Edu
 */
public class MyCellRenderer extends javax.swing.table.DefaultTableCellRenderer {

    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, java.lang.Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final java.awt.Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        //Double val1 = Double.parseDouble((Double)table.getValueAt(row, 2));
        //Double val2 = Double.parseDouble((String)table.getValueAt(row, 4));
        try {
            Double val1 = Double.parseDouble((String) table.getValueAt(row, 2));
            Double val2 = Double.parseDouble((String)table.getValueAt(row, 4));
            if (val1 <  0) {
                cellComponent.setBackground(Color.red);

            } else {
                cellComponent.setBackground(Color.white);
            }    
            if (val2 <  0) {
                cellComponent.setBackground(Color.red);

            } else {
                cellComponent.setBackground(Color.white);
            } 
            if (isSelected) {
                //cellComponent.setForeground(table.getSelectionForeground());
                cellComponent.setBackground(table.getSelectionBackground());
            }
        } catch (Exception e) {
            cellComponent.setBackground(Color.white);
            if (isSelected) {
            //cellComponent.setForeground(table.getSelectionForeground());
            cellComponent.setBackground(table.getSelectionBackground());
            }
        }

        return cellComponent;

    }

}
