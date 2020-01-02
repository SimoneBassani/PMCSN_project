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
        */
    }


    /**
     * Tale metodo implementa l'algorimo di Welford per calcolare la media e la deviazione std di un campione.
     * Quindi calcola l'intervallo di confidenza e salva tutte le statistiche calcolate nelle liste dinamiche
     * previste all'interno dell'oggetto di tipo "Statistics" passato come parametro
     */
    public void computeStatistics(Statistics ensStat, long n, Statistics roundStat, double alpha) { //n=# job, x=valore
        //aggiorna ogni media in esnStat con il valore di roundStat con n = # round
        /*
        double mean = roundStat.getMean();
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
        */
    }

    /**
     * Metodo che calcola un intervallo di confidenza. Restituisce t*s / (n-1)^0.5
     *
     * @param statistics
     * @param alpha
     * @return
     */
    public void computeConfidenceIntervalEstimate(Statistics statistics, double alpha, int n) {
/*
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
/*
        double confInt = (criticalValue * stdDev) / Math.sqrt(n - 1);
        statistics.getConfidenceIntervalList().add(confInt);    //#
        //return ((criticalValue * stdDev) / Math.sqrt(n - 1));
        */
    }


    /**
     * Metodo per aggiornare la media con un valore passato come parametro
     *
     * @param mean
     */
    public void updateMean(Statistics statistics, double mean) {
/*
        double temp = statistics.getMean();
        temp += mean;
        statistics.setMean(temp);
        */
    }


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
     * Metodo per stampare le statistiche steady-state
     * @param cletStatistics
     * @param cloudStatistics
     * @param alpha
     */
    public void printSteadyStateStats(Statistics cletStatistics, Statistics cloudStatistics, double alpha) {

        //CLET
        double cletRespMean = cletStatistics.getRespTimeMean();
        /*
        double cletRespMean_1 = cletStatistics.getMean_1();
        double cletRespMean_2 = cletStatistics.getMean_2();
        */
        double cletRespConfInt = cletStatistics.getRespTimeConfidenceInterval();
        /*
        double cletRespConfInt_1 = cletStatistics.getConfidenceInterval_1();
        double cletRespConfInt_2 = cletStatistics.getConfidenceInterval_2();
        */

        System.out.println("\nCLET");
        System.out.println("Response time");
        System.out.println("total");
        System.out.println("    - mean: " + cletRespMean);
        System.out.println("    - int di conf: " + (cletRespMean - cletRespConfInt) + ", " +
                (cletRespMean + cletRespConfInt));

        System.out.println("- class 1");
        /*
        System.out.println("    - mean: " + cletRespMean_1);
        System.out.println("    - int di conf: " + (cletRespMean_1 - cletRespConfInt_1) + ", " +
                (cletRespMean_1 + cletRespConfInt_1));
                */
        System.out.println("- class 2");
        /*
        System.out.println("    - mean: " + cletRespMean_2);
        System.out.println("    - int di conf: " + (cletRespMean_2 - cletRespConfInt_2) + ", " +
                (cletRespMean_2 + cletRespConfInt_2));
                */

        double cletPopMean = cletStatistics.getPopulationMean();
        /*
        double cletPopMean_1 = cletStatistics.getPopulationMean_1();
        double cletPopMean_2 = cletStatistics.getPopulationMean_2();
        */
        double cletPopConfInt = cletStatistics.getPopulationConfInt();
        /*
        double cletPopConfInt_1 = cletStatistics.getPopulationConfInt_1();
        double cletPopConfInt_2 = cletStatistics.getPopulationConfInt_2();
        */
        System.out.println("Population:");
        System.out.println("- total");
        System.out.println("    - mean: " + cletPopMean);
        System.out.println("    - conf int: " + (cletPopMean - cletPopConfInt) + ", " +
                (cletPopMean + cletPopConfInt));
        System.out.println("- class 1");
        /*
        System.out.println("    - mean: " + cletPopMean_1);
        System.out.println("    - conf int: " + (cletPopMean_1 - cletPopConfInt_1) + ", " +
                (cletPopMean_1 + cletPopConfInt_1));
                */
        System.out.println("- class 2");
        /*
        System.out.println("    - mean: " + cletPopMean_2);
        System.out.println("    - conf int: " + (cletPopMean_2 - cletPopConfInt_2) + ", " +
                (cletPopMean_2 + cletPopConfInt_2));
                */

        double cletThrMean = cletStatistics.getThrMean();
        /*
        double cletThrMean_1 = cletStatistics.getThrMean_1();
        double cletThrMean_2 = cletStatistics.getThrMean_2();
        */
        double cletThrConfInt = cletStatistics.getThrConfInt();
        /*
        double cletThrConfInt_1 = cletStatistics.getThrConfInt_1();
        double cletThrConfInt_2 = cletStatistics.getThrConfInt_2();
        */
        System.out.println("Throughput:");
        System.out.println("- total");
        System.out.println("    - mean: " + cletThrMean);
        System.out.println("    - conf int: " + (cletThrMean - cletThrConfInt) + ", " +
                (cletThrMean + cletThrConfInt));
        /*
        System.out.println("- class 1");
        System.out.println("    - mean: " + cletThrMean_1);
        System.out.println("    - conf int: " + (cletThrMean_1 - cletThrConfInt_1) + ", " +
                (cletThrMean_1 + cletThrConfInt_1));
        System.out.println("- class 2");
        System.out.println("    - mean: " + cletThrMean_2);
        System.out.println("    - conf int: " + (cletThrMean_2 - cletThrConfInt_2) + ", " +
                (cletThrMean_2 + cletThrConfInt_2));
                */

        // CLOUD
        double cloudRespMean = cloudStatistics.getRespTimeMean();
        /*
        double cloudRespMean_1 = cloudStatistics.getMean_1();
        double cloudRespMean_2 = cloudStatistics.getMean_2();
        */
        double cloudRespConfInt = cloudStatistics.getRespTimeConfidenceInterval();
        /*
        double cloudRespConfInt_1 = cloudStatistics.getConfidenceInterval_1();
        double cloudRespConfInt_2 = cloudStatistics.getConfidenceInterval_2();
        */

        System.out.println("\nCLOUD");
        System.out.println("Response time:");
        System.out.println("total");
        System.out.println("    - mean: " + cloudRespMean);
        System.out.println("    - conf int: " + (cloudRespMean - cloudRespConfInt) + ", " +
                (cloudRespMean + cloudRespConfInt));
        System.out.println("- class 1");
        /*
        System.out.println("    - mean: " + cloudRespMean_1);
        System.out.println("    - int di conf: " + (cloudRespMean_1 - cloudRespConfInt_1) + ", " +
                (cloudRespMean_1 + cloudRespConfInt_1));
        System.out.println("- class 2");
        System.out.println("    - mean: " + cloudRespMean_2);
        System.out.println("    - int di conf: " + (cloudRespMean_2 - cloudRespConfInt_2) + ", " +
                (cloudRespMean_2 + cloudRespConfInt_2));
                */

        double cloudPopMean = cloudStatistics.getPopulationMean();
        /*
        double cloudPopMean_1 = cloudStatistics.getPopulationMean_1();
        double cloudPopMean_2 = cloudStatistics.getPopulationMean_2();
        */
        double cloudPopConfInt = cloudStatistics.getPopulationConfInt();
        /*
        double cloudPopConfInt_1 = cloudStatistics.getPopulationConfInt_1();
        double cloudPopConfInt_2 = cloudStatistics.getPopulationConfInt_2();
        */
        System.out.println("Population:");
        System.out.println("- total");
        System.out.println("    - mean: " + cloudPopMean);
        System.out.println("    - conf int: " + (cloudPopMean - cloudPopConfInt) + ", " +
                (cloudPopMean + cloudPopConfInt));
        System.out.println("- class 1");
        /*
        System.out.println("    - mean: " + cloudPopMean_1);
        System.out.println("    - conf int: " + (cloudPopMean_1 - cloudPopConfInt_1) + ", " +
                (cloudPopMean_1 + cloudPopConfInt_1));
        System.out.println("- class 2");
        System.out.println("    - mean: " + cloudPopMean_2);
        System.out.println("    - conf int: " + (cloudPopMean_2 - cloudPopConfInt_2) + ", " +
                (cloudPopMean_2 + cloudPopConfInt_2));
                */

        double cloudThrMean = cloudStatistics.getThrMean();
        /*
        double cloudThrMean_1 = cloudStatistics.getThrMean_1();
        double cloudThrMean_2 = cloudStatistics.getThrMean_2();
        */
        double cloudThrConfInt = cloudStatistics.getThrConfInt();
        /*
        double cloudThrConfInt_1 = cloudStatistics.getThrConfInt_1();
        double cloudThrConfInt_2 = cloudStatistics.getThrConfInt_2();
        */
        System.out.println("Throughput:");
        System.out.println("- total");
        System.out.println("    - mean: " + cloudThrMean);
        System.out.println("    - conf int: " + (cloudThrMean - cloudThrConfInt) + ", " +
                (cloudThrMean + cloudThrConfInt));
        System.out.println("- class 1");
        /*
        System.out.println("    - mean: " + cloudThrMean_1);
        System.out.println("    - conf int: " + (cloudThrMean_1 - cloudThrConfInt_1) + ", " +
                (cloudThrMean_1 + cloudThrConfInt_1));
        System.out.println("- class 2");
        System.out.println("    - mean: " + cloudThrMean_2);
        System.out.println("    - conf int: " + (cloudThrMean_2 - cloudThrConfInt_2) + ", " +
                (cloudThrMean_2 + cloudThrConfInt_2));
                */
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
        // POPOLAZIONE
        double job_in_system = area / time.getCurrent();

        //job presenti nel clet
        double job1_in_clet = areaClet1 / time.getCurrent();
        double job2_in_clet = areaClet2 / time.getCurrent();
        double job_in_clet = areaClet / time.getCurrent();
/*
        cletStatistics.setTotalJob(job_in_clet);
        cletStatistics.setTotalJob_1(job1_in_clet);
        cletStatistics.setTotalJob_2(job2_in_clet);
*/
        // job presenti nel cloud
        double job1_in_cloud = areaCloud1 / time.getCurrent();
        double job2_in_cloud = areaCloud2 / time.getCurrent();
        double job_in_cloud = areaCloud / time.getCurrent();
/*
        cloudStatistics.setTotalJob(job_in_cloud);
        cloudStatistics.setTotalJob_1(job1_in_cloud);
        cloudStatistics.setTotalJob_2(job2_in_cloud);
        */
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
        /*
        newStatistics.setMean(ensStat.getMean());
        newStatistics.setVariance(ensStat.getVariance());
        newStatistics.setStdDeviation(ensStat.getStdDeviation());
        */
/*
        newStatistics.setTotalJob(roundStatistics.getTotalJob());
        newStatistics.setTotalJob_1(roundStatistics.getTotalJob_1());
        newStatistics.setTotalJob_2(roundStatistics.getTotalJob_2());
        */
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

    /**
     * Scorre un array list di medie e calcola la "mean of means" e il relativo int. di conf.
     * @param meanList
     * @param alpha
     * @return
     */
    public ArrayList<Double> computeMean(ArrayList<Double> meanList, int n, double alpha){
        double mean = 0;
        double variance = 0;

        //int n = meanList.size();

        // calcolo la media delle medie dei vari batch
        int i;
        for(i=0; i<n; i++) {
            mean += meanList.get(i);
        }

        mean = mean / n;

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
        results.add(variance);

        return results;
    }


    /**
     * Calcolo la "mean of means" calcolate per i batch e il relativo intervallo di confidenza
     */
    public void computeMeanOfMeans(Statistics statistics, double x, double area, SystemTime time, int n, double alpha, String node) {

        ArrayList <Double> stats;

        //System.out.println("mean-of-means:");
        // RESP TIME
        stats = calcolaMedia(statistics.getRespTimeMean(), statistics.getRespTimeVariance(), x, n, alpha, node);
        statistics.setRespTimeMean(stats.get(0));
        statistics.setRespTimeConfidenceInterval(stats.get(1));
        //System.out.println("*" + stats.get(0) + " " + stats.get(1) + " " + stats.get(2));

        // POP
        stats = calcolaMedia(statistics.getPopulationMean(), statistics.getPopulationVariance(),area/time.getCurrent(),
                n, alpha, node);
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

        //System.out.println("mean-of-means:");
        // RESP TIME
        stats = calcolaMedia(ensStatistics.getRespTimeMean(), ensStatistics.getRespTimeVariance(),
                roundStatistics.getRespTimeMean(), n, alpha, node);
        //System.out.println("* mean=" + stats.get(0) + " confInt=" + stats.get(1) + " var=" + stats.get(2));

        ensStatistics.setRespTimeMean(stats.get(0));
        ensStatistics.setRespTimeConfidenceInterval(stats.get(1));
        ensStatistics.setRespTimeVariance(stats.get(2));

        ensStatistics.getRespTimeMeanList().add(roundStatistics.getRespTimeMean());
        ensStatistics.getRespTimeConfidenceIntervalList().add(roundStatistics.getRespTimeConfidenceInterval());

        // POP
        stats = calcolaMedia(ensStatistics.getPopulationMean(), ensStatistics.getPopulationVariance(),
                roundStatistics.getPopulationMean(), n, alpha, node);
        //System.out.println("**" + stats.get(0) + " " + stats.get(1) + " " + stats.get(2));

        ensStatistics.setPopulationMean(stats.get(0));
        ensStatistics.setPopulationConfInt(stats.get(1));
        ensStatistics.setPopulationVariance(stats.get(2));

        ensStatistics.getPopulationMeanList().add(roundStatistics.getPopulationMean());
        ensStatistics.getPopulationConfIntList().add(roundStatistics.getPopulationConfInt());
    }


    private ArrayList<Double> calcolaMedia(double mean, double variance, double x, int n, double alpha, String node) {

        //System.out.println(node);

        double stdDev, criticalValue, confInt;

        double d = x - mean; //d è la differenza tra x(i) e la media x(i-1)
        //System.out.println("d = " + d + " x = " + x + " mean = " + mean);

        mean += d / n;
        //System.out.println("new mean " + mean);

        if (n > 1) {
            variance += ((n - 1) * pow(d, 2)) / n;
            //System.out.println(variance);

            stdDev = pow(variance / n, 0.5);
            //System.out.println(stdDev);

            Rvms rvms = new Rvms();

            criticalValue = rvms.idfStudent((n - 1), 1 - (alpha / 2));
            //System.out.println(criticalValue);
            confInt = (criticalValue * stdDev) / Math.sqrt(n - 1);
            //System.out.println(confInt);
        }
        else{
            variance = 0.0;
            confInt = 0.0;
        }
/*
        System.out.println(variance);

        double stdDev = pow(variance / n, 0.5);
        System.out.println(stdDev);

        Rvms rvms = new Rvms();

        double criticalValue = rvms.idfStudent((n - 1), 1 - (alpha / 2));
        System.out.println(criticalValue);
        double confInt = (criticalValue * stdDev) / Math.sqrt(n - 1);
        System.out.println(confInt);
*/
        ArrayList<Double> result = new ArrayList<>();
        result.add(mean);
        result.add(confInt);
        result.add(variance);

        return result;
    }

    public void updateTransientStatistics(Statistics roundStats, Statistics roundStats_1, Statistics roundStats_2,
                                          Statistics statistics) {

        statistics.setRespTimeMean(roundStats.getRespTimeMean());
        statistics.setPopulationMean(roundStats.getPopulationMean());
        /*
        System.out.println("update a fine round");
        System.out.println(statistics.getRespTimeMean() + "\n" + statistics.getRespTimeMean_1() + "\n" +
                statistics.getRespTimeMean_2());
                */
    }
}
