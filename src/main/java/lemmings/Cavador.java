package lemmings;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;





public class Cavador extends Bichito implements Habilidad{
    private BufferedImage[] frames;
    private int frameActual = 0;
    private double tiempoAnimacion = 0;
    private double tiempoCavando = 0;
    private boolean habilidadActiva = false;
    private final double VELOCIDAD_CAVIDO = 0.5; // Bloques por segundo
    private boolean ultimaDireccionAntesDeHabilidad = true;

    public Cavador(Bichito b) {
        try {
            setPosicion(b.getX(), b.getY());
            this.setNivel(b.getNivel());
                        if (Lemming.skin.equals("Original")) {
                BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/LemmingsSprite.png"));
                cargarFramesCavador(spriteSheet);
            }
            if(Lemming.skin.equals("LemmingRed"))
            {
                BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/SpriteSkins2.png"));
                cargarFramesCavador(spriteSheet);
            }
            if(Lemming.skin.equals("LemmingViolet"))
            {
                BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/SpriteSkins1.png"));
                cargarFramesCavador(spriteSheet);
            }
            setDireccion(b.estaMirandoDerecha());
            this.setImagen(frames[0]);
            ultimaDireccionAntesDeHabilidad = b.estaMirandoDerecha();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void cargarFramesCavador(BufferedImage spriteSheet) {
        //Matriz de coordenadas para los sprites
        int[][] coordenadas = {
                {20, 247, 11, 12},
                {37, 247, 11, 13},
                {53, 247, 11, 14},
                {69, 247, 11, 13},
                {85, 248, 19, 11},
                {100, 249, 10, 10},
                {131, 248, 11, 11},
                {146, 247, 11, 12},
                {161, 247, 11, 13},
                {176, 247, 12, 14},
                {192, 247, 12, 13},
                {208, 247, 12, 11},
                {224, 249, 13, 10},
                {243, 250, 11, 9},
                {259, 248, 11, 11},
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

        if (habilidadActiva) {
            tiempoCavando += delta * multiplicadorVelocidad;

            // Mover hacia abajo mientras cava
            setY(getY() + VELOCIDAD_CAVIDO * delta * multiplicadorVelocidad * getNivel().getAltoEstructura());

            // Eliminar bloques cavados periódicamente
            if (tiempoCavando >= 0.2 / multiplicadorVelocidad) {
                eliminarBloqueDebajo();
                tiempoCavando = 0;
            }

            // Verificar si debe dejar de cavar
            if (!seguirCavando()) {
                detenerHabilidad();
            }
            // Animación
            if (tiempoAnimacion > 0.1 / multiplicadorVelocidad) {
                frameActual = (frameActual + 1) % frames.length;
                this.setImagen(frames[frameActual]);
                tiempoAnimacion = 0;
            }
        } else {

            super.update(delta);

            // Verificar si debe empezar a cavar
            if (empezarACavar()) {
                iniciarHabilidad(this);
            }
        }
    }

    private boolean empezarACavar() {
        if (habilidadActiva) return false;

        // Verificar si hay un bloque destructible debajo
        int fila = (int)(getY() + getAlto()) / getNivel().getAltoEstructura();
        int col = (int)(getX() + getAncho()/2) / getNivel().getAnchoEstructura();

        if (fila >= getNivel().getFilas()) return false;

        Estructura estructura = getNivel().getEstructura(fila, col);
        return estructura != null && estructura.esDestructible();
    }

    private boolean seguirCavando() {
        // Verificar si hay más bloques para cavar
        int fila = (int)(getY() + getAlto() + 5) / getNivel().getAltoEstructura();
        int col = (int)(getX() + getAncho()/2) / getNivel().getAnchoEstructura();

        if (fila >= getNivel().getFilas()) return false;

        Estructura estructura = getNivel().getEstructura(fila, col);
        return estructura != null && estructura.esDestructible();
    }

    private void eliminarBloqueDebajo() {
        int fila = (int)(getY() + getAlto()) / getNivel().getAltoEstructura();
        int col = (int)(getX() + getAncho()/2) / getNivel().getAnchoEstructura();

        if (fila < getNivel().getFilas() && col < getNivel().getColumnas()) {
            getNivel().getMapaEstructuras()[fila][col] = null; // Elimina el bloque
        }
    }
    @Override
    public void iniciarHabilidad(Bichito b) {
        habilidadActiva = true;
        ultimaDireccionAntesDeHabilidad = b.estaMirandoDerecha();
    }

    @Override
    public void detenerHabilidad() {
        habilidadActiva = false;
        frameActual = 0;
        setDireccion(ultimaDireccionAntesDeHabilidad);
    }
}