import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Cell {
    private String cellID;
    private final String WORKING = "WORKING",
            DONE = "DONE",
            READY = "READY",
            WAITING = "WAITING",
            STARVING = "STARVING,",
            BLOCKED = "BLOCKED";
    private final String TAKE = "TAKE",
            WORK = "WORK",
            GIVE = "GIVE",
            WAIT = "WAIT",
            PEEK = "PEEK";
    private String status;
    private List<Queue<Unit>> inBuffers;
    private int inBufferCapacity;
    private Cell nextCell;
    private List<Cell> previousCells;
    private Unit workingUnit;
    private int cycleTime;

    public Cell(String cellID, int numberOfInBuffers,
                int inBufferCapacity,
                int cycleTime) {
        this.cellID = cellID;
        createInBuffers(numberOfInBuffers);
        this.inBufferCapacity = inBufferCapacity;
        this.cycleTime = cycleTime;
    }

    public double getBufferLevel(){
        int minBufferLevel = 99999;
        for(Queue<Unit> buffer : inBuffers){
            if(buffer.size()<minBufferLevel)
                minBufferLevel = buffer.size();
        }
        return (minBufferLevel*1.0)/(inBufferCapacity*1.0);
    }

    public void receiveInstruction(String instruction) {
        if (instruction.equals(TAKE)) {
            //take unit from inBuffer
            takeUnitFromInBuffer();
        } else if (instruction.equals(WORK)) {
            //processUnit
            workOnUnit();
        } else if (instruction.equals(GIVE)) {
            //give unit to next Cell
            giveUnitToNextCell();
        } else if (instruction.equals(WAIT)) {
            //wait until instructed otherwise
            waitForNextInstruction();
        }
    }

    public String getStatus() {
        return status;
    }

    //return false if buffers empty
    private boolean peekInBuffers() {
        for (Queue<Unit> buffer : inBuffers) {
            if (buffer.size() == 0) {
                status = STARVING;
                return false;
            }
        }
        return true;
    }

    //return true if unit successfully taken from buffer
    private void takeUnitFromInBuffer() {
        if (!peekInBuffers()) {
            return;
        }
        List<Unit> bufferUnits = new ArrayList<>();
        for (Queue<Unit> buffer : inBuffers) {
            Unit bufferUnit = buffer.remove();
            if (bufferUnit != null) {
                bufferUnits.add(bufferUnit);
            }
        }
        Unit unit = bufferUnits.remove(0);
        workingUnit = unit.combineUnits(bufferUnits);
        if (workingUnit != null) {
            timeRemainingOnUnit = cycleTime;
            status = WORKING;
        }
    }

    private int timeRemainingOnUnit = -1;

    //return true if work is complete
    private boolean workOnUnit() {
        if (timeRemainingOnUnit == 0) {
            status = DONE;
            return true;
        }
        status = WORKING;
        timeRemainingOnUnit--;
        return false;
    }

    //return true if unit successfully given to next cell
    private boolean giveUnitToNextCell() {
        if (nextCell.receiveUnitIntoInBuffer(this, workingUnit)) {
            timeRemainingOnUnit = -1;
            status = READY;
            this.workingUnit = null;
            return true;
        }
        status = BLOCKED;
        return false;
    }

    //return true if unit received
    public boolean receiveUnitIntoInBuffer(Cell givingCell, Unit unit) {
        int index = 0;
        for (int i = 0; i < previousCells.size(); i++) {
            if (previousCells.get(i).equals(givingCell)) {
                index = i;
            }
        }
        if (inBuffers.get(index).size() < inBufferCapacity) {
            inBuffers.get(index).add(unit);
            return true;
        }
        return false;
    }

    private void waitForNextInstruction() {
        status = WAITING;
    }

    public void setNextCell(Cell nextCell) {
        this.nextCell = nextCell;
    }

    public void addPreviousCells(List<Cell> previousCells) {
        this.previousCells = previousCells;
    }

    private void createInBuffers(int numberOfInBuffers) {
        inBuffers = new ArrayList<>();
        for (int i = 0; i < numberOfInBuffers; i++) {
            inBuffers.add(new LinkedList<>());
        }
    }

}
