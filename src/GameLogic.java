public class GameLogic {
    public static final int START_SQUARE_ROW = 1;
    public static final int START_SQUARE_COL = 5;
    Square [][] gameBoard;
    Block currentBlock;

    public GameLogic(int cols, int rows){
        gameBoard = new Square[rows][cols];
    }

    public void initializeBoard(){
        //All cells are set to 0
//        for(int i = 0; i < gameBoard.length; i++){
//            for(int j = 0; j < gameBoard[0].length; j++){
//                gameBoard[i][j] = new Square(-1, 0, 0, 0);
//                gameBoard[i][j].setAbsoluteRow(i);
//                gameBoard[i][j].setAbsoluteCol(j);
//            }
//        }

        addBlock(1);
    }

    public Square[][] getUpdate(){
        if(isCurrentBlockVerticallyColliding()){
            if(!addBlock(1)){ //The addBlock method returns false, if the Block couldn't be added --> Game over --> return null
                return null;
            }
        }else {

            moveCurrentBlockVertically();
            moveCurrentBlockHorizontally(1);
        }



        return gameBoard;
    }

    public boolean addBlock(int type){

        currentBlock = new Block(type);
        boolean valid = true;

        //Checks if block can be placed
        for(Square s : currentBlock.blockStructureVerticallySorted){
            int absoluteRow = START_SQUARE_ROW + s.getRelativeRow();
            int absoluteCol = START_SQUARE_COL + s.getRelativeCol();
            if(gameBoard[absoluteRow][absoluteCol] == null){
                valid = false;
            }
            else if(!gameBoard[absoluteRow][absoluteCol].isEmpty()){
                valid = false;
            }
        }


        //Adds Block if possible
        if(valid){
            for(Square s : currentBlock.blockStructureVerticallySorted){
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

        for (Square s : currentBlock.blockStructureHorizontallySorted) {

            if (direction == -1) {
                if (s.getAbsoluteCol() == 0) {
                    return true;
                }
            }
            else if (direction == 1) {
                if (s.getAbsoluteCol() == gameBoard[0].length - 1) {
                    return true;
                }
            }else{
                Square nextSquare = gameBoard[s.getAbsoluteRow() + direction][s.getAbsoluteCol()];
                if (nextSquare.getType() != 0 && nextSquare.getBlockID() != s.getBlockID()) { //Check if Block collides with Anything one Square down (Which isnt part of the Block)
                    return true;
                }
            }








        }
        return false;
    }
    public void moveCurrentBlockHorizontally(int direction){



        if(!isCurrentBlockHorizontallyColliding(direction)){
            if(direction == -1){
                for(Square s: currentBlock.blockStructureHorizontallySorted.reversed()){


                    //Move the Square to the right
                    s.setAbsoluteCol(s.getAbsoluteCol() + direction);
                    gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol()] = s;

                    //Leave an empty Square behind
                    gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol() - direction] = new Square(-1, 0,0,0);
                    gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol() - direction].setAbsoluteRow(s.getAbsoluteRow());
                    gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol() - direction].setAbsoluteCol(s.getAbsoluteCol() - direction);
                }
            }


            if(direction == 1){
                for(Square s: currentBlock.blockStructureHorizontallySorted){

                    //Move the Square to the right
                    s.setAbsoluteCol(s.getAbsoluteCol() + direction);
                    gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol()] = s;

                    //Leave an empty Square behind
                    gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol() - direction] = new Square(-1, 0,0,0);
                    gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol() - direction].setAbsoluteRow(s.getAbsoluteRow());
                    gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol() - direction].setAbsoluteCol(s.getAbsoluteCol() - direction);
                }
            }

        }
    }



    public boolean isCurrentBlockVerticallyColliding(){
        for(Square s : currentBlock.blockStructureVerticallySorted){

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
        for(Square s: currentBlock.blockStructureVerticallySorted){
            s.setAbsoluteRow(s.getAbsoluteRow()+1);

            //Move the Square one down and leave an empty square behind
            gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol()] = s;
            gameBoard[s.getAbsoluteRow()-1][s.getAbsoluteCol()] = new Square(-1, 0,0,0);
            gameBoard[s.getAbsoluteRow()-1][s.getAbsoluteCol()].setAbsoluteRow(s.getAbsoluteRow()-1);
            gameBoard[s.getAbsoluteRow()-1][s.getAbsoluteCol()].setAbsoluteCol(s.getAbsoluteCol());
        }
    }

}
