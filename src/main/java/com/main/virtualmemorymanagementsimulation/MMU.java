package com.main.virtualmemorymanagementsimulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public abstract class MMU extends Thread {
    private final HashMap<Integer,Integer> countPageFaults;
    private final Frame[] frames;
    long readCycles=300;
    protected final int size;
    private final int minimumFrames;
    private final HashSet<Integer> set;
    private final BlockingQueue<Frame> frameQueue;
    private final BlockingQueue<Condition> conditionsQueue;
    private final BlockingQueue<Lock> locksQueue;
    private volatile Scheduler scheduler=null;
    public MMU(int size,int minimumFrames) {
        set=new HashSet<>();
        this.minimumFrames=minimumFrames;
        this.size=size;
        frames=new Frame[size];
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
    public boolean checkDeadLock(int pid){
        synchronized (set){
            return set.contains(pid)||set.size()<size/minimumFrames;
        }
    }
    public void addLock(int pid){
        synchronized (set){
            set.add(pid);
        }
    }
    public void releaseLock(int pid){
        synchronized (set){
            set.remove(pid);
            Main.getLogger().info("hello "+set.size());
        }
    }
    public synchronized void requestFrame(Frame frame,Condition condition,Lock lock) throws InterruptedException {
        frameQueue.put(frame);
        conditionsQueue.put(condition);
        locksQueue.put(lock);
    }

    public HashMap<Integer, Integer> getCountPageFaults() {
        return countPageFaults;
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
                    found= findFillInFrame();
                    frames[found]=frame;
                    countPageFaults.compute(frame.getPid(), (k, v) -> (v == null) ? 1 : v+1);
                    scheduler.cyclesElapsed(readCycles);
                    Main.getLogger().info(String.format("MMU: Process %d Page %d loaded in frame %d.",frame.getPid(),frame.getPage(),found));
                }
                checkIn(found);
                condition.signalAll();
                lock.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract void checkIn(int found);

    protected abstract int findFillInFrame();

}
