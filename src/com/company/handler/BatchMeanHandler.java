package com.company.handler;

import com.company.Controller_2;
import com.company.entity.Statistics;
import com.company.entity.SystemTime;
import com.company.utility.Rvms;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.pow;

public class BatchMeanHandler {

    // static variable single_instance of type Singleton
    private static BatchMeanHandler single_instance = null;

    // static method to create instance of Singleton class
    public static BatchMeanHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new BatchMeanHandler();

        return single_instance;
    }

    private ArrayList<Double> thrSystem = new ArrayList<>();

    public static BatchMeanHandler getSingle_instance() {
        return single_instance;
    }

    public static void setSingle_instance(BatchMeanHandler single_instance) {
        BatchMeanHandler.single_instance = single_instance;
    }


    public ArrayList<Double> calcolaMedia(double mean, double variance, double x, long n, double alpha){

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
        result.add(variance);
        result.add(confInt);

        return result;
    }


    /**
     * Metodo per calcolare la media in un batch con l'A di Welford
     */
    public void computeMeanForBatchMean(Statistics statistics, long n, double responseTime, double area, SystemTime time,
                                        double alpha){

        ArrayList<Double> result = calcolaMedia(statistics.getRespTimeMean(), statistics.getRespTimeVariance(),
        responseTime, n, alpha);

        statistics.setRespTimeMean(result.get(0));
        statistics.setRespTimeVariance(result.get(1));
        statistics.setRespTimeConfidenceInterval(result.get(2));

        result = calcolaMedia(statistics.getPopulationMean(), statistics.getPopulationVariance(),
                area/time.getCurrent(), n, alpha);

        statistics.setPopulationMean(result.get(0));
        statistics.setPopulationVariance(result.get(1));
        statistics.setPopulationConfInt(result.get(2));
    }


    /**
     * Metodo che esegue un'analisi steady-state. Adesso ho un solo round, e le stats ottenute contengono le stats cercate.
     */
    public static void computeSteadyStateStatistics(int algType, double alpha) throws IOException {

        Controller_2 controller_2 = new Controller_2();


        // cletStat e cloudStat conterranno i valori ottenuti con il batch-means
        Statistics cletStatistics = new Statistics(0, 0, 0);
        Statistics cletStatistics_1 = new Statistics(0, 0, 0);
        Statistics cletStatistics_2 = new Statistics(0, 0, 0);

        Statistics cloudStatistics = new Statistics(0, 0, 0);
        Statistics cloudStatistics_1 = new Statistics(0, 0, 0);
        Statistics cloudStatistics_2 = new Statistics(0, 0, 0);

        Statistics systemStatistics = new Statistics(0, 0, 0);
        Statistics systemStatistics_1 = new Statistics(0, 0, 0);
        Statistics systemStatistics_2 = new Statistics(0, 0, 0);

        if(algType == 1)
            controller_2.runAlgorithm_1(cletStatistics, cletStatistics_1, cletStatistics_2, cloudStatistics,
                    cloudStatistics_1, cloudStatistics_2, systemStatistics, systemStatistics_1, systemStatistics_2);

        else
            controller_2.runAlgorithm_2(cletStatistics, cletStatistics_1, cletStatistics_2, cloudStatistics,
                    cloudStatistics_1, cloudStatistics_2, systemStatistics, systemStatistics_1, systemStatistics_2);
    }


    /**
     * Scorre un array list di medie e calcola la "mean of means" e il relativo int. di conf.
     * @param meanList
     * @param alpha
     * @return
     */
    public ArrayList<Double> computeMean(ArrayList<Double> meanList, double alpha){
        double mean = 0;
        double variance = 0;

        int n = meanList.size();

        int i;
        // calcolo la media delle medie dei vari batch
        for(i=0; i<n; i++)
            mean += meanList.get(i);

        mean = mean / n;

        for(i=0; i<n; i++)
            variance += pow((meanList.get(i) - mean), 2);

        variance = variance / (n - 1);

        double stdDev = pow(variance, 0.5);

        Rvms rvms = new Rvms();

        double criticalValue = rvms.idfStudent((n - 1), 1 - (alpha / 2));
        double confInt = (criticalValue * stdDev) / Math.sqrt(n - 1);

        ArrayList<Double> results = new ArrayList<>();
        results.add(mean);
        results.add(confInt);
        results.add(variance);

        return results;
    }


    /**
     * Calcolo la "mean of means" calcolate per i batch e il relativo intervallo di confidenza
     * @param statistics
     * @param alpha
     */
    public void computeMeanOfMeans(Statistics statistics, double alpha) {

        ArrayList <Double> stats;

        // RESP TIME
        stats = computeMean(statistics.getRespTimeMeanList(), alpha);
        statistics.setRespTimeMean(stats.get(0));
        statistics.setRespTimeConfidenceInterval(stats.get(1));
        System.out.println(stats.get(0) + " " + (stats.get(0)-stats.get(1)) + " " + (stats.get(0)+stats.get(1)));

        // POP
        stats = computeMean(statistics.getPopulationMeanList(), alpha);
        statistics.setPopulationMean(stats.get(0));
        statistics.setPopulationConfInt(stats.get(1));
        System.out.println(stats.get(0) + " " + (stats.get(0)-stats.get(1)) + " " + (stats.get(0)+stats.get(1)));

        // THR
        /*
        stats = computeMean(statistics.getThrMeanList(), alpha);
        statistics.setThrMean(stats.get(0));
        statistics.setThrConfInt(stats.get(1));
        */
    }


    /**
     * Aggiornamento per batchMod
     * @param statistics
     */
    public void updateBatchStatistics_2(Statistics statistics) {

        //todo all'aggiornamento, il valore medio va settato a 0 per il nuovo batch?
        // TEMPO DI RISPOSTA
        statistics.getRespTimeMeanList().add(statistics.getRespTimeMean());
        statistics.getRespTimeConfidenceIntervalList().add(statistics.getRespTimeConfidenceInterval());

        // POPOLAZIONE
        statistics.getPopulationMeanList().add(statistics.getPopulationMean());
        statistics.getPopulationConfIntList().add(statistics.getPopulationConfInt());
    }
}
