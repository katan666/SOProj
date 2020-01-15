package Interpreter;


import FileSystem.DiskManager;
import RAM.MMU.MemoryManager;
import Scheduler.Scheduler;
import Process.ProcessMenager;

public class Tester {
    public static boolean isTest = true;
    public static void main(String[] args) {
        DiskManager.fillDisk();
        DiskManager.setBitMap();
        MemoryManager.init();
        ProcessMenager.init();
        Scheduler.set_init();

        while (true) {

            Interpreter.executeInstruction();
        }
    }
}
