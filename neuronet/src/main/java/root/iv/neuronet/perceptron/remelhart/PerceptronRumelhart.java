package root.iv.neuronet.perceptron.remelhart;

import java.util.List;
import java.util.Locale;

import root.iv.neuronet.Number;

public class PerceptronRumelhart {
    private Layer layerR;
    private List<Number> originals;         // Образцы

    public PerceptronRumelhart(int sizeR, int sizeA) {
        layerR = new Layer(sizeR, sizeA);
    }

    public void setInput(int[] input) {
        layerR.setInput(input);
    }

    public int getOutput(StringBuilder logger) {
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
     * Обновление весов, собственно обучение
     * @param goodOutput Образец, к которому должна стремиться сеть
     * @return Суммарное изменение весов, которое было проведено на данном этапе
     */
    public double updateWeights(double[] goodOutput) {
        double globalDelta = 0.0;

        for (int r = 0; r < layerR.size(); r++) {
            double delta = goodOutput[r] - layerR.calculateOutput(r);
            layerR.updateWeights(r, delta);
            globalDelta += delta;
        }

        return globalDelta;
    }

    /**
     *
     * @param minDelta - Минимально изменение, которого необходимо достич
     * @param logger - логи
     * Идеальный образец имеет 1.0 на выходе только у одного элемента
     */
    public void train(double minDelta, StringBuilder logger) {
        double[] good = new double[originals.size()];
        logger.append("Step delta: ");
        double absoluteStepData = Double.MAX_VALUE;                 // Изменения на данном этапе

        for (long i = 0; absoluteStepData > minDelta; i++) {
            absoluteStepData = 0.0;
            for (int n = 0; n < originals.size(); n++) {
                setInput(originals.get(n).getPixs());
                for (int k = 0; k < good.length; k++)
                    good[k] = 0.0;
                good[n] = 1.0;
                absoluteStepData += Math.abs(updateWeights(good));
            }
            logger.append(String.format(Locale.ENGLISH, "%5.2f", absoluteStepData));
        }

        logger.append("\n");
    }

    public void setOriginalNumbers(List<Number> numbers) {
        this.originals = numbers;
    }
}
