package it.unibs.arnaldo.lezione5.squareroots;

/**
 * Classe che rappresenti un nodo numerico
 *
 * @author Paolo Faustini <paolo.faustini@ieee.org>
 */
public class ConstNode implements Node {
    private static final int MINRANDOM = 0;
    private static final int MAXRANDOM = 100;
    private static final String ILLEGAL_CONSTANT = "Illegal constant signature: '%s'";


    private boolean isValueSet;
    private double value;
    private String constant;

    /**
     * Costruttore vuoto (serve per la generazione casuale)
     */
    public ConstNode() {}

    /**
     * Costruttore con una costante numerica
     * @param constant Una string rappresentante la costante
     */
    public ConstNode(String constant) {
        this.isValueSet = false;
        this.constant = constant;
    }

    /**
     * Metodo che genera un valore casuale a questo nodo
     * @param maxDepth La massima profondità dell'albero (non serve in questa implementazione perché una volta
     *                 raggiunto un nodo costante, l'albero è concluso)
     */
    @Override
    public void randomize(int maxDepth) {
        int random = (int) (Math.random() * (MAXRANDOM - MINRANDOM)) + MINRANDOM;
        this.value = random;
        this.constant = String.valueOf(random);
    }

    /**
     * Metodo che restituisce il valore di questo nodo. Effettua il parsing su una stringa (se necessario), quindi
     * potrebbe generare un errore
     * @return Il valore di questo nodo
     * @throws IllegalArgumentException Se ci sono problemi nel parsing
     */
    @Override
    public double calcValue() throws IllegalArgumentException {
        if (isValueSet) return value;

        double v;
        try {
            v = Double.parseDouble(this.constant);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(ILLEGAL_CONSTANT, constant));
        }
        this.value = v;
        this.isValueSet = true;
        return v;
    }

    /**
     * Metodo che formatta con le parentesi adatte questo nodo (per una costante non sono necessarie parentesi)
     * Questo tipo di nodo costituisce il passo base della ricorsione
     * @return L'espressione rappresentata da questo nodo con le parentesi
     */
    @Override
    public String parenthesize() {
        return this.constant;
    }

    /**
     * In ogni caso, non mi servono parentesi intorno a una costante (di nuovo, è un passo base della ricorsione)
     * @return L'espressione di questo nodo, ovvero il suo valore
     */
    @Override
    public String necessaryParentheses() {
        return this.constant;
    }

    /**
     * In ogni caso, non mi servono parentesi intorno a una costante (di nuovo, è un passo base della ricorsione)
     * @return L'espressione di questo nodo, ovvero il suo valore
     */
    @Override
    public String necessaryParentheses(int fatherPrecedence, boolean sameDirectionAndAssoc) {
        return this.constant;
    }
}
