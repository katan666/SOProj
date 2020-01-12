package FileSystem;

public class DiskManager extends Disk {

    public static char[][] fillDisk(){
        for(int i = 0; i < diskSize/blockSize; i++){
            for(int j = 0; j < blockSize; j++){
                disk[i][j] = '%';
            }
        }
        return disk;
    }

    public static int[] setBitMap() {
        for(int i = 0; i<(diskSize/blockSize); i++){
            bitMap[i] = 0;
        }
        return bitMap;
    }

    public static void showDisk(char[][] disk){
        for(int i = 0; i < bitMap.length; i++){
            for(int j = 0; j < blockSize; j++){
                System.out.println(disk[i][j]);
            }
        }
    }
}
