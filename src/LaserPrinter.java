import Utils.ConsoleColor;
import Utils.Utilities;

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
    static int paperPackReplacedCount;
    static int cartridgesReplacedCount;

    // reentrant Lock with fairness enabled
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
                    Utilities.printLogs(Utilities.ProcessLogger.TONER_TECHNICIAN, "Checking toner...No need to replace the toner, Current toner Level is "
                            + currentTonerLevel, Utilities.ProcessLogger.INFO);
//                    System.out.printf(ConsoleColor.PURPLE +
//                            "Checking toner... " + ConsoleColor.RESET +
//                            "No need to replace the toner. Current Toner Level is %d\n", currentTonerLevel);
                    printerCondition.await(5, TimeUnit.SECONDS);
                } else {
                    Utilities.printLogs(Utilities.ProcessLogger.PRINTER, "No requests for printing Documents....", Utilities.ProcessLogger.INFO);
                    //System.out.println("No requests for printing Documents....");
                    break;
                }
            }

            if(currentTonerLevel < Minimum_Toner_Level) {
                // Refilling the toner cartridge
                Utilities.printLogs(Utilities.ProcessLogger.TONER_TECHNICIAN, "toner is low, current toner level is " + currentTonerLevel, Utilities.ProcessLogger.WARN);
                //System.out.println("toner is low, current toner level is " + currentTonerLevel);
                currentTonerLevel += PagesPerTonerCartridge;
                cartridgesReplacedCount ++;
                Utilities.printLogs(Utilities.ProcessLogger.TONER_TECHNICIAN, "toner is replaced, new toner level is " + currentTonerLevel, Utilities.ProcessLogger.INFO);
                //System.out.println("toner is replaced, new toner level is " + currentTonerLevel);
            }
            this.printerCondition.signalAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
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
                    Utilities.printLogs(Utilities.ProcessLogger.PAPER_TECHNICIAN, "Checking paper...No need to refill the papers, Current Paper Level is "
                            + currentPaperLevel, Utilities.ProcessLogger.INFO);
//                    System.out.printf(ConsoleColor.YELLOW +
//                            "Checking paper... " + ConsoleColor.RESET +
//                            "Paper Tray cannot be refilled at this time (exceeds maximum paper level). " +
//                            "Current paper level is %d and Maximum paper level is %d.\n", currentPaperLevel, Full_Paper_Tray);

                    printerCondition.await(5, TimeUnit.SECONDS);
                } else {
                    Utilities.printLogs(Utilities.ProcessLogger.PRINTER, "No requests for printing Documents....", Utilities.ProcessLogger.INFO);
                    //System.out.println("No requests for printing Documents....");
                    break;
                }
            }
            if(currentPaperLevel + SheetsPerPack <= Full_Paper_Tray) {
                // Allow paper technician to refill paper
                Utilities.printLogs(Utilities.ProcessLogger.PAPER_TECHNICIAN, "Checking paper... Refilling printer with paper... ", Utilities.ProcessLogger.INFO);
                //System.out.print(ConsoleColor.YELLOW + "Checking paper... Refilling printer with paper... ");
                currentPaperLevel += SheetsPerPack;
                this.paperPackReplacedCount ++;
                Utilities.printLogs(Utilities.ProcessLogger.PAPER_TECHNICIAN, "Refilled tray with pack of paper. New Paper Level: " + currentPaperLevel, Utilities.ProcessLogger.INFO);
                //System.out.printf("Refilled tray with pack of paper. New Paper Level: %d.\n" + ConsoleColor.RESET, currentPaperLevel);
            }
            this.printerCondition.signalAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
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

            while (currentPaperLevel < numberOfPages || currentTonerLevel < numberOfPages) {
                // User cannot print
                if(currentPaperLevel < numberOfPages && currentTonerLevel < numberOfPages) {
                    Utilities.printLogs(Utilities.ProcessLogger.PRINTER, "Out of paper and toner. Current Paper Level is " + currentPaperLevel + " and Toner Level is " + currentTonerLevel, Utilities.ProcessLogger.INFO);
//                    System.out.printf(ConsoleColor.RED + "[%s][%s][%dpg] - Out of paper and toner. Current Paper Level is %d and Toner Level is %d.\n" + ConsoleColor.RESET,
//                            student, docName, numberOfPages, currentPaperLevel, currentTonerLevel);
                }
                else if(currentPaperLevel < numberOfPages) {
                    Utilities.printLogs(Utilities.ProcessLogger.PRINTER, "Out of paper. Current Paper Level is " + currentPaperLevel, Utilities.ProcessLogger.WARN);
//                    System.out.printf(ConsoleColor.RED + "[%s][%s][%dpg] - Out of paper. Current Paper Level is %d.\n" + ConsoleColor.RESET,
//                            student, docName, numberOfPages, currentPaperLevel);
                }
                else {
                    Utilities.printLogs(Utilities.ProcessLogger.PRINTER, "Out of toner. Current Toner Level is " + currentTonerLevel, Utilities.ProcessLogger.WARN);
//                    System.out.printf(ConsoleColor.RED + "[%s][%s][%dpg] - Out of toner. Current Toner Level is %d.\n" + ConsoleColor.RESET,
//                            student, docName, numberOfPages, currentTonerLevel);
                }

                printerCondition.await();
            }

            if (currentPaperLevel > numberOfPages && currentTonerLevel > numberOfPages) {
                Utilities.printLogs(Utilities.ProcessLogger.PRINTER, "Printing document with page length " + numberOfPages, Utilities.ProcessLogger.INFO);
//                System.out.printf("[%s][%s][%dpg] - Printing document with page length: %d.\n",
//                        student, docName, numberOfPages, numberOfPages);
                currentPaperLevel -= numberOfPages;
                currentTonerLevel -= numberOfPages;
                numberOfDocumentsPrinted++;
                Utilities.printLogs(Utilities.ProcessLogger.PRINTER, "Successfully printed the document. New Paper Level is " + currentPaperLevel + " and Toner Level is " + currentTonerLevel, Utilities.ProcessLogger.INFO);
//                System.out.printf("[%s][%s][%dpg] - Successfully printed the document. New Paper Level is %d and Toner Level is %d.\n",
//                        student, docName, numberOfPages,
//                        currentPaperLevel, currentTonerLevel);

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
