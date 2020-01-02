package com.company.handler;

import com.company.entity.Event;
import com.company.utility.Rngs;

import java.util.ArrayList;

import static java.lang.System.exit;

public class ControllerHandler {

    /** ---------------------------------------
     * return the index of the next event type. Memorizzo l'indice del primo server busy, quindi scorro tutti gli altri
     * e controllo chi ha l'evento + imminente. Restituisco l'indice del server opportuno.
     * ---------------------------------------
     */
    public int nextEvent(ArrayList<Event> events) {

        int e = -1;
        int i;
        double tmin = 0;
        boolean trovato = false;

        for(i=0; i<events.size() && !trovato; ++i){
            if (events.get(i).getStatus() == 1) {      //find the index of the first 'active'. Status 0 = idle

                e = i;
                tmin = events.get(i).getNextTime();
                trovato = true;
            }
        }

        if (i < events.size()) {
            /*
            e = i;      //e=indice del primo server busy del clet
            tmin = events.get(e).getNextTime();
             */

            while (i < events.size()) {         //now, check the others to find which
                if ((events.get(i).getStatus() == 1) && (events.get(i).getNextTime() < tmin)) {
                    e = i;
                    tmin = events.get(i).getNextTime();
                }
                i++;
            }
        }

        return e;
    }


    /**
     *
     * @param r
     * @param round
     * @param sarrival
     * @param arrival_rate
     * @param class_index
     */
    public double getArrival(Rngs r, int round, double[] sarrival, double[] arrival_rate, int class_index) {

        //todo da controllare.Idea per differenziare gli stream
        if(round>0)
            round = round+2;
        r.selectStream(round + class_index);
        sarrival[class_index] += exponential(1 / arrival_rate[class_index], r);

        return sarrival[class_index];
    }


    /** ---------------------------------------------------
     * generate an Exponential random variate, use m > 0.0
     * ---------------------------------------------------
     */
    public double exponential(double m, Rngs r) {

        return (-m * Math.log(1.0 - r.random()));
    }
}
