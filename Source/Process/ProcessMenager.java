package Process;


import RAM.MMU.MemoryManager;
import Scheduler.Scheduler;

import java.util.Random;
import java.util.Vector;
import static Process.ProcessState.NEW;
import static Process.ProcessState.READY;
import static Process.ProcessState.RUNNING;
import static Process.ProcessState.WAITING;
import static Process.ProcessState.TERMINATED;

public class ProcessMenager {
    public static final int DUMMY_PID = 0;
    public static Vector<PCB> list;
    public static int pidG = 1;
    private static Vector<PCB> waitingList;
    public ProcessMenager() {
        list = new Vector<PCB>();
        addDummy();
    }
    public static void init(){
        list = new Vector<PCB>();
        addDummy();
    }


    public static void addDummy(){
        PCB dummy = new PCB("dummy", DUMMY_PID, RUNNING, 0, "Source/Programs/dummy.txt");
        MemoryManager.allocateProcess(dummy);
        list.add(dummy);
    }

    public static int pidGen(){
        return pidG++;
    }

    public static PCB getDummy(){

        return list.elementAt(0);
    }

    public static int newProcess(String name, String filePath){

        //0 - okej
        //1 - nazwa zajeta
        for(PCB pcb : list){
            if(pcb.getName().equals(name)) return 1;
        }
        PCB pcb = new PCB(name, pidGen(), NEW, 0,filePath);
        list.add(pcb);
        MemoryManager.allocateProcess(pcb);
        Scheduler.add_process(pcb);
        return 0;

    }

    public static String listOfProcess(){
        String tem = "";
        for(int i = 0; i < list.size(); i++){
            tem = tem + (i+1) + ". " + list.elementAt(i).getName() + "\t PID: " + list.elementAt(i).getPid() + "\t STATE: " + list.elementAt(i).state + '\n';

        }
        return tem;
    }

    private static int nameToPid(String name){
        for (PCB pcb : list){
            if(name.equals(pcb.getName())) return pcb.getPid();
        }
        return -1;
    }

    public static int terminateProcess(String name){
        //0 - wsztsto spox
        //1 - nie ma procesu o takiej nazwie
        //2 - nie mozna usunac dummy
        if(name.equals(getDummy().getName())) return 2;
        for(PCB pcb : list){
            if(pcb.getName().equals(name)) {
                Scheduler.remove_process(nameToPid(name));
                if(pcb.getPid() == Scheduler.get_running().getPid()){
                    Scheduler.remove_running();
                    MemoryManager.deallocateProcess(pidToPbc(nameToPid(name)));
                    return 0;
                }

                Scheduler.remove_process(nameToPid(name));
                MemoryManager.deallocateProcess(pidToPbc(nameToPid(name)));
                return 0;
            }

        }
        return 1;
    }

    private static PCB pidToPbc(int pid){
        for(PCB pcb : list){
            if (pid == pcb.getPid()) return pcb;
        }
        return null;
    }

    public static Vector<PCB> getWaitingList() {
        return waitingList;
    }
}
