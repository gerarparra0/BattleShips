package battleshipsv4;

import java.util.Scanner;

public class Game {

    // who's turn is it?
    private boolean IS_PLAYERS_TURN;

    // exit flag
    private boolean EXIT;

    // main objects of the game
    private Board AI;
    private Board Player;
    private AI COM;

    // keep track of stuff
    private String playerName;
    private int playerTotalShots;
    private int AITotalShots;

    // dummy board the player will see
    private char[][] dummy;

    // check for loosers
    private boolean playerZero;
    private boolean AIZero;

    /**
     * Creates and initializes all objects in board
     */
    public Game() {
        AI = new Board(new int[]{8, 10});
        Player = new Board(new int[]{8, 10});
        dummy = new char[AI.getRows()][AI.getCols()];
        COM = new AI();

        // initialize dummy
        for (int i = 0; i < dummy.length; i++) {
            for (int j = 0; j < dummy[0].length; j++) {
                dummy[i][j] = Board.WATER;
            }
        }

        EXIT = false;
        IS_PLAYERS_TURN = true;

        playerZero = false;
        AIZero = false;
    }

    /**
     * calculates total shots required to win for Player
     */
    private void setPlayerTotalShots() {
        for (int i = AI.getShipAmt(); i > 0; i--) {
            playerTotalShots += i;
        }

    }

    /**
     * calculates total shots required to win for AI
     */
    private void setAIToalShots() {
        for (int i = Player.getShipAmt(); i > 0; i--) {
            AITotalShots += i;
        }
    }

    /**
     * decreases the amount of shots the player has left
     */
    private void decreasePlayerTotalShots() {
        --playerTotalShots;
    }

    /**
     *
     * @return int - amount of shots the player has
     */
    private int getPlayerTotalShots() {
        return playerTotalShots;
    }

    /**
     *
     * @return int - amount of shots the AI has
     */
    private int getAITotalShots() {
        return AITotalShots;
    }

    /**
     * decreases the amount of shots the AI has left
     */
    private void decreaseAITotalShots() {
        --AITotalShots;
    }

    /**
     * the AI has zero shots left
     *
     * @return true - AIZero
     */
    private boolean getAIZero() {
        return AIZero;
    }

    /**
     * the player has zero shots left
     *
     * @return true - AIZero
     */
    private boolean getPlayerZero() {
        return playerZero;
    }

    /**
     * print a dummy board to show the player instead of printing the actual
     * AI's board
     */
    private void printDummy() {
        for (char[] arr : dummy) {
            for (char c : arr) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    /**
     * This method asks the user for his/her name
     *
     * @param sc - Current scanner to get values from user
     *
     */
    private void askForName(Scanner sc) {
        System.out.println("Enter your name");
        //String word = JOptionPane.showInputDialog("Enter your name");

        if (sc.hasNext()) {
            playerName = sc.next();
        }
    }

    /**
     * Gets coordinates from the player to shoot at the AI
     *
     * @param sc - Current scanner to get values from user
     * @return boolean - true if player hit, false otherwise
     */
    private boolean getCoordinatesAndShoot(Scanner sc) {
        int x, y;

        System.out.println("Enter the coordinates to shoot");
        //String word = JOptionPane.showInputDialog("Enter the coordinates to shoot");

        if (sc.hasNext("exit") || sc.hasNext("Exit")) {
            EXIT = true;
            return false;
        }

        else if (sc.hasNextInt()) {
            x = sc.nextInt();

            if (sc.hasNextInt()) {
                y = sc.nextInt();

                int[] pos = {x, y};
                boolean result = AI.takeShot(pos);
                try {
                    if (result) {
                        dummy[pos[1]][pos[0]] = Ship.SHOT;
                        return true;
                    } else {
                        dummy[pos[1]][pos[0]] = 'M';

                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Invalid coordinate");
                    getCoordinatesAndShoot(sc);
                }
            }
        }
        
        else
            sc.next();

        return false;
    }

    /**
     * Asks the player if he/she likes given configuration of the board if so,
     * keep it
     *
     * @param sc - Current scanner to get values from user
     * @return Board - the board the player likes
     */
    private Board keepBoard(Scanner sc) {
        Board actualBoard = null;

        String res = "no";

        while (!res.equals("yes")) {
            //Creating new player board
            Board t = new Board(new int[]{8, 10});
            t.print();
            System.out.println("Do you like this setup?");
            res = sc.next();
            res = res.toLowerCase();
            actualBoard = t;
            setPlayerTotalShots();
            setAIToalShots();
        }

        return actualBoard;
    }

    /**
     * Draw the dummy board along with the player's board
     */
    private void drawBoards() {
        printDummy();
        System.out.println("--------------------------------");
        Player.print();
    }

    /**
     * Main method of the Game class. All the game logic happens here
     */
    public void run() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to BattleShip.");

        if (playerName == null) {
            askForName(sc);
        } // continue with the logic of the game

        System.out.println("Welcome " + playerName);
        Player = keepBoard(sc);

        while (true) {
            if (IS_PLAYERS_TURN) {
                drawBoards();
                boolean t = getCoordinatesAndShoot(sc);

                if (EXIT) {
                    System.out.println("Thanks for playing. Bye");
                    break;
                }

                if (t) {
                    System.out.println("You Hit the AI!");
                    decreaseAITotalShots();

                } else {
                    System.out.println("You Missed.");
                }

                IS_PLAYERS_TURN = false;
            } else {
                boolean t = COM.insane(Player);
                if (t) {
                    System.out.println("AI hit your ship!");
                    decreasePlayerTotalShots();
                    Player = COM.getBoard();
                } else {
                    System.out.println("AI missed.");
                }

                IS_PLAYERS_TURN = true;
            }

            if (getAITotalShots() == 0) {
                drawBoards();
                System.out.println("You've won!");
                break;
            } else if (getPlayerTotalShots() == 0) {
                drawBoards();
                System.out.println("You've lost");
                break;

            }
            System.out.println(getAITotalShots());
            System.out.println(getPlayerTotalShots());
        }
    }
}
