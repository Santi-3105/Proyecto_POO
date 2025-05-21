package lemmings;

import clasesCompartidas.LanzadorJuego;
import com.entropyinteractive.JGame;
import com.entropyinteractive.Keyboard;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Lemming extends JGame {
    private Keyboard teclado = this.getKeyboard(); // Inicializa el teclado
    private int estado;
    private final int ESTADO_MENU=0;
    private final int ESTADO_ELEGIR_MAPA=1;
    private final int ESTADO_MAPA_1=2;
    private final int ESTADO_MAPA_2=3;
    private final int ESTADO_MAPA_3=4;
    private final int ESTADO_RANKING=5;



    public static void main(String[] args) {
        Lemming game = new Lemming("Lemmings",800,600);
        game.run(1.0 / 60.0);
        System.exit(0);
    }

    public Lemming(String title, int width, int height){
        super(title, width, height);
    }

    public void gameStartup() {
        try {
            estado = ESTADO_MENU;
        } catch (Exception ex) {
            System.out.println("ERROR en gameStartup");
            ex.printStackTrace();
        }
    }

    public void gameUpdate(double delta) {

        if(estado==ESTADO_MENU){
            if (teclado.isKeyPressed(KeyEvent.VK_1)) {
                estado=ESTADO_ELEGIR_MAPA;
            }
            if(teclado.isKeyPressed(KeyEvent.VK_R)){
                estado=ESTADO_RANKING;
            }
            return;
        }

        if(estado==ESTADO_ELEGIR_MAPA){
            if (teclado.isKeyPressed(KeyEvent.VK_1)) {
                jugarMapa1();
            } else if (teclado.isKeyPressed(KeyEvent.VK_2)) {
                jugarMapa2();
            } else if (teclado.isKeyPressed(KeyEvent.VK_3)) {
                jugarMapa3();
            }else if(teclado.isKeyPressed(KeyEvent.VK_ESCAPE)){
                estado=ESTADO_MENU;
            }
            return; // se saltea si no esta en menú
        }

        if(estado==ESTADO_MAPA_1){
            //actualizar mapa 1
            return; // se saltea si no esta en mapa 1
        }

        if(estado==ESTADO_MAPA_2){
            //actualizar mapa 2
            return; // se saltea si no esta en mapa 2
        }

        if(estado==ESTADO_MAPA_3){
            //actualizar mapa 3
            return; // se saltea si no esta en mapa 3
        }
    }

    public void gameDraw(Graphics2D dibuje) {
        dibuje.setColor(Color.BLACK);
        dibuje.fillRect(0, 0, getWidth(), getHeight());

        if (estado == ESTADO_MENU) {
            dibuje.setColor(Color.WHITE);
            dibuje.setFont(new Font("SansSerif", Font.BOLD, 32));
            dibuje.drawString("[1] Elegir mapa", 310, 200);
            dibuje.drawString("[R] Raking", 310, 300);
            dibuje.setFont(new Font("SansSerif", Font.BOLD, 10));
            dibuje.drawString("Presione 1", 379, 225);
            dibuje.drawString("Presione R", 376, 325);
        } else if (estado == ESTADO_ELEGIR_MAPA) {
            dibuje.setColor(Color.WHITE);
            dibuje.setFont(new Font("SansSerif", Font.BOLD, 26));
            dibuje.drawString("[1] Mapa 1", 310, 200);
            dibuje.drawString("[2] Mapa 2", 310, 300);
            dibuje.drawString("[3] Mapa 3", 310, 400);
            dibuje.setFont(new Font("SansSerif", Font.BOLD, 10));
            dibuje.drawString("Presione 1", 379, 225);
            dibuje.drawString("Presione 2", 376, 325);
            dibuje.drawString("Presione 3", 376, 425);
            dibuje.drawString("Volver menú: Esq", 12, 600);
        }else if (estado==ESTADO_MAPA_1){
            //dibujar mapa 1
        }else if (estado==ESTADO_MAPA_2){
            //dibujar mapa 2
        }else if (estado==ESTADO_MAPA_3){
            //dibujar mapa 3
        }

    }

    public void gameShutdown(){}

    private void jugarMapa1(){

    }
    private void jugarMapa2(){

    }
    private void jugarMapa3(){

    }
}
