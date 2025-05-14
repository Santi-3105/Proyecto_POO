package clasesCompartidas;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;


public abstract class ObjetoGrafico implements ObjetoMovible{
    protected BufferedImage imagen = null;

    public double posicionX = 0;
    public double posicionY = 0;

    public ObjetoGrafico(){}
    public ObjetoGrafico(String filename) {
        try {
            imagen = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filename));

        } catch (IOException e) {
            System.out.println("Excepción en ObjectoGrafico "+e);
        }
    }

    public int getAncho(){
        return imagen.getWidth();
    }
    public int getAlto(){return imagen.getHeight();}

    public void setPosicion(double x,double y){
        this.posicionX = x;
        this.posicionY = y;
    }

    public void mostrar(Graphics2D g2) {
        g2.drawImage(imagen,(int) this.posicionX,(int) this.posicionY,null);
    }

    public double getX(){
        return posicionX;
    }

    public double getY(){
        return posicionY;
    }
}
