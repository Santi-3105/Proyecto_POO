package pong;
import java.awt.*;
import java.awt.event.KeyEvent;

import clasesCompartidas.ObjetoGrafico;
import com.entropyinteractive.Keyboard;

public class Paleta extends ObjetoGrafico{
    private int ancho;
    private int alto;
    private double velocidadY;
    private Keyboard keyboard;


    public Paleta(int ancho, int alto, double xInicial, double yInicial,Keyboard keyboard) {
        super(); // No usamos imagen
        this.ancho = ancho;
        this.alto = alto;
        this.posicionX = xInicial;
        this.posicionY = yInicial;
        this.velocidadY = 0; // Inicia quieta
        this.keyboard=keyboard;
    }

    // Para que puedas cambiar la velocidad (por ejemplo, con teclas)
    public void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }

    @Override
    public void update(double delta) {
        // Controles para jugador izquierdo (W/S)

        // Limitar movimiento de la paleta dentro de la ventana
        if (posicionY < 37) {
            posicionY = 37;
        }
        if (posicionY + alto > 600) {
            posicionY = 600 - alto;
        }

        if (posicionX < 400) {
            if (keyboard.isKeyPressed(KeyEvent.VK_W)) {
                setVelocidadY(-200);
            } else if (keyboard.isKeyPressed(KeyEvent.VK_S)) {
                setVelocidadY(200);
            } else {
                setVelocidadY(0);
            }

        }

        // Controles para jugador derecho (Arriba/Abajo)
        if (posicionX > 400) {
            if (keyboard.isKeyPressed(KeyEvent.VK_UP)) {
                setVelocidadY(-200);
            } else if (keyboard.isKeyPressed(KeyEvent.VK_DOWN)) {
                setVelocidadY(200);
            } else {
                setVelocidadY(0);
            }
        }

        posicionY += velocidadY * delta;
    }

    @Override
    public void mostrar(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect((int) posicionX, (int) posicionY, ancho, alto);
    }

    @Override
    public int getAncho() {
        return ancho;
    }

    @Override
    public int getAlto() {
        return alto;
    }

}
