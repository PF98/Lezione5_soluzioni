package it.unibs.arnaldo.lezione5.squareroots;

import java.util.*;

public class OperatorTable {
    private ArrayList<Operator> opers;
    private HashMap<Integer, Boolean> associativity;
    private int precedenceLevel;

    /**
     * Costruttore
     */
    public OperatorTable() {
        this.opers = new ArrayList<>();
        this.associativity = new HashMap<>();
        this.precedenceLevel = 0;
    }

    /**
     * Metodo che controlla se gli operatori di un certo livello di precedenza hanno associatività sinistra
     * @param livPrec Il livello di precedenza da controllare
     * @return True se ha associatività sinistra, false se destra
     */
    public boolean hasLeftAssociativity(int livPrec) {
        return associativity.get(livPrec);
    }

    /**
     * Metodo che aggiunge un operatore all'attuale livello di precedenza nella tabella
     * @param operator
     */
    public void add(Operator operator) {
        this.add(operator, this.precedenceLevel);
    }

    /**
     * Metodo che aumenta di uno il livello di precedenza (da essere utilizzato per l'aggiunta di nuovi operatori)
     */
    public void stepUpPrecedence() {
        this.precedenceLevel++;
    }

    /**
     * Imopsta l'associatività di un certo livello di precedenza
     * @param precedenceLevel Il livello di precedenza dato
     * @param isLeft True se voglio associatività sinistra, false se destra
     */
    public void setGroupAssociativity(int precedenceLevel, boolean isLeft) {
        this.associativity.put(precedenceLevel, isLeft);
    }

    /**
     * Imopsta l'associatività del livello di precedenza attuale
     * @param isLeft True se voglio associatività sinistra, false se destra
     */
    public void setGroupAssociativity(boolean isLeft) {
        this.setGroupAssociativity(this.precedenceLevel, isLeft);
    }

    /**
     * Metodo che aggiunge un operatore al livello di precedenza dato
     * @param operator L'operatore da aggiungere
     * @param precedence Il livello di precedenza di tale operatore
     */
    public void add(Operator operator, int precedence) {
        operator.setPrecedence(precedence);
        this.opers.add(operator);
    }

    /**
     * Restituise un operatore a caso fra quelli presenti
     * @return Un operatore scelto casualmente
     */
    public Operator random() {
        int random = (int) (Math.random() * this.opers.size());
        return this.opers.get(random);
    }

    /**
     * Data una certa stringa contenente un espressione, prova a fare il matching con l'inizio di tale stringa
     * Se trova più di un match, restituisce quello più lungo. Ad esempio, data la stringa "**2+3", il match potrebbe
     * essere con due operatori: "*" e "**" (elevamento a potenza in alcuni linguaggi). Il miglior match è il secondo
     * @param op L'espressione matematica
     * @return Un operatore se è stato trovato un match, null altrimenti
     */
    public Operator bestMatch(String op) {
        ArrayList<Operator> matches = new ArrayList<>();
        for (Operator o : this.opers) {
            if (o.match(op))
                matches.add(o);
        }
        if (matches.isEmpty()) return null;

        // trova l'operatore fra i matches con lunghezza massima
        return Collections.max(matches, Comparator.comparingInt(Operator::getIdLength));
    }
}
