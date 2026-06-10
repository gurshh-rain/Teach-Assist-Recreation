
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
class Person {
	String name;
	int age;
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
	public String getInfo() {
		return "Name: " + name + " | Age: " + age;
	}
}
class Stack {
	int top;
	Student[] students;
	public Stack(int max){
		top = -1;
		students = new Student[max];
	}
	public void add(Student student){
		if(top == students.length-1){
			return;
		}
		top+=1;
		students[top] = student;
	}
	public void pop() {
		if(top<0){
			return;
		}
		top -= 1;
	}
	public void removeByName(String name, int age) {
	    for (int i = 0; i <= top; i++) {
	        if (students[i].name.equalsIgnoreCase(name) && age == students[i].age) {
	            for (int j = i; j < top; j++) {
	                students[j] = students[j + 1];
	            }
	            students[top] = null;
	            top--;
	            return;
	        }
	    }
	    System.out.println("Student not found.");
	}
	public int size(){
		return top+1;
	}
	public void addGradeStudent(Scanner input, String testName,
	    double kweight, double tweight, double cweight, double aweight) {
	    for (int i = 0; i <= top; i++) {
	        System.out.println("Entering grades for: " + students[i].name);
	        System.out.print("  Knowledge grade (enter -1 to skip student): ");
	        double kscore = input.nextDouble();
	        if(kscore == -1) {
	        	continue;
	        }
	        System.out.print("  Thinking grade: ");
	        double tscore = input.nextDouble();
	        System.out.print("  Communication grade: ");
	        double cscore = input.nextDouble();
	        System.out.print("  Application grade: ");
	        double ascore = input.nextDouble();
	        Grade grade = new Grade(testName, kscore, tscore, cscore, ascore,
	                                kweight, tweight, cweight, aweight);
	        students[i].grades.addGrade(grade);
	        System.out.println("Grade added for " + students[i].name + ".");
	    }
	}
	public String toString() {
		return toStringRecursive(0);
	}
	private String toStringRecursive(int index) {
		if (index > top) return "";
		return students[index].toString() + "\n" + toStringRecursive(index + 1);
	}
	public void sortByName() {
		for (int i = 0; i <= top; i++) {
			for (int j = 0; j < top - i; j++) {
				if (students[j].getName().compareToIgnoreCase(students[j+1].getName()) > 0) {
					Student temp = students[j];
					students[j] = students[j+1];
					students[j+1] = temp;
				}
			}
		}
	}
	public void sortByGrade() {
		for (int i = 0; i <= top; i++) {
			for (int j = 0; j < top - i; j++) {
				if (students[j].grades.calculateOverall() < students[j+1].grades.calculateOverall()) {
					Student temp = students[j];
					students[j] = students[j+1];
					students[j+1] = temp;
				}
			}
		}
	}
	public void sortByAge() {
		for (int i = 0; i <= top; i++) {
			for (int j = 0; j < top - i; j++) {
				if (students[j].age > students[j+1].age) {
					Student temp = students[j];
					students[j] = students[j+1];
					students[j+1] = temp;
				}
			}
		}
	}
}
class Course {
	String courseName;
	Stack students = null;
	String[] testNames = new String[100];
	double[] testKWeights = new double[100];
	double[] testTWeights = new double[100];
	double[] testCWeights = new double[100];
	double[] testAWeights = new double[100];
	int testCount = 0;
	public Course(String courseName) {
		this.courseName = courseName;
		this.students = new Stack(30);
	}
	public String getName() {
		return courseName;
	}
	public void addStudent(String studentName, int age, Scanner input){
	    Student s = new Student(studentName, age);
	    students.add(s);
	    if (testCount > 0) {
	        System.out.println("Existing tests are on record. Enter scores for any of them? (y/n): ");
	        String choice = input.nextLine().trim().toLowerCase();
	        if (choice.equals("y")) {
	            for (int t = 0; t < testCount; t++) {
	                System.out.println("Enter scores for [" + testNames[t] + "]? (y/n): ");
	                String enter = input.nextLine().trim().toLowerCase();
	                if (!enter.equals("y")) continue;
	                System.out.print("  Knowledge grade: ");
	                double kscore = input.nextDouble();
	                System.out.print("  Thinking grade: ");
	                double tscore = input.nextDouble();
	                System.out.print("  Communication grade: ");
	                double cscore = input.nextDouble();
	                System.out.print("  Application grade: ");
	                double ascore = input.nextDouble();
	                input.nextLine();
	                s.grades.addGrade(new Grade(testNames[t], kscore, tscore, cscore, ascore,
	                        testKWeights[t], testTWeights[t], testCWeights[t], testAWeights[t]));
	                System.out.println("Grade added.");
	            }
	        }
	    }
	}
	public void addGrades(Scanner input, String testName,
	        double kweight, double tweight, double cweight, double aweight) {
	    // Record the test so future students can use it
	    testNames[testCount] = testName;
	    testKWeights[testCount] = kweight;
	    testTWeights[testCount] = tweight;
	    testCWeights[testCount] = cweight;
	    testAWeights[testCount] = aweight;
	    testCount++;
	    students.addGradeStudent(input, testName, kweight, tweight, cweight, aweight);
	}
	public void registerTest(String testName, double kw, double tw, double cw, double aw) {
	    for (int i = 0; i < testCount; i++) {
	        if (testNames[i].equals(testName)) return;
	    }
	    testNames[testCount] = testName;
	    testKWeights[testCount] = kw;
	    testTWeights[testCount] = tw;
	    testCWeights[testCount] = cw;
	    testAWeights[testCount] = aw;
	    testCount++;
	}
	public void displayStudents(){
		if(students.size() <= 0){
			System.out.println("No students in this course.");
			return;
		}
		System.out.println(students.toString());
	}
	public void displayGradeSummary() {
		if (students.size() <= 0) return;
		double[][] summary = new double[students.size()][4];
		for (int i = 0; i <= students.top; i++) {
			LinkedList.Node current = students.students[i].grades.head;
			int count = 0;
			while (current != null) {
				summary[i][0] += current.grade.kscore;
				summary[i][1] += current.grade.tscore;
				summary[i][2] += current.grade.cscore;
				summary[i][3] += current.grade.ascore;
				count++;
				current = current.next;
			}
			if (count > 0) {
				for (int j = 0; j < 4; j++) summary[i][j] /= count;
			}
		}
		System.out.println("  --- Grade Summary (category averages) ---");
		String[] labels = {"K", "T", "C", "A"};
		for (int i = 0; i <= students.top; i++) {
			System.out.print("  " + students.students[i].name + ": ");
			for (int j = 0; j < 4; j++) {
				System.out.print(labels[j] + "=" + (int) summary[i][j] + "% ");
			}
			System.out.println();
		}
	}
	public void removeStudent(String studentName, int age) {
	    students.removeByName(studentName, age);
	}
	public void editStudent(String oldName, String newName, int newAge) {
	    for (int i = 0; i <= students.top; i++) {
	        if (students.students[i].name.equalsIgnoreCase(oldName)) {
	            students.students[i].name = newName;
	            students.students[i].age = newAge;
	            return;
	        }
	    }
	    System.out.println("Student not found.");
	}
	public void sortByName()  { students.sortByName(); }
	public void sortByGrade() { students.sortByGrade(); }
	public void sortByAge()   { students.sortByAge(); }
}
class Grade {
	String testName;
	double kscore, tscore, cscore, ascore;
	double kweight, tweight, cweight, aweight;
	public Grade(String testName, double kscore, double tscore, double cscore, double ascore,
	             double kweight, double tweight, double cweight, double aweight) {
		this.testName = testName;
		this.kscore = kscore; this.tscore = tscore;
		this.cscore = cscore; this.ascore = ascore;
		this.kweight = kweight; this.tweight = tweight;
		this.cweight = cweight; this.aweight = aweight;
	}
}
class LinkedList {
	Node head;
	int size = 0;
	class Node {
		Node next;
		Grade grade;
		public Node(Grade grade) { this.grade = grade; next = null; }
		public Node(Grade grade, Node next) { this.next = next; this.grade = grade; }
	}
	public void addGrade(Grade grade) {
		if (size == 0) { head = new Node(grade); size++; return; }
		Node current = head;
		while(current.next != null) current = current.next;
		current.next = new Node(grade);
		size++;
	}
	public double calculateOverall() {
	    if (size == 0) return 0;
	    double kTotal = 0, tTotal = 0, cTotal = 0, aTotal = 0;
	    double kw = 0, tw = 0, cw = 0, aw = 0;
	    int count = 0;
	    Node current = head;
	    while (current != null) {
	        Grade g = current.grade;
	        kTotal += g.kscore;
	        tTotal += g.tscore;
	        cTotal += g.cscore;
	        aTotal += g.ascore;
	        kw += g.kweight;
	        tw += g.tweight;
	        cw += g.cweight;
	        aw += g.aweight;
	        count++;
	        current = current.next;
	    }
	    double kAvg = kTotal / count;
	    double tAvg = tTotal / count;
	    double cAvg = cTotal / count;
	    double aAvg = aTotal / count;
	   
	    double kwAvg = kw / count;
	    double twAvg = tw / count;
	    double cwAvg = cw / count;
	    double awAvg = aw / count;
	    double totalWeight = kwAvg + twAvg + cwAvg + awAvg;
	    if (totalWeight == 0) return 0;
	    double overall = (kwAvg * kAvg + twAvg * tAvg + cwAvg * cAvg + awAvg * aAvg) / totalWeight;
	    return Math.min(overall, 100.0);
	}
}
class Student extends Person {
	LinkedList grades;
	public Student(String name, int age) {
		super(name, age);
		grades = new LinkedList();
	}
	public String getName(){ return name; }
	public LinkedList getGrades() { return grades; }
	public String toString() {
	    double overall = grades.calculateOverall();
	    double rounded = Math.round(overall * 100.0) / 100.0;
	    String result = getInfo() + " | Overall Grade: " + rounded + "%\n";
	    if (grades.size == 0) {
	        result += "  No grades yet.\n";
	    } else {
	        LinkedList.Node current = grades.head;
	        while (current != null) {
	            Grade g = current.grade;
	            double totalWeight = g.kweight + g.tweight + g.cweight + g.aweight;
	            double testScore = (totalWeight > 0)
	                ? (g.kweight * g.kscore + g.tweight * g.tscore
	                 + g.cweight * g.cscore + g.aweight * g.ascore) / totalWeight : 0;
	            double roundedTest = Math.round(testScore * 100.0) / 100.0;
	            result += "  [" + g.testName + "] "
	                    + "K:" + g.kscore + "%[" + g.kweight + "] "
	                    + "T:" + g.tscore + "%[" + g.tweight + "] "
	                    + "C:" + g.cscore + "%[" + g.cweight + "] "
	                    + "A:" + g.ascore + "%[" + g.aweight + "] "
	                    + "=> " + roundedTest + "%\n";
	            current = current.next;
	        }
	    }
	    return result;
	}
}
public class TeachAssist {
	static final String DATA_FILE = "teach_assist.txt";
	public static void saveAllCourses(Course[] courses) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE));
			for (int c = 0; c < courses.length; c++) {
				Course course = courses[c];
				writer.write("COURSE:" + course.courseName);
				writer.newLine();
				for (int i = 0; i <= course.students.top; i++) {
					Student s = course.students.students[i];
					writer.write("STUDENT:" + s.name + ":" + s.age);
					writer.newLine();
					LinkedList.Node current = s.grades.head;
					while (current != null) {
						Grade g = current.grade;
						writer.write("GRADE:" + g.testName + ":"
								+ g.kscore + ":" + g.tscore + ":" + g.cscore + ":" + g.ascore + ":"
								+ g.kweight + ":" + g.tweight + ":" + g.cweight + ":" + g.aweight);
						writer.newLine();
						current = current.next;
					}
				}
			}
			writer.close();
			System.out.println("All courses saved to " + DATA_FILE + ".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void clearFile() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE));
			writer.write("");
			writer.close();
			System.out.println("File cleared.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Course[] loadAllCourses() {
		Course[] loaded = new Course[50];
		int count = 0;
		Course currentCourse = null;
		Student currentStudent = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE));
			String firstLine = reader.readLine();
			while (firstLine != null && firstLine.trim().isEmpty()) {
				firstLine = reader.readLine();
			}
			if (firstLine == null) {
				reader.close();
				System.out.println("File is empty, nothing to load.");
				return new Course[0];
			}
			String line = firstLine;
			do {
				line = line.trim();
				if (line.isEmpty()) continue;
				String[] parts = line.split(":");
				if (parts[0].equals("COURSE")) {
					currentCourse = new Course(parts[1]);
					loaded[count++] = currentCourse;
					currentStudent = null;
				} else if (parts[0].equals("STUDENT") && currentCourse != null) {
					currentStudent = new Student(parts[1], Integer.parseInt(parts[2]));
					currentCourse.students.add(currentStudent);
				} else if (parts[0].equals("GRADE") && currentStudent != null) {
					Grade g = new Grade(
						parts[1],
						Double.parseDouble(parts[2]), Double.parseDouble(parts[3]),
						Double.parseDouble(parts[4]), Double.parseDouble(parts[5]),
						Double.parseDouble(parts[6]), Double.parseDouble(parts[7]),
						Double.parseDouble(parts[8]), Double.parseDouble(parts[9])
					);
					currentStudent.grades.addGrade(g);
					// Restore test registry from file data
					if (currentCourse != null) {
						currentCourse.registerTest(parts[1],
							Double.parseDouble(parts[6]), Double.parseDouble(parts[7]),
							Double.parseDouble(parts[8]), Double.parseDouble(parts[9]));
					}
				}
			} while ((line = reader.readLine()) != null);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Course[] result = new Course[count];
		for (int i = 0; i < count; i++) result[i] = loaded[i];
		return result;
	}
	public static void display_course(Course[] courses){
		System.out.println("Select course to view/edit: ");
		for(int i = 0; i < courses.length; i++){
			System.out.println((i+1) + " - " + courses[i].getName());
		}
	}
	public static void helper_function(Course activeCourse, int option, Course[] courses) {
	    Scanner input = new Scanner(System.in);
	    if (option == 1) {
	        System.out.print("Enter student name: ");
	        String name = input.nextLine();
	        System.out.print("Enter student age: ");
	        int age = input.nextInt();
	        input.nextLine();
	        activeCourse.addStudent(name, age, input);
	        System.out.println("Student added successfully.");
	        activeCourse.displayStudents();
	    } else if (option == 2) {
	        System.out.print("Enter the name of the student to remove: ");
	        String name = input.nextLine();
	        System.out.print("Enter the age of the student to remove: ");
	        int age = input.nextInt();
	        activeCourse.removeStudent(name, age);
	        System.out.println("Student removed successfully.");
	        activeCourse.displayStudents();
	    } else if (option == 3) {
	        System.out.print("Enter the name of the student to edit: ");
	        String oldName = input.nextLine();
	        System.out.print("Enter new name: ");
	        String newName = input.nextLine();
	        System.out.print("Enter new age: ");
	        int newAge = input.nextInt();
	        activeCourse.editStudent(oldName, newName, newAge);
	        System.out.println("Student updated successfully.");
	        activeCourse.displayStudents();
	    } else if (option == 4) {
	    	System.out.print("Enter test name: ");
	    	String testName = input.next();
	    	System.out.print("Enter knowledge section weight: ");
	    	double kweight = input.nextDouble();
	    	System.out.print("Enter thinking section weight: ");
	    	double tweight = input.nextDouble();
	    	System.out.print("Enter communication section weight: ");
	    	double cweight = input.nextDouble();
	    	System.out.print("Enter application section weight: ");
	    	double aweight = input.nextDouble();
	        activeCourse.addGrades(input, testName, kweight, tweight, cweight, aweight);
	    } else if (option == 5) {
	        saveAllCourses(courses);
	    } else if (option == 6) {
	        System.out.print("Are you sure you want to clear all saved data? (yes/no): ");
	        String confirm = input.nextLine().trim().toLowerCase();
	        if (confirm.equals("yes")) {
	            clearFile();
	        } else {
	            System.out.println("Clear cancelled.");
	        }
	    } else if (option == 7) {
	    	System.out.println("1. Sort by name");
	    	System.out.println("2. Sort by grade");
	    	System.out.println("3. Sort by age");
	    	int sort_method = input.nextInt();
	    	if (sort_method == 1) {
	    		activeCourse.sortByName();
	    		System.out.println("Sorted by name.");
	    	} else if (sort_method == 2) {
	    		activeCourse.sortByGrade();
	    		System.out.println("Sorted by grade (highest first).");
	    	} else if (sort_method == 3) {
	    		activeCourse.sortByAge();
	    		System.out.println("Sorted by age (youngest first).");
	    	}
	    	activeCourse.displayStudents();
	    }
	}
	public static void main(String[] args) {
	    Scanner input = new Scanner(System.in);
	    System.out.println("████████╗███████╗ █████╗  ██████╗██╗  ██╗    █████╗ ███████╗███████╗██╗███████╗████████╗");
	    System.out.println("╚══██╔══╝██╔════╝██╔══██╗██╔════╝██║  ██║   ██╔══██╗██╔════╝██╔════╝██║██╔════╝╚══██╔══╝");
	    System.out.println("   ██║   █████╗  ███████║██║     ███████║   ███████║███████╗███████╗██║███████╗   ██║   ");
	    System.out.println("   ██║   ██╔══╝  ██╔══██║██║     ██╔══██║   ██╔══██║╚════██║╚════██║██║╚════██║   ██║   ");
	    System.out.println("   ██║   ███████╗██║  ██║╚██████╗██║  ██║   ██║  ██║███████║███████║██║███████║   ██║   ");
	    System.out.println("   ╚═╝   ╚══════╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝   ╚═╝  ╚═╝╚══════╝╚══════╝╚═╝╚══════╝   ╚═╝   ");
	    Course[] courses = new Course[0];
	    System.out.print("\nImport data from " + DATA_FILE + "? (y/n): ");
	    String importChoice = input.nextLine().trim().toLowerCase();
	    if (importChoice.equals("y")) {
	        courses = loadAllCourses();
	        System.out.println(courses.length + " course(s) loaded.");
	    }
	    System.out.print("Enter the number of new courses to create (0 if only importing): ");
	    int numNew = input.nextInt();
	    input.nextLine();
	    Course[] expanded = new Course[courses.length + numNew];
	    for (int i = 0; i < courses.length; i++) expanded[i] = courses[i];
	    for (int i = 0; i < numNew; i++) {
	        System.out.print("Enter course #" + (i + 1) + " name: ");
	        expanded[courses.length + i] = new Course(input.nextLine());
	    }
	    courses = expanded;
	    if (courses.length == 0) {
	        System.out.println("No courses available. Goodbye!");
	        return;
	    }
	    while (true) {
	        display_course(courses);
	        System.out.println("0 - Exit");
	        int courseChoice = input.nextInt();
	        input.nextLine();
	        if (courseChoice == 0) {
	            System.out.println("Goodbye!");
	            break;
	        }
	        if (courseChoice < 1 || courseChoice > courses.length) {
	            System.out.println("Invalid choice, try again.");
	            continue;
	        }
	        Course activeCourse = courses[courseChoice - 1];
	        while (true) {
	            System.out.println("\n" + activeCourse.getName() + ":");
	            System.out.println("1. Add student");
	            System.out.println("2. Remove student");
	            System.out.println("3. Edit student");
	            System.out.println("4. Add test score");
	            System.out.println("5. Save all courses to file");
	            System.out.println("6. Clear saved file");
	            System.out.println("7. Sort students");
	            System.out.println("0. Go back to course list");
	            activeCourse.displayStudents();
	            activeCourse.displayGradeSummary();
	            int option = input.nextInt();
	            input.nextLine();
	            if (option == 0) break;
	            helper_function(activeCourse, option, courses);
	        }
	    }
	}
}



