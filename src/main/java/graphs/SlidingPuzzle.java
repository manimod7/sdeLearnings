package graphs;

import java.util.*;

/**
 * LeetCode 773 - Sliding Puzzle
 *
 * Problem: Given a 2x3 board, return the minimum number of moves to reach the
 * solved state [[1,2,3],[4,5,0]]. Return -1 if unsolvable.
 *
 * Solution: Use BFS over board states with String representation and neighbor
 * transitions from moving the blank space.
 */
public class SlidingPuzzle {
    private static final String TARGET = "123450";
    private static final int[][] ADJ = {{1,3},{0,2,4},{1,5},{0,4},{1,3,5},{2,4}};
    public int slidingPuzzle(int[][] board) {
        String start = boardToString(board);
        Queue<String> q = new ArrayDeque<>();
        q.offer(start);
        Map<String,Integer> dist = new HashMap<>();
        dist.put(start,0);
        while(!q.isEmpty()){
            String cur=q.poll();
            int d=dist.get(cur);
            if(cur.equals(TARGET))return d;
            int zero=cur.indexOf('0');
            for(int nxt:ADJ[zero]){
                String next=swap(cur,zero,nxt);
                if(!dist.containsKey(next)){dist.put(next,d+1);q.offer(next);} 
            }
        }
        return -1;
    }
    private String boardToString(int[][] b){return ""+b[0][0]+b[0][1]+b[0][2]+b[1][0]+b[1][1]+b[1][2];}
    private String swap(String s,int i,int j){char[] a=s.toCharArray(); char t=a[i]; a[i]=a[j]; a[j]=t; return new String(a);}   
}
