package Shell;

import FileSystem.FileManager;
import Scheduler.Scheduler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

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
                System.out.println("Zmiana nazwy pliku");
            break;
        case "DELETE":
            System.out.println("Usuniecie pliku");
            break;
        case "PL":
            System.out.println("Wyswietlenie listy wszystkich procesow");
            break;
        case "LOCKS":
            System.out.println("Wyswietla zamki");
            break;
        case "PRINT_READY":
            //Scheduler.print_ready_queue();
            break;
        case "PRINT_RUNNING":
            //Scheduler.print_running_process();
            break;
        case "FOPEN":
           if( FileManager.openFile(command[1]))
           {
               System.out.println("Nie udalo sie otworzyc pliku.");
           }
           else {
               System.out.println("Plik Otwarty Pomyslnie.");
           }
           break;
        case "FCREATE":
                FileManager.createFile(command[1]);
                break;
        case "WRITE":
            if(command.length == 2) {
                if (FileManager.isOpened(command[1]) == 0) {
                    String data;
                    System.out.println("Prosze podac dane do zapisania w pliku");
                    Scanner read = new Scanner(System.in);
                    data = read.next();
                    if (FileManager.appendFile(command[1], data) == 1) {
                        System.out.println("Dane wpisane pomyslnie");
                    } else if (FileManager.appendFile(command[1], data) == 2) {
                        System.out.println("Brak wolnych blokow do zapisu na dysku");
                    } else if (FileManager.appendFile(command[1], data) == 3) {
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
        case "DATE":
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateobj = new Date();
            System.out.println(df.format(dateobj));
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
