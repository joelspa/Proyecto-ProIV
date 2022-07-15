package logica;

import gui.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class GameServer implements Runnable {

    /**
     * Puerto en el que se debe iniciar este servidor
     */
    private final int port;
    /**
     * Número máximo de jugadores para este juego
     */
    private final int MAX_PLAYERS;
    /**
     * Frame del servidor
     */
    private final Main frame;
    /**
     * Socket del server
     */
    private ServerSocket server;
    /**
     * Lista de clientes que actualmente están conectados a este servidor
     */
    private PlayerConnection[] gameClients;
    /**
     * True si el juuego empezó
     */
    private boolean gameStarted;

    public GameServer(int port, int maxPlayers, Main frame) {
        this.gameStarted = false;
        this.port = port;
        this.MAX_PLAYERS = maxPlayers;
        this.frame = frame;
        try {
            server = new ServerSocket(this.port);
        } catch (IOException e) {
            try {
                server.close();
            } catch (IOException ex) {
            }
        }
        gameClients = new PlayerConnection[MAX_PLAYERS];
    }

    /**
     * Hilo que espera que los clientes se conecten los clientes
     * luego se agregan a la lista de clientes conectados.
     */
    public void run() {
        try {
            while (true) {
                Socket client = server.accept();
                synchronized (this) {
                    this.addConnection(new PlayerConnection(this, client));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void broadcast(String msg) {
        synchronized (gameClients) {
            for (PlayerConnection next : gameClients) {
                next.print(msg);
            }
        }
    }

    /**
     * Agrega PlayerConnection a la lista de clientes si MAX_PLAYERS no se ha
     * alcanzado Cuando todos los jugadores entèn conectados empieza el juego.
     */
    public void addConnection(PlayerConnection c) {
        boolean added = false;
        boolean allClientsReady = false;
        for (int i = 0; !added && i < MAX_PLAYERS; i++) {
            if (gameClients[i] == null) {
                gameClients[i] = (c);
                c.setClientID(i);
                c.print("COLOR#" + (i + 1));
                frame.playerConnected(i);
                added = true;
                if (i == MAX_PLAYERS - 1) {
                    allClientsReady = true;
                }
            } else if (i == MAX_PLAYERS - 1) {
                c.print("FULL#Servidor lleno.");
                c.closeClientConnection();
            }
        }

        if (allClientsReady) {
            this.broadcast("START#" + MAX_PLAYERS);
            this.gameStarted = true;
            frame.setGameState(gameStarted);
        }
    }

    public void removeConnection(PlayerConnection c) {
        System.out.println("Jugador que se desconectó: " + c.getClientID());
        gameClients[c.getClientID()] = null;
        frame.playerDisconnected(c.getClientID());
        if (gameStarted) {
            this.broadcast("ERROR#Un jugador ha cerrado la conexión. El juego termina...");
            this.gameStarted = false;
            frame.setGameState(gameStarted);
        }
    }
}
