package repository;

import model.Karyawan;
import util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KaryawanRepository {
    
    public static void insert(Karyawan k) {
        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO karyawan (nama, jabatan, gaji) VALUES (?, ?, ?)"
            );
            stmt.setString(1, k.nama);
            stmt.setString(2, k.jabatan);
            stmt.setDouble(3, k.gaji);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Karyawan> getAll() {
        List<Karyawan> list = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM karyawan");

            while(rs.next()) {
                Karyawan k = new Karyawan();
                k.id = rs.getInt("id");
                k.nama = rs.getString("nama");
                k.jabatan = rs.getString("jabatan");
                k.gaji = rs.getDouble("gaji");
                list.add(k);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Karyawan cariById(int id) {
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM karyawan WHERE id=?");
            ps.setInt(1, id);
                
            ResultSet rs = ps.executeQuery();
                    
            if (rs.next()) {
                        Karyawan k = new Karyawan();
                        k.id = rs.getInt("id");
                        k.nama = rs.getString("nama");
                        k.jabatan = rs.getString("jabatan");
                        k.gaji = rs.getDouble("gaji");
                        return k;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void update(Karyawan k) {
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE karyawan SET nama=?, jabatan=?, gaji=? WHERE id=?"
            );

            ps.setString(1, k.nama);
            ps.setString(2, k.jabatan);
            ps.setDouble(3, k.gaji);
            ps.setInt(4, k.id);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hapusKaryawan(int id) {
        Connection conn = Database.getConnection();
        try {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement("DELETE FROM karyawan WHERE id=?");
            ps.setInt(1, id);
            
            conn.commit();

            return ps.executeUpdate() > 0;
        } catch(Exception e) {
            try {
                conn.rollback();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
