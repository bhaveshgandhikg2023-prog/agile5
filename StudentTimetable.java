import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class StudentTimetable {
    private String studentId;
    private String studentName;
    private Map<String, TimeSlot[]> weeklySchedule; // day -> array of time slots
    
    public StudentTimetable(String studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.weeklySchedule = new HashMap<>();
        initializeTimetable();
    }
    
    // TimeSlot inner class
    public static class TimeSlot {
        private LocalTime startTime;
        private LocalTime endTime;
        private String subject;
        private String classroom;
        private String instructor;
        
        public TimeSlot(LocalTime startTime, LocalTime endTime, 
                       String subject, String classroom, String instructor) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.subject = subject;
            this.classroom = classroom;
            this.instructor = instructor;
        }
        
        @Override
        public String toString() {
            return startTime + " - " + endTime + " | " + subject + 
                   " | Room: " + classroom + " | Instructor: " + instructor;
        }
        
        // Getters
        public LocalTime getStartTime() { return startTime; }
        public LocalTime getEndTime() { return endTime; }
        public String getSubject() { return subject; }
        public String getClassroom() { return classroom; }
        public String getInstructor() { return instructor; }
    }
    
    private void initializeTimetable() {
        // Initialize all days
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (String day : days) {
            weeklySchedule.put(day, new TimeSlot[6]); // 6 time slots per day
        }
    }
    
    public void addClass(String day, int slotNumber, LocalTime startTime, 
                        LocalTime endTime, String subject, String classroom, String instructor) {
        if (weeklySchedule.containsKey(day)) {
            TimeSlot[] slots = weeklySchedule.get(day);
            slots[slotNumber] = new TimeSlot(startTime, endTime, subject, classroom, instructor);
        }
    }
    
    public void printTimetable() {
        System.out.println("\n========== STUDENT TIMETABLE ==========");
        System.out.println("Student ID: " + studentId);
        System.out.println("Student Name: " + studentName);
        System.out.println("=========================================\n");
        
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (String day : days) {
            System.out.println("\n" + day.toUpperCase());
            System.out.println("-".repeat(80));
            TimeSlot[] slots = weeklySchedule.get(day);
            boolean hasClasses = false;
            for (int i = 0; i < slots.length; i++) {
                if (slots[i] != null) {
                    System.out.println("Slot " + (i + 1) + ": " + slots[i]);
                    hasClasses = true;
                }
            }
            if (!hasClasses) {
                System.out.println("No classes scheduled");
            }
        }
        System.out.println("\n=========================================\n");
    }
    
    // Getters
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public Map<String, TimeSlot[]> getWeeklySchedule() { return weeklySchedule; }
}