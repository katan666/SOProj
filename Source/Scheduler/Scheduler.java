package Scheduler;

import java.util.Vector;

import Interpreter.Interpreter;
import Process.PCB;

import Process.ProcessMenager;
import static Process.ProcessState.READY;
import static Process.ProcessState.RUNNING;
import static Process.ProcessState.WAITING;
import static Process.ProcessState.TERMINATED;

public class Scheduler
{
    private static final double alfa = 0.5; //stala z zakresu [0-1]
    private static double expected_time = 5; //przewidywany czas pracy
    static Vector<PCB> readyQueue = new Vector<PCB>();//wektor procesow gotowych do przydzielenia procesora
    private static PCB running; // uruchomiony proces
    public static void set_init()//metoda ustawiająca init jako uruchomiony
    {
        running= ProcessMenager.getDummy();
    }

    private static void calculate_srt()//metoda obliczajaca srednia wykladnicza ostatnich procesow
    {
        running.expected_time = alfa * running.getCounter() + ((1 - alfa) * running.expected_time);
        //oblicza kazdemu procesowi w kolejce gotowych przewidywany czas
    }

    public static void add_running(PCB x)
    {
        readyQueue.add(x);
    }

    public static int remove_process(int pid)//funkcja usuwajaca proces z kolejki gotowych procesow
    {
        int code=0;
        for(int i=0;i<readyQueue.size();i++)
        {
            if(readyQueue.get(i).getPid()==(pid))
            {
                if(readyQueue.elementAt(i).getPid()!=get_running().getPid())readyQueue.elementAt(i).state = TERMINATED;
                readyQueue.remove(i);
                code=1;
            }
        }
        return code;
    }

    public static int remove_running()//funkcja usuwajaca proces running
    {
        int code=1;
        if (running==ProcessMenager.getDummy())
        {
            code=0;
        }

        int min=999999; //zmienna pomocnicza przy wybieraniu najmnijszej wartosci czasu
        int index=0;

        if(readyQueue.size()==0)
        {
            running.state=TERMINATED;//zmienia stan na terminated
            running=ProcessMenager.getDummy();//ustawia dummy jako running
            running.state=RUNNING;//zmienia stan dummy na running
        }
        else {
            for (int i = 0; i < readyQueue.size(); i++) {
                if (readyQueue.get(i).expected_time < min) {
                    min = (int) readyQueue.get(i).expected_time;
                    index = i;
                }
            }
            running.state = TERMINATED;
            running = readyQueue.get(index);
            remove_process(readyQueue.get(index).getPid());
        }
        return code;
    }


    public static Vector<PCB> print_ready_queue() //wypisywanie calej tablicy procesow gotowych
    {
        return readyQueue;
    }

    public static PCB print_running_process()
    {
        return running;
        //   System.out.println("SCHEDULER -> " + "|ID:" + running.getPid() + "| |Name:" + running.getName() + "| |Tau:" + running.expected_time + "| |Tn:" + running.getCounter() + "| |State:" + running.state + "|"); //wypisuje uruchomiony proces
    }

    public static void add_process(PCB process)//<--------------------SZYMON zmiana dodanie do kolejki procesow gotowych
    {
        if(process.expected_time==0)
        {
            process.expected_time=expected_time;
        }
        if(readyQueue.size()==0 && running== ProcessMenager.getDummy())//jezeli kolejka gotowych procesow jest pusta
        {
            running.state="NULL";
            running=process; //jako proces uruchomiony ustawiam process poniewaz innych nie ma
            running.state=RUNNING;
        }
        else
        {
            readyQueue.add(process);//dodanie procesu do kolejki procesow gotowych
            calculate_srt();
            process.state=READY;
            set_process_running();//wybieranie procesu o najmniejszym TAU
        }
    }

    public static void set_process_running()//wybranie procesu o najmniejszym pzrewidywanym czasie
    {
        int min=999999; //zmienna pomocnicza przy wybieraniu najmnijszej wartosci czasu
        int index=0;
        for(int i=0; i<readyQueue.size();i++)
        {
            if(readyQueue.get(i).expected_time<min)
            {
                min= (int) readyQueue.get(i).expected_time;
                index=i;
            }
        }
        if(running.expected_time>readyQueue.get(index).expected_time)
        {
            add_running(running);
            running.state=READY;
            running = readyQueue.get(index);
            running.state=RUNNING;
            remove_process(readyQueue.get(index).getPid());
        }
    }

    public static int process_waiting() //<-----------------------------------------SZYMON zmiana running na waiting
    {
        calculate_srt();
        int code=1;
        if (running==ProcessMenager.getDummy())
        {
            code=0;
        }

        else {
            int min = 999999; //zmienna pomocnicza przy wybieraniu najmnijszej wartosci czasu
            int index = 0;
            for (int i = 0; i < readyQueue.size(); i++) {
                if (readyQueue.get(i).expected_time < min) {
                    min = (int) readyQueue.get(i).expected_time;
                    index = i;
                }
            }
            running.state = WAITING;
            ProcessMenager.getWaitingList().add(running);
            running = readyQueue.get(index);
            remove_process(readyQueue.get(index).getPid());
        }
        return code;

              /*if (code==0)
        {
            //System.out.println("Nie mozna zmienic stanu powniewaz running jest init");
        }
        else
        {
            //System.out.println("zmieniono stan na waiting");
        }


         */

    }

    public static PCB get_running()
    {
        return running;//zwraca uruchomiony proces
    }
}
