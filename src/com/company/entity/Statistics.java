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
    private double totalJob;
    private double totalJob_1;
    private double totalJob_2;

    public Statistics(double mean, double variance, double stdDeviation) {
        this.mean = mean;
        this.variance = variance;
        this.stdDeviation = stdDeviation;
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
}