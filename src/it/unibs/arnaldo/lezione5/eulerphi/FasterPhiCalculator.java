package it.unibs.arnaldo.lezione5.eulerphi;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe utilizzata per calcolare la funzione phi di eulero
 * Questa classe contiene un'implementazione più rapida rispetto a quelle presenti in PhiCalculator,
 * grazie a un utilizzo più massiccio della programmazione dinamica
 *
 * @author Paolo Faustini <paolo.faustini@ieee.org>
 */
public class FasterPhiCalculator {
    // ArrayList utilizzato per memorizzare i numeri primi già generati
    private ArrayList<Integer> primeNumbers;
    // hashmap contenente tutti i valori già calcolati di phi, per evitare di ricalcolarli in una prossima chiamata della funzione con lo stesso parametro
    private HashMap<Integer, Integer> cached;

    private int original_n;
    private int n;

    // valori da utilizzare per il calcolo di phi
    private boolean hasSingleFactor;
    private int p;
    private int a;
    private int b;

    /**
     * Costruttore
     */
    public FasterPhiCalculator() {
        this.primeNumbers = new ArrayList<>();
        this.cached = new HashMap<>();
        this.cached.put(1, 1);
    }

    /**
     * Metodo ricorsivo per il calcolo della funzione phi
     * @param n Il valore per cui si calcola phi(n)
     * @return phi(n)
     */
    public int fastPhi(int n) {
        // controlliamo subito se il calcolo di phi(n) è già stato fatto
        if (cached.containsKey(n))
            return cached.get(n);

        // chiamata al metodo che scompone n per distinguere i casi n=p^k e n=a*b
        this.decompose(n);

        // se n ha solamente un fattore primo, si può esprimere come p^k
        if (this.hasSingleFactor) {
            // espressione equivalente a (p - 1) * p ^ (k - 1)
            int out = (p - 1) * (n / p);
            // salviamo il valore di phi(n) per usi futuri
            cached.put(n, out);
            return out;
        }

        // se n ha più di un fattore, facciamo il passo ricorsivo con i valori a e b calcolati da this.decompose(n)
        int out = this.fastPhi(a) * this.fastPhi(b);
        // salviamo il valore di phi(n) per usi futuri
        cached.put(n, out);
        return out;
    }

    /**
     * Metodo di appoggio, utilizzato per scomporre n in fattori primi per capire se rientra nel caso
     * n = p^k o nel caso n = a*b
     * Questo metodo modifica molti attributi di questa classe, poi utilizzati da fastPhi(n) per il calcolo di phi:
     * - this.p => il valore di p in n=p^k
     * - this.a => il valore di a in n=a*b
     * - this.b => il valore di b in n=a*b
     * - this.hasSingleFactor => true se n è esprimibile come p^k, false altrimenti
     * @param n Il valore da scomporre
     */
    private void decompose(int n) {
        // salvo il valore di partenza di n...
        this.original_n = n;
        // ... e una versione "modificabile" su cui poter lavorare
        this.n = n;

        // itero su tutti i numeri primi fino ad ora calcolati: per come è strutturato il programma, primeNumbers
        // contiene tutti i primi in ordine (senza saltarne nessuno)
        for (Integer i : primeNumbers) {
            // questo metodo restituisce true se la ricerca dei valori di p,a,b è finita
            if (this.checkFactoring(i))
                return; // e a questo punto posso uscire dal metodo
        }

        // se sono ancora qua a questo punto, significa che i numeri primi che avevo non erano sufficienti: devo
        // generarne di nuovi

        // prendo l'ultimo primo che è stato generato (oppure 1, se primeNumbers è vuoto
        int lastPrime = primeNumbers.isEmpty() ? 1 : primeNumbers.get(primeNumbers.size() - 1);

        // ciclo dal valore successivo di lastPrime fino a quando i*i<n (questa condizione, se raggiunta, mi dice che
        // n è necessariamente un numero primo
        for (int i = lastPrime + 1; i * i <= n; i++) {
            // salto i valori pari maggiori di 2, perché non saranno mai primi
            if (i > 2 && i % 2 == 0) continue;

            // controllo se il numero i è primo:
            if (checkNextPrime(i)) {
                // lo aggiungo alla lista dei numeri primi conosciuti: in questo modo i è necessariamente il numero
                // primo immediatamente successivo all'ultimo che conoscevo
                primeNumbers.add(i);

                // solita ricerca per i valori di p,a,b con il numero primo i appena trovato
                if (this.checkFactoring(i))
                    return;
            }
        }
        // se giungo alla condizione finale i*i>n, allora n è primo.
        // se n è primo, può essere espresso come n^1 => p=n, k=1
        this.hasSingleFactor = true;
        this.p = this.n;
    }

    /**
     * Metodo privato che controlla se un certo numero è primo, data la lista di tutti i
     * numeri primi minori di esso (this.primeNumbers)
     * @param candidate Il numero da testare
     * @return          true se il numero è primo, false altrimenti
     */
    private boolean checkNextPrime(int candidate) {
        // itero su tutti i numeri primi conosciuti
        for (Integer i : primeNumbers) {
            // se vale questa condizione, "candidate" è primo
            if (i * i > candidate) return true;

            // abbiamo trovato un divisore di "candidate", esso non è primo
            if (candidate % i == 0) return false;
        }
        // abbiamo terminato i numeri primi, allora "candidate" è primo
        return true;
    }

    /**
     * Metodo privato fondamentale al calcolo di phi: prova a fattorizzare this.n utilizzando il numero primo "prime",
     * per ricavare il valore di p (o di a,b in alternativa)
     * @param prime Il numero primo da "applicare" a this.n
     * @return true se la ricerca è conclusa (sono stati assegnati i valori di a,b o p),
     *         false se bisogna passare al prossimo numero primo
     */
    private boolean checkFactoring(int prime) {
        // se n non era divisibile per nessuno dei numeri primi precedenti ed è minore del quadrato di "prime", allora
        // non ha nessun divisore oltre a 1 e sé stesso => lo esprimiamo come n^1 (p=n, k=1)
        if (prime * prime > this.original_n) {
            this.hasSingleFactor = true; // un solo fattore: sé stesso
            this.p = this.original_n;
            return true; // abbiamo terminato la ricerca
        }

        // continuo a dividere n per "prime", finché posso
        while (this.n % prime == 0) {
            this.n /= prime;
        }

        // se a questo punto this.n vale 1, significa che il suo unico fattore era "prime" => p="prime"
        if (this.n == 1) {
            this.hasSingleFactor = true; // un solo fattore: "prime"
            this.p = prime;
            return true; // abbiamo terminato la ricerca
        }

        // n era divisibile per "prime", ma ha anche altri divisori
        // dato che siamo usciti dal while, l'attuale valore di this.n non è divisibile per "prime"
        // diamo questo valore a b, mentre assegniamo ad a=n/b.
        // in questo modo, a*b = n/b*b = n
        // inoltre, b è divisibile solo per "prime", mentre a non è divisibile per "prime" => MCD(a, b) = 1
        if (this.n != this.original_n) { // a*b
            this.hasSingleFactor = false; // abbiamo più di un fattore
            this.a = this.original_n / this.n;
            this.b = n;
            return true; // abbiamo terminato la ricerca
        }

        // se nessuna condizione è soddisfatta, significa che n non è divisibile per "prime", ma abbiamo ancora altri
        // tentativi da fare
        return false;
    }
}
