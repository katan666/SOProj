package RAM.MMU;

import java.util.Vector;
import java.util.Stack;

public class PCB {
    private Short PID;
    private String name;
    public Stack <Byte> pageTable;
    public Vector <Short> code;
    public String getName() {
        return name;
    }
    public Short getPID() {
        return PID;
    }
    public void setPID (Short PID) {
        this.PID = PID;
    }
}