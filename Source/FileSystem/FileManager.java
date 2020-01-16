package FileSystem;

import java.util.Vector;
import Scheduler.*;
import Process.*;

public class FileManager extends Files{

    public static Vector<Files> mainCatalog = new Vector<Files>();
    public static Vector<Character> openedFiles = new Vector<Character>();


    private final static char BEGIN = '0';
    private final static int LENGTH = 64;


    public static Vector<String> showMainCatalog(){
        Vector<String>filesInCatalog = new Vector();
        for(int i = 0; i < mainCatalog.size(); i++){
            filesInCatalog.add(mainCatalog.get(i).fileName);
        }
        return filesInCatalog;
    }


    public static Vector<String> showOpenedFiles(){
        Vector<String>of = new Vector();
        for(int i = 0; i < openedFiles.size(); i++) {
            for(int j = 0; j < mainCatalog.size(); j++){
                if (mainCatalog.get(j).indexBlock == openedFiles.get(i)) {
                    of.add(mainCatalog.get(j).fileName);
                }
            }
        }
        return of;
    }


    private static int readAddress(char address){
        /*
        * address == -1 błąd
        * else zwraca adres komorki jako liczbe int
         */
        int index = -1;
        if(address <= (LENGTH + BEGIN) && address >= BEGIN) {
            index = address - BEGIN;
        }
        return index;
    }


    private static char writeAddress(int index){
        char address = '~';
        if(index <= LENGTH && index >= 0) {
            address = (char) (index + BEGIN);
        }
        return address;
    }


    public static int fileLength(String name){
        int fileLength = 0;
        char index;
        int code;
        char toCheck;

        index = getIndexBlock(name);

        for(int j = 0; j < blockSize; j++) {
            if (disk[readAddress(index)][j] != '-') {
                toCheck = disk[readAddress(index)][j];
                for (int g = 0; g < blockSize; g++) {
                    if(disk[readAddress(toCheck)][g] != '%'){
                        fileLength++;
                    }
                }
            }
        }

        return fileLength;
    }


    /*
    public static int getWritePointer(String name){
        int writePointer = 0;
        for (int i = 0; i < mainCatalog.size(); i++) {
            if (name.equals(mainCatalog.get(i).fileName)) {
                writePointer = mainCatalog.get(i).writePointer;
            }
        }
        if(writePointer > 0){
            writePointer += 1;
        }
        return writePointer;
    }


    public static void setWritePointer(String name, int writePointer){
        for (int i = 0; i < mainCatalog.size(); i++) {
            if (name.equals(mainCatalog.get(i).fileName)) {
                    mainCatalog.get(i).writePointer = writePointer;
            }
        }
    }


    public static int getReadPointer(String name){
        int readPointer = 0;
        for (int i = 0; i < mainCatalog.size(); i++) {
            if (name.equals(mainCatalog.get(i).fileName)) {
                readPointer = mainCatalog.get(i).readPointer;
            }
        }
        if(readPointer > 0){
            readPointer += 1;
        }
        return readPointer;
    }


    public static void setReadPointer(String name, int readPointer){
        for (int i = 0; i < mainCatalog.size(); i++) {
            if (name.equals(mainCatalog.get(i).fileName)) {
                mainCatalog.get(i).readPointer = readPointer;
            }
        }
    }
    */


    private static char getIndexBlock(String name){
        char index = '-';
        if(isOpened(name) == 0) {
            for (int i = 0; i < mainCatalog.size(); i++) {
                if (name.equals(mainCatalog.get(i).fileName)) {
                    index = mainCatalog.get(i).indexBlock;
                }
            }
        }
        return index;
    }


    private static int whereIsOpened(String name){
        int index = -1;
        for(int i = 0; i < mainCatalog.size(); i++) {
            if (name.equals(mainCatalog.get(i).fileName)){
                for(int j = 0; j < openedFiles.size(); j++){
                    if (mainCatalog.get(i).indexBlock == openedFiles.get(j)) {
                        index = j;
                        break;
                    }
                }
            }
        }
        return index;
    }


    public static int isOpened(String name){
        /*
        * code == 0 plik otwarty
        * code == 1 plik nie zotal otwarty
        * code == 2 plik nie istnieje
         */
        int code = 2;
        for(int i = 0; i < mainCatalog.size(); i++) {
            if (name.equals(mainCatalog.get(i).fileName)){
                code = 1;
                for(int j = 0; j < openedFiles.size(); j++){
                    if (mainCatalog.get(i).indexBlock == openedFiles.get(j)) {
                        code = 0;
                        break;
                    }
                }
            }
        }
        return code;
    }


    public static boolean createFile(String name){
        /*
        * created == false Nie udalo sie utworzyc pliku
        * created == true Tworzenie pliku zakonczone pomyslnie
         */
        Files file = new Files();
        int i = 0;
        boolean created = false;
        while(!created && i < bitMap.length){
            if(bitMap[i] == 0){
                for(int j = 0; j < blockSize; j++){
                    disk[i][j] = '-';
                }
                created = true;
                file.indexBlock = writeAddress(i);
                file.fileName = name;
                file.Size = 8;
                bitMap[i] = 1;
            }
            i++;
        }
        mainCatalog.add(file);
        return created;
    }


    public static boolean openFile(String name){
        /*
        * opened == false plik nieistnieje
        * opened == true pomyslnie otwarto plik
         */
        boolean opened = false;
        for(int i = 0; i < mainCatalog.size(); i++){
            if(name.equals(mainCatalog.get(i).fileName)) {
                
                int temp = mainCatalog.get(i).zamek.lock();
                
                if (temp == 0) {
                    
                    Scheduler.get_running().openFiles.add(new OpenFile(mainCatalog.get(i)));
                    openedFiles.add(mainCatalog.get(i).indexBlock);
                    opened = true;
                    break;
                    
                } else if (temp == 1 || temp == 2) {

                    break;
                }
            }
        }
        return opened;
    }


    public static int writeFile(String name, String data){
        /*
         * Zmienna code oznacza status errorow
         * code == 0 - Zapisywanie zakonczone pomyslnie
         * code == 1 - Nie znaleziono pliku w wektorze otwartych plikow (czyt. plik nie zostal otwarty)
         * code == 2 - Plik nie istnieje
         * code == 3 - Brak wolnych blokow do zapisu.
         * code == 4 - Plik przekroczyl maksymalna wielkosc do zapisu. Plik za duzy
         * */
        int code;
        char index = '-';
        int blocksAmount = 0;
        int counter = 0;

        clearFile(name);
        code = isOpened(name);
        index = getIndexBlock(name);

        if(code == 0){
            for(int i = 0; i < blockSize; i++){
                if(disk[readAddress(index)][i] == '-'){
                    blocksAmount++;
                }
            }
            if(blocksAmount == 0){
                code = 3;
            }
            else if(blocksAmount * blockSize < data.length()){
                code = 4;
            }
            else{
                for(int i = 0; i < bitMap.length; i++){
                    if(bitMap[i] == 0){
                        int pointer = 0;
                        for(int j = 0; j + counter < data.length() && j < blockSize; j++) {
                            disk[i][j] = data.charAt(counter + j);
                            pointer++;
                        }
                        counter += pointer;
                        bitMap[i] = 1;
                        for(int g = 0; g < blockSize; g++){
                            if(disk[readAddress(index)][g] == '-'){
                                disk[readAddress(index)][g] = writeAddress(i);
                                break;
                            }
                        }
                        if(counter >= data.length()) {
                            break;
                        }
                    }
                }
            }
        }
        return code;
    }


    public static int appendFile(String name, String data){
        /*
        * Zmienna code oznacza status errorow
        * code == 0 - Zapisywanie zakonczone pomyslnie
        * code == 1 - Nie znaleziono pliku w wektorze otwartych plikow (czyt. plik nie zostal otwarty)
        * code == 2 - Plik nie istnieje
        * code == 3 - brak wolnych blokow do zapisu.
        * code == 4 - Plik przekroczyl maksymalna wielkosc do zapisu. Plik za duzy
        * */
        int code;
        char index = '-';
        int blocksAmount = 0;
        int counter = 0;
        int pointer;


        code = isOpened(name);
        index = getIndexBlock(name);

        if(code == 0){
            for(int i = 0; i < blockSize; i++){
                if(disk[readAddress(index)][i] == '-'){
                    blocksAmount++;
                }
            }
            if(blocksAmount == 0){
                code = 3;
            }
            else if(blocksAmount * blockSize < data.length()){
                code = 4;
            }
            else{
                for(int i = 0; i < bitMap.length; i++){
                    if(bitMap[i] == 0){
                        pointer = 0;
                        for(int j = 0; j + counter < data.length() && j < blockSize; j++) {
                            disk[i][j] = data.charAt(counter + j);
                            pointer++;
                        }
                        counter += pointer;
                        bitMap[i] = 1;
                        for(int g = 0; g < blockSize; g++){
                            if(disk[readAddress(index)][g] == '-'){
                                disk[readAddress(index)][g] = writeAddress(i);
                                break;
                            }
                        }
                        if(counter >= data.length()) {
                            break;
                        }
                    }
                }
            }
        }
        return code;
    }


    public static String readFile(String name, int howMuch){
        String stream = "";
        char index;
        char toRead = '-';
        int code;

        code = isOpened(name);
        index = getIndexBlock(name);

            if(code == 0){
                for(int j = 0; j < blockSize && howMuch > 0; j++) {
                    if (disk[readAddress(index)][j] != '-') {
                        toRead = disk[readAddress(index)][j];
                        for (int g = 0; g < blockSize && howMuch > 0; g++) {
                            if(disk[readAddress(toRead)][g] != '%'){
                                stream += disk[readAddress(toRead)][g];
                                howMuch--;
                            }
                        }
                    }
                }
            }
        return stream;
    }


    public static int renameFile(String oldName, String newName){
        /*
        * code == 0 Nazwa zmieniona pomyslnie
        * code == 1 Plik nie został otwarty
        * code == 2 Plik nie istnieje
         */

        int code;

        code = isOpened(oldName);
        if(code == 0){
            code = 1;
        }
        else if(code == 1){
            code = 0;
        }
        if(code == 0) {
            for (int i = 0; i < mainCatalog.size(); i++) {
                if (oldName.equals(mainCatalog.get(i).fileName)) {
                    mainCatalog.get(i).fileName = newName;
                }
            }
        }
        return code;
    }


    public static int closeFile(String name){
        /*
         * Zmienna code oznacza status errorow
         * code == 0 - Plik zamkniety pomyslnie
         * code == 1 - Nie znaleziono pliku w wektorze otwartych plikow (czyt. plik nie zostal otwarty)
         * code == 2 - Plik nie istnieje
         * */
        int code = isOpened(name);

        if (code == 0) {

            int x = 0;

            for (int i = 0; i < mainCatalog.size(); i++) {
                if (name.equals(mainCatalog.get(i).fileName)) {

                    x = i;
                    break;
                }
            }

            int temp = mainCatalog.get(x).zamek.unlock();

            if (temp == 0) {

                int y = 0;

                for (int i = 0; i < Scheduler.get_running().openFiles.size(); ++i) {

                    if (name.equals(Scheduler.get_running().openFiles.get(i).file.fileName)) {

                        y = i;
                        break;
                    }
                }

                Scheduler.get_running().openFiles.remove(y);

                openedFiles.remove(whereIsOpened(name));
            }
        }

        return code;
    }


    public static int deleteFile(String name){
        /*
        * code == 0 usunieto pomyslnie
        * code == 1 plik jest otwarty nie mozna usunac otwartego pliku
        * code == 2 nie ma takiego pliku
         */
        int closed;
        char index;
        char toErase = '-';
        int code = -1;

        closed = isOpened(name);

        if(closed == 0){
            code = 1;
        }
        else if(closed == 1){
            code = 0;
        }
        else{
            code = 2;
        }

        if(code == 0){
            for(int i = 0; i < mainCatalog.size(); i++){
                code = 2;
                if(name.equals(mainCatalog.get(i).fileName)){
                    index = mainCatalog.get(i).indexBlock;
                    for(int j = 0; j < blockSize; j++) {
                        if (disk[readAddress(index)][j] != '-') {
                            toErase = disk[readAddress(index)][j];
                            for (int g = 0; g < blockSize; g++) {
                                disk[readAddress(toErase)][g] = '%';
                            }
                            bitMap[readAddress(index)] = 0;
                            bitMap[readAddress(toErase)] = 0;
                        }
                        disk[readAddress(index)][j] = '%';
                    }
                    mainCatalog.remove(i);
                    code = 0;
                    break;
                }
            }
        }
        return code;
    }


    private static int clearFile(String name){
        /*
         * code == 0 wyczyszczono pomyslnie
         * code == 1 plik jest otwarty nie mozna usunac otwartego pliku
         * code == 2 nie ma takiego pliku
         */
        char index;
        char toErase = '-';
        int code;

        index = getIndexBlock(name);
        code = isOpened(name);

        if(code == 0){
            for(int j = 0; j < blockSize; j++) {
                if (disk[readAddress(index)][j] != '-') {
                    toErase = disk[readAddress(index)][j];
                    for (int g = 0; g < blockSize; g++) {
                        disk[readAddress(toErase)][g] = '%';
                    }
                    bitMap[readAddress(toErase)] = 0;
                }
                disk[readAddress(index)][j] = '-';
            }
        }
        return code;
    }
}
