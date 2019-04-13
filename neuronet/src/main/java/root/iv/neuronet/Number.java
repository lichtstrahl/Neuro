package root.iv.neuronet;

public class Number {
    private int[] pixs;
    private int value;

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
}
