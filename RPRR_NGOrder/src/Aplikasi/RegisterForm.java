/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Aplikasi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterForm extends JFrame {
    JTextField txtUsername, txtTelp, txtAlamat;
    JPasswordField txtPassword;
    JComboBox<String> cmbJenisAkun;
    JButton btnRegister, btnKembali;

    public RegisterForm() {
        setTitle("Registrasi");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.CYAN);
        panel.setBounds(0, 0, 400, 400);
        panel.setLayout(null);
        add(panel);

        JLabel lblTitle = new JLabel("REGISTRASI");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(140, 10, 200, 30);
        panel.add(lblTitle);

        JLabel lblUsername = new JLabel("USERNAME:");
        lblUsername.setBounds(30, 50, 100, 25);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(140, 50, 200, 25);
        panel.add(txtUsername);

        JLabel lblPassword = new JLabel("PASSWORD:");
        lblPassword.setBounds(30, 85, 100, 25);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(140, 85, 200, 25);
        panel.add(txtPassword);

        JLabel lblTelp = new JLabel("NO TELP:");
        lblTelp.setBounds(30, 120, 100, 25);
        panel.add(lblTelp);

        txtTelp = new JTextField();
        txtTelp.setBounds(140, 120, 200, 25);
        panel.add(txtTelp);

        JLabel lblAlamat = new JLabel("ALAMAT:");
        lblAlamat.setBounds(30, 155, 100, 25);
        panel.add(lblAlamat);

        txtAlamat = new JTextField();
        txtAlamat.setBounds(140, 155, 200, 25);
        panel.add(txtAlamat);

        JLabel lblJenisAkun = new JLabel("JENIS AKUN:");
        lblJenisAkun.setBounds(30, 190, 100, 25);
        panel.add(lblJenisAkun);

        cmbJenisAkun = new JComboBox<>(new String[]{"user", "admin"});
        cmbJenisAkun.setBounds(140, 190, 200, 25);
        panel.add(cmbJenisAkun);

        btnRegister = new JButton("REGISTER");
        btnRegister.setBounds(70, 240, 110, 30);
        panel.add(btnRegister);

        btnKembali = new JButton("KEMBALI");
        btnKembali.setBounds(200, 240, 110, 30);
        panel.add(btnKembali);

        // aksi tombol
        btnRegister.addActionListener(e -> register());
        btnKembali.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
    }

    private void register() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String telp = txtTelp.getText().trim();
        String alamat = txtAlamat.getText().trim();
        String jenisAkun = cmbJenisAkun.getSelectedItem().toString();

        // Validasi sederhana
        if (username.isEmpty() || password.isEmpty() || telp.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/warung_nasi_goreng", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (username, password, no_telpon, alamat, jenis_akun) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, telp);
            stmt.setString(4, alamat);
            stmt.setString(5, jenisAkun);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registrasi berhasil!");
            new LoginForm().setVisible(true);
            dispose();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Registrasi gagal: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new RegisterForm().setVisible(true);
    }
}
