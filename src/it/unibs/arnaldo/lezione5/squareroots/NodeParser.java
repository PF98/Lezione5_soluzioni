package it.unibs.arnaldo.lezione5.squareroots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Interfaccia rappresentante il comportamento del parsing di un'espressione e che fornisce il medodo
 * fondamentale per il parsiong
 *
 * @author Paolo Faustini <paolo.faustini@ieee.org>
 */
public interface NodeParser {
    String ILLEGAL_PARENTHESIS = "Illegal parenthesis balancing in the expression: '%s' at character %d";

    /**
     * Metodo che effettua il parsing di una stringa, data una tabella di operatori
     * @param s La stringa da interpretare
     * @param opers La tabella degli operatori
     * @return Il nodo radice di questa espressione
     * @throws IllegalArgumentException Se ci sono problemi nel parsing
     */
    default Node parse(String s, OperatorTable opers) throws IllegalArgumentException {
        // classe interna che rappresenta un match con un operatore dato in una certa posizione nella string
        final class PosMatch {
            private int pos;
            private Operator oper;
            private PosMatch(int pos, Operator oper) { this.pos = pos; this.oper = oper; }
        }

        // Punto 1:
        // Memorizzo la posizione di tutti gli operatori non contenuti fra parentesi

        // converto in stringbuilder così è più comodo operare sulla stringa
        StringBuilder str = new StringBuilder(s);
        ArrayList<PosMatch> matches = new ArrayList<>();
        int pos = 0; // memorizza la posizione attuale nella stringa s
        int parenthesisCount = 0;
        char lastUseful = 0; // ricorda il carattere precedente (se è parentesi), zero altrimenti
        while (str.length() > 0) {
            char start = str.charAt(0);
            // se in testa alla stringa ho una parentesi aperta
            if (start == '(') {
                // errore: non sono ammesse parentesi aperte immediatamente dopo una parentesi chiusa
                if (lastUseful == ')')
                    throw new IllegalArgumentException(String.format(ILLEGAL_PARENTHESIS, s, pos));

                // elimina il primo carattere e aumenta il conto delle parentesi
                parenthesisCount++;
                str.deleteCharAt(0);
                pos++;
                lastUseful = start;
                continue;
            }
            // se in testa alla stringa c'è una parentesi chiusa
            else if (start == ')') {
                // una parentesi chiusa subito dopo una aperta non è ammesso
                if (lastUseful == '(')
                    throw new IllegalArgumentException(String.format(ILLEGAL_PARENTHESIS, s, pos));
                parenthesisCount--;
                // in questo caso, vengono chiuse più parentesi di quante ne vengano aperte
                if (parenthesisCount < 0) {
                    throw new IllegalArgumentException(String.format(ILLEGAL_PARENTHESIS, s, pos));
                }
                // cancello il primo carattere e diminuisco il conto delle parentesi aperte
                str.deleteCharAt(0);
                pos++;
                lastUseful = start;
                continue;
            }
            lastUseful = 0;
            // salto tutti i caratteri dentro alle parentesi: non ho bisogno di cercare fra operatori inclusi in
            // parentesi: essi non potranno essere in cima all'albero
            if (parenthesisCount > 0) {
                str.deleteCharAt(0);
                pos++;
                continue;
            }

            // cerco il match migliore con la testa della stringa
            Operator match = opers.bestMatch(str.toString());
            // non è stato trovato nessun match: in testa alla stringa non c'è un'operatore:
            // elimino il primo carattere e riprovo
            if (match == null) {
                str.deleteCharAt(0);
                pos++;
                continue;
            }

            // se è stato trovato un match, lo aggiungiamo alla lista ed eliminiamo dalla stringa l'id dell'operatore
            matches.add(new PosMatch(pos, match));
            str.delete(0, match.getIdLength());
            pos += match.getIdLength();
        }

        // Passo 2:
        // Una volta terminato lo scorrimento della stringa, elaboro i match trovati

        // se non abbiamo trovato nessun operatore fuori dalle parentesi
        if (matches.isEmpty()) {
            // se in testa c'è una parentesi, l'intera espressione è contenuta fra parentesi [es: "(2 + 3)"]
            if (s.charAt(0) == '(')
                return this.parse(s.substring(1, s.length() - 1), opers); // eliminiamo le parentesi e riproviamo

            // se in tasta non c'è una parentesi, ci troviamo di fronte a un numero
            return new ConstNode(s);
        }

        // a questo punto almeno un match è stato trovato
        // cerchiamo il valore minimo della precedenza degli operatori che hanno avuto un match
        int minPrec = -1;
        for (PosMatch match : matches) {
            if (minPrec == -1 || match.oper.getPrecedence() < minPrec)
                minPrec = match.oper.getPrecedence();
        }
        // se l'operatore ha associatività sinistra, devo cercare l'ultimo match, non il primo
        if (opers.hasLeftAssociativity(minPrec))
            Collections.reverse(matches);

        // cerco il primo match con precedenza giusta in "matches", da mettere in cima all'albero
        PosMatch bestMatch = null;
        for (PosMatch match : matches) {
            if (match.oper.getPrecedence() == minPrec) {
                bestMatch = match;
                break;
            }
        }
        /* IMPLEMENTAZIONE ALTERNATIVA CON UTILIZZO DI STREAM E LAMBDAS
        int minPrec = matches.stream().map((a) -> a.oper.getPrecedence()).min(Integer::compareTo).orElse(-1);
        if (opers.hasLeftAssociativity(minPrec))
            Collections.reverse(matches);

        PosMatch bestMatch = matches.stream().filter((a) -> a.oper.getPrecedence() == minPrec).findFirst().get();
        */

        Operator oper = bestMatch.oper;

        // la substring a sinistra dell'operatore verrà interpretata e il risultato inserito nel figlio sinsitro
        int operPos = bestMatch.pos;
        String left = s.substring(0, operPos);

        // la substring a destra dell'operatore verrà interpretata e il risultato inserito nel figlio destro
        int operLen = oper.getIdLength();
        String right = s.substring(operPos + operLen);

        return new OperatorNode(left, right, oper, opers);
    }
}
