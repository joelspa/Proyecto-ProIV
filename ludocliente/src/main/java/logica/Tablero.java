package logica;

import java.util.ArrayList;
import java.util.List;

public class Tablero {

    /**
     * numero de casillas independientes
     */
    public static final int N_CASILLAS = 40;
    /**
     * estructura del tablero
     */
    private Casilla[][] board = new Casilla[11][11];
    /**
     * lista de casillas independientes
     */
    private final List<Casilla> casillas = new ArrayList<Casilla>();
    /**
     * lista de bases para cada jugador
     */
    private final List<List<Casilla>> bases = new ArrayList<List<Casilla>>();
    /**
     * lista de casillas objetivos para cada jugador
     */
    private final List<List<Casilla>> objetivos = new ArrayList<List<Casilla>>();

    public Tablero() {

        // crear casillas del tablero
        for (int j = 0; j < 4; j++) {
            Casilla field = new Casilla(0);
            board[j][4] = field;
            casillas.add(field);
        }

        for (int i = 4; i >= 0; i--) {
            Casilla field = new Casilla(0);
            board[4][i] = field;
            casillas.add(field);
        }

        Casilla field = new Casilla(0);
        board[5][0] = field;
        casillas.add(field);

        for (int i = 0; i <= 4; i++) {
            field = new Casilla(0);
            board[6][i] = field;
            casillas.add(field);
        }
        for (int j = 7; j < 11; j++) {
            field = new Casilla(0);
            board[j][4] = field;
            casillas.add(field);
        }

        field = new Casilla(0);
        board[10][5] = field;
        casillas.add(field);

        for (int j = 10; j > 5; j--) {
            field = new Casilla(0);
            board[j][6] = field;
            casillas.add(field);
        }

        for (int i = 7; i < 11; i++) {
            field = new Casilla(0);
            board[6][i] = field;
            casillas.add(field);
        }

        field = new Casilla(0);
        board[5][10] = field;
        casillas.add(field);

        for (int i = 10; i > 5; i--) {
            field = new Casilla(0);
            board[4][i] = field;
            casillas.add(field);
        }

        for (int j = 3; j >= 0; j--) {
            field = new Casilla(0);
            board[j][6] = field;
            casillas.add(field);
        }

        field = new Casilla(0);
        board[0][5] = field;
        casillas.add(field);

        // crea casillas base
        // red player
        List<Casilla> p1 = new ArrayList<Casilla>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                field = new Casilla(1);
                board[j][i] = field;
                p1.add(field);
            }
        }
        bases.add(p1);

        // blue player
        List<Casilla> p2 = new ArrayList<Casilla>();
        for (int i = 0; i < 2; i++) {
            for (int j = 9; j < 11; j++) {
                field = new Casilla(2);
                board[j][i] = field;
                p2.add(field);
            }
        }
        bases.add(p2);

        // green player
        List<Casilla> p3 = new ArrayList<Casilla>();
        for (int i = 9; i < 11; i++) {
            for (int j = 9; j < 11; j++) {
                field = new Casilla(3);
                board[j][i] = field;
                p3.add(field);
            }
        }
        bases.add(p3);

        // yellow player
        List<Casilla> p4 = new ArrayList<Casilla>();
        for (int i = 9; i < 11; i++) {
            for (int j = 0; j < 2; j++) {
                field = new Casilla(4);
                board[j][i] = field;
                p4.add(field);
            }
        }
        bases.add(p4);

        // crea casillas objetivo
        // red player
        List<Casilla> p5 = new ArrayList<Casilla>();
        for (int j = 1; j < 5; j++) {
            field = new Casilla(1);
            board[j][5] = field;
            p5.add(field);
        }
        objetivos.add(p5);

        // blue player
        List<Casilla> p6 = new ArrayList<Casilla>();
        for (int i = 1; i < 5; i++) {
            field = new Casilla(2);
            board[5][i] = field;
            p6.add(field);
        }
        objetivos.add(p6);

        // green player
        List<Casilla> p7 = new ArrayList<Casilla>();
        for (int j = 9; j > 5; j--) {
            field = new Casilla(3);
            board[j][5] = field;
            p7.add(field);
        }
        objetivos.add(p7);

        // yellow player
        List<Casilla> p8 = new ArrayList<Casilla>();
        for (int i = 9; i > 5; i--) {
            field = new Casilla(4);
            board[5][i] = field;
            p8.add(field);
        }
        objetivos.add(p8);

    }

    /**
     * retorna la lista de bases del jugador
     */
    public List<Casilla> getHomes(int index) {
        return bases.get(index);
    }

    /**
     * retorna lista de los objetivos del jugador
     */
    public List<Casilla> getEnds(int index) {
        return objetivos.get(index);
    }

    /**
     * retorna la estructura del juego
     */
    public Casilla[][] getBoard() {
        return board;
    }

    /**
     * retorna lista de casillas
     */
    public List<Casilla> getCasillas() {
        return casillas;
    }

}
