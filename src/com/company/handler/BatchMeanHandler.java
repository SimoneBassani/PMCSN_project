package com.company.handler;

import com.company.Controller;
import com.company.entity.Statistics;
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

        //todo test calcolo int di conf V valore intermedio
        /*

        double mean = 0.0;
        double variance = 0.0;
        double stdDev;
        int i;

        ArrayList<Double> means = new ArrayList<>();
        ArrayList<Double> var = new ArrayList<>();
*/

        // calcolo la media delle medie dei vari batch
        /*
        for(i=0; i<n; i++) {
            //System.out.println(batchMeanCletMeans.get(i));
            mean += list.get(i);
        }

        mean = mean / n;
        //System.out.println("mean: " + mean);

        for(i=0; i<n; i++){
            variance += pow((list.get(i) - mean), 2);
        }
        variance = variance / (list.size() - 1);

        stdDev = pow(variance, 0.5);

        statistics.setMean(mean);
        statistics.setStdDeviation(stdDev);
        */

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

        batchMeanHandler.computeMeanOfMeans(cletStatistics, alpha);
        batchMeanHandler.computeMeanOfMeans(cloudStatistics, alpha);
        statisticsHandler.printSteadyStateStats(cletStatistics, cloudStatistics, alpha);
    }


    /**
     * Calcolo la media delle varie medie calcolate per i batch e il relativo intervallo di confidenza
     * @param statistics
     * @param alpha
     */
    private void computeMeanOfMeans(Statistics statistics, double alpha) {
        double mean = 0;
        double variance = 0;

        int n = statistics.getMeanList().size();

        int i;
        // calcolo la media delle medie dei vari batch
        for(i=0; i<n; i++) {
            //System.out.println(batchMeanCletMeans.get(i));
            mean += statistics.getMeanList().get(i);
        }

        mean = mean / n;
        //System.out.println("mean: " + mean);

        for(i=0; i<n; i++){
            variance += pow((statistics.getMeanList().get(i) - mean), 2);
        }
        variance = variance / (n - 1);

        double stdDev = pow(variance, 0.5);

        Rvms rvms = new Rvms();

        double criticalValue = rvms.idfStudent((n - 1), 1 - (alpha / 2));
        double confInt = (criticalValue * stdDev) / Math.sqrt(n - 1);

        statistics.setMean(mean);
        statistics.setConfidenceInterval(confInt);
    }
}
