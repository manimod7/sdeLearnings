**Code Question 2**

Amazon’s database doesn’t support very large numbers, so they store them as strings of binary characters, ‘0’ and ‘1’.  Unfortunately some positions were entered as ‘!’, and it’s unknown whether each ‘!’ should be ‘0’ or ‘1’.  After storage, every time the characters ‘0’ and ‘1’ appear together as a subsequence “01” (not necessarily contiguous) or “10” in the string, they generate errors:

* each subsequence “01” incurs **x** errors
* each subsequence “10” incurs **y** errors

Replace every ‘!’ with either ‘0’ or ‘1’ to **minimize** the total errors, and return the result **modulo** 10⁹+7.

---

### Function Description

Complete the function

```
int getMinErrors(string errorString, int x, int y);  
```

where

* `errorString` is composed only of ‘0’, ‘1’, and ‘!’
* `x` is the error cost per subsequence “01”
* `y` is the error cost per subsequence “10”

The function should return the minimum total errors (mod 10⁹+7).

---

### Input Format

```
errorString
x
y
```

### Constraints

* 1 ≤ |errorString| ≤ 10^5
* 0 ≤ x, y ≤ 10^5
* `errorString[i]` ∈ {‘0’, ‘1’, ‘!’}

> **Note:** A subsequence is obtained by deleting zero or more characters without changing the order of the remaining ones.

---

### Sample Case 0

```
Input:
0!!1!1
2
3

Output:
10
```

**Explanation 0**
There are 2³ = 8 ways to replace the three ‘!’ characters:

|  #  | Possible String | #“01” | #“10” | Errors = #01·2 + #10·3 |
| :-: | :-------------: | :---: | :---: | :--------------------: |
|  1  |      000101     |   7   |   1   |           17           |
|  2  |      000111     |   9   |   0   |           18           |
|  3  |      001101     |   7   |   2   |           20           |
|  4  |      001111     |   8   |   0   |           16           |
|  5  |      010101     |   6   |   3   |           21           |
|  6  |      010111     |   7   |   1   |           17           |
|  7  |      011101     |   5   |   3   |           19           |
|  8  |      011111     |   5   |   0   |           10           |

The minimum is **10**.

---

### Sample Case 1

```
Input:
!!!!!!!
23
47

Output:
0
```

**Explanation 1**
All characters are ‘!’.  Replacing them **all** with ‘0’ (or all with ‘1’) yields no “01” or “10” subsequences → **0** errors.
