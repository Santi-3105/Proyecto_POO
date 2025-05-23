package lemmings;

import clasesCompartidas.ObjetoGrafico;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Paracaidista extends ObjetoGrafico {
    private BufferedImage[] paracaidasDerechaFrames;
    private BufferedImage[] paracaidasIzquierdaFrames;
    private int frameActual = 0;
    private double tiempoAnimacion = 0;
    private int x, y;
    private boolean mirandoDerecha = true;

    public Paracaidista() {
        super("");
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/LemmingsSprite.png"));
            cargarFramesParacaidas(spriteSheet);
            this.setImagen(paracaidasDerechaFrames[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarFramesParacaidas(BufferedImage spriteSheet) {
        int ancho = 16;
        int alto = 13; //el paracaidista tiene 13 pixeles de alto
        int cantidadFrames = 8; 
        int escala = 2;
        int filaDerecha = 8; // la fila 9 en índice base 0
        int filaIzquierda = 9; // fila 10 (base 0) asumiendo que está justo abajo

        paracaidasDerechaFrames = new BufferedImage[cantidadFrames];
        paracaidasIzquierdaFrames = new BufferedImage[cantidadFrames];

        for (int i = 0; i < cantidadFrames; i++) {
            BufferedImage frameDerecha = spriteSheet.getSubimage(i * ancho, filaDerecha * alto - 7, ancho, alto);
            paracaidasDerechaFrames[i] = escalarImagen(frameDerecha, ancho * escala, alto * escala);

            BufferedImage frameIzquierda = spriteSheet.getSubimage(i * ancho, filaIzquierda * alto - 4, ancho, alto);
            paracaidasIzquierdaFrames[i] = escalarImagen(frameIzquierda, ancho * escala, alto * escala);
        }
    }

    private BufferedImage escalarImagen(BufferedImage original, int anchoNuevo, int altoNuevo) {
        BufferedImage escalada = new BufferedImage(anchoNuevo, altoNuevo, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = escalada.createGraphics();
        g2d.drawImage(original, 0, 0, anchoNuevo, altoNuevo, null);
        g2d.dispose();
        return escalada;
    }

    public void setDireccion(boolean derecha) {
        this.mirandoDerecha = derecha;
        // Reset frame para iniciar la animación desde el principio si querés
        frameActual = 0;
        if (mirandoDerecha) {
            this.setImagen(paracaidasDerechaFrames[0]);
        } else {
            this.setImagen(paracaidasIzquierdaFrames[0]);
        }
    }
    public void moverY(int dy) {
        this.y += dy;
    }

    public void update(double delta) {
        tiempoAnimacion += delta;
        if (tiempoAnimacion > 0.1) {
            if (frameActual < paracaidasDerechaFrames.length - 1) {
                frameActual++;
            }
            if (mirandoDerecha) {
                this.setImagen(paracaidasDerechaFrames[frameActual]);
            } else {
                this.setImagen(paracaidasIzquierdaFrames[frameActual]);
            }
            tiempoAnimacion = 0;
        }
        y += 1; // caída lenta
    }

    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void mostrar(Graphics2D g) {
        if (getImagen() != null) {
            g.drawImage(getImagen(), x, y, null);
        }
    }
}