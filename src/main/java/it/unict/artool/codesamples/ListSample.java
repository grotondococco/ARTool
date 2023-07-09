package it.unict.artool.codesamples;


import java.util.ArrayList;
import java.util.LinkedList;

public class ListSample {

    public static void main(String[] args) {

        LinkedList<Student> studentList = new LinkedList<>();
        studentList.add(new Student("Giacomo", "Rotondo"));
        studentList.add(new Student("Mario", "Rossi"));
        studentList.add(new Student("Luigi", "Verdi"));
        studentList.add(new Student("Antonio", "Azzurri"));
        String testString = "TEST";
        ArrayList<String> studentArrayList = new ArrayList<>();
        System.out.println(testString + " " + studentList.size());

    }

    private static class Student {
        public String firstName;
        public String lastNMame;

        public Student(String firstName, String lastNMame) {
            this.firstName = firstName;
            this.lastNMame = lastNMame;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "firstName='" + firstName + '\'' +
                    ", lastNMame='" + lastNMame + '\'' +
                    '}';
        }

    }

}
