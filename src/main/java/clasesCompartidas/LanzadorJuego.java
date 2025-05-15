package clasesCompartidas;
import java.awt.*;
import java.awt.event.*;
import com.entropyinteractive.*;
import pong.Pong;

public class LanzadorJuego extends Frame implements ActionListener{
    private JGame juego;
    protected Thread t;
    protected final Panel tarjetaPong;
    protected final Panel tarjetaLemmings;
    protected final Label titulo;
    protected final Font descripcionFont;
    protected final Font botonFont;
    protected Panel tarjeta;
    protected Canvas canvas;
    protected Button boton;

    public LanzadorJuego(){
        setTitle("Lanzador de Juegos");
        setSize(1100, 700);
        setLayout(null);
        setBackground(new Color(30, 30, 30)); // Fondo oscuro
        setResizable(false);

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
        tarjetaPong = crearTarjeta(pongImage,
                "Juego clásico de tenis de mesa para dos jugadores,\ndemuestra tus habilidades al oponente!",
                "Jugar Pong", 100, descripcionFont, botonFont);
        add(tarjetaPong);

        // Tarjeta Lemmings
        tarjetaLemmings = crearTarjeta(lemmingsImage,
                "Guía a los lemmings a la salida usando habilidades especiales,\ntienes la fuerza necesaria para hacerlo?",
                "Jugar Lemmings", 650, descripcionFont, botonFont);
        add(tarjetaLemmings);


        // Cierre de ventana
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private Panel crearTarjeta(Image imagen, String descripcion, String textoBoton, int x, Font descFont, Font btnFont) {
        tarjeta = new Panel(null);
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBounds(x, 100, 400, 500);

        // Canvas para la imagen
        canvas = new Canvas() {
            public void paint(Graphics g) {
                g.drawImage(imagen, 25, 20, 350, 200, this);
            }
        };
        canvas.setBounds(0, 0, 400, 220);
        tarjeta.add(canvas);

        // Descripción multilineal
        TextArea desc = new TextArea(descripcion, 2, 30, TextArea.SCROLLBARS_NONE);
        desc.setEditable(false);
        desc.setFont(descFont);
        desc.setBackground(Color.WHITE);
        desc.setForeground(Color.BLACK);
        desc.setBounds(20, 240, 360, 60);
        desc.setFocusable(false);
        desc.setEnabled(false);
        tarjeta.add(desc);

        // Botón personalizado
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
        boton.setBounds(130, 430, 140, 40); // Botón más abajo
        boton.setBackground(new Color(0, 0, 0, 0));
        boton.setForeground(Color.WHITE);
        boton.setFont(btnFont);
        tarjeta.add(boton);

        return tarjeta;
    }


    public void actionPerformed(ActionEvent e){

        if (e.getActionCommand().equals("Pong")){
            juego = new Pong("Pong",800,600);

            t = new Thread() {
                public void run() {
                    juego.run(1.0 / 60.0);
                }
            };

            t.start();
        }

       /* if (e.getActionCommand().equals("Lemmings")){
            juego = new Lemming();

            t = new Thread() {
                public void run() {
                    juego.run(1.0 / 60.0);
                }
            };

            t.start();
        }*/

    }
    public static void main(String[] args){
        new LanzadorJuego();
    }


}