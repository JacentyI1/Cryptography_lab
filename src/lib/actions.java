package lib;

import java.util.ArrayList;
import java.util.HashMap;

import static lib.Operations.*;

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
        /*Display*/
        /*System.out.println("Read data:");
        System.out.println(file_data);*/

        /*Take the array of Integers and extract from them the 8 boolean functions.*/
        Operations sBox = new Operations();
        sBox.extractVal(file_data);
        sBox.extractSboxFun();
        /*Display*/
        /*System.out.println("Values extracted: "+sBox.sbox_values.size());
        System.out.println("Sbox values:\n"+sBox.sbox_values);
        sBox.displaySboxFunctions("Extracted sbox functions:");*/

        /*Create a set of 8 affine functions.*/
        sBox.genLinear();
        sBox.extractLinFun();
        /*Display*/
        /*sBox.checkBalance(sBox.lin_functions);
        System.out.println(sBox.linear_values);
        sBox.displayBaseFunctions("First set of linear functions:");*/

        /*Create a set of 255 affine functions based on the 8 generated.*/
        sBox.genSet();
        sBox.checkBalance(sBox.lin_functions);
        sBox.negBase();
//        sBox.displayHashMap(sBox.lin_functions,"These are generated linear functions:");

        /*Repeat but with negated functions and append it to the previous set.*/
//        sBox.getNeg();


        /* Determining the nonlinearity of the S-Box functions.
         * The minimal distance between each s-box function and every linear function generated. */
//        HashMap<Integer, ArrayList<Integer>> linFun = genLinearFun();
//        getBalance(linFun);
//        displayHashMapMessage(linFun, "These are computed affine functions: ");
        /* Checking the strict avalanche criterion (SAC) of level 1 for each function.
         * Should return true/false based on the output. If computed SAC is ~50% then it satisfies the criterion.*/
        //ToDo: check the strict avalanche criterion (SAC)

        /* Checking the XOR block.
         * Return the max value (Integer) is each block. */
        //ToDo: check the XOR block
    }

}
