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
    protected JLabel titulo;
    protected JPanel panelBot;
    protected JPanel panelBotConfg;
    protected MenuConfig config;
    protected JFrame frame;

    public MenuPong() {
        frame = new JFrame();
        frame.setTitle("Pong");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.BLACK);

        // Crear título
        titulo = new JLabel("PONG");
        titulo.setFont(new Font("Arial", Font.BOLD, 80));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(JLabel.CENTER);
        frame.add(titulo, BorderLayout.NORTH);

        unJugador = new JButton("Un jugador");
        dosJugador = new JButton("Dos jugadores");
        options = new JButton("Configuración");
        options.addActionListener(this);

        panelBot = new JPanel();
        panelBot.setLayout(new GridBagLayout());
        panelBot.setBackground(Color.BLACK);

        // Creo una fuente para los botones
        Font buttonFont = new Font("Arial", Font.BOLD, 20);

        // Configurar botón unJugador
        unJugador.setPreferredSize(new Dimension(200, 50));
        unJugador.setFont(buttonFont);
        unJugador.setForeground(Color.WHITE);
        unJugador.setBackground(Color.BLACK);
        unJugador.setBorder(BorderFactory.createLineBorder(Color.WHITE, 6, true)); // Borde blanco y redondeado
        // Configurar boton dosJugador
        dosJugador.setPreferredSize(new Dimension(200, 50));
        dosJugador.setFont(buttonFont);
        dosJugador.setForeground(Color.WHITE);
        dosJugador.setBackground(Color.BLACK);
        dosJugador.setBorder(BorderFactory.createLineBorder(Color.WHITE, 6, true)); // Borde blanco y redondeado
        panelBot.add(unJugador);
        panelBot.add(dosJugador);
        frame.add(panelBot, BorderLayout.CENTER);

        panelBotConfg = new JPanel();
        panelBotConfg.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelBotConfg.setBackground(Color.BLACK);
        panelBotConfg.add(options);
        frame.add(panelBotConfg, BorderLayout.SOUTH);

        // Estilos de los botones options y sound
        // Configurar botón options
        options.setFont(buttonFont);
        options.setForeground(Color.WHITE);
        options.setBackground(Color.BLACK);
        options.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4, true));

        frame.setVisible(true);
        // Crear el menú de configuración pero sin hacerlo visible aún
        config = new MenuConfig(frame, this);
    }
       public void mostrarMenuPrincipal() {
        titulo.setVisible(true);
        panelBot.setVisible(true);
        panelBotConfg.setVisible(true);
    }

    public void ocultarMenuPrincipal() {
        titulo.setVisible(false);
        panelBot.setVisible(false);
        panelBotConfg.setVisible(false);
    }
    public static void main(String[] args) {
        new MenuPong();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
            if(e.getSource()==options)
        {
            ocultarMenuPrincipal();
            config.mostrarMenuConfig();
        }
    }
}
