package com.main.virtualmemorymanagementsimulation;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;
public class Main {

   public static MMU mmu = null;
   public static Process[] processes;
    private static Logger logger;
    public static int n;
    public static int m;
    public static int s;
    public static int Time;
    public static void read( String fileName,String Algorithm) throws IOException {
        FileHandler handler = new FileHandler("default.log", false);
        logger = Logger.getLogger("com.main.virtualmemorymanagementsimulation");
        logger.addHandler(handler);

        try {
            //n #process
            //m #memoryfream
            //s #minmumfreamperProcess
            Scanner scanner=new Scanner(new File(fileName));
             n=Integer.parseInt(scanner.nextLine());
             m=Integer.parseInt(scanner.nextLine());
             s=Integer.parseInt(scanner.nextLine());
           processes=new Process[n];
            for (int i = 0; i < n; i++) {
                String[] strings=scanner.nextLine().split(" ");
                processes[i]=new Process(Integer.parseInt(strings[0]),Double.parseDouble(strings[1]),Double.parseDouble(strings[2]),Integer.parseInt(strings[3]));
                Queue<Integer> traces=processes[i].getTraces();
                for (int j = 4; j < strings.length; j++) {
                    traces.offer(Integer.parseInt(strings[j],16));
                }
            }
            switch (Algorithm){
                case "ClockMMU":
                     mmu=new ClockMMU(m,s);
                    break;
                case "FIFOMMU":
                     mmu=new FIFOMMU(m,s);
                    break;
                case "LRUMMU":
                     mmu=new LRUMMU(m,s);
                    break;
                case "SecondChanceFIFOMMU":
                     mmu=new SecondChanceFIFOMMU(m,s);
                    break;
            }
            mmu.start();
            Scheduler scheduler=new Scheduler(mmu,processes);
            scheduler.setTimeQuantumInCycles(Time);
            scheduler.start();
            for (Process process:processes){
                logger.info(String.format("Process %d took cycles %d.",process.getPid(),process.getEnd()-(long)(process.getStart_time()*360*scheduler.getCycles()-process.getTracesCount())));
            }
            mmu.getCountPageFaults().forEach((u,v)->logger.info(String.format("Process %d had %d page faults",u,v)));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    public static Logger getLogger(){
        return logger;
    }
}
