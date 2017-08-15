import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DecisionNet {
    private List<double[][]> weightMatrices;
    private int numberOfHiddenLayers;
    private int numberOfOutputs, numberOfInputs;
    private int numberOfNeuronsPerLayer;
    private int patternCounter;
    private double[] sumsOfInputs;

    public DecisionNet(int numberOfHiddenLayers, int numberOfOutputs, int numberOfNeuronsPerLayer, int numberOfInputs) {
        this.numberOfHiddenLayers = numberOfHiddenLayers;
        this.numberOfOutputs = numberOfOutputs;
        this.numberOfNeuronsPerLayer = numberOfNeuronsPerLayer;
        this.numberOfInputs = numberOfInputs;

        initialize();
    }

    public void initialize() {
        weightMatrices = new ArrayList<>();
        addLayer(numberOfNeuronsPerLayer, numberOfInputs, numberOfHiddenLayers, numberOfOutputs);
    }

    private void addLayer(int neurons, int inputs, int numberOfLayers, int numberOfOutputs) {
        if (numberOfLayers == -1) return;
        if (numberOfLayers == 0) {
            neurons = numberOfOutputs;
        }
        double[][] weightMatrix = new double[neurons][inputs];
        for (int neuron = 0; neuron < neurons; neuron++) {
            for (int input = 0; input < inputs; input++) {
                weightMatrix[neuron][input] = getRandomWeight(inputs);
            }
        }
        weightMatrices.add(weightMatrix);
        addLayer(neurons, neurons, numberOfLayers - 1, numberOfOutputs);
    }

    public double[] getOutputs(double[] inputs, int weightMatrixIndex) {
        for (int i = 0; i < inputs.length; i++) {
            sumsOfInputs[i] += inputs[i];
        }
        patternCounter++;
        return doLayer(inputs, weightMatrixIndex);
    }

    private double[] doLayer(double[] inputs, int weightMatrixIndex) {
        if (weightMatrixIndex == weightMatrices.size())
            return inputs;
        double[][] weights = weightMatrices.get(weightMatrixIndex);
        double[] outputs = new double[weights.length];
        for (int n = 0; n < weights.length; n++) {
            double output = 0.0;
            for (int i = 0; i < inputs.length; i++) {
                double temp = inputs[i] * weights[n][i];
                output += Math.max(0, temp);
            }
            outputs[n] = output;
        }
        return doLayer(outputs, weightMatrixIndex + 1);
    }

    private double error = 0;

    public void reward(double[] meanResults) {
        error = 1.0 - Math.max(meanResults[0], meanResults[1]);
    }

    public void penalize(double[] meanResults) {
        error = Math.max(meanResults[0], meanResults[1]);
    }

    private void adjustWeights() {

    }

    public void startBatch() {
        sumsOfInputs = new double[numberOfInputs];
        patternCounter = 0;
    }

    public void endBatch() {

    }

    private double getRandomWeight(int inputs) {
        return (-1.0 / Math.sqrt(inputs)) + new Random().nextDouble() * 2.0 / Math.sqrt(inputs);
    }

    public List<double[][]> getWeightMatrices() {
        return weightMatrices;
    }

    public void setWeightMatrices(List<double[][]> weightMatrices) {
        this.weightMatrices = weightMatrices;
    }
}
