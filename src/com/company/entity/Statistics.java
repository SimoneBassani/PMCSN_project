package com.company.entity;

import java.util.ArrayList;

/**
 * Classe contenente le statistiche relative ad una ripetizione.
 */
public class Statistics {

    private double mean;
    private double variance;
    private double stdDeviation;
    private double confidenceInterval;

    //TODO utile?
    private double totalJob;
    private double totalJob_1;
    private double totalJob_2;

    // statistiche di ogni round. Per le "ensStat" sono quelle utilzzate per ottenere i valori ensemble.
    // Per le "roundStat" sono i valori ottenuti per ogni round
    private ArrayList<Double> meanList = new ArrayList<>();
    private ArrayList<Double> confidenceIntervalList = new ArrayList<>();

    private double populationMean;
    private double populationConfInt;

    private ArrayList<Double> populationMeanList = new ArrayList<>();
    private ArrayList<Double> populationConfIntList = new ArrayList<>();

    private double thrMean;
    private double thrMean_1;
    private double thrMean_2;
    private double thrConfInt;
    private double thrConfInt_1;
    private double thrConfInt_2;

    private ArrayList<Double> thrMeanList = new ArrayList<>();
    private ArrayList<Double> thrMeanList_1 = new ArrayList<>();
    private ArrayList<Double> thrMeanList_2 = new ArrayList<>();
    private ArrayList<Double> thrConfIntList = new ArrayList<>();
    private ArrayList<Double> thrConfIntList_1 = new ArrayList<>();
    private ArrayList<Double> thrConfIntList_2 = new ArrayList<>();


    public Statistics(double mean, double variance, double stdDeviation) {
        this.mean = mean;
        this.variance = variance;
        this.stdDeviation = stdDeviation;
    }

    public Statistics(double mean, double variance, double stdDeviation, double confidenceInterval) {
        this.mean = mean;
        this.variance = variance;
        this.stdDeviation = stdDeviation;
        this.confidenceInterval = confidenceInterval;
    }

    public Statistics(double mean, double variance, double stdDeviation, double totalJob, double totalJob_1,
                      double totalJob_2) {
        this.mean = mean;
        this.variance = variance;
        this.stdDeviation = stdDeviation;
        this.totalJob = totalJob;
        this.totalJob_1 = totalJob_1;
        this.totalJob_2 = totalJob_2;
    }

    public double getPopulationMean() {
        return populationMean;
    }

    public void setPopulationMean(double populationMean) {
        this.populationMean = populationMean;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public double getStdDeviation() {
        return stdDeviation;
    }

    public void setStdDeviation(double stdDeviation) {
        this.stdDeviation = stdDeviation;
    }

    public double getTotalJob() {
        return totalJob;
    }

    public void setTotalJob(double totalJob) {
        this.totalJob = totalJob;
    }

    public double getTotalJob_1() {
        return totalJob_1;
    }

    public void setTotalJob_1(double totalJob_1) {
        this.totalJob_1 = totalJob_1;
    }

    public double getTotalJob_2() {
        return totalJob_2;
    }

    public void setTotalJob_2(double totalJob_2) {
        this.totalJob_2 = totalJob_2;
    }

    public double getConfidenceInterval() {
        return confidenceInterval;
    }

    public void setConfidenceInterval(double confidenceInterval) {
        this.confidenceInterval = confidenceInterval;
    }

    public ArrayList<Double> getMeanList() {
        return meanList;
    }

    public void setMeanList(ArrayList<Double> meanList) {
        this.meanList = meanList;
    }

    //public ArrayList<Double> getStdDevList() {
    // return stdDevList;
    // }

    //public void setStdDevList(ArrayList<Double> stdDevList) {
    // this.stdDevList = stdDevList;
    // }

    public ArrayList<Double> getConfidenceIntervalList() {
        return confidenceIntervalList;
    }

    public void setConfidenceIntervalList(ArrayList<Double> confidenceIntervalList) {
        this.confidenceIntervalList = confidenceIntervalList; }

    public double getPopulationConfInt() {
        return populationConfInt;
    }

    public void setPopulationConfInt(double populationConfInt) {
        this.populationConfInt = populationConfInt;
    }

    public ArrayList<Double> getPopulationMeanList() {
        return populationMeanList;
    }

    public void setPopulationMeanList(ArrayList<Double> populationMeanList) {
        this.populationMeanList = populationMeanList;
    }

    public ArrayList<Double> getPopulationConfIntList() {
        return populationConfIntList;
    }

    public void setPopulationConfIntList(ArrayList<Double> populationConfIntList) {
        this.populationConfIntList = populationConfIntList;
    }

    public double getThrMean() {
        return thrMean;
    }

    public void setThrMean(double thrMean) {
        this.thrMean = thrMean;
    }

    public double getThrConfInt() {
        return thrConfInt;
    }

    public void setThrConfInt(double thrConfInt) {
        this.thrConfInt = thrConfInt;
    }

    public ArrayList<Double> getThrMeanList() {
        return thrMeanList;
    }

    public void setThrMeanList(ArrayList<Double> thrMeanList) {
        this.thrMeanList = thrMeanList;
    }

    public ArrayList<Double> getThrConfIntList() {
        return thrConfIntList;
    }

    public void setThrConfIntList(ArrayList<Double> thrConfIntList) {
        this.thrConfIntList = thrConfIntList;
    }

    public double getThrMean_1() {
        return thrMean_1;
    }

    public void setThrMean_1(double thrMean_1) {
        this.thrMean_1 = thrMean_1;
    }

    public double getThrMean_2() {
        return thrMean_2;
    }

    public void setThrMean_2(double thrMean_2) {
        this.thrMean_2 = thrMean_2;
    }

    public double getThrConfInt_1() {
        return thrConfInt_1;
    }

    public void setThrConfInt_1(double thrConfInt_1) {
        this.thrConfInt_1 = thrConfInt_1;
    }

    public double getThrConfInt_2() {
        return thrConfInt_2;
    }

    public void setThrConfInt_2(double thrConfInt_2) {
        this.thrConfInt_2 = thrConfInt_2;
    }

    public ArrayList<Double> getThrMeanList_1() {
        return thrMeanList_1;
    }

    public void setThrMeanList_1(ArrayList<Double> thrMeanList_1) {
        this.thrMeanList_1 = thrMeanList_1;
    }

    public ArrayList<Double> getThrMeanList_2() {
        return thrMeanList_2;
    }

    public void setThrMeanList_2(ArrayList<Double> thrMeanList_2) {
        this.thrMeanList_2 = thrMeanList_2;
    }

    public ArrayList<Double> getThrConfIntList_1() {
        return thrConfIntList_1;
    }

    public void setThrConfIntList_1(ArrayList<Double> thrConfIntList_1) {
        this.thrConfIntList_1 = thrConfIntList_1;
    }

    public ArrayList<Double> getThrConfIntList_2() {
        return thrConfIntList_2;
    }

    public void setThrConfIntList_2(ArrayList<Double> thrConfIntList_2) {
        this.thrConfIntList_2 = thrConfIntList_2;
    }
}
