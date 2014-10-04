/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.the77TCollective.stocks.util.renderer;

import java.awt.Color;
import java.awt.Component;
import java.math.BigInteger;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author BobDevelopment
 */
public class LFreeCashFlowColumnCellRenderer extends StatColumnCellRenderer {
    
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

        // get rid of decimals
        int x = s.indexOf(".");
        if (x != -1) 
            s = s.substring(0,s.indexOf("."));
        BigInteger i = new BigInteger(s);
        BigInteger comparison = new BigInteger(new String("1000000000"));
        if (i.compareTo(comparison) == -1)
        {
            c.setBackground(new Color(255,48,48));
        } else if (i.compareTo(comparison) == 1) {
            c.setBackground(new Color(84,255,159));;
        } else {
            c.setBackground(new Color(255,236,139));
        }
        return c;
    }
}
