package lib;

import java.util.*;

public class Operations {
    /**
     *
     * */
    HashMap<Integer, ArrayList<Integer>> sbox_functions;
    HashMap<Integer, ArrayList<Integer>> lin_fun;
    ArrayList<Integer> sbox_values;
    /**
     *
     * */
    Integer NL;
    Double SAC;
    Integer XOR_block;
    Operations(){
        this.sbox_functions = new HashMap<>();
        this.lin_fun = new HashMap<>();
        this.sbox_values = new ArrayList<>();
        this.NL = 0;
        this.SAC = 100.00;
        this.XOR_block = 2;
    }
    /**
     * S-block is filled with '0000' bytes. Therefore for easier access to the data,
     * actual values of s-block are extracted into an ArrayList<Integer> in the form of integers.
     * @param sBlock a provided s-block
     * @return Integer values inside an S-Box.
     */
    public ArrayList<Integer> extractVal(ArrayList<Integer> sBlock){
        ArrayList<Integer> values = new ArrayList<>();
        for(int i=0; i< 512; i+=2){
            values.add( sBlock.get(i) );
        }
        System.out.println("Actual values from s-box:");
        System.out.println(values);
        return values;
    }
    /** extractFun
     * @return          function from s-block
     * Extracts specified functions from s-block values.
     * Ex. if s-block has 8b entrances, then there will be 8 functions
     */
    public HashMap<Integer,ArrayList<Integer>> extractFun(){
        HashMap<Integer, ArrayList<Integer>> sBlockFunctions = new HashMap<>(); // return
        for(int argument=0; argument< this.sbox_values.size(); argument++){
            ArrayList<Integer> function = new ArrayList<>(); // each function
            int mask = 0b1 << (argument);
            for (Integer val : this.sbox_values) {
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
     * @param list - ArrayList that represents a function
     *
     * getBalance returns the number of '1' in the arraylist.
     * */
    public static void getBalance(HashMap<Integer, ArrayList<Integer>> list){
        boolean nonlinearity = false;
        for (Integer fun: list.keySet()) {
            int counter =0;
            for (Integer bit : list.get(fun)){
                if(bit==1) counter++;
            }
            if(counter != 128) {
                System.out.println("Function " + fun + " is not balanced. Amount of '1': " + counter);
                nonlinearity = true;
            }
//            else System.out.println("balanced");
        }
        if(!nonlinearity) System.out.println("Every function in the set is linear! :D");
    }

    /**
     * @param a first function
     * @param b second function
     *          This method computes the XOR of 2 functions.
     * */
    public static ArrayList<Integer> doXOR(ArrayList<Integer> a, ArrayList<Integer> b){
        ArrayList<Integer> temp = new ArrayList<Integer>();
        temp = fixSize(a,b);
//        System.out.println("A size: "+a.size()+ "A"+a);
//        System.out.println("B size: "+b.size()+ "B"+b);
        for(int i=0; i<a.size() ; i++){
            Integer temp2 = a.get(i) ^ b.get(i);
            temp.set(i, temp2);
//            System.out.println(i);
        }
//        System.out.println("X: "+temp);
        return temp;
    }
    /**@param a : first array
     * @param b : second array
     * Increasing the size of the shorter array to match the larger one.
     * */
    private static ArrayList<Integer> fixSize(ArrayList<Integer> a, ArrayList<Integer> b) {
        if(a.size() == b.size()){
//            System.out.println("Size is the same.");
            return a;
        }
        else if(a.size() == 0 && b.size() > 0 ){
//            System.out.println("Fixed size a");
            fill(a, b.size(), 0);
            return a;
        }else if(b.size() == 0 && a.size() > 0){
//            System.out.println("Fixed size b");
            fill(b, a.size(), 0);
            return b;
        }else {
//            System.out.println("returned 'a' in filling function");
            ArrayList<Integer> temp = new ArrayList<>();
            fill(temp, a.size(), 0);
            return temp;
        }
    }
    public static void fill(ArrayList<Integer> a, int size, int value){
        if(a.isEmpty()) {
            for(int i=0; i<size; i++){
                a.add(i,value);
            }
        }else{
            for(int i=0; i<size; i++){
                a.set(i,value);
            }
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
     * Generates 8 basic 256b linear functions.
     * */
    public static HashMap<Integer, ArrayList<Integer>> genLinearFun(){
        HashMap<Integer, ArrayList<Integer>> linFunGroup = new HashMap<>();

        ArrayList<Integer> fun1 = getBasicFun();
        ArrayList<Integer> fun2 = getBasicFun();
        HashMap<Integer, ArrayList<Integer>> linFun1 = extractFun(fun1);    // 8 basic linear functions
        HashMap<Integer, ArrayList<Integer>> linFun2 = extractFun(fun2);
        linFun2 = negateFun(linFun2);
//        displayHashMapMessage(linFun2, "Negated functions");

        for(int i=1; i<256; i++){
            putLinFun(linFun1, linFunGroup, i, false);
            displayHashMapMessage(linFunGroup, "Linear functions set");
        }

        for(int i=1; i<256; i++){
            linFunGroup = putLinFun(linFun2, linFunGroup, i, true);

        }

        return linFunGroup;
    }
    private static HashMap<Integer, ArrayList<Integer>> negateFun(HashMap<Integer, ArrayList<Integer>> functions){
        HashMap<Integer, ArrayList<Integer>> temp_fun = new HashMap<>();
        for(Integer fun : functions.keySet()){
            ArrayList<Integer> temp = new ArrayList<>();
            fixSize(temp, functions.get(fun));
            fill(temp, functions.get(fun).size(), 1);
            temp_fun.put(fun, doXOR(temp, functions.get(fun))) ;
        }
//        displayHashMapMessage(temp_fun, "Temp_fun: ");
        return temp_fun;
    }
    /**
     * This function returns every possible XOR operation between the specified linear functions.
     *
     * @return*/
    private static HashMap<Integer, ArrayList<Integer>> putLinFun(HashMap<Integer, ArrayList<Integer>> linFun, HashMap<Integer, ArrayList<Integer>> linFunGroup, int i, boolean negated) {
        ArrayList<Integer> i_list = indexList(i);
        ArrayList<Integer> temp = new ArrayList<>();
        for(Integer key : i_list){
            temp = doXOR(linFun.get(key), temp);
//            System.out.println("Temp key "+key+": "+temp);
        }
//        System.out.println("put at "+i+": " +temp);
        if(!negated) linFunGroup.put(i, temp);
        else linFunGroup.put(i+255,temp);
//        System.out.println("Linear function nr "+i+": "+temp);
//        System.out.println("Linear function nr " + i + ": " + Integer.toBinaryString(i));
        return linFunGroup;
    }
    /**
     * indexList creates a list of indexes that correlate directly to the specified functions.
     * @returns an array of 8 indexes at most
     * */
    private static ArrayList<Integer> indexList(int i) {
        ArrayList<Integer> i_list = new ArrayList<>();
        for(int mask = 0b1, index =0; index<8; mask<<=1, index++){
            if((i &mask) != 0) {
                i_list.add(index);
            }
        }   // Here I should have an array of maximum 8 indexes
        return i_list;
    }
    /**
     * @param message   : message to be displayed in the terminal
     * @param fun       : HashMap values to be displayed
     * */
    static void displayHashMapMessage(HashMap<Integer, ArrayList<Integer>> fun, String message) {
        System.out.println(message);
        for(Integer val: fun.keySet()){
            System.out.print(val + ": ");
            System.out.println(fun.get(val));
        }
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
}