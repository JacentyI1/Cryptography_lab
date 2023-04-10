package lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class FileOp {
    String filePath;
    public FileOp() {
        filePath = "src/res/sbox_08x08_20130110_011319_02.SBX";
    }

    /**
     * This method extracts Integers from a S-Box file.
     * @param container - container for the file data
     * */
    public void readSboxFile(ArrayList<Integer> container){
        try{
            FileInputStream read = new FileInputStream(filePath);
            int byt;
            int counter=0;
            while((byt = read.read()) != -1){
                container.add(byt);
                counter ++;
            }
            System.out.println("Read data:");
            System.out.println(container);
            read.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}