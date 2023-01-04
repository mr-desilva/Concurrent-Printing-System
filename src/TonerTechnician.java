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
           // printer.replaceTonerCartridge();

            // Excerpt from spec
            // Paper Technician's behaviour is to ... He/she should "sleep" for a random amount of time between each attempt to refill the paper...
            // Toner Technician... This class is very similar to the paper technician class
            int MINIMUM_SLEEPING_TIME = 1000;
            int MAXIMUM_SLEEPING_TIME = 5000;
            int sleepingTime = MINIMUM_SLEEPING_TIME + random.nextInt(MAXIMUM_SLEEPING_TIME - MINIMUM_SLEEPING_TIME);
            // int sleepingTime = 5000;
            try {
                printer.replaceTonerCartridge();
                Thread.sleep(sleepingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.printf("Toner Technician %s was interrupted during sleeping time " +
                                "after replacing toner cartridge no. %d.\n",
                        sleepingTime, i);
            }
        }

        System.out.printf("Toner Technician Finished, cartridges replaced %s.\n", LaserPrinter.cartridgesReplacedCount);
    }
}
