package com.company.entity;

/**
* Classe che rappresenta l'evento.
 */
public class Event {

    private double nextTime;            //E' il departure time
    private int status;                 //stato dell'evento di arrivo. Vale 0 se è idle, 1 se è busy
    private int classIndex;             //rappresenta la classe. Vale 0 o 1 rispettivamente per classe 1 e classe 2
    private int location;               //0 se è nel clet, 1 se è nel cloud
    private double executionTime;
    private double moved;               //0 se il job non è stato interrotto, 1 altrimenti
    private double startTime;           //istante di tempo in cui il job entra in esecuzione
    private double restartTime;         //istante di tempo in cui il job rientra in esecuzione dopo uno spostamento

    public int getLocation() { return location; }

    public void setLocation(int location) {
        this.location = location;
    }

    public double getNextTime() { return nextTime; }

    public void setNextTime(double nextTime) {
        this.nextTime = nextTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(int classIndex) {
        this.classIndex = classIndex;
    }

    public double getExecutionTime() { return executionTime; }

    public void setExecutionTime(double executionTime) { this.executionTime = executionTime; }

    public double getMoved() { return moved; }

    public void setMoved(double moved) { this.moved = moved; }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getRestartTime() {
        return restartTime;
    }

    public void setRestartTime(double restartTime) {
        this.restartTime = restartTime;
    }
}
