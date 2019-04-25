package root.iv.neuronet.perceptron.rosenblat;

public class Number {
    protected int[] pixs;
    protected int value;

    public Number(int[] pixs, int value) {
        this.pixs = pixs;
        this.value = value;
    }

    public int getPixel(int i) {
        return pixs[i];
    }

    public int getValue() {
        return value;
    }

    public int getSize() {
        return pixs.length;
    }

    public int[] getPixs() {
        return pixs;
    }

    public double[] goodOutput(int allCount) {
        double[] good = new double[allCount];

        for (int i = 0; i < good.length; i++) {
            good[i] = (i == value) ? 1.0 : 0.0;
        }

        return good;
    }
}
