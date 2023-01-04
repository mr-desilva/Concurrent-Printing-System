package Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
    public enum ProcessLogger {
        PRINTER,
        STUDENT,
        TONER_TECHNICIAN,
        PAPER_TECHNICIAN,
        PRINTING_SYSTEM,
        INFO,
        WARN,
        ERROR;
    }

    public static synchronized void printLogs(ProcessLogger processName, String message, ProcessLogger infoType) {
        String logLine = "";

        Date date = new Date(System.currentTimeMillis());
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String currentTimeDate = formatter.format(date);

//        logLine = "[" + currentTimeDate + "]  " + "[" + processName.toString().toUpperCase().replace("_", " ")
//                + "]  [" + infoType.toString().toUpperCase() + "]  " + message + ".";

        //[2023-01-04 22:20:19.390]  [PRINTER]  [INFO]  Out of paper. Current Paper Level is.

        switch (processName) {
//            case PRINTER:
//                logLine = "[" + currentTimeDate + "]  " + "[" + processName.toString().toUpperCase().replace("_", " ")
//                        + "]  [" + infoType.toString().toUpperCase() + "]  " + ConsoleColor.RED + message + ".";
//                System.out.println(logLine);
            case STUDENT:
                logLine = "[" + currentTimeDate + "]  " + "[" + processName.toString().toUpperCase().replace("_", " ")
                        + "]  [" + infoType.toString().toUpperCase() + "]  " + ConsoleColor.GREEN + message + ".";
                System.out.println(logLine);
//            case TONER_TECHNICIAN:
//                logLine = "[" + currentTimeDate + "]  " + "[" + processName.toString().toUpperCase().replace("_", " ")
//                        + "]  [" + infoType.toString().toUpperCase() + "]  " + ConsoleColor.PURPLE + message + ".";
//                System.out.println(logLine);
//            case PAPER_TECHNICIAN:
//                logLine = "[" + currentTimeDate + "]  " + "[" + processName.toString().toUpperCase().replace("_", " ")
//                        + "]  [" + infoType.toString().toUpperCase() + "]  " + ConsoleColor.YELLOW + message + ".";
//                System.out.println(logLine);
            default:
                logLine = "[" + currentTimeDate + "]  " + "[" + processName.toString().toUpperCase().replace("_", " ")
                        + "]  [" + infoType.toString().toUpperCase() + "]  " +  message + ".";
                System.out.println(logLine);
        }


        //System.out.println(logLine);

        //writes log into file.
        try {
            FileWriter myWriter = new FileWriter("OutputFile.txt", true);
            myWriter.write(logLine + "\n");
            myWriter.close();

        } catch (IOException e) {

            e.printStackTrace();
        }


    }
}
