package it.unibs.arnaldo.lezione5.squareroots;

/**
 * Classe che implementa un albero binario rappresentante un'espressione matemeatica
 *
 * @author Paolo Faustini <paolo.faustini@ieee.org>
 */
public class Tree implements NodeParser {
    private OperatorTable opers;
    private Node root;

    /**
     * Costruttore
     * @param opers Un oggetto OperatorTable contenente le informazioni sugli operatori
     */
    public Tree(OperatorTable opers) {
        this.opers = opers;
    }

    /**
     * Metodo che genera casualmente un albero binario di massima profondità data
     * @param maxDepth La profondità massima dell'albero
     */
    public void randomize(int maxDepth) {
        this.root = OperatorNode.startRandomization(maxDepth, this.opers);
    }

    /**
     * Metodo che genera un albero binario a partire da una data espressione matematica
     * @param s Una String contenente l'espressione matematica
     * @throws IllegalArgumentException Se ci sono problemi di formattazione della stringa
     */
    public void parseExpression(String s) throws IllegalArgumentException {
        this.root = this.parse(s, this.opers);
    }

    /**
     * Metodo che restituisce il valore computato dell'albero binario
     * @return Il risultato dell'operazione
     * @throws IllegalArgumentException Se ci sono problemi nelle costanti numeriche
     */
    public double calculate() throws IllegalArgumentException{
        return this.root.calcValue();
    }

    /**
     * Metodo che restituisce la rappresentazione sotto forma di espressione dell'albero binario, con tutte le
     * parentesi, anche se non sono strettamente necessarie
     * @return Una String contenente l'espressione con le parentesi
     */
    public String parenthesize() {
        return this.root.parenthesize();
    }
    /**
     * Metodo che restituisce la rappresentazione sotto forma di espressione dell'albero binario,
     * solamente con le parentesi strettamente necessarie
     * @return Una String contenente l'espressione con le parentesi
     */
    public String necessaryParentheses() {
        return this.root.necessaryParentheses();
    }
}
