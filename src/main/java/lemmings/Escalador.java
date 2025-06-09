package lemmings;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Escalador extends Bichito implements Habilidad {
    private BufferedImage[] escaladaDerechaFrames;
    private BufferedImage[] escaladaIzquierdaFrames;
    private int frameActual = 0;
    private double tiempoAnimacion = 0;
    private boolean habilidadActiva = false;
    private final double VELOCIDAD_ESCALADA = 0.8; // Bloques por segundo
    private boolean ultimaDireccionAntesDeHabilidad = true;
    private boolean escalando = false;
    private double tiempoDesdeUltimoMovimiento = 0;

    public Escalador(Bichito b) {
        try {
            setPosicion(b.getX(), b.getY());
            this.setNivel(b.getNivel());
            if (Lemming.skin.equals("Original")) {
                BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/LemmingsSprite.png"));
                cargarFramesEscalador(spriteSheet);
            }
            if(Lemming.skin.equals("LemmingRed"))
            {
                BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/SpriteSkins2.png"));
                cargarFramesEscalador(spriteSheet);
            }
            if(Lemming.skin.equals("LemmingViolet"))
            {
                BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/SpriteSkins1.png"));
                cargarFramesEscalador(spriteSheet);
            }
            setDireccion(b.estaMirandoDerecha());
            this.setImagen(b.estaMirandoDerecha() ? escaladaDerechaFrames[0] : escaladaIzquierdaFrames[0]);
            ultimaDireccionAntesDeHabilidad = b.estaMirandoDerecha();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarFramesEscalador(BufferedImage spriteSheet) {
        // Frames para escalar hacia la derecha
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

        // Frames para escalar hacia la izquierda
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
        escaladaDerechaFrames = new BufferedImage[coordenadasDerecha.length];
        escaladaIzquierdaFrames = new BufferedImage[coordenadasIzquierda.length];

        // Cargar frames derecha
        for (int i = 0; i < coordenadasDerecha.length; i++) {
            int x = coordenadasDerecha[i][0];
            int y = coordenadasDerecha[i][1];
            int ancho = coordenadasDerecha[i][2];
            int alto = coordenadasDerecha[i][3];

            escaladaDerechaFrames[i] = escalarImagen(
                    spriteSheet.getSubimage(x, y, ancho, alto),
                    ancho * escala,
                    alto * escala
            );
        }

        // Cargar frames izquierda
        for (int i = 0; i < coordenadasIzquierda.length; i++) {
            int x = coordenadasIzquierda[i][0];
            int y = coordenadasIzquierda[i][1];
            int ancho = coordenadasIzquierda[i][2];
            int alto = coordenadasIzquierda[i][3];

            escaladaIzquierdaFrames[i] = escalarImagen(
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
    public void update(double delta) {
        tiempoAnimacion += delta * multiplicadorVelocidad;
        tiempoDesdeUltimoMovimiento += delta;

        if (habilidadActiva) {
            // Lógica de escalada
            if (escalando) {
                // Mover hacia arriba mientras escala
                setY(getY() - VELOCIDAD_ESCALADA * delta * multiplicadorVelocidad * getNivel().getAltoEstructura());

                // Verificar si debe dejar de escalar
                if (!seguirEscalando()) {
                    detenerHabilidad();
                }
            } else {
                // Verificar si debe empezar a escalar
                if (empezarAEscalar()) {
                    escalando = true;
                    tiempoDesdeUltimoMovimiento = 0;
                } else if (tiempoDesdeUltimoMovimiento > 0.5) {
                    // Si no puede escalar después de un tiempo, volver a comportamiento normal
                    detenerHabilidad();
                }
            }

            // Animación de escalada
            if (tiempoAnimacion > 0.1 / multiplicadorVelocidad) {
                frameActual = (frameActual + 1) % escaladaDerechaFrames.length;
                BufferedImage[] framesActuales = estaMirandoDerecha() ? escaladaDerechaFrames : escaladaIzquierdaFrames;
                this.setImagen(framesActuales[frameActual]);
                tiempoAnimacion = 0;
            }
        } else {
            super.update(delta);

            // Verificar si debe activar la habilidad (al chocar con pared)
            if (activarEscalador()) {
                iniciarHabilidad(this);
            }
        }
    }

    @Override
    public void setDireccion(boolean derecha) {
        super.setDireccion(derecha);
        if (habilidadActiva) {
            BufferedImage[] framesActuales = derecha ? escaladaDerechaFrames : escaladaIzquierdaFrames;
            this.setImagen(framesActuales[frameActual]);
        }
    }

    private boolean activarEscalador() {
        if (habilidadActiva) return false;
        isAsignable = false;
        int direccion = estaMirandoDerecha() ? 1 : -1;
        return detectarColisionMapa(direccion,0);
    }

    private boolean empezarAEscalar() {
        // Verificar si hay una pared frente al lemming y espacio arriba
        int direccion = estaMirandoDerecha() ? 1 : -1;
        int xFrente = (int)(getX() + (direccion > 0 ? getAncho() : 0) + direccion * 2);
        int ySuperior = (int)getY() - 2;

        int filaPared = ySuperior / getNivel().getAltoEstructura();
        int columnaPared = xFrente / getNivel().getAnchoEstructura();
        if(direccion==1){
            columnaPared = xFrente / (getNivel().getAnchoEstructura()-1);
        }

        // Verificar que haya espacio para escalar arriba
        int filaArriba = ((int)getY() - getNivel().getAltoEstructura()) / getNivel().getAltoEstructura();
        int columnaArriba = (int)getX() / getNivel().getAnchoEstructura();
        if(direccion == 1){
            filaArriba += 1;
        }
        if (filaPared < 0 || filaPared >= getNivel().getFilas() ||
                columnaPared < 0 || columnaPared >= getNivel().getColumnas()) {
            return false;
        }

        Estructura pared = getNivel().getEstructura(filaPared, columnaPared);
        Estructura espacioArriba = filaArriba >= 0 ? getNivel().getEstructura(filaArriba, columnaArriba) : null;

        return pared != null && pared.esSolida() &&
                (espacioArriba == null || !espacioArriba.esSolida());
    }

    private boolean seguirEscalando() {
        // Verificar si todavía hay pared para escalar y espacio para moverse arriba
        int direccion = estaMirandoDerecha() ? 1 : -1;
        int xFrente = (int)(getX() + (direccion > 0 ? getAncho() : 0) + direccion * 2);
        int ySuperior = (int)getY() - 2;

        int filaPared = ySuperior / getNivel().getAltoEstructura();
        int columnaPared = xFrente / getNivel().getAnchoEstructura();
        if(direccion==1){
            columnaPared = xFrente / (getNivel().getAnchoEstructura()-1);
        }


        // Verificar espacio arriba
        int filaArriba = ((int)getY() - getNivel().getAltoEstructura()) / getNivel().getAltoEstructura();
        int columnaArriba = (int)getX() / getNivel().getAnchoEstructura();
        if(direccion == 1){
            filaArriba += 1;
        }

        if (filaPared < 0 || filaPared >= getNivel().getFilas() ||
                columnaPared < 0 || columnaPared >= getNivel().getColumnas()) {
            return false;
        }

        Estructura pared = getNivel().getEstructura(filaPared, columnaPared);
        Estructura espacioArriba = filaArriba >= 0 ? getNivel().getEstructura(filaArriba, columnaArriba) : null;

        // Si no hay pared pero hay espacio arriba, permitir un último movimiento para subir
        if (pared == null || !pared.esSolida()) {
            if (espacioArriba == null || !espacioArriba.esSolida()) {
                // Último movimiento para subir completamente
                setY(getY() - VELOCIDAD_ESCALADA * getNivel().getAltoEstructura());
            }
            return false;
        }

        return (espacioArriba == null || !espacioArriba.esSolida());
    }

    @Override
    public void iniciarHabilidad(Bichito b) {
        habilidadActiva = true;
        escalando = false; // Se activará cuando detecte la pared
        ultimaDireccionAntesDeHabilidad = b.estaMirandoDerecha();
        tiempoDesdeUltimoMovimiento = 0;
    }

    @Override
    public void detenerHabilidad() {
        // Aseguramos de que está completamente sobre la superficie
        if (!detectarCaida()) {
            // Ajustamos posición final para que quede sobre el bloque
            int fila = (int)getY() / getNivel().getAltoEstructura();
            setY(fila * getNivel().getAltoEstructura());
        }

        habilidadActiva = false;
        escalando = false;
        frameActual = 0;
        setDireccion(ultimaDireccionAntesDeHabilidad);
        isAsignable = true;
    }


}