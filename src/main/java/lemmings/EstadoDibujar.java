package lemmings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EstadoDibujar {
    private Lemming juego;
    private int xBoton;
    private int hudAlto;
    private int botonAncho;
    private int botonAlto;
    private int espacio;
    private int yBoton;
    private String[] etiquetas = { "Bloqueador", "Escalador", "Cavador", "Paracaidista", "Velocidad X2",
            "Destrucción" };
    private Image[] imagenesHabilidades = new Image[6];
    private final Image imageEstadistica;
    private final Image imagePerdedor;

    public EstadoDibujar(Lemming juego) throws IOException {
        this.juego = juego;
        // Cargo las imagenes antes de entrar en el juego
        imageEstadistica = ImageIO.read(getClass().getResource("/lemmings/estadisticas.png"));
        imagePerdedor = ImageIO.read(getClass().getResource("/lemmings/perdedor.png"));
        for (int i = 0; i < imagenesHabilidades.length; i++) {
            if (i > 3) {
                imagenesHabilidades[i] = ImageIO.read(getClass().getResource("/lemmings/habilidad" + i + ".png"));
            } else {
                imagenesHabilidades[i] = ImageIO.read(getClass().getResource("/lemmings/habilidad" + i + ".jpg"));
            }
        }
    }
    public void dibujarMenu(Graphics2D dibuje){
        dibuje.setColor(Color.WHITE);
        dibuje.setFont(new Font("SansSerif", Font.BOLD, 32));
        dibuje.drawString("[J] Elegir mapa", 310, 200);
        dibuje.drawString("[R] Raking", 310, 300);
        dibuje.setFont(new Font("SansSerif", Font.BOLD, 10));
        dibuje.drawString("Presione 1", 379, 225);
        dibuje.drawString("Presione R", 376, 325);
    }
    public void dibujarNivel(Graphics2D dibuje, Nivel nivel, ArrayList<Bichito>lemmingsEnJuego, Bichito lemmingSeleccionado,int bichitosRescatados, Temporizador temporizador){

            if (nivel != null) {
                nivel.mostrar(dibuje);
                creadHUD(dibuje, bichitosRescatados, temporizador);

                for (Bichito bichi : lemmingsEnJuego) {
                    if (bichi != null) {
                        bichi.mostrar(dibuje);
                    }
                }
                if (lemmingSeleccionado != null && lemmingsEnJuego.contains(lemmingSeleccionado)) {
                    dibuje.setColor(Color.YELLOW);
                    dibuje.drawRect((int) lemmingSeleccionado.getX() - 2, (int) lemmingSeleccionado.getY() - 2,
                            lemmingSeleccionado.getAncho() + 4, lemmingSeleccionado.getAlto() + 4);

                }
            }

    }

    public void dibujarEstadoMapa(Graphics2D dibuje){
        dibuje.setColor(Color.WHITE);
        dibuje.setFont(new Font("SansSerif", Font.BOLD, 26));
        dibuje.drawString("[1] Mapa 1", 310, 200);
        dibuje.drawString("[2] Mapa 2", 310, 300);
        dibuje.drawString("[3] Mapa 3", 310, 400);
        dibuje.setFont(new Font("SansSerif", Font.BOLD, 10));
        dibuje.drawString("Presione 1", 379, 225);
        dibuje.drawString("Presione 2", 376, 325);
        dibuje.drawString("Presione 3", 376, 425);
        dibuje.drawString("Volver menú: Esq", 12, 600);
    }

    public void dibujarGanador(Graphics2D dibuje,Jugador jugadorActual){
        dibuje.setColor(Color.WHITE);
        dibuje.drawImage(imageEstadistica, 260, 20, null);
        dibuje.setFont(new Font("SansSerif", Font.BOLD, 18));
        dibuje.drawString("Jugador: ", 50, 300);
        dibuje.drawString(jugadorActual.getNombre(), 140, 300);
        dibuje.drawString("Lemmings rescatados: ", 50, 360);
        dibuje.drawString("" + jugadorActual.getLemmingsRescatados(), 260, 360);
        dibuje.drawString("Tiempo: ", 50, 420);
        dibuje.drawString(jugadorActual.getTiempoJuegoFormateado(), 130, 420);
        dibuje.drawString("Fecha: ", 50, 500);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaFormateada = jugadorActual.getFechaPartida().format(dateFormatter);
        dibuje.drawString(fechaFormateada, 120, 500);
    }
    private void creadHUD(Graphics2D dibuje, int bichitosRescatados, Temporizador temporizador) {
        // Dibujar fondo del HUD con degradado gris oscuro a negro
        hudAlto = 100; // fijo
        GradientPaint gradiente = new GradientPaint(0, juego.getHeight() - hudAlto, new Color(40, 40, 40), 0, juego.getHeight(),
                Color.BLACK);
        dibuje.setPaint(gradiente);
        dibuje.fillRect(0, juego.getHeight() - hudAlto, juego.getWidth(), hudAlto);
        // Dibujar borde blanco alrededor del HUD
        dibuje.setColor(Color.WHITE);
        dibuje.setStroke(new BasicStroke(2));
        dibuje.drawRect(0, juego.getHeight() - hudAlto, juego.getWidth() - 1, hudAlto - 1);

        // Dimensiones de botones e imágenes
        botonAncho = 50;
        botonAlto = 50;
        espacio = 30;
        yBoton = juego.getHeight() - hudAlto + 5; // margen superior
        Font fuenteTexto = new Font("SansSerif", Font.BOLD, 11);
        dibuje.setFont(fuenteTexto);
        FontMetrics metrics = dibuje.getFontMetrics(fuenteTexto);

        for (int i = 0; i < 6; i++) {
            xBoton = 50 + i * (botonAncho + espacio);

            // Imagen (centrada arriba)
            dibuje.drawImage(imagenesHabilidades[i], xBoton, yBoton, botonAncho, botonAlto, null);

            // Coordenadas para los textos (más abajo)
            int yTextoBase = yBoton + botonAlto + 10;

            // Texto de número [n] con sombra
            String tecla = "[" + (i + 1) + "]";
            int anchoTecla = metrics.stringWidth(tecla);
            int xTecla = xBoton + (botonAncho - anchoTecla) / 2;

            dibuje.setColor(Color.BLACK);
            dibuje.drawString(tecla, xTecla + 1, yTextoBase + 1);
            dibuje.setColor(Color.WHITE);
            dibuje.drawString(tecla, xTecla, yTextoBase);

            // Texto de habilidad con sombra, debajo del número
            String texto = etiquetas[i];
            int textoAncho = metrics.stringWidth(texto);
            int xTexto = xBoton + (botonAncho - textoAncho) / 2;
            int yTexto = yTextoBase + 12;

            dibuje.setColor(Color.BLACK);
            dibuje.drawString(texto, xTexto + 1, yTexto + 1);
            dibuje.setColor(Color.WHITE);
            dibuje.drawString(texto, xTexto, yTexto);
        }
        dibuje.setFont(new Font("SansSerif", Font.BOLD, 18));
        dibuje.drawString("Rescatados: " + bichitosRescatados, 610, 585);
        dibuje.drawString("Tiempo: " + temporizador.getTiempoFormateado(), 610, 550);
    }

    public void dibujarPerdedor(Graphics2D dibuje){
        dibuje.setColor(Color.WHITE);
        dibuje.drawImage(imagePerdedor, 300, 200, null);
    }
}
