package com.company.handler;

import com.company.Controller;
import com.company.entity.Statistics;
import com.company.utility.Printer;

import java.io.IOException;
import java.util.ArrayList;

public class TransientHandler {

    /**
     * Metodo che esegue un'analisi transiente.
     */
    public static void computeTransientStatistics(int round, int algType, double alpha) throws IOException {

        Controller controller = new Controller();
        StatisticsHandler statisticsHandler = new StatisticsHandler();
        Printer printer = new Printer();

        Statistics cletStatistics = new Statistics(0, 0, 0);
        Statistics cloudStatistics = new Statistics(0, 0, 0);

        Statistics ensCletStat = new Statistics(0, 0, 0);  //oggetto che memorizza l'ensamble statistics
        Statistics ensCloudStat = new Statistics(0, 0, 0);  //oggetto che memorizza l'ensamble statistics

        /**
         * Liste utilizzate per salvare le statistiche relative alla popolazione media nel caso transient
         */
        //ArrayList<Double> oneRoundPopulation = new ArrayList<>();
        ArrayList<Double> ensamblePopulation_clet = new ArrayList<>();
        ArrayList<Double> ensamblePopulation_cloud = new ArrayList<>();

        /**
         * Il primo elemento di ogni lista è 0 perchè il sistema è vuoto all'istante iniziale
         */
        //oneRoundPopulation.add(0.0);
        ensamblePopulation_clet.add(0.0);
        ensamblePopulation_cloud.add(0.0);

        /**
         * Lista che conterrà la popolazione media di ogni round.
         * todo memorizza le statistiche ritornate o statistiche apposite per la popolazione?
         */
        ArrayList<Statistics> avgCletPopulation = new ArrayList<>();
        ArrayList<Statistics> avgCloudPopulation = new ArrayList<>();
        int j;
        for(j=0; j<round; ++j) {
            avgCletPopulation.add(new Statistics(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            avgCloudPopulation.add(new Statistics(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        }

        //ciclo in cui viene eseguita la simulazione transiente.
        int i;  //todo era i=1 nel for
        for(i=1; i<round; ++i){
            //System.out.println("round: " + i);

            /**
             * controllo il tipo di algoritmo da eseguire con algType.
             * Nel caso transient calcolo le statistiche per le varie ripetizioni e quindi per l'ensamble
             */
            if(algType == 1)
                controller.runAlgorithm1(cletStatistics, cloudStatistics);
            else
                controller.runAlgorithm2(cletStatistics, cloudStatistics);

            //new Printer().printRoundStatistics(cletStatistics, cloudStatistics);

            //todo controllare se giusto
            /**
             * Invece di calcolarla staticamente, calcolo la media steady-state con l'alg di Walford ad 1 passo, sia
             * per il clet che per il cloud
             */
            /*
            statisticsHandler.updateMean(ensCletStat, cletStatistics.getMean());
            statisticsHandler.updateMean(ensCloudStat, cloudStatistics.getMean());
             */

            statisticsHandler.computeMeanAndStdDeviation(ensCletStat, i, cletStatistics.getMean());
            statisticsHandler.computeMeanAndStdDeviation(ensCloudStat, i, cloudStatistics.getMean());

            /**
             * copio le statistiche in avgCletPopulation e avgCloudPopulation.
             * Questi verranno scritti su file per realizzare grafici in Matlab.
             * Da "ensCletStat" prendo le statistiche relative a media, var e dev std.
             * Da "cletStatistics" prendo quelle relative alla popolazione media
             */
            statisticsHandler.copyStatistic(ensCletStat, cletStatistics, avgCletPopulation.get(i));
            statisticsHandler.copyStatistic(ensCloudStat, cloudStatistics, avgCloudPopulation.get(i));

            //todo test sull'int di confidenza
            double cletConfInt = statisticsHandler.computeConfidenceIntervalEstimate(ensCletStat, alpha, i);
            double cloudConfInt = statisticsHandler.computeConfidenceIntervalEstimate(ensCletStat, alpha, i);
            avgCletPopulation.get(i).setConfidenceInterval(cletConfInt);
            avgCloudPopulation.get(i).setConfidenceInterval(cloudConfInt);

            System.out.println("\ntransient stat: round " + i);
            System.out.println(avgCletPopulation.get(i).getConfidenceInterval());
            System.out.println(avgCloudPopulation.get(i).getConfidenceInterval());

            /**
             * Prendo le statistiche relative alla popolazione prese per il round
             */
            ensamblePopulation_clet.add(cletStatistics.getTotalJob());
            ensamblePopulation_cloud.add(cloudStatistics.getTotalJob());
        }

        System.out.println("\n+++ TRANSIENT STATS +++");
        statisticsHandler.printTransientStats(ensCletStat, ensCloudStat, alpha, round,
                ensamblePopulation_clet, ensamblePopulation_cloud);

        /**
         * Invio le statistiche sul file
         */
        printer.printRoundPopulation(ensamblePopulation_clet, 1, 1, algType);
        printer.printRoundPopulation(ensamblePopulation_cloud, 2, 1, algType);

        /**
         * Scrivo le statistiche sul clet e sul cloud
         */
        //TODO da cancellare. sono state sostituite da una funzione unica
        //printer.printCletStatsOnFile(avgCletPopulation);
        //printer.printCloudStatsOnFile(avgCloudPopulation);
        printer.printStatsOnFile(avgCletPopulation, 1, 1, algType);
        printer.printStatsOnFile(avgCloudPopulation, 2, 1, algType);
    }
}
