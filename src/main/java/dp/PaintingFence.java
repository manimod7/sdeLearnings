package dp;

/**
 * Painting Fence Problem
 *
 * Problem: Given n fences and k colors, count ways to paint such that no more
 * than two adjacent fence posts have the same color.
 *
 * Solution: DP with two cases: same and diff for each post.
 *
 * Time: O(n)
 * Space: O(1)
 */
public class PaintingFence {
    public long countWays(int n, int k){
        if(n==1) return k;
        long same=0,diff=k,total=k;
        for(int i=2;i<=n;i++){
            long newSame=diff;
            diff=(total)*(k-1);
            total=newSame+diff;
        }
        return total;
    }
}
