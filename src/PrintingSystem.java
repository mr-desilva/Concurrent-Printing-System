import Utils.Utilities;

public class PrintingSystem {
    public static void main(String[] args) {
        // Clearing the text file with previous logs
        Utilities.clearFile();
        // Creating the thread groups
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Creating Thread Groups...", Utilities.MessageType.INFO);
        ThreadGroup students = new ThreadGroup("students");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Created Student Thread Group...finished", Utilities.MessageType.INFO);
        ThreadGroup technicians = new ThreadGroup("technicians");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Creating Technicians Thread Group...finished", Utilities.MessageType.INFO);

        // Initialising the printer object
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialising Printer Object....", Utilities.MessageType.INFO);
        LaserPrinter printer = new LaserPrinter(0001, 40, 10, students);
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialising Printer Object finished", Utilities.MessageType.INFO);

        // Creating student threads
        Thread student1 = new Student(students, printer, "Tharindu");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialised Student [" + student1.getName() + "]", Utilities.MessageType.INFO);
        Thread student2 = new Student(students, printer, "Adeesha");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialised Student [" + student2.getName() + "]", Utilities.MessageType.INFO);
        Thread student3 = new Student(students, printer, "Sanjula");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialised Student [" + student3.getName() + "]", Utilities.MessageType.INFO);
        Thread student4 = new Student(students, printer, "Nathindu");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialised Student [" + student4.getName() + "]", Utilities.MessageType.INFO);

        // Creating the technician threads
        Thread paperTechnician = new PaperTechnician(technicians, printer, "paperTechnician");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialised Paper Technician", Utilities.MessageType.INFO);
        Thread tonerTechnician = new TonerTechnician(technicians, printer, "tonerTechnician");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialised Toner Technician", Utilities.MessageType.INFO);

        // Starting threads
        student1.start();
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + student1.getName() + "] Started...", Utilities.MessageType.INFO);
        student2.start();
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + student2.getName() + "] Started...", Utilities.MessageType.INFO);
        student3.start();
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + student3.getName() + "] Started...", Utilities.MessageType.INFO);
        student4.start();
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + student4.getName() + "] Started...", Utilities.MessageType.INFO);
        paperTechnician.start();
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Paper Technician Thread Started...", Utilities.MessageType.INFO);
        tonerTechnician.start();
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Toner Technician Thread Started...", Utilities.MessageType.INFO);

        try {
            student1.join();
            Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + student1.getName() + "] Finished execution", Utilities.MessageType.INFO);
            student2.join();
            Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + student2.getName() + "] Finished execution", Utilities.MessageType.INFO);
            student3.join();
            Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + student3.getName() + "] Finished execution", Utilities.MessageType.INFO);
            student4.join();
            Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + student4.getName() + "] Finished execution", Utilities.MessageType.INFO);
            paperTechnician.join();
            Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Paper Technician Thread Finished execution", Utilities.MessageType.INFO);
            tonerTechnician.join();
            Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Toner Technician Thread Finished execution", Utilities.MessageType.INFO);

            Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "All tasks successfully completed, Total Number of pages printed : " + printer.getNumberOfPrintedPages()
                     + ", Final Status of printer " + printer.toString(), Utilities.MessageType.INFO);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
