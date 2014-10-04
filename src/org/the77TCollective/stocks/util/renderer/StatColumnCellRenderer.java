/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.the77TCollective.stocks.util.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.the77TCollective.stocks.util.json.BaseStockStatContent;

/**
 *
 * @author BobDevelopment
 */
public class StatColumnCellRenderer extends DefaultTableCellRenderer {
    
    /**
     * 
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return 
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        TableModel model = table.getModel();
        System.out.println("back again...");
        String s = (String)model.getValueAt(row,column);
        System.out.println("Here is the string" + s);
        if (s.equals("MSFT"))
        {
            System.out.println("The same!");
            c.setBackground(new Color (79,148,205));
        } else {
            c.setBackground(new Color(255,165,79));
        }
  

        return c;
    }
}
