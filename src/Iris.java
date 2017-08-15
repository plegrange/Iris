public class Iris {
    private int unitsProduced, timeStepsPassed;
    private double productionRate;
    private final String TAKE = "TAKE",
            WORK = "WORK",
            GIVE = "GIVE",
            WAIT = "WAIT";

    private void controlCell(Cell cell) {
        switch (cell.getStatus()) {
            case "READY":
            case "STARVING":
                cell.receiveInstruction(TAKE);
                break;
            case "WORKING":
                cell.receiveInstruction(WORK);
                break;
            case "BLOCKED":
            case "DONE":
                cell.receiveInstruction(GIVE);
                break;
            case "WAITING":
                cell.receiveInstruction(WAIT);
                break;
            case "FAILURE":
                cell.repair();
                break;
            default:
                break;
        }
    }

    private DecisionNet decisionNet;

    public Iris(int numberOfHiddenLayers, int numberOfInputs, int numberOfNeuronsPerLayer, int numberOfOutputs) {
        decisionNet = new DecisionNet(numberOfHiddenLayers, numberOfOutputs, numberOfNeuronsPerLayer, numberOfInputs);
    }

    private double totalProductionRate() {
        return (unitsProduced * 1.0) / (timeStepsPassed * 1.0 / 60.0);
    }

    private void giveFeedback() {
        if (totalProductionRate() < productionRate)
            adaptWeights(false);
        else
            adaptWeights(true);
    }

    private void adaptWeights(boolean reward) {
        double[] averageResults = new double[2];
        averageResults[0] = outputSums[0] / numberOfDecisions;
        averageResults[1] = outputSums[1] / numberOfDecisions;
        if (reward)
            decisionNet.reward(averageResults);
        else decisionNet.penalize(averageResults);

    }

    private double[] outputSums;
    private int numberOfDecisions;

    private void startBatch() {
        numberOfDecisions = 0;
        outputSums = new double[2];
    }

    private void endBatch() {
        giveFeedback();
    }

    private void thinkForASecond(Cell cell) {
        double[] inputs = new double[5];
        inputs[0] = cell.getBufferLevel();
        inputs[1] = cell.getConsumptionRate();
        inputs[2] = cell.getProductionRate();
        inputs[3] = cell.getFailureRate();
        inputs[4] = cell.getMeanRepairTime();
        double[] outputs = decisionNet.getOutputs(inputs, 0);
        numberOfDecisions++;
        outputSums[0] += outputs[0];
        outputSums[1] += outputs[1];
        if (outputs[0] > outputs[1])
            cell.receiveInstruction(TAKE);
        else
            cell.receiveInstruction(WAIT);
    }
}
