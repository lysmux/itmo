import static java.lang.Math.*;

public class Main {
    private final static byte Z1_SIZE = 12;
    private final static byte X_SIZE = 19;
    private final static byte Z_ROWS = 12;
    private final static byte Z_COLUMNS = 19;

    private final static short[] TO_CHECK = {1, 5, 9, 11, 17, 23};

    public static void main(String[] args) {
        short[] z1 = new short[Z1_SIZE];
        double[][] z = new double[Z_ROWS][Z_COLUMNS];
        float[] x = new float[X_SIZE];

        // fill z1 array
        for (int i = 0; i < Z1_SIZE; i++) {
            z1[i] = (short) (23 - i * 2);
        }

        // fill x array
        for (int i = 0; i < X_SIZE; i++) {
            x[i] = (float) random() * 18 - 10;
        }

        for (int i = 0; i < Z_ROWS; i++) {
            for (int j = 0; j < Z_COLUMNS; j++) {
                z[i][j] = calculateValue(x[j], z1[i]);
            }
        }

        printMatrix(z);
    }

    public static double calculateValue(float cur_x, short cur_z) {
        if (cur_z == 19) {
            return pow(2 * cos(pow(E, cur_x)), 3);
        } else if (checkExists(TO_CHECK, cur_z)) {
            return pow((pow(atan((cur_x - 1) / 18), 2 * asin((cur_x - 1)/ 18)) + 1) / 12, 2);
        } else {
            return asin(1 / (3 * pow(E, pow(tan(sin(atan((cur_x - 1) / 18))), 2))));
        }
    }

    public static boolean checkExists(short[] array, short num) {
        for (short el : array) {
            if (el == num) return true;
        }
        return false;
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] rows : matrix) {
            for (double element : rows) {
                System.out.printf("%8.5f\t", element);
            }
            System.out.println();
        }
    }
}