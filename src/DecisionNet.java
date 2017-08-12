import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DecisionNet {
    private List<double[][]> weightMatrices;
    private int numberOfHiddenLayers;
    private int numberOfOutputs, numberOfInputs;
    private int numberOfNeuronsPerLayer;

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

    private double[] getOutputs(double[] inputs){

    }

    private double getRandomWeight(int inputs) {
        return (-1.0 / Math.sqrt(inputs)) + new Random().nextDouble() * 2.0 / Math.sqrt(inputs);
    }
}
