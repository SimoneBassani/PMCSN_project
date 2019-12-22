package com.company;

import com.company.entity.Statistics;
import com.company.handler.BatchMeanHandler;
import com.company.handler.StatisticsHandler;
import com.company.handler.TransientHandler;
import com.company.utility.Configuration;
import com.company.utility.Printer;

import java.io.IOException;
import java.util.Scanner;

//import static com.company.handler.BatchMeanHandler.computeSteadyStateStatistics;
//import static com.company.handler.TransientHandler.computeTransientStatistics;
import static java.lang.System.exit;

/**
 * TODO:
 * - completare raccolta valori steady-state. Per il grafico valutare se prendere un sottoinsieme di punti
 * - studiare anche il tempo di risposta medio per ogni round? Capirne l'utilità
 * - controllare il thr
 * - decidere la versione della gestione dei job interrotti
 */
public class Main {

    public static void main(String[] args) throws IOException {

        int algType;    //1 per algoritmo 1, 2 per algoritmo 2

        Scanner scanner = new Scanner(System.in);
        Configuration configuration = Configuration.getInstance();  //classe contenente le configurazioni dei parametri
        TransientHandler transientHandler = new TransientHandler(); //handler dell'analisi transient
        BatchMeanHandler batchMeanHandler = new BatchMeanHandler(); //handler dell'analisi steady-state


        /**
         * policy: specifica l'analisi da eseguire.
         *         - 0 : transient (replication)
         *         - 1 : steady-state (batch means)
         * round: è il numero di round che vengono eseguiti nel caso steady-state
         * batch_size: è la dimensione dei batch usati
         * alpha: livello di confidenza
         */
        int policy = configuration.getPolicy();
        int round = configuration.getRound();

        for(;;) {

            //todo batchMeanHandler ha una lista singleton. Riportarla a zero qui?

            System.out.println("\nMENÙ\nInserire 1 o 2 per il tipo di algoritmo da utilizzare, 3 per modificare i parametri, " +
                    "4 per terminare");

            algType = scanner.nextInt();
            switch (algType){
                case 1:     //ALGORITHM 1

                    if(policy == 1)     //transient
                        transientHandler.computeTransientStatistics(round, 1, configuration.getAlpha());

                    else                //steady-state
                        batchMeanHandler.computeSteadyStateStatistics(1, configuration.getAlpha());

                    break;

                case 2:     //ALGORITHM 2

                    if (policy == 1)    //transient
                        transientHandler.computeTransientStatistics(round, 2, configuration.getAlpha());

                    else                //steady-state
                        batchMeanHandler.computeSteadyStateStatistics(2, configuration.getAlpha());

                    break;

                case 3:     //LISTA PARAMETRI IN USO + AGGIORNAMENTO PARAMETRI
                    System.out.println("\nPARAMETRI\npolicy = " + configuration.getPolicy() + "  round = " + configuration.getRound() +
                            "   STOP = " + configuration.getSTOP());

                    for(;;) {
                        System.out.println("Inserire il numero del parametro da modificare: " +
                                "1 = policy    2 = round     3 = STOP   4 = torna al menù");
                        int parameterIndex = scanner.nextInt();

                        if (parameterIndex == 1) {
                            System.out.println("Inserire il nuovo valore:");
                            int newValue = scanner.nextInt();
                            configuration.setPolicy(newValue);
                        }
                        if (parameterIndex == 2) {
                            System.out.println("Inserire il nuovo valore:");
                            int newValue = scanner.nextInt();
                            configuration.setRound(newValue);
                        }
                        if (parameterIndex == 3) {
                            System.out.println("Inserire il nuovo valore:");
                            double newValue = scanner.nextDouble();
                            configuration.setSTOP(newValue);
                        }
                        if(parameterIndex == 4) {
                            break;
                        }
                        System.out.println("\nPARAMETRI\npolicy = " + configuration.getPolicy() + "  round = " + configuration.getRound() +
                                "   STOP = " + configuration.getSTOP());
                    }
                    break;
                case 4:     //USCITA
                    exit(0);

                default:
                    System.out.println("Valore inserito non valido");
            }
        }
    }

    //todo da cancellare. Spostato in transientHandler
    /**
     * Metodo che esegue un'analisi transiente.
     */
    /*
    public static void computeTransientStatistics(int round, int algType, double alpha) throws IOException {

        Statistics cletStatistics = new Statistics(0, 0, 0);
        Statistics cloudStatistics = new Statistics(0, 0, 0);

        Statistics ensCletStat = new Statistics(0, 0, 0);  //oggetto che memorizza l'ensamble statistics
        Statistics ensCloudStat = new Statistics(0, 0, 0);  //oggetto che memorizza l'ensamble statistics

        Controller controller = new Controller();
        StatisticsHandler statisticsHandler = new StatisticsHandler();
        Printer printer = new Printer();

        /**
         * Liste utilizzate per salvare le statistiche relative alla popolazione media nel caso transient
         */
    /*
        ArrayList<Double> oneRoundPopulation = new ArrayList<>();
        ArrayList<Double> ensamblePopulation_clet = new ArrayList<>();
        ArrayList<Double> ensamblePopulation_cloud = new ArrayList<>();

        /**
         * Il primo elemento di ogni lista è 0 perchè il sistema è vuoto all'istante iniziale
         */
    /*
        oneRoundPopulation.add(0.0);
        ensamblePopulation_clet.add(0.0);
        ensamblePopulation_cloud.add(0.0);

        /**
         * Lista che conterrà la popolazione media di ogni round.
         * todo memorizza le statistiche ritornate o statistiche apposite per la popolazione?
         */
    /*
        ArrayList<Statistics> avgCletPopulation = new ArrayList<>();
        ArrayList<Statistics> avgCloudPopulation = new ArrayList<>();
        int j;
        for(j=0; j<round; ++j) {
            avgCletPopulation.add(new Statistics(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            avgCloudPopulation.add(new Statistics(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        }

        int i;  //todo era i=1 nel for
        for(i=1; i<round; ++i){
            //System.out.println("round: " + i);

            /**
             * controllo il tipo di algoritmo da eseguire con algType.
             * Nel caso transient calcolo le statistiche per le varie ripetizioni e quindi per l'ensamble
             */
    /*
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
/*
            statisticsHandler.computeMeanAndStdDeviation(ensCletStat, i, cletStatistics.getMean());
            statisticsHandler.computeMeanAndStdDeviation(ensCloudStat, i, cloudStatistics.getMean());

            /**
             * copio le statistiche in avgCletPopulation e avgCloudPopulation.
             * Questi verranno scritti su file per realizzare grafici in Matlab.
             * Da "ensCletStat" prendo le statistiche relative a media, var e dev std.
             * Da "cletStatistics" prendo quelle relative alla popolazione media
             */
/*
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
/*
            ensamblePopulation_clet.add(cletStatistics.getTotalJob());
            ensamblePopulation_cloud.add(cloudStatistics.getTotalJob());
        }

        System.out.println("\n+++ TRANSIENT STATS +++");
        statisticsHandler.printTransientStats(ensCletStat, ensCloudStat, alpha, round,
                ensamblePopulation_clet, ensamblePopulation_cloud);

        /**
         * Invio le statistiche sul file
         */
/*
        printer.printRoundPopulation(ensamblePopulation_clet, 1, 1, algType);
        printer.printRoundPopulation(ensamblePopulation_cloud, 2, 1, algType);

        /**
         * Scrivo le statistiche sul clet e sul cloud
         */
/*
        printer.printCletStatsOnFile(avgCletPopulation);
        printer.printCloudStatsOnFile(avgCloudPopulation);
    }
*/

    //todo da cancellare. Spostato in BatchMeanHandler
    /**
     * Metodo che esegue un'analisi steady-state. Adesso ho un solo round, e le stats ottenute contengono le stats cercate.
     */
    /*
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
        statisticsHandler.printSteadyStateStats(cletStatistics, cloudStatistics, alpha);
    }
    */
}
