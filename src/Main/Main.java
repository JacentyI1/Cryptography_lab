package Main;

import lib.FileOp;

import java.util.ArrayList;
import java.util.HashMap;

import static lib.sBlockOp.*;

public class Main {
    public static void main(String[] args){
        ArrayList<Integer> file_data = new ArrayList<>();
        FileOp.readFile(file_data); // reads specified file
        ArrayList<Integer> sBox = extractVal(file_data); // extracts actual values from s-box
        var hm = extractFun(sBox); // extracts each function from s-box values
        // extracts s-box functions from the values
        HashMap<Integer, ArrayList<Integer>> linFun = genLinearFun(getBasicFun());
        getBalance(linFun);


    }
}
