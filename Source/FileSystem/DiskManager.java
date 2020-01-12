package FileSystem;

public class DiskManager extends Disk {

    public char[][] fillDisk(){
        for(int i = 0; i < diskSize/blockSize; i++){
            for(int j = 0; j < blockSize; j++){
                disk[i][j] = '%';
            }
        }
        return disk;
    }

    public int[] setBitMap() {
        for(int i = 0; i<(diskSize/blockSize); i++){
            bitMap[i] = 0;
        }
        return bitMap;
    }


}
