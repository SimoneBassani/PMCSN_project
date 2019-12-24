package com.company.utility;

/**
 * Classe contenente i parametri
 */
public class Configuration {

    private Configuration(){}

    // static variable single_instance of type Singleton
    private static Configuration single_instance = null;

    // static method to create instance of Singleton class
    public static Configuration getInstance()
    {
        if (single_instance == null)
            single_instance = new Configuration();

        return single_instance;
    }

    private int policy = 2;                 //1 = transient, 2 = steady-state

    private int round = 25;                  //numero di ripetizioni per transient. 25 nel transient, 1 nello steady-state

    private int batchSize = 30;             //dimensione del batch
    private int batchObservation = 4096;    //numero di osservazioni nel batch

    private double START = 0.0;             //start time
    private double STOP = 10000.0;         //terminal time. 1000 nel transient, 10000 nello steady-state

    private int N = 20;                     //number of servers
    private int S = 20;                      //limite job in clet per alg2

    private double l1 = 4.0;                //tasso di arrivo job classe 1
    private double l2 = 6.25;               //tasso di arrivo job classe 2
    private double m1_clet = 0.45;          //tasso di servizio job classe 1 nel clet
    private double m2_clet = 0.27;          //tasso di servizio job classe 2 nel clet
    private double m1_cloud = 0.25;         //tasso di servizio job classe 1 nel cloud
    private double m2_cloud = 0.22;         //tasso di servizio job classe 2 nel cloud

    private double alpha = 0.05;            //livello di confidenza


    public double getSTART() {
        return START;
    }

    public void setSTART(double START) {
        this.START = START;
    }

    public double getSTOP() {
        return STOP;
    }

    public void setSTOP(double STOP) {
        this.STOP = STOP;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public int getS() {
        return S;
    }

    public void setS(int s) {
        S = s;
    }

    public double getL1() {
        return l1;
    }

    public void setL1(double l1) {
        this.l1 = l1;
    }

    public double getL2() {
        return l2;
    }

    public void setL2(double l2) {
        this.l2 = l2;
    }

    public double getM1_clet() {
        return m1_clet;
    }

    public void setM1_clet(double m1_clet) {
        this.m1_clet = m1_clet;
    }

    public double getM2_clet() {
        return m2_clet;
    }

    public void setM2_clet(double m2_clet) {
        this.m2_clet = m2_clet;
    }

    public double getM1_cloud() {
        return m1_cloud;
    }

    public void setM1_cloud(double m1_cloud) {
        this.m1_cloud = m1_cloud;
    }

    public double getM2_cloud() {
        return m2_cloud;
    }

    public void setM2_cloud(double m2_cloud) {
        this.m2_cloud = m2_cloud;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public int getBatchSize() { return batchSize; }

    public void setBatchSize(int batchSize) { this.batchSize = batchSize; }

    public int getBatchObservation() { return batchObservation; }

    public void setBatchObservation(int batchObservation) { this.batchObservation = batchObservation; }

    public int getPolicy() { return policy; }

    public void setPolicy(int policy) { this.policy = policy; }

    public int getRound() { return round; }

    public void setRound(int round) { this.round = round; }
}
