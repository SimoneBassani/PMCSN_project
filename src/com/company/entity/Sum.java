package com.company.entity;

/**
* Classe che rappresenta le somme accumulate dei tempi di servizio e del numero di job serviti
 */
public class Sum {

    double service;
    long served;

    public double getService() { return service;}

    public void setService(double service) { this.service = service; }

    public long getServed() { return served; }

    public void setServed(long serverd) { this.served = serverd; }
}
