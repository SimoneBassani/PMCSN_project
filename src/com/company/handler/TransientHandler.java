package com.company.handler;

import com.company.Controller_2;
import com.company.entity.Statistics;
import com.company.utility.Printer;

import java.io.IOException;


public class TransientHandler {

    /**
     * Metodo che esegue un'analisi transiente.
     */
    public static void computeTransientStatistics(int round, int algType, double alpha) throws IOException {

        Controller_2 controller_2 = new Controller_2();
        StatisticsHandler statisticsHandler = new StatisticsHandler();
        Printer printer = new Printer();

        String method = "transient";

        String alg;
        if(algType == 1)
            alg = "alg1";
        else
            alg = "alg2";

        // Oggetto che memorizza l'ensamble statistics riferite al tempo
        Statistics ensCletStat = new Statistics(0, 0, 0, 0, 0, 0);
        Statistics ensCletStat_1 = new Statistics(0, 0, 0, 0, 0, 0);
        Statistics ensCletStat_2 = new Statistics(0, 0, 0, 0, 0, 0);

        Statistics ensCloudStat = new Statistics(0, 0, 0, 0, 0, 0);
        Statistics ensCloudStat_1 = new Statistics(0, 0, 0, 0, 0, 0);
        Statistics ensCloudStat_2 = new Statistics(0, 0, 0, 0, 0, 0);

        Statistics ensSystemStat = new Statistics(0, 0, 0, 0, 0, 0);
        Statistics ensSystemStat_1 = new Statistics(0, 0, 0, 0, 0, 0);
        Statistics ensSystemStat_2 = new Statistics(0, 0, 0, 0, 0, 0);

        //todo usare una classe apposita per ens?
        /*
        EnsembleStatistics ensCletStat = new EnsembleStatistics();
        EnsembleStatistics ensCloudStat = new EnsembleStatistics();
        */
        //ciclo in cui viene eseguita la simulazione transiente
        int i;
        for(i=1; i<=round; ++i){
            System.out.println("round: " + i);

            Statistics cletStatistics = new Statistics(0, 0, 0);
            Statistics cletStatistics_1 = new Statistics(0, 0, 0);
            Statistics cletStatistics_2 = new Statistics(0, 0, 0);

            Statistics cloudStatistics = new Statistics(0, 0, 0);
            Statistics cloudStatistics_1 = new Statistics(0, 0, 0);
            Statistics cloudStatistics_2 = new Statistics(0, 0, 0);

            Statistics systemStatistics = new Statistics(0, 0, 0);
            Statistics systemStatistics_1 = new Statistics(0, 0, 0);
            Statistics systemStatistics_2 = new Statistics(0, 0, 0);

            /**
             * controllo il tipo di algoritmo da eseguire con algType.
             * Nel caso transient calcolo le statistiche per le varie ripetizioni e quindi per l'ensamble
             */
            if(algType == 1)
                controller_2.runAlgorithm_1(cletStatistics, cletStatistics_1, cletStatistics_2, cloudStatistics,
                        cloudStatistics_1, cloudStatistics_2, systemStatistics, systemStatistics_1, systemStatistics_2);
            else
                controller_2.runAlgorithm_2(cletStatistics, cletStatistics_1, cletStatistics_2, cloudStatistics,
                        cloudStatistics_1, cloudStatistics_2, systemStatistics, systemStatistics_1, systemStatistics_2);

            /**
             * OSS in base a quanto scritto nel cap.8 per calcolare la media dell'ens al denominatore si usa
             * il # di round. Lo calcolo attraverso l'A di Welford come sempre per ottimizzare il calcolo.
             * IDEA: Per evitare l'uso delle strutture "avg" posso definire una lista in "ensCLet/CloudStat"
             * e usare l'indice del round come indice della lista.
             */
            statisticsHandler.computeMeanOfMeans_transient(ensCletStat, cletStatistics, i, alpha, "ensClet");
            statisticsHandler.computeMeanOfMeans_transient(ensCletStat_1, cletStatistics_1, i, alpha, "ensClet1");
            statisticsHandler.computeMeanOfMeans_transient(ensCletStat_2, cletStatistics_2, i, alpha, "ensClet2");

            statisticsHandler.computeMeanOfMeans_transient(ensCloudStat, cloudStatistics, i, alpha, "ensCloud");
            statisticsHandler.computeMeanOfMeans_transient(ensCloudStat_1, cloudStatistics_1, i, alpha, "ensCloud1");
            statisticsHandler.computeMeanOfMeans_transient(ensCloudStat_2, cloudStatistics_2, i, alpha, "ensCloud2");

            statisticsHandler.computeMeanOfMeans_transient(ensSystemStat, systemStatistics, i, alpha, "sys");
            statisticsHandler.computeMeanOfMeans_transient(ensSystemStat_1, systemStatistics_1, i, alpha, "sys1");
            statisticsHandler.computeMeanOfMeans_transient(ensSystemStat_2, systemStatistics_2, i, alpha, "sys2");
        }

        System.out.println("--------- FINITI I ROUND ---------");
        System.out.println("\n+++ TRANSIENT STATS +++");
        System.out.println("CLET");
        statisticsHandler.printTransientStats(ensCletStat, ensCletStat_1, ensCletStat_2);
        System.out.println("\nCLOUD");
        statisticsHandler.printTransientStats(ensCloudStat, ensCloudStat_1, ensCloudStat_2);
        System.out.println("\nSYSTEM");
        statisticsHandler.printTransientStats(ensSystemStat, ensSystemStat_1, ensSystemStat_2);

        //Invio le statistiche sul file
        printer.printStatOnFile(ensCletStat, ensCletStat_1, ensCletStat_2, ensCloudStat, ensCloudStat_1, ensCloudStat_2,
                ensSystemStat, ensSystemStat_1, ensSystemStat_2, method, alg);
    }
}
