package lib;

import java.util.ArrayList;
import java.util.HashMap;

import static lib.Operations.*;

public interface actions {
    /**
     * This function serves a purpose of computing the necessary values to distinguish the quality of an S-Box.
     * */
    public static void testBox(){
        /* Open the file and read the data (s-block). Return an array of Integers.*/
        ArrayList<Integer> file_data = new ArrayList<>();
        FileOp file = new FileOp();
        file.readSboxFile(file_data);

        /*Take the array of Integers and extract from them the 8 boolean functions.*/
        Operations sBox = new Operations();
        sBox.extractVal(file_data);
        sBox.extractFun();

        /*==============================================================================================================
         * In order to test the quality of the S-Box the following procedures are to be conducted:
         *
         * - determine the nonlinearity of the S-Box functions
         * - check the strict avalanche criterion (SAC)
         * - check the XOR block
         * */

        /* Determining the nonlinearity of the S-Box functions.
         * The minimal distance between each s-box function and every linear function generated. */
        HashMap<Integer, ArrayList<Integer>> linFun = genLinearFun();
        getBalance(linFun);
        displayHashMapMessage(linFun, "These are computed affine functions: ");
        /* Checking the strict avalanche criterion (SAC) of level 1 for each function.
         * Should return true/false based on the output. If computed SAC is ~50% then it satisfies the criterion.*/
        //ToDo: check the strict avalanche criterion (SAC)

        /* Checking the XOR block.
         * Return the max value (Integer) is each block. */
        //ToDo: check the XOR block
    }

}
