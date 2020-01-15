package Process;

/*

TODO:
 1) Dodać wywołanie "init()" w odpowiednim miejscu.
 2) Uzupełnić dane przy printowaniu stanu nowoutworzonego procesu (np. o nazwę i PID).
 3)

 */

import RAM.MMU.MemoryManager;
import Scheduler.Scheduler;

import java.util.Vector;

public class ProcessManager {

        // Do generowania nowych PID
    private static Byte initialPID = 0;
    private static Vector<PCB> processList = new Vector<>();
    private static Vector<PCB> waitingList = new Vector<>();

        // Możliwe stany procesów:
    final static public String NEW = "NEW";
    final static public String READY = "READY";
    final static public String RUNNING = "RUNNING";
    final static public String WAITING = "WAITING";
    final static public String TERMINATED = "TERMINATED";

    public static void init() {
        PCB dummy = new PCB("dummy", (byte) 0, RUNNING, 0.0, "Source/Programs/dummy.txt");
        processList.add(dummy);
    }

    public static Byte generatePID() {
        return ++initialPID;
    }

    public static PCB getDummy() {
        return processList.firstElement();
    }

    public static void newProcess(String name, String filePath) {

        PCB pcb = new PCB(name, generatePID(), NEW, 0.0, filePath);
        System.out.println(pcb.regToString());
        processList.add(pcb);
        MemoryManager.allocateProcess(pcb);
        Scheduler.add_process(pcb);
    }

    public static String listOfProcesses() {
        String temp = "";
        for (Byte i = 0; i < processList.size(); ++i) {

            temp = temp + "\n\n" + (i+1) + ". " + processList.elementAt(i).getName() + "\tPID: " +
                    processList.elementAt(i).getPID() + "\tSTATE: " + processList.elementAt(i).getState() + "\n";
        }
        return temp;
    }

    private static Integer nameToPid(String name){
        for (PCB pcb : processList){
            if(name.equals(pcb.getName())) return pcb.getPID();
        }
        return -1;
    }

    public static void terminateProcess(String name){
        Scheduler.remove_process(nameToPid(name));
        MemoryManager.deallocateProcess(pidToPbc(nameToPid(name)));
    }

    private static PCB pidToPbc(Integer pid){
        for(PCB pcb : list) {
            if (pid == pcb.getPid()) return pcb;
        }
        return null;
    }

    public static Vector<PCB> getWaitingList() {
        return waitingList;
    }
}