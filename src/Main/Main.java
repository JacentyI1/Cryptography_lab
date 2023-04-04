package Main;

import lib.FileOp;

import java.util.ArrayList;

import static lib.sBlockOp.extractFun;
import static lib.sBlockOp.extractVal;

public class Main {
    public static void main(String[] args){
        ArrayList<Integer> file_data = new ArrayList<>();
        FileOp.readFile(file_data); // reads specified file
        ArrayList<Integer> sBox = extractVal(file_data); // extracts actual values from sbox
        var hm = extractFun(sBox);
        // extracts sbox functions from the values

    }
}
