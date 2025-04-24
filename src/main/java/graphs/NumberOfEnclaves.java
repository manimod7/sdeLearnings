package graphs;

/*
Leetcode 1020: Number of Enclaves

🧠 Problem:
Given a binary matrix `grid` where:
- 0 = sea
- 1 = land

You can walk off the boundary of the grid only through land cells.
Return the number of land cells from which you **cannot** walk off the boundary — i.e., "enclaves".

➡️ Example:
Input:
grid = [[0,0,0,0],
        [1,0,1,0],
        [0,1,1,0],
        [0,0,0,0]]
Output: 3
Explanation: The three 1's in the middle are enclosed and cannot reach the boundary.

✅ Goal:
Count the number of land cells that cannot reach the boundary of the grid.

--------------------------------------------------
🛠️ Solution: DFS – Flood Fill from Boundaries

- Step 1: Any land cell (1) that touches the boundary can escape — we flood fill from all boundary 1's and mark them as sea (0).
- Step 2: After all reachable land is marked, the remaining 1's are enclaves.
- Step 3: Count the number of 1's left.

--------------------------------------------------
⏱ Time Complexity: O(M × N)
- Each cell is visited at most once

💾 Space Complexity: O(M × N)
- Recursion stack or visited matrix in worst case

*/

public class NumberOfEnclaves {
    public int numEnclaves(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Step 1: Flood fill all boundary-connected land (convert them to sea)
        for (int i = 0; i < rows; i++) {
            // Left and Right boundary columns
            if (grid[i][0] == 1) dfs(grid, i, 0);
            if (grid[i][cols - 1] == 1) dfs(grid, i, cols - 1);
        }

        for (int j = 0; j < cols; j++) {
            // Top and Bottom boundary rows
            if (grid[0][j] == 1) dfs(grid, 0, j);
            if (grid[rows - 1][j] == 1) dfs(grid, rows - 1, j);
        }

        // Step 2: Count remaining land cells (enclaves)
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 1) count++;
            }
        }

        return count;
    }

    // DFS to convert connected land to sea
    private void dfs(int[][] grid, int i, int j) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Out of bounds or water, return
        if (i < 0 || i >= rows || j < 0 || j >= cols || grid[i][j] != 1) {
            return;
        }

        // Mark land as visited (convert to sea)
        grid[i][j] = 0;

        // Visit all 4 directions
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }
}

