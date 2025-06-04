package lemmings;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import com.entropyinteractive.JGame;
import com.entropyinteractive.Keyboard;
import com.entropyinteractive.Mouse;

import clasesCompartidas.Sonido;


public class Lemming extends JGame {
    private Keyboard teclado = this.getKeyboard(); // Inicializa el teclado
    private int estado;
    private Nivel nivel1,nivel2;
    private ArrayList<Bichito> arrBichito;
    private ArrayList<Bichito> arrBichito2;
    private ArrayList<Bichito> lemmingsEnJuego;
    //definicion de arreglo de lemmings
    private ArrayList<Bichito> arrBichito1;
    private final int ESTADO_MENU = 0;
    private final int ESTADO_ELEGIR_MAPA = 1;
    private final int ESTADO_MAPA_1 = 2;
    private final int ESTADO_MAPA_2 = 3;
    private final int ESTADO_MAPA_3 = 4;
    private final int ESTADO_RANKING = 5;
    private final int ESTADO_NOMBRE_JUGADOR = 6;
    private final int ESTADO_GANADOR = 7;
    //variable para el tiempo de caida de lemming
    private double tiempoUltimoSpawn = 0;
    private final double tiempoIntervalo = 1.0; // cada segundo va a aparecer un lemming en el spawn
    private final int maxLemmingsNivel1 = 10;
    private int lemmingsGenerados = 0;
    private int bichitosRescatados = 0;
    //manejo de jugador y ranking
    private Jugador jugadorActual;
    private EstadoNombreJugador estadoNombreJugador;
    private EstadoRanking estadoRanking;
    private Temporizador temporizador;
    private Image imageEstadistica;
    private RankingManager baseDatos;

    private boolean mouseFuePresionado = false;
    private Bichito lemmingSeleccionado = null;
    private Bloqueador bloqueador;

    private int xBoton;
    private int hudAlto;
    private int botonAncho;
    private int botonAlto;
    private int espacio;
    private int yBoton;
    private String[] etiquetas = { "Bloqueador", "Escalador", "Cavador", "Paracaidista", "Velocidad X2","Destrucción" };
    private Image[] imagenesHabilidades = new Image[6];

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
            estado = ESTADO_NOMBRE_JUGADOR;
            estadoNombreJugador = new EstadoNombreJugador(this);
            estadoRanking = new EstadoRanking(this);
            temporizador = new Temporizador();
            imageEstadistica = ImageIO.read(getClass().getResource("/lemmings/estadisticas.png"));

            // Cargo las imagenes antes de entrar en el juego
            for (int i = 0; i < imagenesHabilidades.length; i++) {
                if (i > 3) {
                    imagenesHabilidades[i] = ImageIO.read(getClass().getResource("/lemmings/habilidad" + i + ".png"));
                } else {
                    imagenesHabilidades[i] = ImageIO.read(getClass().getResource("/lemmings/habilidad" + i + ".jpg"));
                }
            }
        } catch (Exception ex) {
            System.out.println("ERROR en gameStartup");
            ex.printStackTrace();
        }
    }

    public void gameUpdate(double delta) {
        if (estado == ESTADO_NOMBRE_JUGADOR) {
            estadoNombreJugador.actualizar();
            return;
        }

        if (estado == ESTADO_RANKING) {
            estadoRanking.actualizar();
            return;
        }

        if(estado == ESTADO_GANADOR){
            if (teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                estado = ESTADO_MENU;
            }
            return;
        }

        if (estado == ESTADO_MENU) {
            if (teclado.isKeyPressed(KeyEvent.VK_J)) {
                estado = ESTADO_ELEGIR_MAPA;
            }
            if (teclado.isKeyPressed(KeyEvent.VK_R)) {
                estado = ESTADO_RANKING;
            }
            return;
        }

        if (estado == ESTADO_ELEGIR_MAPA) {
            if (teclado.isKeyPressed(KeyEvent.VK_1)) {
                jugarMapa1();
            } else if (teclado.isKeyPressed(KeyEvent.VK_2)) {
                jugarMapa2();
            } else if (teclado.isKeyPressed(KeyEvent.VK_3)) {
                jugarMapa3();
            } else if (teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                estado = ESTADO_MENU;
            }
            return; // se saltea si no esta en menú
        }

        //Cargo el mouse
        Mouse mouse = this.getMouse();
        int mouseX = mouse.getX();
        int mouseY = mouse.getY();
        boolean clicked = mouse.isLeftButtonPressed();
        if (clicked && !mouseFuePresionado) {
            seleccionarLemmingEn(mouseX, mouseY);
        }
        mouseFuePresionado = clicked;


        if (estado == ESTADO_MAPA_1) {
            // actualizar mapa 1

            aparicionLemmings(nivel1,delta);
            salidaLemmings(nivel1);
            lemmingsMuertos(nivel1,delta);

            if (lemmingsEnJuego != null) {
                for (Bichito bichi : lemmingsEnJuego) {
                    bichi.update(delta); // Llama al update del tipo actual (caminar, paracaidista, etc.)
                }
            }
            if (teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                resetearLemmings();
                estado = ESTADO_MENU;
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
            if(lemmingsGenerados >= maxLemmingsNivel1 && lemmingsEnJuego.isEmpty()){
                jugadorActual.setLemmingsRescatados(bichitosRescatados);
                jugadorActual.setTiempoJuego(temporizador.detener());
                guardarPuntaje();
                estado = ESTADO_GANADOR;
            }

            return; // se saltea si no esta en mapa 1
        }

        if (estado == ESTADO_MAPA_2) {
    aparicionLemmings(nivel2, delta);
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

            if (teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                resetearLemmings();
                estado = ESTADO_MENU;
            }
            return; // se saltea si no esta en mapa 2
        }

        if (estado == ESTADO_MAPA_3) {
            // actualizar mapa 3
            return; // se saltea si no esta en mapa 3
        }
    }

    public void gameDraw(Graphics2D dibuje) {
        dibuje.setColor(Color.BLACK);
        dibuje.fillRect(0, 0, getWidth(), getHeight());
        if(estado == ESTADO_NOMBRE_JUGADOR){
            estadoNombreJugador.dibujar(dibuje);
        }else if (estado == ESTADO_MENU) {
            dibuje.setColor(Color.WHITE);
            dibuje.setFont(new Font("SansSerif", Font.BOLD, 32));
            dibuje.drawString("[J] Elegir mapa", 310, 200);
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
        } else if (estado == ESTADO_MAPA_1) {
            // Dibujar mapa 1
            nivel1.mostrar(dibuje);
            creadHUD(dibuje);

            for (Bichito bichi : lemmingsEnJuego) {
                if (bichi != null) {
                    bichi.mostrar(dibuje);
                }
            }
            if (lemmingSeleccionado != null && lemmingsEnJuego.contains(lemmingSeleccionado)) {
                dibuje.setColor(Color.YELLOW);
                dibuje.drawRect((int) lemmingSeleccionado.getX() - 2, (int) lemmingSeleccionado.getY() - 2,
                        lemmingSeleccionado.getAncho() + 4, lemmingSeleccionado.getAlto() + 4);

            }

        } else if (estado == ESTADO_MAPA_2) {
            // dibujar mapa 2
            nivel2.mostrar(dibuje);
            creadHUD(dibuje);
            for (Bichito bichi : lemmingsEnJuego) {
                if (bichi != null) {
                    bichi.mostrar(dibuje);
                }
            }
            if (lemmingSeleccionado != null) {
                dibuje.setColor(Color.YELLOW);
                dibuje.drawRect((int) lemmingSeleccionado.getX() - 2, (int) lemmingSeleccionado.getY() - 2,
                        lemmingSeleccionado.getAncho() + 4, lemmingSeleccionado.getAlto() + 4);

            }
        } else if (estado == ESTADO_MAPA_3) {
            // dibujar mapa 3
        } else if (estado == ESTADO_RANKING){
            estadoRanking.dibujar(dibuje);
        }else if (estado == ESTADO_GANADOR){
            dibuje.setColor(Color.WHITE);
            dibuje.drawImage(imageEstadistica, 100, 30,null);
            dibuje.setFont(new Font("SansSerif", Font.BOLD, 18));
            dibuje.drawString("Jugador: ", 200, 200);
            dibuje.drawString(jugadorActual.getNombre(), 250, 200);
            dibuje.drawString("Lemmings rescatados: ",200,300);
            dibuje.drawString(""+jugadorActual.getLemmingsRescatados(),370,300);
            dibuje.drawString("Tiempo: ", 200, 400);
            dibuje.drawString(jugadorActual.getTiempoJuegoFormateado(), 250, 400);
            dibuje.drawString("Fecha: ", 200, 500);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fechaFormateada = jugadorActual.getFechaPartida().format(dateFormatter);
            dibuje.drawString(fechaFormateada, 250, 500);

        }

    }

    public void gameShutdown() {
    }

    private void jugarMapa1() {
        resetearLemmings();
        estado = ESTADO_MAPA_1;
        nivel1 = new Nivel("mapa1.txt", "estructurasSet.config");
        arrBichito = new ArrayList<>();
        lemmingsEnJuego = arrBichito;
        temporizador.reiniciar(); // Reinicia antes de iniciar
        temporizador.iniciar(); // Comienza a contar
        bichitosRescatados = 0;
    }

    private void jugarMapa2() {
        resetearLemmings();
        estado = ESTADO_MAPA_2;
        nivel2 = new Nivel("mapa2.txt", "estructurasSet.config");
        arrBichito2 = new ArrayList<>();
        lemmingsEnJuego = arrBichito2;
        temporizador.reiniciar(); // Reinicia antes de iniciar
        temporizador.iniciar();   // Comienza a contar
        bichitosRescatados = 0;
    }


    private void jugarMapa3() {

    }

    private void aparicionLemmings(Nivel nivel,double delta){
        tiempoUltimoSpawn += delta;
        //spawnear nuevo lemming
        if(lemmingsGenerados < maxLemmingsNivel1 && tiempoUltimoSpawn >= tiempoIntervalo && nivel.getSpawnX()!=-1 && nivel.getSpawnY()!=-1){
            Bichito nuevoLemming = new Bichito();
            nuevoLemming.setPosicion(nivel.getSpawnX(),nivel.getSpawnY());
            nuevoLemming.setNivel(nivel);
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

    private void creadHUD(Graphics2D dibuje) {
        // Dibujar fondo del HUD con degradado gris oscuro a negro
        hudAlto = 100; // fijo
        GradientPaint gradiente = new GradientPaint(0, getHeight() - hudAlto, new Color(40, 40, 40), 0, getHeight(),
                Color.BLACK);
        dibuje.setPaint(gradiente);
        dibuje.fillRect(0, getHeight() - hudAlto, getWidth(), hudAlto);
        // Dibujar borde blanco alrededor del HUD
        dibuje.setColor(Color.WHITE);
        dibuje.setStroke(new BasicStroke(2));
        dibuje.drawRect(0, getHeight() - hudAlto, getWidth() - 1, hudAlto - 1);

        // Dimensiones de botones e imágenes
        botonAncho = 50;
        botonAlto = 50;
        espacio = 30;
        yBoton = getHeight() - hudAlto + 5; // margen superior
        Font fuenteTexto = new Font("SansSerif", Font.BOLD, 11);
        dibuje.setFont(fuenteTexto);
        FontMetrics metrics = dibuje.getFontMetrics(fuenteTexto);

        for (int i = 0; i < 6; i++) {
            xBoton = 50 + i * (botonAncho + espacio);

            // Imagen (centrada arriba)
            dibuje.drawImage(imagenesHabilidades[i], xBoton, yBoton, botonAncho, botonAlto, null);

            // Coordenadas para los textos (más abajo)
            int yTextoBase = yBoton + botonAlto + 10;

            // Texto de número [n] con sombra
            String tecla = "[" + (i + 1) + "]";
            int anchoTecla = metrics.stringWidth(tecla);
            int xTecla = xBoton + (botonAncho - anchoTecla) / 2;

            dibuje.setColor(Color.BLACK);
            dibuje.drawString(tecla, xTecla + 1, yTextoBase + 1);
            dibuje.setColor(Color.WHITE);
            dibuje.drawString(tecla, xTecla, yTextoBase);

            // Texto de habilidad con sombra, debajo del número
            String texto = etiquetas[i];
            int textoAncho = metrics.stringWidth(texto);
            int xTexto = xBoton + (botonAncho - textoAncho) / 2;
            int yTexto = yTextoBase + 12;

            dibuje.setColor(Color.BLACK);
            dibuje.drawString(texto, xTexto + 1, yTexto + 1);
            dibuje.setColor(Color.WHITE);
            dibuje.drawString(texto, xTexto, yTexto);
        }
        dibuje.setFont(new Font("SansSerif", Font.BOLD, 18));
        dibuje.drawString("Rescatados: "+bichitosRescatados,610,585);
        dibuje.drawString("Tiempo: "+temporizador.getTiempoFormateado(),610,550);
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

    //metodos para ranking
    public void guardarPuntaje() {
        if (jugadorActual != null) {
            RankingManager manager = new RankingManager();
            manager.guardarJugador(jugadorActual);
        }
    }
    public void setJugadorActual(Jugador jugador) {
        this.jugadorActual = jugador;
    }
    public void cambiarEstado(int nuevoEstado) {
        this.estado = nuevoEstado;

        // Si vamos al ranking, actualizar la lista
        if (nuevoEstado == ESTADO_RANKING) {
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
    }
}