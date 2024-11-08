package Sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Student {
    String name;
    double totalPercentage;
    int numberOfDaysAbsent;

    public Student(String name, double totalPercentage, int numberOfDaysAbsent) {
        this.name = name;
        this.totalPercentage = totalPercentage;
        this.numberOfDaysAbsent = numberOfDaysAbsent;
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', totalPercentage=" + totalPercentage +
                ", numberOfDaysAbsent=" + numberOfDaysAbsent + "}";
    }
}

class StudentComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        // First, sort by totalPercentage in descending order
        int percentageCompare = Double.compare(s2.totalPercentage, s1.totalPercentage);
        if (percentageCompare != 0) {
            return percentageCompare;
        }

        // If percentages are equal, sort by numberOfDaysAbsent in ascending order
        int daysAbsentCompare = Integer.compare(s1.numberOfDaysAbsent, s2.numberOfDaysAbsent);
        if (daysAbsentCompare != 0) {
            return daysAbsentCompare;
        }

        // If both percentage and days absent are equal, sort by name in ascending order
        return s1.name.compareTo(s2.name);
    }

    public static void main(String[] args) {
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("Alice", 88.5, 5));
        students.add(new Student("Bob", 88.5, 3));
        students.add(new Student("Charlie", 95.0, 2));
        students.add(new Student("David", 88.5, 5));
        students.add(new Student("Eve", 92.0, 4));

        // Sort the list using the custom comparator
        Collections.sort(students, new StudentComparator());

        // Print the sorted list
        for (Student student : students) {
            System.out.println(student);
        }
    }
}

