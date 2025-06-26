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

public class AdminDashboardForm extends JFrame {
    JTable tblUser, tblPesanan;
    DefaultTableModel modelUser, modelPesanan;
    JButton btnHapusUser, btnRefresh;

    public AdminDashboardForm() {
        setTitle("Admin Dashboard");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        // Panel Kelola User
        JPanel panelUser = new JPanel(null);
        modelUser = new DefaultTableModel(new String[]{"ID", "Username", "Jenis Akun"}, 0);
        tblUser = new JTable(modelUser);
        JScrollPane spUser = new JScrollPane(tblUser);
        spUser.setBounds(20, 20, 720, 300);
        panelUser.add(spUser);

        btnHapusUser = new JButton("Hapus User");
        btnHapusUser.setBounds(20, 340, 120, 30);
        panelUser.add(btnHapusUser);

        btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(160, 340, 100, 30);
        panelUser.add(btnRefresh);

        btnHapusUser.addActionListener(e -> hapusUser());
        btnRefresh.addActionListener(e -> loadUsers());

        // Panel Kelola Pesanan
        JPanel panelPesanan = new JPanel(null);
        modelPesanan = new DefaultTableModel(new String[]{"ID", "User", "Total", "Status", "Tanggal"}, 0);
        tblPesanan = new JTable(modelPesanan);
        JScrollPane spPesanan = new JScrollPane(tblPesanan);
        spPesanan.setBounds(20, 20, 720, 350);
        panelPesanan.add(spPesanan);

        // Tambah tab
        tabs.add("Kelola User", panelUser);
        tabs.add("Kelola Pesanan", panelPesanan);
        add(tabs);

        // Load data awal
        loadUsers();
        loadPesanan();
    }

    private void loadUsers() {
        modelUser.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/warung_nasi_goreng", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT user_id, username, jenis_akun FROM users")) {

            while (rs.next()) {
                modelUser.addRow(new Object[]{
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("jenis_akun")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load users: " + e.getMessage());
        }
    }

    private void hapusUser() {
        int selected = tblUser.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Pilih user terlebih dahulu.");
            return;
        }

        int userId = (int) modelUser.getValueAt(selected, 0);

        // Konfirmasi
        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus user ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/warung_nasi_goreng", "root", "");
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE user_id = ?")) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User berhasil dihapus.");
            loadUsers();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal hapus user: " + e.getMessage());
        }
    }

    private void loadPesanan() {
        modelPesanan.setRowCount(0);

        String query = "SELECT p.pesanan_id, u.username, p.total_harga, p.status, p.tanggal_pesan " +
                       "FROM pesanan p " +
                       "JOIN users u ON p.user_id = u.user_id " +
                       "ORDER BY p.pesanan_id DESC";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/warung_nasi_goreng", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                modelPesanan.addRow(new Object[]{
                        rs.getInt("pesanan_id"),
                        rs.getString("username"),
                        rs.getDouble("total_harga"),
                        rs.getString("status"),
                        rs.getString("tanggal_pesan")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load pesanan: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new AdminDashboardForm().setVisible(true);
    }
}
