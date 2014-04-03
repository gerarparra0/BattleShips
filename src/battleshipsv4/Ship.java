package battleshipsv4;

public class Ship {

    public static final boolean VERTICAL = false;
    public static final boolean HORIZONTAL = true;
    public static final char SHOT = 'X';
    public static final char EMPTY = 'O';

    private final int ROWS, COLS;
    private final int size;
    private final boolean direction;
    private boolean shipSunk = false;
    private char[][] ship;
    // position on the board(x - columns, y - rows)
    private final int x, y;
    private int currentShipSize;

    public Ship(int sz, boolean dir, int[] pos) {
        size = sz;
        direction = dir;
        x = pos[0];
        y = pos[1];
        currentShipSize = size;

        //horizontal
        if (direction) {
            ship = new char[1][size];
            ROWS = 1;
            COLS = size;
        } else { // vertical
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

    /**
     *
     * @return boolean Returns true if horizontal; false if vertical.
     */
    public boolean isHorizontal() {
        return direction == true;
    }

    /**
     * Decreases ship size and checks if ship will be sunk. If sunk, outputs
     * message to user.
     */
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

    /**
     * Tries to take shot
     *
     * @param pos index[0] for columns, [1] for rows
     * @return boolean returns true if hit, false if miss
     */
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

    /**
     *
     * @param pos index[0] for columns, [1] for rows
     * @return boolean Returns true if already hit
     */
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
