package Shell;

import FileSystem.FileManager;
import Scheduler.Scheduler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Vector;
import Process.PCB;
import Process.ProcessMenager;


public class Shell
{
int finish = 0;

Scanner rd = new Scanner(System.in);

public void Match(String line)
{
    String[] command = line.split("\\s+");

    switch(command[0])
    {
        case "EXIT":
            finish = 1;
            break;
        case "MEMORY":
            System.out.println("Wyswietlenie pamieci ram");
            break;
        case "RENAME":
            if(command.length == 3)
            {
                if(FileManager.renameFile(command[1],command[2]) == 0)
                {
                    System.out.println("Nazwa zostala pomyslnie zmieniona.");
                }
                else if(FileManager.renameFile(command[1],command[2])==1)
                {
                    System.out.println("Plik nie zostal otwarty");
                }
                else if(FileManager.renameFile(command[1],command[2])==2)
                {
                    System.out.println("Plik nie istnieje");
                }
            }
            else System.out.println("Zbyt wiele parametrow.");
            break;
        case "DELETE":
            if(command.length == 2)
            {
                if(FileManager.deleteFile(command[1])==0)
                {
                    System.out.println("Plik usuniety pomyslnie");
                }
                else if(FileManager.deleteFile(command[1])==1)
                {
                    System.out.println("Nie mozna usunac pliku, poniewaz jest on otwarty.");
                }
                else if(FileManager.deleteFile(command[1])==2) {
                    System.out.println("Plik nie istnieje.");
                }
            }
            else System.out.println("Zla liczba argumentow.");
            break;
        case "PL":
            if(command.length==1)
            {
                System.out.println(ProcessMenager.listOfProcess());
            }
            else System.out.println("Niepoprawna liczba argumentow.");
            break;
        case "LOCKS":
            System.out.println("Wyswietla zamki");
            break;
        case "PRINT_READY":
            if(command.length==1)
            {
                Vector<PCB> temp = Scheduler.print_ready_queue();
                if(temp.size()==0)
                {
                        System.out.println("Kolejka gotowych procesow jest pusta.");
                }
                else {
                    for (int i = 0; i < temp.size(); i++) {
                        System.out.println("ID: " + temp.get(i).getPID() + " Name: " + temp.get(i).getName() + " Tau: " + temp.get(i).expected_time + " Tn: " + temp.get(i).getCounter() + " State: " + temp.get(i).state);
                    }
                }
            }
            else System.out.println("Niepoprawna ilosc argumentow.");
            break;
        case "PRINT_RUNNING":
            if(command.length==1)
            {
                PCB temp = Scheduler.print_running_process();
                System.out.println("ID:" + temp.getPid() + " Name: " + temp.getName() + " Tau: " + temp.expected_time + " Tn: " + temp.getCounter() + " State: " + temp.state);
            }
            else System.out.println("Niepoprawna ilosc argumentow.");
            break;
        case "FOPEN":
            if(command.length==2) {
                if (FileManager.openFile(command[1]) == true) {
                    System.out.println("Pomyslnie otworzono plik");
                } else {
                    System.out.println("Nie udalo sie otworzyc pliku.");
                }
            }
            else System.out.println("Nieprawidlowa ilosc argumentow");
           break;
        case "FCREATE":
            if(command.length==2) {
                if (FileManager.createFile(command[1]) == true) {
                    System.out.println("Pomyslnie utworzono plik.");
                } else System.out.println("Nie udalo sie utworzyc pliku.");
            }
            else System.out.println("Nieprawidlowa ilosc argumentow");
            break;
        case "APPEND":
            if(command.length == 2) {
                if (FileManager.isOpened(command[1]) == 0) {
                    String data;
                    System.out.println("Prosze podac dane do zapisania w pliku");
                    Scanner read = new Scanner(System.in);
                    data = read.next();
                    if (FileManager.appendFile(command[1], data) == 0) {
                        System.out.println("Dane wpisane pomyslnie");
                    } else if (FileManager.appendFile(command[1], data) == 3) {
                        System.out.println("Brak wolnych blokow do zapisu na dysku");
                    } else if (FileManager.appendFile(command[1], data) == 4) {
                        System.out.println("Plik przekroczyl maksymalna wielkosc");
                    }
                } else if (FileManager.isOpened(command[1]) == 2) {
                    System.out.println("Plik nie istnieje.");
                } else if (FileManager.isOpened(command[1]) == 1) {
                    System.out.println("Plik nie zostal otwarty.");
                }
            }
            else System.out.println("Zbyt wiele parametrow.");
            break;
        case "WRITE":
            if(command.length == 2) {
                if (FileManager.isOpened(command[1]) == 0) {
                    String data;
                    System.out.println("Prosze podac dane do zapisania w pliku");
                    Scanner read = new Scanner(System.in);
                    data = read.next();
                    if (FileManager.writeFile(command[1], data) == 0) {
                        System.out.println("Dane wpisane pomyslnie");
                    } else if (FileManager.writeFile(command[1], data) == 3) {
                        System.out.println("Brak wolnych blokow do zapisu na dysku");
                    } else if (FileManager.writeFile(command[1], data) == 4) {
                        System.out.println("Plik przekroczyl maksymalna wielkosc");
                    }
                } else if (FileManager.isOpened(command[1]) == 2) {
                    System.out.println("Plik nie istnieje.");
                } else if (FileManager.isOpened(command[1]) == 1) {
                    System.out.println("Plik nie zostal otwarty.");
                }
            }
            else System.out.println("Zbyt wiele parametrow.");
            break;
        case "FCLOSE":
            if(command.length == 2) {
                if (FileManager.closeFile(command[1]) == 0) {
                    System.out.println("Plik zostal pomyslnie zamkniety.");
                } else if (FileManager.closeFile(command[1]) == 1) {
                    System.out.println("Plik nie zostal otwarty");
                } else if (FileManager.closeFile(command[1]) == 2) {
                    System.out.println("Plik nie istnieje.");
                }
            }
            else System.out.println("Zbyt wiele parametrow");
            break;
        case "DATE":
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateobj = new Date();
            System.out.println(df.format(dateobj));
            break;
        case "DIR":
            if(command.length == 1)
            {
                for(String e : FileManager.showMainCatalog())
                {
                    System.out.println(e);
                }
            }
            else System.out.println("Nieprawidlowa ilosc argumentow");
            break;
        case "DIROPEN":
            if(command.length == 1)
            {
                for(String e : FileManager.showOpenedFiles())
                {
                    System.out.println(e);
                }
            }
            else System.out.println("Nieprawidlowa ilosc argumentow");
            break;
        case "CREATE_PROCESS":
            if(command.length==3)
            {
                ProcessMenager.newProcess(command[1],command[2]);
            }
            else System.out.println("Niepoprawna liczba argumentow.");
            break;
        default:
            System.out.println("Niepoprawna komenda");
            break;




    }
}

public void loopStart()
{
    while(true)
    {
        if(finish != 0)
        {
            break;
        }
        System.out.printf("C:\\>");
        String line = rd.nextLine();
        Match(line);

    }
}




}
