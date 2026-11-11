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

import borsaNova.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/** Client di test per alcune funzionalità relative alle <strong>borse</strong>. */
public class PoliticaPrezzoClient {

  /** . */
  private PoliticaPrezzoClient() {}

  /*-
   * Scriva un [@code main} che riceve come parametri sulla linea di comando
   *
   *      nome_borsa valore nome_operatore budget_iniziale
   *
   * il secondo parametro è un intero che determina la politica di prezzo della
   * borsa come segue: se è positivo, la politica è ad incremento costante pari
   * a tale valore, viceversa se è negativo, la politica è a decremento costante
   * pari al valore assoluto di tale valore.
   *
   * Il programma quindi legge dal flusso in ingresso una sequenza di due gruppi
   * di linee (separati tra loro dalla linea contenente solo --) ciascuno della
   * forma descritta di seguito:
   *
   *     nome_azienda numero prezzo_unitario
   *     ...
   *     --
   *     b nome_azienda prezzo_totale
   *     ... [oppure]
   *     s nome_azienda numero_azioni
   *
   * in base al contenuto del primo blocco, quota le azioni delle aziende
   * specificate nella borsa (definita dal primo parametro sulla linea di
   * comando) secondo il numero e prezzo unitario specificati, in base al
   * contenuto del secondo blocco — una volta creato un operatore (di nome
   * e budget iniziale specificati dal terzo e quarto parametro sulla
   * linea di comando) — esegue le operazioni a seconda che il carattere che
   * segue il nome dell'operatore sia:
   *
   * - b compra azioni (dell'azienda specificata, impegnano il prezzo totale
   *   specificato),
   * - s vende azioni (dell'azienda specificata, nel numero specificato).
   *
   * Al termine della lettura il programma emette nel flusso d'uscita l'elenco
   * delle azioni (in ordine alfabetico) seguite dal prezzo (separato da una
   * virgola).
   */
  public static void main(String[] args) {
    if (args.length != 4) throw new IllegalArgumentException("ERRORE! Manca uno o più dei seguenti parametri: nome_borsa, valore, operatore, budget_iniziale.");
    String nomeBorsa = args[0];
    String nomeOperatore = args[2];
    int valore;
    int budgetIniziale;
    try {
      valore = Integer.parseInt(args[1]);
      budgetIniziale = Integer.parseInt(args[3]);
    } catch (NumberFormatException e) {
      throw new NumberFormatException("ERRORE! Il valore e il budget iniziale devono essere numeri interi.");
    }
    Borsa borsa = Borsa.getInstance(nomeBorsa);
    Operatore operatore = Operatore.getInstance(nomeOperatore);
    operatore.deposita(budgetIniziale);
    try (Scanner scanner = new Scanner(System.in)) {
      //Blocco 1: lettura nome azienda, numero azioni e prezzo unitario
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.equals("--")) break;
        String[] tokens = line.split(" ");
        if (tokens.length != 3) throw new IllegalArgumentException("ERRORE! Formato input non valido.");
        String nomeAzienda = tokens[0];
        int numeroAzioni;
        int prezzoUnitario;
        try {
          numeroAzioni = Integer.parseInt(tokens[1]);
          prezzoUnitario = Integer.parseInt(tokens[2]);
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("ERRORE! Il numero di azioni e il prezzo unitario devono essere interi.");
        }
        Azienda azienda = Azienda.getInstance(nomeAzienda);
        azienda.quotaInBorsa(borsa, numeroAzioni, prezzoUnitario);
      }
      //Blocco 2: lettura operazioni
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.isBlank()) break;
        String[] tokens = line.split(" ");
        if (tokens.length != 3) throw new IllegalArgumentException("ERRORE! Formato input non valido.");
        String operazione = tokens[0];
        String nomeAzienda = tokens[1];
        Azienda azienda = Azienda.getInstance(nomeAzienda);
        Borsa.Azione azione = borsa.getQuotazione(azienda);
        PricePolicy policy = valore > 0 ? new PolicyIncrementoCostante(valore) : new PolicyDecrementoCostante(Math.abs(valore));
        switch (operazione) {
          case "b":
            int prezzoTotale;
            try {
              prezzoTotale = Integer.parseInt(tokens[2]);
            } catch (NumberFormatException e) {
              throw new NumberFormatException("ERRORE! Il prezzo totale deve essere un numero intero.");
            }
            int numeroAzioniDaComprare = prezzoTotale / azione.getPrezzoUnitario();
            borsa.setPricePolicy(policy);
            operatore.compraAzioni(borsa, azienda, numeroAzioniDaComprare);
            break;
          case "s":
            int numeroAzioni;
            try {
              numeroAzioni = Integer.parseInt(tokens[2]);
            } catch (NumberFormatException e) {
              throw new NumberFormatException("ERRORE! La quantità deve essere un numero intero.");
            }
            borsa.setPricePolicy(policy);
            operatore.vendiAzioni(borsa, azienda, numeroAzioni);
            break;
          default:
            throw new IllegalArgumentException("ERRORE! Operazione non valida.");
        }
      }
    }
    //Stampa risultati
    List<Borsa.Azione> azioni = new ArrayList<>(borsa.getAzioni());
    azioni.sort(Comparator.comparing(azione -> azione.getAzienda().getNome()));
    for (Borsa.Azione azione : azioni) {
      System.out.println(azione.getAzienda().getNome() + ", " + azione.getPrezzoUnitario());
    }
  }
}
