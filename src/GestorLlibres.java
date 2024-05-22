import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;

public class GestorLlibres {
    private Connection conn;

    public GestorLlibres(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void afegirLlibre(String titol, String autor, String isbn, String editorial, int anyPublicacio, String categoria) {
        String query = "INSERT INTO llibres (Titol, Autor, ISBN, Editorial, Any_Publicacio, Categoria, Estat) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, titol);
            ps.setString(2, autor);
            ps.setString(3, isbn);
            ps.setString(4, editorial);
            ps.setInt(5, anyPublicacio);
            ps.setString(6, categoria);
            ps.setString(7, "Disponible");
            ps.executeUpdate();
            System.out.println("Llibre afegit amb èxit!");
        } catch (SQLException e) {
            System.out.println("Error en afegir el llibre: " + e.getMessage());
        }
    }

    public void modificarLlibre(int id_llibre, String titol, String autor, String isbn, String editorial, int anyPublicacio, String categoria) {
        String query = "UPDATE llibres SET Titol = ?, Autor = ?, ISBN = ?, Editorial = ?, Any_Publicacio = ?, Categoria = ? WHERE ID_Llibre = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, titol);
            ps.setString(2, autor);
            ps.setString(3, isbn);
            ps.setString(4, editorial);
            ps.setInt(5, anyPublicacio);
            ps.setString(6, categoria);
            ps.setInt(7, id_llibre);
            ps.executeUpdate();
            System.out.println("Llibre modificat amb èxit!");
        } catch (SQLException e) {
            System.out.println("Error en modificar el llibre: " + e.getMessage());
        }
    }

    public void eliminarLlibre(int id_llibre) {
        String query = "DELETE FROM llibres WHERE ID_Llibre = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id_llibre);
            ps.executeUpdate();
            System.out.println("Llibre eliminat amb èxit!");
        } catch (SQLException e) {
            System.out.println("Error en eliminar el llibre: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "root", "");
            GestorLlibres gestor = new GestorLlibres(conn);
            gestor.afegirLlibre("El Quixot", "Miguel de Cervantes", "1234567890123", "Editorial XYZ", 1605, "Novel·la");
            gestor.modificarLlibre(1, "El Quixot", "Miguel de Cervantes Saavedra", "1234567890123", "Editorial XYZ", 1605, "Novel·la Clàssica");
            gestor.eliminarLlibre(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}