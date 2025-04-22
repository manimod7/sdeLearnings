package graphs;

public class NumberOfIslands {
        public int numIslands(char[][] grid) {
            if (grid == null || grid.length == 0) return 0;

            int count = 0;
            int rows = grid.length;
            int cols = grid[0].length;

            // Loop through all cells
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    // Start DFS if you find land
                    if (grid[i][j] == '1') {
                        count++;
                        dfs(grid, i, j, rows, cols);
                    }
                }
            }
            return count;
        }

        private void dfs(char[][] grid, int i, int j, int rows, int cols) {
            // Base case: out of bounds or not land
            if (i < 0 || i >= rows || j < 0 || j >= cols || grid[i][j] != '1') return;

            // Mark as visited
            grid[i][j] = '0';

            // Explore all 4 directions
            dfs(grid, i + 1, j, rows, cols); // down
            dfs(grid, i - 1, j, rows, cols); // up
            dfs(grid, i, j + 1, rows, cols); // right
            dfs(grid, i, j - 1, rows, cols); // left
        }
}
