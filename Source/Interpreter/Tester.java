package Interpreter;


import FileSystem.DiskManager;
import RAM.MMU.MemoryManager;
import Scheduler.Scheduler;

public class Tester {
    public static void main(String[] args) {
        DiskManager.fillDisk();
        DiskManager.setBitMap();
        MemoryManager.init();
        Scheduler.set_init();
        while (true) {

            Interpreter.executeInstruction();
        }
    }
}
