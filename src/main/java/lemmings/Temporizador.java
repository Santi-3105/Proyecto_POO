package lemmings;

import java.time.Duration;
import java.time.Instant;

public class Temporizador {
    private Instant inicio;
    private boolean enEjecucion;
    private long segundosAcumulados; // Para pausar/reanudar

    public Temporizador() {
        this.enEjecucion = false;
        this.segundosAcumulados = 0;
    }

    // Inicia el temporizador
    public void iniciar() {
        if (!enEjecucion) {
            inicio = Instant.now();
            enEjecucion = true;
        }
    }

    // Detiene el temporizador y devuelve los segundos que pasaron
    public long detener() {
        if (enEjecucion) {
            segundosAcumulados += Duration.between(inicio, Instant.now()).getSeconds();
            enEjecucion = false;
        }
        return segundosAcumulados;
    }

    // metodo para formatear segundos en mm:ss
    public String getTiempoFormateado() {
        long segundosTotales = enEjecucion
                ? segundosAcumulados + Duration.between(inicio, Instant.now()).getSeconds()
                : segundosAcumulados;

        long minutos = segundosTotales / 60;
        long segundos = segundosTotales % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    // Reinicia el temporizador
    public void reiniciar() {
        segundosAcumulados = 0;
        enEjecucion = false;
    }
}