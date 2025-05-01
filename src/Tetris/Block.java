package Tetris;

import java.util.ArrayList;


public class Block {
    public static int blockCount = 0;
    public int blockID;
    public int type;
    public ArrayList<Square> blockStructureSortedByRow = new ArrayList<Square>();
    public ArrayList<Square> blockStructureSortedByCol;
    public Square center;
    //Constructor
    public Block(int type){

        this.type = type;
        blockID = blockCount;
        blockCount++;

        center = new Square(blockID, type, 0, 0);
        blockStructureSortedByRow.add(center);

        switch(type){
            case 1:

                blockStructureSortedByRow.add(new Square(blockID,type, 1, 0));
                blockStructureSortedByRow.add(new Square(blockID,type, -1, 0));
                blockStructureSortedByRow.add(new Square(blockID,type, -1, 1));

                break;

            case 2:
                blockStructureSortedByRow.add(new Square(blockID,type, 1, 0));
                blockStructureSortedByRow.add(new Square(blockID,type, -1,0 ));
                blockStructureSortedByRow.add(new Square(blockID,type, -1, -1));

                break;

            case 3:

                blockStructureSortedByRow.add(new Square(blockID,type, 1, 0));
                blockStructureSortedByRow.add(new Square(blockID,type, 0, 1));
                blockStructureSortedByRow.add(new Square(blockID,type, -1, 1));
                break;

            case 4:

                blockStructureSortedByRow.add(new Square(blockID,type, -1, -1));
                blockStructureSortedByRow.add(new Square(blockID,type, 0, -1));
                blockStructureSortedByRow.add(new Square(blockID,type, 1, 0));
                break;

            case 5:
                blockStructureSortedByRow.add(new Square(blockID,type, 0, -1));
                blockStructureSortedByRow.add(new Square(blockID,type, 0, 1));
                blockStructureSortedByRow.add(new Square(blockID,type, -1, 0));

                break;

            case 6:

                blockStructureSortedByRow.add(new Square(blockID,type, 0, 1));
                blockStructureSortedByRow.add(new Square(blockID,type, -1, 0));
                blockStructureSortedByRow.add(new Square(blockID,type, -1, 1));
                break;

            case 7:
                blockStructureSortedByRow.add(new Square(blockID,type, 2, 0));
                blockStructureSortedByRow.add(new Square(blockID,type, 1, 0));
                blockStructureSortedByRow.add(new Square(blockID,type, -1, 0));

                break;
        }


        //Creates Arraylist with Squares sorted in reverse by their ROW, for moving down
        blockStructureSortedByRow.sort(Square.Comparators.ROW.reversed());

        //Creates Arraylist with Squares sorted by their COLUMN, for moving left (reversed) and right
        blockStructureSortedByCol = (ArrayList<Square>) blockStructureSortedByRow.clone();
        blockStructureSortedByCol.sort(Square.Comparators.COLUMN);

    }

    public void rotate1(Square [][] gameBoard){

        //The rotation is saved in an Array so it can be checked if the rotation is valid, before executing the Rotation
        int [][] newRelativePositions = new int[blockStructureSortedByRow.size()][2];
        int [][] newAbsolutePositions = new int[blockStructureSortedByRow.size()][2];

        for(int i = 0; i < blockStructureSortedByRow.size(); i++){

            Square s = blockStructureSortedByRow.get(i);

            //Rotation for all Blocks except LongBlock and SquareBlock

            if(s.getRelativeRow() == 0){
                if(s.getRelativeCol() != 0){
                    //Left and right square
                    newRelativePositions[i][0] = s.getRelativeCol(); //Swap previous Row with Column
                    newRelativePositions[i][1] = 0; //New RelativeCol is equal to the old relative Row = 0
                }else{
                    //Center square stays as is
                    newRelativePositions[i][0] = 0;
                    newRelativePositions[i][1] = 0;
                }
            }
            else{
                if (s.getRelativeCol() == 0){
                    //Top and Bottom Tetris.Square
                    newRelativePositions[i][0] = 0; //new Row is old Column (0)
                    newRelativePositions[i][1] = -s.getRelativeRow(); //New column is negative old row
                }
                else{
                    //Edges
                    if(s.getRelativeRow() == s.getRelativeCol()){
                        //Bottom Right and Top Left Tetris.Square
                        newRelativePositions[i][0] = s.getRelativeRow(); //The Row stays the Same
                        newRelativePositions[i][1] = -s.getRelativeCol(); //Column becomes the opposite
                    }
                    else{
                        //Top Right and bottom Left Tetris.Square
                        newRelativePositions[i][0] = -s.getRelativeRow(); //Row becomes the opposite
                        newRelativePositions[i][1] = s.getRelativeCol(); //Column stays the same
                    }
                }
            }

        }


        boolean valid = isRotationValid(gameBoard, newRelativePositions, newAbsolutePositions);
        if(valid){
            rotate(newRelativePositions, newAbsolutePositions);
            //Update gameBoard
            for(Square s : blockStructureSortedByRow){
                gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol()] = s;
            }
        }



    }

    public void rotate2(Square [][] gameBoard){
        int [][] newRelativePositions = new int[blockStructureSortedByRow.size()][2];
        int [][] newAbsolutePositions = new int[blockStructureSortedByRow.size()][2];

        for(int i = 0; i < blockStructureSortedByRow.size(); i++){
            Square s = blockStructureSortedByRow.get(i);
            if(s.getRelativeRow() == 0){
                newRelativePositions[i][0] = -s.getRelativeCol();
                newRelativePositions[i][1] = 0;
            }
            if(s.getRelativeCol() == 0){
                newRelativePositions[i][0] = 0;
                newRelativePositions[i][1] = -s.getRelativeRow();
            }

        }

        boolean valid = isRotationValid(gameBoard, newRelativePositions, newAbsolutePositions);

        if(valid){
            rotate(newRelativePositions, newAbsolutePositions);

            //Update gameBoard
            for(Square s : blockStructureSortedByRow){
                gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol()] = s;
            }
        }


    }

    private void rotate(int[][] newRelativePositions, int[][] newAbsolutePositions) {
        for(int i = 0; i < newRelativePositions.length; i++){
            blockStructureSortedByRow.get(i).setAbsoluteRow(newAbsolutePositions[i][0]);
            blockStructureSortedByRow.get(i).setAbsoluteCol(newAbsolutePositions[i][1]);

            blockStructureSortedByRow.get(i).setRelativeRow(newRelativePositions[i][0]);
            blockStructureSortedByRow.get(i).setRelativeCol(newRelativePositions[i][1]);
        }
        blockStructureSortedByRow.sort(Square.Comparators.ROW.reversed());
        blockStructureSortedByCol.sort(Square.Comparators.COLUMN);



    }


    private boolean isRotationValid(Square[][] gameBoard, int[][] newRelativePositions, int[][] newAbsolutePositions) {
        for(int i = 0; i < newRelativePositions.length; i++){

            newAbsolutePositions[i][0] = center.getAbsoluteRow() + newRelativePositions[i][0];
            newAbsolutePositions[i][1] = center.getAbsoluteCol() + newRelativePositions[i][1];

            int row = newAbsolutePositions[i][0];
            int col = newAbsolutePositions[i][1];

            if(row < 0 || row >= gameBoard.length - 1||col >= gameBoard[0].length-1|| col < 0){
                return false;
            }
            else if (gameBoard[row][col] != null && gameBoard[row][col].getBlockID() != blockID){
                return false;
            }
        }


        for(Square s : blockStructureSortedByRow){
            gameBoard[s.getAbsoluteRow()][s.getAbsoluteCol()] = null;
        }
        return true;
    }


}
