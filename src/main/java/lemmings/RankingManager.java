package lemmings;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RankingManager {
    private final String DB_URL = "jdbc:sqlite:lemming_ranking.db";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public RankingManager() {
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS ranking (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "lemmings_rescatados INTEGER," +
                "tiempo_juego INTEGER," +
                "fecha_partida TEXT," +
                "puntaje INTEGER)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error al crear tabla: " + e.getMessage());
        }
    }

    public void guardarJugador(Jugador jugador) {
        String sql = "INSERT INTO ranking(nombre, lemmings_rescatados, " +
                "tiempo_juego, fecha_partida, puntaje) VALUES(?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, jugador.getNombre());
            pstmt.setInt(2, jugador.getLemmingsRescatados());
            pstmt.setLong(3, jugador.getTiempoJuego());
            pstmt.setString(4, jugador.getFechaPartida().format(DATE_FORMATTER));
            pstmt.setInt(5, jugador.calcularPuntaje());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar jugador: " + e.getMessage());
        }
    }

    public ArrayList<Jugador> obtenerTop10() {
        ArrayList<Jugador> top10 = new ArrayList<>();
        String sql = "SELECT nombre, lemmings_rescatados, " +
                "tiempo_juego, fecha_partida, puntaje FROM ranking " +
                "ORDER BY puntaje DESC LIMIT 10";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Ejecutando consulta TOP10..."); // Debug

            while (rs.next()) {
                Jugador jugador = new Jugador(rs.getString("nombre"));
                jugador.setLemmingsRescatados(rs.getInt("lemmings_rescatados"));
                jugador.setTiempoJuego(rs.getLong("tiempo_juego"));

                String fechaStr = rs.getString("fecha_partida");
                if (fechaStr != null) {
                    LocalDateTime fecha = LocalDateTime.parse(fechaStr, DATE_FORMATTER);
                    jugador.setFechaPartida(fecha);
                }
                System.out.println("Jugador cargado: " + jugador.getNombre() +
                        ", Puntaje: " + rs.getInt("puntaje")); // Debug
                top10.add(jugador);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ranking: " + e.getMessage());
        }

        return top10;
    }
}