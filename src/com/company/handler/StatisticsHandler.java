package com.company.handler;

import com.company.entity.Event;
import com.company.entity.Statistics;
import com.company.entity.Sum;
import com.company.entity.SystemTime;
import com.company.utility.Rvms;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static java.lang.Math.*;
import static java.lang.System.exit;

public class StatisticsHandler {

    /**
     * Tale metodo implementa l'algorimo di Welford per calcolare la media e la deviazione std di un campione.
     * E' dinamico perchè è aggiornato per ogni arrivo nella simulazione
     */
    public void computeMeanAndStdDeviation(Statistics statistics, long n, double x) {    //n=# job, x=valore
/*
        System.out.println("\n++++  computeMean&stdDev, valore di n:" + n + "    ++++");
        System.out.println("++++  computeMean&stdDev, valore di x:" + x + "    ++++\n");
*/
/*
        double mean = statistics.getMean();
        double variance = statistics.getVariance();

        double d = x - mean; //d è la differenza tra x(i) e la media x(i-1)

        //calcolo media, varianza e deviazione std
        mean += d / n;

        if (n > 1)
            variance += ((n - 1) * pow(d, 2)) / n;
        else
            variance = 0.0;

        //aggiorno l'oggetto legato a tale statistica
        statistics.setMean(mean);
        statistics.setVariance(variance);
        statistics.setStdDeviation(pow(variance / n, 0.5));
*/
/*
        System.out.println("variance: " + statistics.getVariance());
        System.out.println("dev std: " + statistics.getStdDeviation());
 */
        double mean = statistics.getMean();
        double variance = statistics.getVariance();

        double d = x - mean; //d è la differenza tra x(i) e la media x(i-1)

        //calcolo media, varianza e deviazione std
        mean += d / n;

        if (n > 1)
            variance += ((n - 1) * pow(d, 2)) / n;
        else
            variance = 0.0;

        //aggiorno l'oggetto legato a tale statistica
        statistics.setMean(mean);
        statistics.setVariance(variance);
        statistics.setStdDeviation(pow(variance / n, 0.5));

        statistics.getMeanList().add(mean); //#

        Rvms rvms = new Rvms();
        double alpha = 0.05;
        double stdDev = statistics.getStdDeviation();
        double criticalValue = rvms.idfStudent((n - 1), 1 - (alpha / 2));
        double confInt = (criticalValue * stdDev) / Math.sqrt(n - 1);
        statistics.getConfidenceIntervalList().add(confInt);    //#
    }


    /**
     * Tale metodo implementa l'algorimo di Welford per calcolare la media e la deviazione std di un campione.
     * Quindi calcola l'intervallo di confidenza e salva tutte le statistiche calcolate nelle liste dinamiche
     * previste all'interno dell'oggetto di tipo "Statistics" passato come parametro
     */
    public void computeStatistics(Statistics statistics, long n, double x, double alpha) {    //n=# job, x=valore

        double mean = statistics.getMean();
        double variance = statistics.getVariance();

        double d = x - mean; //d è la differenza tra x(i) e la media x(i-1)

        //calcolo media, varianza e deviazione std
        mean += d / n;

        if (n > 1)
            variance += ((n - 1) * pow(d, 2)) / n;
        else
            variance = 0.0;

        //aggiorno l'oggetto legato a tale statistica
        statistics.setMean(mean);
        statistics.setVariance(variance);
        double stdDev = pow(variance / n, 0.5);

        statistics.getMeanList().add(mean); //#

        Rvms rvms = new Rvms();
        double criticalValue = rvms.idfStudent((n - 1), 1 - (alpha / 2));
        double confInt = (criticalValue * stdDev) / Math.sqrt(n - 1);
        statistics.getConfidenceIntervalList().add(confInt);    //#
        statistics.setConfidenceInterval(confInt);
    }

    /**
     * Metodo che calcola un intervallo di confidenza. Restituisce t*s / (n-1)^0.5
     *
     * @param statistics
     * @param alpha
     * @return
     */
    public void computeConfidenceIntervalEstimate(Statistics statistics, double alpha, int n) {

        Rvms rvms = new Rvms();

        double levelOfConfidence = 1 - alpha;
        double mean = statistics.getMean();
        double stdDev = statistics.getStdDeviation();

        double criticalValue = rvms.idfStudent((n - 1), 1 - (alpha / 2));
/*
        System.out.println("length: " + n);
        System.out.println("level of conf: " + levelOfConfidence);
        System.out.println("mean: " + mean);
        System.out.println("stdDev: " + stdDev);
        System.out.println("critical value: " + criticalValue);
        System.out.println("return value: " + (criticalValue * stdDev) / Math.sqrt(n - 1));
*/
        double confInt = (criticalValue * stdDev) / Math.sqrt(n - 1);
        statistics.getConfidenceIntervalList().add(confInt);    //#
        //return ((criticalValue * stdDev) / Math.sqrt(n - 1));
    }


    /**
     * Metodo per aggiornare la media con un valore passato come parametro
     *
     * @param mean
     */
    public void updateMean(Statistics statistics, double mean) {

        double temp = statistics.getMean();
        temp += mean;
        statistics.setMean(temp);
    }


    /**
     * Metodo che stampa le statistiche transient
     * @param ensCletStat
     * @param ensCloudStat
     * @param alpha
     * @param round
     */
    public void printTransientStats(Statistics ensCletStat, Statistics ensCloudStat, double alpha, int round,
                                    ArrayList<Double> ensCletPop, ArrayList<Double> ensCloudPop) {
        //Cloudlet
        System.out.println("\nclet \nTEMPO DI RISPOSTA\nmean: " + ensCletStat.getMean() + "\nvar: " + ensCletStat.getVariance());

        //computeConfidenceIntervalEstimate(ensCletStat, alpha, round);
        double cletConfInt = ensCletStat.getConfidenceInterval();
        System.out.println("int di conf: " + (ensCletStat.getMean() - cletConfInt) + ", " +
                (ensCletStat.getMean() + cletConfInt));

        System.out.println("pop: " + ensCletPop.get(ensCletPop.size()-1));

        //Cloud
        System.out.println("\ncloud \nTEMPO DI RISPOSTA\nmean: " + ensCloudStat.getMean() + "\nvar: " + ensCloudStat.getVariance());

        //computeConfidenceIntervalEstimate(ensCloudStat, alpha, round);
        double cloudConfInt = ensCloudStat.getConfidenceInterval();
        System.out.println("int di conf: " + (ensCloudStat.getMean() - cloudConfInt) + ", " + (ensCloudStat.getMean() + cloudConfInt));

        System.out.println("pop: " + ensCloudPop.get(ensCloudPop.size()-1));
    }


    /**
     * Metodo per stampare le statistiche steady-state
     * @param cletStatistics
     * @param cloudStatistics
     * @param alpha
     * todo nel calcolo dell'int.di conf è giusto usare la dimensione della lista (cioè il # di batch) ?
     */
    public void printSteadyStateStats(Statistics cletStatistics, Statistics cloudStatistics, double alpha) {
        BatchMeanHandler batchMeanHandler = BatchMeanHandler.getInstance();

        System.out.println("\nclet \nmean: " + cletStatistics.getMean() + "\nvar: " + cletStatistics.getVariance());
        computeConfidenceIntervalEstimate(cletStatistics, alpha, batchMeanHandler.getBatchMeanCletMeans().size());
        double cletConfInt = cletStatistics.getConfidenceInterval();
        System.out.println("int di conf: " + (cletStatistics.getMean() - cletConfInt) + ", " +
                (cletStatistics.getMean() + cletConfInt));

        System.out.println("\ncloud \nmean: " + cloudStatistics.getMean() + "\nvar: " + cloudStatistics.getVariance());
        computeConfidenceIntervalEstimate(cloudStatistics, alpha, batchMeanHandler.getBatchMeanCloudMeans().size());
        double cloudConfInt = cloudStatistics.getConfidenceInterval();
        System.out.println("int di conf: " + (cloudStatistics.getMean() - cloudConfInt) + ", " +
                (cloudStatistics.getMean() + cloudConfInt));
    }

    public void updateStatistics(SystemTime time, ArrayList<Event> events, ArrayList<Sum> sums, int n, long jobProcessed,
                                 long jobProcessedClet, long jobProcessedCloud, long jobArrived, double area,
                                 double areaClet, double areaCloud, double areaClet1, double areaClet2, double areaCloud1,
                                 double areaCloud2, Statistics cletStatistics, Statistics cloudStatistics) {

        DecimalFormat f = new DecimalFormat("###0.00");
        DecimalFormat g = new DecimalFormat("###0.000");
/*
        System.out.println("\njobArrived in the system: " + jobArrived + "\n");

        System.out.println("for " + jobProcessed + " jobs the service node statistics are:");
        System.out.println("jobProcessed in clet: " + jobProcessedClet);
        System.out.println("jobProcessed in cloud: " + jobProcessedCloud);

        double interarrivals = events.get(0).getNextTime() / jobProcessed;
        System.out.println("  avg interarrivals .. =   " + f.format(interarrivals);

        double wait = area / jobProcessed;
        double wait_in_clet = areaClet / jobProcessedClet;
        double wait_in_cloud = areaCloud / jobProcessedCloud;
        System.out.println("  avg wait ........... =   " + f.format(wait));
        System.out.println("  avg wait clet ...... =   " + f.format(wait_in_clet));
        System.out.println("  avg wait cloud ..... =   " + f.format(wait_in_cloud));
*/
        double job_in_system = area / time.getCurrent();

        //job presenti nel clet
        double job1_in_clet = areaClet1 / time.getCurrent();
        double job2_in_clet = areaClet2 / time.getCurrent();
        double job_in_clet = areaClet / time.getCurrent();

        cletStatistics.setTotalJob(job_in_clet);
        cletStatistics.setTotalJob_1(job1_in_clet);
        cletStatistics.setTotalJob_2(job2_in_clet);

        // job presenti nel cloud
        double job1_in_cloud = areaCloud1 / time.getCurrent();
        double job2_in_cloud = areaCloud2 / time.getCurrent();
        double job_in_cloud = areaCloud / time.getCurrent();

        cloudStatistics.setTotalJob(job_in_cloud);
        cloudStatistics.setTotalJob_1(job1_in_cloud);
        cloudStatistics.setTotalJob_2(job2_in_cloud);
        /*
        System.out.println("  avg # in system ............ =   " + f.format(job_in_system));
        System.out.println("  avg # class 1 in clet ...... =   " + f.format(job1_in_clet));
        System.out.println("  avg # class 2 in clet ...... =   " + f.format(job2_in_clet));
        System.out.println("  avg # in clet .............. =   " + f.format(job_in_clet));
        System.out.println("  avg # class 1 in cloud ..... =   " + f.format(job1_in_cloud));
        System.out.println("  avg # class 2 in cloud ..... =   " + f.format(job2_in_cloud));
        System.out.println("  avg # in cloud ............. =   " + f.format(job_in_cloud));
*/
        //System.out.println("area clet: " + areaClet + "\narea clet 1: " + areaClet1 + "\narea clet 2: " + areaClet2);
/*
        int s;
/*
//non usato
        for (s = 2; s < n+2; s++)          // adjust area to calculate
            area -= sums.get(s).getService();               //averages for the queue
 */
/*
        for (s = 2; s < n+2; s++)          // adjust area to calculate
            areaClet -= sums.get(s).getService();               //averages for the queue

        System.out.println("  avg delay in clet ....... =   " + f.format(areaClet / jobProcessedClet));
        System.out.println("  avg # in clet queue ..... =   " + f.format(areaClet / time.getCurrent()));

        for (s = n+2; s < sums.size(); s++)          // adjust area to calculate
            areaCloud -= sums.get(s).getService();               //averages for the queue

        System.out.println("  avg delay in cloud........ =   " + f.format(areaCloud / jobProcessedCloud));
        System.out.println("  avg # in cloud queue ..... =   " + f.format(areaCloud / time.getCurrent()));
/*
        System.out.println("\nthe server statistics are:\n");
        System.out.println("    server     utilization     avg service      share");

        for (s = 1; s <= N; s++) {
            System.out.print("       " + s +
                    "          " + g.format(sums.get(s).getService() / time.getCurrent()) + "            ");
            System.out.println(f.format(sums.get(s).getService() / sums.get(s).getServed()) + "         " +
                    g.format(sums.get(s).getServed() / (double) jobProcessed));
        }

        System.out.println("");
 */
    }


    /**
     * Copio le statistiche relative alla popolazione media e tempi di risposta dei job.
     */
    public void copyStatistic(Statistics ensStat, Statistics roundStatistics, Statistics newStatistics) {
        //TODO servono media, var e dev std qui? sono doppioni?
        newStatistics.setMean(ensStat.getMean());
        newStatistics.setVariance(ensStat.getVariance());
        newStatistics.setStdDeviation(ensStat.getStdDeviation());

        newStatistics.setTotalJob(roundStatistics.getTotalJob());
        newStatistics.setTotalJob_1(roundStatistics.getTotalJob_1());
        newStatistics.setTotalJob_2(roundStatistics.getTotalJob_2());
    }

    public void updateNodeStatistics(SystemTime time, ArrayList<Event> events, ArrayList<Sum> sums, int n,
                                      long jobProcessed, long jobProcessedNode, long jobArrived, double area,
                                      double areaNode, double areaNode1, double areaNode2,
                                      ArrayList<Double> oneRoundPopulation_node) {

        DecimalFormat f = new DecimalFormat("###0.00");
        DecimalFormat g = new DecimalFormat("###0.000");

        double job_in_system = area / time.getCurrent();
        double job1_in_node = areaNode1 / time.getCurrent();
        double job2_in_node = areaNode2 / time.getCurrent();
        double job_in_node = areaNode / time.getCurrent();

        oneRoundPopulation_node.add(job_in_node);
    }
}
