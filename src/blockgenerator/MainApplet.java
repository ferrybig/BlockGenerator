/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockgenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import javax.swing.JPanel;

/**
 *
 * @author Fernando
 */
public class MainApplet extends javax.swing.JApplet {

    private volatile BufferedImage image;
    private JPanel drawingPanel;
    private int[] width = {160, 320, 160};
    private int height = 400;
    private MultiColor[] colors = new MultiColor[4];

    public void setWidth(int index, int newWidth) throws InvocationTargetException, InterruptedException {
        java.awt.EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                width[index] = newWidth;
                int total = 0;
                for (int i = 0; i < width.length; i++) {
                    total += width[i];
                }
                drawingPanel.setPreferredSize(new Dimension(total, height));
                regenerate();
            }
        });
    }

    public void setWidth(int height) throws InterruptedException, InvocationTargetException {
        java.awt.EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                MainApplet.this.height = height;
                int total = 0;
                for (int i = 0; i < width.length; i++) {
                    total += width[i];
                }
                drawingPanel.setPreferredSize(new Dimension(total, height));
                regenerate();
            }
        });
    }

    /**
     * Initializes the applet MainApplet
     */
    @Override
    public void init() {

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
            java.util.logging.Logger.getLogger(MainApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the applet */
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    MultiColor sides = new MultiColor(Color.yellow, Color.yellow.darker().darker());
                    MultiColor middle = new MultiColor(Color.orange, Color.orange.darker().darker());
                    colors = new MultiColor[]{sides, middle, middle, sides};
                    drawingPanel = new JPanel() {

                        @Override
                        protected void printComponent(Graphics g) {
                            super.printComponent(g);
                            if (image != null) {
                                g.drawImage(image, image.getWidth(this), image.getHeight(this), this);
                            }
                        }

                    };
                    initComponents();
                    drawingPanel.setPreferredSize(new Dimension(640, height));
                    regenerate();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void regenerate() {
        int total = 0;
        for (int i = 0; i < width.length; i++) {
            total += width[i];
        }
        this.image = new BufferedImage(total, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = this.image.createGraphics();
        Random random =new Random();
        try {
            MainGrid grid = new MainGrid(
                    colors[0],
                    new BlockPattern(random, width[0], height, colors[1], width[0] / 16, height / 16, 3, true, false, true),
                    new SolidColorPattern(width[1], height, colors[2]),
                    new BlockPattern(random, width[2], height, colors[3], width[2] / 16, height / 16, 3, true, false, true)
            );
            grid.render(g2);
        } finally {
            g2.dispose();
        }
        this.drawingPanel.repaint();
    }

    /**
     * This method is called from within the init() method to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        drawPanel = new javax.swing.JPanel();
        jPanel1 = drawingPanel;
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        getContentPane().setLayout(new java.awt.CardLayout());

        drawPanel.setLayout(new java.awt.GridBagLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        drawPanel.add(jPanel1, gridBagConstraints);

        getContentPane().add(drawPanel, "card2");

        jScrollPane1.setViewportView(jTextPane1);

        getContentPane().add(jScrollPane1, "card3");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel drawPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
