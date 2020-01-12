package FileSystem;

/*
DO POPRAWY INDEKSY BLOKOW NA DWUCYFROWE!!!!!!!!!!!!!!!!!!!!!!!
 */

import java.util.Vector;

public class FileManager extends  Files{

    public Vector<Files> mainCatalog = new Vector<Files>();
    public Vector<Character> openedFiles = new Vector<Character>();

    public boolean createFile(String name){
        Files file = new Files();
        int i = 0;
        boolean created = false;
        while(created == false && i < 256){
            if(bitMap[i] == 0){
                for(int j = 0; j < blockSize/2; j+=2){
                    disk[i][j] = '-';
                    disk[i][j+1] = '1';
                }
                created = true;
                file.indexBlock = i;
                file.fileName = name;
                file.Size = 8;
                bitMap[i] = 1;
            }
            i++;
        }
        mainCatalog.add(file);
        return created;
    }

    public boolean openFile(String name){
        boolean opened = false;
        int i = 0;
        while(opened == false && i < mainCatalog.size()){
            if(name == mainCatalog.get(i).fileName) {
                openedFiles.add((char)mainCatalog.get(i).indexBlock);
                opened = true;
            }
            i++;
        }

        return opened;
    }

    public int writeFile(String name, String data){
        /*
        * Zmienna code oznacza status errorow
        * 1 - wszystko git
        * 2 - brak wolnych blokow do zapisu. Plik osiagnal maksymalna wielkosc 128 bajtow
        * 3 - plik za duzy. Przekroczyl 128 znaki == 128 bajty
        * 4 - Nie znaleziono pliku w wektorze otwartych plikow (czyt. plik nie zostal otwarty)
        * */
        int pointer = 0;
        int code = 0;
        char index = '-';
        int blocksAmount = 0;
        boolean zapisano = false;

        for(int i = 0; i < mainCatalog.size(); i++){
            if(name == mainCatalog.get(i).fileName){
                int j = 0;
                while(code != 1){
                    if(mainCatalog.get(i).indexBlock==openedFiles.get(j)){
                        index = openedFiles.get(j);
                        code = 1;
                    }
                    j++;
                }
            }
        }

        if(code == 1){
            for(int i = 0; i < blockSize/2; i+=2){
                if(disk[index][i] == '-' && disk[index][i+1] == '1'){
                    blocksAmount++;
                }
            }
            if(blocksAmount == 0){
                code = 2;
            }
            else if(blocksAmount * 16 < data.length()){
                code = 3;
            }
            else{
                for(int i = 0; i < bitMap.length; i++){
                    if(bitMap[i] == 0){
                        for(int j = 0; j < blockSize; j++){
                            disk[i][j] = data.charAt(pointer + j);
                            pointer++;
                        }
                        bitMap[i] = 1;
                    }
                }
            }
        }
        else if(code == 0){
            code = 4;
        }

        return code;
    }

}
