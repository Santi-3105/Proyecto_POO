package lemmings;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.HashMap;

public class Nivel {
    private HashMap<Character, Estructura> estructuras; // Relación carácter -> estructura
    private char[][] mapa; // Matriz que define el nivel
    private int anchoEstructura;
    private int altoEstructura;

    public Nivel(String archivoMapa, String archivoEstructura) {
        estructuras = new HashMap<>();
        cargarEstructura(archivoEstructura);
        cargarMapa(archivoMapa);
    }

    private void cargarEstructura(String archivoConfig) {
        try {
            // Bloques indestructibles
            estructuras.put('M', new Estructura(
                    ImageIO.read(getClass().getResource("/lemmings/bloques/metal1.png")),
                    true,
                    false
            ));

            estructuras.put('N', new Estructura(
                    ImageIO.read(getClass().getResource("/lemmings/bloques/metal2.png")),
                    true,
                    false
            ));

            // Bloques destructibles
            estructuras.put('P', new Estructura(
                    ImageIO.read(getClass().getResource("/lemmings/bloques/pasto.png")),
                    true,
                    true
            ));
            estructuras.put('1', new Estructura(
                    ImageIO.read(getClass().getResource("/lemmings/bloques/tierraGrande.png")),
                    true,
                    true
            ));
            estructuras.put('2', new Estructura(
                    ImageIO.read(getClass().getResource("/lemmings/bloques/tierraCuadrada.png")),
                    true,
                    true
            ));
            estructuras.put('3', new Estructura(
                    ImageIO.read(getClass().getResource("/lemmings/bloques/tierraPiramide.png")),
                    true,
                    true
            ));
            estructuras.put('4', new Estructura(
                    ImageIO.read(getClass().getResource("/lemmings/bloques/tierraPlana.png")),
                    true,
                    true
            ));
            estructuras.put('5', new Estructura(
                    ImageIO.read(getClass().getResource("/lemmings/bloques/tierraCopo.png")),
                    true,
                    true
            ));

            // Elementos especiales
            estructuras.put('T', new Estructura(
                    ImageIO.read(getClass().getResource("/lemmings/bloques/tronco.png")),
                    false,
                    false
            ));
            /*estructuras.put('S', new Estructura(
                    ImageIO.read(getClass().getResource("/lemmings/bloques/spawn.png")),
                    false,
                    false
            ));*/
            estructuras.put('G', new Estructura(
                    ImageIO.read(getClass().getResource("/lemmings/bloques/meta.png")),
                    false,
                    false
            ));

            // Aire
            estructuras.put(' ', null);

        } catch (IOException e) {
            System.err.println("Error cargando estructuras: " + e.getMessage());
            // Crear imagen de fallback
            BufferedImage fallback = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = fallback.createGraphics();
            g.setColor(Color.RED);
            g.fillRect(0, 0, 32, 32);
            g.dispose();

            estructuras.put('#', new Estructura(fallback, true, false));
        }
    }

    private void cargarMapa(String archivoMapa) {
        // Ejemplo simplificado: carga desde un archivo de texto
        // (Implementación real puede usar BufferedReader)
        String[] filas = {
                "BBBBBBBBBBBBBBBBBBBB",
                "B                  B",
                "B    DD     DD     B",
                "B                  B",
                "B   DDDD   DDDD   B",
                "BBBBBBBBBBBBBBBBBBBB"
        };

        mapa = new char[filas.length][filas[0].length()];
        for (int y = 0; y < filas.length; y++) {
            for (int x = 0; x < filas[y].length(); x++) {
                mapa[y][x] = filas[y].charAt(x);
            }
        }
    }

    public void dibujar(Graphics2D g) {
        for (int y = 0; y < mapa.length; y++) {
            for (int x = 0; x < mapa[y].length; x++) {
                char tile = mapa[y][x];
                Estructura estructura = estructuras.get(tile);
                if (estructura != null) {
                    estructura.mostrar(g, x * anchoEstructura, y * altoEstructura);
                }
            }
        }
    }

    // Para colisiones
    public boolean esPosicionSolida(int x, int y) {
        int estructuraX = x / anchoEstructura;
        int estructuraY = y / altoEstructura;
        if (estructuraX >= 0 && estructuraY >= 0 && estructuraY < mapa.length && estructuraX < mapa[estructuraY].length) {
            Estructura estructura = estructuras.get(mapa[estructuraY][estructuraX]);
            return estructura != null && estructura.esSolida();
        }
        return true; // Fuera de límites = sólido
    }
}