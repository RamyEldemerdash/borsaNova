# BorsaNova

Progetto universitario di **Programmazione II** (Università degli Studi di Milano): simulazione, in Java, di un sistema di borse valori con aziende quotate, operatori, compravendita di azioni e politiche configurabili per l'aggiornamento dei prezzi.

## Funzionalità

- Creazione e gestione di **aziende**, **borse** e **operatori**.
- Quotazione di azioni su una borsa, con quantità disponibile e prezzo unitario.
- Deposito e prelievo di capitale per gli operatori.
- Acquisto e vendita di azioni e consultazione delle partecipazioni possedute.
- Applicazione di diverse **politiche di prezzo** alle operazioni di scambio.
- Client da riga di comando che leggono dati da input standard per esercitare le funzionalità.

## Tecnologie e concetti

- **Java** (progetto compilato con OpenJDK 21).
- Programmazione orientata agli oggetti, interfacce e classi annidate.
- Collezioni Java, ordinamenti e iteratori.
- Controlli sugli argomenti e gestione delle eccezioni.

## Struttura del progetto

```text
borsaNova/
├── borsaNova/            # Classi principali e politiche di prezzo
│   ├── Azienda.java
│   ├── Borsa.java
│   ├── Operatore.java
│   ├── PricePolicy.java
│   └── Policy*.java
├── clients/              # Programmi di prova con metodo main
│   ├── BorsaClient.java
│   ├── OperatoreClient.java
│   ├── QuotazioneClient.java
│   └── ...
├── README.md
└── .gitignore
```

La directory `borsaNova/` contiene il package Java `borsaNova`; la directory `clients/` contiene il package `clients`. I client sono programmi separati, ciascuno con il proprio metodo `main`.

## Compilazione ed esecuzione

È necessario avere un **JDK** installato e i comandi `javac` e `java` disponibili nel terminale.

Dalla cartella principale del progetto (quella che contiene `borsaNova/` e `clients/`):

```bash
javac -d out borsaNova/*.java clients/*.java
```

Per avviare, ad esempio, il client dedicato alle borse:

```bash
java -cp out clients.BorsaClient
```

Altri client disponibili includono `clients.AzioneClient`, `clients.OperatoreClient`, `clients.QuotazioneClient`, `clients.PoliticaPrezzoClient`, `clients.PoliticaPrezzoSogliaClient` e `clients.PoliticaPrezzoVocaliClient`.

I client leggono i dati dallo **standard input**; il formato richiesto varia da client a client ed è descritto nei commenti dei rispettivi file sorgente. È anche possibile passare un file di input tramite redirezione:

```bash
java -cp out clients.BorsaClient < input.txt
```

`input.txt` è un file da preparare seguendo il formato previsto dal client; **non è incluso** nel progetto.

## Politiche di prezzo

L'interfaccia `PricePolicy` definisce il contratto per il calcolo del nuovo prezzo di un'azione. Sono presenti le implementazioni:

- `PolicyIncrementoCostante`
- `PolicyDecrementoCostante`
- `PolicyVariazioneCostante`
- `PolicySoglia`
- `PolicyVocaliOrStessaLettera`

Le politiche consentono di sperimentare differenti regole di aggiornamento del prezzo in risposta alle operazioni.

## Contesto universitario e attribuzione

Questo repository raccoglie un progetto sviluppato nell'ambito del corso di **Programmazione II** dell'Università degli Studi di Milano.

**Nota sul materiale didattico:** alcuni file della cartella `clients/` riportano intestazioni di copyright e condizioni di licenza **GNU GPL v3 o successive**, attribuite a Massimo Santini. Tali avvisi sono mantenuti nei file originali. Prima di redistribuire il progetto, verificare gli obblighi di licenza applicabili, inclusa l'eventuale necessità di allegare il testo della GPL.

## Stato del progetto

Progetto didattico. La compilazione di tutti i sorgenti è stata verificata con **OpenJDK 21**; non è presente una suite automatizzata di test nel materiale fornito.
