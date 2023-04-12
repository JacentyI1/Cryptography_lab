package lib;

import java.util.*;

public class Operations {
    /**
     * sbox_functions   - Functions extracted from the S-Box.<br>
     * lin_fun          - A set of linear functions computed by doing an XOR operation between every possible combination of all 8 basic functions.
     * sbox_values      - Values put inside an S-Box.
     * NL               - Nonlinearity of the S-Box functions.
     * SAC              - Strict avalanche criterion.
     * XOR_block        - //ToDo: What actually even is that?
     * */
    HashMap<Integer, ArrayList<Integer>> sbox_functions;
    HashMap<Integer, ArrayList<Integer>> lin_functions;
    HashMap<Integer, ArrayList<Integer>> base_lin;
    ArrayList<Integer> sbox_values;
    ArrayList<Integer> linear_values;
    Integer NL;
    Double SAC;
    Integer XOR_block;
    /**
     * <h3>Constructor</h3>
     * Initiating each of the parameters.<br>
     * nonlinearity = min(x);
     * SAC = x%;
     * XOR_block = max(x)
     * */
    Operations(){
        this.sbox_functions = new HashMap<>();
        this.lin_functions = new HashMap<>();
        this.base_lin = new HashMap<>();
        this.sbox_values = new ArrayList<>();
        this.linear_values = new ArrayList<>();
        this.NL = 0;
        this.SAC = 100.00;
        this.XOR_block = 2;
    }

    /**
     * @param message - message to be displayed in the terminal
     * */
    void displayHashMap(HashMap<Integer, ArrayList<Integer>> param,String message) {
        System.out.println(message);
        for(Integer val: param.keySet()){
            System.out.print(val + ": ");
            System.out.println(param.get(val));
        }
    }
    /**
     * This function gets rid of the '0x00' in the sBox.
     * */
    public void extractVal(ArrayList<Integer> file_data) {
        for(int i=0;i<file_data.size();i+=2){
            this.sbox_values.add(file_data.get(i));
        }
    }
    /**
     * Extracting boolean function from the sBox values.
     * It is done by using a mask to separate specific bits of the S-Box values and then appending them into the array creating a boolean function.
     * */
    public void extractSboxFun() {
        for(int i=0; i < 8; i++){
            ArrayList<Integer> function = new ArrayList<>();
            int m = 0b1 << i;
            for(Integer val : this.sbox_values){
                int temp = val & m;
                temp >>= i;
                function.add(temp);
            }
            sbox_functions.put(i, function);
        }
    }
    /**
     * Works like the function above, however it is targeted for the generated affine functions.
     * */
    public void extractLinFun() {
        for(int i=0; i < 8; i++){
            ArrayList<Integer> function = new ArrayList<>();
            int m = 0b1 << i;
            for(Integer val : this.linear_values){
                int temp = val & m;
                temp >>= i;
                function.add(temp);
            }
            base_lin.put(i, function);
        }
    }
    /**
     * Generates a set of affine functions.
     * */
    public void genLinear() {
        for(int i=0; i<256; i++) linear_values.add(i);
    }
    /**
     * Checks balance of specified function and displays if the functions are not linear.
     * */
    public void checkBalance(HashMap<Integer, ArrayList<Integer>> lin_functions) {
        boolean linear = true;
        for(Integer function : lin_functions.keySet()){
            int counter = 0;
            for(Integer bit : lin_functions.get(function)){
                if(bit == 1) counter++;
            }
            if(counter!=128) linear = false;
        }
        System.out.println("Functions are linear: "+linear);
    }
    /**
     * Puts newly computed functions inside a linear_functions HashMap.
     * */
    public void genSet() {
        for(int i = 1; i<256; i++){
            ArrayList<Integer> temp = new ArrayList<>();
            fillWith(temp, 0);
            for(int j=0; j<8; j++){
                int mask = 0b1 << j;
                int use_function = (i & mask) >> j;
                if(use_function==1){
                    temp = doXorHm(this.base_lin, temp, j);
                }
            }
            lin_functions.put(i, temp);
        }
        ArrayList<Integer> ones = new ArrayList<>();
        fillWith(ones, 1);
        this.displayHashMap(lin_functions,"Linear functions 255:");
        for(int key =256; key<511; key++){
            lin_functions.put(key, doXorHm(lin_functions, ones, key-255));

        }
    }
    /**
     * Does XOR operation on Hashmap functions.
     * */
    private ArrayList<Integer> doXorHm(HashMap<Integer, ArrayList<Integer>> base, ArrayList<Integer> temp, int j) {
        ArrayList<Integer> temp2 = new ArrayList<>();
        for(int i =0; i< base.get(j).size(); i++){
            temp2.add(temp.get(i) ^ base.get(j).get(i));
        }
        return temp2;
    }
    /**
     * Fills an array with 256 same values.
     * */
    private void fillWith(ArrayList<Integer> temp, int i) {
        for (int j = 0; j < 256; j++) temp.add(i);
    }
}

/*

    *//**
 * @param list - ArrayList that represents a function
 *
 * getBalance returns the number of '1' in the arraylist.
 * *//*
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

    *//**
 * @param a first function
 * @param b second function
 *          This method computes the XOR of 2 functions.
 * *//*
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
    *//**@param a : first array
 * @param b : second array
 * Increasing the size of the shorter array to match the larger one.
 * *//*
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

    *//**
 * Creates an ArrayList of Integers from 0 to 255.
 * *//*
    public void getBasicFun(){
        ArrayList<Integer> fun = new ArrayList<>();
        for(int i=0; i<256; i++){
            fun.add(i);
        }
        extractVal(fun);
    }
    *//**
 * Generates 8 basic 256b linear functions.
 * *//*
    public HashMap<Integer, ArrayList<Integer>> genLinearFun(){
        HashMap<Integer, ArrayList<Integer>> linFunGroup = new HashMap<>();

        ArrayList<Integer> fun1 = getBasicFun();
        ArrayList<Integer> fun2 = getBasicFun();
        HashMap<Integer, ArrayList<Integer>> linFun1 = this.extractLinFun(fun1);    // 8 basic linear functions
        HashMap<Integer, ArrayList<Integer>> linFun2 = this.extractLinFun(fun2);
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
    *//**
 * This function returns every possible XOR operation between the specified linear functions.
 *
 * @return*//*
    private static HashMap<Integer, ArrayList<Integer>> putLinFun(HashMap<Integer, ArrayList<Integer>> linFun, HashMap<Integer, ArrayList<Integer>> linFunGroup, int i, boolean negated) {
        ArrayList<Integer> i_list = indexList(i); //
        ArrayList<Integer> temp = new ArrayList<>();
        for(Integer key : i_list){
            temp = doXOR(linFun.get(key), temp);
        }
        if(!negated) linFunGroup.put(i, temp);
        else linFunGroup.put(i+255,temp);
        return linFunGroup;
    }
    *//**
 * indexList creates a list of indexes that correlate directly to the specified functions.
 * @returns an array of 8 indexes at most
 * *//*
    private static ArrayList<Integer> indexList(int i) {
        ArrayList<Integer> i_list = new ArrayList<>();
        for(int mask = 0b1, index =0; index<8; mask<<=1, index++){
            if((i &mask) != 0) {
                i_list.add(index);
            }
        }   // Here I should have an array of maximum 8 indexes
        return i_list;
    }
    */
/**
 * @param a : first function
 * @param b : second function
 *          This method gets the distance between the two specified 256b functions.
 * *//*
    public static int getDistance(ArrayList<Integer> a, ArrayList<Integer> b){
        int distance =0;
        for(int i=0; i<a.size(); i++){
            if(Objects.equals(a.get(i), b.get(i))) distance ++;
        }
        return distance;
    }*/