package Shell;

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
