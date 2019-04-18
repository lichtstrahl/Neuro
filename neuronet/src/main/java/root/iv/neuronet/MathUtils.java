package root.iv.neuronet;

public class MathUtils {
    public static int[][] getSingleMatrix(int size) {
        int[][] result = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = (i == j) ? 1 : 0;
            }
        }

        return result;
    }

    public static int[][] getSingleFullMatrix(int size) {
        int[][] result = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = 1;
            }
        }

        return result;
    }
}
