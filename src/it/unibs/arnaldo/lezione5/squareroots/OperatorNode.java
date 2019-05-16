package it.unibs.arnaldo.lezione5.squareroots;

/**
 * Classe che rappresenti un nodo operatore, contenente quindi un operatore e due figli
 *
 * @author Paolo Faustini <paolo.faustini@ieee.org>
 */
public class OperatorNode implements Node, NodeParser {
    private static final double CONST_NODE_PERCENTAGE = 0.5;

    private OperatorTable opers;
    private Node leftChild;
    private Node rightChild;
    private Operator oper;

    /**
     * Costruttore "vuoto", forniamo solo la tabella degli operatori
     * @param opers La tabella degli operatori
     */
    public OperatorNode(OperatorTable opers) {
        this.opers = opers;
    }

    /**
     * Costruttore, effettua direttamente il parsing dei due figli
     * @param left La stringa da interpretare del figlio sinistro
     * @param right La stringa da interpretare del figlio destro
     * @param oper L'operatore legato a questo nodo
     * @param opers La tabella degli operatori
     * @throws IllegalArgumentException Se ci sono problemi nel parsing
     */
    public OperatorNode(String left, String right, Operator oper, OperatorTable opers) throws IllegalArgumentException {
        this.opers = opers;
        this.oper = oper;
        this.parseExpression(left, right);
    }

    /**
     * Metodo che effettua il parsing dei due nodi figli
     * @param l La stringa da interpretare del figlio sinistro
     * @param r La stringa da interpretare del figlio destro
     * @throws IllegalArgumentException Se ci sono problemi nel parsing
     */
    private void parseExpression(String l, String r) throws IllegalArgumentException {
        this.leftChild = this.parse(l, this.opers);
        this.rightChild = this.parse(r, this.opers);
    }

    /**
     * Metodo statico che genera il primo nodo in un albero casuale: esso dovrà essere necessariamente
     * un'operazione, per questo abbiamo questo metodo aggiuntivo
     * @param maxDepth La massima profondità dell'albero
     * @param opers La tabella degli operatori
     * @return Il nodo generato casualmente
     */
    public static OperatorNode startRandomization(int maxDepth, OperatorTable opers) {
        // crea un nodo "vuoto", da poi popolare casualmente
        OperatorNode out = new OperatorNode(opers);
        // inizia a riempire ricorsivamente il nodo
        out.randomize(maxDepth);
        return out;
    }

    /**
     * Metodo ricorsivo per la generazione dei figli di questo albero
     * @param maxDepth La massima profondità dell'albero
     */
    @Override
    public void randomize(int maxDepth) {
        // generazione casuale dei figli destro e sinistro, permettendo una profondità massima diminuita di 1
        this.leftChild = this.genRandomNode(maxDepth - 1);
        this.rightChild = this.genRandomNode(maxDepth - 1);

        // scelta casuale di un operatore fra quelli disponibili
        Operator op = this.opers.random();
        this.oper = op;
    }

    /**
     * Metodo d'appoggio che genera effettivamente ciascun figlio dell'albero, che può essere un numero o un altro
     * operatore
     * @param maxDepth La massima profondità dell'albero
     * @return Il nodo generato
     */
    private Node genRandomNode(int maxDepth) {
        Node out;
        // se non abbiamo raggiunto la fine del grafo, abbiamo una certa probabilità di generare un nodo Costante
        if (maxDepth <= 1 || Math.random() < CONST_NODE_PERCENTAGE)
            out = new ConstNode();
        else
            out = new OperatorNode(this.opers);

        // popola casualmente il nuovo nodo generato
        out.randomize(maxDepth);
        return out;
    }

    /**
     * Calcola ricorsivamente il valore di questo nodo
     * @return Il valore del nodo
     * @throws IllegalArgumentException Se ci sono problemi nel calcolo dei valori di altri nodi
     */
    @Override
    public double calcValue() throws IllegalArgumentException {
        // calcolo ricorsivo del risultato dell'operazione
        double l = leftChild.calcValue();
        double r = rightChild.calcValue();
        // effettivo calcolo del risultato
        return oper.getOperation().calcValue(l, r);
    }

    /**
     * Metodo che formatta con le parentesi adatte questo nodo
     * @return L'espressione rappresentata da questo nodo con le parentesi
     */
    @Override
    public String parenthesize() {
        StringBuilder out = new StringBuilder();
        out.append("[");
        out.append(this.leftChild.parenthesize());
        out.append(" ");
        out.append(this.oper.getId());
        out.append(" ");
        out.append(this.rightChild.parenthesize());
        out.append("]");
        return out.toString();
    }

    /**
     * Metodo che formatta l'espressione con le sole parentesi necessarie al rispetto dell'ordine delle operazioni
     * @return La stringa formattata
     */
    @Override
    public String necessaryParentheses() {
        // per il primo nodo, supponiamo che il padre abbia precedenza negativa (vedere il prossimo metodo per la
        // spiegazione)
        return necessaryParentheses(-1, false);
    }

    /**
     * Metodo che formatta l'espressione con le sole parentesi necessarie al rispetto dell'ordine delle operazioni,
     * data l'espressione del nodo padre
     * @param fatherPrecedence La precedenza dell'operatore del padre
     * @param sameDirectionAndAssoc Parametro che è true se l'operatore ha associatività sinistra (/destra)
     *                              e il figlio è sinistro (/destro), false altrimenti
     * @return La stringa formattata
     */
    @Override
    public String necessaryParentheses(int fatherPrecedence, boolean sameDirectionAndAssoc) {
        StringBuilder out = new StringBuilder();

        int operatorPrecedence = this.oper.getPrecedence();
        boolean isOperatorLeftAssoc = this.opers.hasLeftAssociativity(operatorPrecedence);

        // genero l'espressione con parentesi del figlio sinistro
        String leftSon = this.leftChild.necessaryParentheses(operatorPrecedence, isOperatorLeftAssoc);
        out.append(leftSon);

        out.append(" ");
        out.append(this.oper.getId());
        out.append(" ");

        // genero l'espressione con parentesi del figlio destro
        String rightSon = this.rightChild.necessaryParentheses(operatorPrecedence, !isOperatorLeftAssoc);
        out.append(rightSon);


        int diff = operatorPrecedence - fatherPrecedence; // se positivo il padre ha meno priorità

        /* tre casi:
         * - (diff < 0) => il figlio ha priorità più bassa del padre
         *   ad esempio, 2 * (3 + 5), le parentesi sono necessarie
         * - (diff == 0 && !sameDirectionAndAssoc)
         *                   => padre e figlio hanno la stessa priorità, ma il figlio è da calcolare
         *                      prima per ragioni di associatività
         *   ad esempio, 2 + (3 + 4), l'operatore ha associatività sinistra ma il figlio (3 + 4) è destro, quindi
         *   sameDirectionAndAssoc è falso => le parentesi sono necessarie, altrimenti l'ordine delle operazioni
         *   sarebbe (2 + 3) + 4 => servono le parentesi (in realtà per l'addizione non sarebbe un problema perché
         *   è un'operazione associativa, ma potrebbe non sempre essere così, basti pensare a sottrazione e divisione)
         * - (diff == 0 && sameDirectionAndAssoc)
         *                   => padre e figlio hanno la stessa priorità, ma il figlio è da calcolare
         *                      dopo per ragioni di associatività
         *   ad esempio, 2 ^ (3 ^ 4), l'operazione ha associatività destra e il figlio (3 ^ 4) è destro, quindi
         *   sameDirectionAndAssoc è vero => le parentesi non sono necessarie, l'associatività naturale
         *   dell'operatore permette lìomissione
         * - (diff > 0) => il figlio ha priorità più alta del padre
         *   ad esempio, 2 + (3 * 5), le parentesi non sono necessarie
         */
        if (diff < 0 || (diff == 0 && !sameDirectionAndAssoc)) {
            out.insert(0, "(").append(")");
        }

        return out.toString();

    }
}
