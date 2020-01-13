package Interpreter;


import FileSystem.DiskManager;
import RAM.MMU.MemoryManager;

public class Tester {
    public static void main(String[] args) {
        DiskManager.fillDisk();
        DiskManager.setBitMap();
        MemoryManager.init();
        while (true) {

            Interpreter.executeInstruction();
        }
    }
}
