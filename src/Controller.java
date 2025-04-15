import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Controller {
    public static final int COLUMNS = 10;
    public static final int ROWS = 20;
    public static final int WINDOW_WIDTH = 400;
    public static final int WINDOW_HEIGHT = 800;
    public static final int TIMER_DELAY = 1000;
    JFrame window;
    GameLogic gameLogic;
    GamePanel gamePanel;
    Timer gameLoop;

    public Controller(){
        gamePanel = new GamePanel();
        gameLogic = new GameLogic(COLUMNS, ROWS);
        window = new JFrame();

        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(gamePanel);
        window.setVisible(true);

        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_LEFT) System.out.println("LEFT");
                if(key == KeyEvent.VK_UP) System.out.println("UP");
                if(key == KeyEvent.VK_RIGHT) System.out.println("RIGHT");
                if(key == KeyEvent.VK_DOWN) System.out.println("DOWN");

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        window.addKeyListener(kl);
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
        gameLoop = new Timer(TIMER_DELAY, loop);
        gameLoop.start();
    }

    public void update(){
        Square[][] temp = gameLogic.getUpdate();

        //Stop game if game is over
        if(temp == null)gameLoop.stop();

        else {
//            for(int i = 0; i < temp.length; i++) {
//                for (int j = 0; j < temp[0].length; j++) {
//                    System.out.print(temp[i][j]);
//                }
//                System.out.println();
//            }
            gamePanel.setGameBoard(temp);
        }



    }

}
