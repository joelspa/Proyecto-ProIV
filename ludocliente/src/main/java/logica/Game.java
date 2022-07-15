package logica;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JOptionPane;

public class Game extends Observable implements Runnable {

    /**
     * numero de jugadores esperados
     */
    private int N_PLAYERS;
    /**
     * lista de jugadores
     */
    private final List<Player> players = new ArrayList<Player>();
    /**
     * lsita de casillas
     */
    private final List<Casilla> casillas;
    /**
     * valor actual del dado
     */
    private int dado;
    /**
     * jugador actual
     */
    private int currentPlayer;
    /**
     * siguiente accion por realizarse
     */
    private char nextMove;
    /**
     * cantidad de rolls
     */
    private int rolls;
    /**
     * revisa si los jugadores hicieron un movimiento correctamente
     */
    private boolean correctMove;
    /**
     * revisa si es el primer movimiento del jugador
     */
    private boolean firstMove = true;
    /**
     * Es true si el jugador no se puede mover a la meta porque esta bloqueado
     */
    private boolean cantMoveToFinish = false;

    /**
     * Inicia un nuevo juego, selecciona el primer jugador, establece el valor
     * del dado a 0, nextMove pasa a "roll" y le da 3 rolls al primer jugador
     *
     * @param board
     * @param numberOfPlayers
     */
    public Game(Tablero board, int numberOfPlayers) {
        N_PLAYERS = numberOfPlayers;
        casillas = board.getCasillas();
        // inicializa 4 jugadores
        for (int i = 1; i <= N_PLAYERS; i++) {
            int startIndex = (i - 1) * 10;
            int endIndex = startIndex - 1;
            if (endIndex == -1) {
                endIndex = 39;
            }
            Player player = new Player(i, board, this, startIndex, endIndex);
            players.add(player);
        }
        // Set first player
        currentPlayer = 1;
        dado = 0;
        nextMove = 'r';
        rolls = 3;
    }

    /**
     * roll a random number between 1 and 6 and call roll(int number) with this
     * value. (used by Button click)
     */
    public void roll() {
        roll((int) ((Math.random()) * 6 + 1));
    }

    /**
     * handles the rolled number by the current player.
     *
     * @param number of dice
     */
    public void roll(int number) {
        if (nextMove == 'r') {
            dado = number;
            boolean movePossible = isMovePossible();
            if (movePossible) {
                boolean wasAutomatic = moveAutomatic();
                if (wasAutomatic) {
                    if (dado == 6) {
                        rolls = 2;
                    } else {
                        rolls = 0;
                    }
                    nextMove = 'r';
                } else {
                    nextMove = 'm';
                }
                // Si esta es la primera vez que el jugador tira,
                // y sus fichas se atascan en/delante del objetivo
                // lanza los dados tres veces.
            } else if (firstMove && cantMoveToFinish) {
                rolls = 3;
                nextMove = 'r';
            } else {
                nextMove = 'r';
            }
            firstMove = false;
            refresh();
        }
    }

    /**
     * Comprueba si el jugador actual puede realizar un movimiento con el valor
     * actual del dado
     */
    public boolean isMovePossible() {
        Player current = players.get(currentPlayer - 1);
        int playerStartIndex = current.getStartIndex();
        int playerEndIndex = current.getEndIndex();
        Casilla startCasilla = casillas.get(playerStartIndex);

        // Primero comprueba si tu propio personaje estï¿½ en el campo inicial y
        // podria moverse, de lo contrario falso, ya que el campo de inicio siempre esta vacio
        // debe convertirse.
        if (startCasilla.tieneFicha()
                && startCasilla.getFicha().getIndex() == currentPlayer) {
            if (casillas.get(playerStartIndex + dado).tieneFicha()
                    && casillas.get(playerStartIndex + dado).getFicha().getIndex() == currentPlayer) {
                return false;
            }
        }

        // Mientras tengas piezas en la casa y ninguna en la casilla inicial,
        // o es un jugador contrario, siempre puedes
        // dibujar.
        if (current.getFichasDeBase() > 0) {
            if (dado == 6) {
                if (!startCasilla.tieneFicha() || startCasilla.tieneFicha()
                        && startCasilla.getFicha().getIndex() != currentPlayer) {
                    return true;
                }
            }
        }

        // Iterar sobre el tablero comprobando para cada pieza el
        // jugadora actual si pudiera moverse. Una vez dibujada una figura
        // puede, se devuelve verdadero.
        for (int index = 0; index < 40; index++) {
            Casilla curCasilla = casillas.get(index);
            Casilla nextCasilla;

            // Comprueba si este es un personaje del jugador actual
            // comercio.
            if (curCasilla.tieneFicha()
                    && curCasilla.getFicha().getIndex() == currentPlayer) {
                // Primero verifique si un campo objetivo puede ser alcanzado
                if (index >= playerEndIndex - 5 && index <= playerEndIndex
                        && (index + dado) - playerEndIndex >= 1) {
                    if (current.getEndFichas() == 0) {
                        return true;
                    } else {
                        int fieldInEnd = (index + dado) - (playerEndIndex + 1);
                        if (fieldInEnd <= 3
                                && !current.getEndCasillas().get(fieldInEnd)
                                        .tieneFicha()) {
                            return true;
                        } else {
                            // Objetivo bloqueado, entonces:
                            cantMoveToFinish = true;
                        }
                    }
                }

                // Ahora comprueba si son posibles los movimientos a casilla.
                int tmp;
                if (index + dado > 39) {
                    tmp = (index + dado) - 40;
                    nextCasilla = casillas.get(tmp);
                } else {
                    tmp = index + dado;
                    nextCasilla = casillas.get(tmp);
                }

                // Si no hay pieza/una pieza del oponente en la siguiente casilla
                // se para...
                if (!cantMoveToFinish && (!nextCasilla.tieneFicha() || nextCasilla.tieneFicha() && nextCasilla.getFicha().getIndex() != currentPlayer)) {
                    return true;
                }

                // Si estas en el "fin" pero el numero de espacios
                // no es suficiente para llegar a la meta...
                if (cantMoveToFinish && tmp >= playerEndIndex - 5 && tmp <= playerEndIndex && (!nextCasilla.tieneFicha() || nextCasilla.tieneFicha() && nextCasilla.getFicha().getIndex() != currentPlayer)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Se mueve automaticamente si todos los peones estan en casa y dados = 6 o
     * si uno de sus fichas bloquea la casilla inicial. Este metodo retorna
     * true.
     */
    public boolean moveAutomatic() {
        Player current = players.get(currentPlayer - 1);
        int playerStartIndex = current.getStartIndex();
        Casilla CasillaInicial = casillas.get(playerStartIndex);
        Casilla nextCasilla = casillas.get(playerStartIndex + dado);

        // Si aun quedan fichas en la casa y dados=6...
        if (current.getFichasDeBase() > 0 && dado == 6) {
            // elegir una ficha de la base y ponerlo automaticamente
            // en la casilla inicial.
            if (!CasillaInicial.tieneFicha()) {
                current.moverseDeBase();
                casillas.get(playerStartIndex).setFicha(new Ficha(currentPlayer));
                return true;
            }

            // Si la casilla inicial = pieza del oponente, devuelve la ficha del jugar a base y la ficha avanza a la casilla inicial
            if (CasillaInicial.tieneFicha() && CasillaInicial.getFicha().getIndex() != currentPlayer) {
                comerFicha(CasillaInicial);
                current.moverseDeBase();
                casillas.get(playerStartIndex).setFicha(new Ficha(currentPlayer));
                return true;
            }

            // Si la casilla inicial = pieza propia
            if (CasillaInicial.tieneFicha() && CasillaInicial.getFicha().getIndex() == currentPlayer && !nextCasilla.tieneFicha()) {
                casillas.get(playerStartIndex).setFicha(null);
                nextCasilla.setFicha(new Ficha(currentPlayer));
                return true;
            }
        }

        if (CasillaInicial.tieneFicha() && CasillaInicial.getFicha().getIndex() == currentPlayer) {
            if (!nextCasilla.tieneFicha()) {
                nextCasilla.setFicha(new Ficha(currentPlayer));
            } else if (nextCasilla.getFicha().getIndex() != currentPlayer) {
                comerFicha(nextCasilla);
                nextCasilla.setFicha(new Ficha(currentPlayer));
            }
            CasillaInicial.setFicha(null);
            return true;
        }
        return false;
    }

    /**
     * Elimina la ficha de un jugador enemigo de la casilla que este jugador
     * quiere mover
     */
    public void comerFicha(Casilla casilla) {
        int enemy = casilla.getFicha().getIndex();
        players.get(enemy - 1).setFichaEnBase();
    }

    /**
     * mueve la ficha a la casilla seleccionada
     */
    public void move(Casilla field) {
        move(casillas.indexOf(field));
    }

    /**
     * mueve la ficha a la casilla indicada por el index
     */
    public void move(int fieldIndex) {
        if (nextMove == 'm' && casillas.get(fieldIndex).tieneFicha()
                && casillas.get(fieldIndex).getFicha().getIndex() == currentPlayer) {
            correctMove = false;
            boolean finishOccupied = false;

            Player current = players.get(currentPlayer - 1);
            int playerEndIndex = current.getEndIndex();
            int fieldIndexInEnd = fieldIndex + dado - playerEndIndex - 1;

            Casilla curCasilla = casillas.get(fieldIndex);
            Casilla newCasilla;

            // Mueve a la casilla final
            if (fieldIndex >= playerEndIndex - 5 && fieldIndex <= playerEndIndex && (fieldIndexInEnd + 1 >= 1)) {
                if (fieldIndexInEnd <= 3 && current.getEndFichas() == 0 || fieldIndexInEnd <= 3 && !current.getEndCasillas().get(fieldIndexInEnd).tieneFicha()) {
                    curCasilla.setFicha(null);
                    current.getEndCasillas().get(fieldIndexInEnd)
                            .setFicha(new Ficha(currentPlayer));
                    correctMove = true;
                } else {
                    finishOccupied = true;
                }
            }

            // Mover a la casilla si es que no esta ocupada
            if (!correctMove && !finishOccupied) {
                // Correccion para el limite del campo de juego
                if (fieldIndex + dado > 39) {
                    newCasilla = casillas.get(fieldIndex + dado - 40);
                } else {
                    newCasilla = casillas.get(fieldIndex + dado);
                }

                if (!newCasilla.tieneFicha()) {
                    curCasilla.setFicha(null);
                    newCasilla.setFicha(new Ficha(currentPlayer));
                    correctMove = true;
                } else if (newCasilla.getFicha().getIndex() != currentPlayer) {
                    comerFicha(newCasilla);
                    curCasilla.setFicha(null);
                    newCasilla.setFicha(new Ficha(currentPlayer));
                    correctMove = true;
                } else if (newCasilla.getFicha().getIndex() == currentPlayer) {
                    correctMove = false;
                }
            }

            if (correctMove) {
                if (dado == 6) {
                    rolls = 2;
                } else {
                    rolls = 0;
                }
                refresh();
            }
        }
    }

    /**
     * Establece al nextPlayer como currentPlayer.
     */
    public void nextPlayer() {
        if (currentPlayer == N_PLAYERS) {
            currentPlayer = 1;
        } else {
            currentPlayer += 1;
        }
    }

    public int getDado() {
        return dado;
    }

    public int getRolls() {
        return rolls;
    }

    public char getNextMove() {
        return nextMove;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * notificar a View sobre los cambios en el modelo.
     */
    public void refresh() {
        // Si todas las fichas del jugador actual estan en el objetivo, gana
        if (players.get(currentPlayer - 1).getEndFichas() == 4) {
            nextMove = 'f';
        }
        // Si el jugador actual se movio correctamente y toca moverse
        if (correctMove && nextMove == 'm') {
            if (dado == 6) {
                rolls = 2;
            } else {
                rolls = 0;
            }
            nextMove = 'r';
        }
        // Si el jugador actual no pudiera moverse...
        if (nextMove == 'r') {
            rolls -= 1;
            if (rolls <= 0) {
                nextPlayer();
                rolls = players.get(currentPlayer - 1).getRolls();
                firstMove = true;
                cantMoveToFinish = false;
            }
        }
        correctMove = false; // correctMove Reiniciar
        setChanged();
        notifyObservers();
    }

    public void refresh(String activate) {
        System.out.println(activate);
        setChanged();
        notifyObservers();
    }

    // ------------------------------Metodos Online---------------------------------------
    private boolean onlineGame;
    private int clientPlayer;
    private Tablero board;

    private Socket serverSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Thread thread;

    public Game(Tablero board, Socket socket) throws Exception {
        onlineGame = true;
        this.board = board;
        casillas = board.getCasillas();

        // Conectar al servidor
        this.serverSocket = socket;
        out = new ObjectOutputStream(serverSocket.getOutputStream());
        in = new ObjectInputStream(new BufferedInputStream(serverSocket.getInputStream()));

        // lee el primer mensaje del servidor (indice de este cliente||el juego ya esta lleno)
        String[] tmp = ((String) in.readObject()).split("#");
        if (tmp[0].equals("COLOR")) {
            clientPlayer = Integer.valueOf(tmp[1]);
        } else if (tmp[0].equals("FULL")) {
            throw new Exception(tmp[1]);
        }
    }

    /**
     * Comienza el hilo para esuchar al servidor
     */
    public void runGameThread() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * Metodo que espera la respuesta del servidor para empezar el juego
     */
    public boolean waitForStart() throws Exception {
        //Esperar a que comience el juego
        String something;
        synchronized (in) {
            something = (String) in.readObject();
        }
        String[] tmp = something.split("#");
        if (tmp[0].equals("START")) {
            N_PLAYERS = Integer.valueOf(tmp[1]);
            return true;
        }
        return false;
    }

    /**
     * Empieza el juego con el numero de jugadores del servidor
     */
    public void initializeGame() {
        // inicializa 4 jugadores
        for (int i = 1; i <= N_PLAYERS; i++) {
            int startIndex = (i - 1) * 10;
            int endIndex = startIndex - 1;
            if (endIndex == -1) {
                endIndex = 39;
            }
            Player player = new Player(i, board, this, startIndex, endIndex);
            players.add(player);
        }
        // establece primer jugador
        currentPlayer = 1;
        dado = 0;
        nextMove = 'r';
        rolls = 4;
    }

    /**
     * Comprueba si este jugador tiene los derechos para tirar y envia el
     * comando al servidor.
     */
    public void rollOnline() throws IOException {
        if (currentPlayer == clientPlayer) {
            int rolled = (int) ((Math.random()) * 6 + 1);
            out.writeObject("ROLL#" + rolled);
        }
    }

    /**
     * Comprueba si este jugador pude moverse y envo­a el comando al servidor.
     */
    public void moveOnline(Casilla casilla) throws IOException {
        if (currentPlayer == clientPlayer && casilla.getFicha().getIndex() == clientPlayer) {
            int indexOfCasilla = casillas.indexOf(casilla);
            out.writeObject("MOVE#" + indexOfCasilla);
        }
    }

    public boolean isOnlineGame() {
        return onlineGame;
    }

    public int getClientIndex() {
        return clientPlayer;
    }

    public int getNumberOfPlayers() {
        return N_PLAYERS;
    }

    public void run() {
        String command;
        String message;
        try {
            this.waitForStart();
            this.initializeGame();
            this.refresh();

            while (true) {
                synchronized (in) {
                    command = (String) in.readObject();
                }
                if (command != null) {
                    String[] tmp = command.split("#");
                    command = tmp[0];
                    message = tmp[1];

                    if (command.equals("ROLL")) {
                        this.roll(Integer.valueOf(message));
                    }
                    if (command.equals("MOVE")) {
                        this.move(Integer.valueOf(message));
                    }
                    if (command.equals("ERROR")) {
                        JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        }
    }

}
