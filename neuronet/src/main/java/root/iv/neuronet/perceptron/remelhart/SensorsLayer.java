package root.iv.neuronet.perceptron.remelhart;

public class SensorsLayer {
    private double[] values;

    public SensorsLayer() {
    }

    public void setValues(int[] values) {
        this.values = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i];
        }
    }

    public double[] getValues() {
        return values;
    }

    public int size() {
        return values.length;
    }
}
