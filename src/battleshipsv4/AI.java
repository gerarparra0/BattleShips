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

    private ArrayList<Integer[]> shotsFired;
    private Board currentBoard;
    private int shots;
    private Integer[] lastSuccessfulShot;
    private boolean lastShotSuccessful;
    private boolean firstTurn;

    // initialize components
    public AI() {
        shotsFired = new ArrayList<>();
        lastSuccessfulShot = new Integer[2];
        firstTurn = true;
        shots = 0;
    }

    private int[] newShot() {
        Random rand = new Random();

        return new int[]{rand.nextInt(currentBoard.getCols()),
            rand.nextInt(currentBoard.getRows())};
    }

    private void saveShot(int[] shot) {
        shotsFired.add(new Integer[]{shot[0], shot[1]});
    }

    //private boolean isPositionBounded(int[] pos)
    public boolean hard(Board player) {
        currentBoard = player;
        // if the shot was successful
        // the next turn, the AI is going to 
        // try horizontally and vertically

        if (lastShotSuccessful) {
            return true;
        } else {
            boolean result = shoot(currentBoard);

            if (result) {
                lastSuccessfulShot = shotsFired.get(--shots);
                lastShotSuccessful = true;
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean easy(Board player) {
        boolean result = shoot(player);
        
        if(result)
            lastSuccessfulShot = shotsFired.get(shots - 1);
        
        return result;
    }

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

    public Board getBoard() {
        return currentBoard;
    }

    public int[] getLastSuccessfulShot() {
        return new int[] {lastSuccessfulShot[0], lastSuccessfulShot[1]};
    }
}
