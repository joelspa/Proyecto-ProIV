package gui;

import logica.Casilla;
import logica.Game;
import logica.Tablero;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class TableroGUI extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    public static Color[] colors = new Color[]{Color.WHITE,
        new Color(0xFF4444), //Red
        new Color(0x33B5E5), //Blue
        new Color(0x99CC00), //Green
        new Color(0xFFBB33), //Yellow
        Color.DARK_GRAY};

    private List<CasillaGUI> fields = new ArrayList<CasillaGUI>();


    private final Game game;

    public TableroGUI(Game game, Tablero board) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);
        this.setBorder(BorderFactory.createLineBorder(Color.black, 10));
        setBackground(Color.black);
        this.game = game;
        this.game.addObserver(this);
        Casilla[][] model = board.getBoard();

        for (int i = 0; i < model.length; i++) {
            for (int j = 0; j < model.length; j++) {
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = i;
                gbc.gridy = j;
                if (model[i][j] != null) {
                    Casilla field = model[i][j];
                    CasillaGUI next = new CasillaGUI(colors[field.getIndex()], field, game);
                    next.update();
                    fields.add(next);
                    add(next, gbc);
                }
            }
        }
    }

    /**
     * actualiza el estado del juego
     */
    public synchronized void update(Observable arg0, Object arg1) {
        for (CasillaGUI field : fields) {
            field.update();
        }
    }

}
