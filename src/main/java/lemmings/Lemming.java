package lemmings;

import clasesCompartidas.Sonido;
import com.entropyinteractive.JGame;
import com.entropyinteractive.Keyboard;
import com.entropyinteractive.Mouse;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Iterator;


public class Lemming extends JGame {
    private Keyboard teclado = this.getKeyboard(); // Inicializa el teclado
    private int estado;
    private Bichito bichito;
    private Paracaidista paracaidista;
    private Bloqueador bloqueador;
    private Bloqueador bloqueador2;
    private Nivel nivel1,nivel2;
    //definicion de arreglo de lemmings
    private ArrayList<Bichito>arrBichito;
    private final int ESTADO_MENU = 0;
    private final int ESTADO_ELEGIR_MAPA = 1;
    private final int ESTADO_MAPA_1 = 2;
    private final int ESTADO_MAPA_2 = 3;
    private final int ESTADO_MAPA_3 = 4;
    private final int ESTADO_RANKING = 5;
    //variable para el tiempo de caida de lemming
    private double tiempoUltimoSpawn = 0;
    private final double tiempoIntervalo = 1.0; // cada segundo va a aparecer un lemming en el spawn
    private final int maxLemmingsNivel1 = 10;
    private int lemmingsGenerados = 0;
    private int bichitosRescatados = 0;

    private boolean mouseFuePresionado = false;
    private Bichito lemmingSeleccionado = null;

    private int xBoton;
    private int hudAlto;
    private int botonAncho;
    private int botonAlto;
    private int espacio;
    private int yBoton;
    private String texto;
    private FontMetrics metrics;
    private int textoAncho;
    private int xTexto;
    private int yTexto;
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
            estado = ESTADO_MENU;

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
            actualizarMovimientoLemmings();

            if (teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                estado = ESTADO_MENU;
            }
            if (teclado.isKeyPressed(KeyEvent.VK_4)) {
                int index = arrBichito.indexOf(lemmingSeleccionado);
                if (index != -1) {
                    Bichito original = arrBichito.get(index);
                    Paracaidista nuevo = new Paracaidista(original);
                    arrBichito.set(index, nuevo); // Reemplaza solo la referencia, misma posición en pantalla
                    lemmingSeleccionado = nuevo; // Actualiza también la referencia seleccionada
                }
            }

            return; // se saltea si no esta en mapa 1
        }

        if (estado == ESTADO_MAPA_2) {
            aparicionLemmings(nivel2,delta);
            salidaLemmings(nivel2);
            lemmingsMuertos(nivel2,delta);
            actualizarMovimientoLemmings();

            if (teclado.isKeyPressed(KeyEvent.VK_4)) {
                int index = arrBichito.indexOf(lemmingSeleccionado);
                if (index != -1) {
                    Bichito original = arrBichito.get(index);
                    Paracaidista nuevo = new Paracaidista(original);
                    arrBichito.set(index, nuevo); // Reemplaza solo la referencia, misma posición en pantalla
                    lemmingSeleccionado = nuevo; // Actualiza también la referencia seleccionada
                }
            }

            if (teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
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

        if (estado == ESTADO_MENU) {
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

            for (Bichito bichi : arrBichito) {
                if (bichi != null) {
                    bichi.mostrar(dibuje);
                }
            }
            if (lemmingSeleccionado != null) {
                dibuje.setColor(Color.YELLOW);
                dibuje.drawRect((int) lemmingSeleccionado.getX() - 2, (int) lemmingSeleccionado.getY() - 2,
                        lemmingSeleccionado.getAncho() + 4, lemmingSeleccionado.getAlto() + 4);

            }

        } else if (estado == ESTADO_MAPA_2) {
            // dibujar mapa 2
            nivel2.mostrar(dibuje);
            creadHUD(dibuje);
            for (Bichito bichi : arrBichito) {
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
        }

    }

    public void gameShutdown() {
    }

    private void jugarMapa1() {
        estado=ESTADO_MAPA_1;
        nivel1=new Nivel("mapa1.txt","estructurasSet.config");
        arrBichito=new ArrayList<>();
    }

    private void jugarMapa2() {
        estado = ESTADO_MAPA_2;
        nivel2=new Nivel("mapa2.txt","estructurasSet.config");
        arrBichito=new ArrayList<>();
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
            arrBichito.add(nuevoLemming);
            lemmingsGenerados++;
            tiempoUltimoSpawn = 0; // se reiniciara el temporizador
        }
    }

    private void dibujarLemmingsRescatados(Graphics2D g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif",Font.BOLD,16));
        g.drawString("Rescatados: "+bichitosRescatados,670,580);
    }

    private void salidaLemmings(Nivel nivel){
        Iterator<Bichito>iterator = arrBichito.iterator();
        while (iterator.hasNext()){
            Bichito bichi = iterator.next();
            //verificar si llegó a la meta
            if(bichi.detectarMeta(nivel)){
                Sonido.reproducir("yippee.wav");
                iterator.remove(); // se elimina del mapa
                bichitosRescatados++;
                continue; // se pasa al siguiente lemming
            }
        }
    }

    private void lemmingsMuertos(Nivel nivel,double delta){
        Iterator<Bichito> iterator = arrBichito.iterator();
        while (iterator.hasNext()) {
            Bichito bichi = iterator.next();

            // Eliminar lemmings muertos
            if (bichi.estaMuerto()) {
                iterator.remove();
                continue;
            }
            bichi.update(delta);
        }
    }

    private void actualizarMovimientoLemmings() {
        if(arrBichito != null) {
            for(Bichito bichi : arrBichito) {
                bichi.caminar();

                // Movimiento horizontal solo si no está cayendo
                if (!bichi.detectarCaida()) {
                    int direccion = bichi.estaMirandoDerecha() ? 1 : -1;
                    if (!bichi.detectarColisionMapa(direccion, 0)) {
                        bichi.moverX(direccion);
                    } else {
                        bichi.setDireccion(!bichi.estaMirandoDerecha());
                    }
                }
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
        dibujarLemmingsRescatados(dibuje);
    }

    private void seleccionarLemmingEn(int mouseX, int mouseY) {
        for (Bichito lemming : arrBichito) {
            double lx = lemming.getX();
            double ly = lemming.getY();
            int lw = lemming.getAncho();
            int lh = lemming.getAlto();
            int offset = 30; // o el valor que veas que lo corrige

            // Verificar si el mouse está dentro del área del lemming
            if (mouseX >= lx && mouseX <= lx + lw && mouseY >= ly - offset && mouseY <= ly + lh - offset) {
                lemmingSeleccionado = lemming;
                System.out.println("Lemming seleccionado: " + lemming);
                break; // Parar cuando encontró uno
            }
        }
    }

}