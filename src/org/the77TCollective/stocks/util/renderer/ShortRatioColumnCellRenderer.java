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
import javax.swing.table.TableModel;

/**
 *
 * @author BobDevelopment
 */
public class ShortRatioColumnCellRenderer extends StatColumnCellRenderer {
    
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
        String s = (String)model.getValueAt(row,column);
            if (s.compareTo("N/A")==0) {
                c.setBackground(Color.WHITE);
                return c;
            }
                    
        Float f = new Float(s);
        if (f >= 15)
        {
            c.setBackground(new Color(255,48,48));
        } else if (f <= 3) {
            c.setBackground(new Color(84,255,159));
        } else {
            c.setBackground(new Color(255,236,139));
        }
        return c;
    }
}
