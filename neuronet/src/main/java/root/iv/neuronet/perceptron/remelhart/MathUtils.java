package root.iv.neuronet.perceptron.remelhart;

public class MathUtils {
    public static double sigmoid(double arg) {
        return (1 / (1 + Math.exp(arg)));
    }
}
