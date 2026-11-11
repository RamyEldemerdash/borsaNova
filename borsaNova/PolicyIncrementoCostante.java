package borsaNova;

import java.util.Objects;

/**
 * Implementazione di PricePolicy che applica incremento costante al prezzo unitario di un'azione.
 * */
public class PolicyIncrementoCostante implements PricePolicy{

    /**
     * Di quanto incrementare il prezzo unitario di un'azione.
     * */
    private final int incremento;

    /**
     * AF: PolicyIncrementoCostante rappresenta una politica di prezzo che incrementa il prezzo unitario
     *     di un'azione di un valore costante.
     *
     * RI:
     *  - {@code incremento} è un intero positivo.
     *  - {@code azione} non è null e deve esistere.
     * */

    /**
     * Crea una nuova politica di incremento costante con il valore specificato.
     *
     * @param incremento l'incremento da applicare al prezzo unitario di un'azione.
     * @throws IllegalArgumentException se l'incremento è minore o uguale a 0.
     * */
    public PolicyIncrementoCostante(int incremento) {
        if (incremento <= 0) throw new IllegalArgumentException("ERRORE! L'incremento deve essere positivo.");
        this.incremento = incremento;

    }

    @Override
    public int calcolaNuovoPrezzo(Borsa.Azione azione, int numeroAzioniScambiate, boolean isBuy) {
        Objects.requireNonNull(azione);
        if (!azione.getBorsa().esisteAzione(azione.getAzienda())) throw new IllegalArgumentException("ERRORE! L'azione non esiste.");
        return isBuy ? azione.getPrezzoUnitario() + incremento : azione.getPrezzoUnitario();
    }
}
