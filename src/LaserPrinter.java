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
    private int numberOfPrintedPages;
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
        this.numberOfPrintedPages = 0;
        this.printerCondition = resourceLock.newCondition();
    }

    @Override
    public synchronized void replaceTonerCartridge() {
        this.resourceLock.lock();

        try {
            while (currentTonerLevel > Minimum_Toner_Level) {
                if(students.activeCount() > 0) {
                    Utilities.printLogs(Utilities.MessageOwner.TONER_TECHNICIAN, "Checking toner...No need to replace the toner, Current toner Level is "
                            + currentTonerLevel, Utilities.MessageType.INFO);
                    printerCondition.await(5, TimeUnit.SECONDS);
                } else {
                    Utilities.printLogs(Utilities.MessageOwner.PRINTER, "No requests for printing Documents....", Utilities.MessageType.INFO);
                    break;
                }
            }

            if(currentTonerLevel < Minimum_Toner_Level) {
                // Refilling the toner cartridge
                Utilities.printLogs(Utilities.MessageOwner.TONER_TECHNICIAN, "toner is low, current toner level is " + currentTonerLevel, Utilities.MessageType.WARN);
                currentTonerLevel += PagesPerTonerCartridge;
                cartridgesReplacedCount ++;
                Utilities.printLogs(Utilities.MessageOwner.TONER_TECHNICIAN, "toner is replaced, new toner level is " + currentTonerLevel, Utilities.MessageType.INFO);
            }
            this.printerCondition.signalAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.resourceLock.unlock();
        }
    }

    @Override
    public synchronized void refillPaper() {
        this.resourceLock.lock();

        try {
            boolean printerCannotBeRefilled = (currentPaperLevel + SheetsPerPack) > Full_Paper_Tray;
            while (printerCannotBeRefilled) {
                if(students.activeCount() > 0) {
                    Utilities.printLogs(Utilities.MessageOwner.PAPER_TECHNICIAN, "Checking paper...No need to refill the papers, Current Paper Level is "
                            + currentPaperLevel, Utilities.MessageType.INFO);

                    printerCondition.await(5, TimeUnit.SECONDS);
                } else {
                    Utilities.printLogs(Utilities.MessageOwner.PRINTER, "No requests for printing Documents....", Utilities.MessageType.INFO);
                    break;
                }
            }
            if(currentPaperLevel + SheetsPerPack <= Full_Paper_Tray) {
                // Allow paper technician to refill paper
                Utilities.printLogs(Utilities.MessageOwner.PAPER_TECHNICIAN, "Checking paper... Refilling printer with paper... ", Utilities.MessageType.INFO);
                currentPaperLevel += SheetsPerPack;
                this.paperPackReplacedCount ++;
                Utilities.printLogs(Utilities.MessageOwner.PAPER_TECHNICIAN, "Refilled tray with pack of paper. New Paper Level: " + currentPaperLevel, Utilities.MessageType.INFO);
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
                    Utilities.printLogs(Utilities.MessageOwner.PRINTER, "Out of paper and toner. Current Paper Level is " + currentPaperLevel + " and Toner Level is " + currentTonerLevel, Utilities.MessageType.INFO);
                }
                else if(currentPaperLevel < numberOfPages) {
                    Utilities.printLogs(Utilities.MessageOwner.PRINTER, "Out of paper. Current Paper Level is " + currentPaperLevel, Utilities.MessageType.WARN);
                }
                else {
                    Utilities.printLogs(Utilities.MessageOwner.PRINTER, "Out of toner. Current Toner Level is " + currentTonerLevel, Utilities.MessageType.WARN);
                }

                printerCondition.await();
            }

            if (currentPaperLevel > numberOfPages && currentTonerLevel > numberOfPages) {
                Utilities.printLogs(Utilities.MessageOwner.PRINTER, "Printing document with page length " + numberOfPages, Utilities.MessageType.INFO);
                currentPaperLevel -= numberOfPages;
                currentTonerLevel -= numberOfPages;
                numberOfDocumentsPrinted++;
                this.numberOfPrintedPages += numberOfPages;
                Utilities.printLogs(Utilities.MessageOwner.PRINTER, "Successfully printed the document. New Paper Level is " + currentPaperLevel + " and Toner Level is " + currentTonerLevel, Utilities.MessageType.INFO);
                this.printerCondition.signalAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.resourceLock.unlock();
        }


    }

    public int getNumberOfPrintedPages() {
        return numberOfPrintedPages;
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
