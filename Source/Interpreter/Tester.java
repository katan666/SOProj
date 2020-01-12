package Interpreter;


import FileSystem.DiskManager;

public class Tester {
    public static void main(String[] args) {
        DiskManager.fillDisk();
        DiskManager.setBitMap();
        while (true) {

            Interpreter.executeInstruction();
        }
    }
}
