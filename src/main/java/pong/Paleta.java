package pong;
import java.awt.*;

import clasesCompartidas.ObjetoGrafico;

public class Paleta extends ObjetoGrafico {
    private int ancho;
    private int alto;
    private double velocidadY;

    public Paleta(int ancho, int alto, double xInicial, double yInicial) {
        super(); // No usamos imagen
        this.ancho = ancho;
        this.alto = alto;
        this.posicionX = xInicial;
        this.posicionY = yInicial;
        this.velocidadY = 0; // Inicia quieta
    }

    // Para que puedas cambiar la velocidad (por ejemplo, con teclas)
    public void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }

    @Override
    public void update(double delta) {
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
