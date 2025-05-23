package lemmings;

import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


import clasesCompartidas.ObjetoGrafico;

public class Bichito extends ObjetoGrafico {
    private BufferedImage[] caminarDerechaFrames;
    private BufferedImage[] caminarIzquierdaFrames;
    private int frameActual = 0;
    private double tiempoAnimacion = 0;
    private boolean mirandoDerecha = true;
    private int x = 0;
    private int y = 0;

    public Bichito(String filename) {
        super("");
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/LemmingsSprite.png"));
            cargarFramesCaminar(spriteSheet);
            this.setImagen(caminarDerechaFrames[0]); // imagen inicial
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void cargarFramesCaminar(BufferedImage spriteSheet) {
        int ancho = 16;
        int alto = 11;
        int cantidadFrames = 8;
        int escala = 2;

        caminarDerechaFrames = new BufferedImage[cantidadFrames];
        caminarIzquierdaFrames = new BufferedImage[cantidadFrames];

        for (int i = 0; i < cantidadFrames; i++) {
            BufferedImage frameDerecha = spriteSheet.getSubimage(i * ancho, 0, ancho, alto);
            BufferedImage frameIzquierda = spriteSheet.getSubimage(i * ancho, alto, ancho, alto);

            caminarDerechaFrames[i] = escalarImagen(frameDerecha, ancho * escala, alto * escala);
            caminarIzquierdaFrames[i] = escalarImagen(frameIzquierda, ancho * escala, alto * escala);
        }
    }

    public void caminar() {
        BufferedImage[] frames = mirandoDerecha ? caminarDerechaFrames : caminarIzquierdaFrames;
        frameActual = (frameActual + 1) % frames.length;
        this.setImagen(frames[frameActual]);
    }

    public void setDireccion(boolean derecha) {
        this.mirandoDerecha = derecha;
    }

    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getAncho() {
    return (getImagen() != null) ? getImagen().getWidth() : 0;
    }

    public void moverX(int dx) {
        this.x += dx;
    }
    @Override
    public void mostrar(Graphics2D g) {
        if (this.getImagen() != null) {
            g.drawImage(this.getImagen(), x, y, null);
        }
    }

    public void aparecer() {

    }

    public void llegarPortal() {

    }

    public void morir() {

    }

    public void explotar() {

    }

    private BufferedImage escalarImagen(BufferedImage imagenOriginal, int nuevaAncho, int nuevaAlto) {
        BufferedImage imagenEscalada = new BufferedImage(nuevaAncho, nuevaAlto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagenEscalada.createGraphics();
        g2d.drawImage(imagenOriginal, 0, 0, nuevaAncho, nuevaAlto, null);
        g2d.dispose();
    return imagenEscalada;
    }
    @Override
    public void update(double delta) {
        tiempoAnimacion += delta;
        if (tiempoAnimacion > 0.1) {
            caminar();
            tiempoAnimacion -= 0.1;
        }
    }
}
