package MMU;

public class FrameTableEntry {
    private boolean taken;
    private Short PID;
    public boolean isTaken() {
        return taken;
    }
    public void setTaken(boolean status) {
        this.taken = status;
    }

    public Short getPID() {
        return PID;
    }
    public void setPID(Short pid) {
        this.PID = pid;
    }
    public FrameTableEntry() {
        this.taken = false;
        this.PID = (-1);
    }
}
