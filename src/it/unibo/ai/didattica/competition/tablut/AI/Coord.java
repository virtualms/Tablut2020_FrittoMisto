package it.unibo.ai.didattica.competition.tablut.AI;

import java.util.Objects;

/**
 *
 * @author E.Cerulo, V.M.Stanzione
 *
 */

public class Coord {

    private int row;
    private int col;

    public Coord(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }


    /**************utility*****************************/
    public int distanceFrom(Coord other){
        return Math.abs(this.getRow() - other.getRow()) + Math.abs(this.getCol() - other.getCol());
    }

    public boolean closeTo(Coord other){
        if(other.getCol() < 0 || other.getRow() < 0)
            return false;

        int res = this.getCol() - other.getCol() + this.getRow() - other.getRow();
        return (res == 1 || res == -1);
        //return Math.abs(this.getCol() - other.getCol() + this.getRow() - other.getRow()) == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return getRow() == coord.getRow() &&
                getCol() == coord.getCol();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getCol());
    }

    @Override
    public String toString() {
        return "Coord{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
