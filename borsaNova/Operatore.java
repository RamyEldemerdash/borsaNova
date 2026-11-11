package borsaNova;

import java.util.*;

/**
 * Rappresenta un operatore, con un budget e un insieme di azioni possedute.
 * */
public class Operatore implements Comparable<Operatore> {

    /**
     * Il nome dell'operatore.
     * */
    private final String nome;
    /**
     * Il budget dell'operatore.
     * */
    private int budget;
    /**
     * Mappa delle azioni possedute dall'operatore.
     * */
    private final Map<Borsa.Azione, Integer> azioniPossedute;
    /**
     * Set che contiene tutte le istanze di {@code Operatore}.
     * */
    private static final Set<Operatore> INSTANCE = new HashSet<>();

    /**
     * AF: Operatore rappresenta un partecipante al mercato azionario con un {@code nome} univoco,
     *     e un {@code budget} iniziale pari a 0. L'operatore possiede un insieme di azioni {@code azioniPossedute}
     *     inizialmente vuoto, dove ogni chiave rappresenta un'azione posseduta e i valori la quantità.
     *
     * RI:
     *    - {@code nome} non deve essere null o vuoto;
     *    - Il budget deve essere maggiore o uguale a 0;
     *    - La mappa delle azioni possedute non deve contenere valori null o negativi.
     *    - Il capitale totale deve essere coerente con la somma del budget e il valore delle azioni possedute.
     * */

    /**
     * Crea una nuova istanza di {@code Operatore} con il nome specificato e inizializza il budget a 0.
     *
     * @param nome il nome dell'operatore.
     * */
    private Operatore(String nome) {
        this.nome = nome;
        this.budget = 0;
        this.azioniPossedute = new HashMap<>();
    }

    /**
     * Restituisce un'istanza di {@code Operatore} con il nome specificato.
     *
     * @param nome il nome dell'operatore.
     * @throws NullPointerException se {@code nome} è null.
     * @throws IllegalArgumentException se {@code nome} è vuoto.
     *
     * @return l'istanza di {@code Operatore}.
     * */
    public static Operatore getInstance(String nome) {
        if (Objects.requireNonNull(nome).isBlank()) throw new IllegalArgumentException("ERRORE! Il nome non può essere vuoto.");
        for (Operatore operatore : INSTANCE) {
            if (operatore.nome.equals(nome)) {
                return operatore;
            }
        }
        Operatore operatore = new Operatore(nome);
        INSTANCE.add(operatore);
        return operatore;
    }

    /**
     * Verifica se esiste un'operatore con il nome specificato.
     *
     * @param nome il nome dell'operatore da cercare.
     * @throws IllegalArgumentException se il nome è vuoto.
     * @throws NullPointerException se il nome è null.
     * @return {@code true} se esiste un'operatore con il nome specificato, {@code false} altrimenti.
     */
    public static boolean esisteOperatore(String nome) {
        if (Objects.requireNonNull(nome).isBlank()) throw new IllegalArgumentException("ERRORE! Il nome non può essere vuoto.");
        for (Operatore operatore : INSTANCE) {
            if (operatore.nome.equals(nome)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Restituisce il nome dell'operatore.
     *
     * @return il nome.
     * */
    public String getNome() {
        return this.nome;
    }

    /**
     * Deposita un importo nel budget dell'operatore.
     *
     * @param importo l'importo da depositare.
     * @throws IllegalArgumentException se {@code importo} è minore di zero.
     * */
    public void deposita(int importo) {
        if (importo < 0) throw new IllegalArgumentException("ERRORE! L'importo deve essere maggiore o uguale a zero.");
        this.budget += importo;
    }

    /**
     * Preleva un importo dal budget dell'operatore.
     *
     * @param importo l'importo da prelevare.
     * @throws IllegalArgumentException se {@code importo} è minore o uguale a zero o superiore al budget.
     * */
    public void preleva(int importo) {
        if (importo <= 0 || importo > this.budget) throw new IllegalArgumentException("ERRORE! L'importo deve essere maggiore di zero e minore o uguale al budget.");
        this.budget -= importo;
    }

    /**
     * Restituisce {@code true} se esiste la borsa, l'azienda e l'azione specificate; {@code false} altrimenti.
     *
     * @param azienda l'azienda di cui verificare la presenza delle azioni.
     * @param borsa la borsa in cui cercare le azioni.
     * @throws NullPointerException se {@code azienda} o {@code borsa} è null.
     * @throws IllegalArgumentException se l'azienda non è quotata in borsa, o se la borsa o l'azienda non esistono.
     *
     * @return {@code true} se tutto ok, {@code false} altrimenti.
     * */
    private boolean checkEsistenzaBorsaAzienda(Borsa borsa, Azienda azienda) {
        Objects.requireNonNull(borsa);
        Objects.requireNonNull(azienda);
        if (!Borsa.esisteBorsa(borsa.getNome())) throw new IllegalArgumentException("ERRORE! La borsa specificata non esiste.");
        if (!Azienda.esisteAzienda(azienda.getNome())) throw new IllegalArgumentException("ERRORE! L'azienda specificata non esiste.");
        if (!borsa.esisteAzione(azienda)) throw new IllegalArgumentException("ERRORE! L'azienda non è quotata nella borsa specificata.");
        return true;
    }

    /**
     * Esegue l'acquisto di un numero di azioni specificate tramite una {@link Borsa}.
     *
     * @param borsa la borsa in cui eseguire l'operazione.
     * @param azienda l'azienda di cui acquistare le azioni.
     * @param quantita il numero di azioni da acquistare.
     * @throws NullPointerException se {@code borsa} o {@code azienda} è null.
     * @throws IllegalArgumentException se {@code quantita} è minore o uguale a zero,
     *                                 se il budget è insufficiente,
     *                                 se l'azienda non è quotata in borsa.
     * */
    public void compraAzioni(Borsa borsa, Azienda azienda, int quantita) {
        Objects.requireNonNull(borsa);
        Objects.requireNonNull(azienda);
        if (quantita <= 0) throw new IllegalArgumentException("ERRORE! La quantità deve essere maggiore di zero.");
        if (checkEsistenzaBorsaAzienda(borsa, azienda)) {
            int costoTotale = borsa.getPrezzoAzione(azienda) * quantita;
            if (costoTotale > this.budget) throw new IllegalArgumentException("ERRORE! Budget insufficiente.");
            borsa.finalizzaAcquistoAzione(this, azienda, quantita);
            this.budget -= costoTotale;
        }
    }

    /**
     * Aggiunge un'azione all'insieme di azioni possedute dall'operatore.
     *
     * @param azione l'azione da aggiungere.
     * @param quantita la quantità di azioni da aggiungere.
     * @throws NullPointerException se {@code azione} è null.
     * @throws IllegalArgumentException se {@code quantita} è minore o uguale a zero.
     * */
    public void aggiungiAzioni(Borsa.Azione azione, int quantita) {
        Objects.requireNonNull(azione);
        if (quantita <= 0) throw new IllegalArgumentException("ERRORE! La quantità deve essere maggiore di zero.");
        azioniPossedute.merge(azione, quantita, Integer::sum);
    }

    /**
     * Esegue la vendita di un numero di azioni specificate tramite una {@link Borsa}.
     *
     * @param borsa la borsa in cui eseguire l'operazione.
     * @param azienda l'azienda di cui vendere le azioni.
     * @param quantita il numero di azioni da vendere.
     * @throws NullPointerException se {@code borsa} o {@code azienda} è null.
     * @throws IllegalArgumentException se {@code quantita} è minore o uguale a zero,
     *                                 se l'operatore non possiede azioni dell'azienda specificata,
     *                                 se l'azienda non è quotata in borsa.
     * */
    public void vendiAzioni(Borsa borsa, Azienda azienda, int quantita) {
        Objects.requireNonNull(borsa);
        Objects.requireNonNull(azienda);
        if (quantita <= 0) throw new IllegalArgumentException("ERRORE! La quantità deve essere maggiore di zero.");
        if (!azioniPossedute.containsKey(Borsa.getAzione(borsa, azienda))) throw new IllegalArgumentException("ERRORE! L'operatore non possiede azioni di questo tipo.");
        if (checkEsistenzaBorsaAzienda(borsa, azienda)) {
            borsa.finalizzaVenditaAzione(this, azienda, quantita);
            this.budget += borsa.getPrezzoAzione(azienda) * quantita;
        }
    }

    /**
     * Rimuove un'azione dall'insieme di azioni possedute dall'operatore.
     *
     * @param azione l'azione da rimuovere.
     * @param quantita la quantità di azioni da rimuovere.
     * @throws NullPointerException se {@code azione} è null.
     * @throws IllegalArgumentException se {@code quantita} è minore o uguale a zero,
     *                                 se l'operatore non possiede azioni dell'azienda specificata.
     * */
    public void rimuoviAzioni(Borsa.Azione azione, int quantita) {
        Objects.requireNonNull(azione);
        if (quantita <= 0) throw new IllegalArgumentException("ERRORE! La quantità deve essere maggiore di zero.");
        if (!azioniPossedute.containsKey(azione)) throw new IllegalArgumentException("ERRORE! L'operatore non possiede azioni di questo tipo.");
        int quantitaPosseduta = azioniPossedute.get(azione);
        if (quantitaPosseduta < quantita) throw new IllegalArgumentException("ERRORE! L'operatore non possiede abbastanza azioni.");
        int nuovaQuantita = quantitaPosseduta - quantita;
        if (nuovaQuantita == 0) {
            azioniPossedute.remove(azione);
        } else {
            azioniPossedute.put(azione, nuovaQuantita);
        }
    }

    /**
     * Restituisce il capitale totale dell'operatore.
     *
     * @param soloBudget {@code true} se si vuole considerare solo il budget, {@code false} altrimenti.
     * @return il capitale totale.
     * */
    public int getCapitaleTotale(boolean soloBudget) {
        if (soloBudget) return this.budget;
        return this.budget + getTotaleValoreAzioniPossedute();
    }

    /**
     * Restituisce il valore totale delle azioni possedute dall'operatore.
     *
     * @return il valore totale delle azioni possedute.
     * */
    public int getTotaleValoreAzioniPossedute() {
        int capitale = 0;
        for (Map.Entry<Borsa.Azione, Integer> entry : this.azioniPossedute.entrySet()) {
            capitale += entry.getKey().getPrezzoUnitario() * entry.getValue();
        }
        return capitale;
    }

    /**
     * Restituisce le azioni possedute dall'operatore.
     *
     * @return le azioni possedute.
     * */
    public Map<Borsa.Azione, Integer> getAzioniPossedute() {
        return Collections.unmodifiableMap(this.azioniPossedute);
    }

    @Override
    public String toString() {
        return getNome() + ", " + getCapitaleTotale(true);
    }

    @Override
    public int compareTo(Operatore otherOperatore) {
        return this.getNome().compareTo(otherOperatore.getNome());
    }
}
