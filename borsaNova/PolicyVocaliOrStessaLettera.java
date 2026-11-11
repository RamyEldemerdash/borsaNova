package borsaNova;

import java.util.Objects;

/**
 * Implementazione di PricePolicy che modifica il prezzo di un'azione se il nome dell'azienda
 * o della borsa inizia con una lettera specificata oppure una vocale.
 * */
public class PolicyVocaliOrStessaLettera implements PricePolicy {

    /**
     * La lettera da cercare.
     * */
    private final char lettera;


    /**
     * AF: PolicyVocaliOrStessaLettera rappresenta una politica di prezzo che modifica il prezzo unitario
     *     di un'azione se il nome dell'azienda o della borsa inizia con una lettera specificata oppure una vocale.
     *
     * RI:
     *  - {@code lettera} è un carattere.
     *  - {@code azione} non è null e deve esistere.
     * */

    /**
     * Crea una nuova politica che modifica il prezzo di un'azione se il nome dell'azienda
     * o della borsa inizia con la lettera specificata o una vocale.
     *
     * @param lettera la lettera da cercare.
     * @throws IllegalArgumentException se la lettera è un numero.
     * */
    public PolicyVocaliOrStessaLettera(char lettera) {
        if (Character.isDigit(lettera)) throw new IllegalArgumentException("ERRORE! La lettera non può essere un numero.");
        this.lettera = lettera;
    }

    @Override
    public int calcolaNuovoPrezzo(Borsa.Azione azione, int numeroAzioniScambiate, boolean isBuy) {
        Objects.requireNonNull(azione);
        if (!azione.getBorsa().esisteAzione(azione.getAzienda())) throw new IllegalArgumentException("ERRORE! L'azione non esiste.");
        if (numeroAzioniScambiate <= 0) throw new IllegalArgumentException("ERRORE! Il numero di azioni deve essere maggiore di zero.");

        String nomeAzienda = azione.getAzienda().getNome().toLowerCase();
        String nomeBorsa = azione.getBorsa().getNome().toLowerCase();
        boolean coinvolta = nomeAzienda.startsWith(String.valueOf(lettera).toLowerCase()) || nomeAzienda.startsWith(String.valueOf(lettera).toUpperCase()) ||
                nomeBorsa.startsWith(String.valueOf(lettera).toLowerCase()) || nomeBorsa.startsWith(String.valueOf(lettera).toUpperCase()) ||
                (nomeAzienda.startsWith("a") || nomeAzienda.startsWith("e") || nomeAzienda.startsWith("i") || nomeAzienda.startsWith("o") || nomeAzienda.startsWith("u"));

        int prezzoUnitario = azione.getPrezzoUnitario();
        if (coinvolta) {
            if (isBuy) {
                prezzoUnitario *= 2;
            } else {
                prezzoUnitario /= 2;
            }
        }
        return prezzoUnitario;
    }
}
