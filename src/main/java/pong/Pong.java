package pong;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Properties;


import com.entropyinteractive.JGame;
import com.entropyinteractive.Keyboard;
import com.entropyinteractive.Log;

import clasesCompartidas.Sonido;
import clasesCompartidas.Musica;
import clasesCompartidas.conversorTecla;


public class Pong extends JGame {
    private Pelota pelota;
    private Paleta paletaIzquierda;
    private Paleta paletaDerecha;
    private Arco arcoIzquierdo;
    private Arco arcoDerecho;
    private boolean esperandoReinicio = false;
    private double tiempoEspera = 0;
    private int teclaArribaJ1;
    private int teclaAbajoJ1;
    private int teclaArribaJ2;
    private int teclaAbajoJ2;
    protected Properties appProperties;
    private Properties appProperties2;
    private Cancha cancha;

    private static final double TIEMPO_ESPERA_MAXIMO = 2.0; // dos segundos de espera para volver a poner la pelota al
    // medio
    private int estado;
    private static final int ESTADO_MENU = 0;
    private static final int ESTADO_JUEGO = 1;
    private static final int ESTADO_RANKING = 2;
    private static final int ESTADO_GANADOR = 3;
    private String ganador;

    public Pong(String title, int width, int height) {
        super(title, width, height);
        appProperties = new Properties();
        appProperties2 = new Properties();
        System.out.println(appProperties.stringPropertyNames());
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
        if (estado == ESTADO_MENU) {
            Keyboard key = this.getKeyboard(); // Inicializa el teclado
            if (key.isKeyPressed(KeyEvent.VK_1)) {
                // Un jugador
            } else if (key.isKeyPressed(KeyEvent.VK_2)) {
                iniciarJuego2Jugadores();
            } else if (key.isKeyPressed(KeyEvent.VK_R)) {
                //Ranking
            }
        }

        if (estado != ESTADO_JUEGO) {
            if (this.getKeyboard().isKeyPressed(KeyEvent.VK_ESCAPE)) {
                estado = ESTADO_MENU;
            }
            return;
        }
        //Solo se sigue si esta en estado_juego, para dejar de dibujar

        if (esperandoReinicio) {
            tiempoEspera += delta;
            if (tiempoEspera >= TIEMPO_ESPERA_MAXIMO) {
                    pelota.reiniciarPelota();
                    esperandoReinicio = false;
                    tiempoEspera = 0;
            }
            return; //No hacer nada si espeandoReinicio es false
        }

        paletaIzquierda.update(delta);
        paletaDerecha.update(delta);
        pelota.update(delta);
        // colisión con paleta izquierda
        if (pelota.colisiona(paletaIzquierda)) {
            Sonido.reproducir("golpe_audio.wav");
            pelota.setVelocidadX((Math.abs(pelota.getVelocidadX()))*1.15); // Rebota a la derecha y aumenta velocidad
        }
        // colisión con paleta derecha
        if (pelota.colisiona(paletaDerecha)) {
            Sonido.reproducir("golpe_audio.wav");
            pelota.setVelocidadX((-Math.abs(pelota.getVelocidadX()))*1.15); // Rebota a la izquierda y aumenta velocidad
        }

        // Colisión de pelota con la parte superior e inferior
        if (pelota.getY() <= 37) {
            // Toca el borde superior
            Sonido.reproducir("golpe_audio.wav");
            pelota.setY(37);
            pelota.invertirDireccionY();
        }
        // colision de pelota con parte inferior
        if (pelota.getY() + pelota.getAlto() >= getHeight()) {
            // Toca el borde inferior
            Sonido.reproducir("golpe_audio.wav");
            pelota.setY(getHeight() - pelota.getAlto()); // La pega al borde inferior
            pelota.invertirDireccionY();
        }

        // verificar si hubo gol
        if (arcoIzquierdo.detectaGol(pelota) || arcoDerecho.detectaGol(pelota)) {
            Sonido.reproducir("gol_audio.wav");
            esperandoReinicio = true;
            pelota.setVelocidadX(0); // La detenemos
            pelota.setVelocidadY(0);

            //verificar cuando algun marcador llege a 10, se dibuje el estadoGanador
            if(arcoIzquierdo.getMarcador().getPuntaje() == 10 || arcoDerecho.getMarcador().getPuntaje() == 10){
                estado=ESTADO_GANADOR;
                return; //cortar para no seguir ejecutando audio
            }
        }

        if (this.getKeyboard().isKeyPressed(KeyEvent.VK_ESCAPE)) {
            estado = ESTADO_MENU;
        }
    }

    public void gameDraw(Graphics2D dibuje) {
        dibuje.setColor(Color.BLACK);
        dibuje.fillRect(0, 0, getWidth(), getHeight());

        if (estado == ESTADO_MENU) {
            dibuje.setColor(Color.WHITE);
            dibuje.setFont(new Font("SansSerif", Font.BOLD, 32));
            dibuje.drawString("1 Jugador", 330, 200);
            dibuje.drawString("2 Jugadores", 312, 300);
            dibuje.drawString("Ranking", 344, 400);
            dibuje.setFont(new Font("SansSerif", Font.BOLD, 16));
            dibuje.drawString("Presione 1", 366, 225);
            dibuje.drawString("Presione 2", 366, 325);
            dibuje.drawString("Presione R", 365, 425);
        } else if (estado == ESTADO_JUEGO) {
            // Agrego un if por si se produce un error la cancha quedara negra (default)
            if (cancha != null) {
                cancha.mostrar(dibuje, getWidth(), getHeight());
            }
            pelota.mostrar(dibuje);
            paletaIzquierda.mostrar(dibuje);
            paletaDerecha.mostrar(dibuje);
            arcoIzquierdo.getMarcador().mostrar(dibuje);
            arcoDerecho.getMarcador().mostrar(dibuje);
            dibuje.setColor(Color.white);
            dibuje.setFont(new Font("SansSerif", Font.BOLD, 13));
            dibuje.drawString("Menu: Esq", 12, 600);
        } else if (estado == ESTADO_GANADOR) {
            dibuje.setColor(Color.white);
            dibuje.setFont(new Font("SansSerif", Font.BOLD, 25));
            dibuje.drawString("El ganador es: ", 200, 200);
            if (arcoIzquierdo.getMarcador().getPuntaje() == 10) {
                ganador = "Jugador 2";
            } else {
                ganador = "Jugador 1";
            }
            dibuje.drawString(ganador, 420, 200);
            dibuje.setFont(new Font("SansSerif", Font.BOLD, 16));
            dibuje.drawString("Presione la tecla Esq para volver al menú", 230, 550);
        }

    }
    public void gameShutdown() {
            Log.info(getClass().getSimpleName(), "Shutting down game");
            System.exit(0);
        }

        private void iniciarJuego2Jugadores() {
            estado = ESTADO_JUEGO;
            cancha = new Cancha();
            pelota = new Pelota(10, 400, 300, 250, 250);
            Keyboard teclado = this.getKeyboard();
            // Cargar propiedades desde el archivo default.properties
            String rutaArchivo = "defaultPong.properties";
            MenuConfig.cargarEnArchivo(appProperties, rutaArchivo);
            // Cargo las propiedades desde el archivo JGame
            String rutaArchivo2 = "jgame.properties";
            MenuConfig.cargarEnArchivo(appProperties2, rutaArchivo2);
            // Leer propiedades de teclas, cancha, pista musical
            try {
                // Leo propiedades
                // Seteo si la pantalla sera en ventana o completa
                /* 
                boolean pantallaCompleta = Boolean.parseBoolean(appProperties2.getProperty("fullScreen", "true"));
                boolean ventana = Boolean.parseBoolean(appProperties2.getProperty("fullScreen", "true")); // valor por defecto
                if (pantallaCompleta) {
                    appProperties2.setProperty("fullScreen", "true");
                    MenuConfig.guardarEnArchivo(appProperties2,rutaArchivo2);
                } else if (ventana) {
                    appProperties2.setProperty("fullScreen", "false");
                    MenuConfig.guardarEnArchivo(appProperties2,rutaArchivo2);
                }
                */
                String t1Arriba = appProperties.getProperty("movArriba1", "W");
                String t1Abajo = appProperties.getProperty("movAbajo1", "S");
                String t2Arriba = appProperties.getProperty("movArriba2", "\u2191");
                String t2Abajo = appProperties.getProperty("movAbajo2", "\u2193");
                // Seteo la cancha
                String estiloCancha = appProperties.getProperty("cancha", "Original");
                cancha.setEstilo(estiloCancha);
                // Seteo si quiero musica o no
                String pistaMusical = appProperties.getProperty("pistaMusical", "pong_cancion.wav");
                Musica.detenerMusicaFondo(); // Por si había una sonando
                boolean musicaActivada = Boolean.parseBoolean(appProperties.getProperty("musicaBox", "true"));
                if (musicaActivada) {
                    Musica.iniciarMusica(pistaMusical);
                }
                // Las convierto
                teclaArribaJ1 = conversorTecla.convertirTecla(t1Arriba);
                teclaAbajoJ1 = conversorTecla.convertirTecla(t1Abajo);
                teclaArribaJ2 = conversorTecla.convertirTecla(t2Arriba);
                teclaAbajoJ2 = conversorTecla.convertirTecla(t2Abajo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            paletaIzquierda = new Paleta(10, 90, 30, 270, teclado, teclaArribaJ1, teclaAbajoJ1);
            paletaDerecha = new Paleta(10, 90, 760, 270, teclado, teclaArribaJ2, teclaAbajoJ2);
            arcoIzquierdo = new Arco(0, true);
            arcoDerecho = new Arco(getWidth(), false);
            // Leo las propiedades y seteo estilos
            try {
                String estiloPelota = appProperties.getProperty("pelota", "Original");
                pelota.setEstilo(estiloPelota);
                String estiloPaleta = appProperties.getProperty("paleta", "Original");
                paletaIzquierda.setEstilo(estiloPaleta);
                paletaDerecha.setEstilo(estiloPaleta);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }    
}
