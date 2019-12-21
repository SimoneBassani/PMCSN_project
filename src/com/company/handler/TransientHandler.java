package com.company.handler;

import com.company.Controller;
import com.company.entity.Statistics;
import com.company.utility.Printer;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.System.exit;
import static jdk.nashorn.internal.objects.Global.print;

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

        /*
        ensCletStat.getMeanList().add(0.0);
        ensCloudStat.getMeanList().add(0.0);
        ensCletStat.getConfidenceIntervalList().add(0.0);
        ensCloudStat.getConfidenceIntervalList().add(0.0);
        ensCletStat.getStdDevList().add(0.0);
        ensCloudStat.getStdDevList().add(0.0);
        */


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

        //ciclo in cui viene eseguita la simulazione transiente
        int i;  //todo mettere <= per arrivare al # round giusto?
        for(i=1; i<=round; ++i){
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

            /**
             * Idea di base: le "roundStat" memorizzano i valori del round, gli "ensStat" i valori dell'ensemble.
             */

            /**
             * OSS in base a quanto scritto nel cap.8 per calcolare la media dell'ens al denominatore si usa
             * il # di round. Lo calcolo attraverso l'A di Welford come sempre per ottimizzare il calcolo.
             * IDEA: Per evitare l'uso delle strutture "avg" posso definire una lista in "ensCLet/CloudStat"
             * e usare l'indice del round come indice della lista.
             */
            statisticsHandler.computeMeanAndStdDeviation(ensCletStat, i, cletStatistics.getMean());
            statisticsHandler.computeMeanAndStdDeviation(ensCloudStat, i, cloudStatistics.getMean());

            //ensCletStat.getMeanList().add(ensCletStat.getMean());
            //ensCletStat.getStdDevList().add(ensCletStat.getStdDeviation());

            //ensCloudStat.getMeanList().add(ensCloudStat.getMean());
            //ensCloudStat.getStdDevList().add(ensCloudStat.getStdDeviation());
            /**
             * copio le statistiche in avgCletPopulation e avgCloudPopulation.
             * Questi verranno scritti su file per realizzare grafici in Matlab.
             * Da "ensCletStat" prendo le statistiche relative a media, var e dev std.
             * Da "cletStatistics" prendo quelle relative alla popolazione media
             */
            //statisticsHandler.copyStatistic(ensCletStat, cletStatistics, avgCletPopulation.get(i));
            //statisticsHandler.copyStatistic(ensCloudStat, cloudStatistics, avgCloudPopulation.get(i));

            statisticsHandler.computeConfidenceIntervalEstimate(ensCletStat, alpha, i);
            statisticsHandler.computeConfidenceIntervalEstimate(ensCloudStat, alpha, i);

            //TODO cancellare
/*
            System.out.println("*******\nsize test: " + ensCletStat.getMeanList().size());
            System.out.println("size test: " + ensCletStat.getConfidenceIntervalList().size());
*/
            //System.out.println("* i=" + i + " " + ensCletStat.getMeanList().get(i-1) + " " + ensCletStat.getConfidenceIntervalList().get(i-1));

            //ensCletStat.getMeanList().add(cletStatistics.getMean());
            //ensCletStat.getStdDevList().add(cletStatistics.getStdDeviation());
            //ensCletStat.getConfidenceIntervalList().add(ensCletConfInt);

            //ensCloudStat.getMeanList().add(cloudStatistics.getMean());
            //ensCloudStat.getStdDevList().add(cloudStatistics.getStdDeviation());
            //ensCloudStat.getConfidenceIntervalList().add(ensCloudConfInt);
/*
            avgCletPopulation.get(i).setConfidenceInterval(cletConfInt);
            avgCloudPopulation.get(i).setConfidenceInterval(cloudConfInt);
*/
            //TODO inserire int di conf dell'ens DOPO aver valutato il punto precedente

            //System.out.println("\ntransient stat: round " + i);
            //System.out.println(avgCletPopulation.get(i).getConfidenceInterval());
            //System.out.println(avgCloudPopulation.get(i).getConfidenceInterval());

            /**
             * Prendo le statistiche relative alla popolazione prese per il round
             */
            /*
            ensamblePopulation_clet.add(cletStatistics.getTotalJob());
            ensamblePopulation_cloud.add(cloudStatistics.getTotalJob());
            */
        }

        System.out.println("--------- FINITI I ROUND ---------");
        System.out.println("\n+++ TRANSIENT STATS +++");
        statisticsHandler.printTransientStats(ensCletStat, ensCloudStat, alpha, round, ensamblePopulation_clet, ensamblePopulation_cloud);

        //for(i=1; i<=round; ++i)
        //System.out.println("* i=" + i + " " + ensCletStat.getMeanList().get(i-1) + " " + ensCletStat.getConfidenceIntervalList().get(i-1));

        /**
         * Invio le statistiche sul file
         */
        //printer.printRoundPopulation(ensamblePopulation_clet, 1, 1, algType);
        //printer.printRoundPopulation(ensamblePopulation_cloud, 2, 1, algType);

        //TODO cancellare
/*
        System.out.println("*******\nsize test: " + ensCletStat.getMeanList().size());
        System.out.println("size test: " + ensCletStat.getConfidenceIntervalList().size());
*/
        printer.printEnsembleStat(ensCletStat.getMeanList(), 1, 1, algType, "mean");
        //printer.printEnsembleStat(ensCletStat.getStdDevList(), 1, 1, algType, "devStd");
        printer.printEnsembleStat(ensCletStat.getConfidenceIntervalList(), 1, 1, algType, "confInt");

        printer.printEnsembleStat(ensCloudStat.getMeanList(), 2, 1, algType, "mean");
        //printer.printEnsembleStat(ensCloudStat.getStdDevList(), 2, 1, algType, "devStd");
        printer.printEnsembleStat(ensCloudStat.getConfidenceIntervalList(), 2, 1, algType, "confInt");

        /**
         * Scrivo le statistiche sul clet e sul cloud
         */
        /*
        printer.printStatsOnFile(avgCletPopulation, 1, 1, algType);
        printer.printStatsOnFile(avgCloudPopulation, 2, 1, algType);
        */
    }
}
