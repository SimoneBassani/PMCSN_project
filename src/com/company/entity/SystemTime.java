package com.company.entity;

/**
* Classe usata per tempo corrente e prossimo evento da trattare
 */
public class SystemTime {

    private double current;     //current time
    private double next;        //next event time (most imminent)

    public SystemTime() {}

    public double getCurrent() { return current; }

    public void setCurrent(double current) { this.current = current; }

    public double getNext() { return next; }

    public void setNext(double next) { this.next = next; }
}