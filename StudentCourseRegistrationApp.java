/*code by Arnav Kumar
          BSC CS , MITWPU , KOTHRUD
		  TASK 4:  STUDENT COURSE REGISTRATION APP
*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StudentCourseRegistrationApp {
    private DefaultTableModel courseTableModel;
    private List<Student> students = new ArrayList<>();
    private JTable courseTable;
    private JList<Course> registeredCoursesList;
    private DefaultListModel<Course> registeredCoursesListModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StudentCourseRegistrationApp();
            }
        });
    }

    public StudentCourseRegistrationApp() {
        // Create a sample course database
        List<Course> availableCourses = new ArrayList<>();
        availableCourses.add(new Course("CSCI101", "Introduction to Computer Science", "An introduction to programming", 30));
        availableCourses.add(new Course("MATH101", "Calculus I", "A first course in calculus", 40));
        availableCourses.add(new Course("PHYS101", "Physics I", "Introduction to classical mechanics", 25));
        availableCourses.add(new Course("CHEM101", "Chemistry I", "Introduction to general chemistry", 35));
        availableCourses.add(new Course("ENGL101", "English Composition", "Basic writing skills", 20));
        availableCourses.add(new Course("HIST101", "World History", "Overview of world history", 30));

        // Create a course table
        courseTableModel = new DefaultTableModel(new String[]{"Course Code", "Title", "Capacity"}, 0);
        for (Course course : availableCourses) {
            courseTableModel.addRow(new String[]{course.getCode(), course.getTitle(), String.valueOf(course.getCapacity())});
        }
        courseTable = new JTable(courseTableModel);
        JScrollPane courseTableScrollPane = new JScrollPane(courseTable);

        // Create a list for registered courses
        registeredCoursesListModel = new DefaultListModel<>();
        registeredCoursesList = new JList<>(registeredCoursesListModel);
        JScrollPane registeredCoursesScrollPane = new JScrollPane(registeredCoursesList);

        // Create buttons for registration and removal
        JButton registerButton = new JButton("Register");
        JButton removeButton = new JButton("Remove");

        // Create the main frame
        JFrame frame = new JFrame("Student Course Registration System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(courseTableScrollPane);
        topPanel.add(registeredCoursesScrollPane);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(registerButton);
        bottomPanel.add(removeButton);

        frame.add(topPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        // Create a sample student
        Student sampleStudent = new Student("John Doe");
        students.add(sampleStudent);

        // Register button action listener
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = courseTable.getSelectedRow();
                if (selectedRow != -1) {
                    Course selectedCourse = availableCourses.get(selectedRow);
                    boolean success = sampleStudent.registerCourse(selectedCourse);
                    if (success) {
                        registeredCoursesListModel.addElement(selectedCourse);
                        courseTableModel.setValueAt(String.valueOf(selectedCourse.getCapacity()), selectedRow, 2);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Course is full, cannot register.");
                    }
                }
            }
        });

        // Remove button action listener
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = registeredCoursesList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Course selectedCourse = registeredCoursesListModel.get(selectedIndex);
                    sampleStudent.removeCourse(selectedCourse);
                    registeredCoursesListModel.removeElement(selectedCourse);
                    for (int i = 0; i < availableCourses.size(); i++) {
                        if (availableCourses.get(i).getCode().equals(selectedCourse.getCode())) {
                            courseTableModel.setValueAt(String.valueOf(selectedCourse.getCapacity()), i, 2);
                            break;
                        }
                    }
                }
            }
        });
    }

    private class Course {
        private String code;
        private String title;
        private String description;
        private int capacity;

        public Course(String code, String title, String description, int capacity) {
            this.code = code;
            this.title = title;
            this.description = description;
            this.capacity = capacity;
        }

        public String getCode() {
            return code;
        }

        public String getTitle() {
            return title;
        }

        public int getCapacity() {
            return capacity;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    private class Student {
        private String name;
        private List<Course> registeredCourses = new ArrayList<>();
        private static final int MAX_COURSES = 6;

        public Student(String name) {
            this.name = name;
        }

        public boolean registerCourse(Course course) {
            if (registeredCourses.size() < MAX_COURSES && course.getCapacity() > 0) {
                registeredCourses.add(course);
                course.capacity--;
                return true;
            }
            return false;
        }

        public void removeCourse(Course course) {
            registeredCourses.remove(course);
            course.capacity++;
        }
    }
}
