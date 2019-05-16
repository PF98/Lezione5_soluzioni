package it.unibs.arnaldo.lezione5.eulerphi;

/**
 * Classe che implementa tre differenti metodi di calcolo per la funzione phi di Eulero.
 * La funzione phi di Eulero è definita come la funzione che conta la quantità di naturali
 * positivi che siano coprimi con un numero naturale positivo n, compresi fra 1 ed n.
 * Ps. Anche il numero 1 viene contato.
 *
 * @author Michele Dusi <michele.dusi.it@ieee.org>
 *
 */
public class PhiCalculator {

    private static final int CACHE_SIZE = 5000;
    private int [] alreadyEvaluated;

    public PhiCalculator() {
        alreadyEvaluated = new int[CACHE_SIZE];
    }

    /**
     * Metodo "base" per il calcolo della funzione phi,
     * utilizza l'espressione ricorsiva della funzione.
     *
     * @param n Il valore di cui calcolare la funzione.
     * @return phi(n).
     */
    public int phi(int n) {
        // Caso particolare n = 1
        if (n == 1) {
            return 1;
        }

        // Comincio a controllare tutti i numeri compresi fra 1 ed n (estremi esclusi)
        for (int a = 2; a < n; a++) {
            // Controllo che a sia un divisore di n
            if (n % a == 0) {
                // A questo punto, ha senso fare la divisione intera fra n e a per trovare b.
                int b = n / a;
                // Calcolo l'MCD fra a e b e verifico che siano coprimi.
                if (mcd(a, b) == 1) {
                    // Ho verificato le condizioni per il passo ricorsivo, lo applico:
                    return phi(a) * phi(b);
                }
            }
        }

        // Se sono uscito dal ciclo precedente senza ritornare alcun valore, sono nel passo base
        // Trovo il primo divisore
        int d;
        for (d = 2; d < n; d++) {
            if (n % d == 0) {
                // Appena trovo un divisore, esco dal ciclo.
                break;
            }
        }
        // Restituisco il valore previsto dalla formula modificata.
        return (d - 1) * n / d;

    }

    /**
     * Metodo "ottimizzato" per il calcolo della funzione phi,
     * utilizza l'espressione ricorsiva della funzione e, dove può, attua alcune soluzioni
     * che permettono di risparmiare istruzioni o cicli.
     *
     * @param n Il valore di cui calcolare la funzione.
     * @return phi(n).
     */
    public int optimizedPhi(int n) {
        // Caso particolare n = 1
        if (n == 1) {
            return 1;
        }

        // Creo un flag che si ricordi se ho già incontrato o meno il primo divisore di n
        boolean foundFirstDivisor = false;
        // Creo una variabile destinata a contenere il primo divisore.
        // La inizializzo ad n poiché è il valore che non viene raggiunto dal ciclo successivo, mentre a me serve che sia considerato.
        int d = n;

        // Comincio a controllare tutti i numeri compresi fra 1 ed n (estremi esclusi)
        for (int a = 2; a < n; a++) {
            // Controllo che a sia un divisore di n
            if (n % a == 0) {
                // Se è il primo divisore che trovo, lo salvo. Potrebbe servirmi nel caso n sia una potenza di primo.
                if (!foundFirstDivisor) {
                    d = a;
                    foundFirstDivisor = true;
                }
                // A questo punto, calcolo l'MCD.
                int aux_a = a;
                int aux_b = n / a;
                int mcd;

                // Forzo l'MCD fra i due fattori di n ad essere uno, continuando a dividere b per l'MCD
                // E continuando a moltiplicare a per l'MCD.
                while ((mcd = mcd(aux_a, aux_b)) != 1) {
                    aux_a *= mcd;
                    aux_b /= mcd;
                }
                // Se non ho diviso interamente b (fino a raggiungere 1), posso applicare il passo ricorsivo.
                if (aux_b != 1) {
                    return optimizedPhi(aux_a) * optimizedPhi(aux_b);
                }
            }
        }

        // Restituisco il valore previsto dalla formula modificata.
        // Poiché sono sicuro di essere nel caso base
        return (d - 1) * n / d;

    }

    /**
     * Calcolo della funzione Phi di Eulero che utilizza la programmazione dinamica.
     * Se un valore è già stato calcolato in precedenza posso ritrovarlo nell'array che man mano riempio.
     *
     * @param n Il valore di cui calcolare la funzione.
     * @return phi(n).
     */
    public int dynamicPhi(int n) {

        // Caso particolare n = 1
        if (n == 1) {
            return 1;
        }

        // Creo un flag che si ricordi se ho già incontrato o meno il primo divisore di n
        boolean foundFirstDivisor = false;
        // Creo una variabile destinata a contenere il primo divisore.
        // La inizializzo ad n poiché è il valore che non viene raggiunto dal ciclo successivo, mentre a me serve che sia considerato.
        int d = n;

        // Comincio a controllare tutti i numeri compresi fra 1 ed n (estremi esclusi)
        for (int a = 2; a < n; a++) {
            // Controllo che a sia un divisore di n
            if (n % a == 0) {
                if (!foundFirstDivisor) {
                    d = a;
                    foundFirstDivisor = true;
                }
                // A questo punto, memorizzo l'mcd.
                int aux_a = a;
                int aux_b = n / a;
                int mcd;

                while ((mcd = mcd(aux_a, aux_b)) != 1) {
                    aux_a *= mcd;
                    aux_b /= mcd;
                }
                if (aux_b != 1) {
                    int phi_a =
                            (aux_a < CACHE_SIZE && alreadyEvaluated[aux_a] != 0) ?
                                    alreadyEvaluated[aux_a] :
                                    dynamicPhi(aux_a);
                    int phi_b =
                            (aux_b < CACHE_SIZE && alreadyEvaluated[aux_b] != 0) ?
                                    alreadyEvaluated[aux_b] :
                                    dynamicPhi(aux_b);

                    int result = phi_a * phi_b;
                    if (n < CACHE_SIZE) {
                        this.alreadyEvaluated[n] = result;
                    }
                    return result;
                }
            }
        }

        // Restituisco il valore previsto dalla formula modificata.
        // Poiché sono sicuro di essere nel caso base
        int result = (d - 1) * n / d;
        if (n < CACHE_SIZE) {
            this.alreadyEvaluated[n] = result;
        }
        return result;
    }

    /**
     * Restituisce il Massimo Comun Divisore fra i due numeri,
     * utilizzando per la computazione l'algoritmo di Euclide in versione iterativa.
     *
     * @param a Il primo numero.
     * @param b Il secondo numero.
     * @return Il Massimo Comun Divisore fra a e b, ossia MCD(a, b).
     */
    private int mcd(int a, int b) {
        int resto;
        while (b != 0) {
            resto = a % b;
            a = b;
            b = resto;
        }
        return a;
    }

}
