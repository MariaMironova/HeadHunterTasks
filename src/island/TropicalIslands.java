package island;

import island.Island;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TropicalIslands {
    public static void main(String[] args) {
        getSolution(System.in, System.out);
    }

    public static void getSolution(final InputStream input, final PrintStream output) {
        List<Island> islands = new ArrayList<>();
        Scanner scanner = new Scanner(input);
        int numOfIslands = scanner.nextInt();

        for (int i = 0; i < numOfIslands; i++) {
            int height = scanner.nextInt();
            int width = scanner.nextInt();
            int[][] matrix = new int[height][width];

            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    matrix[j][k] = scanner.nextInt();
                }
            }

            islands.add(new Island(matrix));
        }

        islands.forEach(island -> island.flood());
        islands.forEach(island -> output.println(island.getResult()));
    }


}
