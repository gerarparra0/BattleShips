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
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Board player = new Board(new int[]{8, 10});
        AI cpu = new AI();

        player.print();
        for (int i = 0; i < 10; i++) {
            while (!cpu.easy(player)) {
                player = cpu.getBoard();
            }
        }
        player.print();
    }

}
