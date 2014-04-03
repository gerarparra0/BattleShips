package battleshipsv4;

import java.util.Random;

public class Board {

    //public static final char WATER = '*';
    private final int ROWS, COLS;

    private final int SHIP_AMT = 4;
    private char[][] board;
    private Ship[] ships;           // 3 ships
    private final int MAX_SHIP_SIZE = 4;
    public final static char WATER = '~';

    public Board(int[] size) {
        ROWS = size[0];
        COLS = size[1];

        board = new char[ROWS][COLS];

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c] = '*';
            }
        }

        // randomize and initialize ships
        randomizeShips();
    }
    
    public int getShipAmt() {return SHIP_AMT;}

    public boolean takeShot(int[] pos) {

        try {
            if (board[pos[1]][pos[0]] == 'O') {
                // calculate which ship is there???
                for (Ship ship : ships) {
                    if (ship.isHorizontal()) {
                        // fund the ship
                        if ((pos[0] >= ship.getX() && pos[0] < ship.getX() + ship.getSize())
                                && ship.getY() == pos[1]) {
                            return ship.takeShot(new int[]{0, pos[0] - ship.getX()});
                        }
                    } else {
                        if ((pos[1] >= ship.getY() && pos[1] < ship.getY() + ship.getSize())
                                && ship.getX() == pos[0]) {
                            return ship.takeShot(new int[]{pos[1] - ship.getY(), 0});
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid index.");
            return false;
        }
        return false;
    }

    public void updateBoard(Ship ship) {
        char[][] s = ship.getShip();

        for (int r = 0, rB = ship.getY(); r < s.length; r++) {
            for (int c = 0, cB = ship.getX(); c < s[0].length; c++) {
                if (ship.isHorizontal()) {
                    board[rB][cB++] = s[r][c];
                } else {
                    board[rB++][cB] = s[r][c];
                }
            }
        }
    }

    public void updateBoard() {
        for (Ship ship : ships) {
            updateBoard(ship);
        }
    }

    public void print() {

        // copy ships into board
        updateBoard();

        // print board
        for (int c = 0; c < getCols(); c++) {
            if (c == 0) {
                System.out.print("  ");
            }
            System.out.print(c + " ");
        }

        System.out.println();

        for (int r = 0; r < getRows(); r++) {
            System.out.print(r + " ");
            for (int c = 0; c < getCols(); c++) {
                System.out.print(board[r][c] + " ");
            }
            System.out.println();
        }
    }

    public int[] getMatrix() {
        int[] t = {ROWS, COLS};
        return t;
    }

    // return false if the ships overlap, true otherwise
    private void randomizeShips() {
        Random rand = new Random();
        int x = rand.nextInt(getCols()),
                y = rand.nextInt(getRows());
        boolean direction = rand.nextBoolean();
        int shipSize = MAX_SHIP_SIZE;
        ships = new Ship[SHIP_AMT];

        boolean nextShip;

        for (int i = 0; i < ships.length; i++) {
            // until the current ship is not correctly positioned
            /// and does not overlap any other
            // continue generating a new position
            nextShip = false;
            while (!nextShip) {
                // if the ship is the first one, do not
                // check for overlapping
                if (i == 0) {
                    while (!isPositionInBounds(new int[]{x, y}, shipSize, direction)) {
                        x = rand.nextInt(getCols());
                        y = rand.nextInt(getRows());
                        direction = rand.nextBoolean();
                    }

                    ships[i] = new Ship(shipSize--, direction, new int[]{x, y});
                    nextShip = true;
                } else {
                    boolean isValid = false;

                    // check if there is a ship in the way, if there is
                    // re-generate
                    while (!isValid) {
                        x = rand.nextInt(getCols());
                        y = rand.nextInt(getRows());
                        direction = rand.nextBoolean();

                        while (!isPositionInBounds(new int[]{x, y}, shipSize, direction)) {
                            x = rand.nextInt(getCols());
                            y = rand.nextInt(getRows());
                            direction = rand.nextBoolean();
                        }

                        // search for other O's
                        if (direction == Ship.HORIZONTAL) {
                            boolean shipFound = false;

                            for (int c = x; c < (x + shipSize); c++) {
                                if (board[y][c] == 'O') {
                                    shipFound = true;
                                    break;
                                }
                            }

                            // create the ship and continue
                            if (!shipFound) {
                                ships[i] = new Ship(shipSize--, direction, new int[]{x, y});
                                isValid = true;
                            }
                        } else {
                            boolean shipFound = false;

                            for (int r = y; r < y + shipSize; r++) {
                                if (board[r][x] == 'O') {
                                    shipFound = true;
                                    break;
                                }
                            }

                            // create the ship and continue
                            if (!shipFound) {
                                ships[i] = new Ship(shipSize--, direction, new int[]{x, y});
                                isValid = true;
                            }
                        }
                    }

                    // pass to next ship
                    nextShip = true;
                }
            }

            // commit changes after each generation
            // in order to avoid overlaps
            updateBoard(ships[i]);
        }
    }

    private char getValueAt(int[] pos) {
        return board[pos[1]][pos[0]];
    }

    public int getRows() {
        return ROWS;
    }

    public int getCols() {
        return COLS;
    }

    private boolean isPositionInBounds(int[] pos, int s, boolean dir) {
        if (dir == Ship.HORIZONTAL) {
            if ((pos[0] + s) < getCols()) {
                return true;
            }
        } else {
            if ((pos[1] + s) < getRows()) {
                return true;
            }
        }

        return false;
    }
}
