package clasesCompartidas;

import pong.MenuConfig;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.*;

import com.entropyinteractive.*;
import pong.Pong;

public class LanzadorJuego extends JFrame implements ActionListener {
    protected JGame juego;
    protected Thread t;
    protected Panel tarjetaPong;
    protected Panel tarjetaLemmings;
    protected Font descripcionFont;
    protected Font botonFont;
    protected Label titulo;
    protected Panel tarjeta;
    protected Button boton;
    protected DefaultListModel<String> listaJuegos;
    protected JList<String> juegos;
    protected JButton botonConfig;
    protected JButton botonIniciar;
    protected JPanel panelImg;
    protected JScrollPane scrollPane;

    public LanzadorJuego() {
        setTitle("Sistema de videojuego");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Lista de juegos
        listaJuegos = new DefaultListModel<>();
        listaJuegos.addElement("Pong");
        listaJuegos.addElement("Lemmings");
        listaJuegos.addElement("Counter-Strike");
        // Bottones
        botonConfig = new JButton("Configuración");
        botonConfig.addActionListener(this);
        botonIniciar = new JButton("Iniciar juego");
        botonIniciar.addActionListener(this);

        // Añado los juegos a la barra de juegos
        juegos = new JList<>(listaJuegos);
        scrollPane = new JScrollPane(juegos);
        add(scrollPane, BorderLayout.WEST);

        //Cargo las imagenes segun aprete los juegos de la lista 
        juegos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selected = juegos.getSelectedValue();
                    panelImg.removeAll();
                    panelImg.setLayout(new BorderLayout());

                    if ("Lemmings".equals(selected)) {
                        panelImg.add(new JPanelImage("/lemmings/portada_lemmings.png"));
                    } else if ("Pong".equals(selected)) {
                        panelImg.add(new JPanelImage("/pong/portada_pong.png"));
                    } else if ("Counter-Strike".equals(selected)) {
                        panelImg.add(new JPanelImage("Iconos/cs.jpg"));
                    }

                    panelImg.revalidate();
                    panelImg.repaint();
                }
            }
        });

        // Creo el panel de la imagen
        panelImg = new JPanel();

        // Meto el panel al frame
        add(panelImg, BorderLayout.CENTER);

        // Creo un panel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(botonIniciar);
        panelBotones.add(botonConfig);

        // Unifico y agrego panel al frame
        add(panelBotones, BorderLayout.SOUTH);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Musica.detenerMusicaFondo();

        if (e.getSource() == botonIniciar) {
            // Agarra el valor del juego seleccionado en la barra
            String juegoSeleccionado = juegos.getSelectedValue();
            if (juegoSeleccionado == null) {
                JOptionPane.showMessageDialog(LanzadorJuego.this, "Error, seleccione un juego para jugar");
            }else if (juegos.getSelectedValue() == "Pong") {
                juego = new Pong("Pong", 800, 600);
                t = new Thread() {
                    public void run() {
                        juego.run(1.0 / 60.0);
                    }
                };
                t.start();
            } else if (juegos.getSelectedValue() == "Lemmings") {
                /*
                 * juego = new Lemming("Lemming",800,600);
                 * t = new Thread() {
                 * public void run() {
                 * juego.run(1.0 / 60.0);
                 * }
                 * };
                 * t.start();
                 */
            }
        }
        if (e.getSource() == botonConfig) {
            // Agarra el valor del juego seleccionado en la barra
            String juegoSeleccionado = juegos.getSelectedValue();
            if (juegoSeleccionado == null) {
                JOptionPane.showMessageDialog(LanzadorJuego.this, "Error, seleccione un juego para configurar");
            } else if (juegoSeleccionado == "Lemmings") {
                // new MenuConfig();
            } else if (juegoSeleccionado == "Pong") {
                new MenuConfig();
            } else if (juegoSeleccionado == "Counter-Strike") {
                // HACER NUEVA CONFIGURACION
                // ConfigVideojuego config = new ConfigVideojuego();
                // config.getClass();
            }
        }
    }

    public static void main(String[] args) {
        new LanzadorJuego();
    }

}