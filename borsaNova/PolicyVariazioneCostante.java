package borsaNova;

import java.util.Objects;

/**
 * Implementazione di PricePolicy che modifica il prezzo di una azione di una quantità costante.
 * */
public class PolicyVariazioneCostante implements PricePolicy {

    /**
     * Di quanto incrementare il prezzo.
     * */
    private final int incremento;
    /**
     * Di quanto decrementare il prezzo.
     * */
    private final int decremento;

    /**
     * AF: PolicyVariazioneCostante rappresenta una politica di prezzo che modifica il prezzo unitario
     *     di un'azione di una quantità costante.
     *
     * RI:
     *  - {@code incremento} è un intero positivo.
     *  - {@code decremento} è un intero positivo.
     *  - {@code azione} non è null e deve esistere.
     * */

    /**
     * Costruisce una nuova PolicyVariazioneCostante con il valore specificato.
     *
     * @param incremento quantità di incremento.
     * @param decremento quantità di decremento.
     * @throws IllegalArgumentException se incremento o decremento è minore o uguale a 0.
     * */
    public PolicyVariazioneCostante(int incremento, int decremento) {
        if (incremento <= 0 || decremento <= 0) {
            throw new IllegalArgumentException("ERRORE! Il valore incremento e decremento devono essere positivi.");
        }
        this.incremento = incremento;
        this.decremento = decremento;
    }

    @Override
    public int calcolaNuovoPrezzo(Borsa.Azione azione, int numeroAzioniScambiate, boolean isBuy) {
        Objects.requireNonNull(azione);
        if (!azione.getBorsa().esisteAzione(azione.getAzienda())) throw new IllegalArgumentException("ERRORE! L'azione non esiste.");
        if (numeroAzioniScambiate <= 0) throw new IllegalArgumentException("ERRORE! Il numero di azioni deve essere maggiore di zero.");

        if (isBuy) {
            PolicyIncrementoCostante policyIncrementoCostante = new PolicyIncrementoCostante(incremento);
            return policyIncrementoCostante.calcolaNuovoPrezzo(azione, numeroAzioniScambiate, true);
        }
        PolicyDecrementoCostante policyDecrementoCostante = new PolicyDecrementoCostante(decremento);
        return policyDecrementoCostante.calcolaNuovoPrezzo(azione, numeroAzioniScambiate, false);
    }
}
