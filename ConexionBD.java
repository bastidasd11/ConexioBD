import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD extends JFrame {

    private JTextField txtUrl;           // Caja de texto para la URL de conexión
    private JTextField txtUsuario;       // Caja de texto para el nombre de usuario
    private JPasswordField txtPassword;  // Caja de texto para la contraseña
    private JButton btnProbar;           // Botón "Probar conexión"
    private JTextArea areaResultado;     // Área donde se muestra si salió bien o mal

    // Constructor
    public ConexionBD() {

        super("Probador de conexión JDBC");              // Le ponemos título a la ventana
        setSize(500, 400);                       // Definimos el tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Cuando se cierre la ventana, termina el programa
        setLocationRelativeTo(null);                     // Centramos la ventana en la pantalla
        setLayout(new BorderLayout());                   // La ventana usa BorderLayout

        // Panel de arriba con los campos para escribir
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new GridLayout(3, 2, 5, 5));

        JLabel lblUrl = new JLabel("URL de conexión:");  // Etiqueta para la URL
        txtUrl = new JTextField("jdbc:sqlite:inventario.db");
        JLabel lblUsuario = new JLabel("Usuario:");      // Etiqueta para el usuario
        txtUsuario = new JTextField();
        JLabel lblPassword = new JLabel("Contraseña:");  // Etiqueta para la contraseña
        txtPassword = new JPasswordField();

        panelCampos.add(lblUrl);
        panelCampos.add(txtUrl);
        panelCampos.add(lblUsuario);
        panelCampos.add(txtUsuario);
        panelCampos.add(lblPassword);
        panelCampos.add(txtPassword);

        // Botón
        btnProbar = new JButton("Probar conexión");      // Creamos el botón

        // Panel de arriba que junta los campos y el botón
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(panelCampos, BorderLayout.CENTER);
        panelNorte.add(btnProbar, BorderLayout.SOUTH);

        // Área de resultados
        areaResultado = new JTextArea();                 // Creamos el área de texto donde saldrán los mensajes
        areaResultado.setEditable(false);                // Evitamos que el usuario escriba en esta área
        areaResultado.setLineWrap(true);                 // Si el mensaje es muy largo, salta de línea automáticamente
        areaResultado.setWrapStyleWord(true);            // El salto de línea se hace por palabras completas
        JScrollPane scroll = new JScrollPane(areaResultado); // Para tener barra de desplazamiento

        // Agregamos todo a la ventana
        add(panelNorte, BorderLayout.NORTH);             // El panel con campos y botón va arriba de la ventana
        add(scroll, BorderLayout.CENTER);                // El área de resultados ocupa el resto de la ventana

        // Evento del botón
        btnProbar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                probarConexion();
            }
        });
    }

    // Método que intenta conectarse a la base de datos
    private void probarConexion() {

        String url = txtUrl.getText();                     // Leemos la URL que escribió el usuario
        String usuario = txtUsuario.getText();             // Leemos el usuario
        String password = new String(txtPassword.getPassword()); // Leemos la contraseña

        areaResultado.setText("");                         // Limpiamos el área de resultados antes de cada prueba

        try {

            Class.forName("org.sqlite.JDBC");               // Cargamos el driver de SQLite


            try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {

                areaResultado.setForeground(new Color(0, 128, 0)); // Ponemos el texto en color verde
                areaResultado.setText("Conexión exitosa");          // Mostramos el mensaje de éxito
            }                                              // Aquí la conexión se cierra automáticamente

        } catch (ClassNotFoundException ex) {              // Se ejecuta si no se encontró el driver de SQLite

            areaResultado.setForeground(Color.RED);        // Ponemos el texto en color rojo
            areaResultado.setText("Error: no se encontró el driver de SQLite.\n" + ex.getMessage()); // Mostramos el error del driver

        } catch (SQLException ex) {                        // Se ejecuta si falla la conexión

            areaResultado.setForeground(Color.RED);        // Ponemos el texto en color rojo
            areaResultado.setText("Fallo de conexión\n" + ex.getMessage());
        }
    }

    // Método main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ConexionBD ventana = new ConexionBD();
                ventana.setVisible(true);
            }
        });
    }
}
