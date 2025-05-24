package lemmings;

import clasesCompartidas.ObjetoGrafico;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bloqueador extends ObjetoGrafico {
    private BufferedImage[] frames;
    private int frameActual = 0;
    private double tiempoAnimacion = 0;

    public Bloqueador() {
        super("");
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/LemmingsSprite.png"));
            cargarFramesBloqueador(spriteSheet);
            this.setImagen(frames[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarFramesBloqueador(BufferedImage spriteSheet) {
        int ancho = 16;
        int alto = 12; // altura estimada del Lemming bloqueador
        int cantidadFrames = 16;
        int escala = 2;
        int fila = 12; // fila 13 en base 0

        frames = new BufferedImage[cantidadFrames];

        for (int i = 0; i < cantidadFrames; i++) {
            BufferedImage frame = spriteSheet.getSubimage(i * ancho, fila * alto + 4, ancho, alto);
            frames[i] = escalarImagen(frame, ancho * escala, alto * escala);
        }
    }

    private BufferedImage escalarImagen(BufferedImage original, int anchoNuevo, int altoNuevo) {
        BufferedImage escalada = new BufferedImage(anchoNuevo, altoNuevo, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = escalada.createGraphics();
        g2d.drawImage(original, 0, 0, anchoNuevo, altoNuevo, null);
        g2d.dispose();
        return escalada;
    }

    public void update(double delta) {
        tiempoAnimacion += delta;
        if (tiempoAnimacion > 0.1) {
            frameActual = (frameActual + 1) % frames.length;
            this.setImagen(frames[frameActual]);
            tiempoAnimacion = 0;
        }
    }

    public void setPosicion(int x, int y) {
        setX(x);
        setY(y);
    }

    @Override
    public void mostrar(Graphics2D g) {
        if (getImagen() != null) {
            g.drawImage(getImagen(), (int) getX(), (int) getY(), null);
        }
    }

    // Para detección de colisiones si hace falta
    public int getAncho() {
        return getImagen().getWidth();
    }

    public int getAlto() {
        return getImagen().getHeight();
    }
}
