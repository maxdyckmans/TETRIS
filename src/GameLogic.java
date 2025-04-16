import java.util.ArrayList;
import java.util.Random;

public class GameLogic {
    public static final int START_SQUARE_ROW = 1;
    public static final int START_SQUARE_COL = 5;
    Square [][] gameBoard;
    Block currentBlock;

    public GameLogic(int rows,int cols ){
        gameBoard = new Square[rows][cols];
    }

    public void initializeBoard(){
        gameBoard = new Square[Controller.ROWS][Controller.COLUMNS];
        addBlock();

    }


    public Square[][] getUpdate(){

        if(isCurrentBlockVerticallyColliding()){
            clearLines();
            if(!addBlock()){ //The addBlock method returns false, if the Block couldn't be added --> Game over --> return null

                return null;

            }
        }
        else {
            moveCurrentBlockVertically();
        }



        return gameBoard;
    }

    public boolean addBlock(){
        Random random = new Random();
        int nextBlock = random.nextInt(Controller.BLOCK_TYPES - 1) + 1;


        currentBlock = new Block(nextBlock);
        boolean valid = true;

        //Checks if block can be placed
        for(Square s : currentBlock.blockStructureSortedByRow){
            int absoluteRow = START_SQUARE_ROW + s.getRelativeRow();
            int absoluteCol = START_SQUARE_COL + s.getRelativeCol();
            if(gameBoard[absoluteRow][absoluteCol] == null){
                valid = true;
            }
            else {
                valid = false;
            }
        }


        //Adds Block if possible
        if(valid){
            for(Square s : currentBlock.blockStructureSortedByRow){
                int absoluteRow = START_SQUARE_ROW + s.getRelativeRow();
                int absoluteCol = START_SQUARE_COL + s.getRelativeCol();
                gameBoard[absoluteRow][absoluteCol] = s;
                s.setAbsoluteRow(absoluteRow);
                s.setAbsoluteCol(absoluteCol);

            }
        }

        return valid;

    }

    public boolean isCurrentBlockHorizontallyColliding(int direction) { //-1 = left, +1 = right

        for (Square s : currentBlock.blockStructureSortedByCol) {

            if (direction == -1) {
                if (s.getAbsoluteCol() == 0) {
                    return true;
                } else {
                    Square nextSquare = gameBoard[s.getAbsoluteRow() ][s.getAbsoluteCol()+ direction];
                    if (nextSquare != null && nextSquare.getBlockID() != s.getBlockID()) { //Check if Block collides with Anything one Square down (Which isnt part of the Block)
                        return true;
                    }
                }
            }

            if (direction == 1) {
                if (s.getAbsoluteCol() == gameBoard[0].length - 1) {
                    return true;
                } else {
                    Square nextSquare = gameBoard[s.getAbsoluteRow() ][s.getAbsoluteCol()+ direction];
                    if (nextSquare != null && nextSquare.getBlockID() != s.getBlockID()) { //Check if Block collides with Anything one Square down (Which isnt part of the Block)
                        return true;
                    }
                }
            }





        }
        return false;
    }
    public void moveCurrentBlockHorizontally(int direction){



        if(!isCurrentBlockHorizontallyColliding(direction)){
            if(direction == -1){
                for(Square s: currentBlock.blockStructureSortedByCol){


                    //Move the Square to the right
                    s.setAbsoluteCol(s.getAbsoluteCol() + direction);
                    gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol()] = s;

                    //Leave an empty Square behind
                    gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol() - direction] = null;

                }
            }


            if(direction == 1){
                for(Square s: currentBlock.blockStructureSortedByCol.reversed()){

                    //Move the Square to the right
                    s.setAbsoluteCol(s.getAbsoluteCol() + direction);
                    gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol()] = s;

                    //Leave an empty Square behind
                    gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol() - direction] = null;

                }
            }

        }
    }


    public boolean isCurrentBlockVerticallyColliding(){
        for(Square s : currentBlock.blockStructureSortedByRow){

            if(s.getAbsoluteRow() == gameBoard.length - 1){ //Check if Block has reached bottom
                return true;
            } else {
                Square nextSquare = gameBoard[s.getAbsoluteRow()+1][s.getAbsoluteCol()];

                if(nextSquare != null && nextSquare.getBlockID()!= s.getBlockID()) { //Check if Block collides with Anything one Square down (Which isnt part of the Block)
                    return true;
                }
            }
        }

        return false;
    }
    public void moveCurrentBlockVertically(){
        for(Square s: currentBlock.blockStructureSortedByRow){
            s.setAbsoluteRow(s.getAbsoluteRow()+1);

            //Move the Square one down and leave an empty square behind
            gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol()] = s;
            gameBoard[s.getAbsoluteRow()-1][s.getAbsoluteCol()] = null;

        }
    }

    public void drop(){
        if(!isCurrentBlockVerticallyColliding()) {
            moveCurrentBlockVertically();
        }
        else{
            clearLines();
        }

    }
    public void hardDrop(){
        while(!isCurrentBlockVerticallyColliding()){
            moveCurrentBlockVertically();
        }
        clearLines();

    }


    public void rotateCurrentBlock(){
        if(currentBlock.type < 6) {
            currentBlock.rotate1(gameBoard);
        }
        else if(currentBlock.type == 7){
            currentBlock.rotate2(gameBoard);
        }
    }


    private ArrayList<Integer> identifyFullRows() {
        ArrayList<Integer> fullRows = new ArrayList<>();

        for(int i = 0; i < gameBoard.length; i++){

            boolean fullRow = true;

            //Go through Row and check if its a fullRow
            for(int j = 0; j < gameBoard[0].length; j++){
                if(gameBoard[i][j] == null){
                    fullRow = false;
                    break;
                }
            }

            //Add Row to Arraylist if its a fullRow
            if(fullRow){
                fullRows.add(i);
            }

        }

        return fullRows;
    }
    public void clearLines(){
        ArrayList<Integer> fullRows = identifyFullRows();

        //Clear fullRows and move none fullRows the amount of fullRows below

        int countFullRowsBelow = 0;

        for(int i = gameBoard.length - 1; i >= 0; i--){

            if(fullRows.contains(i)){
                countFullRowsBelow += 1;
                for (int j = 0; j < gameBoard[0].length; j++){
                    gameBoard[i][j] = null;
                }

            }
            else{
                for (int j = 0; j < gameBoard[0].length; j++){
                    if(gameBoard[i][j] != null){
                        gameBoard[i][j].setAbsoluteRow(gameBoard[i][j].getAbsoluteRow() + countFullRowsBelow);
                        //System.out.print("");
                    }

                }
            }

        }
        if(!fullRows.isEmpty()) {
            updateGameBoard();
            gravity();
            fullRows = null;
        }
    }


    public void updateConnectionsToBottom(){
        //Erase all previous connection to bottom values
        for(Square [] array : gameBoard){
            for(Square s : array){
                if(s != null){
                    s.setConnectedToBottom(false);
                }

            }
        }


        for(Square s : gameBoard[Controller.ROWS-1]){
            if(s != null){
                //All Squares on the Ground (Last Row) are connected to bottom
                s.setConnectedToBottom(true);

                //All Squares on top of GroundSquares are connected to bottom
                if(gameBoard[s.getAbsoluteRow()-1][s.getAbsoluteCol()] != null){
                    gameBoard[s.getAbsoluteRow()-1][s.getAbsoluteCol()].setConnectedToBottom(true);
                }

            }

        }


        //Erste Reihe wurde bereits vollständig belegt, daher gameBoard.length - 2
        for(int i = gameBoard.length - 2; i > 0; i--) {
            for (int j = 0; j < gameBoard[0].length - 1; j++) { //Array soll nur bis zum vorletzten durchlaufen werden, da immer der Linke/Rechte nachbar geupdatet wird --> OutofBounds

                //Checking from both sides of the Board, because Squares can be distantly connected to either side

                Square currentLeft = gameBoard[i][j];
                Square currentRight = gameBoard[i][gameBoard[0].length - 1 - j];

                if (currentLeft != null) {
                    if (currentLeft.isConnectedToBottom()) {

                        //If one of the Squares is connected to bottom, so are its neighbours
                        if (gameBoard[i - 1][j] != null) {
                            gameBoard[i - 1][j].setConnectedToBottom(true);
                        }

                        if (j + 1 < gameBoard[0].length) {
                            if (gameBoard[i][j + 1] != null) {
                                gameBoard[i][j + 1].setConnectedToBottom(true);
                            }
                        }

                    }
                }

                if (currentRight != null) {
                    if (currentRight.isConnectedToBottom()) {
                        //If one of the Squares is connected to bottom, so are its neighbours (Top Square does not need to be set true again)
                        if (j - 1 >= 0) {
                            if (gameBoard[i][j - 1] != null) {
                                gameBoard[i][j - 1].setConnectedToBottom(true);
                            }
                        }

                    }
                }

            }

        }
        //Letze Reihe wird manuell belegt, da darüber keine Reihe sit, daher bringt die obere schleife eine Out of Bounds exception
//        for(int i = 0; i < gameBoard[0].length; i++){
//
//            Square currentLeft = gameBoard[0][i];
//            Square currentRight = gameBoard[0][gameBoard[0].length - 1 - i];
//
//            if(currentLeft.isConnectedToBottom()){
//
//            }
//
//        }

        for (Square[] array : gameBoard) {
            for (Square s : array) {
                if (s == null) System.out.print(0);
                else System.out.print(s.isConnectedToBottom());
            }
            System.out.println();
        }

    }
    public boolean allConnectedToBottom () {
        for (Square[] array : gameBoard) {
            for (Square s : array) {
                if (s != null) {
                    if (!s.isConnectedToBottom()) {
                        System.out.println("NOT ALL CONNECTED TO BOTTOM");
                        return false;
                    }
                }

            }
        }
        System.out.println("ALL CONNECTED TO BOTTOM");
        return true;
    }
    public void gravity(){

        //Method to drop down all Squares that dont (indirectly) touch the bottom

        while(!allConnectedToBottom()){

            updateConnectionsToBottom();

            for(int i = gameBoard.length - 1; i >= 0; i--){
                for(int j = 0; j < gameBoard[0].length; j++){

                    if(gameBoard[i][j] != null){
                        if(!gameBoard[i][j].isConnectedToBottom()){
                            gameBoard[i][j].setAbsoluteRow(gameBoard[i][j].getAbsoluteRow() + 1);
                        }
                    }

                }
            }

            updateGameBoard();
        }

    }


    public void updateGameBoard(){
        //System.out.println(gameBoard.length-1);
        for(int i = gameBoard.length-1; i >= 0; i--){
            for(int j = 0; j < gameBoard[0].length; j++){
                Square current = gameBoard[i][j];
                if(current != null){
                    //Blöcke werden nur geupdatet, wenn sie sich bewegen, da sie ansonsten auf null gesetzwerden würden
                    if(current.getAbsoluteRow() != i || current.getAbsoluteCol() != j){
                        gameBoard[current.getAbsoluteRow()][current.getAbsoluteCol()] = current;
                        gameBoard[i][j] = null;
                        //System.out.print("");
                    }

                }
            }
        }
    }






}
