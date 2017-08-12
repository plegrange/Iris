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
            default:
                break;
        }
    }

    private void thinkForASecond(Cell cell){

    }
}
