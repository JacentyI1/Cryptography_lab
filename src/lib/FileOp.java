package lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class FileOp {
    public FileOp() {}

    // read binary file with sBlock
    public static void readFile(ArrayList<Integer> container){
        try{
            FileInputStream read = new FileInputStream(
                    "src/res/sbox_08x08_20130110_011319_02.SBX");

            int byt;
            int counter=0;
            while((byt = read.read()) != -1){
                container.add(byt);
                counter ++;
            }
            System.out.println("Read data:");
            System.out.println(container);
//            System.out.println("counter:"+ counter);
            read.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void readFile(byte[] container){
        try{
            FileInputStream read = new FileInputStream(
                    "src/res/sbox_08x08_20130110_011319_02.SBX");

            int byt;
            int counter=0;
            while((byt = read.read()) != -1){
                container[counter] = (byte) byt;
                counter ++;
            }
            System.out.println("Read data:");
            System.out.println(container);
//            System.out.println("counter:"+ counter);
            read.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}