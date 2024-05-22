import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class BibliotecariUI extends JFrame {
    private GestorLlibres gestorLlibres;
    private JTextField titolField, autorField, anyPublicacioField, isbnField, editorialField, categoriaField;
    private JTable llibresTable;

    public BibliotecariUI(Connection conn) {
        gestorLlibres = new GestorLlibres(conn);

        setTitle("Interfície Bibliotecari");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Distribució
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Formulari per afegir/modificar llibres
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        titolField = new JTextField(20);
        autorField = new JTextField(20);
        anyPublicacioField = new JTextField(4);
        isbnField = new JTextField(13);
        editorialField = new JTextField(20);
        categoriaField = new JTextField(20);

        formPanel.add(new JLabel("Títol:"));
        formPanel.add(titolField);
        formPanel.add(new JLabel("Autor:"));
        formPanel.add(autorField);
        formPanel.add(new JLabel("Any Publicació:"));
        formPanel.add(anyPublicacioField);
        formPanel.add(new JLabel("ISBN:"));
        formPanel.add(isbnField);
        formPanel.add(new JLabel("Editorial:"));
        formPanel.add(editorialField);
        formPanel.add(new JLabel("Categoria:"));
        formPanel.add(categoriaField);

        JButton btnAfegir = new JButton("Afegir Llibre");
        btnAfegir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gestorLlibres.afegirLlibre(titolField.getText(), autorField.getText(), isbnField.getText(), editorialField.getText(), Integer.parseInt(anyPublicacioField.getText()), categoriaField.getText());
                carregarLlibres();
            }
        });

        JButton btnModificar = new JButton("Modificar Llibre");
        btnModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = llibresTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int idLlibre = (int) llibresTable.getValueAt(selectedRow, 0);
                    gestorLlibres.modificarLlibre(idLlibre, titolField.getText(), autorField.getText(), isbnField.getText(), editorialField.getText(), Integer.parseInt(anyPublicacioField.getText()), categoriaField.getText());
                    carregarLlibres();
                }
            }
        });

        JButton btnEliminar = new JButton("Eliminar Llibre");
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = llibresTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int idLlibre = (int) llibresTable.getValueAt(selectedRow, 0);
                    gestorLlibres.eliminarLlibre(idLlibre);
                    carregarLlibres();
                }
            }
        });

        formPanel.add(btnAfegir);
        formPanel.add(btnModificar);
        formPanel.add(btnEliminar);

        // Taula de llibres
        JPanel llibresPanel = new JPanel();
        llibresTable = new JTable();
        JScrollPane llibresScrollPane = new JScrollPane(llibresTable);
        llibresPanel.add(llibresScrollPane);

        mainPanel.add(formPanel);
        mainPanel.add(llibresPanel);

        add(mainPanel);
        carregarLlibres();
        setVisible(true);
    }

    private void carregarLlibres() {
        try {
            PreparedStatement ps = gestorLlibres.getConn().prepareStatement("SELECT * FROM llibres");
            ResultSet rs = ps.executeQuery();

            Vector<String> columnNames = new Vector<>();
            columnNames.add("ID");
            columnNames.add("Títol");
            columnNames.add("Autor");
            columnNames.add("Any Publicació");
            columnNames.add("ISBN");
            columnNames.add("Editorial");
            columnNames.add("Categoria");
            columnNames.add("Estat");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_llibre"));
                row.add(rs.getString("titol"));
                row.add(rs.getString("autor"));
                row.add(rs.getInt("any_publicacio"));
                row.add(rs.getString("isbn"));
                row.add(rs.getString("editorial"));
                row.add(rs.getString("categoria"));
                row.add(rs.getString("estat"));
                data.add(row);
            }

            llibresTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "root", "");
            new BibliotecariUI(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}