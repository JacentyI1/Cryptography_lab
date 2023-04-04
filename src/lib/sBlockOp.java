package lib;

import java.util.*;

public class sBlockOp {
    sBlockOp(){}


    // Extract actual values from sBlock
    /**
     * @param sBlock : a provided s-block
     * S-block is filled with '0000' bytes. Therefore for easier access to the data,
     * actual values of s-block are extracted into an ArrayList<Integer>.
     */
    public static ArrayList<Integer> extractVal(ArrayList<Integer> sBlock){
        ArrayList<Integer> values = new ArrayList<>();
        for(int i=0; i< 512; i+=2){
            values.add( sBlock.get(i) );
        }
        System.out.println(values);
        return values;
    }

    /** extractFun
     *
     * @param values    : values from s-block
     * @return          : function from s-block
     * Extracts specified functions from s-block values.
     * Ex. if s-block has 8b entrances, then there will be 8 functions
     */
    public static HashMap<Integer,ArrayList<Integer>> extractFun(ArrayList<Integer> values){
        HashMap<Integer, ArrayList<Integer>> sBlockFunctions = new HashMap<>(); // return

        for(int argument=0; argument<8; argument++){
            ArrayList<Integer> function = new ArrayList<>(); // each function
            int mask = 0b1 << (argument);
            for (Integer val : values) {
                int temp = val & mask;
                temp >>= argument;
                function.add(temp);
            }
            sBlockFunctions.put(argument, function);
        }
//        System.out.println(sBlockFunctions);
        for(Integer val: sBlockFunctions.keySet()){
            System.out.println(sBlockFunctions.get(val));
        }
        // deleteArray()
        return sBlockFunctions;
    }

    /**
     * @param a - first function
     * @param b - second function
     *          This method gets the distance between the two specified 256b functions.
     * */
    public static int getDistance(Integer a, Integer b){
        int cnt =0;
        int mask = 0b1;
        for(int i=0; i<256; i++, mask<<=1){
            if((a & mask) != (b & mask)) cnt++;
        }
        return cnt;
    }
}