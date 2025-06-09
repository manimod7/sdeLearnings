package recursion;

import java.util.ArrayList;

public class TowerOfHanoi {

    public ArrayList<ArrayList<Integer>> towerOfHanoi1(int A) {
        ArrayList<ArrayList<Integer>> ans = new ArrayList();
        int n = A;
        helper(n,ans,1,3,2);
        return ans;

    }
    public void helper(int n, ArrayList<ArrayList<Integer>> moves, int from, int to, int help) {
        if(n==0)
            return;
        helper(n-1, moves, from, help, to);

        moves.add(new ArrayList<Integer>(){{
            add(n);
            add(from);
            add(to);
        }});

        helper(n-1,moves,help,to,from);
    }

    private int moveIndex = 0;  // Class field to track the current move index

    public int[][] towerOfHanoi(int A) {
        int totalMoves = (int) Math.pow(2, A) - 1;  // Calculate the number of moves
        int[][] moves = new int[totalMoves][3];     // Create a 2D array to store the moves
        solveHanoi(A, 1, 3, 2, moves);              // Start the recursive process
        moveIndex = 0;  // Reset moveIndex if the method might be called multiple times
        return moves;
    }

    private void solveHanoi(int n, int start, int end, int aux, int[][] moves) {
        if (n == 1) {
            moves[moveIndex++] = new int[]{n, start, end};
            return;
        }
        solveHanoi(n - 1, start, aux, end, moves);
        moves[moveIndex++] = new int[]{n, start, end};
        solveHanoi(n - 1, aux, end, start, moves);
    }
    //Problem Description
    //
    //In the classic problem of the Towers of Hanoi, you have 3 towers numbered from 1 to 3 (left to right) and A disks numbered from 1 to A (top to bottom) of different sizes which can slide onto any tower.
    //The puzzle starts with disks sorted in ascending order of size from top to bottom (i.e., each disk sits on top of an even larger one).
    //You have the following constraints:
    //
    //Only one disk can be moved at a time.
    //A disk is slid off the top of one tower onto another tower.
    //A disk cannot be placed on top of a smaller disk.
    //
    //You have to find the solution to the Tower of Hanoi problem.
    //You have to return a 2D array of dimensions M x 3, where M is the minimum number of moves needed to solve the problem.
    //In each row, there should be 3 integers (disk, start, end), where:
    //
    //disk - number of the disk being moved
    //start - number of the tower from which the disk is being moved
    //end - number of the tower to which the disk is being moved
}
