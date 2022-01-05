package com.main.virtualmemorymanagementsimulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;
public class Main {
    private static Logger logger;
    public static void main(String[] args) throws IOException {
        FileHandler handler = new FileHandler("default.log", false);

        logger = Logger.getLogger("com.main.virtualmemorymanagementsimulation");
        logger.addHandler(handler);
        String fileName="input.txt";
        try {
            Scanner scanner=new Scanner(new File("src/main/resources/"+fileName));
            int n=Integer.parseInt(scanner.nextLine());
            int m=Integer.parseInt(scanner.nextLine());
            int s=Integer.parseInt(scanner.nextLine());
            Process[] processes=new Process[n];
            for (int i = 0; i < n; i++) {
                String[] strings=scanner.nextLine().split(" ");
                processes[i]=new Process(Integer.parseInt(strings[0]),Double.parseDouble(strings[1]),Double.parseDouble(strings[2]),Integer.parseInt(strings[3]));
                Queue<Integer> traces=processes[i].getTraces();
                for (int j = 4; j < strings.length; j++) {
                    traces.offer(Integer.parseInt(strings[j],16));
                }
            }
            MMU mmu=new ClockMMU(m,s);
            mmu.start();
            Scheduler scheduler=new Scheduler(mmu,processes);
            scheduler.start();
            logger.info("The scheduler finished");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Logger getLogger(){
        return logger;
    }
}
