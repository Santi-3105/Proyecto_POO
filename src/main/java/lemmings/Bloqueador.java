package lemmings;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bloqueador extends Bichito {
    private BufferedImage[] frames;
    private int frameActual = 0;
    private double tiempoAnimacion = 0;

    public Bloqueador(Bichito b) {
            try {
            setPosicion(b.getX(), b.getY());
            this.setNivel(b.getNivel()); // Copiar el nivel
                        if (Lemming.skin.equals("Original")) {
                BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/LemmingsSprite.png"));
                cargarFrames(spriteSheet);
            }
            if(Lemming.skin.equals("LemmingRed"))
            {
                BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/SpriteSkins2.png"));
                cargarFrames(spriteSheet);
            }
            if(Lemming.skin.equals("LemmingViolet"))
            {
                BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/SpriteSkins1.png"));
                cargarFrames(spriteSheet);
            }
            setDireccion(b.estaMirandoDerecha());
            this.setImagen(frames[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void cargarFrames(BufferedImage spriteSheet) {
        //Matriz de coordenadas para los sprites
        int[][] coordenadas = {
            {20, 148, 10, 10}, 
            {36, 148, 10, 10}, 
            {52, 148, 10, 10}, 
            {68, 148, 10, 10}, 
            {84, 148, 10, 10}, 
            {100, 148, 10, 10}, 
            {116, 148, 10, 10}, 
            {132, 148, 10, 10}, 
            {148, 148, 10, 10}, 
            {164, 148, 10, 10}, 
            {180, 148, 10, 10}, 
            {196, 148, 10, 10}, 
            {212, 148, 10, 10}, 
            {228, 148, 10, 10}, 
            {244, 148, 10, 10}, 
            {260, 148, 10, 10}, 
        };

        int escala=2;
        int cantidadFrames= coordenadas.length;

        frames = new BufferedImage[cantidadFrames];

        for (int i = 0; i < cantidadFrames; i++) {
            int x = coordenadas[i][0];
            int y = coordenadas[i][1];
            int ancho = coordenadas[i][2];
            int alto = coordenadas[i][3];

            frames[i] = escalarImagen(
                    spriteSheet.getSubimage(x, y, ancho, alto),
                    ancho * escala,
                    alto * escala
            );
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
        tiempoAnimacion += delta * multiplicadorVelocidad;
        if (tiempoAnimacion > 0.1 / multiplicadorVelocidad) {
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
