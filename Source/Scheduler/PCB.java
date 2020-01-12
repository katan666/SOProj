package Scheduler
public class PCB

{
    private String name;
    private String pid;
    public String state;
    public double expected_time;
    public int real_time;

    public PCB(String n, String p,String s, double ex, int re)
    {
        name=n;
        pid=p;
        state=s;
        expected_time=ex;
        real_time=re;
    }

    public PCB()
    {
        name="none";
        pid="none";
        state="New";
        expected_time=0;
        real_time=0;
    }



    public String getName()
    {
        return name;
    }

    public String getPid()
    {
        return pid;
    }
}

