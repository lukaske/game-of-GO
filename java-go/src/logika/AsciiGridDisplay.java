package logika;

import java.util.Random;
import java.util.Set;

public class AsciiGridDisplay {

    public AsciiGridDisplay() {
    }

    public static void printBoard(Point[][] array, Set<Point> group) {
        int rows = array.length;
        int cols = array[0].length;


        // Print horizontal lines
        numberedHorizontal(cols);
        printHorizontalLine(cols);

        // Print array elements
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (j == 0) {
                    System.out.print(Integer.toString(i) +  " | ");
                }

                String ANSI_RESET = "\u001B[0m";
                String ANSI_COLORS = "\u001B[38;5;";
                int colorCode = 190;

                if (group.contains(array[i][j])) {
                    System.out.printf(ANSI_COLORS + colorCode + "m" +  "%2d " + ANSI_RESET, array[i][j].typeToInt());
                }
                else {
                    System.out.printf("%2d ", array[i][j].typeToInt());
                }

                if (j == cols - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();

            // Print horizontal lines
            printHorizontalLine(cols);
        }
        System.out.println("   ");
    }

    private static void printHorizontalLine(int cols) {
        System.out.print("  ");
        for (int i = 0; i < cols; i++) {
            System.out.print("---");
        }
        System.out.println("+");
    }

    private static void numberedHorizontal(int cols) {
        System.out.print("   ");
        for (int i = 0; i < cols; i++) {
            System.out.print("  " + i);
        }
        System.out.println(" ");
    }

    public static void printPointSet(Set<Point> set){
        System.out.println("Printing set of points, len: " + set.size());
        for (Point p : set){
            System.out.println("- " + p.x() + " " + p.y());
        }
    }

}

