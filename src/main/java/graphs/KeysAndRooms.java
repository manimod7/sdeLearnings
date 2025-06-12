package graphs;

import java.util.*;

/**
 * LeetCode 841 - Keys and Rooms
 *
 * Problem: There are N rooms labeled 0..N-1. Each room i has keys to other rooms.
 * Starting from room 0, determine if we can visit all rooms.
 *
 * Solution: Simple DFS/BFS from room 0 following keys to mark visited rooms.
 */
public class KeysAndRooms {
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        int n = rooms.size();
        boolean[] vis = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        vis[0] = true;
        while (!stack.isEmpty()) {
            int room = stack.pop();
            for (int key : rooms.get(room)) {
                if (!vis[key]) { vis[key] = true; stack.push(key); }
            }
        }
        for (boolean v : vis) if (!v) return false;
        return true;
    }
}
