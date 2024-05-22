import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class UsuariUI extends JFrame {
    private JTextField cerca;
    private JTable llibres;
    private JTable historic;
    private Connection conn;
    private int idUsuari; // Identificador de l'usuari actual

    public UsuariUI(Connection conn, int idUsuari) {
        this.conn = conn;
        this.idUsuari = idUsuari;

        setTitle("Interfície Usuari");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Distribució
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Cerca
        JPanel cercaPanel = new JPanel();
        cerca = new JTextField(20);
        JButton btnCerca = new JButton("Cerca");
        btnCerca.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cercaLlibres();
            }
        });
        cercaPanel.add(new JLabel("Cerca:"));
        cercaPanel.add(cerca);
        cercaPanel.add(btnCerca);

        // Llibres
        JPanel llibresPanel = new JPanel();
        llibres = new JTable();
        JScrollPane llibresScrollPane = new JScrollPane(llibres);
        llibresPanel.add(llibresScrollPane);

        // Històric
        JPanel historicPanel = new JPanel();
        historic = new JTable();
        JScrollPane historicScrollPane = new JScrollPane(historic);
        historicPanel.add(historicScrollPane);

        mainPanel.add(cercaPanel);
        mainPanel.add(llibresPanel);
        mainPanel.add(historicPanel);

        add(mainPanel);
        
        carregarHistoric();
        setVisible(true);
    }

    private void cercaLlibres() {
        String cercar = cerca.getText();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM llibres WHERE titol LIKE ? OR autor LIKE ?");
            ps.setString(1, "%" + cercar + "%");
            ps.setString(2, "%" + cercar + "%");
            ResultSet rs = ps.executeQuery();

            Vector<String> columnNames = new Vector<>();
            columnNames.add("Títol");
            columnNames.add("Autor");
            columnNames.add("Any Publicació");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("titol"));
                row.add(rs.getString("autor"));
                row.add(rs.getInt("any_publicacio"));
                data.add(row);
            }

            llibres.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void carregarHistoric() {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM prestecs WHERE id_usuari = ?");
            ps.setInt(1, idUsuari);
            ResultSet rs = ps.executeQuery();

            Vector<String> columnNames = new Vector<>();
            columnNames.add("ID Préstec");
            columnNames.add("ID Llibre");
            columnNames.add("Data Préstec");
            columnNames.add("Data Retorn Prevista");
            columnNames.add("Data Retorn Real");
            columnNames.add("Estat");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_prestec"));
                row.add(rs.getInt("id_llibre"));
                row.add(rs.getDate("data_prestec"));
                row.add(rs.getDate("data_retorn_prevista"));
                row.add(rs.getDate("data_retorn_real"));
                row.add(rs.getString("estat"));
                data.add(row);
            }

            historic.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "root", "");
            new UsuariUI(conn, 1); // Substitueix 1 per l'ID real de l'usuari actual
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}