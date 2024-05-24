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
    private GestorUsuaris gestorUsuaris;
    private JTextField titolField, autorField, anyPublicacioField, isbnField, editorialField, categoriaField;
    private JTextField nomField, cognomsField, emailField, telefonField, rolField;
    private JPasswordField passwordField;
    private JTable llibresTable;
    private JTable usuarisTable;

    public BibliotecariUI(Connection conn) {
        gestorLlibres = new GestorLlibres(conn);
        gestorUsuaris = new GestorUsuaris(conn);

        setTitle("Interfície Bibliotecari");
        setSize(1000, 800);
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

        JButton btnAfegirLlibre = new JButton("Afegir Llibre");
        btnAfegirLlibre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gestorLlibres.afegirLlibre(titolField.getText(), autorField.getText(), isbnField.getText(), editorialField.getText(), Integer.parseInt(anyPublicacioField.getText()), categoriaField.getText());
                carregarLlibres();
            }
        });

        JButton btnModificarLlibre = new JButton("Modificar Llibre");
        btnModificarLlibre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = llibresTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int idLlibre = (int) llibresTable.getValueAt(selectedRow, 0);
                    gestorLlibres.modificarLlibre(idLlibre, titolField.getText(), autorField.getText(), isbnField.getText(), editorialField.getText(), Integer.parseInt(anyPublicacioField.getText()), categoriaField.getText());
                    carregarLlibres();
                }
            }
        });

        JButton btnEliminarLlibre = new JButton("Eliminar Llibre");
        btnEliminarLlibre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = llibresTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int idLlibre = (int) llibresTable.getValueAt(selectedRow, 0);
                    gestorLlibres.eliminarLlibre(idLlibre);
                    carregarLlibres();
                }
            }
        });

        formPanel.add(btnAfegirLlibre);
        formPanel.add(btnModificarLlibre);
        formPanel.add(btnEliminarLlibre);

        // Taula de llibres
        JPanel llibresPanel = new JPanel();
        llibresTable = new JTable();
        JScrollPane llibresScrollPane = new JScrollPane(llibresTable);
        llibresPanel.add(llibresScrollPane);

        // Formulari per afegir/modificar usuaris
        JPanel usuariFormPanel = new JPanel();
        usuariFormPanel.setLayout(new BoxLayout(usuariFormPanel, BoxLayout.Y_AXIS));

        nomField = new JTextField(20);
        cognomsField = new JTextField(20);
        emailField = new JTextField(20);
        telefonField = new JTextField(15);
        rolField = new JTextField(20);
        passwordField = new JPasswordField(20);

        usuariFormPanel.add(new JLabel("Nom:"));
        usuariFormPanel.add(nomField);
        usuariFormPanel.add(new JLabel("Cognoms:"));
        usuariFormPanel.add(cognomsField);
        usuariFormPanel.add(new JLabel("Email:"));
        usuariFormPanel.add(emailField);
        usuariFormPanel.add(new JLabel("Telefon:"));
        usuariFormPanel.add(telefonField);
        usuariFormPanel.add(new JLabel("Rol:"));
        usuariFormPanel.add(rolField);
        usuariFormPanel.add(new JLabel("Password:"));
        usuariFormPanel.add(passwordField);

        JButton btnAfegirUsuari = new JButton("Afegir Usuari");
        btnAfegirUsuari.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gestorUsuaris.registrarUsuari(nomField.getText(), cognomsField.getText(), emailField.getText(), telefonField.getText(), rolField.getText(), new String(passwordField.getPassword()));
                carregarUsuaris();
            }
        });

        JButton btnModificarUsuari = new JButton("Modificar Usuari");
        btnModificarUsuari.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = usuarisTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int idUsuari = (int) usuarisTable.getValueAt(selectedRow, 0);
                    gestorUsuaris.modificarUsuari(idUsuari, nomField.getText(), cognomsField.getText(), emailField.getText(), telefonField.getText(), rolField.getText(), new String(passwordField.getPassword()));
                    carregarUsuaris();
                }
            }
        });

        JButton btnEliminarUsuari = new JButton("Eliminar Usuari");
        btnEliminarUsuari.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = usuarisTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int idUsuari = (int) usuarisTable.getValueAt(selectedRow, 0);
                    gestorUsuaris.eliminarUsuari(idUsuari);
                    carregarUsuaris();
                }
            }
        });

        usuariFormPanel.add(btnAfegirUsuari);
        usuariFormPanel.add(btnModificarUsuari);
        usuariFormPanel.add(btnEliminarUsuari);

        // Taula d'usuaris
        JPanel usuarisPanel = new JPanel();
        usuarisTable = new JTable();
        JScrollPane usuarisScrollPane = new JScrollPane(usuarisTable);
        usuarisPanel.add(usuarisScrollPane);

        mainPanel.add(formPanel);
        mainPanel.add(llibresPanel);
        mainPanel.add(usuariFormPanel);
        mainPanel.add(usuarisPanel);

        add(mainPanel);
        carregarLlibres();
        carregarUsuaris();
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

    private void carregarUsuaris() {
        try {
            PreparedStatement ps = gestorUsuaris.getConn().prepareStatement("SELECT * FROM usuaris");
            ResultSet rs = ps.executeQuery();

            Vector<String> columnNames = new Vector<>();
            columnNames.add("ID");
            columnNames.add("Nom");
            columnNames.add("Cognoms");
            columnNames.add("Email");
            columnNames.add("Telefon");
            columnNames.add("Rol");
            columnNames.add("Data Registre");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id_usuari"));
                row.add(rs.getString("nom"));
                row.add(rs.getString("cognoms"));
                row.add(rs.getString("email"));
                row.add(rs.getString("telefon"));
                row.add(rs.getString("rol"));
                row.add(rs.getDate("data_registre"));
                data.add(row);
            }

            usuarisTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "root", "");
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new BibliotecariUI(conn);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
