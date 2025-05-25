package lemmings;

public interface Habilidad {

    void activarHabilidad(); 

    // Métodos abstractos que cada habilidad debe implementar
    public abstract void iniciarHabilidad();
    public abstract void detenerHabilidad();
}