package com.main.virtualmemorymanagementsimulation;

import java.util.ArrayList;
import java.util.LinkedList;

public class SecondChanceFIFOMMU extends MMU{
    private final ArrayList<Integer> secondChance;
    private final ArrayList<Boolean> checkBits;
    public int findFillInFrame(){
        if (secondChance.size()==size){
            int j=0;
            while (checkBits.get(j)&&j<size) {
                checkBits.set(j,false);
                j++;
            }
            if (j==size)j=0;
            checkBits.remove(j);
            return secondChance.remove(j);
        }else{
            secondChance.add(secondChance.size());
            checkBits.add(false);
            return secondChance.size()-1;
        }

    }
    public void checkIn(int i){
        for (int j = 0; j < secondChance.size(); j++) {
            if (secondChance.get(j)==i){
                checkBits.set(j,true);
                return;
            }
        }
    }
    public SecondChanceFIFOMMU(int size, int minimumFrames) {
        super(size, minimumFrames);
        secondChance=new ArrayList<>();
        checkBits=new ArrayList<>();
    }
}
