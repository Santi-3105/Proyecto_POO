package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

class Marcador {
    private int puntaje;
    public void Marcador(){
        puntaje = 0;
    }

    public void incrementarPuntaje() {
        puntaje++;
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        //g.drawString(String.valueOf(puntosJugadorIzquierda), 100, 50);
        //g.drawString(String.valueOf(puntosJugadorDerecha), 500, 50);
    }
}
