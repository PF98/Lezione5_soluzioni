package it.unibs.arnaldo.lezione5.eulerphi;


import java.util.HashMap;

public class EulerPhi {

    private static final int TESTS = 100000;
    private static final double NANOS_PER_MICRO = 1e6;
    private static final String TIME_TEXT = "Tempo impiegato: %.6f ms%n";

    public static void main(String[] args) {
        System.out.println("Dimostrazione di alcune diverse implementazioni per il calcolo di \u03c6.");
        System.out.println("È previsto il calcolo di tutti i valori di phi da 1 a 100000");
        System.out.printf("Per provare altri valori cambiare la variabile TESTS [valore attuale: %d]%n", TESTS);

        System.out.printf("%nIl programma dovrebbe terminare dopo qualche decina di secondi%n%n");

        System.out.printf("Calcolo dei primi %d valori di \u03c6:%n", TESTS);
        System.out.println();
        long start, diff;
        FasterPhiCalculator fastPhiCalc = new FasterPhiCalculator();
        PhiCalculator phiCalc = new PhiCalculator();

        ////////////////////////////////////////////////////////////////
        System.out.println("Funzione \u03c6 standard (PhiCalculator::phi)");
        start = System.nanoTime();

        for (int n = 1; n < TESTS; n++) {
            phiCalc.phi(n);
        }

        diff = System.nanoTime() - start;
        System.out.printf(TIME_TEXT, diff / NANOS_PER_MICRO);
        System.out.println();
        ////////////////////////////////////////////////////////////////
        System.out.println("Funzione \u03c6 ottimizzata (PhiCalculator::optimizedPhi)");
        start = System.nanoTime();

        for (int n = 1; n < TESTS; n++) {
            phiCalc.optimizedPhi(n);
        }

        diff = System.nanoTime() - start;
        System.out.printf(TIME_TEXT, diff / NANOS_PER_MICRO);
        System.out.println();
        ////////////////////////////////////////////////////////////////
        System.out.println("Funzione \u03c6 dinamica (PhiCalculator::dynamicPhi)");
        start = System.nanoTime();

        for (int n = 1; n < TESTS; n++) {
            phiCalc.dynamicPhi(n);
        }

        diff = System.nanoTime() - start;
        System.out.printf(TIME_TEXT, diff / NANOS_PER_MICRO);
        System.out.println();
        ////////////////////////////////////////////////////////////////
        System.out.println("Funzione \u03c6 dinamica veloce (FasterPhiCalculator::fastPhi)");
        start = System.nanoTime();

        for (int n = 1; n < TESTS; n++) {
            fastPhiCalc.fastPhi(n);
        }

        diff = System.nanoTime() - start;
        System.out.printf(TIME_TEXT, diff / NANOS_PER_MICRO);
        System.out.println();
        ////////////////////////////////////////////////////////////////

    }


}
