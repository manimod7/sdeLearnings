#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
using namespace std;

const int MOD = 1e9 + 7;

int getMinErrors(string s, int x, int y) {
    int n = s.length();
    
    // dp[i][last] = minimum errors for first i characters, with last character being 'last'
    // last: 0 for '0', 1 for '1'
    vector<vector<long long>> dp(n + 1, vector<long long>(2, 1e18));
    
    dp[0][0] = dp[0][1] = 0;
    
    for (int i = 0; i < n; i++) {
        for (int last = 0; last < 2; last++) {
            if (dp[i][last] == 1e18) continue;
            
            if (s[i] == '0' || s[i] == '!') {
                // Place '0' at position i
                long long cost = dp[i][last];
                if (last == 1) cost += y; // "10" subsequence
                dp[i + 1][0] = min(dp[i + 1][0], cost);
            }
            
            if (s[i] == '1' || s[i] == '!') {
                // Place '1' at position i
                long long cost = dp[i][last];
                if (last == 0) cost += x; // "01" subsequence
                dp[i + 1][1] = min(dp[i + 1][1], cost);
            }
        }
    }
    
    return min(dp[n][0], dp[n][1]) % MOD;
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);
    
    string s;
    int x, y;
    cin >> s >> x >> y;
    
    cout << getMinErrors(s, x, y) << endl;
    
    return 0;
}