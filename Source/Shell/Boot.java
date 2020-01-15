package Shell;

import Process.ProcessMenager;
import Scheduler.Scheduler;
public class Boot
{

    public static void main(String[] args) {
        ProcessMenager.init();
        Scheduler.set_init();
        Shell roboczy;
        roboczy = new Shell();
        roboczy.loopStart();

    }
}
