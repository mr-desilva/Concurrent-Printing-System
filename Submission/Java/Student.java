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
            int MAXIMUM_NUMBER_OF_PAGE_PER_DOCUMENT = 10;
            // random number of pages per document, Adding 1 to ensure document is at least one page in length
            int numberOfPages = MINIMUM_NUMBER_OF_PAGE_PER_DOCUMENT +
                    random.nextInt(MAXIMUM_NUMBER_OF_PAGE_PER_DOCUMENT - MINIMUM_NUMBER_OF_PAGE_PER_DOCUMENT);
            String documentName = "cwk" + i;


            Document document = new Document(this.getName(), documentName, numberOfPages);
            printer.printDocument(document);

            // After printing the final document no need to sleep the student thread
            boolean lastDocument = i == numberOfDocumentsPerStudent;
            // Student should sleep for a random time between each attempt to print the documents.
            if (!lastDocument) {
                int MINIMUM_SLEEPING_TIME = 1000;
                int MAXIMUM_SLEEPING_TIME = 5000;
                int sleepingTime = MINIMUM_SLEEPING_TIME + random.nextInt(MAXIMUM_SLEEPING_TIME - MINIMUM_SLEEPING_TIME);
                try {
                    sleep(sleepingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Utilities.printLogs(Utilities.MessageOwner.STUDENT, this.getName() + " was interrupted during sleeping time "
                            + sleepingTime + " after printing : " + documentName, Utilities.MessageType.ERROR);
                }
            }
        }

        Utilities.printLogs(Utilities.MessageOwner.STUDENT, this.getName() + " Finished printing : " + numberOfDocumentsPerStudent, Utilities.MessageType.INFO);
    }
}
