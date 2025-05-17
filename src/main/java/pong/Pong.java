package pong;

import com.entropyinteractive.JGame;
import com.entropyinteractive.Keyboard;
import com.entropyinteractive.Log;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;
import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class Pong extends JGame {
    protected Pelota pelota;
    protected Paleta paletaIzquierda;
    protected Paleta paletaDerecha;
    protected Arco arcoIzquierdo;
    protected Arco arcoDerecho;

    public static void main(String[] args) {
        Pong game = new Pong("Pong",800,600);
        game.run(1.0 / 60.0);
        System.exit(0);
    }

    public Pong(String title, int width, int height) {
        super(title, width, height);
    }


    public void gameStartup(){
        try{
            pelota = new Pelota(10, 400, 300, 150, 150);
            Keyboard teclado = this.getKeyboard();
            paletaIzquierda = new Paleta(10, 90, 30, 270,teclado);
            paletaDerecha = new Paleta(10, 90, 760, 270,teclado);
            arcoIzquierdo = new Arco(0, true);
            arcoDerecho = new Arco(getWidth(),false); //Acá estaba el getWidth() - 5


        }catch(Exception ex){
            System.out.println("ERROR en gameStartup");
            ex.printStackTrace();
        }
    }




    public void gameUpdate(double delta){
        paletaIzquierda.update(delta);
        paletaDerecha.update(delta);
        pelota.update(delta);
        //colisión con paleta izquierda
        if (pelota.colisiona(paletaIzquierda)) {
            pelota.setVelocidadX(Math.abs(pelota.getVelocidadX())); // Rebota a la derecha
        }
        //colisión con paleta derecha
        if (pelota.colisiona(paletaDerecha)) {
            pelota.setVelocidadX(-Math.abs(pelota.getVelocidadX())); // Rebota a la izquierda
        }

        // Colisión de pelota con la parte superior e inferior
        if (pelota.getY() <= 0) {
            // Toca el borde superior
            pelota.setY(37);
            pelota.invertirDireccionY();
        }

        if (pelota.getY() + pelota.getAlto() >= getHeight()) {
            // Toca el borde inferior
            pelota.setY(getHeight() - pelota.getAlto()); // La pega al borde inferior
            pelota.invertirDireccionY();
        }
    }



    public void gameDraw(Graphics2D dibuje){
        dibuje.setBackground(Color.BLACK);
        pelota.mostrar(dibuje);
        paletaIzquierda.mostrar(dibuje);
        paletaDerecha.mostrar(dibuje);
    }

    public void gameShutdown(){
        Log.info(getClass().getSimpleName(), "Shutting down game");
        System.exit(0);
    }

}
