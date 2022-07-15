package logica;

import java.util.ArrayList;
import java.util.List;

public class Player {

    /**
     * Lista de la base de cada jugador
     */
    private List<Casilla> homeCasillas = new ArrayList<Casilla>();
    /**
     * Lista de la meta de cada jugador
     */
    private List<Casilla> endCasillas = new ArrayList<Casilla>();
    /**
     * Lista de casillas
     */
    private List<Casilla> fields;
    /**
     * index of the player
     */
    private final int index;
    /**
     * Primer cassilla del jugador
     */
    private int startIndex;
    /**
     * Casilla objetivo
     */
    private int endIndex;

    public Player(int index, Tablero board, Game game, int startIndex, int endIndex) {
        this.index = index;
        this.fields = board.getCasillas();
        this.homeCasillas = board.getHomes(index - 1);
        this.endCasillas = board.getEnds(index - 1);
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        for (Casilla field : homeCasillas) {
            Ficha pawn = new Ficha(index);
            field.setFicha(pawn);
        }
    }

    /**
     * Escoge una ficha de la base para mover cuando el dado = 6
     */
    public void moverseDeBase() {
        for (Casilla field : homeCasillas) {
            if (field.getFicha() != null) {
                field.setFicha(null);
                break;
            }
        }
    }

    /**
     * Después de que una ficha sea comida por otro jugador, este metodo lo
     * regresa a la base
     */
    public void setFichaEnBase() {
        for (Casilla newCasilla : homeCasillas) {
            if (newCasilla.getFicha() == null) {
                newCasilla.setFicha(new Ficha(index));
                break;
            }
        }
    }

    /**
     * retorna el numero del roll que el jugador obtuvo dependiendo de estado de
     * la base y las casillas objetivo
     */
    public int getRolls() {
        if (this.getFichasDeBase() + this.getEndFichas() == 4) {
            return 3;
        }
        return 1;
    }

    public List<Casilla> getHomeCasillas() {
        return homeCasillas;
    }

    public List<Casilla> getEndCasillas() {
        return endCasillas;
    }

    public List<Casilla> getCasillas() {
        return fields;
    }

    public int getIndex() {
        return index;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    /**
     * retorna el numero de fichas que el jugador tiene en la base
     */
    public int getFichasDeBase() {
        int pawns = 0;
        for (int i = 0; i < 4; i++) {
            if (homeCasillas.get(i).getFicha() != null) {
                pawns++;
            }
        }
        return pawns;
    }

    /**
     * retorna el numero de fichas que el jugador tiene en el objetivo
     */
    public int getEndFichas() {
        int pawns = 0;
        for (int i = 0; i < 4; i++) {
            if (endCasillas.get(i).getFicha() != null) {
                pawns++;
            }
        }
        return pawns;
    }

}
