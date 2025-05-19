package pong;
import java.awt.*;
import java.awt.event.KeyEvent;

import clasesCompartidas.ObjetoGrafico;
import com.entropyinteractive.Keyboard;

public class Paleta extends ObjetoGrafico{
    private int ancho;
    private int alto;
    private double velocidad=200;
    private Keyboard keyboard;


    public Paleta(int ancho, int alto, double xInicial, double yInicial,Keyboard keyboard) {
        super(); // No usamos imagen
        this.ancho = ancho;
        this.alto = alto;
        this.posicionX = xInicial;
        this.posicionY = yInicial;
        this.velocidad =0; //inicia quieta
        this.keyboard=keyboard;
    }


    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
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
                setVelocidad(-300);
            } else if (keyboard.isKeyPressed(KeyEvent.VK_S)) {
                setVelocidad(300);
            } else {
                setVelocidad(0);
            }

        }

        // Controles para jugador derecho (Arriba/Abajo)
        if (posicionX > 400) {
            if (keyboard.isKeyPressed(KeyEvent.VK_UP)) {
                setVelocidad(-300);
            } else if (keyboard.isKeyPressed(KeyEvent.VK_DOWN)) {
                setVelocidad(300);
            } else {
                setVelocidad(0);
            }
        }

        posicionY += velocidad * delta;
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
