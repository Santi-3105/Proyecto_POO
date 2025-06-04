package lemmings;

import java.time.LocalDateTime;

public class Jugador {
    private final String nombre;
    private LocalDateTime fechaPartida;
    private int lemmingsRescatados;
    private long tiempoJuego; // en segundos

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.fechaPartida = LocalDateTime.now();
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDateTime getFechaPartida() {
        return fechaPartida;
    }

    public int getLemmingsRescatados() {
        return lemmingsRescatados;
    }

    public void setLemmingsRescatados(int lemmingsRescatados) {
        this.lemmingsRescatados = lemmingsRescatados;
    }

    public long getTiempoJuego() {
        return tiempoJuego;
    }

    public void setTiempoJuego(long tiempoJuego) {
        this.tiempoJuego = tiempoJuego;
    }

    public void setFechaPartida(LocalDateTime fechaPartida) {
        this.fechaPartida = fechaPartida;
    }

    public int calcularPuntaje() {
        return lemmingsRescatados * 100 - (int)tiempoJuego;
    }

    public String getTiempoJuegoFormateado() {
        long minutos = tiempoJuego / 60;
        long segundos = tiempoJuego % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }
}
