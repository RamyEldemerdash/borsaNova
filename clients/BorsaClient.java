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

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/** Client di test per alcune funzionalità relative alle <strong>borse</strong>. */
public class BorsaClient {

  /** . */
  private BorsaClient() {}

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
   *
   * Assuma che i nomi non contengano spazi. Iqn base al contenuto del primo
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
   *
   * Osservi che l'acquisto può determinare un resto, nel caso in cui il prezzo
   * totale non sia un multiplo esatto del prezzo dell'azione; tale resto rimane
   * a disposizione dell'operatore per eventuali operazioni successive.
   *
   * Al termine della lettura il programma emette nel flusso d'uscita (una per
   * linea) l'elenco delle borse coinvolte (in ordine alfabetico), per ogni
   * borsa emette l'elenco delle azioni in essa quotate (in ordine alfabetico,
   * prefissate da -), e per ognuna di esse i nomi degli operatori e delle
   * quantità che ne possiedono (in ordine alfabetico, prefissati da =).
   */
  public static void main(String[] args) {
    Map<String, Borsa> borse = new TreeMap<>();
    Map<String, Azienda> azinede = new TreeMap<>();
    Map<String, Operatore> operatori = new TreeMap<>();
    try (Scanner scanner = new Scanner(System.in)) {
      //Blocco 1: lettura azienda, borsa, numero azioni e prezzo unitario
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.equals("--")) break;
        String[] tokens = line.split(" ");
        if (tokens.length != 4) throw new IllegalArgumentException("ERRORE! formato non valido.");
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
        if (!azinede.containsKey(nomeAzienda)) azinede.put(nomeAzienda, Azienda.getInstance(nomeAzienda));
        if (!borse.containsKey(nomeBorsa)) borse.put(nomeBorsa, Borsa.getInstance(nomeBorsa));
        Azienda azienda = azinede.get(nomeAzienda);
        Borsa borsa = borse.get(nomeBorsa);
        azienda.quotaInBorsa(borsa, numeroAzioni, prezzoUnitario);
      }
      //Blocco 2: lettura operatore e budget iniziale
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.equals("--")) break;
        String[] tokens = line.split(" ");
        if (tokens.length != 2) throw new IllegalArgumentException("ERRORE! Formato input non valido.");
        String nomeOperatore = tokens[0];
        int budgetIniziale;
        try {
          budgetIniziale = Integer.parseInt(tokens[1]);
        } catch (NumberFormatException e) {
          throw new NumberFormatException("ERRORE! Il budget iniziale deve essere un intero.");
        }
        Operatore operatore = Operatore.getInstance(nomeOperatore);
        operatori.put(nomeOperatore, operatore);
        operatore.deposita(budgetIniziale);
      }
      //Blocco 3: lettura operazioni
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.isBlank()) break;
        String[] tokens = line.split(" ");
        if (tokens.length != 5) throw new IllegalArgumentException("ERRORE! Formato input non valido.");
        String nomeOperatore = tokens[0];
        String operazione = tokens[1];
        String nomeBorsa = tokens[2];
        String nomeAzienda = tokens[3];
        int valore;
        try {
          valore = Integer.parseInt(tokens[4]);
        } catch (NumberFormatException e) {
          throw new NumberFormatException("ERRORE! Il valore deve essere un intero.");
        }
        if (!borse.containsKey(nomeBorsa) || !azinede.containsKey(nomeAzienda) || !operatori.containsKey(nomeOperatore)) {
          throw new IllegalArgumentException("ERRORE! Borsa, azienda o operatore non trovati.");
        }
        Borsa borsa = borse.get(nomeBorsa);
        Azienda azienda = azinede.get(nomeAzienda);
        Operatore operatore = operatori.get(nomeOperatore);
        Borsa.Azione azione = Borsa.getAzione(borsa, azienda);
        if (operazione.equals("b")) {
          int quantita = valore / azione.getPrezzoUnitario();
          operatore.compraAzioni(borsa, azienda, quantita);
        } else if (operazione.equals("s")) {
          operatore.vendiAzioni(borsa, azienda, valore);
        } else throw new IllegalArgumentException("ERRORE! Operazione non valida.");
      }
    }
    for (Borsa borsa : borse.values()) {
      System.out.println(borsa.toString());
    }
  }
}
