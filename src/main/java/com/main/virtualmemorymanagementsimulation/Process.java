package com.main.virtualmemorymanagementsimulation;

import java.util.*;
import java.util.concurrent.locks.*;

public class Process implements Runnable {
    private final int pid;
    private final double start_time;
    private final double duration;
    private final int size;
    private final Queue<Integer> traces;
    private volatile MMU mmu=null;
    private volatile Scheduler scheduler=null;
    private long runUntil=0;
    private long end=-1;
    private final Lock lock=new ReentrantLock(true);
    private final Condition runCondition=lock.newCondition();
    public Process(int pid, double start_time, double duration, int size) {
        this.pid = pid;
        this.start_time = start_time;
        this.duration = duration;
        this.size = size;
        this.traces=new LinkedList<>();
    }

    public void setRunUntil(long runUntil) {
        lock.lock();
        this.runUntil = runUntil;
        runCondition.signalAll();
        lock.unlock();
    }

    public int getPid() {
        return pid;
    }

    public double getStart_time() {
        return start_time;
    }

    public double getDuration() {
        return duration;
    }

    public int getSize() {
        return size;
    }

    public Queue<Integer> getTraces() {
        return traces;
    }

    public void setMmu(MMU mmu) {
        this.mmu = mmu;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        try {
            while (mmu == null) Thread.onSpinWait();
            while (scheduler == null) Thread.onSpinWait();
            while (!traces.isEmpty()){
                lock.lock();
                scheduler.addReadyProcess(this);
                runCondition.await();
                Main.getLogger().info(String.format("Process %d started.",getPid()));
                while (scheduler.getCycles()<=runUntil&&!traces.isEmpty()){
                    mmu.requestFrame(new Frame(pid, traces.peek()),runCondition,lock);
                    Main.getLogger().info(String.format("Process %d requested page %d.",getPid(),traces.peek()));
                    runCondition.await();
                    traces.poll();
                    scheduler.cyclesElapsed(1);
                }
                lock.unlock();
            }
            end=scheduler.getCycles();
            Main.getLogger().info(String.format("Process %d finished at %d.",getPid(),end));
            scheduler.processFinished();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
