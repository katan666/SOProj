package Shell;

import FileSystem.FileManager;
import Interpreter.Interpreter;
import Scheduler.Scheduler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Vector;
import Process.PCB;
import Process.ProcessMenager;
import RAM.MMU.MemoryManager;
import FileSystem.DiskManager;


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
        case "PRINT_MEMORY":
            if(command.length == 1)
            {
                MemoryManager.printMemory();
            }
            else System.out.println("Niepoprawna ilosc argumentow.");
            break;
        case "PRINT_FRAME":
            if(command.length == 2)
            {
               int temp = Integer.parseInt(command[1]);
               if(temp >=0 && temp <=63)
               {
                   byte temp2 = (byte) temp;
                   System.out.println(temp2);
                   MemoryManager.printFrame(temp2);
               }
               else System.out.println("Nie ma ramki o tym numerze.");
            }
            else System.out.println("Nieprawidlowa liczba parametrow");
            break;
        case "PRINT_CELL":
            if(command.length == 2)
            {
                int temp = Integer.parseInt(command[1]);
                if(temp>=0 && temp<=1023)
                {
                    short temp1 = (short)temp;
                    MemoryManager.printCell(temp1);
                }
                else System.out.println("Nie ma komorki o tym adresie fizycznym.");
            }
            else System.out.println("Nieprawidlowa liczba paramterow");
            break;
        case "PRINT_MEMORY_ASCII":
            if(command.length==1)
            {
                MemoryManager.printMemoryASCII();
            }
            else System.out.println("Nieprawidlowa liczba paramterow.");
            break;
        case "PRINT_FRAME_ASCII":
            if(command.length == 2)
            {
                int temp = Integer.parseInt(command[1]);
                if(temp>=0 && temp <= 63)
                {
                    byte temp1 = (byte)temp;
                    MemoryManager.printFrameASCII(temp1);
                }
                else System.out.println("Nie ma komorki o tym adresie fizycznym.");
            }
            else System.out.println("Nieprawidlowa liczba arugmentow");
            break;
        case "PRINT_CELL_ASCII":
            if(command.length == 2)
            {
                int temp = Integer.parseInt(command[1]);
                if(temp>=0 && temp<=1023)
                {
                    short temp1 = (short)temp;
                    MemoryManager.printCellASCII(temp1);
                }
                else System.out.println("Nie ma komorki o tym adresie fizycznym.");
            }
            else System.out.println("Nieprawidlowa liczba paramterow");
            break;
        case "PRINT_FREE_FRAMES":
            if(command.length == 1)
            {
                MemoryManager.printFreeFrames();
            }
            else System.out.println("Nieprawidlowa liczba paramterow");
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
        case "READ":
            if(command.length == 2)
            {
                    if(FileManager.isOpened(command[1]) == 0)
                    {
                        System.out.println("Ten plik zawiera: " + FileManager.fileLength(command[1]) + " znakow.");
                        System.out.println("Ile znakow chcesz odczytac?");
                        Scanner temp = new Scanner(System.in);
                        int data = temp.nextInt();
                        FileManager.readFile(command[1],data);
                    }
                    else if(FileManager.isOpened(command[1]) == 1)
                    {
                        System.out.println("Plik nie zostal otwarty.");
                    }
                    else if(FileManager.isOpened(command[1]) == 2)
                    {
                        System.out.println("Plik nie istnieje.");
                    }
            }
            else System.out.println("Niepoprawna ilosc argumentow,");
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
        case "DIR_OPEN":
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
               if(ProcessMenager.newProcess(command[1],command[2]) == 0)
               {
                   System.out.println("Proces utworzony pomyslnie.");
               }
               else if(ProcessMenager.newProcess(command[1],command[2]) ==1)
                {
                    System.out.println("Nie udalo sie utworzyc procesu. Ta nazwa jest juz zajeta.");
                }
            }
            else System.out.println("Niepoprawna liczba argumentow.");
            break;
        case "TERMINATE_PROCESS":
            if(command.length == 2)
            {
                if(ProcessMenager.terminateProcess(command[1])==0)
                {
                    System.out.println("Proces zabity.");
                }
                else if(ProcessMenager.terminateProcess(command[1])==1)
                {
                    System.out.println("Nie ma procesu o takiej nazwie");
                }
                else if(ProcessMenager.terminateProcess(command[1])==2)
                {
                    System.out.println("Nie mozna usunac dummy");
                }
            }
            else System.out.println("Nieprawidlowa liczba argumentow");
            break;
        case "HELP":
            if(command.length==1)
            {
                Help();
            }
            else System.out.println("Nieprawidlowa liczba argumentow");
            break;
        case "DISK":
            if(command.length == 1)
            {
                DiskManager.showDisk();
            }
            else System.out.println("Nieprawidlowa liczba argumentow");
            break;
        case "MAP":
            if(command.length==1)
            {
                DiskManager.showBitMap();
            }
            else System.out.println("Nieprawidlowa liczba argumentow");
            break;
        case "STEP":
            if(command.length == 1)
            {
                Interpreter.executeInstruction();
                System.out.println(Scheduler.get_running().toStringReg());
            }
            else System.out.println("Nieprawidlowa liczba argumentow");
            break;
        case "REG":
            if(command.length == 1)
            {
                System.out.println(Scheduler.get_running().toStringReg());
            }
            else System.out.println("Nieprawidlowa liczba argumentow");
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
        line = line.toUpperCase();
        Match(line);

    }
}

public void Help()
{
    System.out.println("DATE                                                        -Wyswietla aktualna date i godzine.");
    System.out.println("DIR                                                         -Wyswietla zawartosc glownego katalogu");
    System.out.println("DIR_OPEN                                                    -Wyswietla liste otwartych plikow");
    System.out.println("FCREATE nazwa pliku                                         -Tworzy plik o podanej nazwie");
    System.out.println("FOPEN nazwa pliku                                           -Otwiera plik o podanej nazwie");
    System.out.println("APPEND nazwa pliku                                          -Dopisuje dane na koniec pliku");
    System.out.println("WRITE nazwa pliku                                           -Nadpisuje dane pliku o podanej nazwie");
    System.out.println("FCLOSE nazwa pliku                                          -Zamyka plik o podanej nazwie");
    System.out.println("READ nazwa pliku                                            -Wyswietla plik o podanej nazwie");
    System.out.println("CREATE_PROCESS nazwa procesu nazwa pliku                    -Tworzy nowy proces");
    System.out.println("TERMINATE_PROCESS nazwa procesu                             -Zabija proces o danej nazwie");
    System.out.println("PRINT_RUNNING                                               -Wyswietla uruchomiony proces");
    System.out.println("PRINT_READY                                                 -Wyswietla kolejke gotowych procesow");
    System.out.println("LOCKS                                                       -Wyswietla zamki");
    System.out.println("PL                                                          -Wyswietla liste wszystkich procesow");
    System.out.println("DELETE nazwa pliku                                          -Usuwa plik o podanej nazwie");
    System.out.println("RENAME aktualna nazwa nowa nazwa                            -Zmienia nazwe pliku");
    System.out.println("PRINT_MEMORY                                                -Wyswietla cala pamiec RAM");
    System.out.println("PRINT_FRAME numer stronicy                                  -Wyswietla dana stronice pamieci RAM");
    System.out.println("PRINT_CELL adres fizyczny                                   -Wyswietla zawartosc komorki pamieci pod podanym adresem");
    System.out.println("PRINT_MEMORY_ASCII                                          -Wyswietla cala pamiec RAM rzutowana na ASCII");
    System.out.println("PRINT_FRAME_ASCII numer stronicy                            -Wyswietla dana stronice pamieci RAM rzutowana na ASCII");
    System.out.println("PRINT_CELL_ASCII  adres fizyczny                            -Wyswielta zawartosci komorki pamieci pod podanym adresem rzutowana na ASCII");
    System.out.println("PRINT_FREE_FRAME                                            -Wyswielta numery wszystkich wolnych ramek");
    System.out.println("EXIT                                                        -Konczy dzialanie systemu");
    System.out.println("HELP                                                        -Wyswietla pomoc dla uzytkownika ");
    System.out.println("DISK                                                        -Wyswielta zawartosc dysku");
    System.out.println("MAP                                                         -Wyswielta macierz zajetosci bloku");
    System.out.println("STEP                                                        -Wykonuje nastepna komende");
    System.out.println("REG                                                         -Wyswietla rejestry");


}




}
