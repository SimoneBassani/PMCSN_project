package com.company.handler;

import com.company.Controller;
import com.company.entity.Event;
import com.company.entity.Statistics;
import com.company.entity.Sum;
import com.company.entity.SystemTime;
import com.company.utility.Rvms;

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

    private ArrayList<Double> cletMeanRespTime = new ArrayList<Double>();
    private ArrayList<Double> cloudMeanRespTime = new ArrayList<Double>();
    private ArrayList<Double> cletRespTimeConfInt = new ArrayList<>();
    private ArrayList<Double> cloudRespTimeConfInt = new ArrayList<>();

    private ArrayList<Double> cletMeanPopulation = new ArrayList<Double>();
    private ArrayList<Double> cloudMeanPopulation = new ArrayList<Double>();
    private ArrayList<Double> cletPopulationConfInt = new ArrayList<>();
    private ArrayList<Double> cloudPopulationConfInt = new ArrayList<>();
    //todo servono liste per job 1 e 2

    private ArrayList<Double> thrSystem = new ArrayList<>();

    public static BatchMeanHandler getSingle_instance() {
        return single_instance;
    }

    public static void setSingle_instance(BatchMeanHandler single_instance) {
        BatchMeanHandler.single_instance = single_instance;
    }

    public ArrayList<Double> getCletMeanRespTime() {
        return cletMeanRespTime;
    }

    public void setCletMeanRespTime(ArrayList<Double> cletMeanRespTime) {
        this.cletMeanRespTime = cletMeanRespTime;
    }

    public ArrayList<Double> getCloudMeanRespTime() {
        return cloudMeanRespTime;
    }

    public void setCloudMeanRespTime(ArrayList<Double> cloudMeanRespTime) {
        this.cloudMeanRespTime = cloudMeanRespTime;
    }

    public ArrayList<Double> getCletRespTimeConfInt() {
        return cletRespTimeConfInt;
    }

    public void setCletRespTimeConfInt(ArrayList<Double> cletRespTimeConfInt) {
        this.cletRespTimeConfInt = cletRespTimeConfInt;
    }

    public ArrayList<Double> getCloudRespTimeConfInt() {
        return cloudRespTimeConfInt;
    }

    public void setCloudRespTimeConfInt(ArrayList<Double> cloudRespTimeConfInt) {
        this.cloudRespTimeConfInt = cloudRespTimeConfInt;
    }

    public ArrayList<Double> getCletMeanPopulation() { return cletMeanPopulation; }

    public void setCletMeanPopulation(ArrayList<Double> cletMeanPopulation) {
        this.cletMeanPopulation = cletMeanPopulation;
    }

    public ArrayList<Double> getCloudMeanPopulation() { return cloudMeanPopulation; }

    public void setCloudMeanPopulation(ArrayList<Double> cloudMeanPopulation) {
        this.cloudMeanPopulation = cloudMeanPopulation;
    }

    public ArrayList<Double> getCletPopulationConfInt() { return cletPopulationConfInt; }

    public void setCletPopulationConfInt(ArrayList<Double> cletPopulationConfInt) {
        this.cletPopulationConfInt = cletPopulationConfInt;
    }

    public ArrayList<Double> getCloudPopulationConfInt() { return cloudPopulationConfInt; }

    public void setCloudPopulationConfInt(ArrayList<Double> cloudPopulationConfInt) {
        this.cloudPopulationConfInt = cloudPopulationConfInt;
    }

    /**
     * Ho un batch completo. Calcolo l'intervallo di confidenza con la media e la var salvata nell'oggetto Statistics
     * passato come parametro, quindi salvo media e int di confidenza in una lista dinamica appostica nello stesso
     * oggetto di tipo Statistics.
     * @param statistics
     * @param alpha
     */
    public void computeStatsForBatchMean(Statistics statistics, int n, double alpha){

        double stdDev = statistics.getStdDeviation();

        Rvms rvms = new Rvms();

        double criticalValue = rvms.idfStudent((n - 1), 1 - (alpha / 2));
        double confInt = (criticalValue * stdDev) / Math.sqrt(n - 1);

        statistics.getMeanList().add(statistics.getMean());
        statistics.getConfidenceIntervalList().add(confInt);
    }


    /**
     * Prendo il valore relativo alla media e al relativo int. di confidenza dall'oggetto legato al batch e li
     * salvo nella lista dinamica relativa alla statistica di carattere generale cletStat o cloudStat
     * @param statistics
     * @param batchStats
     */
    public void storeStatsForBatch(Statistics statistics, Statistics batchStats){

        statistics.getMeanList().add(batchStats.getMean());
        statistics.getConfidenceIntervalList().add(batchStats.getConfidenceInterval());
    }


    /**
     * Metodo per calcolare la media in un batch con l'A di Welford
     * @param statistics
     */
    public void computeMeanForBatchMean(Statistics statistics, long n, double x, double alpha){

        //todo test calcolo int di conf V valore intermedio ?

        double mean = statistics.getMean();
        double variance = statistics.getVariance();

        double d = x - mean; //d è la differenza tra x(i) e la media x(i-1)


        mean += d / n;

        if (n > 1)
            variance += ((n - 1) * pow(d, 2)) / n;
        else
            variance = 0.0;

        double stdDev = pow(variance / n, 0.5);

        Rvms rvms = new Rvms();

        double criticalValue = rvms.idfStudent((n - 1), 1 - (alpha / 2));
        double confInt = (criticalValue * stdDev) / Math.sqrt(n - 1);

        //aggiorno l'oggetto legato a tale statistica
        statistics.setMean(mean);
        statistics.setVariance(variance);
        statistics.setStdDeviation(stdDev);
        statistics.setConfidenceInterval(confInt);
    }


    /**
     * Metodo che calcola un intervallo di confidenza. Restituisce t*s / (n-1)^0.5
     *
     * @param statistics
     * @param alpha
     * @return
     */
    public double computeConfidenceIntervalEstimate(Statistics statistics, double alpha, int n) {

        Rvms rvms = new Rvms();

        double levelOfConfidence = 1 - alpha;
        double mean = statistics.getMean();
        double stdDev = statistics.getStdDeviation();

        double criticalValue = rvms.idfStudent((n - 1), 1 - (alpha / 2));
        double confInt = (criticalValue * stdDev) / Math.sqrt(n - 1);

        return confInt;
    }


    /**
     * Metodo che esegue un'analisi steady-state. Adesso ho un solo round, e le stats ottenute contengono le stats cercate.
     */
    public static void computeSteadyStateStatistics(int algType, double alpha) throws IOException {

        Controller controller = new Controller();
        StatisticsHandler statisticsHandler = new StatisticsHandler();
        BatchMeanHandler batchMeanHandler = BatchMeanHandler.getInstance();

        // cletStat e cloudStat conterranno i valori ottenuti con il batch-means
        Statistics cletStatistics = new Statistics(0, 0, 0);
        Statistics cloudStatistics = new Statistics(0, 0, 0);

        if(algType == 1)
            controller.runAlgorithm1(cletStatistics, cloudStatistics);
        else
            controller.runAlgorithm2(cletStatistics, cloudStatistics);

        System.out.println("\n+++ STEADY-STATE STATS +++");

        //cletStat e cloudStat contengono le liste con i valori dei vari round/batch, quindi ne calcolo la "mean of means"
        batchMeanHandler.computeMeanOfMeans(cletStatistics, alpha);
        batchMeanHandler.computeMeanOfMeans(cloudStatistics, alpha);
        statisticsHandler.printSteadyStateStats(cletStatistics, cloudStatistics, alpha);
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
        for(i=0; i<n; i++) {
            //System.out.println(batchMeanCletMeans.get(i));
            mean += meanList.get(i);
        }

        mean = mean / n;
        //System.out.println("mean: " + mean);

        for(i=0; i<n; i++){
            variance += pow((meanList.get(i) - mean), 2);
        }
        variance = variance / (n - 1);

        double stdDev = pow(variance, 0.5);

        Rvms rvms = new Rvms();

        double criticalValue = rvms.idfStudent((n - 1), 1 - (alpha / 2));
        double confInt = (criticalValue * stdDev) / Math.sqrt(n - 1);

        ArrayList<Double> results = new ArrayList<>();
        results.add(mean);
        results.add(confInt);

        return results;
    }


    /**
     * Calcolo la "mean of means" calcolate per i batch e il relativo intervallo di confidenza
     * @param statistics
     * @param alpha
     */
    public void computeMeanOfMeans(Statistics statistics, double alpha) {

        ArrayList <Double> stats = new ArrayList<>();

        // RESP TIME
        stats = computeMean(statistics.getMeanList(), alpha);
        statistics.setMean(stats.get(0));
        statistics.setConfidenceInterval(stats.get(1));

        // POP
        stats = computeMean(statistics.getPopulationMeanList(), alpha);
        statistics.setPopulationMean(stats.get(0));
        statistics.setPopulationConfInt(stats.get(1));

        // THR
        stats = computeMean(statistics.getThrMeanList(), alpha);
        statistics.setThrMean(stats.get(0));
        statistics.setThrConfInt(stats.get(1));

        stats = computeMean(statistics.getThrMeanList_1(), alpha);
        statistics.setThrMean_1(stats.get(0));
        statistics.setThrConfInt_1(stats.get(1));

        stats = computeMean(statistics.getThrMeanList_2(), alpha);
        statistics.setThrMean_2(stats.get(0));
        statistics.setThrConfInt_2(stats.get(1));
    }

    public void updateBatchStatistics(SystemTime time, ArrayList<Event> events, ArrayList<Sum> sums, int n,
                                      long jobProcessed, long jobProcessedCloud, long jobArrived, double area,
                                      double areaNode, double area1, double area2, double jobOut, double jobOut_1,
                                      double jobOut_2, int node, Statistics statistics, Statistics batchStatistics) {

        // TEMPO DI RISPOSTA
        statistics.getMeanList().add(batchStatistics.getMean());
        statistics.getConfidenceIntervalList().add(batchStatistics.getConfidenceInterval());

        // POPOLAZIONE
        //todo aggiungere liste per pop1 e pop2
        double job = area / time.getCurrent();

        //job presenti nel nodo
        double job1Node = area1 / time.getCurrent();
        double job2Node = area2 / time.getCurrent();

        statistics.getPopulationMeanList().add(areaNode / time.getCurrent());

        // THR
        statistics.getThrMeanList().add(jobOut/time.getCurrent());
        statistics.getThrMeanList_1().add(jobOut_1/time.getCurrent());
        statistics.getThrMeanList_2().add(jobOut_2/time.getCurrent());
    }
}
