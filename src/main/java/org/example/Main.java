package org.example;


import com.azure.ai.textanalytics.models.TextDocumentInput;
import org.example.operations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {

    // client con credenziali Azure
    private static String endpoint = "https://readeremailai.cognitiveservices.azure.com/";
    private static String apiKey = "1feaa1ba360f41ccab761490d6983d1c";
    private static String documentA = "Buongiorno Sono arrivato da Dubai con il volo Ek91 Emirates da DXB-MXP avevo un rollup spedito da Dubai che non ho trovato sul rullo, ho guardato " +
            "anche nei fuori misura ma senza trovarlo. Dopo un po sono andato via. Volevo capire se nel frattempo è stato recapitato a voi. Grazie";

    private static String documentB = "04/02/2023 AZ120 All¿arrivo (7:00) l¿illuminazione del cartello con le indicazioni per il car rental era spenta, rendendo difficile la lettura. " +
            "Una volta arrivato al parcheggi per il rental return mi sono trovato nella spiacevole situazione di non avere dei carrelli per i bagagli a portata di mano (avevo tre bagagli ed ero da solo). Ho impiegato 20 minuti per andare fino al terminal arrivi e prendere un carrello a pagamento (2¿, solo in contanti e senza possibilità di pagare con carta di credito). A piano 3 del parcheggio rental cars la porta antincendio era bloccata (!!!) e il carrello con i bagagli non ci passava (ho dovuto usare altre porte tagliafuoco più piccole e scomode). L¿ascensore per accedere al terminal partenze era troppo stretto e ho dovuto scaricare i bagagli per poter entrare con il carrello. Una esperienza deludente, ne terrò in considerazione la prossima volta che dovrò partire per lavoro da Milano.";

    private static String documentC = "Buongiorno, in data 01.02.2023 ho effettuato una prenotazione di parcheggio, avente nr. 3ES4PX8SP0M. Oggi 06.02.2023 per esigenze lavorative ho " +
            "dovuto modificare tale prenotazione. In fase di aggiornamento della prenotazione in questione, dopo aver effettuato l'accesso mi si aggiornava automaticamente la pagina, " +
            "reindirizzandomi ad una pagina per per il pagamento online. Premesso che avevo già pagato il cambio prenotazione (24 euro circa) mediante il credito voucher acquistato in " +
            "precedenza. Data l'anomalia della richiesta di ulteriore pagamento, chiudevo la pagina internet, e dopo essere tornato sulla mia prenotazione ,la stessa risultava bloccata. " +
            "C'è scritto che la prenotazione è modificata, ma non vi sono le date aggiornate, nè tantomeno mi permette di visualizzare o accedervi. Grazie per l'attenzione, saluti Cirignotta Aleandro ";
    private static List<TextDocumentInput> batchDocuments = new ArrayList<>(
            Arrays.asList(new TextDocumentInput("0", documentA), new TextDocumentInput("1", documentB), new TextDocumentInput("2", documentC)));

    public static void main(String[] args) {

        switch (args != null ? args[0] : "-1") {
            case "AnalyzeSentimentWithOpinionMiningASync": {
                AnalyzeSentimentWithOpinionMiningASync.analize(batchDocuments, apiKey, endpoint);
                break;
            }
            case "AnalyzeSentimentWithOpinionMining": {
                AnalyzeSentimentWithOpinionMining.analizeDocumentList(batchDocuments, apiKey, endpoint);
                break;
            }
            case "AnalyzeActions": {
                AnalyzeActions.analize(batchDocuments, apiKey, endpoint);
                break;
            }
            case "RecognizeEntities": // Mirco
            {
                RecognizeEntities.analize(batchDocuments, apiKey, endpoint);
                break;
            }
            case "ExtractKeyPhrases": // Mirco
            {
                for (TextDocumentInput document : batchDocuments) {
                    System.out.println("Extracting key phrases for document id: " + document.getId());
                    ExtractKeyPhrases.analyze(document.getText(), apiKey, endpoint);
                }
                break;
            }
            case "ExtractSummary": {
                for (TextDocumentInput document : batchDocuments) {
                    System.out.println("Extracting key phrases for document id: " + document.getId());
                    ExtractSummary.analyze(document.getText(), apiKey, endpoint);
                }
                break;
            }

            case "test": {

                RecognizeEntities.analizesingleToJson(batchDocuments.get(0), apiKey, endpoint);
                break;

            }
            default: {

                System.out.println("Set at least one run parameter!");
                break;
            }
        }
    }
}