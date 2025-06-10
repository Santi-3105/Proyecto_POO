package lemmings;

public interface Habilidad {
    // Métodos abstractos que cada habilidad debe implementar
    public abstract void iniciarHabilidad(Bichito b);
    public abstract void detenerHabilidad();
}