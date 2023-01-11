import Utils.Utilities;

public class Student implements Runnable{
    private String name;
    private ThreadGroup group;
    private Printer printer;

    public Student(String name, ThreadGroup group, Printer printer) {
        super();
        this.name = name;
        this.group = group;
        this.printer = printer;
    }

    @Override
    public void run() {
        Document[] documentArr = new Document[5];
        documentArr[0] = new Document("Doc001","6SENG006C_CWK", 10);
        documentArr[1] = new Document("Doc002","6COSC023C_CWK01_PP", 10);
        documentArr[2] = new Document("Doc003","6COSC023C_CWK02_PSPD", 15);
        documentArr[3] = new Document("Doc004","6SENG006_CWK", 10);
        documentArr[4] = new Document("Doc005","Final_Project_Draft", 15);

        for (Document doc : documentArr) {
            printer.printDocument(doc);
            try {
                int num = ((int)Math.random() * 100);
                Thread.sleep(num);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Utilities.printLogs(Utilities.MessageOwner.STUDENT, this.name + " finished printing all documents", Utilities.MessageType.INFO);
    }
}
