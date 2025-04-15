import javax.swing.*;
import java.awt.*;

import static java.awt.Transparency.TRANSLUCENT;

public class GamePanel extends JPanel {


public static final int ROW_HEIGHT = Controller.WINDOW_HEIGHT/Controller.ROWS;
public static final int COLUMN_WIDTH = Controller.WINDOW_WIDTH/Controller.COLUMNS;
public Square[][] gameBoard = new Square[Controller.ROWS][Controller.COLUMNS];
private final Color[] blockColors = {Color.WHITE, Color.BLUE, Color.ORANGE, Color.RED, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN };

    public GamePanel(){
        for(int i = 0; i<gameBoard.length; i++){
            for(int j = 0; j < gameBoard[0].length; j++){

                gameBoard[i][j] = new Square(-1, 0, 0, 0);
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(Square[] array: gameBoard){
            for(Square s: array){
                if(s != null){
                    g.setColor(blockColors[s.getType()]);
                    int x = (s.getAbsoluteCol())* COLUMN_WIDTH;
                    int y = (s.getAbsoluteRow())* ROW_HEIGHT;
                    g.fillRect( x, y, COLUMN_WIDTH, ROW_HEIGHT);
                }


            }
        }
    }

    public void setGameBoard(Square[][] gameBoard) {
        this.gameBoard = gameBoard;
    }
}
