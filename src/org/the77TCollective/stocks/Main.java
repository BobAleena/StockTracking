/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.the77TCollective.stocks;

/**
 *
 * @author BobDevelopment
 */

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Toolkit;

public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
                /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainJFrame mainJFrame;
                mainJFrame = new MainJFrame();
                /*
                 * Set the Fram size and position
                 */
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                int numberOfScreens = 1;
                int width;
                int height;
                try 
                {
                    GraphicsDevice[] gs = ge.getScreenDevices();
                    numberOfScreens = gs.length;
                }
                catch (HeadlessException he)
                {
                    System.err.println(he);
                }
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                if(numberOfScreens > 1)
                {
                    width = screenSize.width;
                    height = screenSize.height;
                    if(screenSize.width > screenSize.height)
                    {
                        width = screenSize.width/numberOfScreens;                        
                    }
                    else
                    {
                        height = screenSize.height/numberOfScreens;
                    }
                }
                else
                {
                    width = screenSize.width;
                    height = screenSize.height;
                }
                mainJFrame.setSize(new Dimension((width/2),(height/2)));
                Dimension frameSize = mainJFrame.getSize();
                if (frameSize.height > height)
                {
                    frameSize.height = height;
                }
                if (frameSize.width > width)
                {
                    frameSize.width = width;
                }
                mainJFrame.setLocation((width - frameSize.width) / 2, (height - frameSize.height) / 2);
                int scrollableViewportWidth = (width/2)-75, scrollableViewportHeight = (height/2)-125;
                mainJFrame.setScrollableViewportSize(scrollableViewportWidth, scrollableViewportHeight);
                mainJFrame.setVisible(true);
            }
        });
    }
}