package RAM.MMU;

import java.util.Stack;
import Process.PCB;
public class MemoryManager {
    final static public int FRAMES = 64;
    final static public int FRAME_SIZE = 16;
    private static FrameTableEntry[] frameTable = new FrameTableEntry[FRAMES];
    private static Stack<Byte> freeFrames = new Stack<Byte>();
    private static byte[] RAM = new byte[FRAMES*FRAME_SIZE];

    private static short byteToUShort (byte c) {
        if (c < 0)
            return (short)(c + 128);
        else
            return (short)((c + 128) & 0xff);
    }

    public static void init() {
        for (byte r = 0; r < frameTable.length; r++) {
            frameTable[r] = new FrameTableEntry();
        }
        for (byte x = 0; x < frameTable.length; x++) {
            freeFrames.push(x);
        }
        for (short g = 0; g < RAM.length; g++) {
            RAM[g] = '%';
        }
    }

    public static byte neededFrames(PCB pcb) {
        if (pcb.code.size() % FRAME_SIZE == 0)
            return (byte)(pcb.code.size()/ FRAME_SIZE);
        else
            return (byte)( (pcb.code.size()/ FRAME_SIZE) + 1);
    }
//odczyt z pamieci, poprzez adres logiczny zmieniany na fizyczny
    public static short readInProcess (PCB pcb, short adr) {
        if (pcb.pageTable.empty()) {
            System.out.println("\n Procesowi \"" + pcb.getName() + "\"(" + pcb.getPID() + ") nie przydzielono zadnej pamieci. Najpierw zaalokuj ja za pomoca odpowiedniego polecenia.");
            return -1;
        }
        else {
            short physAdr = (short)(pcb.pageTable.get(adr / FRAME_SIZE) * FRAME_SIZE + (adr - ((adr / FRAME_SIZE) * FRAME_SIZE)));
            if (frameTable[physAdr / FRAME_SIZE].getPID() == pcb.getPID()) {
                return byteToUShort(RAM[physAdr]);
            }
            else {
                System.out.println("\n Proces \"" + pcb.getName() + "\"(" + pcb.getPID() + ") nie ma dostepu do ramki pamieci o indeksie " + (physAdr / FRAME_SIZE) + ".");
                return -2;
            }
        }
    }
//bezposredni odczyt danych z pamieci ram, pod wskazanym adresem
    public static short readInRAM (short physAdr) {
        //Nie rozumiem po co w ten sposob

        return byteToUShort(RAM[physAdr]);
        //return (short) RAM[physAdr];

    }
//zapis danej w pamieci procesu, poprzez adres logiczny zamieniany na fizyczny
    public static void writeInProcess (PCB pcb, short adr, short data) {
        if (pcb.pageTable.empty()) {
            System.out.println("\n Procesowi \"" + pcb.getName() + "\"(" + pcb.getPID() + ") nie przydzielono zadnej pamieci. Najpierw zaalokuj ja za pomoca odpowiedniego polecenia.");
        }
        else {
            short physAdr = (short)(pcb.pageTable.get(adr / FRAME_SIZE) * FRAME_SIZE + (adr - ((adr / FRAME_SIZE) * FRAME_SIZE)));
            if (frameTable[physAdr / FRAME_SIZE].getPID() == pcb.getPID()) {
                RAM[physAdr] = (byte)(data - 128);
            }
            else {
                System.out.println("\n Proces \"" + pcb.getName() + "\"(" + pcb.getPID() + ") nie ma dostepu do ramki pamieci o indeksie " + (physAdr / FRAME_SIZE) + ".");
            }
        }
    }
//bezposredni zapis danej do ramu, pod wskazanym adresem
    public static void writeInRAM (short physAdr, short data) {
        RAM[physAdr] = (byte)(data - 128);
        //RAM[physAdr] = (byte)(data);
    }
//metoda sluzaca do alokacji procesu i tablicy stron w ramie
    public static void allocateProcess (PCB pcb) {
        if (neededFrames(pcb) == 0) {
            System.out.println("\n Proces \"" + pcb.getName() + "\"(" + pcb.getPID() + ") nie ma kodu, wiec nie mozna go zaalokowac.");
        }
        else if (neededFrames(pcb) > frameTable.length) {
            System.out.println("\n Za duzy rozmiar programu (ponad 16 x 64 bajtow).");
        }
        else if (neededFrames(pcb) > freeFrames.size() ) {
            System.out.println("\n Za malo wolnych ramek (" + freeFrames.size() + "), by zaladować kod procesu \"" + pcb.getName() + "\"(" + pcb.getPID() + ") [potrzebne ramki: " + neededFrames(pcb) + "].");
        }
        else {
            for (byte w = 0; w < neededFrames(pcb); w++) {
                byte currentFrame = freeFrames.peek();
                if (!frameTable[currentFrame].isTaken()) {
                    frameTable[currentFrame].setTaken(true);
                    frameTable[currentFrame].setPID((short) pcb.getPID() );
                    pcb.pageTable.push(currentFrame);
                    freeFrames.pop();
                }
            }
            for (short t = 0; t < pcb.code.size(); t++) {
                writeInProcess( pcb, t, pcb.code.get(t) );
            }
        }
    }
//alokowanie dodatkowej strony pamieci dla procesu
    public static void addPage (PCB pcb) {
        if (!freeFrames.empty()) {
            pcb.pageTable.push( freeFrames.peek() );
            freeFrames.pop();
        }
        else {
            System.out.println("\n Nie mozna zaalokowac dodatkowej strony dla procesu \"" + pcb.getName() + "\"(" + pcb.getPID() + "). Brak wolnych ramek pamieci operacyjnej.");
        }
    }
//usuwanie ostatniej strony z tabeli stron + zwolnienie ramki
    public static void removePage (PCB pcb) {
        if (pcb.pageTable.empty()) {
            System.out.println("\n Nie mozna odjac strony pamieci, bo proces \"" + pcb.getName() + "\"(" + pcb.getPID() + ") nie ma przypisanej zadnej pamieci.");
        }
        else {
            byte currentFrame = pcb.pageTable.peek();
            frameTable[currentFrame].setTaken(false);
            frameTable[currentFrame].setPID((short) -1);
            freeFrames.push(currentFrame);
            pcb.pageTable.pop();
        }
    }
//dealokowanie pamieci procesu wraz z wyczyszczeniem tablicy stron i zwolnieniem ramek
    public static void deallocateProcess (PCB pcb) {
        if (pcb.pageTable.empty()) {
            System.out.println("\n Nie ma czego dealokowac, bo proces \"" + pcb.getName() + "\"(" + pcb.getPID() + ") nie ma przypisanej zadnej pamieci.");
        }
        else {
            while (!pcb.pageTable.empty()) {
                byte currentFrame = pcb.pageTable.peek();
                frameTable[currentFrame].setTaken(false);
                frameTable[currentFrame].setPID((short) -1);
                freeFrames.push(currentFrame);
                pcb.pageTable.pop();
            }
        }
    }
//sprawdza czy proces ma przypisane jakies ramki
    public static boolean isProcessAllocated(PCB pcb) {
        for (byte i = 0; i < frameTable.length; i++) {
            if (frameTable[i].getPID() == pcb.getPID() ) {
                return true;
            }
        }
        return false;
    }
//wyswietlanie zawartosci pamieci wraz z wlasnosciami ramek
    public static void printMemory() {
        System.out.println("\n Zawartosc pamieci fizycznej:");
        for (byte i = 0; i < frameTable.length; i++) {
            System.out.print("\t Ramka " + i + " nalezy do procesu o ID: " + frameTable[i].getPID() + ":\n\t\t");
            for (byte j = 0; j < FRAME_SIZE; j++) {
                System.out.print(readInRAM((short)(i*FRAME_SIZE + j)) + "\t");
            }
            System.out.println();
        }
    }
//zwracanie zawartosci ramki o danym indeksie
    public static void printFrame(byte frame) {
        if (frame >= 0 && frame < frameTable.length) {
            System.out.print("\n Zawartosc ramki nr " + frame + ":\n\t\t");
            for (byte j = 0; j < FRAME_SIZE; j++) {
                System.out.print( readInRAM( (short)(frame*FRAME_SIZE + j) ) + "\t");
            }
            System.out.println();
        }
        /*else {
            System.out.println("\n Ramka o podanym numerze nie istnieje.");
        }*/
    }
//zwracanie zawartosci pamieci fizycznej pod podanym adresem
    public static void printCell(short physAddr) {
        if (physAddr >= 0 && physAddr < RAM.length) {
            System.out.println("\n  Zawartosc pamieci fizycznej pod adresem " + physAddr + ":\t" + RAM[physAddr]);
        }
        /*else {
            System.out.println("\n Adres spoza zasięgu pamięci.");
        }*/
    }
//wyswietlanie zawartosci pamieci fizycznej, zrzutowane na ascii + wlasnosci ramek
    public static void printMemoryASCII() {
        System.out.println("\n Zawartosc pamieci fizycznej, zrzutowana na ASCII:");
        for (byte i = 0; i < frameTable.length; i++) {
            System.out.print("\t Ramka " + i + " nalezy do procesu o ID: " + frameTable[i].getPID() + ":\n\t\t");
            for (byte j = 0; j < FRAME_SIZE; j++) {
                System.out.print( (char)(readInRAM( (short)(i*FRAME_SIZE + j) )) + "\t");
            }
            System.out.println();
        }
    }
//wyswietlanie zawartosci ramek o wskazanym indeksie zrzutowane na ascii
    public static void printFrameASCII(byte frame) {
        if (frame >= 0 && frame < frameTable.length) {
            System.out.print("\n Zawartosc ramki o indeksie " + frame + ", zrzutowana na ASCII:\n\t\t");
            for (byte j = 0; j < FRAME_SIZE; j++) {
                System.out.print((char)RAM[frame*FRAME_SIZE + j] + "\t");
            }
            System.out.println();
        }
        /*else {
            System.out.println("\n  Ramka o podanym indeksie nie istnieje.");
        }*/
    }
//wyswietlanie zawartosci pamieci fizycznej pod podanym adresem zrzutowane na ascii
    public static void printCellASCII(short physAddr) {
        if (physAddr >= 0 && physAddr < RAM.length) {
            System.out.println("\n  Zawartosc pamieci fizycznej pod adresem " + physAddr + ", zrzutowana na ASCII:\t" + (char)RAM[physAddr]);
        }
        /*else {
            System.out.println("\n  Adres spoza zasięgu pamięci.");
        }*/
    }
//wyswietlanie indeksow wolnych ramek
    public static void printFreeFrames() {
        System.out.print("\n  Wolne ramki pamieci fizycznej (indeksy):\n\t\t");
        for (byte i = 0; i < frameTable.length; i++) {
            if(!frameTable[i].isTaken()) {
                System.out.print(i + "\t");
            }
        }
        System.out.print("\n Stos wolnych ramek (indeksy) od dolu do gory:\n\t\t");
        for (byte i = 0; i < freeFrames.size(); i++) {
            System.out.print(freeFrames.get(i) + "\t");
        }
        System.out.println();
    }
//wyswietlanie zawartosci pamieci logicznej danego procesu:
    public static void printLogicMemory(PCB pcb) {

        System.out.print("\n Zawartosc pamieci logicznej procesu \"" + pcb.getName() + "\" (PID: " + pcb.getPID() + "):" +
                         "\n\t\t");
        for (byte i = 0; i < pcb.pageTable.size(); i++) {
            System.out.print("\t Strona " + i + ", czyli ramka " + pcb.pageTable.get(i) + ":\n\t\t");
            for (byte j = 0; j < FRAME_SIZE; j++) {
                System.out.print(readInRAM((short) (pcb.pageTable.get(i) * FRAME_SIZE + j)) + "\t");
            }
            System.out.println();
        }
    }

//wyswietlanie zawartosci pamieci logicznej danego procesu, zrzutowanej na ASCII:
    public static void printLogicMemoryASCII(PCB pcb) {

        System.out.print("\n Zawartosc pamieci logicznej procesu \"" + pcb.getName() + "\" (PID: " + pcb.getPID() + ")," +
                         "zrzutowana na ASCII:\n\t\t");
        for (byte i = 0; i < pcb.pageTable.size(); i++) {
            System.out.print("\t Strona " + i + ", czyli ramka " + pcb.pageTable.get(i) + ":\n\t\t");
            for (byte j = 0; j < FRAME_SIZE; j++) {
                System.out.print((char)(readInRAM((short) (pcb.pageTable.get(i) * FRAME_SIZE + j))) + "\t");
            }
            System.out.println();
        }
    }

//wyswietlanie tablicy stron danego procesu:
    public static void printPageTable(PCB pcb) {

        System.out.print("\n Zawartosc tablicy stron procesu \"" + pcb.getName() + "\" (PID: " + pcb.getPID() + "):\n\t\t");

        System.out.print("\t Strona \tRamka\n\t\t");

        for (byte i = 0; i < pcb.pageTable.size(); ++i) {

            System.out.print("\t " + i + " \t" + pcb.pageTable.get(i) + "\n\t\t");
        }
        System.out.println();
    }
}