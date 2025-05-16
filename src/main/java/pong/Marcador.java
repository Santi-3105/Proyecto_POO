package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

class Marcador {
    private int puntaje;
    private int posicionX;
    private int posicionY;
    public Marcador(int posicionX, int posicionY){
        puntaje = 0;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    public void incrementarPuntaje() {
        puntaje++;
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(String.valueOf(puntaje), posicionX, posicionY);
    }
}
