import Utils.Utilities;

public class PaperTechnician implements Runnable{
    private String name;
    private ThreadGroup group;
    private ServicePrinter printer;

    public PaperTechnician(String name, ThreadGroup group, ServicePrinter printer) {
        super();
        this.name = name;
        this.group = group;
        this.printer = printer;
    }


    @Override
    public void run() {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            printer.refillPaper();

            if(((LaserPrinter)printer).isPaperRefilled()){
                count ++;
            }
            try {
                int num = ((int)Math.random() * 100);
                Thread.sleep(num);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Utilities.printLogs(Utilities.MessageOwner.PAPER_TECHNICIAN, "Paper Technician Finished, packs of paper used "
                + count, Utilities.MessageType.INFO);
    }
}
