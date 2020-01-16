package Shell;

import FileSystem.DiskManager;
import FileSystem.FileManager;
import Process.ProcessMenager;
import Scheduler.Scheduler;
import RAM.MMU.MemoryManager;

public class Boot
{

    public static void main(String[] args) {
        FileManager.init();
        MemoryManager.init();
        ProcessMenager.init();
        Scheduler.set_init();
        Shell roboczy;
        roboczy = new Shell();
        roboczy.loopStart();
    }
}
