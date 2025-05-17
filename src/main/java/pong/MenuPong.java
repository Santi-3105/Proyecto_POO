package pong;

import java.awt.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPong implements ActionListener {
    protected JButton options;
    protected JCheckBox sound;
    protected JButton unJugador;
    protected JButton dosJugador;
    protected MenuConfig config;
    protected JFrame frame;
    protected JPanel fondoMenu;
    protected JPanel tarjeta;
    protected CardLayout cardLayout;
    protected JPanel panelPrincipal;

    public MenuPong() {
        frame = new JFrame();
        frame.setTitle("Pong");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Crear panel con CardLayout para intercambiar menús
        tarjeta = new JPanel();
        cardLayout = new CardLayout();
        tarjeta.setLayout(cardLayout);

        // Crear panel con fondo de imagen
        fondoMenu = new JPanel() {
            Image pongFondo = new ImageIcon(getClass().getResource("/pong/pongFondo.jpg")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (pongFondo != null) {
                    g.drawImage(pongFondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.out.println("No se pudo cargar la imagen de fondo.");
                }
            }
        };
        fondoMenu.setLayout(new BorderLayout());

        // Crear el menú principal
        panelPrincipal = new JPanel();
        panelPrincipal.setOpaque(false); // No cubre la imagen de fondo
        panelPrincipal.setLayout(new GridBagLayout());

        // Crear botones
        unJugador = new JButton("Un jugador");
        dosJugador = new JButton("Dos jugadores");
        options = new JButton("Configuración");
        options.addActionListener(this);

        // Configurar botones
        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        unJugador.setPreferredSize(new Dimension(200, 50));
        unJugador.setFont(buttonFont);
        unJugador.setForeground(Color.WHITE);
        unJugador.setBackground(Color.BLACK);
        unJugador.setBorder(BorderFactory.createLineBorder(Color.WHITE, 6, true));

        dosJugador.setPreferredSize(new Dimension(200, 50));
        dosJugador.setFont(buttonFont);
        dosJugador.setForeground(Color.WHITE);
        dosJugador.setBackground(Color.BLACK);
        dosJugador.setBorder(BorderFactory.createLineBorder(Color.WHITE, 6, true));

        options.setPreferredSize(new Dimension(200, 50));
        options.setFont(buttonFont);
        options.setForeground(Color.WHITE);
        options.setBackground(Color.BLACK);
        options.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4, true));

        // Añadir botones al panel principal
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy = 0;
        panelPrincipal.add(unJugador, gbc);
        gbc.gridy++;
        panelPrincipal.add(dosJugador, gbc);
        gbc.gridy++;
        panelPrincipal.add(options, gbc);

        // Añadir panel principal a fondo
        fondoMenu.add(panelPrincipal, BorderLayout.CENTER);

        // Añadir el panel con la imagen de fondo al card layout
        tarjeta.add(fondoMenu, "menuPrincipal");

        // Crear el menú de configuración pero sin hacerlo visible aún
        config = new MenuConfig(frame, this);
        tarjeta.add(config.getPanelConfig(), "menuConfig");

        // Añadir tarjeta al frame
        frame.add(tarjeta);
        frame.setVisible(true);

        //Termina constructor
    }

    public void mostrarMenuPrincipal() {
        cardLayout.show(tarjeta, "menuPrincipal");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == options) {
            cardLayout.show(tarjeta, "menuConfig");
        }
    }

    public static void main(String[] args) {
        new MenuPong();
    }
}
