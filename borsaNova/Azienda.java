package borsaNova;

import java.util.*;

/**
 * Classe che rappresenta un'azienda che può quotare azioni in una borsa.
 * */
public class Azienda implements Comparable<Azienda> {

    /**
     * Il {@code nome} dell'azienda.
     * */
    private final String nome;
    /**
     * Mappa che associa a ogni {@code borsa} le {@code azioni} dell'azienda quotate in essa.
     * */
    private final Map<Borsa, Borsa.Azione> azioniQuotate;
    /**
     * Set che contiene le istanze di {@code Azienda}.
     * */
    private static final Set<Azienda> INSTANCE = new HashSet<>();

    /**
     * AF: {@code Azienda} rappresenta un'entità Azienda identificata dal {@code nome} univoco,
     *     con azioni quotate in borsa rappresentate dalla mappa {@code azioniQuotate}.
     *
     * RI:
     *  - {@code nome} non deve essere vuoto o null,
     *  - Le chiave della mappa {@code azioniQuotate} non deve essere null e deve essere una borsa esistente,
     *  - Il valore della mappa {@code azioniQuotate} non deve essere null e deve essere un'istanza di {@code Borsa.Azione}.
     * */

    /**
     * Crea una nuova istanza di {@code Azienda} con il {@code nome} specificato.
     *
     * @param nome il nome dell'azienda.
     */
    private Azienda(final String nome) {
        this.nome = nome;
        this.azioniQuotate = new HashMap<>();
    }


    /**
     * Restituisce un'istanza di {@code Azienda} con il nome specificato.
     *
     * @param nome il nome dell'azienda.
     * @throws NullPointerException se {@code nome} è null.
     * @throws IllegalArgumentException se {@code nome} è vuoto.
     *
     * @return l'istanza di {@code Azienda}.
     */
    public static Azienda getInstance(final String nome) {
        if (Objects.requireNonNull(nome).isBlank()) throw new IllegalArgumentException("ERRORE! Il nome non può essere vuoto.");
        for (Azienda azienda : INSTANCE) {
            if (azienda.nome.equals(nome)) {
                return azienda;
            }
        }
        Azienda azienda = new Azienda(nome);
        INSTANCE.add(azienda);
        return azienda;
    }

    /**
     * Verifica se esiste un'azienda con il {@code nome} specificato.
     *
     * @param nome il nome dell'azienda da cercare.
     * @throws IllegalArgumentException se il nome è vuoto.
     * @throws NullPointerException se il nome è null.
     * @return {@code true} se esiste un'{@code azienda} con il nome specificato, {@code false} altrimenti.
     */
    public static boolean esisteAzienda(final String nome) {
        if (Objects.requireNonNull(nome).isBlank()) throw new IllegalArgumentException("ERRORE! Il nome non può essere vuoto.");
        for (Azienda azienda : INSTANCE) {
            if (azienda.nome.equals(nome)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Restituisce il nome dell'azienda.
     *
     * @return il {@code nome}.
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Restituisce le azioni quotate dall'azienda in borsa.
     *
     * @return le {@code azioni} quotate.
     */
    public Map<Borsa, Borsa.Azione> getAzioniQuotate() {
        return Collections.unmodifiableMap(this.azioniQuotate);
    }

    /**
     * Quota l'azienda in borsa.
     *
     * @param borsa la borsa in cui quotare l'azienda.
     * @param numeroAzioni il numero di azioni da quotare.
     * @param prezzoUnitario il prezzo unitario delle azioni.
     * @throws NullPointerException se la {@code borsa} è nulla.
     * @throws IllegalArgumentException se la {@code borsa} non esiste,
     *                                  o se il {@code numero di azioni} o il {@code prezzo unitario} è minore o uguale a zero,
     *                                  o se l'{@code azienda} è già quotata in borsa.
     */
    public void quotaInBorsa(Borsa borsa, final int numeroAzioni, final int prezzoUnitario) {
        Objects.requireNonNull(borsa, "ERRORE! La borsa specificata non può essere nulla.");
        if (!Borsa.esisteBorsa(borsa.getNome())) throw new IllegalArgumentException("ERRORE! La borsa specificata non esiste.");
        if (numeroAzioni <= 0 || prezzoUnitario <= 0) throw new IllegalArgumentException("ERRORE! Numero azioni e prezzo unitario devono essere positivi.");
        if (azioniQuotate.containsKey(borsa)) throw new IllegalArgumentException("ERRORE! Azienda già quotata in borsa.");

        borsa.registraAzione(this, numeroAzioni, prezzoUnitario);
        azioniQuotate.put(borsa, Borsa.getAzione(borsa, this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Azienda azienda = (Azienda) o;
        return nome.equals(azienda.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }

    @Override
    public int compareTo(Azienda otherAzienda) {
        return this.getNome().compareTo(otherAzienda.getNome());
    }

    @Override
    public String toString() {
        return this.getNome() + " - " + this.getAzioniQuotate();
    }
}
