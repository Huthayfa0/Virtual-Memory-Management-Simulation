package com.main.virtualmemorymanagementsimulation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class FileGenerator {
    public FileGenerator(String fileName) throws IOException {
        PrintWriter printWriter=new PrintWriter(new FileWriter("src/main/resources/"+fileName));
        Random random=new Random();
        int n=random.nextInt(20)+1;
        int m=random.nextInt(200)+60;
        int s= random.nextInt(10)+1;
        printWriter.println(n);
        printWriter.println(m);
        printWriter.println(s);
        for (int i = 0; i < n; i++) {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(i);stringBuilder.append(' ');
            int start= random.nextInt(10);
            stringBuilder.append(start);stringBuilder.append(' ');
            double duration=1+ (random.nextInt(9*360+1))/360.0;
            stringBuilder.append(duration);stringBuilder.append(' ');
            int size=s+random.nextInt(20);
            stringBuilder.append(size);stringBuilder.append(' ');
            for (int j = 0; j <duration*360*100; j++) {
                stringBuilder.append(String.format("%x",random.nextInt(size)));stringBuilder.append(' ');
            }
            printWriter.println(stringBuilder);
        }
        printWriter.close();
    }
    public static void main(String[] args) {
        try {
            new FileGenerator("input.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
