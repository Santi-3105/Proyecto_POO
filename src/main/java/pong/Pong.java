package pong;

import com.entropyinteractive.JGame;
import com.entropyinteractive.Keyboard;
import com.entropyinteractive.Log;

import java.awt.*;


public class Pong extends JGame {
    private Pelota pelota;
    private Paleta paletaIzquierda;
    private Paleta paletaDerecha;
    private Arco arcoIzquierdo;
    private Arco arcoDerecho;
    private boolean esperandoReinicio = false;
    private double tiempoEspera = 0;
    private static final double TIEMPO_ESPERA_MAXIMO = 0.5; // medio segundo de espera para volver a poner la pelota al medio


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
            pelota = new Pelota(10, 400, 300, 250, 250);
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
        if (esperandoReinicio) {
            tiempoEspera += delta;
            if (tiempoEspera >= TIEMPO_ESPERA_MAXIMO) {
                pelota.reiniciarPelota();
                esperandoReinicio = false;
                tiempoEspera = 0;
            }
            return; //No hacer nada más mientras esperamos
        }

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
        if (pelota.getY() <= 37) {
            // Toca el borde superior
            pelota.setY(37);
            pelota.invertirDireccionY();
        }
        //colision de pelota con parte inferior
        if (pelota.getY() + pelota.getAlto() >= getHeight()) {
            // Toca el borde inferior
            pelota.setY(getHeight() - pelota.getAlto()); // La pega al borde inferior
            pelota.invertirDireccionY();
        }

        //verificar si hubo gol
        if (arcoIzquierdo.detectaGol(pelota) || arcoDerecho.detectaGol(pelota)) {
            esperandoReinicio = true;
            pelota.setVelocidadX(0); // La detenemos
            pelota.setVelocidadY(0);
        }
    }



    public void gameDraw(Graphics2D dibuje){
        dibuje.setBackground(Color.BLACK);
        pelota.mostrar(dibuje);
        paletaIzquierda.mostrar(dibuje);
        paletaDerecha.mostrar(dibuje);
        arcoIzquierdo.getMarcador().dibujar(dibuje);
        arcoDerecho.getMarcador().dibujar(dibuje);
    }

    public void gameShutdown(){
        Log.info(getClass().getSimpleName(), "Shutting down game");
        System.exit(0);
    }


}
