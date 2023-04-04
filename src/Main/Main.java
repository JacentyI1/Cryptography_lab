package Main;

import lib.FileOp;

import java.util.ArrayList;

import static lib.sBlockOp.*;

public class Main {
    public static void main(String[] args){
        ArrayList<Integer> file_data = new ArrayList<>();
        FileOp.readFile(file_data); // reads specified file
        ArrayList<Integer> sBox = extractVal(file_data); // extracts actual values from s-box
        var hm = extractFun(sBox, 8); // extracts each function from s-box values
        // extracts s-box functions from the values
        genLinearFun(getBasicFun());

    }
}
