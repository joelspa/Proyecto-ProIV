package logica;

public class Casilla {

    private final int index;
    private Ficha ficha;

    public Casilla(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }

    public boolean tieneFicha() {
        return this.getFicha() != null;
    }

}
