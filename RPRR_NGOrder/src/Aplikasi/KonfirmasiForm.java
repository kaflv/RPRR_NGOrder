/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Aplikasi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class KonfirmasiForm extends JFrame {
    JLabel lblNama, lblAlamat, lblStatus, lblTotal;
    JTable tblRingkasan;
    DefaultTableModel modelRingkasan;

    int pesananId;

    public KonfirmasiForm(int pesananId) {
        this.pesananId = pesananId;

        setTitle("Konfirmasi Pesanan");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lbl1 = new JLabel("Nama Pemesan:");
        lbl1.setBounds(30, 20, 120, 25);
        add(lbl1);

        lblNama = new JLabel("-");
        lblNama.setBounds(150, 20, 200, 25);
        add(lblNama);

        JLabel lbl2 = new JLabel("Alamat:");
        lbl2.setBounds(30, 50, 120, 25);
        add(lbl2);

        lblAlamat = new JLabel("-");
        lblAlamat.setBounds(150, 50, 400, 25);
        add(lblAlamat);

        JLabel lbl3 = new JLabel("Status:");
        lbl3.setBounds(30, 80, 120, 25);
        add(lbl3);

        lblStatus = new JLabel("-");
        lblStatus.setBounds(150, 80, 200, 25);
        add(lblStatus);

        JLabel lbl4 = new JLabel("Ringkasan Pesanan:");
        lbl4.setBounds(30, 110, 200, 25);
        add(lbl4);

        modelRingkasan = new DefaultTableModel(new String[]{"Menu", "Jumlah", "Subtotal"}, 0);
        tblRingkasan = new JTable(modelRingkasan);
        JScrollPane sp = new JScrollPane(tblRingkasan);
        sp.setBounds(30, 140, 520, 150);
        add(sp);

        lblTotal = new JLabel("Total: Rp 0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setBounds(30, 300, 300, 25);
        add(lblTotal);

        JButton btnSelesai = new JButton("Selesai");
        btnSelesai.setBounds(450, 300, 100, 30);
        add(btnSelesai);

        btnSelesai.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Terima kasih!");
            dispose();
            new LoginForm().setVisible(true); // kembali ke login
        });

        loadData();
    }

    private void loadData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/warung_nasi_goreng", "root", "")) {

            // Ambil info pesanan dan user
            PreparedStatement ps1 = conn.prepareStatement(
                "SELECT p.total_harga, p.status, u.username, u.alamat FROM pesanan p " +
                "JOIN users u ON p.user_id = u.user_id WHERE p.pesanan_id = ?"
            );
            ps1.setInt(1, pesananId);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                lblNama.setText(rs1.getString("username"));
                lblAlamat.setText(rs1.getString("alamat"));
                lblStatus.setText(rs1.getString("status"));
                lblTotal.setText("Total: Rp " + rs1.getInt("total_harga"));
            }

            // Ambil detail pesanan
            PreparedStatement ps2 = conn.prepareStatement(
                "SELECT m.nama_menu, d.jumlah, d.subtotal FROM pesanan_detail d " +
                "JOIN menu m ON d.menu_id = m.menu_id WHERE d.pesanan_id = ?"
            );
            ps2.setInt(1, pesananId);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                modelRingkasan.addRow(new Object[]{
                    rs2.getString("nama_menu"),
                    rs2.getInt("jumlah"),
                    rs2.getInt("subtotal")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }
}
