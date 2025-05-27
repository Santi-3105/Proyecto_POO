package lemmings;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import clasesCompartidas.ObjetoGrafico;

public class Cavador extends Bichito{
    private BufferedImage[] frames;
    private int frameActual = 0;
    private double tiempoAnimacion = 0;
    private boolean estaActiva = false;
    private static final int ESCALA = 4;

    public Cavador() {
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/LemmingsSprite.png"));
            cargarFramesCavador(spriteSheet);
            this.setImagen(frames[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarFramesCavador(BufferedImage spriteSheet) {
        int ancho = 16;
        int alto = 12;
        int cantidadFrames = 16;
        int fila = 21; // Fila específica para cavar en el spritesheet

        frames = new BufferedImage[cantidadFrames];
        for (int i = 0; i < cantidadFrames; i++) {
            // Extraer frame y escalarlo
            BufferedImage frame = spriteSheet.getSubimage((i+1) * ancho, fila * alto, ancho, alto-2);
            frames[i] = escalarImagen(frame);
        }
    }

    private BufferedImage escalarImagen(BufferedImage original) {
         BufferedImage escalada = new BufferedImage(
            original.getWidth() * ESCALA,
            original.getHeight() * ESCALA,
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = escalada.createGraphics();
        g2d.drawImage(original, 0, 0, escalada.getWidth(), escalada.getHeight(), null);
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


    // Resto de métodos existentes...
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
}