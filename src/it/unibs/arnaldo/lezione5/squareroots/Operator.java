package it.unibs.arnaldo.lezione5.squareroots;

/**
 * Classe rappresentante un operatore. Esso sarà munito di identificatore, un livello di precedenza e
 * un'operazione (quest'ultima non rientra nei punti fondamentali di questo esercizio, può essere ignorata
 * per il momento)
 *
 * @author Paolo Faustini <paolo.faustini@ieee.org>
 */
public class Operator {
    private String identifier;
    private int precedence;
    private Operation oper;

    /**
     * Costruttore, data la stringa dell'identificatore e l'operazione matematica
     * @param identifier Stringa costituente l'identificatore dell'operatore
     * @param oper Operazione binaria dell'operatore
     */
    public Operator(String identifier, Operation oper) {
        this.identifier = identifier;
        this.oper = oper;
    }

    /**
     * Getter dell'identificatore dell'operatore
     * @return L'identificatore dell'operatore
     */
    public String getId() {
        return this.identifier;
    }

    /**
     * Restituisce la lunghezza della stringa dell'identificatore
     * @return La lunghezza dell'identificatore
     */
    public int getIdLength() {
        return this.identifier.length();
    }

    /**
     * Getter del livello di precedenza dell'operatore
     * @return Il livello di precedenza
     */
    public int getPrecedence() {
        return this.precedence;
    }

    /**
     * Getter dell'operazione matematica di questo operatore
     * @return L'operazione
     */
    public Operation getOperation() {
        return this.oper;
    }

    /**
     * Setter del livello di precedenza
     * @param precedence Il livello di precedenza da impostare
     */
    public void setPrecedence(int precedence) {
        this.precedence = precedence;
    }

    /**
     * Metodo che, data una stringa contenente un'espressione matematica (o una parte di essa), verifica
     * se è presente questo operatore in testa alla stringa
     * @param phrase L'espressione (o parte di essa) da controllare
     * @return True se questo operatore è presente in testa all'espressione, falso altrimenti
     */
    public boolean match(String phrase) {
        // se l'id dell'operatore è più lungo dell'espressione, restituisce false
        if (phrase.length() < this.identifier.length())
            return false;

        // altrimenti, controlla se l'inizio della frase è uguale all'id dell'operatore
        return this.identifier.equals(phrase.substring(0,this.identifier.length()));
    }
}
