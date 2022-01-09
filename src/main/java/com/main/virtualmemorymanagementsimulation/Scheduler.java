package com.main.virtualmemorymanagementsimulation;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Scheduler {
    private final Lock lock=new ReentrantLock(true);
    private final Condition condition=lock.newCondition();
    private long cycles=0;
    private long timeQuantumInCycles=1000;
    private int contextSwitching=5;
    private long cyclesPerSecond=1000;
    private final MMU mmu;
    private final Process[] processes;
    private final BlockingQueue<Process> readyQueue=new LinkedBlockingQueue<>();

    public Scheduler(MMU mmu, Process[] processes) {
        this.mmu = mmu;
        this.processes = processes;
        mmu.setScheduler(this);
        Arrays.sort(processes, Comparator.comparing(Process::getStart_time));
        Executor processesExecutor = Executors.newFixedThreadPool(processes.length);
        for (Process process : processes) {
            process.setScheduler(this);
            process.setMmu(mmu);
            processesExecutor.execute(process);
        }
    }
    private int loadedProcesses=0;
    private int finishedProcesses=0;
    public void start(){
        Process prev=null;
        try {
            while (finishedProcesses < processes.length) {
                loadProcesses();
                lock.lock();
                Thread.sleep(100);
                Process process = readyQueue.take();
                if (!mmu.checkDeadLock(process.getPid())) {
                    readyQueue.put(process);
                    lock.unlock();
                    continue;
                }
                if (!process.equals(prev)){
                    cyclesElapsed(getContextSwitching());
                    prev=process;
                }
                process.setRunUntil(cycles+getTimeQuantumInCycles());
                condition.await();
                lock.unlock();
            }
            Main.getLogger().info("Scheduler finished.");
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    private void loadProcesses() {
        for (int i = loadedProcesses; i < processes.length; i++) {
            if (processes[i].getStart_time()<=cycles){
                loadedProcesses++;
                Main.getLogger().info(String.format("Scheduler: Process %d loaded at %d.",processes[i].getPid(),cycles));
                addReadyProcess(processes[i]);
            }else{
                break;
            }
        }
    }

    public long getCycles() {
        return cycles;
    }

    public long getCyclesPerSecond() {
        return cyclesPerSecond;
    }

    public void setCyclesPerSecond(long cyclesPerSecond) {
        this.cyclesPerSecond = cyclesPerSecond;
    }

    public boolean setTimeQuantumInCycles(long timeQuantumInCycles) {
        if (timeQuantumInCycles<0)return false;
        this.timeQuantumInCycles = timeQuantumInCycles;
        return true;
    }

    public boolean setContextSwitching(int contextSwitching) {
        if (contextSwitching<0)return false;
        this.contextSwitching = contextSwitching;
        return true;
    }

    public long getTimeQuantumInCycles() {
        return timeQuantumInCycles;
    }
    public synchronized void processFinished() {
        finishedProcesses++;
    }
    public int getContextSwitching() {
        return contextSwitching;
    }
    public synchronized void addReadyProcess(Process process){
        try {
            Main.getLogger().info(String.format("Scheduler: Process %d is ready.",process.getPid()));
            readyQueue.put(process);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void releaseLock(){
        lock.lock();
        condition.signalAll();
        lock.unlock();
    }
    public synchronized void cyclesElapsed(long cycles) {
        if (cycles<0)return;
        this.cycles += cycles;
    }
}
