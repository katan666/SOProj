public enum CommandType {
    ADD(2),
    ADD_INT(2),
    SUBTRACT(2),
    SUBTRACT_INT(2),
    MULTIPLY(2),
    MULTIPLY_INT(2),
    MOVE(2),
    MOVE_INT_TO_REGISTER(2),
    JUMP_TO(1),
    JUMP_ZERO(1),
    HALT(0),
    FORM_FILE(2),
    DELETE_FILE(2),
    CREATE_FILE(2),
    READ_FILE(3),
    WRITE_FILE(3),
    FORM_PROCESS(3),
    DELETE_PROCESS(3),
    RUN_PROCESS(3),
    DECREMENT(1),
    INCREMENT(1),
    DO_NOTHING(0),
    SWAP_BYTE(3),

    INVALID_COMMAND(0);

    private final int qtyOfArgs;

    CommandType(int qtyOfArgs){
        this.qtyOfArgs = qtyOfArgs;
    }


    //Zwraca liczbe argumentow
    public int getQtyOfArgs() {
        return qtyOfArgs;
    }

    //Na podstawie dwuliterowego oznaczenia zwraca odpowiedni typ
    public static CommandType defineType(String name){
        if(name.length() != 2) return INVALID_COMMAND;

        else if(name.equals("AD")) return ADD;
        else if(name.equals("AI")) return ADD_INT;
        else if(name.equals("SB")) return SUBTRACT;
        else if(name.equals("SI")) return SUBTRACT_INT;
        else if(name.equals("MU")) return MULTIPLY;
        else if(name.equals("MI")) return MULTIPLY_INT;
        else if(name.equals("MV")) return MOVE;
        else if(name.equals("MX")) return MOVE_INT_TO_REGISTER;
        else if(name.equals("JT")) return JUMP_TO;
        else if(name.equals("JZ")) return JUMP_ZERO;
        else if(name.equals("HT")) return HALT;
        else if(name.equals("FF")) return FORM_FILE;
        else if(name.equals("DF")) return DELETE_FILE;
        else if(name.equals("CF")) return CREATE_FILE;
        else if(name.equals("RF")) return READ_FILE;
        else if(name.equals("WF")) return WRITE_FILE;
        else if(name.equals("FP")) return FORM_PROCESS;
        else if(name.equals("DP")) return DELETE_PROCESS;
        else if(name.equals("RP")) return RUN_PROCESS;
        else if(name.equals("DC")) return DECREMENT;
        else if(name.equals("IC")) return INCREMENT;
        else if(name.equals("DN")) return DO_NOTHING;
        else if(name.equals("SW")) return SWAP_BYTE;

        else return INVALID_COMMAND;

    }
}
