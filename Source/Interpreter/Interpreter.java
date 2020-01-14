package Interpreter;

import FileSystem.Disk;
import FileSystem.DiskManager;
import FileSystem.FileManager;
import Programs.FileLoader;
import RAM.MMU.MemoryManager;
import Process.ProcessMenager;
import RAM.MMU.PCB;

import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

public class Interpreter {
    private final static String REG_A = "AX";
    private final static String REG_B = "BX";
    private final static String REG_C = "CX";
    private final static String REG_D = "DX";


    private static int rA;
    private static int rB;
    private static int rC;
    private static int rD;
    private static int counter;
    //TODO private static PCB pcb;

    //TODO
    private static void getRegisters() {}
    //TODO
    private static void setRegisters() {}
    //TODO
    private static String readCommand(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Write command in: ");
        String string = scanner.nextLine();
        if(string.equals("show")){
            System.out.printf("AX:%d\nBX:%d\nCX:%d\nDX:%d\n", rA,rB,rC,rD);
            return readCommand();
        } else if (string.equals("showDisk")){
            DiskManager.showDisk();
            return readCommand();
        } else if (string.equals("showRAM")){
            MemoryManager.printMemory();
            return readCommand();
        } else if (string.equals("showRAM ascii")){
            MemoryManager.printMemoryASCII();
            return readCommand();
        } else if (string.equals("writeRAM")){
            MemoryManager.writeInRAM((short)16, (short)60);
            return readCommand();
        } else if (string.equals("showBitMap")){
            DiskManager.showBitMap();
            return readCommand();
        } else if (string.equals("test0")){
            Stack<Byte> v1 = new Stack<>();
            Stack<Byte> v2 = new Stack<>();
            RAM.MMU.PCB pcb0 = new RAM.MMU.PCB((short)6,"nazwa", v1, FileLoader.readAllBytesFromFileToShortVec("Source/Programs/dummy.txt"));
            RAM.MMU.PCB pcb1 = new RAM.MMU.PCB((short)23,"nazwa", v2, FileLoader.readAllBytesFromFileToShortVec("Source/Programs/ciagLiczbKwadratowych.txt"));
            MemoryManager.allocateProcess(pcb0);
            MemoryManager.allocateProcess(pcb1);
            for (Byte e : pcb0.pageTable){
                System.out.println(e);
            }
            return readCommand();
        } else if (string.equals("showProcessList")){
            System.out.println(ProcessMenager.listOfProcess());

            return readCommand();
    }
        return string;
    }
    //Na podstawie pierwszych 2 znakow w argumencie str okresla typ komendy
    private static CommandType typeOf(String str){
        return CommandType.defineType(str.substring(0,2));
    }
    //Wybiera i wykonuje komende
    private static void execute(String strCommand) {
        try {
            switch (typeOf(strCommand)) {
                case ADD:
                    add(strCommand.substring(3));
                    break;
                case HALT:
                    halt();
                    break;
                case MOVE:
                    move(strCommand.substring(3));
                    break;
                case ADD_INT:
                    addInt(strCommand.substring(3));
                    break;
                case JUMP_TO:
                    jumpTo(strCommand.substring(3));
                    break;
                case MULTIPLY:
                    multiply(strCommand.substring(3));
                    break;
                case SUBTRACT:
                    subtract(strCommand.substring(3));
                    break;
                case DECREMENT:
                    decrement(strCommand.substring(3));
                    break;
                case OPEN_FILE:
                    openFile(strCommand.substring(3));
                    break;
                case INCREMENT:
                    increment(strCommand.substring(3));
                    break;
                case JUMP_ZERO:
                    jumpZero(strCommand.substring(3));
                    break;
                case READ_FILE:
                    readFile(strCommand.substring(3));
                    break;
                case SWAP_BYTE:
                    swapByte(strCommand.substring(3));
                    break;
                case DO_NOTHING:
                    doNothing();
                    break;
                case WRITE_FILE:
                    writeFile(strCommand.substring(3));
                    break;
                case CREATE_FILE:
                    createFile(strCommand.substring(3));
                    break;
                case DELETE_FILE:
                    deleteFile(strCommand.substring(3));
                    break;
                case RUN_PROCESS:
                    runProcess(strCommand.substring(3));
                    break;
                case FORM_PROCESS:
                    formProcess(strCommand.substring(3));
                    break;
                case MULTIPLY_INT:
                    multiplyInt(strCommand.substring(3));
                    break;
                case SUBTRACT_INT:
                    subtractInt(strCommand.substring(3));
                    break;
                case DELETE_PROCESS:
                    deleteProcess(strCommand.substring(3));
                    break;
                case INVALID_COMMAND:
                    System.out.println("INVALID COMMAND");
                    break;
                case MOVE_INT_TO_REGISTER:
                    moveIntToReg(strCommand.substring(3));
                    break;
                case CLOSE_FILE:
                    closeFile(strCommand.substring(3));
                    break;
                default:
                    System.out.println("Default");
            }
        } catch (InvalidArgumentsInterpreterException e){
            System.out.println(e.getMessage());
        }
    }

    //Jedyna metoda udostepniona pozostalym modulom
    public static void executeInstruction(){
        try {
            getRegisters();
            execute(readCommand());
            setRegisters();
        }catch (StringIndexOutOfBoundsException e0){
            //TODO obsluzyc wyjatek
            System.out.println(e0.getMessage());
        }
    }







    private static void add(String argsStr) throws InvalidArgumentsInterpreterException {
        String[] args = argsStr.split(" ");
        if (args.length != 2) {
            throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawna liczba argumentow");
        }
        int buff;
        switch (args[1]){
            case REG_A:
                buff = rA;
                break;
            case REG_B:
                buff = rB;
                break;
            case REG_C:
                buff = rC;
                break;
            case REG_D:
                buff = rD;
                break;
            default:
                throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawny argument");
        }
        switch (args[0]){
            case REG_A:
                rA+=buff;
                break;
            case REG_B:
                rB+=buff;
                break;
            case REG_C:
                rC+=buff;
                break;
            case REG_D:
                rD+=buff;
                break;
            default:
                throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawny argument");
        }
        counter += argsStr.length() + 3;

    }

    private static void addInt(String argsStr) throws InvalidArgumentsInterpreterException {
        String[] args = argsStr.split(" ");
        if (args.length != 2) {
            throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawna liczba argumentow");
        }
        int buff;
        try {
            buff = Integer.parseInt(args[1]);
        } catch (NumberFormatException e){
            buff = 0;
        }
        switch (args[0]){
            case REG_A:
                rA+=buff;
                break;
            case REG_B:
                rB+=buff;
                break;
            case REG_C:
                rC+=buff;
                break;
            case REG_D:
                rD+=buff;
                break;
            default:
                throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawny argument");
        }
        counter += argsStr.length() + 3;
    }

    private static void subtract(String argsStr) throws InvalidArgumentsInterpreterException {
        String[] args = argsStr.split(" ");
        if (args.length != 2) {
            throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawna liczba argumentow");
        }
        int buff;
        switch (args[1]){
            case REG_A:
                buff = rA;
                break;
            case REG_B:
                buff = rB;
                break;
            case REG_C:
                buff = rC;
                break;
            case REG_D:
                buff = rD;
                break;
            default:
                throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawny argument");
        }
        switch (args[0]){
            case REG_A:
                rA-=buff;
                break;
            case REG_B:
                rB-=buff;
                break;
            case REG_C:
                rC-=buff;
                break;
            case REG_D:
                rD-=buff;
                break;
            default:
                throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawny argument");
        }
        counter += argsStr.length() + 3;
    }

    private static void subtractInt(String argsStr) throws InvalidArgumentsInterpreterException {
        String[] args = argsStr.split(" ");
        if (args.length != 2) {
            throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawna liczba argumentow");
        }
        int buff;
        try {
            buff = Integer.parseInt(args[1]);
        } catch (NumberFormatException e){
            buff = 0;
        }
        switch (args[0]){
            case REG_A:
                rA-=buff;
                break;
            case REG_B:
                rB-=buff;
                break;
            case REG_C:
                rC-=buff;
                break;
            case REG_D:
                rD-=buff;
                break;
            default:
                throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawny argument");
        }
        counter += argsStr.length() + 3;
    }

    private static void multiply(String argsStr) throws InvalidArgumentsInterpreterException {
        String[] args = argsStr.split(" ");
        if (args.length != 2) {
            throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawna liczba argumentow");
        }
        int buff;
        switch (args[1]){
            case REG_A:
                buff = rA;
                break;
            case REG_B:
                buff = rB;
                break;
            case REG_C:
                buff = rC;
                break;
            case REG_D:
                buff = rD;
                break;
            default:
                throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawny argument");
        }
        switch (args[0]){
            case REG_A:
                rA*=buff;
                break;
            case REG_B:
                rB*=buff;
                break;
            case REG_C:
                rC*=buff;
                break;
            case REG_D:
                rD*=buff;
                break;
            default:
                throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawny argument");
        }
        counter += argsStr.length() + 3;
    }

    private static void multiplyInt(String argsStr) throws InvalidArgumentsInterpreterException {
        String[] args = argsStr.split(" ");
        if (args.length != 2) {
            throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawna liczba argumentow");
        }
        int buff;
        try {
            buff = Integer.parseInt(args[1]);
        } catch (NumberFormatException e){
            buff = 0;
        }
        switch (args[0]){
            case REG_A:
                rA*=buff;
                break;
            case REG_B:
                rB*=buff;
                break;
            case REG_C:
                rC*=buff;
                break;
            case REG_D:
                rD*=buff;
                break;
            default:
                throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawny argument");
        }
        counter += argsStr.length() + 3;
    }

    private static void move(String argsStr) throws InvalidArgumentsInterpreterException {
            String[] args = argsStr.split(" ");
            if (args.length != 2) {
                throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawna liczba argumentow");
            }
            int buff;
            switch (args[1]){
                case REG_A:
                    buff = rA;
                    break;
                case REG_B:
                    buff = rB;
                    break;
                case REG_C:
                    buff = rC;
                    break;
                case REG_D:
                    buff = rD;
                    break;
                default:
                    throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawny argument");
            }
            switch (args[0]){
                case REG_A:
                    rA=buff;
                    break;
                case REG_B:
                    rB=buff;
                    break;
                case REG_C:
                    rC=buff;
                    break;
                case REG_D:
                    rD=buff;
                    break;
                default:
                    throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawny argument");
            }
            counter += argsStr.length() + 3;
    }

    private static void moveIntToReg(String argsStr) throws InvalidArgumentsInterpreterException {
        String[] args = argsStr.split(" ");
        if (args.length != 2) {
            throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawna liczba argumentow");
        }
        int buff;
        try {
            buff = Integer.parseInt(args[1]);
        } catch (NumberFormatException e){
            buff = 0;
        }
        switch (args[0]){
            case REG_A:
                rA=buff;
                break;
            case REG_B:
                rB=buff;
                break;
            case REG_C:
                rC=buff;
                break;
            case REG_D:
                rD=buff;
                break;
            default:
                throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawny argument");
        }
        counter += argsStr.length() + 3;
    }
    //TODO
    private static void jumpTo(String args) {
        System.out.printf("jumpTo args: %s\n", args);
    }
    //TODO
    private static void jumpZero(String args) {
        System.out.printf("jumpZero args: %s\n", args);
    }
    //TODO
    private static void halt() {
        System.out.printf("halt args: %s\n", "");
    }

    private static void openFile (String argsStr) throws InvalidArgumentsInterpreterException{
        String[] args = argsStr.split(" ");
        if(args.length != 1) throw new InvalidArgumentsInterpreterException("Interpreter: Zla liczba argumentow.");
        boolean isOkey = FileManager.openFile(args[0]);
        if (!isOkey) throw new InvalidArgumentsInterpreterException("Interpreter: Problem z FileManager.openFile");
    }

    private static void closeFile(String argsStr) throws InvalidArgumentsInterpreterException{
        String[] args = argsStr.split(" ");
        if(args.length != 1) throw new InvalidArgumentsInterpreterException("Interpreter: Zla liczba argumentow.");
        int isOkey = FileManager.closeFile(args[0]);
        if (isOkey != 0) throw new InvalidArgumentsInterpreterException("Interpreter: Problem z FileManager.closeFile");

    }

    private static void deleteFile(String argsStr) throws InvalidArgumentsInterpreterException{
        String[] args = argsStr.split(" ");
        if(args.length != 1) throw new InvalidArgumentsInterpreterException("Interpreter: Zla liczba argumentow.");
        int isOkey = FileManager.deleteFile(args[0]);
        if (isOkey != 0) throw new InvalidArgumentsInterpreterException("Interpreter: Problem z FileManager.deleteFile");
    }

    private static void createFile(String argsStr) throws InvalidArgumentsInterpreterException{
        String[] args = argsStr.split(" ");
        if(args.length != 1) throw new InvalidArgumentsInterpreterException("Interpreter: Zla liczba argumentow.");
        boolean isOkey = FileManager.createFile(args[0]);
        if (!isOkey) throw new InvalidArgumentsInterpreterException("Interpreter: Problem z FileManager.createFile");
    }
    //TODO
    private static void readFile(String args) {
        System.out.printf("readFile args: %s\n", args);
    }
    //TODO
    private static void writeFile(String argsStr) {
        String[] args = argsStr.split(" ");
        System.out.println(FileManager.writeFile(args[0], "kurwa"));
    }
    //TODO
    private static void formProcess(String args) {
        System.out.printf("formProcess args: %s\n", args);
    }
    //TODO
    private static void deleteProcess(String argsStr) throws InvalidArgumentsInterpreterException{
        String[] args = argsStr.split(" ");
    }
    //TODO
    private static void runProcess(String args) {
        System.out.printf("runProcess args: %s\n", args);
    }

    private static void decrement(String argsStr) throws InvalidArgumentsInterpreterException {
        String[] args = argsStr.split(" ");
        if (args.length != 1) {
            throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawna liczba argumentow");
        }
        switch (args[0]){
            case REG_A:
                rA--;
                break;
            case REG_B:
                rB--;
                break;
            case REG_C:
                rC--;
                break;
            case REG_D:
                rD--;
                break;
            default:
                throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawny argument");
        }
        counter += argsStr.length() + 3;
    }

    private static void increment(String argsStr) throws InvalidArgumentsInterpreterException {
        String[] args = argsStr.split(" ");
        if (args.length != 1) {
            throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawna liczba argumentow");
        }
        switch (args[0]){
            case REG_A:
                rA++;
                break;
            case REG_B:
                rB++;
                break;
            case REG_C:
                rC++;
                break;
            case REG_D:
                rD++;
                break;
            default:
                throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawny argument");
        }
        counter += argsStr.length() + 3;
    }

    private static void doNothing() {
        //totalna utopia
    }
    //TODO
    private static void swapByte(String args) {
        System.out.printf("swapByte args: %s\n", args);
    }





}

