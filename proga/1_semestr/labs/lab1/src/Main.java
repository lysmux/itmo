import java.util.Random;

import static java.lang.Math.*;

public class Main {
    private final static byte Z1_SIZE = 12;
    private final static byte X_SIZE = 19;

    private final static byte Z_ROWS = 12;
    private final static byte Z_COLUMNS = 19;

    public static void main(String[] args) {
        Random rand = new Random();

        short[] z1 = new short[Z1_SIZE];
        float[] x = new float[X_SIZE];
        double[][] z = new double[Z_ROWS][Z_COLUMNS];

        // fill z1 array with  odd numbers from 1 to 23 in descending order
        for (int i = 0; i < Z1_SIZE; i++) {
            z1[i] = (short) (23 - i * 2);
        }

        // fill x array with random numbers from -8.0 to 10.0
        for (int i = 0; i < X_SIZE; i++) {
            x[i] = rand.nextFloat(18 + 1) - 8;
        }

        // fill matrix
        for (int i = 0; i < Z_ROWS; i++) {
            for (int j = 0; j < Z_COLUMNS; j++) {
                z[i][j] = calculateValue(x[j], z1[i]);
            }
        }

        printMatrix(z);
    }

    /**
     * Calculates the new value of a two-dimensional array
     *
     * @param cur_x parameter for calculation
     * @param cur_z parameter for calculation
     * @return calculated value
     */
    public static double calculateValue(float cur_x, short cur_z) {
        final short[] TO_CHECK = {1, 5, 9, 11, 17, 23};

        if (cur_z == 19) {
            return pow(2 * cos(pow(E, cur_x)), 3);
        } else if (checkExists(TO_CHECK, cur_z)) {
            return pow((pow(atan((cur_x - 1) / 18), 2 * asin((cur_x - 1) / 18)) + 1) / 12, 2);
        } else {
            return asin(1 / (3 * pow(E, pow(tan(sin(atan((cur_x - 1) / 18))), 2))));
        }
    }

    /**
     * Checks if the element is in the array
     *
     * @param array array to search for
     * @param num   desired element
     * @return true if exists, otherwise false
     */
    public static boolean checkExists(short[] array, short num) {
        for (short el : array) {
            if (el == num) return true;
        }
        return false;
    }


    /**
     * Prints the matrix
     *
     * @param matrix matrix to be shown
     */
    public static void printMatrix(double[][] matrix) {
        for (double[] rows : matrix) {
            for (double element : rows) {
                System.out.printf("%8.5f\t", element);
            }
            System.out.println();
        }
    }
}