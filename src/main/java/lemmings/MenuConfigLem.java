package lemmings;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class MenuConfigLem implements ActionListener {
    private JRadioButton ventana;
    private JRadioButton pantallaCompleta;
    private JCheckBox musicaBox;
    private JCheckBox efectoSonidoBox;
    private JTextField bEfectoSonido;
    private JTextField bMusica;
    private JTextField pausar;
    private JTextField habilidad1;
    private JTextField habilidad2;
    private JTextField habilidad3;
    private JTextField habilidad4;
    private JTextField habilidad5;
    private JTextField habilidad6;
    private JTextField habilidad7;
    private JTextField habilidad8;
    private JTextField acelerarJuego;
    private JTextField autodestruccion;
    private JTextField iniciarJuego;
    private JComboBox<String> pistaMusical;
    private JComboBox<String> skin;
    private JButton guardar;
    private JButton reset;
    protected JPanel config;
    protected JPanel panelConfigBot;
    protected JFrame frame;
    protected JPanel panelCompleto;
    protected String archivoConfig = "defaultLemmings.properties";
    protected Properties defaultProps;
    protected Map<String, JComponent> componentes;
    protected String rutaArchivo;
    protected JComponent[] componentsConfg;

    public MenuConfigLem() {

        frame = new JFrame();
        frame.setTitle("Configuracion");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Crear el panel principal del menú de configuración
        // Crear el panel principal del menú de configuración con fondo de imagen
        panelCompleto = new JPanel(new BorderLayout()) {
            Image lemmingsFondo = new ImageIcon(getClass().getResource("/lemmings/confgFondo2.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (lemmingsFondo != null) {
                    g.drawImage(lemmingsFondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.out.println("No se pudo cargar la imagen de fondo.");
                }
            }
        };
        // Config botones
        // Config botones
        ventana = new JRadioButton("Ventana", true);
        pantallaCompleta = new JRadioButton("Pantalla completa", false);
        ButtonGroup grupoVentana = new ButtonGroup();
        grupoVentana.add(ventana);
        grupoVentana.add(pantallaCompleta);
        musicaBox = new JCheckBox("", true);
        musicaBox.addActionListener(this);
        efectoSonidoBox = new JCheckBox("", true);
        efectoSonidoBox.addActionListener(this);
        bEfectoSonido = new JTextField("Q", 5);
        bMusica = new JTextField("W", 5);
        pausar = new JTextField("Space", 5);
        habilidad1 = new JTextField("1", 5);
        habilidad2 = new JTextField("2", 5);
        habilidad3 = new JTextField("3", 5);
        habilidad4 = new JTextField("4", 5);
        habilidad5 = new JTextField("5", 5);
        habilidad6 = new JTextField("6", 5);
        habilidad7 = new JTextField("7", 5);
        habilidad8 = new JTextField("8", 5);
        acelerarJuego = new JTextField("E", 5);
        autodestruccion = new JTextField("R", 5);
        iniciarJuego = new JTextField("Enter", 5);
        pistaMusical = new JComboBox<>(new String[] { "Original", "V2", "V3" });
        skin = new JComboBox<>(new String[] { "Original", "V2", "V3" });
        reset = new JButton("Restablecer");
        reset.addActionListener(this);
        reset.setVisible(true);
        guardar = new JButton("Guardar");
        guardar.addActionListener(this);
        guardar.setVisible(true);
        defaultProps = new Properties();
        componentes = new HashMap<>();

        // JComponent el cual engloba todo
        // Cargo en un mapa las key y los valores
        // JComponent el cual engloba todo
        // JRadioButtons
        componentes.put("ventana", ventana);
        componentes.put("pantallaCompleta", pantallaCompleta);

        // JCheckBoxes
        componentes.put("musicaBox", musicaBox);
        componentes.put("efectoSonidoBox", efectoSonidoBox);

        // JTextFields
        componentes.put("bEfectoSonido", bEfectoSonido);
        componentes.put("bMusica", bMusica);
        componentes.put("pausar", pausar);
        componentes.put("habilidad1", habilidad1);
        componentes.put("habilidad2", habilidad2);
        componentes.put("habilidad3", habilidad3);
        componentes.put("habilidad4", habilidad4);
        componentes.put("habilidad5", habilidad5);
        componentes.put("habilidad6", habilidad6);
        componentes.put("habilidad7", habilidad7);
        componentes.put("habilidad8", habilidad8);
        componentes.put("acelerarJuego", acelerarJuego);
        componentes.put("autodestruccion", autodestruccion);
        componentes.put("iniciarJuego", iniciarJuego);

        // JComboBox
        componentes.put("pistaMusical", pistaMusical);
        componentes.put("skin", skin);

        // Iniciara el mapa (cargarConfig) con los datos que le pase el archivo
        // (cargarEnArchivo)

        cargarEnArchivo(defaultProps, archivoConfig);
        cargarConfiguracion(componentes, defaultProps);

        // Creo el menu configuracion
        // Añado sus botones
        // Config panel
        config = new JPanel(new GridBagLayout());
        config.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(5, 5, 5, 5);
        config.add(new JLabel("Pantalla: "), g);
        g.gridx++;
        config.add(ventana, g);
        g.gridx++;
        config.add(pantallaCompleta, g);
        g.gridx = 0;
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Sonido: "), g);
        g.gridx++;
        config.add(musicaBox, g);
        g.gridx++;
        config.add(new JLabel("Efectos de sonido: "), g);
        g.gridx++;
        config.add(efectoSonidoBox, g);
        g.gridx = 0;
        g.gridy++;
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Atajos: "), g);
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Activar/Desactivar efectos de sonido: "), g);
        g.gridx++;
        config.add(bEfectoSonido, g);
        g.gridx++;
        config.add(new JLabel("Activar/Desactivar Musica: "), g);
        g.gridx++;
        config.add(bMusica, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Iniciar juego: "), g);
        g.gridx++;
        config.add(iniciarJuego, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Pausar/Reanudar juego: "), g);
        g.gridx++;
        config.add(pausar, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Habilidades: "), g);
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Habilidad 1: "), g);
        g.gridx++;
        config.add(habilidad1, g);
        g.gridx++;
        config.add(new JLabel("Habilidad 2: "), g);
        g.gridx++;
        config.add(habilidad2, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Habilidad 3: "), g);
        g.gridx++;
        config.add(habilidad3, g);
        g.gridx++;
        config.add(new JLabel("Habilidad 4: "), g);
        g.gridx++;
        config.add(habilidad4, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Habilidad 5: "), g);
        g.gridx++;
        config.add(habilidad5, g);
        g.gridx++;
        config.add(new JLabel("Habilidad 6: "), g);
        g.gridx++;
        config.add(habilidad6, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Habilidad 7: "), g);
        g.gridx++;
        config.add(habilidad7, g);
        g.gridx++;
        config.add(new JLabel("Habilidad 8: "), g);
        g.gridx++;
        config.add(habilidad8, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Otras teclas y configuraciones: "), g);
        g.gridy++;
        g.gridy++;
        config.add(new JLabel("Acelerar juego: "), g);
        g.gridx++;
        config.add(acelerarJuego, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Autodestruccion: "), g);
        g.gridx++;
        config.add(autodestruccion, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Elegir pista musical: "), g);
        g.gridx++;
        config.add(pistaMusical, g);
        g.gridx = 0;
        g.gridy++;
        config.add(new JLabel("Elegir skin: "), g);
        g.gridx++;
        config.add(skin, g);

        // Config panel botones
        panelConfigBot = new JPanel();
        panelConfigBot.add(guardar);
        panelConfigBot.add(reset);
        panelConfigBot.setOpaque(false);
        // Cambiar color de todos los JLabel en el panel de configuración
        Component[] components = config.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                component.setForeground(Color.WHITE);
                component.setFont(new Font("Comic Sans MS", Font.BOLD, 20)); // Opcional, para que coincida el estilo
            }
        }
        // Unificar y agregar paneles al frame
        panelCompleto.add(config, BorderLayout.CENTER);
        panelCompleto.add(panelConfigBot, BorderLayout.SOUTH);
        frame.add(panelCompleto);
        frame.setVisible(true);

        // Añado los componentes a mi arreglo asi los puedo setear todos juntos a la vez
        componentsConfg = new JComponent[] {
                ventana,
                pantallaCompleta,
                musicaBox,
                efectoSonidoBox,
                bEfectoSonido,
                bMusica,
                pausar,
                habilidad1,
                habilidad2,
                habilidad3,
                habilidad4,
                habilidad5,
                habilidad6,
                habilidad7,
                habilidad8,
                acelerarJuego,
                autodestruccion,
                iniciarJuego,
                pistaMusical,
                skin,
                guardar,
                reset
        };
        // Configuro todos los botones, textfield, etc con color blanco y fondo
        // transparente
        for (JComponent c : componentsConfg) {
            configurarComponente(c);
        }
    }

    private void cargarConfiguracion(Map<String, JComponent> componentes, Properties defaultProps) {
        for (Map.Entry<String, JComponent> entry : componentes.entrySet()) {
            String clave = entry.getKey();
            JComponent comp = entry.getValue();
            String valor = defaultProps.getProperty(clave);

            if (valor == null)
                continue;

            if (comp instanceof JRadioButton) {
                ((JRadioButton) comp).setSelected(Boolean.parseBoolean(valor));
            } else if (comp instanceof JCheckBox) {
                ((JCheckBox) comp).setSelected(Boolean.parseBoolean(valor));
            } else if (comp instanceof JTextField) {
                ((JTextField) comp).setText(valor);
            } else if (comp instanceof JComboBox) {
                ((JComboBox<String>) comp).setSelectedItem(valor);
            }
        }
    }

    private void guardarConfiguracion(Map<String, JComponent> componentes, Properties defaultProps) {
        for (Map.Entry<String, JComponent> entry : componentes.entrySet()) {
            String clave = entry.getKey();
            JComponent comp = entry.getValue();

            if (comp instanceof JRadioButton) {
                defaultProps.setProperty(clave, String.valueOf(((JRadioButton) comp).isSelected()));
            } else if (comp instanceof JCheckBox) {
                defaultProps.setProperty(clave, String.valueOf(((JCheckBox) comp).isSelected()));
            } else if (comp instanceof JTextField) {
                defaultProps.setProperty(clave, ((JTextField) comp).getText());
            } else if (comp instanceof JComboBox) {
                defaultProps.setProperty(clave, String.valueOf(((JComboBox<String>) comp).getSelectedItem()));
            }
        }
    }

    protected static void guardarEnArchivo(Properties defaultProps, String rutaArchivo) {
        try (FileOutputStream out = new FileOutputStream(rutaArchivo)) {
            defaultProps.store(out, "ConfgUsuario");
            String currentDirectory = System.getProperty("user.dir");
            System.out.println("El directorio actual es: " + currentDirectory);
        } catch (Exception e) {
            System.out.println("No se pudo encontrar la ruta");
            e.printStackTrace();
        }
    }

    protected static void cargarEnArchivo(Properties defaultProps, String rutaArchivo) {
        try (FileInputStream in = new FileInputStream(rutaArchivo)) {
            defaultProps.load(in);
        } catch (Exception e) {
            // Lo normal seria que a la primera tire error
            System.out.println("No se pudo cargar configuración previa.");
        }
    }

    private void configurarComponente(JComponent componente) {
        componente.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        componente.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true));

        if (componente instanceof JTextField) {
            componente.setOpaque(true);
            componente.setBackground(Color.WHITE);
            componente.setForeground(Color.BLACK);
        } else if(componente instanceof JComboBox) {
            componente.setOpaque(true);
            componente.setBackground(Color.WHITE);
            componente.setForeground(Color.BLACK);
        }
        else{
            componente.setOpaque(false); // para JCheckBox, JRadioButton, etc.
            componente.setForeground(Color.WHITE);
            componente.setBackground(new Color(0, 0, 0, 0)); // totalmente transparente
        }
    }

    // Método genérico para convertir un string en el keycode correspondiente
    private int convertirTecla(String nombreTecla) {
        nombreTecla = nombreTecla.trim();

        // Traducir flechas Unicode a teclas reales
        switch (nombreTecla) {
            case "↑":
                return KeyEvent.VK_UP;
            case "↓":
                return KeyEvent.VK_DOWN;
        }

        // Tecla simple (una letra o número)
        if (nombreTecla.length() == 1) {
            return KeyEvent.getExtendedKeyCodeForChar(nombreTecla.toUpperCase().charAt(0));
        }

        // Tecla especial como "UP", "SPACE", etc.
        try {
            return KeyEvent.class.getField("VK_" + nombreTecla.toUpperCase()).getInt(null);
        } catch (Exception e) {
            System.out.println("Tecla inválida: " + nombreTecla + ". Se usará VK_UNDEFINED.");
            return KeyEvent.VK_UNDEFINED;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == guardar) {
            guardarConfiguracion(componentes, defaultProps);
            guardarEnArchivo(defaultProps, archivoConfig);
        } else if (e.getSource() == reset) {
            // Reestablece botones
            musicaBox.setSelected(true);
            efectoSonidoBox.setSelected(true);
            bEfectoSonido.setText("Q");
            bMusica.setText("W");
            pausar.setText("Space");
            habilidad1.setText("1");
            habilidad2.setText("2");
            habilidad3.setText("3");
            habilidad4.setText("4");
            habilidad5.setText("5");
            habilidad6.setText("6");
            habilidad7.setText("7");
            habilidad8.setText("8");
            acelerarJuego.setText("E");
            autodestruccion.setText("R");
            iniciarJuego.setText("Enter");
            pistaMusical.setSelectedIndex(0);
            skin.setSelectedIndex(0);
        }
    }
}