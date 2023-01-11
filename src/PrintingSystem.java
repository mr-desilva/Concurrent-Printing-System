import Utils.Utilities;

public class PrintingSystem {
    public static void main(String[] args) {
        // Clearing the text file with previous logs
        Utilities.clearFile();
        // Creating the thread groups
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Creating Thread Groups...", Utilities.MessageType.INFO);
        ThreadGroup studentsGroup = new ThreadGroup("Students");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Created Student Thread Group...finished", Utilities.MessageType.INFO);
        ThreadGroup technicalGroup = new ThreadGroup("Technician");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Creating Technicians Thread Group...finished", Utilities.MessageType.INFO);

        // Initialising the printer object
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialising Printer Object....", Utilities.MessageType.INFO);
        ServicePrinter printer = new LaserPrinter("canon printer", 10, 110, 15);
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialising Printer Object finished", Utilities.MessageType.INFO);

        Runnable studentRunnable1 = new Student("Adeesha", studentsGroup, printer);
        Runnable studentRunnable2 = new Student("Malith", studentsGroup, printer);
        Runnable studentRunnable3 = new Student("Sanjula", studentsGroup, printer);
        Runnable studentRunnable4 = new Student("Abhilash", studentsGroup, printer);

        // Creating student threads
        Thread studentThread1 = new Thread(studentsGroup, studentRunnable1, "Adeesha");;
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialised Student [" + studentThread1.getName() + "]", Utilities.MessageType.INFO);
        Thread studentThread2 = new Thread(studentsGroup, studentRunnable2, "Malith");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialised Student [" + studentThread2.getName() + "]", Utilities.MessageType.INFO);
        Thread studentThread3 = new Thread(studentsGroup, studentRunnable3, "Sanjula");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialised Student [" + studentThread3.getName() + "]", Utilities.MessageType.INFO);
        Thread studentThread4 = new Thread(studentsGroup, studentRunnable4, "Abhilash");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialised Student [" + studentThread4.getName() + "]", Utilities.MessageType.INFO);


        Runnable paperTechnician =new PaperTechnician("PaperTech", technicalGroup, printer);
        Runnable tonerTechnician =new TonerTechnician("TornerTech", technicalGroup, printer);
        // Creating the technician threads
        Thread paperTechThread = new Thread(technicalGroup, paperTechnician, "Paper Tech");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialised Paper Technician", Utilities.MessageType.INFO);
        Thread tonerTechThread = new Thread(technicalGroup, tonerTechnician, "Toner Tech");
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Initialised Toner Technician", Utilities.MessageType.INFO);

        // Starting threads
        studentThread1.start();
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + studentThread1.getName() + "] Started...", Utilities.MessageType.INFO);
        studentThread2.start();
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + studentThread2.getName() + "] Started...", Utilities.MessageType.INFO);
        studentThread3.start();
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + studentThread3.getName() + "] Started...", Utilities.MessageType.INFO);
        studentThread4.start();
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + studentThread4.getName() + "] Started...", Utilities.MessageType.INFO);
        paperTechThread.start();
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Paper Technician Thread Started...", Utilities.MessageType.INFO);
        tonerTechThread.start();
        Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Toner Technician Thread Started...", Utilities.MessageType.INFO);

        try {
            studentThread1.join();
            Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + studentThread1.getName() + "] Finished execution", Utilities.MessageType.INFO);
            studentThread2.join();
            Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + studentThread2.getName() + "] Finished execution", Utilities.MessageType.INFO);
            studentThread3.join();
            Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + studentThread3.getName() + "] Finished execution", Utilities.MessageType.INFO);
            studentThread4.join();
            Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Student Thread [" + studentThread4.getName() + "] Finished execution", Utilities.MessageType.INFO);
            paperTechThread.join();
            Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Paper Technician Thread Finished execution", Utilities.MessageType.INFO);
            tonerTechThread.join();
            Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "Toner Technician Thread Finished execution", Utilities.MessageType.INFO);

            Utilities.printLogs(Utilities.MessageOwner.PRINTING_SYSTEM, "All tasks successfully completed. "
                     + ", Final Status of printer " + printer.toString(), Utilities.MessageType.INFO);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
