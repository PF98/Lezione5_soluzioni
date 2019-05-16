package it.unibs.arnaldo.lezione5.squareroots;

import java.util.Scanner;

/**
 * Classe del main
 *
 * @author Paolo Faustini <paolo.faustini@ieee.org>
 */
public class SquareRoots {
    public static void main(String[] args) throws IllegalArgumentException {
        // aggiunta degli operatori
        OperatorTable operators = new OperatorTable();
        operators.setGroupAssociativity(true); // imposto l'associatività sinistra per tutto il liv. di precedenza
        operators.add(new Operator("+", (l, r) -> l+r));
        operators.add(new Operator("-", (l, r) -> l-r));
        operators.stepUpPrecedence(); // aumento il livello di precedenza
        operators.setGroupAssociativity(true);
        operators.add(new Operator("*", (l, r) -> l*r));
        operators.add(new Operator("/", (l, r) -> l/r));
        // tolgo l'operatore di elevamento a potenza perché causa overflow vari nel calcolo del risultato
//        operators.stepUpPrecedence();
//        operators.setGroupAssociativity(false);
//        operators.add(new Operator("^", (l, r) -> (int)Math.pow(l, r)));

        // costruisco l'albero con gli operatori che ho inserito
        Tree tree = new Tree(operators);

        System.out.println("Generazione di alberi binari rappresentanti espressioni matematiche");

        System.out.println("Cosa vuoi fare?");
        System.out.println("1) Genera un albero casualmente");
        System.out.println("2) Genera un albero da un'espressione matematica");

        Scanner in = new Scanner(System.in).useDelimiter(System.lineSeparator());
        int selection = -1;
        do {
            System.out.print(" > ");
            try {
                String sel = in.nextLine();
                selection = Integer.parseInt(sel);
            } catch (Exception ignored) {}

            if (selection < 1 || selection > 2)
                System.out.print("Riprova");
        } while (selection < 1 || selection > 2);

        switch (selection) {
            case 1: // generazione casuale di profondità massima 10
                tree.randomize(10);
                break;
            case 2: // parsing dell'espressione letta da tastiera
                System.out.println("Inserisci l'espressione da utilizzare");
                String expr = in.nextLine().replaceAll("\\s+", "");
                tree.parseExpression(expr);
        }

        System.out.printf("%nRappresentazione dell'espressione con tutte le parentesi, anche quelle non necessarie:%n");
        System.out.println(tree.parenthesize());

        System.out.printf("%n%nRappresentazione dell'espressione con solo le parentesi strettamente necessarie:%n");
        System.out.println(tree.necessaryParentheses());

        System.out.printf("%n%nCalcolo del risultato dell'espressione:%n");
        System.out.println("Risultato = " + tree.calculate());
    }



}
