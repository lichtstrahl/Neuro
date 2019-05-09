package root.iv.neuronet.perceptron;

public class ArrayUtils {
    public static double sum(double[] array) {
        double sum = 0;

        for (double d : array)
            sum += d;

        return sum;
    }

    public static double sumABS(double[] array) {
        double sum = 0;

        for (double d : array)
            sum += Math.abs(d);

        return sum;
    }
}
