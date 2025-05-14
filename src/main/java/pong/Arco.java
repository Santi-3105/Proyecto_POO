package pong;

import clasesCompartidas.ObjetoGrafico;

class Arco extends ObjetoGrafico {
    private int ancho;
    private int alto;
    private Marcador MarcadorAsociado; 

    public  Arco(int ancho,int alto,double posicionX,double posicionY, Marcador MarcadorAsociado){
        this.ancho=ancho;
        this.alto=alto;
        this.posicionX=posicionX;
        this.posicionY=posicionY;
        this.MarcadorAsociado=MarcadorAsociado;
    }
    public boolean detectaGol(Pelota pelota) {
        boolean gol= false;
        if(pelota.getX() + pelota.getAncho() > this.posicionX &&
            pelota.getX() < this.posicionY + ancho &&
            pelota.getY() + pelota.getAlto() > this.posicionY &&
            pelota.getY() < this.posicionY + alto){
                gol= true;
                MarcadorAsociado.incrementarPuntaje();
            }
        return gol;
    }
    @Override
    public void update(double delta){}
    @Override
    public double getX() {
        return this.posicionX;
    }
    @Override
    public double getY() {
        return this.posicionY;
    }
}
