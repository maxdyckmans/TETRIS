package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;


public class TetrisEnvironment {
    public static final int COLUMNS = 10;
    public static final int ROWS = 20;
    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 1000;
    public static final int BLOCK_TYPES = 8;
    public static final int DRAW_DELAY = 100;



    int [] state;
    String renderMode;
    JFrame window;
    GameLogic gameLogic;
    GamePanel gamePanel;
    Timer gameLoop;
    KeyListener keyListener;

    public TetrisEnvironment(String renderMode){

        this.renderMode = renderMode;
        gameLogic = new GameLogic(ROWS,COLUMNS);

        if(renderMode.equals("human")) {
            initializeGraphics();
        }
    }

    public void initializeGraphics(){
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.repaint();
            }
        };
        gameLoop = new Timer(DRAW_DELAY, al);
        gameLoop.setRepeats(false);

        gamePanel = new GamePanel();
        gamePanel.gameBoard = gameLogic.gameBoard;
        gamePanel.setPreferredSize(new Dimension(WINDOW_WIDTH + 1, WINDOW_HEIGHT + 1));

        //Helper panel to center the gamePanel
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.add(gamePanel);

        window = new JFrame();
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.add(wrapper, BorderLayout.CENTER);
        window.setVisible(true);

    }

    public Transition reset(){

        return gameLogic.reset();
    }

    public Transition step(int action) {

        //Action 0 = Left
        if (action == 0) {
            gameLogic.moveCurrentBlockHorizontally(-1);
        }
        //Action 1 = Right
        if (action == 1) {
            gameLogic.moveCurrentBlockHorizontally(1);
        }

        //Action 2 = Rotate
        if (action == 2) {
            gameLogic.rotateCurrentBlock();
        }

        //Action 3 = Drop
        if (action == 3) {
            gameLogic.drop();

        }

        //Action 4 = hard Drop
        if (action == 4) {
            gameLogic.hardDrop();
        }

        //Action 5 = wait
        //

        if(renderMode.equals("human")){
            try {
                Thread.sleep(DRAW_DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            gamePanel.gameBoard = gameLogic.gameBoard;
            gamePanel.repaint();
        }
        return gameLogic.step();

    }

    public void setRenderMode(String renderMode) {
        this.renderMode = renderMode;
    }
}


