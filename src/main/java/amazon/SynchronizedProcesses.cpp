#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

int getMinimumRemovals(vector<int>& starts, vector<int>& ends) {
    int n = starts.size();
    if (n <= 1) return 0;
    
    // Sort all start times and end times separately
    vector<int> sortedStarts = starts;
    vector<int> sortedEnds = ends;
    sort(sortedStarts.begin(), sortedStarts.end());
    sort(sortedEnds.begin(), sortedEnds.end());
    
    int maxOverlaps = 1; // At least one process can be kept
    
    // For each interval i as potential pivot
    for (int i = 0; i < n; i++) {
        // Count intervals that overlap with interval i [starts[i], ends[i]]
        // An interval [a,b] overlaps with [starts[i], ends[i]] if max(a, starts[i]) <= min(b, ends[i])
        // Which is equivalent to: a <= ends[i] AND b >= starts[i]
        
        // A = number of intervals with start <= ends[i]
        int A = upper_bound(sortedStarts.begin(), sortedStarts.end(), ends[i]) - sortedStarts.begin();
        
        // B = number of intervals with end < starts[i]  
        int B = lower_bound(sortedEnds.begin(), sortedEnds.end(), starts[i]) - sortedEnds.begin();
        
        // OverlapCount(i) = A - B (intervals that overlap with interval i)
        int overlapCount = A - B;
        maxOverlaps = max(maxOverlaps, overlapCount);
    }
    
    return n - maxOverlaps;
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);
    
    int n;
    cin >> n;
    
    vector<int> starts(n);
    for (int i = 0; i < n; i++) {
        cin >> starts[i];
    }
    
    int m;
    cin >> m; // This should equal n
    
    vector<int> ends(n);
    for (int i = 0; i < n; i++) {
        cin >> ends[i];
    }
    
    cout << getMinimumRemovals(starts, ends) << endl;
    
    return 0;
}