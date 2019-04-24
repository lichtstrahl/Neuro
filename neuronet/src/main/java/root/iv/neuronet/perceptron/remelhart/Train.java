package root.iv.neuronet.perceptron.remelhart;

import java.util.List;

public class Train {
    private static final int NEURON_COUNT = 26;
    private PerceptronRumelhart perceptron;
    private List<TrainSet> trainSets;

    public Train() {
    }

    public void train(long count) {
        for (long i = 0; i < count; i++) {
            int index = ((int) (Math.random() * trainSets.size()));
            TrainSet set = trainSets.get(index);
            perceptron.setInput(set.getInput());
            perceptron.adjustWages(set.getGoodOutput());
        }
    }

    public void setInputs(int[] inputs) {
        perceptron.setInput(inputs);
    }

    public void addTrainingSet(TrainSet set) {
        trainSets.add(set);
    }

    public List<Double> getOutputs() {
        return perceptron.getOutputs();
    }


    public void buildPerceptron(int count) {
        this.perceptron = new PerceptronRumelhart();
        this.perceptron.addNeurons(count);
    }

    public void setTrainSets(List<TrainSet> trainSets) {
        this.trainSets = trainSets;
    }
}
