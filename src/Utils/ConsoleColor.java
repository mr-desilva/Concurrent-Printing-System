package Utils;

public enum ConsoleColor {
    RESET("\033[0m"), // Reset color of text
    RED("\033[0;31m"), GREEN("\033[0;32m"), PURPLE("\033[0;35m"), YELLOW("\033[0;33m");

    String consoleColor;

    ConsoleColor(String consoleColor) {
        this.consoleColor = consoleColor;
    }

    @Override
    public String toString() {
        return consoleColor;
    }
}
