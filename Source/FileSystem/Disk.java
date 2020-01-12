package FileSystem;

public class Disk {
    /*
    bitMap to tablica zajetosci blokow
    element bitmapy to jakby pierwszy bit odpowiadajacego bloku.
    np: bitMap[4] = 0; oznacza ze disk[4] jest pusty.
        bitMap[4] = 1; oznacza ze disk[4] jest zajety.
     */
    public static int diskSize = 256;
    public static int blockSize = 8;
    public static int[] bitMap = new int[diskSize/blockSize];
    public static char[][] disk = new char[diskSize/blockSize][blockSize];


}
