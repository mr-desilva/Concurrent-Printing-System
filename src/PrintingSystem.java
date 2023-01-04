public class PrintingSystem {
    public static void main(String[] args) {
        ThreadGroup students = new ThreadGroup("students");
        ThreadGroup technicians = new ThreadGroup("technicians");

        ServicePrinter printer = new LaserPrinter(0002, 10, 10, students);

        Thread student1 = new Student(students, printer, "student1");
        Thread student2 = new Student(students, printer, "student2");
        Thread student3 = new Student(students, printer, "student3");
        Thread student4 = new Student(students, printer, "student4");

        Thread paperTechnician = new PaperTechnician(technicians, printer, "paperTechnician");
        Thread tonerTechnician = new TonerTechnician(technicians, printer, "tonerTechnician");


        student1.start();
        student2.start();
        student3.start();
        student4.start();
        paperTechnician.start();
        tonerTechnician.start();

        try {
            student1.join();
            student2.join();
            student3.join();
            student4.join();
            paperTechnician.join();
            tonerTechnician.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
