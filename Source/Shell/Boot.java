package Shell;

import Process.ProcessMenager;
import Scheduler.Scheduler;
import RAM.MMU.MemoryManager;

public class Boot
{

    public static void main(String[] args) {
        MemoryManager.init();
        ProcessMenager.init();
        Scheduler.set_init();
        Shell roboczy;
        roboczy = new Shell();
        roboczy.loopStart();

    }
}
