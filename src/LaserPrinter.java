import Utils.Utilities;

public class LaserPrinter implements ServicePrinter{
    private String name;
    private int id;
    private int currentPaperLevel;
    private int currentTonerLevel;
    private int numberOfDocumentsPrinted;
    private boolean paperRefilled = false; // to keep track of call to refill paper() is sucesssful or not
    private boolean tonerRefilled = false; // to keep track of call to refill replaced() is sucessfull or not

    public boolean isTonerRefilled() {
        return tonerRefilled;
    }

    public boolean isPaperRefilled() {
        return paperRefilled;
    }

    public LaserPrinter(String name, int id, int currentPaperLevel, int currentTonerLevel) {
        super();
        this.name = name;
        this.id = id;
        this.currentPaperLevel = currentPaperLevel;
        this.currentTonerLevel = currentTonerLevel;
        this.numberOfDocumentsPrinted = 0;
        this.paperRefilled = paperRefilled;
        this.tonerRefilled = tonerRefilled;
    }

    /*
     * method to replace Toner cartridge of printer
     * toner can only be replaced when current toner level goes below the minimum toner level
     * when toner cartridge is replaced, toner level goes upto full toner level
     */

    @Override
    public synchronized void replaceTonerCartridge() {
        this.tonerRefilled = false;
        if (currentTonerLevel > Minimum_Toner_Level) {
            try {
                Utilities.printLogs(Utilities.MessageOwner.TONER_TECHNICIAN, "Checking toner...No need to replace the toner, Current toner Level is "
                        + currentTonerLevel, Utilities.MessageType.INFO);
                wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (currentTonerLevel < Minimum_Toner_Level) {
            Utilities.printLogs(Utilities.MessageOwner.TONER_TECHNICIAN, "toner is low, current toner level is " + currentTonerLevel, Utilities.MessageType.WARN);
            this.currentTonerLevel = PagesPerTonerCartridge;
            Utilities.printLogs(Utilities.MessageOwner.TONER_TECHNICIAN, "toner is replaced, new toner level is " + currentTonerLevel, Utilities.MessageType.INFO);
            this.tonerRefilled = true;
        }
        notifyAll();
    }



    /**
     * method to refill paper tray of printer with paper
     * paper tray can only be refilled if the new paper level does not exceed the full (maximum) paper level
     * each paper refill increases the paper level by the number of papers in pack
     */

    @Override
    public void refillPaper() {
        this.paperRefilled = false;
        while (currentPaperLevel == Full_Paper_Tray){
            try {
                Utilities.printLogs(Utilities.MessageOwner.PAPER_TECHNICIAN, "Checking paper...No need to refill the papers, Current Paper Level is "
                        + currentPaperLevel, Utilities.MessageType.INFO);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (currentPaperLevel < Full_Paper_Tray) {
            Utilities.printLogs(Utilities.MessageOwner.PAPER_TECHNICIAN, "Checking paper... Refilling printer with paper... ", Utilities.MessageType.INFO);
            this.currentPaperLevel += SheetsPerPack;
            Utilities.printLogs(Utilities.MessageOwner.PAPER_TECHNICIAN, "Refilled tray with pack of paper. New Paper Level: " + currentPaperLevel, Utilities.MessageType.INFO);
            this.paperRefilled = true;
        }
    }



    /**
     * method to print a documents
     * a document can be printed if there are enough paper and toner resources
     * printing a document reduces both the paper level and toner level by the number of pages in the document.
     */

    @Override
    public synchronized void printDocument(Document document) {
        //if the printer does not have enough paper or toner, the process has to wait
        while (document.getNumberOfPages() >= currentPaperLevel || document.getNumberOfPages() >= currentTonerLevel) {
            if(currentTonerLevel < document.getNumberOfPages()) {
                Utilities.printLogs(Utilities.MessageOwner.PRINTER, "Out of paper and toner. Current Paper Level is " + currentPaperLevel + " and Toner Level is " + currentTonerLevel, Utilities.MessageType.WARN);
            } else if (currentPaperLevel < document.getNumberOfPages()) {
                Utilities.printLogs(Utilities.MessageOwner.PRINTER, "Out of paper. Current Paper Level is " + currentPaperLevel, Utilities.MessageType.WARN);
            }
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if(currentPaperLevel > document.getNumberOfPages() && currentTonerLevel > document.getNumberOfPages()) {
            Utilities.printLogs(Utilities.MessageOwner.PRINTER, Thread.currentThread().getName() +  " is printing " + document.getDocumentName() + " with page length " + document.getNumberOfPages(), Utilities.MessageType.INFO);
            this.currentPaperLevel -= document.getNumberOfPages();
            this.currentTonerLevel -= document.getNumberOfPages();
            numberOfDocumentsPrinted++;
            Utilities.printLogs(Utilities.MessageOwner.PRINTER, Thread.currentThread().getName() + " successfully printed the document. New Paper Level is " + currentPaperLevel + " and Toner Level is " + currentTonerLevel, Utilities.MessageType.INFO);
        }
        notifyAll();
    }

    @Override
    public String toString() {
        return "LaserPrinter{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", Current Paper Level =" + currentPaperLevel +
                ", Current Toner Level =" + currentTonerLevel +
                ", Number of documents printed=" + numberOfDocumentsPrinted +
                '}';
    }
}
