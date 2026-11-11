package borsaNova;

import java.util.Objects;

/**
 * Implementazione di PricePolicy che applica decremento costante al prezzo unitario di un'azione.
 * */
public class PolicyDecrementoCostante implements PricePolicy{

    /**
     * Di quanto decrementare il prezzo unitario di un'azione.
     * */
    private final int decremento;

    /**
     * AF: PolicyDecrementoCostante rappresenta una politica di prezzo che decrementa il prezzo unitario
     *     di un'azione di un valore costante, garantendo che il nuovo prezzo sia sempre maggiore o uguale a 1.
     *
     * RI:
     *  - {@code decremento} è un intero positivo.
     *  - {@code azione} non è null e deve esistere.
     * */

    /**
     * Crea una nuova politica di decremento costante con il valore specificato.
     *
     * @param decremento il decremento da applicare al prezzo unitario di un'azione.
     * @throws IllegalArgumentException se il decremento è minore o uguale a 0.
     * */
    public PolicyDecrementoCostante(int decremento) {
        if (decremento <= 0) throw new IllegalArgumentException("ERRORE! Il decremento deve essere positivo.");
        this.decremento = decremento;
    }

    @Override
    public int calcolaNuovoPrezzo(Borsa.Azione azione, int numeroAzioniScambiate, boolean isBuy) {
        Objects.requireNonNull(azione);
        if (!azione.getBorsa().esisteAzione(azione.getAzienda())) throw new IllegalArgumentException("ERRORE! L'azione non esiste.");
        return isBuy ? azione.getPrezzoUnitario() : Math.max(azione.getPrezzoUnitario() - decremento, 1);
    }
}
