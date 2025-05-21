    package pong;

    import clasesCompartidas.Musica;
    import clasesCompartidas.Sonido;
    import clasesCompartidas.conversorTecla;

    import com.entropyinteractive.JGame;
    import com.entropyinteractive.Keyboard;
    import com.entropyinteractive.Log;
    import com.entropyinteractive.Mouse;
    import javax.swing.JFrame;

    import java.awt.*;
    import java.util.Properties;
    import java.util.Set;

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
        private Properties appProperties;
        private Properties appProperties2;
        private Cancha cancha;

        private static final double TIEMPO_ESPERA_MAXIMO = 2.0; // dos segundos de espera para volver a poner la pelota al
                                                                // medio
        private int estado;
        private static final int ESTADO_MENU = 0;
        private static final int ESTADO_JUEGO = 1;
        private static final int ESTADO_RANKING = 2;

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
                Mouse mouse = getMouse();
                if (mouse.isLeftButtonPressed()) {
                    int x = mouse.getX();
                    int y = mouse.getY();

                    if ((x >= 270 && x <= 480) && (y >= 245 && y <= 265)) { // Dos jugadores
                        iniciarJuego2Jugadores();
                    } else if ((x >= 315 && x <= 465) && (y >= 145 && y <= 170)) { // Un jugador
                        System.out.println("1 jugador");
                    } else if ((x >= 325 && x <= 445) && (y >= 345 && y <= 370)) { // Ranking
                        System.out.println("Ranking");
                    }
                }
                return; // se saltea si no esta en menú
            }

            if (esperandoReinicio) {
                tiempoEspera += delta;
                if (tiempoEspera >= TIEMPO_ESPERA_MAXIMO) {
                    pelota.reiniciarPelota();
                    esperandoReinicio = false;
                    tiempoEspera = 0;
                }
                return; // No hacer nada más mientras esperamos
            }

            paletaIzquierda.update(delta);
            paletaDerecha.update(delta);
            pelota.update(delta);
            // colisión con paleta izquierda
            if (pelota.colisiona(paletaIzquierda)) {
                Sonido.reproducir("golpe_audio.wav");
                pelota.setVelocidadX(Math.abs(pelota.getVelocidadX())); // Rebota a la derecha
            }
            // colisión con paleta derecha
            if (pelota.colisiona(paletaDerecha)) {
                Sonido.reproducir("golpe_audio.wav");
                pelota.setVelocidadX(-Math.abs(pelota.getVelocidadX())); // Rebota a la izquierda
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
            }
        }

        public void gameDraw(Graphics2D dibuje) {
            dibuje.setColor(Color.BLACK);
            dibuje.fillRect(0, 0, getWidth(), getHeight());

            if (estado == ESTADO_MENU) {
                dibuje.setColor(Color.WHITE);
                dibuje.setFont(new Font("SansSerif", Font.BOLD, 32));
                dibuje.drawString("1 Jugador", 320, 200);
                dibuje.drawString("2 Jugadores", 300, 300);
                dibuje.drawString("Ranking", 330, 400);
            } else if (estado == ESTADO_JUEGO) {
                // Agrego un if por si se produce un error la cancha quedara negra (default)
                if (cancha != null) {
                    cancha.mostrar(dibuje, getWidth(), getHeight());
                }
                pelota.mostrar(dibuje);
                paletaIzquierda.mostrar(dibuje);
                paletaDerecha.mostrar(dibuje);
                arcoIzquierdo.getMarcador().dibujar(dibuje);
                arcoDerecho.getMarcador().dibujar(dibuje);
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
            String rutaArchivo = "default.properties";
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
