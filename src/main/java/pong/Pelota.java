package pong;

import clasesCompartidas.ObjetoGrafico;

import java.awt.*;

import javax.swing.ImageIcon;

class Pelota extends ObjetoGrafico {
    private int tamanio;
    private double velocidadX;
    private double velocidadY;
    private String estilo = "Original";
    private Image imagenPelota;

    public Pelota(int tamanio, double xInicial, double yInicial, double velocidadX, double velocidadY) {
        super(); // No hay imagen, solo es un cuadrado blanco
        this.tamanio = tamanio;
        this.posicionX = xInicial;
        this.posicionY = yInicial;
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
    }

    @Override
    public void update(double delta) {
        posicionX += velocidadX * delta;
        posicionY += velocidadY * delta;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
        String ruta = "/pong/";

        switch (estilo) {
            case "Futbol":
                imagenPelota = new ImageIcon(getClass().getResource(ruta + "SoccerBall.png")).getImage();
                break;
            case "Basquet":
                imagenPelota = new ImageIcon(getClass().getResource(ruta + "Basketball.png")).getImage();
                break;
            case "Original":
            default:
                imagenPelota = null;
                break;
        }
    }

    @Override
    public void mostrar(Graphics2D g2) {
        if (imagenPelota != null) {
            g2.drawImage(imagenPelota, (int) posicionX, (int) posicionY, tamanio, tamanio, null);
        } else {
            g2.setColor(Color.WHITE); // fallback
            g2.fillRect((int) posicionX, (int) posicionY, tamanio, tamanio);
        }
    }

    @Override
    public int getAncho() {
        return tamanio;
    }

    @Override
    public int getAlto() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    public void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }

    public double getVelocidadY() {
        return this.velocidadY;
    }

    public void setVelocidadX(double velocidadX) {
        this.velocidadX = velocidadX;
    }

    public double getVelocidadX() {
        return this.velocidadX;
    }

    boolean colisiona(ObjetoGrafico b) {
        return getX() < b.getX() + b.getAncho() &&
                getX() + getAncho() > b.getX() &&
                getY() < b.getY() + b.getAlto() &&
                getY() + getAlto() > b.getY();
    }

    void invertirDireccionY() {
        // Multiplicamos por -1 para invertir dirección vertical
        setVelocidadY(-getVelocidadY());
    }

    public void reiniciarPelota() {
        this.tamanio = 10;
        // se pone la pelota en el medio
        posicionX = 400;
        posicionY = 300;
        // seteo la velocidad en 250
        this.velocidadX = 250;
        this.velocidadY = 250;
    }
}
