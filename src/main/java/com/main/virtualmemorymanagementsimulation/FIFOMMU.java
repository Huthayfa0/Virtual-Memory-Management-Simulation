package com.main.virtualmemorymanagementsimulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FIFOMMU extends MMU{
    private final Queue<Integer> queue;
    public int findFillInFrame(){
        if (queue.size()==size){
            assert !queue.isEmpty();
            int f= queue.poll();
            queue.offer(f);
            return f;
        }else{
            queue.add(queue.size());
            return queue.size()-1;
        }

    }
    public void checkIn(int i){
    }
    public FIFOMMU(int size, int minimumFrames) {
        super(size, minimumFrames);
        queue=new LinkedList<>();
    }
}
