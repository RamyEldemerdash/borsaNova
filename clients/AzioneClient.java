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

import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/** Client di test per alcune funzionalità relative alle <strong>azioni</strong>. */
public class AzioneClient {

  /** . */
  private AzioneClient() {}

  /*-
   * Scriva un [@code main} che, ricevuto un nome di borsa come parametro sulla
   * linea di comando, legge dal flusso in ingresso una sequenza di linee della
   * forma
   *
   *     nome_azienda numero prezzo_unitario
   *
   * e dopo aver quotato l'azienda nella borsa specificata come parametro
   * produce le azioni quotate emettendo per ciascuna di esse, nel flusso
   * d'uscita, il nome dell'azienda (in ordine alfabetico) seguito da prezzo e
   * numero (separati da virgole). Assuma che il nome dell'azienda non contenga
   * spazi.
   */
  public static void main(String[] args) {
    if (args.length != 1) throw new IllegalArgumentException("ERRORE! Dev'essere specificato il nome della borsa.");
    String nomeBorsa = args[0];
    Borsa borsa = Borsa.getInstance(nomeBorsa);
    Map<Azienda, Borsa.Azione> azioniMap = new TreeMap<>(Comparator.comparing(Azienda::getNome));
    try (Scanner scanner = new Scanner(System.in)) {
      while (scanner.hasNextLine()) {
        String[] tokens = scanner.nextLine().split(" ");
        if (tokens.length != 3) throw new IllegalArgumentException("ERRORE! Formato input non valido.");
        String nomeAzienda = tokens[0];
        int numeroAzioni;
        int prezzoUnitario;
        try {
          numeroAzioni = Integer.parseInt(tokens[1]);
          prezzoUnitario = Integer.parseInt(tokens[2]);
        } catch (NumberFormatException e) {
          throw new NumberFormatException("ERRORE! Numero azioni e prezzo unitario devono essere interi.");
        }
        Azienda azienda = Azienda.getInstance(nomeAzienda);
        azienda.quotaInBorsa(borsa, numeroAzioni, prezzoUnitario);
        azioniMap.put(azienda, Borsa.getAzione(borsa, azienda));
      }
      for (Map.Entry<Azienda, Borsa.Azione> entry : azioniMap.entrySet()) {
        Borsa.Azione azione = entry.getValue();
        System.out.println(azione.toString());
      }
    }
  }
}
