package pong;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.sound.sampled.Clip;
import javax.swing.*;

public class MenuConfig implements ActionListener {
    protected JRadioButton ventana;
    protected JRadioButton pantallaCompleta;
    protected Clip musica;
    protected JCheckBox musicaBox;
    protected JComboBox<String> pistaMusical;
    protected JComboBox<String> pelota;
    protected JComboBox<String> cancha;
    protected JComboBox<String> paleta;
    protected JTextField movArriba1;
    protected JTextField movAbajo1;
    protected JTextField movArriba2;
    protected JTextField movAbajo2;
    protected JButton reset;
    protected JButton atras;
    protected JPanel config;
    protected JPanel panelConfigBot;
    protected JFrame frame;
    protected MenuPong menuPong;
    protected JPanel panelCompleto;

    public MenuConfig(JFrame frame, MenuPong menuPong) {

        this.frame = frame;
        this.menuPong = menuPong;

        // Crear el panel principal del menú de configuración
        // Crear el panel principal del menú de configuración con fondo de imagen
        panelCompleto = new JPanel(new BorderLayout()) {
            Image pongFondo = new ImageIcon(getClass().getResource("/pong/confgFondo.jpg")).getImage();

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
        // Config botones
        ventana = new JRadioButton("Ventana", true);
        pantallaCompleta = new JRadioButton("Pantalla completa", false);
        ButtonGroup grupoVentana = new ButtonGroup();
        grupoVentana.add(ventana);
        grupoVentana.add(pantallaCompleta);
        musicaBox = new JCheckBox("", true);
        musicaBox.addActionListener(this);
        movArriba1 = new JTextField("↑", 5);
        movAbajo1 = new JTextField("↓", 5);
        movArriba2 = new JTextField("W", 5);
        movAbajo2 = new JTextField("S", 5);
        pistaMusical = new JComboBox<>(new String[] { "Original", "V2", "V3", "V4" });
        pelota = new JComboBox<>(new String[] { "Original", "V2", "V3", "V4" });
        paleta = new JComboBox<>(new String[] { "Original", "V2", "V3", "V4" });
        cancha = new JComboBox<>(new String[] { "Original", "V2", "V3", "V4" });
        reset = new JButton("Restablecer");
        reset.addActionListener(this);
        reset.setVisible(true);
        atras = new JButton("←");
        atras.addActionListener(this);
        atras.setVisible(true);

        frame.setVisible(true);

        // Creo el menu configuracion
        // Añado sus botones
        // Config panel
        config = new JPanel(new GridBagLayout());
        config.setOpaque(false);
        // config.setVisible(false);
        // config.setBackground(Color.BLACK);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(5, 5, 5, 5);
        config.add(new JLabel("Pantalla: "), g);
        g.gridx++;
        config.add(ventana, g);
        g.gridx++;
        config.add(pantallaCompleta, g);
        g.gridx = 0;
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Musica: "), g);
        g.gridx++;
        config.add(musicaBox, g);
        g.gridx = 0;
        g.gridy++;
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Teclas por defecto: "), g);
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Habilidades jugador 1: "), g);
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Movimiento arriba: "), g);
        g.gridx++;
        config.add(movArriba1, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Movimiento abajo: "), g);
        g.gridx++;
        config.add(movAbajo1, g);
        g.gridx = 0;
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Habilidades jugador 2: "), g);
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Movimiento arriba: "), g);
        g.gridx++;
        config.add(movArriba2, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Movimiento abajo: "), g);
        g.gridx++;
        config.add(movAbajo2, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Elegir pista musical: "), g);
        g.gridx++;
        config.add(pistaMusical, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Elegir pelota: "), g);
        g.gridx++;
        config.add(pelota, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Elegir cancha: "), g);
        g.gridx++;
        config.add(cancha, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Elegir paleta: "), g);
        g.gridx++;
        config.add(paleta, g);

        // Config panel botones
        panelConfigBot = new JPanel();
        panelConfigBot.add(atras);
        panelConfigBot.add(reset);
        panelConfigBot.setOpaque(false);
        // panelConfigBot.setVisible(false);
        // panelConfigBot.setBackground(Color.BLACK);
        // NUEVO
        // Configuro los botones con color blanco y fondo transparente
        Font buttonFontConfg = new Font("Courier New", Font.BOLD, 20);
        ventana.setFont(buttonFontConfg);
        ventana.setForeground(Color.WHITE);
        ventana.setBackground(Color.BLACK);
        ventana.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));

        pantallaCompleta.setFont(buttonFontConfg);
        pantallaCompleta.setForeground(Color.WHITE);
        pantallaCompleta.setBackground(Color.BLACK);
        pantallaCompleta.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));

        musicaBox.setFont(buttonFontConfg);
        musicaBox.setForeground(Color.WHITE);
        musicaBox.setBackground(Color.BLACK);
        musicaBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));

        pistaMusical.setFont(buttonFontConfg);
        pistaMusical.setForeground(Color.WHITE);
        pistaMusical.setBackground(Color.BLACK);
        pistaMusical.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));

        pelota.setFont(buttonFontConfg);
        pelota.setForeground(Color.WHITE);
        pelota.setBackground(Color.BLACK);
        pelota.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));

        cancha.setFont(buttonFontConfg);
        cancha.setForeground(Color.WHITE);
        cancha.setBackground(Color.BLACK);
        cancha.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));

        paleta.setFont(buttonFontConfg);
        paleta.setForeground(Color.WHITE);
        paleta.setBackground(Color.BLACK);
        paleta.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));

        movArriba1.setFont(buttonFontConfg);
        movArriba1.setForeground(Color.WHITE);
        movArriba1.setBackground(Color.BLACK);
        movArriba1.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));

        movAbajo1.setFont(buttonFontConfg);
        movAbajo1.setForeground(Color.WHITE);
        movAbajo1.setBackground(Color.BLACK);
        movAbajo1.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));

        movArriba2.setFont(buttonFontConfg);
        movArriba2.setForeground(Color.WHITE);
        movArriba2.setBackground(Color.BLACK);
        movArriba2.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));

        movAbajo2.setFont(buttonFontConfg);
        movAbajo2.setForeground(Color.WHITE);
        movAbajo2.setBackground(Color.BLACK);
        movAbajo2.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));

        reset.setFont(buttonFontConfg);
        reset.setForeground(Color.WHITE);
        reset.setBackground(Color.BLACK);
        reset.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));

        atras.setFont(buttonFontConfg);
        atras.setForeground(Color.WHITE);
        atras.setBackground(Color.BLACK);
        atras.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));
        // Cambiar color de todos los JLabel en el panel de configuración
        Component[] components = config.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                component.setForeground(Color.WHITE);
                component.setFont(new Font("Courier New", Font.BOLD, 20)); // Opcional, para que coincida el estilo
            }
        }
        // Unificar y agregar paneles al frame

        panelCompleto.add(config, BorderLayout.CENTER);
        panelCompleto.add(panelConfigBot, BorderLayout.SOUTH);
        // frame.add(panelCompleto);
    }

    public JPanel getPanelConfig() {
        return panelCompleto;
    }

    /*
     * public void mostrarMenuConfig()
     * {
     * frame.getContentPane().removeAll(); // Limpia el contenido actual
     * frame.add(config, BorderLayout.CENTER);
     * frame.add(panelConfigBot, BorderLayout.SOUTH);
     * frame.getContentPane().revalidate();
     * frame.getContentPane().repaint();
     * config.setVisible(true);
     * panelConfigBot.setVisible(true);
     * }
     * 
     * public void ocultarMenuConfig() {
     * config.setVisible(false);
     * panelConfigBot.setVisible(false);
     * menuPong.mostrarMenuPrincipal();
     * }
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == atras) {
            menuPong.mostrarMenuPrincipal();
        }
    }
}