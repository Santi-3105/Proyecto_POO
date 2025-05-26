package lemmings;

public interface Habilidad {

    public void guardarHabilidad(); 

    // Métodos abstractos que cada habilidad debe implementar
    public abstract void iniciarHabilidad();
    public abstract void detenerHabilidad();
}