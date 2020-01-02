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
     * Print the quantity of job in the system, in the cloudlet and in the cloud at the end of the simulation. Then print
     * the throughput for the system, the cloudlet and the cloud for each type of job.
     */
    public void printJobStat(long jobInSystem, long jobInClet, long jobInClet_1, long jobInClet_2, long jobInCloud,
                             long jobInCloud_1, long jobInCloud_2, long jobOutSystem, long jobOutClet, long jobOutClet_1,
                             long jobOutClet_2, long jobOutCloud, long jobOutCloud_1, long jobOutCloud_2, int jobInterrotti,
                             SystemTime time) {

        System.out.println("# OF JOBS:\n - in system: " + jobInSystem +
                "\n - in clet: " + jobInClet +
                "\n - 1 in clet: " + jobInClet_1 + "\n - 2 in clet: " + jobInClet_2 + "\n - in cloud: " +
                jobInCloud + "\n - 1 in cloud: " + jobInCloud_1 + "\n - 2 in cloud: " + jobInCloud_2 +
                "\n - out of system: " + jobOutSystem + "\n - out clet: " + jobOutClet + "\n - 1 out clet: " +
                jobOutClet_1 + "\n - 2 out clet: " + jobOutClet_2 + "\n - out cloud: " + jobOutCloud +
                "\n - 1 out cloud: " + jobOutCloud_1 + "\n - 2 out cloud: " + jobOutCloud_2 +
                "\n - 2 interrotti: " + jobInterrotti);

        double currentTime = time.getCurrent();

        System.out.println("\nTHROUGHPUT:\n - system: " + jobOutSystem/currentTime +
                "\n - clet: " + jobOutClet/currentTime + "\n - clet job1: " + jobOutClet_1/currentTime + "\n - clet job2: " +
                jobOutClet_2/currentTime + "\n - cloud: " + jobOutCloud/time.getCurrent() + "\n - cloud job1: " +
                jobOutCloud_1/currentTime + "\n - cloud job2: " + jobOutCloud_2/currentTime);
    }


    public void printSystemStatus(long jobInSystem, long jobInClet, long jobInClet_1, long jobInClet_2, long jobInCloud,
                                  long jobInCloud_1, long jobInCloud_2) {

        System.out.println("\njob in system: " + jobInSystem +
                "\n job in clet: " + jobInClet + "  job1 in clet: " + jobInClet_1 + "  job2 in clet: " + jobInClet_2 +
                "\n job in cloud: " + jobInCloud + "  job1 in cloud: " + jobInCloud_1 + "  job2 in cloud: " +
                jobInCloud_2);
    }


    public void printOnFile(Statistics statistics, String node, String policy, String alg, String target) throws IOException {
        String path = "C:\\Users\\Simone\\Desktop\\simulazione_pmcsn\\";

        String meanPath = node + "_mean_" + target + "_" + policy + "_" + alg + ".txt";
        String confIntPath = node + "_confInt_" + target + "_" + policy + "_" + alg + ".txt";

        // RESPONSE TIME
        writeFile(path + "responseTime\\" + meanPath , statistics.getRespTimeMeanList());
        writeFile(path + "responseTime\\" + confIntPath, statistics.getRespTimeConfidenceIntervalList());

        // POPULATION
        writeFile(path + "population\\" + meanPath , statistics.getPopulationMeanList());
        writeFile(path + "population\\" + confIntPath, statistics.getPopulationConfIntList());
    }


    private void writeFile(String path, ArrayList<Double> list) throws IOException {
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


    public void printStatOnFile(Statistics cletStatistics, Statistics cletStatistics_1, Statistics cletStatistics_2,
                                Statistics cloudStatistics, Statistics cloudStatistics_1, Statistics cloudStatistics_2,
                                Statistics systemStatistics, Statistics systemStatistics_1, Statistics systemStatistics_2,
                                String method, String alg) throws IOException {

        printOnFile(cletStatistics, "cloudlet", method, alg, "global");
        printOnFile(cletStatistics_1, "cloudlet", method, alg, "class1");
        printOnFile(cletStatistics_2, "cloudlet", method, alg,"class2");

        printOnFile(cloudStatistics, "cloud", method, alg, "global");
        printOnFile(cloudStatistics_1, "cloud", method, alg, "class1");
        printOnFile(cloudStatistics_2, "cloud", method, alg, "class2");

        printOnFile(systemStatistics, "system", method, alg, "global");
        printOnFile(systemStatistics_1, "system", method, alg, "class1");
        printOnFile(systemStatistics_2, "system", method, alg, "class2");
    }
}
