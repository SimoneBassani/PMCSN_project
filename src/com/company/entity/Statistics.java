package com.company.entity;

import java.util.ArrayList;

/**
 * Classe contenente le statistiche relative ad una ripetizione.
 */
public class Statistics {

    private double mean;
    private double mean_1;
    private double mean_2;
    private double variance;
    private double stdDeviation;
    private double confidenceInterval;
    private double confidenceInterval_1;
    private double confidenceInterval_2;

    //TODO utile?
    private double totalJob;
    private double totalJob_1;
    private double totalJob_2;

    // statistiche di ogni round. Per le "ensStat" sono quelle utilzzate per ottenere i valori ensemble.
    // Per le "roundStat" sono i valori ottenuti per ogni round
    private ArrayList<Double> meanList = new ArrayList<>();
    private ArrayList<Double> meanList_1 = new ArrayList<>();
    private ArrayList<Double> meanList_2 = new ArrayList<>();
    private ArrayList<Double> confidenceIntervalList = new ArrayList<>();
    private ArrayList<Double> confidenceIntervalList_1 = new ArrayList<>();
    private ArrayList<Double> confidenceIntervalList_2 = new ArrayList<>();

    private double populationMean;
    private double populationMean_1;
    private double populationMean_2;
    private double populationConfInt;
    private double populationConfInt_1;
    private double populationConfInt_2;

    private ArrayList<Double> populationMeanList = new ArrayList<>();
    private ArrayList<Double> populationMeanList_1 = new ArrayList<>();
    private ArrayList<Double> populationMeanList_2 = new ArrayList<>();
    private ArrayList<Double> populationConfIntList = new ArrayList<>();
    private ArrayList<Double> populationConfIntList_1 = new ArrayList<>();
    private ArrayList<Double> populationConfIntList_2 = new ArrayList<>();

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

    public ArrayList<Double> getMeanList_1() {
        return meanList_1;
    }

    public void setMeanList_1(ArrayList<Double> meanList_1) {
        this.meanList_1 = meanList_1;
    }

    public ArrayList<Double> getMeanList_2() {
        return meanList_2;
    }

    public void setMeanList_2(ArrayList<Double> meanList_2) {
        this.meanList_2 = meanList_2;
    }

    public ArrayList<Double> getConfidenceIntervalList_1() {
        return confidenceIntervalList_1;
    }

    public void setConfidenceIntervalList_1(ArrayList<Double> confidenceIntervalList_1) {
        this.confidenceIntervalList_1 = confidenceIntervalList_1;
    }

    public ArrayList<Double> getConfidenceIntervalList_2() {
        return confidenceIntervalList_2;
    }

    public void setConfidenceIntervalList_2(ArrayList<Double> confidenceIntervalList_2) {
        this.confidenceIntervalList_2 = confidenceIntervalList_2;
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

    public double getPopulationMean_1() {
        return populationMean_1;
    }

    public void setPopulationMean_1(double populationMean_1) {
        this.populationMean_1 = populationMean_1;
    }

    public double getPopulationMean_2() {
        return populationMean_2;
    }

    public void setPopulationMean_2(double populationMean_2) {
        this.populationMean_2 = populationMean_2;
    }

    public double getPopulationConfInt_1() {
        return populationConfInt_1;
    }

    public void setPopulationConfInt_1(double populationConfInt_1) {
        this.populationConfInt_1 = populationConfInt_1;
    }

    public double getPopulationConfInt_2() {
        return populationConfInt_2;
    }

    public void setPopulationConfInt_2(double populationConfInt_2) {
        this.populationConfInt_2 = populationConfInt_2;
    }

    public ArrayList<Double> getPopulationMeanList_1() {
        return populationMeanList_1;
    }

    public void setPopulationMeanList_1(ArrayList<Double> populationMeanList_1) {
        this.populationMeanList_1 = populationMeanList_1;
    }

    public ArrayList<Double> getPopulationMeanList_2() {
        return populationMeanList_2;
    }

    public void setPopulationMeanList_2(ArrayList<Double> populationMeanList_2) {
        this.populationMeanList_2 = populationMeanList_2;
    }

    public ArrayList<Double> getPopulationConfIntList_1() {
        return populationConfIntList_1;
    }

    public void setPopulationConfIntList_1(ArrayList<Double> populationConfIntList_1) {
        this.populationConfIntList_1 = populationConfIntList_1;
    }

    public ArrayList<Double> getPopulationConfIntList_2() {
        return populationConfIntList_2;
    }

    public void setPopulationConfIntList_2(ArrayList<Double> populationConfIntList_2) {
        this.populationConfIntList_2 = populationConfIntList_2;
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

    public double getMean_1() {
        return mean_1;
    }

    public void setMean_1(double mean_1) {
        this.mean_1 = mean_1;
    }

    public double getMean_2() {
        return mean_2;
    }

    public void setMean_2(double mean_2) {
        this.mean_2 = mean_2;
    }

    public double getConfidenceInterval_1() {
        return confidenceInterval_1;
    }

    public void setConfidenceInterval_1(double confidenceInterval_1) {
        this.confidenceInterval_1 = confidenceInterval_1;
    }

    public double getConfidenceInterval_2() {
        return confidenceInterval_2;
    }

    public void setConfidenceInterval_2(double confidenceInterval_2) {
        this.confidenceInterval_2 = confidenceInterval_2;
    }
}
