package lemmings;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Nivel {
    private HashMap<Character, Estructura> estructuras; // Relación carácter -> estructura
    private Estructura[][] mapaEstructuras;
    private int anchoEstructura = 32;
    private int altoEstructura = 32;

    public Nivel(String archivoMapa, String archivoEstructura) {
        estructuras = new HashMap<>();
        cargarEstructura(archivoEstructura);
        cargarMapa(archivoMapa);
    }

    private void cargarEstructura(String archivoConfig) {
        try {
            InputStream is = getClass().getResourceAsStream("/lemmings/" + archivoConfig);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

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
            estructuras.put('S', new Estructura(
                    ImageIO.read(getClass().getResource("/lemmings/bloques/spawn.png")),
                    false,
                    false
            ));
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
        try {
            InputStream is = getClass().getResourceAsStream("/lemmings/mapas/" + archivoMapa);
            if (is == null) {
                System.err.println("No se encontró el archivo del mapa: " + archivoMapa);
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            ArrayList<String> lineas = new ArrayList<>();
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    lineas.add(linea);
                }
            }

            if (lineas.isEmpty()) {
                System.err.println("El archivo del mapa está vacío.");
                return;
            }

            int filas = lineas.size();
            int columnas = lineas.get(0).length();

            // Validar que todas las filas tengan el mismo largo
            for (int i = 0; i < filas; i++) {
                if (lineas.get(i).length() != columnas) {
                    System.err.println("La fila " + i + " no tiene la misma longitud que la primera.");
                    return;
                }
            }

            mapaEstructuras = new Estructura[filas][columnas];

            for (int fila = 0; fila < filas; fila++) {
                String filaTexto = lineas.get(fila);
                for (int col = 0; col < columnas; col++) {
                    char simbolo = filaTexto.charAt(col);
                    Estructura estructura = estructuras.get(simbolo);
                    if (estructura == null) {
                        System.err.println("Símbolo '" + simbolo + "' no mapeado en estructuras.");
                        continue;
                    }
                    mapaEstructuras[fila][col] = estructura;
                }
            }

        } catch (IOException e) {
            System.err.println("Error cargando mapa: " + e.getMessage());
        }
    }


    public void mostrar(Graphics2D g) {
        if (mapaEstructuras == null) return;

        for (int fila = 0; fila < mapaEstructuras.length; fila++) {
            for (int col = 0; col < mapaEstructuras[0].length; col++) {
                Estructura estructura = mapaEstructuras[fila][col];
                if (estructura != null) {
                    estructura.mostrar(g, col * anchoEstructura, fila * altoEstructura);
                }
            }
        }
    }



}