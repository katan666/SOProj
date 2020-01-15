package Shell;

import ProcessManager;
import Scheduler.Scheduler;
public class Boot
{

    public static void main(String[] args) {
        ProcessManager.init();
        Scheduler.set_init();
        Shell roboczy;
        roboczy = new Shell();
        roboczy.loopStart();

    }
}
