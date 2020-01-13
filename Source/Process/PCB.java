package Process;

import RAM.MMU.MemoryManager;

import java.util.Vector;
import java.util.Stack;

public class PCB {
    //Identyfikator procesu
    final private int PID;
    //Nazwa procesu
    public final String name;
    //Stan procesu
    private ProcessState state;
    //Rejestry i licznik
    private int counter, rA, rB, rC, rD;

    private byte PC;
    //nwm
    int expectedTime;
    //zlicza rozkazy
    private int realTime;
    //tablica stron
    public Stack <Byte> pageTable;
    //dlugosc kodu
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

    //??? ~MB
    //private CPUState cpuState;



    MemoryManager ram;

                                            //~MB
    PCB(int PID, String name, int priority, /*Memory ram,*/ byte PC, int codeLength) {
        this.PID = PID;
        this.name = name;

        if (priority < 0){
            //??? ~MB
            //Utils.log("Priority is too low, cannot create process", true);
        }
        if (priority > 17) {
            //??? MB
            //Utils.log("Priority is too high, changed to priority max size - 17", true);
            priority = 17;
        }
        this.basePriority = priority;
        this.dynamicPriority = priority;

        this.expectedTime=0;
        this.codeLength = codeLength;
        this.PC = PC;

        this.state = ProcessState.READY;

        //??? ~MB
        //Utils.log("Created process " + this.name + " with pid " + this.PID
        //        + " and with priority " + this.basePriority);

        //~MB
        counter = rA = rB = rC = rD = 0;

    }

    public int getRealTime() { return  realTime; }

    public void setRealTime(int realTime)
    {
        this.realTime=realTime;
    }


    public int getPID() {
        return PID;
    }

    public String getName() {
        return name;
    }

    public byte getPC() {
        return PC;
    }

    public void setPC(byte PC) {
        this.PC = PC;
    }

    public ProcessState getProcessState(){
        return state;
    }

    public void setState(ProcessState state) {

        if (this.state != state){//in case of some error

            //??? ~MB
            //Utils.log("Changed state of proces " + this.getSignature() + " from " + this.state
            //        + " to " + state);

            switch (state){
                //TODO: find some way to use it or delete
                case READY:{
                    break;
                }
                case RUNNING:{
                    break;
                }
            }

            this.state = state;
        } //else do nothing

    }

    //Priority-------------------------------------------------------

    public int getBasePriority() {
        return basePriority;
    }

    public int getDynamicPriority() {
        return dynamicPriority;
    }

    /**
     * Adds given parameter to dynamic priority, if sum is bigger than 15,
     * sum is set with value 15 and gives error log
     *
     * @param newPriority value to add for dynamic priority
     */
    public void setDynamicPriority(final int newPriority)
    {
        dynamicPriority = (newPriority < 15) ? newPriority : 15;
        if(dynamicPriority < basePriority) {dynamicPriority=basePriority;}
    }
    /**
     * Sets dynamic priority with it's base value
     */
    public void setBasePriority(){
        if(dynamicPriority != basePriority) {
            this.dynamicPriority = this.basePriority;
            //~MB
            //Utils.log("Changed priority of " + this.getSignature() + " from " + this.dynamicPriority +
            //        " to base value - " + this.basePriority);
        }
    }



    //~MB
    //public boolean execute() {
    //    Utils.log(toString());
    //    Assembler.execute(this);
    //
    // return PC < codeLength;
    //}

    public byte getByteAt(final byte address) {

        return (byte) ram.readInRAM(address);
        //return this.code[address];
    }
    public void writeByteAt(final byte address, final byte value) {

        ram.writeInRAM(address, value);
        //this.code[address] = value;
    }

    //~MB
    //public CPUState getCpuState() {
    //    return cpuState;
    //}
    //
    //public void setCPUState(CPUState cpuState) {
    //    this.cpuState.set(cpuState);
    //}

    /*~MB
    @Override
    public String toString() {
        return "PCB{" +
                "PID=" + PID +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", PC=" + PC +
                ", basePriority=" + basePriority +
                ", dynamicPriority=" + dynamicPriority +
                ", readyTime=" + readyTime +
                ", executedOrders=" + executedOrders +
                '}';
    }*/

    public String getSignature() {
        return name + " id: " + PID;
    }

    /*
    @Override
    public int compareTo(PCB o) {
        return 0;
    }*/

    //~MB
    //Gettery i settery
    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getrA() {
        return rA;
    }

    public void setrA(int rA) {
        this.rA = rA;
    }

    public int getrB() {
        return rB;
    }

    public void setrB(int rB) {
        this.rB = rB;
    }

    public int getrC() {
        return rC;
    }

    public void setrC(int rC) {
        this.rC = rC;
    }

    public int getrD() {
        return rD;
    }

    public void setrD(int rD) {
        this.rD = rD;
    }
}
