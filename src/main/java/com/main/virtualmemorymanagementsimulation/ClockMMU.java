package com.main.virtualmemorymanagementsimulation;

public class ClockMMU extends MMU{
    private int i=0;
    private final boolean[] clock;
    public int findFillInFrame(){
        while (clock[i]){
            clock[i]=false;
            i=(i+1)%size;
        }
        return i;
    }
    public void checkIn(int i){
        clock[i]=true;
    }
    public ClockMMU(int size, int minimumFrames) {
        super(size, minimumFrames);
        clock=new boolean[size];
    }
}
