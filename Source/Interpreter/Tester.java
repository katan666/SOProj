package Interpreter;


import FileSystem.DiskManager;
import FileSystem.FileManager;
import RAM.MMU.MemoryManager;
import Scheduler.Scheduler;
import Process.ProcessMenager;

import java.util.Scanner;

public class Tester {
    public static boolean isTest = true;
    public static void main(String[] args) {
        DiskManager.fillDisk();
        DiskManager.setBitMap();
        MemoryManager.init();
        ProcessMenager.init();
        Scheduler.set_init();
        Scanner scanner = new Scanner(System.in);
        String string;

        while (true) {
            string = scanner.nextLine();
            String[] tab = string.split(" ");
            for(String s : tab)System.out.println(s);
            switch (tab[0]){
                case "addProcess":
                    System.out.println(args[1]);
                    ProcessMenager.newProcess(args[1], ("Source/Programs/" + args[2]));
                    break;
                case "delProcess":
                    ProcessMenager.terminateProcess(args[1]);
                    break;
                case "step":
                    Interpreter.executeInstruction();
                    break;
                case "showRAM":
                    MemoryManager.printMemoryASCII();
                    break;
                case "showDisk":
                    FileManager.showDisk();
                    break;


            }

        }
    }
}
