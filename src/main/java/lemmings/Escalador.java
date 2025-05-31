package lemmings;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Escalador extends Bichito {

    private BufferedImage[] escaladorDerechaFrames;
    private BufferedImage[] escaladorIzquierdaFrames;
    private int frameActual = 0;
    private double tiempoAnimacion = 0;
    private boolean mirandoDerecha = false;

    public Escalador() {
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/LemmingsSprite.png"));
            cargarFramesEscalador(spriteSheet);
            this.setImagen(escaladorDerechaFrames[0]);
            setPosicion(200, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarFramesEscalador(BufferedImage spriteSheet) {
        //Matriz de coordenadas para los sprites
        int[][] coordenadasDerecha = {
            {19, 43, 6, 9}, // Frame 1
            {35, 42, 6, 10}, // Frame 2
            {51, 42, 6, 10}, // Frame 3
            {66, 41, 7, 11}, // Frame 4 
            {81, 42, 8, 10}, // Frame 5 
            {96, 43, 9, 9}, // Frame 6
            {113, 44, 8, 8}, // Frame 7
            {130, 44, 7, 8} // Frame 8
        };

        int[][] coordenadasIzquierda = {
            {24, 55, 6, 9}, // Frame 1
            {40, 54, 6, 10}, // Frame 2
            {56, 54, 6, 10}, // Frame 3
            {72, 53, 7, 11}, // Frame 4 
            {88, 54, 8, 10}, // Frame 5 
            {104, 55, 9, 9}, // Frame 6
            {120, 56, 8, 8}, // Frame 7
            {136, 56, 7, 8} // Frame 8
        };

        int escala = 2;
        int cantidadFrames = 8;

        escaladorDerechaFrames = new BufferedImage[cantidadFrames];
        escaladorIzquierdaFrames = new BufferedImage[cantidadFrames];

        for (int i = 0; i < cantidadFrames; i++) {
            int x = coordenadasDerecha[i][0];
            int y = coordenadasDerecha[i][1];
            int ancho = coordenadasDerecha[i][2];
            int alto = coordenadasDerecha[i][3];

            escaladorDerechaFrames[i] = escalarImagen(
                    spriteSheet.getSubimage(x, y, ancho, alto),
                    ancho * escala,
                    alto * escala
            );
        }

        for (int i = 0; i < cantidadFrames; i++) {
            int x = coordenadasIzquierda[i][0];
            int y = coordenadasIzquierda[i][1];
            int ancho = coordenadasIzquierda[i][2];
            int alto = coordenadasIzquierda[i][3];

            escaladorIzquierdaFrames[i] = escalarImagen(
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

    @Override
    public void setDireccion(boolean derecha) {
        this.mirandoDerecha = derecha;
        // Reset frame para iniciar la animación desde el principio si querés
        frameActual = 0;
        if (mirandoDerecha) {
            this.setImagen(escaladorDerechaFrames[0]);
        } else {
            this.setImagen(escaladorIzquierdaFrames[0]);
        }
    }

    public void moverY(int dy) {
        posicionY -= dy;
    }

    @Override
    public void update(double delta) {
        tiempoAnimacion += delta;

        // Control de velocidad de animación (ajusta 0.1 según necesites)
        if (tiempoAnimacion > 0.1) {
            // Animación cíclica usando módulo
            frameActual = (frameActual + 1) % escaladorDerechaFrames.length;

            if (mirandoDerecha) {
                this.setImagen(escaladorDerechaFrames[frameActual]);
            } else {
                this.setImagen(escaladorIzquierdaFrames[frameActual]);
            }

            tiempoAnimacion = 0;
        }

        moverY(1);
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
}
