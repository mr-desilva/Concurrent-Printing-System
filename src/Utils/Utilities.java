package Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {

    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";

    public enum MessageOwner {
        PRINTER,
        STUDENT,
        TONER_TECHNICIAN,
        PAPER_TECHNICIAN,
        PRINTING_SYSTEM
    }

    public enum MessageType {
        INFO,
        ERROR,
        WARN
    }

    public static synchronized void printLogs(MessageOwner messageOwner, String message, MessageType infoType) {

    Date date = new Date(System.currentTimeMillis());
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    String currentTimeDate = formatter.format(date);

    String infoLog = "[" + messageOwner.toString().toUpperCase().replace("_", " ") + "]  [" + infoType.toString().toUpperCase() + "] ";
    String infoMessage = "[" + currentTimeDate + "]  " + message + ".";


    switch (infoType) {
        case WARN:
            coloredOutput(YELLOW, messageOwner, infoLog, infoMessage);
            break;
        case INFO:
            coloredOutput(RESET, messageOwner, infoLog, infoMessage);
            break;
        case ERROR:
            coloredOutput(RED, messageOwner, infoLog, infoMessage);
            break;

        default:
            System.out.println(message);
            break;
    }

    //writes log into file.
    try {
        FileWriter myWriter = new FileWriter("OutputFile.txt", true);
        myWriter.write(infoLog + infoMessage + "\n");
        myWriter.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
    }

        private static void coloredOutput(String consoleColors, MessageOwner messageOwner, String infoLog, String infoMessage) {
            switch (messageOwner) {
                case STUDENT:
                    System.out.println(consoleColors + infoLog + GREEN + infoMessage + RESET);
                    break;
                case TONER_TECHNICIAN:
                    System.out.println(consoleColors + infoLog + PURPLE + infoMessage + RESET);
                    break;
                case PAPER_TECHNICIAN:
                    System.out.println(consoleColors + infoLog + CYAN + infoMessage + RESET);
                    break;
                case PRINTER:
                    System.out.println(consoleColors + infoLog + infoMessage + RESET);
                    break;
                default:
                    System.out.println(infoLog + infoMessage);
            }
     }

    public static void clearFile(){
        try {
            FileWriter fileWriter = new FileWriter("OutputFile.txt", false);
            PrintWriter printWriter = new PrintWriter(fileWriter, false);
            printWriter.flush();
            printWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
