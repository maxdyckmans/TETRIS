import java.util.Comparator;

public class Square {
    private final int type;
    private int blockID;

    private final int relativeRow;
    private final int relativeCol;
    private int absoluteRow;
    private int absoluteCol;

    public Square (int blockID, int type, int relativeRow, int relativeCol){
        this.type = type;
        this.blockID = blockID;
        this.relativeRow = relativeRow;
        this.relativeCol = relativeCol;
    }

    public int getRelativeRow() {
        return relativeRow;
    }

    public int getRelativeCol() {
        return relativeCol;
    }

    public int getAbsoluteRow() {
        return absoluteRow;
    }

    public int getAbsoluteCol() {
        return absoluteCol;
    }

    public void setAbsoluteRow(int absoluteRow) {
        this.absoluteRow = absoluteRow;
    }

    public void setAbsoluteCol(int absoluteCol) {
        this.absoluteCol = absoluteCol;
    }

    public boolean isEmpty(){
        return type == 0;
    }

    public int getType() {
        return type;
    }

    public int getBlockID() {
        return blockID;
    }

    @Override
    public String toString() {
        return Integer.toString(type);
    }





    public static class Comparators {

        public static final Comparator<Square> ROW = (Square s1, Square s2) -> Integer.compare(s1.relativeRow, s2.relativeRow);
        public static final Comparator<Square> COLUMN = (Square s1, Square s2) -> Integer.compare(s1.relativeCol, s2.relativeCol);
    }

}
