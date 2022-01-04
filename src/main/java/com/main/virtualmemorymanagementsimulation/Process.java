package com.main.virtualmemorymanagementsimulation;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Process {
    private int pid;
    private double start_time;
    private double duration;
    private int size;
    private Queue<Short> traces;

    public Process(int pid, double start_time, double duration, int size) {
        this.pid = pid;
        this.start_time = start_time;
        this.duration = duration;
        this.size = size;
        this.traces=new LinkedList<>();
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

    public Queue<Short> getTraces() {
        return traces;
    }
}
