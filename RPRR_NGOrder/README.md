# RPRR_NGOrder
Tugas kelompok 5 kelas 04TPLP004

# RPRR_NGOrder
Aplikasi pemesanan nasi goreng berbasis Java Swing dan MySQL

## Cara install
1. Import database `warung_nasi_goreng.sql`
2. Buka project di NetBeans
3. Jalankan `LoginForm.java`

Langkah Instalasi Lengkapnya
✅ 1. Setup Database
🔹 Gunakan phpMyAdmin (XAMPP)
Buka XAMPP dan start Apache & MySQL

Akses http://localhost/phpmyadmin di browser

Klik tab Database > buat dengan nama:

nginx
Copy
Edit
warung_nasi_goreng
Klik database yang baru dibuat → klik tab Import

Pilih file warung_nasi_goreng.sql dari folder database/

Klik tombol Go

💡 Pastikan semua tabel muncul: users, menu, pesanan, pesanan_detail

✅ 2. Import Project ke NetBeans
Buka NetBeans

Klik File → Open Project

Arahkan ke folder RPRR_NGOrder/

Pilih dan buka proyeknya

✅ 3. Cek Koneksi Database di Kode
Buka salah satu file Java (misalnya LoginForm.java), lalu pastikan konfigurasi koneksi:

java
Copy
Edit
Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/warung_nasi_goreng", "root", "");
Jika password root MySQL Anda berbeda, ganti "" dengan password Anda

✅ 4. Jalankan Aplikasi
Klik kanan pada file LoginForm.java

Pilih Run File

Aplikasi akan berjalan dimulai dari form login



## Fitur
- Login & Register
- Pemesanan
- Pembayaran
- Konfirmasi
- Dashboard Admin
