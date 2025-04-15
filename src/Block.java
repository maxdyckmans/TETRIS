import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Block {
    public static int blockCount = 0;
    public int blockID;
    public int type;
    public ArrayList<Square> blockStructureVerticallySorted = new ArrayList<Square>();
    public ArrayList<Square> blockStructureHorizontallySorted;
    //Constructor
    public Block(int type){
        this.type = type;
        blockID = blockCount;
        blockCount++;


        blockStructureVerticallySorted.add(new Square(blockID, type, 0, 0));
        switch(type){
            case 1:

                blockStructureVerticallySorted.add(new Square(blockID,type, 1, 0));
                blockStructureVerticallySorted.add(new Square(blockID,type, -1, 0));
                blockStructureVerticallySorted.add(new Square(blockID,type, -1, 1));

                break;

            case 2:
                blockStructureVerticallySorted.add(new Square(blockID,type, 1, 0));
                blockStructureVerticallySorted.add(new Square(blockID,type, -1,0 ));
                blockStructureVerticallySorted.add(new Square(blockID,type, -1, -1));

                break;

            case 3:

                blockStructureVerticallySorted.add(new Square(blockID,type, 1, 0));
                blockStructureVerticallySorted.add(new Square(blockID,type, 0, 1));
                blockStructureVerticallySorted.add(new Square(blockID,type, -1, 1));
                break;

            case 4:
                blockStructureVerticallySorted.add(new Square(blockID,type, 1, 0));
                blockStructureVerticallySorted.add(new Square(blockID,type, -1, 1));
                blockStructureVerticallySorted.add(new Square(blockID,type, -1, 0));

                break;

            case 5:
                blockStructureVerticallySorted.add(new Square(blockID,type, 0, -1));
                blockStructureVerticallySorted.add(new Square(blockID,type, 0, 1));
                blockStructureVerticallySorted.add(new Square(blockID,type, -1, 0));

                break;

            case 6:

                blockStructureVerticallySorted.add(new Square(blockID,type, 0, 1));
                blockStructureVerticallySorted.add(new Square(blockID,type, -1, 0));
                blockStructureVerticallySorted.add(new Square(blockID,type, -1, 1));
                break;

            case 7:
                blockStructureVerticallySorted.add(new Square(blockID,type, 2, 0));
                blockStructureVerticallySorted.add(new Square(blockID,type, 1, 0));
                blockStructureVerticallySorted.add(new Square(blockID,type, -1, 0));

                break;
        }
        //Creates Arraylist with Squares sorted in reverse by their ROW, for moving down
        Collections.sort(blockStructureVerticallySorted, Square.Comparators.ROW.reversed());

        //Creates Arraylist with Squares sorted by their COLUMN, for moving left (reversed) and right
        blockStructureHorizontallySorted = (ArrayList<Square>) blockStructureVerticallySorted.clone();
        Collections.sort(blockStructureHorizontallySorted, Square.Comparators.COLUMN);


        for(Square s: blockStructureVerticallySorted){
            System.out.println("ROW: " + s.getRelativeRow()+ " COLUMN: "+ s.getRelativeCol());
        }

    }


}
