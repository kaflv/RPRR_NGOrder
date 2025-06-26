/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Aplikasi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PemesananForm extends JFrame {
    JTable tblMenu, tblKeranjang;
    DefaultTableModel modelMenu, modelKeranjang;
    JTextField txtJumlah;
    JLabel lblTotal;
    int totalHarga = 0;
    int userId;

    public PemesananForm(int userId) {
        this.userId = userId;
        setTitle("Form Pemesanan");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Label
        JLabel lblDaftarMenu = new JLabel("Daftar Menu");
        lblDaftarMenu.setBounds(30, 20, 150, 20);
        add(lblDaftarMenu);

        // Tabel Menu
        modelMenu = new DefaultTableModel(new String[]{"ID", "Menu", "Harga"}, 0);
        tblMenu = new JTable(modelMenu);
        JScrollPane spMenu = new JScrollPane(tblMenu);
        spMenu.setBounds(30, 50, 300, 300);
        add(spMenu);

        // Input Jumlah
        JLabel lblJumlah = new JLabel("Jumlah:");
        lblJumlah.setBounds(30, 370, 60, 20);
        add(lblJumlah);

        txtJumlah = new JTextField("1");
        txtJumlah.setBounds(90, 370, 60, 25);
        add(txtJumlah);

        JButton btnTambah = new JButton("Tambah ke Keranjang");
        btnTambah.setBounds(160, 370, 170, 25);
        add(btnTambah);

        // Tabel Keranjang
        JLabel lblKeranjang = new JLabel("Keranjang");
        lblKeranjang.setBounds(400, 20, 150, 20);
        add(lblKeranjang);

        modelKeranjang = new DefaultTableModel(new String[]{"Menu", "Harga", "Jumlah", "Subtotal"}, 0);
        tblKeranjang = new JTable(modelKeranjang);
        JScrollPane spKeranjang = new JScrollPane(tblKeranjang);
        spKeranjang.setBounds(400, 50, 360, 300);
        add(spKeranjang);

        // Total Harga
        lblTotal = new JLabel("Total: Rp 0");
        lblTotal.setBounds(400, 370, 200, 25);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTotal);

        JButton btnHapus = new JButton("Hapus Item");
        btnHapus.setBounds(400, 400, 120, 25);
        add(btnHapus);

        JButton btnLanjut = new JButton("Lanjut Pembayaran");
        btnLanjut.setBounds(540, 400, 180, 25);
        add(btnLanjut);

        // Load menu dari database
        loadMenu();

        // Event Tambah ke Keranjang
        btnTambah.addActionListener(e -> tambahKeKeranjang());

        // Event Hapus Item
        btnHapus.addActionListener(e -> hapusItem());

        // Event Lanjut
        btnLanjut.addActionListener(e -> {
    new PembayaranForm(totalHarga, userId, modelKeranjang).setVisible(true);
    dispose(); // opsional
});

    }

    private PemesananForm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void loadMenu() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/warung_nasi_goreng", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM menu")) {

            while (rs.next()) {
                int id = rs.getInt("menu_id");
                String nama = rs.getString("nama_menu");
                int harga = rs.getInt("harga");
                modelMenu.addRow(new Object[]{id, nama, harga});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load menu: " + e.getMessage());
        }
    }

    private void tambahKeKeranjang() {
        int selectedRow = tblMenu.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih menu terlebih dahulu.");
            return;
        }

        String nama = modelMenu.getValueAt(selectedRow, 1).toString();
        int harga = Integer.parseInt(modelMenu.getValueAt(selectedRow, 2).toString());
        int jumlah;

        try {
            jumlah = Integer.parseInt(txtJumlah.getText());
            if (jumlah <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah tidak valid.");
            return;
        }

        int subtotal = harga * jumlah;
        totalHarga += subtotal;
        modelKeranjang.addRow(new Object[]{nama, harga, jumlah, subtotal});
        lblTotal.setText("Total: Rp " + totalHarga);
    }

    private void hapusItem() {
        int selectedRow = tblKeranjang.getSelectedRow();
        if (selectedRow != -1) {
            int subtotal = Integer.parseInt(modelKeranjang.getValueAt(selectedRow, 3).toString());
            totalHarga -= subtotal;
            modelKeranjang.removeRow(selectedRow);
            lblTotal.setText("Total: Rp " + totalHarga);
        }
    }

    public static void main(String[] args) {
        new PemesananForm().setVisible(true);
    }
}
