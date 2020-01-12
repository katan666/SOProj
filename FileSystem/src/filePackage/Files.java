package filePackage;

import diskPackage.DiskManager;

public class Files extends DiskManager {
    public String fileName;
    public int indexBlock;
    public int Size = 0;
    public int readPointer;
    public int writePointer;

    public Files(){
        this.readPointer = 0;
        this.writePointer = 0;
    }

    public Files(String name){
        this.fileName = name;
        this.readPointer = 0;
        this.writePointer = 0;
    }
}
