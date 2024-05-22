import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestorUsuaris {
    private Connection conn;

    public GestorUsuaris(Connection conn) {
        this.conn = conn;
    }

    public void registrarUsuari(String nom, String cognoms, String email, String telefon, String rol) {
        String query = "INSERT INTO usuaris (Nom, Cognoms, Email, Telefon, Rol, Data_Registre) VALUES (?, ?, ?, ?, ?, NOW())";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, nom);
            ps.setString(2, cognoms);
            ps.setString(3, email);
            ps.setString(4, telefon);
            ps.setString(5, rol);
            ps.executeUpdate();
            System.out.println("Usuari registrat amb èxit!");
        } catch (SQLException e) {
            System.out.println("Error en registrar l'usuari: " + e.getMessage());
        }
    }

    public void modificarUsuari(int id_usuari, String nom, String cognoms, String email, String telefon, String rol) {
        String query = "UPDATE usuaris SET Nom = ?, Cognoms = ?, Email = ?, Telefon = ?, Rol = ? WHERE ID_Usuari = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, nom);
            ps.setString(2, cognoms);
            ps.setString(3, email);
            ps.setString(4, telefon);
            ps.setString(5, rol);
            ps.setInt(6, id_usuari);
            ps.executeUpdate();
            System.out.println("Usuari modificat amb èxit!");
        } catch (SQLException e) {
            System.out.println("Error en modificar l'usuari: " + e.getMessage());
        }
    }

    public void eliminarUsuari(int id_usuari) {
        String query = "DELETE FROM usuaris WHERE ID_Usuari = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id_usuari);
            ps.executeUpdate();
            System.out.println("Usuari eliminat amb èxit!");
        } catch (SQLException e) {
            System.out.println("Error en eliminar l'usuari: " + e.getMessage());
        }
    }

    public void veureHistorialPrestecs(int id_usuari) {
        String query = "SELECT * FROM prestecs WHERE id_usuari = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id_usuari);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("ID Préstec: " + rs.getInt("id_prestec") + ", ID Llibre: " + rs.getInt("id_llibre") + ", Data Préstec: " + rs.getDate("data_prestec") + ", Data Retorn Prevista: " + rs.getDate("data_retorn_prevista") + ", Data Retorn Real: " + rs.getDate("data_retorn_real"));
            }
        } catch (SQLException e) {
            System.out.println("Error en veure l'historial de préstecs: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            // Utilitza l'usuari root sense contrasenya
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "root", "");
            GestorUsuaris gestor = new GestorUsuaris(conn);
            gestor.registrarUsuari("Joan", "Garcia", "joan@example.com", "123456789", "Lector");
            gestor.modificarUsuari(1, "Joan", "Garcia i López", "joan@example.com", "987654321", "Lector");
            gestor.eliminarUsuari(1);
            gestor.veureHistorialPrestecs(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}