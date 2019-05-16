package it.unibs.arnaldo.lezione5.intothewoods;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe che rappresenta un nodo di un albero.
 *
 * @author Michele Dusi <michele.dusi.it@ieee.org>
 *
 */
public class Node {

    /**
     * Probabilità che un nodo venga creato adiacentemente ad un nodo dato.
     * Nota: è importante che non valga mai 0, o la generazione casuale di alberi dà problemi.
     */
    private static final double ADJACENCY_PERCENTAGE = 0.5;

    private static long idCounter = 0;
    private long id;
    private String label;
    private List<Node> adjacents;

    /**
     * Costruttore.
     *
     * @param _label
     */
    public Node(String _label) {
        this.id = Node.idCounter++;
        this.label = _label;
        this.adjacents = new LinkedList<Node>();
    }

    /**
     * Aggiunge un nodo adiacente istanziando un nuovo oggetto Node.
     *
     * @param new_label La stringa del nuovo nodo
     */
    public void addNewAdjacentNode(String new_label) {
        this.adjacents.add(new Node(new_label));
    }

    /**
     * Metodo utilizzato per la generazione casuale di alberi.
     * Sulla base di una certa percentuale, decide se aggiungere un nuovo nodo
     * come adiacente al nodo stesso, o se richiamare l'aggiunta casuale su un suo adiacente.
     *
     * @param new_label
     */
    public void addNewRandomNode(String new_label) {
        if (this.adjacents.size() < 1 || Math.random() < ADJACENCY_PERCENTAGE) {
            this.addNewAdjacentNode(new_label);
        } else {
            int randomAdjacentIndex = (int) (Math.random() * this.adjacents.size());
            this.adjacents.get(randomAdjacentIndex).addNewRandomNode(new_label);
        }
    }

    /**
     * Restituisce l'etichetta del nodo.
     *
     * @return L'etichetta del nodo.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Restituisce l'id del nodo.
     *
     * @return L'id del nodo.
     */
    public long getId() {
        return this.id;
    }

    /**
     * Restituisce il numero di nodi adiacenti a tale nodo.
     *
     * @return Il numero di adiacenti.
     */
    public int getNumberOfAdjacents() {
        return this.adjacents.size();
    }

    public boolean find(String label) {
        if (this.label.equals(label)) return true;

        for (Node child : this.adjacents) {
            if (child.find(label)) return true;
        }

        return false;
    }

}
