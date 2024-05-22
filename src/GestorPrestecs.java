import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GestorPrestecs {
    private Connection conn;

    public GestorPrestecs(Connection conn) {
        this.conn = conn;
    }

    public void prestarLlibre(int id_llibre, int id_usuari) {
        String query = "INSERT INTO prestecs (ID_Llibre, ID_Usuari, Data_Préstec, Data_Retorn_Prevista, Estat) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'Actiu')";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id_llibre);
            ps.setInt(2, id_usuari);
            ps.executeUpdate();
            System.out.println("Préstec realitzat amb èxit!");
        } catch (SQLException e) {
            System.out.println("Error en prestar el llibre: " + e.getMessage());
        }
    }

    public void retornarLlibre(int id_prestec) {
        String query = "UPDATE prestecs SET Data_Retorn_Real = NOW(), Estat = 'Completat' WHERE ID_Préstec = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id_prestec);
            ps.executeUpdate();
            System.out.println("Llibre retornat amb èxit!");
        } catch (SQLException e) {
            System.out.println("Error en retornar el llibre: " + e.getMessage());
        }
    }

    public void gestionarMultes() {
        String query = "UPDATE prestecs SET Estat = 'Retardat' WHERE Data_Retorn_Real IS NULL AND Data_Retorn_Prevista < NOW()";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
            System.out.println("Multes actualitzades!");
        } catch (SQLException e) {
            System.out.println("Error en gestionar les multes: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            // Utilitza l'usuari root sense contrasenya
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "root", "");
            GestorPrestecs gestor = new GestorPrestecs(conn);
            gestor.prestarLlibre(1, 1);
            gestor.retornarLlibre(1);
            gestor.gestionarMultes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}