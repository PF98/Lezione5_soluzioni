package it.unibs.arnaldo.lezione5.intothewoods;

import java.util.List;

/**
 * Classe che rappresenta un albero di elementi, ossia un grafo aciclico non orientato e non pesato.
 *
 * @author Michele Dusi <michele.dusi.it@ieee.org>
 *
 */
public class Tree {

    private Node root;
    private static final String ROOT_LABEL = "root";
    private static final String NO_LABELS_EXCEPTION = "Non è possibile generare un albero casuale senza definire le etichette da utilizzare.";
    private static final String NOT_ENOUGH_NODES_EXCEPTION = "Non è possibile generare un albero poiché il numero di nodi richiesto non basta per utilizzare tutte le etichette.";

    /**
     * Costruttore vuoto, privato.
     * Si limita ad istanziare un albero vuoto.
     */
    private Tree() {
        this.root = new Node(ROOT_LABEL);
    }

    /**
     * Costruttore <b>CASUALE</b> che genera un nuovo albero dalla dimensione prefissata,
     * attingendo dalla lista di stringhe passata come parametro per generare le etichette dei nodi.
     * <b>Nota:</b> questo metodo fa in modo che tutte le etichette passate nella lista vengano utilizzate nella generazione dell'albero.
     * Questo assicura che l'albero conterrà sicuramente almeno un nood per ciascuna etichetta della lista.
     * <b>Nota:</b> per scelta implementativa, il nodo radice NON VIENE CONTATO nella dimensione dell'albero.
     *
     * @param size La dimensione dell'albero.
     * @param labels La lista di etichette.
     */
    public Tree(int size, List<String> labels) {
        this();
        if (labels == null || labels.isEmpty()) {
            throw new IllegalArgumentException(NO_LABELS_EXCEPTION);
        } else if (labels.size() > size) {
            throw new IllegalArgumentException(NOT_ENOUGH_NODES_EXCEPTION);
        } else {
            for (String l : labels) {
                this.root.addNewRandomNode(l);
            }
            for (int i = 0; i < (size - labels.size()); i++) {
                String random_label = labels.get((int) (Math.random() * labels.size()));
                this.root.addNewRandomNode(random_label);
            }
        }
    }

    /**
     * Verifica se all'interno dell'albero è presente almeno un nodo
     * con la label specificata.
     *
     * @param label L'etichetta da cercare.
     * @return true se esiste almeno un nodo con l'etichetta data.
     */
    public boolean containsLabel(String label) {
//////////////////////////////////////////////////////////////////////////
        return this.root.find(label);
//////////////////////////////////////////////////////////////////////////
    }

}
