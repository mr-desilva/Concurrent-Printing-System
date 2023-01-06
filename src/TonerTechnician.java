import Utils.Utilities;

import java.util.Random;

public class TonerTechnician extends Thread{
    private ServicePrinter printer;

    public TonerTechnician(ThreadGroup group, ServicePrinter printer, String name) {
        super(group, name);
        this.printer = printer;
    }

    @Override
    public void run() {
        Random random = new Random();
        int numberOfRefills = 3;

        for (int i = 1; i <= numberOfRefills; i++) {
           printer.replaceTonerCartridge();

            // Toner Technician's should sleep for a random time between each attempt to refill the toner.
            int MINIMUM_SLEEPING_TIME = 1000;
            int MAXIMUM_SLEEPING_TIME = 5000;
            int sleepingTime = MINIMUM_SLEEPING_TIME + random.nextInt(MAXIMUM_SLEEPING_TIME - MINIMUM_SLEEPING_TIME);
            try {
                Thread.sleep(sleepingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Utilities.printLogs(Utilities.MessageOwner.TONER_TECHNICIAN, "Toner Technician was interrupted during sleeping time " + sleepingTime +
                                ", after replacing toner cartridge no " + i, Utilities.MessageType.ERROR);
            }
        }
        Utilities.printLogs(Utilities.MessageOwner.TONER_TECHNICIAN, "Toner Technician Finished, cartridges replaced "
                + LaserPrinter.cartridgesReplacedCount, Utilities.MessageType.INFO);
    }
}
