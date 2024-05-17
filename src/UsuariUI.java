import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;


public class UsuariUI extends JFrame{
    private JTextField cerca;
    private JTable llibres;
    private JTable historic;

    private Connection conn;

    public UsuariUI(Connection conn){
        this.conn=conn;

        setTitle("Interfície usuari");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //distribució
        JPanel mainPanel=new JPanel();
        distribucioPP(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //cerca

        JPanel cercaJPanel=new JPanel();
        cerca=new JTextField(20);
        JButton btnCerca=new JButton("Cerca");
        btnCerca.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                cercaLlibres();
            }
        });
        cercaPaneladd(new JLabel("Cerca:"));
        cercaPaneladd(cerca);
        cercaPaneladd(btnCerca);

        //llibres

        JPanel llibresJPanel=new JPanel();
        llibresT=new JTable();
        JScrollPane llibresScrollPane(llibresT);
        llibresPanel(llibresScrollPane);

        //historial

        JPanel historicJPanel=new JPanel();
        historicT=new JTable();
        JScrollPane historiScrollPane(historicT);
        historicPanel(historiScrollPane);

        mainPaneladd(cercaJPanel);
        mainPaneladd(llibresJPanel);
        mainPaneladd(historicJPanel);

        add(mainPanel);

        setVisible(true);
    }

    private void cercaLlibres(){
        String cercar=cerca.getText();
        try {
            PreparedStatement ps=conn.prepareStatement("SELECT*FROM llibres WHERE titol LIKE ? OR autor LIKE ?");
            ps.setString(1,"%" + cerca + "%");
            ps.setString(2,"%" + cercar + "%");
            ResultSet rs=ps.executeQuery();

            Vector<Vector<Object>> data=new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> row=new Vector<Object>();
                row.add(rs.getString("Títol"));
                row.add(rs.getString("Autor"));
                row.add(rs.getString("Any_Publicació"));
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
