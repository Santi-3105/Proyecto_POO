package lemmings;


import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
        int ancho = 16;
        int alto = 13; // el paracaidista tiene 13 pixeles de alto
        int cantidadFrames = 8;
        int escala = 2;
        int filaDerecha = 8; // la fila 9 en índice base 0
        int filaIzquierda = 9; // fila 10 (base 0) asumiendo que está justo abajo

        paracaidasDerechaFrames = new BufferedImage[cantidadFrames];
        paracaidasIzquierdaFrames = new BufferedImage[cantidadFrames];

        for (int i = 0; i < cantidadFrames; i++) {
            BufferedImage frameDerecha = spriteSheet.getSubimage((i + 1) * ancho, filaDerecha * alto - 7, ancho, alto);
            paracaidasDerechaFrames[i] = escalarImagen(frameDerecha, ancho * escala, alto * escala);

            BufferedImage frameIzquierda = spriteSheet.getSubimage((i + 1) * ancho, filaIzquierda * alto - 4, ancho,
                    alto);
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
        super.setDireccion(derecha);
        this.setImagen(derecha ? paracaidasDerechaFrames[0] : paracaidasIzquierdaFrames[0]);
    }

    public void moverY(int dy) {
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
        tiempoAnimacion += delta;
        if (tiempoAnimacion > 0.1) {
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

        moverY(1); // caída lenta

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