package Locks;

import java.util.Queue;
import java.util.LinkedList;

import Process.*;
import Scheduler.*;

public class Lock {

        // Kolejka oczekujących procesów:
    private Queue<PCB> m_waitingProcesses;

        // Stan zamka.
    private Boolean m_locked;

        // PID właściciela.
    private int m_ownerPID;

        // Getter stanu zamka:
    public Boolean isLocked() {return m_locked;}

        // Getter PID właściciela:
    public int getOwner() {return m_ownerPID;}

        // Konstruktor:
    public Lock () {
        m_waitingProcesses = new LinkedList<>();
        m_locked = false;
        m_ownerPID = 0;
    }

    public int lock() {

        if (!m_locked && m_ownerPID == 0) {

            m_locked = true;
            m_ownerPID = Scheduler.get_running().getPID();
            // Scheduler.get_running().openFiles.add()
            return 0;

        } else if (m_locked && m_ownerPID != 0) {

            m_waitingProcesses.add(Scheduler.get_running());
            Scheduler.process_waiting();
            return 1;

        } else if ((!m_locked && m_ownerPID != 0) || (m_locked && m_ownerPID == 0)) {

            ProcessMenager.terminateProcess(Scheduler.get_running().getName());
            return 2;
        }

        return 2;
    }
        /*
        Zamykanie zamka. Jeśli jest otwarty i nie ma właściciela, pole „locked” jest
        ustawiane na „true”, a „ownerPID” na ID procesu zamykającego zamek. Jeśli jest
        otwarty, ale ma właściciela, wyświetlany jest komunikat o błędzie, próbujący
        otworzyć zamek proces jest zabijany i wywoływana jest metoda ponownego
        przydziału czasu, z modułu planisty procesora. Jeśli jest zamknięty, wywoływane
        jest uśpienie próbującego zamknąć go procesu z modułu zarządcy procesora, jego
        PCB jest dodawane do kolejki oczekujących i również wywoływana jest metoda
        planisty do ponownego przydziału czasu. Będzie wywoływana przez interpreter
        rozkazów asemblerowych, w momencie próby dostępu do zasobu.
        */

    public int unlock() {

        if (m_locked && m_ownerPID == Scheduler.get_running().getPID()) {

            m_locked = true;
            m_ownerPID = Scheduler.get_running().getPID();
            // Scheduler.get_running().openFiles.add()

        } else if (m_locked && m_ownerPID != 0) {

            m_waitingProcesses.add(Scheduler.get_running());
            Scheduler.process_waiting();

        } else if ((!m_locked && m_ownerPID != 0) || (m_locked && m_ownerPID == 0)) {

            ProcessMenager.terminateProcess(Scheduler.get_running().getName());
        }
    }
        /*
        Otwieranie zamka. Jeśli jest już otwarty, metoda wyświetli stosowny
        komunikat, proces próbujący to zrobić zostanie zabity, a plan przydziału czasu
        procesora zaktualizowany. Jeśli jest zamknięty i ID procesu próbującego
        otworzyć zamek jest inne niż ID właściciela, metoda wyświetli komunikat o
        odmowie dostępu i jej powodzie, proces próbujący to zrobić zostanie zabity, a
        plan przydziału czasu procesora zaktualizowany. Jeśli ID jest zgodne, metoda
        zmieni pole locked na „false”, ownerPID na „0” oraz wywoła budzenie pierwszego
        czekającego w kolejce procesu, korzystając z modułu zarządcy procesora.
        Wywoływana również przez interpreter.
        */

        // + ew. settery

        /*
        Komunikat o utworzeniu zamka dla pliku o danej nazwie / ID.
        Komunikat o usunięciu zamka dla pliku o danej nazwie / ID.
        Komunikat o zamknięciu zamka + PID właściciela.
        Komunikat o zwolnieniu zamka + PID zwalniającego.
        Komunikat o dodaniu procesu o podanym PID do kolejki.
        Komunikat o usunięciu procesu o podanym PID z kolejki.
        Polecenie do wyświetlenia zawartości zamka danego pliku.
        */
}
        /*
        Instancje tej klasy znajdować się będą w obiekcie każdego zasobu (u
        nas tylko pliku). Jej konstruktor będzie więc wywoływany przez moduł
        systemu plików.
        */
