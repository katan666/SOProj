package Shell;

import Scheduler.Scheduler;
public class Boot
{

    public static void main(String[] args) {
        Scheduler.set_init();
        Shell roboczy;
        roboczy = new Shell();
        roboczy.loopStart();

    }
}
