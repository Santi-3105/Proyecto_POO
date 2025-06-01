package lemmings;

import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


import clasesCompartidas.ObjetoGrafico;
import clasesCompartidas.Sonido;

public class Bichito extends ObjetoGrafico {
    private BufferedImage[] caminarDerechaFrames;
    private BufferedImage[] caminarIzquierdaFrames;
    private int frameActual = 0;
    private double tiempoAnimacion = 0;
    private boolean mirandoDerecha = true;
    private Nivel nivel;
    private boolean estaMuerto = false;
    private double alturaCaidaAcumulada = 0;
    private boolean estabaCayendo = false;
    private final double altura_maxima_caida = 100;


    public Bichito() {
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/lemmings/LemmingsSprite.png"));
            cargarFrames(spriteSheet);
            this.setImagen(caminarDerechaFrames[0]); // imagen inicial
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarFrames(BufferedImage spriteSheet) {
        int ancho = 16;
        int alto = 11;
        int cantidadFrames = 8;
        int escala = 2;

        caminarDerechaFrames = new BufferedImage[cantidadFrames];
        caminarIzquierdaFrames = new BufferedImage[cantidadFrames];

        for (int i = 0; i < cantidadFrames; i++) {
            BufferedImage frameDerecha = spriteSheet.getSubimage((i + 1) * ancho, 0, ancho, alto);
            BufferedImage frameIzquierda = spriteSheet.getSubimage((i + 1) * ancho, alto - 1, ancho, alto);

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

    public boolean estaMirandoDerecha() {
        return mirandoDerecha;
    }

    public int getAncho() {
        return getImagen().getWidth();
    }

    public int getAlto() {
        return getImagen().getHeight();
    }

    public void setPosicion(int x, int y) {
        setX(x);
        setY(y);
    }

    public void moverX(int dx) {
        setX(getX() + dx);
    }

    public void moverY(int dy) {
        setY(getY() + dy);
    }

    @Override
    public void mostrar(Graphics2D g) {
        if (getImagen() != null) {
            g.drawImage(getImagen(), (int) getX(), (int) getY(), null);
        }
    }

    public boolean colisionaCon(Bloqueador bloqueador) {
        Rectangle rectBichito = new Rectangle((int) getX(), (int) getY(), getAncho(), getAlto());
        Rectangle rectBloqueador = new Rectangle((int) bloqueador.getX(), (int) bloqueador.getY(), bloqueador.getAncho(), bloqueador.getAlto());
        return rectBichito.intersects(rectBloqueador);
    }

    public boolean estaMuerto() {
        return estaMuerto;
    }


    public void morir() {
        estaMuerto = true;
        Sonido.reproducir("die.wav");
        //añadir animación de muerte

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
        if (estaMuerto) return;
        /*
        tiempoAnimacion += delta;
        if (tiempoAnimacion > 0.1) {
            caminar();
            tiempoAnimacion -= 0.1;
        }*/
        if (detectarPinche(nivel)) {
            morir();
            return;
        }

        if (detectarCaida()) {
            moverY(2);
        } else {
            int direccion = estaMirandoDerecha() ? 1 : -1;
            if (!detectarColisionMapa(direccion, 0)) {
                moverX(direccion);
            } else {
                setDireccion(!estaMirandoDerecha());
            }
        }

        actualizarCaida(delta);
    }

    private void actualizarCaida(double delta) {
        boolean estaCayendoAhora = detectarCaida();
        if (estaCayendoAhora) {
            alturaCaidaAcumulada += 2;
            moverY(2);
            estabaCayendo = true;
        } else {
            // si dejó de caer, se verifica si la caída fue mortal
            if (estabaCayendo && alturaCaidaAcumulada > altura_maxima_caida) {
                morir();
            }
            //resetea valores de caídas
            alturaCaidaAcumulada = 0;
            estabaCayendo = false;
        }
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Nivel getNivel() {
        return this.nivel;
    }

    public boolean detectarColisionMapa(int dx, int dy) {
        if (nivel == null) {
            return false;
        }

        int nuevoX = (int) getX() + dx;
        int nuevoY = (int) getY() + dy;

        // Coordenadas del mapa en filas/columnas
        int fila = nuevoY / nivel.getAltoEstructura();
        int columna = nuevoX / nivel.getAnchoEstructura();

        // Asegurarse de que esté dentro de los límites del mapa
        if (fila < 0 || fila >= nivel.getFilas() || columna < 0 || columna >= nivel.getColumnas()) {
            return true; // Colisiona con el borde
        }

        Estructura estructura = nivel.getEstructura(fila, columna);
        return estructura != null && estructura.esSolida();
    }

    public boolean detectarCaida() {
        if (nivel == null) {
            return false;
        }

        // Verificar ambos bordes inferiores (izquierdo y derecho)
        int xIzquierdo = (int) getX() + 5; // Pequeño margen desde el borde izquierdo
        int xDerecho = (int) getX() + getAncho() - 5; // Pequeño margen desde el borde derecho
        int yInferior = (int) getY() + getAlto() + 1; // Justo debajo del lemming

        // Convertir a coordenadas de mapa
        int filaDebajo = yInferior / nivel.getAltoEstructura();
        int columnaIzquierda = xIzquierdo / nivel.getAnchoEstructura();
        int columnaDerecha = xDerecho / nivel.getAnchoEstructura();

        // Verificar si está dentro de los límites del mapa
        if (filaDebajo >= nivel.getFilas()) {
            return true; // Está cayendo fuera del mapa
        }

        // Verificar ambos lados
        boolean haySoporteIzquierdo = false;
        boolean haySoporteDerecho = false;

        if (columnaIzquierda >= 0 && columnaIzquierda < nivel.getColumnas()) {
            Estructura estructuraIzquierda = nivel.getEstructura(filaDebajo, columnaIzquierda);
            haySoporteIzquierdo = estructuraIzquierda != null && estructuraIzquierda.esSolida();
        }

        if (columnaDerecha >= 0 && columnaDerecha < nivel.getColumnas()) {
            Estructura estructuraDerecha = nivel.getEstructura(filaDebajo, columnaDerecha);
            haySoporteDerecho = estructuraDerecha != null && estructuraDerecha.esSolida();
        }

        // Solo hay caída si ambos puntos no tienen soporte
        return !(haySoporteIzquierdo || haySoporteDerecho);
    }

    public boolean detectarMeta(Nivel nivel) {
        if (nivel == null || nivel.getMetaX() == -1) {
            return false;
        }
        Rectangle rectLemming = new Rectangle((int) getX(), (int) getY(), getAncho(), getAlto());
        //Rectangle rectMeta = new Rectangle(nivel.getMetaX(),nivel.getMetaY(),nivel.getAnchoEstructura(),nivel.getAltoEstructura());

        return rectLemming.intersects(nivel.getMetaX(), nivel.getMetaY(), nivel.getAnchoEstructura(), nivel.getAltoEstructura());
    }

    public boolean detectarPinche(Nivel nivel) {
        if (nivel == null || nivel.getPincheX() == -1) {
            return false;
        }
        Rectangle rectLemming = new Rectangle((int) getX(), (int) getY(), getAncho(), getAlto());
        // Verificar colisión con todos los pinches del nivel
        for (Rectangle pinche : nivel.getPinches()) {
            if (rectLemming.intersects(pinche)) {
                return true;
            }
        }
        return false;

    }

}
