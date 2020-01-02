package com.company.entity;

import java.util.ArrayList;

/**
 * Classe contenente le statistiche relative ad una ripetizione.
 */
//todo cancellare varianza e stdDev??
public class Statistics {

    // STEADY
    private double respTimeMean;
    private double respTimeVariance;
    private double respTimeStdDeviation;
    private double respTimeConfidenceInterval;

    // statistiche di ogni round. Per le "ensStat" sono quelle utilzzate per ottenere i valori ensemble.
    // Per le "roundStat" sono i valori ottenuti per ogni round
    private ArrayList<Double> respTimeMeanList = new ArrayList<>();
    private ArrayList<Double> respTimeConfidenceIntervalList = new ArrayList<>();

    private double populationMean;
    private double populationVariance;
    private double populationConfInt;

    private ArrayList<Double> populationMeanList = new ArrayList<>();
    private ArrayList<Double> populationConfIntList = new ArrayList<>();

    private double thrMean;
    private double thrConfInt;

    private ArrayList<Double> thrMeanList = new ArrayList<>();
    private ArrayList<Double> thrConfIntList = new ArrayList<>();


    public Statistics(double mean, double variance, double stdDeviation) {
        this.respTimeMean = mean;
        this.respTimeVariance = variance;
        this.respTimeStdDeviation = stdDeviation;
    }

    public Statistics(double mean, double variance, double stdDeviation, double confidenceInterval) {
        this.respTimeMean = mean;
        this.respTimeVariance = variance;
        this.respTimeStdDeviation = stdDeviation;
        this.respTimeConfidenceInterval = confidenceInterval;
    }

    public double getRespTimeMean() {
        return respTimeMean;
    }

    public void setRespTimeMean(double respTimeMean) {
        this.respTimeMean = respTimeMean;
    }

    public double getRespTimeVariance() {
        return respTimeVariance;
    }

    public void setRespTimeVariance(double respTimeVariance) {
        this.respTimeVariance = respTimeVariance;
    }

    public double getRespTimeStdDeviation() {
        return respTimeStdDeviation;
    }

    public void setRespTimeStdDeviation(double respTimeStdDeviation) {
        this.respTimeStdDeviation = respTimeStdDeviation;
    }

    public double getRespTimeConfidenceInterval() {
        return respTimeConfidenceInterval;
    }

    public void setRespTimeConfidenceInterval(double respTimeConfidenceInterval) {
        this.respTimeConfidenceInterval = respTimeConfidenceInterval;
    }

    public double getPopulationMean() {
        return populationMean;
    }

    public void setPopulationMean(double populationMean) {
        this.populationMean = populationMean;
    }

    public double getPopulationVariance() {
        return populationVariance;
    }

    public void setPopulationVariance(double populationVariance) {
        this.populationVariance = populationVariance;
    }

    public ArrayList<Double> getRespTimeMeanList() {
        return respTimeMeanList;
    }

    public void setRespTimeMeanList(ArrayList<Double> respTimeMeanList) {
        this.respTimeMeanList = respTimeMeanList;
    }

    public ArrayList<Double> getRespTimeConfidenceIntervalList() {
        return respTimeConfidenceIntervalList;
    }

    public void setRespTimeConfidenceIntervalList(ArrayList<Double> respTimeConfidenceIntervalList) {
        this.respTimeConfidenceIntervalList = respTimeConfidenceIntervalList;
    }

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
}
