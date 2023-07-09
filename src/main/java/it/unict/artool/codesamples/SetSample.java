package it.unict.artool.codesamples;

import java.util.HashSet;
import java.util.TreeSet;

public class SetSample {

    public static void main(String[] args) {

        TreeSet<Student> studentSet = new TreeSet<>();
        studentSet.add(new Student("Giacomo", "Rotondo"));
        studentSet.add(new Student("Mario", "Rossi"));
        studentSet.add(new Student("Luigi", "Verdi"));
        studentSet.add(new Student("Antonio", "Azzurri"));
        String testString = "TEST";
        HashSet<String> stringHashSet = new HashSet<>();
        if (studentSet.size() == 4) {
            System.out.println("TEST_TRUE");
        } else {
            System.out.println("TEST_FALSE");
        }

    }

    private static class Student implements Comparable<Student> {
        public String firstName;
        public String lastNMame;

        public Student(String firstName, String lastNMame) {
            this.firstName = firstName;
            this.lastNMame = lastNMame;
        }

        @Override
        public int compareTo(Student o) {
            return this.lastNMame.compareTo(o.lastNMame);
        }

        @Override
        public String toString() {
            return lastNMame;
        }

    }

}
