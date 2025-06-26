/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Aplikasi;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginForm extends JFrame {
    JTextField txtUsername;
    JPasswordField txtPassword;
    JButton btnLogin, btnRegister;

    public LoginForm() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.CYAN);
        panel.setBounds(0, 0, 400, 250);
        panel.setLayout(null);
        add(panel);

        JLabel lblTitle = new JLabel("LOGIN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(160, 10, 100, 30);
        panel.add(lblTitle);

        JLabel lblUsername = new JLabel("USERNAME");
        lblUsername.setBounds(30, 60, 100, 25);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(130, 60, 220, 25);
        panel.add(txtUsername);

        JLabel lblPassword = new JLabel("PASSWORD");
        lblPassword.setBounds(30, 100, 100, 25);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(130, 100, 220, 25);
        panel.add(txtPassword);

        btnLogin = new JButton("LOGIN");
        btnLogin.setBounds(80, 150, 100, 30);
        panel.add(btnLogin);

        btnRegister = new JButton("REGISTER");
        btnRegister.setBounds(200, 150, 100, 30);
        panel.add(btnRegister);

        btnLogin.addActionListener(e -> login());
        btnRegister.addActionListener(e -> {
            new RegisterForm().setVisible(true);
            dispose();
        });
    }

    private void login() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password wajib diisi.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/warung_nasi_goreng", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT user_id, username, jenis_akun FROM users WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String jenisAkun = rs.getString("jenis_akun");

                JOptionPane.showMessageDialog(this, "Login berhasil sebagai " + jenisAkun + "!");

                if (jenisAkun.equalsIgnoreCase("admin")) {
                    new AdminDashboardForm().setVisible(true);
                } else {
                    new PemesananForm(userId).setVisible(true);
                }
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Koneksi gagal: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new LoginForm().setVisible(true);
    }
}
