package FileSystem;

//import Locks.*;

public class Files extends DiskManager {
    public String fileName;
    public char indexBlock;
    public int Size;
    
    //public Lock zamek;

    public Files() {
        //zamek = new Lock();
    }

    public Files(String name) {
        this.fileName = name;
        //zamek = new Lock();
    }
}
