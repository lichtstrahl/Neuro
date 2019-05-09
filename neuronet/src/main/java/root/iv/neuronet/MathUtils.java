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

    /**
     * Чем больше значение, тем ближе к 1
     * Чем меньше значение, тем ближе к -1
     * @param arg
     * @return
     */
    public static double sigmoid(double arg) {
        return (1 / (1 + Math.exp(-arg)));
    }

    /**
     * Производная активационной функции
     */
    public static double sigmoid_(double arg) {
        return Math.exp(-arg) / Math.pow(Math.exp(-arg) + 1, 2);
    }
}
