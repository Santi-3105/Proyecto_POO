package lemmings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.security.Key;

import com.entropyinteractive.Keyboard;

import javax.imageio.ImageIO;

public class EstadoNombreJugador {
    private final Lemming juego;
    private String nombreJugador = "";
    private final Image img_cavador = ImageIO.read(getClass().getResource("/lemmings/cavador.png"));
    private final Image img_paracaida = ImageIO.read(getClass().getResource("/lemmings/paracaida.png"));
    private final Image img_bloqueador = ImageIO.read(getClass().getResource("/lemmings/bloqueador.png"));


    public EstadoNombreJugador(Lemming juego) throws IOException {
        this.juego = juego;
    }

    public void actualizar() {
        Keyboard teclado = juego.getKeyboard();

        // Manejar entrada de texto
        for (KeyEvent event : teclado.getEvents()){
            if (event.getID() == KeyEvent.KEY_TYPED) {
                char c = event.getKeyChar();
                if (Character.isLetterOrDigit(c)) {
                    if (nombreJugador.length() < 15) {
                        nombreJugador += c;
                    }
                }
                //para borrar
                if (teclado.isKeyPressed(KeyEvent.VK_BACK_SPACE) && nombreJugador.length() > 0) {
                    nombreJugador = nombreJugador.substring(0, nombreJugador.length() - 1);
                }
            }
        }

        // Confirmar nombre
        if (teclado.isKeyPressed(KeyEvent.VK_ENTER) && !nombreJugador.isEmpty()) {
            juego.setJugadorActual(new Jugador(nombreJugador));
            juego.cambiarEstado(Lemming.EstadoJuego.MENU);
        }
    }

    public void dibujar(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, juego.getWidth(), juego.getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Ingrese su nombre:", 100, 100);

        g.drawString(nombreJugador + (System.currentTimeMillis() % 1000 < 500 ? "_" : ""), 100, 150);

        g.drawImage(img_cavador, 590, 380,null);
        g.drawImage(img_paracaida, 580, 40,null);
        g.drawImage(img_bloqueador, 50, 380,null);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Presione ENTER para continuar", 300, 560);
    }
}