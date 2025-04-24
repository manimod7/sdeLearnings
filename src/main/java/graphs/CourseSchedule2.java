package graphs;

/*
Leetcode 210: Course Schedule II

🧠 Problem:
You are given `numCourses` courses labeled from 0 to numCourses-1 and a list of prerequisites.
Each prerequisite is a pair [a, b], meaning to take course `a`, you must first complete course `b`.

Return an array representing the order in which you can take the courses such that all prerequisites are satisfied.
If it is impossible to finish all courses, return an empty array.

➡️ Example:
Input: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
Output: [0,1,2,3] or [0,2,1,3] (any valid topological order)

Input: numCourses = 2, prerequisites = [[1,0],[0,1]]
Output: [] (cycle exists)

✅ Goal:
Return a valid **topological order** of courses if possible, else return [].

--------------------------------------------------
🛠️ Solution: Topological Sort (BFS – Kahn's Algorithm)

- Build a graph using adjacency list.
- Track in-degrees (number of prerequisites) for each course.
- Start with all courses having in-degree 0 (no prerequisites).
- Repeatedly:
    - Pick course with in-degree 0
    - Add to result
    - Reduce in-degree of its neighbors
- If we can schedule all `numCourses`, return result.
- Else, a cycle exists → return empty array.

--------------------------------------------------
⏱ Time Complexity: O(N + E)
- N = number of courses (nodes)
- E = number of prerequisites (edges)

💾 Space Complexity: O(N + E)
- Graph + in-degree array + result array + queue

*/

import java.util.*;

public class CourseSchedule2 {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        // Step 1: Build graph and in-degree array
        List<List<Integer>> graph = new ArrayList<>();
        int[] inDegree = new int[numCourses];

        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }

        for (int[] prereq : prerequisites) {
            int course = prereq[0];
            int prereqCourse = prereq[1];
            graph.get(prereqCourse).add(course); // prereq → course
            inDegree[course]++;
        }

        // Step 2: Add all courses with in-degree 0 to queue
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        // Step 3: Perform BFS and build result
        int[] result = new int[numCourses];
        int index = 0;

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            result[index++] = curr;

            for (int neighbor : graph.get(curr)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Step 4: Check if topological sort was successful
        if (index == numCourses) {
            return result; // valid order found
        } else {
            return new int[0]; // cycle detected
        }
    }
}

