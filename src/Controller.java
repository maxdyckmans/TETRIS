import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Controller {
    public static final int COLUMNS = 10;
    public static final int ROWS = 20;
    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 1000;
    public static final int TIMER_DELAY = 500;
    public static final int BLOCK_TYPES = 8;


    JFrame window;
    GameLogic gameLogic;
    GamePanel gamePanel;
    Timer gameLoop;
    KeyListener keyListener;

    public Controller(){


        gamePanel = new GamePanel();
        gameLogic = new GameLogic(ROWS,COLUMNS );
        window = new JFrame();

        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(gamePanel);
        window.setVisible(true);

        keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {

                    gameLogic.moveCurrentBlockHorizontally(-1);
                    gamePanel.repaint();
                }

                if(key == KeyEvent.VK_UP|| key == KeyEvent.VK_W){

                    gameLogic.rotateCurrentBlock();
                    System.out.println("ROTATION DONE");



                    // This runs on the EDT after doInBackground finishes
                    gamePanel.revalidate();
                    gamePanel.repaint();
                    System.out.println("REPAINT");


                    //Rotating the Block takes to long, so it is calculated in a new Thread, so that the repaint method is called at the right moment


                }


                if(key == KeyEvent.VK_RIGHT|| key == KeyEvent.VK_D) {

                    gameLogic.moveCurrentBlockHorizontally(1);

                    gamePanel.repaint();

                }
                if(key == KeyEvent.VK_DOWN|| key == KeyEvent.VK_S){
                    gameLogic.drop();
                    gamePanel.repaint();
                }
                if(key == KeyEvent.VK_SPACE){

                    gameLogic.hardDrop();
                    gamePanel.repaint();
                    gameLoop.restart();

                    //Create custom event to ensure block is hard dropped and not moveable anymore
                    ActionEvent evt = new ActionEvent(gameLoop, 0, gameLoop.getActionCommand(), System.currentTimeMillis(), 0);
                    for(ActionListener a: gameLoop.getActionListeners()){
                        a.actionPerformed(evt);
                    }

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        window.addKeyListener(keyListener);
    }


    public void startGameLoop(){

        ActionListener loop = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("GameLoop Running");
                update();
                gamePanel.repaint();
            }
        };

        gameLogic.initializeBoard();
        gamePanel.repaint();
        gameLoop = new Timer(TIMER_DELAY, loop);
        gameLoop.start();
    }

    public void update(){

//        for(int i = 0; i < gameLogic.gameBoard.length; i++) {
//            for (int j = 0; j < gameLogic.gameBoard[0].length; j++) {
//                if(gameLogic.gameBoard[i][j] == null) System.out.print(0);
//                else System.out.print(gameLogic.gameBoard[i][j]);
//            }
//            System.out.println();
//        }
//        System.out.println("\n");

        Square[][] temp = gameLogic.getUpdate();

        //Stop game if game is over
        if(temp == null) {
            gameLoop.stop();
            window.removeKeyListener(keyListener);

        }
        else {
//            for(int i = 0; i < temp.length; i++) {
//                for (int j = 0; j < temp[0].length; j++) {
//                    if(temp[i][j] == null) System.out.print(0);
//                    else System.out.print(temp[i][j]);
//                }
//                System.out.println();
//            }
//            System.out.println("\n");
            gamePanel.setGameBoard(temp);
            gamePanel.repaint();
        }



    }

}
