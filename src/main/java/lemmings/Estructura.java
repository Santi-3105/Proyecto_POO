package lemmings;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Estructura {
    private BufferedImage imagen;
    private boolean esSolida;
    private boolean esDestructible;

    // Constructor que acepta BufferedImage
    public Estructura(BufferedImage imagen, boolean esSolida, boolean esDestructible) {
        this.imagen = imagen;
        this.esSolida = esSolida;
        this.esDestructible = esDestructible;
    }

    // Métodos getters
    public BufferedImage getImagen() { return imagen; }
    public boolean esSolida() { return esSolida; }
    public boolean esDestructible() { return esDestructible; }

    public void mostrar(Graphics2D g, int x, int y) {
        if (imagen != null) {
            g.drawImage(imagen, x, y, null);
        }
    }

}