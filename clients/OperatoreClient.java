/*

Copyright 2024 Massimo Santini

This file is part of "Programmazione 2 @ UniMI" teaching material.

This is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This material is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this file.  If not, see <https://www.gnu.org/licenses/>.

*/

package clients;

import borsaNova.Azienda;
import borsaNova.Borsa;
import borsaNova.Operatore;

import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/** Client di test per alcune funzionalità relative agli <strong>operatori</strong>. */
public class OperatoreClient {

  /** . */
  private OperatoreClient() {}

  /*-
   * Scriva un [@code main} che legge dal flusso in ingresso una sequenza di tre
   * gruppi di linee (separati tra loro dalla linea contenente solo --) ciascuno
   * della forma descritta di seguito:
   *
   *     nome_azienda nome_borsa numero prezzo_unitario
   *     ...
   *     --
   *     nome_operatore budget_iniziale
   *     ...
   *     --
   *     nome_operatore b nome_borsa nome_azienda prezzo_totale
   *     ... [oppure]
   *     nome_operatore s nome_borsa nome_azienda numero_azioni
   *     ... [oppure]
   *     nome_operatore d valore
   *     ... [oppure] nome_operatore w valore
   *
   * Assuma che i nomi non contengano spazi. In base al contenuto del primo
   * blocco, quota le azioni delle aziende nelle borse secondo il numero e
   * prezzo unitario specificati, in base al secondo blocco crea gli operatori
   * specificati con il budget iniziale specificato e in base al terzo blocco
   * esegue le operazioni, a seconda che il carattere che segue il nome
   * dell'operatore sia:
   *
   * - b compra azioni (quotate nella borsa e dell'azienda specificata,
   *   impegnano il prezzo totale specificato),
   * - s vende azioni (quotate nella borsa e dell'azienda specificata, nel
   *   numero specificato).
   * - d deposita denaro (secondo il valore specificato),
   * - w preleva denaro (secondo il valore specificato).
   *
   * Osservi che l'acquisto può determinare un resto, nel caso in cui il prezzo
   * totale non sia un multiplo esatto del prezzo dell'azione; tale resto rimane
   * a disposizione dell'operatore per eventuali operazioni successive.
   *
   * Al termine della lettura il programma emette nel flusso d'uscita l'elenco
   * degli operatori coinvolti (in ordine alfabetico) ciascuno dei quali seguito
   * (sulla stessa linea e separato da virgole) dal suo budget finale e dalla
   * somma del valore delle azioni che possiede, ogni operatore è poi seguito
   * dall'elenco delle azioni che possiede, ciascuna azione va descritta
   * emettendo il nome della borsa (in ordine alfabetico, preceduto da -)
   * seguito da quello dell'azienda e dal numero di azioni possedute (separati
   * da virgole).
   */
  public static void main(String[] args) {
    Map<String, Borsa> borse = new TreeMap<>();
    Map<String, Azienda> aziende = new TreeMap<>();
    Map<String, Operatore> operatori = new TreeMap<>();
    try (Scanner scanner = new Scanner(System.in)) {
      //Blocco 1: lettura azioni
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.equals("--")) break;
        String[] tokens = line.split(" ");
        if (tokens.length != 4) throw new IllegalArgumentException("ERRORE! Formato non valido.");
        String nomeAzienda = tokens[0];
        String nomeBorsa = tokens[1];
        int numeroAzioni;
        int prezzoUnitario;
        try {
          numeroAzioni = Integer.parseInt(tokens[2]);
          prezzoUnitario = Integer.parseInt(tokens[3]);
        } catch (NumberFormatException e) {
          throw new NumberFormatException("ERRORE! Numero azioni e prezzo unitario devono essere interi.");
        }
        if (!borse.containsKey(nomeBorsa)) borse.put(nomeBorsa, Borsa.getInstance(nomeBorsa));
        if (!aziende.containsKey(nomeAzienda)) aziende.put(nomeAzienda, Azienda.getInstance(nomeAzienda));
        Azienda azienda = aziende.get(nomeAzienda);
        Borsa borsa = borse.get(nomeBorsa);
        azienda.quotaInBorsa(borsa, numeroAzioni, prezzoUnitario);
      }
      //Blocco 2: lettura operatore e budget iniziale
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.equals("--")) break;
        String[] tokens = line.split(" ");
        if (tokens.length != 2) throw new IllegalArgumentException("ERRORE! Formato input non valido!");
        String nomeOperatore = tokens[0];
        int budgetIniziale;
        try {
          budgetIniziale = Integer.parseInt(tokens[1]);
        } catch (NumberFormatException e) {
          throw new NumberFormatException("ERRORE! Il budget iniziale deve essere un numero intero.");
        }
        Operatore operatore = Operatore.getInstance(nomeOperatore);
        operatore.deposita(budgetIniziale);
        operatori.put(nomeOperatore, operatore);
      }
      //Blocco 3: letture operazioni
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.isEmpty()) break;
        String[] tokens = line.split(" ");
        String nomeOperatore = tokens[0];
        String operazione = tokens[1];
        Operatore operatore = operatori.get(nomeOperatore);
        switch (operazione) {
          case "b":
            String nomeBorsa = tokens[2];
            String nomeAzienda = tokens[3];
            int quantita;
            try {
              quantita = Integer.parseInt(tokens[4]);
            } catch (NumberFormatException e) {
              throw new NumberFormatException("ERRORE! Quantità totale deve essere un numero intero.");
            }
            Borsa borsa = borse.get(nomeBorsa);
            Azienda azienda = aziende.get(nomeAzienda);
            Borsa.Azione azione = Borsa.getAzione(borsa, azienda);
            int numeroAzioniDaComprare = quantita / azione.getPrezzoUnitario();
            operatore.compraAzioni(borsa, azienda, numeroAzioniDaComprare);
            break;
          case "s":
            nomeBorsa = tokens[2];
            nomeAzienda = tokens[3];
            int numeroAzioni;
            try {
              numeroAzioni = Integer.parseInt(tokens[4]);
            } catch (NumberFormatException e) {
              throw new NumberFormatException("ERRORE! Numero azioni deve essere un numero intero.");
            }
            Borsa borsa2 = borse.get(nomeBorsa);
            Azienda azienda2 = aziende.get(nomeAzienda);
            operatore.vendiAzioni(borsa2, azienda2, numeroAzioni);
            break;
          case "d":
            int importo;
            try {
              importo = Integer.parseInt(tokens[2]);
            } catch (NumberFormatException e) {
              throw new NumberFormatException("ERRORE! Importo deve essere un numero intero.");
            }
            operatore.deposita(importo);
            break;
          case "w":
            try {
              importo = Integer.parseInt(tokens[2]);
            } catch (NumberFormatException e) {
              throw new NumberFormatException("ERRORE! Importo deve essere un numero intero.");
            }
            operatore.preleva(importo);
            break;
          default:
            throw new IllegalArgumentException("ERRORE! Operazione non valida.");
        }
      }
    }
    //Stampa risultati
    for (Operatore operatore : operatori.values()) {
      int valoreAzioni = operatore.getTotaleValoreAzioniPossedute();
      System.out.println(operatore.getNome() + ", " + operatore.getCapitaleTotale(true) + ", " + valoreAzioni);
      Map<Borsa.Azione, Integer> azioniPossedute = new TreeMap<>(Comparator.comparing(a -> a.getBorsa().getNome()));
      azioniPossedute.putAll(operatore.getAzioniPossedute());
      for (Map.Entry<Borsa.Azione, Integer> entry : azioniPossedute.entrySet()) {
        Borsa.Azione azione = entry.getKey();
        int quantita = entry.getValue();
        System.out.println("- " + azione.getBorsa().getNome() + ", " + azione.getAzienda().getNome() + ", " + quantita);
      }
    }
  }
}
