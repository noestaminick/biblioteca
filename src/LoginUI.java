import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Connection conn;

    public LoginUI(Connection conn) {
        this.conn = conn;

        setTitle("Log In");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        panel.add(new JLabel("Nom d'Usuari:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JButton loginButton = new JButton("Log In");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                autenticarUsuari();
            }
        });

        panel.add(loginButton);

        add(panel);
        setVisible(true);
    }

    private void autenticarUsuari() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT rol, id_usuari FROM usuaris WHERE nom = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");
                int idUsuari = rs.getInt("id_usuari");
                if (rol.equals("bibliotecari")) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            new BibliotecariUI(conn);
                        }
                    });
                } else if (rol.equals("usuari")) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            new UsuariUI(conn, idUsuari);
                        }
                    });
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credencials incorrectes");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "root", "");
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new LoginUI(conn);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
