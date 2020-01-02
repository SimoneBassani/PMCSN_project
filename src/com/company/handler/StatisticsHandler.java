package com.company.handler;

import com.company.entity.Statistics;
import com.company.entity.SystemTime;
import com.company.utility.Rvms;

import java.util.ArrayList;

import static java.lang.Math.*;

public class StatisticsHandler {

    /**
     * Metodo che stampa le statistiche transient su schermo.
     * @param ensStat oggetto contenente le statistiche relative ai job generici
     * @param ensStat_1 oggetto contenente le statistiche relative ai job di classe 1
     * @param ensStat_2 oggetto contenente le statistiche relative ai job di classe 2
     */
    public void printTransientStats(Statistics ensStat, Statistics ensStat_1, Statistics ensStat_2) {

        //RESP TIME
        System.out.println("- response time:");
        System.out.println("    - global: " + ensStat.getRespTimeMean() + ", " +
                        (ensStat.getRespTimeMean() - ensStat.getRespTimeConfidenceInterval()) + ", " +
                        (ensStat.getRespTimeMean() + ensStat.getRespTimeConfidenceInterval()));

        System.out.println("    - class 1: " + ensStat_1.getRespTimeMean() + ", " +
                (ensStat_1.getRespTimeMean() - ensStat_1.getRespTimeConfidenceInterval()) + ", " +
                (ensStat_1.getRespTimeMean() + ensStat_1.getRespTimeConfidenceInterval()));

        System.out.println("    - class 2: " + ensStat_2.getRespTimeMean() + ", " +
                (ensStat_2.getRespTimeMean() - ensStat_2.getRespTimeConfidenceInterval()) + ", " +
                (ensStat_2.getRespTimeMean() + ensStat_2.getRespTimeConfidenceInterval()));


        // POPULATION
        System.out.println("- population:");
        System.out.println("    - global: " + ensStat.getPopulationMean() + ", " +
                (ensStat.getPopulationMean() - ensStat.getPopulationConfInt()) + ", " +
                (ensStat.getPopulationMean() + ensStat.getPopulationConfInt()));

        System.out.println("    - class 1: " + ensStat_1.getPopulationMean() + ", " +
                (ensStat_1.getPopulationMean() - ensStat_1.getPopulationConfInt()) + ", " +
                (ensStat_1.getPopulationMean() + ensStat_1.getPopulationConfInt()));

        System.out.println("    - class 2: " + ensStat_2.getPopulationMean() + ", " +
                (ensStat_2.getPopulationMean() - ensStat_2.getPopulationConfInt()) + ", " +
                (ensStat_2.getPopulationMean() + ensStat_2.getPopulationConfInt()));
    }


    /**
     * Calcolo la "mean of means" calcolate per i batch e il relativo intervallo di confidenza
     */
    public void computeMeanOfMeans(Statistics statistics, double x, double area, SystemTime time, int n, double alpha, String node) {

        ArrayList <Double> stats;

        // RESP TIME
        stats = calcolaMedia(statistics.getRespTimeMean(), statistics.getRespTimeVariance(), x, n, alpha);
        statistics.setRespTimeMean(stats.get(0));
        statistics.setRespTimeConfidenceInterval(stats.get(1));
        //System.out.println("*" + stats.get(0) + " " + stats.get(1) + " " + stats.get(2));

        // POP
        stats = calcolaMedia(statistics.getPopulationMean(), statistics.getPopulationVariance(),area/time.getCurrent(),
                n, alpha);
        statistics.setPopulationMean(stats.get(0));
        statistics.setPopulationConfInt(stats.get(1));
        //System.out.println("**" + stats.get(0) + " " + stats.get(1) + " " + stats.get(2));
    }


    /**
     * Calcolo la "mean of means" calcolate per i batch e il relativo intervallo di confidenza
     */
    public void computeMeanOfMeans_transient(Statistics ensStatistics, Statistics roundStatistics, int n, double alpha,
                                             String node) {

        ArrayList <Double> stats;

        // RESP TIME
        stats = calcolaMedia(ensStatistics.getRespTimeMean(), ensStatistics.getRespTimeVariance(),
                roundStatistics.getRespTimeMean(), n, alpha);
        //System.out.println("* mean=" + stats.get(0) + " confInt=" + stats.get(1) + " var=" + stats.get(2));

        ensStatistics.setRespTimeMean(stats.get(0));
        ensStatistics.setRespTimeConfidenceInterval(stats.get(1));
        ensStatistics.setRespTimeVariance(stats.get(2));

        ensStatistics.getRespTimeMeanList().add(roundStatistics.getRespTimeMean());
        ensStatistics.getRespTimeConfidenceIntervalList().add(roundStatistics.getRespTimeConfidenceInterval());

        // POP
        stats = calcolaMedia(ensStatistics.getPopulationMean(), ensStatistics.getPopulationVariance(),
                roundStatistics.getPopulationMean(), n, alpha);
        //System.out.println("**" + stats.get(0) + " " + stats.get(1) + " " + stats.get(2));

        ensStatistics.setPopulationMean(stats.get(0));
        ensStatistics.setPopulationConfInt(stats.get(1));
        ensStatistics.setPopulationVariance(stats.get(2));

        ensStatistics.getPopulationMeanList().add(roundStatistics.getPopulationMean());
        ensStatistics.getPopulationConfIntList().add(roundStatistics.getPopulationConfInt());
    }


    private ArrayList<Double> calcolaMedia(double mean, double variance, double x, int n, double alpha) {

        double stdDev, criticalValue, confInt;

        double d = x - mean; //d è la differenza tra x(i) e la media x(i-1)

        mean += d / n;

        if (n > 1) {
            variance += ((n - 1) * pow(d, 2)) / n;

            stdDev = pow(variance / n, 0.5);

            Rvms rvms = new Rvms();

            criticalValue = rvms.idfStudent((n - 1), 1 - (alpha / 2));
            confInt = (criticalValue * stdDev) / Math.sqrt(n - 1);
        }
        else{
            variance = 0.0;
            confInt = 0.0;
        }

        ArrayList<Double> result = new ArrayList<>();
        result.add(mean);
        result.add(confInt);
        result.add(variance);

        return result;
    }
}
