package com.main.virtualmemorymanagementsimulation;

import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MMU extends Thread {
    private final HashMap<Integer,Integer> countPageFaults;
    private final Frame[] frames;
    private final boolean[] secondChanceClock;
    long readCycles=300;
    private final int size;
    private int i=0;

    private final BlockingQueue<Frame> frameQueue;
    private final BlockingQueue<Condition> conditionsQueue;
    private final BlockingQueue<Lock> locksQueue;
    private volatile Scheduler scheduler=null;
    public MMU(int size) {
        this.size=size;
        frames=new Frame[size];
        secondChanceClock=new boolean[size];
        frameQueue=new LinkedBlockingQueue<>();
        conditionsQueue=new LinkedBlockingQueue<>();
        locksQueue=new LinkedBlockingQueue<>();
        countPageFaults=new HashMap<>();
        this.setPriority(Thread.MAX_PRIORITY);
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setReadCycles(long readCycles) {
        this.readCycles = readCycles;
    }

    public synchronized void requestFrame(Frame frame,Condition condition,Lock lock) throws InterruptedException {
        frameQueue.put(frame);
        conditionsQueue.put(condition);
        locksQueue.put(lock);
    }
    @Override
    public void run() {
        try {
            while (scheduler == null) Thread.onSpinWait();
            while (true) {
                Frame frame = frameQueue.take();
                Condition condition=conditionsQueue.take();
                Lock lock=locksQueue.take();
                lock.lock();
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
                    countPageFaults.compute(frame.getPid(), (k, v) -> (v == null) ? 1 : v+1);
                    scheduler.cyclesElapsed(readCycles);
                    Main.getLogger().info(String.format("MMU: Process %d Page %d loaded in frame %d.",frame.getPid(),frame.getPage(),i));
                }
                secondChanceClock[found]=true;
                condition.signalAll();
                lock.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
