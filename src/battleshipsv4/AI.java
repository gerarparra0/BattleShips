/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipsv4;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Gerardo
 */
public class AI {

    // main objects of the AI's class
    private ArrayList<Integer[]> shotsFired;
    private Board currentBoard;

    // amount of shots fired (on the list)
    private int shots;

    // copy of the last successful shot
    private Integer[] lastSuccessfulShot;

    // first turn tracker
    private boolean firstTurn;

    /**
     * Initialize all objects and fields
     */
    public AI() {
        shotsFired = new ArrayList<>();
        lastSuccessfulShot = new Integer[2];
        firstTurn = true;
        shots = 0;
    }

    /**
     * Generate a new shot
     *
     * @return int[] - the shot generated {x, y}
     */
    private int[] newShot() {
        Random rand = new Random();

        return new int[]{rand.nextInt(currentBoard.getCols()),
            rand.nextInt(currentBoard.getRows())};
    }

    /**
     * Save a given shot on the list
     *
     * @param pos - shot to be saved
     */
    private void saveShot(int[] shot) {
        shotsFired.add(new Integer[]{shot[0], shot[1]});
    }

    /**
     * Hard AI. Done by generating a random number and comparing to shoot
     *
     *
     * @param player - shot to be saved
     * @return boolean - true if the AI hit
     */
    public boolean hard(Board player) {
        Random rand = new Random();
        int num = rand.nextInt(10);

        boolean result = shoot(player);

        if (num == 8) {
            while (!result) {
                result = shoot(currentBoard);
            }
        }

        return result;
    }

    /**
     * Insane AI. Done by generating a random number and comparing to shoot
     * (only 3 numbers generated, therefore a bigger chance of hitting)
     *
     *
     * @param player - current player's board
     * @return boolean - true if the AI hit
     */
    public boolean insane(Board player) {
        Random rand = new Random();
        int num = rand.nextInt(4);

        boolean result = shoot(player);

        if (num == 3) {
            while (!result) {
                result = shoot(currentBoard);
            }
        }

        return result;
    }

    /**
     * Easy AI. No checking whatsoever. Blindly shooting (actually very easy)
     *
     * @param player - current player's board
     * @return boolean - true if the AI hit
     */
    public boolean easy(Board player) {
        boolean result = shoot(player);

        if (result) {
            lastSuccessfulShot = shotsFired.get(shots - 1);
        }

        return result;
    }

    /**
     * Main shooting method. it is going to generate a new position to shoot at,
     * search if the shot was fired already, then shoot at the player's board.
     * It is going to create a copy of the modified player's board to be able to
     * do getBoard() later
     *
     * @param player - current player's board
     * @return boolean - true if the AI hit
     */
    private boolean shoot(Board player) {
        currentBoard = player;

        int[] shot = newShot();

        // if the AI is on the first turn
        // just save the shot
        if (firstTurn) {
            saveShot(shot);
            shots++;
            firstTurn = false;
        } else {
            // search on the list until 
            // the shot is a new one
            while (shotFired(shot)) {
                shot = newShot();
            }
            saveShot(shot);
            shots++;
        }

        return currentBoard.takeShot(shot);
    }

    /**
     * Check if a shot is already present on the list.
     *
     * @param pos - position to check for
     * @return boolean - true if the shot is present
     */
    private boolean shotFired(int[] pos) {
        // return true if found
        for (Integer[] i : shotsFired) {
            if (i[0] == pos[0] && i[1] == pos[1]) {
                return true;
            }
        }

        // shot not found
        return false;
    }

    /**
     * Retrieve a copy of the modified player's board
     *
     * @return Board - copy of board
     */
    public Board getBoard() {
        return currentBoard;
    }

    /**
     * Retrieve the last successful shot
     *
     * @return int[] - shot
     */
    public int[] getLastSuccessfulShot() {
        return new int[]{lastSuccessfulShot[0], lastSuccessfulShot[1]};
    }
}
