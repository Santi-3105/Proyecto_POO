package pong;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.sound.sampled.Clip;
import javax.swing.*;

public class MenuConfig implements ActionListener{
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

    public MenuConfig(JFrame frame, MenuPong menuPong) {

        this.frame = frame;
        this.menuPong = menuPong;
        //Config botones
        ventana = new JRadioButton("Ventana", true);
        pantallaCompleta = new JRadioButton("Pantalla completa", false);
        ButtonGroup grupoVentana = new ButtonGroup();
        grupoVentana.add(ventana);
        grupoVentana.add(pantallaCompleta);
        musicaBox = new JCheckBox("", true);
        musicaBox.addActionListener(this);
        movArriba1 = new JTextField("↑",5);
        movAbajo1 = new JTextField("↓",5);
        movArriba2 = new JTextField("W",5);
        movAbajo2 = new JTextField("S",5);
        pistaMusical = new JComboBox<>(new String[]{"Original","V2","V3","V4"});
        pelota = new JComboBox<>(new String[]{"Original","V2","V3","V4"});
        paleta = new JComboBox<>(new String[]{"Original","V2","V3","V4"});
        cancha = new JComboBox<>(new String[]{"Original","V2","V3","V4"});
        reset = new JButton("Restablecer");
        reset.addActionListener(this);
        reset.setVisible(true);
        atras = new JButton("←");
        atras.addActionListener(this);
        atras.setVisible(true);

        frame.setVisible(true);

        // Creo el menu configuracion
        // Añado sus botones
        //Config panel
        config = new JPanel(new GridBagLayout());
        config.setVisible(true);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx=0;
        g.gridy=0;
        g.anchor= GridBagConstraints.WEST;
        g.insets= new Insets(5,5,5,5);
        config.add(new JLabel("Pantalla: "), g);
        g.gridx++;
        config.add(ventana, g);
        g.gridx++;
        config.add(pantallaCompleta, g);
        g.gridx=0;
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Musica: "),g);
        g.gridx++;
        config.add(musicaBox,g);
        g.gridx=0;
        g.gridy++;
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Teclas por defecto: "),g);
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Habilidades jugador 1: "),g);
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Movimiento arriba: "),g);
        g.gridx++;
        config.add(movArriba1,g);
        g.gridx=0;
        g.gridy++;
        config.add(new JLabel("Movimiento abajo: "),g);
        g.gridx++;
        config.add(movAbajo1,g);
        g.gridx=0;
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Habilidades jugador 2: "),g);
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Movimiento arriba: "),g);
        g.gridx++;
        config.add(movArriba2,g);
        g.gridx=0;
        g.gridy++;
        config.add(new JLabel("Movimiento abajo: "),g);
        g.gridx++;
        config.add(movAbajo2,g);
        g.gridx=0;
        g.gridy++;
        config.add(new JLabel("Elegir pista musical: "),g);
        g.gridx++;
        config.add(pistaMusical,g);
        g.gridx=0;
        g.gridy++;
        config.add(new JLabel("Elegir pelota: "),g);
        g.gridx++;
        config.add(pelota,g);
        g.gridx=0;
        g.gridy++;
        config.add(new JLabel("Elegir cancha: "),g);
        g.gridx++;
        config.add(cancha,g);
        g.gridx=0;
        g.gridy++;
        config.add(new JLabel("Elegir paleta: "),g);
        g.gridx++;
        config.add(paleta,g);

        //Config panel botones
        panelConfigBot = new JPanel();
        panelConfigBot.add(atras);
        panelConfigBot.add(reset);

        //Unificar y agregar paneles al frame

        frame.add(config, BorderLayout.CENTER);
        frame.add(panelConfigBot, BorderLayout.SOUTH);

        


    }
    public void mostrarMenuConfig() 
    {
        config.setVisible(true);
        panelConfigBot.setVisible(true);
    }

    public void ocultarMenuConfig() {
        config.setVisible(false);
        panelConfigBot.setVisible(false);
        menuPong.mostrarMenuPrincipal();
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource()==atras)
        {
            ocultarMenuConfig();
        }
    }
}