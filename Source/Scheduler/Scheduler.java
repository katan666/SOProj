import java.util.Vector;

class scheduler
{
    private static final double alfa = 0.5; //stala z zakresu [0-1]

    private static double expected_time = 5; //przewidywany czas pracy
    private static double real_time = 0; // rzeczywisty czas pracy
    static Vector<PCB> readyQueue = new Vector<PCB>();//wektor procesow gotowych do przydzielenia procesora
    private static PCB running; // uruchomiony proces
    private static Object Running;
    public static PCB init = new PCB ("init", "0","Running", 0, 0);

    public static void set_init()//metoda ustawiająca init jako uruchomiony
    {
        running=init;
    }

    private static void calculate_srt()//metoda obliczajaca srednia wykladnicza ostatnich procesow
    {
        running.expected_time = alfa * running.real_time + ((1 - alfa) * running.expected_time); //oblicza kazdemu procesowi w kolejce gotowych przewidywany czas
    }

    public static void add_process(PCB process)//metoda dodajaca proces do kolejki procesow gotowych
    {
        if(process.expected_time==0)
        {
            process.expected_time=expected_time;
        }
        if(readyQueue.size()==0 && running==init)//jezeli kolejka gotowych procesow jest pusta
        {
            running=process; //jako proces uruchomiony ustawiam process poniewaz innych nie ma
            running.state=state_running();
        }
        else
        {
            readyQueue.add(process);//dodanie procesu do kolejki procesow gotowych
            calculate_srt();
            process.state=state_ready();
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
            running.state=state_ready();
            running = readyQueue.get(index);
            running.state=state_running();
            remove_process(readyQueue.get(index).getPid());
        }
    }

    public static void add_running(PCB x)
    {
        readyQueue.add(x);
    }

    public static void remove_process(String pid)//metoda usuwajaca proces z kolejki gotowych procesow
    {
        int x=0;
        for(int i=0;i<readyQueue.size();i++)
        {
            if(readyQueue.get(i).getPid().equals(pid))
            {
                readyQueue.remove(i);
                x=1;
            }
        }

        if(x==1)
        {
            System.out.println("SCHEDULER ->" + " usunieto proces z kolejki procesow gotowych");
        }
        else if(x==0)
        {
            System.out.println("SCHEDULER ->" + " w kolejce procesow gotowych nie ma procesu o podanym id: " + pid);
        }
    }

    public static void remove_running()
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
        running=readyQueue.get(index);
        remove_process(readyQueue.get(index).getPid());
    }

    public static void print_ready_queue() //wypisywanie calej tablicy procesow gotowych
    {
        if(readyQueue.size()==0)//jezeli kolejka procesow gotowych jest pusta wypisuje info
        {
            System.out.println("SCHEDULER ->" + " kolejka gotowych procesow jest pusta");
        }
        else
        {
            for (int i = 0; i < readyQueue.size(); i++)
            {
                System.out.println("SCHEDULER -> " + "|ID:" + readyQueue.get(i).getPid() + "| |Name:" + readyQueue.get(i).getName() + "| |Tau:" + readyQueue.get(i).expected_time + "| |Tn:" + readyQueue.get(i).real_time + "| |State:" + readyQueue.get(i).state + "|"); //wypisuje numer procesu, nazwe oraz Tau
            }
        }
    }

    public static void print_running_process()
    {
        System.out.println("SCHEDULER -> " + "|ID:" + running.getPid() + "| |Name:" + running.getName() + "| |Tau:" + running.expected_time + "| |Tn:" + running.real_time + "| |State:" + running.state + "|"); //wypisuje uruchomiony proces
    }

    public static void print_process(PCB p)
    {
        System.out.println("|ID:" + p.getPid() + "| |Name:" + p.getName() + "| |Tau:" + p.expected_time + "| |Tn:" + p.real_time + "| |State:" + p.state + "|");
    }

    public static PCB get_running()
    {
        return running;//zwraca uruchomiony proces
    }

    public static String state_running()
    {
        return "Running";
    }
    public static String state_ready()
    {
        return "Ready";
    }
    public static String state_waiting()
    {
        return "Waiting";
    }

    public static void ready()
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
        running.state=state_ready();
        System.out.println("Stan zmieniony na ready");
        add_running(running);
        running=readyQueue.get(index);
        running.state=state_running();
        remove_process(readyQueue.get(index).getPid());
    }
    public static void waiting()
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
        running.state=state_waiting();
        System.out.println("Stan zmieniony na waiting");
        running=readyQueue.get(index);
        running.state=state_running();
        remove_process(readyQueue.get(index).getPid());
    }
}


//PCB, expected_time=0, real_time-zlicza rozkazy, state