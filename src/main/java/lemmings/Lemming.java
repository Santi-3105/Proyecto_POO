package lemmings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import com.entropyinteractive.JGame;
import com.entropyinteractive.Keyboard;
import com.entropyinteractive.Mouse;

import clasesCompartidas.Sonido;

public class Lemming extends JGame {
    private Keyboard teclado = this.getKeyboard(); // Inicializa el teclado
    private Nivel nivel1, nivel2, nivel3, nivelJugando;
    private ArrayList<Bichito> arrBichito,arrBichito2,arrBichito3,lemmingsEnJuego;
    private boolean nukeActivado = false;
    public EstadoJuego estado;
    public EstadoJuego estadoAnterior;
    public enum EstadoJuego {MENU, ELEGIR_MAPA, MAPA_1, MAPA_2, MAPA_3, RANKING, NOMBRE_JUGADOR, GANADOR, PAUSA, PERDEDOR}

    // variable para el tiempo de caida de lemming
    private double tiempoUltimoSpawn = 0;
    private final double tiempoIntervalo = 1.5; // cada segundo va a aparecer un lemming en el spawn
    private final int maxLemmingsNivel1 = 10;
    private final int maxLemmingsNivel2 = 15;
    private final int maxLemmingsNivel3 = 20;
    private int lemmingsGenerados = 0;
    private int bichitosRescatados = 0;
    // manejo de jugador y ranking
    private RankingManager manager;
    private Jugador jugadorActual;
    private EstadoNombreJugador estadoNombreJugador;
    private EstadoRanking estadoRanking;
    private Temporizador temporizador;
    // Manejo de mouse
    private boolean mouseFuePresionado = false;
    private Bichito lemmingSeleccionado = null;
    // Bloqueador que sera pasado por parametro y paracaidista generado
    private Bloqueador bloqueador;
    private Paracaidista nuevo;
    // Manejo de velocidades
    private boolean velocidadDobleActiva = false;
    private boolean tecla5PresionadaAnteriormente = false;
    private boolean tecla5PresionadaAhora;
    private boolean velocidadRapidaActiva = false;
    EstadoDibujar estadoClase;

    public static void main(String[] args) {
        Lemming game = new Lemming("Lemmings", 800, 600);
        game.run(1.0 / 60.0);
        System.exit(0);
    }

    public Lemming(String title, int width, int height) {
        super(title, width, height);
    }

    public void gameStartup() {
        try {
            estado = EstadoJuego.NOMBRE_JUGADOR;
            estadoNombreJugador = new EstadoNombreJugador(this);
            estadoRanking = new EstadoRanking(this);
            temporizador = new Temporizador();
            manager = new RankingManager();
            estadoClase = new EstadoDibujar(this);
        } catch (Exception ex) {
            System.out.println("ERROR en gameStartup");
            ex.printStackTrace();
        }
    }

    public void gameUpdate(double delta) {
        temporizador.update(delta);
        if (estado == EstadoJuego.NOMBRE_JUGADOR) {
            estadoNombreJugador.actualizar();
            return;
        }

        if (estado == EstadoJuego.PAUSA) {
            for (KeyEvent event : teclado.getEvents()) {
                if (event.getID() == KeyEvent.KEY_PRESSED && event.getKeyCode() == KeyEvent.VK_P) {
                    estado = estadoAnterior;
                    temporizador.renaudar(temporizador.getTiempoEnSegundos());
                    temporizador.iniciar();
                }
            }
        }

        if (estado == EstadoJuego.RANKING) {estadoRanking.actualizar();}

        if (estado == EstadoJuego.GANADOR) {salir(EstadoJuego.MENU);}

        if (estado == EstadoJuego.PERDEDOR) {salir(EstadoJuego.MENU);}

        if (estado == EstadoJuego.MENU) {
            if (teclado.isKeyPressed(KeyEvent.VK_J)) {
                estado = EstadoJuego.ELEGIR_MAPA;
            }
            if (teclado.isKeyPressed(KeyEvent.VK_R)) {
                estado = EstadoJuego.RANKING;
            }
        }

        if (estado == EstadoJuego.ELEGIR_MAPA) {
            if (teclado.isKeyPressed(KeyEvent.VK_1)) {
                jugarMapa1();
            } else if (teclado.isKeyPressed(KeyEvent.VK_2)) {
                jugarMapa2();
            } else if (teclado.isKeyPressed(KeyEvent.VK_3)) {
                jugarMapa3();
            } else if (teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                estado = EstadoJuego.MENU;
            }
            return; // se saltea si no esta en menú
        }

        // Cargo el mouse
        Mouse mouse = this.getMouse();
        int mouseX = mouse.getX();
        int mouseY = mouse.getY();
        boolean clicked = mouse.isLeftButtonPressed();
        if (clicked && !mouseFuePresionado) {
            seleccionarLemmingEn(mouseX, mouseY);
        }
        mouseFuePresionado = clicked;

        if (estado == EstadoJuego.MAPA_1) {
            pausar(nivel1, EstadoJuego.MAPA_1);
            aparicionLemmings(nivel1, delta, maxLemmingsNivel1);
            salidaLemmings(nivel1);
            lemmingsMuertos(nivel1, delta);

            if (lemmingsEnJuego != null) {
                for (Bichito bichi : lemmingsEnJuego) {
                    bichi.update(delta); // Llama al update del tipo actual (caminar, paracaidista, etc.)
                }
            }
            if (teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                resetearLemmings();
                estado = EstadoJuego.MENU;
            }
            if (teclado.isKeyPressed(KeyEvent.VK_4)) {
                int index = lemmingsEnJuego.indexOf(lemmingSeleccionado);
                if (index != -1) {
                    Bichito original = lemmingsEnJuego.get(index);
                    nuevo = new Paracaidista(original);
                    lemmingsEnJuego.set(index, nuevo); // Reemplaza solo la referencia, misma posición en pantalla
                    lemmingSeleccionado = nuevo; // Actualiza también la referencia seleccionada
                }
            }

            if (teclado.isKeyPressed(KeyEvent.VK_6) && !nukeActivado) {
                nukeActivado = true;
                for (int i = 0; i < lemmingsEnJuego.size(); i++) {
                    Bichito original = lemmingsEnJuego.get(i);
                    if (!original.estaMuerto()) {
                        Bichito nukeado = new Nuke(original);
                        lemmingsEnJuego.set(i, nukeado);
                        if (lemmingSeleccionado == original) {
                            lemmingSeleccionado = nukeado; //QUE SE MUERA EL BLOQUEADOR TAMBIEN
                        }
                    }
                }
            }
            if (!teclado.isKeyPressed(KeyEvent.VK_6)) {
                nukeActivado = false; // se libera cuando soltás la tecla
            }
            velocidadx2();
            detenerJuego(maxLemmingsNivel1, 1,7,35); //7 max para ganar

            return; // se saltea si no esta en mapa 1
        }

        if (estado == EstadoJuego.MAPA_2) {
            pausar(nivel2, EstadoJuego.MAPA_2);
            aparicionLemmings(nivel2, delta, maxLemmingsNivel2);
            salidaLemmings(nivel2);
            lemmingsMuertos(nivel2, delta);

            if (lemmingsEnJuego != null) {
                for (Bichito bichi : lemmingsEnJuego) {
                    // Primero verificar colisión ANTES de actualizar
                    boolean bloqueado = false;
                    if (bloqueador != null && !(bichi instanceof Bloqueador)) {
                        // Calcular posición futura para prevenir la colisión
                        int direccion = bichi.estaMirandoDerecha() ? 1 : -1;
                        double futuraX = bichi.getX() + direccion;
                        double futuraY = bichi.getY();

                        // Crear rectángulo temporal para la posición futura
                        Rectangle rectFuturo = new Rectangle((int) futuraX, (int) futuraY,
                                bichi.getAncho(), bichi.getAlto());
                        Rectangle rectBloqueador = new Rectangle((int) bloqueador.getX(),
                                (int) bloqueador.getY(),
                                bloqueador.getAncho(),
                                bloqueador.getAlto());

                        if (rectFuturo.intersects(rectBloqueador)) {
                            bichi.setDireccion(!bichi.estaMirandoDerecha());
                            bloqueado = true;
                        }
                    }

                    // Solo actualizar si no está bloqueado o si es el propio bloqueador
                    if (!bloqueado || bichi instanceof Bloqueador) {
                        bichi.update(delta);
                    }
                }
            }
            if (teclado.isKeyPressed(KeyEvent.VK_1)) {
                int index = lemmingsEnJuego.indexOf(lemmingSeleccionado);
                if (index != -1) {
                    Bichito original = lemmingsEnJuego.get(index);
                    bloqueador = new Bloqueador(original);
                    lemmingsEnJuego.set(index, bloqueador); // Reemplaza solo la referencia, misma posición en pantalla
                    lemmingSeleccionado = bloqueador; // Actualiza también la referencia seleccionada
                }
            }
            if (teclado.isKeyPressed(KeyEvent.VK_4)) {
                int index = lemmingsEnJuego.indexOf(lemmingSeleccionado);
                if (index != -1) {
                    Bichito original = lemmingsEnJuego.get(index);
                    Paracaidista nuevo = new Paracaidista(original);
                    lemmingsEnJuego.set(index, nuevo); // Reemplaza solo la referencia, misma posición en pantalla
                    lemmingSeleccionado = nuevo; // Actualiza también la referencia seleccionada
                }
            }
            if (teclado.isKeyPressed(KeyEvent.VK_6) && !nukeActivado) {
                nukeActivado = true;
                for (int i = 0; i < lemmingsEnJuego.size(); i++) {
                    Bichito original = lemmingsEnJuego.get(i);
                    if (!original.estaMuerto()) {
                        Bichito nukeado = new Nuke(original);
                        lemmingsEnJuego.set(i, nukeado);
                        if (lemmingSeleccionado == original) {
                            lemmingSeleccionado = nukeado;
                        }
                    }
                }
            }
            if (!teclado.isKeyPressed(KeyEvent.VK_6)) {
                nukeActivado = false; // se libera cuando soltás la tecla
            }

            velocidadx2();

            if (teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                resetearLemmings();
                estado = EstadoJuego.MENU;
            }

            detenerJuego(maxLemmingsNivel2, 2,10,60); //10 max para ganar
            return; // se saltea si no esta en mapa 2
        }

        if (estado == EstadoJuego.MAPA_3) {
            pausar(nivel3, EstadoJuego.MAPA_3);
            aparicionLemmings(nivel3, delta, maxLemmingsNivel3);
            salidaLemmings(nivel3);
            lemmingsMuertos(nivel3, delta);
            if (lemmingsEnJuego != null) {
                for (Bichito bichi : lemmingsEnJuego) {
                    bichi.update(delta); // Llama al update del tipo actual (caminar, paracaidista, etc.)
                }
                if (teclado.isKeyPressed(KeyEvent.VK_3)) {
                    int index = lemmingsEnJuego.indexOf(lemmingSeleccionado);
                    if (index != -1) {
                        Bichito original = lemmingsEnJuego.get(index);
                        Cavador cavador = new Cavador(original);
                        lemmingsEnJuego.set(index, cavador);
                        lemmingSeleccionado = cavador;
                    }
                }
                if (teclado.isKeyPressed(KeyEvent.VK_2)) {
                    int index = lemmingsEnJuego.indexOf(lemmingSeleccionado);
                    if (index != -1) {
                        Bichito original = lemmingsEnJuego.get(index);
                        Escalador escalador = new Escalador(original);
                        lemmingsEnJuego.set(index, escalador);
                        lemmingSeleccionado = escalador;
                    }
                }
            }
            if (teclado.isKeyPressed(KeyEvent.VK_6) && !nukeActivado) {
                nukeActivado = true;
                for (int i = 0; i < lemmingsEnJuego.size(); i++) {
                    Bichito original = lemmingsEnJuego.get(i);
                    if (!original.estaMuerto()) {
                        Bichito nukeado = new Nuke(original);
                        lemmingsEnJuego.set(i, nukeado);
                        if (lemmingSeleccionado == original) {
                            lemmingSeleccionado = nukeado;
                        }
                    }
                }
            }
            if (!teclado.isKeyPressed(KeyEvent.VK_6)) {
                nukeActivado = false; // se libera cuando soltás la tecla
            }


            if (teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                resetearLemmings();
                estado = EstadoJuego.MENU;
            }
            velocidadx2();
            detenerJuego(maxLemmingsNivel3, 3,15,120); //15 max para ganar
            return; // se saltea si no esta en mapa 3
        }
    }

    public void gameDraw(Graphics2D dibuje) {
        dibuje.setColor(Color.BLACK);
        dibuje.fillRect(0, 0, getWidth(), getHeight());
        if (estado == EstadoJuego.NOMBRE_JUGADOR) {
            estadoNombreJugador.dibujar(dibuje);
        } else if (estado == EstadoJuego.MENU) {
            estadoClase.dibujarMenu(dibuje);
        } else if (estado == EstadoJuego.ELEGIR_MAPA) {
            estadoClase.dibujarEstadoMapa(dibuje);
        } else if (estado == EstadoJuego.MAPA_1) {
            estadoClase.dibujarNivel(dibuje, nivel1, lemmingsEnJuego, lemmingSeleccionado, bichitosRescatados,
                    temporizador);
        } else if (estado == EstadoJuego.MAPA_2) {
            estadoClase.dibujarNivel(dibuje, nivel2, lemmingsEnJuego, lemmingSeleccionado, bichitosRescatados,
                    temporizador);
        } else if (estado == EstadoJuego.MAPA_3) {
            estadoClase.dibujarNivel(dibuje, nivel3, lemmingsEnJuego, lemmingSeleccionado, bichitosRescatados,
                    temporizador);
        } else if (estado == EstadoJuego.RANKING) {
            estadoRanking.dibujar(dibuje);
        } else if (estado == EstadoJuego.GANADOR) {
            estadoClase.dibujarGanador(dibuje, jugadorActual);
        } else if (estado == EstadoJuego.PAUSA) {
            estadoClase.dibujarNivel(dibuje, nivelJugando, lemmingsEnJuego, lemmingSeleccionado, bichitosRescatados,
                    temporizador);
        }else if(estado == EstadoJuego.PERDEDOR){
            estadoClase.dibujarPerdedor(dibuje);
        }

    }

    public void gameShutdown() {
    }

    private void jugarMapa1() {
        Sonido.reproducir("letsgo.wav");
        resetearLemmings();
        estado = EstadoJuego.MAPA_1;
        nivel1 = new Nivel("mapa1.txt", "estructurasSet.config");
        arrBichito = new ArrayList<>();
        lemmingsEnJuego = arrBichito;
        temporizador.reiniciar(); // Reinicia antes de iniciar
        temporizador.iniciar(); // Comienza a contar
        bichitosRescatados = 0;
    }

    private void jugarMapa2() {
        Sonido.reproducir("letsgo.wav");
        resetearLemmings();
        estado = EstadoJuego.MAPA_2;
        nivel2 = new Nivel("mapa2.txt", "estructurasSet.config");
        arrBichito2 = new ArrayList<>();
        lemmingsEnJuego = arrBichito2;
        temporizador.reiniciar(); // Reinicia antes de iniciar
        temporizador.iniciar(); // Comienza a contar
        bichitosRescatados = 0;
    }

    private void jugarMapa3() {
        Sonido.reproducir("letsgo.wav");
        resetearLemmings();
        estado = EstadoJuego.MAPA_3;
        nivel3 = new Nivel("mapa3.txt", "estructurasSet.config");
        arrBichito3 = new ArrayList<>();
        lemmingsEnJuego = arrBichito3;
        temporizador.reiniciar(); // Reinicia antes de iniciar
        temporizador.iniciar(); // Comienza a contar
        bichitosRescatados = 0;
    }

    private void pausar(Nivel nivel, EstadoJuego ESTADO_MAPA) {
        for (KeyEvent event : teclado.getEvents()) {
            if (event.getID() == KeyEvent.KEY_PRESSED && event.getKeyCode() == KeyEvent.VK_P) {
                estado = EstadoJuego.PAUSA;
                temporizador.detener();
                estadoAnterior = ESTADO_MAPA;
                nivelJugando = nivel;
            }
        }
    }
    private void salir(EstadoJuego estado){
        for (KeyEvent event : teclado.getEvents()) {
            if (event.getID() == KeyEvent.KEY_PRESSED && event.getKeyCode() == KeyEvent.VK_ESCAPE) {
               this.estado = estado;
            }
        }
    }

    private void aparicionLemmings(Nivel nivel, double delta, int maxLemmingsNivel) {
        tiempoUltimoSpawn += delta;
        // spawnear nuevo lemming
        if (lemmingsGenerados < maxLemmingsNivel && tiempoUltimoSpawn >= tiempoIntervalo && nivel.getSpawnX() != -1
                && nivel.getSpawnY() != -1) {
            Bichito nuevoLemming = new Bichito();
            nuevoLemming.setPosicion(nivel.getSpawnX(), nivel.getSpawnY());
            nuevoLemming.setNivel(nivel);
            if (velocidadRapidaActiva) {
                nuevoLemming.setVelocidadX2(true);
            }
            lemmingsEnJuego.add(nuevoLemming);
            lemmingsGenerados++;
            tiempoUltimoSpawn = 0; // se reiniciara el temporizador
        }
    }

    private void salidaLemmings(Nivel nivel) {
        Iterator<Bichito> iterator = lemmingsEnJuego.iterator();
        while (iterator.hasNext()) {
            Bichito bichi = iterator.next();
            // verificar si llegó a la meta
            if (bichi.detectarMeta(nivel)) {
                Sonido.reproducir("yippee.wav");
                iterator.remove(); // se elimina del mapa
                bichitosRescatados++;
                continue; // se pasa al siguiente lemming
            }
        }
    }

    private void lemmingsMuertos(Nivel nivel, double delta) {
        Iterator<Bichito> iterator = lemmingsEnJuego.iterator();
        while (iterator.hasNext()) {
            Bichito bichi = iterator.next();

            // Eliminar lemmings muertos
            if (bichi.estaMuerto()) {
                iterator.remove();
                continue;
            }
        }
    }

    private void seleccionarLemmingEn(int mouseX, int mouseY) {
        for (Bichito lemming : lemmingsEnJuego) {
            double lx = lemming.getX();
            double ly = lemming.getY();
            int lw = lemming.getAncho();
            int lh = lemming.getAlto();
            int offset = 30; // o el valor que veas que lo corrige

            // Verificar si el mouse está dentro del área del lemming
            if (mouseX >= lx && mouseX <= lx + lw && mouseY >= ly - offset && mouseY <= ly + lh - offset) {
                lemmingSeleccionado = lemming;
                break; // Parar cuando encontró uno
            }
        }
    }

    public void setJugadorActual(Jugador jugador) {
        this.jugadorActual = jugador;
    }

    public void cambiarEstado(EstadoJuego nuevoEstado) {
        estado = nuevoEstado;

        // Si vamos al ranking, actualizar la lista
        if (nuevoEstado == EstadoJuego.RANKING) {
            estadoRanking = new EstadoRanking(this);
        }
    }

    private void resetearLemmings() {
        if (lemmingsEnJuego != null) {
            lemmingsEnJuego.clear();
        }
        lemmingSeleccionado = null;
        bloqueador = null;
        lemmingsGenerados = 0;
        tiempoUltimoSpawn = 0;
        bichitosRescatados = 0;
        // Reseteo las velocidades
        velocidadDobleActiva = false;
        tecla5PresionadaAnteriormente = false;
        velocidadRapidaActiva = false;
        Bichito.multiplicadorVelocidad = 1.0;
        temporizador.reiniciar();
        nukeActivado = false;
    }

    private void detenerJuego(int maxLemmingsNivel, int nroNivel,int maxGanar, double tiempoMaximo) {
        if (lemmingsGenerados >= maxLemmingsNivel && lemmingsEnJuego.isEmpty() && bichitosRescatados>=maxGanar && temporizador.getTiempoEnSegundos()<=tiempoMaximo) {
            // Nuevo
            temporizador.detener();
            jugadorActual.setLemmingsRescatados(bichitosRescatados);
            jugadorActual.setTiempoJuego((long) temporizador.getTiempoEnSegundos());
            jugadorActual.setNivel(nroNivel);
            manager.guardarJugador(jugadorActual);
            estadoRanking = new EstadoRanking(this);
            estado = EstadoJuego.GANADOR;
        }
        if(lemmingsGenerados>= maxLemmingsNivel && lemmingsEnJuego.isEmpty() && bichitosRescatados<maxGanar && temporizador.getTiempoEnSegundos()>tiempoMaximo){
            temporizador.detener();
            estado = EstadoJuego.PERDEDOR;
        }
    }

    private void velocidadx2() {
        for (KeyEvent event : teclado.getEvents()) {
            if (event.getID() == KeyEvent.KEY_TYPED) {
                char c = event.getKeyChar();
                if (c == '5') {
                    velocidadRapidaActiva = !velocidadRapidaActiva;
                    temporizador.setMultiplicadorVelocidad(velocidadRapidaActiva ? 1.3 : 1.0);
                    for (Bichito b : lemmingsEnJuego) {
                        b.setVelocidadX2(velocidadRapidaActiva);
                    }
                }
            }
        }
    }
}