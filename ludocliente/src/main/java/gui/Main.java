package gui;

import logica.Game;
import logica.Tablero;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private static JMenuBar menuBar;

    private static JMenu game;
    private static JMenu newGame;
    private static JMenuItem twoPlayers;
    private static JMenuItem threePlayers;
    private static JMenuItem fourPlayers;
    private static JMenuItem onlineGame;

    public Main() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 750);
        Image icon = new ImageIcon(getClass().getClassLoader().getResource("parchis.jpg")).getImage();
        setIconImage(icon);
        setTitle("Ludo");

        menuBar = new JMenuBar();

        game = new JMenu("New Game");
        newGame = new JMenu("Local");
        twoPlayers = new JMenuItem("2 Players");
        twoPlayers.addActionListener(this);
        threePlayers = new JMenuItem("3 Players");
        threePlayers.addActionListener(this);
        fourPlayers = new JMenuItem("4 Players");
        fourPlayers.addActionListener(this);
        onlineGame = new JMenuItem("Multiplayer");
        onlineGame.addActionListener(this);

        menuBar.add(game);
        game.add(newGame);
        newGame.add(twoPlayers);
        newGame.add(threePlayers);
        newGame.add(fourPlayers);
        game.add(onlineGame);

        this.setJMenuBar(menuBar);

        contentPane = new JPanel();
        contentPane.setBackground(Color.black);
        setContentPane(contentPane);

    }

    /**
     * Inicia el juego
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Main frame = new Main();

                Tablero board = new Tablero();
                Game game = new Game(board, 0);
                TableroGUI boardView = new TableroGUI(game, board);

                frame.add(new Inicial());
                frame.add(boardView, BorderLayout.CENTER);
                frame.setVisible(true);
            }
        });
    }

    public void startAGame(Boolean playOnline, int playersForOfflineGame) throws Exception {
        this.getContentPane().removeAll();
        Game game;
        Tablero board = new Tablero();

        if (playOnline) {
            String serverIP = JOptionPane.showInputDialog("Ingresa la IP del servidor", "localhost");
            int serverPort = Integer.valueOf(JOptionPane.showInputDialog("Ingresa el puerto del servidor", "8888"));

            game = new Game(board, new Socket(serverIP, serverPort));
            this.getContentPane().add(new OnlineDado(game), BorderLayout.NORTH);
            game.runGameThread();

        } else {
            game = new Game(board, playersForOfflineGame);
            this.getContentPane().add(new Dado(game), BorderLayout.NORTH);
        }
        this.getContentPane().add(new TableroGUI(game, board), BorderLayout.CENTER);
        this.getContentPane().repaint();
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent object) {
        try {
            if (object.getSource() == twoPlayers) {
                this.startAGame(false, 2);
            } else if (object.getSource() == threePlayers) {
                this.startAGame(false, 3);
            } else if (object.getSource() == fourPlayers) {
                this.startAGame(false, 4);
            } else if (object.getSource() == onlineGame) {
                this.startAGame(true, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(),
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
