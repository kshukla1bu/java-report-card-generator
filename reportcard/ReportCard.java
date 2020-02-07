/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportcard;

import java.io.*;
import java.util.*;

/**
 *
 * @author Kaushal
 */

class Course{
    int id;
    String name;
    String teacher;
}

class FinalCourse{
    int course_id;
    String name;
    String teacher;
    double finalGrade;
    boolean courseTaken;
    
    FinalCourse(){
        this.courseTaken = false;
    }
}

class StudentReportCard {
    int StudentId;
    String name;
    double average;
    ArrayList<FinalCourse> FC;
}

class Student {
    int id;
    String name;
}

class Test {
    int test_id, course_id, weight;
}

class Mark {
    int test_id,student_id,marks, course_id, weight;
    double calculatedWeight;
}

public class ReportCard {

    static ArrayList<Course> courseList = new ArrayList<>();
    static ArrayList<Student> studentList = new ArrayList<>();
    static ArrayList<Mark> markList = new ArrayList<>();
    static ArrayList<Test> testList = new ArrayList<>();
    
    static ArrayList<StudentReportCard> srcList = new ArrayList<>();
    
    public static void parseCSV(String filename) throws FileNotFoundException, IOException{
        String row;
        try (BufferedReader csvReader = new BufferedReader(new FileReader(filename))) {
            Boolean flag = false;
            while((row = csvReader.readLine()) != null){
                String[] data = row.split(",");
                switch(filename){
                    case("courses.csv"):
                        if(!flag)
                        {
                            flag = true;
                        }
                        else
                        {
                            Course course = new Course();
                            course.id = Integer.parseInt(data[0]);
                            course.name = data[1];
                            course.teacher = data[2];
                            courseList.add(course);
                        }
                        break;
                    case("students.csv"):
                        if(!flag)
                        {
                            flag = true;
                        }
                        else
                        {
                            Student student = new Student();
                            student.id = Integer.parseInt(data[0]);
                            student.name = data[1];
                            studentList.add(student);
                        }
                        break;
                    case("marks.csv"):
                        if(!flag)
                        {
                            flag = true;
                        }
                        else
                        {
                            Mark mark = new Mark();
                            mark.test_id = Integer.parseInt(data[0]);
                            mark.student_id = Integer.parseInt(data[1]);
                            mark.marks = Integer.parseInt(data[2]);
                            markList.add(mark);
                        }
                        break;
                    case("tests.csv"):
                        if(!flag)
                        {
                            flag = true;
                        }
                        else
                        {
                            Test test = new Test();
                            test.test_id = Integer.parseInt(data[0]);
                            test.course_id = Integer.parseInt(data[1]);
                            test.weight = Integer.parseInt(data[2]);
                            testList.add(test);
                        }
                        break;    
                }
            }
        }
    }

    public static double getPercentile(Mark mark){
        int test_id = mark.test_id;
        int marks = mark.marks;
        Test t = null;
        for(int i = 0; i < testList.size(); i++)
        {
            if(test_id == testList.get(i).test_id)
            {
                t = testList.get(i);
                break;
            }
        }
        int weight = t.weight;
        double percentile = ((double)marks*(double)weight)/100;
        return percentile;
    }
    
    public static void getAverage(StudentReportCard sc){
        double avg = 0.0;
        for(int i = 0; i < sc.FC.size(); i++)
        {
            sc.FC.get(i).finalGrade = Math.round(sc.FC.get(i).finalGrade*100.00)/100.00;
            if(!sc.FC.get(i).courseTaken)
            {
                sc.FC.remove(i);
            }
            avg += sc.FC.get(i).finalGrade;
        }
        double divide = (double)sc.FC.size();
        sc.average = avg/divide;
        sc.average = Math.round(sc.average*100.0)/100.0;
    }
    
    public static void generateBasicReportCard(){
        for(int i = 0; i < studentList.size(); i++)
        {
            StudentReportCard src = new StudentReportCard();
            src.name = studentList.get(i).name;
            src.StudentId = studentList.get(i).id;
            src.FC = new ArrayList<>();
            for(int j= 0; j < courseList.size(); j++)
            {
                FinalCourse finalCourse = new FinalCourse();
                finalCourse.course_id = courseList.get(j).id;
                finalCourse.name = courseList.get(j).name;
                finalCourse.teacher = courseList.get(j).teacher;
                src.FC.add(finalCourse);
            }
            srcList.add(src);
        } 
    }
    
    public static void combineMarksAndTests(){
        for(int i = 0 ; i < markList.size(); i++)
        {   
            for(int j = 0; j < testList.size(); j++)
            {
                if(testList.get(j).test_id == markList.get(i).test_id)
                {
                    markList.get(i).course_id = testList.get(j).course_id;
                    markList.get(i).weight = testList.get(j).weight;
                }
                markList.get(i).calculatedWeight = getPercentile(markList.get(i));
            }
        }
    }
    
    public static void calculateFinalGrade(){
        for(int i = 0 ; i < markList.size(); i++)
        {
            srcList.get(markList.get(i).student_id-1).FC.get(markList.get(i).course_id-1).finalGrade += markList.get(i).calculatedWeight;
            srcList.get(markList.get(i).student_id-1).FC.get(markList.get(i).course_id-1).courseTaken = true;
        }
    }
    
    public static void generateReportCard(ArrayList<StudentReportCard> sRep){
        
       try {
            FileWriter writer = new FileWriter("ReportCard.txt", false);
            for(int i = 0; i < sRep.size(); i++)
            {
                writer.write("Student Id: "+sRep.get(i).StudentId+", "+"name: "+sRep.get(i).name);
                writer.write("\r\n");
                writer.write("Total Average:");
                writer.write("\t");
                writer.write(sRep.get(i).average+"%");
                writer.write("\r\n");
                writer.write("\r\n");
                for(int j = 0; j < sRep.get(i).FC.size(); j++)
                {
                    writer.write("\t");
                    writer.write("Course: "+sRep.get(i).FC.get(j).name+", Teacher: "+sRep.get(i).FC.get(j).teacher);
                    writer.write("\r\n");
                    writer.write("\t");
                    writer.write("Final Grade:");
                    writer.write("\t");
                    writer.write(sRep.get(i).FC.get(j).finalGrade+"%");
                    writer.write("\r\n");
                    writer.write("\r\n");
                    if(j == sRep.get(i).FC.size()-1)
                        writer.write("\r\n");
                }
            }
            writer.close();
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        parseCSV("courses.csv");
        parseCSV("students.csv");
        parseCSV("marks.csv");
        parseCSV("tests.csv");
        
        //Generate Basic Report Details
        generateBasicReportCard();
        
        //Combining Marks and Tests table
        combineMarksAndTests();
        
        //Calculate final grade and mark course taken
        calculateFinalGrade();
        
        //Get Average for every student
        for(int i = 0; i < srcList.size(); i++)
        {
            getAverage(srcList.get(i));
        }
        
        //Final Report Card text file
        generateReportCard(srcList);
    } 
    
}
