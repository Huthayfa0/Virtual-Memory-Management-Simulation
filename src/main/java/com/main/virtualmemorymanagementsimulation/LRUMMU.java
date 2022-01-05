package com.main.virtualmemorymanagementsimulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class LRUMMU extends MMU{
    private final ArrayList<Integer> list;
    public int findFillInFrame(){
        if (list.size()==size){
            int f= list.remove(0);
            list.add(f);
            return f;
        }else{
            list.add(list.size());
            return list.size()-1;
        }

    }
    public void checkIn(int i){
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j)==i){
                list.remove(j);
                list.add(i);
                return;
            }
        }
    }
    public LRUMMU(int size, int minimumFrames) {
        super(size, minimumFrames);
        list=new ArrayList<>();
    }
}
