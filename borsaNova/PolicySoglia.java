package borsaNova;

import java.util.Objects;

/**
 * Implementazione di PricePolicy che applica una politica di prezzo basata su una certa soglia.
 * */
public class PolicySoglia implements PricePolicy {

    /**
     * La soglia oltre la quale il prezzo di un'azione viene modificato.
     * */
    private final int soglia;

    /**
     * AF: PolicySoglia rappresenta una politica di prezzo che modifica il prezzo unitario di un'azione
     *     in base al numero di azioni scambiate, in caso di acquisto e superamento della soglia raddoppia, in
     *     caso di vendita e superamento della soglia dimezza.
     *
     * RI:
     *  - {@code soglia} è un intero positivo.
     *  - {@code azione} non è null e deve esistere.
     * */

    /**
     * Crea una nuova politica di prezzo basata su una certa soglia.
     *
     * @param soglia la soglia oltre la quale il prezzo di un'azione viene modificato.
     * @throws IllegalArgumentException se la soglia è minore o uguale a 0.
     * */
    public PolicySoglia(int soglia) {
        if (soglia <= 0) throw new IllegalArgumentException("ERRORE! La soglia deve essere positiva.");
        this.soglia = soglia;
    }

    @Override
    public int calcolaNuovoPrezzo(Borsa.Azione azione, int numeroAzioniScambiate, boolean isBuy) {
        Objects.requireNonNull(azione);
        if (!azione.getBorsa().esisteAzione(azione.getAzienda())) throw new IllegalArgumentException("ERRORE! L'azione non esiste.");
        if (isBuy) {
            if (numeroAzioniScambiate > soglia) {
                return azione.getPrezzoUnitario()*2;
            }
        } else {
            if (numeroAzioniScambiate > soglia) {
                return Math.max(azione.getPrezzoUnitario()/2, 1);
            }
        }
        return azione.getPrezzoUnitario();
    }
}
