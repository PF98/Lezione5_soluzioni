package it.unibs.arnaldo.lezione5.squareroots;

/**
 * Interfaccia rappresentante un operazione (necessaria per le lambda-expression)
 * Non è un punto fondamentale di questo esercizio, può anche essere ignorato al momento
 *
 * @author Paolo Faustini <paolo.faustini@ieee.org>
 */
public interface Operation {
    /**
     * Metodo che calcola il risultato dell'operazione
     * @param l Il primo parametro
     * @param r Il secondo parametro
     * @return Il risultato
     */
    double calcValue(double l, double r);
}
