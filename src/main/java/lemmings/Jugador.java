package lemmings;

import java.time.LocalDateTime;

public class Jugador {
    private String nombre;
    private LocalDateTime fechaPartida;
    private int lemmingsRescatados;
    private String nivelAlcanzado;
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

    public String getNivelAlcanzado() {
        return nivelAlcanzado;
    }

    public void setNivelAlcanzado(String nivelAlcanzado) {
        this.nivelAlcanzado = nivelAlcanzado;
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
}
