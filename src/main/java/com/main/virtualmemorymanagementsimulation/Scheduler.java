package com.main.virtualmemorymanagementsimulation;

import java.util.*;
import java.util.concurrent.*;

public class Scheduler {
    private long cycles=0;
    private long timeQuantumInCycles=1000;
    private int contextSwitching=5;
    private long cyclesPerSecond=1000;
    private MMU mmu;
    private Process[] processes;
    private BlockingQueue<Process> readyQueue=new LinkedBlockingQueue<>();
    private final Executor processesExecutor;
    public Scheduler(MMU mmu, Process[] processes) {
        this.mmu = mmu;
        this.processes = processes;
        mmu.setScheduler(this);
        Arrays.sort(processes, Comparator.comparing(Process::getStart_time));
        processesExecutor= Executors.newFixedThreadPool(processes.length);
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
                Process process = readyQueue.take();
                if (!process.equals(prev)){
                    cyclesElapsed(getContextSwitching());
                    prev=process;
                }
                process.setRunUntil(cycles+getTimeQuantumInCycles());

            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void loadProcesses() {
        for (int i = loadedProcesses; i < processes.length; i++) {
            if (processes[i].getStart_time()*360*cyclesPerSecond<=cycles){
                loadedProcesses++;
                Main.getLogger().info(String.format("Scheduler: Process %d loaded at %d.",processes[i].getPid(),cycles));
                addReadyProcess(processes[i]);
            }else if (readyQueue.isEmpty()){
                cycles=(long)(processes[i].getStart_time()*360*cyclesPerSecond);
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
            readyQueue.put(process);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void cyclesElapsed(long cycles) {
        if (cycles<0)return;
        this.cycles += cycles;
    }
}
