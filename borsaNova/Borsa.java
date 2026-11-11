package borsaNova;

import java.util.*;

/**
 * Classe che rappresenta una borsa in cui vengono quotate azioni di aziende.
 * */
public class Borsa implements Comparable<Borsa>, Iterable<Borsa.Azione> {
    /**
     * Il {@code nome} della borsa.
     * */
    private final String nome;
    /**
     * Mappa che associa a ogni {@code azienda} le {@code azioni} quotate in borsa.
     * */
    private final Map<Azienda, Azione> quotazioni;
    /**
     * Lista delle {@code allocazioni} degli operatori.
     * */
    private final List<Allocazione> allocazioni;
    /**
     * Set che contiene le istanze di {@code Borsa}.
     * */
    private static final Set<Borsa> INSTANCE = new HashSet<>();
    /**
     * La {@code policy} di aggiornamento del prezzo delle azioni.
     * */
    private PricePolicy policy;
    /**
     * AF: Borsa rappresenta un mercato azionario identificata da un {@code nome} univoco,
     *     con le azioni quotate in borsa {@code quotazioni} che collega le aziende alle loro azioni,
     *     e le allocazioni degli operatori {@code allocazioni} che tiene traccia delle azioni possedute
     *     da ciascun operatore.
     *
     * RI:
     *  - {@code nome} non deve essere vuoto o null.
     *  - Ogni chiave di {@code quotazioni} deve essere un'{@link Azienda} non null ed esistente,
     *    e i valori devono essere istanze di {@link Azione} non null.
     *  - Ogni istanza di {@link Allocazione} deve essere non null.
     * */

    /**
     * Crea una nuova istanza di {@code Borsa} con il nome specificato.
     *
     * @param nome il nome della borsa.
     * */
    private Borsa(String nome) {
        this.nome = nome;
        this.quotazioni = new HashMap<>();
        this.allocazioni = new ArrayList<>();
    }
    /**
     * Restituisce un'istanza di {@code Borsa} con il nome specificato.
     *
     * @param nome il nome della borsa.
     * @throws NullPointerException se {@code nome} è null.
     * @throws IllegalArgumentException se {@code nome} è vuoto.
     * @return l'istanza di {@code Borsa}.
     * */
    public static Borsa getInstance(String nome) {
        if (Objects.requireNonNull(nome).isBlank()) throw new IllegalArgumentException("ERRORE! Il nome non può essere vuoto.");
        for (Borsa borsa : INSTANCE) {
            if (borsa.nome.equals(nome)) {
                return borsa;
            }
        }
        Borsa borsa = new Borsa(nome);
        INSTANCE.add(borsa);
        return borsa;
    }
    /**
     * Verifica se esiste una borsa con il nome specificato.
     *
     * @param nome il nome della borsa da cercare.
     * @throws NullPointerException se {@code nome} è null.
     * @throws IllegalArgumentException se il nome è vuoto.
     * @return {@code true} se esiste una {@code borsa} con il nome specificato, {@code false} altrimenti.
     * */
    public static boolean esisteBorsa(String nome) {
        if (Objects.requireNonNull(nome).isBlank()) throw new IllegalArgumentException("ERRORE! Il nome non può essere vuoto.");
        for (Borsa borsa : INSTANCE) {
            if (borsa.nome.equals(nome)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Restituisce il nome della borsa.
     *
     * @return il nome.
     * */
    public String getNome() {
        return this.nome;
    }
    /**
     * Verifica se esiste un'azione quotata in borsa per l'azienda specificata.
     *
     * @param azienda l'azienda di cui verificare l'esistenza dell'azione quotata.
     * @return {@code true} se esiste un'azione quotata in borsa per l'azienda specificata, {@code false} altrimenti.
     * */
    public boolean esisteAzione(Azienda azienda) {
        return this.quotazioni.containsKey(azienda);
    }
    /**
     * Registra una nuova azione in borsa per l'azienda specificata.
     *
     * @param azienda l'azienda di cui quotare le azioni.
     * @param numeroAzioni il numero di azioni da quotare.
     * @param prezzoUnitario il prezzo unitario delle azioni.
     * @throws NullPointerException se {@code azienda} è null.
     * @throws IllegalArgumentException se l'azienda specificata non esiste,
     *                                  o se {@code numeroAzioni} o {@code prezzoUnitario} sono minori o uguali a 0,
     *                                  o se l'azione è già quotata in borsa.
     * */
    public void registraAzione(Azienda azienda, final int numeroAzioni, final int prezzoUnitario) {
        Objects.requireNonNull(azienda);
        if (!Azienda.esisteAzienda(azienda.getNome())) throw new IllegalArgumentException("ERRORE! L'azienda specificata non esiste.");
        if (numeroAzioni <= 0 || prezzoUnitario <= 0) throw new IllegalArgumentException("ERRORE! Numero azioni e prezzo unitario devono essere positivi.");

        Azione azione = new Azione(azienda, numeroAzioni, prezzoUnitario);
        if (this.quotazioni.containsKey(azienda)) throw new IllegalArgumentException("ERRORE! Azione già quotata in borsa.");

        this.quotazioni.put(azienda, azione);
    }
    /**
     * Imposta la policy di aggiornamento del prezzo delle azioni.
     *
     * @param policy la policy da impostare.
     * @throws NullPointerException se la policy è null.
     * */
    public void setPricePolicy(PricePolicy policy) {
        Objects.requireNonNull(policy, "ERRORE! La policy non può essere nulla.");
        this.policy = policy;
    }
    /**
     * Restituisce l'istanza di {@code Azione} associata all'azienda specificata.
     *
     * @param borsa la borsa in cui cercare l'azione.
     * @param azienda l'azienda di cui cercare l'azione.
     * @throws NullPointerException se la borsa è null oppure l'azienda è null.
     * @throws IllegalArgumentException se l'azienda specificata non esiste,
     *                                  se la borsa specificata non esiste,
     *                                  se l'azienda specificata non è quotata in borsa.
     * @return l'{@code Azione}.
     * */
    public static Borsa.Azione getAzione(Borsa borsa, Azienda azienda) {
        if (!Azienda.esisteAzienda(azienda.getNome())) throw new IllegalArgumentException("ERRORE! L'azienda specificata non esiste.");
        if (!Borsa.esisteBorsa(borsa.getNome())) throw new IllegalArgumentException("ERRORE! La borsa specificata non esiste.");
        Objects.requireNonNull(borsa, "ERRORE! La borsa non può essere nulla.");
        Objects.requireNonNull(azienda, "ERRORE! L'azienda non può essere nulla.");
        for (Borsa.Azione azione : borsa) {
            if (azione.getAzienda().equals(azienda)) {
                return azione;
            }
        }
        throw new IllegalArgumentException("ERRORE! L'azienda specificata non è quotata in borsa.");
    }
    /**
     * Aggiunge o aggiorna un'allocazione per l'operatore specificato.
     *
     * @param operatore l'operatore coinvolto nell'allocazione.
     * @param azione l'azione coinvolta nell'allocazione.
     * @param quantita la quantità di azioni da allocare.
     * @throws IllegalArgumentException se l'operatore non esiste,
     *                                 se l'azione non esiste,
     *                                 se la quantità è minore o uguale a 0.
     * @throws NullPointerException se l'operatore è null o l'azione è null.
     * */
    private void aggiungiOAggiornaAllocazione(Operatore operatore, Azione azione, int quantita) {
        checkAggiuntaORimuoviAllocazioni(operatore, azione, quantita);
        for (Allocazione alloc : this.allocazioni) {
            if (alloc.operatore.equals(operatore) && alloc.azione.equals(azione)) {
                alloc.aggiungiQuantita(quantita);
                return;
            }
        }
        this.allocazioni.add(new Allocazione(operatore, azione, quantita));
    }
    /**
     * Rimuove un'allocazione per l'operatore specificato.
     *
     * @param operatore l'operatore coinvolto nell'allocazione.
     * @param azione l'azione coinvolta nell'allocazione.
     * @param quantita la quantità di azioni da rimuovere.
     * @throws IllegalArgumentException se l'operatore non esiste,
     *                                 se l'azione non esiste,
     *                                 se la quantità è minore o uguale a 0.
     * @throws NullPointerException se l'operatore è null o l'azione è null.
     * */
    private void rimuoviAllocazioni(Operatore operatore, Azione azione, int quantita) {
        checkAggiuntaORimuoviAllocazioni(operatore, azione, quantita);
        for (Allocazione alloc : this.allocazioni) {
            if (alloc.operatore.equals(operatore) && alloc.azione.equals(azione)) {
                alloc.rimuoviQuantita(quantita);
                if (alloc.quantita == 0) {
                    this.allocazioni.remove(alloc);
                }
                return;
            }
        }
        throw new IllegalArgumentException("ERRORE! L'operatore non ha azioni di questa azienda.");
    }
    /**
     * Controllra se l'operatore e l'azione sono validi per l'aggiunta o rimozione di allocazioni e se la quantità è positiva.
     *
     * @param operatore l'operatore coinvolto nell'allocazione.
     * @param azione l'azione coinvolta nell'allocazione.
     * @param quantita la quantità di azioni da allocare o rimuovere.
     * @throws IllegalArgumentException se l'operatore non esiste,
     *                                 se l'azione non esiste,
     *                                 se la quantità è minore o uguale a 0.
     * @throws NullPointerException se l'operatore è null o l'azione è null.
     * */
    private void checkAggiuntaORimuoviAllocazioni(Operatore operatore, Azione azione, int quantita) {
        if (!Operatore.esisteOperatore(operatore.getNome())) throw new IllegalArgumentException("ERRORE! L'operatore specificato non esiste.");
        if (!esisteAzione(azione.getAzienda())) throw new IllegalArgumentException("ERRORE! L'azione specificata non esiste.");
        Objects.requireNonNull(operatore);
        Objects.requireNonNull(azione);
        if (quantita <= 0) throw new IllegalArgumentException("ERRORE! La quantità deve essere positiva.");
    }
    /**
     * Finalizza l'acquisto di un'azione da parte dell'operatore.
     *
     * @param operatore l'operatore che acquista l'azione.
     * @param azienda l'azienda di cui acquistare l'azione.
     * @param quantita quanti azioni vuole comprare.
     * @throws NullPointerException se l'operatore è null o l'azienda è null.
     * @throws IllegalArgumentException se la quantità è minore o uguale a zero,
     *                                  se non ci sono abbastanza azioni disponibili,
     *                                  se l'azienda non è quotata in borsa,
     *                                  se l'operatore o l'azienda non esiste.
     * */
    public void finalizzaAcquistoAzione(Operatore operatore, Azienda azienda, final int quantita) {
        checkFinalizzazioneAcquistoVendita(operatore, azienda, quantita);
        int numeroAzioniDisponibili = this.quotazioni.get(azienda).getNumeroAzioni();
        if (numeroAzioniDisponibili < quantita) throw new IllegalArgumentException("ERRORE! Non ci sono abbastanza azioni disponibili.");
        if (!esisteAzione(azienda)) throw new IllegalArgumentException("ERRORE! L'azienda non è quotata in borsa.");

        Azione azione = this.quotazioni.get(azienda);
        aggiungiOAggiornaAllocazione(operatore, azione, quantita);
        operatore.aggiungiAzioni(azione, quantita);
        this.quotazioni.get(azienda).updatePrezzoUnitario(this.policy, quantita, true);
        this.quotazioni.get(azienda).decrementaNumeroAzioni(quantita);
    }
    /**
     * Controlla se l'operatore e l'azienda sono validi per la finalizzazione di un acquisto
     * o vendita e se la quantità è positiva.
     *
     * @param operatore l'operatore coinvolto nell'operazione.
     * @param azienda l'azienda coinvolta nell'operazione.
     * @param quantita la quantità di azioni da acquistare o vendere.
     * @throws NullPointerException se l'operatore o l'azienda sono null.
     * @throws IllegalArgumentException se la quantità è minore o uguale a zero,
     *                                  se l'azienda o l'operatore non esiste.
     * */
    private static void checkFinalizzazioneAcquistoVendita(Operatore operatore, Azienda azienda, int quantita) {
        Objects.requireNonNull(operatore);
        Objects.requireNonNull(azienda);
        if (quantita <= 0) throw new IllegalArgumentException("ERRORE! La quantità deve essere positiva.");
        if (!Azienda.esisteAzienda(azienda.getNome()) || !Operatore.esisteOperatore(operatore.getNome())) {
            throw new IllegalArgumentException("ERRORE! L'azienda o l'operatore specificato non esiste.");
        }
    }
    /**
     * Restituisce la quantità di azioni possedute dall'operatore per l'azienda specificata.
     *
     * @param operatore l'operatore coinvolto nell'allocazione.
     * @param azienda l'azienda coinvolta nell'allocazione.
     * @return la quantità di azioni possedute.
     * @throws NullPointerException se l'operatore o l'azienda sono null.
     * @throws IllegalArgumentException se l'azienda o l'operatore non esiste.
     * */
    private int getQuantitaAzioniPossedute(Operatore operatore, Azienda azienda) {
        Objects.requireNonNull(operatore);
        Objects.requireNonNull(azienda);
        if (!Azienda.esisteAzienda(azienda.getNome()) || !Operatore.esisteOperatore(operatore.getNome())) {
            throw new IllegalArgumentException("ERRORE! L'azienda o l'operatore specificato non esiste.");
        }
        for (Allocazione alloc : this.allocazioni) {
            if (alloc.operatore.equals(operatore) && alloc.azione.getAzienda().equals(azienda)) {
                return alloc.quantita;
            }
        }
        return 0;
    }
    /**
     * Finalizza la vendita di un'azione da parte dell'operatore.
     *
     * @param operatore l'operatore che vende l'azione.
     * @param azienda l'azienda di cui vendere l'azione.
     * @param quantita quanti azioni vuole vendere.
     * @throws NullPointerException se l'operatore è null o l'azienda è null.
     * @throws IllegalArgumentException se la quantità è minore o uguale a zero,
     *                                  se l'operatore non possiede abbastanza azioni per la vendita,
     *                                  se l'azienda non è quotata in borsa,
     *                                  se l'operatore o l'azienda non esiste.
     * */
    public void finalizzaVenditaAzione(Operatore operatore, Azienda azienda, final int quantita) {
        checkFinalizzazioneAcquistoVendita(operatore, azienda, quantita);
        int numeroAzioniPossedute = getQuantitaAzioniPossedute(operatore, azienda);
        if (numeroAzioniPossedute < quantita) throw new IllegalArgumentException("ERRORE! Non possiedi abbastanza azioni da vendere.");
        if (!esisteAzione(azienda)) throw new IllegalArgumentException("ERRORE! L'azienda non è quotata in borsa.");

        Azione azione = this.quotazioni.get(azienda);
        rimuoviAllocazioni(operatore, azione, quantita);
        operatore.rimuoviAzioni(azione, quantita);
        this.quotazioni.get(azienda).updatePrezzoUnitario(this.policy, quantita, false);
        this.quotazioni.get(azienda).incrementaNumeroAzioni(quantita);
    }
    /**
     * Restituisce il prezzo di un'azione quotata in borsa.
     *
     * @param azienda l'azienda di cui si vuole conoscere il prezzo dell'azione.
     * @throws IllegalArgumentException se l'azienda specificata non esiste,
     *                                  se l'azienda non è quotata in borsa.
     * @throws NullPointerException se l'azienda è null.
     * @return il prezzo dell'azione.
     * */
    public int getPrezzoAzione(Azienda azienda) {
        Objects.requireNonNull(azienda);
        if (!Azienda.esisteAzienda(azienda.getNome())) throw new IllegalArgumentException("ERRORE! L'azienda specificata non esiste.");
        if (!this.quotazioni.containsKey(azienda)) throw new IllegalArgumentException("ERRORE! L'azienda non è quotata in borsa.");
        return this.quotazioni.get(azienda).getPrezzoUnitario();
    }
    /**
     * Restituisce la quotazione di un'azienda.
     *
     * @param azienda l'azienda di cui si vogliono conoscere le quotazioni.
     * @throws IllegalArgumentException se l'azienda non è quotata in borsa,
     *                                  se l'azienda non esiste.
     * @throws NullPointerException se l'azienda è null.
     * @return l'azione dell'azienda.
     * */
    public Azione getQuotazione(Azienda azienda) {
        Objects.requireNonNull(azienda);
        if (!Azienda.esisteAzienda(azienda.getNome())) throw new IllegalArgumentException("ERRORE! L'azienda specificata non esiste.");
        if (!this.quotazioni.containsKey(azienda)) {
            throw new IllegalArgumentException("ERRORE! Azienda non quotata in borsa.");
        }
        return this.quotazioni.get(azienda);
    }
    /**
     * Restituisce le quotazioni delle azioni in borsa.
     *
     * @return le quotazioni delle azioni.
     * */
    public Map<Azienda, Azione> getQuotazioni() {
        return Collections.unmodifiableMap(this.quotazioni);
    }
    /**
     * Restituisce le allocazioni degli operatori.
     *
     * @return le allocazioni degli operatori.
     * */
    public Map<Operatore, List<Azione>> getAllocazioni() {
        Map<Operatore, List<Azione>> mapAllocazioni = new HashMap<>();
        for (Allocazione alloc : this.allocazioni) {
            if (!mapAllocazioni.containsKey(alloc.operatore)) {
                mapAllocazioni.put(alloc.operatore, new ArrayList<>());
            }
            mapAllocazioni.get(alloc.operatore).add(alloc.azione);
        }
        return Collections.unmodifiableMap(mapAllocazioni);
    }
    /**
     * Restituisce tutte le azioni quotate in borsa.
     *
     * @return le azioni quotate.
     * */
    public List<Azione> getAzioni() {
        return List.copyOf(this.quotazioni.values());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getNome()).append("\n");
        List<Azione> azioni = new ArrayList<>(this.quotazioni.values());
        azioni.sort(Comparator.comparing(a -> a.getAzienda().getNome()));
        for (Azione azione : azioni) {
            sb.append("- ").append(azione.getAzienda().getNome()).append(" ").append(azione.getNumeroAzioni()).append("\n");
            List<Allocazione> allocazioni = new ArrayList<>();
            for (Allocazione alloc : this.allocazioni) {
                if (alloc.azione.equals(azione)) {
                    allocazioni.add(alloc);
                }
            }
            allocazioni.sort(Comparator.comparing(a -> a.operatore.getNome()));
            for (Allocazione alloc : allocazioni) {
                sb.append("= ").append(alloc.operatore.getNome()).append(" ").append(alloc.quantita).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Borsa otherBorsa) {
        return this.getNome().compareTo(otherBorsa.getNome());
    }

    @Override
    public Iterator<Borsa.Azione> iterator() {
        return this.quotazioni.values().iterator();
    }

    /**
     * Classe che rappresenta un'azione quotata in borsa.
     * */
    public class Azione {
        /**
         * L'azienda a cui l'azione è associata.
         */
        private final Azienda azienda;
        /**
         * La borsa in cui l'azione è quotata.
         */
        private final Borsa borsa;
        /**
         * Il numero di azioni disponibili.
         */
        private int numeroAzioni;
        /**
         * Il prezzo unitario dell'azione.
         */
        private int prezzoUnitario;

        /**
         * AF: Azione rappresenta un titolo azionario emesso da un'azienda e quotato in una borsa.
         *     È caratterizzata da un prezzo unitario, e un numero di azioni emesse.
         *
         * RI:
         *  - {@code azienda} non deve essere null e deve esistere;
         *  - {@code prezzoUnitario} e {@code numeroAzioni} devono essere maggiori di zero.
         * */

        /**
         * Creare un'istanza di {@code Azione} associata all'azienda specificata e quotata in borsa.
         *
         * @param azienda l'azienda a cui l'azione è associata.
         * @param numeroAzioni quanti azioni emette l'azienda
         * @param prezzoUnitario il prezzo dell'azione
         * @throws IllegalArgumentException se il prezzo unitario o il numero di azioni è minore o uguale a zero,
         *                                  l'azienda non esiste.
         * @throws NullPointerException se {@code azienda} è null.
         * */
        private Azione(Azienda azienda, int numeroAzioni, int prezzoUnitario) {
            Objects.requireNonNull(azienda);
            if (!Azienda.esisteAzienda(azienda.getNome())) throw new IllegalArgumentException("ERRORE! Azienda specificata non esiste.");
            if (numeroAzioni <= 0 || prezzoUnitario <= 0) throw new IllegalArgumentException("ERRORE! Numero azioni e prezzo unitario devono essere positivi.");

            this.azienda = azienda;
            this.numeroAzioni = numeroAzioni;
            this.prezzoUnitario = prezzoUnitario;
            this.borsa = Borsa.this;
        }
        /**
         * Restituisce l'azienda a cui l'azione è associata.
         *
         * @return l'azienda.
         * */
        public Azienda getAzienda() {
            return this.azienda;
        }
        /**
         * Restituisce la borsa a cui l'azione è associata.
         *
         * @return la borsa.
         * */
        public Borsa getBorsa() {
            return this.borsa;
        }
        /**
         * Restituisce il numero di azioni disponibili.
         *
         * @return il numero di azioni.
         * */
        public int getNumeroAzioni() {
            return this.numeroAzioni;
        }
        /**
         * Restituisce il prezzo dell'azione.
         *
         * @return il prezzo.
         * */
        public int getPrezzoUnitario() {
            return this.prezzoUnitario;
        }
        /**
         * Aggiorna il prezzo unitario dell'azione.
         *
         * @param policy la politica di aggiornamento del prezzo.
         * @param numeroAzioniScambiate il numero di azioni scambiate.
         * @param isBuy {@code true} se l'operazione è di acquisto, {@code false} se è di vendita.
         * @throws IllegalArgumentException se il numero di azioni scambiate è minore o uguale a zero.
         * */
        private void updatePrezzoUnitario(PricePolicy policy, final int numeroAzioniScambiate, boolean isBuy) {
            if (policy != null) {
                if (numeroAzioniScambiate<= 0) throw new IllegalArgumentException("ERRORE! Il numero di azioni scambiate deve essere positivo.");
                this.prezzoUnitario = policy.calcolaNuovoPrezzo(this, numeroAzioniScambiate, isBuy);
            }
        }
        /**
         * Incrementa il numero di azioni disponibili.
         *
         * @param numeroAzioni il numero di azioni da incrementare
         * @throws IllegalArgumentException se il numero di azioni da incrementare è minore o uguale a zero.
         * */
        private void incrementaNumeroAzioni(int numeroAzioni) {
            if (numeroAzioni <= 0) throw new IllegalArgumentException("ERRORE! Il numero di azioni da incrementare deve essere positivo.");
            this.numeroAzioni += numeroAzioni;
        }
        /**
         * Decrementa il numero di azioni disponibili.
         *
         * @param numeroAzioni il numero di azioni da decrementare.
         * @throws IllegalArgumentException se il numero di azioni da decrementare è maggiore del numero di azioni emesse.
         * */
        private void decrementaNumeroAzioni(int numeroAzioni) {
            if (numeroAzioni <= 0) throw new IllegalArgumentException("ERRORE! Il numero di azioni da decrementare deve essere positivo.");
            this.numeroAzioni -= numeroAzioni;
        }

        @Override
        public String toString() {
            return getAzienda().getNome() + ", " + getPrezzoUnitario() + ", " + getNumeroAzioni();
        }
    }

    /**
     * Classe che rappresenta un'allocazione di azioni per un operatore.
     * */
    private class Allocazione {
        /**
         * L'operatore coinvolto nell'allocazione.
         * */
        private final Operatore operatore;
        /**
         * L'azione nell'allocazione.
         * */
        private final Azione azione;
        /**
         * La quantità di azioni allocate.
         * */
        private int quantita;
        /**
         * AF: Allocazione rappresenta un'allocazione di azioni per un operatore.
         *
         * RI:
         *  - {@code operatore} e {@code azione} non devono essere null ed devono esistere.
         *  - {@code quantita} deve essere maggiore di zero.
         * */
        /**
         * Crea un'istanza di {@code Allocazione} per l'operatore specificato.
         *
         * @param operatore l'operatore coinvolto nell'allocazione.
         * @param azione l'azione coinvolta nell'allocazione.
         * @param quantita quanti azioni allocare.
         * @throws NullPointerException se operatore o azione sono null.
         * @throws IllegalArgumentException se la quantità è minore o uguale a zero,
         *                                  l'operatore o l'azione non esiste.
         * */
        private Allocazione(Operatore operatore, Azione azione, final int quantita) {
            Objects.requireNonNull(operatore);
            Objects.requireNonNull(azione);
            if (quantita <= 0) throw new IllegalArgumentException("ERRORE! La quantità deve essere positiva.");
            if (!Operatore.esisteOperatore(operatore.getNome())) throw new IllegalArgumentException("ERRORE! L'operatore specificato non esiste.");
            if (!esisteAzione(azione.getAzienda())) throw new IllegalArgumentException("ERRORE! L'azione specificata non esiste.");
            this.operatore = operatore;
            this.azione = azione;
            this.quantita = quantita;
        }
        /**
         * Aggiugne la quantità specificata di azioni all'allocazione.
         *
         * @param quantita la quantità di azioni da aggiungere.
         * @throws IllegalArgumentException se la quantità è minore di zero.
         * */
        private void aggiungiQuantita(int quantita) {
            if (quantita < 0) throw new IllegalArgumentException("ERRORE! Quantità da aggiungere non può essere negativa");
            this.quantita += quantita;
        }
        /**
         * Rimuove la quantità specificata di azioni all'allocazione.
         *
         * @param quantita la quantità di azioni da rimuovere.
         * @throws IllegalArgumentException se la quantità è minore di zero,
         *                                  o se la quantità è maggiore della quantità allocata.
         * */
        private void rimuoviQuantita(int quantita) {
            if (quantita < 0 || quantita > this.quantita) throw new IllegalArgumentException("ERRORE! Quantità da rimuovere non valida");
            this.quantita -= quantita;
        }
    }
}
