package lemmings;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Paracaidista extends Bichito implements Habilidad{
    private BufferedImage[] paracaidasDerechaFrames;
    private BufferedImage[] paracaidasIzquierdaFrames;
    private int frameActual = 0;
    private double tiempoAnimacion = 0;
    private boolean mirandoDerecha = true;
    private boolean faseRepeticionActiva = false;
    private int[] cicloFrames = { 5, 6, 7 };
    private int indiceCiclo = 0;
    private boolean habilidadActiva = false;
    private boolean yaEstabaEnAire = false;
    private boolean ultimaDireccionAntesDeHabilidad = true;

    public Paracaidista(Bichito b) {
        try {
            setPosicion(b.getX(), b.getY());
            this.setNivel(b.getNivel()); // Copiar el nivel
            BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/LemmingsSprite.png"));
            cargarFrames(spriteSheet);
            setDireccion(b.estaMirandoDerecha());
            this.setImagen(b.estaMirandoDerecha() ? paracaidasDerechaFrames[0] : paracaidasIzquierdaFrames[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void cargarFrames(BufferedImage spriteSheet) {
        
        int[][] coordenadasDerecha = {
            {20, 102, 7, 9}, 
            {37, 102, 6, 9},
            {53, 102, 7, 9},
            {68, 97, 9, 14},
            {83, 96, 9, 16},
            {99, 96, 9, 15}, 
            {115, 96, 9, 15},
            {131, 96, 9, 14} 
        };

        int[][] coordenadasIzquierda = {
            {22, 118, 7, 9}, 
            {38, 118, 6, 9},
            {53, 118, 7, 9},
            {68, 113, 9, 14},
            {85, 112, 9, 16},
            {101, 112, 9, 15}, 
            {117, 112, 9, 15},
            {133, 112, 9, 14}
        };

        int cantidadFrames = coordenadasDerecha.length;
        int escala = 2;

        paracaidasDerechaFrames = new BufferedImage[cantidadFrames];
        paracaidasIzquierdaFrames = new BufferedImage[cantidadFrames];

         for (int i = 0; i < cantidadFrames; i++) {
            int x = coordenadasDerecha[i][0];
            int y = coordenadasDerecha[i][1];
            int ancho = coordenadasDerecha[i][2];
            int alto = coordenadasDerecha[i][3];

            paracaidasDerechaFrames[i] = escalarImagen(
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

            paracaidasIzquierdaFrames[i] = escalarImagen(
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

    public void setDireccion(boolean derecha) {
        super.setDireccion(derecha);
        this.setImagen(derecha ? paracaidasDerechaFrames[0] : paracaidasIzquierdaFrames[0]);
    }

    public void moverY(double dy) {
        setY(getY() + dy); // caída lenta
    }

    public void update(double delta) {
        boolean estaEnAire = detectarCaida();

        // Si aún no empezó a caer, no se activa la habilidad
        if (!habilidadActiva) {
            if (estaEnAire && !yaEstabaEnAire) {
                iniciarHabilidad(this);
            } else {
                yaEstabaEnAire = estaEnAire;
                super.update(delta); // permite que siga su comportamiento normal
                return;
            }
        }

        // Animación y caída lenta
        tiempoAnimacion += delta * multiplicadorVelocidad;
        if (tiempoAnimacion > 0.2 / multiplicadorVelocidad) {
            if (!faseRepeticionActiva) {
                if (frameActual < 6) {
                    frameActual++;
                } else {
                    faseRepeticionActiva = true;
                    indiceCiclo = 0;
                    frameActual = cicloFrames[indiceCiclo];
                }
            } else {
                indiceCiclo = (indiceCiclo + 1) % cicloFrames.length;
                frameActual = cicloFrames[indiceCiclo];
            }

            if (estaMirandoDerecha()) {
                setImagen(paracaidasDerechaFrames[frameActual]);
            } else {
                setImagen(paracaidasIzquierdaFrames[frameActual]);
            }

            tiempoAnimacion = 0;
        }

        moverY(1 * multiplicadorVelocidad); // caída lenta (si esta en x2 se modificara)

        // Si tocó el suelo, detenemos la habilidad
        if (!estaEnAire) {
            detenerHabilidad();
            super.update(delta);
        }

    }

    @Override
    public void guardarHabilidad() {
        // No hace nada. La usamos para indicar que la habilidad fue "asignada"
    }

    @Override
    public void iniciarHabilidad(Bichito b) {
        ultimaDireccionAntesDeHabilidad = b.estaMirandoDerecha();
        this.habilidadActiva = true;
        this.yaEstabaEnAire = true;
    }

    @Override
    public void detenerHabilidad() {
        habilidadActiva = false;
        yaEstabaEnAire = false;
        frameActual = 0;
        faseRepeticionActiva = false;
        setDireccion(ultimaDireccionAntesDeHabilidad);
    }

    @Override
    public void setPosicion(int x, int y) {
        setX(x);
        setY(y);
    }

    @Override
    public void mostrar(Graphics2D g) {
        if (getImagen() != null) {
            g.drawImage(getImagen(), (int)getX(), (int)getY(), null);
        }
    }
}