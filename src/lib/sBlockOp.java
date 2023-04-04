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
        System.out.println("Actual values from s-box:");
        System.out.println(values);
        return values;
    }

    /** extractFun
     *
     * @param values    : values from s-block
     * @param numberOf  : number of functions to extract from array
     * @return          : function from s-block
     * Extracts specified functions from s-block values.
     * Ex. if s-block has 8b entrances, then there will be 8 functions
     */
    public static HashMap<Integer,ArrayList<Integer>> extractFun(ArrayList<Integer> values, int numberOf){
        HashMap<Integer, ArrayList<Integer>> sBlockFunctions = new HashMap<>(); // return
        for(int argument=0; argument<numberOf; argument++){
            ArrayList<Integer> function = new ArrayList<>(); // each function
            int mask = 0b1 << (argument);
            for (Integer val : values) {
                int temp = val & mask;
                temp >>= argument;
                function.add(temp);
            }
            sBlockFunctions.put(argument, function);
        }
//        displayHashMapMessage(sBlockFunctions, "S-box functions:");
        return sBlockFunctions;
    }

    /**
     * @param a : first function
     * @param b : second function
     *          This method gets the distance between the two specified 256b functions.
     * */
    public static int getDistance(ArrayList<Integer> a, ArrayList<Integer> b){
        int distance =0;
        for(int i=0; i<a.size(); i++){
            if(Objects.equals(a.get(i), b.get(i))) distance ++;
        }
        return distance;
    }

    /**
     * @param a : first function
     * @param b : second function
     *          This method computes the XOR of 2 functions.
     * */
    public static ArrayList<Integer> doXOR(ArrayList<Integer> a, ArrayList<Integer> b){
        var temp = new ArrayList<Integer>();
        a = fixSize(a, b);
        temp = fixSize(a,b);
        for(int i=0; i<a.size() ; i++){
            Integer temp2 = a.get(i) ^ b.get(i);
            temp.set(i, temp2);
            System.out.println(i);
        }
        System.out.println("Temp: "+temp);

        return temp;
    }
    /**
     * Fixing the sizes of arrays, to be the same size.
     * */
    private static ArrayList<Integer> fixSize(ArrayList<Integer> a, ArrayList<Integer> b) {
        if(a.size() == 0 && b.size() > 0){
            a = fill(a, b.size(), 0);
        }else if(b.size() == 0 && a.size() > 0){
            b = fill(b, a.size(), 0);
        }
        return a;
    }

    public static ArrayList<Integer> fill(ArrayList<Integer> a, int size, int value){
        for(int i=0; i<size; i++){
            a.add(i,value);
        }
        return a;
    }

    /**
     * Creates an ArrayList of Integers from 0 to 255.
     * */
    public static ArrayList<Integer> getBasicFun(){
        ArrayList<Integer> fun = new ArrayList<>();
        for(int i=0; i<256; i++){
            fun.add(i);
        }
        return fun;
    }

    /**
     *
     * */
    public static HashMap<Integer, ArrayList<Integer>> genLinearFun(ArrayList<Integer> values){
        HashMap<Integer, ArrayList<Integer>> linFun = extractFun(values, 8);    // 8 basic linear functions

        displayHashMapMessage(linFun, "Linear functions:");

        HashMap<Integer, ArrayList<Integer>> linFunGroup = new HashMap<>();
        ArrayList<Integer> fun = new ArrayList<>();

        for(int i=0; i<256; i++){                       // all the possible combinations of the keys

            ArrayList<Integer> indexList = new ArrayList<>();   // list of functions indexes (8 elements)
            int index=0;
            for(int mask = 0b1; mask<256; mask<<=1, index++){   // for loop for getting the indexes
                if((i&mask) != 0) {
                    indexList.add(index);
                }
            }   // Here I should have an array of maximum 8 indexes
            ArrayList<Integer> temp = new ArrayList<>();
            for(Integer key : indexList){
                temp = doXOR(
                        linFun.get(key),
                        temp
                );
//                System.out.print("linfun: "+linFun.get(key));
            }
            linFunGroup.put(i, temp);
            System.out.println("temp genLinearFun"+temp);

//            linFunGroup.put(i, temp);

            // adding the computed function HashMap
        }
        return linFunGroup;
    }


    /**
     * @param message   : message to be displayed in the terminal
     * @param fun       : HashMap values to be displayed
     * */
    private static void displayHashMapMessage(HashMap<Integer, ArrayList<Integer>> fun, String message) {
        System.out.println(message);
        for(Integer val: fun.keySet()){
            System.out.print(val + ": ");
            System.out.println(fun.get(val));
        }
    }
}