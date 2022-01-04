package com.main.virtualmemorymanagementsimulation;

import java.util.concurrent.*;

public class MMU implements Runnable {
    private final Frame[] frames;
    private final Boolean[] secondChanceClock;
    private final int size;
    private int i=0;
    private final BlockingQueue<Frame> frameQueue;
    public MMU(int size) {
        this.size=size;
        frames=new Frame[size];
        secondChanceClock=new Boolean[size];
        frameQueue=new LinkedBlockingQueue<>();
    }
    public void requestFrame(Frame frame) throws InterruptedException {
        frameQueue.put(frame);
    }
    @Override
    public void run() {
        try {
            while (true) {
                if (frameQueue.isEmpty()) {
                    Thread.sleep((int) (Math.random() * 10000));
                    continue;
                }
                Frame frame = frameQueue.take();
                int found=-1;
                for (int j = 0; j < size; j++) {
                    if (frames[j]==null)break;
                    if (frame.equals(frames[j])){
                        found=j;
                    }
                }
                if (found==-1){
                    while (secondChanceClock[i]){
                        secondChanceClock[i]=false;
                        i=(i+1)%size;
                    }
                    found=i;
                    frames[i]=frame;
                }
                secondChanceClock[found]=true;
                //lock release

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
