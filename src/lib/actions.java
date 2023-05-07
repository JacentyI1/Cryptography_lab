package lib;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;

public interface actions {
    /**
     * This function serves a purpose of computing the necessary values to distinguish the quality of an S-Box.
     *==============================================================================================================
     *          * In order to test the quality of the S-Box the following procedures are to be conducted:
     *          *
     *          * - determining the nonlinearity of the S-Box functions
     *          * - checking the strict avalanche criterion (SAC)
     *          * - checking the XOR block
     *          *
     * */
    public static void testBox(){
        /* Open the file and read the data (s-block). Return an array of Integers.*/
        ArrayList<Integer> file_data = new ArrayList<>();
        FileOp file = new FileOp();
        file.readSboxFile(file_data);

        /*Take the array of Integers and extract from them the 8 boolean functions.*/
        Operations sBox = new Operations();
        sBox.extractVal(file_data);
        sBox.extractSboxFun();

        /*Create a set of 8 affine functions.*/
        sBox.genLinear();
        sBox.extractLinFun();

        /*Create a set of 255 affine functions based on the 8 generated.*/
        sBox.genSet();
        sBox.computeHamming();
        System.out.println("Linearity: \n"+sBox.NL);

        /*Computing SAC*/
        sBox.computeSAC();
//        sBox.displaySAC(sBox.SAC, "SAC results:");
        sBox.getXORProfileMax(sBox.computeXOProfile());
    }
}
