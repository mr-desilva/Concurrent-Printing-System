import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LaserPrinter implements ServicePrinter{
    private int id;
    private int currentPaperLevel;
    private int currentTonerLevel;
    private int numberOfDocumentsPrinted;
    private ThreadGroup students;

    // reentrant Lock
    private Lock resourceLock = new ReentrantLock(true); //fairness Enabled
    private Condition printerCondition;

    public LaserPrinter(int id, int initialPaperLevel, int initialTonerLevel, ThreadGroup students) {
        this.id = id;
        this.currentPaperLevel = initialPaperLevel;
        this.currentTonerLevel = initialTonerLevel;
        this.students = students;
        this.numberOfDocumentsPrinted = 0;
        this.printerCondition = resourceLock.newCondition();
    }

    @Override
    public synchronized void replaceTonerCartridge() {
        this.resourceLock.lock();

        try {
            while (currentTonerLevel > Minimum_Toner_Level) {
                if(students.activeCount() > 0) {
                    System.out.printf(ConsoleColor.PURPLE +
                            "Checking toner... " + ConsoleColor.RESET +
                            "Toner need not be replaced at this time. Current Toner Level is %d\n", currentTonerLevel);
                    printerCondition.await(5, TimeUnit.SECONDS);
                } else {
                    System.out.println("Printing finished");
                    break;
                }
            }

            if(currentTonerLevel < Minimum_Toner_Level) {
                // Refilling the toner cartridge
                System.out.println("toner is low, current toner level is " + currentTonerLevel);
                currentTonerLevel += PagesPerTonerCartridge;
                System.out.println("toner is replaced, new toner level is " + currentTonerLevel);
            }
            this.printerCondition.signalAll();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.resourceLock.unlock();
        }
    }

    private boolean allStudentsHaveFinishedPrinting() {
        return students.activeCount() < 1;
    }

    @Override
    public synchronized void refillPaper() {
        this.resourceLock.lock();

        try {
            boolean printerCannotBeRefilled = (currentPaperLevel + SheetsPerPack) > Full_Paper_Tray;
            while (printerCannotBeRefilled) {
                if(students.activeCount() > 0) {
                    System.out.printf(ConsoleColor.YELLOW +
                            "Checking paper... " + ConsoleColor.RESET +
                            "Paper Tray cannot be refilled at this time (exceeds maximum paper level). " +
                            "Current paper level is %d and Maximum paper level is %d.\n", currentPaperLevel, Full_Paper_Tray);

                    printerCondition.await(5, TimeUnit.SECONDS);
                } else {
                    System.out.println("Printing finished");
                    break;
                }
            }
            if(currentPaperLevel + SheetsPerPack <= Full_Paper_Tray) {
                // Allow paper technician to refill paper
                System.out.print(ConsoleColor.YELLOW + "Checking paper... Refilling printer with paper... ");
                currentPaperLevel += SheetsPerPack;
                System.out.printf("Refilled tray with pack of paper. New Paper Level: %d.\n" + ConsoleColor.RESET, currentPaperLevel);
            }
            this.printerCondition.signalAll();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.resourceLock.unlock();
        }
    }

    @Override
    public void printDocument(Document document) {
        this.resourceLock.lock();

        try {
            String student = document.getUserID();
            String docName = document.getDocumentName();
            int numberOfPages = document.getNumberOfPages();

            boolean insufficientPaperLevel = numberOfPages > currentPaperLevel;
            boolean insufficientTonerLevel = numberOfPages > currentTonerLevel;

            while (currentPaperLevel < numberOfPages || currentTonerLevel < numberOfPages) {
                // User cannot print
                if(currentPaperLevel < numberOfPages && currentTonerLevel < numberOfPages) {
                    System.out.printf(ConsoleColor.RED + "[%s][%s][%dpg] - Out of paper and toner. Current Paper Level is %d and Toner Level is %d.\n" + ConsoleColor.RESET,
                            student, docName, numberOfPages, currentPaperLevel, currentTonerLevel);
                }
                else if(currentPaperLevel < numberOfPages) {
                    System.out.printf(ConsoleColor.RED + "[%s][%s][%dpg] - Out of paper. Current Paper Level is %d.\n" + ConsoleColor.RESET,
                            student, docName, numberOfPages, currentPaperLevel);
                }
                else {
                    System.out.printf(ConsoleColor.RED + "[%s][%s][%dpg] - Out of toner. Current Toner Level is %d.\n" + ConsoleColor.RESET,
                            student, docName, numberOfPages, currentTonerLevel);
                }

//                try {
//                    wait();
//                    insufficientPaperLevel = numberOfPages > currentPaperLevel;
//                    insufficientTonerLevel = numberOfPages > currentTonerLevel;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                printerCondition.await();
            }

            if (currentPaperLevel > numberOfPages && currentTonerLevel > numberOfPages) {
                System.out.printf("[%s][%s][%dpg] - Printing document with page length: %d.\n",
                        student, docName, numberOfPages, numberOfPages);
                currentPaperLevel -= numberOfPages;
                currentTonerLevel -= numberOfPages;
                numberOfDocumentsPrinted++;
                System.out.printf("[%s][%s][%dpg] - Successfully printed the document. New Paper Level is %d and Toner Level is %d.\n",
                        student, docName, numberOfPages,
                        currentPaperLevel, currentTonerLevel);

                this.printerCondition.signalAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.resourceLock.unlock();
        }


    }


    @Override
    public  String toString() {
        return "LaserPrinter{" +
                "PrinterID: '" + id + '\'' + ", " +
                "Paper Level: " + currentPaperLevel + ", " +
                "Toner Level: " + currentTonerLevel + ", " +
                "Documents Printed: " + numberOfDocumentsPrinted +
                '}';
    }
}
