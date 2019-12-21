import java.util.Scanner;

public class Interpreter {
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
        return string;
    }
    //Na podstawie pierwszych 2 znakow w argumencie str okresla typ komendy
    private static CommandType typeOf(String str){
        return CommandType.defineType(str.substring(0,2));
    }
    //Wybiera i wykonuje komende
    private static void execute(String strCommand){
        switch (typeOf(strCommand)){
            case ADD:                   add(strCommand.substring(3));           break;
            case HALT:                  halt();                                 break;
            case MOVE:                  move(strCommand.substring(3));          break;
            case ADD_INT:               addInt(strCommand.substring(3));        break;
            case JUMP_TO:               jumpTo(strCommand.substring(3));        break;
            case MULTIPLY:              multiply(strCommand.substring(3));      break;
            case SUBTRACT:              subtract(strCommand.substring(3));      break;
            case DECREMENT:             decrement(strCommand.substring(3));     break;
            case FORM_FILE:             formFile(strCommand.substring(3));      break;
            case INCREMENT:             increment(strCommand.substring(3));     break;
            case JUMP_ZERO:             jumpZero(strCommand.substring(3));      break;
            case READ_FILE:             readFile(strCommand.substring(3));      break;
            case SWAP_BYTE:             swapByte(strCommand.substring(3));      break;
            case DO_NOTHING:            doNothing();                            break;
            case WRITE_FILE:            writeFile(strCommand.substring(3));     break;
            case CREATE_FILE:           createFile(strCommand.substring(3));    break;
            case DELETE_FILE:           deleteFile(strCommand.substring(3));    break;
            case RUN_PROCESS:           runProcess(strCommand.substring(3));    break;
            case FORM_PROCESS:          formProcess(strCommand.substring(3));   break;
            case MULTIPLY_INT:          multiplyInt(strCommand.substring(3));   break;
            case SUBTRACT_INT:          subtractInt(strCommand.substring(3));   break;
            case DELETE_PROCESS:        deleteProcess(strCommand.substring(3)); break;
            case INVALID_COMMAND:       System.out.println("INVALID COMMAND");  break;
            case MOVE_INT_TO_REGISTER:  moveIntToReg(strCommand.substring(3));  break;
            default:                    System.out.println("Default");
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






    //TODO commands method
    private static void add(String args) {
        System.out.printf("add args: %s\n", args);
    }
    private static void addInt(String args) {
        System.out.printf("addInt args: %s\n", args);
    }
    private static void subtract(String args) {
        System.out.printf("subtract args: %s\n", args);
    }
    private static void subtractInt(String args) {
        System.out.printf("subtractInt args: %s\n", args);
    }
    private static void multiply(String args) {
        System.out.printf("multiply args: %s\n", args);
    }
    private static void multiplyInt(String args) {
        System.out.printf("multiplyInt args: %s\n", args);
    }
    private static void move(String args) {
        System.out.printf("move args: %s\n", args);
    }
    private static void moveIntToReg(String args) {
        System.out.printf("moveIntToReg args: %s\n", args);
    }
    private static void jumpTo(String args) {
        System.out.printf("jumpTo args: %s\n", args);
    }
    private static void jumpZero(String args) {
        System.out.printf("jumpZero args: %s\n", args);
    }
    private static void halt() {
        System.out.printf("halt args: %s\n", "");
    }
    private static void formFile(String args) {
        System.out.printf("formFile args: %s\n", args);
    }
    private static void deleteFile(String args) {
        System.out.printf("deleteFile args: %s\n", args);
    }
    private static void createFile(String args) {
        System.out.printf("createFile args: %s\n", args);
    }
    private static void readFile(String args) {
        System.out.printf("readFile args: %s\n", args);
    }
    private static void writeFile(String args) {
        System.out.printf("writeFile args: %s\n", args);
    }
    private static void formProcess(String args) {
        System.out.printf("formProcess args: %s\n", args);
    }
    private static void deleteProcess(String args) {
        System.out.printf("deleteProcess args: %s\n", args);
    }
    private static void runProcess(String args) {
        System.out.printf("runProcess args: %s\n", args);
    }
    private static void decrement(String args) {
        System.out.printf("decrement args: %s\n", args);
    }
    private static void increment(String args) {
        System.out.printf("increment args: %s\n", args);
    }
    private static void doNothing() {
        System.out.printf("doNothing args: %s\n", "");
    }
    private static void swapByte(String args) {
        System.out.printf("swapByte args: %s\n", args);
    }


}

