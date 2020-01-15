package Scheduler;

import java.util.Vector;

import Interpreter.Interpreter;
import Process.PCB;

import Process.ProcessMenager;
import static Process.ProcessState.NEW;
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
    private int x;
    public static void set_init()//metoda ustawiająca init jako uruchomiony                                                                                                <---------- DOMINIK
    {
        running= ProcessMenager.getDummy();
    }

    private static void calculate_srt()//metoda obliczajaca srednia wykladnicza ostatnich procesow
    {
        running.expected_time = alfa * running.getCounter() + ((1 - alfa) * running.expected_time); //oblicza kazdemu procesowi w kolejce gotowych przewidywany czas
    }

    public static void add_running(PCB x)
    {
        readyQueue.add(x);
    }

    public static int remove_process(int pid)//metoda usuwajaca proces z kolejki gotowych procesow
    {
        int code=0;
        for(int i=0;i<readyQueue.size();i++)
        {
            if(readyQueue.get(i).getPid()==(pid))
            {
                readyQueue.remove(i);
                code=1;
            }
        }

        /*
        if(code==1)
        {
            System.out.println("SCHEDULER ->" + " usunieto proces z kolejki procesow gotowych");
        }
        else if(code==0)
        {
            System.out.println("SCHEDULER ->" + " w kolejce procesow gotowych nie ma procesu o podanym id: " + pid);
        }
        */
        return code;
    }

    public static int remove_running()
    {
        int code=1;
        if (running==ProcessMenager.getDummy())
        {
            code=0;
        }

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
        running.state=TERMINATED;
        running=readyQueue.get(index);
        remove_process(readyQueue.get(index).getPid());

        return code;


        /*if (code==0)
        {
            //System.out.println("Nie mozna usunac poniewaz runing jest initem");
        }
        else
        {
            //System.out.println("usunieto proces running");
        }


         */
    }


    public static Vector<PCB> print_ready_queue() //wypisywanie calej tablicy procesow gotowych
    {

        /*

        if(readyQueue.size()==0)//jezeli kolejka procesow gotowych jest pusta wypisuje info
        {
            System.out.println("SCHEDULER ->" + " kolejka gotowych procesow jest pusta");
        }
        else
        {
            for (int i = 0; i < readyQueue.size(); i++)
            {
                System.out.println("SCHEDULER -> " + "|ID:" + readyQueue.get(i).getPid() + "| |Name:" + readyQueue.get(i).getName() + "| |Tau:" + readyQueue.get(i).expected_time + "| |Tn:" + readyQueue.get(i).getCounter() + "| |State:" + readyQueue.get(i).state + "|"); //wypisuje numer procesu, nazwe oraz Tau
            }
        }

         */
        return readyQueue;
    }

    public static PCB print_running_process()
    {
        return running;
     //   System.out.println("SCHEDULER -> " + "|ID:" + running.getPid() + "| |Name:" + running.getName() + "| |Tau:" + running.expected_time + "| |Tn:" + running.getCounter() + "| |State:" + running.state + "|"); //wypisuje uruchomiony proces
    }

    public static void add_process(PCB process)//metoda dodajaca proces do kolejki procesow gotowych
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
    public static PCB get_running()
    {
        return running;//zwraca uruchomiony proces                                                                              <---------- Do wykorzystania
    }
}