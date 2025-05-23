package view;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import model.dao.DAOSQL;

/**
 * This class defines the main menu of the application. Actions that can be
 * done: insert, read, delete, update, readAll.
 *
 * @author Francesc Perez
 * @version 1.1.0
 */
public class Menu extends javax.swing.JFrame {

    private DAOSQL userdb = new DAOSQL();

    public Menu() {
        initComponents();
        try {
            setIconImage(new ImageIcon(ImageIO.read(new File("images/logo.png"))).getImage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Application logo is not available", "WARNING MESSAGE", JOptionPane.WARNING_MESSAGE);
        }
    }

    public JButton getInsert() {
        return insert;
    }

    public JButton getRead() {
        return read;
    }

    public JButton getDelete() {
        return delete;
    }

    public JButton getUpdate() {
        return update;
    }

    public JButton getReadAll() {
        return readAll;
    }

    public JButton getDeleteAll() {
        return deleteAll;
    }

    public JButton getCount() {
        return btnCount;
    }

    public void updatePeopleCount(int count) {
        lblCount.setText("Personas registradas: " + count);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        read = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        update = new javax.swing.JButton();
        insert = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        readAll = new javax.swing.JButton();
        lblCount = new javax.swing.JButton();
        deleteAll = new javax.swing.JButton();
        btnCount = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu - People v1.1.0");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        setMinimumSize(new java.awt.Dimension(300, 450));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        read.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        read.setText("READ");
        read.setMaximumSize(new java.awt.Dimension(120, 50));
        read.setMinimumSize(new java.awt.Dimension(120, 50));
        read.setPreferredSize(new java.awt.Dimension(120, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 24, 12, 24);
        getContentPane().add(read, gridBagConstraints);

        delete.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        delete.setText("DELETE");
        delete.setMaximumSize(new java.awt.Dimension(120, 50));
        delete.setMinimumSize(new java.awt.Dimension(120, 50));
        delete.setPreferredSize(new java.awt.Dimension(120, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 24, 12, 24);
        getContentPane().add(delete, gridBagConstraints);

        update.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        update.setText("UPDATE");
        update.setMaximumSize(new java.awt.Dimension(120, 50));
        update.setMinimumSize(new java.awt.Dimension(120, 50));
        update.setPreferredSize(new java.awt.Dimension(120, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 24, 12, 24);
        getContentPane().add(update, gridBagConstraints);

        insert.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        insert.setText("INSERT");
        insert.setMaximumSize(new java.awt.Dimension(120, 50));
        insert.setMinimumSize(new java.awt.Dimension(120, 50));
        insert.setPreferredSize(new java.awt.Dimension(120, 50));
        insert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 24, 12, 24);
        getContentPane().add(insert, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 2, 8)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Author: francesc.perez@stucom.com - Version 1.1.0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 24, 12, 24);
        getContentPane().add(jLabel1, gridBagConstraints);

        readAll.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        readAll.setText("READ ALL");
        readAll.setMaximumSize(new java.awt.Dimension(120, 50));
        readAll.setMinimumSize(new java.awt.Dimension(120, 50));
        readAll.setPreferredSize(new java.awt.Dimension(120, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 24, 12, 24);
        getContentPane().add(readAll, gridBagConstraints);

        lblCount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblCount.setText("REGISTER");
        lblCount.setMaximumSize(new java.awt.Dimension(120, 50));
        lblCount.setMinimumSize(new java.awt.Dimension(120, 50));
        lblCount.setPreferredSize(new java.awt.Dimension(120, 50));
        lblCount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblCountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 24, 12, 24);
        getContentPane().add(lblCount, gridBagConstraints);

        deleteAll.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        deleteAll.setText("DELETE ALL");
        deleteAll.setMaximumSize(new java.awt.Dimension(120, 50));
        deleteAll.setMinimumSize(new java.awt.Dimension(120, 50));
        deleteAll.setPreferredSize(new java.awt.Dimension(120, 50));
        deleteAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 24, 12, 24);
        getContentPane().add(deleteAll, gridBagConstraints);

        btnCount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCount.setText("COUNT");
        btnCount.setMaximumSize(new java.awt.Dimension(120, 50));
        btnCount.setMinimumSize(new java.awt.Dimension(120, 50));
        btnCount.setPreferredSize(new java.awt.Dimension(120, 50));
        btnCount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 24, 12, 24);
        getContentPane().add(btnCount, gridBagConstraints);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblCountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblCountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCountActionPerformed

    private void deleteAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAllActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteAllActionPerformed

    private void btnCountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCountActionPerformed
        int count = userdb.countUsers();
        updatePeopleCount(count); // Esto actualiza el label lblCount
        JOptionPane.showMessageDialog(null, "Total number of people: " + count,
                "User Count", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnCountActionPerformed

    private void insertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_insertActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCount;
    private javax.swing.JButton delete;
    private javax.swing.JButton deleteAll;
    private javax.swing.JButton insert;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton lblCount;
    private javax.swing.JButton read;
    private javax.swing.JButton readAll;
    private javax.swing.JButton update;
    // End of variables declaration//GEN-END:variables
}
