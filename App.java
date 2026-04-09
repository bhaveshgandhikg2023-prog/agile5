import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        StudentTimetable timetable = new StudentTimetable();
        timetable.addCourse("Mathematics", "Monday", "10:00", "12:00");
        timetable.addCourse("Computer Science", "Tuesday", "09:00", "11:00");
        timetable.addCourse("Physics", "Wednesday", "11:00", "13:00");

        // Display the timetable
        timetable.displayTimetable();
    }
}

class StudentTimetable {
    private Map<String, List<String>> schedule;

    public StudentTimetable() {
        schedule = new HashMap<>();
    }

    public void addCourse(String courseName, String day, String startTime, String endTime) {
        String courseDetails = courseName + " from " + startTime + " to " + endTime;
        schedule.putIfAbsent(day, new ArrayList<>());
        schedule.get(day).add(courseDetails);
    }

    public void displayTimetable() {
        System.out.println("Student Timetable:");
        for (Map.Entry<String, List<String>> entry : schedule.entrySet()) {
            System.out.println(entry.getKey() + ":");
            for (String course : entry.getValue()) {
                System.out.println("   " + course);
            }
        }
    }
}