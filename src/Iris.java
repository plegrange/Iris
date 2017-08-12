public class Iris {
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

    private double totalProductionRate(){

    }
    private void giveFeedback(){

    }

    private void adaptWeights(){

    }

    private void thinkForASecond(Cell cell) {
        double[] inputs = new double[5];
        inputs[0] = cell.getBufferLevel();
        inputs[1] = cell.getConsumptionRate();
        inputs[2] = cell.getProductionRate();
        inputs[3] = cell.getFailureRate();
        inputs[4] = cell.getMeanRepairTime();
        double[] outputs = decisionNet.getOutputs(inputs, 0);

        if (outputs[0] > outputs[1])
            cell.receiveInstruction(TAKE);
        else
            cell.receiveInstruction(WAIT);
    }
}
