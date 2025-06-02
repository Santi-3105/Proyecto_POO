package lemmings;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RankingManager {
    private String DB_URL = "jdbc:sqlite:lemming_ranking.db";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public RankingManager() {
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS ranking (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "lemmings_rescatados INTEGER NOT NULL," +
                "nivel_alcanzado TEXT NOT NULL," +
                "tiempo_juego INTEGER NOT NULL," +
                "fecha_partida TEXT NOT NULL," +
                "puntaje INTEGER NOT NULL)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error al crear tabla: " + e.getMessage());
        }
        System.out.println("Ruta de la DB: " + new File("lemming_ranking.db").getAbsolutePath());
    }

    public void guardarJugador(Jugador jugador) {
        String sql = "INSERT INTO ranking(nombre, lemmings_rescatados, nivel_alcanzado, " +
                "tiempo_juego, fecha_partida, puntaje) VALUES(?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, jugador.getNombre());
            pstmt.setInt(2, jugador.getLemmingsRescatados());
            pstmt.setString(3, jugador.getNivelAlcanzado());
            pstmt.setLong(4, jugador.getTiempoJuego());
            pstmt.setString(5, jugador.getFechaPartida().format(DATE_FORMATTER));
            pstmt.setInt(6, jugador.calcularPuntaje());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar jugador: " + e.getMessage());
        }
    }

    public ArrayList<Jugador> obtenerTop10() {
        ArrayList<Jugador> top10 = new ArrayList<>();
        String sql = "SELECT nombre, lemmings_rescatados, nivel_alcanzado, " +
                "tiempo_juego, fecha_partida FROM ranking " +
                "ORDER BY puntaje DESC LIMIT 10";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Jugador jugador = new Jugador(rs.getString("nombre"));
                jugador.setLemmingsRescatados(rs.getInt("lemmings_rescatados"));
                jugador.setNivelAlcanzado(rs.getString("nivel_alcanzado"));
                jugador.setTiempoJuego(rs.getLong("tiempo_juego"));

                LocalDateTime fecha = LocalDateTime.parse(
                        rs.getString("fecha_partida"),
                        DATE_FORMATTER
                );
                jugador.setFechaPartida(fecha);

                top10.add(jugador);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ranking: " + e.getMessage());
        }

        return top10;
    }
}