package Process;


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

    public ProcessMenager() {
        list = new Vector<PCB>();
        addDummy();
    }


    public static void addDummy(){
        PCB dummy = new PCB("dummy", DUMMY_PID, RUNNING, 0, "Source/Programs/dummy.txt");
        list.add(dummy);
    }

    public static int pidGen(){
        int i = 1;
        while (true){
            for(PCB pcb : list){
                if(pcb.getPid() != i)return i;
                i++;
            }
        }
    }

    public static PCB getDummy(){
        return list.elementAt(0);
    }

    public static void newProcess(String name, String filePath){
        PCB pcb = new PCB(name, pidGen(), NEW, 0,filePath);
        list.add(pcb);
        Scheduler.add_process(pcb);
    }

    public static String listOfProcess(){
        String tem = "";
        int i = 1;
        for(PCB pcb : list){
            tem =+ i + ". " + pcb.getName() + "\t PID: " + pcb.getPid() + "\t STATE: " + pcb.state + '\n';
        }
        return tem;
    }

}
