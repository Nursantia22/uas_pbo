package Main;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // LINK MYSQL –> sesuaikan database, user, dan password kamu di sini
        String url = "jdbc:mysql://localhost:3306/toko";
        String user = "root";
        String password = ""; // isi jika pakai password MySQL

        try {
            // Load driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Koneksi ke MySQL
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Koneksi ke database berhasil.\n");

            // Ambil input dari user
            System.out.print("Masukkan kode barang: ");
            String kode = input.nextLine();
            System.out.print("Masukkan nama barang: ");
            String nama = input.nextLine();
            System.out.print("Masukkan harga barang: ");
            int harga = input.nextInt();
            System.out.print("Masukkan stok barang: ");
            int stok = input.nextInt();

            if (harga < 0 || stok < 0) {
                System.out.println("❌ Harga dan stok tidak boleh negatif!");
                return;
            }

            // Panggil procedure untuk insert
            CallableStatement cs = conn.prepareCall("{CALL insert_barang(?, ?, ?, ?)}");
            cs.setString(1, kode);
            cs.setString(2, nama);
            cs.setInt(3, harga);
            cs.setInt(4, stok);
            cs.execute();
            System.out.println("\n✅ Data berhasil disimpan ke database.\n");

            // Tampilkan semua data dari view
            System.out.println("📋 Data Barang dari View:");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM view_barang_total");

            System.out.println("Kode | Nama | Harga | Stok | Total Nilai");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                System.out.println(
                        rs.getString("kode") + " | " +
                                rs.getString("nama") + " | " +
                                rs.getInt("harga") + " | " +
                                rs.getInt("stok") + " | " +
                                rs.getInt("total_nilai"));
            }

            // Tampilkan nama barang saja
            System.out.println("\n🧾 Daftar Nama Barang:");
            rs = st.executeQuery("SELECT nama FROM barang");
            while (rs.next()) {
                System.out.println("- " + rs.getString("nama"));
            }

            // Tutup koneksi
            conn.close();

        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver JDBC tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("❌ Kesalahan SQL: " + e.getMessage());
        }
    }
}
