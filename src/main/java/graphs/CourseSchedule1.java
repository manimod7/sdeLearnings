package graphs;

/*
Leetcode 207: Course Schedule I

🧠 Problem:
You are given `numCourses` courses labeled from 0 to numCourses-1 and a list of prerequisites.
Each prerequisite is a pair [a, b], meaning you must take course `b` before course `a`.

Return true if it is possible to finish all courses, otherwise return false.

➡️ Example:
Input: numCourses = 2, prerequisites = [[1,0]]
Output: true (Take 0 first, then 1)

Input: numCourses = 2, prerequisites = [[1,0],[0,1]]
Output: false (Cycle exists)

✅ Goal:
Detect if a **cycle** exists in the course dependency graph.

--------------------------------------------------
🛠️ Solution Approach: DFS-Based Cycle Detection

- Treat courses as nodes in a directed graph.
- An edge from course `b` to `a` means: to take `a`, you need `b` first.
- If there's a cycle in the graph, you cannot finish all courses.

We use:
- DFS to traverse the graph.
- A `visited` array to mark the status of each node:
    0 = not visited
    1 = visiting (in recursion stack)
    2 = visited (safe / no cycle from this node)

If during DFS we revisit a node marked as "visiting", it means we found a cycle.

--------------------------------------------------
⏱ Time Complexity: O(N + E)
- N = number of courses (nodes)
- E = number of prerequisites (edges)

💾 Space Complexity: O(N + E)
- Graph storage + visited array + recursion stack

*/

import java.util.*;

public class CourseSchedule1 {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // Step 1: Build the adjacency list representation of the graph
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }

        // prerequisites[i] = [a, b] => b -> a
        for (int[] prereq : prerequisites) {
            int course = prereq[0];
            int prereqCourse = prereq[1];
            graph.get(prereqCourse).add(course);
        }

        // Step 2: Create a visited array to track states
        // 0 = unvisited, 1 = visiting, 2 = visited
        int[] visited = new int[numCourses];

        // Step 3: Perform DFS for each unvisited course
        for (int i = 0; i < numCourses; i++) {
            if (!dfs(i, graph, visited)) {
                return false; // cycle detected
            }
        }

        return true; // no cycles, all courses can be finished
    }

    // DFS helper to detect cycles
    private boolean dfs(int course, List<List<Integer>> graph, int[] visited) {
        if (visited[course] == 1) return false; // Found a cycle
        if (visited[course] == 2) return true;  // Already processed and safe

        // Mark as visiting
        visited[course] = 1;

        // Visit all neighbors (courses dependent on this course)
        for (int neighbor : graph.get(course)) {
            if (!dfs(neighbor, graph, visited)) {
                return false; // cycle found in neighbor
            }
        }

        // Mark as visited after all neighbors processed
        visited[course] = 2;
        return true;
    }
}

