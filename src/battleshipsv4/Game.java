/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipsv4;

import java.util.Scanner;

/**
 *
 * @author gerardo.parra001
 */
public class Game {

    // create boards [2]
    // main loop everything takes place,
    // switch turns
    // implement ai
    // print messages
    // handle errors
    private final int PLAYER = 0;
    private final int CPU = 1;
    private Board AI;
    private Board Player;
    private Board board;
    private AI COM;
    private Ship ships;
    private String playerName;
    private boolean askedForName = false;
    private int turn = PLAYER;
    private char[][] dummy;

    public Game() {
        AI = new Board(new int[]{8, 10});
        Player = new Board(new int[]{8, 10});
        dummy = new char[AI.getRows()][AI.getCols()];

        // initialize dummy
        for (int i = 0; i < dummy.length; i++) {
            for (int j = 0; j < dummy[0].length; j++) {
                dummy[i][j] = Board.WATER;
            }
        }

    }

    public void printDummy() {
        // print board
        for (int r = 0; r < dummy.length; r++) {
            for (int c = 0; c < dummy[0].length; c++) {
                System.out.println(dummy[r][c] +" ");
            }
            System.out.println();
        }

    }

    private void nextTurn() {
        if (turn == PLAYER) {
            turn = CPU;
        } else {
            turn = PLAYER;
        }
    }

    private void askForName(Scanner sc) {
        System.out.println("Enter your name");
        //String word = JOptionPane.showInputDialog("Enter your name");

        if (sc.hasNext()) {
            playerName = sc.next();
        }
    }

    private boolean getCoordinatesAndShoot(Scanner sc) {
        int x, y;

        System.out.println("Enter the coordinates to shoot");
        //String word = JOptionPane.showInputDialog("Enter the coordinates to shoot");

        if (sc.hasNextInt()) {
            x = sc.nextInt();

            if (sc.hasNextInt()) {
                y = sc.nextInt();

                int[] pos = {x, y};
                boolean result = AI.takeShot(pos);

                if (result) {
                    dummy[pos[1]][pos[0]] = Ship.SHOT;
                }
            }
        }
        
        //String word = JOptionPane.showInputDialog("Enter a valid coordinate");
        return false;
    }

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
        }

        return actualBoard;
    }

    private void drawBoards() {
        printDummy();
        System.out.println("--------------------------------");
        Player.print();
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Battleships");

        boolean finished = false;

        while (!finished) {

            // make sure we asked for the name
            if (playerName == null) {
                askForName(sc);
                askedForName = true;
            } // continue with the logic of the game
            else {
                if (askedForName) {
                    System.out.println("Welcome " + playerName);
                    Player = keepBoard(sc);
                }
                askedForName = false;       // do not repeat again


                // first turn is the player

                int playerScore = 0;
                int aiScore = 0;

                while (true) {
                    if (turn == PLAYER) {
                        System.out.println(playerName + "'s turn");

                        // ask for coordinates to shoot
                        //coordinates in main
                        boolean result = getCoordinatesAndShoot(sc);

                        // the shot was successfull, continue
                        if (result) {
                            //player coordinates hit ai's ship
                            //fix scores
                            playerScore++;
                            if (playerScore == 3) {
                                System.out.println(playerName + " wins");
                                break;
                            }

                            System.out.println("Great!");
                            System.out.println("AI's turn, hold on tight!");
                            nextTurn();
                        } // if the shot was not successfull, ask for the coordinates again
                        System.out.println("Missed");
                        nextTurn();
                    } else {
                        System.out.println("AI's turn");
                        boolean result = COM.easy(Player);
                        //if ai hits players ship
                        if (result) {
                            // update the dummy board with an
                            // X on the last position shot
                            int[] lastShot = COM.getLastSuccessfulShot();
                            Player = COM.getBoard();
                            System.out.println("AI hits your ship");
                            dummy[lastShot[1]][lastShot[0]] = Ship.SHOT;

                            aiScore++;
                            if (aiScore == 3) {
                                System.out.println("AI wins");
                                break;
                            }
                        } else {
                            System.out.println("AI missed");
                            nextTurn();
                        }
                    }
                    // inform the player if he hit or miss and update the board
                    // CPU's turn
                    // inform the user if the CPU missed or hit
                    // update boards 

                }

            }
        }
    }
}
