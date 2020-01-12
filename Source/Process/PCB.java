package Process;

import java.util.Vector;
import java.util.Stack;

public class PCB {

    final private int PID;
    public final String name;
    private ProcessState state;
    //program counter
    private byte PC;
    //nwm
    int expectedTime;
    //zlicza rozkazy
    private int realTime;
    //tablica stron
    public Stack <Byte> pageTable;
    //dlugosc kodu?
    private final int codeLength;
    /**
     * Unchangeable base priority, set in constructor,
     * Value 0 - idle process,
     * Values 1-15 - dynamic priority,
     * Values 16-17 - real-time priority
     */
    private final int basePriority;
    /**
     * Dynamically changed priority,
     * Values 1-15 - dynamic priority
     * Values 16-17 - real-time priority
     */
    private int dynamicPriority;


    PCB(int PID, String name, int priority, Memory ram, byte PC, int codeLength) {
        this.PID = PID;
        this.name = name;

        if (priority < 0){
            Utils.log("Priority is too low, cannot create process", true);
        }
        if (priority > 17) {
            Utils.log("Priority is too high, changed to priority max size - 17", true);
            priority = 17;
        }
        this.basePriority = priority;
        this.dynamicPriority = priority;

        this.expectedTime=0;
        this.codeLength = codeLength;
        this.PC = PC;

        this.state = ProcessState.READY;

        Utils.log("Created process " + this.name + " with pid " + this.PID
                + " and with priority " + this.basePriority);

    }

}
