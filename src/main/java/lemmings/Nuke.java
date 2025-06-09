package lemmings;

import clasesCompartidas.Sonido;
import com.entropyinteractive.Keyboard;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Nuke extends Bichito {

    private BufferedImage[] countdownFrames;
    private BufferedImage[] explosionFrames;
    private int frameActual = 0;
    private double tiempoAnimacion = 0;
    private boolean explotado = false;
    public static boolean nukeActivado = false;

    public Nuke(Bichito b) {
        try {
            setPosicion(b.getX(), b.getY());
            this.setNivel(b.getNivel()); // Copiar nivel del lemming original
            if (Lemming.skin.equals("Original")) {
                BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/LemmingsSprite.png"));
                cargarFrames(spriteSheet);
            }
            if (Lemming.skin.equals("LemmingRed")) {
                BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/SpriteSkins2.png"));
                cargarFrames(spriteSheet);
            }
            if (Lemming.skin.equals("LemmingViolet")) {
                BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/SpriteSkins1.png"));
                cargarFrames(spriteSheet);
            }
            setDireccion(b.estaMirandoDerecha());
            this.setImagen(countdownFrames[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarFrames(BufferedImage spriteSheet) {
        int escala = 2;

        // Coordenadas personalizadas: columna 29 (cuenta regresiva)
        int[][] countdownCoords = {
                { 0, 360, 10, 10 }, // Número 5
                { 16, 360, 10, 10 }, // Número 4
                { 32, 360, 10, 10 }, // Número 3
                { 48, 360, 10, 10 }, // Número 2
                { 64, 360, 10, 10 } // Número 1
        };
        int[][] explosionCoords = {
                { 17, 170, 16, 12 }, // Frame 1 (x=17, y=148)
                { 33, 170, 16, 12 }, // Frame 2 (x += 16)
                { 49, 170, 16, 12 }, // Frame 3
                { 65, 170, 16, 12 }, // Frame 4
                { 81, 170, 16, 12 }, // Frame 5
                { 97, 170, 16, 12 }, // Frame 6
                { 113, 170, 16, 12 }, // Frame 7
                { 129, 170, 16, 12 }, // Frame 8
                { 145, 170, 16, 12 }, // Frame 9
                { 161, 170, 16, 12 }, // Frame 10
                { 177, 170, 16, 12 }, // Frame 11
                { 193, 170, 16, 12 }, // Frame 12
                { 209, 170, 16, 12 } // Frame 13
        };

        countdownFrames = new BufferedImage[countdownCoords.length];
        for (

                int i = 0; i < countdownCoords.length; i++) {
            int x = countdownCoords[i][0];
            int y = countdownCoords[i][1];
            int ancho = countdownCoords[i][2];
            int alto = countdownCoords[i][3];
            countdownFrames[i] = escalarImagen(
                    spriteSheet.getSubimage(x, y, ancho, alto),
                    ancho * escala, alto * escala);
        }

        explosionFrames = new BufferedImage[explosionCoords.length];
        for (int i = 0; i < explosionCoords.length; i++) {
            int x = explosionCoords[i][0];
            int y = explosionCoords[i][1];
            int ancho = explosionCoords[i][2];
            int alto = explosionCoords[i][3];
            explosionFrames[i] = escalarImagen(
                    spriteSheet.getSubimage(x, y, ancho, alto),
                    ancho * escala, alto * escala);
        }
    }

    @Override
    public void update(double delta) {
        tiempoAnimacion += delta;

        if (!explotado) {
            // Cuenta regresiva (0.5s por frame)
            if (tiempoAnimacion >= 0.5) {
                tiempoAnimacion = 0;
                if (frameActual < countdownFrames.length - 1) {
                    frameActual++;
                    setImagen(countdownFrames[frameActual]);
                } else {
                    explotado = true;
                    frameActual = 0;
                    tiempoAnimacion = 0; // Reinicia para la explosión
                }
            }
        } else {
            // Explosión (0.1s por frame)
            if (tiempoAnimacion >= 0.1) {
                tiempoAnimacion = 0;
                if (frameActual < explosionFrames.length - 1) {
                    frameActual++;
                    setImagen(explosionFrames[frameActual]);
                } else {
                    super.morir(); // Solo marca como muerto al terminar TODOS los frames
                    if (Lemming.sonidoActivo) {
                        Sonido.reproducir("tenton.wav");
                    }
                }
            }
        }
    }

    private BufferedImage escalarImagen(BufferedImage original, int anchoNuevo, int altoNuevo) {
        BufferedImage escalada = new BufferedImage(anchoNuevo, altoNuevo, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = escalada.createGraphics();
        g2d.drawImage(original, 0, 0, anchoNuevo, altoNuevo, null);
        g2d.dispose();
        return escalada;
    }

    public static void nukear(Keyboard teclado, int Intautodestruccion, ArrayList<Bichito> lemmingsEnJuego) {
        if (teclado.isKeyPressed(Intautodestruccion) && !nukeActivado) {
            nukeActivado = true;
            for (int i = 0; i < lemmingsEnJuego.size(); i++) {
                Bichito original = lemmingsEnJuego.get(i);
                if (!original.estaMuerto()) {
                    // Crear un Nuke basado en el original
                    // Si era un bloqueador, marcamos que fue nukeado
                    if (original instanceof Bloqueador) {
                        if (Lemming.bloqueador == original) {
                            Lemming.bloqueador = null; // Eliminás el viejo bloqueador ya que va a ser reemplazado por un Nuke
                        }
                    }
                    Bichito nukeado = new Nuke(original);
                    lemmingsEnJuego.set(i, nukeado);
                }
            }
        }
    }
}
