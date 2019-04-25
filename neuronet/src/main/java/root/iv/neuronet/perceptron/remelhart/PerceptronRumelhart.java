package root.iv.neuronet.perceptron.remelhart;

import java.util.List;
import java.util.Locale;

import root.iv.neuronet.perceptron.rosenblat.Number;

public class PerceptronRumelhart {
    private SensorsLayer    layerS;
    private Layer           layerA;
    private Layer           layerR;

    private List<Number> originals;         // Образцы

    public PerceptronRumelhart(int sizeS, int sizeA, int sizeR) {
        layerS = new SensorsLayer();
        layerA = new Layer(sizeA, sizeS);
        layerR = new Layer(sizeR, sizeA);
    }

    public void setInput(int[] input) {
        layerS.setValues(input);
    }

    public int getOutput(StringBuilder logger) {
        layerR.setInput(layerS.getValues());

        // Подсчет на R-слое
        double[] out = new double[layerR.size()];

        int iMax = 0;
        logger.append("Out: ");
        for (int r = 0; r < layerR.size(); r++) {
            out[r] = layerR.calculateOutput(r);
            if (out[r] > out[iMax])  iMax = r;
            logger.append(String.format(Locale.ENGLISH, "%4.1f", out[r]));
        }
        logger.append("\n");

        return iMax;
    }

    /**
     *
     * @param minDelta - Минимально изменение, которого необходимо достич
     * @param logger - логи
     * Идеальный образец имеет 1.0 на выходе только у одного элемента
     */
    public void train(double minDelta, StringBuilder logger) {
        logger.append("Step delta: ");
        double absoluteStepData = Double.MAX_VALUE;                 // Изменения на данном этапе

        for (long i = 0; absoluteStepData > minDelta; i++) {
            absoluteStepData = 0.0;
            for (int n = 0; n < originals.size(); n++) {
                setInput(originals.get(n).getPixs());
                layerR.setInput(layerS.getValues());
                absoluteStepData += Math.abs(layerR.updateWeights(originals.get(n).goodOutput(originals.size())));
            }
            logger.append(String.format(Locale.ENGLISH, "%5.2f", absoluteStepData));
        }

        logger.append("\n");
    }

    public void setOriginalNumbers(List<Number> numbers) {
        this.originals = numbers;
    }
}
