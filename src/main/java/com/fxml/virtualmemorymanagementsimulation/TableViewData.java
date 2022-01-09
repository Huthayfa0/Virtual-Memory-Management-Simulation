package com.fxml.virtualmemorymanagementsimulation;

import com.main.virtualmemorymanagementsimulation.Main;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

public class TableViewData {
    private SimpleIntegerProperty PID;
    private SimpleIntegerProperty NumberOfFaultsT;
    private SimpleDoubleProperty arrival;
    private SimpleDoubleProperty completion;
    private SimpleDoubleProperty TurnAround;
    private SimpleDoubleProperty WaitingTime;


    public TableViewData(int PID,int NumberOfFaultsT,double arrival,double completion,double TurnAround,double WaitingTime){
        this.PID = new SimpleIntegerProperty(PID);
        this.NumberOfFaultsT = new SimpleIntegerProperty(NumberOfFaultsT);
        this.arrival = new SimpleDoubleProperty(arrival);
        this.completion = new SimpleDoubleProperty(completion);
        this.TurnAround = new SimpleDoubleProperty(TurnAround);
        this.WaitingTime = new SimpleDoubleProperty(WaitingTime);
    }
    public  static ArrayList<TableViewData> read() {
        ArrayList<TableViewData> processList=new ArrayList<>();
        for (int i=0;i<Main.processes.length;i++){
            processList.add(new TableViewData(Main.processes[i].getPid(),  Main.mmu.getCountPageFaults().get(Main.processes[i].getPid()),  Main.processes[i].getStart_time(),Main.processes[i].getEnd(),(Main.processes[i].getEnd()-Main.processes[i].getStart_time()),(Main.processes[i].getEnd()-Main.processes[i].getStart_time())-Main.processes[i].getDuration()));
        }
        return processList;
    }

    public int getPID() {
        return PID.get();
    }

    public SimpleIntegerProperty PIDProperty() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID.set(PID);
    }

    public Double getArrival() {
        return arrival.get();
    }

    public void setArrival(Double arrival) {
        this.arrival= new SimpleDoubleProperty(arrival);
    }

    public Double getCompletion() {
        return completion.get();
    }

    public void setCompletion(Double completion) {
        this.completion= new SimpleDoubleProperty(completion);
    }

    public Double getTurnAround() {
        return TurnAround.get();
    }

    public void setTurnAround(Double TurnAround) {
        this.TurnAround= new SimpleDoubleProperty(TurnAround);
    }

    public Double getWaitingTime() {
        return WaitingTime.get();
    }

    public void setWaitingTime(Double WaitingTime) {
        this.WaitingTime= new SimpleDoubleProperty(WaitingTime);
    }

    public Integer getNumberOfFaultsT() {
        return NumberOfFaultsT.get();
    }

    public void setNumberOfFaultsT(Integer NumberOfFaultsT) {
        this.NumberOfFaultsT= new SimpleIntegerProperty(NumberOfFaultsT);
    }


}
