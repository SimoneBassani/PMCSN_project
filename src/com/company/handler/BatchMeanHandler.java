package com.company.handler;

import com.company.Controller;
import com.company.entity.Statistics;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.pow;

public class BatchMeanHandler {

    //private BatchMeanHandler(){}

    // static variable single_instance of type Singleton
    private static BatchMeanHandler single_instance = null;

    // static method to create instance of Singleton class
    public static BatchMeanHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new BatchMeanHandler();

        return single_instance;
    }

    private ArrayList<Double> batchMeanCletMeans = new ArrayList<Double>();
    private ArrayList<Double> batchMeanCloudMeans = new ArrayList<Double>();

    public ArrayList<Double> getBatchMeanCletMeans() { return batchMeanCletMeans; }

    public void setBatchMeanCletMeans(ArrayList<Double> batchMeanCletMeans) { this.batchMeanCletMeans = batchMeanCletMeans; }

    public ArrayList<Double> getBatchMeanCloudMeans() { return batchMeanCloudMeans; }

    public void setBatchMeanCloudMeans(ArrayList<Double> batchMeanCloudMeans) { this.batchMeanCloudMeans = batchMeanCloudMeans; }


    /**
     * Metodo per calcolare la media dai vari batch-mean in modo STATICO
     * @param statistics
     */
    public void computeMeanForBatchMean(Statistics statistics, ArrayList<Double> list){
        double meanSum = 0.0;
        double mean;
        double variance = 0.0;
        double stdDev;
        int i;

        //todo test calcolo int di conf V valore intermedio
        ArrayList<Double> means = new ArrayList<>();
        ArrayList<Double> var = new ArrayList<>();

        int bmListDim = list.size();
        //System.out.println("bmlistDim " + bmListDim);

        for(i=0; i<bmListDim; i++) {
            //System.out.println(batchMeanCletMeans.get(i));
            meanSum += list.get(i);
        }

        mean = meanSum / bmListDim;
        //System.out.println("mean/batchNumber: " + mean);

        for(i=0; i<bmListDim; i++){
            variance += pow((list.get(i) - mean), 2);
        }
        variance = variance / (list.size() - 1);

        stdDev = pow(variance, 0.5);

        statistics.setMean(mean);
        statistics.setVariance(variance);
        statistics.setStdDeviation(stdDev);
    }

    /**
     * Metodo che esegue un'analisi steady-state. Adesso ho un solo round, e le stats ottenute contengono le stats cercate.
     */
    public static void computeSteadyStateStatistics(int algType, double alpha) throws IOException {

        Statistics cletStatistics = new Statistics(0, 0, 0);
        Statistics cloudStatistics = new Statistics(0, 0, 0);
        Controller controller = new Controller();
        StatisticsHandler statisticsHandler = new StatisticsHandler();
        BatchMeanHandler batchMeanHandler = BatchMeanHandler.getInstance();

        if(algType == 1)
            controller.runAlgorithm1(cletStatistics, cloudStatistics);
        else
            controller.runAlgorithm2(cletStatistics, cloudStatistics);

        System.out.println("\n+++ STEADY-STATE STATS +++");
        //TODO risolvere nuovo percorso file di out
        statisticsHandler.printSteadyStateStats(cletStatistics, cloudStatistics, alpha);
    }
}
