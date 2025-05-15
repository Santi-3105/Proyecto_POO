package pong;

import java.awt.*;
import javax.swing.*;

import clasesCompartidas.ObjetoGrafico;

public class MenuPong extends ObjetoGrafico {
    protected JButton options;
    protected JCheckBox sound;
    protected JButton unJugador;
    protected JButton dosJugador;
    protected JLabel titulo;

    public MenuPong() {
        JFrame frame = new JFrame();
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
        sound = new JCheckBox("Sonido");

        JPanel panelBot = new JPanel();
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

        JPanel panelBotConfg = new JPanel();
        panelBotConfg.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelBotConfg.setBackground(Color.BLACK);
        panelBotConfg.add(sound);
        panelBotConfg.add(options);
        frame.add(panelBotConfg, BorderLayout.SOUTH);

        // Estilos de los botones options y sound
        // Configurar botón options
        options.setFont(buttonFont);
        options.setForeground(Color.WHITE);
        options.setBackground(Color.BLACK);
        options.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4, true));
        // Configurar boton sound
        // Configurar checkbox sound
        sound.setFont(buttonFont);
        sound.setForeground(Color.WHITE);
        sound.setBackground(Color.BLACK);
        sound.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true)); // Borde blanco y redondeado

        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new MenuPong();
    }

    @Override
    public void update(double delta) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}
