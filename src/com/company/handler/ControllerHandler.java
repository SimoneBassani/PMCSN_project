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
/*
        int e;
        int i = 0;
        double tmin;

        //todo errore in esecuzione. Esce fuori dall'array all'ultimo giro o dopo
        while (events.get(i).getStatus() == 0 && i<events.size()){       //find the index of the first 'active'. Status 0 = idle
            i++;                        // element in the event list
        }

        e = i;      //e=indice del primo server busy del clet
        tmin = events.get(e).getNextTime();
        i++;

        while (i < events.size()) {         //now, check the others to find which
            //System.out.println("while con i:" + i);
            if ((events.get(i).getStatus() == 1) && (events.get(i).getNextTime() < tmin)) {
                e = i;
                tmin = events.get(i).getNextTime();
            }
            i++;
        }
        //System.out.println("tmin: " + tmin);

        return e;
 */
        int e = -1;
        int i;
        double tmin = 0;
        boolean trovato = false;

        //System.out.println("events.size: " + events.size());
        for(i=0; i<events.size() && !trovato; ++i){
            if (events.get(i).getStatus() == 1) {      //find the index of the first 'active'. Status 0 = idle

                e = i;
                tmin = events.get(i).getNextTime();
                trovato = true;

                //System.out.println("tmin 1° giro: e = " + e + "  tmin = " + tmin);
            }
        }

        //System.out.println("i: " + i);

        if (i < events.size()) {
            /*
            e = i;      //e=indice del primo server busy del clet
            tmin = events.get(e).getNextTime();
             */

            while (i < events.size()) {         //now, check the others to find which
                //System.out.println("while con i:" + i);
                if ((events.get(i).getStatus() == 1) && (events.get(i).getNextTime() < tmin)) {
                    e = i;
                    tmin = events.get(i).getNextTime();
                }
                i++;
            }
        }
        //System.out.println("tmin alla fine del giro: e = " + e + "  tmin = " + tmin);

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

    public boolean controllaContatori(ArrayList<Event> events, long jobInClet, long jobInClet_1, long jobInClet_2,
                                   long jobInCloud, long jobInCloud_1, long jobInCloud_2, int dimList) {
        int clet=0, clet1=0, clet2=0, cloud=0, cloud1=0, cloud2=0;
        boolean trovato = false;

        int i;
        for(i=2; i<dimList; ++i){
            if(events.get(i).getStatus()==1){
                clet++;
                if(events.get(i).getClassIndex()==0)
                    clet1++;
                else
                    clet2++;
            }
        }

        for(i=dimList; i<events.size(); ++i){
            if(events.get(i).getStatus()==1){
                cloud++;
                if(events.get(i).getClassIndex()==0)
                    cloud1++;
                else
                    cloud2++;
            }
        }

        if(clet != jobInClet) {
            System.out.println("clet = " + clet);
            trovato = true;
        }
        if(clet1 != jobInClet_1) {
            System.out.println("clet1 = " + clet1);
            trovato = true;
        }
        if(clet2 != jobInClet_2) {
            System.out.println("clet2 = " + clet2);
            trovato = true;
        }
        if(cloud != jobInCloud) {
            System.out.println("cloud = " + cloud);
            trovato = true;
        }
        if(cloud1 != jobInCloud_1) {
            System.out.println("cloud1 = " + cloud1);
            trovato = true;
        }
        if(cloud2 != jobInCloud_2) {
            System.out.println("cloud2 = " + cloud2);
            trovato = true;
        }

        return trovato;
    }

/*
        Configuration configuration = new Configuration()

        double start = configuration.getSTART();         //start time
        double stop = configuration.getSTOP();          //terminal time
        double sarrival[] = {start, start};             //tempi di arrivo dei job

        int n = configuration.getN();           //number of servers
        int s = configuration.getS();           //limite job in clet per alg2

        double arrival_rate[] = {configuration.getL1(), configuration.getL2()};                     //tassi di arrivo dei job
        double service_rate_clet[] = {configuration.getM1_clet(), configuration.getM2_clet()};      //tassi di servizio nel clet
        double service_rate_cloud[] = {configuration.getM1_cloud(), configuration.getM2_cloud()};   //tassi di servizio nel cloud

        double alpha = configuration.getAlpha();         //livello di confidenza

        int init = 1;                       //lo uso per generare i primi due arrivi
        int count1 = 0;                     //contatori per il numero di job processati nel clet per ogni classe
        int count2 = 0;
        int stampa = 1;                     //se pari a 0 non si stampa, altrimenti si
        int round = 0;                        //contatore per il numero di repliche


        /**
         * Funzione per assegnare un job al clet. L'idea è di calcolarne il tempo di esecuzione, quindi scorrere la lista
         * nelle posizioni del clet, cioè nell'intervallo [1, N+1] e trovare il primo server libero. Assegno a tale server
         * il job e registro il suo tempo di completamento.
         */
 /*       public void sendToClet(Rngs r, Event event, ArrayList<Event> events, SystemTime time, ArrayList<Sum> sums){

                double executionTime = getCletService(r, event.getClassIndex());    //genero un tempo di esecuzione

                int idleServer = findServer(events);    //ottengo l'indice di un server idle del clet
                System.out.println("idle server scelto: " + idleServer);

                //aggiorno sum
                //sum[s].service += service;
                //sum[s].served++;

                sums.get(idleServer).setService(sums.get(idleServer).getService() + executionTime);
                sums.get(idleServer).setServed(sums.get(idleServer).getServed() + 1);

        /*aggiorno il server idle trovato. Il tempo del prox evento (cioè la sua partenza) rappresentato da "NextTime",
        è impostato al tempo corrente del clock + il tempo di esecuzione generato.
        "status" diventa 1 per segnalare che è busy.
        */
/*                events.get(idleServer).setNextTime(time.getCurrent() + executionTime);
                events.get(idleServer).setStatus(1);
                events.get(idleServer).setClassIndex(events.get(0).getClassIndex());
                events.get(idleServer).setLocation(0);
                events.get(idleServer).setExecutionTime(executionTime);

                //System.out.println("***** classe del job arrivato " + events.get(idleServer).getClassIndex());
        }


        /**
         * Funzione per trovare un server idle nel clet. Restituisce il primo server idle trovato.
         * todo ricerca del server inutilizzato da + tempo
         */
/*        public int findServer(ArrayList<Event> events) {
                int idleServer = 0;
                int i;

                //restituisce il primo server libero nel clet
                for(i=1; i<=n+1 && idleServer == 0; i++){
                        if(events.get(i).getStatus() == 0){
                                idleServer = i;
                        }
                }

                //todo se non si vuole il primo libero ma quello libero da + tempo usare questa seconda parte
        /*
        while(i<N+1){
            if(events.get(i).getStatus() == 0) {
                idleServer = i;
        }
        */
/*
                if(idleServer == 0)
                        System.out.println("non è stato trovato un server idle nel clet");

                return idleServer;
        }


        /**
         * Funzione che manda un job al cloud. Crea un nuovo evento che viene inizializzato nel modo opportuno, dunque lo
         * inserisce in fondo alla lista. La location di tali job è 1.
         */
 /*       public void sendToCloud(Rngs r, Event event, ArrayList<Event> events, SystemTime time, ArrayList<Sum> sums) {

                Printer printer = new Printer();

                double executionTime = getCloudService(r, event.getClassIndex());

                //todo n+1 o n+2 ?
                sums.get(n+2).setService(sums.get(n+2).getService() + executionTime);
                sums.get(n+2).setServed(sums.get(n+2).getServed() + 1);

                /**
                 * "NextTime" rappresenta il prox evento, cioè la partenza del job arrivato. E' impostato al clock corrente + il
                 * tempo di esecuzione generato
                 */
  /*              events.get(n+2).setNextTime(time.getCurrent() + executionTime);
                events.get(n+2).setStatus(1);
                events.get(n+2).setLocation(1);
                events.get(n+2).setExecutionTime(executionTime);

                printer.printEventList(stampa, events);
        }


        /** --------------------------------------------------------------
         * generate the next arrival time, with rate 4 for class 1 and rate 6.25 for class 2.
         * Il parametro "round" tiene conto del numero della replicazione in modo da utilizzare due stream
         * diversi per ognuna. In questo modo si hanno valori diversi per ogni run.
         * --------------------------------------------------------------
         */
  /*      public Event getArrival(Rngs r, int round) {

                //System.out.println("getArrival");

                int class_index;

                if(init == 1) {
                        //System.out.println("produco i primi arrivi");
                        r.selectStream(round);
                        sarrival[0] += exponential(1 / arrival_rate[0], r);

                        r.selectStream(round+1);
                        sarrival[1] += exponential(1 / arrival_rate[1], r);

                        init = 0;   //ho generato i primi arrivi, non entrerò + qui

                        //System.out.println("sarrival[0] e [1]: " + sarrival[0] + " " + sarrival[1]);
                }

                //System.out.println("decido tra arrivo di classe 1 o 2");
                if(sarrival[0] < sarrival[1]) {
                        class_index = 0;
                        count1++;
                }
                else {
                        class_index = 1;
                        count2++;
                }

                //aggiorno un elemento event con il tempo di arrivo e la classe di arrivo
                Event event = new Event();
                event.setNextTime(sarrival[class_index]);
                event.setClassIndex(class_index);
                event.setStatus(1);
                event.setLocation(0);

                r.selectStream(class_index);
                sarrival[class_index] += exponential(arrival_rate[class_index], r);

                //System.out.println("sarrival[class_index]: " + sarrival[class_index]);

                return event;
        }


        /** ------------------------------
         * generate the next service time in the Cloudlet, with rate 0.45 for class 1 and 0.27 for class 2
         * ------------------------------
         */
   /*     public double getCletService(Rngs r, int type) {

                r.selectStream(type + 2);
                double service = exponential(1/service_rate_clet[type], r);
                return service;
        }


        /** ------------------------------
         * generate the next service time in the Cloud, with rate 0.25 for class 1 and 0.22 for class 2
         * ------------------------------
         */
  /*      public double getCloudService(Rngs r, int type) {

                r.selectStream(3);
                double service = exponential(1/service_rate_cloud[type], r);
                return service;
        }


        /** ---------------------------------------------------
         * generate an Exponential random variate, use m > 0.0
         * ---------------------------------------------------
         */
   /*     public double exponential(double m, Rngs r) {

                return (-m * Math.log(1.0 - r.random()));
        }


        /** ---------------------------------------
         * return the index of the next event type. Memorizzo l'indice del primo server busy, quindi scorro tutti gli altri
         * e controllo chi ha l'evento + imminente. Restituisco l'indice del server opportuno.
         * ---------------------------------------
         */
  /*      public int nextEvent(ArrayList<Event> events) {
/*
        int e;
        int i = 0;
        double tmin;

        while (events.get(i).getStatus() == 0){       //find the index of the first 'active'. Status 0 = idle
            System.out.println("i = " + i);
            i++;                        // element in the event list
        }
        System.out.println("fine while");
        e = i;      //e=indice del primo server busy del clet
        tmin = events.get(e).getNextTime();
        i++;
        while (i < events.size()) {         //now, check the others to find which
            //System.out.println("while con i:" + i);
            if ((events.get(i).getStatus() == 1) && (events.get(i).getNextTime() < tmin))
                e = i;
            i++;
        }
        return e;
*/

      /*          int e = -1;
                double tmin = -1;
                int i;

                for(i=0; i<events.size(); i++){

                        //System.out.println("****************");
                        //System.out.println("nextEvent for");

                        //1° ciclo per settare il min
                        if(e == -1){

                                //System.out.println("nextEvent for e = -1");

                                if(events.get(i).getStatus() == 1) {

                                        //System.out.println("event.get(i).getstatus = 1");

                                        tmin = events.get(i).getNextTime();
                                        e = i;
                                        //todo è giusto il continue??
                                        //continue;
                                        //System.out.println("ho trovato evento all'indice e: " + e);
                                }
                        }
                        //2° ciclo per terminare la ricerca nella seconda parte della lista
                        if(e != -1){

                                //System.out.println("2° giro");

                                if(events.get(i).getStatus() != 0 && events.get(i).getNextTime()<tmin){

                                        tmin = events.get(i).getNextTime();
                                        e = i;
                                        //System.out.println("ho trovato evento all'indice e: " + e);
                                }
                        }
                }

                //System.out.println(e);
                if(e == -1) {
                        System.out.println("*****");
                        System.out.println("EVENTI FINITI e: " + e + " tmin: " + tmin);
                        System.out.println("*****");
                }
                return e;

        }


        /** Funzione che sposta un job di classe 2 dal clet al cloud (evento causato dall'arrivo di un job di classe 1 quando
         * n1+n2>=S.
         * L'idea è di scorrere la lista degli eventi, trovo il primo job di classe 2 nel clet, ne copio il contenuto in un
         * nuovo job e lo invio al cloud. "newEvent" è il job di classe 1 arrivato, e il suo contenuto va copiato nella lista
         * alla posizione del job di classe 2 spostato.
         */
   /*     public void moveFromCletToCloud(Rngs r, Event newEvent, ArrayList<Event> events, SystemTime time, ArrayList<Sum> sums) {
                int i, found = 0;

                for(i=0; i<events.size() && found==0; i++){

                        //prendo nella lista degli eventi il primo job di classe 2 nel clet
                        if(events.get(i).getLocation() == 0 && events.get(i).getClassIndex() == 1)
                                found = 1;
                }

                System.out.println("sposto l'evento in posizione i: " + i);
                Event class2Event = new Event();
                copiaEvento(events.get(i), class2Event);

                copiaEvento(newEvent, events.get(i));

                //todo gestire tempo di setup dovuto  allo spostamento
                sendToCloud(r, class2Event, events, time, sums);
        }


        /**
         * Funzione che copia i campi di un evento "oldEvent" nell'evento "newEvent".
         */
   /*     public void copiaEvento(Event oldEvent, Event newEvent) {

                newEvent.setStatus(oldEvent.getStatus());
                newEvent.setNextTime(oldEvent.getNextTime());
                newEvent.setClassIndex(oldEvent.getClassIndex());
                newEvent.setLocation(oldEvent.getLocation());
        }
    */
}
