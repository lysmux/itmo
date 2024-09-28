public class Main {
    public static void main(String[] args) {
        short[] z1 = new short[23];
        double[][] z2 = new double[12][19];
        float[] x = new float[19];

        // fill z array
        for (short i = 23; i > 0; i--) {
            z1[23 - i] = i;
        }

        // fill x array
        for (int i = 0; i < 19; i++) {
            x[i] = (float) Math.random() * 18 - 10;
        }

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 19; j++) {
                float cur_x = x[j];
                if (z1[i] == 19) z2[i][j] = Math.pow((2 * Math.cos(Math.pow(Math.E, cur_x))), 3);
                else if (z1[i] == 1 || z1[i] == 5 || z1[i] == 9 || z1[i] == 11 || z1[i] == 17 || z1[i] == 23) {
                    z2[i][j] = Math.pow((), 2);
                } else {
                    z2[i][j] = Math.asin(
                            (1/3) * (1 / Math.pow(Math.E, Math.pow(Math.tan(
                                    Math.sin(Math.atan((cur_x - 1) / 18))
                            ), 2)))
                    );
                }
            }
        }


        printMatrix(z2);
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double element : row) {
                System.out.printf("%.2f\t", element);
            }
            System.out.println();
        }
    }
}