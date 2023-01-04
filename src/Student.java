import Utils.ConsoleColor;
import Utils.Utilities;

import java.util.Random;

public class Student extends Thread{
    private Printer printer;

    public Student(ThreadGroup threadGroup, Printer printer, String name) {
        super(threadGroup, name);
        this.printer = printer;
    }

    @Override
    public void run() {
        Random random = new Random();
        int numberOfDocumentsPerStudent = 5;

        for (int i = 1; i <= numberOfDocumentsPerStudent; i++) {

            int MINIMUM_NUMBER_OF_PAGE_PER_DOCUMENT = 1;

            // If this is greater than the Toner Level, then there is a problem eventually where Toner won't refill
            // because Toner Level hasn't dropped below the specified threshold.
            // But a student may want to print a document with more pages than the current toner level.
            int MAXIMUM_NUMBER_OF_PAGE_PER_DOCUMENT = 10;

            int numberOfPages = MINIMUM_NUMBER_OF_PAGE_PER_DOCUMENT +
                    random.nextInt(MAXIMUM_NUMBER_OF_PAGE_PER_DOCUMENT - MINIMUM_NUMBER_OF_PAGE_PER_DOCUMENT); // Adding 1 to ensure document is at least one page in length
            String documentName = "cwk" + i;

            // System.out.printf("Printing document: %s of student: %s with page length: %s.\n", documentName, name, numberOfPages);
            Document document = new Document(this.getName(), documentName, numberOfPages);
            printer.printDocument(document);

            // Excerpt from spec
            // Student's behaviour is to ... He/she should "sleep" for a random amount of time between each printing request.
            boolean lastDocument = i == numberOfDocumentsPerStudent;
            if (!lastDocument) {
                int MINIMUM_SLEEPING_TIME = 1000;
                int MAXIMUM_SLEEPING_TIME = 5000;
                int sleepingTime = MINIMUM_SLEEPING_TIME + random.nextInt(MAXIMUM_SLEEPING_TIME - MINIMUM_SLEEPING_TIME);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.printf("%s was interrupted during sleeping time after printing \'%s\' document.\n",
                            sleepingTime, documentName);
                }
            }
        }

        Utilities.printLogs(Utilities.ProcessLogger.STUDENT, this.getName() + " Finished printing : " + numberOfDocumentsPerStudent, Utilities.ProcessLogger.INFO);
        //System.out.printf(ConsoleColor.GREEN + "Student %s finished printing documents.\n" + ConsoleColor.RESET, this.getName());
    }
}
