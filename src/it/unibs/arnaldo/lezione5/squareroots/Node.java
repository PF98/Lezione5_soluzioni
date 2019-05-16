package it.unibs.arnaldo.lezione5.squareroots;

/**
 * Interfaccia rappresentante lo scheletro di un nodo dell'albero
 *
 * @author Paolo Faustini <paolo.faustini@ieee.org>
 */
public interface Node {
    /**
     * Metodo che calcola il valore numerico di questo nodo
     * @return Il valore numerico del nodo
     * @throws IllegalArgumentException Se non è possibile effettuare questo calcolo
     */
    public double calcValue() throws IllegalArgumentException;

    /**
     * Metodo che formatta con le parentesi adatte questo nodo
     * @return L'espressione rappresentata da questo nodo con le parentesi
     */
    public String parenthesize();

    /**
     * Metodo che genera un "sottoalbero" a questo nodo, di profondità massima data
     * @param maxDepth La massima profondità dell'albero
     */
    public void randomize(int maxDepth);

    /**
     * Metodo che formatta l'espressione con le sole parentesi necessarie al rispetto dell'ordine delle operazioni
     * @return La stringa formattata
     */
    public String necessaryParentheses();

    /**
     * Metodo che formatta l'espressione con le sole parentesi necessarie al rispetto dell'ordine delle operazioni,
     * data l'espressione del nodo padre
     * @param fatherPrecedence La precedenza dell'operatore del padre
     * @param sameDirectionAndAssoc Parametro che è true se l'operatore ha associatività sinistra (/destra)
     *                              e il figlio è sinistro (/destro), false altrimenti
     * @return La stringa formattata
     */
    public String necessaryParentheses(int fatherPrecedence, boolean sameDirectionAndAssoc);
}
