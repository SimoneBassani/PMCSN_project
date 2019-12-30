package com.company.utility;

import com.company.entity.Event;
import com.company.entity.Statistics;
import com.company.entity.Sum;
import com.company.entity.SystemTime;
import com.company.handler.BatchMeanHandler;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static jdk.nashorn.internal.objects.Global.print;

public class Printer {

    /**
     * Stampa un arraylist di elementi Double
     * @param list
     */
    public void printDoubleList(ArrayList<Double> list){
        int i;
        for(i=0; i<list.size(); i++)
            System.out.println(list.get(i));
        System.out.println();
    }

    /**
     * Metodo per stampare le statistiche
     */
    public void printStatistics(SystemTime time, ArrayList<Event> events, ArrayList<Sum> sums, int n, long jobProcessed,
                                 long jobProcessedClet, long jobProcessedCloud, long jobArrived, double area,
                                 double areaClet, double areaCloud, double areaClet1, double areaClet2,
                                 double areaCloud1, double areaCloud2) {

        DecimalFormat f = new DecimalFormat("###0.00");
        DecimalFormat g = new DecimalFormat("###0.000");

        System.out.println("\njobArrived in the system: " + jobArrived + "\n");

        System.out.println("for " + jobProcessed + " jobs the service node statistics are:");
        System.out.println("jobProcessed in clet: " + jobProcessedClet);
        System.out.println("jobProcessed in cloud: " + jobProcessedCloud);

        System.out.println("  avg interarrivals .. =   " + f.format(events.get(0).getNextTime() / jobProcessed));

        System.out.println("  avg wait ........... =   " + f.format(area / jobProcessed));
        System.out.println("  avg wait clet ...... =   " + f.format(areaClet / jobProcessedClet));
        System.out.println("  avg wait cloud ..... =   " + f.format(areaCloud / jobProcessedCloud));

        System.out.println("  avg # in system ............ =   " + f.format(area / time.getCurrent()));
        System.out.println("  avg # class 1 in clet ...... =   " + f.format(areaClet1 / time.getCurrent()));
        System.out.println("  avg # class 2 in clet ...... =   " + f.format(areaClet2 / time.getCurrent()));
        System.out.println("  avg # in clet .............. =   " + f.format(areaClet / time.getCurrent()));
        System.out.println("  avg # class 1 in cloud ..... =   " + f.format(areaCloud1 / time.getCurrent()));
        System.out.println("  avg # class 2 in cloud ..... =   " + f.format(areaCloud2 / time.getCurrent()));
        System.out.println("  avg # in cloud ............. =   " + f.format(areaCloud / time.getCurrent()));

        //System.out.println("area clet: " + areaClet + "\narea clet 1: " + areaClet1 + "\narea clet 2: " + areaClet2);

        int s;
/*
        for (s = 2; s < n+2; s++)          // adjust area to calculate
            area -= sums.get(s).getService();               //averages for the queue
 */
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
     * Metodo che stampa una lista
     */
    public void printEventList(int stampa, ArrayList<Event> events, int cloudIndex) {

        if(stampa != 0) {
            int i;

            for (i = 0; i < events.size(); i++) {
                if(i==0)
                    System.out.println("ARRIVI");
                if(i==2)
                    System.out.println("CLET");
                if(i==cloudIndex)
                    System.out.println("CLOUD");

                Event event = events.get(i);

                System.out.println(i + ": status = " + event.getStatus() + "   class index = " + (event.getClassIndex()+1) +
                        "   next time = " + event.getNextTime() + "   location = " + event.getLocation());
            }
            System.out.println(" ");
        }
    }

    /**
     * Metodo per stampare su file liste di elementi Double
     */
    public void printLists(ArrayList<Double> arrivalTimes, ArrayList<Double> cletArrivalTimes,
                          ArrayList<Double> cloudArrivalTimes, ArrayList<Double> departureTimes,
                          ArrayList<Double> cletDepartureTimes, ArrayList<Double> cloudDepartureTimes) throws IOException {

        //Creazione di un file vuoto
        String path = "C:\\Users\\Simone\\Desktop\\simulazione_pmcsn\\risultati\\risultati_pmcsn.txt";
        File file = new File(path);
        FileWriter fw = new FileWriter(file);

        fw.write(String.valueOf("Arrival times"));
        fw.write("\n");
        printList(arrivalTimes, path, fw);

        fw.write(String.valueOf("Clet arrival times"));
        fw.write("\n");
        printList(cletArrivalTimes, path, fw);

        fw.write(String.valueOf("Cloud arrival times"));
        fw.write("\n");
        printList(cloudArrivalTimes, path, fw);

        fw.write(String.valueOf("Departure times"));
        fw.write("\n");
        printList(departureTimes, path, fw);

        fw.write(String.valueOf("Clet departure times"));
        fw.write("\n");
        printList(cletDepartureTimes, path, fw);

        fw.write(String.valueOf("Cloud departure times"));
        fw.write("\n");
        printList(cloudDepartureTimes, path, fw);

        fw.flush();
        fw.close();
    }


    /**
     *
     * @param statisticsList lista contenente le statistiche da scrivere su file
     * @param type
     * @param policy
     * @param algorithm
     */
    public void printStatsOnFile(ArrayList<Statistics> statisticsList, int type, int policy, int algorithm) throws IOException {
        int i;
        //Creazione di un file vuoto
        String path = "C:\\Users\\Simone\\Desktop\\simulazione_pmcsn\\risultati\\";
        String alg;

        if(algorithm == 1)
            alg = "alg1";
        else
            alg = "alg2";

        if(type == 0) {
            if (policy == 1)
                //Creazione di un file vuoto
                path += "risultati_sistema_transient_" + alg + ".txt";
            else
                path += "risultati_sistema_steady_" + alg + ".txt";
        }
        else if(type == 1) {
            if (policy == 1)
                path += "risultati_clet_transient_" + alg + ".txt";
            else
                path += "risultati_clet_steady_" + alg + ".txt";
        }
        else {
            if (policy == 1)
                path += "risultati_cloud_transient_" + alg + ".txt";
            else
                path += "risultati_cloud_steady_" + alg + ".txt";
        }

        File file = new File(path);
        FileWriter fw = new FileWriter(file);

        fw.write(String.valueOf("CLOUDLET\n"));
        fw.write(String.valueOf("-  Tempo di risposta\n"));
        fw.write(String.valueOf("mean\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            fw.write(String.valueOf(statisticsList.get(i).getRespTimeMean()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("var\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            fw.write(String.valueOf(statisticsList.get(i).getRespTimeVariance()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("std dev\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            fw.write(String.valueOf(statisticsList.get(i).getRespTimeStdDeviation()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("confInt\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            fw.write(String.valueOf(statisticsList.get(i).getRespTimeConfidenceInterval()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("-  Population\n"));
        fw.write(String.valueOf("# class 1 jobs\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getTotalJob_1()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("# class 2 jobs\n"));
        for(i=0; i<statisticsList.size(); ++i) {
           // fw.write(String.valueOf(statisticsList.get(i).getTotalJob_2()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("# total jobs\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getTotalJob()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.flush();
        fw.close();
    }


    /**
     * Metodo per stampare su file una di statistiche
     */
    public void printCletStatsOnFile(ArrayList<Statistics> statisticsList) throws IOException {
        int i;
        //Creazione di un file vuoto
        String path = "C:\\Users\\Simone\\Desktop\\simulazione_pmcsn\\risultati_clet\\";
        String alg;

        File file = new File(path);
        FileWriter fw = new FileWriter(file);

        fw.write(String.valueOf("CLOUDLET\n"));
        fw.write(String.valueOf("-  Tempo di risposta\n"));
        fw.write(String.valueOf("mean\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getMean()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("var\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getVariance()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("std dev\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getStdDeviation()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("confInt\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getConfidenceInterval()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("-  Population\n"));
        fw.write(String.valueOf("# class 1 jobs\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getTotalJob_1()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("# class 2 jobs\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getTotalJob_2()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("# total jobs\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getTotalJob()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.flush();
        fw.close();
    }


    /**
     * Metodo per stampare su file una di statistiche
     */
    public void printCloudStatsOnFile(ArrayList<Statistics> statisticsList) throws IOException {
        int i;
        //Creazione di un file vuoto
        String path = "C:\\Users\\Simone\\Desktop\\simulazione_pmcsn\\risultati\\risultati_cloud.txt";
        File file = new File(path);
        FileWriter fw = new FileWriter(file);

        fw.write(String.valueOf("CLOUD\n"));
        fw.write(String.valueOf("-  Tempo di risposta\n"));
        fw.write(String.valueOf("mean\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getMean()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("var\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getVariance()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("std dev\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getStdDeviation()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("confInt\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getConfidenceInterval()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("-  Population\n"));
        fw.write(String.valueOf("# class 1 jobs\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getTotalJob_1()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("# class 2 jobs\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getTotalJob_2()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.write(String.valueOf("# total jobs\n"));
        for(i=0; i<statisticsList.size(); ++i) {
            //fw.write(String.valueOf(statisticsList.get(i).getTotalJob()));
            fw.write("\n");
        }
        fw.write("\n");

        fw.flush();
        fw.close();
    }


    /**
     * Metodo per stampare una lista ??
     * @param list
     * @param path
     * @param fw
     * @throws IOException
     */
    private void printList(ArrayList<Double> list, String path, FileWriter fw) throws IOException {

        int i;

        //stampa su schermo
/*
        for(i=0; i<list.size(); ++i)
            System.out.println(list.get(i));
        System.out.println("");
 */

        for(i=0; i<list.size(); ++i) {

            fw.write(String.valueOf(list.get(i)));
            fw.write("\n");
        }
        fw.write("\n");
    }


    /**
     * Metodo per stampare le statistiche prodotte da un round
     */
    public void printRoundStatistics(Statistics cletStatistics, Statistics cloudStatistics) {
        /*
        System.out.println("\ncletStatistics");
        System.out.println("    - mean = " + cletStatistics.getMean() + "\n    - variance = " + cletStatistics.getVariance() +
                "\n    - std deviation = " + cletStatistics.getStdDeviation() + "\n");

        System.out.println("cloudStatistics:");
        System.out.println("    - mean = " + cloudStatistics.getMean() + "\n    - variance = " + cloudStatistics.getVariance() +
                "\n    - std deviation = " + cloudStatistics.getStdDeviation() + "\n");
*/
    }


    /**
     * Metodo che stampa lo stato del sistema per quanto riguarda i job eseguiti
     * @param jobInClet
     * @param jobInCloud
     * @param jobProcessed
     * @param jobProcessedClet
     * @param jobProcessedCloud
     */
    public void printJobSummary(long jobInClet, long jobInCloud, long jobProcessed, long jobProcessedClet, long jobProcessedCloud) {
        System.out.println("job in clet: " + jobInClet + "     job in cloud: " + jobInCloud +
                "     job processati: " + jobProcessed + "    job processati dal clet: " + jobProcessedClet +
                "     job processati dal cloud: " + jobProcessedCloud);
    }


    /**
     * Metodo per stampare una lista di batch-mean
     * @param stampa
     */
    public void printBatchMeanList(int stampa) {
        /*
        if (stampa != 0) {
            BatchMeanHandler batchMeanHandler = BatchMeanHandler.getInstance();
            ArrayList<Double> list = batchMeanHandler.getCletMeanRespTime();

            System.out.println("*+*+ BATCH LIST *+*+");

            int j;
            for (j = 0; j < list.size(); ++j)
                System.out.println(list.get(j));
        }
        */
    }

    public void printEntireStat(Statistics statistics) {
/*
        System.out.println("\nmean: " + statistics.getMean());
        System.out.println("var " + statistics.getVariance());
        System.out.println("std dev " + statistics.getStdDeviation());
        */
        /*
        System.out.println("class 1 jobs " + statistics.getTotalJob_1());
        System.out.println("class 2 jobs " + statistics.getTotalJob_2());
        System.out.println("total of jobs " + statistics.getTotalJob());
        */
        System.out.println("\n");
    }


    /**
     * Print the quantity of job in the system, in the cloudlet and in the cloud at the end of the simulation. Then print
     * the throughput for the system, the cloudlet and the cloud for each type of job.
     * @param jobInSystem
     * @param jobInClet
     * @param jobInClet_1
     * @param jobInClet_2
     * @param jobInCloud
     * @param jobInCloud_1
     * @param jobInCloud_2
     * @param jobOutSystem
     * @param jobOutClet
     * @param jobOutClet_1
     * @param jobOutClet_2
     * @param jobOutCloud
     * @param jobOutCloud_1
     * @param jobOutCloud_2
     */
    //todo ho cancellato dei job passati come parametro. jobinsystem1 e jobinsystem2 per es
    public void printJobStat(long jobInSystem, long jobInClet, long jobInClet_1, long jobInClet_2, long jobInCloud,
                             long jobInCloud_1, long jobInCloud_2, long jobOutSystem, long jobOutClet, long jobOutClet_1,
                             long jobOutClet_2, long jobOutCloud, long jobOutCloud_1, long jobOutCloud_2, int jobInterrotti,
                             SystemTime time) {

        System.out.println("# OF JOBS:\n - in system: " + jobInSystem +
                //"\n - 1 in system: " + jobInSystem_1 + "\n - 2 in system: " + jobInSystem_2 +
                "\n - in clet: " + jobInClet +
                "\n - 1 in clet: " + jobInClet_1 + "\n - 2 in clet: " + jobInClet_2 + "\n - in cloud: " +
                jobInCloud + "\n - 1 in cloud: " + jobInCloud_1 + "\n - 2 in cloud: " + jobInCloud_2 +
                "\n - out of system: " + jobOutSystem + "\n - out clet: " + jobOutClet + "\n - 1 out clet: " +
                jobOutClet_1 + "\n - 2 out clet: " + jobOutClet_2 + "\n - out cloud: " + jobOutCloud +
                "\n - 1 out cloud: " + jobOutCloud_1 + "\n - 2 out cloud: " + jobOutCloud_2 +
                "\n - 2 interrotti: " + jobInterrotti);

        double currentTime = time.getCurrent();

        System.out.println("\nTHROUGHPUT:\n - system: " + jobOutSystem/currentTime +
                //"\n - system job1: " + jobOutSystem_1/currentTime + "\n - system job2: " + jobOutSystem_2/currentTime +
                "\n - clet: " + jobOutClet/currentTime + "\n - clet job1: " + jobOutClet_1/currentTime + "\n - clet job2: " +
                jobOutClet_2/currentTime + "\n - cloud: " + jobOutCloud/time.getCurrent() + "\n - cloud job1: " +
                jobOutCloud_1/currentTime + "\n - cloud job2: " + jobOutCloud_2/currentTime);
    }


    /**
     * Stampo sul file le statistiche relative alla popolazione.
     * @param oneRoundPopulation
     * @param type 0 sistema, 1 clet, 2 cloud
     * @param policy 1 transient, 2 steady-state
     * @param algorithm 1 algorithm 1, 2 algorithm 2
     * @throws IOException
     */
    public void printRoundPopulation(ArrayList<Double> oneRoundPopulation, int type, int policy, int algorithm, String dir) throws IOException {
        String path = "C:\\Users\\Simone\\Desktop\\simulazione_pmcsn\\" + dir + "\\";
        String alg;

        if(algorithm == 1)
            alg = "alg1";
        else
            alg = "alg2";

        if(type == 0) {
            if (policy == 1)
                path += "system_population_transient_" + alg + ".txt";
            else
                path += "system_population_steady_" + alg + ".txt";
        }
        else if(type == 1) {
            if (policy == 1)
                path += "clet_population_transient_" + alg + ".txt";
            else
                path += "clet_population_steady_" + alg + ".txt";
        }
            else {
            if (policy == 1)
                path += "cloud_population_transient_" + alg + ".txt";
            else
                path += "cloud_population_steady_" + alg + ".txt";
        }

        File file = new File(path);
        FileWriter fw = new FileWriter(file);

        int i;
        for(i=0; i<oneRoundPopulation.size(); ++i) {
            fw.write(String.valueOf(oneRoundPopulation.get(i)));
            fw.write("\n");
        }
        fw.write("\n");

        fw.flush();
        fw.close();
    }


    public void printSystemStatus(long jobInSystem, long jobInClet, long jobInClet_1, long jobInClet_2, long jobInCloud,
                                  long jobInCloud_1, long jobInCloud_2) {

        System.out.println("\njob in system: " + jobInSystem +
                "\n job in clet: " + jobInClet + "  job1 in clet: " + jobInClet_1 + "  job2 in clet: " + jobInClet_2 +
                "\n job in cloud: " + jobInCloud + "  job1 in cloud: " + jobInCloud_1 + "  job2 in cloud: " +
                jobInCloud_2);
    }


    /**
     * Stampa su file i valori relativi al throughput
     * @param list
     * @param index
     * @param algorithm
     * @param policy
     * @param classIndex
     * @throws IOException
     */
    public void printThr(ArrayList<Double> list, int index, int algorithm, int policy, int classIndex) throws IOException {
        String path = "C:\\Users\\Simone\\Desktop\\simulazione_pmcsn\\thr\\";
        String subsystem;
        String analysis;

        //todo cambiare path e aggiungere cartella thr
        //Creazione di un file vuoto
        if(index==1)
            subsystem = "clet";
        else
            subsystem = "cloud";

        if(policy == 1)
            analysis = "tr";
        else
            analysis = "ss";

        path += "thr_job" + String.valueOf(classIndex) + "_" + subsystem +
                "_" + analysis + "_alg" + algorithm + ".txt";

        File file = new File(path);
        FileWriter fw = new FileWriter(file);

        int i;
        for(i=0; i<list.size(); ++i) {
            fw.write(String.valueOf(list.get(i)));
            fw.write("\n");
        }
        fw.write("\n");

        fw.flush();
        fw.close();
    }


    public void printEnsembleStat(ArrayList<Double> list, int type, int policy, int algorithm, String statName, String directory) throws IOException {
        //String path = "C:\\Users\\Simone\\Desktop\\simulazione_pmcsn\\ensStat\\";
        String path = "C:\\Users\\Simone\\Desktop\\simulazione_pmcsn\\" + directory + "\\";
        String alg;

        int i;
        /*
        for(i=1; i<=25; ++i)
            System.out.println("*TEST IN PRINT - " + statName + " i=" + i + " " + list.get(i-1));
*/
        if(algorithm == 1)
            alg = "alg1";
        else
            alg = "alg2";

        if(type == 0) {
            if (policy == 1)
                //Creazione di un file vuoto
                path += "system_" + statName + "_transient_" + alg + ".txt";
            else
                path += "system_" + statName + "steady_" + alg + ".txt";
        }
        else if(type == 1) {
            if (policy == 1)
                path += "clet_" + statName + "_transient_" + alg + ".txt";
            else
                path += "clet_" + statName + "_steady_" + alg + ".txt";
        }
        else {
            if (policy == 1)
                path += "cloud_" + statName + "_transient_" + alg + ".txt";
            else
                path += "cloud_" + statName + "_steady_" + alg + ".txt";
        }

        File file = new File(path);
        FileWriter fw = new FileWriter(file);

        //int i;
        for(i=0; i<list.size(); ++i) {
            fw.write(String.valueOf(list.get(i)));
            fw.write("\n");
        }
        fw.write("\n");

        fw.flush();
        fw.close();
    }


/*
    public void printSteadyStatsOnFile(Statistics cletStatistics, Statistics cloudStatistics, int alg) throws IOException {
        //RESPONSE TIME
        printEnsembleStat(cletStatistics.getMeanList(),1, 2, alg, "mean", "responseTime");
        printEnsembleStat(cletStatistics.getMeanList_1(),1, 2, alg, "mean_1", "responseTime");
        printEnsembleStat(cletStatistics.getMeanList_2(),1, 2, alg, "mean_2", "responseTime");
        printEnsembleStat(cletStatistics.getConfidenceIntervalList(), 1, 2, alg, "confInt", "responseTime");
        printEnsembleStat(cletStatistics.getConfidenceIntervalList_1(), 1, 2, alg, "confInt_1", "responseTime");
        printEnsembleStat(cletStatistics.getConfidenceIntervalList_1(), 1, 2, alg, "confInt_2", "responseTime");

        printEnsembleStat(cloudStatistics.getMeanList(),2, 2, alg, "mean", "responseTime");
        printEnsembleStat(cloudStatistics.getMeanList_1(),2, 2, alg, "mean_1", "responseTime");
        printEnsembleStat(cloudStatistics.getMeanList_2(),2, 2, alg, "mean_2", "responseTime");
        printEnsembleStat(cloudStatistics.getConfidenceIntervalList(), 2, 2, alg, "confInt", "responseTime");
        printEnsembleStat(cloudStatistics.getConfidenceIntervalList_1(), 2, 2, alg, "confInt_1", "responseTime");
        printEnsembleStat(cloudStatistics.getConfidenceIntervalList_2(), 2, 2, alg, "confInt_2", "responseTime");

        //POPULATION
        printEnsembleStat(cletStatistics.getPopulationMeanList(),1, 2, alg, "mean", "population");
        printEnsembleStat(cletStatistics.getPopulationMeanList_1(),1, 2, alg, "mean_1", "population");
        printEnsembleStat(cletStatistics.getPopulationMeanList_2(),1, 2, alg, "mean_2", "population");
        printEnsembleStat(cletStatistics.getPopulationConfIntList(), 1, 2, alg, "confInt", "population");
        printEnsembleStat(cletStatistics.getPopulationConfIntList_1(), 1, 2, alg, "confInt_1", "population");
        printEnsembleStat(cletStatistics.getPopulationConfIntList_2(), 1, 2, alg, "confInt_2", "population");

        printEnsembleStat(cloudStatistics.getPopulationMeanList(),2, 2, alg, "mean", "population");
        printEnsembleStat(cloudStatistics.getPopulationMeanList_1(),2, 2, alg, "mean_1", "population");
        printEnsembleStat(cloudStatistics.getPopulationMeanList_2(),2, 2, alg, "mean_2", "population");
        printEnsembleStat(cloudStatistics.getPopulationConfIntList(), 2, 2, alg, "confInt", "population");
        printEnsembleStat(cloudStatistics.getPopulationConfIntList_1(), 2, 2, alg, "confInt_1", "population");
        printEnsembleStat(cloudStatistics.getPopulationConfIntList_2(), 2, 2, alg, "confInt_2", "population");

        //THR
        printEnsembleStat(cletStatistics.getThrMeanList(),1, 2, alg, "mean", "thr");
        printEnsembleStat(cletStatistics.getThrMeanList_1(),1, 2, alg, "mean_1", "thr");
        printEnsembleStat(cletStatistics.getThrMeanList_2(),1, 2, alg, "mean_2", "thr");
        printEnsembleStat(cletStatistics.getThrConfIntList(), 1, 2, alg, "confInt", "thr");
        printEnsembleStat(cletStatistics.getThrConfIntList_1(), 1, 2, alg, "confInt_1", "thr");
        printEnsembleStat(cletStatistics.getThrConfIntList_2(), 1, 2, alg, "confInt_2", "thr");

        printEnsembleStat(cloudStatistics.getThrMeanList(),2, 2, alg, "mean", "thr");
        printEnsembleStat(cloudStatistics.getThrMeanList_1(),2, 2, alg, "mean_1", "thr");
        printEnsembleStat(cloudStatistics.getThrMeanList_2(),2, 2, alg, "mean_2", "thr");
        printEnsembleStat(cloudStatistics.getThrConfIntList(), 2, 2, alg, "confInt", "thr");
        printEnsembleStat(cloudStatistics.getThrConfIntList_1(), 2, 2, alg, "confInt_1", "thr");
        printEnsembleStat(cloudStatistics.getThrConfIntList_2(), 2, 2, alg, "confInt_2", "thr");
    }
    */
}
