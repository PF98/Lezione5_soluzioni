package it.unibs.arnaldo.lezione5.intothewoods;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe main del programma.
 * Implementa i test progressivi per verificarne l'accuratezza.
 *
 * @author Michele Dusi <michele.dusi.it@ieee.org>
 *
 */
public final class IntoTheWoods {

    private static final int TEST = 1000;
    private static final int CHAR_PER_LABEL = 5;
    private static final String TEST_FORMAT = "Numero di test effettuati: %d\n";
    private static final String RESULT_FORMAT = "Percentuale di successo dei test: %.4f%%\n";
    private static final String ABSENT_LABEL = "Etichetta che sicuramente non viene generata automaticamente.";
    private static char [] alphabet;

    public static void main(String [] args) {
        generateAlphabet();

        double result = 0;
        for (int t = 0; t < TEST; t++) {
            result += testTree((int) (1.25 * t + 1), t + 1);
        }

        System.out.printf(TEST_FORMAT, TEST);
        System.out.printf(RESULT_FORMAT, result * 100 / TEST);
    }

    /**
     * Restituisce un valore compreso fra 0 e 1, dove:
     * - 0 indica il totale fallimento del test.
     * - 1 indica il totale successo del test.
     *
     * @param size       La dimensione dell'albero
     * @param num_labels Il numero di etichette
     * @return Un valore tra 0 e 1 indicante il livello di successo del test
     */
    private static double testTree(int size, int num_labels) {
        List<String> labels = generateLabels(num_labels);
        Tree test_tree = new Tree(size, labels);
        // Inizio la fase di testing
        try {
            int success_number = 0;
            for (String l : labels) {
                if (test_tree.containsLabel(l)) {
                    success_number++;
                }
            }
            if (!test_tree.containsLabel(ABSENT_LABEL)) {
                success_number++;
            }
            return success_number / (num_labels + 1.0);

        } catch (Exception e) {
            // In caso venga generata un'eccezione, il test è da considerarsi fallito.
            return 0;
        }
    }

    /**
     * Genera l'alfabeto da utilizzare per le etichette.
     */
    private static void generateAlphabet() {
        int alphabet_index = 0;
        int letters_number = 'z' - 'a' + 1;
        alphabet = new char[2 * letters_number + 10];
        for (int l = 0; l < letters_number; l++) {
            alphabet[alphabet_index + letters_number] = (char) ('a' + l);
            alphabet[alphabet_index++] = (char) ('A' + l);
        }
        alphabet_index += letters_number;
        for (int n = 0; n < 10; n++) {
            alphabet[alphabet_index++] = (char) ('0' + n);
        }
    }

    /**
     * Genera una lista di stringhe casuali da utilizzare come etichette.
     *
     * @param numberOfLabels numero di label
     * @return La lista di etichette.
     */
    private static List<String> generateLabels(int numberOfLabels) {
        List<String> labels = new ArrayList<>(numberOfLabels);
        for (int l = 0; l < numberOfLabels; l++) {
            StringBuilder s = new StringBuilder();
            for (int c = 0; c < CHAR_PER_LABEL; c++) {
                s.append(alphabet[(int) (Math.random() * alphabet.length)]);
            }
            labels.add(s.toString());
        }
        return labels;
    }

}
