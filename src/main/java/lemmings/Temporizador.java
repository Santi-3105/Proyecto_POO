package lemmings;

public class Temporizador {
    private double tiempoAcumulado; // en segundos
    private boolean enEjecucion;
    private double multiplicadorVelocidad = 1.0;

    public Temporizador() {
        tiempoAcumulado = 0;
        enEjecucion = false;
    }

    public void iniciar() {
        enEjecucion = true;
    }

    public void detener() {
        enEjecucion = false;
    }

    public void reiniciar() {
        tiempoAcumulado = 0.0; 
        enEjecucion = false;
        multiplicadorVelocidad = 1.0; // volver a la velocidad normal
    }

    // Se llama desde tu método update general (por ejemplo GameUpdate)
    public void update(double delta) {
        if (enEjecucion) {
            tiempoAcumulado += delta * multiplicadorVelocidad;
        }
    }

    public void setMultiplicadorVelocidad(double factor) {
        this.multiplicadorVelocidad = factor;
    }
    public void renaudar(double tiempoAcumulado){this.tiempoAcumulado = tiempoAcumulado;}

    public String getTiempoFormateado() {
        int totalSegundos = (int) tiempoAcumulado;
        int minutos = totalSegundos / 60;
        int segundos = totalSegundos % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    public double getTiempoEnSegundos() {
        return tiempoAcumulado;
    }
}
