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
     * @return          : function from s-block
     * Extracts specified functions from s-block values.
     * Ex. if s-block has 8b entrances, then there will be 8 functions
     */
    public static HashMap<Integer,ArrayList<Integer>> extractFun(ArrayList<Integer> values){
        HashMap<Integer, ArrayList<Integer>> sBlockFunctions = new HashMap<>(); // return
        for(int argument=0; argument< values.size(); argument++){
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
     * @param list - ArrayList that represents a function
     *
     * getBalance returns the number of '1' in the arraylist.
     * */
    public static void getBalance(HashMap<Integer, ArrayList<Integer>> list){


        for (Integer fun: list.keySet()) {
            int counter =0;
            for (Integer bit : list.get(fun)){
                if(bit==1) counter++;
            }
            if(counter != 128) System.out.println("Function "+fun+": "+counter);    //ToDo: Write try catch
        }
    }

    /**
     * @param a : first function
     * @param b : second function
     *          This method computes the XOR of 2 functions.
     * */
    public static ArrayList<Integer> doXOR(ArrayList<Integer> a, ArrayList<Integer> b){
        ArrayList<Integer> temp = new ArrayList<Integer>();
        temp = fixSize(a,b);
        for(int i=0; i<a.size() ; i++){
            Integer temp2 = a.get(i) ^ b.get(i);
            temp.set(i, temp2);
//            System.out.println(i);
        }
//        System.out.println("Temp: "+temp);
        return temp;
    }
    /**@param a : first array
     * @param b : second array
     * Increasing the size of the shorter array to match the larger one.
     * */
    private static ArrayList<Integer> fixSize(ArrayList<Integer> a, ArrayList<Integer> b) {
        if(a.size() == 0 && b.size() > 0){
            fill(a, b.size(), 0);
        }else if(b.size() == 0 && a.size() > 0){
            fill(b, a.size(), 0);
        }
        return a;
    }

    public static void fill(ArrayList<Integer> a, int size, int value){
        for(int i=0; i<size; i++){
            a.add(i,value);
        }
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
     * @param values : basic linear 256b functions
     * Generates 8 basic 256b linear functions.
     * */
    public static HashMap<Integer, ArrayList<Integer>> genLinearFun(ArrayList<Integer> values){
        HashMap<Integer, ArrayList<Integer>> linFun = extractFun(values);    // 8 basic linear functions
        displayHashMapMessage(linFun, "Linear functions:");
        HashMap<Integer, ArrayList<Integer>> linFunGroup = new HashMap<>();
        ArrayList<Integer> fun = new ArrayList<>();
        for(int i=1; i<256; i++){                       // all the possible combinations of the keys
            ArrayList<Integer> i_list = new ArrayList<>();   // list of functions indexes (8 elements)
            indexList(i, i_list);
            ArrayList<Integer> temp = new ArrayList<>();
            putLinFun(linFun, linFunGroup, i, i_list, temp);
        }
        return linFunGroup;
    }

    /**
     * @param linFun - The 8 standard linear functions.
     * @param i - variation of the linear functions in XOR operation between them
     * @param i_list - i turned into bit array for easier usage
     * @param linFunGroup - a group of linear function
     * @param temp - temporary array
     * */
    private static void putLinFun(HashMap<Integer, ArrayList<Integer>> linFun, HashMap<Integer, ArrayList<Integer>> linFunGroup, int i, ArrayList<Integer> i_list, ArrayList<Integer> temp) {
        for(Integer key : i_list){
            temp = doXOR(linFun.get(key), temp);
        }
        linFunGroup.put(i, temp);
        System.out.println("Linear function nr "+i+": "+temp);
        System.out.println("Linear function nr " + i + ": " + Integer.toBinaryString(i));
    }

    private static void indexList(int i, ArrayList<Integer> i_list) {
        int index=0;
        for(int mask = 0b1; mask<256; mask<<=1, index++){   // for loop for getting the indexes
            if((i &mask) != 0) {
                i_list.add(index);
            }
        }   // Here I should have an array of maximum 8 indexes
    }


    /**=============DISPLAY=======================
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