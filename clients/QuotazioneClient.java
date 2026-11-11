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

import java.util.*;

/** Client di test per alcune funzionalità relative alle <strong>quotazioni</strong>. */
public class QuotazioneClient {

  /** . */
  private QuotazioneClient() {}

  /*-
   * Scriva un {@code main} che legge dal flusso di ingresso una sequenza di
   * linee della forma
   *
   *    nome_azienda nome_borsa quantità prezzo
   *
   * Assuma che i nomi non contengano spazi. Dopo aver quotato le aziende nelle
   * borse emette nel flusso d'uscita
   *
   * - per ciascuna azienda, l'elenco delle borse in cui è quotata, poi
   * - per ciascuna borsa, l'elenco di aziende in essa quotate;
   *
   * I nomi delle borse e delle aziende devono essere uno per linea, in ordine
   * alfabetico; i nomi di borsa nel primo elenco e i azienda nel secondo devono
   * essere prefissati da "- ".
   */
  public static void main(String[] args) {
    Map<String, Azienda> aziende = new TreeMap<>();
    Map<String, Borsa> borse = new TreeMap<>();
    try (Scanner scanner = new Scanner(System.in)) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.isBlank()) break;
        String[] tokens = line.split(" ");
        if (tokens.length != 4) throw new IllegalArgumentException("ERRORE! Formato input non valido!");
        String nomeAzienda = tokens[0];
        String nomeBorsa = tokens[1];
        int quantita;
        int prezzo;
        try {
          quantita = Integer.parseInt(tokens[2]);
          prezzo = Integer.parseInt(tokens[3]);
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("ERRORE! Quantità e prezzo devono essere numeri interi!");
        }
        if (!borse.containsKey(nomeBorsa)) borse.put(nomeBorsa, Borsa.getInstance(nomeBorsa));
        if (!aziende.containsKey(nomeAzienda)) aziende.put(nomeAzienda, Azienda.getInstance(nomeAzienda));
        Azienda azienda = aziende.get(nomeAzienda);
        Borsa borsa = borse.get(nomeBorsa);
        azienda.quotaInBorsa(borsa, quantita, prezzo);
      }
    }
    //Output delle borse per ciascuna azienda
    for (Azienda azienda : aziende.values()) {
      System.out.println(azienda.getNome());
      for (Borsa borsa : borse.values()) {
        if (borsa.getQuotazioni().containsKey(aziende.get(azienda.getNome()))) {
          System.out.println("- " + borsa.getNome());
        }
      }
    }
    //Output delle aziende per ciascuna borsa
    for (Borsa borsa : borse.values()) {
      System.out.println(borsa.getNome());
      List<Azienda> aziendeInBorsa = new ArrayList<>(borse.get(borsa.getNome()).getQuotazioni().keySet());
      Collections.sort(aziendeInBorsa);
      for (Azienda azienda : aziendeInBorsa) {
        System.out.println("- " + azienda.getNome());
      }
    }
  }
}
