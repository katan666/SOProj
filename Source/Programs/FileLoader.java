package Programs;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

public class FileLoader {
    public static String readAllBytesFromFile(String filePath)
    {
        String content = "";
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return content;

    }

    public static Vector<Short> readAllBytesFromFileToShortVec(String filePath){
        Vector<Short> vector = new Vector<>();
        String string = readAllBytesFromFile(filePath);
        for(char e : string.toCharArray()){
            vector.add((short)e);
        }
        return vector;
    }

    public static void main(String[] args) throws IOException{
        System.out.println(readAllBytesFromFileToShortVec("Source/Programs/ciagLiczbKwadratowych.txt"));
    }
}
