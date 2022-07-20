package utilities;

public class Printer {
    private static Printer printer;

    private Printer() {
    }

    public static Printer getPrinter() {
        if (printer == null) {
            printer = new Printer();
        }
        return printer;
    }

    public void println() {
        System.out.println();
    }

    public void println(String text) {
        System.out.println(text);
    }

    public void println(int text) {
        System.out.println(text);
    }

    public void println(boolean text) {
        System.out.println(text);
    }

    public void println(double text) {
        System.out.println(text);
    }

    public void print(String text) {
        System.out.print(text);
    }

    public void print(int text) {
        System.out.print(text);
    }

    public void print(boolean text) {
        System.out.print(text);
    }

    public void print(double text) {
        System.out.print(text);
    }

    public void printlnError(String text) {  // Error for the user, NOT the programmer
        System.out.println(PrintableCharacters.ANSI_RED_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printlnError(int text) {  // Error for the user, NOT the programmer
        System.out.println(PrintableCharacters.ANSI_RED_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printlnRed(String text) {
        System.out.println(PrintableCharacters.ANSI_RED_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printlnRed(int text) {
        System.out.println(PrintableCharacters.ANSI_RED_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printlnRed(double text) {
        System.out.println(PrintableCharacters.ANSI_RED_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printRed(String text) {
        System.out.print(PrintableCharacters.ANSI_RED_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printRed(int text) {
        System.out.print(PrintableCharacters.ANSI_RED_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printRed(double text) {
        System.out.print(PrintableCharacters.ANSI_RED_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printlnBlue(String text) {
        System.out.println(PrintableCharacters.ANSI_BLUE_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printlnBlue(int text) {
        System.out.println(PrintableCharacters.ANSI_BLUE_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printlnBlue(double text) {
        System.out.println(PrintableCharacters.ANSI_BLUE_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printBlue(String text) {
        System.out.print(PrintableCharacters.ANSI_BLUE_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printBlue(int text) {
        System.out.print(PrintableCharacters.ANSI_BLUE_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printBlue(double text) {
        System.out.print(PrintableCharacters.ANSI_BLUE_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printlnGreen(String text) {
        System.out.println(PrintableCharacters.ANSI_GREEN_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printlnGreen(int text) {
        System.out.println(PrintableCharacters.ANSI_GREEN_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printGreen(String text) {
        System.out.print(PrintableCharacters.ANSI_GREEN_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printGreen(int text) {
        System.out.print(PrintableCharacters.ANSI_GREEN_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printlnPurple(String text) {
        System.out.println(PrintableCharacters.ANSI_PURPLE_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printlnPurple(int text) {
        System.out.println(PrintableCharacters.ANSI_PURPLE_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printPurple(String text) {
        System.out.print(PrintableCharacters.ANSI_PURPLE_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printPurple(int text) {
        System.out.print(PrintableCharacters.ANSI_PURPLE_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printlnYellow(String text) {
        System.out.println(PrintableCharacters.ANSI_YELLOW_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printlnYellow(int text) {
        System.out.println(PrintableCharacters.ANSI_YELLOW_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printYellow(String text) {
        System.out.print(PrintableCharacters.ANSI_YELLOW_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }

    public void printYellow(int text) {
        System.out.print(PrintableCharacters.ANSI_YELLOW_BACKGROUND + text + PrintableCharacters.ANSI_RESET);
    }
}
