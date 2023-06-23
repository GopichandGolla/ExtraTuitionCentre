import java.util.*;

// Enum to represent subject areas
enum Subject {
    ENGLISH_COMPREHENSION,
    ENGLISH_WRITING,
    MATH,
    NUMERICAL_REASONING,
    VERBAL_REASONING,
    NON_VERBAL_REASONING
}

// Class to represent a tutor
class Tutor {
    private String name;
    private Set<Subject> specialties;
    private Map<Integer, Boolean> timetable; // Key: Hour (24-hour format), Value: Availability

    public Tutor(String name, Set<Subject> specialties) {
        this.name = name;
        this.specialties = specialties;
        this.timetable = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Set<Subject> getSpecialties() {
        return specialties;
    }

    public Map<Integer, Boolean> getTimetable() {
        return timetable;
    }

    public void setAvailability(int hour, boolean available) {
        timetable.put(hour, available);
    }
}

// Class to represent a student
class Student {
    private String name;
    private String gender;
    private Date dob;
    private String emergencyContact;
    private Map<Subject, List<Lesson>> lessons; // Key: Subject, Value: List of lessons

    public Student(String name, String gender, Date dob, String emergencyContact) {
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.emergencyContact = emergencyContact;
        this.lessons = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Date getDob() {
        return dob;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public Map<Subject, List<Lesson>> getLessons() {
        return lessons;
    }

    public void bookLesson(Subject subject, Lesson lesson) {
        if (!lessons.containsKey(subject)) {
            lessons.put(subject, new ArrayList<>());
        }
        lessons.get(subject).add(lesson);
    }

    public void cancelLesson(Subject subject, Lesson lesson) {
        if (lessons.containsKey(subject)) {
            lessons.get(subject).remove(lesson);
        }
    }
}

// Class to represent a lesson
class Lesson {
    private Tutor tutor;
    private Date date;
    private int hour;
    private String review;
    private int rating;

    public Lesson(Tutor tutor, Date date, int hour) {
        this.tutor = tutor;
        this.date = date;
        this.hour = hour;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public Date getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

// Class to manage the tuition system
class TuitionSystem {
    private Map<String, Tutor> tutors; // Key: Tutor name, Value: Tutor object
    private List<Student> students;

    public TuitionSystem() {
        this.tutors = new HashMap<>();
        this.students = new ArrayList<>();
    }

    public void addTutor(Tutor tutor) {
        tutors.put(tutor.getName(), tutor);
    }

    public Tutor getTutor(String name) {
        return tutors.get(name);
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void bookLesson(Student student, Subject subject, Tutor tutor, Date date, int hour) {
        Lesson lesson = new Lesson(tutor, date, hour);
        student.bookLesson(subject, lesson);
    }

    public void cancelLesson(Student student, Subject subject, Lesson lesson) {
        student.cancelLesson(subject, lesson);
    }

    public void printLessonSummary(Student student) {
        System.out.println("Lesson Summary for Student: " + student.getName());
        System.out.println("-------------------------------");

        Map<Subject, List<Lesson>> lessons = student.getLessons();
        for (Map.Entry<Subject, List<Lesson>> entry : lessons.entrySet()) {
            Subject subject = entry.getKey();
            List<Lesson> lessonList = entry.getValue();

            System.out.println("Subject: " + subject);
            System.out.println("Booked Lessons: " + lessonList.size());

            int attendedLessons = 0;
            int canceledLessons = 0;

            for (Lesson lesson : lessonList) {
                if (lesson.getRating() > 0) {
                    attendedLessons++;
                } else {
                    canceledLessons++;
                }
            }

            System.out.println("Attended Lessons: " + attendedLessons);
            System.out.println("Canceled Lessons: " + canceledLessons);
            System.out.println();
        }
    }

    public void printTutorReviews(Tutor tutor) {
        System.out.println("Reviews for Tutor: " + tutor.getName());
        System.out.println("---------------------------");

        for (Student student : students) {
            Map<Subject, List<Lesson>> lessons = student.getLessons();
            for (List<Lesson> lessonList : lessons.values()) {
                for (Lesson lesson : lessonList) {
                    if (lesson.getTutor().equals(tutor) && lesson.getRating() > 0) {
                        System.out.println("Student: " + student.getName());
                        System.out.println("Review: " + lesson.getReview());
                        System.out.println("Rating: " + lesson.getRating());
                        System.out.println();
                    }
                }
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TuitionSystem tuitionSystem = new TuitionSystem();

        // Add tutors
        System.out.println("Adding Tutors");
        System.out.println("-------------");
        System.out.print("Enter the number of tutors: ");
        int numTutors = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        for (int i = 0; i < numTutors; i++) {
            System.out.println("Tutor " + (i + 1));
            System.out.print("Enter tutor name: ");
            String tutorName = scanner.nextLine();
            System.out.print("Enter specialties (comma-separated): ");
            String specialtiesInput = scanner.nextLine();
            String[] specialtiesArray = specialtiesInput.split(",");
            Set<Subject> specialties = new HashSet<>();

            for (String specialty : specialtiesArray) {
                specialties.add(Subject.valueOf(specialty.trim().toUpperCase()));
            }

            Tutor tutor = new Tutor(tutorName, specialties);
            tuitionSystem.addTutor(tutor);
            System.out.println("Tutor added successfully!");
            System.out.println();
        }

        // Add students
        System.out.println("Adding Students");
        System.out.println("---------------");
        System.out.print("Enter the number of students: ");
        int numStudents = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        for (int i = 0; i < numStudents; i++) {
            System.out.println("Student " + (i + 1));
            System.out.print("Enter student name: ");
            String studentName = scanner.nextLine();
            System.out.print("Enter student gender: ");
            String gender = scanner.nextLine();
            System.out.print("Enter date of birth (YYYY-MM-DD): ");
            String dobString = scanner.nextLine();
            Date dob = parseDate(dobString);
            System.out.print("Enter emergency contact phone number: ");
            String emergencyContact = scanner.nextLine();

            Student student = new Student(studentName, gender, dob, emergencyContact);
            tuitionSystem.addStudent(student);
            System.out.println("Student added successfully!");
            System.out.println();
        }

        // Book lessons
        System.out.println("Booking Lessons");
        System.out.println("---------------");
        System.out.print("Enter the number of lessons to book: ");
        int numLessons = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        for (int i = 0; i < numLessons; i++) {
            System.out.println("Lesson " + (i + 1));
            System.out.print("Enter student name: ");
            String studentName = scanner.nextLine();
            System.out.print("Enter subject: ");
            String subjectString = scanner.nextLine();
            Subject subject = Subject.valueOf(subjectString.trim().toUpperCase());
            System.out.print("Enter tutor name: ");
            String tutorName = scanner.nextLine();
            System.out.print("Enter date (YYYY-MM-DD): ");
            String dateString = scanner.nextLine();
            Date date = parseDate(dateString);
            System.out.print("Enter hour (24-hour format): ");
            int hour = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            Student student = tuitionSystem.getStudents().stream()
                    .filter(s -> s.getName().equalsIgnoreCase(studentName))
                    .findFirst()
                    .orElse(null);

            Tutor tutor = tuitionSystem.getTutor(tutorName);

            if (student != null && tutor != null) {
                tuitionSystem.bookLesson(student, subject, tutor, date, hour);
                System.out.println("Lesson booked successfully!");
            } else {
                System.out.println("Invalid student or tutor name. Lesson booking failed!");
            }

            System.out.println();
        }

        // Print lesson summary for a student
        System.out.println("Print Lesson Summary");
        System.out.println("--------------------");
        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine();

        Student student = tuitionSystem.getStudents().stream()
                .filter(s -> s.getName().equalsIgnoreCase(studentName))
                .findFirst()
                .orElse(null);

        if (student != null) {
            tuitionSystem.printLessonSummary(student);
        } else {
            System.out.println("Invalid student name. Lesson summary cannot be printed!");
        }

        // Print tutor reviews
        System.out.println("Print Tutor Reviews");
        System.out.println("-------------------");
        System.out.print("Enter tutor name: ");
        String tutorName = scanner.nextLine();

        Tutor tutor = tuitionSystem.getTutor(tutorName);

        if (tutor != null) {
            tuitionSystem.printTutorReviews(tutor);
        } else {
            System.out.println("Invalid tutor name. Tutor reviews cannot be printed!");
        }

        scanner.close();
    }

    private static Date parseDate(String dateString) {

        return null;
    }
}
