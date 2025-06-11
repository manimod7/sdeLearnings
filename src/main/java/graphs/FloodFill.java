package graphs;

/**
 * LeetCode 733 - Flood Fill
 *
 * Problem: Starting from a pixel (sr, sc) in an image, change the color of all
 * connected pixels of the same color to a new color.
 *
 * Solution: Perform DFS/BFS from the starting pixel changing colors.
 */
public class FloodFill {
    public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        int orig = image[sr][sc];
        if (orig == newColor) return image;
        dfs(image, sr, sc, orig, newColor);
        return image;
    }
    private void dfs(int[][] img, int r, int c, int orig, int newColor) {
        if (r<0||r>=img.length||c<0||c>=img[0].length||img[r][c]!=orig) return;
        img[r][c] = newColor;
        dfs(img,r+1,c,orig,newColor);
        dfs(img,r-1,c,orig,newColor);
        dfs(img,r,c+1,orig,newColor);
        dfs(img,r,c-1,orig,newColor);
    }
}
