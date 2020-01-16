package Interpreter;

import FileSystem.Disk;
import FileSystem.DiskManager;
import FileSystem.FileManager;
import Programs.FileLoader;
import RAM.MMU.MemoryManager;
import Process.ProcessMenager;
import Scheduler.Scheduler;
import Process.PCB;

import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

import static Interpreter.Tester.isTest;

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


    private static void getRegisters() {
        rA = Scheduler.get_running().getrA();
        rB = Scheduler.get_running().getrB();
        rC = Scheduler.get_running().getrC();
        rD = Scheduler.get_running().getrD();
        counter = Scheduler.get_running().getCounter();
    }

    private static void setRegisters() {
        Scheduler.get_running().setrA(rA);
        Scheduler.get_running().setrB(rB);
        Scheduler.get_running().setrC(rC);
        Scheduler.get_running().setrD(rD);
        Scheduler.get_running().setCounter(counter);
    }

    private static String readCommand(){
        String buff = "";
        int adr = 0;
        char mark;

        for(int s = 0; s < Scheduler.get_running().pageTable.size(); s++){
            if(counter/MemoryManager.FRAME_SIZE != s) continue;

            for(int i = 0 ; i < MemoryManager.FRAME_SIZE; i++){
                if(counter != s*MemoryManager.FRAME_SIZE+i) continue;

                adr = Scheduler.get_running().pageTable.elementAt(s)*MemoryManager.FRAME_SIZE +i;
                mark = (char) MemoryManager.readInRAM((short)adr);
                if(mark == ';'){
                    counter = counter + 2;
                    System.out.println(buff);
                    return buff;
                }
                else buff = buff + mark;
                counter++;
            }
        }

        return null;
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
                case APPEND_FILE:
                    appendFile(strCommand.substring(3));
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
            Scheduler.get_running().licznikRoz++;
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
    }
    //TODO
    private static void jumpTo(String argsStr) throws InvalidArgumentsInterpreterException{
        String[] args = argsStr.split(" ");
        if (args.length != 1) {
            throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawna liczba argumentow");
        }
        String buff = args[0].replace("[","").replace("]","");
        counter =  Integer.parseInt(buff);
    }
    //TODO
    private static void jumpZero(String argsStr) throws InvalidArgumentsInterpreterException{
        String[] args = argsStr.split(" ");
        if (args.length != 1) {
            throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawna liczba argumentow");
        }

        String buff = args[0].replace("[","").replace("]","");
        if(rD == 0)counter =  Integer.parseInt(buff);
    }
    //TODO
    private static void halt() {
        ProcessMenager.terminateProcess(Scheduler.get_running().getName());
        getRegisters();
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

    private static void writeFile(String argsStr) throws InvalidArgumentsInterpreterException{
        String[] args = argsStr.split(" ");
        if(args.length != 3) throw new InvalidArgumentsInterpreterException("Interpreter: Zla liczba argumentow.");
        String buff = "";
        int adr = 0;
        char mark;
        String dataCounterS = args[1].replace("[", "").replace("]", "");
        int dataCounter = Integer.parseInt(dataCounterS);
        int len = Integer.parseInt(args[2]);


        for(int s = 0; s < Scheduler.get_running().pageTable.size(); s++){
            if(dataCounter/MemoryManager.FRAME_SIZE != s) continue;

            for(int i = 0 ; i < MemoryManager.FRAME_SIZE; i++){
                if(dataCounter != s*MemoryManager.FRAME_SIZE+i) continue;

                adr = Scheduler.get_running().pageTable.elementAt(s)*MemoryManager.FRAME_SIZE +i;
                mark = (char) MemoryManager.readInRAM((short)adr);
                if(len == 0){
                    dataCounter = dataCounter + 2;

                    //System.out.println(buff);
                    FileManager.writeFile(args[0], buff);
                    return;
                }
                else buff = buff + mark;
                dataCounter++;
                len--;
            }
        }

    }

    private static void formProcess(String argsStr) throws InvalidArgumentsInterpreterException{
        String[] args = argsStr.split(" ");
        if(args.length != 2) throw new InvalidArgumentsInterpreterException("Interpreter: Zla liczba argumentow.");
        String path = "Source/Programs/" + args[1];
        ProcessMenager.newProcess(args[0], path);
        for(PCB pcb : ProcessMenager.list){
            if (Scheduler.get_running().getName().equals(args[0])){
                getRegisters();
            }
        }
    }

    private static void deleteProcess(String argsStr) throws InvalidArgumentsInterpreterException {
        String[] args = argsStr.split(" ");
        if (args.length != 1) throw new InvalidArgumentsInterpreterException("Interpreter: Zla liczba argumentow.");
        ProcessMenager.terminateProcess(args[0]);
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
    }

    private static void doNothing() {
        //totalna utopia
    }

    private static void swapByte(String argsStr) throws InvalidArgumentsInterpreterException {
        String[] args = argsStr.split(" ");
        if (args.length != 3) {
            throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawna liczba argumentow");
        }
        String string = FileManager.readFile(args[0], FileManager.fileLength(args[0])).replace(args[1], args[2]);
        FileManager.writeFile(args[0], string);
    }


    private static void appendFile(String argsStr) throws InvalidArgumentsInterpreterException {
        String[] args = argsStr.split(" ");
        if (args.length != 2) {
            throw new InvalidArgumentsInterpreterException("Interpreter: Niepoprawna liczba argumentow");
        }
        FileManager.appendFile(args[0], args[1]);
    }


}