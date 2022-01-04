package com.main.virtualmemorymanagementsimulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
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
                Queue<Short> traces=processes[i].getTraces();
                for (int j = 4; j < strings.length; j++) {
                    traces.offer(Short.parseShort(strings[j]));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
