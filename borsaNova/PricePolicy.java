package borsaNova;

/**
 * Interfaccia per la politica di prezzo.
 * */
public interface PricePolicy {
    /**
     * Calcola e restituisce il nuovo prezzo di un'azione.
     *
     * @param azione l'azione di cui calcolare il prezzo.
     * @param numeroAzioniScambiate il numero di azioni scambiate.
     * @param isBuy {@code true} se l'operazione è un acquisto, {@code false} se è una vendita.
     * @return il nuovo prezzo.
     * */
    int calcolaNuovoPrezzo(Borsa.Azione azione, int numeroAzioniScambiate, boolean isBuy);
}
