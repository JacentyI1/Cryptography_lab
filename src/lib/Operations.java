package lib;

import java.text.DecimalFormat;
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
    ArrayList<Integer> NL;
    HashMap<Integer, ArrayList<Double>> SAC;
    ArrayList<ArrayList<Integer>> XOR_block;
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
        this.NL = new ArrayList<>();
        this.SAC = new HashMap<>();
        this.XOR_block = new ArrayList<>();
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
     * @param message - message to be displayed in the terminal
     * */
    void displaySAC(HashMap<Integer, ArrayList<Double>> param,String message) {
        System.out.println(message);
        DecimalFormat df = new DecimalFormat("#0.00");
        for(Integer key: param.keySet()){
            System.out.print("Function "+key+": [ ");
            for(Double val : param.get(key)){
                System.out.print(df.format(val)+"%; ");
            }
            System.out.println("]");
        }
    }
    /**
     * Displays the rate of non-linearity of S-Box functions.
     * */
    void displayNonLinearity(ArrayList<Double> arr){
        DecimalFormat df = new DecimalFormat("#,##0.00");
        for(Double val : arr){
            String formatedValue = df.format(val);
            System.out.print(formatedValue);
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
//        this.displayHashMap(lin_functions,"Linear functions 255:");
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
        if(temp.size()==0)
            for (int j = 0; j < 256; j++) temp.add(i);
        else if(temp.size()==256)
            for (int j = 0; j < 256; j++) temp.set(j, i);
    }
    /**
     * Computing the Hamming distance by XORing the S-Box functions and the generated affine functions from @method genSet()
     * */
    public void computeHamming() {
        for(Integer fun_key : sbox_functions.keySet()){
            ArrayList<Integer> distance = new ArrayList<>();
            for(Integer key : lin_functions.keySet()){
                ArrayList<Integer> ones = new ArrayList<>();
                fillWith(ones, 0);
                distance.add(countOnes(doXorHm(sbox_functions, lin_functions.get(key), fun_key)));
            }
            this.NL.add(getMin(distance));
        }
    }
    /**
     * Returns the minimal value of specified ArrayList of Integers.
     * */
    private Integer getMin(ArrayList<Integer> distance) {
        int comparator = distance.get(0);
        for(int i=1; i<distance.size()-1; i++){
            if(comparator > distance.get(i)) comparator = distance.get(i);
        }
        return comparator;
    }
    /**
     * Counts the number of '1' (bits) in the specified ArrayList.
     * */
    private int countOnes(ArrayList<Integer> arr) {
        int counter = 0;
        for(int i=0; i<arr.size(); i++){
            if(arr.get(i) == 1) counter++;
        }
        return counter;
    }
    /**
     * Computes the strict avalanche criterion for every function from S-Box.
     * Returns a HashMap of Double ArrayLists as well as sets the SAC parameter's values.
     * */
    public void computeSAC() {
        ArrayList<Integer> compare = new ArrayList<>();
        for(Integer key : sbox_functions.keySet()){
            ArrayList<Double> sac_result  = new ArrayList<>();
            for(int i=0; i<8; i++){
                fillWith(compare, 0);
                int next_index = (int)Math.pow(2.00, (double)i);
                for(int j=0; j<sbox_functions.get(key).size() - next_index; j += next_index*2){
                    compare.set(j, sbox_functions.get(key).get(j+next_index));
                    compare.set(j+next_index, sbox_functions.get(key).get(j));
                }
                ArrayList<Integer> result = doXorHm(sbox_functions, compare, i);
                double input = (double)countOnes(result)/256.00*100.00;
                sac_result.add(input);
            }
            SAC.put(key, sac_result);
        }
        double result = 0.00;
        for(Integer key : SAC.keySet()){
            for(Double arr : SAC.get(key)){
                result += arr;
            }
        }
        result /= 64.00;
        DecimalFormat df = new DecimalFormat("#0.00");
        System.out.println("SAC: "+df.format(result));
    }
    /**
     * Computes XOR-block values for the S-Block.
     * */
    public void computeXORprofile() {
        int input1;
        int input2;
        int output1;
        int output2;
        for(input1=0; input1 < 256; input1++){
            output1 = sbox_values.get(input1);
            for(input2=0; input2 < 256; input2++){
                output2 = sbox_values.get(input2);
            }
        }
    }
}