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
    private boolean playerZero = false;
    private boolean AIZero = false;

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

    }

    //calculates total shots required to win
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

    //will decrease and Flags will = true when total shots = 0
    public void decreasePlayerTotalShots() {

        --playerTotalShots;
    }
    public int getPlayerTotalShots()
    {
        return playerTotalShots;
    }
    public int getAITotalShots()
    {
        return AITotalShots;
    }

    public void decreaseAITotalShots() {

        --AITotalShots;
    }

    public boolean getAIZero() {
        return AIZero;
    }

    public boolean getPlayerZero() {
        return playerZero;
    }

    public void printDummy() {
        // print board
        for (int r = 0; r < dummy.length; r++) {
            for (int c = 0; c < dummy[0].length; c++) {
                System.out.print(dummy[r][c] + " ");
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
            setPlayerTotalShots();
            setAIToalShots();
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

                if (t) {
                    System.out.println("You Hit the AI!");
                    decreaseAITotalShots();
                    
                } else {
                    System.out.println("You Missed.");
                }

                IS_PLAYERS_TURN = false;
            } else {
                boolean t = COM.easy(Player);
                if (t) {
                    System.out.println("AI hit your ship!");
                    decreasePlayerTotalShots();
                    Player = COM.getBoard();
                } else {
                    System.out.println("AI missed.");
                }

                IS_PLAYERS_TURN = true;
            }

           if(getAITotalShots()== 0)
            {
                drawBoards();
                System.out.println("You've won!");
                break;
            } else if(getPlayerTotalShots() == 0)
            {   drawBoards();
                System.out.println("You've lost");
                break;
                
            }
             System.out.println(getAITotalShots());
             System.out.println(getPlayerTotalShots());
        }
    }

}
