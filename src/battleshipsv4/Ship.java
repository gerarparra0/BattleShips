/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipsv4;

/**
 *
 * @author Gerardo
 */
public class Ship {

    // constants to access directions
    public static final boolean VERTICAL = false;
    public static final boolean HORIZONTAL = true;
    public static final char SHOT = 'X';
    public static final char EMPTY = 'O';

    private final int ROWS, COLS;
    private char[][] ship;
    private final int size;
    private boolean shipSunk = false;
    //the direction of the ship to be used by
    // isHorizontal();
    // true - horizontal
    // false - vertical
    private final boolean direction;
    // position on the board(x - columns, y - rows)
    private final int x, y;
    // keep track of the ship size
    private int currentShipSize;

    public Ship(int sz, boolean dir, int[] pos) {
        size = sz;
        direction = dir;
        x = pos[0];
        y = pos[1];
        currentShipSize = size;

        //horizontal
        if (direction == true) {
            ship = new char[1][size];
            ROWS = 1;
            COLS = size;
        } else {        // vertical
            ship = new char[size][1];
            ROWS = size;
            COLS = 1;
        }

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                ship[r][c] = 'O';
            }
        }
    }

    public boolean isHorizontal() {
        return direction == true;
    }

    private void decreaseShipSize() {
        if (!shipSunk) {
            if (currentShipSize == 1) {
                System.out.println("Ship sunk.");
                shipSunk = true;
            } else {
                currentShipSize--;
            }
        }
    }

    public boolean takeShot(int[] pos) {
        // try to take a shot in the array
        if (!isHit(pos)) {
            if (isHorizontal()) {
                ship[0][pos[1]] = 'X';
            } else {
                ship[pos[0]][0] = 'X';
            }

            decreaseShipSize();
            return true;
        } else {
            return false;
        }

    }

    public int getSize() {
        return size;
    }

    public int getRows() {
        return ROWS;
    }

    public int getCols() {
        return COLS;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private boolean isHit(int[] pos) {
        if (isHorizontal()) {
            return ship[0][pos[1]] == 'X';
        } else {
            return ship[pos[0]][0] == 'X';
        }
    }

    public char getValueAt(int[] pos) {
        if (isHorizontal()) {
            return ship[0][pos[1]];
        } else {
            return ship[pos[0]][0];
        }
    }

    public char[][] getShip() {
        return ship;
    }
}
