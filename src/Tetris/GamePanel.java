package Tetris;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {


public static final int ROW_HEIGHT = TetrisEnvironment.WINDOW_HEIGHT/ TetrisEnvironment.ROWS;
public static final int COLUMN_WIDTH = TetrisEnvironment.WINDOW_WIDTH/ TetrisEnvironment.COLUMNS;
public Square[][] gameBoard = new Square[TetrisEnvironment.ROWS][TetrisEnvironment.COLUMNS];
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

        //Draw the Tetris.Block falling
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

        //Draw the Lines
        Graphics2D g2 = (Graphics2D) g;


        //Drawing from Left to Right
        for(int i = 0; i <= gameBoard.length; i++){

            if(i == 0 || i == gameBoard.length){
                //Thick Black lines if it's the Edge
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3));
            }
            else{
                //Thin GreyLines if it's not the Edge
                g.setColor(Color.GRAY);
                g2.setStroke(new BasicStroke(1));
            }


            g2.drawLine(0,i * ROW_HEIGHT , TetrisEnvironment.COLUMNS * COLUMN_WIDTH,i * ROW_HEIGHT  );
        }

        //Drawing from Top to Bottom
        for(int i = 0; i <= gameBoard[0].length; i++){

            if(i == 0 || i == gameBoard[0].length){
                //Thick Black lines if it's the Edge

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3));
            }
            else{
                //Thin GreyLines if it's not the Edge
                g.setColor(Color.GRAY);
                g2.setStroke(new BasicStroke(1));
            }
            g2.drawLine(i * COLUMN_WIDTH, 0, i * COLUMN_WIDTH, TetrisEnvironment.ROWS * ROW_HEIGHT );
        }


    }

    public void setGameBoard(Square[][] gameBoard) {
        this.gameBoard = gameBoard;
    }
}
