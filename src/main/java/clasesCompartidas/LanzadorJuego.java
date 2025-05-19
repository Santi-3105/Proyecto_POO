package clasesCompartidas;
import pong.MenuPong;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;


import com.entropyinteractive.*;
import lemmings.Lemming;
import pong.Pong;


public class LanzadorJuego extends Frame implements ActionListener{
    protected JGame juego;
    protected  Thread t;
    protected Panel tarjetaPong;
    protected Panel tarjetaLemmings;
    protected Font descripcionFont;
    protected Font botonFont;
    protected Label titulo;
    protected Panel tarjeta;
    protected Button boton;

    public LanzadorJuego(){
        setTitle("Lanzador de Juegos");
        setSize(1100, 700);
        setLayout(null);
        setBackground(new Color(30, 30, 30)); // Fondo oscuro

        Musica.iniciarMusica("musica_lanzador.wav");

        titulo = new Label("Bienvenidos al Lanzador de Juegos", Label.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 40, getWidth(), 40);
        add(titulo);


        // Fuente
        descripcionFont = new Font("SansSerif", Font.PLAIN, 16);
        botonFont = new Font("SansSerif", Font.BOLD, 14);

        // Cargar imágenes
        Image pongImage = Toolkit.getDefaultToolkit().getImage(
                getClass().getClassLoader().getResource("pong/portada_pong.png")
        );
        Image lemmingsImage = Toolkit.getDefaultToolkit().getImage(
                getClass().getClassLoader().getResource("lemmings/portada_lemmings.png")
        );

        // Tarjeta Pong
        tarjetaPong = crearTarjeta(pongImage, "Juego clásico de tenis de mesa para dos jugadores",
                "Jugar Pong", 100, descripcionFont, botonFont);
        add(tarjetaPong);

        // Tarjeta Lemmings
        tarjetaLemmings = crearTarjeta(lemmingsImage, "Guía a los lemmings a la salida usando habilidades",
                "Jugar Lemmings",650, descripcionFont, botonFont);
        add(tarjetaLemmings);

        // Cierre de ventana
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setResizable(false);
        setVisible(true);
    }

    private Panel crearTarjeta(Image imagen, String descripcion, String textoBoton, int x, Font descFont, Font btnFont) {
        tarjeta = new Panel(null);
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBounds(x, 100, 400, 500);

        // Canvas para mostrar la imagen dentro de la tarjeta
        Canvas canvas = new Canvas() {
            public void paint(Graphics g) {
                g.drawImage(imagen, 25, 20, 350, 200, this);
            }
        };
        canvas.setBounds(0, 0, 400, 220);
        tarjeta.add(canvas);

        // Descripción
        Label desc = new Label(descripcion, Label.CENTER);
        desc.setFont(descFont);
        desc.setBounds(20, 240, 360, 60);
        tarjeta.add(desc);

        // Botón (verde, redondeado, texto blanco)
        boton = new Button(textoBoton) {
            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(46, 139, 87)); // Verde
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(Color.WHITE);
                g2.setFont(btnFont);
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getLabel());
                int textAscent = fm.getAscent();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() + textAscent) / 2 - 4;

                g2.drawString(getLabel(), x, y);
            }
        };
        boton.setBounds(130, 430, 140, 40);
        boton.setBackground(new Color(0, 0, 0, 0)); // Transparente para evitar fondo del botón por defecto
        boton.setForeground(Color.WHITE);
        boton.setFont(btnFont);
        tarjeta.add(boton);
        boton.addActionListener(this);

        return tarjeta;
    }


    public void actionPerformed(ActionEvent e){
        Musica.detenerMusicaFondo();

        if (e.getActionCommand().equals("Jugar Pong")){
            juego = new Pong("Pong",800,600);
            // Nuevo Crear el menú y pasar la instancia del juego
            MenuPong menu = new MenuPong((Pong) juego);

        }

        if (e.getActionCommand().equals("Jugar Lemmings")){
            //juego = new Lemming();

            t = new Thread() {
                public void run() {
                    juego.run(1.0 / 60.0);
                }
            };

            t.start();
        }

    }

    public static void main(String[] args){
        new LanzadorJuego();
    }


}