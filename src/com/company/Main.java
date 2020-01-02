package com.company;

import com.company.handler.BatchMeanHandler;
import com.company.handler.TransientHandler;
import com.company.utility.Configuration;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) throws IOException {

        int algType;    //"1" per algoritmo 1, "2" per algoritmo 2

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
}
