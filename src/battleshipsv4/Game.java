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
    private boolean IS_PLAYERS_TURN = true;
    // turn = true for player, false for AI

    // ** not sure if we need this ** 
    //private boolean turn = IS_PLAYERS_TURN;
    private Board AI;
    private Board Player;

    private AI COM;
    private Ship ships;
    private String playerName;
    private boolean askedForName = false;
    private int playerTotalShots;
    private int AITotalShots;
    private char[][] dummy;
    private boolean playerFlag = false;
    private boolean AIFlag = false;

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

    public void setPlayerTotalShots() {
        for (int i = AI.getShipAmt(); i > 0; i--) {
            playerTotalShots += i;
        }

    }

    public void setAIToalShots() {
        for (int i = Player.getShipAmt(); i > 0; i--) {
            AITotalShots += i;
        }
    }

    //to decrease and bool to check for win
    public boolean decreasePlayerTotalShots() {
        if (playerTotalShots == 1) {
            AIFlag = true;
            return true;
        }
        playerTotalShots--;
        return false;
    }

    public boolean decreaseAIPlayerTotalShots() {
        if (AITotalShots == 1) {
            playerFlag = true;
            return true;
        }
        AITotalShots--;
        return false;
    }

    public void printDummy() {
        // print board
        for (int r = 0; r < dummy.length; r++) {
            for (int c = 0; c < dummy[0].length; c++) {
                System.out.println(dummy[r][c] + " ");
            }
            System.out.println();
        }

    }

//    private void nextTurn() {
//        if (turn) {
//            turn = IS_PLAYERS_TURN;
//        } else {
//            turn = ;
//        }
//    }
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

        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Welcome to BattleShip.");
            if (IS_PLAYERS_TURN) {
                if (playerName == null) {
                    askForName(sc);
                    askedForName = true;
                } // continue with the logic of the game
                else {
                    if (askedForName) {
                        System.out.println("Welcome " + playerName);
                        Player = keepBoard(sc);
                    }
                }
            }
            // ...

            if (playerFlag) {
                System.out.println("You've lost.");
                break;
            }
            if (AIFlag) {
                System.out.println("You've won");
                break;

            }
            if (IS_PLAYERS_TURN) {
                if (getCoordinatesAndShoot(sc)) {
                    decreaseAIPlayerTotalShots();
                    printDummy();
                    IS_PLAYERS_TURN = false;
                }
            } else {
                boolean t = COM.easy(Player);
                if (t) {
                    Player = COM.getBoard();
                } else {
                    System.out.println("AI missed.");
                }

            }
        }
    }

}
