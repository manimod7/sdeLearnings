package Sorting;

import java.util.*;


// Leetcode 1366
public class RankTeamByVotes {
    public String rankTeams(String[] votes) {
        // Handle edge case
            if (votes == null || votes.length == 0) {
            return "";
        }

        int numTeams = votes[0].length();
        // Step 1: Initialize map to store ranking counts for each team
        Map<Character, int[]> rankMap = new HashMap<>();
            for (char team : votes[0].toCharArray()) {
            rankMap.put(team, new int[numTeams]);
        }

        // Step 2: Populate the rankMap with votes for each position
            for (String vote : votes) {
            for (int i = 0; i < vote.length(); i++) {
                char team = vote.charAt(i);
                rankMap.get(team)[i]++;
            }
        }

        // Step 3: Sort teams using a custom comparator
        List<Character> teams = new ArrayList<>(rankMap.keySet());
            Collections.sort(teams, (a, b) -> {
            int[] rankA = rankMap.get(a);
            int[] rankB = rankMap.get(b);
            for (int i = 0; i < numTeams; i++) {
                if (rankA[i] != rankB[i]) {
                    // Sort by descending order of votes for each rank position
                    return rankB[i] - rankA[i];
                }
            }
            // If ranks are tied, sort alphabetically
            return Character.compare(a, b);
        });

        // Step 4: Build and return the result string
        StringBuilder result = new StringBuilder();
            for (char team : teams) {
            result.append(team);
        }

        return result.toString();
    }
}
