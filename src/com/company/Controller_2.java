package com.company;

import com.company.entity.Event;
import com.company.entity.Statistics;
import com.company.entity.Sum;
import com.company.entity.SystemTime;
import com.company.handler.BatchMeanHandler;
import com.company.handler.ControllerHandler;
import com.company.handler.StatisticsHandler;
import com.company.utility.Configuration;
import com.company.utility.Printer;
import com.company.utility.Rngs;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.System.exit;

/**
 * Il controller si occupa di eseguire i due algoritmi previsti per la schedulazione
 */
public class Controller_2 {

    Printer printer = new Printer();
    Configuration configuration = Configuration.getInstance();
    StatisticsHandler statisticsHandler = new StatisticsHandler();
    ControllerHandler controllerHandler = new ControllerHandler();

    double start = configuration.getSTART();         //start time
    double stop = configuration.getSTOP();          //terminal time
    double sarrival[] = {start, start};             //tempi di arrivo dei job

    int n = configuration.getN();           //number of servers
    int s = configuration.getS();           //limite job in clet per alg2

    double arrival_rate[] = {configuration.getL1(), configuration.getL2()};                     //tassi di arrivo dei job
    double service_rate_clet[] = {configuration.getM1_clet(), configuration.getM2_clet()};      //tassi di servizio nel clet
    double service_rate_cloud[] = {configuration.getM1_cloud(), configuration.getM2_cloud()};   //tassi di servizio nel cloud

    int init = 1;                       //lo uso per generare i primi due arrivi
    int round = 0;                        //contatore per il numero di repliche
    int policy = configuration.getPolicy();

    /**
     * Metodo che esegue l'algoritmo 1
     */
    public void runAlgorithm1(Statistics cletStatistics, Statistics cloudStatistics) throws IOException {

        BatchMeanHandler batchMeanHandler = BatchMeanHandler.getInstance();
        int batchSize = configuration.getBatchSize();                 //grandezza di ogni batch

        //number of job in/out of the clet/cloud/system
        long jobInClet = 0, jobInCloud = 0, jobInSystem = 0;
        long jobInClet_1 = 0, jobInClet_2 = 0, jobInCloud_1 = 0, jobInCloud_2 = 0, jobInSystem_1 = 0, jobInSystem_2 = 0;
        long jobOutClet = 0, jobOutCloud = 0, jobOutSystem = 0;
        long jobOutClet_1 = 0, jobOutClet_2 = 0, jobOutCloud_1 = 0, jobOutCloud_2 = 0, jobOutSystem_1 = 0, jobOutSystem_2 = 0;
        long jobProcessedClet = 0, jobProcessedCloud = 0, jobProcessed = 0;
        long jobArrived = 0;

        //index
        int e;                      //next-event index
        int i;                      //generic index

        double area = 0.0;          //time integrated number in the node
        double area_1 = 0.0;          //time integrated number in the node
        double area_2 = 0.0;          //time integrated number in the node
        double areaClet = 0.0;          //time integrated number in the node
        double areaClet_1 = 0.0;          //time integrated number in the node
        double areaClet_2 = 0.0;          //time integrated number in the node
        double areaCloud = 0.0;          //time integrated number in the node
        double areaCloud_1 = 0.0;          //time integrated number in the node
        double areaCloud_2 = 0.0;          //time integrated number in the node

        long cletBatchCount = 0;
        long cletBatchCount_1 = 0;
        long cletBatchCount_2 = 0;
        long cloudBatchCount = 0;
        long cloudBatchCount_1 = 0;
        long cloudBatchCount_2 = 0;
        long systemBatchCount = 0;
        long systemBatchCount_1 = 0;
        long systemBatchCount_2 = 0;

        int dimList = n+2;  //dimensione lista eventi arrivo + clet

        double alpha = configuration.getAlpha();

        String alg = "alg1";

        String method;
        if(policy == 1)
            method = "transient";
        else
            method = "steady";

        /**
         * Strutture per calcolare media e varianza di ogni batch per i casi: tempo di risposta, popolazione, altro???
         */
        Statistics cletBatchStats = new Statistics(0, 0, 0);
        Statistics cletBatchStats_1 = new Statistics(0, 0, 0);
        Statistics cletBatchStats_2 = new Statistics(0, 0, 0);
        Statistics cloudBatchStats = new Statistics(0, 0, 0);
        Statistics cloudBatchStats_1 = new Statistics(0, 0, 0);
        Statistics cloudBatchStats_2 = new Statistics(0, 0, 0);
        Statistics systemBatchStats = new Statistics(0, 0, 0);
        Statistics systemBatchStats_1 = new Statistics(0, 0, 0);
        Statistics systemBatchStats_2 = new Statistics(0, 0, 0);

        //inizializzo tutti gli stream
        Rngs r = new Rngs();
        r.plantSeeds(123);

        /**
         * dichiaro le strutture dati. La struttura usata è la seguente:
         *   - events[0]                       //arrivi classe 1
         *   - events[1]                       //arrivi classe 2
         *   - events[2], ... ,events[N+2]     //server clet
         *   - events[N+3], ...                //server cloud
         */
        ArrayList<Event> events = new ArrayList(dimList); //uso una lista dinamica unica per rappresentare gli eventi.
        ArrayList<Sum> sums = new ArrayList<>(dimList);

        /**
         * Inizializzo la lista di eventi che rappresentano i server del clet e un server del cloud.
         * Per il cloud inizializzo solo l'evento di indice "n+2", gli altri verranno inizializzati se eventualmente
         * verranno richiesti durante l'esecuzione.
         */
        for(i=0; i<dimList; ++i){
            Event event = new Event();

            if(i<2) {   //SETUP ARRIVI
                event.setNextTime(controllerHandler.getArrival(r, round, sarrival, arrival_rate, i));
                event.setStatus(1);     //status=1 perchè l'arrivo è busy
            }
            else{       //SETUP SERVER
                event.setNextTime(start);
                event.setStatus(0);         //status=0 significa che il server è idle
            }
            event.setLocation(0);
            events.add(i, event);
        }
        printer.printEventList(0, events, dimList);

        /**
         * Inizializzo la lista per l'integrale. Ne porto avanti una, alla fine la dividerò in lista per il clet e
         * lista per il cloud.
         */
        for(i=0; i<dimList; ++i){
            Sum sum = new Sum();

            sum.setService(0.0);
            sum.setServed(0);

            sums.add(sum);
        }

        SystemTime time = new SystemTime();
        time.setCurrent(start);
        //System.out.println("*** getCurrent & getNext:" + time.getCurrent() + " " + time.getNext() + "***");

        //todo invece di usare il tempo usare un evento speciale?
        /**
         * Le condizioni di uscita sono:
         * event[0].x == 0               //processo degli arrivi di classe 1 bloccato
         * event[1].x == 0               //processo degli arrivi di classe 2 bloccato
         * number == 0                   //il sistema è vuoto
         */
        while ((events.get(0).getStatus() != 0) || (events.get(1).getStatus() != 0) || (jobInSystem != 0)) {

            /**
             * Prendo "e", indice dell'evento + imminente
             */
            e = controllerHandler.nextEvent(events);
            if(e == -1) {
                //System.out.println("BREAK");
                break;
            }

            time.setNext(events.get(e).getNextTime());      //aggiorno time con l'evento da eseguire
            //System.out.println("\nnextTime: " + time.getNext());

            //printer.printEventList(0, events, dimList);

            double t = time.getNext() - time.getCurrent();
            areaClet += t * jobInClet;        //aggiorno l'area per clet
            areaClet_1 += t * jobInClet_1;        //aggiorno l'area per clet
            areaClet_2 += t * jobInClet_2;        //aggiorno l'area per clet
            areaCloud += t * jobInCloud;        //aggiorno l'area per cloud
            areaCloud_1 += t * jobInCloud_1;        //aggiorno l'area per cloud
            areaCloud_2 += t * jobInCloud_2;        //aggiorno l'area per cloud
            area += t * jobInSystem;        //aggiorno l'area totale
            area_1 += t * jobInSystem_1;        //aggiorno l'area totale
            area_2 += t * jobInSystem_2;        //aggiorno l'area totale

            time.setCurrent(time.getNext());                //il clock avanza all'evento scelto

            /**
             * ARRIVO
             */
            if(e == 0 || e == 1){

                //System.out.println("\nArrivo " + time.getCurrent() + " dell'evento con indice: " + e);

                jobInSystem++;
                jobArrived++;

                events.get(e).setNextTime(controllerHandler.getArrival(r, round, sarrival, arrival_rate, e));
                //System.out.println("indice: " + e + " nuovo tempo di arrivo: " + events.get(e).getNextTime());

                /**
                 * Se il tempo di arrivo è oltre lo STOP blocco il processo degli arrivi
                 */
                if (events.get(e).getNextTime() > stop) {
                    events.get(e).setStatus(0);

                    //System.out.println("\n\n*****   ho oltrepassato STOP. chiusura porte    *****\n\n");
                }

                if(jobInClet < n){

                    //System.out.println("\naccetto nel clet " + time.getCurrent());

                    //aggiorno i contatori
                    jobInClet++;

                    if(e == 0) {
                        jobInSystem_1++;
                        jobInClet_1++;
                    }
                    else {
                        jobInSystem_2++;
                        jobInClet_2++;
                    }

                    sendToClet(r, events.get(e), events, sums, e, dimList, time);

                    //cletArrivalTimes.add(time.getCurrent());
                    //printer.printEventList(stampa, events);
                }
                else{
                    //System.out.println("\nspedisco al cloud " + time.getCurrent());

                    //aggiorno i contatori
                    jobInCloud++;

                    if(e == 0) {
                        jobInSystem_1++;
                        jobInCloud_1++;
                    }
                    else {
                        jobInSystem_2++;
                        jobInCloud_2++;
                    }

                    sendToCloud2(r, events.get(e), events, sums, e, dimList, time, 0);
                }
            }
            /**
             * PARTENZA
             */
            else{
                jobProcessed++;

                jobInSystem--;
                jobOutSystem++;

                systemBatchCount++;

                /**
                 * Partenza dal clet
                 */
                if(e > 1 && e < dimList) {
                    //controllo il numero di elementi presenti per sapere se parte dal clet o dal cluster

                    //System.out.println("e: " + e + "\npartenza dal clet " + time.getCurrent());

                    //aggiorno i contatori
                    jobInClet--;
                    jobOutClet++;

                    jobProcessedClet++;

                    cletBatchCount++;

                    if(events.get(e).getClassIndex() == 0) {
                        jobInClet_1--;
                        jobOutClet_1++;

                        jobInSystem_1--;
                        jobOutSystem_1++;

                        cletBatchCount_1++;
                        systemBatchCount_1++;
                    }
                    else {
                        jobInClet_2--;
                        jobOutClet_2++;

                        jobInSystem_2--;
                        jobOutSystem_2++;

                        cletBatchCount_2++;
                        systemBatchCount_2++;
                    }

                    /**
                     * CLET
                     * if policy == 1 calcolo transient stats
                     * if policy == 2 calcolo steady-state stats
                     */
                    //TRANSIENT
                    if(policy == 1)
                        statisticsHandler.computeMeanAndStdDeviation(cletStatistics, jobProcessedClet,
                                events.get(e).getExecutionTime());

                        //STEADY
                    else{
                        /**
                         * Se ho analizzato b-elementi, ho un batch completo e posso riazzerare il contatore e procedere
                         * con il calcolo della media del batch.
                         * Il valore calcolato viene aggiunto alla lista nel batchMeanHandler
                         */

                        batchMeanHandler.computeMeanForBatchMean(systemBatchStats, systemBatchCount,
                                events.get(e).getExecutionTime(), area, time, alpha);

                        // calcolo la media con l'A di Welford
                        batchMeanHandler.computeMeanForBatchMean(cletBatchStats, cletBatchCount,
                                events.get(e).getExecutionTime(), areaClet, time, alpha);

                        //controllo la classe del job per aggiornare l'istanza giusta
                        if(events.get(e).getClassIndex() == 0) {
                            batchMeanHandler.computeMeanForBatchMean(cletBatchStats_1, cletBatchCount_1,
                                    events.get(e).getExecutionTime(), areaClet_1, time, alpha);

                            batchMeanHandler.computeMeanForBatchMean(systemBatchStats_1, systemBatchCount_1,
                                    events.get(e).getExecutionTime(), area_1, time, alpha);
                        }
                        else {
                            batchMeanHandler.computeMeanForBatchMean(cletBatchStats_2, cletBatchCount_2,
                                    events.get(e).getExecutionTime(), areaClet_2, time, alpha);

                            batchMeanHandler.computeMeanForBatchMean(systemBatchStats_2, systemBatchCount_2,
                                    events.get(e).getExecutionTime(), area_2, time, alpha);
                        }

                        /**
                         * Calcolo la popolazione media per il batch e la salvo in cletStat.
                         * node = 1 per clet, = 2 per cloud
                         * statisticType = 0 per nodo, = 1 per job1, = 2 per job2, = 3 per movedJob
                         */
                        if (cletBatchCount == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(cletBatchStats);
                            cletBatchCount = 0;
                        }
                        if (cletBatchCount_1 == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(cletBatchStats_1);
                            cletBatchCount_1 = 0;
                        }
                        if (cletBatchCount_2 == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(cletBatchStats_2);
                            cletBatchCount_2 = 0;
                        }
                        if (systemBatchCount == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(systemBatchStats);
                            systemBatchCount = 0;
                        }
                        if (systemBatchCount_1 == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(systemBatchStats_1);
                            systemBatchCount_1 = 0;
                        }
                        if (systemBatchCount_2 == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(systemBatchStats_2);
                            systemBatchCount_2 = 0;
                        }
                    }

                    events.get(e).setStatus(0);         //il server torna idle
                    //System.out.println("server con indice " + e + " torna idle");

                    printer.printEventList(0, events, dimList);
                }
                /**
                 * Partenza dal cloud
                 */
                else {
                    //System.out.println("e: " + e + "\npartenza dal cloud " + time.getCurrent());
                    jobInCloud--;
                    jobOutCloud++;

                    jobProcessedCloud++;

                    cloudBatchCount++;

                    if(events.get(e).getClassIndex() == 0) {
                        jobInCloud_1--;
                        jobOutCloud_1++;

                        jobInSystem_1--;
                        jobOutSystem_1++;

                        cloudBatchCount_1++;
                        systemBatchCount_1++;
                    }
                    else {
                        jobInCloud_2--;
                        jobOutCloud_2++;

                        jobInSystem_2--;
                        jobOutSystem_2++;

                        cloudBatchCount_2++;
                        systemBatchCount_2++;
                    }

                    /**
                     * CLOUD
                     * if policy == 1 calcolo transient stats
                     * if policy == 2 calcolo steady-state stats
                     */
                    if(policy == 1)
                        statisticsHandler.computeMeanAndStdDeviation(cloudStatistics, jobProcessedCloud, events.get(e).getExecutionTime());
                    else{
                        batchMeanHandler.computeMeanForBatchMean(systemBatchStats, systemBatchCount,
                                events.get(e).getExecutionTime(), area, time, alpha);

                        // calcolo la media con l'A di Welford
                        batchMeanHandler.computeMeanForBatchMean(cloudBatchStats, cloudBatchCount,
                                events.get(e).getExecutionTime(), areaCloud, time, alpha);

                        //controllo la classe del job per aggiornare l'istanza giusta
                        if(events.get(e).getClassIndex() == 0) {
                            batchMeanHandler.computeMeanForBatchMean(cloudBatchStats_1, cloudBatchCount_1,
                                    events.get(e).getExecutionTime(), areaCloud_1, time, alpha);

                            batchMeanHandler.computeMeanForBatchMean(systemBatchStats_1, systemBatchCount_1,
                                    events.get(e).getExecutionTime(), area_1, time, alpha);
                        }
                        else {
                            batchMeanHandler.computeMeanForBatchMean(cloudBatchStats_2, cloudBatchCount_2,
                                    events.get(e).getExecutionTime(), areaCloud_2, time, alpha);

                            batchMeanHandler.computeMeanForBatchMean(systemBatchStats_2, systemBatchCount_2,
                                    events.get(e).getExecutionTime(), area_2, time, alpha);
                        }

                        if (cloudBatchCount == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(cloudBatchStats);
                            cloudBatchCount = 0;
                        }

                        if (cloudBatchCount_1 == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(cloudBatchStats_1);
                            cloudBatchCount_1 = 0;
                        }
                        if (cloudBatchCount_2 == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(cloudBatchStats_2);
                            cloudBatchCount_2 = 0;
                        }
                        if(systemBatchCount == batchSize){
                            batchMeanHandler.updateBatchStatistics_2(systemBatchStats);
                            systemBatchCount = 0;
                        }
                        if(systemBatchCount_1 == batchSize){
                            batchMeanHandler.updateBatchStatistics_2(systemBatchStats_1);
                            systemBatchCount_1 = 0;
                        }
                        if(systemBatchCount_2 == batchSize){
                            batchMeanHandler.updateBatchStatistics_2(systemBatchStats_2);
                            systemBatchCount_2 = 0;
                        }
                    }

                    events.get(e).setStatus(0); //il server torna idle
                    //System.out.println("server con indice " + e + " torna idle");
/*
                    events.remove(e);
                    System.out.println("rimosso indice: " + e);
 */
                    printer.printEventList(0, events, dimList);
                }
            }
        }
        //FUORI DAL ROUND

        /*
        printer.printJobStat(jobInSystem, jobInSystem_1, jobInSystem_2, jobInClet, jobInClet_1, jobInClet_2, jobInCloud,
                jobInCloud_1, jobInCloud_2, jobOutSystem, jobOutSystem_1, jobOutSystem_2, jobOutClet, jobOutClet_1,
                jobOutClet_2, jobOutCloud, jobOutCloud_1, jobOutCloud_2, time);
        */
        /*
        printer.printStatistics(time, events, sums, n, jobProcessed, jobProcessedClet, jobProcessedCloud, jobArrived,
                area, areaClet, areaCloud, areaClet1, areaClet2, areaCloud1, areaCloud2);
         */
        if(policy == 1) {
            // CALCOLO STAT POPOLAZIONE DEL ROUND. SALVA IN CLETSTATISTICS E CLOUDSTATISTICS
            statisticsHandler.updateStatistics(time, events, sums, n, jobProcessed, jobProcessedClet, jobProcessedCloud,
                    jobArrived, area, areaClet, areaCloud, areaClet_1, areaClet_2, areaCloud_1, areaCloud_2, cletStatistics,
                    cloudStatistics);
/*
            System.out.println("**stat di fine round");
            System.out.println(cletStatistics.getRespTimeMean());
            System.out.println(cletStatistics.getRespTimeConfidenceInterval());
            //System.out.println(cletStatistics.getTotalJob());

            System.out.println(cloudStatistics.getRespTimeMean());
            System.out.println(cloudStatistics.getRespTimeConfidenceInterval());
            //System.out.println(cloudStatistics.getTotalJob());
*/
            round = round+2;
            init = 1;
            sarrival[0] = 0.0;
            sarrival[1] = 0.0;
        }

        /**
         * Adesso ogni lista dei batch ha le medie di ogni batch. Posso calcolare la media della statistica
         */
        else{
            //calcolo la mean-of-means per ogni statistica
            System.out.println("\nCLET");
            batchMeanHandler.computeMeanOfMeans(cletBatchStats, alpha);
            System.out.println("\nCLET 1");
            batchMeanHandler.computeMeanOfMeans(cletBatchStats_1, alpha);
            System.out.println("\nCLET 2");
            batchMeanHandler.computeMeanOfMeans(cletBatchStats_2, alpha);
            System.out.println("\nCLOUD");
            batchMeanHandler.computeMeanOfMeans(cloudBatchStats, alpha);
            System.out.println("\nCLOUD 1");
            batchMeanHandler.computeMeanOfMeans(cloudBatchStats_1, alpha);
            System.out.println("\nCLOUD 2");
            batchMeanHandler.computeMeanOfMeans(cloudBatchStats_2, alpha);
            System.out.println("\nSYSTEM");
            batchMeanHandler.computeMeanOfMeans(systemBatchStats, alpha);
            System.out.println("\nSYSTEM 1");
            batchMeanHandler.computeMeanOfMeans(systemBatchStats_1, alpha);
            System.out.println("\nSYSTEM 2");
            batchMeanHandler.computeMeanOfMeans(systemBatchStats_2, alpha);

            // stampa valori su file
            /*
            method = "steady";
            alg = "alg1";
            */
            //todo rendere generale la scrittura su file
            printer.printOnFile(cletBatchStats, "cloudlet", method, alg, "global");
            printer.printOnFile(cletBatchStats_1, "cloudlet", method, alg, "class1");
            printer.printOnFile(cletBatchStats_2, "cloudlet", method, alg,"class2");

            printer.printOnFile(cloudBatchStats, "cloud", method, alg, "global");
            printer.printOnFile(cloudBatchStats_1, "cloud", method, alg, "class1");
            printer.printOnFile(cloudBatchStats_2, "cloud", method, alg, "class2");

            printer.printOnFile(systemBatchStats, "system", method, alg, "global");
            printer.printOnFile(systemBatchStats_1, "system", method, alg, "class1");
            printer.printOnFile(systemBatchStats_2, "system", method, alg, "class2");

            //printer.printSteadyStatsOnFile(cletStatistics, cloudStatistics, 1);
        }
    }


    /**
     * Metodo che esegue l'algoritmo 2
     * todo: valutare percentuale job 2 interrotti per scegliere valore matlab
     */
    public void runAlgorithm2(Statistics cletStatistics, Statistics cletStatistics_1, Statistics cletStatistics_2,
                              Statistics cloudStatistics, Statistics cloudStatistics_1, Statistics cloudStatistics_2,
                              Statistics systemStatistics, Statistics systemStatistics_1, Statistics systemStatistics_2) throws IOException {

        BatchMeanHandler batchMeanHandler = BatchMeanHandler.getInstance();

        //number of job in/out of the clet/cloud/system
        long jobInClet = 0, jobInCloud = 0, jobInSystem = 0;
        long jobInClet_1 = 0, jobInClet_2 = 0, jobInCloud_1 = 0, jobInCloud_2 = 0, jobInSystem_1 = 0, jobInSystem_2 = 0;
        long jobOutClet = 0, jobOutCloud = 0, jobOutSystem = 0;
        long jobOutClet_1 = 0, jobOutClet_2 = 0, jobOutCloud_1 = 0, jobOutCloud_2 = 0, jobOutSystem_1 = 0, jobOutSystem_2 = 0;
        long jobArrived = 0;

        //index
        int e;                      //next-event index
        int i;                      //generic index

        double area = 0.0;          //time integrated number in the node
        double area_1 = 0.0;          //time integrated number in the node
        double area_2 = 0.0;          //time integrated number in the node
        double areaClet = 0.0;          //time integrated number in the node
        double areaClet_1 = 0.0;          //time integrated number in the node
        double areaClet_2 = 0.0;          //time integrated number in the node
        double areaCloud = 0.0;          //time integrated number in the node
        double areaCloud_1 = 0.0;          //time integrated number in the node
        double areaCloud_2 = 0.0;          //time integrated number in the node
        double areaMovedClet = 0.0;          //time integrated number in the node

        double setup = 0.8;     //tempo medio di setup

        int batchSize = configuration.getBatchSize();                 //grandezza di ogni batch
        long cletBatchCount = 0;
        long cletBatchCount_1 = 0;
        long cletBatchCount_2 = 0;
        long cloudBatchCount = 0;
        long cloudBatchCount_1 = 0;
        long cloudBatchCount_2 = 0;
        long systemBatchCount = 0;
        long systemBatchCount_1 = 0;
        long systemBatchCount_2 = 0;
        int movedJobCount = 0;
        int movedJob = 0;

        int cont = 0;

        double alpha = configuration.getAlpha();

        String alg = "alg2";

        String method;
        if(policy == 1)
            method = "transient";
        else
            method = "steady";

        /**
         * Strutture per calcolare media e relativo intervallo di confidenza di ogni batch per le statistiche:
         * - tempo di risposta
         * - popolazione
         * - throughput ?
         */
        /*
        Statistics cletBatchStats = new Statistics(0, 0, 0);
        Statistics cletBatchStats_1 = new Statistics(0, 0, 0);
        Statistics cletBatchStats_2 = new Statistics(0, 0, 0);
        */
        Statistics cloudBatchStats = new Statistics(0, 0, 0);
        Statistics cloudBatchStats_1 = new Statistics(0, 0, 0);
        Statistics cloudBatchStats_2 = new Statistics(0, 0, 0);
        Statistics movedBatchStats = new Statistics(0, 0, 0);
        Statistics systemBatchStats = new Statistics(0, 0, 0);
        Statistics systemBatchStats_1 = new Statistics(0, 0, 0);
        Statistics systemBatchStats_2 = new Statistics(0, 0, 0);


        //inizializzo tutti gli stream
        Rngs r = new Rngs();
        r.plantSeeds(123);

        int dimList = n+2;      //dimensione lista eventi arrivo + clet

        /**
         * dichiaro le strutture dati. La struttura usata è la seguente:
         *   - events[0]                       //arrivi classe 1
         *   - events[1]                       //arrivi classe 2
         *   - events[2], ... ,events[N+2]     //server clet
         *   - events[N+3], ...                //server cloud
         */
        ArrayList<Event> events = new ArrayList(dimList); //uso una lista dinamica unica per rappresentare gli eventi.
        ArrayList<Sum> sums = new ArrayList<>(dimList);

        /**
         * Inizializzo la lista di eventi che rappresentano i server del clet e un server del cloud.
         * Per il cloud inizializzo solo l'evento di indice "n+2", gli altri verranno inizializzati se eventualmente
         * verranno richiesti durante l'esecuzione.
         */
        for (i = 0; i < dimList; ++i) {
            Event event = new Event();

            /**
             * Setup degli eventi di arrivo.
             * Per gli eventi di arrivo genero due tempi di partenza (un arrivo di classe 1 e uno di classe 2)
             */
            if (i < 2) {   //setup eventi di arrivo
                event.setNextTime(controllerHandler.getArrival(r, round, sarrival, arrival_rate, i));
                event.setStatus(1);     //status=1 perchè l'arrivo è busy
            }
            else {       //setup server del clet
                event.setNextTime(start);
                event.setStatus(0);         //status=0 significa che il server è idle
            }
            event.setLocation(0);
            events.add(i, event);
        }
        printer.printEventList(0, events, dimList);
        //printer.printSystemStatus(jobInSystem, jobInClet, jobInClet_1, jobInClet_2, jobInCloud,
        //        jobInCloud_1, jobInCloud_2, contInterrotti);

        /**
         * Inizializzo la lista per l'integrale. Ne porto avanti una, alla fine la dividerò in lista per il clet e
         * lista per il cloud.
         */
        for (i = 0; i < dimList; ++i) {
            Sum sum = new Sum();

            sum.setService(0.0);
            sum.setServed(0);

            sums.add(sum);
        }

        SystemTime time = new SystemTime();
        time.setCurrent(start);
        //System.out.println("*** getCurrent & getNext:" + time.getCurrent() + " " + time.getNext() + "***");

        /**
         * Le condizioni di uscita sono:
         * event[0].x == 0               //processo degli arrivi di classe 1 bloccato
         * event[1].x == 0               //processo degli arrivi di classe 2 bloccato
         * number == 0                   //il sistema è vuoto
         */
        while ((events.get(0).getStatus() != 0) || (jobInSystem != 0)) {

            //System.out.println("size lista: " + events.size());
            printer.printEventList(0, events, dimList);

            //todo controllare thr
            /**
             * Ogni x job catturo il numero di job presenti nel sistema.
             */
            /*
            if(jobOutSystem%100 == 0) {
                cletThr_1.add(jobOutClet_1 / time.getCurrent());
                cletThr_2.add(jobOutClet_2/ time.getCurrent());
                cloudThr_1.add(jobOutCloud_1 / time.getCurrent());
                cloudThr_2.add(jobOutCloud_2 / time.getCurrent());
                cletThr.add(jobOutClet / time.getCurrent());
                cloudThr.add(jobOutCloud / time.getCurrent());
            }
            */

            /**
             * Prendo "e", indice dell'evento + imminente
             */
            e = controllerHandler.nextEvent(events);
            if (e == -1) {
                System.out.println("\nBREAK\n");
                break;
            }

            /**
             * Aggiorno time con l'evento da eseguire
             */
            time.setNext(events.get(e).getNextTime());
            //System.out.println("\nnextTime: " + time.getNext());

            //printer.printEventList(0, events, dimList);

            double t = time.getNext() - time.getCurrent();
            area += t * jobInSystem;        //aggiorno l'area totale
            area_1 += t * jobInSystem_1;        //aggiorno l'area totale
            area_2 += t * jobInSystem_2;        //aggiorno l'area totale
            areaClet += t * jobInClet;        //aggiorno l'area per clet
            areaClet_1 += t * jobInClet_1;        //aggiorno l'area per clet
            areaClet_2 += t * jobInClet_2;        //aggiorno l'area per clet
            areaCloud += t * jobInCloud;        //aggiorno l'area per cloud
            areaCloud_1 += t * jobInCloud_1;        //aggiorno l'area per cloud
            areaCloud_2 += t * jobInCloud_2;        //aggiorno l'area per cloud
            areaMovedClet += t * movedJob;  //todo meglio il count dopo aver aggiunto il suo decremento?


            /**
             * Il clock avanza all'evento scelto
             */
            time.setCurrent(time.getNext());

            /**
             * ARRIVO
             */
            if (e == 0 || e == 1) {

                System.out.println("---------");
                System.out.println("Arrivo dell'evento di classe: " + (e+1) +" e tempo " + time.getCurrent());

                jobInSystem++;
                jobArrived++;
                System.out.println("job numero: " + jobArrived);

                /**
                 * Per l'evento di arrivo trattato genero un nuovo tempo di arrivo. I primi due eventi in nextTime()
                 * avranno il tempo di arrivo del prossimo job di classe 1 e 2
                 */
                events.get(e).setNextTime(controllerHandler.getArrival(r, round, sarrival, arrival_rate, e));
                //System.out.println("indice: " + e + " nuovo tempo di arrivo: " + events.get(e).getNextTime());

                /**
                 * Se il tempo di arrivo è oltre lo STOP blocco il processo degli arrivi
                 */
                if (events.get(e).getNextTime() > stop) {
                    events.get(e).setStatus(0);

                    //System.out.println("\n\n*****   ho oltrepassato STOP. Chiusura porte    *****\n\n");
                }

                /**
                 * Arrivo job classe 1
                 */
                if (e == 0) {
                    System.out.println("il job di classe 1 ha tempo " + time.getCurrent());

                    jobInSystem_1++;

                    if (jobInClet_1 == n) {
                        System.out.println("- spedisco job 1 al cloud");

                        jobInCloud++;
                        jobInCloud_1++;

                        sendToCloud2(r, events.get(e), events, sums, e, dimList, time, 0);
                    }

                    else if ((jobInClet_1 + jobInClet_2) < s) {
                        System.out.println("- S rispettata. Accetto nel clet ");

                        jobInClet++;
                        jobInClet_1++;

                        sendToClet(r, events.get(e), events, sums, e, dimList, time);

                    } else if (jobInClet_2 > 0) {
                        System.out.println("- sposto un job 2 dal clet al cloud");

                        jobInClet_1++;
                        jobInClet_2--;

                        jobInCloud++;
                        jobInCloud_2++;

                        movedJobCount++;
                        moveFromCletToCloud(r, events.get(e), events, time, sums, dimList, setup);
                    }
                    else {

                        jobInClet++;
                        jobInClet_1++;

                        sendToClet(r, events.get(e), events, sums, e, dimList, time);
                    }
                }
                /**
                 * Arrivo job classe 2
                 */
                else {
                    //System.out.println("il job di classe 2 ha tempo " + time.getCurrent());

                    jobInSystem_2++;

                    if (jobInClet_1 + jobInClet_2 >= s) {
                        System.out.println("- spedisco al cloud");

                        jobInCloud++;
                        jobInCloud_2++;

                        sendToCloud2(r, events.get(e), events, sums, e, dimList, time, 0);
                    }
                    else {
                        System.out.println("- S rispettata. Accetto nel clet");

                        jobInClet++;
                        jobInClet_2++;

                        cont++;

                        sendToClet(r, events.get(e), events, sums, e, dimList, time);
                    }
                }
                printer.printEventList(0, events, dimList);
            }
            /**
             * PARTENZA
             */
            else {
                System.out.println("\n");

                jobInSystem--;
                jobOutSystem++;

                systemBatchCount++;

                /**
                 * Partenza dal clet
                 */
                if (e > 1 && e < dimList) {
                    System.out.println("----------");
                    System.out.println("Partenza dal clet. Server indice " + e);

                    jobInClet--;
                    jobOutClet++;
                    cletBatchCount++;

                    if (events.get(e).getClassIndex() == 0) {
                        jobInClet_1--;
                        jobOutClet_1++;

                        jobInSystem_1--;
                        jobOutSystem_1++;

                        cletBatchCount_1++;
                        systemBatchCount_1++;

                        //System.out.println("job di classe 1 con dep-time = " + time.getCurrent());
                    }
                    else {
                        jobInClet_2--;
                        jobOutClet_2++;

                        jobInSystem_2--;
                        jobOutSystem_2++;

                        cletBatchCount_2++;
                        systemBatchCount_2++;

                        //System.out.println("job di classe 2 con dep-time = " + time.getCurrent());
                    }

                    /**
                     * CLET
                     * if policy == 1 calcolo transient stats
                     * if policy == 2 calcolo steady-state stats
                     */
                    //TRANSIENT
                    if (policy == 1) {
                        //statisticsHandler.computeMeanAndStdDeviation(cletStatistics, jobProcessedClet, events.get(e).getExecutionTime());
                        statisticsHandler.computeMeanOfMeans(cletStatistics, events.get(e).getExecutionTime(),
                                areaClet, time, (int)jobOutClet, alpha, "clet");

                        statisticsHandler.computeMeanOfMeans(systemStatistics, events.get(e).getExecutionTime(),
                                area, time, (int)jobOutSystem, alpha, "system");

                        if(events.get(e).getClassIndex() == 0) {
                            statisticsHandler.computeMeanOfMeans(cletStatistics_1, events.get(e).getExecutionTime(),
                                    areaClet_1, time, (int) jobOutClet_1, alpha, "clet1");

                            statisticsHandler.computeMeanOfMeans(systemStatistics_1, events.get(e).getExecutionTime(),
                                    area_1, time, (int)jobOutSystem_1, alpha, "system1");
                        }
                        if(events.get(e).getClassIndex() == 1) {
                            statisticsHandler.computeMeanOfMeans(cletStatistics_2, events.get(e).getExecutionTime(),
                                    areaClet_2, time, (int) jobOutClet_2, alpha, "clet2");

                            statisticsHandler.computeMeanOfMeans(systemStatistics_2, events.get(e).getExecutionTime(),
                                    area_2, time, (int)jobOutSystem_2, alpha, "system2");
                        }
                    }
                    //STEADY
                    else {
                        /**
                         * Se ho analizzato b-elementi, ho un batch completo e posso riazzerare il contatore e procedere
                         * con il calcolo della media del batch.
                         * Il valore calcolato viene aggiunto alla lista nel batchMeanHandler
                         */

                        batchMeanHandler.computeMeanForBatchMean(systemBatchStats, systemBatchCount,
                                events.get(e).getExecutionTime(), area, time, alpha);

                        // calcolo la media con l'A di Welford
                        batchMeanHandler.computeMeanForBatchMean(cletStatistics, cletBatchCount,
                                events.get(e).getExecutionTime(), areaClet, time, alpha);

                        //controllo la classe del job per aggiornare l'istanza giusta
                        if(events.get(e).getClassIndex() == 0) {
                            batchMeanHandler.computeMeanForBatchMean(cletStatistics_1, cletBatchCount_1,
                                    events.get(e).getExecutionTime(), areaClet_1, time, alpha);

                            batchMeanHandler.computeMeanForBatchMean(systemBatchStats_1, systemBatchCount_1,
                                    events.get(e).getExecutionTime(), area_1, time, alpha);
                        }
                        else {
                            batchMeanHandler.computeMeanForBatchMean(cletStatistics_2, cletBatchCount_2,
                                    events.get(e).getExecutionTime(), areaClet_2, time, alpha);

                            batchMeanHandler.computeMeanForBatchMean(systemBatchStats_2, systemBatchCount_2,
                                    events.get(e).getExecutionTime(), area_2, time, alpha);
                        }

                        /**
                         * Calcolo la popolazione media per il batch e la salvo in cletStat.
                         * node = 1 per clet, = 2 per cloud
                         * statisticType = 0 per nodo, = 1 per job1, = 2 per job2, = 3 per movedJob
                         */
                        if (cletBatchCount == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(cletStatistics);
                            cletBatchCount = 0;
                        }

                        if (cletBatchCount_1 == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(cletStatistics_1);
                            cletBatchCount_1 = 0;
                        }
                        if (cletBatchCount_2 == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(cletStatistics_2);
                            cletBatchCount_2 = 0;
                        }
                        if (systemBatchCount == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(systemBatchStats);
                            systemBatchCount = 0;
                        }
                        if (systemBatchCount_1 == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(systemBatchStats_1);
                            systemBatchCount_1 = 0;
                        }
                        if (systemBatchCount_2 == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(systemBatchStats_2);
                            systemBatchCount_2 = 0;
                        }
                    }
                    events.get(e).setStatus(0);         //il server torna idle
                    //System.out.println("server con indice " + e + " torna idle");

                    //cletDepartureTimes.add(time.getCurrent());

                    printer.printEventList(0, events, dimList);
                }
                /**
                 * Partenza dal cloud
                 */
                else {
                    /*
                    System.out.println("----------");
                    System.out.println("Partenza dal cloud. Server indice " + e);
*/
                    jobInCloud--;
                    jobOutCloud++;
                    cloudBatchCount++;

                    if(events.get(e).getMoved() == 1)
                        movedJob++;

                    if(events.get(e).getClassIndex() == 0) {
                        jobInCloud_1--;
                        jobOutCloud_1++;

                        jobOutSystem_1++;
                        jobInSystem_1--;

                        cloudBatchCount_1++;
                        systemBatchCount_1++;
/*
                        System.out.println(jobOutCloud_1);
                        System.out.println("job classe 1 con dep-time = " + time.getCurrent());
                        */
                    }
                    else {
                        jobInCloud_2--;
                        jobOutCloud_2++;

                        jobOutSystem_2++;
                        jobInSystem_2--;

                        cloudBatchCount_2++;
                        systemBatchCount_2++;

                        //System.out.println("job classe 2 job con dep-time = " + time.getCurrent());
                    }

                    /**
                     * CLOUD
                     * if policy == 1 calcolo transient stats
                     * if policy == 2 calcolo steady-state stats
                     */
                    if (policy == 1) {
                        //statisticsHandler.computeMeanAndStdDeviation(cloudStatistics, jobProcessedCloud, events.get(e).getExecutionTime());
                        statisticsHandler.computeMeanOfMeans(cloudStatistics, events.get(e).getExecutionTime(),
                                areaCloud, time, (int)jobOutCloud, alpha, "cloud");

                        statisticsHandler.computeMeanOfMeans(systemStatistics, events.get(e).getExecutionTime(),
                                area, time, (int)jobOutSystem, alpha, "system");

                        if(events.get(e).getClassIndex() == 0) {
                            statisticsHandler.computeMeanOfMeans(cloudStatistics_1, events.get(e).getExecutionTime(),
                                    areaCloud_1, time, (int) jobOutCloud_1, alpha, "cloud1");

                            statisticsHandler.computeMeanOfMeans(systemStatistics_1, events.get(e).getExecutionTime(),
                                    area_1, time, (int) jobOutSystem_1, alpha, "system1");
                        }
                        if(events.get(e).getClassIndex() == 1) {
                            statisticsHandler.computeMeanOfMeans(cloudStatistics_2, events.get(e).getExecutionTime(),
                                    areaCloud_2, time, (int) jobOutCloud_2, alpha, "cloud2");

                            statisticsHandler.computeMeanOfMeans(systemStatistics_2, events.get(e).getExecutionTime(),
                                    area_2, time, (int) jobOutSystem_2, alpha, "system2");
                        }
                    }
                    else {
                        batchMeanHandler.computeMeanForBatchMean(systemBatchStats, systemBatchCount,
                                events.get(e).getExecutionTime(), area, time, alpha);

                        // calcolo la media con l'A di Welford
                        batchMeanHandler.computeMeanForBatchMean(cloudBatchStats, cloudBatchCount,
                                events.get(e).getExecutionTime(), areaCloud, time, alpha);

                        //controllo la classe del job per aggiornare l'istanza giusta
                        if(events.get(e).getClassIndex() == 0) {
                            batchMeanHandler.computeMeanForBatchMean(cloudBatchStats_1, cloudBatchCount_1,
                                    events.get(e).getExecutionTime(), areaCloud_1, time, alpha);

                            batchMeanHandler.computeMeanForBatchMean(systemBatchStats_1, systemBatchCount_1,
                                    events.get(e).getExecutionTime(), area_1, time, alpha);
                        }
                        else {
                            batchMeanHandler.computeMeanForBatchMean(cloudBatchStats_2, cloudBatchCount_2,
                                    events.get(e).getExecutionTime(), areaCloud_2, time, alpha);

                            batchMeanHandler.computeMeanForBatchMean(systemBatchStats_2, systemBatchCount_2,
                                    events.get(e).getExecutionTime(), area_2, time, alpha);
                        }

                        if(events.get(e).getMoved() == 1) {
                            batchMeanHandler.computeMeanForBatchMean(movedBatchStats, movedJob,
                                    events.get(e).getExecutionTime(), areaMovedClet, time, alpha);
                        }

                        if (cloudBatchCount == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(cloudBatchStats);
                            cloudBatchCount = 0;
                        }

                        if (cloudBatchCount_1 == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(cloudBatchStats_1);
                            cloudBatchCount_1 = 0;
                        }
                        if (cloudBatchCount_2 == batchSize) {
                            batchMeanHandler.updateBatchStatistics_2(cloudBatchStats_2);
                            cloudBatchCount_2 = 0;
                        }
                        if(movedJob == batchSize){
                            batchMeanHandler.updateBatchStatistics_2(movedBatchStats);
                            movedJob = 0;
                        }
                        if(systemBatchCount == batchSize){
                            batchMeanHandler.updateBatchStatistics_2(systemBatchStats);
                            systemBatchCount = 0;
                        }
                        if(systemBatchCount_1 == batchSize){
                            batchMeanHandler.updateBatchStatistics_2(systemBatchStats_1);
                            systemBatchCount_1 = 0;
                        }
                        if(systemBatchCount_2 == batchSize){
                            batchMeanHandler.updateBatchStatistics_2(systemBatchStats_2);
                            systemBatchCount_2 = 0;
                        }
                    }
                    //cloudDepartureTimes.add(time.getCurrent());

                    events.get(e).setStatus(0); //il server torna idle
                    //System.out.println("server con indice " + e + " torna idle");

                    printer.printEventList(0, events, dimList);
                }
            }

            printer.printEventList(0, events, dimList);

            //printer.printSystemStatus(jobInSystem, jobInClet, jobInClet_1, jobInClet_2, jobInCloud, jobInCloud_1, jobInCloud_2, contInterrotti);
        }
        printer.printSystemStatus(jobInSystem, jobInClet, jobInClet_1, jobInClet_2, jobInCloud, jobInCloud_1, jobInCloud_2);

        printer.printJobStat(jobInSystem, jobInClet, jobInClet_1, jobInClet_2, jobInCloud, jobInCloud_1, jobInCloud_2,
                jobOutSystem, jobOutClet, jobOutClet_1, jobOutClet_2, jobOutCloud, jobOutCloud_1, jobOutCloud_2,
                movedJobCount, time);

        //TRANSIENT
        if(policy == 1) {
/*
        statisticsHandler.updateStatistics(time, events, sums, n, jobProcessed, jobProcessedClet, jobProcessedCloud, jobArrived,
                area, areaClet, areaCloud, areaClet_1, areaClet_2, areaCloud_1, areaCloud_2, cletStatistics, cloudStatistics);
*/
            //statisticsHandler.updateTransientStatistics(cletStatistics, cletStatistics_1, cletStatistics_2, cletStatistics);
        }

        /**
         * Adesso ogni lista dei batch ha le medie di ogni batch. Posso calcolare la media della statistica
         */
        //STEADY
        if(policy == 2){
        //else {
            System.out.println("job 2 totali arrivati nel clet: " + cont);

            System.out.println("\nCLET");
            batchMeanHandler.computeMeanOfMeans(cletStatistics, alpha);
            System.out.println("\nCLET 1");
            batchMeanHandler.computeMeanOfMeans(cletStatistics_1, alpha);
            System.out.println("\nCLET 2");
            batchMeanHandler.computeMeanOfMeans(cletStatistics_2, alpha);
            System.out.println("\nCLOUD");
            batchMeanHandler.computeMeanOfMeans(cloudBatchStats, alpha);
            System.out.println("\nCLOUD 1");
            batchMeanHandler.computeMeanOfMeans(cloudBatchStats_1, alpha);
            System.out.println("\nCLOUD 2");
            batchMeanHandler.computeMeanOfMeans(cloudBatchStats_2, alpha);
            System.out.println("\nMOVED JOB");
            batchMeanHandler.computeMeanOfMeans(movedBatchStats, alpha);
            System.out.println("\nSYSTEM");
            batchMeanHandler.computeMeanOfMeans(systemBatchStats, alpha);
            System.out.println("\nSYSTEM 1");
            batchMeanHandler.computeMeanOfMeans(systemBatchStats_1, alpha);
            System.out.println("\nSYSTEM 2");
            batchMeanHandler.computeMeanOfMeans(systemBatchStats_2, alpha);

            // stampa valori su file
            /*
            method = "steady";
            alg = "alg2";
            */
            printer.printOnFile(cletStatistics, "cloudlet", method, alg, "global");
            printer.printOnFile(cletStatistics_1, "cloudlet", method, alg, "class1");
            printer.printOnFile(cletStatistics_2, "cloudlet", method, alg,"class2");

            printer.printOnFile(cloudBatchStats, "cloud", method, alg, "global");
            printer.printOnFile(cloudBatchStats_1, "cloud", method, alg, "class1");
            printer.printOnFile(cloudBatchStats_2, "cloud", method, alg, "class2");

            printer.printOnFile(systemBatchStats, "system", method, alg, "global");
            printer.printOnFile(systemBatchStats_1, "system", method, alg, "class1");
            printer.printOnFile(systemBatchStats_2, "system", method, alg, "class2");

            //scrive i risultati sul file
            //printer.printSteadyStatsOnFile(cletStatistics, cloudStatistics, algorithm);
        }

        round = round + 2;
        init = 1;
        sarrival[0] = 0.0;
        sarrival[1] = 0.0;
    }


    /**
     * Funzione per trovare un server idle nel clet. Restituisce il primo server idle trovato.
     */
    public int findServer(ArrayList<Event> events, int dimList) {
        int idleServerIndex = 0;    //lo setto a 0 perchè tanto i server del clet partono da i=2
        boolean trovato = false;
        int i;

        //restituisce il primo server libero nel clet
        for(i=2; i<dimList && !trovato; i++){
            if(events.get(i).getStatus() == 0){
                idleServerIndex = i;
                trovato = true;
            }
        }

        return idleServerIndex;
    }


    /**
     * Funzione per assegnare un job al clet. L'idea è di calcolarne il tempo di esecuzione, quindi scorrere la lista
     * nelle posizioni del clet, cioè nell'intervallo [2, N+2] e trovare il primo server libero. Assegno a tale server
     * il job e registro il suo tempo di completamento.
     * "classIndex" = 0/1 ci da la classe del job, perchè event[0] = job di classe 1, event[1] = job di classe 2.
     */
    public void sendToClet(Rngs r, Event event, ArrayList<Event> events, ArrayList<Sum> sums, int classIndex,
                           int dimList, SystemTime time){

        double arrivalTime = time.getCurrent();                  //è il tempo di arrivo dell'evento da eseguire
        double executionTime = getCletService(r, classIndex);    //genero un tempo di esecuzione
        double departureTime = arrivalTime + executionTime;     //tempo di partenza del job

        int idleServer = findServer(events, dimList);            //ottengo l'indice di un server idle del clet
        //System.out.println("idle server scelto: " + idleServer + " per job di indice: " + classIndex);

        if(idleServer == 0) {
            System.out.println("\n\n +++ CLET PIENO +++ \n\n");
        }

        /**
         * Aggiorno la lista sums in questo modo:
         * -     sum[s].service += service;
         * -     sum[s].served++;
         */
        sums.get(idleServer).setService(sums.get(idleServer).getService() + executionTime);
        sums.get(idleServer).setServed(sums.get(idleServer).getServed() + 1);

        /**
         * aggiorno il server idle trovato. Il tempo del prox evento (cioè la sua partenza) rappresentato da "NextTime",
         * è impostato al tempo corrente del clock + il tempo di esecuzione generato.
         * "status" diventa 1 per segnalare che è busy.
         */
/*
        System.out.println("arriveTime: " + arrivalTime + "\nexecutionTime in clet: " + executionTime +
                "\ntotale: " + departureTime);
*/
        events.get(idleServer).setNextTime(departureTime);  //il server conterrà in nextTime il tempo di partenza
        events.get(idleServer).setStatus(1);                //server diventa busy
        events.get(idleServer).setLocation(0);              //è assegnato al clet
        events.get(idleServer).setClassIndex(classIndex);   //classe del job
        events.get(idleServer).setExecutionTime(executionTime);
    }


    /**
     * return the index of the next event type. Memorizzo l'indice del primo server busy, quindi scorro tutti gli altri
     * e controllo chi ha l'evento + imminente. Restituisco l'indice del server opportuno.
     */
/*
    public int nextEvent(ArrayList<Event> events) {
    }
*/

    /**
     * Funzione che sposta un job di classe 2 dal clet al cloud (evento causato dall'arrivo di un job di classe 1 quando
     * n1+n2>=S.
     * L'idea è di scorrere la lista degli eventi, trovo il primo job di classe 2 nel clet, ne copio il contenuto in un
     * nuovo job e lo invio al cloud. "newEvent" è il job di classe 1 arrivato, e il suo contenuto va copiato nella lista
     * alla posizione del job di classe 2 spostato.
     */
    public void moveFromCletToCloud(Rngs r, Event job1Arrived, ArrayList<Event> events, SystemTime time,
                                    ArrayList<Sum> sums, int dimList, double setup) {
        boolean trovato = false;

        int i = 2;
        while(i<dimList && !trovato){
            //prendo nella lista degli eventi il primo job di classe 2 nel clet
            if(events.get(i).getClassIndex() == 1 && events.get(i).getStatus() == 1) {
                System.out.println("sposto il job in posizione " + i + " di next-time " + events.get(i).getNextTime());
                trovato = true;
            }
            else
                i++;
        }

        /**
         * Creo un nuovo evento "eventToBeMoved" e copio in esso l'evento da spostare "events.get(i)", quindi lo invio
         * al cloud. "job1Arrived" è il job di classe 1 per cui è avvenuta la prelazione, quindi lo invio al clet.
         */
        Event eventToBeMoved = new Event();
        copiaEvento(events.get(i), eventToBeMoved);

        events.get(i).setStatus(0);
        sendToClet(r,job1Arrived,events,sums,0,dimList,time);

        //copiaEvento(job1Arrived, events.get(i));

        /**
         * Al job 2 che viene spostato devo aggiungere un setup per lo spostamento. Non lo aggiungo a "getNextTime()"
         * perchè il nextTime c'è il tempo di completamento. Usando timer.getCurrent() ottengo l'istante in cui è stato
         * interrotto.
         */
        eventToBeMoved.setMoved(1);
        sendToCloud2(r, eventToBeMoved, events, sums, 1, dimList, time, setup);
    }


    /**
     * Funzione che copia i campi di un evento "oldEvent" nell'evento "newEvent".
     */
    public void copiaEvento(Event event, Event newEvent) {

        newEvent.setNextTime(event.getNextTime());
        newEvent.setClassIndex(event.getClassIndex());
        newEvent.setLocation(event.getLocation());
        newEvent.setStatus(1);
    }


    /**
     * Funzione per trovare un server idle nel cloud. Restituisce il primo server idle trovato.
     */
    public int findCloudServer(ArrayList<Event> events, int dimList) {
        int idleServerIndex = 0;
        int i;

        for (i = dimList; i < events.size() && idleServerIndex == 0; i++) {
            if (events.get(i).getStatus() == 0) {
                idleServerIndex = i;
            }
        }
        return idleServerIndex;
    }


    /**
     * Metodo per inviare un job al cloud per Alg1
     * @param r
     * @param event
     * @param events
     * @param sums
     * @param classIndex
     * @param dimList
     */
    private void sendToCloud2(Rngs r, Event event, ArrayList<Event> events, ArrayList<Sum> sums, int classIndex,
                              int dimList, SystemTime time, double setup) {

        double arrivalTime = time.getCurrent();                 //tempo di arrivo dell'evento analizzato
        double executionTime = getCloudService(r, classIndex);  //genero un tempo di esecuzine
        double departureTime = arrivalTime + executionTime;     //tempo di partenza

        //se il job è stato spostato aggiungo un setup alla partenza
        if(event.getMoved() == 1) {
            departureTime = departureTime + setup;
        }

/*
        System.out.println("arriveTime: " + arrivalTime + "\nexecutionTime in cloud: " + executionTime +
                "\ntotale: " + departureTime);
*/
        /**
         * controllo se c'è un server disponibile nel cloud, quindi:
         * -    se lo trovo affido a lui l'evento arrivato
         * -    aggiungo alla lista un nuovo server del cloud a cui affido l'evento
         */
        int idleServer = findCloudServer(events, dimList);
        //System.out.println("class index: " + classIndex);

        /**
         * Assegno l'evento ad un server già esistente del cloud. NextTime() dei server contiene il tempo di partenza
         */
        if(idleServer != 0){
            events.get(idleServer).setNextTime(departureTime);
            events.get(idleServer).setExecutionTime(executionTime);
            events.get(idleServer).setClassIndex(classIndex);
            events.get(idleServer).setStatus(1);
            events.get(idleServer).setMoved(event.getMoved());
            //System.out.println("il server di indice " + idleServer + " diventa busy");

            sums.get(idleServer).setService(sums.get(idleServer).getService() + executionTime);
            sums.get(idleServer).setServed(sums.get(idleServer).getServed() + 1);
        }
        /**
         * Aggiungo un nuovo server al cloud e gli assegno l'evento da eseguire
         */
        else{
            Event newEvent = new Event();
            newEvent.setNextTime(departureTime);
            newEvent.setStatus(1);                  //server è busy
            newEvent.setLocation(1);                //il job è nel cloud
            newEvent.setClassIndex(classIndex);     //classe del job
            newEvent.setMoved(event.getMoved());
            events.add(newEvent);

            Sum sum = new Sum();
            sum.setServed(1);
            sum.setService(executionTime);
            sums.add(sum);
        }
    }


    /**
     * generate the next service time in the Cloudlet, with rate 0.45 for class 1 and 0.27 for class 2
     */
    public double getCletService(Rngs r, int type) {
        //todo controllare.Idea per differenziare gli stream
        if(round>0)
            round = round+2;
        r.selectStream(type + round);
        double service = exponential(1/service_rate_clet[type], r);

        return service;
    }


    /**
     * generate the next service time in the Cloud, with rate 0.25 for class 1 and 0.22 for class 2
     */
    public double getCloudService(Rngs r, int type) {
        //todo controllare.Idea per differenziare gli stream
        if(round>0)
            round = round+2;
        r.selectStream(type + round);
        double service = exponential(1/service_rate_cloud[type], r);

        return service;
    }


    /**
     * generate an Exponential random variate, use m > 0.0
     */
    public double exponential(double m, Rngs r) {

        return (-m * Math.log(1.0 - r.random()));
    }
}
