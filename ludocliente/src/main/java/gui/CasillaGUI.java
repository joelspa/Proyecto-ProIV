package gui;

import logica.Casilla;
import logica.Game;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CasillaGUI extends JPanel implements MouseListener {

    private static final long serialVersionUID = 1L;
    /**
     * color original del borde de la casilla
     */
    private final Color original;
    /**
     * colo actual de la casilla
     */
    private Color color;
    /**
     * ficha en la casilla
     */
    private boolean fill = false;
    /**
     * casillas asignadas
     */
    private Casilla field;

    private Game game;

    public CasillaGUI(Color color, Casilla field, Game game) {
        this.color = color;
        this.original = color;
        this.game = game;
        this.field = field;
        setPreferredSize(new Dimension(46, 46));
        setBackground(Color.black);
        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(color);
        if (fill) {
            
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            g.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
        } else {
            g2D.setStroke(new BasicStroke(2));
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

    /**
     * sincroniza el estado de la casilla con el estado de ocupado
     */
    public void update() {
        if (field.getFicha() != null) {
            color = TableroGUI.colors[field.getFicha().getIndex()];
            fill = true;
        } else {
            color = original;
            fill = false;
        }
        repaint(); // repinta panel.
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseClicked(MouseEvent arg0) {
        //Mover
        if (this.field.getIndex() == 0) {
            if (game.isOnlineGame()) {
                try {
                    game.moveOnline(field);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                game.move(field);
            }
        }

    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

}
