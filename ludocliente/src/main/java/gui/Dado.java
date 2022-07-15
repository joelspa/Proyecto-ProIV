package gui;

import logica.Game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Dado extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;
    private final JButton button;
    private final JLabel rolls;
    private final Game game;
    /**
     * indice el jugador actual
     */
    private int index = 1;

    public Dado(final Game game) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(150, 100));
        this.setBorder(BorderFactory.createLineBorder(Color.lightGray, 3));
        this.setBackground(Color.black);
        this.game = game;
        this.game.addObserver(this);

        rolls = new JLabel("Empezar");
        rolls.setForeground(Color.lightGray);
        rolls.setFont(rolls.getFont().deriveFont(16.0f));

        ImageIcon tmpImage = new ImageIcon(getClass().getClassLoader().getResource("0.png"));
        button = new JButton("", tmpImage);
        tmpImage = new ImageIcon(getClass().getClassLoader().getResource("rnd.gif"));
        button.setPressedIcon(tmpImage);
        button.setBorder(BorderFactory.createLineBorder(Color.black, 6));
        button.setContentAreaFilled(false);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                game.roll();
            }

        });
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(button);
        rolls.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(rolls);
    }

    /**
     * actualiza los dados, el jugador actual y el próximo movimiento en la
     * interfaz de usuario.
     */
    public void update(Observable arg0, Object arg1) {
        index = game.getCurrentPlayer();
        ImageIcon tmpImage = new ImageIcon(getClass().getClassLoader().getResource(game.getDado() + ".png"));
        button.setIcon(tmpImage);
        this.setBorder(BorderFactory.createLineBorder(TableroGUI.colors[index],
                3));
        rolls.setForeground(TableroGUI.colors[index]);
        rolls.setText("Tiros: " + game.getRolls());

        // Bloquea dados
        if (game.getNextMove() == 'm') {
            button.setEnabled(false);
        } else if (game.getNextMove() == 'f') {
            button.setEnabled(false);
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        Winner frame = new Winner(TableroGUI.colors[index], index);
                        frame.setVisible(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            button.setEnabled(true);
        }
    }

}
