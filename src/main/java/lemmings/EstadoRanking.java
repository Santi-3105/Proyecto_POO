package lemmings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.entropyinteractive.Keyboard;
import lemmings.Lemming;
import lemmings.Jugador;
import lemmings.RankingManager;

public class EstadoRanking {
    private Lemming juego;
    private RankingManager rankingManager;
    private ArrayList<Jugador> topJugadores;

    public EstadoRanking(Lemming juego) {
        this.juego = juego;
        this.rankingManager = new RankingManager();
        this.topJugadores = rankingManager.obtenerTop10();
    }

    public void actualizar(double delta) {
        Keyboard teclado = juego.getKeyboard();

        if (teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            juego.cambiarEstado(0); // Volver al menú principal
        }
    }

    public void dibujar(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, juego.getWidth(), juego.getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("TOP 10 MEJORES PUNTAJES", 200, 100);

        g.setFont(new Font("Arial", Font.PLAIN, 20));

        int y = 100;
        for (int i = 0; i < topJugadores.size(); i++) {
            Jugador jugador = topJugadores.get(i);
            String linea = String.format("%d. %s - %d lemmings - Nivel %s - Tiempo: %ds",
                    i + 1,
                    jugador.getNombre(),
                    jugador.getLemmingsRescatados(),
                    jugador.getNivelAlcanzado(),
                    jugador.getTiempoJuego());
            g.drawString(linea, 100, y);
            y += 30;
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Presione ESC para volver", 300, 550);
    }
}