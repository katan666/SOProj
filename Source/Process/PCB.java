package Process;

import FileSystem.Files;
import Programs.FileLoader;

import java.util.Vector;
import java.util.Stack;

public class PCB {

    public class OpenFile {

        public Files file;
        public int readPointer;
        public int writePointer;

        public OpenFile(){
            this.readPointer = 0;
            this.writePointer = 0;
            openFiles = new Vector();
        }
    }

    public Vector<OpenFile> openFiles;

    private int counter, rA, rB, rC, rD;
    private String name;
    private int pid;
    public String state;
    public double expected_time;//tau
    public Stack <Byte> pageTable;
    public Vector <Short> code;
    public PCB parent;
    public Vector<PCB> children;

    public PCB(String name, int pid, String state, double expected_time,  String filePath) {
        this.name = name;
        this.pid = pid;
        this.state = state;
        this.expected_time = expected_time;
        counter = rA = rB = rC = rD = 0;
        code = FileLoader.readAllBytesFromFileToShortVec(filePath);
        pageTable = new Stack<>();
        System.out.println(filePath);
    }


    public String toStringReg(){
        return "AX: "+ rA + "\t" + "BX: "+ rB + "\t" +
                "CX: "+ rC + "\t" + "DX: "+ rD + "\t" +
                "DX: "+ rD;
    }



    //Gettery i settery jo
    public String getName() {
        return name;
    }

    public int getPid() {
        return pid;
    }
    public int getPID() {
        return pid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getExpected_time() {
        return expected_time;
    }

    public void setExpected_time(double expected_time) {
        this.expected_time = expected_time;
    }

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
