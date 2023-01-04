import Utils.Utilities;

import java.util.Random;

public class PaperTechnician extends Thread {
    private ServicePrinter printer;

    public PaperTechnician(ThreadGroup group, ServicePrinter printer, String name) {
        super(group, name);
        this.printer = printer;
    }

    @Override
    public void run() {
        Random random = new Random();
        int numberOfRefills = 3;

        for (int i = 1; i <= numberOfRefills; i++) {
            printer.refillPaper();

            // Excerpt from spec
            // Paper Technician's behaviour is to ... He/she should "sleep" for a random amount of time between each attempt to refill the paper.
            int MINIMUM_SLEEPING_TIME = 1000;
            int MAXIMUM_SLEEPING_TIME = 5000;
            int sleepingTime = MINIMUM_SLEEPING_TIME + random.nextInt(MAXIMUM_SLEEPING_TIME - MINIMUM_SLEEPING_TIME);
            try {
                Thread.sleep(sleepingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Utilities.printLogs(Utilities.MessageOwner.PAPER_TECHNICIAN, "Paper Technician was interrupted during sleeping time " + sleepingTime +
                                ", after refilling paper pack no " + i, Utilities.MessageType.ERROR);
            }
        }

        Utilities.printLogs(Utilities.MessageOwner.PAPER_TECHNICIAN, "Paper Technician Finished, packs of paper used "
                + LaserPrinter.paperPackReplacedCount, Utilities.MessageType.INFO);
    }
}
