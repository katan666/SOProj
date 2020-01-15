package RAM.MMU;

import java.util.Stack;
import Process.PCB;
public class MemoryManager {
    final static public int FRAMES = 64;
    final static public int FRAME_SIZE = 16;

    private static FrameTableEntry[] frameTable = new FrameTableEntry[FRAMES];
    private static Stack<Byte> freeFrames = new Stack<Byte>();
    private static byte[] RAM = new byte[FRAMES*FRAME_SIZE];
    //private static final byte FRAME_SIZE = 16;

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

    public static short readInProcess (PCB pcb, short adr) {
        if (pcb.pageTable.empty()) {
            System.out.println("\n <MemoryManager> [BŁĄD] Procesowi \"" + pcb.getName() + "\"(" + pcb.getPID() + ") nie przydzielono żadnej pamięci. Najpierw zaalokuj ją za pomocą odpowiedniego polecenia.");
            return -1;
        }
        else {
            short physAdr = (short)(pcb.pageTable.get(adr / FRAME_SIZE) * FRAME_SIZE + (adr - ((adr / FRAME_SIZE) * FRAME_SIZE)));
            if (frameTable[physAdr / FRAME_SIZE].getPID() == pcb.getPID()) {
                return byteToUShort(RAM[physAdr]);
            }
            else {
                System.out.println("\n <MemoryManager> [BŁĄD] Proces \"" + pcb.getName() + "\"(" + pcb.getPID() + ") nie ma dostępu do ramki pamięci o indeksie " + (physAdr / FRAME_SIZE) + ".");
                return -2;
            }
        }
    }

    public static short readInRAM (short physAdr) {
        //Nie rozumiem po co w ten sposob

        return byteToUShort(RAM[physAdr]);
        //return (short) RAM[physAdr];

    }

    public static void writeInProcess (PCB pcb, short adr, short data) {
        if (pcb.pageTable.empty()) {
            System.out.println("\n <MemoryManager> [BŁĄD] Procesowi \"" + pcb.getName() + "\"(" + pcb.getPID() + ") nie przydzielono żadnej pamięci. Najpierw zaalokuj ją za pomocą odpowiedniego polecenia.");
        }
        else {
            short physAdr = (short)(pcb.pageTable.get(adr / FRAME_SIZE) * FRAME_SIZE + (adr - ((adr / FRAME_SIZE) * FRAME_SIZE)));
            if (frameTable[physAdr / FRAME_SIZE].getPID() == pcb.getPID()) {
                RAM[physAdr] = (byte)(data - 128);
            }
            else {
                System.out.println("\n <MemoryManager> [BŁĄD] Proces \"" + pcb.getName() + "\"(" + pcb.getPID() + ") nie ma dostępu do ramki pamięci o indeksie " + (physAdr / FRAME_SIZE) + ".");
            }
        }
    }

    public static void writeInRAM (short physAdr, short data) {
        //Znowu nie rozumiem dlaczego tak

        RAM[physAdr] = (byte)(data - 128);
        //RAM[physAdr] = (byte)(data);
    }

    public static void allocateProcess (PCB pcb) {
        if (neededFrames(pcb) == 0) {
            System.out.println("\n <MemoryManager> [BŁĄD] Proces \"" + pcb.getName() + "\"(" + pcb.getPID() + ") nie ma kodu, więc nie można go zaalokować.");
        }
        else if (neededFrames(pcb) > frameTable.length) {
            System.out.println("\n <MemoryManager> [BŁĄD] Za duży rozmiar programu (ponad 256 bajtów).");
        }
        else if (neededFrames(pcb) > freeFrames.size() ) {
            System.out.println("\n <MemoryManager> [BŁĄD] Za mało wolnych ramek (" + freeFrames.size() + "), by załadować kod procesu \"" + pcb.getName() + "\"(" + pcb.getPID() + ") [potrzebne ramki: " + neededFrames(pcb) + "].");
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

    public static void addPage (PCB pcb) {
        if (!freeFrames.empty()) {
            pcb.pageTable.push( freeFrames.peek() );
            freeFrames.pop();
        }
        else {
            System.out.println("\n <MemoryManager> [BŁĄD] Nie mozna zaalokowac dodatkowej strony dla procesu \"" + pcb.getName() + "\"(" + pcb.getPID() + "). Brak wolnych ramek pamieci operacyjnej.");
        }
    }

    public static void removePage (PCB pcb) {
        if (pcb.pageTable.empty()) {
            System.out.println("\n <MemoryManager> [BŁĄD] Nie można odjąć strony pamięci, bo proces \"" + pcb.getName() + "\"(" + pcb.getPID() + ") nie ma przypisanej żadnej pamięci.");
        }
        else {
            byte currentFrame = pcb.pageTable.peek();
            frameTable[currentFrame].setTaken(false);
            frameTable[currentFrame].setPID((short) -1);
            freeFrames.push(currentFrame);
            pcb.pageTable.pop();
        }
    }

    public static void deallocateProcess (PCB pcb) {
        if (pcb.pageTable.empty()) {
            System.out.println("\n <MemoryManager> [BŁĄD] Nie ma czego dealokować, bo proces \"" + pcb.getName() + "\"(" + pcb.getPID() + ") nie ma przypisanej żadnej pamięci.");
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

    public static boolean isProcessAllocated(PCB pcb) {
        for (byte i = 0; i < frameTable.length; i++) {
            if (frameTable[i].getPID() == pcb.getPID() ) {
                return true;
            }
        }
        return false;
    }

    public static void printMemory() {
        System.out.println("\n <MemoryManager> Zawartość pamięci fizycznej:");
        for (byte i = 0; i < frameTable.length; i++) {
            System.out.print("\t Ramka " + i + " (należy do procesu o ID: " + frameTable[i].getPID() + "):\n\t\t");
            for (byte j = 0; j < FRAME_SIZE; j++) {
                System.out.print( readInRAM((short)(i*FRAME_SIZE + j)) + "\t");
            }
            System.out.println();
        }
    }

    public static void printFrame(byte frame) {
        if (frame >= 0 && frame < frameTable.length) {
            System.out.print("\n <MemoryManager> Zawartość ramki nr " + frame + ":\n\t\t");
            for (byte j = 0; j < FRAME_SIZE; j++) {
                System.out.print( readInRAM( (short)(frame*FRAME_SIZE + j) ) + "\t");
            }
            System.out.println();
        }
        else {
            System.out.println("\n <MemoryManager> [BŁĄD] Ramka o podanym numerze nie istnieje (jest spoza zasięgu).");
        }
    }

    public static void printCell(short physAddr) {
        if (physAddr >= 0 && physAddr < RAM.length) {
            System.out.println("\n <MemoryManager> Zawartość pamięci fizycznej pod adresem " + physAddr + ":\t" + RAM[physAddr]);
        }
        else {
            System.out.println("\n <MemoryManager> [BŁĄD] Adres spoza zasięgu pamięci.");
        }
    }

    public static void printMemoryASCII() {
        System.out.println("\n <MemoryManager> Zawartość pamięci fizycznej zrzutowana na ASCII:");
        for (byte i = 0; i < frameTable.length; i++) {
            System.out.print("\t Ramka " + i + " (należy do procesu o ID: " + frameTable[i].getPID() + "):\n\t\t");
            for (byte j = 0; j < FRAME_SIZE; j++) {
                System.out.print( (char)(readInRAM( (short)(i*FRAME_SIZE + j) )) + "\t");
            }
            System.out.println();
        }
    }

    public static void printFrameASCII(byte frame) {
        if (frame >= 0 && frame < frameTable.length) {
            System.out.print("\n <MemoryManager> Zawartość ramki o indeksie " + frame + " zrzutowana na ASCII:\n\t\t");
            for (byte j = 0; j < FRAME_SIZE; j++) {
                System.out.print((char)RAM[frame*FRAME_SIZE + j] + "\t");
            }
            System.out.println();
        }
        else {
            System.out.println("\n <MemoryManager> [BŁĄD] Ramka o podanym indeksie nie istnieje (jest spoza zasięgu).");
        }
    }

    public static void printCellASCII(short physAddr) {
        if (physAddr >= 0 && physAddr < RAM.length) {
            System.out.println("\n <MemoryManager> Zawartość pamięci fizycznej pod adresem " + physAddr + ",zrzutowana na ASCII:\t" + (char)RAM[physAddr]);
        } else {
            System.out.println("\n <MemoryManager> [BŁĄD] Adres spoza zasięgu pamięci.");
        }
    }

    public static void printFreeFrames() {
        System.out.print("\n <MemoryManager> Wolne ramki pamięci fizycznej (indeksy):\n\t\t");
        for (byte i = 0; i < frameTable.length; i++) {
            if(!frameTable[i].isTaken()) {
                System.out.print(i + "\t");
            }
        }
        System.out.print("\n Stos wolnych ramek (indeksy) (od dołu do góry):\n\t\t");
        for (byte i = 0; i < freeFrames.size(); i++) {
            System.out.print(freeFrames.get(i) + "\t");
        }
        System.out.println();
    }
}