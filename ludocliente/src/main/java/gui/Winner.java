package gui;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Winner extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Crea el Frame
     */
    public Winner(Color winnerColor, int winner) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(225, 300, 400, 150);
        setTitle("Ganaste!");
        contentPane = new JPanel();
        contentPane.setBackground(winnerColor);
        setContentPane(contentPane);

        JLabel lbl = new JLabel("El jugador " + winner + " ha ganado!");
        lbl.setFont(lbl.getFont().deriveFont(32.0f));
        JLabel lbl2 = new JLabel("Felicidades!");
        lbl2.setFont(lbl2.getFont().deriveFont(28.0f));
        this.add(lbl);
        this.add(lbl2);
    }

}
