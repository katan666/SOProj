package FileSystem;

public class DiskManager extends Disk {

    public static void fillDisk(){
        for(int i = 0; i < diskSize/blockSize; i++){
            for(int j = 0; j < blockSize; j++){
                disk[i][j] = '%';
            }
        }
    }

    public static void setBitMap() {
        for(int i = 0; i<(diskSize/blockSize); i++){
            bitMap[i] = 0;
        }
    }

    public static void showDisk(){
        for(int i = 0; i < bitMap.length; i++){
            for(int j = 0; j < blockSize; j++){
                System.out.print(disk[i][j]);
            }
            System.out.print(' ');
            if((i+1)%4==0){
                System.out.println();
            }
        }
    }

    public static void showBitMap(){
        for(int i = 0; i<bitMap.length; i++){
            System.out.print(bitMap[i]);
        }
    }
}
