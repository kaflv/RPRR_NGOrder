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

public class PembayaranForm extends JFrame {
    JLabel lblTotal, lblKembalian;
    JTextField txtBayar;
    JComboBox<String> cmbMetode;
    JButton btnBayar;

    int totalHarga;
    int userId;
    DefaultTableModel keranjangModel;

    public PembayaranForm(int totalHarga, int userId, DefaultTableModel keranjangModel) {
        this.totalHarga = totalHarga;
        this.userId = userId;
        this.keranjangModel = keranjangModel;

        setTitle("Form Pembayaran");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lbl1 = new JLabel("Total Belanja:");
        lbl1.setBounds(30, 30, 120, 25);
        add(lbl1);

        lblTotal = new JLabel("Rp " + totalHarga);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setBounds(150, 30, 200, 25);
        add(lblTotal);

        JLabel lbl2 = new JLabel("Metode:");
        lbl2.setBounds(30, 70, 120, 25);
        add(lbl2);

        cmbMetode = new JComboBox<>(new String[]{"COD", "Transfer"});
        cmbMetode.setBounds(150, 70, 150, 25);
        add(cmbMetode);

        JLabel lbl3 = new JLabel("Bayar:");
        lbl3.setBounds(30, 110, 120, 25);
        add(lbl3);

        txtBayar = new JTextField();
        txtBayar.setBounds(150, 110, 150, 25);
        add(txtBayar);

        JLabel lbl4 = new JLabel("Kembalian:");
        lbl4.setBounds(30, 150, 120, 25);
        add(lbl4);

        lblKembalian = new JLabel("Rp 0");
        lblKembalian.setBounds(150, 150, 150, 25);
        add(lblKembalian);

        btnBayar = new JButton("Bayar Sekarang");
        btnBayar.setBounds(100, 200, 180, 30);
        add(btnBayar);

        btnBayar.addActionListener(e -> prosesBayar());
    }

    private void prosesBayar() {
        int bayar;
        try {
            bayar = Integer.parseInt(txtBayar.getText());
            if (bayar < totalHarga) {
                JOptionPane.showMessageDialog(this, "Uang tidak cukup.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input tidak valid.");
            return;
        }

        int kembalian = bayar - totalHarga;
        lblKembalian.setText("Rp " + kembalian);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/warung_nasi_goreng", "root", "")) {
            conn.setAutoCommit(false);

            // 1. Simpan ke pesanan
            String sqlPesanan = "INSERT INTO pesanan (user_id, total_harga, status) VALUES (?, ?, ?)";
            PreparedStatement psPesanan = conn.prepareStatement(sqlPesanan, Statement.RETURN_GENERATED_KEYS);
            psPesanan.setInt(1, userId);
            psPesanan.setInt(2, totalHarga);
            psPesanan.setString(3, "Dipesan");
            psPesanan.executeUpdate();

            ResultSet rs = psPesanan.getGeneratedKeys();
            int pesananId = 0;
            if (rs.next()) {
                pesananId = rs.getInt(1);
            }

            // 2. Simpan ke pesanan_detail
            String sqlDetail = "INSERT INTO pesanan_detail (pesanan_id, menu_id, jumlah, subtotal) VALUES (?, ?, ?, ?)";
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);

            for (int i = 0; i < keranjangModel.getRowCount(); i++) {
                String namaMenu = keranjangModel.getValueAt(i, 0).toString();
                int harga = Integer.parseInt(keranjangModel.getValueAt(i, 1).toString());
                int jumlah = Integer.parseInt(keranjangModel.getValueAt(i, 2).toString());
                int subtotal = Integer.parseInt(keranjangModel.getValueAt(i, 3).toString());

                int menuId = getMenuIdByName(conn, namaMenu);
                if (menuId == -1) continue;

                psDetail.setInt(1, pesananId);
                psDetail.setInt(2, menuId);
                psDetail.setInt(3, jumlah);
                psDetail.setInt(4, subtotal);
                psDetail.addBatch();
            }
            psDetail.executeBatch();
            conn.commit();

            JOptionPane.showMessageDialog(this, "Pembayaran berhasil!");

            // Menuju ke form konfirmasi
            new KonfirmasiForm(pesananId).setVisible(true);
            dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan pesanan: " + e.getMessage());
        }
    }

    private int getMenuIdByName(Connection conn, String namaMenu) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT menu_id FROM menu WHERE nama_menu = ?");
            ps.setString(1, namaMenu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("menu_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
