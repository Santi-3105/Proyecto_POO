package lemmings;

import java.awt.*;
import java.util.ArrayList;


public class EstadoRanking {
    private final Lemming juego;
    private final RankingManager rankingManager;
    private final ArrayList<Jugador> topJugadores;

    public EstadoRanking(Lemming juego) {
        this.juego = juego;
        this.rankingManager = new RankingManager();
        this.topJugadores = rankingManager.obtenerTop10();
    }

    public void dibujar(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, juego.getWidth(), juego.getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("TOP 10 MEJORES PUNTAJES", 200, 80);

        g.setFont(new Font("Arial", Font.PLAIN, 20));

        int y = 150;
        for (int i = 0; i < topJugadores.size(); i++) {
            Jugador jugador = topJugadores.get(i);
            String linea = String.format("%d.   %s   -   %d lemmings   -   Nivel: %d   -   Tiempo: %ds",
                    i + 1,
                    jugador.getNombre(),
                    jugador.getLemmingsRescatados(),
                    jugador.getNivel(),
                    jugador.getTiempoJuego());
            g.drawString(linea, 100, y);
            y += 40;
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Presione ESC para volver", 300, 550);
    }
}